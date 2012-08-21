// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SliceY.java

package zombie.iso;

import java.io.*;
import java.nio.ByteBuffer;
import zombie.GameProfiler;
import zombie.GameWindow;

// Referenced classes of package zombie.iso:
//            IsoGridSquare, IsoCell, IsoWorld

public class SliceY
{

    public SliceY(IsoCell cell, int width, int tall, int y)
    {
        bSaveDirty = true;
        this.y = y;
        this.cell = cell;
        this.width = width;
        this.tall = IsoCell.getMaxHeight();
        Squares = new IsoGridSquare[width][tall];
    }

    void renderFloor(int minX, int maxX, int z, int y)
    {
        for(int x = minX; x < maxX; x++)
            if(Squares[x][z] != null)
            {
                GameProfiler.instance.Start("RenderFloor");
                Squares[x][z].renderFloor(x - minX, y);
                GameProfiler.instance.End("RenderFloor");
            }

    }

    void renderMinusFloor(int minX, int maxX, int z, int minY, int maxZ)
    {
        for(int x = minX; x < maxX; x++)
        {
            if(Squares[x][z] == null)
                continue;
            Squares[x][z].renderMinusFloor(minY, x - minX, maxZ);
            if(Squares[x][z] != null)
                Squares[x][z].renderCharacters(maxZ);
        }

    }

    void save(ObjectOutputStream outputObj)
        throws IOException
    {
        if(SliceBuffer == null)
            SliceBuffer = ByteBuffer.allocate(0x989680);
        SliceBuffer.rewind();
        File outFile = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).append(IsoWorld.instance.playerCell).append("map_").append(new Integer(y)).append(".bin").toString());
        FileOutputStream outStream = new FileOutputStream(outFile);
        BufferedOutputStream output = new BufferedOutputStream(outStream);
        bSaveDirty = false;
        SliceBuffer.putInt(tall);
        for(int n = 0; n < width; n++)
        {
            for(int m = 0; m < IsoCell.MaxHeight; m++)
                if(Squares[n][m] == null)
                {
                    SliceBuffer.put((byte)0);
                } else
                {
                    SliceBuffer.put((byte)1);
                    Squares[n][m].save(SliceBuffer, null);
                    Squares[n][m].savebytestream(SliceBuffer);
                }

        }

        output.write(SliceBuffer.array(), 0, SliceBuffer.position());
        output.flush();
        outStream.close();
        int df = 0;
    }

    void load(BufferedInputStream input2, Object object)
        throws IOException
    {
        if(SliceBuffer == null)
            SliceBuffer = ByteBuffer.allocate(0x989680);
        SliceBuffer.rewind();
        input2.read(SliceBuffer.array());
        ByteBuffer b = SliceBuffer;
        tall = b.getInt();
        for(int n = 0; n < width; n++)
        {
            for(int m = 0; m < IsoCell.MaxHeight; m++)
            {
                int x = n;
                int z = m;
                boolean r = b.get() != 0;
                if(r)
                {
                    Squares[n][m] = new IsoGridSquare(cell, this, n, y, m);
                    Squares[n][m].load(b);
                    Squares[n][m].loadbytestream(b);
                }
            }

        }

    }

    IsoCell cell;
    public IsoGridSquare Squares[][];
    int tall;
    int width;
    int y;
    boolean bSaveDirty;
    static ByteBuffer SliceBuffer;
}
