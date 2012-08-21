// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Mask.java

package zombie.core.textures;

import java.io.*;
import java.nio.ByteBuffer;
import zombie.interfaces.ITexture;

// Referenced classes of package zombie.core.textures:
//            Texture

public class Mask
    implements Serializable
{

    protected Mask()
    {
        isTrans = false;
    }

    public Mask(int width, int height)
    {
        isTrans = false;
        this.width = width;
        this.height = height;
        mask = new boolean[width][height];
        full();
    }

    public Mask(Texture from, Texture texture, int x, int y, int width, int height)
    {
        isTrans = false;
        if(from.getMask() == null)
            return;
        width = texture.getWidth();
        height = texture.getHeight();
        int t = texture.getWidthHW();
        int xs = (int)(texture.getXStart() * (float)t);
        int xe = (int)(texture.getXEnd() * (float)t);
        int ys = (int)(texture.getYStart() * (float)(t = texture.getHeightHW()));
        int ye = (int)(texture.getYEnd() * (float)t);
        texture.setMask(this);
        mask = new boolean[width][height];
        for(int sx = x; sx < x + width; sx++)
        {
            for(int sy = y; sy < y + height; sy++)
            {
                mask[sx - x][sy - y] = from.getMask().mask[sx][sy];
                if(from.getMask().trans[sx][sy])
                    isTrans = true;
            }

        }

    }

    public Mask(ITexture texture)
    {
        isTrans = false;
        width = texture.getWidth();
        height = texture.getHeight();
        int t = texture.getWidthHW();
        int xs = (int)(texture.getXStart() * (float)t);
        int xe = (int)(texture.getXEnd() * (float)t);
        int ys = (int)(texture.getYStart() * (float)(t = texture.getHeightHW()));
        int ye = (int)(texture.getYEnd() * (float)t);
        texture.setMask(this);
        mask = new boolean[width][height];
        trans = new boolean[width][height];
        ByteBuffer temp = texture.getData();
        temp.rewind();
label0:
        for(int y = 0; y < texture.getHeightHW(); y++)
        {
            int x = 0;
            do
            {
                if(x >= texture.getWidthHW())
                    continue label0;
                temp.get();
                temp.get();
                temp.get();
                int alpha = temp.get();
                if(x >= xs && x < xe && y >= ys && y < ye)
                    if(alpha == 0)
                    {
                        full = mask[x - xs][y - ys] = false;
                    } else
                    {
                        int n;
                        if(alpha < 127)
                            trans[x - xs][y - ys] = true;
                        else
                            n = 0;
                        mask[x - xs][y - ys] = true;
                    }
                if(y >= ye)
                    continue label0;
                x++;
            } while(true);
        }

    }

    public Mask(Mask obj)
    {
        isTrans = false;
        width = obj.width;
        height = obj.height;
        full = obj.full;
        mask = (boolean[][])obj.mask.clone();
    }

    public Object clone()
    {
        return new Mask(this);
    }

    public void full()
    {
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
                mask[x][y] = true;

        }

        full = true;
    }

    public void set(int x, int y, boolean val)
    {
        if(!(mask[x][y] = val) && full)
            full = false;
    }

    private void readObject(ObjectInputStream s)
        throws IOException, ClassNotFoundException
    {
        width = s.readInt();
        height = s.readInt();
        full = s.readBoolean();
        if(s.readBoolean())
            mask = (boolean[][])(boolean[][])s.readObject();
    }

    private void writeObject(ObjectOutputStream s)
        throws IOException
    {
        s.writeInt(width);
        s.writeInt(height);
        s.writeBoolean(full);
        if(mask != null)
        {
            s.writeBoolean(true);
            s.writeObject(mask);
        } else
        {
            s.writeBoolean(false);
        }
    }

    private static final long serialVersionUID = 0xb12f689d2672c29aL;
    public boolean full;
    public int height;
    public boolean mask[][];
    public boolean trans[][];
    public int width;
    public boolean isTrans;
}
