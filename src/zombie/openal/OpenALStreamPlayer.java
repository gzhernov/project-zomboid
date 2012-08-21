// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   OpenALStreamPlayer.java

package zombie.openal;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.OpenALException;
import zombie.GameWindow;

// Referenced classes of package zombie.openal:
//            OggInputStream, AudioInputStream

public class OpenALStreamPlayer
{

    public OpenALStreamPlayer(int source, String ref)
    {
        buffer = new byte[0x14000];
        bufferData = BufferUtils.createByteBuffer(0x14000);
        done = true;
        unqueued = BufferUtils.createIntBuffer(1);
        this.source = source;
        this.ref = ref;
        bufferNames = BufferUtils.createIntBuffer(3);
        AL10.alGenBuffers(bufferNames);
    }

    public OpenALStreamPlayer(int source, URL url)
    {
        buffer = new byte[0x14000];
        bufferData = BufferUtils.createByteBuffer(0x14000);
        done = true;
        unqueued = BufferUtils.createIntBuffer(1);
        this.source = source;
        this.url = url;
        bufferNames = BufferUtils.createIntBuffer(3);
        AL10.alGenBuffers(bufferNames);
    }

    public boolean done()
    {
        return done;
    }

    public float getPosition()
    {
        return positionOffset + AL10.alGetSourcef(source, 4132);
    }

    public String getSource()
    {
        return url != null ? url.toString() : ref;
    }

    public void play(boolean loop)
        throws IOException
    {
        this.loop = loop;
        initStreams();
        done = false;
        AL10.alSourceStop(source);
        removeBuffers();
        startPlayback();
    }

    public boolean setPosition(float position)
    {
        float sampleRate;
        float sampleSize;
        try
        /*     */     {
        	
        if(getPosition() > position)
            initStreams();
        sampleRate = audio.getRate();
        if(audio.getChannels() > 1)
            sampleSize = 4F;
        else
            sampleSize = 2.0F;
        while(positionOffset < position) 
        {
            int count = audio.read(buffer);
            if(count != -1)
            {
                float bufferLength = (float)count / sampleSize / sampleRate;
                positionOffset += bufferLength;
            } else
            {
                if(loop)
                    initStreams();
                else
                    done = true;
                return false;
            }
        }
        startPlayback();
        return true;


        
    /*     */     }
    /*     */     catch (IOException e)
    /*     */     {
    /*     */     }
        return false;
        
    }

    public void setup(float pitch)
    {
        this.pitch = pitch;
    }

    public boolean stream(int bufferId)
    {
    	/*     */     try
    	/*     */     {
    		
    
        int count = this.audio.read(buffer);
        if(count != -1)
        {
            bufferData.clear();
            bufferData.put(buffer, 0, count);
            bufferData.flip();
            int format = audio.getChannels() <= 1 ? 4353 : 4355;
            try
            /*     */         {
            /* 252 */           AL10.alBufferData(bufferId, format, this.bufferData, this.audio.getRate());
            /*     */         }
            /*     */         catch (OpenALException e)
            /*     */         {
            /* 258 */           return false;
            /*     */         }

            
            
        }else if(loop)
        {
            initStreams();
            stream(bufferId);
        } else
        {
            done = true;
            return false;
        }
        
        /* 277 */       return true;
    /*     */     }
    /*     */     catch (IOException e)
    /*     */     {
    /*     */     }
    /*     */ 
    /* 283 */     return false;

    }

    public void update()
    {
        if(done)
            return;
        float sampleRate = audio.getRate();
        float sampleSize;
        if(audio.getChannels() > 1)
            sampleSize = 4F;
        else
            sampleSize = 2.0F;
        for(int processed = AL10.alGetSourcei(source, 4118); processed > 0; processed--)
        {
            unqueued.clear();
            AL10.alSourceUnqueueBuffers(source, unqueued);
            int bufferIndex = unqueued.get(0);
            float bufferLength = (float)AL10.alGetBufferi(bufferIndex, 8196) / sampleSize / sampleRate;
            positionOffset += bufferLength;
            if(stream(bufferIndex))
            {
                AL10.alSourceQueueBuffers(source, unqueued);
                continue;
            }
            remainingBufferCount--;
            if(remainingBufferCount == 0)
                done = true;
        }

        int state = AL10.alGetSourcei(source, 4112);
        if(state != 4114)
            AL10.alSourcePlay(source);
    }

    private void initStreams()
        throws IOException
    {
        if(this.audio != null)
            this.audio.close();
        OggInputStream audio;
        if(url != null)
        {
            audio = new OggInputStream(url.openStream());
        } else
        {
            String home = GameWindow.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
            home = home.replace("zombie.jar", "");
            str = new FileInputStream((new StringBuilder()).append(home).append(ref).toString());
            audio = new OggInputStream(str);
        }
        this.audio = audio;
        positionOffset = 0.0F;
    }

    private void removeBuffers()
    {
        IntBuffer buffer = BufferUtils.createIntBuffer(1);
        for(int queued = AL10.alGetSourcei(source, 4117); queued > 0; queued--)
            AL10.alSourceUnqueueBuffers(source, buffer);

    }

    private void startPlayback()
    {
        AL10.alSourcei(source, 4103, 0);
        AL10.alSourcef(source, 4099, pitch);
        remainingBufferCount = 3;
        for(int i = 0; i < 3; i++)
            stream(bufferNames.get(i));

        AL10.alSourceQueueBuffers(source, bufferNames);
        AL10.alSourcePlay(source);
    }

    public static final int BUFFER_COUNT = 3;
    private static final int sectionSize = 0x14000;
    private AudioInputStream audio;
    private byte buffer[];
    private ByteBuffer bufferData;
    private IntBuffer bufferNames;
    private boolean done;
    private boolean loop;
    private float pitch;
    private float positionOffset;
    private String ref;
    private int remainingBufferCount;
    private int source;
    private IntBuffer unqueued;
    private URL url;
    FileInputStream str;
}
