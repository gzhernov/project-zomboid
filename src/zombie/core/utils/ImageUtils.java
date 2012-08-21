// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ImageUtils.java

package zombie.core.utils;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import zombie.core.Core;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureID;

public class ImageUtils
{

    private ImageUtils()
    {
    }

    public static void depureTexture(Texture texture, float limit)
    {
        ByteBuffer data = texture.getData();
        data.rewind();
        int ilimit = (int)(limit * 255F);
        long tot = texture.getWidthHW() * texture.getHeightHW();
        for(int i = 0; (long)i < tot; i++)
        {
            data.mark();
            data.get();
            data.get();
            data.get();
            byte alpha = data.get();
            int ialpha;
            if(alpha < 0)
                ialpha = 256 + alpha;
            else
                ialpha = alpha;
            if(ialpha < ilimit)
            {
                data.reset();
                data.put((byte)0);
                data.put((byte)0);
                data.put((byte)0);
                data.put((byte)0);
            }
        }

        texture.setData(data);
    }

    public static int getNextPowerOfTwo(int fold)
    {
        int pow;
        for(pow = 2; pow < fold; pow += pow);
        return pow;
    }

    public static int getNextPowerOfTwoHW(int fold)
    {
        int pow;
        for(pow = 2; pow < fold; pow += pow);
        return pow;
    }

    public static Texture getScreenShot()
    {
        Texture texture = new Texture(Core.getInstance().getScreenWidth(), Core.getInstance().getScreenHeight());
        IntBuffer point = BufferUtils.createIntBuffer(4);
        texture.bind();
        point.rewind();
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glCopyTexImage2D(3553, 0, 6408, 0, 0, texture.getWidthHW(), texture.getHeightHW(), 0);
        return texture;
    }

    public static ByteBuffer makeTransp(ByteBuffer data, int red, int green, int blue, int widthHW, int heightHW)
    {
        return makeTransp(data, red, green, blue, 0, widthHW, heightHW);
    }

    public static ByteBuffer makeTransp(ByteBuffer data, int red, int green, int blue, int alpha, int widthHW, int heightHW)
    {
        data.rewind();
        for(int y = 0; y < heightHW; y++)
        {
            for(int x = 0; x < widthHW; x++)
            {
                byte r = data.get();
                byte g = data.get();
                byte b = data.get();
                if(r == (byte)red && g == (byte)green && b == (byte)blue)
                    data.put((byte)alpha);
                else
                    data.get();
            }

        }

        data.rewind();
        return data;
    }

    public static void saveBmpImage(Texture texture, String path)
    {
        saveImage(texture, path, "bmp");
    }

    public static void saveImage(Texture texture, String path, String format)
    {
        BufferedImage image = new BufferedImage(texture.getWidth(), texture.getHeight(), 1);
        WritableRaster raster = image.getRaster();
        ByteBuffer bb = texture.getData();
        bb.rewind();
        for(int y = 0; y < texture.getHeightHW() && y < texture.getHeight(); y++)
        {
            for(int x = 0; x < texture.getWidthHW(); x++)
                if(x >= texture.getWidth())
                {
                    bb.get();
                    bb.get();
                    bb.get();
                    bb.get();
                } else
                {
                    raster.setPixel(x, texture.getHeight() - 1 - y, new int[] {
                        bb.get(), bb.get(), bb.get()
                    });
                    bb.get();
                }

        }

        try
        {
            ImageIO.write(image, "png", new File(path));
        }
        catch(IOException e) { }
    }

    public static void saveJpgImage(Texture texture, String path)
    {
        saveImage(texture, path, "jpg");
    }

    public static void savePngImage(Texture texture, String path)
    {
        saveImage(texture, path, "png");
    }

    public static void setSmoothImageState(boolean value)
    {
        if(value)
        {
            if(TextureID.USE_MIPMAP)
                GL11.glTexParameteri(3553, 10241, 9987);
            else
                GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
        } else
        {
            if(TextureID.USE_MIPMAP)
                GL11.glTexParameteri(3553, 10241, 9984);
            else
                GL11.glTexParameteri(3553, 10241, 9728);
            GL11.glTexParameteri(3553, 10240, 9728);
        }
    }

    public static boolean USE_MIPMAP = true;

}
