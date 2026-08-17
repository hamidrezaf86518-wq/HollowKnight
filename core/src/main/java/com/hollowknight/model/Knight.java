package com.hollowknight.model;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.hollowknight.HollowKnight;
import com.hollowknight.controller.KnightController;
import com.hollowknight.model.Charms.CharmInventory;
import com.hollowknight.model.Effects.DashEffect;
import com.hollowknight.model.Effects.NailEffect;
import com.hollowknight.model.Enemies.Enemy;
import com.hollowknight.view.EntitiesView.KnightView;
import com.hollowknight.view.screens.GameScreen;

import static com.hollowknight.model.Constants.*;


public class Knight extends Sprite {
    public static float ATTACK_TIMER = 0.3f;
    private static float ATTACK_COOLDOWN = 0.3f;
    private static float FOCUS_TIMER = 1.5f;
    public World world;
    public Body body;
    private Fixture mainFixture;
    private Fixture shadowDashFixture;
    private final HollowKnight game;
    private GameScreen screen;
    private int currentMasks;
    private CharmInventory charmInventory;
    private int souls = 0;
    private Array<Object> shadowHitTargets = new Array<>();


    private boolean isCasting = false;
    private float castTimer = 0f;
    private static final float CAST_LOCK = 0.4f;

    public enum State{FALL, WALLSLIDE, JUMP, POGO, SHADOWPDASH, DOUBLEJUMP, RUN, IDLE, DASH, FOCUS, RIGHTATTACK, LEFTATTACK, UPATTACK, DOWNATTACK};


    private boolean isHited;

    private boolean onGround;
    private boolean onRWall = false;
    private boolean onLWall = false;
    private boolean isWallSlide = false;
    private float stateTimer;
    private int face;

    public State currentState;
    public State previousState;

    private boolean isDead = false;
    private float damageCoolDown = 0f;
    private float respawnTimer = 0f;
    private static final float RESPAWN_DELAY = 1.2f;


    private Nail nail;
    private NailEffect nailEffect;


    private float attackTimer;
    private float attackCoolDown;
    private boolean isAttacking;


    private float dashCoolDown;
    private boolean isDashing;
    private float dashTimer;
    private DashEffect dashEffect;


    private boolean canAirDash = true;


    private boolean isPressed;
    private boolean wasPressed;
    private float pressTime = 0f;
    private boolean isJumping;
    private float jumpTimer;


    private boolean isFocusPressed;
    private boolean wasFocusPressed;
    private float focusTimer;

    private boolean isKnockedBack = false;
    private float knockBackTimer = 0f;
    private static final float KNOCKBACK_DURATION = 0.18f;
    private static final float KNOCKBACK_FORCE = 4.5f;

    private float blinkTimer = 0f;
    private static final float BLINK_INTERVAL = 0.08f;
    private boolean spriteVisible = true;


    private boolean canDoubleJump = false;
    private boolean isDoubleJumping = false;



    private boolean canPogo = true;
    private static final float POGO_FORCE = 6.5f;

    private boolean pogoChecking = false;


    private boolean pogoBounced = false;


    private boolean godMode = false;
    private boolean noclip = false;
    private short savedMaskBits;
    private boolean maskBitsSaved = false;

    private final KnightView view;

    public Knight(World world, HollowKnight game, GameScreen screen,float x , float y){
        super();
        this.world = world;
        this.game = game;
        this.screen = screen;
        currentState = State.IDLE;
        previousState = State.IDLE;
        stateTimer = 0;
        face = -1;
        currentMasks = 5;
        charmInventory = new CharmInventory();
        nail = new Nail(world,this);
        dashEffect = new DashEffect(game);
        nailEffect = new NailEffect(this,game);
        dashEffect.setOffset(30, 0);
        view = new KnightView(this);
        defineKnight(x,y);
    }

