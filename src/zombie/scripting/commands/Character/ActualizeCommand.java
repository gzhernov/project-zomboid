// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ActualizeCommand.java

package zombie.scripting.commands.Character;

import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.*;

public class ActualizeCommand extends BaseCommand
{

    public ActualizeCommand()
    {
    }

    public void init(String object, String params[])
    {
        if(params.length == 1)
        {
            Waypoint pt = module.getWaypoint(params[0]);
            x = pt.x;
            y = pt.y;
            z = pt.z;
            owner = object;
        }
    }

    public void begin()
    {
        module.getCharacter(owner).Actualise(x, y, z);
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
    int x;
    int y;
    int z;
}
