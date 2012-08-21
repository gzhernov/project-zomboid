// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LosUtil.java

package zombie.iso;

import zombie.characters.IsoGameCharacter;

// Referenced classes of package zombie.iso:
//            IsoCell, IsoGridSquare, IsoMovingObject

public class LosUtil
{

	/*     */   public static enum TestResults
	/*     */   {
	/*  31 */     Clear, 
	/*  32 */     ClearThroughOpenDoor, 
	/*  33 */     ClearThroughWindow, 
	/*  34 */     Blocked;
	/*     */   }


    public LosUtil()
    {
    }

    public static int DistToWindow(IsoCell cell, int x1, int y1, int z1, int x0, int y0, int z0)
    {
        int dy = y1 - y0;
        int dx = x1 - x0;
        int dz = z1 - z0;
        int dist = 0;
        float t = 0.5F;
        float t2 = 0.5F;
        int lx = x0;
        int ly = y0;
        int lz = z0;
        IsoGridSquare b = cell.getGridSquare(lx, ly, lz);
        if(Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > Math.abs(dz))
        {
            float m = (float)dy / (float)dx;
            float m2 = (float)dz / (float)dx;
            t += y0;
            t2 += z0;
            dx = dx >= 0 ? 1 : -1;
            m *= dx;
            m2 *= dx;
            while(x0 != x1) 
            {
                x0 += dx;
                t += m;
                t2 += m2;
                IsoGridSquare a = cell.getGridSquare(x0, (int)t, (int)t2);
                if(a != null && b != null && a.testVisionAdjacent(b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ(), true, true) == TestResults.ClearThroughWindow)
                    return dist;
                b = a;
                lx = x0;
                ly = (int)t;
                lz = (int)t2;
                dist++;
            }
        } else
        if(Math.abs(dy) >= Math.abs(dx) && Math.abs(dy) > Math.abs(dz))
        {
            float m = (float)dx / (float)dy;
            float m2 = (float)dz / (float)dy;
            t += x0;
            t2 += z0;
            dy = dy >= 0 ? 1 : -1;
            m *= dy;
            m2 *= dy;
            while(y0 != y1) 
            {
                y0 += dy;
                t += m;
                t2 += m2;
                IsoGridSquare a = cell.getGridSquare((int)t, y0, (int)t2);
                if(a != null && b != null && a.testVisionAdjacent(b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ(), true, true) == TestResults.ClearThroughWindow)
                    return dist;
                b = a;
                lx = (int)t;
                ly = y0;
                lz = (int)t2;
                dist++;
            }
        } else
        {
            float m = (float)dx / (float)dz;
            float m2 = (float)dy / (float)dz;
            t += x0;
            t2 += y0;
            dz = dz >= 0 ? 1 : -1;
            m *= dz;
            m2 *= dz;
            while(z0 != z1) 
            {
                z0 += dz;
                t += m;
                t2 += m2;
                IsoGridSquare a = cell.getGridSquare((int)t, (int)t2, z0);
                if(a != null && b != null && a.testVisionAdjacent(b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ(), true, true) == TestResults.ClearThroughWindow)
                    return dist;
                b = a;
                lx = (int)t;
                ly = (int)t2;
                lz = z0;
                dist++;
            }
        }
        return dist;
    }

