// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoLightSource.java

package zombie.iso;

import zombie.core.Collections.NulledArrayList;

// Referenced classes of package zombie.iso:
//            IsoGridSquare, LosUtil, IsoUtils

public class IsoLightSource
{

    public IsoLightSource(int x, int y, int z, float r, float g, float b, int radius)
    {
        affects = new NulledArrayList(0);
        switches = new NulledArrayList(0);
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;
        this.g = g;
        this.b = b;
        this.radius = radius;
        bActive = true;
    }

    public void update()
    {
        for(int n = 0; n < affects.size(); n++)
        {
            IsoGridSquare sq = (IsoGridSquare)affects.get(n);
            LosUtil.TestResults test = LosUtil.lineClear(sq.getCell(), x, y, z, sq.getX(), sq.getY(), sq.getZ(), false);
            if((sq.getX() != x || sq.getY() != y || sq.getZ() != z) && test == LosUtil.TestResults.Blocked)
                continue;
            float del = 0.0F;
            float dist = IsoUtils.DistanceTo(x, y, z, sq.getX(), sq.getY(), sq.getZ());
            if(Math.abs(sq.getZ() - z) <= 1)
                dist = IsoUtils.DistanceTo(x, y, 0.0F, sq.getX(), sq.getY(), 0.0F);
            if(dist > (float)radius)
                continue;
            del = dist / (float)radius;
            del = 1.0F - del;
            float totR = del * r;
            float totG = del * g;
            float totB = del * b;
            if(bActive)
            {
                sq.setLampostTotalR(sq.getLampostTotalR() + totR);
                sq.setLampostTotalG(sq.getLampostTotalG() + totG);
                sq.setLampostTotalB(sq.getLampostTotalB() + totB);
            } else
            {
                sq.setLampostTotalR(sq.getLampostTotalR() - totR);
                sq.setLampostTotalG(sq.getLampostTotalG() - totG);
                sq.setLampostTotalB(sq.getLampostTotalB() - totB);
            }
        }

    }

    public void resetTiles()
    {
        for(int n = 0; n < affects.size(); n++)
        {
            IsoGridSquare sq = (IsoGridSquare)affects.get(n);
        }

    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getZ()
    {
        return z;
    }

    public void setZ(int z)
    {
        this.z = z;
    }

    public float getR()
    {
        return r;
    }

    public void setR(float r)
    {
        this.r = r;
    }

    public float getG()
    {
        return g;
    }

    public void setG(float g)
    {
        this.g = g;
    }

    public float getB()
    {
        return b;
    }

    public void setB(float b)
    {
        this.b = b;
    }

    public int getRadius()
    {
        return radius;
    }

    public void setRadius(int radius)
    {
        this.radius = radius;
    }

    public boolean isActive()
    {
        return bActive;
    }

    public void setActive(boolean bActive)
    {
        this.bActive = bActive;
    }

    public boolean wasActive()
    {
        return bWasActive;
    }

    public void setWasActive(boolean bWasActive)
    {
        this.bWasActive = bWasActive;
    }

    public NulledArrayList getAffects()
    {
        return affects;
    }

    public void setAffects(NulledArrayList affects)
    {
        this.affects = affects;
    }

    public NulledArrayList getSwitches()
    {
        return switches;
    }

    public void setSwitches(NulledArrayList switches)
    {
        this.switches = switches;
    }

    public int x;
    public int y;
    public int z;
    public float r;
    public float g;
    public float b;
    public int radius;
    public boolean bActive;
    public boolean bWasActive;
    public NulledArrayList affects;
    public NulledArrayList switches;
}
