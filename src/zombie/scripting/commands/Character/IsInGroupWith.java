// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsInGroupWith.java

package zombie.scripting.commands.Character;

import zombie.characters.*;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.*;

public class IsInGroupWith extends BaseCommand
{

    public IsInGroupWith()
    {
        modifier = 0;
        Other = "";
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
        Other = params[0].trim();
    }

    public boolean getValue()
    {
        if(currentinstance != null && currentinstance.HasAlias(owner))
        {
            chr = currentinstance.getAlias(owner);
        } else
        {
            if(module.getCharacter(owner) == null)
                return false;
            if(module.getCharacter(owner).Actual == null)
                return false;
            chr = module.getCharacter(owner).Actual;
        }
        IsoGameCharacter other;
        if(currentinstance != null && currentinstance.HasAlias(Other))
        {
            other = currentinstance.getAlias(Other);
        } else
        {
            if(module.getCharacter(Other) == null)
                return false;
            if(module.getCharacter(Other).Actual == null)
                return false;
            other = module.getCharacter(Other).Actual;
        }
        if(chr == null)
            return false;
        if(chr.getDescriptor().getGroup() != null && chr.getDescriptor().getGroup().IsMember(other.getDescriptor()))
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
    String Other;
    boolean invert;
}
