// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EquipItem.java

package zombie.scripting.commands.Character;

import zombie.characters.IsoGameCharacter;
import zombie.inventory.ItemContainer;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptModule;

public class EquipItem extends BaseCommand
{

    public EquipItem()
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
            String item = this.item;
            if(item.contains("."))
                item = item.substring(item.lastIndexOf(".") + 1);
            if(cont.contains(item))
                actual.setPrimaryHandItem(cont.FindAndReturn(item));
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
    String item;
}
