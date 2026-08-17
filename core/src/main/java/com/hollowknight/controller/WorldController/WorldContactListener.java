package com.hollowknight.controller.WorldController;

import com.badlogic.gdx.physics.box2d.*;
import com.hollowknight.model.Effects.Laser;
import com.hollowknight.model.Enemies.Enemy;
import com.hollowknight.model.Enemies.Mosquito;
import com.hollowknight.model.FalseKnight;
import com.hollowknight.model.Knight;
import com.hollowknight.model.Nail;
import com.hollowknight.model.Spells.HowlingWraiths;
import com.hollowknight.model.Spells.VengefulSpirit;
import com.hollowknight.model.Zote;
import com.hollowknight.view.Scenes.Hud;
import com.hollowknight.view.screens.GameScreen;

import static com.hollowknight.model.Constants.*;


public class WorldContactListener implements ContactListener {
    private Hud hud;
    private Knight knight;
    private GameScreen screen;

    public WorldContactListener(Hud hud, Knight knight, GameScreen screen) {
        this.screen = screen;
        this.knight = knight;
        this.hud = hud;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case KNIGHT_BIT | RIGHT_WALL_BIT:
                knight.setOnRWall(true);
                break;
            case KNIGHT_BIT | LEFT_WALL_BIT:
                knight.setOnLWall(true);
                break;
            case KNIGHT_BIT | GROUND_BIT:
                knight.setOnGround(true);
                break;
            case ENEMY_BIT | OBSTACLE_BIT:
                if(fixA.getFilterData().categoryBits == ENEMY_BIT) {
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                    if(fixA.getUserData().getClass() == Mosquito.class){
                        ((Mosquito)fixA.getUserData()).stopAttacking();
                    }
                }
                else{
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                    if(fixB.getUserData().getClass() == Mosquito.class){
                        ((Mosquito)fixB.getUserData()).stopAttacking();
                    }
                }
                break;
            case ENEMY_BIT | KNIGHT_BIT:


                if(!knight.isShadowDashing() && !knight.isPogoActive()) {
                    if (fixA.getFilterData().categoryBits == KNIGHT_BIT) {
                        Knight k = (Knight) fixA.getBody().getUserData();
                        float sourceX = fixB.getBody().getPosition().x;
                        k.takeDamage(sourceX);
                    } else {
                        Knight k = (Knight) fixB.getBody().getUserData();
                        float sourceX = fixA.getBody().getPosition().x;
                        k.takeDamage(sourceX);
                    }
                }
                break;
            case KNIGHT_BIT | OBSTACLE_BIT:
                knight.setOnGround(true);
                break;
            case KNIGHT_BIT | MOSQUITO_BIT:
                float x , y;
                if(fixA.getFilterData().categoryBits == MOSQUITO_BIT) {
                    x = fixB.getBody().getPosition().x;
                    y = fixB.getBody().getPosition().y;
                    ((Mosquito) fixA.getUserData()).setStartAttack(true,x,y);
                }
                else {
                    x = fixA.getBody().getPosition().x;
                    y = fixA.getBody().getPosition().y;
                    ((Mosquito) fixB.getUserData()).setStartAttack(true,x,y);
                }
                break;
            case BOSS_GROUND_BIT | KNIGHT_BIT:
                knight.setOnGround(true);
                if(screen.getBoss() != null && !screen.getBoss().isDead()){
                    screen.getBoss().setActive(true);
                    screen.requestLockBossArena();
                }
                break;
            case SHOCK_WAVE_BIT | KNIGHT_BIT:
                if (fixA.getFilterData().categoryBits == KNIGHT_BIT) {
                    Knight k = (Knight) fixA.getBody().getUserData();
                    float sourceX = fixB.getBody().getPosition().x;
                    k.takeDamage(sourceX);
                } else {
                    Knight k = (Knight) fixB.getBody().getUserData();
                    float sourceX = fixA.getBody().getPosition().x;
                    k.takeDamage(sourceX);
                }
                break;
            case SHADOW_DASH_BIT | ENEMY_BIT:
                handleShadowDashEnemy(fixA, fixB);
                break;

            case SHADOW_DASH_BIT | FALSE_KNIGHT_BIT:
                handleShadowDashBoss(fixA, fixB);
                break;

            case LASER_BIT | RIGHT_WALL_BIT:
            case LASER_BIT | LEFT_WALL_BIT:
            case LASER_BIT | OBSTACLE_BIT:
                if(fixA.getFilterData().categoryBits == LASER_BIT)
                    ((Laser)fixA.getUserData()).setDestroyed(true);
                else
                    ((Laser)fixB.getUserData()).setDestroyed(true);
                break;
            case LASER_BIT | KNIGHT_BIT:
                Laser laser;
                Fixture laserFix;
                if(fixA.getFilterData().categoryBits == LASER_BIT) {
                    laser = (Laser) fixA.getUserData();
                    laserFix = fixA;
                } else {
                    laser = (Laser) fixB.getUserData();
                    laserFix = fixB;
                }

                if(!laser.isCollidWithKnight()){
                    float sourceX = laserFix.getBody().getPosition().x;
                    knight.takeDamage(sourceX);
                    laser.setCollidWithKnight(true);
                }
                break;
            case SPIKE_BIT | KNIGHT_BIT:

                if(!knight.isPogoActive()) {
                    if (fixA.getFilterData().categoryBits == KNIGHT_BIT) {
                        Knight k = (Knight) fixA.getBody().getUserData();
                        float sourceX = fixB.getBody().getPosition().x;
                        k.takeDamage(sourceX);
                    } else {
                        Knight k = (Knight) fixB.getBody().getUserData();
                        float sourceX = fixA.getBody().getPosition().x;
                        k.takeDamage(sourceX);
                    }
                }
                break;
            case NAIL_BIT | ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == NAIL_BIT) {
                    if(!(fixB.getUserData() instanceof Zote)) {
                        Knight k = ((Nail) fixA.getBody().getUserData()).getKnight();
                        k.hitEnemey((Enemy) fixB.getUserData());
                        if(k.getCurrentState() == Knight.State.POGO) k.pogoBounce();
                    } else {
                        ((Zote) fixB.getUserData()).onHit();
                    }
                } else {
                    if(!(fixA.getUserData() instanceof Zote)) {
                        Knight k = ((Nail) fixB.getBody().getUserData()).getKnight();
                        k.hitEnemey((Enemy) fixA.getUserData());
                        if(k.getCurrentState() == Knight.State.POGO) k.pogoBounce();
                    } else {
                        ((Zote) fixA.getUserData()).onHit();
                    }
                }
                break;

