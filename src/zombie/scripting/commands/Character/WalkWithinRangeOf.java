// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WalkWithinRangeOf.java

package zombie.scripting.commands.Character;

import zombie.behaviors.Behavior;
import zombie.behaviors.general.PathFindBehavior;
import zombie.characters.IsoGameCharacter;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.*;

public class WalkWithinRangeOf extends BaseCommand
{

    public WalkWithinRangeOf()
    {
        res = zombie.behaviors.Behavior.BehaviorResult.Working;
        behavior = new PathFindBehavior(true);
        cycle = 120;
    }

    public boolean IsFinished()
    {
        return true;
    }

    public void update()
    {
        cycle--;
        if(cycle <= 0)
        {
            cycle = 120;
            behavior.sx = (int)chr.getX();
            behavior.sy = (int)chr.getY();
            behavior.sz = (int)chr.getZ();
            behavior.tx = (int)other.getX();
            behavior.ty = (int)other.getY();
            behavior.tz = (int)other.getZ();
            behavior.pathIndex = 0;
        }
        res = behavior.process(null, chr);
    }

    public void init(String object, String params[])
    {
        owner = object;
        String total = "";
        Other = params[0].trim();
        if(Other.equals("null"))
            Other = null;
        range = Integer.parseInt(params[1].trim());
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
        behavior.sx = (int)chr.getX();
        behavior.sy = (int)chr.getY();
        behavior.sz = (int)chr.getZ();
        behavior.tx = (int)other.getX();
        behavior.ty = (int)other.getY();
        behavior.tz = (int)other.getZ();
        behavior.pathIndex = 0;
    }

    public boolean DoesInstantly()
    {
        return true;
    }

    public boolean AllowCharacterBehaviour(String scriptCharacter)
    {
        return true;
    }

    public void Finish()
    {
        res = zombie.behaviors.Behavior.BehaviorResult.Working;
        behavior.reset();
    }

    String owner;
    String say;
    String Other;
    IsoGameCharacter other;
    IsoGameCharacter chr;
    int range;
    zombie.behaviors.Behavior.BehaviorResult res;
    PathFindBehavior behavior;
    int cycle;
}
