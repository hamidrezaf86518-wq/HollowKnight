package com.hollowknight.view.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hollowknight.HollowKnight;

import static com.hollowknight.model.Constants.WORLD_HEIGHT;
import static com.hollowknight.model.Constants.WORLD_WIDTH;


public class MainMenuScreen implements Screen {
    private final HollowKnight game;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin;
    private Texture background;
    private Texture logo;
    private Texture rightLogo;
    private Texture leftLogo;

    public MainMenuScreen(HollowKnight game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT,camera);
        background = game.assetSystem.get("textures/Voidheart_menu_BG.png", Texture.class);
        logo = game.assetSystem.get("textures/vheart_title_RU.png",Texture.class);
        rightLogo = game.assetSystem.get("textures/team_cherry_logo_main_menu.png", Texture.class);
        leftLogo = game.assetSystem.get("textures/Hidden_Dreams_Logo.png",Texture.class);
        stage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal("ui/shade/skin/uiskin.json"));
    }

    @Override
    public void show() {
        Table table = new Table();
        table.bottom().padBottom(50);
        table.defaults().pad(5);

        TextButton gameStartBtn = new TextButton("GAME START",skin);
        TextButton settingsBtn = new TextButton("SETTINGS",skin);
        TextButton guideBtn = new TextButton("GUIDE",skin);
        TextButton achievementsBtn = new TextButton("ACHIEVEMENTS",skin);
        TextButton quitBtn = new TextButton("QUIT",skin);

        table.add(gameStartBtn).width(300).height(40).row();
        table.add(guideBtn).width(300).height(40).row();
        table.add(settingsBtn).width(300).height(40).row();
        table.add(achievementsBtn).width(300).height(40).row();
        table.add(quitBtn).width(300).height(40).row();

        Stack stack = new Stack();
        stack.setFillParent(true);
        stack.add(table);
        game.audioSystem.playEnvironmentBGM("MainMenu");

        stage.addActor(stack);
        Gdx.input.setInputProcessor(stage);

        gameStartBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameStartScreen(game));
            }
        });

        guideBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GuideMenuScreen(game));
            }
        });

        settingsBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsMenuScreen(game,MainMenuScreen.this));
            }

        });

        achievementsBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new AchievementsScreen(game));
            }
        });

        quitBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.audioSystem.update(delta);

        float x = (WORLD_WIDTH-logo.getWidth()*0.8f)/2f;
        float y = (WORLD_HEIGHT-logo.getHeight()*0.8f)/2f;

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(background,0,0,WORLD_WIDTH,WORLD_HEIGHT);
        game.batch.draw(logo,x,y+100,logo.getWidth()*0.8f,logo.getHeight()*0.8f);
        game.batch.draw(rightLogo,WORLD_WIDTH-300,WORLD_HEIGHT-650,rightLogo.getWidth()*0.6f,rightLogo.getHeight()*0.6f);
        game.batch.draw(leftLogo,200,WORLD_HEIGHT-610);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height,true);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
