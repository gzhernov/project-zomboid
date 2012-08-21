// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   OggInputStream.java

package zombie.openal;

import com.jcraft.jogg.*;
import com.jcraft.jorbis.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.lwjgl.BufferUtils;

// Referenced classes of package zombie.openal:
//            AudioInputStream

public class OggInputStream extends InputStream
    implements AudioInputStream
{

    public OggInputStream(InputStream input)
        throws IOException
    {
        bigEndian = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
        bytes = 0;
        endOfBitStream = true;
        inited = false;
        comment = new Comment();
        convsize = 16384;
        convbuffer = new byte[convsize];
        dspState = new DspState();
        oggInfo = new Info();
        packet = new Packet();
        page = new Page();
        pcmBuffer = BufferUtils.createByteBuffer(0x1f4000);
        streamState = new StreamState();
        syncState = new SyncState();
        vorbisBlock = new Block(dspState);
        this.input = input;
        total = input.available();
        init();
    }

    public boolean atEnd()
    {
        return endOfStream && readIndex >= pcmBuffer.position();
    }

    public int available()
    {
        return endOfStream ? 0 : 1;
    }

    public void close()
        throws IOException
    {
    }

    public int getChannels()
    {
        return oggInfo.channels;
    }

    public int getLength()
    {
        return total;
    }

    public int getRate()
    {
        return oggInfo.rate;
    }

    public int read()
        throws IOException
    {
        if(readIndex >= pcmBuffer.position())
        {
            pcmBuffer.clear();
            readPCM();
            readIndex = 0;
        }
        if(readIndex >= pcmBuffer.position())
            return -1;
        int value = pcmBuffer.get(readIndex);
        if(value < 0)
            value = 256 + value;
        readIndex++;
        return value;
    }

    public int read(byte b[])
        throws IOException
    {
        return read(b, 0, b.length);
    }

    
    
    
    public int read(byte b[], int off, int len)
        throws IOException
    {
    	/* 199 */     for (int i = 0; i < len; i++)
    	/*     */     {
    	/*     */       try
    	/*     */       {
    	/* 204 */         int value = read();
    	/*     */ 
    	/* 206 */         if (value >= 0)
    	/*     */         {
    	/* 208 */           b[i] = (byte)value;
    	/*     */         }
    	/*     */         else
    	/*     */         {
    	/* 213 */           if (i == 0)
    	/*     */           {
    	/* 215 */             return -1;
    	/*     */           }
    	/*     */ 
    	/* 219 */           return i;
    	/*     */         }
    	/*     */ 
    	/*     */       }
    	/*     */       catch (IOException e)
    	/*     */       {
    	/* 227 */         return i;
    	/*     */       }
    	/*     */     }
    	/*     */ 
    	/* 231 */     return len;
    }
    
    
    
    

    private boolean getPageAndPacket()
    {
        int i;
        int index = syncState.buffer(4096);
        buffer = syncState.data;
        if(buffer == null)
        {
            endOfStream = true;
            return false;
        }
        try
        {
            bytes = input.read(buffer, index, 4096);
        }
        catch(Exception e)
        {
            endOfStream = true;
            return false;
        }
        syncState.wrote(bytes);
        if(syncState.pageout(page) != 1)
            if(bytes < 4096)
            {
                return false;
            } else
            {
                endOfStream = true;
                return false;
            }
        streamState.init(page.serialno());
        oggInfo.init();
        comment.init();
        if(streamState.pagein(page) < 0)
        {
            endOfStream = true;
            return false;
        }
        if(streamState.packetout(packet) != 1)
        {
            endOfStream = true;
            return false;
        }
        if(oggInfo.synthesis_headerin(comment, packet) < 0)
        {
            endOfStream = true;
            return false;
        }
       
        
        /* 344 */     i = 0;
        /*     */ 
        /* 346 */     while (i < 2)
        /*     */     {
        /* 349 */       while (i < 2)
        /*     */       {
        /* 352 */         int result = this.syncState.pageout(this.page);
        /*     */ 
        /* 354 */         if (result == 0)
        /*     */         {
        /*     */           break;
        /*     */         }
        /*     */ 
        /* 360 */         if (result == 1)
        /*     */         {
        /* 362 */           this.streamState.pagein(this.page);
        /*     */ 
        /* 366 */           while (i < 2)
        /*     */           {
        /* 368 */             result = this.streamState.packetout(this.packet);
        /*     */ 
        /* 370 */             if (result == 0) {
        /*     */               break;
        /*     */             }
        /* 373 */             if (result == -1)
        /*     */             {
        /* 379 */               this.endOfStream = true;
        /*     */ 
        /* 381 */               return false;
        /*     */             }
        /*     */ 
        /* 384 */             this.oggInfo.synthesis_headerin(this.comment, this.packet);
        /* 385 */             i++;
        /*     */           }
        /*     */         }
        /*     */ 
        /*     */       }
        /*     */ 
        /* 391 */       index = this.syncState.buffer(4096);
        /* 392 */       this.buffer = this.syncState.data;
        /*     */       try
        /*     */       {
        /* 396 */         this.bytes = this.input.read(this.buffer, index, 4096);
        /*     */       }
        /*     */       catch (Exception e)
        /*     */       {
        /* 403 */         this.endOfStream = true;
        /*     */ 
        /* 405 */         return false;
        /*     */       }
        /*     */ 
        /* 408 */       if ((this.bytes == 0) && (i < 2))
        /*     */       {
        /* 412 */         this.endOfStream = true;
        /*     */ 
        /* 414 */         return false;
        /*     */       }
        /*     */ 
        /* 417 */       this.syncState.wrote(this.bytes);
        /*     */     }
        /*     */ 
        /* 420 */     this.convsize = (4096 / this.oggInfo.channels);
        /*     */ 
        /* 424 */     this.dspState.synthesis_init(this.oggInfo);
        /* 425 */     this.vorbisBlock.init(this.dspState);
        /*     */ 
        /* 432 */     return true;
    }

    private void init()
        throws IOException
    {
        initVorbis();
        readPCM();
    }

    private void initVorbis()
    {
        syncState.init();
    }

    private void readPCM()
        throws IOException
    {
        boolean wrote = false;
        do
        {
            if(endOfBitStream)
            {
                if(!getPageAndPacket())
                    break;
                endOfBitStream = false;
            }
            if(!inited)
            {
                inited = true;
                return;
            }
            float _pcm[][][] = new float[1][][];
            int _index[] = new int[oggInfo.channels];
            do
            {
                if(endOfBitStream)
                    break;
                do
                {
                    if(endOfBitStream)
                        break;
                    int result = syncState.pageout(page);
                    if(result == 0)
                        break;
                    if(result != -1)
                    {
                        streamState.pagein(page);
                        do
                        {
                            result = streamState.packetout(packet);
                            if(result == 0)
                                break;
                            if(result != -1)
                            {
                                if(vorbisBlock.synthesis(packet) == 0)
                                    dspState.synthesis_blockin(vorbisBlock);
                                int samples;
                                while((samples = dspState.synthesis_pcmout(_pcm, _index)) > 0) 
                                {
                                    float pcm[][] = _pcm[0];
                                    int bout = samples >= convsize ? convsize : samples;
                                    for(int i = 0; i < oggInfo.channels; i++)
                                    {
                                        int ptr = i * 2;
                                        int mono = _index[i];
                                        for(int j = 0; j < bout; j++)
                                        {
                                            int val = (int)((double)pcm[i][mono + j] * 32767D);
                                            if(val > 32767)
                                                val = 32767;
                                            if(val < -32768)
                                                val = -32768;
                                            if(val < 0)
                                                val |= 0x8000;
                                            if(bigEndian)
                                            {
                                                convbuffer[ptr] = (byte)(val >>> 8);
                                                convbuffer[ptr + 1] = (byte)val;
                                            } else
                                            {
                                                convbuffer[ptr] = (byte)val;
                                                convbuffer[ptr + 1] = (byte)(val >>> 8);
                                            }
                                            ptr += 2 * oggInfo.channels;
                                        }

                                    }

                                    int bytesToWrite = 2 * oggInfo.channels * bout;
                                    if(bytesToWrite < pcmBuffer.remaining())
                                        pcmBuffer.put(convbuffer, 0, bytesToWrite);
                                    wrote = true;
                                    dspState.synthesis_read(bout);
                                }
                            }
                        } while(true);
                        if(page.eos() != 0)
                            endOfBitStream = true;
                        if(!endOfBitStream && wrote)
                            return;
                    }
                } while(true);
                if(!endOfBitStream)
                {
                    bytes = 0;
                    int index = syncState.buffer(4096);
                    if(index >= 0)
                    {
                        buffer = syncState.data;
                        try
                        {
                            bytes = input.read(buffer, index, 4096);
                        }
                        catch(Exception e)
                        {
                            endOfStream = true;
                            return;
                        }
                    } else
                    {
                        bytes = 0;
                    }
                    syncState.wrote(bytes);
                    if(bytes == 0)
                        endOfBitStream = true;
                }
            } while(true);
            streamState.clear();
            vorbisBlock.clear();
            dspState.clear();
            oggInfo.clear();
        } while(true);
        syncState.clear();
        endOfStream = true;
    }

    boolean bigEndian;
    byte buffer[];
    int bytes;
    boolean endOfBitStream;
    boolean inited;
    private Comment comment;
    private int convsize;
    private byte convbuffer[];
    private DspState dspState;
    private boolean endOfStream;
    private InputStream input;
    private Info oggInfo;
    private Packet packet;
    private Page page;
    private ByteBuffer pcmBuffer;
    private int readIndex;
    private StreamState streamState;
    private SyncState syncState;
    private int total;
    private Block vorbisBlock;
}
