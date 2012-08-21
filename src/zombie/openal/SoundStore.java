// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SoundStore.java

package zombie.openal;

import gnu.trove.map.hash.THashMap;
import java.io.*;
import java.net.URL;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.security.*;
import java.util.Stack;
import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.openal.*;
import zombie.GameWindow;

// Referenced classes of package zombie.openal:
//            NullAudio, DeferredSound, AudioImpl, MODSound, 
//            OggDecoder, StreamSound, OpenALStreamPlayer, OggData, 
//            AiffData, WaveData, Audio

public class SoundStore
{

    private SoundStore()
    {
        currentMusic = -1;
        inited = false;
        lastCurrentMusicVolume = 1.0F;
        loaded = new THashMap();
        maxSources = 64;
        musicVolume = 1.0F;
        soundVolume = 1.0F;
        sourcePos = BufferUtils.createFloatBuffer(3);
        sourceVel = BufferUtils.createFloatBuffer(3).put(new float[] {
            0.0F, 0.0F, 0.0F
        });
        audioStore = new Stack();
        usedAudio = new Stack();
    }

    public static SoundStore get()
    {
        return store;
    }

    public void clear()
    {
        store = new SoundStore();
    }

    public void disable()
    {
        inited = true;
    }

    public Audio getAIF(String ref)
        throws IOException
    {
        return getAIF(ref, ((InputStream) (new FileInputStream(ref))));
    }

    public Audio getAIF(InputStream in)
        throws IOException
    {
        return getAIF(in.toString(), in);
    }

    public Audio getAIF(String ref, InputStream in)
        throws IOException
    {
        in = new BufferedInputStream(in);
        if(!soundWorks)
            return new NullAudio();
        if(!inited)
            throw new RuntimeException("Can't load sounds until SoundStore is init(). Use the container init() method.");
        if(deferred)
            return new DeferredSound(ref, in, 4);
        int buffer = -1;
        if(loaded.get(ref) != null)
            buffer = ((Integer)loaded.get(ref)).intValue();
        else
            try
            {
                IntBuffer buf = BufferUtils.createIntBuffer(1);
                AiffData data = AiffData.create(in);
                AL10.alGenBuffers(buf);
                AL10.alBufferData(buf.get(0), data.format, data.data, data.samplerate);
                loaded.put(ref, new Integer(buf.get(0)));
                buffer = buf.get(0);
            }
            catch(Exception e)
            {
                IOException x = new IOException((new StringBuilder()).append("Failed to load: ").append(ref).toString());
                x.initCause(e);
                throw x;
            }
        if(buffer == -1)
            throw new IOException((new StringBuilder()).append("Unable to load: ").append(ref).toString());
        else
            return getAudio().initAudioImpl(this, buffer);
    }

    public AudioImpl getAudio()
    {
        if(audioStore.isEmpty())
        {
            for(int n = 0; n < 100; n++)
            {
                AudioImpl imp = new AudioImpl();
                audioStore.push(imp);
            }

        }
        AudioImpl a = (AudioImpl)audioStore.pop();
        usedAudio.push(a);
        return a;
    }

    public float getCurrentMusicVolume()
    {
        return lastCurrentMusicVolume;
    }

    public Audio getMOD(String ref)
        throws IOException
    {
        return getMOD(ref, ((InputStream) (new FileInputStream(ref))));
    }

    public Audio getMOD(InputStream in)
        throws IOException
    {
        return getMOD(in.toString(), in);
    }

    public Audio getMOD(String ref, InputStream in)
        throws IOException
    {
        if(!soundWorks)
            return new NullAudio();
        if(!inited)
            throw new RuntimeException("Can't load sounds until SoundStore is init(). Use the container init() method.");
        if(deferred)
            return new DeferredSound(ref, in, 3);
        else
            return new MODSound(this, in);
    }

    public float getMusicVolume()
    {
        return musicVolume;
    }

    public Audio getOgg(String ref)
        throws IOException
    {
        return getOgg(ref, ref);
    }

    public Audio getOggFile(String ref)
        throws IOException
    {
        return getOggFile(ref, ref);
    }

