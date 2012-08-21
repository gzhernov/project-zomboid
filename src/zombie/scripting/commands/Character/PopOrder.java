// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PopOrder.java

package zombie.scripting.commands.Character;

import java.util.Stack;
import zombie.characters.IsoGameCharacter;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.Script;
import zombie.scripting.objects.ScriptModule;

public class PopOrder extends BaseCommand
{

    public PopOrder()
    {
        index = -1;
    }

    public void init(String object, String params[])
    {
        owner = object;
        if(params.length == 1)
            index = Integer.parseInt(params[0].trim());
    }

    public void begin()
    {
        IsoGameCharacter actual = null;
        if(currentinstance.HasAlias(owner))
            actual = currentinstance.getAlias(owner);
        else
            actual = module.getCharacterActual(owner);
        if(!actual.getOrders().empty())
            if(index == -1)
            {
                actual.getOrders().pop();
            } else
            {
                index = actual.getOrders().size() - index - 1;
                if(index < actual.getOrders().size() && index >= 0)
                    actual.getOrders().remove(index);
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
    int index;
}
