// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DeferredSound.java

package zombie.openal;

import java.io.IOException;
import java.io.InputStream;
import zombie.loading.DeferredResource;
import zombie.loading.LoadingList;

// Referenced classes of package zombie.openal:
//            AudioImpl, Audio

public class DeferredSound extends AudioImpl
    implements DeferredResource
{

    public DeferredSound(String ref, InputStream in, int type)
    {
        this.ref = ref;
        this.type = type;
        if(ref.equals(in.toString()))
            this.in = in;
        LoadingList.get().add(this);
    }

    public String getDescription()
    {
        return ref;
    }

    public boolean isPlaying()
    {
        checkTarget();
        return target.isPlaying();
    }

    public void load()
        throws IOException
    {
    }

    public int playAsMusic(float pitch, float gain, boolean loop)
    {
        checkTarget();
        return target.playAsMusic(pitch, gain, loop);
    }

    public int playAsSoundEffect(float pitch, float gain, boolean loop)
    {
        checkTarget();
        return target.playAsSoundEffect(pitch, gain, loop);
    }

    public int playAsSoundEffect(float pitch, float gain, boolean loop, float x, float y, float z)
    {
        checkTarget();
        return target.playAsSoundEffect(pitch, gain, loop, x, y, z);
    }

    public void stop()
    {
        checkTarget();
        target.stop();
    }

    private void checkTarget()
    {
        if(target == null)
            throw new RuntimeException("Attempt to use deferred sound before loading");
        else
            return;
    }

    public static final int AIF = 4;
    public static final int MOD = 3;
    public static final int OGG = 1;
    public static final int WAV = 2;
    private InputStream in;
    private String ref;
    private Audio target;
    private int type;
}
