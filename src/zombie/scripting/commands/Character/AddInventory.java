// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AddInventory.java

package zombie.scripting.commands.Character;

import zombie.characters.IsoGameCharacter;
import zombie.inventory.ItemContainer;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptContainer;
import zombie.scripting.objects.ScriptModule;

public class AddInventory extends BaseCommand
{

    public AddInventory()
    {
    }

    public void init(String object, String params[])
    {
        owner = object;
        item = params[0];
    }

    public void begin()
    {
        IsoGameCharacter actual = module.getCharacterActual(owner);
        ItemContainer cont = null;
        if(actual != null)
        {
            cont = actual.getInventory();
        } else
        {
            ScriptContainer chr = module.getScriptContainer(owner);
            if(chr == null)
                return;
            cont = chr.getActual();
            if(cont == null)
                return;
        }
        String item = this.item;
        if(!item.contains("."))
            item = (new StringBuilder()).append(module.name).append(".").append(item).toString();
        cont.AddItem(item);
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
    String item;
}
