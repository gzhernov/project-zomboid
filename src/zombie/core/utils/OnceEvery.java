// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   OnceEvery.java

package zombie.core.utils;

import java.util.Stack;
import zombie.GameTime;
import zombie.core.Rand;

public class OnceEvery
{

    public OnceEvery(float seconds, boolean bRandomStart)
    {
        FPS = 60;
        millioff = 0;
        millimax = 0;
        millimax = (int)((float)FPS * seconds);
        if(bRandomStart)
            millioff = Rand.Next(millimax);
    }

    public OnceEvery(float seconds)
    {
        FPS = 60;
        millioff = 0;
        millimax = 0;
        millimax = (int)((float)FPS * seconds);
    }

    public void SetFrequency(float seconds)
    {
        millimax = (int)((float)FPS * seconds);
    }

    public boolean Check()
    {
        if(millimax == 0)
            return true;
        long a = ((long)last - (long)millioff) % (long)millimax;
        long b = ((long)comp - (long)millioff) % (long)millimax;
        return a > b || (float)millimax < comp - last;
    }

    public static void update()
    {
        last = comp;
        comp += 1.0F * GameTime.instance.getMultiplier();
    }

    int FPS;
    int millioff;
    int millimax;
    static float comp = 0.0F;
    static float last = 0.0F;
    static Stack list = new Stack();

}
