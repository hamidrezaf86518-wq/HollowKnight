package com.hollowknight.view.Scenes;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.hollowknight.HollowKnight;


public class MaskActor extends Actor {
    private enum State{ FULL, BREAKING, EMPTY, FILLING};
    private float stateTimer;
    private State currentState;
    private HollowKnight game;


    private TextureRegion emptyMask;
    private Animation<TextureRegion> fullMask;
    private Animation<TextureRegion> breakAnim;
    private Animation<TextureRegion> refillAnim;

    public MaskActor(HollowKnight game){
        this.game = game;
        stateTimer = 0;
        currentState = State.FULL;

        emptyMask = new TextureRegion(game.assetSystem.get("textures/HUD/EmptyHealth.png", Texture.class));

        Array<TextureRegion> keyFrames = getKeyFrames(6,"textures/HUD/BreakHealth.png");
        breakAnim = new Animation<>(0.1f,keyFrames);

        keyFrames = getKeyFrames(5,"textures/HUD/HealthRefill.png");
        refillAnim = new Animation<>(0.1f,keyFrames);

        keyFrames = getKeyFrames(5,"textures/HUD/FilledHealthShine.png");
        fullMask = new Animation<>(0.2f,keyFrames);

        setSize(fullMask.getKeyFrame(0).getRegionWidth(), fullMask.getKeyFrame(0).getRegionHeight());
    }

    private Array<TextureRegion> getKeyFrames(int amount, String path){
        Array<TextureRegion> keyFrames = new Array<>();
        Texture spriteSheet = game.assetSystem.get(path, Texture.class);
        TextureRegion[] frames = TextureRegion.split(spriteSheet,spriteSheet.getWidth()/amount,spriteSheet.getHeight())[0];

        for(int i = 0 ; i < amount ; i++){
            keyFrames.add(frames[i]);
        }

        return keyFrames;
    }
    
    public void breakMask() {
        if (currentState == State.FULL) {
            currentState = State.BREAKING;
            stateTimer = 0f;
        }
    }

    
    public void fillMask() {
        if (currentState == State.EMPTY) {
            currentState = State.FILLING;
            stateTimer = 0f;
        }
    }

    
    public void resetToFull() {
        currentState = State.FULL;
        stateTimer = 0f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTimer += delta;

        
        if (currentState == State.BREAKING && breakAnim.isAnimationFinished(stateTimer)) {
            currentState = State.EMPTY;
        }

        
        if (currentState == State.FILLING && refillAnim.isAnimationFinished(stateTimer)) {
            currentState = State.FULL;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion currentFrame;

        switch (currentState) {
            case State.BREAKING:
                currentFrame = breakAnim.getKeyFrame(stateTimer);
                break;
            case State.FILLING:
                currentFrame = refillAnim.getKeyFrame(stateTimer);
                break;
            case State.EMPTY:
                currentFrame = emptyMask;
                break;
            case State.FULL:
            default:
                currentFrame = fullMask.getKeyFrame(stateTimer,true);
                break;
        }

        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
    }
}

