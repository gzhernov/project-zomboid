// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GotoNextTo.java

package zombie.behaviors.survivor.orders;

import java.util.Stack;
import zombie.behaviors.Behavior;
import zombie.behaviors.survivor.orders.LittleTasks.FaceOrder;
import zombie.characters.IsoGameCharacter;
import zombie.core.Rand;
import zombie.iso.*;
import zombie.ui.TextManager;
import zombie.ui.UIFont;

// Referenced classes of package zombie.behaviors.survivor.orders:
//            Order, OrderSequence, GotoOrder

public class GotoNextTo extends Order
{

    public GotoNextTo(IsoGameCharacter chr, int x, int y, int z)
    {
        super(chr);
        order = null;
        order = new OrderSequence(chr);
        Stack choices = new Stack();
        IsoGridSquare osq = IsoWorld.instance.CurrentCell.getGridSquare(x, y, z);
        for(int ax = -1; ax <= 1; ax++)
        {
            for(int ay = -1; ay <= 1; ay++)
            {
                if(ax != 0 && ay != 0 || ax == 0 && ay == 0)
                    continue;
                IsoGridSquare sq = IsoWorld.instance.CurrentCell.getGridSquare(x + ax, y + ay, z);
                if(sq != null && sq.isFree(false) && (osq == null || sq.getRoom() == osq.getRoom()))
                    choices.add(sq);
            }

        }

        if(choices.isEmpty())
        {
            return;
        } else
        {
            IsoGridSquare choice = (IsoGridSquare)choices.get(Rand.Next(choices.size()));
            order.Orders.add(new GotoOrder(chr, choice.getX(), choice.getY(), choice.getZ()));
            Vector2 dirVec = new Vector2(0.0F, 0.0F);
            dirVec.x = x - choice.getX();
            dirVec.y = y - choice.getY();
            order.Orders.add(new FaceOrder(chr, IsoDirections.fromAngle(dirVec)));
            return;
        }
    }

    public int getAttackIfEnemiesAroundBias()
    {
        return character.getCurrentSquare().getRoom() == null ? 0 : -1000;
    }

    public void update()
    {
        if(order != null)
            order.update();
    }

    public boolean complete()
    {
        if(order == null)
            return true;
        else
            return order.complete();
    }

    public zombie.behaviors.Behavior.BehaviorResult process()
    {
        if(order == null)
            return zombie.behaviors.Behavior.BehaviorResult.Failed;
        else
            return order.process();
    }

    public int renderDebug(int y)
    {
        int x = 50;
        TextManager.instance.DrawString(UIFont.Small, x, y, "GotoNextTo", 1.0F, 1.0F, 1.0F, 1.0F);
        y += 30;
        if(order != null)
            order.renderDebug(y);
        return y;
    }

    public void initOrder()
    {
        if(order != null)
            order.initOrder();
    }

    public float getPriority(IsoGameCharacter character)
    {
        if(order == null)
            return -100000F;
        else
            return order.getPriority(character);
    }

    OrderSequence order;
}
