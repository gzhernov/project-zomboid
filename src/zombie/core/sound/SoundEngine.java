// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SoundEngine.java

package zombie.core.sound;

import java.io.PrintStream;
import java.nio.FloatBuffer;
import java.util.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.*;

// Referenced classes of package zombie.core.sound:
//            Source, SoundEffectImpl, Sound, StreamInstance, 
//            SoundEffect, NoAttenuation, Attenuator

public class SoundEngine
{
    private class StreamThread extends Thread
    {

        public void run()
        {
        }

        void finish()
        {
            finished = true;
        }

        private boolean finished;
       

        StreamThread()
        {
          
            setPriority(8);
            setDaemon(true);
        }
    }

    private static class SoundEffectWrapper
    {

        SoundEffectImpl soundEffect;
        Source linked;
        int priority;
        long age;

        private SoundEffectWrapper()
        {
        }

    }


    public SoundEngine()
    {
        gain = 1.0F;
        currentGain = 1.0F;
        targetGain = 1.0F;
        defaultAttenuator = DEFAULT_ATTENUATOR;
        instance = this;
    }

    public void create()
    {
        if(isCreated())
            return;
        if(!AL.isCreated())
            return;
        sources = new ArrayList();
        try
        {
            for(int i = 0; i < MAX_SOURCES; i++)
            {
                Source s = new Source();
                s.create();
                sources.add(s);
            }

        }
        catch(OpenALException e) { }
        monoSoundEffects = new ArrayList();
        stereoSoundEffects = new ArrayList();
        streams = new LinkedList();
        streamThread = new StreamThread();
        streamThread.start();
        FloatBuffer listenerPos = BufferUtils.createFloatBuffer(3).put(new float[] {
            0.0F, 0.0F, 0.0F
        });
        FloatBuffer listenerVel = BufferUtils.createFloatBuffer(3).put(new float[] {
            0.0F, 0.0F, 0.0F
        });
        FloatBuffer listenerOri = BufferUtils.createFloatBuffer(6).put(new float[] {
            0.0F, 0.0F, -1F, 0.0F, 1.0F, 0.0F
        });
        listenerPos.flip();
        listenerVel.flip();
        listenerOri.flip();
        AL10.alListener(4100, listenerPos);
        AL10.alListener(4102, listenerVel);
        AL10.alListener(4111, listenerOri);
    }

    public void destroy()
    {
        if(!isCreated())
            return;
        synchronized(streams)
        {
            streams = null;
        }
        streamThread.finish();
        try
        {
            streamThread.join();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace(System.err);
        }
        streamThread = null;
        SoundEffectWrapper sew;
        for(Iterator i$ = monoSoundEffects.iterator(); i$.hasNext(); sew.soundEffect.getSource().destroy())
        {
            sew = (SoundEffectWrapper)i$.next();
            sew.soundEffect.deactivate();
        }

        
        for(Iterator i$ = stereoSoundEffects.iterator(); i$.hasNext(); sew.linked.destroy())
        {
            sew = (SoundEffectWrapper)i$.next();
            sew.soundEffect.deactivate();
            sew.soundEffect.getSource().destroy();
        }

        monoSoundEffects = null;
        stereoSoundEffects = null;
        Source s;
        for(Iterator i$ = sources.iterator(); i$.hasNext(); s.destroy())
            s = (Source)i$.next();

        sources = null;
    }

