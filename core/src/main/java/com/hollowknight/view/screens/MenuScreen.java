package com.hollowknight.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.hollowknight.HollowKnight;
import com.hollowknight.i18n.Text;
import com.hollowknight.view.ui.Theme;

import static com.hollowknight.model.Constants.WORLD_HEIGHT;
import static com.hollowknight.model.Constants.WORLD_WIDTH;

public abstract class MenuScreen implements Screen {
    protected final HollowKnight game;
    protected final OrthographicCamera camera = new OrthographicCamera();
    protected final StretchViewport viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
    protected final Stage stage;
    protected final Texture background;

    protected MenuScreen(HollowKnight game) {
        this.game = game;
        this.stage = new Stage(viewport, game.batch);
        this.background = game.assetSystem.get("textures/Voidheart_menu_BG.png", Texture.class);
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE && handlesBack() && !Theme.hasDialog(stage)) {
                    onBack();
                    return true;
                }
                return false;
            }
        });
    }

    protected abstract void build();

    protected void onBack() {
    }

    protected boolean handlesBack() {
        return true;
    }

    protected float dimAlpha() {
        return 0.55f;
    }

    protected void drawBackdrop(Batch batch) {
        batch.setColor(1f, 1f, 1f, 1f);
        batch.draw(background, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.setColor(0f, 0f, 0f, dimAlpha());
        batch.draw(Theme.pixel(), 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    protected void refresh() {
        stage.getRoot().clearChildren();
        build();
    }

    protected void addBackButton() {
        TextButton backBtn = Theme.button(Text.BACK.get());
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onBack();
            }
        });
        Table wrapper = new Table();
        wrapper.setFillParent(true);
        wrapper.top().left().pad(24f);
        wrapper.add(backBtn).width(150f).height(42f);
        stage.addActor(wrapper);
    }

    @Override
    public void show() {
        game.audioSystem.playEnvironmentBGM("MainMenu");
        Gdx.input.setInputProcessor(stage);
        refresh();
        stage.getRoot().getColor().a = 0f;
        stage.getRoot().clearActions();
        stage.getRoot().addAction(Actions.fadeIn(0.3f));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.audioSystem.update(delta);

        camera.update();
        viewport.apply();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        drawBackdrop(game.batch);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
