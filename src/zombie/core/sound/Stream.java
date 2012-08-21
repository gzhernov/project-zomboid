// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Stream.java

package zombie.core.sound;

import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.openal.AL;

// Referenced classes of package zombie.core.sound:
//            AbstractSound, StreamInstance, Attenuator, Source

public class Stream extends AbstractSound
{

    public Stream()
    {
    }

    public Stream(String url, int priority)
    {
        super(url, priority, true);
    }

    public Stream(String url, int priority, boolean looped)
    {
        super(url, priority, looped);
    }

    public Stream(String url, float gain, float pitch, int priority, boolean looped)
    {
        super(url, gain, pitch, priority, looped, null);
    }

    public Stream(String url, float gain, float pitch, int priority, boolean looped, Attenuator attenuator)
    {
        super(url, gain, pitch, priority, looped, attenuator);
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
            StreamInstance ret = new StreamInstance(this);
            ret.create();
            format = ret.getFormat();
            ret.destroy();
            created = true;
            return;
        }
    }

    public void destroy()
    {
        if(!isCreated())
            return;
        Iterator i = POOL.iterator();
        do
        {
            if(!i.hasNext())
                break;
            StreamInstance si = (StreamInstance)i.next();
            if(si.getStream() == this)
            {
                si.destroy();
                i.remove();
            }
        } while(true);
    }

    public boolean isCreated()
    {
        return created;
    }

    public int getFormat()
    {
        if(!isCreated())
            throw new IllegalStateException("Not yet created");
        else
            return format;
    }

    public boolean isStereo()
    {
        if(!isCreated())
            throw new IllegalStateException("Not yet created");
        else
            return format == 4355;
    }

    public String toString()
    {
        return (new StringBuilder()).append("Stream[").append(url).append("]").toString();
    }

    public StreamInstance getInstance(Source source)
    {
        if(!isCreated())
            throw new IllegalStateException("Not yet created");
        for(int i = 0; i < POOL.size(); i++)
        {
            StreamInstance ret = (StreamInstance)POOL.get(i);
            if(!ret.isPlaying())
            {
                ret.setSource(source);
                ret.init(this);
                ret.reset();
                return ret;
            }
        }

        StreamInstance ret = new StreamInstance(this);
        ret.create();
        ret.setSource(source);
        POOL.add(ret);
        return ret;
    }



    private static final ArrayList POOL = new ArrayList(4);
    private int format;
    private boolean created;

}
