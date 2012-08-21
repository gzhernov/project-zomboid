// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LootContainer.java

package zombie.behaviors.survivor.orders;

import zombie.behaviors.Behavior;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.Food;
import zombie.inventory.types.HandWeapon;

// Referenced classes of package zombie.behaviors.survivor.orders:
//            Order, LootBuilding

class LootContainer extends Order
{

    public LootContainer(IsoGameCharacter chr, ItemContainer con, LootBuilding.LootStyle style)
    {
        super(chr);
        this.chr = chr;
        this.con = con;
        this.style = style;
    }

    public zombie.behaviors.Behavior.BehaviorResult process()
    {
        for(int n = 0; n < con.Items.size(); n++)
        {
            InventoryItem item = (InventoryItem)con.Items.get(n);
            if(item.CanStoreWater)
            {
                con.Remove(item);
                chr.getInventory().AddItem(item);
                continue;
            }
            if((item instanceof Food) && !((IsoSurvivor)chr).SatisfiedWithInventory(style, zombie.characters.IsoSurvivor.SatisfiedBy.Food) || style == LootBuilding.LootStyle.Extreme || chr.getDescriptor().getGroup().HasNeed("Type:Food"))
            {
                con.Remove(item);
                chr.getInventory().AddItem(item);
                continue;
            }
            if((item instanceof HandWeapon) && !((IsoSurvivor)chr).SatisfiedWithInventory(style, zombie.characters.IsoSurvivor.SatisfiedBy.Weapons) || style == LootBuilding.LootStyle.Extreme || chr.getDescriptor().getGroup().HasNeed("Type:Weapon"))
            {
                con.Remove(item);
                chr.getInventory().AddItem(item);
                continue;
            }
            if(chr.getDescriptor().getGroup().HasNeed(item.getType()))
            {
                con.Remove(item);
                chr.getInventory().AddItem(item);
            }
        }

        return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
    }

    public boolean complete()
    {
        return true;
    }

    public void update()
    {
    }

    IsoGameCharacter chr;
    ItemContainer con;
    LootBuilding.LootStyle style;
}
