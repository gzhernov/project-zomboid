// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ZombieGroup.java

package zombie.characters;


// Referenced classes of package zombie.characters:
//            IsoZombie

public class ZombieGroup
{

    public ZombieGroup()
    {
        zombies = new IsoZombie[MaxSize];
        Leader = null;
    }

    void update()
    {
        IsoZombie oldLeader = Leader;
        if(Leader == null || Leader.isDead())
        {
            IsoZombie z = null;
            int n = 0;
            do
            {
                z = zombies[n];
                if(z != null && z.isDead())
                {
                    zombies[n] = null;
                    z = null;
                }
            } while(++n < MaxSize && (z == null || z.isDead()));
            if(n < MaxSize)
            {
                Leader = z;
                if(oldLeader == null);
            }
        }
    }

    public static int MaxSize = 200;
    public IsoZombie zombies[];
    public IsoZombie Leader;

}
