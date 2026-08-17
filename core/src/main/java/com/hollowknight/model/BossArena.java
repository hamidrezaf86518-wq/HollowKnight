package com.hollowknight.model;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

import static com.hollowknight.model.Constants.*;


public class BossArena {

    private final World world;
    private Body leftWallBody;
    private Body rightWallBody;

    private float centerX, centerY;
    private float left, right, bottom, top;
    private boolean valid = false;   
    private boolean locked = false;

    public BossArena(World world, TiledMap map) {
        this.world = world;
        loadFromMap(map);
    }

    private void loadFromMap(TiledMap map) {
        
        Rectangle rect = findArenaRect(map, "Collision");
        if (rect == null) {
            rect = findArenaRect(map, "BossArena");   
        }
        if (rect == null) {
            
            return;
        }

        left   = rect.getX() / PPM;
        right  = (rect.getX() + rect.getWidth()) / PPM;
        bottom = rect.getY() / PPM;
        top    = (rect.getY() + rect.getHeight()) / PPM;

        centerX = (left + right) / 2f;
        centerY = (bottom + top) / 2f;

        buildWalls();
        valid = true;

        
    }

    private Rectangle findArenaRect(TiledMap map, String layerName) {
        if (map.getLayers().get(layerName) == null) return null;
        for (MapObject object : map.getLayers().get(layerName)
            .getObjects().getByType(RectangleMapObject.class)) {
            String type = object.getProperties().get("type", String.class);
            if ("bossarena".equals(type)) {
                return ((RectangleMapObject) object).getRectangle();
            }
        }
        return null;
    }

    
    private void buildWalls() {
        float wallHalfHeight = (top - bottom) / 2f;
        float wallThickness = 10f / PPM;

        
        leftWallBody = createWall(left, centerY, wallThickness, wallHalfHeight, OBSTACLE_BIT);
        
        rightWallBody = createWall(right, centerY, wallThickness, wallHalfHeight, OBSTACLE_BIT);

        
        leftWallBody.setActive(false);
        rightWallBody.setActive(false);
    }

    private Body createWall(float x, float y, float halfW, float halfH, short categoryBit) {
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.position.set(x, y);

        Body body = world.createBody(bDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(halfW, halfH);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.filter.categoryBits = categoryBit;
        
        fDef.filter.maskBits = KNIGHT_BIT | FALSE_KNIGHT_BIT;

        body.createFixture(fDef);
        shape.dispose();
        return body;
    }

    
    public void lock() {
        if (!valid || locked) return;
        locked = true;
        if (leftWallBody != null) leftWallBody.setActive(true);
        if (rightWallBody != null) rightWallBody.setActive(true);
    }

    
    public void unlock() {
        if (!valid || !locked) return;
        locked = false;
        if (leftWallBody != null) leftWallBody.setActive(false);
        if (rightWallBody != null) rightWallBody.setActive(false);
    }

    public boolean isLocked()  { return locked; }
    public boolean isValid()   { return valid; }
    public float getCenterX()  { return centerX; }
    public float getCenterY()  { return centerY; }
    
    public float getWidth()  { return right - left; }
    public float getHeight() { return top - bottom; }
    public float getLeft()   { return left; }
    public float getRight()  { return right; }
    public float getBottom() { return bottom; }
    public float getTop()    { return top; }
}