    public static TestResults lineClear(IsoCell cell, int x1, int y1, int z1, int x0, int y0, int z0, boolean bIgnoreDoors)
    {
        TestResults test = TestResults.Clear;
        int n;
        if(x0 == 56 && y0 == 20 && z0 == 0)
            n = 0;
        if(x1 == 57 && y1 == 23 && z1 == 0)
            n = 0;
        int dy = y1 - y0;
        int dx = x1 - x0;
        int dz = z1 - z0;
        float t = 0.5F;
        float t2 = 0.5F;
        int lx = x0;
        int ly = y0;
        int lz = z0;
        IsoGridSquare b = cell.getGridSquare(lx, ly, lz);
        if(Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > Math.abs(dz))
        {
            float m = (float)dy / (float)dx;
            float m2 = (float)dz / (float)dx;
            t += y0;
            t2 += z0;
            dx = dx >= 0 ? 1 : -1;
            m *= dx;
            m2 *= dx;
            while(x0 != x1) 
            {
                x0 += dx;
                t += m;
                t2 += m2;
                IsoGridSquare a = cell.getGridSquare(x0, (int)t, (int)t2);
                if(a != null && b != null)
                {
                    TestResults newTest = a.testVisionAdjacent(b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ(), true, bIgnoreDoors);
                    if(newTest == TestResults.Blocked || test == TestResults.Clear || newTest == TestResults.ClearThroughWindow && test == TestResults.ClearThroughOpenDoor)
                        test = newTest;
                    if(test == TestResults.Blocked)
                        return TestResults.Blocked;
                }
                b = a;
                lx = x0;
                ly = (int)t;
                lz = (int)t2;
            }
        } else
        if(Math.abs(dy) >= Math.abs(dx) && Math.abs(dy) > Math.abs(dz))
        {
            float m = (float)dx / (float)dy;
            float m2 = (float)dz / (float)dy;
            t += x0;
            t2 += z0;
            dy = dy >= 0 ? 1 : -1;
            m *= dy;
            m2 *= dy;
            while(y0 != y1) 
            {
                y0 += dy;
                t += m;
                t2 += m2;
                IsoGridSquare a = cell.getGridSquare((int)t, y0, (int)t2);
                if(a != null && b != null)
                {
                    TestResults newTest = a.testVisionAdjacent(b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ(), true, bIgnoreDoors);
                    if(newTest == TestResults.Blocked || test == TestResults.Clear || newTest == TestResults.ClearThroughWindow && test == TestResults.ClearThroughOpenDoor)
                        test = newTest;
                    if(test == TestResults.Blocked)
                        return TestResults.Blocked;
                }
                b = a;
                lx = (int)t;
                ly = y0;
                lz = (int)t2;
            }
        } else
        {
            float m = (float)dx / (float)dz;
            float m2 = (float)dy / (float)dz;
            t += x0;
            t2 += y0;
            dz = dz >= 0 ? 1 : -1;
            m *= dz;
            m2 *= dz;
            while(z0 != z1) 
            {
                z0 += dz;
                t += m;
                t2 += m2;
                IsoGridSquare a = cell.getGridSquare((int)t, (int)t2, z0);
                if(a != null && b != null)
                {
                    TestResults newTest = a.testVisionAdjacent(b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ(), true, bIgnoreDoors);
                    if(newTest == TestResults.Blocked || test == TestResults.Clear || newTest == TestResults.ClearThroughWindow && test == TestResults.ClearThroughOpenDoor)
                        test = newTest;
                    if(test == TestResults.Blocked)
                        return TestResults.Blocked;
                }
                b = a;
                lx = (int)t;
                ly = (int)t2;
                lz = z0;
            }
        }
        return test;
    }

    public static int lineClearCollideCount(IsoGameCharacter chr, IsoCell cell, int x1, int y1, int z1, int x0, int y0, int z0)
    {
        int l = 0;
        int dy = y1 - y0;
        int dx = x1 - x0;
        int dz = z1 - z0;
        float t = 0.5F;
        float t2 = 0.5F;
        int lx = x0;
        int ly = y0;
        int lz = z0;
        IsoGridSquare b = cell.getGridSquare(lx, ly, lz);
        if(Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > Math.abs(dz))
        {
            float m = (float)dy / (float)dx;
            float m2 = (float)dz / (float)dx;
            t += y0;
            t2 += z0;
            dx = dx >= 0 ? 1 : -1;
            m *= dx;
            m2 *= dx;
            while(x0 != x1) 
            {
                x0 += dx;
                t += m;
                t2 += m2;
                IsoGridSquare a = cell.getGridSquare(x0, (int)t, (int)t2);
                if(a != null && b != null)
                {
                    boolean bTest = a.testCollideAdjacent(chr, b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ());
                    if(bTest)
                        return l;
                }
                l++;
                b = a;
                lx = x0;
                ly = (int)t;
                lz = (int)t2;
            }
        } else
        if(Math.abs(dy) >= Math.abs(dx) && Math.abs(dy) > Math.abs(dz))
        {
            float m = (float)dx / (float)dy;
            float m2 = (float)dz / (float)dy;
            t += x0;
            t2 += z0;
            dy = dy >= 0 ? 1 : -1;
            m *= dy;
            m2 *= dy;
            while(y0 != y1) 
            {
                y0 += dy;
                t += m;
                t2 += m2;
                IsoGridSquare a = cell.getGridSquare((int)t, y0, (int)t2);
                if(a != null && b != null)
                {
                    boolean bTest = a.testCollideAdjacent(chr, b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ());
                    if(bTest)
                        return l;
                }
                l++;
                b = a;
                lx = (int)t;
                ly = y0;
                lz = (int)t2;
            }
        } else
        {
            float m = (float)dx / (float)dz;
            float m2 = (float)dy / (float)dz;
            t += x0;
            t2 += y0;
            dz = dz >= 0 ? 1 : -1;
            m *= dz;
            m2 *= dz;
            while(z0 != z1) 
            {
                z0 += dz;
                t += m;
                t2 += m2;
                IsoGridSquare a = cell.getGridSquare((int)t, (int)t2, z0);
                if(a != null && b != null)
                {
                    boolean bTest = a.testCollideAdjacent(chr, b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ());
                    if(bTest)
                        return l;
                }
                l++;
                b = a;
                lx = (int)t;
                ly = (int)t2;
                lz = z0;
            }
        }
        return l;
    }

