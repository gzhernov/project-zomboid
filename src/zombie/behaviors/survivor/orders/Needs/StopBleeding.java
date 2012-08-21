// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StopBleeding.java

package zombie.behaviors.survivor.orders.Needs;

import java.util.Stack;
import zombie.behaviors.survivor.orders.LittleTasks.BandageOrder;
import zombie.behaviors.survivor.orders.ObtainItem;
import zombie.behaviors.survivor.orders.OrderSequence;
import zombie.characters.IsoGameCharacter;

public class StopBleeding extends OrderSequence
{

    public StopBleeding(IsoGameCharacter chr)
    {
        super(chr);
        Items = new Stack();
        type = "StopBleeding";
    }

    public void initOrder()
    {
        Items.add("RippedSheets");
        Orders.add(new ObtainItem(character, Items, 1000));
        Orders.add(new BandageOrder(character));
    }

    public boolean isCritical()
    {
        return true;
    }

    public Stack Items;
}