    public void setGain(float gain)
    {
        if(gain < 0.0F || gain > 1.0F)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("gain must be between 0.0f and 1.0f inclusive; got ").append(gain).toString());
        } else
        {
            this.gain = gain;
            return;
        }
    }

    public float getGain()
    {
        return gain;
    }

    public void tick()
    {
        if(!isCreated())
            throw new IllegalStateException("Sound Engine is not created");
        if(currentGain < targetGain)
            currentGain = Math.min(1.0F, currentGain + 0.1F);
        else
        if(currentGain > targetGain)
            currentGain = Math.max(0.0F, currentGain - 0.1F);
        AL10.alListenerf(4106, gain * currentGain);
        Iterator i = monoSoundEffects.iterator();
        do
        {
            if(!i.hasNext())
                break;
            SoundEffectWrapper sew = (SoundEffectWrapper)i.next();
            if(sew.soundEffect.isActive())
            {
                sew.age++;
                sew.soundEffect.tick();
            }
            if(!sew.soundEffect.isActive())
            {
                sources.add(sew.soundEffect.getSource());
                i.remove();
            }
        } while(true);
        i = stereoSoundEffects.iterator();
        do
        {
            if(!i.hasNext())
                break;
            SoundEffectWrapper sew = (SoundEffectWrapper)i.next();
            sew.age++;
            if(sew.soundEffect.isActive())
                sew.soundEffect.tick();
            if(!sew.soundEffect.isActive())
            {
                sources.add(sew.soundEffect.getSource());
                sources.add(sew.linked);
                i.remove();
            }
        } while(true);
        if(musicEffect != null && !musicEffect.isActive())
        {
            music = null;
            musicEffect = null;
        }
    }

    public boolean isCreated()
    {
        return sources != null;
    }

    public SoundEffect play(Sound buf)
    {
        SoundEffectWrapper sew = allocate(buf);
        if(sew != null)
        {
            sew.soundEffect.play();
            if(buf.getFormat() == 4355)
                stereoSoundEffects.add(sew);
            else
                monoSoundEffects.add(sew);
            return sew.soundEffect;
        } else
        {
            SoundEffectImpl ret = new SoundEffectImpl(this, null, null);
            ret.deactivate();
            return ret;
        }
    }

    private SoundEffectWrapper allocate(Sound sound)
    {
        if(sources.size() > (sound.getFormat() != 4355 ? 0 : 1))
        {
            SoundEffectWrapper ret = new SoundEffectWrapper();
            ret.soundEffect = new SoundEffectImpl(this, (Source)sources.remove(sources.size() - 1), sound);
            ret.priority = sound.getPriority();
            if(sound.getFormat() == 4355)
                ret.linked = (Source)sources.remove(sources.size() - 1);
            return ret;
        }
        List search = sound.getFormat() != 4355 ? monoSoundEffects : stereoSoundEffects;
        long oldestAge = 0L;
        int lowestPriority = sound.getPriority();
        int foundIndex = -1;
        int n = search.size();
        for(int i = 0; i < n; i++)
        {
            SoundEffectWrapper sew = (SoundEffectWrapper)search.get(i);
            if(sew.priority < lowestPriority)
            {
                foundIndex = i;
                lowestPriority = sew.priority;
                oldestAge = sew.age;
                continue;
            }
            if(sew.priority == lowestPriority && sew.age > oldestAge)
            {
                foundIndex = i;
                oldestAge = sew.age;
            }
        }

        if(foundIndex != -1)
        {
            SoundEffectWrapper ret = new SoundEffectWrapper();
            SoundEffectWrapper old = (SoundEffectWrapper)search.remove(foundIndex);
            old.soundEffect.deactivate();
            ret.soundEffect = new SoundEffectImpl(this, old.soundEffect.getSource(), sound);
            ret.priority = sound.getPriority();
            if(sound.getFormat() == 4355)
                ret.linked = old.linked;
            return ret;
        } else
        {
            return null;
        }
    }

    public void fadeOut()
    {
        targetGain = 0.0F;
    }

    public void fadeIn()
    {
        targetGain = 1.0F;
    }

    public void music(Sound sound, int fadeDuration)
    {
        if(music == sound)
            return;
        if(musicEffect != null)
            musicEffect.setFade(fadeDuration, 0.0F, true);
        music = sound;
        if(music != null)
        {
            musicEffect = play(music);
            musicEffect.setGain(0.0F);
            musicEffect.setFade(fadeDuration, 1.0F, false);
        }
    }

    public SoundEffect getMusic()
    {
        return musicEffect;
    }

    void registerStream(StreamInstance stream)
    {
        synchronized(streams)
        {
            if(!streams.contains(stream))
                streams.add(stream);
        }
    }

    void deregisterStream(StreamInstance stream)
    {
        synchronized(streams)
        {
            streams.remove(stream);
        }
    }

    private void log(String msg)
    {
        System.err.println(msg);
    }

    public void setDefaultAttenuator(Attenuator defaultAttenuator)
    {
        if(defaultAttenuator == null)
        {
            throw new IllegalArgumentException("defaultAttenuator cannot be null");
        } else
        {
            this.defaultAttenuator = defaultAttenuator;
            return;
        }
    }

    public Attenuator getDefaultAttenuator()
    {
        return defaultAttenuator;
    }

    public void setOrigin(float x, float y, float z)
    {
        originX = x;
        originY = y;
        originZ = z;
    }

    public float getOriginX()
    {
        return originX;
    }

    public float getOriginY()
    {
        return originY;
    }

    public float getOriginZ()
    {
        return originZ;
    }

    private static final boolean DEBUG = true;
    private static final Attenuator DEFAULT_ATTENUATOR;
    private static final float MASTER_FADE_SPEED = 0.1F;
    private static final int MAX_SOURCES = Integer.parseInt(System.getProperty("zombie.core.sound.MAX_SOURCES", "64"));
    public static SoundEngine instance = null;
    private List sources;
    private List monoSoundEffects;
    private List stereoSoundEffects;
    private List streams;
    private Sound music;
    private SoundEffect musicEffect;
    private float gain;
    private float currentGain;
    private float targetGain;
    private Attenuator defaultAttenuator;
    private float originX;
    private float originY;
    private float originZ;
    private StreamThread streamThread;

    static 
    {
        DEFAULT_ATTENUATOR = NoAttenuation.INSTANCE;
    }
}
