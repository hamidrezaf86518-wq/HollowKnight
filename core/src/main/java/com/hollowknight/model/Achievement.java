package com.hollowknight.model;



public enum Achievement {
    COMPLETION(
        "Completion",
        "Finish the game by defeating the False Knight."
    ),
    SPEEDRUN(
        "Speedrun",
        "Finish the game in under " + (Constants.SPEEDRUN_TARGET_SECONDS / 60) + " minutes."
    ),
    TRUE_HUNTER(
        "True Hunter",
        "Kill at least one of every enemy type in the game."
    ),
    DEFEAT_FALSE_KNIGHT(
        "Defeat False Knight",
        "Defeat the False Knight boss."
    ),
    FLAWLESS_VICTORY(
        "Flawless Victory",
        "Defeat the False Knight without dying once."
    );

    private final String title;
    private final String description;

    Achievement(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
