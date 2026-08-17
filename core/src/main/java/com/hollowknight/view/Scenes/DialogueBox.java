
package com.hollowknight.view.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class DialogueBox {
    private String[] lines;
    private int currentLine = 0;
    private boolean active = false;

    
    private float charTimer = 0;
    private int visibleChars = 0;
    private static final float CHAR_SPEED = 0.03f;

    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    
    private Runnable onLineChange; 



    public DialogueBox(BitmapFont font) {
        this.font = font;
        shapeRenderer = new ShapeRenderer();
    }

    public void setOnLineChange(Runnable onLineChange) {
        this.onLineChange = onLineChange;
    }

    public void start(String[] lines) {
        this.lines = lines;
        this.currentLine = 0;
        this.active = true;
        resetTyping();
        if (onLineChange != null) onLineChange.run(); 
    }

    private void resetTyping() {
        charTimer = 0;
        visibleChars = 0;
    }

    
    public void next() {
        if (!active) return;

        
        if (visibleChars < lines[currentLine].length()) {
            visibleChars = lines[currentLine].length();
            return;
        }

        currentLine++;
        if (currentLine >= lines.length) {
            active = false;
        } else {
            resetTyping();
            if (onLineChange != null) onLineChange.run(); 
        }
    }

    public void update(float dt) {
        if (!active) return;
        if (visibleChars < lines[currentLine].length()) {
            charTimer += dt;
            if (charTimer >= CHAR_SPEED) {
                charTimer = 0;
                visibleChars++;
            }
        }
    }

    public boolean isActive() { return active; }

    
    public void render(SpriteBatch batch, float screenWidth, float screenHeight) {
        if (!active) return;

        String fullText = lines[currentLine];
        String shownText = fullText.substring(0, Math.min(visibleChars, fullText.length()));

        float boxWidth = screenWidth * 0.7f;
        float boxHeight = 120;
        float boxX = (screenWidth - boxWidth) / 2f;
        float boxY = 40; 

        
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.8f);
        shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);
        shapeRenderer.end();
        batch.begin();

        
        font.setColor(Color.WHITE);
        font.draw(batch, shownText, boxX + 25, boxY + boxHeight - 25,
            boxWidth - 50, -1, true);

        
        if (visibleChars >= fullText.length()) {
            font.setColor(Color.GRAY);
            font.draw(batch, "[Enter]", boxX + boxWidth - 90, boxY + 30);
        }
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
