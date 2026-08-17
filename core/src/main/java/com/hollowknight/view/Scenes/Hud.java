package com.hollowknight.view.Scenes;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hollowknight.HollowKnight;
import com.hollowknight.controller.AchievementListener;
import com.hollowknight.model.Achievement;
import com.hollowknight.controller.Systems.AssetSystem;
import com.hollowknight.view.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

import static com.hollowknight.model.Constants.WORLD_HEIGHT;
import static com.hollowknight.model.Constants.WORLD_WIDTH;


public class Hud implements Disposable, AchievementListener {
    public Stage stage;
    private Viewport viewport;
    private SpriteBatch sb;
    private AssetSystem assetSystem;
    private HollowKnight game;

    private MaskActor[] masksArray;
    private Table table1;
    private Vector2 targetPosition;

    private SoulOrbActor soulOrb;
    private Label achievementPopupLabel;

    Label masksLabel;

    public Hud(SpriteBatch sb, HollowKnight game) {
        this.sb = sb;
        this.game = game;
        assetSystem = game.assetSystem;
        viewport = new FitViewport(WORLD_WIDTH,WORLD_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,sb);
        targetPosition = new Vector2();

        table1 = new Table();
        table1.top().left();

        Table table2 = new Table();
        table2.top().left();

        Texture healthBar = assetSystem.get("textures/HUD/HealthBar_005.png", Texture.class);
        Image healthBarImage = new Image(healthBar);

        table2.add(healthBarImage);

        masksArray = new MaskActor[5];
        for (int i = 0; i < 5; i++) {
            masksArray[i] = new MaskActor(game);
            if (i == 0) {
                table1.add(masksArray[i]).padLeft(100);
            } else {
                table1.add(masksArray[i]);
            }
        }


        soulOrb = new SoulOrbActor(game);
        Table table3 = new Table();
        table3.top().left();
        table3.add(soulOrb).padLeft(10).padTop(38).size(130, 125);




        Label.LabelStyle popupStyle = LabelStyle.getStyle(20);
        achievementPopupLabel = new Label("", popupStyle);
        achievementPopupLabel.setAlignment(Align.center);
        achievementPopupLabel.getColor().a = 0f;
        Table popupTable = new Table();
        popupTable.top();
        popupTable.add(achievementPopupLabel).padTop(20);

        Stack stack = new Stack();
        stack.setFillParent(true);
        stack.add(table2);
        stack.add(table1);
        stack.add(table3);
        stack.add(popupTable);

        stage.addActor(stack);

        game.achievementManager.addListener(this);
    }


    @Override
    public void onAchievementUnlocked(Achievement achievement) {
        achievementPopupLabel.setText("Achievement Unlocked: " + achievement.getTitle());
        achievementPopupLabel.clearActions();
        achievementPopupLabel.getColor().a = 0f;
        achievementPopupLabel.addAction(Actions.sequence(
            Actions.fadeIn(0.4f),
            Actions.delay(2.5f),
            Actions.fadeOut(0.6f)
        ));
    }

    public void onKnightTakeDamage(int currentHealth) {







        int index = currentHealth - 1;
        if (index >= 0 && index < 5) {
            masksArray[index].breakMask();
        }
    }

    public void onKnightHeal(int currentHealth) {
        int index = currentHealth - 1;
        if (index >= 0 && index < 5) {
            masksArray[index].fillMask();
        }
    }


    public void resetMasksFull() {
        for (MaskActor mask : masksArray) {
            mask.resetToFull();
        }
    }


    public void onSoulChange(int currentSoul, int maxSoul) {
        soulOrb.setSoulPercent(currentSoul / (float) maxSoul);
    }

    public void update(float dt) {
        stage.act(dt);
    }

    public void render() {
        stage.draw();
    }

    @Override
    public void dispose() {
        game.achievementManager.removeListener(this);
        stage.dispose();
    }
}
