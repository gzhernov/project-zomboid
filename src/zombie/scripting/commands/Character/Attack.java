// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Attack.java

package zombie.scripting.commands.Character;

import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoLivingCharacter;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.*;

public class Attack extends BaseCommand
{

    public Attack()
    {
    }

    public void init(String object, String params[])
    {
        owner = object;
    }

    public void begin()
    {
        IsoGameCharacter chr = null;
        if(currentinstance != null && currentinstance.HasAlias(owner))
        {
            chr = currentinstance.getAlias(owner);
        } else
        {
            if(module.getCharacter(owner) == null)
                return;
            if(module.getCharacter(owner).Actual == null)
                return;
            chr = module.getCharacter(owner).Actual;
        }
        ((IsoLivingCharacter)chr).AttemptAttack(1.0F);
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
}