            case NAIL_BIT | FALSE_KNIGHT_BIT:
                if(fixA.getFilterData().categoryBits == NAIL_BIT) {
                    Knight k = ((Nail)fixA.getBody().getUserData()).getKnight();
                    k.hitFalseKnight((FalseKnight) fixB.getUserData());
                    if(k.getCurrentState() == Knight.State.POGO) k.pogoBounce();
                } else {
                    Knight k = ((Nail)fixB.getBody().getUserData()).getKnight();
                    k.hitFalseKnight((FalseKnight) fixA.getUserData());
                    if(k.getCurrentState() == Knight.State.POGO) k.pogoBounce();
                }
                break;
            case NAIL_BIT | SPIKE_BIT:

                if(fixA.getFilterData().categoryBits == NAIL_BIT){
                    Knight k = ((Nail)fixA.getBody().getUserData()).getKnight();

                    if(k.getCurrentState() == Knight.State.POGO) {

                        k.pogoBounce();
                    }
                } else {
                    Knight k = ((Nail)fixB.getBody().getUserData()).getKnight();

                    if(k.getCurrentState() == Knight.State.POGO) {

                        k.pogoBounce();
                    }
                }
                break;
            case SPELL_BIT | RIGHT_WALL_BIT:
            case SPELL_BIT | LEFT_WALL_BIT:
            case SPELL_BIT | OBSTACLE_BIT:
                Object spell = (fixA.getFilterData().categoryBits == SPELL_BIT)
                    ? fixA.getUserData() : fixB.getUserData();
                if (spell instanceof VengefulSpirit) {
                    ((VengefulSpirit) spell).setDestroyed(true);
                }
                break;

