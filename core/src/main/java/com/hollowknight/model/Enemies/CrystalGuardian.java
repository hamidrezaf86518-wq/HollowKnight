package com.hollowknight.model.Enemies;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.hollowknight.HollowKnight;
import com.hollowknight.model.Effects.Laser;
import com.hollowknight.model.Knight;
import com.hollowknight.view.EntitiesView.CrystalGuardianView;
import com.hollowknight.view.screens.GameScreen;

import static com.hollowknight.model.Constants.*;


public class CrystalGuardian extends Enemy {
    public enum State { IDLE, RUN, SHOOT, EVADE, DEATHLAND }
    private State currentState;
    private State previousState;

    private float shootTimer;
    private float angryTimer;
    private boolean laserAttacked;
    private Laser laser;

    private boolean deathHandled = false;

    private final CrystalGuardianView view;

    public CrystalGuardian(HollowKnight game, GameScreen screen, float x, float y) {
        super(game, screen, x, y);
        HP = 800;
        previousState = State.IDLE;
        currentState = State.IDLE;
        velocity = new Vector2();
        face = 1;
        view = new CrystalGuardianView(this);
    }

    private boolean isInSight() {
        Knight knight = screen.knight;
        float dx = body.getPosition().x - knight.body.getPosition().x;
        if (face == -1 && dx > -1 && Math.abs(body.getPosition().y - knight.body.getPosition().y) < 0.5) {
            return true;
        }
        if (face == 1 && dx < 1 && Math.abs(body.getPosition().y - knight.body.getPosition().y) < 0.5) {
            return true;
        }
        return false;
    }

    private void setState(float dt) {
        if (isInSight() && shootTimer == 0 && currentState != State.RUN) {
            previousState = currentState;
            currentState = State.SHOOT;
            laserAttacked = false;
        }

        if (currentState == State.SHOOT) {
            shootTimer = Math.min(1f, shootTimer + dt);
            if (!laserAttacked) {
                laserAttack();
                laserAttacked = true;
            }
            if (shootTimer == 1f) {
                previousState = currentState;
                currentState = State.RUN;
                velocity.set(face * 2f, 0);
                shootTimer = 0;
                angryTimer = 0;
            }
        }

        if (currentState == State.RUN) {
            angryTimer = Math.min(4, angryTimer + dt);
            if (angryTimer == 4) {
                previousState = currentState;
                currentState = State.IDLE;
                velocity.set(0, 0);
            }
        }
    }

    private void laserAttack() {
        laser = new Laser(game, world, face, body.getPosition().x, body.getPosition().y);
    }

    @Override
    public void defineEnemy(float x, float y) {
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(x, y);

        CircleShape shape = new CircleShape();
        shape.setRadius(35 / PPM);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.filter.categoryBits = ENEMY_BIT;
        fDef.filter.maskBits = KNIGHT_BIT |
            GROUND_BIT |
            OBSTACLE_BIT |
            NAIL_BIT |
            RIGHT_WALL_BIT |
            LEFT_WALL_BIT |
            SHADOW_DASH_BIT |
            SPELL_BIT;

        body = screen.getWorld().createBody(bDef);
        body.createFixture(fDef).setUserData(this);
        shape.dispose();
    }

    @Override
    public void update(float dt) {
        if (isDead) {


            if (!deathHandled) {
                deathHandled = true;
                if (currentState != State.DEATHLAND) {
                    previousState = currentState;
                    currentState = State.DEATHLAND;
                    setStateTimer(0);
                }
                if (body != null) {
                    body.setLinearVelocity(0, 0);

                    Filter filter = new Filter();
                    filter.categoryBits = ENEMY_BIT;
                    filter.maskBits = GROUND_BIT | OBSTACLE_BIT;
                    for (Fixture f : body.getFixtureList()) f.setFilterData(filter);
                }
            }


            if (laser != null) {
                laser.update(dt);
                if (laser.isDestroyed()) laser = null;
            }


            view.updateFrame(this, dt);

            if (body != null)
                setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 35 / PPM);
            return;
        }

        if (laser != null) {
            laser.update(dt);
            if (laser.isDestroyed()) {
                laser = null;
            }
        }
        setState(dt);
        view.updateFrame(this, dt);
        body.setLinearVelocity(velocity);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 35 / PPM);
    }

    @Override
    public void drawView(Batch batch) {
        view.draw(batch, this);
    }

    public Laser getLaser() { return laser; }
    public void setLaser(Laser laser) { this.laser = laser; }
    public State getCurrentState() { return currentState; }
    public State getPreviousState() { return previousState; }
    public void setPreviousState(State previousState) { this.previousState = previousState; }
}
