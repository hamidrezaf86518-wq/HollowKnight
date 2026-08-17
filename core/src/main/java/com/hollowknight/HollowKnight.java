package com.hollowknight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hollowknight.controller.Systems.AudioSystem;
import com.hollowknight.controller.Systems.AssetSystem;
import com.hollowknight.controller.Systems.SaveSystem;
import com.hollowknight.controller.AchievementManager;
import com.hollowknight.view.screens.MainMenuScreen;


public class HollowKnight extends Game{public SpriteBatch batch;
    public AssetSystem assetSystem;
    public AudioSystem audioSystem;
    public SaveSystem saveSystem;
    public AchievementManager achievementManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assetSystem = new AssetSystem();
        audioSystem = new AudioSystem(assetSystem);
        saveSystem = new SaveSystem();
        achievementManager = new AchievementManager();
        assetSystem.loadAll();
        assetSystem.finishLoading();
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
