// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ImageData.java

package zombie.core.textures;

import java.awt.Graphics;
import java.awt.image.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import zombie.core.Core;
import zombie.core.utils.ImageUtils;

// Referenced classes of package zombie.core.textures:
//            Pcx, TextureID, Texture

public class ImageData
    implements Serializable
{

    public ImageData(TextureID texture, ByteBuffer bb)
    {
        solid = true;
        width = texture.width;
        widthHW = texture.widthHW;
        height = texture.height;
        heightHW = texture.heightHW;
        solid = texture.solid;
    }

    public void Load(BufferedImage image)
    {
        byte imgdata[] = (byte[])(byte[])image.getRaster().getDataElements(0, 0, width, height, null);
        int x = 0;
        int y = 0;
        if(4 * widthHW * heightHW == imgdata.length)
        {
            for(int index = 0; index < imgdata.length; index++)
            {
                data.put(imgdata[index]).put(imgdata[++index]).put(imgdata[++index]).put(imgdata[++index]);
                if(++x == width)
                {
                    data.position(widthHW * 4 * ++y);
                    x = 0;
                }
            }

        } else
        {
            for(int index = 0; index < imgdata.length; index++)
            {
                data.put(imgdata[index]).put(imgdata[++index]).put(imgdata[++index]).put((byte)-1);
                if(++x == width)
                {
                    data.position(widthHW * 4 * ++y);
                    x = 0;
                }
            }

        }
        data.rewind();
    }

    public ImageData(BufferedImage image)
    {
        solid = true;
        width = image.getWidth();
        height = image.getHeight();
        widthHW = ImageUtils.getNextPowerOfTwoHW(width);
        heightHW = ImageUtils.getNextPowerOfTwoHW(height);
        if(data == null)
            data = ByteBuffer.allocateDirect(0x400000);
        data.clear();
        byte imgdata[] = (byte[])(byte[])image.getRaster().getDataElements(0, 0, width, height, null);
        int x = 0;
        int y = 0;
        if(4 * widthHW * heightHW == imgdata.length)
        {
            for(int index = 0; index < imgdata.length; index++)
            {
                data.put(imgdata[index]).put(imgdata[++index]).put(imgdata[++index]).put(imgdata[++index]);
                if(++x == width)
                {
                    data.position(widthHW * 4 * ++y);
                    x = 0;
                }
            }

        } else
        {
            for(int index = 0; index < imgdata.length; index++)
            {
                data.put(imgdata[index]).put(imgdata[++index]).put(imgdata[++index]).put((byte)-1);
                if(++x == width)
                {
                    data.position(widthHW * 4 * ++y);
                    x = 0;
                }
            }

        }
        data.rewind();
    }

    public ImageData(String path)
    {
        solid = true;
        if(path.contains(".txt"))
        {
            String descPath = path;
            path = path.replace(".txt", ".png");
        }
        InputStream is = null;
        if(path.startsWith("/"))
            path = path.substring(1);
        int index;
        while((index = path.indexOf("\\")) != -1) 
            path = (new StringBuilder()).append(path.substring(0, index)).append('/').append(path.substring(index + 1)).toString();
        String trypath = path.substring(path.indexOf("/") + 1);
        if(is == null)
            try
            {
                is = new FileInputStream((new StringBuilder()).append(Core.storyDirectory).append(trypath).toString());
            }
            catch(FileNotFoundException ex) { }
        if(is == null)
        {
            try
            {
                is = new FileInputStream(path);
            }
            catch(FileNotFoundException ex) { }
            if(is == null)
            {
                width = height = -1;
                data = null;
                if(Texture.WarnFailFindTexture && Core.bDebug)
                    System.out.println((new StringBuilder()).append("Texture failed to load: ").append(path).toString());
                return;
            }
        }
        
        assert (is != null);
        
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(is);
        }
        catch(IOException ex)
        {
            Logger.getLogger(ImageData.class.getName()).log(Level.SEVERE, null, ex);
        }
        width = image.getWidth();
        height = image.getHeight();
        widthHW = ImageUtils.getNextPowerOfTwoHW(width);
        heightHW = ImageUtils.getNextPowerOfTwoHW(height);
        if(data == null)
            data = ByteBuffer.allocateDirect(0x400000);
        data.clear();
        data.rewind();
        int imgdata[] = image.getRaster().getPixels(0, 0, width, height, (int[])null);
        int x = 0;
        int y = 0;
        solid = !image.getColorModel().hasAlpha();
        if(4 * width * height == imgdata.length)
        {
            for(index = 0; index < imgdata.length; index++)
            {
                data.put((byte)imgdata[index]).put((byte)imgdata[++index]).put((byte)imgdata[++index]).put((byte)imgdata[++index]);
                if(++x == width)
                {
                    data.position(widthHW * 4 * ++y);
                    x = 0;
                }
            }

        } else
        if(imgdata.length % 3 == 0 && solid)
        {
            for(index = 0; index < imgdata.length; index++)
            {
                data.put((byte)imgdata[index]).put((byte)imgdata[++index]).put((byte)imgdata[++index]).put((byte)-1);
                if(++x == width)
                {
                    data.position(widthHW * 4 * ++y);
                    x = 0;
                }
            }

        } else
        {
            BufferedImage bimage = new BufferedImage(width, height, 6);
            Graphics g = bimage.createGraphics();
            g.drawImage(image, 0, 0, null);
            imgdata = image.getRaster().getPixels(0, 0, width, height, (int[])null);
            for(index = 0; index < imgdata.length; index++)
            {
                data.put((byte)imgdata[index]).put((byte)imgdata[++index]).put((byte)imgdata[++index]).put((byte)imgdata[++index]);
                if(++x == width)
                {
                    data.position(widthHW * 4 * ++y);
                    x = 0;
                }
            }

        }
        data.rewind();
    }

    public ImageData(int width, int height)
    {
        solid = true;
        this.width = width;
        this.height = height;
        widthHW = ImageUtils.getNextPowerOfTwoHW(width);
        heightHW = ImageUtils.getNextPowerOfTwoHW(height);
        if(data == null)
            data = ByteBuffer.allocateDirect(0x400000);
    }

    ImageData(String pcx, String palette)
    {
        solid = true;
        Pcx image = new Pcx(pcx, palette);
        width = image.imageWidth;
        height = image.imageHeight;
        widthHW = ImageUtils.getNextPowerOfTwoHW(width);
        heightHW = ImageUtils.getNextPowerOfTwoHW(height);
        if(data == null)
            data = ByteBuffer.allocateDirect(0x400000);
        setData(image);
        makeTransp((byte)image.palette[762], (byte)image.palette[763], (byte)image.palette[764], (byte)0);
    }

    ImageData(String pcx, int palette[])
    {
        solid = true;
        Pcx image = new Pcx(pcx, palette);
        width = image.imageWidth;
        height = image.imageHeight;
        widthHW = ImageUtils.getNextPowerOfTwoHW(width);
        heightHW = ImageUtils.getNextPowerOfTwoHW(height);
        if(data == null)
            data = ByteBuffer.allocateDirect(0x400000);
        setData(image);
        makeTransp((byte)image.palette[762], (byte)image.palette[763], (byte)image.palette[764], (byte)0);
    }

    public ByteBuffer getData()
    {
        data.rewind();
        return data;
    }

    public void makeTransp(byte red, byte green, byte blue)
    {
        makeTransp(red, green, blue, (byte)0);
    }

    public void makeTransp(byte red, byte green, byte blue, byte alpha)
    {
        solid = false;
        data.rewind();
        int step = widthHW * 4;
        int y = 0;
        do
        {
            if(y >= heightHW)
                break;
            int position = data.position();
            int x = 0;
            do
            {
                if(x >= widthHW)
                    break;
                int r = data.get();
                int g = data.get();
                int b = data.get();
                if(r == red && g == green && b == blue)
                    data.put(alpha);
                else
                    data.get();
                if(x == width)
                {
                    data.position(position + step);
                    break;
                }
                x++;
            } while(true);
            if(y == height)
                break;
            y++;
        } while(true);
        data.rewind();
    }

    public void setData(BufferedImage image)
    {
        if(image != null)
            setData(image.getData());
    }

    public void setData(Raster rasterData)
    {
        if(rasterData == null)
        {
            (new Exception()).printStackTrace();
            return;
        }
        width = rasterData.getWidth();
        height = rasterData.getHeight();
        if(width > widthHW || height > heightHW)
        {
            (new Exception()).printStackTrace();
            return;
        }
        int pixelData[] = rasterData.getPixels(0, 0, width, height, (int[])null);
        data.rewind();
        int counter = 0;
        int position = data.position();
        int step = widthHW * 4;
        for(int i = 0; i < pixelData.length; i++)
        {
            if(++counter > width)
            {
                data.position(position + step);
                position = data.position();
                counter = 1;
            }
            data.put((byte)pixelData[i]);
            data.put((byte)pixelData[++i]);
            data.put((byte)pixelData[++i]);
            data.put((byte)pixelData[++i]);
        }

        data.rewind();
        solid = false;
    }

    private void readObject(ObjectInputStream s)
        throws IOException, ClassNotFoundException
    {
        s.defaultReadObject();
        if(data == null)
            data = ByteBuffer.allocateDirect(0x400000);
        data.rewind();
        for(int i = 0; i < widthHW * heightHW; i++)
            data.put(s.readByte()).put(s.readByte()).put(s.readByte()).put(s.readByte());

        data.rewind();
    }

    private void setData(Pcx image)
    {
        width = image.imageWidth;
        height = image.imageHeight;
        if(width > widthHW || height > heightHW)
        {
            (new Exception()).printStackTrace();
            return;
        }
        data.rewind();
        int counter = 0;
        int position = data.position();
        int step = widthHW * 4;
        for(int i = 0; i < heightHW * widthHW * 3; i++)
        {
            if(++counter > width)
            {
                position = data.position();
                counter = 1;
            }
            data.put(image.imageData[i]);
            data.put(image.imageData[++i]);
            data.put(image.imageData[++i]);
            data.put((byte)-1);
        }

        data.rewind();
        solid = false;
    }

    private void writeObject(ObjectOutputStream s)
        throws IOException
    {
        s.defaultWriteObject();
        data.rewind();
        for(int i = 0; i < widthHW * heightHW; i++)
        {
            s.writeByte(data.get());
            s.writeByte(data.get());
            s.writeByte(data.get());
            s.writeByte(data.get());
        }

    }

    private static final long serialVersionUID = 0x927509b6c6c3622cL;
    public static ByteBuffer data;
    protected int height;
    protected int heightHW;
    protected boolean solid;
    protected int width;
    protected int widthHW;
   

}
