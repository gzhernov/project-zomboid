// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FaceCommand.java

package zombie.scripting.commands.Character;

import zombie.characters.IsoGameCharacter;
import zombie.iso.IsoDirections;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.*;

public class FaceCommand extends BaseCommand
{

    public FaceCommand()
    {
    }

    public void init(String object, String params[])
    {
        owner = object;
        dir = IsoDirections.valueOf(params[0]);
    }

    public void begin()
    {
        IsoGameCharacter chr = null;
        if(currentinstance != null && currentinstance.HasAlias(owner))
            chr = currentinstance.getAlias(owner);
        else
            chr = module.getCharacter(owner).Actual;
        if(chr == null)
        {
            return;
        } else
        {
            chr.setDir(dir);
            return;
        }
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
    IsoDirections dir;
}
