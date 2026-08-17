package com.hollowknight.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.hollowknight.model.Knight;
import com.hollowknight.view.screens.GameScreen;

import static com.hollowknight.model.KeyBinding.*;


public class KnightController {
    public static void handleInput(Knight knight, float dt){
        if (knight.isKnockedBack()) return;

        if (Gdx.input.isKeyPressed(FOCUS) && knight.isOnGround()) {
            knight.setFocusPressed(true);
            knight.previousState = knight.currentState;
            knight.currentState = Knight.State.FOCUS;
            return;
        } else {
            knight.setFocusPressed(false);
        }


        if (Gdx.input.isKeyJustPressed(JUMP) && !knight.isOnGround()) {
            knight.jump();
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            if (knight.getScreen().getSpellManager().castVengefulSpirit()) {
                knight.lockMovementForCast();
            }
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            if (knight.getScreen().getSpellManager().castHowlingWraiths()) {
                knight.lockMovementForCast();
            }
        }


        if (Gdx.input.isKeyPressed(JUMP) && knight.isOnGround()) {
            knight.setPressed(true);
        } else {
            knight.setPressed(false);
        }



        boolean pogoTriggered = false;
        if (!knight.isOnGround() && Gdx.input.isKeyPressed(FACE_DOWN) && Gdx.input.isKeyJustPressed(ATTACK)) {
            knight.doPogo();
            pogoTriggered = true;
        }

        if (Gdx.input.isKeyJustPressed(PAUSE)) {
            ((GameScreen) knight.getGame().getScreen()).setPaused(true);
        } else if (pogoTriggered) {

        } else if (Gdx.input.isKeyPressed(MOVE_RIGHT) && Gdx.input.isKeyJustPressed(DASH)) {
            knight.setFace(1);
            if (knight.getCharmInventory().hasSharpShadow()) knight.shadowDash();
            else knight.dash();
        } else if (Gdx.input.isKeyPressed(MOVE_LEFT) && Gdx.input.isKeyJustPressed(DASH)) {
            knight.setFace(-1);
            if (knight.getCharmInventory().hasSharpShadow()) knight.shadowDash();
            else knight.dash();
        } else if (Gdx.input.isKeyPressed(MOVE_RIGHT) && Gdx.input.isKeyJustPressed(ATTACK)) {
            knight.setFace(1);
            knight.attack();
        } else if (Gdx.input.isKeyPressed(MOVE_LEFT) && Gdx.input.isKeyJustPressed(ATTACK)) {
            knight.setFace(-1);
            knight.attack();
        } else if (Gdx.input.isKeyPressed(MOVE_RIGHT) && knight.isOnRWall() && !knight.isOnGround()) {
            knight.setFace(1);
            knight.wallSlide();
        } else if (Gdx.input.isKeyPressed(MOVE_LEFT) && knight.isOnLWall() && !knight.isOnGround()) {
            knight.setFace(-1);
            knight.wallSlide();
        } else if (Gdx.input.isKeyPressed(MOVE_RIGHT)) {
            knight.runRight();
        } else if (Gdx.input.isKeyPressed(MOVE_LEFT)) {
            knight.runLeft();
        } else if (Gdx.input.isKeyJustPressed(DASH)) {
            if (knight.getCharmInventory().hasSharpShadow()) knight.shadowDash();
            else knight.dash();
        } else if (Gdx.input.isKeyJustPressed(FACE_UP)) {
            knight.setFace(2);
        } else if (Gdx.input.isKeyJustPressed(FACE_DOWN)) {
            knight.setFace(-2);
        } else if (Gdx.input.isKeyJustPressed(ATTACK)) {
            knight.attack();
        } else if (!knight.isPressed() && knight.isOnGround() && !knight.isJumping()) {
            knight.previousState = knight.currentState;
            knight.currentState = Knight.State.IDLE;
            knight.body.setLinearVelocity(0, 0);
        }
    }
}
