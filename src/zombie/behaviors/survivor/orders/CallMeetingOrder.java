// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CallMeetingOrder.java

package zombie.behaviors.survivor.orders;

import java.util.Stack;
import zombie.behaviors.survivor.orders.LittleTasks.GotoRoomOrder;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;
import zombie.iso.IsoGridSquare;
import zombie.iso.areas.IsoRoom;

// Referenced classes of package zombie.behaviors.survivor.orders:
//            OrderSequence, GuardOrder, GotoOrder

public class CallMeetingOrder extends OrderSequence
{

    public CallMeetingOrder(IsoGameCharacter chr)
    {
        super(chr);
        characters = new Stack();
        timeout = 500;
        this.chr = chr;
    }

    public void update()
    {
        timeout--;
        super.update();
    }

    public boolean complete()
    {
        if(timeout < 0)
            return true;
        for(int n = 0; n < characters.size(); n++)
            if(!((IsoGameCharacter)characters.get(n)).isDead() && !((IsoGameCharacter)characters.get(n)).InRoomWith(chr))
                return false;

        return true;
    }

    public void initOrder()
    {
        for(int n = 0; n < chr.getDescriptor().getGroup().Members.size(); n++)
        {
            SurvivorDesc desc = (SurvivorDesc)chr.getDescriptor().getGroup().Members.get(n);
            if(desc != chr.getDescriptor() && desc != null && desc.getInstance().InBuildingWith(chr))
            {
                IsoGridSquare sq = chr.getCurrentSquare().getRoom().getFreeTile();
                desc.getInstance().GiveOrder(new GuardOrder(desc.getInstance()), false);
                desc.getInstance().GiveOrder(new GotoRoomOrder(desc.getInstance(), chr.getCurrentSquare().getRoom()), false);
                desc.getInstance().GiveOrder(new GotoOrder(desc.getInstance(), sq.getX(), sq.getY(), sq.getZ()), false);
                characters.add(desc.getInstance());
            }
        }

        Orders.add(new GuardOrder(chr));
    }

    IsoGameCharacter chr;
    Stack characters;
    int timeout;
}
