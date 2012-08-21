// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DefAttenuation.java

package zombie.core.sound;


// Referenced classes of package zombie.core.sound:
//            Attenuator

public class DefAttenuation
    implements Attenuator
{

    public DefAttenuation()
    {
    }

    public float getAttenuation(float x, float y)
    {
        return 1.0F;
    }

    public float getXPosition(float x, float y, float z)
    {
        return x;
    }

    public float getYPosition(float x, float y, float z)
    {
        return y;
    }

    public float getZPosition(float x, float y, float z)
    {
        return z;
    }

    public static final Attenuator INSTANCE = new DefAttenuation();

}
