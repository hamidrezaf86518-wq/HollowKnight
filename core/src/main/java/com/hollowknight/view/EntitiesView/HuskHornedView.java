package com.hollowknight.view.EntitiesView;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.hollowknight.model.Enemies.HuskHorned;

import static com.hollowknight.model.Constants.PPM;

public class HuskHornedView {

    private final Animation<TextureRegion> animAttackAnticipate;
    private final Animation<TextureRegion> animAttack;
    private final Animation<TextureRegion> animWalk;
    private final Animation<TextureRegion> animIdle;
    private final Animation<TextureRegion> animDeathLand;

    public HuskHornedView(HuskHorned enemy) {
        Array<TextureRegion> keyFrames = enemy.getKeyFrames("textures/HuskHorned/Attack Anticipate.png", 5);
        animAttackAnticipate = new Animation<>(0.1f, keyFrames);

        keyFrames = enemy.getKeyFrames("textures/HuskHorned/Attack Lunge.png", 12);
        animAttack = new Animation<>(0.1f, keyFrames);

        keyFrames = enemy.getKeyFrames("textures/HuskHorned/Walk.png", 7);
        animWalk = new Animation<>(0.1f, keyFrames);

        keyFrames = enemy.getKeyFrames("textures/HuskHorned/Idle.png", 6);
        animIdle = new Animation<>(0.1f, keyFrames);

        keyFrames = enemy.getKeyFrames("textures/HuskHorned/Death Land.png", 8);
        animDeathLand = new Animation<>(0.12f, keyFrames);

        enemy.setRegion(animWalk.getKeyFrame(0));
        enemy.setBounds(enemy.getX(), enemy.getY(),
            animWalk.getKeyFrame(0).getRegionWidth() / PPM,
            animWalk.getKeyFrame(0).getRegionHeight() / PPM);
    }

    public void updateFrame(HuskHorned enemy, float dt) {
        TextureRegion region;
        float stateTimer = enemy.getStateTimer();

        switch (enemy.getCurrentState()) {
            case IDLE:
                region = animIdle.getKeyFrame(stateTimer, true);
                break;
            case WALK:
                region = animWalk.getKeyFrame(stateTimer, true);
                break;
            case ATTACKANTICIPATE:
                region = animAttackAnticipate.getKeyFrame(stateTimer);
                break;
            case ATTACK:
                region = animAttack.getKeyFrame(stateTimer, true);
                break;
            case DEATHLAND:
                region = animDeathLand.getKeyFrame(stateTimer, false);
                break;
            default:
                region = animIdle.getKeyFrame(stateTimer, true);
        }

        int face = enemy.getFace();
        if (face == 1 && !region.isFlipX()) region.flip(true, false);
        else if (face == -1 && region.isFlipX()) region.flip(true, false);

        enemy.setStateTimer(enemy.getCurrentState() == enemy.getPreviousState() ? stateTimer + dt : 0);
        enemy.setPreviousState(enemy.getCurrentState());
        enemy.setRegion(region);
    }

    public void draw(Batch batch, HuskHorned enemy) {
        enemy.drawSelf(batch);
    }
}
