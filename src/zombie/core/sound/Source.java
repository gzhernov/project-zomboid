// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Source.java

package zombie.core.sound;

import java.io.PrintStream;
import java.nio.IntBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.*;

// Referenced classes of package zombie.core.sound:
//            Buffer

class Source
{

    public Source()
    {
    }

    static synchronized void unattach(Buffer buffer)
    {
        for(int i = 0; i < CREATED_SOURCES.size(); i++)
        {
            Source s = (Source)CREATED_SOURCES.get(i);
            if(s.attachedBuffer == buffer)
            {
                s.stop();
                s.unattach();
            }
        }

    }

    public void play()
    {
    	assert (isCreated()) : (this + " is not created yet");
            AL10.alSourcePlay(source);
            return;
        
    }

    public void pause()
    {
    	assert (isCreated()) : (this + " is not created yet");
            AL10.alSourcePause(source);
            return;
        
    }

    public void stop()
    {
    	assert (isCreated()) : (this + " is not created yet");
            AL10.alSourceStop(source);
            return;
        
    }

    public void rewind()
    {
    	assert (isCreated()) : (this + " is not created yet");
            AL10.alSourceRewind(source);
            return;
        
    }

    public void set(int property, float value)
    {
    	assert (isCreated()) : (this + " is not created yet");
            AL10.alSourcef(source, property, value);
            return;
        
    }

    public void set(int property, float x, float y, float z)
    {
    	assert (isCreated()) : (this + " is not created yet");
            AL10.alSource3f(source, property, x, y, z);
            return;
        
    }

    public void set(int property, int value)
    {
    	assert (isCreated()) : (this + " is not created yet");
            AL10.alSourcei(source, property, value);
            return;
        
    }

    public void create()
    {
        if(isCreated())
            return;
        if(!AL.isCreated())
        {
            return;
        } else
        {
            source = AL10.alGenSources();
            CREATED_SOURCES.add(this);
            return;
        }
    }

    public void destroy()
    {
        if(!isCreated())
        {
            return;
        } else
        {
            unattach();
            AL10.alDeleteSources(source);
            source = 0;
            CREATED_SOURCES.remove(this);
            return;
        }
    }

    public boolean isCreated()
    {
        return source != 0;
    }

    public int getSourceID()
    {
    	assert (isCreated()) : (this + " is not created yet");
            return source;
    }

    public void attach(Buffer buffer)
    {
    	assert (isCreated()) : (this + " is not created yet");
        try
        {
            unattach();
            set(4105, buffer.getBufferID());
            attachedBuffer = buffer;
        }
        catch(OpenALException e)
        {
            System.err.println((new StringBuilder()).append("Failed: ").append(buffer.getBufferID()).append(" source: ").append(source).toString());
            throw e;
        }
        if(buffer.isLooped())
            set(4103, 1);
        else
            set(4103, 0);
    }

    public void queue(Buffer buffer)
    {
    	assert (isCreated()) : (this + " is not created yet");
        if(attachedBuffer != null)
        {
            attachedBuffer = null;
            stop();
        }
        try
        {
            AL10.alSourceQueueBuffers(source, buffer.getBufferID());
        }
        catch(OpenALException e)
        {
            System.out.println((new StringBuilder()).append("Failed to queue ").append(buffer).append(" on ").append(this).toString());
            throw e;
        }
        set(4103, 0);
    }

    public void dequeue()
    {
    	assert (isCreated()) : (this + " is not created yet");
            AL10.alSourceUnqueueBuffers(source);
            return;
       
    }

    public void unattach()
    {
        stop();
        if(attachedBuffer != null)
        {
            set(4105, 0);
            attachedBuffer = null;
        }
    }

    public void setLooped(boolean looped)
    {
        set(4103, looped ? 1 : 0);
    }

    public String toString()
    {
        return (new StringBuilder()).append("Source[").append(source).append("]").toString();
    }

    public int getInt(int param)
    {
    	assert (isCreated()) : (this + " is not created yet");
            return AL10.alGetSourcei(source, param);
    }

    public float getFloat(int param)
    {
    	assert (isCreated()) : (this + " is not created yet");
    	
            return AL10.alGetSourcef(source, param);
    }

    private static final IntBuffer SCRATCH = BufferUtils.createIntBuffer(64);
    private static final ArrayList CREATED_SOURCES = new ArrayList(8);
    private int source;
    private Buffer attachedBuffer;


}
