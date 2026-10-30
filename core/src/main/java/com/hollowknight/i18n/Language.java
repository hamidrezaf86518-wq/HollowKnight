package com.hollowknight.i18n;

import java.util.Locale;

public enum Language {
    EN("en", "ENGLISH", Locale.ENGLISH),
    FR("fr", "FRANÇAIS", Locale.FRENCH);

    private final String code;
    private final String nativeName;
    private final Locale locale;

    Language(String code, String nativeName, Locale locale) {
        this.code = code;
        this.nativeName = nativeName;
        this.locale = locale;
    }

    public String getCode() {
        return code;
    }

    public String getNativeName() {
        return nativeName;
    }

    public Locale getLocale() {
        return locale;
    }

    public static Language fromCode(String code) {
        for (Language language : values()) {
            if (language.code.equalsIgnoreCase(code)) {
                return language;
            }
        }
        return EN;
    }
}
