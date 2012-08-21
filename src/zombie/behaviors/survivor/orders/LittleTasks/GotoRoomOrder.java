// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GotoRoomOrder.java

package zombie.behaviors.survivor.orders.LittleTasks;

import java.util.Vector;
import zombie.behaviors.survivor.orders.GotoOrder;
import zombie.characters.IsoGameCharacter;
import zombie.iso.IsoGridSquare;
import zombie.iso.areas.IsoRoom;
import zombie.iso.areas.IsoRoomExit;

public class GotoRoomOrder extends GotoOrder
{

    public GotoRoomOrder(IsoGameCharacter chr, IsoRoom b)
    {
        super(chr);
        this.b = b;
        this.chr = chr;
        if(b.Exits.isEmpty())
            return;
        IsoRoomExit e = (IsoRoomExit)b.Exits.get(0);
        if(e.From == null)
            e = e.To;
        IsoGridSquare sq = e.From.getFreeTile();
        init(sq.getX(), sq.getY(), sq.getZ());
    }

    public boolean complete()
    {
        return super.complete();
    }

    IsoRoom b;
    IsoGameCharacter chr;
}
