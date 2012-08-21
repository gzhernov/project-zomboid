// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Order.java

package zombie.scripting.commands.Character;

import java.util.Stack;
import zombie.behaviors.survivor.orders.FollowOrder;
import zombie.behaviors.survivor.orders.IdleOrder;
import zombie.behaviors.survivor.orders.LittleTasks.FaceOrder;
import zombie.characters.IsoGameCharacter;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.Script;
import zombie.scripting.objects.ScriptModule;

public class Order extends BaseCommand
{

    public Order()
    {
        bGory = false;
        order = null;
    }

    public void init(String object, String params[])
    {
        owner = object;
        this.params = new String[params.length - 1];
        int n = 0;
        String arr$[] = params;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            String p = arr$[i$];
            if(n > 0)
                this.params[n - 1] = p.trim();
            n++;
        }

        order = params[0].trim();
    }

    public zombie.behaviors.survivor.orders.Order orderInfo(IsoGameCharacter actual)
    {
        if(order.equals("Idle"))
            actual.getOrders().push(new IdleOrder(actual));
        if(order.equals("Follow"))
        {
            IsoGameCharacter other = null;
            if(currentinstance.HasAlias(params[0]))
                other = currentinstance.getAlias(params[0]);
            else
                other = module.getCharacterActual(params[0]);
            int range = Integer.parseInt(params[1]);
            actual.getOrders().push(new FollowOrder(actual, other, range));
        } else
        if(order.equals("Face"))
        {
            IsoGameCharacter other = null;
            if(currentinstance.HasAlias(params[0]))
                other = currentinstance.getAlias(params[0]);
            else
                other = module.getCharacterActual(params[0]);
            actual.getOrders().push(new FaceOrder(actual, other));
        } else
        if(order.equals("FollowStrict"))
        {
            IsoGameCharacter other = null;
            if(currentinstance.HasAlias(params[0]))
                other = currentinstance.getAlias(params[0]);
            else
                other = module.getCharacterActual(params[0]);
            int range = Integer.parseInt(params[1]);
            actual.getOrders().push(new FollowOrder(actual, other, range, true));
        }
        actual.setOrder((zombie.behaviors.survivor.orders.Order)actual.getOrders().peek());
        return (zombie.behaviors.survivor.orders.Order)actual.getOrders().peek();
    }

    public void begin()
    {
        IsoGameCharacter actual = null;
        if(currentinstance.HasAlias(owner))
            actual = currentinstance.getAlias(owner);
        else
            actual = module.getCharacterActual(owner);
        orderInfo(actual);
    }

    public void Finish()
    {
    }

    public boolean IsFinished()
    {
        return true;
    }

    public void update()
    {
    }

    public boolean DoesInstantly()
    {
        return true;
    }

    String owner;
    boolean bGory;
    String params[];
    String order;
}
