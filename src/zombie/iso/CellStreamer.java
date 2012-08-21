// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CellStreamer.java

package zombie.iso;


public class CellStreamer
{

    public CellStreamer()
    {
    }

    public static void LoadInitialStream(int wx, int wy, int x, int y)
    {
        int minCellX = x / WidthTiles;
        int minCellY = y / HeightTiles;
        int minWX = wx;
        int minWY = wy;
        int maxWX = wx;
        int maxWY = wy;
        minCellX--;
        minCellY--;
        int maxCellX = minCellX + 2;
        int maxCellY = minCellY + 2;
        if(minCellX < 0)
        {
            minCellX += 3;
            minWX--;
        }
        if(minCellY < 0)
        {
            minCellY += 3;
            minWY--;
        }
        if(minCellX > 2)
        {
            minCellX -= 3;
            minWX++;
        }
        if(minCellY > 2)
        {
            minCellY -= 3;
            minWY++;
        }
    }

    public static int WidthTiles = 50;
    public static int HeightTiles = 50;

}
