// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TextureID.java

package zombie.core.textures;

import gnu.trove.map.hash.THashMap;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Stack;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.glu.GLU;
import zombie.IndieGL;
import zombie.core.Core;
import zombie.core.opengl.OpenGLState;
import zombie.interfaces.IDestroyable;

// Referenced classes of package zombie.core.textures:
//            ImageData, AlphaColorIndex, Texture

public class TextureID
    implements IDestroyable, Serializable
{

    protected TextureID()
    {
        id = -1;
        alphaList = new ArrayList();
        referenceCount = 0;
    }

    public TextureID(int width, int height)
    {
        id = -1;
        alphaList = new ArrayList();
        referenceCount = 0;
        data = new ImageData(width, height);
        createTexture();
    }

    public TextureID(ImageData image)
    {
        id = -1;
        alphaList = new ArrayList();
        referenceCount = 0;
        data = image;
        createTexture();
    }

    public TextureID(String pcx, String palette)
    {
        id = -1;
        alphaList = new ArrayList();
        referenceCount = 0;
        data = new ImageData(pcx, palette);
        pathFileName = pcx;
        createTexture();
    }

    public TextureID(String pcx, int palette[])
    {
        id = -1;
        alphaList = new ArrayList();
        referenceCount = 0;
        data = new ImageData(pcx, palette);
        pathFileName = pcx;
        createTexture();
    }

    public TextureID(String path, int red, int green, int blue)
    {
        id = -1;
        alphaList = new ArrayList();
        referenceCount = 0;
        if(path.startsWith("/"))
            path = path.substring(1);
        int index;
        while((index = path.indexOf("\\")) != -1) 
            path = (new StringBuilder()).append(path.substring(0, index)).append('/').append(path.substring(index + 1)).toString();
        (data = new ImageData(path)).makeTransp((byte)red, (byte)green, (byte)blue);
        alphaList.add(new AlphaColorIndex(red, green, blue, 0));
        pathFileName = path;
        createTexture();
    }

    public TextureID(String path)
    {
        id = -1;
        alphaList = new ArrayList();
        referenceCount = 0;
        if(path.toLowerCase().contains(".pcx"))
            data = new ImageData(path, path);
        else
            data = new ImageData(path);
        pathFileName = path;
        createTexture();
    }

    public TextureID(TextureID source)
    {
        id = -1;
        alphaList = new ArrayList();
        referenceCount = 0;
        ByteBuffer buffer = source.getData();
        createTexture();
    }

    public boolean bind()
    {
        if(id != OpenGLState.lastTextureID)
        {
            IndieGL.End();
            if(id == -1)
                generateHwId();
            GL11.glBindTexture(3553, id);
            OpenGLState.lastlastTextureID = OpenGLState.lastTextureID;
            OpenGLState.lastTextureID = id;
            Texture.BindCount++;
            return true;
        } else
        {
            return false;
        }
    }

    public boolean bindalways()
    {
        IndieGL.End();
        if(id == -1)
            generateHwId();
        GL11.glBindTexture(3553, id);
        OpenGLState.lastlastTextureID = OpenGLState.lastTextureID;
        OpenGLState.lastTextureID = id;
        Texture.BindCount++;
        return true;
    }

    public void destroy()
    {
        if(id == -1)
        {
            return;
        } else
        {
            idBuffer.rewind();
            GL11.glDeleteTextures(idBuffer);
            id = -1;
            return;
        }
    }

    public void finalize()
    {
        if(id == -1)
        {
            return;
        } else
        {
            idBuffer.rewind();
            GL11.glDeleteTextures(idBuffer);
            id = -1;
            return;
        }
    }

    public void freeMemory()
    {
        data = null;
    }

    public ByteBuffer getData()
    {
        bind();
        ByteBuffer bb = ByteBuffer.allocateDirect(heightHW * widthHW * 4);
        GL11.glGetTexImage(3553, 0, 6408, 5121, bb);
        return bb;
    }

    public ImageData getImageData()
    {
        return data;
    }

    public String getPathFileName()
    {
        return pathFileName;
    }

    public boolean isDestroyed()
    {
        return id == -1;
    }

    public boolean isSolid()
    {
        return solid;
    }

    public void setData(ByteBuffer bdata)
    {
        if(bdata == null)
        {
            freeMemory();
            return;
        }
        bind();
        bdata.rewind();
        GL11.glTexSubImage2D(3553, 0, 0, 0, widthHW, heightHW, 6408, 5121, bdata);
        if(data != null)
        {
            ImageData _tmp = data;
            ImageData.data = bdata;
        }
        if(USE_MIPMAP)
            generateMipmap(bdata);
    }

    public void setImageData(ImageData data)
    {
        this.data = data;
    }

    private void createTexture()
    {
        width = data.width;
        height = data.height;
        widthHW = data.widthHW;
        heightHW = data.heightHW;
        solid = data.solid;
        generateHwId();
    }

    private void generateHwId()
    {
        idBuffer = BufferUtils.createIntBuffer(4);
        id = GL11.glGenTextures();
        GL11.glBindTexture(3553, OpenGLState.lastTextureID = id);
        if(UseFiltering)
        {
            GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
        } else
        {
            GL11.glTexParameteri(3553, 10241, 9728);
            GL11.glTexParameteri(3553, 10240, 9728);
        }
        if(USE_MIPMAP)
        {
            ImageData _tmp = data;
            generateMipmap(ImageData.data);
        } else
        {
            ImageData _tmp1 = data;
            GL11.glTexImage2D(3553, 0, 6408, widthHW, heightHW, 0, 6408, 5121, ImageData.data);
        }
        if(!FREE_MEMORY);
        TextureIDMap.put(Integer.valueOf(id), pathFileName);
    }

    private void generateMipmap(ByteBuffer data)
    {
        data.rewind();
        if(Core.getGLMajorVersion() >= 3)
        {
            GL11.glTexImage2D(3553, 0, 6408, widthHW, heightHW, 0, 6408, 5121, data);
            GL30.glGenerateMipmap(3553);
        } else
        if(Core.getGLMajorVersion() >= 2)
        {
            GL11.glTexParameteri(3553, 33169, 1);
            GL11.glTexImage2D(3553, 0, 6408, widthHW, heightHW, 0, 6408, 5121, data);
        } else
        {
            GLU.gluBuild2DMipmaps(3553, 6408, widthHW, heightHW, 6408, 5121, data);
        }
    }

    private void readObject(ObjectInputStream s)
        throws IOException, ClassNotFoundException
    {
        boolean fromFile = s.readBoolean();
        if(!fromFile)
        {
            data = (ImageData)s.readObject();
            s.defaultReadObject();
        } else
        {
            data = new ImageData(pathFileName);
            s.defaultReadObject();
        }
        createTexture();
    }

    private void writeObject(ObjectOutputStream s)
        throws IOException
    {
        boolean fromFile = pathFileName == null;
        if(!fromFile)
        {
            if(data == null)
                data = new ImageData(this, getData());
            s.writeBoolean(false);
            s.writeObject(data);
            s.defaultWriteObject();
        } else
        {
            s.writeBoolean(true);
            s.defaultWriteObject();
        }
    }

    private static final long serialVersionUID = 0x3d30d0c5c73dc25aL;
    public static boolean USE_MIPMAP = false;
    public static boolean FREE_MEMORY = true;
    public static THashMap TextureIDMap = new THashMap();
    public static Stack TextureIDStack = new Stack();
    static boolean bAlt = false;
    protected transient ImageData data;
    protected int height;
    protected int heightHW;
    protected transient int id;
    protected transient IntBuffer idBuffer;
    protected String pathFileName;
    protected boolean solid;
    protected int width;
    protected int widthHW;
    ArrayList alphaList;
    int referenceCount;
    public static boolean UseFiltering = false;

}
