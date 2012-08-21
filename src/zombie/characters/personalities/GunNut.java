// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GunNut.java

package zombie.characters.personalities;

import zombie.behaviors.SequenceBehavior;
import zombie.behaviors.survivor.MasterSurvivorBehavior;
import zombie.characters.IsoSurvivor;
import zombie.characters.SurvivorPersonality;
import zombie.inventory.types.HandWeapon;

public class GunNut extends SurvivorPersonality
{

    public GunNut()
    {
    }

    public void CreateBehaviours(IsoSurvivor survivor)
    {
        survivor.setMasterProper(new MasterSurvivorBehavior(survivor));
        survivor.getMasterBehaviorList().addChild(survivor.getMasterProper());
        if(survivor.getPrimaryHandItem() != null)
        {
            zombie.inventory.InventoryItem attackItem = survivor.getPrimaryHandItem();
            if(attackItem instanceof HandWeapon)
                survivor.setUseHandWeapon((HandWeapon)attackItem);
        }
        survivor.getMasterBehaviorList().addChild(survivor.behaviours);
    }

    public int getHuntZombieRange()
    {
        return 10;
    }
}
