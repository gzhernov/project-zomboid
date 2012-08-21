// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BlockInfo.java

package zombie.iso;


public class BlockInfo
{

    public BlockInfo()
    {
        ThroughDoor = false;
        ThroughStairs = false;
        ThroughWindow = false;
    }

    public boolean isThroughDoor()
    {
        return ThroughDoor;
    }

    public boolean isThroughStairs()
    {
        return ThroughStairs;
    }

    public boolean isThroughWindow()
    {
        return ThroughWindow;
    }

    public boolean ThroughDoor;
    public boolean ThroughStairs;
    public boolean ThroughWindow;
}
