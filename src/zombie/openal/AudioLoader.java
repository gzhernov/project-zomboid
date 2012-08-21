// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AudioLoader.java

package zombie.openal;

import java.io.IOException;
import java.net.URL;
import zombie.FrameLoader;

// Referenced classes of package zombie.openal:
//            SoundStore, Audio

public class AudioLoader
{

    public AudioLoader()
    {
    }

    public static Audio getAudio(String format, String ref)
        throws IOException
    {
        init();
        if(format.equals("AIF"))
            return SoundStore.get().getAIF(ref);
        if(format.equals("WAV"))
            return SoundStore.get().getWAV(ref);
        if(format.equals("OGG"))
            return SoundStore.get().getOgg(ref);
        else
            throw new IOException((new StringBuilder()).append("Unsupported format for non-streaming Audio: ").append(format).toString());
    }

    public static Audio getAudioFile(String format, String ref)
        throws IOException
    {
        init();
        if(format.equals("AIF"))
            return SoundStore.get().getAIF(ref);
        if(format.equals("WAV"))
            return SoundStore.get().getWAV(ref);
        if(format.equals("OGG"))
            return SoundStore.get().getOggFile(ref);
        else
            throw new IOException((new StringBuilder()).append("Unsupported format for non-streaming Audio: ").append(format).toString());
    }

    public static Audio getStreamingAudio(String format, URL url)
        throws IOException
    {
        init();
        if(format.equals("OGG"))
            return SoundStore.get().getOggStream(url);
        if(format.equals("MOD"))
            return SoundStore.get().getMOD(url.openStream());
        if(format.equals("XM"))
            return SoundStore.get().getMOD(url.openStream());
        else
            throw new IOException((new StringBuilder()).append("Unsupported format for streaming Audio: ").append(format).toString());
    }

    public static Audio getStreamingAudio(String format, String url)
        throws IOException
    {
        init();
        if(format.equals("OGG"))
            return SoundStore.get().getOggStream(url);
        if(format.equals("MOD"))
            return SoundStore.get().getMOD(url);
        if(format.equals("XM"))
            return SoundStore.get().getMOD(url);
        else
            throw new IOException((new StringBuilder()).append("Unsupported format for streaming Audio: ").append(format).toString());
    }

    public static void update()
    {
        init();
    }

    private static void init()
    {
        if(FrameLoader.bServer)
            return;
        if(!inited)
        {
            SoundStore.get().init();
            inited = true;
        }
    }

    private static final String AIF = "AIF";
    private static final String MOD = "MOD";
    private static final String OGG = "OGG";
    private static final String WAV = "WAV";
    private static final String XM = "XM";
    private static boolean inited = false;

}
