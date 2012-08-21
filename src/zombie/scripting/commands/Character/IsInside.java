// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsInside.java

package zombie.scripting.commands.Character;

import zombie.characters.IsoGameCharacter;
import zombie.iso.IsoGridSquare;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptModule;

public class IsInside extends BaseCommand
{

    public IsInside()
    {
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
    }

    public boolean getValue()
    {
        IsoGameCharacter chr = module.getCharacterActual(owner);
        if(chr == null)
            return false;
        if(invert)
            return chr.getCurrentSquare().getRoom() == null;
        else
            return chr.getCurrentSquare().getRoom() != null;
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
    boolean invert;
}
