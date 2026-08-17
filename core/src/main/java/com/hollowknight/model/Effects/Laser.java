package com.hollowknight.model.Effects;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.hollowknight.HollowKnight;

import static com.hollowknight.model.Constants.*;


public class Laser extends Sprite{
    private HollowKnight game;
    private World world;
    private Body body;
    private boolean destroyed = false;
    private boolean collidWithKnight;
    private float lifeTime = 0f;
    private int direction;
    private float stateTimer;

    private Animation<TextureRegion> laserAnim;

    public Laser(HollowKnight game, World world, int direction, float x, float y){
        this.game = game;
        this.world = world;
        this.direction = direction;
        stateTimer = 0;
        collidWithKnight = false;
        defineLaser(x,y);

        Texture spriteSheet = game.assetSystem.get("textures/CrystalGuardian/LaserCircle.png", Texture.class);
        TextureRegion[] frames = TextureRegion.split(spriteSheet,spriteSheet.getWidth()/11,spriteSheet.getHeight())[0];
        Array<TextureRegion> keyFrames = new Array<>();
        for (int i = 0 ;  i < 11 ; i++){
            keyFrames.add(frames[i]);
        }
        laserAnim = new Animation<>(0.1f,keyFrames);

        setRegion(laserAnim.getKeyFrame(0));
        setBounds(getX(),getY(),laserAnim.getKeyFrame(0).getRegionWidth()/PPM,laserAnim.getKeyFrame(0).getRegionHeight()/PPM);
    }

    public void update(float dt){
        if(destroyed){
            world.destroyBody(body);
            return;
        }

        TextureRegion currentFrame = laserAnim.getKeyFrame(stateTimer);
        stateTimer += dt;
        body.setLinearVelocity(direction*5f,0);
        setPosition(body.getPosition().x-25/PPM,body.getPosition().y-25/PPM);
        setRegion(currentFrame);
    }

    private void defineLaser(float x, float y){
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(x,y+0.6f);

        body = world.createBody(bDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(25/PPM);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.isSensor = true;
        fDef.filter.categoryBits = LASER_BIT;
        fDef.filter.maskBits = KNIGHT_BIT |
            RIGHT_WALL_BIT |
            LEFT_WALL_BIT |
            OBSTACLE_BIT;

        body.createFixture(fDef).setUserData(this);
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean isCollidWithKnight() {
        return collidWithKnight;
    }

    public void setCollidWithKnight(boolean collidWithKnight) {
        this.collidWithKnight = collidWithKnight;
    }
}
