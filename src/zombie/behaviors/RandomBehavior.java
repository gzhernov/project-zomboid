// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RandomBehavior.java

package zombie.behaviors;

import zombie.characters.IsoGameCharacter;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;

// Referenced classes of package zombie.behaviors:
//            Behavior, DecisionPath

public class RandomBehavior extends Behavior
{

    public RandomBehavior()
    {
    }

    public Behavior.BehaviorResult process(DecisionPath path, IsoGameCharacter character)
    {
        return processChild(path, character, Rand.Next(childNodes.size()));
    }

    public void reset()
    {
    }

    public boolean valid()
    {
        return true;
    }
}
