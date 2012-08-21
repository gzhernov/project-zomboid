// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AudioManager.java

package zombie.core.audio;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import zombie.openal.Audio;
import zombie.openal.AudioLoader;

public class AudioManager
{
    public class AudioInstance
    {

        public Audio internalAudio;
        public boolean bLoaded;
        

        public AudioInstance()
        {
           
            bLoaded = false;
        }
    }

    public class AudioLoaderThread extends Thread
    {

        public String fileToLoad;
        public AudioInstance toFill;
        PostLoadEvent event;
        

        public AudioLoaderThread(Runnable runnable, String fileToLoad, PostLoadEvent event)
        {
           
            this.fileToLoad = null;
            toFill = null;
            this.fileToLoad = fileToLoad;
            toFill = new AudioInstance();
        }
    }

    public static interface PostLoadEvent
    {

        public abstract void run();
    }


    public AudioManager()
    {
        AudioLoaderThread = null;
        fileToLoad = null;
    }

    public void init()
    {
    }

    public AudioInstance LoadAndPlayAudio(String filename)
    {
        return LoadAudio(filename, new PostLoadEvent() {

            public void run()
            {
            }

           

            
            
        }
);
    }

    public AudioInstance LoadAudio(String filename, PostLoadEvent event)
    {
        fileToLoad = filename;
        AudioLoaderThread = new AudioLoaderThread(new Runnable() {

            public void run()
            {
                if(fileToLoad == null)
                    return;
                try
                {
                    String format = "OGG";
                    if(fileToLoad.toLowerCase().contains(".wav"))
                        format = "WAV";
                    Audio a = AudioLoader.getAudio(format, fileToLoad);
                    if(a != null)
                        AudioLoaderThread.toFill.internalAudio = a;
                }
                catch(IOException ex)
                {
                    Logger.getLogger(AudioManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

           
        }
, filename, event);
        AudioLoaderThread.run();
        return AudioLoaderThread.toFill;
    }

    public static AudioManager instance = new AudioManager();
    public AudioLoaderThread AudioLoaderThread;
    public String fileToLoad;

}
