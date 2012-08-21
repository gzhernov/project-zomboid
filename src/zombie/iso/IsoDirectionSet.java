// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoDirectionSet.java

package zombie.iso;


// Referenced classes of package zombie.iso:
//            IsoDirections

public class IsoDirectionSet
{

    public IsoDirectionSet()
    {
        set = 0;
    }

    public void AddDirection(IsoDirections dir)
    {
        set = set | 1 << dir.index();
    }

    public void AddDirection(int dir)
    {
        dir %= 8;
        set = set | 1 << dir;
    }

    public IsoDirections getNext()
    {
        for(int i = 0; i < 8; i++)
        {
            int bit = 1 << i;
            if((set & bit) != 0)
            {
                set ^= bit;
                return IsoDirections.fromIndex(i);
            }
        }

        return IsoDirections.Max;
    }

    public static IsoDirections rotate(IsoDirections dir, int amount)
    {
        amount += dir.index();
        amount %= 8;
        return IsoDirections.fromIndex(amount);
    }

    public int set;
}
