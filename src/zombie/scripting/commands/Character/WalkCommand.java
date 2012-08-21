// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WalkCommand.java

package zombie.scripting.commands.Character;

import java.security.InvalidParameterException;
import zombie.behaviors.Behavior;
import zombie.behaviors.general.PathFindBehavior;
import zombie.characters.IsoGameCharacter;
import zombie.core.Collections.NulledArrayList;
import zombie.iso.*;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.*;

public class WalkCommand extends BaseCommand
{

    public void updateskip()
    {
        IsoGameCharacter Actual = module.getCharacterActual(owner);
        Actual.setX((float)x + 0.5F);
        Actual.setY((float)y + 0.5F);
        Actual.setZ(z);
        Actual.getCurrentSquare().getMovingObjects().remove(Actual);
        Actual.setCurrent(IsoWorld.instance.CurrentCell.getGridSquare(x, y, z));
        if(Actual.getCurrentSquare() != null)
            Actual.getCurrentSquare().getMovingObjects().add(Actual);
    }

    public WalkCommand()
    {
        res = zombie.behaviors.Behavior.BehaviorResult.Working;
        behavior = new PathFindBehavior(true);
    }

    public void init(String object, String params[])
    {
        if(params.length == 1)
        {
            Waypoint pt = module.getWaypoint(params[0].trim());
            if(pt != null)
            {
                x = pt.x;
                y = pt.y;
                z = pt.z;
            }
            owner = object;
        }
    }

    public void begin()
    {
        if(module.getCharacter(owner).Actual == null)
        {
            throw new InvalidParameterException();
        } else
        {
            aowner = module.getCharacter(owner).Actual;
            behavior.sx = (int)aowner.getX();
            behavior.sy = (int)aowner.getY();
            behavior.sz = (int)aowner.getZ();
            behavior.tx = x;
            behavior.ty = y;
            behavior.tz = z;
            behavior.pathIndex = 0;
            return;
        }
    }

    public boolean AllowCharacterBehaviour(String scriptCharacter)
    {
        return false;
    }

    public void Finish()
    {
        aowner = null;
        res = zombie.behaviors.Behavior.BehaviorResult.Working;
        behavior.reset();
    }

    public boolean IsFinished()
    {
        return res == zombie.behaviors.Behavior.BehaviorResult.Succeeded;
    }

    public void update()
    {
        if(res == zombie.behaviors.Behavior.BehaviorResult.Failed)
            begin();
        res = behavior.process(null, aowner);
    }

    public boolean DoesInstantly()
    {
        return false;
    }

    int x;
    int y;
    int z;
    zombie.behaviors.Behavior.BehaviorResult res;
    PathFindBehavior behavior;
    String owner;
    IsoGameCharacter aowner;
}
