// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsNumberOfLocalOver.java

package zombie.scripting.commands.Character;

import zombie.characters.IsoGameCharacter;
import zombie.core.Collections.NulledArrayList;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.*;

public class IsNumberOfLocalOver extends BaseCommand
{

    public IsNumberOfLocalOver()
    {
        number = 0;
        invert = false;
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
        if(object.indexOf("!") == 0)
        {
            invert = true;
            object = object.substring(1);
        }
        number = Integer.parseInt(params[0].trim());
        owner = object;
    }

    public boolean getValue()
    {
        if(currentinstance != null && currentinstance.HasAlias(owner))
            chr = currentinstance.getAlias(owner);
        else
            chr = module.getCharacter(owner).Actual;
        if(chr == null)
            return false;
        if(number < chr.getLocalList().size())
            return !invert;
        else
            return invert;
    }

    public void begin()
    {
    }

    public boolean AllowCharacterBehaviour(String scriptCharacter)
    {
        return true;
    }

    public boolean DoesInstantly()
    {
        return true;
    }

    String owner;
    String stat;
    int number;
    IsoGameCharacter chr;
    boolean invert;
}
