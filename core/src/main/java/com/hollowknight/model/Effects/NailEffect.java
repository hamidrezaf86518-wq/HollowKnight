package com.hollowknight.model.Effects;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.hollowknight.HollowKnight;
import com.hollowknight.model.Knight;

import static com.hollowknight.model.Constants.PPM;


public class NailEffect extends Sprite {
    private enum State{UP, DOWN, RIGHT, LEFT}
    private State currentState;
    private HollowKnight game;
    private Animation<TextureRegion> upSlashAnimation;
    private Animation<TextureRegion> downSlashAnimation;
    private Animation<TextureRegion> horizontalSlashAnimation;
    private float stateTimer = 0;
    private Knight knight;


    private boolean isPlaying = false;
    private float duration;
    private float offsetX = 0;
    private float offsetY = 0;

    private Vector2 position;

    public NailEffect(Knight knight,HollowKnight game) {
        this.game = game;
        this.knight = knight;
        duration = 0.6f;
        currentState = State.RIGHT;

        
        Array<TextureRegion> keyFrames = getKeyFrames("textures/Knight/UpSlashEffect.png",6);
        upSlashAnimation = new Animation<>(Knight.ATTACK_TIMER/6,keyFrames);

        
        keyFrames = getKeyFrames("textures/Knight/DownSlashEffect.png",6);
        downSlashAnimation = new Animation<>(Knight.ATTACK_TIMER/6,keyFrames);

        
        keyFrames = getKeyFrames("textures/Knight/SlashEffect.png",6);
        horizontalSlashAnimation = new Animation<>(Knight.ATTACK_TIMER/6,keyFrames);

        TextureRegion firstFrame = horizontalSlashAnimation.getKeyFrame(0);
        setRegion(firstFrame);
        setSize(
            firstFrame.getRegionWidth() / PPM,
            firstFrame.getRegionHeight() / PPM
        );

        setAlpha(0.8f);

    }
    private void updatePosition() {
        float x = 0;
        float y = 0;

        switch (knight.getFace()){
            case 1:
                x = position.x + (40 / PPM);
                y = position.y;
                break;
            case -1:
                x = position.x - (40 / PPM);
                y = position.y;
                break;
            case 2:
                x = position.x;
                y = position.y + (40/PPM);
                break;
            case -2:
                x = position.x;
                y = position.y - (40/PPM);
                break;
        }

        setPosition(
            x - getWidth() / 2,
            y - getHeight() / 2
        );
    }
    public void start(Vector2 position) {
        this.position = position;
        this.isPlaying = true;
        this.stateTimer = 0;

        switch (knight.getFace()){
            case 1:
                currentState = State.RIGHT;
                break;
            case -1:
                currentState = State.LEFT;
                break;
            case 2:
                currentState = State.UP;
                break;
            case -2:
                currentState = State.DOWN;
                break;
        }

        updatePosition();

    }

    public void update(float dt) {
        if (!isPlaying) return;

        updatePosition();
        stateTimer += dt;

        Animation<TextureRegion> currentAnimation = null;
        TextureRegion region = null;
        switch (currentState){
            case UP:
                currentAnimation = upSlashAnimation;
                region = upSlashAnimation.getKeyFrame(stateTimer);
                break;
            case DOWN:
                currentAnimation = downSlashAnimation;
                region = downSlashAnimation.getKeyFrame(stateTimer);
                break;
            case RIGHT:
                currentAnimation = horizontalSlashAnimation;
                region = horizontalSlashAnimation.getKeyFrame(stateTimer,true);
                break;
            case LEFT:
                currentAnimation = horizontalSlashAnimation;
                region = horizontalSlashAnimation.getKeyFrame(stateTimer);
                break;
        }


        
        if (knight.getFace() == 1 && !region.isFlipX()) {
            region.flip(true, false);
        } else if (knight.getFace() == -1 && region.isFlipX()) {
            region.flip(true, false);
        }

        setRegion(region);
        setSize(region.getRegionWidth()/PPM, region.getRegionHeight()/PPM);

        if (currentAnimation.isAnimationFinished(stateTimer)) {
            isPlaying = false;
        }
    }

    private Array<TextureRegion> getKeyFrames(String path, int amount){
        Texture spriteSheet = game.assetSystem.get(path,Texture.class);
        TextureRegion[] split = TextureRegion.split(spriteSheet,spriteSheet.getWidth()/amount,spriteSheet.getHeight())[0];
        Array<TextureRegion> keyFrames = new Array<>();

        for (int i = 0 ; i < amount ; i++){
            keyFrames.add(split[i]);
        }

        return keyFrames;
    }

    public void draw(com.badlogic.gdx.graphics.g2d.Batch batch) {
        if (isPlaying) {
            super.draw(batch);
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
