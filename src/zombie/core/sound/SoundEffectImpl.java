// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SoundEffectImpl.java

package zombie.core.sound;

import java.io.PrintStream;
import org.lwjgl.openal.OpenALException;

// Referenced classes of package zombie.core.sound:
//            Buffer, Stream, SoundEffect, SoundEngine, 
//            Source, Sound, StreamInstance, Attenuator

class SoundEffectImpl
    implements SoundEffect
{
	/*     */   private static enum State
	/*     */   {
	/*  81 */     PLAY, 
	/*  82 */     PAUSE, 
	/*  83 */     REWIND, 
	/*  84 */     STOP;
	/*     */   }
	/*     */ 
	/*     */   private static enum Fade
	/*     */   {
	/*  73 */     NONE, 
	/*  74 */     IN, 
	/*  75 */     OUT;
	/*     */   }

    SoundEffectImpl(SoundEngine player, Source source, Sound sound)
    {
        active = true;
        updatePitch = true;
        updateState = true;
        updateBuffer = true;
        updateLooping = true;
        gain = 1.0F;
        pitch = 1.0F;
        fadeType = Fade.NONE;
        state = State.PLAY;
        this.player = player;
        this.source = source;
        this.sound = sound;
        if(source == null || sound == null)
        {
            return;
        } else
        {
            updateBuffer = sound instanceof Buffer;
            buffer = updateBuffer ? (Buffer)sound : null;
            stream = updateBuffer ? null : ((Stream)sound).getInstance(source);
            attenuator = player.getDefaultAttenuator();
            looped = sound.isLooped();
            return;
        }
    }

    void tick()
    {
        if(!active)
            return;
        
        try
        /*     */     {
        	
        if(stream != null && stream.getOwner() != source)
        {
            System.out.println((new StringBuilder()).append(this).append(" lost ownership of stream ").append(stream).append(" to ").append(stream.getOwner()).toString());
            stream = null;
            deactivate();
            return;
        }
        

        switch(fadeType.ordinal())
        {
        case 1: // '\001'
            if(fadeDuration == 0)
                gain = finalGain;
            else
                gain = interpolate(initialGain, finalGain, (float)fadeTick / (float)fadeDuration);
            fadeTick++;
            if(fadeTick >= fadeDuration)
            {
                gain = interpolate(initialGain, finalGain, (float)fadeTick / (float)fadeDuration);
                fadeType = Fade.NONE;
            }
            break;

        case 3: // '\003'
            break;

        case 2: // '\002'
            if(fadeDuration == 0)
                gain = finalGain;
            else
                gain = interpolate(initialGain, finalGain, (float)fadeTick / (float)fadeDuration);
            fadeTick++;
            if(fadeTick >= fadeDuration)
            {
                fadeType = Fade.NONE;
                state = State.STOP;
                updateState = true;
            }
            break;

       
        }
        
        float attenuation = attenuator.getAttenuation(x - player.getOriginX(), y - player.getOriginY());
        source.set(4106, gain * sound.getGain() * attenuation);
        if(updatePitch)
        {
            source.set(4099, pitch * sound.getPitch());
            updatePitch = false;
        }
        if(updateLooping)
        {
            source.setLooped(looped);
            updateLooping = false;
        }
        if(updateBuffer)
        {
            if(buffer != null)
                source.attach(buffer);
            updateBuffer = false;
        }
        
        
        if (this.updateState) {
        	/* 180 */         switch (this.state.ordinal()) {
        	/*     */         case 1:
        	/* 182 */           if (this.stream != null) {
        	/* 183 */             this.stream.setPlaying(true);
        	/* 184 */             this.player.registerStream(this.stream);
        	/*     */           } else {
        	/* 186 */             this.source.play();
        	/*     */           }
        	/* 188 */           break;
        	/*     */         case 2:
        	/* 190 */           this.source.pause();
        	/* 191 */           break;
        	/*     */         case 3:
        	/* 193 */           this.source.rewind();
        	/* 194 */           break;
        	/*     */         case 4:
        	/* 196 */           if (this.stream != null) {
        	/* 197 */             this.player.deregisterStream(this.stream);
        	/* 198 */             this.stream.setPlaying(false);
        	/* 199 */             deactivate();
        	/* 200 */             return;
        	/*     */           }
        	/* 202 */           this.source.stop();
        	/*     */ 
        	/* 204 */           break;

        	/*     */        
        	/*     */         }
        	/*     */ 
        	/*     */       }
        	/*     */ 
        	/* 211 */       if (!this.updateState)
        	/* 212 */         if (this.buffer != null) {
        	/* 213 */           int sourceState = this.source.getInt(4112);
        	/* 214 */           if ((sourceState == 4116) || (sourceState == 4113))
        	/*     */           {
        	/* 216 */             deactivate();
        	/*     */           }
        	/* 218 */         } else if ((this.stream != null) && (!this.stream.isPlaying())) {
        	/* 219 */           deactivate();
        	/*     */         }
        	
        	

/*     */     }
/*     */     catch (OpenALException e) {
/* 223 */       deactivate();
/*     */     }
/*     */ 
/* 226 */     this.updateState = false;
        
        
        
    }

    public void setAttenuator(Attenuator attenuator)
    {
        if(attenuator == null)
            this.attenuator = player.getDefaultAttenuator();
        else
            this.attenuator = attenuator;
    }

    public void setPosition(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setGain(float gain)
    {
        this.gain = gain;
    }

    public void setPitch(float pitch)
    {
        this.pitch = pitch;
        updatePitch = true;
    }

    public void play()
    {
        state = State.PLAY;
        updateState = true;
    }

    public void pause()
    {
        state = State.PAUSE;
        updateState = true;
    }

    public void stop()
    {
        state = State.STOP;
        updateState = true;
    }

    public void rewind()
    {
        state = State.REWIND;
        updateState = true;
    }

    public void setLooped(boolean looped)
    {
        this.looped = looped;
        updateLooping = true;
    }

    public void setFade(int duration, float finalGain, boolean stopAtEnd)
    {
        fadeDuration = duration;
        initialGain = gain;
        this.finalGain = finalGain;
        fadeTick = 0;
        if(stopAtEnd)
            fadeType = Fade.OUT;
        else
            fadeType = Fade.IN;
    }

    public boolean isActive()
    {
        return active;
    }

    void deactivate()
    {
        if(!active)
            return;
        try
        {
            buffer = null;
            if(stream != null)
            {
                player.deregisterStream(stream);
                stream.setPlaying(false);
                stream = null;
            }
            if(source != null)
                source.stop();
        }
        catch(OpenALException e) { }
        active = false;
    }

    Source getSource()
    {
        return source;
    }

    private static float interpolate(float a, float b, float ratio)
    {
        if(a == b)
            return a;
        if(ratio < 0.0F)
            ratio = 0.0F;
        else
        if(ratio > 1.0F)
            ratio = 1.0F;
        return a * (1.0F - ratio) + b * ratio;
    }

    private final Source source;
    private final Sound sound;
    private boolean active;
    private boolean updatePitch;
    private boolean updateState;
    private boolean updateBuffer;
    private boolean updateLooping;
    private Attenuator attenuator;
    private float x;
    private float y;
    private float z;
    private float gain;
    private float pitch;
    boolean looped;
    int fadeTick;
    int fadeDuration;
    float initialGain;
    float finalGain;
    Buffer buffer;
    StreamInstance stream;
    Fade fadeType;
    private State state;
    private final SoundEngine player;
 

}
