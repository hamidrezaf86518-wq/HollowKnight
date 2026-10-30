package com.hollowknight.i18n;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.Locale;

public final class I18n {
    private static final String PREFS_NAME = "hollowknight_settings";
    private static final String KEY_LANGUAGE = "language";

    private static Language current = Language.EN;

    private I18n() {
    }

    public static void init() {
        String fallback = Locale.getDefault().getLanguage().equalsIgnoreCase("fr") ? "fr" : "en";
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        current = Language.fromCode(prefs.getString(KEY_LANGUAGE, fallback));
    }

    public static Language getLanguage() {
        return current;
    }

    public static void setLanguage(Language language) {
        current = language;
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        prefs.putString(KEY_LANGUAGE, language.getCode());
        prefs.flush();
    }

    public static Locale locale() {
        return current.getLocale();
    }

    public static boolean isFrench() {
        return current == Language.FR;
    }
}
