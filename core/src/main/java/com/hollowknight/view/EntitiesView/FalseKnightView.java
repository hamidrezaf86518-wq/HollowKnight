package com.hollowknight.view.EntitiesView;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.hollowknight.model.FalseKnight;

import static com.hollowknight.model.Constants.PPM;


public class FalseKnightView {

    private final Animation<TextureRegion> maceSlamAntiAnim;
    private final Animation<TextureRegion> maceSlamAnim;
    private final Animation<TextureRegion> maceSlamRecovAnim;
    private final Animation<TextureRegion> deathLandAnim;
    private final Animation<TextureRegion> stunAnim;
    private final Animation<TextureRegion> chargeRunAnim;
    private final Animation<TextureRegion> jumpAntiAnim;
    private final Animation<TextureRegion> jumpAnim;
    private final Animation<TextureRegion> powerfulHitAnim;
    private final Animation<TextureRegion> idleAnim;
    private final Animation<TextureRegion> stunRecovAnim;
    private final Animation<TextureRegion> chargeRunAntiAnim;

    public FalseKnightView(FalseKnight boss) {
        Array<TextureRegion> keyFrames = boss.getKeyFrames("textures/falseknight/Attack Antic.png", 6);
        maceSlamAntiAnim = new Animation<>(0.1f, keyFrames);
        keyFrames = boss.getKeyFrames("textures/falseknight/Attack.png", 3);
        maceSlamAnim = new Animation<>(0.1f, keyFrames);
        keyFrames = boss.getKeyFrames("textures/falseknight/Attack Recover.png", 5);
        maceSlamRecovAnim = new Animation<>(0.1f, keyFrames);
        keyFrames = boss.getKeyFrames("textures/falseknight/DeathLand.png", 11);
        deathLandAnim = new Animation<>(0.1f, keyFrames);
        keyFrames = boss.getKeyFrames("textures/falseknight/Body.png", 5);
        stunAnim = new Animation<>(0.1f, keyFrames);
        keyFrames = boss.getKeyFrames("textures/falseknight/Idle.png", 5);
        idleAnim = new Animation<>(0.1f, keyFrames);
        keyFrames = boss.getKeyFrames("textures/falseknight/Jump Antic.png", 3);
        jumpAntiAnim = new Animation<>(0.1f, keyFrames);
        keyFrames = boss.getKeyFrames("textures/falseknight/Jump.png", 4);
        jumpAnim = new Animation<>(0.1f, keyFrames);
        keyFrames = boss.getKeyFrames("textures/falseknight/Jump Attack.png", 8);
        powerfulHitAnim = new Animation<>(0.1f, keyFrames);
        keyFrames = boss.getKeyFrames("textures/falseknight/Run Antic.png", 2);
        chargeRunAntiAnim = new Animation<>(0.1f, keyFrames);
        keyFrames = boss.getKeyFrames("textures/falseknight/Run.png", 5);
        chargeRunAnim = new Animation<>(0.1f, keyFrames);
        keyFrames = boss.getKeyFrames("textures/falseknight/Stun Recover.png", 6);
        stunRecovAnim = new Animation<>(0.1f, keyFrames);

        boss.setRegion(idleAnim.getKeyFrame(0));
        boss.setBounds(0, 0,
            idleAnim.getKeyFrame(0).getRegionWidth() / PPM,
            idleAnim.getKeyFrame(0).getRegionHeight() - 200 / PPM);
    }

    public void updateFrame(FalseKnight boss, float dt) {
        TextureRegion region;
        float stateTimer = boss.getStateTimer();

        switch (boss.getCurrentState()) {
            case CHARGERUNANTI:
                region = chargeRunAntiAnim.getKeyFrame(stateTimer);
                break;
            case IDLE:
                region = idleAnim.getKeyFrame(stateTimer, true);
                break;
            case CHARGERUN:
                region = chargeRunAnim.getKeyFrame(stateTimer, true);
                break;
            case OFFENSIVELEAPANTI:
            case DEFENSIVELEAPANTI:
                region = jumpAntiAnim.getKeyFrame(stateTimer);
                break;
            case OFFENSIVELEAP:
            case DEFENSIVELEAP:
                region = jumpAnim.getKeyFrame(stateTimer);
                break;
            case MACESLAMANTI:
                region = maceSlamAntiAnim.getKeyFrame(stateTimer);
                break;
            case MACESLAM:
                region = maceSlamAnim.getKeyFrame(stateTimer);
                break;
            case MACESLAMRECOV:
                region = maceSlamRecovAnim.getKeyFrame(stateTimer);
                break;
            case DEATHLAND:
                region = deathLandAnim.getKeyFrame(stateTimer,false);
                break;
            case STUN:
                region = stunAnim.getKeyFrame(stateTimer);
                break;
            case POWERFULHIT:
                region = powerfulHitAnim.getKeyFrame(stateTimer);
                break;
            case STUNRECOV:
                region = stunRecovAnim.getKeyFrame(stateTimer);
                break;
            default:
                region = idleAnim.getKeyFrame(stateTimer, true);
        }

        int face = boss.getFace();
        if (face == 1 && !region.isFlipX()) {
            region.flip(true, false);
        } else if (face == -1 && region.isFlipX()) {
            region.flip(true, false);
        }

        boss.setStateTimer(boss.getCurrentState() == boss.getPreviousState() ? stateTimer + dt : 0);
        boss.setPreviousState(boss.getCurrentState());

        boss.setRegion(region);
        boss.setSize(region.getRegionWidth() / PPM, region.getRegionHeight() / PPM);
    }

    public void draw(Batch batch, FalseKnight boss) {
        if (boss.getWave() != null) {
            boss.getWave().draw(batch);
        }

        boss.drawSelf(batch);
    }
}
