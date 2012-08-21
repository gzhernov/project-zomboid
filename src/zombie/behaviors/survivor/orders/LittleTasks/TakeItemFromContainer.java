// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TakeItemFromContainer.java

package zombie.behaviors.survivor.orders.LittleTasks;

import zombie.behaviors.Behavior;
import zombie.behaviors.survivor.orders.Order;
import zombie.characters.IsoGameCharacter;
import zombie.core.Collections.NulledArrayList;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;

public class TakeItemFromContainer extends Order
{

    public TakeItemFromContainer(IsoGameCharacter chr, ItemContainer con, String type)
    {
        super(chr);
        this.chr = chr;
        this.con = con;
        this.type = type;
    }

    public zombie.behaviors.Behavior.BehaviorResult process()
    {
        if(type.contains("Type:"))
        {
            if(type.contains("Food"))
            {
                InventoryItem i = con.getBestFood(chr.getDescriptor());
                con.Remove(i);
                chr.getInventory().AddItem(i);
            }
            if(type.contains("Weapon"))
            {
                InventoryItem i = con.getBestWeapon(chr.getDescriptor());
                con.Remove(i);
                chr.getInventory().AddItem(i);
            }
        }
        for(int n = 0; n < con.Items.size(); n++)
        {
            InventoryItem item = (InventoryItem)con.Items.get(n);
            if(item.getType().equals(type))
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
    String type;
}
