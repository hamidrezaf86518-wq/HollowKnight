package com.hollowknight.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PointMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hollowknight.HollowKnight;
import com.hollowknight.controller.WorldController.B2WorldCreator;
import com.hollowknight.controller.CheatCodeManager;
import com.hollowknight.model.*;
import com.hollowknight.model.Enemies.CrystalCrawler;
import com.hollowknight.model.Enemies.CrystalGuardian;
import com.hollowknight.model.Enemies.HuskHorned;
import com.hollowknight.model.Enemies.Mosquito;
import com.hollowknight.model.Spells.SpellManager;
import com.hollowknight.view.Scenes.DialogueBox;
import com.hollowknight.view.Scenes.Hud;
import com.hollowknight.controller.WorldController.WorldContactListener;

import static com.hollowknight.model.Constants.*;


public class GameScreen implements Screen {
    private final HollowKnight game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Hud hud;
    private PauseMenu pauseMenu;
    private boolean isPaused = false;
    private Array<Body> bodiesToDestroy = new Array<>();

    private CrystalGuardian crystalguardian;
    private Mosquito mosquito;
    private CrystalCrawler crystalCrawler;
    private HuskHorned huskHorned;
    private FalseKnight boss;


    private boolean pendingLockArena = false;
    private boolean pendingUnlockArena = false;


    private BossArena bossArena;

    private static final String MAP_PATH = "maps/crystalpeaks.tmx";
    private int slot = -1;
    private GameData pendingLoad;
    private float playTimeSeconds = 0f;
    private int deathCount = 0;
    private int enemiesKilled = 0;
    private float spawnX;
    private float spawnY;

    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;

    private CheatCodeManager cheatCodeManager;

    public Knight knight;
    private Zote zote;
    private DialogueBox dialogueBox;
    private BitmapFont dialogueFont;
    private boolean inDialogue = false;

    private SpellManager spellManager;
    private CharmMenu charmMenu;
    private VictoryScreen victoryScreen;


    private static final String DEFAULT_AREA_BGM = "CrystalPeaks";
    private static final String SECOND_AREA_BGM = "ForgottenCrossroads";
    private Vector2 areaTriggerPos = null;
    private static final float AREA_TRIGGER_RADIUS = 1.5f;
    private boolean inSecondArea = false;
    private boolean victoryMusicPlayed = false;

    public GameScreen(HollowKnight game) {
        this(game, null);
    }

    public GameScreen(HollowKnight game, GameData loadedData) {
        this.game = game;
        this.pendingLoad = loadedData;
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH/PPM, WORLD_HEIGHT/PPM, camera);
        hud = new Hud(game.batch, game);

        maploader = new TmxMapLoader();
        map = maploader.load(loadedData != null && loadedData.mapPath != null ? loadedData.mapPath : MAP_PATH);
        renderer = new OrthogonalTiledMapRenderer(map, 1/PPM);
        camera.position.set(viewport.getScreenWidth()/2, viewport.getScreenHeight()/2, 0);

        world = new World(new Vector2(0, -7), true);

        pauseMenu = new PauseMenu(game.batch, game);

        new B2WorldCreator(world, map);

        loadSpawnPoints();


        bossArena = new BossArena(world, map);

        charmMenu = new CharmMenu(game, knight.getCharmInventory());
        victoryScreen = new VictoryScreen(game.batch, game);
        spellManager = new SpellManager(game, world, knight, this);
        dialogueFont = new BitmapFont();
        dialogueFont.getData().setScale(1.3f);
        dialogueBox = new DialogueBox(dialogueFont);

        dialogueBox.setOnLineChange(() -> game.audioSystem.playZoteGrunt());

        if(pendingLoad != null){
            applyGameData(pendingLoad);
            slot = pendingLoad.slot;
        }

        world.setContactListener(new WorldContactListener(hud, knight, this));
        cheatCodeManager = new CheatCodeManager(this);

