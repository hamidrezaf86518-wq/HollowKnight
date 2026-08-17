package com.hollowknight.view.EntitiesView;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.hollowknight.model.Enemies.CrystalGuardian;

import static com.hollowknight.model.Constants.PPM;

public class CrystalGuardianView {

    private final Animation<TextureRegion> idleAnim;
    private final Animation<TextureRegion> runAnim;
    private final Animation<TextureRegion> shootAnim;
    private final Animation<TextureRegion> evadeAnim;
    private final Animation<TextureRegion> deathLandAnim;

    public CrystalGuardianView(CrystalGuardian enemy) {
        Array<TextureRegion> keyFrames = enemy.getKeyFrames("textures/CrystalGuardian/Idle.png", 5);
        idleAnim = new Animation<>(0.1f, keyFrames);

        keyFrames = enemy.getKeyFrames("textures/CrystalGuardian/Run.png", 6);
        runAnim = new Animation<>(0.1f, keyFrames);

        keyFrames = enemy.getKeyFrames("textures/CrystalGuardian/Shoot.png", 7);
        shootAnim = new Animation<>(0.1f, keyFrames);

        keyFrames = enemy.getKeyFrames("textures/CrystalGuardian/Evade.png", 7);
        evadeAnim = new Animation<>(0.1f, keyFrames);

        keyFrames = enemy.getKeyFrames("textures/CrystalGuardian/Death Land.png", 3);
        deathLandAnim = new Animation<>(0.15f, keyFrames);

        enemy.setRegion(idleAnim.getKeyFrame(0));
        enemy.setBounds(enemy.getX(), enemy.getY(),
            idleAnim.getKeyFrame(0).getRegionWidth() / PPM,
            idleAnim.getKeyFrame(0).getRegionHeight() / PPM);
    }

    public void updateFrame(CrystalGuardian enemy, float dt) {
        TextureRegion region;
        float stateTimer = enemy.getStateTimer();

        switch (enemy.getCurrentState()) {
            case IDLE:
                region = idleAnim.getKeyFrame(stateTimer, true);
                break;
            case RUN:
                region = runAnim.getKeyFrame(stateTimer, true);
                break;
            case SHOOT:
                region = shootAnim.getKeyFrame(stateTimer);
                break;
            case DEATHLAND:

                region = deathLandAnim.getKeyFrame(stateTimer, false);
                break;
            default:
                region = idleAnim.getKeyFrame(stateTimer, true);
        }

        int face = enemy.getFace();
        if (face == 1 && !region.isFlipX()) {
            region.flip(true, false);
        } else if (face == -1 && region.isFlipX()) {
            region.flip(true, false);
        }

        enemy.setStateTimer(enemy.getCurrentState() == enemy.getPreviousState() ? stateTimer + dt : 0);
        enemy.setPreviousState(enemy.getCurrentState());
        enemy.setRegion(region);
        enemy.setSize(region.getRegionWidth() / PPM, region.getRegionHeight() / PPM);
    }

    public void draw(Batch batch, CrystalGuardian enemy) {

        if (enemy.getLaser() != null) {
            enemy.getLaser().draw(batch);
        }
        enemy.drawSelf(batch);
    }
}
