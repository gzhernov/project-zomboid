// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:44
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CollisionMatrixPrototypes.java

package zombie;

import java.util.HashMap;

// Referenced classes of package zombie:
//            BitMatrix

public class CollisionMatrixPrototypes
{

    public CollisionMatrixPrototypes()
    {
        Map = new HashMap();
    }

    public int ToBitMatrix(boolean bools[][][])
    {
        int ret = 0;
        for(int x = 0; x < 3; x++)
        {
            for(int y = 0; y < 3; y++)
            {
                for(int z = 0; z < 3; z++)
                    if(bools[x][y][z])
                        ret = BitMatrix.Set(ret, x - 1, y - 1, z - 1, true);

            }

        }

        return ret;
    }

    public boolean[][][] Add(int bitMatrix)
    {
        if(Map.containsKey(Integer.valueOf(bitMatrix)))
            return (boolean[][][])Map.get(Integer.valueOf(bitMatrix));
        boolean n[][][] = new boolean[3][3][3];
        for(int x = 0; x < 3; x++)
        {
            for(int y = 0; y < 3; y++)
            {
                for(int z = 0; z < 3; z++)
                    n[x][y][z] = BitMatrix.Is(bitMatrix, x - 1, y - 1, z - 1);

            }

        }

        Map.put(Integer.valueOf(bitMatrix), n);
        return n;
    }

    public static CollisionMatrixPrototypes instance = new CollisionMatrixPrototypes();
    public HashMap Map;

}