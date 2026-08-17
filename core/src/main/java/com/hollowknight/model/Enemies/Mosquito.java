package com.hollowknight.model.Enemies;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.hollowknight.HollowKnight;
import com.hollowknight.model.Knight;
import com.hollowknight.view.EntitiesView.MosquitoView;
import com.hollowknight.view.screens.GameScreen;

import static com.hollowknight.model.Constants.*;


public class Mosquito extends Enemy {
    public enum State { IDLE, DEATHAIR, DEATHLAND, ATTACK, ATTACKANTICIPATE }
    private State previousState;
    private State currentState;
    private boolean isAttacking;
    private float attackStartTimer;
    private float attackTimer;
    private boolean startAttack;
    private boolean hasSpottedKnight;
    private float targetX = 0;
    private float targetY = 0;


    private boolean landed = false;
    private boolean deathStarted = false;

    private static final float CHASE_SPEED = 2.5f;

    private final MosquitoView view;

    public Mosquito(HollowKnight game, GameScreen screen, float x, float y) {
        super(game, screen, x, y);
        face = -1;
        HP = 500;
        currentState = State.IDLE;
        previousState = State.IDLE;
        velocity = new Vector2();
        view = new MosquitoView(this);
    }

    @Override
    public void defineEnemy(float x, float y) {
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(x, y);
        bDef.gravityScale = 0;

        CircleShape shape = new CircleShape();
        shape.setRadius(55 / PPM);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.filter.categoryBits = ENEMY_BIT;
        fDef.filter.maskBits = GROUND_BIT |
            KNIGHT_BIT |
            NAIL_BIT |
            OBSTACLE_BIT |
            SHADOW_DASH_BIT |
            RIGHT_WALL_BIT |
            LEFT_WALL_BIT |
            SPELL_BIT;

        body = world.createBody(bDef);
        body.createFixture(fDef).setUserData(this);
        shape.dispose();

        CircleShape shape1 = new CircleShape();
        shape1.setRadius(300 / PPM);
        FixtureDef fDef1 = new FixtureDef();
        fDef1.shape = shape1;
        fDef1.filter.categoryBits = MOSQUITO_BIT;
        fDef1.filter.maskBits = KNIGHT_BIT;
        fDef1.isSensor = true;
        body.createFixture(fDef1).setUserData(this);
        shape1.dispose();

        body.setUserData(this);
    }

    private void setState(float dt) {
        if (isAttacking) {
            if (currentState == State.ATTACKANTICIPATE) {
                attackStartTimer += dt;
                velocity.set(0, 0);
                if (attackStartTimer >= 0.5f) {
                    currentState = State.ATTACK;
                    attackTimer = 0;
                    float dx = targetX - body.getPosition().x;
                    float dy = targetY - body.getPosition().y;
                    Vector2 dir = new Vector2(dx, dy);
                    if (dir.len2() > 0.0001f) dir.nor();
                    velocity = dir.scl(MOSQUITO_CHARGE_SPEED);
                    face = dx >= 0 ? 1 : -1;
                }
            } else if (currentState == State.ATTACK) {
                attackTimer += dt;
                if (attackTimer >= 5f) {
                    isAttacking = false;
                    currentState = State.IDLE;
                }
            }
            return;
        }

        if (startAttack) {
            isAttacking = true;
            hasSpottedKnight = true;
            attackStartTimer = 0;
            currentState = State.ATTACKANTICIPATE;
            velocity.set(0, 0);
            startAttack = false;
            return;
        }

        if (hasSpottedKnight) {
            Knight knight = screen.knight;
            float dx = knight.body.getPosition().x - body.getPosition().x;
            float dy = knight.body.getPosition().y - body.getPosition().y;
            Vector2 dir = new Vector2(dx, dy);
            if (dir.len2() > 0.0001f) dir.nor();
            velocity = dir.scl(CHASE_SPEED);
            face = dx >= 0 ? 1 : -1;
            currentState = State.IDLE;
        } else {
            velocity.set(0, 0);
        }
    }

    public void setStartAttack(boolean startAttack, float x, float y) {
        this.startAttack = startAttack;
        this.hasSpottedKnight = true;
        targetX = x;
        targetY = y;
    }

    @Override
    public void update(float dt) {
        if (isDead) {
            handleDeath(dt);
            return;
        }
        setState(dt);
        view.updateFrame(this, dt);
        body.setLinearVelocity(velocity);
        setPosition(body.getPosition().x - 55 / PPM, body.getPosition().y - 55 / PPM);
    }


    private void handleDeath(float dt) {
        if (!deathStarted) {
            deathStarted = true;
            previousState = currentState;
            currentState = State.DEATHAIR;
            setStateTimer(0);

            if (body != null) {
                body.setGravityScale(1f);
                body.setLinearVelocity(0, 0);

                Filter filter = new Filter();
                filter.categoryBits = ENEMY_BIT;
                filter.maskBits = GROUND_BIT | OBSTACLE_BIT;
                for (Fixture f : body.getFixtureList()) f.setFilterData(filter);
            }
        }


        if (!landed && body != null) {
            float vy = body.getLinearVelocity().y;
            if (currentState == State.DEATHAIR && Math.abs(vy) < 0.1f && getStateTimer() > 0.15f) {
                landed = true;
                previousState = currentState;
                currentState = State.DEATHLAND;
                setStateTimer(0);
                body.setLinearVelocity(0, 0);
                body.setType(BodyDef.BodyType.StaticBody);
            }
        }

        view.updateFrame(this, dt);
        if (body != null)
            setPosition(body.getPosition().x - 55 / PPM, body.getPosition().y - 55 / PPM);
    }

    @Override
    public void drawView(com.badlogic.gdx.graphics.g2d.Batch batch) {
        view.draw(batch, this);
    }

    public State getPreviousState() { return previousState; }
    public void setPreviousState(State previousState) { this.previousState = previousState; }
    public State getCurrentState() { return currentState; }
    public void setCurrentState(State currentState) { this.currentState = currentState; }
    public boolean isAttacking() { return isAttacking; }

    public void stopAttacking() {
        isAttacking = false;
        velocity.set(0, 0);
        currentState = State.IDLE;
        previousState = State.ATTACK;
    }

    public float getAttackStartTimer() { return attackStartTimer; }
    public void setAttackStartTimer(float attackStartTimer) { this.attackStartTimer = attackStartTimer; }
    public float getAttackTimer() { return attackTimer; }
    public void setAttackTimer(float attackTimer) { this.attackTimer = attackTimer; }
    public boolean isStartAttack() { return startAttack; }
    public boolean isLanded() { return landed; }
}
