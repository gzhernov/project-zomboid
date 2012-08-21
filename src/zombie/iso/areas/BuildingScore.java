// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BuildingScore.java

package zombie.iso.areas;


// Referenced classes of package zombie.iso.areas:
//            IsoBuilding

public class BuildingScore
{

    public BuildingScore(IsoBuilding b)
    {
        building = b;
    }

    public float weapons;
    public float food;
    public float wood;
    public float defense;
    public IsoBuilding building;
    public int size;
    public int safety;
}
