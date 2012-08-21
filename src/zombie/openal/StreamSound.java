// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StreamSound.java

package zombie.openal;

import java.io.IOException;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

// Referenced classes of package zombie.openal:
//            AudioImpl, SoundStore, OpenALStreamPlayer

public class StreamSound extends AudioImpl
{

    public StreamSound(OpenALStreamPlayer player)
    {
        this.player = player;
    }

    public float getPosition()
    {
        return player.getPosition();
    }

    public boolean isPlaying()
    {
        return SoundStore.get().isPlaying(player);
    }

    public int playAsMusic(float pitch, float gain, boolean loop)
    {
        try
        {
            cleanUpSource();
            player.setup(pitch);
            player.play(loop);
            SoundStore.get().setStream(player);
        }
        catch(IOException e) { }
        return SoundStore.get().getSource(0);
    }

    public int playAsSoundEffect(float pitch, float gain, boolean loop)
    {
        return playAsMusic(pitch, gain, loop);
    }

    public int playAsSoundEffect(float pitch, float gain, boolean loop, float x, float y, float z)
    {
        return playAsMusic(pitch, gain, loop);
    }

    public boolean setPosition(float position)
    {
        return player.setPosition(position);
    }

    public void stop()
    {
        SoundStore.get().setStream(null);
    }

    private void cleanUpSource()
    {
        SoundStore store = SoundStore.get();
        AL10.alSourceStop(store.getSource(0));
        IntBuffer buffer = BufferUtils.createIntBuffer(1);
        for(int queued = AL10.alGetSourcei(store.getSource(0), 4117); queued > 0; queued--)
            AL10.alSourceUnqueueBuffers(store.getSource(0), buffer);

        AL10.alSourcei(store.getSource(0), 4105, 0);
    }

    private OpenALStreamPlayer player;
}
