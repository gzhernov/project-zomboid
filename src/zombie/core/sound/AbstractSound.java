// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AbstractSound.java

package zombie.core.sound;


// Referenced classes of package zombie.core.sound:
//            Sound, Attenuator

abstract class AbstractSound
    implements Sound
{

    public AbstractSound()
    {
        gain = 1.0F;
        pitch = 1.0F;
    }

    public AbstractSound(String url, int priority, boolean looped)
    {
        this(url, 1.0F, 1.0F, priority, looped, null);
    }

    public AbstractSound(String url, float gain, float pitch, int priority, boolean looped)
    {
        this(url, gain, pitch, priority, looped, null);
    }

    public AbstractSound(String url, float gain, float pitch, int priority, boolean looped, Attenuator attenuator)
    {
        this.gain = 1.0F;
        this.pitch = 1.0F;
        setURL(url);
        setGain(gain);
        setPitch(pitch);
        setPriority(priority);
        setLooped(looped);
        setAttenuator(attenuator);
    }

    public final void setGain(float gain)
    {
        if(gain < 0.0F || gain > 1.0F)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("gain must be between 0.0f and 1.0f inclusive; got ").append(gain).toString());
        } else
        {
            this.gain = gain;
            return;
        }
    }

    public final void setPitch(float pitch)
    {
        if(pitch <= 0.0F)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("pitch must be > 0.0f; got ").append(pitch).toString());
        } else
        {
            this.pitch = pitch;
            return;
        }
    }

    public final void setPriority(int priority)
    {
        if(priority < 0)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("priority must be >= 0; got ").append(priority).toString());
        } else
        {
            this.priority = priority;
            return;
        }
    }

    public final void setLooped(boolean looped)
    {
        this.looped = looped;
    }

    public final void setURL(String url)
    {
        this.url = url;
    }

    public final float getGain()
    {
        return gain;
    }

    public final float getPitch()
    {
        return pitch;
    }

    public final int getPriority()
    {
        return priority;
    }

    public final boolean isLooped()
    {
        return looped;
    }

    public final String getURL()
    {
        return url;
    }

    public Attenuator getAttenuator()
    {
        return attenuator;
    }

    public void setAttenuator(Attenuator attenuator)
    {
        this.attenuator = attenuator;
    }

    protected String url;
    private float gain;
    private float pitch;
    private int priority;
    private boolean looped;
    private Attenuator attenuator;
}
