// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NullAudio.java

package zombie.openal;


// Referenced classes of package zombie.openal:
//            Audio

public class NullAudio
    implements Audio
{

    public NullAudio()
    {
    }

    public int getBufferID()
    {
        return 0;
    }

    public float getPosition()
    {
        return 0.0F;
    }

    public boolean isPlaying()
    {
        return false;
    }

    public int playAsMusic(float pitch, float gain, boolean loop)
    {
        return 0;
    }

    public int playAsSoundEffect(float pitch, float gain, boolean loop)
    {
        return 0;
    }

    public int playAsSoundEffect(float pitch, float gain, boolean loop, float f, float f1, float f2)
    {
        return 0;
    }

    public void setID(int i)
    {
    }

    public boolean setPosition(float position)
    {
        return false;
    }

    public void setVolume(float f)
    {
    }

    public void stop()
    {
    }

    public int playAsSoundEffectUnbuffered(float pitch, float gain, boolean loop)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
