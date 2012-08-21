// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AiffData.java

package zombie.openal;

import java.io.*;
import java.net.URL;
import java.nio.*;
import javax.sound.sampled.*;
import org.lwjgl.LWJGLUtil;
import javax.sound.sampled.AudioInputStream;

public class AiffData
{

    private AiffData(ByteBuffer data, int format, int samplerate)
    {
        this.data = data;
        this.format = format;
        this.samplerate = samplerate;
    }

    public static AiffData create(URL path)
    {
        try
        {
            return create(AudioSystem.getAudioInputStream(new BufferedInputStream(path.openStream())));
        }
        catch(Exception e)
        {
            LWJGLUtil.log((new StringBuilder()).append("Unable to create from: ").append(path).toString());
            e.printStackTrace();
            return null;
        }
    }

    public static AiffData create(String path)
    {
        return create(AiffData.class.getClassLoader().getResource(path));
    }

    public static AiffData create(InputStream is)
    {
        try
        {
            return create(AudioSystem.getAudioInputStream(is));
        }
        catch(Exception e)
        {
            LWJGLUtil.log("Unable to create from inputstream");
            e.printStackTrace();
            return null;
        }
    }

    public static AiffData create(byte buffer[])
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

    public static AiffData create(ByteBuffer buffer)
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

    public static AiffData create(AudioInputStream ais)
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
        ByteBuffer buffer = convertAudioBytes(audioformat, buf, audioformat.getSampleSizeInBits() == 16);
        AiffData Aiffdata = new AiffData(buffer, channels, (int)audioformat.getSampleRate());
        try
        {
            ais.close();
        }
        catch(IOException ioe) { }
        return Aiffdata;
    }

    private static ByteBuffer convertAudioBytes(AudioFormat format, byte audio_bytes[], boolean two_bytes_data)
    {
        ByteBuffer dest = ByteBuffer.allocateDirect(audio_bytes.length);
        dest.order(ByteOrder.nativeOrder());
        ByteBuffer src = ByteBuffer.wrap(audio_bytes);
        src.order(ByteOrder.BIG_ENDIAN);
        if(two_bytes_data)
        {
            ShortBuffer dest_short = dest.asShortBuffer();
            for(ShortBuffer src_short = src.asShortBuffer(); src_short.hasRemaining(); dest_short.put(src_short.get()));
        } else
        {
            byte b;
            for(; src.hasRemaining(); dest.put(b))
            {
                b = src.get();
                if(format.getEncoding() == javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED)
                    b += 127;
            }

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
