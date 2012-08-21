// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Behavior.java

package zombie.behaviors;

import java.util.Stack;
import zombie.characters.IsoGameCharacter;
import zombie.core.Collections.NulledArrayList;

// Referenced classes of package zombie.behaviors:
//            DecisionPath

public abstract class Behavior
{
	/*    */   public static enum BehaviorResult
	/*    */   {
	/* 46 */     Failed, 
	/* 47 */     Working, 
	/* 48 */     Succeeded;
	/*    */   }


    public Behavior()
    {
        last = BehaviorResult.Working;
    }

    public float getPathSpeed()
    {
        return 0.06F;
    }

    public int renderDebug(int y)
    {
        return y;
    }

    public void update()
    {
    }

    public void onSwitch()
    {
    }

    public abstract BehaviorResult process(DecisionPath decisionpath, IsoGameCharacter isogamecharacter);

    public abstract void reset();

    public abstract boolean valid();

    public void addChild(Behavior child)
    {
        childNodes.add(child);
    }

    public BehaviorResult processChild(DecisionPath path, IsoGameCharacter character, int id)
    {
        if(!((Behavior)childNodes.get(id)).valid())
        {
            return BehaviorResult.Failed;
        } else
        {
            path.DecisionPath.push(this);
            BehaviorResult result = ((Behavior)childNodes.get(id)).process(path, character);
            path.DecisionPath.pop();
            return result;
        }
    }

    public BehaviorResult last;
    protected final NulledArrayList childNodes = new NulledArrayList(3);
}
