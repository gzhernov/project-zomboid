// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StreamInstance.java

package zombie.core.sound;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

// Referenced classes of package zombie.core.sound:
//            Buffer, OggInputStream, Stream, Source

class StreamInstance
{

    StreamInstance(Stream sourceStream)
    {
        init(sourceStream);
    }

    void init(Stream stream)
    {
        this.stream = stream;
    }

    Stream getStream()
    {
        return stream;
    }

    synchronized void setSource(Source newSource)
    {
        if(source != newSource)
            setPlaying(false);
        source = newSource;
    }

    Source getOwner()
    {
        return source;
    }

    void create()
    {
        if(isCreated())
            return;
        if(!AL.isCreated())
            return;
        try
        {
            buffers = new Buffer[BUFFERS];
            for(int i = 0; i < buffers.length; i++)
            {
                buffers[i] = new Buffer();
                buffers[i].create();
            }

            buf = new byte[BUFSIZE];
            byteBuf = BufferUtils.createByteBuffer(BUFSIZE);
            initStream();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private void initStream()
    {
        String url = stream.getURL();
        if(url.startsWith("classpath:"))
            try
            {
                inputStream = new OggInputStream(new BufferedInputStream(new FileInputStream(url.substring(10))));
            }
            catch(FileNotFoundException ex)
            {
                Logger.getLogger(StreamInstance.class.getName()).log(Level.SEVERE, null, ex);
            }
        else
            try
            {
                inputStream = new OggInputStream(new BufferedInputStream((new URL(url)).openStream()));
            }
            catch(IOException e)
            {
                throw new RuntimeException((new StringBuilder()).append("Failed to open URL ").append(url).toString(), e);
            }
        frequency = inputStream.getRate();
        switch(inputStream.getFormat())
        {
        case 1: // '\001'
            format = 4353;
            break;

        case 2: // '\002'
            format = 4355;
            break;

        default:
            throw new RuntimeException((new StringBuilder()).append("Unsupported ogg format loading ").append(url).toString());
        }
    }

    void destroy()
    {
        if(!isCreated())
            return;
        if(buffers != null)
        {
            for(int i = 0; i < buffers.length; i++)
                buffers[i].destroy();

            buffers = null;
        }
        if(inputStream != null)
        {
            try
            {
                inputStream.close();
            }
            catch(IOException e)
            {
                e.printStackTrace(System.err);
            }
            inputStream = null;
        }
        buf = null;
        byteBuf = null;
    }

    boolean isCreated()
    {
        return buf != null;
    }

    int getFormat()
    {
        return format;
    }

    synchronized boolean isPlaying()
    {
        return playing;
    }

    synchronized void setPlaying(boolean playing)
    {
        if(this.playing == playing)
            return;
        this.playing = playing;
        if(!playing)
        {
            numQueued = 0;
            waitForQueue = false;
            startPlaying = false;
            writeBuf = 0;
            source.stop();
            int queued;
            do
            {
                queued = AL10.alGetSourcei(source.getSourceID(), 4117);
                if(queued > 0)
                    source.dequeue();
            } while(queued > 0);
            int processed;
            do
            {
                processed = AL10.alGetSourcei(source.getSourceID(), 4118);
                if(processed > 0)
                    AL10.alSourceUnqueueBuffers(source.getSourceID());
            } while(processed > 0);
        } else
        {
            startPlaying = true;
        }
        notifyAll();
    }

    void reset()
    {
        if(inputStream != null)
        {
            try
            {
                inputStream.close();
            }
            catch(IOException e) { }
            inputStream = null;
        }
        initStream();
    }

    synchronized void tick()
        throws Exception
    {
        if(!buffers[writeBuf].isCreated())
            return;
        if(!isPlaying())
            return;
        if(waitForQueue)
        {
            int processed = AL10.alGetSourcei(source.getSourceID(), 4118);
            if(processed <= 0)
                return;
            source.dequeue();
            numQueued -= processed;
            waitForQueue = false;
        }
        int read = inputStream.read(buf, pos, buf.length - pos);
        if(read == -1)
        {
            if(stream.isLooped())
            {
                initStream();
                tick();
            } else
            if(numQueued == 0)
                setPlaying(false);
            else
                waitForQueue = true;
        } else
        {
            pos += read;
            if(pos == buf.length)
            {
                pos = 0;
                byteBuf.put(buf);
                byteBuf.flip();
                Buffer bufToQueue = buffers[writeBuf++];
                if(writeBuf == buffers.length)
                    writeBuf = 0;
                numQueued++;
                if(numQueued == buffers.length)
                    waitForQueue = true;
                if(startPlaying)
                {
                    source.unattach();
                    startPlaying = false;
                }
                AL10.alBufferData(bufToQueue.getBufferID(), format, byteBuf, frequency);
                source.queue(bufToQueue);
                if(source.getInt(4112) != 4114)
                    source.play();
                byteBuf.clear();
            }
        }
    }

    private static final int BUFSIZE = Integer.parseInt(System.getProperty("zombie.core.sound.StreamInstance.BUFSIZE", "65536"));
    private static final int BUFFERS = Integer.parseInt(System.getProperty("zombie.core.sound.StreamInstance.BUFFERS", "6"));
    private Stream stream;
    private Buffer buffers[];
    private int writeBuf;
    private OggInputStream inputStream;
    private byte buf[];
    private ByteBuffer byteBuf;
    private int pos;
    private int numQueued;
    private boolean waitForQueue;
    private boolean playing;
    private boolean startPlaying;
    private int frequency;
    private int format;
    private Source source;

}
