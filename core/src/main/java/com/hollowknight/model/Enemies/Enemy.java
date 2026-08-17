package com.hollowknight.model.Enemies;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.hollowknight.HollowKnight;
import com.hollowknight.view.screens.GameScreen;


public abstract class Enemy extends Sprite {
    protected World world;
    protected Body body;
    protected HollowKnight game;
    protected GameScreen screen;
    protected Vector2 velocity;
    protected float stateTimer;
    protected int face;
    protected int HP;
    protected boolean isDead;
    protected float knockbackTimer = 0;

    public void takeDamage(int damage){
        if(isDead) return;
        HP = Math.max(0,HP-damage);
        if(HP == 0) {
            isDead = true;
            if(screen != null) screen.onEnemyKilled(this);
        }
    }

    public Enemy(HollowKnight game, GameScreen screen, float x, float y){
        this.game = game;
        this.screen = screen;
        world = screen.getWorld();
        stateTimer = 0;
        defineEnemy(x,y);

    }

    public abstract void defineEnemy(float x, float y);
    public abstract void update(float dt);

    
    public abstract void drawView(Batch batch);

    
    public void drawSelf(Batch batch) {
        super.draw(batch);
    }

    public void reverseVelocity(boolean x, boolean y){
        if(x) {
            velocity.x = -velocity.x;
            face = -face;
        }
        if(y) {
            velocity.y = -velocity.y;
        }
    }

    public void applyKnockBack(int face, float force){
        knockbackTimer = 0.3f;
        if(body != null) {

            body.setLinearVelocity(0, 0);
            body.applyLinearImpulse(new Vector2(face * force, force), body.getWorldCenter(), true);
        }
    }

    public Array<TextureRegion> getKeyFrames(String path, int amount){
        Texture spriteSheet = game.assetSystem.get(path,Texture.class);
        TextureRegion[] split = TextureRegion.split(spriteSheet,spriteSheet.getWidth()/amount,spriteSheet.getHeight())[0];
        Array<TextureRegion> keyFrames = new Array<>();

        for (int i = 0 ; i < amount ; i++){
            keyFrames.add(split[i]);
        }

        return keyFrames;
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

    public void setGame(HollowKnight game) {
        this.game = game;
    }

    public GameScreen getScreen() {
        return screen;
    }

    public void setScreen(GameScreen screen) {
        this.screen = screen;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public void setStateTimer(float stateTimer) {
        this.stateTimer = stateTimer;
    }

    public int getFace() {
        return face;
    }

    public void setFace(int face) {
        this.face = face;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public float getKnockbackTimer() {
        return knockbackTimer;
    }

    public void setKnockbackTimer(float knockbackTimer) {
        this.knockbackTimer = knockbackTimer;
    }
}
