// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BandageOrder.java

package zombie.behaviors.survivor.orders.LittleTasks;

import zombie.behaviors.Behavior;
import zombie.behaviors.survivor.orders.Order;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.IsoGameCharacter;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;

public class BandageOrder extends Order
{

    public BandageOrder(IsoGameCharacter chr)
    {
        super(chr);
        this.chr = chr;
    }

    public zombie.behaviors.Behavior.BehaviorResult process()
    {
        return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
    }

    public void initOrder()
    {
        InventoryItem item = chr.getInventory().getBestBandage(chr.getDescriptor());
        if(item == null)
        {
            return;
        } else
        {
            chr.getBodyDamage().UseBandageOnMostNeededPart();
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
}