    public Audio getOgg(String ref, String file)
        throws IOException
    {
        if(!soundWorks)
            init();
        if(!inited)
            throw new RuntimeException("Can't load sounds until SoundStore is init(). Use the container init() method.");
        int buffer = -1;
        if(loaded.get(file) != null)
            buffer = ((Integer)loaded.get(file)).intValue();
        else
            try
            {
                IntBuffer buf = BufferUtils.createIntBuffer(1);
                OggDecoder decoder = new OggDecoder();
                OggData ogg = decoder.getData(new FileInputStream(ref));
                AL10.alGenBuffers(buf);
                AL10.alBufferData(buf.get(0), ogg.channels <= 1 ? 4353 : 4355, ogg.data, ogg.rate);
                buffer = buf.get(0);
                loaded.put(ref, new Integer(buffer));
            }
            catch(Exception e)
            {
                Sys.alert("Error", (new StringBuilder()).append("Failed to load: ").append(ref).append(" - ").append(e.getMessage()).toString());
                throw new IOException((new StringBuilder()).append("Unable to load: ").append(ref).toString());
            }
        if(buffer == -1)
            throw new IOException((new StringBuilder()).append("Unable to load: ").append(ref).toString());
        else
            return getAudio().initAudioImpl(this, buffer);
    }

    public Audio getOggFile(String ref, String file)
        throws IOException
    {
        if(!soundWorks)
            return new NullAudio();
        if(!inited)
            throw new RuntimeException("Can't load sounds until SoundStore is init(). Use the container init() method.");
        int buffer = -1;
        if(loaded.get(file) != null)
            buffer = ((Integer)loaded.get(file)).intValue();
        else
            try
            {
                IntBuffer buf = BufferUtils.createIntBuffer(1);
                OggDecoder decoder = new OggDecoder();
                String home = GameWindow.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
                home = home.replace("zombie.jar", "");
                FileInputStream str = new FileInputStream((new StringBuilder()).append(home).append(ref).toString());
                OggData ogg = decoder.getData(str);
                AL10.alGenBuffers(buf);
                AL10.alBufferData(buf.get(0), ogg.channels <= 1 ? 4353 : 4355, ogg.data, ogg.rate);
                buffer = buf.get(0);
            }
            catch(Exception e)
            {
                Sys.alert("Error", (new StringBuilder()).append("Failed to load: ").append(ref).append(" - ").append(e.getMessage()).toString());
                throw new IOException((new StringBuilder()).append("Unable to load: ").append(ref).toString());
            }
        if(buffer == -1)
            throw new IOException((new StringBuilder()).append("Unable to load: ").append(ref).toString());
        else
            return getAudio().initAudioImpl(this, buffer);
    }

    public Audio getOggStream(String ref)
        throws IOException
    {
        if(!soundWorks)
            return new NullAudio();
        setMOD(null);
        setStream(null);
        if(currentMusic != -1)
            AL10.alSourceStop(sources.get(0));
        getMusicSource();
        currentMusic = sources.get(0);
        return new StreamSound(new OpenALStreamPlayer(currentMusic, ref));
    }

    public Audio getOggStream(URL ref)
        throws IOException
    {
        if(!soundWorks)
            return new NullAudio();
        setMOD(null);
        setStream(null);
        if(currentMusic != -1)
            AL10.alSourceStop(sources.get(0));
        getMusicSource();
        currentMusic = sources.get(0);
        return new StreamSound(new OpenALStreamPlayer(currentMusic, ref));
    }

    public float getSoundVolume()
    {
        return soundVolume;
    }

    public int getSource(int index)
    {
        if(!soundWorks)
            return -1;
        if(index < 0)
            return -1;
        else
            return sources.get(index);
    }

    public int getSourceCount()
    {
        return sourceCount;
    }

    public Audio getWAV(String ref)
        throws IOException
    {
        return getWAV(ref, ref);
    }

    public Audio getWAV(InputStream in)
        throws IOException
    {
        return null;
    }

    public Audio getWAV(String ref, String in)
        throws IOException
    {
        if(!soundWorks)
            return new NullAudio();
        if(!inited)
            throw new RuntimeException("Can't load sounds until SoundStore is init(). Use the container init() method.");
        int buffer = -1;
        if(loaded.get(ref) != null)
            buffer = ((Integer)loaded.get(ref)).intValue();
        else
            try
            {
                IntBuffer buf = BufferUtils.createIntBuffer(1);
                WaveData data = WaveData.create(in);
                AL10.alGenBuffers(buf);
                AL10.alBufferData(buf.get(0), data.format, data.data, data.samplerate);
                loaded.put(ref, new Integer(buf.get(0)));
                buffer = buf.get(0);
            }
            catch(Exception e)
            {
                IOException x = new IOException((new StringBuilder()).append("Failed to load: ").append(ref).toString());
                x.initCause(e);
                throw x;
            }
        if(buffer == -1)
            throw new IOException((new StringBuilder()).append("Unable to load: ").append(ref).toString());
        else
            return getAudio().initAudioImpl(this, buffer);
    }

