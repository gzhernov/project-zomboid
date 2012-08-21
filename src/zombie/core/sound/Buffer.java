// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Buffer.java

package zombie.core.sound;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

// Referenced classes of package zombie.core.sound:
//            AbstractSound, OggInputStream, Source, Attenuator

public class Buffer extends AbstractSound
{

    public Buffer()
    {
    }

    public Buffer(String url, int priority)
    {
        super(url, priority, false);
    }

    public Buffer(String url, int priority, boolean looped)
    {
        super(url, priority, looped);
    }

    public Buffer(String url, float gain, float pitch, int priority, boolean looped)
    {
        super(url, gain, pitch, priority, looped, null);
    }

    public Buffer(String url, float gain, float pitch, int priority, boolean looped, Attenuator attenuator)
    {
        super(url, gain, pitch, priority, looped, attenuator);
    }

    public void create()
    {
        InputStream is;
        if(!AL.isCreated())
            return;
        buffer = AL10.alGenBuffers();
        if(getURL() == null)
            return;
        is = null;
        try
        {
            if(url.startsWith("classpath:"))
                is = new BufferedInputStream(new FileInputStream(url.substring(10)));
            else
                is = new BufferedInputStream((new URL(url)).openStream());
            OggInputStream ois = new OggInputStream(is);
            switch(ois.getFormat())
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
            int frequency = ois.getRate();
            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFSIZE);
            byte buf[] = new byte[BUFSIZE];
            int read;
            while((read = ois.read(buf)) != -1) 
                baos.write(buf, 0, read);
            ByteBuffer decoded = BufferUtils.createByteBuffer(baos.size());
            decoded.put(baos.toByteArray());
            decoded.flip();
            AL10.alBufferData(buffer, format, decoded, frequency);
            System.out.println((new StringBuilder()).append("Uploaded ").append(decoded.limit()).append(" bytes to OpenAL").toString());
        }
        catch(Exception e)
        {
            if(buffer != 0)
            {
                AL10.alDeleteBuffers(buffer);
                buffer = 0;
            }
            throw new RuntimeException(e);
        } finally {
        	/* 171 */       if (is != null)
        		/*     */         try {
        		/* 173 */           is.close();
        		/*     */         }
        		/*     */         catch (IOException e)
        		/*     */         {
        		/*     */         }
        		/*     */     }
        
    }

    public void destroy()
    {
        if(buffer != 0)
        {
            Source.unattach(this);
            AL10.alDeleteBuffers(buffer);
            buffer = 0;
        }
    }

    public boolean isCreated()
    {
        return buffer != 0;
    }

    public final int getBufferID()
    {
        if(!isCreated())
            throw new IllegalStateException("Not yet created");
        else
            return buffer;
    }

    public String toString()
    {
        return (new StringBuilder()).append("Buffer[").append(buffer).append(": ").append(url).append("]").toString();
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



    private static final int BUFSIZE = Integer.parseInt(System.getProperty("zombie.core.sound.Buffer.BUFSIZE", "196608"));
    private int buffer;
    private int format;

}
