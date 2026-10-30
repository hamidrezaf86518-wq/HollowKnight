package com.hollowknight.i18n;

public enum Text {
    GAME_START("GAME START", "JOUER"),
    GUIDE("GUIDE", "GUIDE"),
    SETTINGS("SETTINGS", "PARAMÈTRES"),
    ACHIEVEMENTS("ACHIEVEMENTS", "SUCCÈS"),
    QUIT("QUIT", "QUITTER"),
    BACK("BACK", "RETOUR"),
    CLOSE("CLOSE", "FERMER"),
    YES("YES", "OUI"),
    NO("NO", "NON"),

    SELECT_PROFILE("SELECT PROFILE", "CHOISIR UN PROFIL"),
    PROFILE("PROFILE %d", "PROFIL %d"),
    EMPTY_SLOT("Empty slot", "Emplacement vide"),
    NEW("NEW", "NOUVEAU"),
    LOAD("LOAD", "CHARGER"),
    PLAY_TIME("Play time", "Temps de jeu"),
    DEATHS("Deaths", "Morts"),
    OVERWRITE_TITLE("OVERWRITE SAVE?", "ÉCRASER LA SAUVEGARDE ?"),
    OVERWRITE_MESSAGE("The existing progress in this profile will be lost.", "La progression existante de ce profil sera perdue."),

    AUDIO("AUDIO", "AUDIO"),
    MUSIC_VOLUME("Music volume", "Volume de la musique"),
    SFX_VOLUME("Effects volume", "Volume des effets"),
    MUTE("MUTE", "COUPER"),
    UNMUTE("UNMUTE", "ACTIVER"),
    LANGUAGE("LANGUAGE", "LANGUE"),
    RESET("RESET", "RÉINITIALISER"),

    PAUSE_TITLE("PAUSED", "PAUSE"),
    RESUME("RESUME", "REPRENDRE"),
    CHEAT_CODES("CHEAT CODES", "CODES DE TRICHE"),
    SAVE_AND_QUIT("SAVE AND QUIT", "SAUVEGARDER ET QUITTER"),
    QUIT_TO_MENU("QUIT TO MENU", "RETOUR AU MENU"),
    PAUSE_HINT("Press %s to resume", "Appuyez sur %s pour reprendre"),
    CHEAT_BOSS("Teleport to Boss Arena", "Téléportation à l'arène du boss"),
    CHEAT_NOCLIP("Noclip / Spectator Mode", "Mode fantôme / spectateur"),
    CHEAT_HEAL("Emergency Heal", "Soin d'urgence"),
    CHEAT_SOUL("Refill Soul Vessel", "Recharger le réceptacle d'âme"),
    CHEAT_GOD("Toggle God Mode", "Activer / désactiver le mode dieu"),

    GUIDE_TITLE("GUIDE", "GUIDE"),
    GUIDE_CONTROLS("CONTROLS", "COMMANDES"),
    GUIDE_CONTROLS_HINT("Press a key to see it light up.", "Appuyez sur une touche pour la voir s'illuminer."),
    GUIDE_ABILITIES("ABILITIES", "CAPACITÉS"),
    GUIDE_HEALTH("HEALTH & SOUL", "VIE ET ÂME"),
    ACTION_ATTACK("Attack", "Attaquer"),
    ACTION_JUMP("Jump", "Sauter"),
    ACTION_DASH("Dash", "Foncer"),
    ACTION_MOVE_RIGHT("Move right", "Aller à droite"),
    ACTION_MOVE_LEFT("Move left", "Aller à gauche"),
    ACTION_FOCUS("Focus", "Concentration"),
    ACTION_PAUSE("Pause", "Pause"),

    ABILITY_NAIL("NAIL", "CLOU"),
    ABILITY_NAIL_DESC(
        "Your primary weapon. Strike enemies to deal damage and gather SOUL.",
        "Votre arme principale. Frappez les ennemis pour infliger des dégâts et récolter de l'ÂME."),
    ABILITY_JUMP("JUMP", "SAUT"),
    ABILITY_JUMP_DESC(
        "Leap into the air to avoid attacks and reach higher platforms.",
        "Bondissez pour éviter les attaques et atteindre les plateformes plus hautes."),
    ABILITY_DASH("SHADOW DASH", "COURSE DE L'OMBRE"),
    ABILITY_DASH_DESC(
        "Dash quickly forward. While dashing, you can pass through enemies and some obstacles without taking damage.",
        "Foncez rapidement vers l'avant. Pendant l'élan, vous traversez les ennemis et certains obstacles sans subir de dégâts."),
    ABILITY_FOCUS("FOCUS", "CONCENTRATION"),
    ABILITY_FOCUS_DESC(
        "Hold to channel SOUL and restore lost Masks. You cannot move while focusing.",
        "Maintenez pour canaliser l'ÂME et restaurer les Masques perdus. Vous ne pouvez pas bouger pendant la concentration."),
    ABILITY_SPIRIT("VENGEFUL SPIRIT", "ESPRIT VENGEUR"),
    ABILITY_SPIRIT_DESC(
        "Release gathered SOUL to fire a spell that damages enemies from a distance.",
        "Libérez l'ÂME récoltée pour lancer un sort qui blesse les ennemis à distance."),
    HEALTH_MASKS("MASKS (HEALTH)", "MASQUES (VIE)"),
    HEALTH_MASKS_DESC(
        "Your health is shown as Masks at the top. Each hit breaks one Mask. If all Masks break, you die.",
        "Votre vie est représentée par des Masques en haut de l'écran. Chaque coup en brise un. Si tous les Masques se brisent, vous mourez."),
    HEALTH_SOUL("SOUL", "ÂME"),
    HEALTH_SOUL_DESC(
        "The SOUL meter fills as you strike enemies. Gather SOUL to heal or cast spells.",
        "La jauge d'ÂME se remplit quand vous frappez les ennemis. Récoltez de l'ÂME pour vous soigner ou lancer des sorts."),
    HEALTH_HEALING("HEALING", "SOIN"),
    HEALTH_HEALING_DESC(
        "Hold FOCUS to spend SOUL and restore a Mask. You cannot move while healing.",
        "Maintenez CONCENTRATION pour dépenser de l'ÂME et restaurer un Masque. Vous ne pouvez pas bouger pendant le soin."),

