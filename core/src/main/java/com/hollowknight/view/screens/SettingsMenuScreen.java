package com.hollowknight.view.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.hollowknight.HollowKnight;
import com.hollowknight.i18n.I18n;
import com.hollowknight.i18n.Language;
import com.hollowknight.i18n.Text;
import com.hollowknight.view.ui.Theme;

public class SettingsMenuScreen extends MenuScreen {
    private static final float DEFAULT_MUSIC_VOLUME = 0.7f;
    private static final float DEFAULT_SFX_VOLUME = 1f;
    private static final float UNMUTE_VOLUME = 0.5f;

    private final Screen returnScreen;

    public SettingsMenuScreen(HollowKnight game, Screen returnScreen) {
        super(game);
        this.returnScreen = returnScreen;
    }

    @Override
    protected void build() {
        Table root = new Table();
        root.setFillParent(true);
        root.center();

        root.add(Theme.label(Text.SETTINGS.get(), "title")).padBottom(10f).row();
        root.add(Theme.divider(460f)).width(460f).height(13f).padBottom(22f).row();

        Table panel = new Table();
        panel.setBackground(Theme.panel());
        panel.pad(30f, 40f, 34f, 40f);

        panel.add(Theme.label(Text.AUDIO.get(), "gold")).left().padBottom(12f).row();

        final Slider musicSlider = new Slider(0f, 1f, 0.05f, false, Theme.skin());
        final Label musicPercent = Theme.label("", "small");
        final TextButton musicToggle = Theme.button("", "small");
        final Slider sfxSlider = new Slider(0f, 1f, 0.05f, false, Theme.skin());
        final Label sfxPercent = Theme.label("", "small");
        final TextButton sfxToggle = Theme.button("", "small");

        final boolean[] silent = {false};

        silent[0] = true;
        musicSlider.setValue(game.audioSystem.isMusicMuted() ? 0f : game.audioSystem.getMusicVolume());
        sfxSlider.setValue(game.audioSystem.isSfxMuted() ? 0f : game.audioSystem.getSfxVolume());
        silent[0] = false;

        updateAudioRow(musicPercent, musicToggle, musicSlider.getValue(), game.audioSystem.isMusicMuted());
        updateAudioRow(sfxPercent, sfxToggle, sfxSlider.getValue(), game.audioSystem.isSfxMuted());

        addAudioRow(panel, Text.MUSIC_VOLUME.get(), musicSlider, musicPercent, musicToggle);
        addAudioRow(panel, Text.SFX_VOLUME.get(), sfxSlider, sfxPercent, sfxToggle);

        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (silent[0]) return;
                game.audioSystem.setMusicVolume(musicSlider.getValue());
                updateAudioRow(musicPercent, musicToggle, musicSlider.getValue(), game.audioSystem.isMusicMuted());
            }
        });

        sfxSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (silent[0]) return;
                game.audioSystem.setSfxVolume(sfxSlider.getValue());
                updateAudioRow(sfxPercent, sfxToggle, sfxSlider.getValue(), game.audioSystem.isSfxMuted());
            }
        });

        musicToggle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.audioSystem.toggleMusic();
                boolean muted = game.audioSystem.isMusicMuted();
                silent[0] = true;
                if (muted) {
                    musicSlider.setValue(0f);
                } else {
                    musicSlider.setValue(UNMUTE_VOLUME);
                    game.audioSystem.setMusicVolume(UNMUTE_VOLUME);
                }
                silent[0] = false;
                updateAudioRow(musicPercent, musicToggle, musicSlider.getValue(), muted);
            }
        });

        sfxToggle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.audioSystem.toggleSFX();
                boolean muted = game.audioSystem.isSfxMuted();
                silent[0] = true;
                if (muted) {
                    sfxSlider.setValue(0f);
                } else {
                    sfxSlider.setValue(UNMUTE_VOLUME);
                    game.audioSystem.setSfxVolume(UNMUTE_VOLUME);
                }
                silent[0] = false;
                updateAudioRow(sfxPercent, sfxToggle, sfxSlider.getValue(), muted);
            }
        });

        panel.add(Theme.label(Text.LANGUAGE.get(), "gold")).left().padTop(26f).padBottom(12f).row();

        Table languages = new Table();
        languages.defaults().width(220f).height(44f).pad(0f, 8f, 0f, 8f);
        for (Language language : Language.values()) {
            languages.add(createLanguageButton(language));
        }
        panel.add(languages).center().row();

        root.add(panel).padBottom(22f).row();

        TextButton resetBtn = Theme.button(Text.RESET.get());
        resetBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.audioSystem.setMusicVolume(DEFAULT_MUSIC_VOLUME);
                game.audioSystem.setSfxVolume(DEFAULT_SFX_VOLUME);
                if (game.audioSystem.isMusicMuted()) {
                    game.audioSystem.toggleMusic();
                }
                if (game.audioSystem.isSfxMuted()) {
                    game.audioSystem.toggleSFX();
                }
                refresh();
            }
        });

        TextButton backBtn = Theme.button(Text.BACK.get());
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onBack();
            }
        });

        Table buttons = new Table();
        buttons.defaults().width(220f).height(44f).pad(0f, 8f, 0f, 8f);
        buttons.add(resetBtn);
        buttons.add(backBtn);
        root.add(buttons);

        stage.addActor(root);
    }

    private void addAudioRow(Table panel, String title, Slider slider, Label percent, TextButton toggle) {
        Table row = new Table();
        Label name = Theme.label(title, "default");
        row.add(name).width(250f).left();
        row.add(slider).width(260f).padLeft(12f);
        row.add(percent).width(64f).padLeft(12f).right();
        row.add(toggle).width(120f).height(36f).padLeft(12f);
        panel.add(row).padBottom(14f).row();
    }

    private void updateAudioRow(Label percent, TextButton toggle, float value, boolean muted) {
        percent.setText(Math.round(value * 100f) + "%");
        percent.setAlignment(Align.right);
        toggle.setText(muted ? Text.UNMUTE.get() : Text.MUTE.get());
    }

    private TextButton createLanguageButton(Language language) {
        boolean active = I18n.getLanguage() == language;
        TextButton button = Theme.button(language.getNativeName(), active ? "selected" : "default");
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (I18n.getLanguage() != language) {
                    I18n.setLanguage(language);
                    refresh();
                }
            }
        });
        return button;
    }

    @Override
    protected void onBack() {
        game.setScreen(returnScreen);
    }
}