    public void update(float dt){
        if(isDead){
            if(body.isActive()){
                body.setActive(false);
            }
            respawnTimer += dt;
            if(respawnTimer >= RESPAWN_DELAY){
                respawn();
            }
            return;
        }


        if(onGround && body.getLinearVelocity().y <= 0.01f){
            canDoubleJump = true;
            canAirDash = true;
            isDoubleJumping = false;
        }

        if (isCasting) {
            castTimer += dt;
            body.setLinearVelocity(0, body.getLinearVelocity().y);
            if (castTimer >= CAST_LOCK) isCasting = false;
            view.updateFrame(this, dt);
            return;
        }

        damageCoolDown = Math.max(0, damageCoolDown - dt);

        if(charmInventory.hasQuickSlash()){
            ATTACK_COOLDOWN = 0.15f;
            ATTACK_TIMER = 0.15f;
        }else{
            ATTACK_COOLDOWN = 0.3f;
            ATTACK_TIMER = 0.3f;
        }

        if(charmInventory.hasQuickFocus()){
            FOCUS_TIMER = 1f;
        }else{
            FOCUS_TIMER = 1.5f;
        }

        setPosition(body.getPosition().x-getWidth()/2,body.getPosition().y-45/PPM);
        nail.updateNailPosition();
        dashEffect.update(dt);
        nailEffect.update(dt);

        if(isFocusPressed && !wasFocusPressed){
            wasFocusPressed = true;
            focusTimer = 0;
        }

        if(isFocusPressed && wasFocusPressed){
            focusTimer = Math.min(FOCUS_TIMER,focusTimer+dt);
            if(focusTimer == FOCUS_TIMER){
                currentMasks = Math.min(5,currentMasks+1);
                screen.getHud().onKnightHeal(currentMasks);
                focusTimer = 0;
            }
        }

        if(!isFocusPressed && wasFocusPressed){
            if(focusTimer == FOCUS_TIMER){
                currentMasks = Math.min(5, currentMasks+1);
                screen.getHud().onKnightHeal(currentMasks);
                game.audioSystem.playSFX("focus");
                focusTimer = 0;
            }
            wasFocusPressed = false;
        }

        if(isWallSlide && (!onLWall || !onRWall)){
            isWallSlide = false;
            previousState = currentState;
            currentState = State.IDLE;
        }

        if(isJumping){
            jumpTimer = Math.min(0.96f,jumpTimer+dt);
            if(jumpTimer == 0.96f){
                isJumping = false;
                jumpTimer = 0;
            }
        }

        if(isPressed && !wasPressed){
            wasPressed = true;
            pressTime = 0;
        }

        if(isPressed && wasPressed){
            pressTime = Math.min(15,pressTime+0.2f);
        }

        if(!isPressed && wasPressed){
            wasPressed = false;
            jump();
        }

        State attackStateToHold = null;

        if(isAttacking){
            attackStateToHold = currentState;
            attackTimer = Math.min(ATTACK_TIMER,attackTimer+=dt);
            if (attackTimer < 0.05f && !nailEffect.isPlaying()) {
                Vector2 effectPos = new Vector2(
                    body.getPosition().x,
                    body.getPosition().y
                );
                nailEffect.start(effectPos);
            }
            if(attackTimer >= ATTACK_TIMER){
                isAttacking = false;
                attackCoolDown = ATTACK_COOLDOWN;
                nail.getNailBody().setActive(false);
                attackStateToHold = null;
            }
        }


        if (currentState == State.POGO && pogoChecking && !pogoBounced) {
            if (checkPogoTargetBelow()) {
                pogoBounce();
            }
        }

        attackCoolDown = Math.max(0,attackCoolDown-dt);

        if(isDashing){
            dashCoolDown = Math.max(0,dashCoolDown-dt);
            dashTimer += dt;

            if (dashTimer < 0.1f && !dashEffect.isPlaying() && !charmInventory.hasSharpShadow()) {
                Vector2 effectPos = new Vector2(
                    body.getPosition().x,
                    body.getPosition().y
                );
                dashEffect.start(effectPos, face);
            }

            if(dashTimer >= 0.6f){
                isDashing = false;
                setShadowMode(false);
                dashTimer = 0;

                if(charmInventory.hasDashmaster())
                    dashCoolDown = 0.8f;
                else
                    dashCoolDown = 1.4f;
            }else{
                view.updateFrame(this, dt);
                return;
            }
        }

        dashCoolDown = Math.max(0,dashCoolDown-dt);
        damageCoolDown = Math.max(0, damageCoolDown - dt);

        if (isKnockedBack) {
            knockBackTimer += dt;
            if (knockBackTimer >= KNOCKBACK_DURATION) {
                isKnockedBack = false;
            }
        }

        if (damageCoolDown > 0) {
            blinkTimer += dt;
            if (blinkTimer >= BLINK_INTERVAL) {
                blinkTimer = 0f;
                spriteVisible = !spriteVisible;
            }
            setColor(1f, 1f, 1f, spriteVisible ? 1f : 0.25f);
        } else {
            spriteVisible = true;
            blinkTimer = 0f;
            setColor(1f, 1f, 1f, 1f);
        }

        KnightController.handleInput(this, dt);

        if(noclip){
            float vx = 0, vy = 0;
            if(com.badlogic.gdx.Gdx.input.isKeyPressed(KeyBinding.MOVE_RIGHT)) vx = 6f;
            if(com.badlogic.gdx.Gdx.input.isKeyPressed(KeyBinding.MOVE_LEFT))  vx = -6f;
            if(com.badlogic.gdx.Gdx.input.isKeyPressed(KeyBinding.FACE_UP))    vy = 6f;
            if(com.badlogic.gdx.Gdx.input.isKeyPressed(KeyBinding.FACE_DOWN))  vy = -6f;
            body.setLinearVelocity(vx, vy);
        }

        if(attackStateToHold != null){
            currentState = attackStateToHold;
        }

        view.updateFrame(this, dt);
    }

