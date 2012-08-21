// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RemoveNamedOrder.java

package zombie.scripting.commands.Character;

import java.util.Stack;
import zombie.behaviors.survivor.orders.Order;
import zombie.characters.IsoGameCharacter;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.Script;
import zombie.scripting.objects.ScriptModule;

public class RemoveNamedOrder extends BaseCommand
{

    public RemoveNamedOrder()
    {
    }

    public void init(String object, String params[])
    {
        owner = object;
        if(params.length == 1)
            name = params[0].trim();
    }

    public void begin()
    {
        IsoGameCharacter actual = null;
        if(currentinstance.HasAlias(owner))
            actual = currentinstance.getAlias(owner);
        else
            actual = module.getCharacterActual(owner);
        if(!actual.getOrders().empty())
        {
            for(int n = 0; n < actual.getOrders().size(); n++)
                if(((Order)actual.getOrders().get(n)).name.equals(name))
                {
                    actual.getOrders().remove(n);
                    n--;
                }

        }
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
    String name;
}
