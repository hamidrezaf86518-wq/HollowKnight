package com.hollowknight.controller.Systems;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.hollowknight.model.GameData;


public class SaveSystem {
    private static final String SAVE_DIR = "saves/";
    public static final int SLOT_COUNT = 4;

    private final Json json;

    public SaveSystem() {
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);
    }

    private FileHandle fileFor(int slot) {
        return Gdx.files.local(SAVE_DIR + "slot" + slot + ".json");
    }


    public void save(int slot, GameData data) {
        data.slot = slot;
        data.savedAt = System.currentTimeMillis();
        FileHandle file = fileFor(slot);
        file.writeString(json.prettyPrint(data), false);
    }


    public GameData load(int slot) {
        FileHandle file = fileFor(slot);
        if (!file.exists()) return null;
        try {
            return json.fromJson(GameData.class, file);
        } catch (Exception e) {
            Gdx.app.error("SaveSystem", "Failed to load slot " + slot, e);
            return null;
        }
    }

    public boolean slotExists(int slot) {
        return fileFor(slot).exists();
    }

    public void deleteSlot(int slot) {
        FileHandle file = fileFor(slot);
        if (file.exists()) file.delete();
    }
}
