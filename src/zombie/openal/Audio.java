// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Audio.java

package zombie.openal;


public interface Audio
{

    public abstract int getBufferID();

    public abstract float getPosition();

    public abstract boolean isPlaying();

    public abstract int playAsMusic(float f, float f1, boolean flag);

    public abstract int playAsSoundEffect(float f, float f1, boolean flag);

    public abstract int playAsSoundEffect(float f, float f1, boolean flag, float f2, float f3, float f4);

    public abstract void setID(int i);

    public abstract boolean setPosition(float f);

    public abstract void setVolume(float f);

    public abstract void stop();

    public abstract int playAsSoundEffectUnbuffered(float f, float f1, boolean flag);
}
