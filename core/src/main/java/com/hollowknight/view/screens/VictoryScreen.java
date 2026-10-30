package com.hollowknight.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.hollowknight.HollowKnight;
import com.hollowknight.i18n.Text;
import com.hollowknight.view.ui.Theme;

import static com.hollowknight.model.Constants.WORLD_HEIGHT;
import static com.hollowknight.model.Constants.WORLD_WIDTH;

public class VictoryScreen implements Disposable {
    private static final float FADE_SPEED = 1.6f;

    private final Stage stage;
    private final HollowKnight game;

    private Table rootTable;
    private boolean visible = false;
    private float fadeAlpha = 0f;

    public VictoryScreen(SpriteBatch batch, HollowKnight game) {
        this.game = game;
        this.stage = new Stage(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT), batch);
    }

    private void buildUI(float playTimeSeconds, int deathCount, int enemiesKilled) {
        stage.getRoot().clearChildren();

        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setBackground(Theme.dim(0.82f));

        Label title = Theme.label(Text.VICTORY_TITLE.get(), "title");
        title.setColor(Theme.GOLD);
        title.setAlignment(Align.center);

        Table stats = new Table();
        stats.defaults().width(240f).pad(0f, 14f, 0f, 14f);
        stats.add(createStat(Theme.formatTime(playTimeSeconds), Text.STAT_TIME.get()));
        stats.add(createStat(String.valueOf(deathCount), Text.STAT_DEATHS.get()));
        stats.add(createStat(String.valueOf(enemiesKilled), Text.STAT_ENEMIES.get()));

        TextButton restartBtn = Theme.button(Text.RESTART.get());
        restartBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        TextButton menuBtn = Theme.button(Text.MAIN_MENU.get());
        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        rootTable.add(title).padBottom(12f).row();
        rootTable.add(Theme.divider(520f)).width(520f).height(13f).padBottom(40f).row();
        rootTable.add(stats).padBottom(52f).row();

        Table buttons = new Table();
        buttons.defaults().width(260f).height(50f).pad(0f, 10f, 0f, 10f);
        buttons.add(restartBtn);
        buttons.add(menuBtn);
        rootTable.add(buttons);

        rootTable.getColor().a = 0f;
        stage.addActor(rootTable);
    }

    private Table createStat(String value, String caption) {
        Table box = new Table();
        box.setBackground(Theme.card());
        box.pad(22f, 16f, 20f, 16f);
        Label number = Theme.label(value, "stat");
        number.setAlignment(Align.center);
        Label name = Theme.label(caption, "muted");
        name.setAlignment(Align.center);
        box.add(number).row();
        box.add(name).padTop(8f);
        return box;
    }

    public void show(float playTimeSeconds, int deathCount, int enemiesKilled) {
        visible = true;
        fadeAlpha = 0f;

        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        if (w <= 0) w = 1280;
        if (h <= 0) h = 720;

        stage.getViewport().update(w, h, true);
        buildUI(playTimeSeconds, deathCount, enemiesKilled);
        stage.act(0f);

        Gdx.input.setInputProcessor(stage);
        playVictoryMusicIfAvailable();
    }

    private void playVictoryMusicIfAvailable() {
        try {
            game.audioSystem.playEnvironmentBGM("Victory");
        } catch (Exception e) {
            Gdx.app.error("VictoryScreen", "victory music unavailable", e);
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
        stage.getViewport().apply();
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
    }
}
