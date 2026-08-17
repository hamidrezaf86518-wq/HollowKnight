package com.hollowknight.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.hollowknight.HollowKnight;
import com.hollowknight.view.LabelStyle;

import java.util.HashMap;
import java.util.Map;

import static com.hollowknight.model.Constants.WORLD_HEIGHT;
import static com.hollowknight.model.Constants.WORLD_WIDTH;
import static com.hollowknight.model.KeyBinding.*;

public class GuideMenuScreen implements Screen {
    private final HollowKnight game;
    private Stage stage;
    private Skin skin;
    private StretchViewport viewport;
    private OrthographicCamera camera;
    private Texture background;


    private final Map<Integer, TextButton> keyToButtonMap = new HashMap<>();

    private final Map<TextButton, TextButton.TextButtonStyle> originalStyles = new HashMap<>();

    public GuideMenuScreen(HollowKnight game) {
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
        Label.LabelStyle style = LabelStyle.getStyle(30);
        Table backBtnWrapper = new Table(skin);
        backBtnWrapper.top().left();
        Table root = new Table(skin);
        root.top().center();

        Table table = new Table(skin);
        table.top().left();
        table.defaults().pad(10);

        TextButton backBtn = new TextButton("BACK", skin);

        Label title = new Label("GUIDE MENU", style);
        style = LabelStyle.getStyle(20);
        Label title1 = new Label("BUTTONS EXPLANATIONS", style);
        Label title2 = new Label("ABILITIES EXPLANATIONS", style);
        Label title3 = new Label("HEALTH & SOUL", style);
        Label title4 = new Label("CHEAT CODES", style);
        style = LabelStyle.getStyle(15);
        Label attackExp = new Label("ATTACK : " + Input.Keys.toString(ATTACK), style);
        Label jumpExp = new Label("JUMP : " + Input.Keys.toString(JUMP), style);
        Label dashExp = new Label("DASH : " + Input.Keys.toString(DASH), style);
        Label moveRightExp = new Label("MOVE RIGHT : " + Input.Keys.toString(MOVE_RIGHT), style);
        Label moveLeftExp = new Label("MOVE LEFT : " + Input.Keys.toString(MOVE_LEFT), style);
        Label pauseExp = new Label("PAUSE : " + Input.Keys.toString(PAUSE), style);
        Label focusExp = new Label("FOCUS : " + Input.Keys.toString(FOCUS), style);

        Label AbilitiesExp = new Label("NAIL\n" +
            "Your primary weapon. Strike enemies to deal\n" +
            "damage and gather SOUL.\n\n" +
            "JUMP\n" +
            "Leap into the air to avoid attacks and reach\n" +
            "higher platforms.\n\n" +
            "SHADOW DASH\n" +
            "Dash quickly forward. While dashing, you can\n" +
            "pass through enemies and some obstacles\n" +
            "without taking damage.\n\n" +
            "FOCUS\n" +
            "Hold to channel SOUL and restore lost Masks.\n" +
            "You cannot move while focusing.\n\n" +
            "VENGEFUL SPIRIT\n" +
            "Release gathered SOUL to fire a spell that\n" +
            "damages enemies from a distance.", style);

        Label healthAndSoulExp = new Label("MASKS (Health)\n" +
            "Your health is shown as Masks at the top.\n" +
            "Each hit breaks one Mask.\n" +
            "If all Masks break, you die.\n\n" +
            "SOUL\n" +
            "The SOUL meter fills as you strike enemies.\n" +
            "Gather SOUL to heal or cast spells.\n\n" +
            "HEALING\n" +
            "Hold FOCUS to spend SOUL and restore a Mask.\n" +
            "You cannot move while healing.", style);

        Label cheatCodes = new Label("LEFT CTRL + B  --------------->   Teleport to Boss Arena\n" +
            "LEFT CTRL + N  --------------->   Noclip / Spectator Mode\n" +
            "LEFT CTRL + H  --------------->   Emergency Heal\n" +
            "LEFT CTRL + S  --------------->   Refill Soul Vessel\n" +
            "LEFT CTRL + G  --------------->   Toggle God Mode\n", style);

        TextButton attackBtn = new TextButton("ATTACK", skin);
        TextButton jumpBtn = new TextButton("JUMP", skin);
        TextButton dashBtn = new TextButton("DASH", skin);
        TextButton moveRightBtn = new TextButton("MOVE RIGHT", skin);
        TextButton moveLeftBtn = new TextButton("MOVE LEFT", skin);
        TextButton focusBtn = new TextButton("FOCUS", skin);
        TextButton pauseBtn = new TextButton("PAUSE", skin);


        registerKeyButton(ATTACK, attackBtn);
        registerKeyButton(JUMP, jumpBtn);
        registerKeyButton(DASH, dashBtn);
        registerKeyButton(MOVE_RIGHT, moveRightBtn);
        registerKeyButton(MOVE_LEFT, moveLeftBtn);
        registerKeyButton(FOCUS, focusBtn);
        registerKeyButton(PAUSE,pauseBtn);


        backBtnWrapper.add(backBtn).size(100, 50).padLeft(50).padTop(20);
        root.add(title).padBottom(20).row();
        table.add(title1).colspan(2).row();
        table.add(attackExp).growX().left();
        table.add(attackBtn).width(200).row();
        table.add(jumpExp).growX().left();
        table.add(jumpBtn).width(200).row();
        table.add(dashExp).growX().left();
        table.add(dashBtn).width(200).row();
        table.add(moveRightExp).growX().left();
        table.add(moveRightBtn).width(200).row();
        table.add(moveLeftExp).growX().left();
        table.add(moveLeftBtn).width(200).row();
        table.add(focusExp).growX().left();
        table.add(focusBtn).width(200).row();
        table.add(pauseExp).growX().left();
        table.add(pauseBtn).width(200).row();
        table.add(title2).colspan(2).row();
        table.add(AbilitiesExp).colspan(2).growX().left().row();
        table.add(title3).colspan(2).row();
        table.add(healthAndSoulExp).colspan(2).growX().left().row();
        table.add(title4).colspan(2).row();
        table.add(cheatCodes).colspan(2).growX().left().row();

        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFadeScrollBars(true);
        scrollPane.setScrollBarPositions(false, true);

        root.add(scrollPane)
            .width(800)
            .height(600)
            .center().row();

        Stack stack = new Stack();
        stack.setFillParent(true);
        stack.add(backBtnWrapper);
        stack.add(root);
        stage.addActor(stack);

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });


        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                TextButton btn = keyToButtonMap.get(keycode);
                if (btn != null) {
                    pressButtonVisually(btn);
                }
                return false;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                TextButton btn = keyToButtonMap.get(keycode);
                if (btn != null) {
                    releaseButtonVisually(btn);
                }
                return false;
            }
        });

        stage.setKeyboardFocus(stage.getRoot());
    }


    private void registerKeyButton(int keycode, TextButton button) {
        keyToButtonMap.put(keycode, button);
        originalStyles.put(button, button.getStyle());
    }


    private void pressButtonVisually(TextButton button) {
        TextButton.TextButtonStyle original = originalStyles.get(button);
        if (original == null) return;

        TextButton.TextButtonStyle pressedStyle = new TextButton.TextButtonStyle(original);
        pressedStyle.up = original.down != null ? original.down : original.up;
        button.setStyle(pressedStyle);
    }


    private void releaseButtonVisually(TextButton button) {
        TextButton.TextButtonStyle original = originalStyles.get(button);
        if (original != null) {
            button.setStyle(original);
        }
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
    @Override public void dispose() {}
}
