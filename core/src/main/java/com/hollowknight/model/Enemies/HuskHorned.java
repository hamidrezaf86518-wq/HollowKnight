package com.hollowknight.model.Enemies;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.hollowknight.HollowKnight;
import com.hollowknight.model.Knight;
import com.hollowknight.view.EntitiesView.HuskHornedView;
import com.hollowknight.view.screens.GameScreen;

import static com.hollowknight.model.Constants.*;

public class HuskHorned extends Enemy{
    public enum State{ATTACKANTICIPATE, ATTACK, WALK, IDLE, DEATHLAND}
    private float walkTimer;
    private float restTimer;
    private float attackTimer;
    private boolean isResting;
    private boolean isAttacking;
    private float attackStartTimer;
    private State currentState;
    private State previousState;
    private boolean deathHandled = false;
    private final HuskHornedView view;

    public HuskHorned(HollowKnight game, GameScreen screen, float x, float y) {
        super(game, screen, x, y);
        HP = 600;
        walkTimer = 0;
        restTimer = 0;
        face = -1;
        isResting = false;
        currentState = State.WALK;
        previousState = State.WALK;
        velocity = new Vector2(face * 1f, -2);
        view = new HuskHornedView(this);
    }

    @Override
    public void defineEnemy(float x, float y) {
        BodyDef bDef = new BodyDef();
        bDef.position.set(x,y);
        bDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(35/PPM);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.filter.categoryBits = ENEMY_BIT;
        fDef.filter.maskBits = GROUND_BIT |
            KNIGHT_BIT |
            NAIL_BIT |
            OBSTACLE_BIT |
            RIGHT_WALL_BIT |
            LEFT_WALL_BIT |
            SHADOW_DASH_BIT |
            SPELL_BIT;

        body.createFixture(fDef).setUserData(this);
        shape.dispose();
    }


    private float getDistance(){
        Knight knight = screen.knight;
        return knight.body.getPosition().x - body.getPosition().x;
    }


    private boolean startAttack(){
        float dx = getDistance();
        float dy = Math.abs(screen.knight.body.getPosition().y - body.getPosition().y);
        if(dy > 0.8f) return false;
        if(face == 1  && dx > 0 && dx < 3f) return true;
        if(face == -1 && dx < 0 && dx > -3f) return true;
        return false;
    }

    @Override
    public void update(float dt) {
        if(isDead){
            if(!deathHandled && body != null){
                body.setLinearVelocity(0, 0);
                Filter filter = new Filter();
                filter.categoryBits = ENEMY_BIT;
                filter.maskBits = GROUND_BIT | OBSTACLE_BIT;
                for(Fixture f : body.getFixtureList()) f.setFilterData(filter);
                if(currentState != State.DEATHLAND){
                    previousState = currentState;
                    currentState = State.DEATHLAND;
                    stateTimer = 0;
                }
                deathHandled = true;
            }
            view.updateFrame(this, dt);
            if(body != null) setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - 35/PPM);
            return;
        }

        setState(dt);
        view.updateFrame(this, dt);
        body.setLinearVelocity(velocity);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 35 /PPM);
    }

    @Override
    public void drawView(com.badlogic.gdx.graphics.g2d.Batch batch) {
        view.draw(batch, this);
    }

    private void setState(float dt){


        if(!isAttacking && (currentState == State.WALK)){

        }

        if(isAttacking){
            if(currentState == State.ATTACKANTICIPATE){
                attackStartTimer += dt;
                if(attackStartTimer >= 0.5f){
                    currentState = State.ATTACK;
                    attackStartTimer = 0;
                    attackTimer = 0;
                }
            } else if(currentState == State.ATTACK){

                attackTimer += dt;
                if(attackTimer >= 1.0f){
                    isAttacking = false;
                    attackTimer = 0;
                    currentState = State.IDLE;
                    isResting = true;
                    restTimer = 0;
                }
            }
            applyStateVelocity();
            return;
        }


        if(startAttack()){
            isAttacking = true;
            currentState = State.ATTACKANTICIPATE;
            attackStartTimer = 0;
            applyStateVelocity();
            return;
        }

        if(isResting){
            restTimer = Math.min(RESTTIME, restTimer + dt);
            if(restTimer >= RESTTIME){
                isResting = false;
                restTimer = 0;
                currentState = State.WALK;
            }
        } else if(currentState == State.WALK){
            walkTimer = Math.min(WALKTIME, walkTimer + dt);
            if(walkTimer >= WALKTIME){
                walkTimer = 0;
                currentState = State.IDLE;
                isResting = true;
            }
        }

        applyStateVelocity();
    }

    private void applyStateVelocity(){
        switch (currentState){
            case IDLE:
                velocity.set(0, 0);
                break;
            case WALK:
                velocity.set(face * 1f, -2);
                break;
            case ATTACKANTICIPATE:
                velocity.set(0, -2);
                break;
            case ATTACK:
                velocity.set(face * 4f, -2);
                break;
            default:
                velocity.set(0, -2);
        }
    }

    public State getCurrentState() { return currentState; }
    public State getPreviousState() { return previousState; }
    public void setPreviousState(State previousState) { this.previousState = previousState; }
}