            case SPELL_BIT | ENEMY_BIT:
            case SPELL_BIT | MOSQUITO_BIT:
                Fixture spellFix = (fixA.getFilterData().categoryBits == SPELL_BIT) ? fixA : fixB;
                Fixture enemyFix = (spellFix == fixA) ? fixB : fixA;
                Object spellData = spellFix.getUserData();
                Object enemyData = enemyFix.getUserData();

                if (enemyData instanceof Enemy) {
                    if (spellData instanceof VengefulSpirit) {
                        ((Enemy) enemyData).takeDamage(((VengefulSpirit) spellData).getDamage());
                    } else if (spellData instanceof HowlingWraiths) {
                        ((Enemy) enemyData).takeDamage(((HowlingWraiths) spellData).getDamage());
                    }
                }
                break;

            case SPELL_BIT | FALSE_KNIGHT_BIT:
                spellFix = (fixA.getFilterData().categoryBits == SPELL_BIT) ? fixA : fixB;
                Fixture bossFix = (spellFix == fixA) ? fixB : fixA;
                spellData = spellFix.getUserData();
                if (bossFix.getUserData() instanceof FalseKnight) {
                    FalseKnight boss = (FalseKnight) bossFix.getUserData();
                    if (spellData instanceof VengefulSpirit)
                        boss.takeDamage(((VengefulSpirit) spellData).getDamage());
                    else if (spellData instanceof HowlingWraiths)
                        boss.takeDamage(((HowlingWraiths) spellData).getDamage());
                }
                break;
            default:

        }
    }

    private void handleShadowDashEnemy(Fixture fixA, Fixture fixB) {
        Fixture shadowFix, enemyFix;

        if ("shadowDash".equals(fixA.getUserData())) {
            shadowFix = fixA;
            enemyFix = fixB;
        } else {
            shadowFix = fixB;
            enemyFix = fixA;
        }

        Object enemyData = enemyFix.getUserData();
        if (!(enemyData instanceof Enemy)) return;

        Knight k = (Knight) shadowFix.getBody().getUserData();
        Enemy enemy = (Enemy) enemyData;

        if (k.isShadowDashing() && k.canHitByShadowDash(enemy)) {
            enemy.takeDamage(50);
            k.registerShadowDashHit(enemy);
        }
    }

    private void handleShadowDashBoss(Fixture fixA, Fixture fixB) {
        Fixture shadowFix, bossFix;

        if ("shadowDash".equals(fixA.getUserData())) {
            shadowFix = fixA;
            bossFix = fixB;
        } else {
            shadowFix = fixB;
            bossFix = fixA;
        }

        Object bossData = bossFix.getUserData();
        if (!(bossData instanceof FalseKnight)) return;

        Knight k = (Knight) shadowFix.getBody().getUserData();
        FalseKnight boss = (FalseKnight) bossData;

        if (k.isShadowDashing() && k.canHitByShadowDash(boss)) {
            boss.takeDamage(50);
            k.registerShadowDashHit(boss);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case KNIGHT_BIT | GROUND_BIT:
                knight.setOnGround(false);
                break;
            case KNIGHT_BIT | OBSTACLE_BIT:
                knight.setOnGround(false);
                break;
            case KNIGHT_BIT | BOSS_GROUND_BIT:
                knight.setOnGround(false);
                break;

            case KNIGHT_BIT | RIGHT_WALL_BIT:
                knight.setOnRWall(false);
                break;
            case KNIGHT_BIT | LEFT_WALL_BIT:
                knight.setOnLWall(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
