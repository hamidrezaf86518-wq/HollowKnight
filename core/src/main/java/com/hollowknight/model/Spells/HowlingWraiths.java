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


public class HowlingWraiths extends Sprite {

    private World world;
    private Body body;
    private HollowKnight game;

    private Animation<TextureRegion> anim;
    private float stateTimer = 0f;
    private boolean destroyed = false;

    private final int damage;
    private int tickCount = 0;
    private float tickTimer = 0f;
    private static final float TICK_INTERVAL = 0.15f;
    private static final int MAX_TICKS = 3;

    private static final String SPRITE_PATH = "textures/SoulScream.png";
    private static final int FRAME_COUNT = 13;

    public HowlingWraiths(HollowKnight game, World world, float knightX, float knightY, boolean voidHeart) {
        this.game = game;
        this.world = world;
        this.damage = voidHeart ? 120 : 80;

        Texture sheet = game.assetSystem.get(SPRITE_PATH, Texture.class);
        TextureRegion[] split = TextureRegion.split(sheet, sheet.getWidth() / FRAME_COUNT, sheet.getHeight())[0];

        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < FRAME_COUNT; i++) {
            frames.add(split[i]);
        }

        anim = new Animation<>(0.09f, frames, Animation.PlayMode.NORMAL);

        TextureRegion firstFrame = anim.getKeyFrame(0);
        setRegion(firstFrame);

        float width = firstFrame.getRegionWidth() / PPM;
        float height = firstFrame.getRegionHeight() / PPM;
        setSize(width, height);

        
        float effectCenterX = knightX;
        float effectCenterY = knightY + 1.2f; 

        
        setPosition(effectCenterX - width / 2f, effectCenterY - height / 2f);

        defineBody(effectCenterX, effectCenterY);
    }

    private void defineBody(float centerX, float centerY) {
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.position.set(centerX, centerY);
        body = world.createBody(bDef);

        PolygonShape shape = new PolygonShape();

        
        shape.setAsBox(getWidth() * 0.35f, getHeight() * 0.45f);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.isSensor = true;
        fDef.filter.categoryBits = SPELL_BIT;
        fDef.filter.maskBits = ENEMY_BIT | FALSE_KNIGHT_BIT | MOSQUITO_BIT;

        body.createFixture(fDef).setUserData(this);
        body.setUserData(this);

        shape.dispose();
    }

    public void update(float dt) {
        stateTimer += dt;

        tickTimer += dt;
        if (tickTimer >= TICK_INTERVAL && tickCount < MAX_TICKS) {
            tickTimer = 0f;
            tickCount++;
        }

        setRegion(anim.getKeyFrame(stateTimer));

        if (anim.isAnimationFinished(stateTimer)) {
            destroyed = true;
        }
    }

    @Override
    public void draw(Batch batch) {
        if (!destroyed) {
            super.draw(batch);
        }
    }

    public boolean canDealDamage() {
        return tickCount > 0 && tickCount <= MAX_TICKS && !destroyed;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public Body getBody() {
        return body;
    }
}
