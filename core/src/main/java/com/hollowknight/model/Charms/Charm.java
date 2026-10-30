package com.hollowknight.model.Charms;

import com.hollowknight.i18n.Text;

public enum Charm {
    QUICK_SLASH(Text.CHARM_QUICK_SLASH, Text.CHARM_QUICK_SLASH_DESC, "textures/Charms/Quick Slash - _0003_charm_nail_slash_speed_up.png"),
    QUICK_FOCUS(Text.CHARM_QUICK_FOCUS, Text.CHARM_QUICK_FOCUS_DESC, "textures/Charms/Quick Focus - _0005_charm_fast_focus.png"),
    SOUL_CATCHER(Text.CHARM_SOUL_CATCHER, Text.CHARM_SOUL_CATCHER_DESC, "textures/Charms/Soul Catcher - _0001_charm_more_soul.png"),
    UNBREAKABLE_STRENGTH(Text.CHARM_UNBREAKABLE_STRENGTH, Text.CHARM_UNBREAKABLE_STRENGTH_DESC, "textures/Charms/Unbreakable Strength_0002_charm_glass_attack_up_full.png"),
    HEAVY_BLOW(Text.CHARM_HEAVY_BLOW, Text.CHARM_HEAVY_BLOW_DESC, "textures/Charms/Heavy Blow - _0008_charm_nail_damage_up.png"),
    DASHMASTER(Text.CHARM_DASHMASTER, Text.CHARM_DASHMASTER_DESC, "textures/Charms/Dashmaster - _0011_charm_generic_03.png"),
    SHARP_SHADOW(Text.CHARM_SHARP_SHADOW, Text.CHARM_SHARP_SHADOW_DESC, "textures/Charms/Sharp Shadow - charm_shade_impact.png"),
    VOID_HEART(Text.CHARM_VOID_HEART, Text.CHARM_VOID_HEART_DESC, "textures/Charms/VoidHeart.png");

    private final Text displayName;
    private final Text description;
    private final String iconPath;

    Charm(Text displayName, Text description, String iconPath) {
        this.displayName = displayName;
        this.description = description;
        this.iconPath = iconPath;
    }

    public String getDisplayName() { return displayName.get(); }
    public String getDescription() { return description.get(); }
    public String getIconPath() { return iconPath; }
}