    public void lockMovementForCast() {
        isCasting = true;
        castTimer = 0f;
        body.setLinearVelocity(0, 0);
    }


    public void doPogo() {



        if (onGround) return;
        if (isAttacking) return;
        if (attackCoolDown > 0) return;

        game.audioSystem.playSFX("nail_slash");

        previousState = currentState;

        isHited = false;
        isAttacking = true;

        attackTimer = 0f;
        attackCoolDown = 0.15f;


        currentState = State.POGO;


        face = -2;

        pogoBounced = false;
        pogoChecking = true;


        if (nail != null && nail.getNailBody() != null) {
            nail.getNailBody().setActive(true);
        }


    }

    private boolean checkPogoTargetBelow() {

        if (body == null || world == null) {
            return false;
        }

        final float knightX = body.getPosition().x;
        final float knightY = body.getPosition().y;


        final float halfWidth = 0.28f;


        final float feetY = knightY - 0.50f;


        final float lowerY = feetY - 0.20f;
        final float upperY = feetY + 0.18f;

        final boolean[] targetFound = {false};

        world.QueryAABB(fixture -> {

                if (fixture == null || fixture.getBody() == body) {
                    return true;
                }

                short category = fixture.getFilterData().categoryBits;
                Object userData = fixture.getBody().getUserData();

                boolean isTarget = false;


                if ((category & SPIKE_BIT) != 0 ||
                    (category & SPIKE_BIT) != 0) {

                    isTarget = true;
                }


                if (userData != null) {

                    String dataName = userData.getClass().getSimpleName()
                        .toLowerCase();

                    if (dataName.contains("spike") ||
                        dataName.contains("enemy") ||
                        dataName.contains("falseknight") ||
                        dataName.contains("crystalguardian")) {

                        isTarget = true;
                    }

                    if (userData instanceof String) {
                        String text = ((String) userData).toLowerCase();

                        if (text.contains("spike") ||
                            text.contains("enemy") ||
                            text.contains("boss")) {

                            isTarget = true;
                        }
                    }
                }

                if (isTarget) {
                    targetFound[0] = true;




                    return false;
                }

                return true;

            },
            knightX - halfWidth,
            lowerY,
            knightX + halfWidth,
            upperY);

        return targetFound[0];
    }


