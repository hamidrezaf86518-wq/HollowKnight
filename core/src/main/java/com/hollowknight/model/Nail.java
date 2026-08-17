package com.hollowknight.model;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static com.hollowknight.model.Constants.*;


public class Nail {
    private Body nailBody;
    private World world;
    private Knight knight;

    private boolean isAttacking;
    private float attackTimer;
    private float attackDuration = 0.2f;
    private float attackCooldown = 0.3f;
    private float lastAttackTime = 0;
    private Array<Object> hitEnemies = new Array<>();
    private float nailWidth = 85f;
    private float nailHeight = 15f;
    private float offsetX = 20f;
    private float offsetY = 5f;
    private FixtureDef fDef = new FixtureDef();

    public Nail(World world, Knight knight) {
        this.world = world;
        this.knight = knight;
        createNailBody();
    }

    public Knight getKnight() {
        return knight;
    }

    private void createNailBody() {
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(0, 0);




        PolygonShape shape = new PolygonShape();
        float halfWidth = (nailWidth / PPM) / 2f;
        float halfHeight = (nailHeight / PPM) / 2f;
        shape.setAsBox(halfWidth, halfHeight);

        fDef.shape = shape;
        fDef.filter.categoryBits = NAIL_BIT;
        fDef.filter.maskBits = ENEMY_BIT |
            SPIKE_BIT;
        fDef.isSensor = true;

        nailBody = world.createBody(bDef);
        nailBody.setActive(false);
        nailBody.createFixture(fDef);
        nailBody.setUserData(this);

    }


    public void updateNailPosition() {
        if (knight == null || knight.body == null) return;

        Vector2 knightPos = knight.body.getPosition();
        int face = knight.getFace();

        float x = 0;
        float y = 0;

        if(face == 1 || face == -1) {
            clearFixtures();
            PolygonShape shape = new PolygonShape();
            float halfWidth = (nailWidth / PPM) / 2f;
            float halfHeight = (nailHeight / PPM) / 2f;
            shape.setAsBox(halfWidth, halfHeight);

            fDef.shape = shape;
            fDef.filter.categoryBits = NAIL_BIT;
            fDef.filter.maskBits = ENEMY_BIT |
                FALSE_KNIGHT_BIT |
                SPIKE_BIT;
            fDef.isSensor = true;
            nailBody.createFixture(fDef);
            x = knightPos.x + (face * (offsetX + nailWidth / 2) / PPM);
            y = knightPos.y + (offsetY / PPM);
        } else {
            clearFixtures();
            PolygonShape shape = new PolygonShape();
            float halfWidth = (nailHeight / PPM) / 2f;
            float halfHeight = (nailWidth / PPM) / 2f;
            shape.setAsBox(halfWidth, halfHeight);
            fDef.shape = shape;
            fDef.filter.categoryBits = NAIL_BIT;
            
            
            fDef.filter.maskBits = ENEMY_BIT |
                FALSE_KNIGHT_BIT |
                SPIKE_BIT;
            fDef.isSensor = true;
            nailBody.createFixture(fDef);
            if (face == 2) {
                x = knightPos.x + (offsetY / PPM);
                y = knightPos.y + (offsetX + nailWidth / 2) / PPM;
            } else if (face == -2) {
                x = knightPos.x + (offsetY / PPM);
                y = knightPos.y - (offsetX + nailWidth / 2) / PPM;
            }
        }

        nailBody.setTransform(new Vector2(x, y), 0);
    }

    private void clearFixtures() {
        if (nailBody.getFixtureList() != null) {
            Array<Fixture> fixtures = new Array<>();
            for (Fixture f : nailBody.getFixtureList()) {
                fixtures.add(f);
            }
            for (Fixture f : fixtures) {
                nailBody.destroyFixture(f);
            }
        }
    }

    public Body getNailBody() {
        return nailBody;
    }
}
