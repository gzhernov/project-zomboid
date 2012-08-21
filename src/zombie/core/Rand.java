// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Rand.java

package zombie.core;

import java.util.Random;

public class Rand
{

    public Rand()
    {
    }

    public static void init()
    {
        generator = new Random(System.currentTimeMillis());
        for(int n = 0; n < 10000; n++)
            randGen[n] = Math.abs(generator.nextInt());

    }

    public static int Next(int max)
    {
        if(max == 0)
            return 0;
        id++;
        if(id >= 10000)
            id = 0;
        return randGen[id] % max;
    }

    public static int Next(int min, int max)
    {
        if(max == min)
            return min;
        if(min > max)
        {
            int temp = min;
            min = max;
            max = temp;
        }
        id++;
        if(id >= 10000)
            id = 0;
        int n = randGen[id] % (max - min);
        return n + min;
    }

    public static float Next(float min, float max)
    {
        if(max == min)
            return min;
        if(min > max)
        {
            float temp = min;
            min = max;
            max = temp;
        }
        id++;
        if(id >= 10000)
            id = 0;
        int n = randGen[id] % 0x5f5e100;
        return min + (max - min) * ((float)n / 1E+008F);
    }

    static Random generator = new Random(0x12ac60bL);
    static int randGen[] = new int[10000];
    public static int id = 0;

}