    ACH_TITLE("ACHIEVEMENTS", "SUCCÈS"),
    ACH_UNLOCKED("UNLOCKED", "DÉBLOQUÉ"),
    ACH_LOCKED("LOCKED", "VERROUILLÉ"),
    ACH_POPUP("Achievement Unlocked", "Succès débloqué"),
    ACH_PROGRESS("%d / %d unlocked", "%d / %d débloqués"),
    ACH_COMPLETION("Completion", "Accomplissement"),
    ACH_COMPLETION_DESC(
        "Finish the game by defeating the False Knight.",
        "Terminez le jeu en vainquant le Faux Chevalier."),
    ACH_SPEEDRUN("Speedrun", "Speedrun"),
    ACH_SPEEDRUN_DESC(
        "Finish the game in under %d minutes.",
        "Terminez le jeu en moins de %d minutes."),
    ACH_TRUE_HUNTER("True Hunter", "Vrai chasseur"),
    ACH_TRUE_HUNTER_DESC(
        "Kill at least one of every enemy type in the game.",
        "Tuez au moins un ennemi de chaque type du jeu."),
    ACH_DEFEAT_FALSE_KNIGHT("Defeat False Knight", "Vaincre le Faux Chevalier"),
    ACH_DEFEAT_FALSE_KNIGHT_DESC(
        "Defeat the False Knight boss.",
        "Battez le boss Faux Chevalier."),
    ACH_FLAWLESS("Flawless Victory", "Victoire parfaite"),
    ACH_FLAWLESS_DESC(
        "Defeat the False Knight without dying once.",
        "Battez le Faux Chevalier sans mourir une seule fois."),

    VICTORY_TITLE("FALSE KNIGHT DEFEATED", "FAUX CHEVALIER VAINCU"),
    STAT_TIME("PLAY TIME", "TEMPS DE JEU"),
    STAT_DEATHS("DEATHS", "MORTS"),
    STAT_ENEMIES("ENEMIES DEFEATED", "ENNEMIS VAINCUS"),
    RESTART("RESTART", "RECOMMENCER"),
    MAIN_MENU("MAIN MENU", "MENU PRINCIPAL"),

    CHARM_TITLE("CHARMS", "AMULETTES"),
    CHARM_EQUIPPED("EQUIPPED", "ÉQUIPÉE"),
    CHARM_NOT_EQUIPPED("NOT EQUIPPED", "NON ÉQUIPÉE"),
    CHARM_NOTCHES("Notches", "Encoches"),
    CHARM_HINT("Arrows: select     Enter: equip / unequip     I: close", "Flèches : choisir     Entrée : équiper / retirer     I : fermer"),
    CHARM_EMPTY("No charms collected yet.", "Aucune amulette pour l'instant."),
    CHARM_QUICK_SLASH("Quick Slash", "Frappe rapide"),
    CHARM_QUICK_SLASH_DESC(
        "Increases the speed of your nail slashes.",
        "Augmente la vitesse de vos coups de clou."),
    CHARM_QUICK_FOCUS("Quick Focus", "Concentration rapide"),
    CHARM_QUICK_FOCUS_DESC(
        "Focus (heal) charges faster than before.",
        "La concentration (soin) se charge plus vite."),
    CHARM_SOUL_CATCHER("Soul Catcher", "Capteur d'âme"),
    CHARM_SOUL_CATCHER_DESC(
        "Gathers more soul with each nail strike.",
        "Récolte plus d'âme à chaque coup de clou."),
    CHARM_UNBREAKABLE_STRENGTH("Unbreakable Strength", "Force incassable"),
    CHARM_UNBREAKABLE_STRENGTH_DESC(
        "Increases the damage dealt by your nail.",
        "Augmente les dégâts infligés par votre clou."),
    CHARM_HEAVY_BLOW("Heavy Blow", "Coup lourd"),
    CHARM_HEAVY_BLOW_DESC(
        "Knocks enemies back further when hit.",
        "Repousse les ennemis plus loin lorsqu'ils sont touchés."),
    CHARM_DASHMASTER("Dashmaster", "Maître de l'élan"),
    CHARM_DASHMASTER_DESC(
        "Reduces the cooldown between dashes.",
        "Réduit le délai entre deux élans."),
    CHARM_SHARP_SHADOW("Sharp Shadow", "Ombre acérée"),
    CHARM_SHARP_SHADOW_DESC(
        "Damages enemies while performing a Shadow Dash.",
        "Blesse les ennemis pendant une Course de l'ombre."),
    CHARM_VOID_HEART("Void Heart", "Cœur du Néant"),
    CHARM_VOID_HEART_DESC(
        "Empowers your spells, dealing more damage.",
        "Renforce vos sorts et leur fait infliger plus de dégâts."),

    ENTER_PROMPT("[Enter]", "[Entrée]");

    private final String en;
    private final String fr;

    Text(String en, String fr) {
        this.en = en;
        this.fr = fr;
    }

    public String get() {
        return I18n.isFrench() ? fr : en;
    }

    public String fmt(Object... args) {
        return String.format(I18n.locale(), get(), args);
    }

    @Override
    public String toString() {
        return get();
    }
}
