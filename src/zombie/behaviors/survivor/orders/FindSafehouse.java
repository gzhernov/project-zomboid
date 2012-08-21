// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FindSafehouse.java

package zombie.behaviors.survivor.orders;

import java.util.Stack;
import zombie.characters.*;

// Referenced classes of package zombie.behaviors.survivor.orders:
//            OrderSequence, GotoBuildingOrder, SecureSafehouse, LootBuilding

public class FindSafehouse extends OrderSequence
{

    public FindSafehouse(IsoGameCharacter chr)
    {
        super(chr);
        IsoSurvivor s = (IsoSurvivor)chr;
        Orders.add(new GotoBuildingOrder(s, s.getDescriptor().getGroup().Safehouse));
        Orders.add(new SecureSafehouse(s));
        Orders.add(new LootBuilding(chr, s.getDescriptor().getGroup().Safehouse, LootBuilding.LootStyle.Safehouse));
    }
}
