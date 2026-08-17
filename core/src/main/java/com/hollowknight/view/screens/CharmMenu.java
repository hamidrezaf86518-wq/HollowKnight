package com.hollowknight.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ObjectMap;
import com.hollowknight.HollowKnight;
import com.hollowknight.model.Charms.Charm;
import com.hollowknight.model.Charms.CharmInventory;

import java.util.List;


public class CharmMenu {

    private final HollowKnight game;
    private final CharmInventory inventory;
    private final BitmapFont font;
    private final ShapeRenderer shapeRenderer;

    private final ObjectMap<Charm, Texture> icons = new ObjectMap<>();

    private boolean open = false;
    private int selectedIndex = 0;

    
    private List<Charm> charms;

    
    private static final int COLS = 4;
    private static final float ICON_SIZE = 64f;
    private static final float PADDING = 30f;
    private static final float START_X = 140f;
    private static final float START_Y = 520f; 

    public CharmMenu(HollowKnight game, CharmInventory inventory) {
        this.game = game;
        this.inventory = inventory;
        this.charms = inventory.getOwnedCharms();

        font = new BitmapFont();
        font.getData().setScale(1.1f);
        shapeRenderer = new ShapeRenderer();

        
        for (Charm c : charms) {
            try {
                icons.put(c, new Texture(Gdx.files.internal(c.getIconPath())));
            } catch (Exception e) {
                Gdx.app.log("CharmMenu", "Icon not found: " + c.getIconPath());
            }
        }
    }

    public boolean isOpen() { return open; }

    
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            open = !open;
        }

        if (!open) return;

        if (charms.isEmpty()) return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            selectedIndex = (selectedIndex + 1) % charms.size();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            selectedIndex = (selectedIndex - 1 + charms.size()) % charms.size();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex = Math.min(charms.size() - 1, selectedIndex + COLS);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex = Math.max(0, selectedIndex - COLS);
        }

        
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            Charm selected = charms.get(selectedIndex);
            if (inventory.getEquippedCharms().contains(selected)) {
                inventory.unequipCharm(selected);
            } else {
                inventory.equipCharm(selected); 
            }
        }
    }

    
    public void render(Batch batch, Matrix4 uiProjection, float screenW, float screenH) {
        if (!open) return;
        if (charms.isEmpty()) return;

        Gdx.gl.glEnable(GL20.GL_BLEND);

        
        shapeRenderer.setProjectionMatrix(uiProjection);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.78f);
        shapeRenderer.rect(0, 0, screenW, screenH);
        shapeRenderer.end();

        
        int selCol = selectedIndex % COLS;
        int selRow = selectedIndex / COLS;
        float selX = START_X + selCol * (ICON_SIZE + PADDING);
        float selY = START_Y - selRow * (ICON_SIZE + PADDING);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GOLD);
        shapeRenderer.rect(selX - 4, selY - 4, ICON_SIZE + 8, ICON_SIZE + 8);
        shapeRenderer.end();

        
        batch.setProjectionMatrix(uiProjection);
        batch.begin();

        for (int i = 0; i < charms.size(); i++) {
            Charm c = charms.get(i);
            int col = i % COLS;
            int row = i / COLS;
            float x = START_X + col * (ICON_SIZE + PADDING);
            float y = START_Y - row * (ICON_SIZE + PADDING);

            Texture icon = icons.get(c);

            if (inventory.getEquippedCharms().contains(c)) {
                batch.setColor(1, 1, 1, 1);
            } else {
                batch.setColor(0.45f, 0.45f, 0.45f, 1);
            }

            if (icon != null) {
                batch.draw(icon, x, y, ICON_SIZE, ICON_SIZE);
            }
        }
        batch.setColor(1, 1, 1, 1);

        
        Charm selected = charms.get(selectedIndex);
        float infoY = START_Y - 2 * (ICON_SIZE + PADDING) - 40;

        font.setColor(Color.GOLD);
        font.draw(batch, selected.getDisplayName(), START_X, infoY);

        font.setColor(Color.WHITE);
        font.draw(batch, selected.getDescription(), START_X, infoY - 35);

        boolean isEquipped = inventory.getEquippedCharms().contains(selected);
        font.setColor(isEquipped ? Color.GREEN : Color.GRAY);
        font.draw(batch, isEquipped ? "[ EQUIPPED ]" : "[ NOT EQUIPPED ]",
            START_X, infoY - 70);

        font.setColor(Color.CYAN);
        font.draw(batch, "Notches: " + inventory.getUsedNotches() + " / " + inventory.getMaxNotches(),
            START_X, infoY - 105);

        font.setColor(Color.LIGHT_GRAY);
        font.draw(batch, "Arrows: Select    Enter: Equip/Unequip    i: Close",
            START_X, 60);
        font.setColor(Color.WHITE);

        batch.end();   
    }

    public void dispose() {
        font.dispose();
        shapeRenderer.dispose();
        for (Texture t : icons.values()) {
            if (t != null) t.dispose();
        }
    }
}
