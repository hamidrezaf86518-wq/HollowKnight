package com.hollowknight.view.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.hollowknight.HollowKnight;
import com.hollowknight.i18n.Text;
import com.hollowknight.view.ui.Theme;

import java.util.HashMap;
import java.util.Map;

import static com.hollowknight.model.Constants.WORLD_HEIGHT;
import static com.hollowknight.model.KeyBinding.*;

public class GuideMenuScreen extends MenuScreen {
    private static final float CONTENT_WIDTH = 820f;

    private final Map<Integer, KeyChip> chips = new HashMap<>();

    public GuideMenuScreen(HollowKnight game) {
        super(game);
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                KeyChip chip = chips.get(keycode);
                if (chip != null) {
                    chip.setPressed(true);
                }
                return false;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                KeyChip chip = chips.get(keycode);
                if (chip != null) {
                    chip.setPressed(false);
                }
                return false;
            }
        });
    }

    @Override
    protected void build() {
        chips.clear();

        Table root = new Table();
        root.setFillParent(true);
        root.top().padTop(30f);
        root.add(Theme.label(Text.GUIDE_TITLE.get(), "title")).padBottom(10f).row();
        root.add(Theme.divider(460f)).width(460f).height(13f).padBottom(18f).row();

        Table content = new Table();
        content.top().left();
        content.defaults().left();

        addSection(content, Text.GUIDE_CONTROLS.get(), true);
        content.add(Theme.label(Text.GUIDE_CONTROLS_HINT.get(), "muted")).left().padBottom(10f).row();
        addKeyRow(content, Text.ACTION_ATTACK.get(), ATTACK);
        addKeyRow(content, Text.ACTION_JUMP.get(), JUMP);
        addKeyRow(content, Text.ACTION_DASH.get(), DASH);
        addKeyRow(content, Text.ACTION_MOVE_RIGHT.get(), MOVE_RIGHT);
        addKeyRow(content, Text.ACTION_MOVE_LEFT.get(), MOVE_LEFT);
        addKeyRow(content, Text.ACTION_FOCUS.get(), FOCUS);
        addKeyRow(content, Text.ACTION_PAUSE.get(), PAUSE);

        addSection(content, Text.GUIDE_ABILITIES.get(), false);
        addEntry(content, Text.ABILITY_NAIL.get(), Text.ABILITY_NAIL_DESC.get());
        addEntry(content, Text.ABILITY_JUMP.get(), Text.ABILITY_JUMP_DESC.get());
        addEntry(content, Text.ABILITY_DASH.get(), Text.ABILITY_DASH_DESC.get());
        addEntry(content, Text.ABILITY_FOCUS.get(), Text.ABILITY_FOCUS_DESC.get());
        addEntry(content, Text.ABILITY_SPIRIT.get(), Text.ABILITY_SPIRIT_DESC.get());

        addSection(content, Text.GUIDE_HEALTH.get(), false);
        addEntry(content, Text.HEALTH_MASKS.get(), Text.HEALTH_MASKS_DESC.get());
        addEntry(content, Text.HEALTH_SOUL.get(), Text.HEALTH_SOUL_DESC.get());
        addEntry(content, Text.HEALTH_HEALING.get(), Text.HEALTH_HEALING_DESC.get());

        addSection(content, Text.CHEAT_CODES.get(), false);
        addCheatRow(content, "LEFT CTRL + B", Text.CHEAT_BOSS.get());
        addCheatRow(content, "LEFT CTRL + N", Text.CHEAT_NOCLIP.get());
        addCheatRow(content, "LEFT CTRL + H", Text.CHEAT_HEAL.get());
        addCheatRow(content, "LEFT CTRL + S", Text.CHEAT_SOUL.get());
        addCheatRow(content, "LEFT CTRL + G", Text.CHEAT_GOD.get());

        ScrollPane scrollPane = new ScrollPane(content, Theme.skin());
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFadeScrollBars(false);

        Table frame = new Table();
        frame.setBackground(Theme.panel());
        frame.pad(24f, 30f, 24f, 24f);
        frame.add(scrollPane).grow();

        root.add(frame).width(CONTENT_WIDTH + 90f).height(WORLD_HEIGHT - 170f);

        stage.addActor(root);
        addBackButton();
        stage.setScrollFocus(scrollPane);
    }

    private void addSection(Table content, String title, boolean first) {
        content.add(Theme.label(title, "gold")).left().padTop(first ? 0f : 28f).padBottom(6f).row();
        content.add(Theme.divider(CONTENT_WIDTH)).width(CONTENT_WIDTH).height(13f).padBottom(12f).row();
    }

    private void addKeyRow(Table content, String action, int keycode) {
        KeyChip chip = new KeyChip(Theme.keyName(keycode));
        chips.put(keycode, chip);

        Table row = new Table();
        row.add(Theme.label(action, "default")).left().expandX();
        row.add(chip).width(200f).height(36f);
        content.add(row).width(CONTENT_WIDTH).padBottom(8f).row();
    }

    private void addCheatRow(Table content, String keys, String description) {
        Table row = new Table();
        row.add(new KeyChip(keys)).width(220f).height(34f).padRight(24f);
        row.add(Theme.label(description, "small")).left().expandX();
        content.add(row).width(CONTENT_WIDTH).padBottom(8f).row();
    }

    private void addEntry(Table content, String name, String description) {
        content.add(Theme.label(name, "default")).left().padTop(6f).row();
        Label text = Theme.label(description, "muted");
        text.setWrap(true);
        text.setAlignment(Align.left);
        content.add(text).width(CONTENT_WIDTH).padBottom(8f).row();
    }

    @Override
    protected boolean handlesBack() {
        return false;
    }

    @Override
    protected void onBack() {
        game.setScreen(new MainMenuScreen(game));
    }

    private static class KeyChip extends Table {
        private final Label label;

        KeyChip(String text) {
            label = Theme.label(text, "small");
            label.setAlignment(Align.center);
            setBackground(Theme.chip());
            add(label).expand().center();
        }

        void setPressed(boolean pressed) {
            setBackground(pressed ? Theme.chipOn() : Theme.chip());
            label.setColor(pressed ? Theme.DARK : Theme.TEXT);
        }
    }
}
