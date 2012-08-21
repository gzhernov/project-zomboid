// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AddEnemy.java

package zombie.scripting.commands.Character;

import java.util.Stack;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoSurvivor;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.*;

public class AddEnemy extends BaseCommand
{

    public AddEnemy()
    {
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
        owner = object;
        String total = "";
        Other = params[0].trim();
    }

    public void begin()
    {
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
        IsoGameCharacter other;
        if(currentinstance != null && currentinstance.HasAlias(Other))
        {
            other = currentinstance.getAlias(Other);
        } else
        {
            if(module.getCharacter(Other) == null)
                return;
            if(module.getCharacter(Other).Actual == null)
                return;
            other = module.getCharacter(Other).Actual;
        }
        if(chr instanceof IsoSurvivor)
            ((IsoSurvivor)chr).getEnemyList().add(other);
    }

    public boolean DoesInstantly()
    {
        return true;
    }

    String owner;
    String say;
    String Other;
    IsoGameCharacter chr;
}
