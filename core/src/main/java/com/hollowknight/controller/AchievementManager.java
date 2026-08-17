package com.hollowknight.controller;


import com.badlogic.gdx.Gdx;
import com.hollowknight.model.Achievement;
import com.hollowknight.model.Enemies.CrystalCrawler;
import com.hollowknight.model.Enemies.CrystalGuardian;
import com.hollowknight.model.Enemies.Enemy;
import com.hollowknight.model.Enemies.HuskHorned;
import com.hollowknight.model.Enemies.Mosquito;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.hollowknight.model.Constants.SPEEDRUN_TARGET_SECONDS;


public class AchievementManager {

    private final Set<Achievement> unlocked = EnumSet.noneOf(Achievement.class);
    private final Set<Class<? extends Enemy>> killedEnemyTypes = new HashSet<>();
    private final List<AchievementListener> listeners = new ArrayList<>();

    
    private static final Set<Class<? extends Enemy>> REQUIRED_ENEMY_TYPES = new HashSet<>();
    static {
        REQUIRED_ENEMY_TYPES.add(CrystalCrawler.class);
        REQUIRED_ENEMY_TYPES.add(CrystalGuardian.class);
        REQUIRED_ENEMY_TYPES.add(HuskHorned.class);
        REQUIRED_ENEMY_TYPES.add(Mosquito.class);
    }

    
    public void addListener(AchievementListener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    public void removeListener(AchievementListener listener) {
        listeners.remove(listener);
    }

    
    public boolean isUnlocked(Achievement achievement) {
        return unlocked.contains(achievement);
    }

    public Set<Achievement> getUnlocked() {
        return EnumSet.copyOf(unlocked);
    }

    

    
    public void onEnemyKilled(Enemy enemy) {
        killedEnemyTypes.add(enemy.getClass());
        if (killedEnemyTypes.containsAll(REQUIRED_ENEMY_TYPES)) {
            unlock(Achievement.TRUE_HUNTER);
        }
    }

    
    public void onFalseKnightDefeated() {
        unlock(Achievement.DEFEAT_FALSE_KNIGHT);
    }

    
    public void onGameCompleted(float playTimeSeconds, int deathCount) {
        unlock(Achievement.COMPLETION);
        if (playTimeSeconds <= SPEEDRUN_TARGET_SECONDS) {
            unlock(Achievement.SPEEDRUN);
        }
        if (deathCount == 0) {
            unlock(Achievement.FLAWLESS_VICTORY);
        }
    }

    
    private void unlock(Achievement achievement) {
        if (unlocked.add(achievement)) {
            for (AchievementListener listener : listeners) {
                try {
                    listener.onAchievementUnlocked(achievement);
                } catch (Exception e) {
                    
                    
                    
                    
                    Gdx.app.error("AchievementManager", "listener failed for " + achievement, e);
                }
            }
        }
    }
}
