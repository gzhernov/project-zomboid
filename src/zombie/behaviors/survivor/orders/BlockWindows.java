// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BlockWindows.java

package zombie.behaviors.survivor.orders;

import java.util.Stack;
import java.util.Vector;
import zombie.behaviors.survivor.orders.LittleTasks.AquireSheetAndBlockWindow;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.iso.IsoGridSquare;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.objects.IsoCurtain;
import zombie.iso.objects.IsoWindow;
import zombie.ui.TextManager;
import zombie.ui.UIFont;

// Referenced classes of package zombie.behaviors.survivor.orders:
//            OrderSequence, Order, FollowOrder

public class BlockWindows extends OrderSequence
{

    public BlockWindows(IsoGameCharacter chr, IsoBuilding building)
    {
        super(chr);
        windowsAll = new Stack();
        windowsFloorAB = new Stack();
        b = building;
    }

    public int renderDebug(int y)
    {
        int x = 50;
        TextManager.instance.DrawString(UIFont.Small, x, y, "BlockWindows", 1.0F, 1.0F, 1.0F, 1.0F);
        y += 30;
        if(ID < Orders.size())
            ((Order)Orders.get(ID)).renderDebug(y);
        return y;
    }

    public void initOrder()
    {
        if(((IsoSurvivor)character).getDescriptor().getGroup().Leader == character.getDescriptor())
        {
            for(int n = 0; n < character.getDescriptor().getGroup().Members.size(); n++)
            {
                SurvivorDesc desc = (SurvivorDesc)character.getDescriptor().getGroup().Members.get(n);
                if(desc.getInstance() != null && (desc.getInstance().getOrder() == null || (desc.getInstance().getOrder() instanceof FollowOrder)))
                    desc.getInstance().GiveOrder(new BlockWindows(desc.getInstance(), b), false);
            }

        }
        if(b.Windows.size() > 0)
        {
            IsoWindow r;
            for(; !b.Windows.isEmpty(); b.Windows.remove(r))
            {
                r = (IsoWindow)b.Windows.get(Rand.Next(b.Windows.size()));
                if(windowsAll.contains(r))
                    continue;
                IsoCurtain c = r.HasCurtains();
                windowsAll.add(r);
                if(r.square.getZ() < 2 && (c == null || c.open))
                    windowsFloorAB.add(r);
            }

            b.Windows.addAll(windowsAll);
            for(int n = 0; n < windowsFloorAB.size(); n++)
            {
                IsoWindow w = (IsoWindow)windowsFloorAB.get(n);
                IsoCurtain c = w.HasCurtains();
                Orders.add(new AquireSheetAndBlockWindow(character, w, c));
            }

        }
    }

    Stack windowsAll;
    Stack windowsFloorAB;
    IsoBuilding b;
}
