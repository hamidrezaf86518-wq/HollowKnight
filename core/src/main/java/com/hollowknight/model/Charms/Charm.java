package com.hollowknight.model.Charms;


public enum Charm {
    QUICK_SLASH         ("Quick Slash",          "Increases the speed of your nail slashes.",        "textures/Charms/Quick Slash - _0003_charm_nail_slash_speed_up.png"),
    QUICK_FOCUS         ("Quick Focus",          "Focus (heal) charges faster than before.",         "textures/Charms/Quick Focus - _0005_charm_fast_focus.png"),
    SOUL_CATCHER        ("Soul Catcher",         "Gathers more soul with each nail strike.",         "textures/Charms/Soul Catcher - _0001_charm_more_soul.png"),
    UNBREAKABLE_STRENGTH("Unbreakable Strength", "Increases the damage dealt by your nail.",         "textures/Charms/Unbreakable Strength_0002_charm_glass_attack_up_full.png"),
    HEAVY_BLOW         ("Heavy Blow",            "Knocks enemies back further when hit.",            "textures/Charms/Heavy Blow - _0008_charm_nail_damage_up.png"),
    DASHMASTER         ("Dashmaster",            "Reduces the cooldown between dashes.",             "textures/Charms/Dashmaster - _0011_charm_generic_03.png"),
    SHARP_SHADOW       ("Sharp Shadow",          "Damages enemies while performing a Shadow Dash.",  "textures/Charms/Sharp Shadow - charm_shade_impact.png"),
    VOID_HEART         ("Void Heart",            "Empowers your spells, dealing more damage.",       "textures/Charms/VoidHeart.png");

    private final String displayName;
    private final String description;
    private final String iconPath;

    Charm(String displayName, String description, String iconPath) {
        this.displayName = displayName;
        this.description = description;
        this.iconPath = iconPath;
    }

    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public String getIconPath()    { return iconPath; }
}
