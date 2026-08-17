package com.hollowknight.view.EntitiesView;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.hollowknight.model.Enemies.Enemy;

import static com.hollowknight.model.Constants.PPM;

public class CrystalCrawlerView {

    private final Animation<TextureRegion> animWalk;
    private final Animation<TextureRegion> animDeathLand;
    private float deathTimer = 0f;

    public CrystalCrawlerView(Enemy enemy) {
        Array<TextureRegion> keyFrames = enemy.getKeyFrames("textures/CrystalCrawler/Walk.png", 4);
        animWalk = new Animation<>(0.1f, keyFrames);

        keyFrames = enemy.getKeyFrames("textures/CrystalCrawler/Death Land.png", 2);
        animDeathLand = new Animation<>(0.15f, keyFrames);

        enemy.setRegion(animWalk.getKeyFrame(0));
        enemy.setBounds(enemy.getX(), enemy.getY(),
            animWalk.getKeyFrame(0).getRegionWidth() / PPM,
            animWalk.getKeyFrame(0).getRegionHeight() / PPM);
    }

    public void updateFrame(Enemy enemy, float dt) {
        TextureRegion region;

        if (enemy.isDead()) {
            deathTimer += dt;
            region = animDeathLand.getKeyFrame(deathTimer, false);
        } else {
            region = animWalk.getKeyFrame(enemy.getStateTimer(), true);
            enemy.setStateTimer(enemy.getStateTimer() + dt);
        }

        int face = enemy.getFace();
        if (face == 1 && !region.isFlipX()) region.flip(true, false);
        else if (face == -1 && region.isFlipX()) region.flip(true, false);

        enemy.setRegion(region);
        enemy.setSize(region.getRegionWidth() / PPM, region.getRegionHeight() / PPM);
    }

    public void draw(Batch batch, Enemy enemy) {
        enemy.drawSelf(batch);
    }
}
