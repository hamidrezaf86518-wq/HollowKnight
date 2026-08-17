package com.hollowknight.model.Effects;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.hollowknight.HollowKnight;
import com.hollowknight.view.screens.GameScreen;

import static com.hollowknight.model.Constants.*;


public class ShockWave extends Sprite {
    private Body body;
    private GameScreen screen;
    private Animation<TextureRegion> waveAnim;
    private int face;
    private float timer;
    private boolean destroyed = false;   

    public ShockWave(GameScreen screen, HollowKnight game, float x, float y, int face){
        this.screen = screen;
        this.face = face;
        defineShockWave(x, y);
        timer = 0;

        Array<TextureRegion> keyFrames = new Array<>();
        for (int i = 0 ; i < 8 ; i++) {
            keyFrames.add(new TextureRegion(
                game.assetSystem.get("textures/falseknight/Shockwave_00" + i + ".png", Texture.class)));
        }
        waveAnim = new Animation<>(0.2f, keyFrames);

        TextureRegion firstFrame = waveAnim.getKeyFrame(0);
        setRegion(firstFrame);
        setBounds(x, y, firstFrame.getRegionWidth() / PPM, firstFrame.getRegionHeight() / PPM);

        if(face == -1) this.flip(true, false);
    }

    public void update(float dt){
        if(destroyed) return;

        if(waveAnim.isAnimationFinished(timer)){
            
            markDestroyed();
            return;
        }

        TextureRegion currentFrame = waveAnim.getKeyFrame(timer);
        if(face == -1 && !currentFrame.isFlipX()){
            currentFrame.flip(true, false);
        } else if (face == 1 && currentFrame.isFlipX()){
            currentFrame.flip(true, false);
        }

        timer += dt;
        if(body != null) body.setLinearVelocity(face * 2f, 0);
        if(body != null) setPosition(body.getPosition().x - 50/PPM, body.getPosition().y - 15/PPM);
        setRegion(currentFrame);
    }

    
    private void markDestroyed(){
        if(destroyed) return;
        destroyed = true;
        if(body != null){
            
            
            screen.addBodyToDestroy(body);
            body = null;
        }
    }

    public void draw(com.badlogic.gdx.graphics.g2d.Batch batch){
        if(!destroyed) super.draw(batch);
    }

    private void defineShockWave(float x, float y){
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(x, y);
        bDef.gravityScale = 0;
        body = screen.getWorld().createBody(bDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(100/PPM, 30/PPM);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.filter.categoryBits = SHOCK_WAVE_BIT;
        fDef.filter.maskBits = KNIGHT_BIT |
            GROUND_BIT |
            BOSS_GROUND_BIT |
            OBSTACLE_BIT;
        fDef.isSensor = true;

        body.createFixture(fDef).setUserData(this);
        shape.dispose();
    }

    public boolean isDestroyed() { return destroyed; }

    
    public void destroy(){
        markDestroyed();
    }

    public Body getBody() { return body; }
    public int getFace() { return face; }
}
