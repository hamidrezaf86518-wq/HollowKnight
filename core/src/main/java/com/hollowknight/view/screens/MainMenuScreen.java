package com.hollowknight.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hollowknight.HollowKnight;
import com.hollowknight.i18n.Text;
import com.hollowknight.view.ui.Theme;

import static com.hollowknight.model.Constants.WORLD_HEIGHT;
import static com.hollowknight.model.Constants.WORLD_WIDTH;

public class MainMenuScreen extends MenuScreen {
    private final Texture logo;
    private final Texture rightLogo;
    private final Texture leftLogo;

    public MainMenuScreen(HollowKnight game) {
        super(game);
        logo = game.assetSystem.get("textures/vheart_title_RU.png", Texture.class);
        rightLogo = game.assetSystem.get("textures/team_cherry_logo_main_menu.png", Texture.class);
        leftLogo = game.assetSystem.get("textures/Hidden_Dreams_Logo.png", Texture.class);
    }

    @Override
    protected void build() {
        Table root = new Table();
        root.setFillParent(true);
        root.bottom().padBottom(48f);
        root.defaults().width(320f).height(42f).padTop(8f);

        root.add(menuButton(Text.GAME_START.get(), () -> game.setScreen(new GameStartScreen(game)))).row();
        root.add(menuButton(Text.GUIDE.get(), () -> game.setScreen(new GuideMenuScreen(game)))).row();
        root.add(menuButton(Text.SETTINGS.get(), () -> game.setScreen(new SettingsMenuScreen(game, MainMenuScreen.this)))).row();
        root.add(menuButton(Text.ACHIEVEMENTS.get(), () -> game.setScreen(new AchievementsScreen(game)))).row();
        root.add(menuButton(Text.QUIT.get(), () -> Gdx.app.exit())).row();

        stage.addActor(root);
    }

    private TextButton menuButton(String text, Runnable action) {
        TextButton button = Theme.button(text);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                action.run();
            }
        });
        return button;
    }

    @Override
    protected boolean handlesBack() {
        return false;
    }

    @Override
    protected void drawBackdrop(Batch batch) {
        batch.setColor(1f, 1f, 1f, 1f);
        batch.draw(background, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        batch.setColor(1f, 1f, 1f, 0.95f);
        batch.draw(Theme.shade(), 0, 0, WORLD_WIDTH, WORLD_HEIGHT * 0.55f);
        batch.setColor(1f, 1f, 1f, 1f);

        float logoW = logo.getWidth() * 0.8f;
        float logoH = logo.getHeight() * 0.8f;
        float x = (WORLD_WIDTH - logoW) / 2f;
        float y = (WORLD_HEIGHT - logoH) / 2f;

        batch.draw(logo, x, y + 100f, logoW, logoH);
        batch.draw(rightLogo, WORLD_WIDTH - 300f, WORLD_HEIGHT - 650f, rightLogo.getWidth() * 0.6f, rightLogo.getHeight() * 0.6f);
        batch.draw(leftLogo, 200f, WORLD_HEIGHT - 610f);
    }
}