    public static int lineClearCollideCount(IsoMovingObject chr, IsoCell cell, int x1, int y1, int z1, int x0, int y0, int z0, 
            int returnMin)
    {
        int l = 0;
        int dy = y1 - y0;
        int dx = x1 - x0;
        int dz = z1 - z0;
        float t = 0.5F;
        float t2 = 0.5F;
        int lx = x0;
        int ly = y0;
        int lz = z0;
        IsoGridSquare b = cell.getGridSquare(lx, ly, lz);
        if(Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > Math.abs(dz))
        {
            float m = (float)dy / (float)dx;
            float m2 = (float)dz / (float)dx;
            t += y0;
            t2 += z0;
            dx = dx >= 0 ? 1 : -1;
            m *= dx;
            m2 *= dx;
            while(x0 != x1) 
            {
                x0 += dx;
                t += m;
                t2 += m2;
                IsoGridSquare a = cell.getGridSquare(x0, (int)t, (int)t2);
                if(a != null && b != null)
                {
                    boolean bTest = a.testCollideAdjacent(chr, b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ());
                    if(bTest)
                        return l;
                }
                if(++l >= returnMin)
                    return l;
                b = a;
                lx = x0;
                ly = (int)t;
                lz = (int)t2;
            }
        } else
        if(Math.abs(dy) >= Math.abs(dx) && Math.abs(dy) > Math.abs(dz))
        {
            float m = (float)dx / (float)dy;
            float m2 = (float)dz / (float)dy;
            t += x0;
            t2 += z0;
            dy = dy >= 0 ? 1 : -1;
            m *= dy;
            m2 *= dy;
            while(y0 != y1) 
            {
                y0 += dy;
                t += m;
                t2 += m2;
                IsoGridSquare a = cell.getGridSquare((int)t, y0, (int)t2);
                if(a != null && b != null)
                {
                    boolean bTest = a.testCollideAdjacent(chr, b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ());
                    if(bTest)
                        return l;
                }
                if(++l >= returnMin)
                    return l;
                b = a;
                lx = (int)t;
                ly = y0;
                lz = (int)t2;
            }
        } else
        {
            float m = (float)dx / (float)dz;
            float m2 = (float)dy / (float)dz;
            t += x0;
            t2 += y0;
            dz = dz >= 0 ? 1 : -1;
            m *= dz;
            m2 *= dz;
            while(z0 != z1) 
            {
                z0 += dz;
                t += m;
                t2 += m2;
                IsoGridSquare a = cell.getGridSquare((int)t, (int)t2, z0);
                if(a != null && b != null)
                {
                    boolean bTest = a.testCollideAdjacent(chr, b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ());
                    if(bTest)
                        return l;
                }
                if(++l >= returnMin)
                    return l;
                b = a;
                lx = (int)t;
                ly = (int)t2;
                lz = z0;
            }
        }
        return l;
    }

