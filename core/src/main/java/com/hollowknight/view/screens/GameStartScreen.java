package com.hollowknight.view.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.hollowknight.HollowKnight;
import com.hollowknight.view.LabelStyle;
import com.hollowknight.view.SaveCard;

import static com.hollowknight.model.Constants.WORLD_HEIGHT;
import static com.hollowknight.model.Constants.WORLD_WIDTH;



public class GameStartScreen implements Screen {
    private final HollowKnight game;
    private Stage stage;
    private Skin skin;
    private StretchViewport viewport;
    private OrthographicCamera camera;
    private Texture background;
    private Texture saveBackground;
    private Texture top;
    private Texture bot;

    public GameStartScreen(HollowKnight game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);
        stage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal("ui/shade/skin/uiskin.json"));
        background = game.assetSystem.get("textures/Voidheart_menu_BG.png", Texture.class);
        saveBackground = game.assetSystem.get("textures/vheart_save_Switch.png", Texture.class);
        top = game.assetSystem.get("textures/Controller_Dialogue_0000_top.png", Texture.class);
        bot = game.assetSystem.get("textures/Controller_Dialogue_0001_bot.png", Texture.class);
        Gdx.input.setInputProcessor(stage);
    }



    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.audioSystem.update(delta);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(background,0,0,WORLD_WIDTH,WORLD_HEIGHT);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }
    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void show() {
        game.audioSystem.playEnvironmentBGM("MainMenu");
        Label.LabelStyle labelStyle= LabelStyle.getStyle(50);
        Stack stack = new Stack();
        stack.setFillParent(true);

        Table rootTable = new Table(skin);
        Table topTable = new Table(skin);
        Table botTable = new Table(skin);
        Table slot0 = new Table(skin);
        Table slot1 = new Table(skin);
        Table slot2 = new Table(skin);
        Table slot3 = new Table(skin);

        topTable.top().padTop(50);
        rootTable.top();
        rootTable.add(topTable).row();
        topTable.top();
        rootTable.add(botTable).size(600,450).padTop(50);
        botTable.top();
        botTable.defaults().pad(5);

        botTable.add(slot0).size(600,100).row();
        botTable.add(slot1).size(600,100).row();
        botTable.add(slot2).size(600,100).row();
        botTable.add(slot3).size(600,100).row();

        Image topImage = new Image(top);
        Image botImage = new Image(bot);
        topTable.add(topImage).row();
        Label title = new Label("SELECT PROFILE",labelStyle);
        topTable.add(title).row();
        topTable.add(botImage).padTop(10).row();

        

        TextButton backBtn = new TextButton("BACK",skin);
        Table backBtnWrapper = new Table(skin);
        backBtnWrapper.top().left().padTop(20).padLeft(50);
        backBtnWrapper.add(backBtn).size(100,50);

        slot0.add(new SaveCard(skin, game,0)).expand().fill();
        slot1.add(new SaveCard(skin, game,1)).expand().fill();
        slot2.add(new SaveCard(skin, game,2)).expand().fill();
        slot3.add(new SaveCard(skin, game,3)).expand().fill();


        stack.add(rootTable);
        stack.add(backBtnWrapper);
        stage.addActor(stack);

        backBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }

   
}
