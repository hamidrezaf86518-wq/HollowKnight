package com.hollowknight.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.hollowknight.HollowKnight;
import com.hollowknight.model.Achievement;
import com.hollowknight.view.LabelStyle;

import static com.hollowknight.model.Constants.WORLD_HEIGHT;
import static com.hollowknight.model.Constants.WORLD_WIDTH;


public class AchievementsScreen implements Screen {

    private final HollowKnight game;
    private Stage stage;
    private Skin skin;
    private StretchViewport viewport;
    private OrthographicCamera camera;
    private Texture background;

    private static final Color LOCKED_COLOR = new Color(0.35f, 0.35f, 0.35f, 0.6f);
    private static final Color UNLOCKED_COLOR = Color.WHITE;

    public AchievementsScreen(HollowKnight game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        stage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal("ui/shade/skin/uiskin.json"));
        background = game.assetSystem.get("textures/Voidheart_menu_BG.png", Texture.class);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        game.audioSystem.playEnvironmentBGM("MainMenu");
        Label.LabelStyle titleStyle = LabelStyle.getStyle(30);
        Label.LabelStyle achievementTitleStyle = LabelStyle.getStyle(20);
        Label.LabelStyle achievementDescStyle = LabelStyle.getStyle(14);
        Label.LabelStyle statusStyle = LabelStyle.getStyle(14);

        Table root = new Table(skin);
        root.top().center();
        root.setFillParent(true);

        Label title = new Label("ACHIEVEMENTS", titleStyle);

        Table list = new Table(skin);
        list.top();
        list.defaults().pad(12).left();

        for (Achievement achievement : Achievement.values()) {
            boolean unlocked = game.achievementManager.isUnlocked(achievement);
            Color color = unlocked ? UNLOCKED_COLOR : LOCKED_COLOR;

            Table row = new Table(skin);
            row.defaults().left().pad(4);

            Label statusBadge = new Label(unlocked ? "[ UNLOCKED ]" : "[ LOCKED ]", statusStyle);
            statusBadge.setColor(unlocked ? new Color(0.4f, 1f, 0.4f, 1f) : new Color(0.6f, 0.6f, 0.6f, 0.8f));

            Label nameLabel = new Label(achievement.getTitle(), achievementTitleStyle);
            nameLabel.setColor(color);

            
            
            
            
            String descriptionText = unlocked ? achievement.getDescription() : "???";
            Label descLabel = new Label(descriptionText, achievementDescStyle);
            descLabel.setColor(color);

            Table textColumn = new Table(skin);
            textColumn.add(nameLabel).left().row();
            textColumn.add(descLabel).left().row();

            row.add(statusBadge).width(140);
            row.add(textColumn).left().padLeft(20);

            list.add(row).growX().row();
        }

        ScrollPane scrollPane = new ScrollPane(list, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        TextButton backBtn = new TextButton("BACK", skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        root.add(title).padTop(30).padBottom(20).row();
        root.add(scrollPane).width(WORLD_WIDTH * 0.7f).height(WORLD_HEIGHT * 0.55f).padBottom(20).row();
        root.add(backBtn).width(200).height(50);

        stage.addActor(root);
        stage.setKeyboardFocus(stage.getRoot());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.audioSystem.update(delta);

        camera.update();
        viewport.apply();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