    public void pogoBounce(){
        if(pogoBounced) return;
        pogoBounced = true;
        pogoChecking = false;
        isAttacking = false;


        body.setLinearVelocity(body.getLinearVelocity().x, POGO_FORCE);


        canDoubleJump = true;
        canAirDash = true;
        dashCoolDown = 0f;

        currentState = State.JUMP;
        setStateTimer(0);


    }


    public boolean isPogoActive(){
        return currentState == State.POGO && isAttacking;
    }

    public void wallSlide(){
        isWallSlide = true;
        previousState = currentState;
        currentState = State.WALLSLIDE;
        body.setLinearVelocity(0,-1f);
    }

    public void runRight(){
        if(body.getLinearVelocity().x <= 2) {
            body.applyLinearImpulse(new Vector2(0.1f, 0), body.getWorldCenter(), true);
        }
        face = 1;
        if(onGround){
            previousState = currentState;
            currentState = State.RUN;
        }
    }

    public void runLeft(){
        if(body.getLinearVelocity().x >= -2) {
            body.applyLinearImpulse(new Vector2(-0.1f, 0), body.getWorldCenter(), true);
        }
        face = -1;
        if(onGround){
            previousState = currentState;
            currentState = State.RUN;
        }
    }

    public void jump(){
        if(onGround){
            isJumping = true;
            jumpTimer = 0;
            previousState = currentState;
            currentState = State.JUMP;
            body.setLinearVelocity(body.getLinearVelocity().x, pressTime/10 * 5);

            canDoubleJump = true;
            canAirDash = true;
            isDoubleJumping = false;
        } else if(canDoubleJump){
            canDoubleJump = false;
            isDoubleJumping = true;
            previousState = currentState;
            currentState = State.DOUBLEJUMP;
            setStateTimer(0);
            body.setLinearVelocity(body.getLinearVelocity().x, 5.5f);
        }
    }

    public void dash(){
        boolean allowed = onGround || canAirDash;
        if(dashCoolDown == 0 && allowed){
            previousState = currentState;
            currentState = State.DASH;
            isDashing = true;
            dashTimer = 0f;
            body.setLinearVelocity(face * 5.5f,0);

            if(!onGround) canAirDash = false;
        }
    }

    public void shadowDash(){
        boolean allowed = onGround || canAirDash;
        if(dashCoolDown == 0 && allowed){
            previousState = currentState;
            currentState = State.SHADOWPDASH;
            isDashing = true;
            dashTimer = 0f;
            shadowHitTargets.clear();

            setShadowMode(true);

            body.setLinearVelocity(face * 6.5f, 0);

            if(!onGround) canAirDash = false;
        }
    }

    public boolean isShadowDashing() {
        return isDashing && currentState == State.SHADOWPDASH;
    }

    public boolean canHitByShadowDash(Object target) {
        return !shadowHitTargets.contains(target, true);
    }

    public void registerShadowDashHit(Object target) {
        shadowHitTargets.add(target);
    }

    public void attack(){
        if(isAttacking || attackCoolDown > 0) return;
        game.audioSystem.playSFX("nail_slash");
        previousState = currentState;
        isHited = false;
        isAttacking = true;
        attackTimer = 0;
        nail.getNailBody().setActive(true);
        switch (face){
            case 1:
                currentState = State.RIGHTATTACK;
                break;
            case -1:
                currentState = State.LEFTATTACK;
                break;
            case 2:
                currentState = State.UPATTACK;
                break;
            case -2:
                currentState = State.DOWNATTACK;
                break;
        }
    }

