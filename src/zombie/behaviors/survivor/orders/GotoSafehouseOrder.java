// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GotoSafehouseOrder.java

package zombie.behaviors.survivor.orders;

import java.util.Vector;
import zombie.characters.*;
import zombie.core.Rand;
import zombie.iso.IsoGridSquare;
import zombie.iso.areas.*;

// Referenced classes of package zombie.behaviors.survivor.orders:
//            GotoOrder

public class GotoSafehouseOrder extends GotoOrder
{

    public GotoSafehouseOrder(IsoGameCharacter chr)
    {
        super(chr);
        this.chr = chr;
    }

    public void initOrder()
    {
        b = chr.getDescriptor().getGroup().Safehouse;
        IsoRoomExit e = (IsoRoomExit)b.Exits.get(Rand.Next(b.Exits.size()));
        if(e.From == null)
            e = e.To;
        IsoGridSquare sq = e.From.getFreeTile();
        init(sq.getX(), sq.getY(), sq.getZ());
    }

    public boolean complete()
    {
        return super.complete();
    }

    IsoBuilding b;
    IsoGameCharacter chr;
}
