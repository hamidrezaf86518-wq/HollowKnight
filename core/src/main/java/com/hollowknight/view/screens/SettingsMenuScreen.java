package com.hollowknight.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.hollowknight.HollowKnight;

import static com.hollowknight.model.Constants.WORLD_HEIGHT;
import static com.hollowknight.model.Constants.WORLD_WIDTH;

public class SettingsMenuScreen implements Screen {
    private final HollowKnight game;
    private final Screen returnScreen;
    private Stage stage;
    private StretchViewport viewport;
    private OrthographicCamera camera;
    private Texture background;
    private Skin skin;

    private static final float DEFAULT_MUSIC_VOLUME = 0.7f;
    private static final float DEFAULT_SFX_VOLUME = 1f;
    private static final float MUTE_MID_VOLUME = 0.5f;

    private boolean suppressMusicSliderEvent = false;
    private boolean suppressSfxSliderEvent = false;

    public SettingsMenuScreen(HollowKnight game, Screen returnScreen) {
        this.game = game;
        this.returnScreen = returnScreen;
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

        Table root = new Table();
        root.setFillParent(true);
        root.top();

        Label title = new Label("SETTINGS", skin);
        title.setFontScale(3f);
        root.add(title).padTop(40).row();

        Table content = new Table();
        content.defaults().pad(15).height(50);

        Label musicVolumeLabel = new Label("MUSIC VOLUME", skin);
        musicVolumeLabel.setFontScale(1.5f);
        Slider musicVolumeSlider = new Slider(0f, 1f, 0.05f, false, skin);


        if (game.audioSystem.isMusicMuted()) {
            musicVolumeSlider.setValue(0f);
        } else {
            musicVolumeSlider.setValue(game.audioSystem.getMusicVolume());
        }

        content.add(musicVolumeLabel).left();
        content.add(musicVolumeSlider).width(300).row();

        TextButton musicToggleBtn = new TextButton(
            game.audioSystem.isMusicMuted() ? "UNMUTE MUSIC" : "MUTE MUSIC", skin);
        content.add(musicToggleBtn).colspan(2).width(300).row();

        Label sfxVolumeLabel = new Label("SFX VOLUME", skin);
        sfxVolumeLabel.setFontScale(1.5f);
        Slider sfxVolumeSlider = new Slider(0f, 1f, 0.05f, false, skin);


        if (game.audioSystem.isSfxMuted()) {
            sfxVolumeSlider.setValue(0f);
        } else {
            sfxVolumeSlider.setValue(game.audioSystem.getSfxVolume());
        }

        content.add(sfxVolumeLabel).left();
        content.add(sfxVolumeSlider).width(300).row();
        TextButton sfxToggleBtn = new TextButton(
            game.audioSystem.isSfxMuted() ? "UNMUTE SFX" : "MUTE SFX", skin);
        content.add(sfxToggleBtn).colspan(2).width(300).row();

        TextButton resetBtn = new TextButton("RESET", skin);
        content.add(resetBtn).colspan(2).width(300).padTop(30).row();

        TextButton backBtn = new TextButton("BACK", skin);

        root.add(content).row();
        root.add(backBtn).width(150).height(50).padTop(30);

        stage.addActor(root);

        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (suppressMusicSliderEvent) return;
                game.audioSystem.setMusicVolume(musicVolumeSlider.getValue());
            }
        });

        sfxVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (suppressSfxSliderEvent) return;
                game.audioSystem.setSfxVolume(sfxVolumeSlider.getValue());
            }
        });

        musicToggleBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.audioSystem.toggleMusic();
                boolean nowMuted = game.audioSystem.isMusicMuted();
                musicToggleBtn.setText(nowMuted ? "UNMUTE MUSIC" : "MUTE MUSIC");

                suppressMusicSliderEvent = true;
                if (nowMuted) {
                    musicVolumeSlider.setValue(0f);
                } else {
                    musicVolumeSlider.setValue(MUTE_MID_VOLUME);
                    game.audioSystem.setMusicVolume(MUTE_MID_VOLUME);
                }
                suppressMusicSliderEvent = false;
            }
        });

        sfxToggleBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.audioSystem.toggleSFX();
                boolean nowMuted = game.audioSystem.isSfxMuted();
                sfxToggleBtn.setText(nowMuted ? "UNMUTE SFX" : "MUTE SFX");

                suppressSfxSliderEvent = true;
                if (nowMuted) {
                    sfxVolumeSlider.setValue(0f);
                } else {
                    sfxVolumeSlider.setValue(MUTE_MID_VOLUME);
                    game.audioSystem.setSfxVolume(MUTE_MID_VOLUME);
                }
                suppressSfxSliderEvent = false;
            }
        });

        resetBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                suppressMusicSliderEvent = true;
                musicVolumeSlider.setValue(DEFAULT_MUSIC_VOLUME);
                suppressMusicSliderEvent = false;
                game.audioSystem.setMusicVolume(DEFAULT_MUSIC_VOLUME);

                suppressSfxSliderEvent = true;
                sfxVolumeSlider.setValue(DEFAULT_SFX_VOLUME);
                suppressSfxSliderEvent = false;
                game.audioSystem.setSfxVolume(DEFAULT_SFX_VOLUME);

                if (game.audioSystem.isMusicMuted()) {
                    game.audioSystem.toggleMusic();
                }
                musicToggleBtn.setText("MUTE MUSIC");

                if (game.audioSystem.isSfxMuted()) {
                    game.audioSystem.toggleSFX();
                }
                sfxToggleBtn.setText("MUTE SFX");
            }
        });

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(returnScreen);
            }
        });
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
