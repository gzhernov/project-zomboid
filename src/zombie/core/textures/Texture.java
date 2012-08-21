// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Texture.java

package zombie.core.textures;

import gnu.trove.map.hash.THashMap;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import zombie.*;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.bucket.Bucket;
import zombie.core.bucket.BucketManager;
import zombie.core.opengl.OpenGLState;
import zombie.core.utils.ImageUtils;
import zombie.interfaces.IDestroyable;
import zombie.interfaces.ITexture;

// Referenced classes of package zombie.core.textures:
//            TextureID, ImageData, TextureNotFoundException, Mask, 
//            AlphaColorIndex, TextureNameAlreadyInUseException, TexturePackPage

public class Texture
    implements IDestroyable, ITexture, Serializable
{

    public Texture(TextureID data, String name)
    {
        flip = false;
        offsetX = 0.0F;
        offsetY = 0.0F;
        xEnd = 1.0F;
        yEnd = 1.0F;
        xStart = 0.0F;
        yStart = 0.0F;
        destroyed = false;
        dataid = data;
        dataid.referenceCount++;
        solid = dataid.solid;
        width = data.width;
        height = data.height;
        xEnd = (float)width / (float)data.widthHW;
        yEnd = (float)height / (float)data.heightHW;
        this.name = name;
    }

    public Texture(BufferedImage bImage, String name)
    {
        this(new TextureID(new ImageData(bImage)), name);
    }

    public void Load(BufferedImage image)
    {
        if(dataid.data == null)
            dataid.data = new ImageData(width, height);
        dataid.data.Load(image);
    }

    public Texture(String file)
    {
        this(new TextureID(file), file);
        setUseAlphaChannel(true);
    }

    public Texture(String name, String palette)
    {
        this(new TextureID(name, palette), name);
        setUseAlphaChannel(true);
    }

    public Texture(String name, int palette[])
    {
        this(new TextureID(name, palette), name);
        int n;
        if(name.contains("drag"))
            n = 0;
        setUseAlphaChannel(true);
    }

    public Texture(String file, boolean useAlphaChannel)
    {
        this(new TextureID(file), file);
        setUseAlphaChannel(useAlphaChannel);
    }

    public Texture(int width, int height, String name)
    {
        this(new TextureID(width, height), name);
    }

    public Texture(int width, int height)
    {
        this(new TextureID(width, height), ((String) (null)));
    }

    public Texture(String file, int red, int green, int blue)
    {
        this(new TextureID(file, red, green, blue), file);
    }

    public Texture(Texture t)
    {
        this(t.dataid, (new StringBuilder()).append(t.name).append("(copy)").toString());
        width = t.width;
        height = t.height;
        name = t.name;
        xStart = t.xStart;
        yStart = t.yStart;
        xEnd = t.xEnd;
        yEnd = t.yEnd;
        solid = t.solid;
    }

    public Texture()
    {
        flip = false;
        offsetX = 0.0F;
        offsetY = 0.0F;
        xEnd = 1.0F;
        yEnd = 1.0F;
        xStart = 0.0F;
        yStart = 0.0F;
        destroyed = false;
    }

    public static void bindNone()
    {
        IndieGL.glDisable(3553);
        OpenGLState.lastTextureID = -1;
        BindCount--;
    }

    public static void clearTextures()
    {
        textures.clear();
    }

    public static Texture getSharedTexture(String name)
    {
        if(FrameLoader.bServer)
            return null;
        try
        {
            return getSharedTextureInternal(name);
        }
        catch(Exception ex)
        {
            return null;
        }
    }

    public static Texture trygetTexture(String name)
    {
        if(FrameLoader.bServer)
            return null;
        if(!name.contains(".txt"))
        {
            String subname = name;
            if(subname.contains(".pcx") || subname.contains(".png"))
                subname = subname.substring(0, name.lastIndexOf("."));
            subname = subname.substring(name.lastIndexOf("/") + 1);
            Texture tex = TexturePackPage.getTexture(subname);
            if(tex != null)
                return tex;
        }
        if(BucketManager.Shared().HasTexture(name))
            return BucketManager.Shared().getTexture(name);
        if(name.toLowerCase().contains(".pcx"))
            return null;
        else
            return null;
    }

    static Texture getSharedTextureInternal(String name)
    {
        if(FrameLoader.bServer)
            return null;
        if(texmap.contains(name))
            return (Texture)texmap.get(name);
        if(!name.contains(".txt"))
        {
            String subname = name;
            if(subname.contains(".pcx") || subname.contains(".png"))
                subname = subname.substring(0, name.lastIndexOf("."));
            subname = subname.substring(name.lastIndexOf("/") + 1);
            Texture tex = TexturePackPage.getTexture(subname);
            if(tex != null)
            {
                texmap.put(name, tex);
                return tex;
            }
        }
        Texture t;
        if(BucketManager.Shared().HasTexture(name))
        {
            t = BucketManager.Shared().getTexture(name);
            texmap.put(name, t);
            return t;
        }
        if(name.toLowerCase().contains(".pcx"))
            return null;
        t = new Texture(name);
        if(t.dataid.width == -1)
        {
            return null;
        } else
        {
            BucketManager.Shared().AddTexture(name, t);
            texmap.put(name, t);
            return t;
        }
    }

    public static Texture getSharedTexture(String name, String palette)
    {
        if(BucketManager.Shared().HasTexture((new StringBuilder()).append(name).append(palette).toString()))
        {
            return BucketManager.Shared().getTexture((new StringBuilder()).append(name).append(palette).toString());
        } else
        {
            Texture t = new Texture(name, palette);
            if(!autoCreateMask);
            BucketManager.Shared().AddTexture((new StringBuilder()).append(name).append(palette).toString(), t);
            return t;
        }
    }

    public static Texture getSharedTexture(String name, int palette[], String paletteName)
    {
        if(BucketManager.Shared().HasTexture((new StringBuilder()).append(name).append(paletteName).toString()))
        {
            return BucketManager.Shared().getTexture((new StringBuilder()).append(name).append(paletteName).toString());
        } else
        {
            Texture t = new Texture(name, palette);
            if(!autoCreateMask);
            BucketManager.Shared().AddTexture((new StringBuilder()).append(name).append(paletteName).toString(), t);
            return t;
        }
    }

    public static Texture getTexture(String name)
    {
        if(!name.contains(".txt"))
        {
            String subname = name;
            subname = subname.replace(".png", "");
            subname = subname.replace(".pcx", "");
            subname = subname.substring(name.lastIndexOf("/") + 1);
            Texture tex = TexturePackPage.getTexture(subname);
            if(tex != null)
                return tex;
        }
        if(BucketManager.Active().HasTexture(name))
            return BucketManager.Active().getTexture(name);
        try
        {
            Texture t = new Texture(name);
            if(!autoCreateMask);
            BucketManager.Active().AddTexture(name, t);
            return t;
        }
        catch(TextureNotFoundException exp)
        {
            return null;
        }
    }

    public void bind()
    {
        bind(33984);
    }

    public void bind(int unit)
    {
        if(isDestroyed() || !isValid())
        {
            return;
        } else
        {
            if(!dataid.bind());
            return;
        }
    }

    public void bindstrip(float a, float r, float g, float b)
    {
        bindstrip(33984, a, r, g, b);
    }

    public void bindstrip(int unit, float a, float r, float g, float b)
    {
    	 try
    	 /*      */     {
    	 /*  527 */       if ((isDestroyed()) || (!isValid()))
    	 /*      */       {
    	 /*  529 */         return;
    	 /*      */       }
    	 /*      */ 
    	 /*  532 */       if (this.dataid.id != OpenGLState.lastTextureID)
    	 /*      */       {
    	 /*  535 */         binds = 0;
    	 /*      */ 
    	 /*  537 */         if ((bDoingQuad) && (IndieGL.nCount == 1))
    	 /*      */         {
    	 /*      */           try
    	 /*      */           {
    	 /*  543 */             IndieGL.End();
    	 /*      */           }
    	 /*      */           catch (Exception ex)
    	 /*      */           {
    	 /*  550 */             Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
    	 /*      */           }
    	 /*  552 */           bDoingQuad = false;
    	 /*      */ 
    	 /*  554 */           this.dataid.bindalways();
    	 /*      */ 
    	 /*  558 */           IndieGL.Begin();
    	 /*      */ 
    	 /*  560 */           lasttex = this.dataid.getPathFileName();
    	 /*  561 */           bDoingQuad = true;
    	 /*  562 */           return;
    	 /*      */         }
    	 /*      */ 
    	 /*      */       }
    	 /*      */       else
    	 /*      */       {
    	 /*  570 */         binds += 1;
    	 /*  571 */         if (binds > maxbinds) {
    	 /*  572 */           maxbinds = binds;
    	 /*      */         }
    	 /*      */       }

    	 /*  575 */       if (this.dataid.bind())
    	 /*      */       {
    	 /*  578 */         IndieGL.Begin();
    	 /*      */ 
    	 /*  580 */         lasttex = this.dataid.getPathFileName();
    	 /*  581 */         bDoingQuad = true;
    	 /*      */       }
    	 /*      */     }
    	 /*      */     catch (Exception ex)
    	 /*      */     {
    	 /*  586 */       bDoingQuad = false;
    	 /*  587 */       Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
    	 /*      */     }
    	 /*      */ 
    	 /*  590 */     IndieGL.Begin();
    }

    public void copyMaskRegion(Texture from, int x, int y, int width, int height)
    {
        if(from.getMask() == null)
        {
            return;
        } else
        {
            new Mask(from, this, x, y, width, height);
            return;
        }
    }

    public void createMask()
    {
        new Mask(this);
    }

    public void destroy()
    {
        if(destroyed)
            return;
        if(dataid != null && --dataid.referenceCount == 0)
            dataid.destroy();
        destroyed = true;
    }

    public boolean equals(Texture other)
    {
        return other.xStart == xStart && other.xEnd == xEnd && other.yStart == yStart && other.yEnd == yEnd && other.width == width && other.height == height && other.solid == solid && (dataid == null || other.dataid == null || other.dataid.pathFileName == null || dataid.pathFileName == null || other.dataid.pathFileName.equals(dataid.pathFileName));
    }

    public ByteBuffer getData()
    {
        return dataid.getData();
    }

    public int getHeight()
    {
        return height;
    }

    public int getHeightHW()
    {
        return dataid.heightHW;
    }

    public int getHeightOrig()
    {
        if(heightOrig == 0)
            return height;
        else
            return heightOrig;
    }

    public int getID()
    {
        return dataid.id;
    }

    public Mask getMask()
    {
        return mask;
    }

    public String getName()
    {
        return name;
    }

    public TextureID getTextureId()
    {
        return dataid;
    }

    public boolean getUseAlphaChannel()
    {
        return !solid;
    }

    public int getWidth()
    {
        return width;
    }

    public int getWidthHW()
    {
        return dataid.widthHW;
    }

    public int getWidthOrig()
    {
        if(widthOrig == 0)
            return width;
        else
            return widthOrig;
    }

    public float getXEnd()
    {
        return xEnd;
    }

    public float getXStart()
    {
        return xStart;
    }

    public float getYEnd()
    {
        return yEnd;
    }

    public float getYStart()
    {
        return yStart;
    }

    public boolean isCollisionable()
    {
        return mask != null;
    }

    public boolean isDestroyed()
    {
        return destroyed;
    }

    public boolean isSolid()
    {
        return solid;
    }

    public boolean isValid()
    {
        return dataid != null;
    }

    public void makeTransp(int red, int green, int blue)
    {
        setAlphaForeach(red, green, blue, 0);
    }

    public void render(int x, int y, int width, int height)
    {
        render(x, y, width, height, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void render(int x, int y)
    {
        render(x, y, width, height, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void rendershader(Texture other, int x, int y)
    {
        rendershader(other, x, y, width, height, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void render(int x, int y, int width, int height, float r, float g, float b, 
            float a)
    {
        if(a == 0.0F)
            return;
        if(bDoingQuad)
        {
            bDoingQuad = false;
            IndieGL.End();
        }
        float sx = getXStart();
        float sy = getYStart();
        float ex = getXEnd();
        float ey = getYEnd();
        if(flip)
        {
            float temp = ex;
            ex = sx;
            sx = temp;
            x = (int)((float)x + ((float)widthOrig - offsetX - (float)this.width));
            y = (int)((float)y + offsetY);
        } else
        {
            x = (int)((float)x + offsetX);
            y = (int)((float)y + offsetY);
        }
        if(r > 1.0F)
            r = 1.0F;
        if(g > 1.0F)
            g = 1.0F;
        if(b > 1.0F)
            b = 1.0F;
        if(a > 1.0F)
            a = 1.0F;
        if(r < 0.0F)
            r = 0.0F;
        if(g < 0.0F)
            g = 0.0F;
        if(b < 0.0F)
            b = 0.0F;
        if(a < 0.0F)
            a = 0.0F;
        if(x + width <= 0)
            return;
        if(y + height <= 0)
            return;
        if(x >= Core.getInstance().getOffscreenWidth())
            return;
        if(y >= Core.getInstance().getOffscreenHeight())
            return;
        try
        {
            if(!Core.getInstance().bUseShaders);
            lr = r;
            lg = g;
            lb = b;
            la = a;
            SpriteRenderer.instance.render(this, x, y, width, height, r, g, b, a);
        }
        catch(Exception ex1)
        {
            bDoingQuad = false;
            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex1);
        }
        return;
    }

    public void rendershader(Texture other, int x, int y, int width, int height, float r, float g, 
            float b, float a)
    {
        if(a == 0.0F)
            return;
        float sx;
        float sy;
        float ex;
        float ey;
        float osx;
        float osy;
        float oex;
        float oey;
        if(bDoingQuad)
        {
            bDoingQuad = false;
            IndieGL.End();
        }
        sx = getXStart();
        sy = getYStart();
        ex = getXEnd();
        ey = getYEnd();
        osx = other.getXStart();
        osy = other.getYStart();
        oex = other.getXEnd();
        oey = other.getYEnd();
        if(flip)
        {
            float temp = ex;
            ex = sx;
            sx = temp;
            x = (int)((float)x + ((float)widthOrig - offsetX - (float)this.width));
            y = (int)((float)y + offsetY);
        } else
        {
            x = (int)((float)x + offsetX);
            y = (int)((float)y + offsetY);
        }
        if(r > 1.0F)
            r = 1.0F;
        if(g > 1.0F)
            g = 1.0F;
        if(b > 1.0F)
            b = 1.0F;
        if(a > 1.0F)
            a = 1.0F;
        if(r < 0.0F)
            r = 0.0F;
        if(g < 0.0F)
            g = 0.0F;
        if(b < 0.0F)
            b = 0.0F;
        if(a < 0.0F)
            a = 0.0F;
        if(x + width <= 0)
            return;
        if(y + height <= 0)
            return;
        if(x >= Core.getInstance().getOffscreenWidth())
            return;
        if(y >= Core.getInstance().getOffscreenHeight())
            return;
        try
        {
            if(lr != r || lg != g || lb != b || la != a)
                GL11.glColor4f(r, g, b, a);
            if(!Core.getInstance().bUseShaders);
            lr = r;
            lg = g;
            lb = b;
            la = a;
            bind();
            IndieGL.Begin();
            GL13.glMultiTexCoord2f(33984, sx, sy);
            GL13.glMultiTexCoord2f(33985, osx, osy);
            GL11.glVertex2i(x, y);
            GL13.glMultiTexCoord2f(33984, ex, sy);
            GL13.glMultiTexCoord2f(33985, oex, osy);
            GL11.glVertex2i(x + width, y);
            GL13.glMultiTexCoord2f(33984, ex, ey);
            GL13.glMultiTexCoord2f(33985, oex, oey);
            GL11.glVertex2i(x + width, y + height);
            GL13.glMultiTexCoord2f(33984, sx, ey);
            GL13.glMultiTexCoord2f(33985, osx, oey);
            GL11.glVertex2i(x, y + height);
            IndieGL.End();
        }
        catch(Exception ex1)
        {
            bDoingQuad = false;
            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex1);
        }
        return;
    }

    public void renderdiamond(int x, int y, int width, int height, float u, float d, float l, 
            float r, float a)
    {
        renderdiamond(x, y, width, height, u, u, u, u * a, d, d, d, d * a, l, l, l, l * a, r, r, r, r * a);
    }

    public void renderdiamond(int x, int y, int width, int height, float ru, float gu, float bu, 
            float au, float rd, float gd, float bd, float ad, float rl, float gl, 
            float bl, float al, float rr, float gr, float br, float ar)
    {
        x--;
        SpriteRenderer.instance.render(null, x, y, x + width / 2, y - height / 2, x + width, y, x + width / 2, y + height / 2, rl, gl, bl, al, ru, gu, bu, au, rr, gr, br, ar, rd, gd, bd, ad);
    }

    public void renderdiamond(int x, int y, int width, int height, int u, int d, int l, 
            int r)
    {
        x--;
        SpriteRenderer.instance.render(null, x, y, x + width / 2, y - height / 2, x + width, y, x + width / 2, y + height / 2, l, u, r, d);
    }

    public void renderwallw(int x, int y, int width, int height, float ru, float gu, float bu, 
            float au, float rd, float gd, float bd, float ad, float ru2, float gu2, 
            float bu2, float au2, float rd2, float gd2, float bd2, float ad2)
    {
        try
        {
            float sx = getXStart();
            float sy = getYStart();
            float ex = getXEnd();
            float ey = getYEnd();
            lr = -1F;
            lg = -1F;
            lb = -1F;
            la = -1F;
            sx += (ex - sx) * 0.01F;
            sy += (ey - sy) * 0.01F;
            ex -= (ex - sx) * 0.01F;
            ey -= (ey - sy) * 0.01F;
            if(flip)
            {
                float temp = ex;
                ex = sx;
                sx = temp;
                x = (int)((float)x + ((float)widthOrig - offsetX - (float)this.width));
                y = (int)((float)y + offsetY);
            } else
            {
                x = (int)((float)x + offsetX);
                y = (int)((float)y + offsetY);
            }
            width -= 4;
            if(!Core.getInstance().bUseShaders);
            SpriteRenderer.instance.render(null, x, y + 4, x, (y - 118) + 17, x + width / 2 + 4, (((y - height / 2) + 1) - 118) + 17, x + width / 2 + 4, (y - height / 2) + 4, rd, gd, bd, ad, rd2, gd2, bd2, ad2, ru2, gu2, bu2, au2, ru, gu, bu, au);
        }
        catch(Exception ex)
        {
            bDoingQuad = false;
            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void renderwallw(int x, int y, int width, int height, int u, int d, int u2, 
            int d2)
    {
        try
        {
            float sx = getXStart();
            float sy = getYStart();
            float ex = getXEnd();
            float ey = getYEnd();
            lr = -1F;
            lg = -1F;
            lb = -1F;
            la = -1F;
            sx += (ex - sx) * 0.01F;
            sy += (ey - sy) * 0.01F;
            ex -= (ex - sx) * 0.01F;
            ey -= (ey - sy) * 0.01F;
            if(flip)
            {
                float temp = ex;
                ex = sx;
                sx = temp;
                x = (int)((float)x + ((float)widthOrig - offsetX - (float)this.width));
                y = (int)((float)y + offsetY);
            } else
            {
                x = (int)((float)x + offsetX);
                y = (int)((float)y + offsetY);
            }
            width -= 4;
            if(!Core.getInstance().bUseShaders);
            SpriteRenderer.instance.render(null, x, y + 4, x, (y - 118) + 17, x + width / 2 + 4, (((y - height / 2) + 1) - 118) + 17, x + width / 2 + 4, (y - height / 2) + 4, d, d2, u2, u);
        }
        catch(Exception ex)
        {
            bDoingQuad = false;
            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void renderroofw(int x, int y, int width, int height, float ru, float gu, float bu, 
            float au, float rd, float gd, float bd, float ad, float ru2, float gu2, 
            float bu2, float au2, float rd2, float gd2, float bd2, float ad2)
    {
        try
        {
            float sx = getXStart();
            float sy = getYStart();
            float ex = getXEnd();
            float ey = getYEnd();
            lr = -1F;
            lg = -1F;
            lb = -1F;
            la = -1F;
            sx += (ex - sx) * 0.01F;
            sy += (ey - sy) * 0.01F;
            ex -= (ex - sx) * 0.01F;
            ey -= (ey - sy) * 0.01F;
            x = (int)((float)x + offsetX);
            y = (int)((float)y + offsetY);
            width -= 4;
            if(!Core.getInstance().bUseShaders);
            bindstrip(1.0F, 1.0F, 1.0F, 1.0F);
            int lx = x;
            int ly = y + height;
            lx += 32;
            ly -= 50;
            ly -= 32;
            GL11.glColor4f(rd, gd, bd, ad);
            GL11.glTexCoord2f(sx, ey);
            GL11.glVertex2i(lx, ly);
            GL11.glColor4f(rd2, gd2, bd2, ad2);
            GL11.glTexCoord2f(sx, sy);
            lx += 32;
            ly -= 16;
            GL11.glVertex2i(lx, ly);
            GL11.glColor4f(ru2, gu2, bu2, au2);
            GL11.glTexCoord2f(ex, sy);
            lx += 32;
            ly += 48;
            GL11.glVertex2i(lx, ly);
            GL11.glColor4f(ru, gu, bu, au);
            GL11.glTexCoord2f(ex, ey);
            lx -= 32;
            ly += 16;
            GL11.glVertex2i(lx, ly);
        }
        catch(Exception ex)
        {
            bDoingQuad = false;
            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void renderwalln(int x, int y, int width, int height, float ru, float gu, float bu, 
            float au, float rd, float gd, float bd, float ad, float ru2, float gu2, 
            float bu2, float au2, float rd2, float gd2, float bd2, float ad2)
    {
        try
        {
            float sx = getXStart();
            float sy = getYStart();
            float ex = getXEnd();
            float ey = getYEnd();
            lr = -1F;
            lg = -1F;
            lb = -1F;
            la = -1F;
            sx += (ex - sx) * 0.01F;
            sy += (ey - sy) * 0.01F;
            ex -= (ex - sx) * 0.01F;
            ey -= (ey - sy) * 0.01F;
            y += 4;
            if(flip)
            {
                float temp = ex;
                ex = sx;
                sx = temp;
                x = (int)((float)x + ((float)widthOrig - offsetX - (float)this.width));
                y = (int)((float)y + offsetY);
            } else
            {
                x = (int)((float)x + offsetX);
                y = (int)((float)y + offsetY);
            }
            if(!Core.getInstance().bUseShaders);
            x = (x -= 4) + width / 2;
            width -= 2;
            height += 3;
            SpriteRenderer.instance.render(null, x, y - 17, x, y - 119, x + width / 2 + 4, (y + height / 2 + 1) - 119, x + width / 2 + 4, (y + height / 2 + 4) - 17, ru, gu, bu, au, ru2, gu2, bu2, au2, rd2, gd2, bd2, ad2, rd, gd, bd, ad);
        }
        catch(Exception ex)
        {
            bDoingQuad = false;
            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void renderwalln(int x, int y, int width, int height, int u, int d, int u2, 
            int d2)
    {
        try
        {
            float sx = getXStart();
            float sy = getYStart();
            float ex = getXEnd();
            float ey = getYEnd();
            lr = -1F;
            lg = -1F;
            lb = -1F;
            la = -1F;
            sx += (ex - sx) * 0.01F;
            sy += (ey - sy) * 0.01F;
            ex -= (ex - sx) * 0.01F;
            ey -= (ey - sy) * 0.01F;
            y += 4;
            if(flip)
            {
                float temp = ex;
                ex = sx;
                sx = temp;
                x = (int)((float)x + ((float)widthOrig - offsetX - (float)this.width));
                y = (int)((float)y + offsetY);
            } else
            {
                x = (int)((float)x + offsetX);
                y = (int)((float)y + offsetY);
            }
            if(!Core.getInstance().bUseShaders);
            x = (x -= 4) + width / 2;
            width -= 2;
            SpriteRenderer.instance.render(null, x, y - 15, x, y - 119, x + width / 2 + 4, (y + height / 2 + 1) - 119, x + width / 2 + 4, (y + height / 2 + 4) - 17, u, u2, d2, d);
        }
        catch(Exception ex)
        {
            bDoingQuad = false;
            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void renderstrip(int x, int y, int width, int height, float r, float g, float b, 
            float a)
    {
        if(a <= 0.0F)
            return;
        try
        {
            if(r > 1.0F)
                r = 1.0F;
            if(g > 1.0F)
                g = 1.0F;
            if(b > 1.0F)
                b = 1.0F;
            if(a > 1.0F)
                a = 1.0F;
            if(r < 0.0F)
                r = 0.0F;
            if(g < 0.0F)
                g = 0.0F;
            if(b < 0.0F)
                b = 0.0F;
            if(a < 0.0F)
                a = 0.0F;
            float sx = getXStart();
            float sy = getYStart();
            float ex = getXEnd();
            float ey = getYEnd();
            if(flip)
            {
                float temp = ex;
                ex = sx;
                sx = temp;
                x = (int)((float)x + ((float)widthOrig - offsetX - (float)this.width));
                y = (int)((float)y + offsetY);
            } else
            {
                x = (int)((float)x + offsetX);
                y = (int)((float)y + offsetY);
            }
            SpriteRenderer.instance.render(this, x, y, width, height, r, g, b, a);
        }
        catch(Exception ex)
        {
            bDoingQuad = false;
            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return;
    }

    public void setAlphaForeach(int red, int green, int blue, int alpha)
    {
        if(getTextureId().getImageData() != null)
            getTextureId().getImageData().makeTransp((byte)red, (byte)green, (byte)blue, (byte)alpha);
        else
            setData(ImageUtils.makeTransp(getData(), red, green, blue, alpha, getWidthHW(), getHeightHW()));
        AlphaColorIndex alphaColorIndex = new AlphaColorIndex(red, green, blue, alpha);
        if(!dataid.alphaList.contains(alphaColorIndex))
            dataid.alphaList.add(alphaColorIndex);
    }

    public void setCustomizedTexture()
    {
        dataid.pathFileName = null;
    }

    public void setData(ByteBuffer data)
    {
        dataid.setData(data);
    }

    public void setMask(Mask mask)
    {
        this.mask = mask;
    }

    public void setName(String name)
    {
        if(name == null)
            return;
        if(name.equals(this.name))
        {
            if(!textures.containsKey(name))
                textures.put(name, this);
            return;
        }
        if(textures.containsKey(name))
            throw new TextureNameAlreadyInUseException(name);
        if(textures.containsKey(this.name))
            textures.remove(this.name);
        this.name = name;
        textures.put(name, this);
    }

    public void setRegion(int x, int y, int width, int height)
    {
        if(x < 0 || x > getWidthHW())
            return;
        if(y < 0 || y > getHeightHW())
            return;
        if(width <= 0)
            return;
        if(height <= 0)
            return;
        if(width + x > getWidthHW())
            width = getWidthHW() - x;
        if(height > getHeightHW())
            height = getHeightHW() - y;
        xStart = (float)x / (float)getWidthHW();
        yStart = (float)y / (float)getHeightHW();
        xEnd = (float)(x + width) / (float)getWidthHW();
        yEnd = (float)(y + height) / (float)getHeightHW();
        this.width = width;
        this.height = height;
    }

    public void setUseAlphaChannel(boolean value)
    {
        dataid.solid = solid = !value;
    }

    public Texture split(int xOffset, int yOffset, int width, int height)
    {
        Texture tex = new Texture(getTextureId(), (new StringBuilder()).append(name).append("_").append(xOffset).append("_").append(yOffset).toString());
        tex.setRegion(xOffset, yOffset, width, height);
        return tex;
    }

    public Texture[] split(int xOffset, int yOffset, int row, int coloumn, int width, int height, int spaceX, 
            int spaceY)
    {
        Texture temp[] = new Texture[row * coloumn];
        for(int y = 0; y < row; y++)
        {
            for(int x = 0; x < coloumn; x++)
            {
                temp[x + y * coloumn] = new Texture(getTextureId(), (new StringBuilder()).append(name).append("_").append(row).append("_").append(coloumn).toString());
                temp[x + y * coloumn].setRegion(xOffset + x * width + spaceX * x, yOffset + y * height + spaceY * y, width, height);
                temp[x + y * coloumn].copyMaskRegion(this, xOffset + x * width + spaceX * x, yOffset + y * height + spaceY * y, width, height);
            }

        }

        return temp;
    }

    public Texture[][] split2D(int xstep[], int ystep[])
    {
        if(xstep == null || ystep == null)
            return (Texture[][])null;
        Texture texts[][] = new Texture[xstep.length][ystep.length];
        float oldy;
        float newH;
        float oldx = oldy = newH = 0.0F;
        for(int y = 0; y < ystep.length; y++)
        {
            oldy += newH;
            newH = (float)ystep[y] / (float)getHeightHW();
            oldx = 0.0F;
            for(int x = 0; x < xstep.length; x++)
            {
                float newW = (float)xstep[x] / (float)getWidthHW();
                Texture ttext = texts[x][y] = new Texture(this);
                ttext.width = xstep[x];
                ttext.height = ystep[y];
                ttext.xStart = oldx;
                ttext.xEnd = oldx += newW;
                ttext.yStart = oldy;
                ttext.yEnd = oldy + newH;
            }

        }

        return texts;
    }

    public String toString()
    {
        return name;
    }

    protected void finalize()
        throws Throwable
    {
        if(!isDestroyed())
            destroy();
        super.finalize();
    }

    private void readVersion3(ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        if(textures.containsKey(name))
            System.out.println("ERROR: Texture's name already loaded");
        else
            textures.put(name, this);
        boolean fromFile = in.readBoolean();
        if(fromFile)
        {
            String path = (String)in.readObject();
            boolean useAlpha = !in.readBoolean();
            xStart = in.readFloat();
            xEnd = in.readFloat();
            yStart = in.readFloat();
            yEnd = in.readFloat();
            System.out.println((new StringBuilder()).append("path: ").append(path).toString());
            dataid = new TextureID(path);
            dataid.referenceCount++;
            if(useAlpha)
            {
                ArrayList alphaColors = (ArrayList)in.readObject();
                for(int i = 0; i < alphaColors.size(); i++)
                {
                    AlphaColorIndex alphaColor = (AlphaColorIndex)alphaColors.get(i);
                    makeTransp(alphaColor.red, alphaColor.green, alphaColor.blue);
                }

            }
        } else
        {
            System.out.println("Loading runtime customized texture");
            dataid = (TextureID)in.readObject();
        }
        if(in.readBoolean())
            mask = (Mask)in.readObject();
    }

    public void renderTest(int x, int y)
    {
        float sx;
        float sy;
        float ex;
        float ey;
        if(bDoingQuad)
        {
            bDoingQuad = false;
            IndieGL.End();
        }
        sx = getXStart();
        sy = getYStart();
        ex = getXEnd();
        ey = getYEnd();
        if(flip)
        {
            float temp = ex;
            ex = sx;
            sx = temp;
            x = (int)((float)x + ((float)widthOrig - offsetX - (float)width));
            y = (int)((float)y + offsetY);
        } else
        {
            x = (int)((float)x + offsetX);
            y = (int)((float)y + offsetY);
        }
        if(x + width <= 0)
            return;
        if(y + height <= 0)
            return;
        if(x >= Core.getInstance().getOffscreenWidth())
            return;
        if(y >= Core.getInstance().getOffscreenHeight())
            return;
        try
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            bind();
            IndieGL.Begin();
            GL11.glTexCoord2f(sx, sy);
            GL11.glVertex2i(x, y);
            GL11.glTexCoord2f(ex, sy);
            GL11.glVertex2i(x + 32, y - 16);
            GL11.glTexCoord2f(ex, ey);
            GL11.glVertex2i(x + 32, y - 16 - 96);
            GL11.glTexCoord2f(sx, ey);
            GL11.glVertex2i(x, y - 96);
            IndieGL.End();
        }
        catch(Exception ex1)
        {
            bDoingQuad = false;
            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex1);
        }
        return;
    }

    private static THashMap textures = new THashMap();
    private static final long serialVersionUID = 0x67b32af801d76992L;
    public static boolean autoCreateMask = false;
    public static int BindCount = 0;
    public static int renderQuadBatchCount = 0;
    public static int startStack = 0;
    public static boolean bDoingQuad = false;
    public static float lr;
    public static float lg;
    public static float lb;
    public static float la;
    public boolean flip;
    public float offsetX;
    public float offsetY;
    public float xEnd;
    public float yEnd;
    public float xStart;
    public float yStart;
    protected TextureID dataid;
    protected Mask mask;
    protected String name;
    protected boolean solid;
    protected int width;
    protected int height;
    protected int heightOrig;
    protected int widthOrig;
    private boolean destroyed;
    static THashMap texmap = new THashMap();
    public static boolean WarnFailFindTexture = true;
    static int maxbinds = 0;
    static int binds = 0;
    static String lasttex = "";

}
