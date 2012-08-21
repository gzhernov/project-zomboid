// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HasInventory.java

package zombie.scripting.commands.Character;

import zombie.characters.IsoGameCharacter;
import zombie.inventory.ItemContainer;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptContainer;
import zombie.scripting.objects.ScriptModule;

public class HasInventory extends BaseCommand
{

    public HasInventory()
    {
        invert = false;
    }

    public void begin()
    {
    }

    public boolean getValue()
    {
        IsoGameCharacter actual = module.getCharacterActual(character);
        ItemContainer cont = null;
        if(actual != null)
        {
            cont = actual.getInventory();
        } else
        {
            ScriptContainer chr = module.getScriptContainer(character);
            if(chr == null)
                return false;
            cont = chr.getActual();
            if(cont == null)
                return false;
        }
        if(invert)
            return !cont.contains(item);
        else
            return cont.contains(item);
    }

    public boolean IsFinished()
    {
        return true;
    }

    public void update()
    {
    }

    public void init(String object, String params[])
    {
        character = object;
        item = params[0].replace("\"", "");
        if(character.indexOf("!") == 0)
        {
            invert = true;
            character = character.substring(1);
        }
    }

    public boolean DoesInstantly()
    {
        return true;
    }

    boolean invert;
    String character;
    String item;
}
