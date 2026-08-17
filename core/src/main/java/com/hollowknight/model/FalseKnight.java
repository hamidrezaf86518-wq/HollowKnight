package com.hollowknight.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.hollowknight.HollowKnight;
import com.hollowknight.model.Effects.ShockWave;
import com.hollowknight.view.EntitiesView.FalseKnightView;
import com.hollowknight.view.screens.GameScreen;

import java.util.Random;

import static com.hollowknight.model.Constants.*;


public class FalseKnight extends Sprite {
    public enum State{MACESLAMANTI, MACESLAM, MACESLAMRECOV, DEATHLAND, STUN,
        CHARGERUN, OFFENSIVELEAPANTI, OFFENSIVELEAP, DEFENSIVELEAPANTI, DEFENSIVELEAP,
        POWERFULHIT, IDLE, STUNRECOV, CHARGERUNANTI}

    private Body body;
    private int HP;
    private State currentState;
    private State previousState;
    private HollowKnight game;
    private Random random;
    private GameScreen screen;
    private int face;
    private ShockWave wave;
    private boolean maceHitApplied = false;

    private boolean isActive = false;
    private boolean isPhase2 = false;
    private boolean waveSpawned = false;
    private boolean isDead = false;
    private boolean deathHandled = false;

    private final float spawnX;
    private final float spawnY;

    private float maceSlamAntiTimer;
    private float maceSlamTimer;
    private float maceSlamRecovTimer;
    private float offensiveLeapAntiTimer;
    private float offensiveLeapTimer;
    private float powerfulHitTimer;
    private float idleTimer;
    private float chargeRunAntiTimer;
    private float stunTimer;
    private float deathLandTimer;
    private float stateTimer = 0;


    private State lastAttackMove = null;


    private int recentHitsCount = 0;
    private float recentHitsWindow = 0f;
    private static final float RECENT_HIT_WINDOW_TIME = 2.0f;
    private static final int RECENT_HIT_THRESHOLD = 2;


    private static final float STUN_DAMAGE_MULTIPLIER = 1.75f;


    private static final float PHASE2_SPEED_MULTIPLIER = 1.4f;
    private static final float PHASE2_TIME_SCALE = 0.7f;

    private final FalseKnightView view;

    public FalseKnight(HollowKnight game, GameScreen screen, float x, float y){
        this.game = game;
        this.screen = screen;
        this.spawnX = x;
        this.spawnY = y;
        HP = 5000;
        face = -1;
        currentState = State.IDLE;
        previousState = State.IDLE;
        random = new Random();

        defineFalseKnight(x,y);
        view = new FalseKnightView(this);
    }