    public void defineKnight(float x , float y){
        BodyDef bdef = new BodyDef();
        bdef.position.set(x,y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(20/PPM,45/PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = KNIGHT_BIT;
        fdef.filter.maskBits = GROUND_BIT |
            ENEMY_BIT |
            OBSTACLE_BIT |
            MOSQUITO_BIT |
            BOSS_GROUND_BIT |
            FALSE_KNIGHT_BIT |
            SHOCK_WAVE_BIT |
            RIGHT_WALL_BIT |
            LEFT_WALL_BIT |
            SPIKE_BIT;

        mainFixture = body.createFixture(fdef);
        mainFixture.setUserData(this);

        FixtureDef shadowDef = new FixtureDef();
        shadowDef.shape = shape;
        shadowDef.isSensor = true;
        shadowDef.filter.categoryBits = SHADOW_DASH_BIT;
        shadowDef.filter.maskBits = ENEMY_BIT | FALSE_KNIGHT_BIT | MOSQUITO_BIT;

        shadowDashFixture = body.createFixture(shadowDef);
        shadowDashFixture.setUserData("shadowDash");
        shadowDashFixture.setSensor(true);

        body.setUserData(this);

        shape.dispose();
    }

    private void setShadowMode(boolean shadow) {
        Filter filter = mainFixture.getFilterData();

        if(shadow){
            filter.maskBits = GROUND_BIT |
                BOSS_GROUND_BIT |
                OBSTACLE_BIT |
                RIGHT_WALL_BIT |
                LEFT_WALL_BIT;
        } else {
            filter.maskBits = GROUND_BIT |
                ENEMY_BIT |
                OBSTACLE_BIT |
                MOSQUITO_BIT |
                BOSS_GROUND_BIT |
                FALSE_KNIGHT_BIT |
                SHOCK_WAVE_BIT |
                RIGHT_WALL_BIT |
                LEFT_WALL_BIT |
                SPIKE_BIT |
                LASER_BIT;
        }

        mainFixture.setFilterData(filter);
    }

    public void hitEnemey(Enemy enemy){
        if(!isHited) {
            if(charmInventory.hasUnbreakableStrength())
                enemy.takeDamage(200);
            else
                enemy.takeDamage(100);

            if(charmInventory.hasHeavyBlow())
                enemy.applyKnockBack(face, 3);
            else
                enemy.applyKnockBack(face,1.5f);
            isHited = true;
        }
        game.audioSystem.playSFX("soul_gain");
        if(charmInventory.hasSoulCatcher()){
            souls = Math.min(MAX_SOULS,souls+15);
            screen.getHud().onSoulChange(souls,99);
        }else{
            souls = Math.min(MAX_SOULS,souls+11);
            screen.getHud().onSoulChange(souls,99);
        }
    }

    public void hitFalseKnight(FalseKnight boss){
        if(!isHited) {
            if(charmInventory.hasUnbreakableStrength())
                boss.takeDamage(200);
            else
                boss.takeDamage(100);
            isHited = true;
        }
        game.audioSystem.playSFX("soul_gain");
        if(charmInventory.hasSoulCatcher()){
            souls = Math.min(MAX_SOULS,souls+15);
        }else{
            souls = Math.min(MAX_SOULS,souls+11);
        }
    }

    public void drawSelf(Batch batch) {
        super.draw(batch);
    }

    public void takeDamage(){
        takeDamage(body.getPosition().x + face);
    }

    public void takeDamage(float sourceX){
        if(isDead || damageCoolDown > 0 || godMode) return;
        game.audioSystem.playSFX("damage");
        screen.getHud().onKnightTakeDamage(currentMasks);
        currentMasks = Math.max(0,currentMasks-1);
        damageCoolDown = 1f;

        float dir = (sourceX <= body.getPosition().x) ? 1f : -1f;
        startKnockback(dir);

        if(currentMasks == 0){
            isDead = true;
            if(screen != null) screen.onKnightDeath();
        }
    }

    public boolean isGodMode() { return godMode; }
    public void setGodMode(boolean godMode) { this.godMode = godMode; }

    public boolean isNoclip() { return noclip; }

    public void setNoclip(boolean noclip){
        this.noclip = noclip;
        if(mainFixture == null || body == null) return;

        Filter filter = mainFixture.getFilterData();
        if(noclip){
            if(!maskBitsSaved){
                savedMaskBits = filter.maskBits;
                maskBitsSaved = true;
            }
            filter.maskBits = 0;
            mainFixture.setFilterData(filter);
            body.setGravityScale(0f);
            body.setLinearVelocity(0, 0);
        } else {
            if(maskBitsSaved){
                filter.maskBits = savedMaskBits;
                mainFixture.setFilterData(filter);
                maskBitsSaved = false;
            }
            body.setGravityScale(1f);
        }
    }

    public void healFully(){
        currentMasks = 5;
        if(screen != null && screen.getHud() != null){
            screen.getHud().resetMasksFull();
        }
        game.audioSystem.playSFX("focus");
    }

    public void refillSoul(){
        souls = MAX_SOULS;
        if(screen != null && screen.getHud() != null){
            screen.getHud().onSoulChange(souls, 99);
        }
        game.audioSystem.playSFX("soul_gain");
    }

    public void teleportTo(float x, float y){
        if(body == null) return;
        body.setLinearVelocity(0, 0);
        body.setTransform(x, y, 0);
        setPosition(x - getWidth()/2, y - 45/PPM);
    }

    private void respawn(){
        currentMasks = 5;
        isDead = false;
        respawnTimer = 0f;
        damageCoolDown = 1f;

        float sx = (screen != null) ? screen.getSpawnX() : body.getPosition().x;
        float sy = (screen != null) ? screen.getSpawnY() : body.getPosition().y;

        body.setActive(true);
        body.setLinearVelocity(0, 0);
        body.setTransform(sx, sy, 0);
        setPosition(sx - getWidth()/2, sy - 45/PPM);

        if(screen != null && screen.getHud() != null){
            screen.getHud().resetMasksFull();
        }

        if(screen != null){
            screen.onKnightRespawned();
        }
    }

    private void startKnockback(float dir){
        isKnockedBack = true;
        knockBackTimer = 0f;
        body.setLinearVelocity(dir * KNOCKBACK_FORCE, 5f);
    }

    public boolean isKnockedBack(){
        return isKnockedBack;
    }
    public boolean isDead() {
        return isDead;
    }

    public int getFace() {
        return face;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public int getCurrentMasks() {
        return currentMasks;
    }

    public void setCurrentMasks(int currentMasks) {
        this.currentMasks = currentMasks;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public HollowKnight getGame() {
        return game;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public void setStateTimer(float stateTimer) {
        this.stateTimer = stateTimer;
    }

    public void setFace(int face) {
        this.face = face;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public State getPreviousState() {
        return previousState;
    }

    public void setPreviousState(State previousState) {
        this.previousState = previousState;
    }

    public Nail getNail() {
        return nail;
    }

    public void setNail(Nail nail) {
        this.nail = nail;
    }

    public NailEffect getNailEffect() {
        return nailEffect;
    }

    public void setNailEffect(NailEffect nailEffect) {
        this.nailEffect = nailEffect;
    }

    public float getAttackTimer() {
        return attackTimer;
    }

    public void setAttackTimer(float attackTimer) {
        this.attackTimer = attackTimer;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public float getDashCoolDown() {
        return dashCoolDown;
    }

    public void setDashCoolDown(float dashCoolDown) {
        this.dashCoolDown = dashCoolDown;
    }

    public boolean isDashing() {
        return isDashing;
    }

    public void setDashing(boolean dashing) {
        isDashing = dashing;
    }

    public float getDashTimer() {
        return dashTimer;
    }

    public void setDashTimer(float dashTimer) {
        this.dashTimer = dashTimer;
    }

    public DashEffect getDashEffect() {
        return dashEffect;
    }

    public void setDashEffect(DashEffect dashEffect) {
        this.dashEffect = dashEffect;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }

    public boolean isWasPressed() {
        return wasPressed;
    }

    public void setWasPressed(boolean wasPressed) {
        this.wasPressed = wasPressed;
    }

    public float getPressTime() {
        return pressTime;
    }

    public void setPressTime(float pressTime) {
        this.pressTime = pressTime;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }

    public float getJumpTimer() {
        return jumpTimer;
    }

    public void setJumpTimer(float jumpTimer) {
        this.jumpTimer = jumpTimer;
    }

    public static float getAttackCooldown() {
        return ATTACK_COOLDOWN;
    }

    public static void setAttackCooldown(float attackCooldown) {
        ATTACK_COOLDOWN = attackCooldown;
    }

    public static float getFocusTimer() {
        return FOCUS_TIMER;
    }

    public static void setFocusTimer(float focusTimer) {
        FOCUS_TIMER = focusTimer;
    }

    public KnightView getView() {
        return view;
    }

    public Fixture getMainFixture() {
        return mainFixture;
    }

    public void setMainFixture(Fixture mainFixture) {
        this.mainFixture = mainFixture;
    }

    public Fixture getShadowDashFixture() {
        return shadowDashFixture;
    }

    public void setShadowDashFixture(Fixture shadowDashFixture) {
        this.shadowDashFixture = shadowDashFixture;
    }

    public GameScreen getScreen() {
        return screen;
    }

    public void setScreen(GameScreen screen) {
        this.screen = screen;
    }

    public CharmInventory getCharmInventory() {
        return charmInventory;
    }

    public void setCharmInventory(CharmInventory charmInventory) {
        this.charmInventory = charmInventory;
    }

    public int getSouls() {
        return souls;
    }

    public void setSouls(int souls) {
        this.souls = souls;
    }

    public Array<Object> getShadowHitTargets() {
        return shadowHitTargets;
    }

    public void setShadowHitTargets(Array<Object> shadowHitTargets) {
        this.shadowHitTargets = shadowHitTargets;
    }

    public boolean isHited() {
        return isHited;
    }

    public void setHited(boolean hited) {
        isHited = hited;
    }

    public boolean isWallSlide() {
        return isWallSlide;
    }

    public void setWallSlide(boolean wallSlide) {
        isWallSlide = wallSlide;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public float getAttackCoolDown() {
        return attackCoolDown;
    }

    public void setAttackCoolDown(float attackCoolDown) {
        this.attackCoolDown = attackCoolDown;
    }

    public boolean isFocusPressed() {
        return isFocusPressed;
    }

    public void setFocusPressed(boolean focusPressed) {
        isFocusPressed = focusPressed;
    }

    public boolean isWasFocusPressed() {
        return wasFocusPressed;
    }

    public void setWasFocusPressed(boolean wasFocusPressed) {
        this.wasFocusPressed = wasFocusPressed;
    }

    public boolean isOnRWall() {
        return onRWall;
    }

    public void setOnRWall(boolean onRWall) {
        this.onRWall = onRWall;
    }

    public boolean isOnLWall() {
        return onLWall;
    }

    public void setOnLWall(boolean onLWall) {
        this.onLWall = onLWall;
    }

    public boolean isCasting() {
        return isCasting;
    }

    public void setCasting(boolean casting) {
        isCasting = casting;
    }

    public float getCastTimer() {
        return castTimer;
    }

    public void setCastTimer(float castTimer) {
        this.castTimer = castTimer;
    }

    public float getDamageCoolDown() {
        return damageCoolDown;
    }

    public void setDamageCoolDown(float damageCoolDown) {
        this.damageCoolDown = damageCoolDown;
    }

    public boolean isDoubleJumping() { return isDoubleJumping; }
    public boolean canDoubleJump() { return canDoubleJump; }
    public boolean canPogo() { return canPogo; }
    public boolean canAirDash() { return canAirDash; }
}
