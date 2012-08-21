// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LootMission.java

package zombie.behaviors.survivor.orders;

import java.util.Stack;
import java.util.Vector;
import zombie.characters.*;
import zombie.iso.IsoCell;
import zombie.iso.IsoWorld;
import zombie.iso.areas.BuildingScore;
import zombie.iso.areas.IsoBuilding;

// Referenced classes of package zombie.behaviors.survivor.orders:
//            OrderSequence, GotoBuildingOrder, CallMeetingOrder, ArrangeLootingTeamOrder, 
//            EndMeetingOrder, LootBuilding, GotoSafehouseOrder, SecureSafehouse, 
//            ReturnToSafehouseConversation, DumpLootOrder

public class LootMission extends OrderSequence
{

    public LootMission(IsoGameCharacter chr)
    {
        super(chr);
        this.chr = chr;
    }

    public void initOrder()
    {
        character = chr;
        Stack scores = IsoWorld.instance.CurrentCell.getBestBuildings(zombie.iso.IsoCell.BuildingSearchCriteria.General, 8);
        float highestNeedScore = -1F;
        IsoBuilding b = null;
        for(int n = 0; n < scores.size(); n++)
        {
            if(((BuildingScore)scores.get(n)).building == ((IsoSurvivor)chr).getDescriptor().getGroup().Safehouse)
            {
                scores.remove(n);
                n--;
                continue;
            }
            float needsScore = ((BuildingScore)scores.get(n)).building.getNeedsScore(((IsoSurvivor)chr).getDescriptor().getGroup());
            if(needsScore > highestNeedScore)
            {
                highestNeedScore = needsScore;
                b = ((BuildingScore)scores.get(n)).building;
            }
        }

        if(b == null || b.Exits.isEmpty())
            return;
        if(!character.IsInBuilding(((IsoSurvivor)chr).getDescriptor().getGroup().Safehouse))
            Orders.add(new GotoBuildingOrder(chr, ((IsoSurvivor)chr).getDescriptor().getGroup().Safehouse));
        Orders.add(new CallMeetingOrder(chr));
        Orders.add(new ArrangeLootingTeamOrder(chr, "Base.ArrangeLooting"));
        Orders.add(new EndMeetingOrder(chr));
        Orders.add(new GotoBuildingOrder(chr, b));
        Orders.add(new LootBuilding(chr, b, LootBuilding.LootStyle.Extreme));
        b = ((IsoSurvivor)chr).getDescriptor().getGroup().Safehouse;
        Orders.add(new GotoSafehouseOrder(chr));
        Orders.add(new SecureSafehouse(chr));
        Orders.add(new ReturnToSafehouseConversation(chr, "Base.BackWithLoot"));
        Orders.add(new DumpLootOrder(chr, b));
    }

    IsoBuilding b;
    IsoGameCharacter chr;
}
