// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EndMeetingOrder.java

package zombie.behaviors.survivor.orders;

import java.util.Stack;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;

// Referenced classes of package zombie.behaviors.survivor.orders:
//            OrderSequence, GuardOrder

public class EndMeetingOrder extends OrderSequence
{

    public EndMeetingOrder(IsoGameCharacter chr)
    {
        super(chr);
        this.chr = chr;
    }

    public boolean complete()
    {
        return true;
    }

    public void initOrder()
    {
        for(int n = 0; n < chr.getDescriptor().getGroup().Members.size(); n++)
        {
            SurvivorDesc desc = (SurvivorDesc)chr.getDescriptor().getGroup().Members.get(n);
            if(desc != chr.getDescriptor() && desc.getInstance().InRoomWith(chr) && (desc.getInstance().getOrder() instanceof GuardOrder))
                desc.getInstance().getOrders().pop();
        }

    }

    IsoGameCharacter chr;
}
