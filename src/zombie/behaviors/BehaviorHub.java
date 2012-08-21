// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BehaviorHub.java

package zombie.behaviors;

import gnu.trove.map.hash.THashMap;
import java.util.*;
import zombie.characters.IsoGameCharacter;

// Referenced classes of package zombie.behaviors:
//            Behavior, DecisionPath

public class BehaviorHub extends Behavior
{
    public class BehaviorTrigger
    {

        public float Decay;
        public String Name;
        public Behavior TriggerBehavior;
        public float TriggerValue;
        public float Value;
        private float LastValue;
       



        public BehaviorTrigger()
        {

        }
    }


    public BehaviorHub()
    {
        TriggerMap = new THashMap();
    }

    public BehaviorTrigger AddTrigger(String name, float startValue, float triggerValue, float decay, Behavior behavior)
    {
        BehaviorTrigger trig = new BehaviorTrigger();
        trig.Name = name;
        trig.Value = startValue;
        trig.TriggerValue = triggerValue;
        trig.TriggerBehavior = behavior;
        trig.Decay = decay;
        trig.LastValue = startValue;
        TriggerMap.put(name, trig);
        return trig;
    }

    public void ChangeTriggerValue(String name, float amount)
    {
        ((BehaviorTrigger)TriggerMap.get(name)).LastValue = ((BehaviorTrigger)TriggerMap.get(name)).Value;
        ((BehaviorTrigger)TriggerMap.get(name)).Value += amount;
    }

    public Behavior.BehaviorResult process(DecisionPath path, IsoGameCharacter character)
    {
        path.DecisionPath.push(this);
        for(Iterator i$ = TriggerMap.values().iterator(); i$.hasNext();)
        {
            BehaviorTrigger trigger = (BehaviorTrigger)i$.next();
            if(trigger.Value > 1.0F)
                trigger.Value = 1.0F;
            trigger.Value -= trigger.Decay;
            if(trigger.Value < 0.0F)
                trigger.Value = 0.0F;
            if(trigger.Value >= trigger.TriggerValue)
            {
                if(trigger.LastValue < trigger.TriggerValue)
                    trigger.TriggerBehavior.reset();
                Behavior.BehaviorResult res = trigger.TriggerBehavior.process(path, character);
                path.DecisionPath.pop();
                if(res == Behavior.BehaviorResult.Failed)
                    return Behavior.BehaviorResult.Succeeded;
                if(res == Behavior.BehaviorResult.Succeeded)
                    return Behavior.BehaviorResult.Succeeded;
                else
                    return Behavior.BehaviorResult.Working;
            }
        }

        path.DecisionPath.pop();
        return Behavior.BehaviorResult.Succeeded;
    }

    public void reset()
    {
        BehaviorTrigger t;
        for(Iterator i$ = TriggerMap.values().iterator(); i$.hasNext(); t.TriggerBehavior.reset())
            t = (BehaviorTrigger)i$.next();

    }

    public void SetTriggerValue(String name, float amount)
    {
        if(!TriggerMap.containsKey(name))
        {
            return;
        } else
        {
            ((BehaviorTrigger)TriggerMap.get(name)).LastValue = ((BehaviorTrigger)TriggerMap.get(name)).Value;
            ((BehaviorTrigger)TriggerMap.get(name)).Value = amount;
            return;
        }
    }

    public boolean valid()
    {
        return true;
    }

    public THashMap TriggerMap;
}
