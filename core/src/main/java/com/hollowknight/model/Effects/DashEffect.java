package com.hollowknight.model.Effects;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.hollowknight.HollowKnight;

import static com.hollowknight.model.Constants.PPM;


public class DashEffect extends Sprite {
    
    private Animation<TextureRegion> dashEffectAnimation;
    private float stateTimer = 0;

    
    private boolean isPlaying = false;
    private float duration;  
    private int face;

    
    private Vector2 position;
    private float offsetX = 0;
    private float offsetY = 0;

    public DashEffect(HollowKnight game) {
        
        Array<TextureRegion> keyFrames = getKeyFrames(game, "textures/Knight/Dash Effect.png", 7);
        dashEffectAnimation = new Animation<>(0.1f, keyFrames);

        
        TextureRegion firstFrame = dashEffectAnimation.getKeyFrame(0);
        setRegion(firstFrame);
        setSize(
            firstFrame.getRegionWidth() / PPM,
            firstFrame.getRegionHeight() / PPM
        );

        
        duration = 7 * 0.1f;

        
        setAlpha(0.8f);
    }

    
    private Array<TextureRegion> getKeyFrames(HollowKnight game, String path, int amount) {
        Texture spriteSheet = game.assetSystem.get(path, Texture.class);
        TextureRegion[] split = TextureRegion.split(
            spriteSheet,
            spriteSheet.getWidth() / amount,
            spriteSheet.getHeight()
        )[0];

        Array<TextureRegion> keyFrames = new Array<>();
        for (int i = 0; i < amount; i++) {
            keyFrames.add(split[i]);
        }
        return keyFrames;
    }

    
    public void start(Vector2 position, int face) {
        this.position = position;
        this.face = face;
        this.isPlaying = true;
        this.stateTimer = 0;

        
        updatePosition();
    }

    
    private void updatePosition() {
        float x = position.x + (face * offsetX / PPM);
        float y = position.y + (offsetY / PPM);

        setPosition(
            x - getWidth() / 2,
            y - getHeight() / 2
        );
    }

    
    public void update(float dt) {
        if (!isPlaying) return;

        stateTimer += dt;

        
        TextureRegion region = dashEffectAnimation.getKeyFrame(stateTimer, false);
        setRegion(region);

        
        if (face == 1 && region.isFlipX()) {
            region.flip(true, false);
        } else if (face == -1 && !region.isFlipX()) {
            region.flip(true, false);
        }


        
        if (dashEffectAnimation.isAnimationFinished(stateTimer)) {
            isPlaying = false;
        }
    }

    
    public void draw(com.badlogic.gdx.graphics.g2d.Batch batch) {
        if (isPlaying) {
            super.draw(batch);
        }
    }

    
    public boolean isPlaying() {
        return isPlaying;
    }

    public void setOffset(float x, float y) {
        this.offsetX = x;
        this.offsetY = y;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }
}
