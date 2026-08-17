package com.hollowknight.controller.Systems;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class AssetSystem {
    private final AssetManager assetManager = new AssetManager();

    public void loadAll() {


        assetManager.load("textures/Hidden_Dreams_Logo.png", Texture.class);
        assetManager.load("textures/team_cherry_logo_main_menu.png", Texture.class);
        assetManager.load("textures/vheart_title_RU.png", Texture.class);
        assetManager.load("textures/Voidheart_menu_BG.png", Texture.class);
        assetManager.load("textures/vheart_save_Switch.png", Texture.class);
        assetManager.load("textures/Controller_Dialogue_0000_top.png", Texture.class);
        assetManager.load("textures/Controller_Dialogue_0001_bot.png", Texture.class);
        assetManager.load("textures/Knight/Dash.png",Texture.class);
        assetManager.load("textures/Knight/Double Jump.png",Texture.class);
        assetManager.load("textures/Knight/Focus.png",Texture.class);
        assetManager.load("textures/Knight/Idle.png",Texture.class);
        assetManager.load("textures/Knight/Run.png",Texture.class);
        assetManager.load("textures/Knight/Wall Slide.png",Texture.class);
        assetManager.load("textures/Knight/Fall_000.png", Texture.class);
        assetManager.load("textures/Knight/Fall_001.png", Texture.class);
        assetManager.load("textures/Knight/Fall_002.png", Texture.class);
        assetManager.load("textures/Knight/Fall_003.png", Texture.class);
        assetManager.load("textures/Knight/Fall_004.png", Texture.class);
        assetManager.load("textures/Knight/Fall_005.png", Texture.class);
        assetManager.load("textures/HUD/BreakHealth.png", Texture.class);
        assetManager.load("textures/HUD/EmptyHealth.png", Texture.class);
        assetManager.load("textures/HUD/FilledHealth.png", Texture.class);
        assetManager.load("textures/HUD/HealthRefill.png", Texture.class);
        assetManager.load("textures/HUD/HealthBar_005.png", Texture.class);
        assetManager.load("textures/CrystalCrawler/Death Air.png", Texture.class);
        assetManager.load("textures/CrystalCrawler/Death Land.png", Texture.class);
        assetManager.load("textures/CrystalCrawler/Turn.png", Texture.class);
        assetManager.load("textures/CrystalCrawler/Walk.png", Texture.class);
        assetManager.load("textures/CrystalCrawler/Death Land.png", Texture.class);
        assetManager.load("textures/HuskHorned/Attack Anticipate.png", Texture.class);
        assetManager.load("textures/HuskHorned/Attack Lunge.png", Texture.class);
        assetManager.load("textures/HuskHorned/Idle.png", Texture.class);
        assetManager.load("textures/HuskHorned/Walk.png", Texture.class);
        assetManager.load("textures/HuskHorned/Death Land.png", Texture.class);
        assetManager.load("textures/Knight/Dash Effect.png", Texture.class);
        assetManager.load("textures/Knight/Slash.png", Texture.class);
        assetManager.load("textures/Knight/DownSlash.png", Texture.class);
        assetManager.load("textures/Knight/UpSlash.png", Texture.class);
        assetManager.load("textures/Knight/Wall Slide.png", Texture.class);
        assetManager.load("textures/Knight/UpSlashEffect.png", Texture.class);
        assetManager.load("textures/Knight/DownSlashEffect.png", Texture.class);
        assetManager.load("textures/Knight/SlashEffect.png", Texture.class);
        assetManager.load("textures/Knight/Airborne.png", Texture.class);
        assetManager.load("textures/Mosquito/Attack.png", Texture.class);
        assetManager.load("textures/Mosquito/Attack Anticipate.png", Texture.class);
        assetManager.load("textures/Mosquito/Death Air.png", Texture.class);
        assetManager.load("textures/Mosquito/Death Land.png", Texture.class);
        assetManager.load("textures/Mosquito/Idle.png", Texture.class);
        assetManager.load("textures/falseknight/Attack.png", Texture.class);
        assetManager.load("textures/falseknight/Attack Antic.png", Texture.class);
        assetManager.load("textures/falseknight/Attack Recover.png", Texture.class);
        assetManager.load("textures/falseknight/Body.png", Texture.class);
        assetManager.load("textures/falseknight/DeathLand.png", Texture.class);
        assetManager.load("textures/falseknight/Idle.png", Texture.class);
        assetManager.load("textures/falseknight/Jump.png", Texture.class);
        assetManager.load("textures/falseknight/Jump Antic.png", Texture.class);
        assetManager.load("textures/falseknight/Jump Attack.png", Texture.class);
        assetManager.load("textures/falseknight/Run.png", Texture.class);
        assetManager.load("textures/falseknight/Run Antic.png", Texture.class);
        assetManager.load("textures/falseknight/Stun Recover.png", Texture.class);
        assetManager.load("textures/falseknight/Shockwave_000.png", Texture.class);
        assetManager.load("textures/falseknight/Shockwave_001.png", Texture.class);
        assetManager.load("textures/falseknight/Shockwave_002.png", Texture.class);
        assetManager.load("textures/falseknight/Shockwave_003.png", Texture.class);
        assetManager.load("textures/falseknight/Shockwave_004.png", Texture.class);
        assetManager.load("textures/falseknight/Shockwave_005.png", Texture.class);
        assetManager.load("textures/falseknight/Shockwave_006.png", Texture.class);
        assetManager.load("textures/falseknight/Shockwave_007.png", Texture.class);
        assetManager.load("textures/HUD/FilledHealthShine.png",Texture.class);
        assetManager.load("textures/Knight/Shadow Dash.png", Texture.class);
        assetManager.load("textures/CrystalGuardian/Death Land.png", Texture.class);
        assetManager.load("textures/CrystalGuardian/Evade.png", Texture.class);
        assetManager.load("textures/CrystalGuardian/Idle.png", Texture.class);
        assetManager.load("textures/CrystalGuardian/Run.png", Texture.class);
        assetManager.load("textures/CrystalGuardian/Shoot.png",Texture.class);
        assetManager.load("textures/CrystalGuardian/CrystalLaser.png", Texture.class);
        assetManager.load("textures/CrystalGuardian/LaserCircle.png", Texture.class);
        assetManager.load("textures/Zote/Attack.png",Texture.class);
        assetManager.load("textures/Zote/Idle.png", Texture.class);
        assetManager.load("textures/Zote/Talk.png", Texture.class);
        assetManager.load("textures/Zote/controller__0007_Switch_Up.png", Texture.class);
        assetManager.load("textures/HUD/soulorb.png", Texture.class);
        assetManager.load("textures/HUD/Soulorb.atlas", TextureAtlas.class);
        assetManager.load("textures/SoulBall.png", Texture.class);
        assetManager.load("textures/SoulScream.png", Texture.class);

        assetManager.load("music/MainMenu.mp3", Music.class);
        assetManager.load("music/CrystalPeaks.mp3", Music.class);
        assetManager.load("music/Crossroads.mp3", Music.class);
        assetManager.load("music/Victory.mp3", Music.class);
        assetManager.load("music/FalseKnight.mp3", Music.class);


        assetManager.load("sfx/nail_slash.wav", Sound.class);
        assetManager.load("sfx/damage.wav", Sound.class);
        assetManager.load("sfx/soul_gain.wav", Sound.class);
        assetManager.load("sfx/focus.wav", Sound.class);


        assetManager.load("sfx/zote/zote_1.wav", Sound.class);
        assetManager.load("sfx/zote/zote_2.wav", Sound.class);
        assetManager.load("sfx/zote/zote_3.wav", Sound.class);
        assetManager.load("sfx/zote/zote_4.wav", Sound.class);


    }


    public void finishLoading() { assetManager.finishLoading(); }
    public <T> T get(String path, Class<T> type) {
        return assetManager.get(path, type);
    }
}
