// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NamedOrder.java

package zombie.scripting.commands.Character;

import zombie.behaviors.survivor.orders.Order;
import zombie.characters.IsoGameCharacter;
import zombie.scripting.objects.Script;
import zombie.scripting.objects.ScriptModule;

// Referenced classes of package zombie.scripting.commands.Character:
//            Order

public class NamedOrder extends zombie.scripting.commands.Character.Order
{

    public NamedOrder()
    {
    }

    public void init(String object, String params[])
    {
        owner = object;
        this.params = new String[params.length - 2];
        int n = 0;
        String arr$[] = params;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            String p = arr$[i$];
            if(n > 1)
                this.params[n - 2] = p.trim();
            n++;
        }

        name = params[0].trim();
        order = params[1].trim();
    }

    public void begin()
    {
        IsoGameCharacter actual = null;
        if(currentinstance.HasAlias(owner))
            actual = currentinstance.getAlias(owner);
        else
            actual = module.getCharacterActual(owner);
        Order order = orderInfo(actual);
        order.name = name;
    }

    String name;
}
