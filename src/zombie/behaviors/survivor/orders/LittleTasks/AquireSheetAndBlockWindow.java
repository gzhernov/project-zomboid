// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AquireSheetAndBlockWindow.java

package zombie.behaviors.survivor.orders.LittleTasks;

import java.util.Stack;
import zombie.behaviors.survivor.orders.*;
import zombie.characters.IsoGameCharacter;
import zombie.inventory.ItemContainer;
import zombie.iso.IsoGridSquare;
import zombie.iso.objects.IsoCurtain;
import zombie.iso.objects.IsoWindow;

// Referenced classes of package zombie.behaviors.survivor.orders.LittleTasks:
//            CloseCurtains, HangSheet

public class AquireSheetAndBlockWindow extends OrderSequence
{

    public AquireSheetAndBlockWindow(IsoGameCharacter chr, IsoWindow w, IsoCurtain c)
    {
        super(chr);
        this.c = c;
        this.w = w;
    }

    public boolean complete()
    {
        if(ID < Orders.size() && !(Orders.get(ID) instanceof ObtainItem) && c == null && !character.getInventory().contains("Sheet"))
            return true;
        else
            return super.complete();
    }

    public void initOrder()
    {
        if(c != null && c.open)
        {
            if(c.square.isFree(false))
                Orders.add(new GotoOrder(character, c.square.getX(), c.square.getY(), c.square.getZ()));
            else
                Orders.add(new GotoNextTo(character, c.square.getX(), c.square.getY(), c.square.getZ()));
            Orders.add(new CloseCurtains(character, c));
        } else
        if(c == null)
        {
            Orders.add(new ObtainItem(character, "Sheet", 10000));
            IsoGridSquare inside = w.getInsideSquare();
            if(inside.isFree(false))
                Orders.add(new GotoOrder(character, inside.getX(), inside.getY(), inside.getZ()));
            else
                Orders.add(new GotoNextTo(character, inside.getX(), inside.getY(), inside.getZ()));
            Orders.add(new HangSheet(character, w, inside));
        }
    }

    IsoWindow w;
    IsoCurtain c;
}
