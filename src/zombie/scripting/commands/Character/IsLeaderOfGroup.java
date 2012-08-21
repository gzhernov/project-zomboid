// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsLeaderOfGroup.java

package zombie.scripting.commands.Character;

import zombie.characters.*;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.*;

public class IsLeaderOfGroup extends BaseCommand
{

    public IsLeaderOfGroup()
    {
        modifier = 0;
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
        if(chr.getDescriptor().getGroup() != null && chr.getDescriptor().getGroup().Leader == chr.getDescriptor())
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
    int modifier;
    IsoGameCharacter chr;
    boolean invert;
}
