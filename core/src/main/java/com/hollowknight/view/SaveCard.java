package com.hollowknight.view;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hollowknight.HollowKnight;
import com.hollowknight.view.screens.GameScreen;


public class SaveCard extends Table{
    private final HollowKnight hollowKnight;

    public SaveCard(Skin skin, HollowKnight hollowKnight, int slot) {
        super(skin);
        this.hollowKnight = hollowKnight;
        this.defaults().pad(20).padTop(30);

        boolean occupied = hollowKnight.saveSystem.slotExists(slot);

        Label statusLabel = new Label(occupied ? "SAVE FOUND" : "EMPTY SLOT", skin);
        TextButton newBtn = new TextButton("NEW", skin);
        TextButton loadBtn = new TextButton("LOAD", skin);
        loadBtn.setDisabled(!occupied);
        loadBtn.setTouchable(occupied ? com.badlogic.gdx.scenes.scene2d.Touchable.enabled
            : com.badlogic.gdx.scenes.scene2d.Touchable.disabled);

        this.add(statusLabel).colspan(2).row();
        this.add(newBtn).size(100,35);
        this.add(loadBtn).size(100,35);


        newBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hollowKnight.saveSystem.deleteSlot(slot);
                GameScreen gameScreen = new GameScreen(hollowKnight);
                gameScreen.setSlot(slot);
                hollowKnight.setScreen(gameScreen);
            }
        });

        loadBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(hollowKnight.saveSystem.slotExists(slot)) {
                    GameScreen gameScreen = new GameScreen(hollowKnight, hollowKnight.saveSystem.load(slot));
                    gameScreen.setSlot(slot);
                    hollowKnight.setScreen(gameScreen);
                }
            }
        });
    }
}
