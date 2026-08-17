package com.hollowknight.model.Spells;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.hollowknight.HollowKnight;
import com.hollowknight.model.Knight;
import com.hollowknight.view.screens.GameScreen;

import static com.hollowknight.model.Constants.*;

public class SpellManager {

    private HollowKnight game;
    private World world;
    private Knight knight;
    private GameScreen screen;

    private Array<VengefulSpirit> spirits = new Array<>();
    private Array<HowlingWraiths> wraiths = new Array<>();

    private static final int SOUL_COST = 33; 

    public SpellManager(HollowKnight game, World world, Knight knight, GameScreen screen) {
        this.game = game;
        this.world = world;
        this.knight = knight;
        this.screen = screen;
    }

    public boolean castVengefulSpirit() {
        if (knight.getSouls() < SOUL_COST) return false;

        knight.setSouls(knight.getSouls() - SOUL_COST);
        screen.getHud().onSoulChange(knight.getSouls(), 99);

        boolean voidHeart = knight.getCharmInventory().hasVoidHeart();
        float x = knight.getBody().getPosition().x;
        float y = knight.getBody().getPosition().y;
        spirits.add(new VengefulSpirit(game, world, x, y, knight.getFace(), voidHeart));
        return true;
    }

    public boolean castHowlingWraiths() {
        if (knight.getSouls() < SOUL_COST) return false;

        knight.setSouls(knight.getSouls() - SOUL_COST);
        screen.getHud().onSoulChange(knight.getSouls(), 99);

        boolean voidHeart = knight.getCharmInventory().hasVoidHeart();
        float x = knight.getBody().getPosition().x;
        float y = knight.getBody().getPosition().y;
        wraiths.add(new HowlingWraiths(game, world, x, y, voidHeart));
        return true;
    }

    public void update(float dt) {
        
        for (int i = spirits.size - 1; i >= 0; i--) {
            VengefulSpirit s = spirits.get(i);
            s.update(dt);
            if (s.isDestroyed()) {
                if (s.getBody() != null) screen.addBodyToDestroy(s.getBody());
                spirits.removeIndex(i);
            }
        }

        
        for (int i = wraiths.size - 1; i >= 0; i--) {
            HowlingWraiths w = wraiths.get(i);
            w.update(dt);
            if (w.isDestroyed()) {
                if (w.getBody() != null) screen.addBodyToDestroy(w.getBody());
                wraiths.removeIndex(i);
            }
        }
    }

    public void draw(Batch batch) {
        for (VengefulSpirit s : spirits) s.draw(batch);
        for (HowlingWraiths w : wraiths) w.draw(batch);
    }
}
