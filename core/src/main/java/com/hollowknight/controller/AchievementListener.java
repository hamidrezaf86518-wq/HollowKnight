package com.hollowknight.controller;


import com.hollowknight.model.Achievement;


public interface AchievementListener {
    void onAchievementUnlocked(Achievement achievement);
}
