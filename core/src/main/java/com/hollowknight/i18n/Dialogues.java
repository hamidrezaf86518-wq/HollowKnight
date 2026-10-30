package com.hollowknight.i18n;

public final class Dialogues {
    private static final String[][] ZOTE_EN = {
        {
            "I am Zote the Mighty, a knight of great renown.",
            "Tremble before my name!",
            "You look like you could learn much from me."
        },
        {
            "Precept One: Always Win Your Battles.",
            "Losing a battle earns you nothing and teaches you nothing.",
            "Win, and you will be stronger forever."
        },
        {
            "Precept Two: Never Let Them Laugh At You.",
            "Fools laugh at greatness when they fail to understand it.",
            "Ignore them. They are beneath you."
        }
    };

    private static final String[][] ZOTE_FR = {
        {
            "Je suis Zote le Puissant, un chevalier de grande renommée.",
            "Tremblez devant mon nom !",
            "Vous semblez pouvoir apprendre beaucoup de moi."
        },
        {
            "Précepte numéro un : gagnez toujours vos batailles.",
            "Perdre une bataille ne rapporte rien et n'enseigne rien.",
            "Gagnez, et vous serez plus fort pour toujours."
        },
        {
            "Précepte numéro deux : ne laissez jamais personne se moquer de vous.",
            "Les sots rient de la grandeur quand ils ne la comprennent pas.",
            "Ignorez-les. Ils sont indignes de vous."
        }
    };

    private Dialogues() {
    }

    public static String[][] zote() {
        return I18n.isFrench() ? ZOTE_FR : ZOTE_EN;
    }
}
