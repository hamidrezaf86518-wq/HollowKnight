package com.hollowknight.view.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.hollowknight.HollowKnight;
import com.hollowknight.i18n.Text;
import com.hollowknight.model.KeyBinding;
import com.hollowknight.view.ui.Theme;

import static com.hollowknight.model.Constants.WORLD_HEIGHT;
import static com.hollowknight.model.Constants.WORLD_WIDTH;


public class PauseMenu implements Screen {
    private static final float RESUME_GUARD_SECONDS = 0.25f;

    private final HollowKnight game;
    private Stage stage;
    private float openTime = 0f;
    private boolean resumeRequested = false;

    public PauseMenu(SpriteBatch batch, HollowKnight game) {
        this.game = game;
        this.stage = new Stage(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT), batch);
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == KeyBinding.PAUSE && openTime > RESUME_GUARD_SECONDS && !Theme.hasDialog(stage)) {
                    resumeRequested = true;
                    return true;
                }
                return false;
            }
        });
        rebuild();
    }

    public void open() {
        openTime = 0f;
        resumeRequested = false;
        rebuild();
        stage.getRoot().clearActions();
        stage.getRoot().getColor().a = 0f;
        stage.getRoot().addAction(Actions.fadeIn(0.18f));
    }

    public void rebuild() {
        stage.getRoot().clearChildren();

        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setBackground(Theme.dim(0.62f));

        Table panel = new Table();
        panel.setBackground(Theme.panel());
        panel.pad(34f, 56f, 28f, 56f);

        panel.add(Theme.label(Text.PAUSE_TITLE.get(), "title")).padBottom(8f).row();
        panel.add(Theme.divider(340f)).width(340f).height(13f).padBottom(22f).row();

        boolean canSave = canSaveProgress();

        TextButton resumeBtn = Theme.button(Text.RESUME.get());
        TextButton cheatBtn = Theme.button(Text.CHEAT_CODES.get());
        TextButton settingsBtn = Theme.button(Text.SETTINGS.get());
        TextButton quitBtn = Theme.button(canSave ? Text.SAVE_AND_QUIT.get() : Text.QUIT_TO_MENU.get());

        panel.defaults().width(380f).height(48f).padBottom(12f);
        panel.add(resumeBtn).row();
        panel.add(cheatBtn).row();
        panel.add(settingsBtn).row();
        panel.add(quitBtn).row();

        Label hint = Theme.label(Text.PAUSE_HINT.fmt(Theme.keyName(KeyBinding.PAUSE)), "muted");
        hint.setAlignment(Align.center);
        panel.add(hint).height(24f).padTop(6f).padBottom(0f).row();

        overlay.add(panel);
        stage.addActor(overlay);

        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resumeRequested = true;
            }
        });

        cheatBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showCheatCodes();
            }
        });

        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsMenuScreen(game, game.getScreen()));
            }
        });

        quitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameScreen gameScreen = (GameScreen) game.getScreen();
                gameScreen.saveGame();
                gameScreen.setPaused(false);
                game.setScreen(new MainMenuScreen(game));
                gameScreen.dispose();
            }
        });
    }

    private boolean canSaveProgress() {
        Screen current = game.getScreen();
        return current instanceof GameScreen && ((GameScreen) current).getSlot() >= 0;
    }

    private void showCheatCodes() {
        String[][] codes = {
            {"LEFT CTRL + B", Text.CHEAT_BOSS.get()},
            {"LEFT CTRL + N", Text.CHEAT_NOCLIP.get()},
            {"LEFT CTRL + H", Text.CHEAT_HEAL.get()},
            {"LEFT CTRL + S", Text.CHEAT_SOUL.get()},
            {"LEFT CTRL + G", Text.CHEAT_GOD.get()}
        };

        Table list = new Table();
        for (String[] code : codes) {
            Table chip = new Table();
            chip.setBackground(Theme.chip());
            chip.add(Theme.label(code[0], "small")).pad(6f, 12f, 6f, 12f);
            list.add(chip).width(200f).left().padBottom(10f).padRight(22f);
            list.add(Theme.label(code[1], "small")).left().padBottom(10f).row();
        }

        Theme.dialog(stage, Text.CHEAT_CODES.get(), list, Text.CLOSE.get());
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
        openTime += delta;
        stage.act(delta);
        stage.getViewport().apply();
        stage.draw();

        if (resumeRequested) {
            resumeRequested = false;
            Screen current = game.getScreen();
            if (current instanceof GameScreen) {
                ((GameScreen) current).setPaused(false);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
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
        stage.dispose();
    }
}
