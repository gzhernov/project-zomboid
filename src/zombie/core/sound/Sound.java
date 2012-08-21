// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Sound.java

package zombie.core.sound;


// Referenced classes of package zombie.core.sound:
//            Attenuator

public interface Sound
{

    public abstract void setGain(float f);

    public abstract void setPitch(float f);

    public abstract void setPriority(int i);

    public abstract void setLooped(boolean flag);

    public abstract void setURL(String s);

    public abstract float getGain();

    public abstract float getPitch();

    public abstract int getPriority();

    public abstract boolean isLooped();

    public abstract Attenuator getAttenuator();

    public abstract void setAttenuator(Attenuator attenuator);

    public abstract String getURL();

    public abstract int getFormat();

    public abstract boolean isStereo();
}
