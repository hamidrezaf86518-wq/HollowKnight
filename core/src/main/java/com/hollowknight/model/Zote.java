package com.hollowknight.model;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.hollowknight.HollowKnight;
import com.hollowknight.i18n.Dialogues;
import com.hollowknight.view.screens.GameScreen;

import static com.hollowknight.model.Constants.*;
import static com.hollowknight.model.Constants.GROUND_BIT;
import static com.hollowknight.model.Constants.NAIL_BIT;
import static com.hollowknight.model.Constants.OBSTACLE_BIT;
import static com.hollowknight.model.Constants.SHADOW_DASH_BIT;




public class Zote extends Sprite {
    private enum State{IDLE, TALK, ATTACK}
    private HollowKnight game;
    private World world;
    private GameScreen screen;
    private Body body;
    private State currentState;
    private State previousState;
    private int face;
    private float stateTimer;
    private TextureRegion promptIcon;
    private float promptTimer = 0;
    private int currentDialogueSet = 0; 





    
    private float attackTimer = 0;

    
    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> attackAnim;

    public Zote(HollowKnight game, World world, GameScreen screen, float x, float y){
        this.game = game;
        this.world = world;
        this.screen = screen;
        currentState = State.IDLE;
        previousState = State.IDLE;
        face = -1;
        stateTimer = 0;

        promptIcon = new TextureRegion(
            game.assetSystem.get("textures/Zote/controller__0007_Switch_Up.png", Texture.class));

        
        Array<TextureRegion> keyFrames = getKeyFrames("textures/Zote/Idle.png",5);
        idleAnim = new Animation<>(0.1f,keyFrames);


        
        keyFrames = getKeyFrames("textures/Zote/Attack.png",4);
        attackAnim = new Animation<>(0.1f,keyFrames);

        TextureRegion region;
        region = idleAnim.getKeyFrame(0);
        setRegion(region);
        setBounds(getX(),getY(),region.getRegionWidth()/PPM,region.getRegionHeight()/PPM);
        defineZote(x,y);


    }

    
    public void drawPrompt(SpriteBatch batch, float dt) {
        promptTimer += dt;
        
        float bounce = (float) Math.sin(promptTimer * 3) * 0.05f;

        float iconW = promptIcon.getRegionWidth() / PPM;
        float iconH = promptIcon.getRegionHeight() / PPM;

        batch.draw(promptIcon,
            body.getPosition().x - iconW / 2f,
            body.getPosition().y + 0.9f + bounce,
            iconW, iconH);
    }

    public void update(float dt) {
        
        if (currentState == State.ATTACK) {
            attackTimer = Math.min(0.4f,attackTimer+dt);
            if (attackTimer == 0.4f) {
                currentState = State.IDLE;
                attackTimer = 0;
            }
        }

        setRegion(getFrame(dt));
        setPosition(body.getPosition().x - getWidth() / 2f,
            body.getPosition().y - 30 / PPM);
    }


    private TextureRegion getFrame(float dt) {
        TextureRegion region;
        switch (currentState) {
            case ATTACK:
                region = attackAnim.getKeyFrame(stateTimer, false);
                break;
            default:
                region = idleAnim.getKeyFrame(stateTimer, true);
        }

        int newFace = screen.knight.getBody().getPosition().x < body.getPosition().x ? -1 : 1;
        if (newFace == 1 && !region.isFlipX()) region.flip(true, false);
        else if (newFace == -1 && region.isFlipX()) region.flip(true, false);
        face = newFace;

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public void onHit() {
        if (currentState != State.ATTACK) {
            currentState = State.ATTACK;
            attackTimer = 0;
            playGrunt();
        }
    }

    public void playGrunt() {
        game.audioSystem.playZoteGrunt();   
    }

    public boolean isNearKnight() {
        float dx = Math.abs(screen.knight.getBody().getPosition().x - body.getPosition().x);
        float dy = Math.abs(screen.knight.getBody().getPosition().y - body.getPosition().y);
        return dx < 1.5f && dy < 1.5f;
    }
    public String[] getNextDialogueSet() {
        String[][] dialogueSets = Dialogues.zote();
        String[] set = dialogueSets[currentDialogueSet];
        currentDialogueSet = Math.min(currentDialogueSet + 1, dialogueSets.length - 1);
        return set;
    }

    public void defineZote(float x, float y) {
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(x,y);


        CircleShape shape = new CircleShape();
        shape.setRadius(35/PPM);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
       
        fDef.filter.categoryBits = ENEMY_BIT;
        fDef.filter.maskBits = GROUND_BIT |
            OBSTACLE_BIT |
            NAIL_BIT ;

        body = world.createBody(bDef);
        body.createFixture(fDef).setUserData(this);
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
}
