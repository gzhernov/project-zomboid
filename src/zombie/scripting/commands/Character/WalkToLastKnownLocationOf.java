// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WalkToLastKnownLocationOf.java

package zombie.scripting.commands.Character;

import java.security.InvalidParameterException;
import zombie.behaviors.Behavior;
import zombie.behaviors.general.PathFindBehavior;
import zombie.characters.IsoGameCharacter;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptCharacter;
import zombie.scripting.objects.ScriptModule;

public class WalkToLastKnownLocationOf extends BaseCommand
{

    public WalkToLastKnownLocationOf()
    {
        timer = 0;
        res = zombie.behaviors.Behavior.BehaviorResult.Working;
        behavior = new PathFindBehavior(true);
        bDone = false;
    }

    public void init(String object, String params[])
    {
        if(params.length == 1)
        {
            chara = params[0].trim();
            owner = object;
        }
    }

    public void begin()
    {
        if(module.getCharacter(owner).Actual == null)
            throw new InvalidParameterException();
        if(module.getCharacter(chara).Actual == null)
            throw new InvalidParameterException();
        aowner = module.getCharacter(owner).Actual;
        zombie.characters.IsoGameCharacter.Location loc = aowner.getLastKnownLocationOf(chara);
        if(loc != null)
        {
            behavior.sx = (int)aowner.getX();
            behavior.sy = (int)aowner.getY();
            behavior.sz = (int)aowner.getZ();
            behavior.tx = loc.x;
            behavior.ty = loc.y;
            behavior.tz = loc.z;
            behavior.pathIndex = 0;
        } else
        {
            bDone = true;
        }
        timer = 10;
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
        return bDone || res == zombie.behaviors.Behavior.BehaviorResult.Succeeded;
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

    String chara;
    int x;
    int y;
    int z;
    int timer;
    zombie.behaviors.Behavior.BehaviorResult res;
    PathFindBehavior behavior;
    String owner;
    boolean bDone;
    IsoGameCharacter aowner;
}
