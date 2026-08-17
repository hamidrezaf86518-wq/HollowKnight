package com.hollowknight.view.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.hollowknight.HollowKnight;
import com.hollowknight.view.LabelStyle;


public class PauseMenu implements Screen {
    private Stage stage;
    private Table root;
    private Skin skin;
    private HollowKnight game;

    public PauseMenu(SpriteBatch batch, HollowKnight game) {
        stage = new Stage(new ScreenViewport(),batch);
        this.game = game;
        skin = new Skin(Gdx.files.internal("ui/shade/skin/uiskin.json"));
        root = new Table(skin);
        root.setFillParent(true);
        root.setSize(200,400);
        stage.addActor(root);
        buildUI();
    }

    private void buildUI(){

        Table table = new Table(skin);
        table.setBackground("panel1");
        table.setSize(200,400);
        table.defaults().pad(10);
        TextButton resumeBtn = new TextButton("RESUME",skin);
        TextButton showCheatBtn = new TextButton("SHOW CHEAT CODES",skin);
        TextButton settingsBtn = new TextButton("SETTINGS",skin);
        TextButton saveBtn = new TextButton("SAVE AND QUIT",skin);

        Label.LabelStyle labelStyle= LabelStyle.getStyle(50);

        Label title = new Label("PAUSE MENU",labelStyle);

        table.center().top();
        table.defaults().pad(10).grow();
        table.add(title).padBottom(30).row();
        table.add(resumeBtn).row();
        table.add(showCheatBtn).row();
        table.add(settingsBtn).row();
        table.add(saveBtn);

        root.add(table);
        resumeBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((GameScreen)game.getScreen()).setPaused(false);
            }
        });

        showCheatBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showCheatCodes();
            }
        });

        saveBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameScreen gameScreen = (GameScreen) game.getScreen();


                gameScreen.saveGame();
                gameScreen.setPaused(false);
                game.setScreen(new MainMenuScreen(game));
                gameScreen.dispose();
            }
        });

        settingsBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {


                game.setScreen(new SettingsMenuScreen(game, game.getScreen()));
            }
        });




    }

    private void showCheatCodes(){
        Dialog dialog = new Dialog("CHEAT CODES", skin);
        dialog.pad(20);

        String[] codes = {
            "LEFT CTRL + B  -  Teleport to Boss Arena",
            "LEFT CTRL + N  -  Noclip / Spectator Mode",
            "LEFT CTRL + H  -  Emergency Heal",
            "LEFT CTRL + S  -  Refill Soul Vessel",
            "LEFT CTRL + G  -  Toggle God Mode"
        };

        for (String code : codes) {
            Label line = new Label(code, skin);
            dialog.getContentTable().add(line).left().padBottom(8).row();
        }

        dialog.button("CLOSE");
        dialog.show(stage);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height,true);
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
