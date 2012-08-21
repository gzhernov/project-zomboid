// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsOnFloor.java

package zombie.scripting.commands.Character;

import zombie.characters.IsoGameCharacter;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptModule;

public class IsOnFloor extends BaseCommand
{

    public IsOnFloor()
    {
        min = 0;
        max = 0;
        invert = false;
    }

    public void init(String object, String params[])
    {
        owner = object;
        if(owner.indexOf("!") == 0)
        {
            invert = true;
            owner = owner.substring(1);
        }
        if(params.length == 1)
            min = max = Integer.parseInt(params[0].trim());
        if(params.length == 2)
        {
            min = Integer.parseInt(params[0].trim());
            max = Integer.parseInt(params[1].trim());
        }
    }

    public boolean getValue()
    {
        IsoGameCharacter chr = module.getCharacterActual(owner);
        if(chr == null)
            return false;
        if(invert)
            return chr.getZ() < (float)min || chr.getZ() > (float)max;
        else
            return chr.getZ() >= (float)min && chr.getZ() <= (float)max;
    }

    public void begin()
    {
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
    int min;
    int max;
    boolean invert;
}
