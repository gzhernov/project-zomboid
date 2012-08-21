// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LootBuilding.java

package zombie.behaviors.survivor.orders;

import java.util.Stack;
import java.util.Vector;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.inventory.ItemContainer;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.areas.IsoRoom;

// Referenced classes of package zombie.behaviors.survivor.orders:
//            OrderSequence, LootRoom, FollowOrder

public class LootBuilding extends OrderSequence
{
	/*    */   public static enum LootStyle
	/*    */   {
	/* 23 */     Safehouse, 
	/* 24 */     Medium, 
	/* 25 */     Extreme;
	/*    */   }


    public LootBuilding(IsoGameCharacter chr, IsoBuilding building, LootStyle style)
    {
        super(chr);
        this.building = building;
        this.style = style;
        this.chr = chr;
        Stack rooms = new Stack();
        rooms.addAll(building.Rooms);
        
        for(int n = 0; n < rooms.size(); n++)
            if(((IsoRoom)rooms.get(n)).Containers.isEmpty())
            {
                rooms.remove(n);
                n--;
            }

        IsoRoom room;
        for(; !rooms.isEmpty(); rooms.remove(room))
        {
            room = (IsoRoom)rooms.get(Rand.Next(rooms.size()));
            Orders.add(new LootRoom(chr, room, style));
        }

    }

    public boolean complete()
    {
        if(chr.getInventory().getWeight() + 10 >= chr.getMaxWeight().intValue() / 2)
            return true;
        else
            return super.complete();
    }

    public void initOrder()
    {
        if(((IsoSurvivor)chr).getDescriptor().getGroup().Leader == chr.getDescriptor())
        {
            for(int n = 0; n < chr.getDescriptor().getGroup().Members.size(); n++)
            {
                SurvivorDesc desc = (SurvivorDesc)chr.getDescriptor().getGroup().Members.get(n);
                if(desc.getInstance() == null)
                    return;
                if(desc.getInstance().getOrder() instanceof FollowOrder)
                    desc.getInstance().GiveOrder(new LootBuilding(desc.getInstance(), building, style), false);
            }

        }
    }

    IsoGameCharacter chr;
    IsoBuilding building;
    LootStyle style;
}
