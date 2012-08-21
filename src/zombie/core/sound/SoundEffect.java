// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SoundEffect.java

package zombie.core.sound;


// Referenced classes of package zombie.core.sound:
//            Attenuator

public interface SoundEffect
{

    public abstract void setPosition(float f, float f1, float f2);

    public abstract void setGain(float f);

    public abstract void setPitch(float f);

    public abstract void play();

    public abstract void pause();

    public abstract void stop();

    public abstract void rewind();

    public abstract void setLooped(boolean flag);

    public abstract void setAttenuator(Attenuator attenuator);

    public abstract void setFade(int i, float f, boolean flag);

    public abstract boolean isActive();
}
