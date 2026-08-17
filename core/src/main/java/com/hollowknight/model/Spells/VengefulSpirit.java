package com.hollowknight.model.Spells;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.hollowknight.HollowKnight;

import static com.hollowknight.model.Constants.*;


public class VengefulSpirit extends Sprite {

    private World world;
    private Body body;
    private HollowKnight game;

    private Animation<TextureRegion> flyAnim;
    private float stateTimer = 0f;
    private int face;
    private boolean destroyed = false;

    private final int damage;
    private final float speed = 6f;

    
    private static final String SPRITE_PATH = "textures/SoulBall.png";
    private static final int FRAME_COUNT = 4;

    public VengefulSpirit(HollowKnight game, World world, float x, float y, int face, boolean voidHeart) {
        this.game = game;
        this.world = world;
        this.face = face;
        this.damage = voidHeart ? 300 : 200; 

        Texture sheet = game.assetSystem.get(SPRITE_PATH, Texture.class);
        TextureRegion[] split = TextureRegion.split(sheet, sheet.getWidth()/FRAME_COUNT, sheet.getHeight())[0];
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < FRAME_COUNT; i++) frames.add(split[i]);
        flyAnim = new Animation<>(0.08f, frames, Animation.PlayMode.LOOP);

        setRegion(flyAnim.getKeyFrame(0));
        setBounds(x, y, split[0].getRegionWidth()/PPM, split[0].getRegionHeight()/PPM);

        defineBody(x, y);
    }

    private void defineBody(float x, float y) {
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.KinematicBody; 
        bDef.position.set(x, y);
        body = world.createBody(bDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(30/PPM, 15/PPM);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.isSensor = true; 
        fDef.filter.categoryBits = SPELL_BIT;
        fDef.filter.maskBits = ENEMY_BIT | FALSE_KNIGHT_BIT | MOSQUITO_BIT |
            RIGHT_WALL_BIT | LEFT_WALL_BIT | OBSTACLE_BIT;

        body.createFixture(fDef).setUserData(this);
        body.setUserData(this);
        body.setLinearVelocity(face * speed, 0);
        shape.dispose();
    }

    public void update(float dt) {
        stateTimer += dt;
        if (body != null) {
            setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight()/2);
        }

        TextureRegion frame = flyAnim.getKeyFrame(stateTimer);
        if (face == -1 && !frame.isFlipX()) frame.flip(true, false);
        else if (face == 1 && frame.isFlipX()) frame.flip(true, false);
        setRegion(frame);
    }

    public void draw(Batch batch) {
        if (!destroyed) super.draw(batch);
    }

    public int getDamage() { return damage; }

    public boolean isDestroyed() { return destroyed; }

    public void setDestroyed(boolean destroyed) { this.destroyed = destroyed; }

    public Body getBody() { return body; }

}
