// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DumpLootInContainer.java

package zombie.behaviors.survivor.orders.LittleTasks;

import zombie.behaviors.Behavior;
import zombie.behaviors.survivor.orders.LootBuilding;
import zombie.behaviors.survivor.orders.Order;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoSurvivor;
import zombie.core.Collections.NulledArrayList;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.Food;
import zombie.inventory.types.HandWeapon;

public class DumpLootInContainer extends Order
{

    public DumpLootInContainer(IsoGameCharacter chr, ItemContainer con)
    {
        super(chr);
        this.chr = chr;
        this.con = con;
    }

    public zombie.behaviors.Behavior.BehaviorResult process()
    {
        for(int n = 0; n < chr.getInventory().Items.size(); n++)
        {
            InventoryItem item = (InventoryItem)chr.getInventory().Items.get(n);
            boolean bSatisfiedWithFood = ((IsoSurvivor)chr).SatisfiedWithInventory(zombie.behaviors.survivor.orders.LootBuilding.LootStyle.Safehouse, zombie.characters.IsoSurvivor.SatisfiedBy.Food);
            boolean bSatisfiedWithWeapons = ((IsoSurvivor)chr).SatisfiedWithInventory(zombie.behaviors.survivor.orders.LootBuilding.LootStyle.Safehouse, zombie.characters.IsoSurvivor.SatisfiedBy.Weapons);
            int wc = chr.getInventory().getWaterContainerCount();
            if(item.CanStoreWater)
            {
                if(wc > 2)
                {
                    chr.getInventory().Remove(item);
                    con.AddItem(item);
                }
                continue;
            }
            if(bSatisfiedWithFood && (item instanceof Food))
            {
                chr.getInventory().Remove(item);
                con.AddItem(item);
                continue;
            }
            if(bSatisfiedWithWeapons && (item instanceof HandWeapon))
            {
                chr.getInventory().Remove(item);
                con.AddItem(item);
                continue;
            }
            if(!(item instanceof HandWeapon) && !(item instanceof Food))
            {
                chr.getInventory().Remove(item);
                con.AddItem(item);
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
}