    public void update(float delta){
        if(isDead){
            handleDeath(delta);
            return;
        }


        if(recentHitsWindow > 0){
            recentHitsWindow -= delta;
            if(recentHitsWindow <= 0){
                recentHitsWindow = 0;
                recentHitsCount = 0;
            }
        }

        setFace();
        setState(delta);
        view.updateFrame(this, delta);
        if(wave != null) wave.update(delta);
        setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - 220/PPM);
    }

    private void handleDeath(float delta){
        if(!deathHandled){
            deathHandled = true;
            deathLandTimer = 0;

            if(currentState != State.DEATHLAND){
                previousState = currentState;
                currentState = State.DEATHLAND;
                stateTimer = 0;
            }

            if(body != null){
                body.setLinearVelocity(0, 0);
                body.setType(BodyDef.BodyType.StaticBody);
                Filter filter = new Filter();
                filter.categoryBits = FALSE_KNIGHT_BIT;
                filter.maskBits = GROUND_BIT | BOSS_GROUND_BIT | OBSTACLE_BIT;
                for(Fixture f : body.getFixtureList()) f.setFilterData(filter);
            }

            screen.onBossDefeated();
        }

        deathLandTimer += delta;
        view.updateFrame(this, delta);
        if(wave != null){
            wave.update(delta);
            if(wave.isDestroyed()) wave = null;
        }
        if(body != null)
            setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - 220/PPM);
    }

    private float getDistance(){
        Knight knight = ((GameScreen)game.getScreen()).knight;
        return Math.abs(knight.body.getPosition().x - body.getPosition().x);
    }


    private float speedMultiplier(){
        return isPhase2 ? PHASE2_SPEED_MULTIPLIER : 1.0f;
    }


    private float timeScale(){
        return isPhase2 ? PHASE2_TIME_SCALE : 1.0f;
    }

    public void setState(float delta){
        if(!isActive) return;

        if(currentState != State.CHARGERUN &&
            currentState != State.OFFENSIVELEAP &&
            currentState != State.DEFENSIVELEAP) {
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        }

        if(!isPhase2 && HP <= 2500) {
            isPhase2 = true;
            changeState(State.STUN);
            stunTimer = 0;
            return;
        }

        if(currentState == State.STUN) {
            stunTimer += delta;
            if(stunTimer >= 8.0f) {
                changeState(State.STUNRECOV);
                stunTimer = 0;
            }
            return;
        }

        if(currentState == State.STUNRECOV) {
            stunTimer += delta;
            if(stunTimer >= 0.6f) {
                changeState(State.IDLE);
                idleTimer = 1f;
            }
            return;
        }

        if(currentState == State.POWERFULHIT){
            powerfulHitTimer += delta;
            if(powerfulHitTimer >= 0.4f * timeScale() && !waveSpawned) {
                spawnWave();
                checkMaceHit();
                waveSpawned = true;
            }
            if(powerfulHitTimer >= 0.8f * timeScale()){
                changeState(State.IDLE);
                idleTimer = 1.5f * timeScale();
            }
            return;
        }

        if(currentState == State.MACESLAMANTI){
            maceSlamAntiTimer += delta;
            if (maceSlamAntiTimer >= 0.6f * timeScale()){
                changeState(State.MACESLAM);
                maceSlamTimer = 0;
            }
            return;
        }

        if(currentState == State.MACESLAM) {
            maceSlamTimer += delta;
            if (maceSlamTimer >= 0.1f && !maceHitApplied) {
                checkMaceHit();
                maceHitApplied = true;
            }
            if (maceSlamTimer >= 0.3f * timeScale()) {
                changeState(State.MACESLAMRECOV);
                maceSlamRecovTimer = 0;
            }
            return;
        }

        if(currentState == State.MACESLAMRECOV){
            maceSlamRecovTimer += delta;
            if(maceSlamRecovTimer >= 0.5f * timeScale()){
                changeState(State.IDLE);
                idleTimer = 1.5f * timeScale();
            }
            return;
        }

        if(currentState == State.CHARGERUNANTI){
            chargeRunAntiTimer += delta;
            if(chargeRunAntiTimer >= 0.2f * timeScale()){
                changeState(State.CHARGERUN);
            }
            return;
        }

        if(currentState == State.CHARGERUN){
            if(!chargeRun(delta)){
                changeState(State.IDLE);
                idleTimer = 1.0f * timeScale();
            }
            return;
        }

        if(currentState == State.OFFENSIVELEAPANTI){
            offensiveLeapAntiTimer += delta;
            if(offensiveLeapAntiTimer >= 0.3f * timeScale()){
                offensiveLeapTimer = 1.0f;
                changeState(State.OFFENSIVELEAP);
                body.setLinearVelocity(face * 6.5f * speedMultiplier(), 12f * speedMultiplier());
            }
            return;
        }

        if(currentState == State.DEFENSIVELEAPANTI){
            offensiveLeapAntiTimer += delta;
            if(offensiveLeapAntiTimer >= 0.3f * timeScale()){
                offensiveLeapTimer = 1.0f;
                changeState(State.DEFENSIVELEAP);
                body.setLinearVelocity(-face * 5f * speedMultiplier(), 10f * speedMultiplier());
                recentHitsCount = 0;
            }
            return;
        }

        if(currentState == State.OFFENSIVELEAP || currentState == State.DEFENSIVELEAP){
            offensiveLeapTimer -= delta;
            if(offensiveLeapTimer <= 0f || (offensiveLeapTimer < 0.8f && Math.abs(body.getLinearVelocity().y) < 0.1f)){
                changeState(State.IDLE);
                idleTimer = 1.0f * timeScale();
                body.setLinearVelocity(0, 0);
            }
            return;
        }

        if(currentState == State.IDLE){
            maceHitApplied = false;
            waveSpawned = false;

            if(idleTimer > 0){
                idleTimer -= delta;
                return;
            }

            float distance = getDistance();
            State chosen = pickNextMove(distance);
            lastAttackMove = chosen;

            switch (chosen){
                case POWERFULHIT:
                    changeState(State.POWERFULHIT);
                    powerfulHitTimer = 0;
                    waveSpawned = false;
                    break;
                case MACESLAMANTI:
                    changeState(State.MACESLAMANTI);
                    maceSlamAntiTimer = 0;
                    break;
                case DEFENSIVELEAPANTI:
                    changeState(State.DEFENSIVELEAPANTI);
                    offensiveLeapAntiTimer = 0;
                    break;
                case OFFENSIVELEAPANTI:
                    changeState(State.OFFENSIVELEAPANTI);
                    offensiveLeapAntiTimer = 0;
                    break;
                case CHARGERUNANTI:
                    changeState(State.CHARGERUNANTI);
                    chargeRunAntiTimer = 0;
                    break;
                default:
                    break;
            }
        }
    }


    private State pickNextMove(float distance){

        if(recentHitsCount >= RECENT_HIT_THRESHOLD && distance < 6.0f){
            return State.DEFENSIVELEAPANTI;
        }

        int roll = random.nextInt(100);
        State candidate;

        if(distance < 3.5f){
            if(!isPhase2 && roll < 40) candidate = State.POWERFULHIT;
            else if(roll < 75) candidate = State.MACESLAMANTI;
            else candidate = State.DEFENSIVELEAPANTI;
        } else if(distance < 8.0f){
            candidate = (roll < 50) ? State.OFFENSIVELEAPANTI : State.CHARGERUNANTI;
        } else {
            candidate = State.CHARGERUNANTI;
        }


        if(candidate == lastAttackMove){
            int reroll = random.nextInt(100);
            if(distance < 3.5f){
                candidate = (reroll < 50) ? State.MACESLAMANTI : State.DEFENSIVELEAPANTI;
            } else if(distance < 8.0f){
                candidate = (reroll < 50) ? State.OFFENSIVELEAPANTI : State.CHARGERUNANTI;
            } else {
                candidate = State.OFFENSIVELEAPANTI;
            }
        }

        return candidate;
    }

    private void checkMaceHit() {
        Knight knight = ((GameScreen)game.getScreen()).knight;
        float distance = getDistance();
        float dx = knight.body.getPosition().x - body.getPosition().x;
        boolean isInFront = (face == 1 && dx > 0) || (face == -1 && dx < 0);

        if (isInFront && distance < 3.5f) {
            screen.getHud().onKnightTakeDamage(knight.getCurrentMasks());
            knight.takeDamage();
        }
    }

    private void changeState(State newState) {
        previousState = currentState;
        currentState = newState;
    }

    private void setFace(){
        if(currentState != State.IDLE && currentState != State.CHARGERUN && currentState != State.CHARGERUNANTI) return;
        Knight knight = ((GameScreen)game.getScreen()).knight;
        if(knight.body.getPosition().x - body.getPosition().x < 0) face = -1;
        else face = 1;
    }


    public void takeDamage(int damageAmount){
        if(!isActive || isDead) return;

        int actualDamage = damageAmount;
        if(currentState == State.STUN){
            actualDamage = Math.round(damageAmount * STUN_DAMAGE_MULTIPLIER);
        }

        HP = Math.max(0, HP - actualDamage);


        recentHitsCount++;
        recentHitsWindow = RECENT_HIT_WINDOW_TIME;

        if(HP == 0){
            isDead = true;
        }
    }

    private boolean chargeRun(float delta){
        Knight knight = ((GameScreen)game.getScreen()).knight;
        float distance = getDistance();
        if(distance < 2.5f) return false;
        face = knight.body.getPosition().x - body.getPosition().x < 0 ? -1 : 1;
        body.setLinearVelocity(face * 4f * speedMultiplier(), body.getLinearVelocity().y);
        return true;
    }

    private void spawnWave() {
        wave = new ShockWave(screen, game, body.getPosition().x + (face * 1f), body.getPosition().y - 2, face);
    }

    private void defineFalseKnight(float x, float y){
        BodyDef bDef = new BodyDef();
        bDef.position.set(x,y);
        bDef.type = BodyDef.BodyType.DynamicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(200/PPM);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.filter.categoryBits = FALSE_KNIGHT_BIT;
        fDef.filter.maskBits = KNIGHT_BIT |
            OBSTACLE_BIT |
            GROUND_BIT |
            NAIL_BIT |
            BOSS_GROUND_BIT |
            RIGHT_WALL_BIT |
            LEFT_WALL_BIT |
            SHADOW_DASH_BIT;

        body = screen.getWorld().createBody(bDef);
        body.createFixture(fDef).setUserData(this);
        shape.dispose();
    }

    public Array<TextureRegion> getKeyFrames(String path, int amount){
        Texture spriteSheet = game.assetSystem.get(path,Texture.class);
        TextureRegion[] split = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/amount, spriteSheet.getHeight())[0];
        Array<TextureRegion> keyFrames = new Array<>();
        for (int i = 0 ; i < amount ; i++) keyFrames.add(split[i]);
        return keyFrames;
    }

    public void setActive(boolean active) { isActive = active; }
    public boolean isActive() { return isActive; }

    public void resetAndDeactivate() {
        if(isDead) return;

        isActive = false;
        isPhase2 = false;
        HP = 1000;
        currentState = State.IDLE;
        previousState = State.IDLE;
        face = -1;

        stateTimer = 0;
        idleTimer = 0;
        maceSlamAntiTimer = 0;
        maceSlamTimer = 0;
        maceSlamRecovTimer = 0;
        offensiveLeapAntiTimer = 0;
        offensiveLeapTimer = 0;
        powerfulHitTimer = 0;
        chargeRunAntiTimer = 0;
        stunTimer = 0;
        deathLandTimer = 0;
        maceHitApplied = false;
        waveSpawned = false;
        wave = null;

        lastAttackMove = null;
        recentHitsCount = 0;
        recentHitsWindow = 0;

        if(body != null){
            body.setLinearVelocity(0, 0);
            body.setTransform(spawnX, spawnY, 0);
        }
    }

    public State getCurrentState() { return currentState; }
    public State getPreviousState() { return previousState; }
    public void setPreviousState(State previousState) { this.previousState = previousState; }
    public float getStateTimer() { return stateTimer; }
    public void setStateTimer(float stateTimer) { this.stateTimer = stateTimer; }
    public int getFace() { return face; }
    public boolean isDead() { return isDead; }
    public boolean isPhase2() { return isPhase2; }
    public ShockWave getWave() { return wave; }

    public void drawSelf(Batch batch) { super.draw(batch); }
    public void draw(Batch batch) { view.draw(batch, this); }

    public Body getBody() {
        return body;
    }

    public void setWaveSpawned(boolean waveSpawned) { this.waveSpawned = waveSpawned; }
    public void setWave(ShockWave wave) { this.wave = wave; }
}
