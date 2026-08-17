package com.hollowknight.view.EntitiesView;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.hollowknight.model.Knight;

import static com.hollowknight.model.Constants.PPM;


public class KnightView {

    private Animation<TextureRegion> knightRun;
    private Animation<TextureRegion> knightJump;
    private Animation<TextureRegion> knightIdle;
    private Animation<TextureRegion> knightDash;
    private Animation<TextureRegion> knightFocus;
    private Animation<TextureRegion> leftAttack;
    private Animation<TextureRegion> upAttack;
    private Animation<TextureRegion> downAttack;
    private Animation<TextureRegion> doubleJumpAnim;
    private Animation<TextureRegion> wallSlideAnim;
    private Animation<TextureRegion> shadowDashAnim;

    public KnightView(Knight knight) {

        Array<TextureRegion> keyFrames = getKeyFrames(knight, "textures/Knight/Run.png", 13);
        knightRun = new Animation<>(0.1f, keyFrames);
        keyFrames.clear();


        keyFrames = getKeyFrames(knight, "textures/Knight/Idle.png", 9);
        knightIdle = new Animation<>(0.1f, keyFrames);
        keyFrames.clear();


        keyFrames = getKeyFrames(knight, "textures/Knight/Dash.png", 12);
        knightDash = new Animation<>(0.1f, keyFrames);


        keyFrames = getKeyFrames(knight, "textures/Knight/Focus.png", 4);
        knightFocus = new Animation<>(0.1f, keyFrames);


        keyFrames = getKeyFrames(knight, "textures/Knight/Slash.png", 5);
        leftAttack = new Animation<>(Knight.ATTACK_TIMER / 5, keyFrames);


        keyFrames = getKeyFrames(knight, "textures/Knight/UpSlash.png", 5);
        upAttack = new Animation<>(Knight.ATTACK_TIMER / 5, keyFrames);


        keyFrames = getKeyFrames(knight, "textures/Knight/DownSlash.png", 5);
        downAttack = new Animation<>(Knight.ATTACK_TIMER / 5, keyFrames);


        keyFrames = getKeyFrames(knight, "textures/Knight/Airborne.png", 12);
        knightJump = new Animation<>(0.08f, keyFrames);


        keyFrames = getKeyFrames(knight, "textures/Knight/Double Jump.png", 8);
        doubleJumpAnim = new Animation<>(0.1f, keyFrames);


        keyFrames = getKeyFrames(knight, "textures/Knight/Wall Slide.png", 4);
        wallSlideAnim = new Animation<>(0.1f, keyFrames);


        keyFrames = getKeyFrames(knight, "textures/Knight/Shadow Dash.png", 11);
        shadowDashAnim = new Animation<>(0.1f, keyFrames);

        knight.setRegion(knightIdle.getKeyFrame(0));
        knight.setBounds(0, 0,
            knightIdle.getKeyFrame(0).getRegionWidth() / PPM,
            knightIdle.getKeyFrame(0).getRegionHeight() / PPM);
    }

    private Array<TextureRegion> getKeyFrames(Knight knight, String path, int amount){
        Texture spriteSheet = knight.getGame().assetSystem.get(path,Texture.class);
        TextureRegion[] split = TextureRegion.split(spriteSheet,spriteSheet.getWidth()/amount,spriteSheet.getHeight())[0];
        Array<TextureRegion> keyFrames = new Array<>();

        for (int i = 0 ; i < amount ; i++){
            keyFrames.add(split[i]);
        }

        return keyFrames;
    }

    public void updateFrame(Knight knight, float dt){


        TextureRegion region;
        switch (knight.currentState){
            case RUN:
                region = knightRun.getKeyFrame(knight.getStateTimer(),true);
                break;
            case IDLE:
                region  = knightIdle.getKeyFrame(knight.getStateTimer(), true);
                break;
            case DASH:
                region = knightDash.getKeyFrame(knight.getStateTimer());
                break;
            case FOCUS:
                region = knightFocus.getKeyFrame(knight.getStateTimer());
                break;
            case LEFTATTACK:
                region = leftAttack.getKeyFrame(knight.getStateTimer());
                break;
            case RIGHTATTACK:
                region = leftAttack.getKeyFrame(knight.getStateTimer());
                break;
            case UPATTACK:
                region = upAttack.getKeyFrame(knight.getStateTimer());
                break;
            case DOWNATTACK:
                region = downAttack.getKeyFrame(knight.getStateTimer());
                break;
            case POGO:
                region = downAttack.getKeyFrame(knight.getStateTimer());
                break;
            case JUMP:
                region = knightJump.getKeyFrame(knight.getStateTimer());
                break;
            case DOUBLEJUMP:
                region = doubleJumpAnim.getKeyFrame(knight.getStateTimer());
                break;
            case WALLSLIDE:
                region = wallSlideAnim.getKeyFrame(knight.getStateTimer(),true);
                break;
            case SHADOWPDASH:
                region = shadowDashAnim.getKeyFrame(knight.getStateTimer());
                break;
            default:
                region = knightIdle.getKeyFrame(knight.getStateTimer(),true);
        }

        if(knight.getFace() == 1 && !region.isFlipX()){
            region.flip(true,false);
        }else if(knight.getFace() == -1 && region.isFlipX()){
            region.flip(true,false);
        }

        knight.setRegion(region);
        knight.setSize(region.getRegionWidth() / PPM, region.getRegionHeight() / PPM);

        knight.setStateTimer(knight.currentState == knight.previousState ? knight.getStateTimer() + dt : 0);
        knight.previousState = knight.currentState;
    }

    public void draw(SpriteBatch batch, Knight knight) {
        if (knight.isDead()) return;
        if (knight.getNailEffect().isPlaying()) {
            knight.getNailEffect().draw(batch);
        }
        if (knight.getDashEffect().isPlaying()) {
            knight.getDashEffect().draw(batch);
        }
        knight.drawSelf(batch);
    }
}
