// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UseItemOnIsoObject.java

package zombie.behaviors.survivor.orders.LittleTasks;

import zombie.behaviors.Behavior;
import zombie.behaviors.survivor.orders.Order;
import zombie.characters.IsoGameCharacter;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.iso.IsoObject;

public class UseItemOnIsoObject extends Order
{

    public UseItemOnIsoObject(IsoGameCharacter chr, String item, IsoObject obj)
    {
        super(chr);
        inv = null;
        this.chr = chr;
        inv = item;
        this.obj = obj;
    }

    public zombie.behaviors.Behavior.BehaviorResult process()
    {
        return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
    }

    public void initOrder()
    {
        InventoryItem item = chr.getInventory().FindAndReturn(inv);
        if(item == null)
        {
            return;
        } else
        {
            obj.useItemOn(item);
            item.Use();
            return;
        }
    }

    public boolean complete()
    {
        return true;
    }

    public void update()
    {
    }

    IsoGameCharacter chr;
    String inv;
    IsoObject obj;
}
