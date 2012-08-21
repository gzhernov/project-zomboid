// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SurvivorPersonality.java

package zombie.characters;

import zombie.characters.personalities.Cowardly;
import zombie.characters.personalities.FriendlyArmed;
import zombie.characters.personalities.GunNut;

// Referenced classes of package zombie.characters:
//            IsoSurvivor

public class SurvivorPersonality
{
	
	
	/*    */   public static enum Personality
	/*    */   {
	/* 36 */     GunNut, 
	/* 37 */     Kate, 
	/* 38 */     FriendlyArmed, 
	/* 39 */     Cowardly;
	/*    */   }


    public SurvivorPersonality()
    {
    }

    public int getZombieFleeAmount()
    {
        return 10;
    }

    public float getPlayerDistanceComfort()
    {
        return 5F;
    }

    public int getZombieIgnoreOrdersCount()
    {
        return 4;
    }

    public static SurvivorPersonality CreatePersonality(Personality type)
    {
        SurvivorPersonality p = null;
        if(type == Personality.GunNut)
            p = new GunNut();
        if(type == Personality.FriendlyArmed)
            p = new FriendlyArmed();
        if(type == Personality.Cowardly)
            p = new Cowardly();
        if(p != null)
            p.type = type;
        return p;
    }

    public void CreateBehaviours(IsoSurvivor isosurvivor)
    {
    }

    public int getHuntZombieRange()
    {
        return 5;
    }

    public Personality type;
}
