// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IdleOrder.java

package zombie.behaviors.survivor.orders;

import zombie.behaviors.Behavior;
import zombie.behaviors.survivor.SatisfyIdleBehavior;
import zombie.characters.IsoGameCharacter;

// Referenced classes of package zombie.behaviors.survivor.orders:
//            Order

public class IdleOrder extends Order
{

    public IdleOrder(IsoGameCharacter chr)
    {
        super(chr);
        idle = new SatisfyIdleBehavior();
    }

    public zombie.behaviors.Behavior.BehaviorResult process()
    {
        idle.process(null, character);
        return zombie.behaviors.Behavior.BehaviorResult.Working;
    }

    public boolean complete()
    {
        return false;
    }

    public void update()
    {
    }

    public float getPriority(IsoGameCharacter character)
    {
        return 200F;
    }

    SatisfyIdleBehavior idle;
}
