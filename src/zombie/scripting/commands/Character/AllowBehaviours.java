// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AllowBehaviours.java

package zombie.scripting.commands.Character;

import zombie.characters.IsoGameCharacter;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptModule;

public class AllowBehaviours extends BaseCommand
{

    public AllowBehaviours()
    {
        allow = false;
    }

    public void init(String object, String params[])
    {
        if(params.length == 1)
        {
            allow = params[0].trim().equals("true");
            owner = object;
        }
    }

    public void begin()
    {
        IsoGameCharacter chr = module.getCharacterActual(owner);
        if(chr == null)
        {
            return;
        } else
        {
            chr.setAllowBehaviours(allow);
            return;
        }
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

    public String command;
    public String chr;
    public String params[];
    String owner;
    boolean allow;
}
