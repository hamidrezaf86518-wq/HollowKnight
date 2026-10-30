package com.hollowknight.model;

import com.hollowknight.i18n.Text;

public enum Achievement {
    COMPLETION(Text.ACH_COMPLETION, Text.ACH_COMPLETION_DESC),
    SPEEDRUN(Text.ACH_SPEEDRUN, Text.ACH_SPEEDRUN_DESC),
    TRUE_HUNTER(Text.ACH_TRUE_HUNTER, Text.ACH_TRUE_HUNTER_DESC),
    DEFEAT_FALSE_KNIGHT(Text.ACH_DEFEAT_FALSE_KNIGHT, Text.ACH_DEFEAT_FALSE_KNIGHT_DESC),
    FLAWLESS_VICTORY(Text.ACH_FLAWLESS, Text.ACH_FLAWLESS_DESC);

    private final Text title;
    private final Text description;

    Achievement(Text title, Text description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title.get();
    }

    public String getDescription() {
        if (this == SPEEDRUN) {
            return description.fmt(Constants.SPEEDRUN_TARGET_SECONDS / 60);
        }
        return description.get();
    }
}