        game.audioSystem.playEnvironmentBGM(DEFAULT_AREA_BGM);
    }

    public void addBodyToDestroy(Body body){
        if(body != null && !bodiesToDestroy.contains(body, true)){
            bodiesToDestroy.add(body);
        }
    }

    private void applyGameData(GameData data){
        if(knight != null){
            knight.body.setTransform(data.knightX, data.knightY, 0);
            knight.setFace(data.faceDirection != 0 ? data.faceDirection : -1);
            knight.setCurrentMasks(data.currentMasks > 0 ? data.currentMasks : 5);
        }
        playTimeSeconds = data.playTimeSeconds;
        deathCount = data.deathCount;
        enemiesKilled = data.enemiesKilled;
    }

    public GameData buildGameData(){
        GameData data = new GameData();
        data.mapPath = MAP_PATH;
        if(knight != null){
            data.knightX = knight.body.getPosition().x;
            data.knightY = knight.body.getPosition().y;
            data.faceDirection = knight.getFace();
            data.currentMasks = knight.getCurrentMasks();
        }
        data.playTimeSeconds = playTimeSeconds;
        data.deathCount = deathCount;
        data.enemiesKilled = enemiesKilled;
        return data;
    }

    public boolean saveGame(){
        if(slot < 0) return false;
        game.saveSystem.save(slot, buildGameData());
        return true;
    }

    public void onBossDefeated(){
        try {
            game.achievementManager.onFalseKnightDefeated();
            game.achievementManager.onGameCompleted(playTimeSeconds, deathCount);
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "onBossDefeated: achievement tracking failed", e);
        }


        requestUnlockBossArena();

        if(victoryScreen != null){
            isPaused = false;
            if(!victoryMusicPlayed){
                game.audioSystem.playEnvironmentBGM("Victory");
                victoryMusicPlayed = true;
            }
            victoryScreen.show(playTimeSeconds, deathCount, enemiesKilled);
        }
    }

    public void onKnightDeath(){
        deathCount++;
    }


    public void onKnightRespawned(){
        if(boss != null && !boss.isDead()){
            boss.resetAndDeactivate();
        }

        requestUnlockBossArena();
    }


    public void requestLockBossArena(){
        pendingLockArena = true;
    }


    public void requestUnlockBossArena(){
        pendingUnlockArena = true;
    }

    public void onEnemyKilled(com.hollowknight.model.Enemies.Enemy enemy){
        enemiesKilled++;
        game.achievementManager.onEnemyKilled(enemy);
    }

    private float getObjectX(MapObject object) {
        if (object instanceof PointMapObject) {
            return ((PointMapObject) object).getPoint().x;
        }
        return Float.parseFloat(object.getProperties().get("x", String.class));
    }

    private float getObjectY(MapObject object) {
        if (object instanceof PointMapObject) {
            return ((PointMapObject) object).getPoint().y;
        }
        return Float.parseFloat(object.getProperties().get("y", String.class));
    }

    private void loadSpawnPoints() {
        MapLayer spawnLayer = map.getLayers().get("spawns");
        if (spawnLayer == null) {

            return;
        }

        MapObjects objects = spawnLayer.getObjects();

        for (MapObject object : objects) {
            String type = object.getProperties().get("type", String.class);
            if (type == null) {

                continue;
            }

            float x = getObjectX(object) / Constants.PPM;
            float y = getObjectY(object) / Constants.PPM;

            switch (type) {
                case "player":
                    spawnX = x;
                    spawnY = y;
                    knight = new Knight(world, game, this, x, y);
                    break;
                case "crystalcrawler":
                    crystalCrawler = new CrystalCrawler(game,this,x,y);
                    break;
                case "crystalguardian":
                    crystalguardian = new CrystalGuardian(game, this, x, y);
                    break;
                case "mosquito":
                    mosquito = new Mosquito(game, this, x, y);
                    break;
                case "huskhorned":
                    huskHorned = new HuskHorned(game,this,x,y);
                    break;
                case "falseknight":
                    boss = new FalseKnight(game, this, x, y);
                    break;
                case "Zote":
                    zote = new Zote(game, world, this, x, y);
                    break;
                case "areatrigger":
                    areaTriggerPos = new Vector2(x, y);

                    break;
                default:
                    break;
            }
        }
    }

    private void updateAreaBGM() {
        if (areaTriggerPos == null || knight == null) return;
        if (victoryScreen != null && victoryScreen.isVisible()) return;

        float knightX = knight.body.getPosition().x;
        boolean shouldBeInSecondArea = knightX > areaTriggerPos.x;

        if (shouldBeInSecondArea && !inSecondArea) {
            inSecondArea = true;
            game.audioSystem.playEnvironmentBGM(SECOND_AREA_BGM);
        } else if (!shouldBeInSecondArea && inSecondArea) {
            inSecondArea = false;
            game.audioSystem.playEnvironmentBGM(DEFAULT_AREA_BGM);
        }
    }

    public void update(float delta){
        charmMenu.handleInput();
        if (charmMenu.isOpen()) return;
        if(cheatCodeManager != null) cheatCodeManager.update();

        if (victoryScreen.isVisible()) {
            victoryScreen.update(delta);
            return;
        }

        playTimeSeconds += delta;

        world.step(1/60f, 6, 2);
        if(pendingLockArena){
            pendingLockArena = false;
            if(bossArena != null) bossArena.lock();
        }
        if(pendingUnlockArena){
            pendingUnlockArena = false;
            if(bossArena != null) bossArena.unlock();
        }
        camera.update();
        renderer.setView(camera);

        for(Body b : bodiesToDestroy){
            world.destroyBody(b);
        }
        bodiesToDestroy.clear();

        if (!inDialogue) {
            knight.update(delta);
        }

        crystalguardian.update(delta);
        mosquito.update(delta);
        boss.update(delta);
        crystalCrawler.update(delta);
        huskHorned.update(delta);
        hud.update(delta);
        if (spellManager != null) spellManager.update(delta);

        if (zote != null) zote.update(delta);
        if (dialogueBox != null) dialogueBox.update(delta);

        updateAreaBGM();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (dialogueBox.isActive()) {
                dialogueBox.next();
            } else if (zote != null && zote.isNearKnight()) {
                dialogueBox.start(zote.getNextDialogueSet());
            }
        }

        inDialogue = dialogueBox.isActive();



        if (bossArena != null && bossArena.isLocked()) {
            camera.zoom = 1f;

            float halfViewW = (viewport.getWorldWidth()  * camera.zoom) / 2f;
            float halfViewH = (viewport.getWorldHeight() * camera.zoom) / 2f;


            float camX = knight.body.getPosition().x;
            float minX = bossArena.getLeft()  + halfViewW;
            float maxX = bossArena.getRight() - halfViewW;
            if (minX > maxX) {
                camX = bossArena.getCenterX();
            } else {
                camX = MathUtils.clamp(camX, minX, maxX);
            }


            float camY = knight.body.getPosition().y;
            float minY = bossArena.getBottom() + halfViewH;
            float maxY = bossArena.getTop()    - halfViewH;
            if (minY > maxY) {
                camY = bossArena.getCenterY();
            } else {
                camY = MathUtils.clamp(camY, minY, maxY);
            }

            camera.position.set(camX, camY, 0);
        } else {
            camera.zoom = 1f;
            camera.position.set(knight.body.getPosition(), 0);
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(isPaused ? pauseMenu.getStage() : null);
        game.audioSystem.playEnvironmentBGM(inSecondArea ? SECOND_AREA_BGM : DEFAULT_AREA_BGM);
    }

    @Override
    public void render(float delta) {
        try {
            if(!isPaused) update(delta);
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "update() crashed", e);
        }

        game.audioSystem.update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        try {
            knight.getView().draw(game.batch, knight);
            crystalguardian.drawView(game.batch);
            mosquito.drawView(game.batch);
            boss.draw(game.batch);
            crystalCrawler.drawView(game.batch);
            huskHorned.drawView(game.batch);
            if (zote != null) zote.draw(game.batch);
            if (spellManager != null) spellManager.draw(game.batch);
            if (zote != null && zote.isNearKnight() && !dialogueBox.isActive()) {
                zote.drawPrompt(game.batch, delta);
            }
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "world drawing crashed", e);
        } finally {
            game.batch.end();
        }

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.render();

        charmMenu.render(game.batch, hud.stage.getCamera().combined,
            WORLD_WIDTH, WORLD_HEIGHT);

        if (dialogueBox != null && dialogueBox.isActive()) {
            game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
            game.batch.begin();
            dialogueBox.render(game.batch, WORLD_WIDTH, WORLD_HEIGHT);
            game.batch.end();
        }

        if (victoryScreen != null) {
            victoryScreen.render();
        }
        if(isPaused){
            pauseMenu.getStage().act(delta);
            pauseMenu.getStage().draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hud.stage.getViewport().update(width, height, true);
        pauseMenu.resize(width, height);
        if (victoryScreen != null) victoryScreen.resize(width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();

        hud.dispose();
        if (dialogueBox != null) dialogueBox.dispose();
        if (dialogueFont != null) dialogueFont.dispose();
        if (charmMenu != null) charmMenu.dispose();
        if (victoryScreen != null) victoryScreen.dispose();
    }

    public World getWorld() { return world; }
    public float getSpawnX() { return spawnX; }
    public float getSpawnY() { return spawnY; }
    public boolean isPaused() { return isPaused; }

    public void setPaused(boolean paused) {
        isPaused = paused;
        Gdx.input.setInputProcessor(paused ? pauseMenu.getStage() : null);
    }

    public FalseKnight getBoss() { return boss; }
    public int getSlot() { return slot; }
    public void setSlot(int slot) { this.slot = slot; }
    public float getPlayTimeSeconds() { return playTimeSeconds; }
    public int getDeathCount() { return deathCount; }
    public int getEnemiesKilled() { return enemiesKilled; }
    public Hud getHud() { return hud; }
    public SpellManager getSpellManager() { return spellManager; }
    public BossArena getBossArena() { return bossArena; }
}
