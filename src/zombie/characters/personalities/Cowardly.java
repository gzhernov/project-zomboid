// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Cowardly.java

package zombie.characters.personalities;

import zombie.behaviors.SequenceBehavior;
import zombie.behaviors.survivor.MasterSurvivorBehavior;
import zombie.characters.IsoSurvivor;
import zombie.characters.SurvivorPersonality;

public class Cowardly extends SurvivorPersonality
{

    public Cowardly()
    {
    }

    public void CreateBehaviours(IsoSurvivor survivor)
    {
        survivor.setMasterProper(new MasterSurvivorBehavior(survivor));
        survivor.getMasterBehaviorList().addChild(survivor.getMasterProper());
        survivor.getMasterBehaviorList().addChild(survivor.behaviours);
    }

    public int getHuntZombieRange()
    {
        return 10;
    }

    public int getZombieFleeAmount()
    {
        return 1;
    }
}
