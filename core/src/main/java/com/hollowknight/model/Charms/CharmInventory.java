package com.hollowknight.model.Charms;


import com.hollowknight.model.Constants;

import java.util.ArrayList;
import java.util.List;


public class CharmInventory {
    
    private List<Charm> ownedCharms   = new ArrayList<>();
    
    private List<Charm> equippedCharms = new ArrayList<>();

    
    private int usedNotches = 0;

    public CharmInventory() {
        ownedCharms.add(Charm.SOUL_CATCHER);
        ownedCharms.add(Charm.QUICK_FOCUS);
        ownedCharms.add(Charm.DASHMASTER);
        ownedCharms.add(Charm.UNBREAKABLE_STRENGTH);
        ownedCharms.add(Charm.QUICK_SLASH);
        ownedCharms.add(Charm.HEAVY_BLOW);
        ownedCharms.add(Charm.SHARP_SHADOW);
        this.equipCharm(Charm.SOUL_CATCHER);
        this.equipCharm(Charm.QUICK_SLASH);
        this.equipCharm(Charm.SHARP_SHADOW);
        usedNotches = 3;
    }

    
    public boolean equipCharm(Charm charm) {
        if (!ownedCharms.contains(charm)) return false;
        if (usedNotches + 1 > Constants.CHARM_NOTCHES) return false;
        if (equippedCharms.contains(charm)) return false;
        equippedCharms.add(charm);
        usedNotches++;
        return true;
    }

    
    public void unequipCharm(Charm charm) {
        if (equippedCharms.remove(charm)) usedNotches--;
    }

    
    public void addCharm(Charm charm) {
        if (!ownedCharms.contains(charm)) ownedCharms.add(charm);
    }

    

    
    public boolean hasSoulCatcher() {
        return isEquipped(Charm.SOUL_CATCHER);
    }
    
    public boolean hasDashmaster() {
        return isEquipped(Charm.DASHMASTER);
    }
    
    public boolean hasUnbreakableStrength() {
        return isEquipped(Charm.UNBREAKABLE_STRENGTH);
    }
    
    public boolean hasQuickSlash() {
        return isEquipped(Charm.QUICK_SLASH);
    }
    
    public boolean hasQuickFocus() {
        return isEquipped(Charm.QUICK_FOCUS);
    }
    
    public boolean hasHeavyBlow() {
        return isEquipped(Charm.HEAVY_BLOW);
    }
    
    public boolean hasSharpShadow() {
        return isEquipped(Charm.SHARP_SHADOW);
    }
    
    public boolean hasVoidHeart() {
        return isEquipped(Charm.VOID_HEART);
    }

    private boolean isEquipped(Charm c) { return equippedCharms.contains(c); }

    public List<Charm> getOwnedCharms()    { return ownedCharms; }
    public List<Charm> getEquippedCharms() { return equippedCharms; }
    public int getUsedNotches()            { return usedNotches; }
    public int getMaxNotches()             { return Constants.CHARM_NOTCHES; }
}
