package com.hollowknight.view.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.hollowknight.HollowKnight;
import com.hollowknight.controller.Systems.SaveSystem;
import com.hollowknight.i18n.Text;
import com.hollowknight.view.SaveCard;
import com.hollowknight.view.ui.Theme;

public class GameStartScreen extends MenuScreen {

    public GameStartScreen(HollowKnight game) {
        super(game);
    }

    @Override
    protected void build() {
        Table root = new Table();
        root.setFillParent(true);
        root.top().padTop(36f);

        root.add(Theme.label(Text.SELECT_PROFILE.get(), "title")).padBottom(10f).row();
        root.add(Theme.divider(460f)).width(460f).height(13f).padBottom(26f).row();

        for (int slot = 0; slot < SaveSystem.SLOT_COUNT; slot++) {
            root.add(new SaveCard(game, slot)).width(720f).height(96f).padBottom(12f).row();
        }

        stage.addActor(root);
        addBackButton();
    }

    @Override
    protected void onBack() {
        game.setScreen(new MainMenuScreen(game));
    }
}