    public static boolean lineClearCollide(IsoMovingObject chr, IsoCell cell, int x1, int y1, int z1, int x0, int y0, int z0)
    {
        int dy = y1 - y0;
        int dx = x1 - x0;
        int dz = z1 - z0;
        float t = 0.5F;
        float t2 = 0.5F;
        int lx = x0;
        int ly = y0;
        int lz = z0;
        IsoGridSquare b = cell.getGridSquare(lx, ly, lz);
        if(Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > Math.abs(dz))
        {
            float m = (float)dy / (float)dx;
            float m2 = (float)dz / (float)dx;
            t += y0;
            t2 += z0;
            dx = dx >= 0 ? 1 : -1;
            m *= dx;
            m2 *= dx;
            while(x0 != x1) 
            {
                x0 += dx;
                t += m;
                t2 += m2;
                IsoGridSquare a = cell.getGridSquare(x0, (int)t, (int)t2);
                if(a != null && b != null)
                {
                    boolean bTest = a.testCollideAdjacent(chr, b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ());
                    if(bTest)
                        return true;
                }
                b = a;
                lx = x0;
                ly = (int)t;
                lz = (int)t2;
            }
        } else
        if(Math.abs(dy) >= Math.abs(dx) && Math.abs(dy) > Math.abs(dz))
        {
            float m = (float)dx / (float)dy;
            float m2 = (float)dz / (float)dy;
            t += x0;
            t2 += z0;
            dy = dy >= 0 ? 1 : -1;
            m *= dy;
            m2 *= dy;
            while(y0 != y1) 
            {
                y0 += dy;
                t += m;
                t2 += m2;
                IsoGridSquare a = cell.getGridSquare((int)t, y0, (int)t2);
                if(a != null && b != null)
                {
                    boolean bTest = a.testCollideAdjacent(chr, b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ());
                    if(bTest)
                        return true;
                }
                b = a;
                lx = (int)t;
                ly = y0;
                lz = (int)t2;
            }
        } else
        {
            float m = (float)dx / (float)dz;
            float m2 = (float)dy / (float)dz;
            t += x0;
            t2 += y0;
            dz = dz >= 0 ? 1 : -1;
            m *= dz;
            m2 *= dz;
            while(z0 != z1) 
            {
                z0 += dz;
                t += m;
                t2 += m2;
                IsoGridSquare a = cell.getGridSquare((int)t, (int)t2, z0);
                if(a != null && b != null)
                {
                    boolean bTest = a.testCollideAdjacent(chr, b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ());
                    if(bTest)
                        return true;
                }
                b = a;
                lx = (int)t;
                ly = (int)t2;
                lz = z0;
            }
        }
        return false;
    }

