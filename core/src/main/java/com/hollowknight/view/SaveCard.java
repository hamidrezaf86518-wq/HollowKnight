package com.hollowknight.view;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hollowknight.HollowKnight;
import com.hollowknight.i18n.I18n;
import com.hollowknight.i18n.Text;
import com.hollowknight.model.GameData;
import com.hollowknight.view.screens.GameScreen;
import com.hollowknight.view.ui.Theme;

import java.text.DateFormat;
import java.util.Date;

public class SaveCard extends Table {
    private final HollowKnight hollowKnight;
    private final int slot;

    public SaveCard(HollowKnight hollowKnight, int slot) {
        this.hollowKnight = hollowKnight;
        this.slot = slot;

        GameData data = hollowKnight.saveSystem.slotExists(slot) ? hollowKnight.saveSystem.load(slot) : null;
        boolean occupied = data != null;

        setBackground(Theme.card());
        pad(14f, 24f, 14f, 18f);

        Table info = new Table();
        info.left();
        Label name = Theme.label(Text.PROFILE.fmt(slot + 1), occupied ? "gold" : "heading");
        info.add(name).left().row();

        if (occupied) {
            String stats = Text.PLAY_TIME.get() + "  " + Theme.formatTime(data.playTimeSeconds)
                + "     " + Text.DEATHS.get() + "  " + data.deathCount;
            info.add(Theme.label(stats, "small")).left().padTop(6f).row();
            String date = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, I18n.locale())
                .format(new Date(data.savedAt));
            info.add(Theme.label(date, "muted")).left().padTop(2f).row();
        } else {
            info.add(Theme.label(Text.EMPTY_SLOT.get(), "muted")).left().padTop(6f).row();
        }

        TextButton newBtn = Theme.button(Text.NEW.get());
        TextButton loadBtn = Theme.button(Text.LOAD.get());
        loadBtn.setDisabled(!occupied);
        loadBtn.setTouchable(occupied ? Touchable.enabled : Touchable.disabled);

        add(info).expandX().left();
        add(newBtn).width(130f).height(42f).padRight(10f);
        add(loadBtn).width(130f).height(42f);

        newBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (occupied) {
                    Theme.confirm(getStage(), Text.OVERWRITE_TITLE.get(), Text.OVERWRITE_MESSAGE.get(), SaveCard.this::startNew);
                } else {
                    startNew();
                }
            }
        });

        loadBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startLoad();
            }
        });
    }

    private void startNew() {
        hollowKnight.saveSystem.deleteSlot(slot);
        GameScreen gameScreen = new GameScreen(hollowKnight);
        gameScreen.setSlot(slot);
        hollowKnight.setScreen(gameScreen);
    }

    private void startLoad() {
        GameData data = hollowKnight.saveSystem.load(slot);
        if (data == null) {
            return;
        }
        GameScreen gameScreen = new GameScreen(hollowKnight, data);
        gameScreen.setSlot(slot);
        hollowKnight.setScreen(gameScreen);
    }
}
