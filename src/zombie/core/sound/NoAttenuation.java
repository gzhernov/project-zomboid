// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NoAttenuation.java

package zombie.core.sound;


// Referenced classes of package zombie.core.sound:
//            Attenuator

public class NoAttenuation
    implements Attenuator
{

    public NoAttenuation()
    {
    }

    public float getAttenuation(float x, float y)
    {
        return 1.0F;
    }

    public float getXPosition(float x, float y, float z)
    {
        return 0.0F;
    }

    public float getYPosition(float x, float y, float z)
    {
        return 0.0F;
    }

    public float getZPosition(float x, float y, float z)
    {
        return 0.0F;
    }

    public static final Attenuator INSTANCE = new NoAttenuation();

}
