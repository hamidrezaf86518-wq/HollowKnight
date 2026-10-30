package com.hollowknight.view.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.hollowknight.HollowKnight;
import com.hollowknight.i18n.Text;
import com.hollowknight.model.Achievement;
import com.hollowknight.view.ui.Theme;

import static com.hollowknight.model.Constants.WORLD_HEIGHT;

public class AchievementsScreen extends MenuScreen {

    public AchievementsScreen(HollowKnight game) {
        super(game);
    }

    @Override
    protected void build() {
        Table root = new Table();
        root.setFillParent(true);
        root.top().padTop(36f);

        int unlockedCount = 0;
        for (Achievement achievement : Achievement.values()) {
            if (game.achievementManager.isUnlocked(achievement)) {
                unlockedCount++;
            }
        }

        root.add(Theme.label(Text.ACH_TITLE.get(), "title")).padBottom(4f).row();
        root.add(Theme.label(Text.ACH_PROGRESS.fmt(unlockedCount, Achievement.values().length), "muted")).padBottom(10f).row();
        root.add(Theme.divider(460f)).width(460f).height(13f).padBottom(22f).row();

        Table list = new Table();
        list.top();
        list.defaults().growX().padBottom(12f);

        for (Achievement achievement : Achievement.values()) {
            list.add(createCard(achievement)).row();
        }

        ScrollPane scrollPane = new ScrollPane(list, Theme.skin());
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setOverscroll(false, false);

        Table frame = new Table();
        frame.setBackground(Theme.panel());
        frame.pad(22f);
        frame.add(scrollPane).grow();

        root.add(frame).width(900f).height(WORLD_HEIGHT - 230f);

        stage.addActor(root);
        addBackButton();
    }

    private Table createCard(Achievement achievement) {
        boolean unlocked = game.achievementManager.isUnlocked(achievement);

        Table card = new Table();
        card.setBackground(Theme.card());
        card.pad(16f, 20f, 16f, 20f);

        Label badge = Theme.label(unlocked ? Text.ACH_UNLOCKED.get() : Text.ACH_LOCKED.get(), "small");
        badge.setColor(unlocked ? Theme.GOOD : Theme.MUTED);
        Table badgeBox = new Table();
        badgeBox.setBackground(Theme.chip());
        badgeBox.pad(6f, 10f, 6f, 10f);
        badgeBox.add(badge);

        Label name = Theme.label(achievement.getTitle(), "gold");
        name.setColor(unlocked ? Theme.GOLD : Theme.MUTED);
        Label description = Theme.label(unlocked ? achievement.getDescription() : "???", "small");
        description.setWrap(true);
        description.setColor(unlocked ? Theme.TEXT : Theme.MUTED);

        Table text = new Table();
        text.left();
        text.add(name).left().row();
        text.add(description).left().growX().padTop(4f).row();

        card.add(badgeBox).width(150f).left().top();
        card.add(text).growX().padLeft(22f).left();

        if (!unlocked) {
            card.setColor(1f, 1f, 1f, 0.7f);
        }
        return card;
    }

    @Override
    protected void onBack() {
        game.setScreen(new MainMenuScreen(game));
    }
}
