package com.hollowknight.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.hollowknight.HollowKnight;
import com.hollowknight.i18n.Text;
import com.hollowknight.model.Charms.Charm;
import com.hollowknight.model.Charms.CharmInventory;
import com.hollowknight.view.ui.Theme;

import java.util.List;

public class CharmMenu {
    private static final int COLS = 4;
    private static final float ICON_SIZE = 72f;
    private static final float GAP = 26f;
    private static final float TILE_PAD = 8f;
    private static final float GRID_PAD = 34f;
    private static final float PANEL_Y = 140f;
    private static final float PANEL_H = 380f;
    private static final float GRID_PANEL_X = 130f;
    private static final float DETAIL_PANEL_X = 640f;
    private static final float DETAIL_PANEL_W = 510f;

    private final HollowKnight game;
    private final CharmInventory inventory;
    private final ObjectMap<Charm, Texture> icons = new ObjectMap<>();
    private final List<Charm> charms;

    private boolean open = false;
    private int selectedIndex = 0;

    public CharmMenu(HollowKnight game, CharmInventory inventory) {
        this.game = game;
        this.inventory = inventory;
        this.charms = inventory.getOwnedCharms();

        for (Charm charm : charms) {
            try {
                icons.put(charm, new Texture(Gdx.files.internal(charm.getIconPath())));
            } catch (Exception e) {
                Gdx.app.log("CharmMenu", "Icon not found: " + charm.getIconPath());
            }
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            open = !open;
        }

        if (!open || charms.isEmpty()) return;

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

        batch.setProjectionMatrix(uiProjection);
        batch.begin();

        batch.setColor(0f, 0f, 0f, 0.82f);
        batch.draw(Theme.pixel(), 0, 0, screenW, screenH);
        batch.setColor(Color.WHITE);

        drawText(batch, Theme.font(46), Text.CHARM_TITLE.get(), 0f, screenH - 44f, screenW, Align.center, Color.WHITE);
        float dividerW = 460f;
        batch.draw(Theme.dividerTexture(), (screenW - dividerW) / 2f, screenH - 124f, dividerW, 13f);

        if (charms.isEmpty()) {
            drawText(batch, Theme.font(20), Text.CHARM_EMPTY.get(), 0f, screenH / 2f, screenW, Align.center, Theme.MUTED);
            batch.end();
            return;
        }

        drawGrid(batch);
        drawDetails(batch);

        drawText(batch, Theme.font(16), Text.CHARM_HINT.get(), 0f, 84f, screenW, Align.center, Theme.MUTED);

        batch.setColor(Color.WHITE);
        batch.end();
    }

    private void drawGrid(Batch batch) {
        int rows = (charms.size() + COLS - 1) / COLS;
        float gridW = COLS * ICON_SIZE + (COLS - 1) * GAP;
        float gridH = rows * ICON_SIZE + (rows - 1) * GAP;
        float panelW = gridW + GRID_PAD * 2f;

        Theme.panel().draw(batch, GRID_PANEL_X, PANEL_Y, panelW, PANEL_H);

        float gridLeft = GRID_PANEL_X + GRID_PAD;
        float gridTop = PANEL_Y + PANEL_H - GRID_PAD;

        for (int i = 0; i < charms.size(); i++) {
            Charm charm = charms.get(i);
            int col = i % COLS;
            int row = i / COLS;
            float x = gridLeft + col * (ICON_SIZE + GAP);
            float y = gridTop - ICON_SIZE - row * (ICON_SIZE + GAP);
            boolean equipped = inventory.getEquippedCharms().contains(charm);
            boolean selected = i == selectedIndex;

            batch.setColor(Color.WHITE);
            if (selected) {
                Theme.chipOn().draw(batch, x - TILE_PAD, y - TILE_PAD, ICON_SIZE + TILE_PAD * 2f, ICON_SIZE + TILE_PAD * 2f);
            } else {
                Theme.card().draw(batch, x - TILE_PAD, y - TILE_PAD, ICON_SIZE + TILE_PAD * 2f, ICON_SIZE + TILE_PAD * 2f);
            }

            Texture icon = icons.get(charm);
            if (icon != null) {
                if (equipped) {
                    batch.setColor(1f, 1f, 1f, 1f);
                } else {
                    batch.setColor(0.45f, 0.45f, 0.45f, 1f);
                }
                batch.draw(icon, x, y, ICON_SIZE, ICON_SIZE);
            }

            if (equipped) {
                batch.setColor(Theme.GOLD);
                batch.draw(Theme.pixel(), x + 6f, y - TILE_PAD - 7f, ICON_SIZE - 12f, 3f);
            }
        }
        batch.setColor(Color.WHITE);
    }

    private void drawDetails(Batch batch) {
        Charm selected = charms.get(selectedIndex);
        boolean equipped = inventory.getEquippedCharms().contains(selected);

        Theme.panel().draw(batch, DETAIL_PANEL_X, PANEL_Y, DETAIL_PANEL_W, PANEL_H);

        float left = DETAIL_PANEL_X + 36f;
        float textW = DETAIL_PANEL_W - 72f;
        float y = PANEL_Y + PANEL_H - 40f;

        drawText(batch, Theme.font(30), selected.getDisplayName(), left, y, textW, Align.left, Theme.GOLD);
        y -= 46f;
        batch.setColor(1f, 1f, 1f, 0.8f);
        batch.draw(Theme.dividerTexture(), left, y, textW, 13f);
        batch.setColor(Color.WHITE);
        y -= 28f;

        BitmapFont body = Theme.font(20);
        body.setColor(Theme.TEXT);
        GlyphLayout layout = new GlyphLayout(body, selected.getDescription(), Theme.TEXT, textW, Align.left, true);
        body.draw(batch, layout, left, y);
        body.setColor(Color.WHITE);

        float statusY = PANEL_Y + 112f;
        drawText(batch, Theme.font(20), equipped ? Text.CHARM_EQUIPPED.get() : Text.CHARM_NOT_EQUIPPED.get(),
            left, statusY, textW, Align.left, equipped ? Theme.GOOD : Theme.MUTED);

        int used = inventory.getUsedNotches();
        int max = inventory.getMaxNotches();
        drawText(batch, Theme.font(18), Text.CHARM_NOTCHES.get() + "  " + used + " / " + max,
            left, statusY - 40f, textW, Align.left, Theme.TEXT);

        float pipX = left;
        float pipY = statusY - 100f;
        for (int i = 0; i < max; i++) {
            Theme.chip().draw(batch, pipX, pipY, 28f, 28f);
            if (i < used) {
                batch.setColor(Theme.GOLD);
                batch.draw(Theme.pixel(), pipX + 7f, pipY + 7f, 14f, 14f);
                batch.setColor(Color.WHITE);
            }
            pipX += 38f;
        }
    }

    private void drawText(Batch batch, BitmapFont font, String text, float x, float y, float width, int align, Color color) {
        font.setColor(color);
        font.draw(batch, text, x, y, width, align, false);
        font.setColor(Color.WHITE);
    }

    public void dispose() {
        for (Texture texture : icons.values()) {
            if (texture != null) texture.dispose();
        }
    }
}
