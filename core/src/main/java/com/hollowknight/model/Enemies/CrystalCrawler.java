package com.hollowknight.model.Enemies;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.hollowknight.HollowKnight;
import com.hollowknight.view.EntitiesView.CrystalCrawlerView;
import com.hollowknight.view.screens.GameScreen;

import static com.hollowknight.model.Constants.*;

public class CrystalCrawler extends Enemy {
    private final CrystalCrawlerView view;
    private boolean deathHandled = false;

    public CrystalCrawler(HollowKnight game, GameScreen screen, float x, float y) {
        super(game, screen, x, y);
        face = -1;
        velocity = new Vector2(-2, -2);
        HP = 500;
        view = new CrystalCrawlerView(this);
    }

    @Override
    public void defineEnemy(float x, float y) {
        BodyDef bDef =  new BodyDef();
        bDef.position.set(x,y);
        bDef.type = BodyDef.BodyType.DynamicBody;
        CircleShape shape = new CircleShape();
        shape.setRadius(70/ PPM);

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

        body = world.createBody(bDef);
        body.createFixture(fDef).setUserData(this);
        shape.dispose();
    }

    public void update(float dt){
        if(isDead){

            if(!deathHandled && body != null){
                body.setLinearVelocity(0, 0);
                Filter filter = new Filter();
                filter.categoryBits = ENEMY_BIT;
                filter.maskBits = GROUND_BIT | OBSTACLE_BIT;
                for(Fixture f : body.getFixtureList()) f.setFilterData(filter);
                deathHandled = true;
            }
            view.updateFrame(this, dt);
            if(body != null) setPosition(body.getPosition().x - 70/PPM, body.getPosition().y - 70/PPM);
            return;
        }

        if(knockbackTimer > 0){
            knockbackTimer = Math.max(0, knockbackTimer - dt);
        }else{
            body.setLinearVelocity(velocity);
        }

        setPosition(body.getPosition().x - 70 / PPM, body.getPosition().y - 70 /PPM);
        view.updateFrame(this, dt);
    }

    @Override
    public void drawView(Batch batch) {
        view.draw(batch, this);
    }
}
