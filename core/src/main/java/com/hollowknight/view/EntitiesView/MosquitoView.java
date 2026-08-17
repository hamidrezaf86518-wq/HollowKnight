package com.hollowknight.view.EntitiesView;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.hollowknight.model.Enemies.Mosquito;

import static com.hollowknight.model.Constants.PPM;

public class MosquitoView {

    private final Animation<TextureRegion> idleAnim;
    private final Animation<TextureRegion> deathAirAnim;
    private final Animation<TextureRegion> deathLandAnim;
    private final Animation<TextureRegion> attackAnticipateAnim;
    private final Animation<TextureRegion> attackAnim;

    public MosquitoView(Mosquito enemy) {
        Array<TextureRegion> keyFrames = enemy.getKeyFrames("textures/Mosquito/Idle.png", 8);
        idleAnim = new Animation<>(0.1f, keyFrames);

        keyFrames = enemy.getKeyFrames("textures/Mosquito/Death Land.png", 2);
        deathLandAnim = new Animation<>(0.15f, keyFrames);

        keyFrames = enemy.getKeyFrames("textures/Mosquito/Death Air.png", 3);
        deathAirAnim = new Animation<>(0.12f, keyFrames);

        keyFrames = enemy.getKeyFrames("textures/Mosquito/Attack Anticipate.png", 6);
        attackAnticipateAnim = new Animation<>(0.1f, keyFrames);

        keyFrames = enemy.getKeyFrames("textures/Mosquito/Attack.png", 3);
        attackAnim = new Animation<>(0.1f, keyFrames);

        enemy.setRegion(idleAnim.getKeyFrame(0));
        enemy.setBounds(enemy.getX(), enemy.getY(),
            idleAnim.getKeyFrame(0).getRegionWidth() / PPM,
            idleAnim.getKeyFrame(0).getRegionHeight() / PPM);
    }

    public void updateFrame(Mosquito enemy, float dt) {
        TextureRegion region;
        float stateTimer = enemy.getStateTimer();

        switch (enemy.getCurrentState()) {
            case IDLE:
                region = idleAnim.getKeyFrame(stateTimer, true);
                break;
            case ATTACKANTICIPATE:
                region = attackAnticipateAnim.getKeyFrame(stateTimer);
                break;
            case ATTACK:
                region = attackAnim.getKeyFrame(stateTimer, true);
                break;
            case DEATHAIR:

                region = deathAirAnim.getKeyFrame(stateTimer, true);
                break;
            case DEATHLAND:

                region = deathLandAnim.getKeyFrame(stateTimer, false);
                break;
            default:
                region = idleAnim.getKeyFrame(stateTimer, true);
        }

        int face = enemy.getFace();
        if (face == 1 && !region.isFlipX()) region.flip(true, false);
        else if (face == -1 && region.isFlipX()) region.flip(true, false);

        enemy.setStateTimer(enemy.getCurrentState() == enemy.getPreviousState() ? stateTimer + dt : 0);
        enemy.setPreviousState(enemy.getCurrentState());
        enemy.setRegion(region);
        enemy.setSize(region.getRegionWidth() / PPM, region.getRegionHeight() / PPM);
    }

    public void draw(Batch batch, Mosquito enemy) {
        enemy.drawSelf(batch);
    }
}
