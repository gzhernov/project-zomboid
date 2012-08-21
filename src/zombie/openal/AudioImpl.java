// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AudioImpl.java

package zombie.openal;

import org.lwjgl.openal.AL10;

// Referenced classes of package zombie.openal:
//            Audio, SoundStore

public class AudioImpl
    implements Audio
{

    protected AudioImpl()
    {
        id = 1;
        index = -1;
    }

    AudioImpl(SoundStore store, int buffer)
    {
        id = 1;
        index = -1;
        this.store = store;
        this.buffer = buffer;
        int bytes = AL10.alGetBufferi(buffer, 8196);
        int bits = AL10.alGetBufferi(buffer, 8194);
        if(bits == 0)
        {
            return;
        } else
        {
            int channels = AL10.alGetBufferi(buffer, 8195);
            int freq = AL10.alGetBufferi(buffer, 8193);
            int samples = bytes / (bits / 8);
            length = (float)samples / (float)freq / (float)channels;
            return;
        }
    }

    public AudioImpl initAudioImpl(SoundStore store, int buffer)
    {
        this.store = store;
        this.buffer = buffer;
        int bytes = AL10.alGetBufferi(buffer, 8196);
        int bits = AL10.alGetBufferi(buffer, 8194);
        if(bits == 0)
        {
            return this;
        } else
        {
            int channels = AL10.alGetBufferi(buffer, 8195);
            int freq = AL10.alGetBufferi(buffer, 8193);
            int samples = bytes / (bits / 8);
            length = (float)samples / (float)freq / (float)channels;
            return this;
        }
    }

    public static void pauseMusic()
    {
        SoundStore.get().pauseLoop();
    }

    public static void restartMusic()
    {
        SoundStore.get().restartLoop();
    }

    public int getBufferID()
    {
        return buffer;
    }

    public float getPosition()
    {
        return AL10.alGetSourcef(store.getSource(index), 4132);
    }

    public boolean isPlaying()
    {
        if(index != -1)
            return store.isPlaying(index);
        else
            return false;
    }

    public int playAsMusic(float pitch, float gain, boolean loop)
    {
        store.playAsMusic(buffer, pitch, gain * volDelta, loop);
        index = 0;
        return store.getSource(0);
    }

    public int playAsSoundEffect(float pitch, float gain, boolean loop)
    {
        index = store.playAsSound(buffer, pitch, gain * volDelta, loop);
        return store.getSource(index);
    }

    public int playAsSoundEffect(float pitch, float gain, boolean loop, float x, float y, float z)
    {
        index = store.playAsSoundAt(buffer, pitch, gain * volDelta, loop, x, y, z);
        return store.getSource(index);
    }

    public void setID(int id)
    {
        this.id = id;
    }

    public boolean setPosition(float position)
    {
        position %= length;
        AL10.alSourcef(store.getSource(index), 4132, position);
        return AL10.alGetError() == 0;
    }

    public void setVolume(float volume)
    {
        if(store == null)
        {
            return;
        } else
        {
            store.setMusicGain(volume * volDelta, id);
            return;
        }
    }

    public void stop()
    {
        if(index != -1)
            store.stopSource(index);
    }

    public int playAsSoundEffectUnbuffered(float pitch, float gain, boolean loop)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    int id;
    private int buffer;
    private int index;
    private float length;
    private SoundStore store;
    public static float volDelta = 0.4F;

}
