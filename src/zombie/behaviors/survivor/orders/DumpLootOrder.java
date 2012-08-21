// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DumpLootOrder.java

package zombie.behaviors.survivor.orders;

import java.util.Stack;
import zombie.behaviors.survivor.orders.LittleTasks.DumpLootInContainer;
import zombie.characters.IsoGameCharacter;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.inventory.ItemContainer;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.areas.IsoBuilding;

// Referenced classes of package zombie.behaviors.survivor.orders:
//            OrderSequence, GotoNextTo

class DumpLootOrder extends OrderSequence
{

    public DumpLootOrder(IsoGameCharacter chr, IsoBuilding b)
    {
        super(chr);
        if(b.container.size() == 0)
        {
            return;
        } else
        {
            ItemContainer con = (ItemContainer)b.container.get(Rand.Next(b.container.size()));
            Orders.add(new GotoNextTo(chr, con.parent.square.getX(), con.parent.square.getY(), con.parent.square.getZ()));
            Orders.add(new DumpLootInContainer(chr, con));
            return;
        }
    }
}
