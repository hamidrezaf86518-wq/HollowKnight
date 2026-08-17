package com.hollowknight.view.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.hollowknight.HollowKnight;


public class SoulOrbActor extends Actor {

    private enum FillState { IDLE, GROW, SHRINK }

    private final Animation<TextureRegion> growAnim;
    private final Animation<TextureRegion> idleAnim;
    private final Animation<TextureRegion> shrinkAnim;

    private FillState state = FillState.IDLE;
    private float stateTime = 0f;

    private float displayedPercent = 0f; 
    private float targetPercent = 0f;    

    private final ShapeRenderer shapeRenderer;

    private static final float FILL_W = 130f;
    private static final float FILL_H = 125f;
    private static final float FRAME_DUR = 0.035f;
    private static final float IDLE_FRAME_DUR = 0.12f;
    private static final float FILL_SPEED = 1.2f; 

    public SoulOrbActor(HollowKnight game) {
        TextureAtlas atlas = game.assetSystem.get("textures/HUD/Soulorb.atlas", TextureAtlas.class);

        Array<TextureAtlas.AtlasRegion> grow = new Array<>();
        Array<TextureAtlas.AtlasRegion> idle = new Array<>();
        Array<TextureAtlas.AtlasRegion> shrink = new Array<>();

        for (int i = 0; i <= 7; i++)
            grow.add(atlas.findRegion(String.format("HUD_Soulorb_fills_soul_grow%04d", i)));
        for (int i = 0; i <= 5; i++)
            idle.add(atlas.findRegion(String.format("HUD_Soulorb_fills_soul_idle%04d", i)));
        for (int i = 0; i <= 5; i++)
            shrink.add(atlas.findRegion(String.format("HUD_Soulorb_fills_soul_shrink%04d", i)));

        growAnim = new Animation<>(FRAME_DUR, grow, Animation.PlayMode.NORMAL);
        idleAnim = new Animation<>(IDLE_FRAME_DUR, idle, Animation.PlayMode.LOOP);
        shrinkAnim = new Animation<>(FRAME_DUR, shrink, Animation.PlayMode.NORMAL);

        shapeRenderer = new ShapeRenderer();

        setSize(FILL_W, FILL_H);
    }

    
    public void setSoulPercent(float percent) {
        targetPercent = MathUtils.clamp(percent, 0f, 1f);

        if (targetPercent > displayedPercent + 0.001f && state != FillState.GROW) {
            state = FillState.GROW;
            stateTime = 0f;
        } else if (targetPercent < displayedPercent - 0.001f && state != FillState.SHRINK) {
            state = FillState.SHRINK;
            stateTime = 0f;
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;

        if (displayedPercent < targetPercent) {
            displayedPercent = Math.min(targetPercent, displayedPercent + FILL_SPEED * delta);
        } else if (displayedPercent > targetPercent) {
            displayedPercent = Math.max(targetPercent, displayedPercent - FILL_SPEED * delta);
        }

        if (state == FillState.GROW && growAnim.isAnimationFinished(stateTime)) {
            state = FillState.IDLE;
            stateTime = 0f;
        } else if (state == FillState.SHRINK && shrinkAnim.isAnimationFinished(stateTime)) {
            state = FillState.IDLE;
            stateTime = 0f;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion frame;
        switch (state) {
            case GROW:   frame = growAnim.getKeyFrame(stateTime);   break;
            case SHRINK: frame = shrinkAnim.getKeyFrame(stateTime); break;
            default:     frame = idleAnim.getKeyFrame(stateTime);  break;
        }

        float x = getX();
        float y = getY();
        float w = getWidth();
        float h = getHeight();

        float centerX = x + w / 2f;
        float centerY = y + h / 2f;


        float radius = 54f;   

        
        batch.end();

        Gdx.gl.glEnable(GL20.GL_STENCIL_TEST);
        Gdx.gl.glClear(GL20.GL_STENCIL_BUFFER_BIT);
        Gdx.gl.glColorMask(false, false, false, false);
        Gdx.gl.glDepthMask(false);
        Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 1, 0xFF);
        Gdx.gl.glStencilOp(GL20.GL_REPLACE, GL20.GL_REPLACE, GL20.GL_REPLACE);

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(centerX, centerY, radius, 64);
        shapeRenderer.end();

        Gdx.gl.glColorMask(true, true, true, true);
        Gdx.gl.glDepthMask(true);
        Gdx.gl.glStencilFunc(GL20.GL_EQUAL, 1, 0xFF);
        Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_KEEP);

        batch.begin();

        float bottomHidden = y - h;   
        float bottomFull = y;         
        float drawY = MathUtils.lerp(bottomHidden, bottomFull, displayedPercent);

        batch.draw(frame, x, drawY, w, h);

        batch.end();
        Gdx.gl.glDisable(GL20.GL_STENCIL_TEST);

        batch.begin(); 
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