    public static TestResults lineClearCached(IsoCell cell, int x1, int y1, int z1, int x0, int y0, int z0, boolean bIgnoreDoors)
    {
        int sx = x0;
        int sy = y0;
        int sz = z0;
        int dy = y1 - y0;
        int dx = x1 - x0;
        int dz = z1 - z0;
        int cx = dx;
        int cy = dy;
        int cz = dz;
        cx += 100;
        cy += 100;
        cz += 16;
        TestResults res = TestResults.Clear;
        int resultToPropagate = 1;
        if(cachedresults[cx][cy][cz] != 0)
        {
            if(cachedresults[cx][cy][cz] == 1)
                res = TestResults.Clear;
            if(cachedresults[cx][cy][cz] == 2)
                res = TestResults.ClearThroughOpenDoor;
            if(cachedresults[cx][cy][cz] == 3)
                res = TestResults.ClearThroughWindow;
            if(cachedresults[cx][cy][cz] == 4)
                res = TestResults.Blocked;
            return res;
        }
        float t = 0.5F;
        float t2 = 0.5F;
        int lx = x0;
        int ly = y0;
        int lz = z0;
        IsoGridSquare b = cell.getGridSquare(lx, ly, lz);
        if(Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > Math.abs(dz))
        {
            float m = (float)dy / (float)dx;
            float m2 = (float)dz / (float)dx;
            t += y0;
            t2 += z0;
            dx = dx >= 0 ? 1 : -1;
            m *= dx;
            m2 *= dx;
            while(x0 != x1) 
            {
                x0 += dx;
                t += m;
                t2 += m2;
                IsoGridSquare a = cell.getGridSquare(x0, (int)t, (int)t2);
                if(a != null && b != null)
                {
                    if(a.testVisionAdjacent(b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ(), true, bIgnoreDoors) == TestResults.Blocked)
                        resultToPropagate = 4;
                    int cx2 = x0 - sx;
                    int cy2 = (int)t - sy;
                    int cz2 = (int)t2 - sz;
                    cx2 += 100;
                    cy2 += 100;
                    cz2 += 16;
                    cachedresults[cx2][cy2][cz2] = (byte)resultToPropagate;
                } else
                {
                    int cx2 = x0 - sx;
                    int cy2 = (int)t - sy;
                    int cz2 = (int)t2 - sz;
                    cx2 += 100;
                    cy2 += 100;
                    cz2 += 16;
                    cachedresults[cx2][cy2][cz2] = (byte)resultToPropagate;
                }
                b = a;
                lx = x0;
                ly = (int)t;
                lz = (int)t2;
            }
        } else
        if(Math.abs(dy) >= Math.abs(dx) && Math.abs(dy) > Math.abs(dz))
        {
            float m = (float)dx / (float)dy;
            float m2 = (float)dz / (float)dy;
            t += x0;
            t2 += z0;
            dy = dy >= 0 ? 1 : -1;
            m *= dy;
            m2 *= dy;
            while(y0 != y1) 
            {
                y0 += dy;
                t += m;
                t2 += m2;
                IsoGridSquare a = cell.getGridSquare((int)t, y0, (int)t2);
                if(a != null && b != null)
                {
                    if(a.testVisionAdjacent(b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ(), true, bIgnoreDoors) == TestResults.Blocked)
                        resultToPropagate = 4;
                    int cx2 = (int)t - sx;
                    int cy2 = y0 - sy;
                    int cz2 = (int)t2 - sz;
                    cx2 += 100;
                    cy2 += 100;
                    cz2 += 16;
                    cachedresults[cx2][cy2][cz2] = (byte)resultToPropagate;
                } else
                {
                    int cx2 = (int)t - sx;
                    int cy2 = y0 - sy;
                    int cz2 = (int)t2 - sz;
                    cx2 += 100;
                    cy2 += 100;
                    cz2 += 16;
                    cachedresults[cx2][cy2][cz2] = (byte)resultToPropagate;
                }
                b = a;
                lx = (int)t;
                ly = y0;
                lz = (int)t2;
            }
        } else
        {
            float m = (float)dx / (float)dz;
            float m2 = (float)dy / (float)dz;
            t += x0;
            t2 += y0;
            dz = dz >= 0 ? 1 : -1;
            m *= dz;
            m2 *= dz;
            while(z0 != z1) 
            {
                z0 += dz;
                t += m;
                t2 += m2;
                IsoGridSquare a = cell.getGridSquare((int)t, (int)t2, z0);
                if(a != null && b != null)
                {
                    if(a.testVisionAdjacent(b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ(), true, bIgnoreDoors) == TestResults.Blocked)
                        resultToPropagate = 4;
                    int cx2 = (int)t - sx;
                    int cy2 = (int)t2 - sy;
                    int cz2 = z0 - sz;
                    cx2 += 100;
                    cy2 += 100;
                    cz2 += 16;
                    cachedresults[cx2][cy2][cz2] = (byte)resultToPropagate;
                } else
                {
                    int cx2 = (int)t - sx;
                    int cy2 = (int)t2 - sy;
                    int cz2 = z0 - sz;
                    cx2 += 100;
                    cy2 += 100;
                    cz2 += 16;
                    cachedresults[cx2][cy2][cz2] = (byte)resultToPropagate;
                }
                b = a;
                lx = (int)t;
                ly = (int)t2;
                lz = z0;
            }
        }
        if(resultToPropagate == 1)
        {
            cachedresults[cx][cy][cz] = (byte)resultToPropagate;
            return TestResults.Clear;
        }
        if(resultToPropagate == 2)
        {
            cachedresults[cx][cy][cz] = (byte)resultToPropagate;
            return TestResults.ClearThroughOpenDoor;
        }
        if(resultToPropagate == 3)
        {
            cachedresults[cx][cy][cz] = (byte)resultToPropagate;
            return TestResults.ClearThroughWindow;
        }
        if(resultToPropagate == 4)
        {
            cachedresults[cx][cy][cz] = (byte)resultToPropagate;
            return TestResults.Blocked;
        } else
        {
            return TestResults.Blocked;
        }
    }

    public static byte cachedresults[][][] = new byte[200][200][32];
    public static boolean cachecleared = true;

}
