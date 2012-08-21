// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SecureSafehouse.java

package zombie.behaviors.survivor.orders;

import java.util.Stack;
import java.util.Vector;
import zombie.behaviors.survivor.orders.LittleTasks.CloseDoor;
import zombie.characters.*;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.areas.IsoRoomExit;
import zombie.iso.objects.IsoDoor;

// Referenced classes of package zombie.behaviors.survivor.orders:
//            OrderSequence, GotoOrder, WaitUntilFollowersArrive, BlockWindows

public class SecureSafehouse extends OrderSequence
{

    public SecureSafehouse(IsoGameCharacter chr)
    {
        super(chr);
        IsoBuilding b = ((IsoSurvivor)chr).getDescriptor().getGroup().Safehouse;
        for(int n = 0; n < b.Exits.size(); n++)
        {
            IsoRoomExit e = (IsoRoomExit)b.Exits.get(n);
            IsoDoor door = e.getDoor(chr.getCell());
            int x = 0;
            int y = 0;
            int z = 0;
            if(e.From == null)
            {
                x = e.To.x;
                y = e.To.y;
                z = e.To.layer;
            } else
            {
                x = e.x;
                y = e.y;
                z = e.layer;
            }
            Orders.add(new GotoOrder(chr, x, y, z));
            Orders.add(new CloseDoor(chr, door));
            Orders.add(new WaitUntilFollowersArrive(chr));
            Orders.add(new BlockWindows(chr, b));
        }

    }
}
