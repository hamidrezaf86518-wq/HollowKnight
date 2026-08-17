package com.hollowknight.controller.Systems;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;


public class AudioSystem {

    private final AssetSystem assets;

    private Music currentBGM;
    private String currentBGMName = null;

    private float musicVolume = 0.7f;
    private float sfxVolume   = 1f;
    private boolean musicMuted = false;
    private boolean sfxMuted   = false;


    private Music nextBGM;
    private String nextBGMName;
    private boolean fadingOut = false;
    private boolean fadingIn  = false;
    private float fadeSpeed = 1.2f;
    private float currentFadeVolume = 0f;

    public AudioSystem(AssetSystem assets) {
        this.assets = assets;
    }






    public void playEnvironmentBGM(String environmentName) {
        if (environmentName == null) return;


        if (environmentName.equals(currentBGMName)) return;

        if (environmentName.equals(nextBGMName)) return;

        String path = getBGMPath(environmentName);
        if (path == null) return;

        Music target = assets.get(path, Music.class);
        target.setLooping(true);

        if (currentBGM == null) {

            currentBGM = target;
            currentBGMName = environmentName;
            currentFadeVolume = 0f;
            currentBGM.setVolume(0f);
            currentBGM.play();
            fadingIn = true;
            fadingOut = false;
        } else {

            nextBGM = target;
            nextBGMName = environmentName;
            fadingOut = true;
            fadingIn = false;
        }
    }

    private String getBGMPath(String environmentName) {
        switch (environmentName) {
            case "MainMenu":       return "music/MainMenu.mp3";
            case "CrystalPeaks":   return "music/CrystalPeaks.mp3";
            case "ForgottenCrossroads":      return "music/Crossroads.mp3";
            case "Victory":        return "music/Victory.mp3";
            case "BossFight":      return "music/FalseKnight.mp3";
            default:               return null;
        }
    }


    public void update(float delta) {
        float targetVol = musicMuted ? 0f : musicVolume;


        if (fadingOut && currentBGM != null) {
            currentFadeVolume -= delta * fadeSpeed;
            if (currentFadeVolume <= 0f) {
                currentFadeVolume = 0f;
                currentBGM.stop();


                currentBGM = nextBGM;
                currentBGMName = nextBGMName;
                nextBGM = null;
                nextBGMName = null;
                fadingOut = false;

                if (currentBGM != null) {
                    currentBGM.setVolume(0f);
                    currentBGM.play();
                    fadingIn = true;
                }
            } else {
                currentBGM.setVolume(currentFadeVolume * targetVol);
            }
        }


        if (fadingIn && currentBGM != null) {
            currentFadeVolume += delta * fadeSpeed;
            if (currentFadeVolume >= 1f) {
                currentFadeVolume = 1f;
                fadingIn = false;
            }
            currentBGM.setVolume(currentFadeVolume * targetVol);
        }
    }






    public void playSFX(String sfxKey) {
        if (sfxMuted) return;
        String path = getSFXPath(sfxKey);
        if (path == null) return;
        try {
            Sound s = assets.get(path, Sound.class);
            s.play(sfxVolume);
        } catch (Exception e) {

        }
    }

    private String getSFXPath(String sfxKey) {
        switch (sfxKey) {
            case "nail_slash": return "sfx/nail_slash.wav";
            case "damage":     return "sfx/damage.wav";
            case "soul_gain":  return "sfx/soul_gain.wav";
            case "focus":      return "sfx/focus.wav";
            default:           return null;
        }
    }





    private final String[] zoteGrunts = {
        "sfx/zote/zote_1.wav",
        "sfx/zote/zote_2.wav",
        "sfx/zote/zote_3.wav",
        "sfx/zote/zote_4.wav"
    };


    public void playZoteGrunt() {
        if (sfxMuted) return;
        String path = zoteGrunts[MathUtils.random(zoteGrunts.length - 1)];
        try {
            Sound s = assets.get(path, Sound.class);
            s.play(sfxVolume);
        } catch (Exception e) {

        }
    }





    public void setMusicVolume(float v) {
        musicVolume = v;
        if (currentBGM != null && !musicMuted && !fadingIn && !fadingOut) {
            currentBGM.setVolume(v);
        }
    }

    public void toggleMusic() {
        musicMuted = !musicMuted;
        if (currentBGM != null) {
            currentBGM.setVolume(musicMuted ? 0 : musicVolume);
        }
    }

    public void toggleSFX() { sfxMuted = !sfxMuted; }

    public float getMusicVolume() { return musicVolume; }
    public float getSfxVolume()   { return sfxVolume; }
    public void setSfxVolume(float v) { sfxVolume = v; }
    public boolean isMusicMuted() { return musicMuted; }
    public boolean isSfxMuted()   { return sfxMuted; }

    public String getCurrentBGMName() { return currentBGMName; }

    public void dispose() {
        if (currentBGM != null) currentBGM.stop();

    }
}
