// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WaitUntilFollowersArrive.java

package zombie.behaviors.survivor.orders;

import zombie.behaviors.Behavior;
import zombie.behaviors.survivor.SatisfyIdleBehavior;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;

// Referenced classes of package zombie.behaviors.survivor.orders:
//            Order, FollowOrder

public class WaitUntilFollowersArrive extends Order
{

    public WaitUntilFollowersArrive(IsoGameCharacter chr)
    {
        super(chr);
        timeout = 600;
        idle = new SatisfyIdleBehavior();
    }

    public zombie.behaviors.Behavior.BehaviorResult process()
    {
        idle.process(null, character);
        timeout--;
        return zombie.behaviors.Behavior.BehaviorResult.Working;
    }

    public boolean complete()
    {
        if(timeout <= 0)
            return true;
        for(int n = 0; n < character.getDescriptor().getGroup().Members.size(); n++)
        {
            SurvivorDesc desc = (SurvivorDesc)character.getDescriptor().getGroup().Members.get(n);
            if(desc.getInstance() != null && desc.getInstance() != character && !desc.getInstance().isDead() && (desc.getInstance().getOrder() instanceof FollowOrder) && ((FollowOrder)desc.getInstance().getOrder()).target == character && !desc.getInstance().InBuildingWith(character))
                return false;
        }

        return true;
    }

    public void update()
    {
    }

    SatisfyIdleBehavior idle;
    int timeout;
}
