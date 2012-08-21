// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   OrderSequence.java

package zombie.behaviors.survivor.orders;

import java.util.Stack;
import zombie.behaviors.Behavior;
import zombie.characters.IsoGameCharacter;
import zombie.ui.TextManager;
import zombie.ui.UIFont;

// Referenced classes of package zombie.behaviors.survivor.orders:
//            Order

public class OrderSequence extends Order
{

    public OrderSequence(IsoGameCharacter chr)
    {
        super(chr);
        ID = 0;
        Orders = new Stack();
    }

    public boolean complete()
    {
        return ID >= Orders.size();
    }

    public void update()
    {
        if(ID < Orders.size())
            ((Order)Orders.get(ID)).update();
    }

    public void initOrder()
    {
    }

    public zombie.behaviors.Behavior.BehaviorResult process()
    {
        if(ID >= Orders.size())
            return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
        if(!((Order)Orders.get(ID)).bInit)
        {
            ((Order)Orders.get(ID)).initOrder();
            ((Order)Orders.get(ID)).bInit = true;
        }
        zombie.behaviors.Behavior.BehaviorResult res = ((Order)Orders.get(ID)).process();
        if(((Order)Orders.get(ID)).complete())
            ID++;
        if(ID >= Orders.size())
            return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
        else
            return zombie.behaviors.Behavior.BehaviorResult.Working;
    }

    public int renderDebug(int y)
    {
        int x = 50;
        TextManager.instance.DrawString(UIFont.Small, x, y, "OrderSequence", 1.0F, 1.0F, 1.0F, 1.0F);
        y += 30;
        if(ID < Orders.size())
            ((Order)Orders.get(ID)).renderDebug(y);
        return y;
    }

    public float getPriority(IsoGameCharacter character)
    {
        if(!bInit)
            return 10000F;
        if(Orders.size() <= ID)
            return -10000F;
        else
            return ((Order)Orders.get(ID)).getPriority(character);
    }

    public int ID;
    public Stack Orders;
}
