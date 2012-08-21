// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FastTrig.java

package zombie.core;


public class FastTrig
{

    public FastTrig()
    {
    }

    public static double cos(double radians)
    {
        return sin(radians + 1.5707963267948966D);
    }

    public static double sin(double radians)
    {
        radians = reduceSinAngle(radians);
        if(Math.abs(radians) <= 0.78539816339744828D)
            return Math.sin(radians);
        else
            return Math.cos(1.5707963267948966D - radians);
    }

    private static double reduceSinAngle(double radians)
    {
        double orig = radians;
        radians %= 6.2831853071795862D;
        if(Math.abs(radians) > 3.1415926535897931D)
            radians -= 6.2831853071795862D;
        if(Math.abs(radians) > 1.5707963267948966D)
            radians = 3.1415926535897931D - radians;
        return radians;
    }
}
