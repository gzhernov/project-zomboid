// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsDead.java

package zombie.scripting.commands.Character;

import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.IsoGameCharacter;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.*;

public class IsDead extends BaseCommand
{

    public IsDead()
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
        IsoGameCharacter chr = null;
        if(currentinstance != null && currentinstance.HasAlias(owner))
            chr = currentinstance.getAlias(owner);
        else
            chr = module.getCharacter(owner).Actual;
        if(chr == null)
            return false;
        if(invert)
            return chr.getHealth() > 0.0F && chr.getBodyDamage().getHealth() > 0.0F;
        else
            return chr.getHealth() <= 0.0F || chr.getBodyDamage().getHealth() <= 0.0F;
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
