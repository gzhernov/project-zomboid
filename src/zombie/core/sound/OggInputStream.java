// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   OggInputStream.java

package zombie.core.sound;

import com.jcraft.jogg.*;
import com.jcraft.jorbis.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class OggInputStream extends FilterInputStream
{

    public OggInputStream(InputStream input)
    {
        super(input);
        _pcm = new float[1][][];
        eos = false;
        syncState = new SyncState();
        streamState = new StreamState();
        page = new Page();
        packet = new Packet();
        info = new Info();
        comment = new Comment();
        dspState = new DspState();
        block = new Block(dspState);
        convsize = 8192;
        convbuffer = new byte[convsize];
        convbufferOff = 0;
        convbufferSize = 0;
        readDummy = new byte[1];
        try
        {
            initVorbis();
            _index = new int[info.channels];
        }
        catch(Exception e)
        {
            e.printStackTrace();
            eos = true;
        }
    }

    public int getFormat()
    {
        return info.channels != 1 ? 2 : 1;
    }

    public int getRate()
    {
        return info.rate;
    }

    public int read()
        throws IOException
    {
        int retVal = read(readDummy, 0, 1);
        return retVal != -1 ? readDummy[0] : -1;
    }

    public int read(byte b[], int off, int len)
        throws IOException
    {
        if(eos)
            return -1;
        int bytesRead = 0;
        do
        {
            if(eos || len <= 0)
                break;
            fillConvbuffer();
            if(!eos)
            {
                int bytesToCopy = Math.min(len, convbufferSize - convbufferOff);
                System.arraycopy(convbuffer, convbufferOff, b, off, bytesToCopy);
                convbufferOff += bytesToCopy;
                bytesRead += bytesToCopy;
                len -= bytesToCopy;
                off += bytesToCopy;
            }
        } while(true);
        return bytesRead;
    }

    public int read(ByteBuffer b, int off, int len)
        throws IOException
    {
        if(eos)
            return -1;
        b.position(off);
        int bytesRead = 0;
        do
        {
            if(eos || len <= 0)
                break;
            fillConvbuffer();
            if(!eos)
            {
                int bytesToCopy = Math.min(len, convbufferSize - convbufferOff);
                b.put(convbuffer, convbufferOff, bytesToCopy);
                convbufferOff += bytesToCopy;
                bytesRead += bytesToCopy;
                len -= bytesToCopy;
            }
        } while(true);
        return bytesRead;
    }

    private void fillConvbuffer()
        throws IOException
    {
        if(convbufferOff >= convbufferSize)
        {
            convbufferSize = lazyDecodePacket();
            convbufferOff = 0;
            if(convbufferSize == -1)
                eos = true;
        }
    }

    public int available()
        throws IOException
    {
        return eos ? 0 : 1;
    }

    public synchronized void reset()
        throws IOException
    {
    }

    public boolean markSupported()
    {
        return false;
    }

    public long skip(long n)
        throws IOException
    {
        int bytesRead = 0;
        do
        {
            if((long)bytesRead >= n)
                break;
            int res = read();
            if(res == -1)
                break;
            bytesRead++;
        } while(true);
        return (long)bytesRead;
    }

    private void initVorbis()
        throws Exception
    {
        int i;
        syncState.init();
        int index = syncState.buffer(4096);
        byte buffer[] = syncState.data;
        int bytes = in.read(buffer, index, 4096);
        syncState.wrote(bytes);
        if(syncState.pageout(page) != 1)
            if(bytes < 4096)
                return;
            else
                throw new Exception("Input does not appear to be an Ogg bitstream.");
        streamState.init(page.serialno());
        info.init();
        comment.init();
        if(streamState.pagein(page) < 0)
            throw new Exception("Error reading first page of Ogg bitstream data.");
        if(streamState.packetout(packet) != 1)
            throw new Exception("Error reading initial header packet.");
        if(info.synthesis_headerin(comment, packet) < 0)
            throw new Exception("This Ogg bitstream does not contain Vorbis audio data.");

        i = 0;
        /* 341 */     while (i < 2) {
        /* 342 */       while (i < 2)
        /*     */       {
        /* 344 */         int result = this.syncState.pageout(this.page);
        /* 345 */         if (result == 0)
        /*     */         {
        /*     */           break;
        /*     */         }
        /*     */ 
        /* 351 */         if (result == 1) {
        /* 352 */           this.streamState.pagein(this.page);
        /*     */ 
        /* 355 */           while (i < 2) {
        /* 356 */             result = this.streamState.packetout(this.packet);
        /* 357 */             if (result == 0)
        /*     */             {
        /*     */               break;
        /*     */             }
        /* 361 */             if (result == -1)
        /*     */             {
        /* 364 */               throw new Exception("Corrupt secondary header. Exiting.");
        /*     */             }
        /*     */ 
        /* 367 */             this.info.synthesis_headerin(this.comment, this.packet);
        /* 368 */             i++;
        /*     */           }
        /*     */         }
        /*     */ 
        /*     */       }
        /*     */ 
        /* 374 */       index = this.syncState.buffer(4096);
        /* 375 */       buffer = this.syncState.data;
        /* 376 */       bytes = this.in.read(buffer, index, 4096);
        /*     */ 
        /* 379 */       if (bytes < 0) {
        /* 380 */         bytes = 0;
        /*     */       }
        /*     */ 
        /* 383 */       if ((bytes == 0) && (i < 2)) {
        /* 384 */         throw new Exception("End of file before finding all Vorbis headers!");
        /*     */       }
        /*     */ 
        /* 387 */       this.syncState.wrote(bytes);
        /*     */     }
        
        convsize = 4096 / info.channels;
        dspState.synthesis_init(info);
        block.init(dspState);
        return;
    }

    private int decodePacket(Packet packet)
    {
        boolean bigEndian = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
        if(block.synthesis(packet) == 0)
            dspState.synthesis_blockin(block);
        int convOff = 0;
        int samples;
        while((samples = dspState.synthesis_pcmout(_pcm, _index)) > 0) 
        {
            float pcm[][] = _pcm[0];
            int bout = samples >= convsize ? convsize : samples;
            for(int i = 0; i < info.channels; i++)
            {
                int ptr = (i << 1) + convOff;
                int mono = _index[i];
                for(int j = 0; j < bout; j++)
                {
                    int val = (int)((double)pcm[i][mono + j] * 32767D);
                    val = Math.max(-32768, Math.min(32767, val));
                    val |= val >= 0 ? 0 : 0x8000;
                    convbuffer[ptr + 0] = (byte)(bigEndian ? val >>> 8 : val);
                    convbuffer[ptr + 1] = (byte)(bigEndian ? val : val >>> 8);
                    ptr += info.channels << 1;
                }

            }

            convOff += 2 * info.channels * bout;
            dspState.synthesis_read(bout);
        }
        return convOff;
    }

    private int lazyDecodePacket()
        throws IOException
    {
        int result = getNextPacket(packet);
        if(result == -1)
            return -1;
        else
            return decodePacket(packet);
    }

    private int getNextPacket(Packet packet)
        throws IOException
    {
        for(boolean fetchedPacket = false; !eos && !fetchedPacket;)
        {
            int result1 = streamState.packetout(packet);
            if(result1 == 0)
            {
                int result2 = 0;
                do
                {
                    if(eos || result2 != 0)
                        break;
                    result2 = syncState.pageout(page);
                    if(result2 == 0)
                        fetchData();
                } while(true);
                if(result2 == 0 && page.eos() != 0)
                    return -1;
                if(result2 == 0)
                {
                    fetchData();
                } else
                {
                    if(result2 == -1)
                    {
                        System.err.println("syncState.pageout(page) result == -1");
                        return -1;
                    }
                    streamState.pagein(page);
                }
            } else
            {
                if(result1 == -1)
                {
                    System.err.println("streamState.packetout(packet) result == -1");
                    return -1;
                }
                fetchedPacket = true;
            }
        }

        return 0;
    }

    private void fetchData()
        throws IOException
    {
        if(!eos)
        {
            int index = syncState.buffer(4096);
            if(index < 0)
            {
                eos = true;
                return;
            }
            int bytes = in.read(syncState.data, index, 4096);
            syncState.wrote(bytes);
            if(bytes == 0)
                eos = true;
        }
    }

    public String toString()
    {
        String s = "";
        s = (new StringBuilder()).append(s).append("version         ").append(info.version).append("\n").toString();
        s = (new StringBuilder()).append(s).append("channels        ").append(info.channels).append("\n").toString();
        s = (new StringBuilder()).append(s).append("rate (hz)       ").append(info.rate).toString();
        return s;
    }

    public static final int FORMAT_MONO16 = 1;
    public static final int FORMAT_STEREO16 = 2;
    private float _pcm[][][];
    private int _index[];
    private boolean eos;
    private SyncState syncState;
    private StreamState streamState;
    private Page page;
    private Packet packet;
    private Info info;
    private Comment comment;
    private DspState dspState;
    private Block block;
    private int convsize;
    private byte convbuffer[];
    private int convbufferOff;
    private int convbufferSize;
    private byte readDummy[];
}
