// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TestStat.java

package zombie.scripting.commands.Character;

import zombie.characters.IsoGameCharacter;
import zombie.characters.SurvivorDesc;
import zombie.core.Rand;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.*;

public class TestStat extends BaseCommand
{

    public TestStat()
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
        stat = params[0].trim();
        if(params.length > 1)
            modifier = Integer.parseInt(params[1].trim());
    }

    public boolean getValue()
    {
        float stat = 0.0F;
        if(currentinstance != null && currentinstance.HasAlias(owner))
            chr = currentinstance.getAlias(owner);
        else
            chr = module.getCharacter(owner).Actual;
        if(chr == null)
            return false;
        if(this.stat.contains("Compassion"))
            stat = chr.getDescriptor().getCompassion();
        if(this.stat.contains("Bravery"))
            stat = chr.getDescriptor().getBravery() * 2.0F;
        if(this.stat.contains("Loner"))
            stat = chr.getDescriptor().getLoner();
        if(this.stat.contains("Temper"))
            stat = chr.getDescriptor().getTemper();
        stat *= 10F;
        if(invert)
            return (float)Rand.Next(100) >= stat + (float)modifier;
        else
            return (float)Rand.Next(100) < stat + (float)modifier;
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
