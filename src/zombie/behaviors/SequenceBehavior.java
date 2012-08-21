// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SequenceBehavior.java

package zombie.behaviors;

import zombie.characters.IsoGameCharacter;
import zombie.core.Collections.NulledArrayList;

// Referenced classes of package zombie.behaviors:
//            Behavior, DecisionPath

public class SequenceBehavior extends Behavior
{

    public SequenceBehavior()
    {
        ID = 0;
        ProcessNextOnFail = false;
    }

    public Behavior.BehaviorResult process(DecisionPath path, IsoGameCharacter character)
    {
        if(ID >= childNodes.size())
            return Behavior.BehaviorResult.Succeeded;
        while(ID < childNodes.size()) 
        {
            Behavior.BehaviorResult res = processChild(path, character, ID);
            if(res == Behavior.BehaviorResult.Succeeded)
                ID++;
            else
            if(res == Behavior.BehaviorResult.Failed)
            {
                if(ProcessNextOnFail)
                {
                    ID++;
                } else
                {
                    ID = 0;
                    return res;
                }
            } else
            {
                return res;
            }
            if(ID == childNodes.size() && (res == Behavior.BehaviorResult.Succeeded || res == Behavior.BehaviorResult.Failed && ProcessNextOnFail))
            {
                ID = 0;
                return Behavior.BehaviorResult.Succeeded;
            }
        }
        return Behavior.BehaviorResult.Working;
    }

    public void reset()
    {
        ID = 0;
        for(int n = 0; n < childNodes.size(); n++)
            ((Behavior)childNodes.get(n)).reset();

    }

    public boolean valid()
    {
        return true;
    }

    public int ID;
    public boolean ProcessNextOnFail;
}
