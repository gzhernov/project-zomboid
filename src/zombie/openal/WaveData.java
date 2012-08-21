// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WaveData.java

package zombie.openal;

import java.io.*;
import java.nio.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;
import org.lwjgl.LWJGLUtil;
/*     */ import javax.sound.sampled.AudioInputStream;

public class WaveData
{

    private WaveData(ByteBuffer data, int format, int samplerate)
    {
        this.data = data;
        this.format = format;
        this.samplerate = samplerate;
    }

    public static WaveData create(InputStream path)
    {
        try
        {
            return create(AudioSystem.getAudioInputStream(new BufferedInputStream(path)));
        }
        catch(Exception e)
        {
            LWJGLUtil.log((new StringBuilder()).append("Unable to create from: ").append(path).toString());
            e.printStackTrace();
            return null;
        }
    }

    public static WaveData create(String path)
    {
        try
        {
            return create(((InputStream) (new FileInputStream(path))));
        }
        catch(FileNotFoundException ex)
        {
            Logger.getLogger(WaveData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static WaveData create(byte buffer[])
    {
        try
        {
            return create(AudioSystem.getAudioInputStream(new BufferedInputStream(new ByteArrayInputStream(buffer))));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static WaveData create(ByteBuffer buffer)
    {
        try
        {
            byte bytes[] = null;
            if(buffer.hasArray())
            {
                bytes = buffer.array();
            } else
            {
                bytes = new byte[buffer.capacity()];
                buffer.get(bytes);
            }
            return create(bytes);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static WaveData create(AudioInputStream ais)
    {
        AudioFormat audioformat = ais.getFormat();
        int channels = 0;
        if(audioformat.getChannels() == 1)
        {
            if(audioformat.getSampleSizeInBits() == 8)
                channels = 4352;
            else
            if(audioformat.getSampleSizeInBits() == 16)
                channels = 4353;
            else
                throw new RuntimeException("Illegal sample size");
        } else
        if(audioformat.getChannels() == 2)
        {
            if(audioformat.getSampleSizeInBits() == 8)
                channels = 4354;
            else
            if(audioformat.getSampleSizeInBits() == 16)
                channels = 4355;
            else
                throw new RuntimeException("Illegal sample size");
        } else
        {
            throw new RuntimeException("Only mono or stereo is supported");
        }
        byte buf[] = new byte[(audioformat.getChannels() * (int)ais.getFrameLength() * audioformat.getSampleSizeInBits()) / 8];
        int read = 0;
        int total = 0;
        try
        {
            while((read = ais.read(buf, total, buf.length - total)) != -1 && total < buf.length) 
                total += read;
        }
        catch(IOException ioe)
        {
            return null;
        }
        ByteBuffer buffer = convertAudioBytes(buf, audioformat.getSampleSizeInBits() == 16);
        WaveData wavedata = new WaveData(buffer, channels, (int)audioformat.getSampleRate());
        try
        {
            ais.close();
        }
        catch(IOException ioe) { }
        return wavedata;
    }

    private static ByteBuffer convertAudioBytes(byte audio_bytes[], boolean two_bytes_data)
    {
        ByteBuffer dest = ByteBuffer.allocateDirect(audio_bytes.length);
        dest.order(ByteOrder.nativeOrder());
        ByteBuffer src = ByteBuffer.wrap(audio_bytes);
        src.order(ByteOrder.LITTLE_ENDIAN);
        if(two_bytes_data)
        {
            ShortBuffer dest_short = dest.asShortBuffer();
            for(ShortBuffer src_short = src.asShortBuffer(); src_short.hasRemaining(); dest_short.put(src_short.get()));
        } else
        {
            for(; src.hasRemaining(); dest.put(src.get()));
        }
        dest.rewind();
        return dest;
    }

    public void dispose()
    {
        data.clear();
    }

    public final ByteBuffer data;
    public final int format;
    public final int samplerate;
}
