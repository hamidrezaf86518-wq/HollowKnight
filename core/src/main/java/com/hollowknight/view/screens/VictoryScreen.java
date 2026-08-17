package com.hollowknight.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.hollowknight.HollowKnight;


public class VictoryScreen implements Disposable {

    private final Stage stage;
    private final Skin skin;
    private final HollowKnight game;

    private boolean visible = false;
    private float fadeAlpha = 0f;
    private static final float FADE_SPEED = 1.6f;

    private Label statsLabel;
    private Table rootTable;   

    public VictoryScreen(SpriteBatch batch, HollowKnight game) {
        this.game = game;
        
        stage = new Stage(new ScreenViewport());
        skin = buildSkin();
        buildUI();
    }

    private Skin buildSkin() {
        Skin skin = new Skin();

        BitmapFont titleFont = new BitmapFont();
        titleFont.getData().setScale(3.2f);
        BitmapFont normalFont = new BitmapFont();
        normalFont.getData().setScale(1.3f);

        
        Pixmap bgPm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        bgPm.setColor(0, 0, 0, 0.78f);
        bgPm.fill();
        skin.add("black-bg", new Texture(bgPm));
        bgPm.dispose();

        
        Pixmap btnUp = new Pixmap(260, 56, Pixmap.Format.RGBA8888);
        btnUp.setColor(0.08f, 0.08f, 0.08f, 0.95f);
        btnUp.fill();
        btnUp.setColor(Color.GOLDENROD);
        btnUp.drawRectangle(0, 0, 260, 56);
        btnUp.drawRectangle(1, 1, 258, 54);
        skin.add("btn-up", new Texture(btnUp));
        btnUp.dispose();

        
        Pixmap btnDown = new Pixmap(260, 56, Pixmap.Format.RGBA8888);
        btnDown.setColor(Color.GOLDENROD);
        btnDown.fill();
        skin.add("btn-down", new Texture(btnDown));
        btnDown.dispose();

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.GOLDENROD);
        skin.add("title", titleStyle);

        Label.LabelStyle normalStyle = new Label.LabelStyle(normalFont, Color.WHITE);
        skin.add("normal", normalStyle);

        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.up = skin.getDrawable("btn-up");
        btnStyle.down = skin.getDrawable("btn-down");
        btnStyle.font = normalFont;
        btnStyle.fontColor = Color.BLACK;
        btnStyle.overFontColor = Color.DARK_GRAY;
        skin.add("default", btnStyle);

        return skin;
    }

    private void buildUI() {
        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setBackground(skin.getDrawable("black-bg"));

        Label title = new Label("FALSE KNIGHT DEFEATED", skin, "title");
        title.setAlignment(Align.center);

        statsLabel = new Label("", skin, "normal");
        statsLabel.setAlignment(Align.center);

        TextButton restartBtn = new TextButton("Restart", skin);
        restartBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        TextButton menuBtn = new TextButton("Return to Main Menu", skin);
        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        rootTable.add(title).padBottom(40).row();
        rootTable.add(statsLabel).padBottom(50).row();
        rootTable.add(restartBtn).width(280).height(60).padBottom(20).row();
        rootTable.add(menuBtn).width(280).height(60);

        stage.addActor(rootTable);
        rootTable.getColor().a = 0f; 
    }

    
    public void show(float playTimeSeconds, int deathCount, int enemiesKilled) {
        visible = true;
        fadeAlpha = 0f;

        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        if (w <= 0) w = 1280;   
        if (h <= 0) h = 720;

        
        stage.getViewport().update(w, h, true);

        int minutes = (int) (playTimeSeconds / 60);
        int seconds = (int) (playTimeSeconds % 60);
        statsLabel.setText(String.format(
            "Play Time: %02d:%02d   |   Deaths: %d   |   Enemies Defeated: %d",
            minutes, seconds, deathCount, enemiesKilled));

        
        stage.getRoot().setSize(w, h);
        rootTable.setSize(w, h);
        rootTable.invalidateHierarchy();
        stage.act(0);

        Gdx.input.setInputProcessor(stage);
        playVictoryMusicIfAvailable();
    }

    private void playVictoryMusicIfAvailable() {
        try {
            game.audioSystem.playEnvironmentBGM("Victory");
        } catch (Exception e) {
            
        }
    }

    public void hide() {
        visible = false;
        Gdx.input.setInputProcessor(null);
    }

    public boolean isVisible() {
        return visible;
    }

    public void update(float delta) {
        if (!visible) return;

        if (fadeAlpha < 1f) {
            fadeAlpha = Math.min(1f, fadeAlpha + delta * FADE_SPEED);
            rootTable.getColor().a = fadeAlpha;
        }

        stage.act(delta);
    }

    public void render() {
        if (!visible) return;
        stage.draw();   
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void dispose() {
        stage.dispose();  
        skin.dispose();
    }
}
