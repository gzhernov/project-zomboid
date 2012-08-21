// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MODSound.java

package zombie.openal;

import java.io.IOException;
import java.io.InputStream;

// Referenced classes of package zombie.openal:
//            AudioImpl, SoundStore

public class MODSound extends AudioImpl
{

    public MODSound(SoundStore store, InputStream in)
        throws IOException
    {
        this.store = store;
    }

    public float getPosition()
    {
        throw new RuntimeException("Positioning on modules is not currently supported");
    }

    public int playAsMusic(float pitch, float gain, boolean loop)
    {
        return store.getSource(0);
    }

    public int playAsSoundEffect(float pitch, float gain, boolean loop)
    {
        return -1;
    }

    public void poll()
    {
    }

    public boolean setPosition(float position)
    {
        throw new RuntimeException("Positioning on modules is not currently supported");
    }

    public void stop()
    {
    }

    private void cleanUpSource()
    {
    }

    private SoundStore store;
}
