package com.hollowknight.controller.WorldController;


import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

import static com.hollowknight.model.Constants.*;


public class B2WorldCreator {
    public B2WorldCreator(World world, TiledMap map){
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for(MapObject object : map.getLayers().get("Collision").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            String type = object.getProperties().get("type",String.class);
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2)/PPM, (rect.getY() + rect.getHeight()/2)/PPM);



            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2/PPM,rect.height/2/PPM);
            fdef.shape = shape;



            switch (type){
                case "ground":
                    fdef.filter.categoryBits = GROUND_BIT;
                    fdef.filter.maskBits = KNIGHT_BIT |
                        ENEMY_BIT |
                        FALSE_KNIGHT_BIT|
                        SHOCK_WAVE_BIT;
                    break;
                case "obstacle":
                    fdef.filter.categoryBits = OBSTACLE_BIT;
                    fdef.filter.maskBits = KNIGHT_BIT |
                        ENEMY_BIT |
                        FALSE_KNIGHT_BIT|
                        SHOCK_WAVE_BIT|
                        LASER_BIT;
                    break;
                case "rwall":
                    fdef.filter.categoryBits = RIGHT_WALL_BIT;
                    fdef.filter.maskBits = KNIGHT_BIT |
                        ENEMY_BIT |
                        FALSE_KNIGHT_BIT|
                        SHOCK_WAVE_BIT|
                        LASER_BIT;
                    break;
                case "lwall":
                    fdef.filter.categoryBits = LEFT_WALL_BIT;
                    fdef.filter.maskBits = KNIGHT_BIT |
                        ENEMY_BIT |
                        FALSE_KNIGHT_BIT|
                        SHOCK_WAVE_BIT|
                        LASER_BIT;
                    break;
                case "cliff":
                    fdef.filter.categoryBits = OBSTACLE_BIT;
                    fdef.filter.maskBits = ENEMY_BIT;
                    fdef.isSensor = true;
                    break;
                case "boss":
                    fdef.filter.categoryBits = BOSS_GROUND_BIT;
                    fdef.filter.maskBits = KNIGHT_BIT |
                        FALSE_KNIGHT_BIT|
                        SHOCK_WAVE_BIT;
                    break;
                case "spike":
                    fdef.filter.categoryBits = SPIKE_BIT;
                    fdef.filter.maskBits = KNIGHT_BIT;
                default:

            }
            body.createFixture(fdef);
        }
    }
}
