package com.hollowknight.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.hollowknight.model.Knight;
import com.hollowknight.view.screens.GameScreen;


public class

CheatCodeManager {

    private final GameScreen screen;

    public CheatCodeManager(GameScreen screen){
        this.screen = screen;
    }

    public void update(){
        boolean ctrl = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT);
        if(!ctrl) return;

        Knight knight = screen.knight;
        if(knight == null) return;


        if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
            teleportToBossArena();
        }


        if(Gdx.input.isKeyJustPressed(Input.Keys.N)){
            knight.setNoclip(!knight.isNoclip());
            Gdx.app.log("CHEAT", "Noclip: " + knight.isNoclip());
        }


        if(Gdx.input.isKeyJustPressed(Input.Keys.H)){
            knight.healFully();
            Gdx.app.log("CHEAT", "Emergency Heal");
        }


        if(Gdx.input.isKeyJustPressed(Input.Keys.S)){
            knight.refillSoul();
            Gdx.app.log("CHEAT", "Soul Refilled");
        }


        if(Gdx.input.isKeyJustPressed(Input.Keys.G)){
            knight.setGodMode(!knight.isGodMode());
            Gdx.app.log("CHEAT", "God Mode: " + knight.isGodMode());
        }
    }


    private void teleportToBossArena(){
        Knight knight = screen.knight;
        if(screen.getBossArena() != null && screen.getBossArena().isValid()){
            float x = screen.getBossArena().getCenterX();
            float y = screen.getBossArena().getCenterY();
            knight.teleportTo(x, y);
            Gdx.app.log("CHEAT", "Teleported to Boss Arena");
        } else if(screen.getBoss() != null){

            float x = screen.getBoss().getBody().getPosition().x + 3f;
            float y = screen.getBoss().getBody().getPosition().y;
            knight.teleportTo(x, y);
            Gdx.app.log("CHEAT", "Teleported near Boss");
        }
    }
}
