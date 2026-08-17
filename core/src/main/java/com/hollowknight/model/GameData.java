package com.hollowknight.model;



public class GameData {
    
    public int slot;

    
    public String mapPath = "maps/crystalpeaks.tmx";

    
    public float knightX;
    public float knightY;

    
    public int faceDirection = -1;

    
    public int currentMasks = 5;

    
    public float playTimeSeconds;
    public int deathCount;
    public int enemiesKilled;

    
    public long savedAt;

    public GameData() {
        
    }
}
