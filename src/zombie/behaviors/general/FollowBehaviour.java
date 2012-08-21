// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FollowBehaviour.java

package zombie.behaviors.general;

import zombie.behaviors.Behavior;
import zombie.behaviors.DecisionPath;
import zombie.characters.IsoGameCharacter;
import zombie.characters.SurvivorPersonality;
import zombie.core.Rand;
import zombie.inventory.InventoryItem;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoUtils;

// Referenced classes of package zombie.behaviors.general:
//            PathFindBehavior

public class FollowBehaviour extends Behavior
{

    public FollowBehaviour()
    {
        thinkTime = 30;
        thinkTimeMax = 30;
        stayInside = false;
        pathFind = new PathFindBehavior("FollowBehaviour");
        Target = null;
        weapon = null;
        timeout = 180;
    }

    public zombie.behaviors.Behavior.BehaviorResult process(DecisionPath path, IsoGameCharacter character)
    {
        character.setFollowingTarget(Target);
        boolean PickedTargetThisFrame = false;
        timeout--;
        if(timeout > 0);
        if(Target != null && Target.getHealth() <= 0.0F)
        {
            Target = null;
            weapon = null;
            timeout = 180;
            return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
        }
        thinkTime--;
        if(Target == null && thinkTime <= 0)
        {
            Target = character.getCurrentSquare().FindFriend(character, character.getPersonality().getHuntZombieRange(), character.getEnemyList());
            if(Target != null && Target.getCurrentSquare() != null)
                PickedTargetThisFrame = true;
            if(Rand.Next(2) != 0)
                character.setPathSpeed(0.08F);
            else
                character.setPathSpeed(0.05F);
            thinkTime = thinkTimeMax;
            pathFind.sx = character.getCurrentSquare().getX();
            pathFind.sy = character.getCurrentSquare().getY();
            pathFind.sz = character.getCurrentSquare().getZ();
        }
        if(Target == null)
        {
            weapon = null;
            timeout = 180;
            return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
        }
        IsoGridSquare mine = character.getCurrentSquare();
        IsoGridSquare theirs = Target.getCurrentSquare();
        if(mine != null && theirs != null)
        {
            float dist = IsoUtils.DistanceManhatten(mine.getX(), mine.getY(), theirs.getX(), theirs.getY());
            if(theirs.getZ() != mine.getZ() || 5F < dist)
            {
                if(PickedTargetThisFrame)
                {
                    pathFind.tx = theirs.getX();
                    pathFind.ty = theirs.getY();
                    pathFind.tz = theirs.getZ();
                    PickedTargetThisFrame = false;
                }
                zombie.behaviors.Behavior.BehaviorResult res = pathFind.process(path, character);
                if(res == zombie.behaviors.Behavior.BehaviorResult.Failed)
                {
                    Target = null;
                    weapon = null;
                    thinkTime = thinkTimeMax;
                    return res;
                }
                if(res == zombie.behaviors.Behavior.BehaviorResult.Succeeded)
                {
                    Target = null;
                    weapon = null;
                    thinkTime = 0;
                    return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
                }
            } else
            {
                timeout = 180;
                return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
            }
        }
        return zombie.behaviors.Behavior.BehaviorResult.Working;
    }

    public void reset()
    {
        Target = null;
        weapon = null;
        timeout = 180;
        pathFind.reset();
    }

    public boolean valid()
    {
        return true;
    }

    public int thinkTime;
    public int thinkTimeMax;
    public boolean stayInside;
    PathFindBehavior pathFind;
    IsoGameCharacter Target;
    InventoryItem weapon;
    int timeout;
}