    public void init()
    {
        if(inited)
            return;
        inited = true;
        AccessController.doPrivileged(new PrivilegedAction() {

            public Object run()
            {
                try
                {
                    AL.create();
                    soundWorks = true;
                    sounds = true;
                    music = true;
                }
                catch(Exception e)
                {
                    soundWorks = false;
                    sounds = false;
                    music = false;
                }
                return null;
            }
        
        }
);
        
        
        
       
        
        
        if (this.soundWorks)
        /*      */     {
        /*  701 */       this.sourceCount = 0;
        /*  702 */       this.sources = BufferUtils.createIntBuffer(this.maxSources);
        /*      */ 
        /*  704 */       while (AL10.alGetError() == 0)
        /*      */       {
        /*  706 */         IntBuffer temp = BufferUtils.createIntBuffer(1);
        /*      */         try
        /*      */         {
        /*  710 */           AL10.alGenSources(temp);
        /*      */ 
        /*  712 */           if (AL10.alGetError() == 0)
        /*      */           {
        /*  714 */             this.sourceCount += 1;
        /*  715 */             this.sources.put(temp.get(0));
        /*      */ 
        /*  717 */             if (this.sourceCount > this.maxSources - 1)
        /*      */             {
        /*  719 */               break;
        /*      */             }
        /*      */ 
        /*      */           }
        /*      */ 
        /*      */         }
        /*      */         catch (OpenALException e)
        /*      */         {
        /*  727 */           break;
        /*      */         }
        /*      */ 
        /*      */       }
        /*      */ 
        /*  732 */       if (AL10.alGetError() != 0)
        /*      */       {
        /*  734 */         this.sounds = false;
        /*  735 */         this.music = false;
        /*  736 */         this.soundWorks = false;
        /*      */       }
        /*      */       else
        /*      */       {
        /*  742 */         FloatBuffer listenerOri = BufferUtils.createFloatBuffer(6).put(new float[] { 0.0F, 0.0F, -1.0F, 0.0F, 1.0F, 0.0F });
        /*      */ 
        /*  744 */         FloatBuffer listenerVel = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0F, 0.0F, 0.0F });
        /*  745 */         FloatBuffer listenerPos = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0F, 0.0F, 0.0F });
        /*  746 */         listenerPos.flip();
        /*  747 */         listenerVel.flip();
        /*  748 */         listenerOri.flip();
        /*  749 */         AL10.alListener(4100, listenerPos);
        /*  750 */         AL10.alListener(4102, listenerVel);
        /*  751 */         AL10.alListener(4111, listenerOri);
        /*      */       }
        /*      */     }
    }

    public boolean isDeferredLoading()
    {
        return deferred;
    }

    public boolean isMusicOn()
    {
        return music;
    }

    public boolean isMusicPlaying()
    {
        if(!soundWorks)
        {
            return false;
        } else
        {
            int state = AL10.alGetSourcei(sources.get(0), 4112);
            return state == 4114 || state == 4115;
        }
    }

    public boolean musicOn()
    {
        return music;
    }

    public void pauseLoop()
    {
        if(soundWorks && currentMusic != -1)
        {
            paused = true;
            AL10.alSourcePause(currentMusic);
        }
    }

    public void poll(int delta)
    {
        if(!soundWorks)
            return;
        if(paused)
            return;
        for(int n = 0; n < usedAudio.size(); n++)
            if(!((AudioImpl)usedAudio.get(n)).isPlaying())
            {
                AudioImpl a = (AudioImpl)usedAudio.remove(n);
                n--;
                audioStore.push(a);
            }

    }

    public void restartLoop()
    {
        if(music && soundWorks && currentMusic != -1)
        {
            paused = false;
            AL10.alSourcePlay(currentMusic);
        }
    }

    public void setCurrentMusicVolume(float volume)
    {
        if(volume < 0.0F)
            volume = 0.0F;
        if(volume > 1.0F)
            volume = 1.0F;
        if(soundWorks)
        {
            lastCurrentMusicVolume = volume;
            AL10.alSourcef(sources.get(0), 4106, lastCurrentMusicVolume * musicVolume);
        }
    }

    public void setDeferredLoading(boolean deferred)
    {
        this.deferred = deferred;
    }

    public void setMaxSources(int max)
    {
        maxSources = max;
    }

    public void setMusicOn(boolean music)
    {
        if(soundWorks)
        {
            this.music = music;
            if(music)
            {
                restartLoop();
                setMusicVolume(musicVolume);
            } else
            {
                pauseLoop();
            }
        }
    }

    public void setMusicPitch(float pitch)
    {
        if(soundWorks)
            AL10.alSourcef(sources.get(0), 4099, pitch);
    }

    public void setMusicVolume(float volume)
    {
        if(volume < 0.0F)
            volume = 0.0F;
        if(volume > 1.0F)
            volume = 1.0F;
        musicVolume = volume;
        if(soundWorks)
            AL10.alSourcef(sources.get(0), 4106, lastCurrentMusicVolume * musicVolume);
    }

    public void setSoundsOn(boolean sounds)
    {
        if(soundWorks)
            this.sounds = sounds;
    }

    public void setSoundVolume(float volume)
    {
        if(volume < 0.0F)
            volume = 0.0F;
        soundVolume = volume;
    }

    public boolean soundsOn()
    {
        return sounds;
    }

    public boolean soundWorks()
    {
        return soundWorks;
    }

    public void stopSoundEffect(int id)
    {
        AL10.alSourceStop(id);
    }

    boolean isPlaying(int index)
    {
        int state = AL10.alGetSourcei(sources.get(index), 4112);
        return state == 4114;
    }

    boolean isPlaying(OpenALStreamPlayer player)
    {
        return stream == player;
    }

    void playAsMusic(int buffer, float pitch, float gain, boolean loop)
    {
        paused = false;
        setMOD(null);
        if(soundWorks)
        {
            if(currentMusic != -1)
                AL10.alSourceStop(sources.get(0));
            getMusicSource();
            AL10.alSourcei(sources.get(0), 4105, buffer);
            AL10.alSourcef(sources.get(0), 4106, gain);
            AL10.alSourcef(sources.get(0), 4099, pitch);
            AL10.alSourcei(sources.get(0), 4103, loop ? 1 : 0);
            currentMusic = sources.get(0);
            if(!music)
                pauseLoop();
            else
                AL10.alSourcePlay(sources.get(0));
        }
    }

    int playAsSound(int buffer, float pitch, float gain, boolean loop)
    {
        return playAsSoundAt(buffer, pitch, gain, loop, 0.0F, 0.0F, 0.0F);
    }

    int playAsSoundAt(int buffer, float pitch, float gain, boolean loop, float x, float y, float z)
    {
        gain *= soundVolume;
        if(gain == 0.0F)
            gain = 0.001F;
        if(soundWorks && sounds)
        {
            int nextSource = findFreeSource();
            if(nextSource == -1)
            {
                return -1;
            } else
            {
                AL10.alSourceStop(sources.get(nextSource));
                AL10.alSourcei(sources.get(nextSource), 4105, buffer);
                AL10.alSourcef(sources.get(nextSource), 4099, pitch);
                AL10.alSourcef(sources.get(nextSource), 4106, gain);
                AL10.alSourcei(sources.get(nextSource), 4103, loop ? 1 : 0);
                sourcePos.clear();
                sourceVel.clear();
                sourceVel.put(new float[] {
                    0.0F, 0.0F, 0.0F
                });
                sourcePos.put(new float[] {
                    x, y, z
                });
                sourcePos.flip();
                sourceVel.flip();
                AL10.alSourcePlay(sources.get(nextSource));
                return nextSource;
            }
        } else
        {
            return -1;
        }
    }

    void setMOD(MODSound sound)
    {
        if(!soundWorks)
            return;
        currentMusic = sources.get(0);
        stopSource(0);
        mod = sound;
        if(sound != null)
            stream = null;
        paused = false;
    }

    void setMusicGain(float volume, int bufferID)
    {
        if(soundWorks)
            try
            {
                AL10.alSourcef(bufferID, 4106, volume);
            }
            catch(Exception ex) { }
    }

    void setStream(OpenALStreamPlayer stream)
    {
        if(!soundWorks)
            return;
        currentMusic = sources.get(0);
        this.stream = stream;
        if(stream != null)
            mod = null;
        paused = false;
    }

    void stopSource(int index)
    {
        AL10.alSourceStop(sources.get(index));
    }

    private int findFreeSource()
    {
        for(int i = 1; i < sourceCount - 1; i++)
        {
            int state = AL10.alGetSourcei(sources.get(i), 4112);
            if(state != 4114 && state != 4115)
                return i;
        }

        return -1;
    }

    private int getMusicSource()
    {
        return sources.get(0);
    }

    private static SoundStore store = new SoundStore();
    private int currentMusic;
    private boolean deferred;
    private boolean inited;
    private float lastCurrentMusicVolume;
    private THashMap loaded;
    private int maxSources;
    private MODSound mod;
    private boolean music;
    private float musicVolume;
    private int nextSource;
    private boolean paused;
    private boolean sounds;
    private float soundVolume;
    private boolean soundWorks;
    private int sourceCount;
    private FloatBuffer sourcePos;
    private IntBuffer sources;
    private FloatBuffer sourceVel;
    private OpenALStreamPlayer stream;
    public Stack audioStore;
    public Stack usedAudio;




}
