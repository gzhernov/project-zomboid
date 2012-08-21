// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RootBehavior.java

package zombie.behaviors;

import zombie.characters.IsoGameCharacter;
import zombie.core.Collections.NulledArrayList;

// Referenced classes of package zombie.behaviors:
//            Behavior, DecisionPath

public class RootBehavior extends Behavior
{

    public RootBehavior()
    {
    }

    public Behavior.BehaviorResult process(DecisionPath path, IsoGameCharacter character)
    {
        if(childNodes.size() == 0)
            return Behavior.BehaviorResult.Working;
        for(int ID = 0; ID < childNodes.size(); ID++)
            processChild(path, character, ID);

        return Behavior.BehaviorResult.Working;
    }

    public void reset()
    {
    }

    public boolean valid()
    {
        return true;
    }
}
