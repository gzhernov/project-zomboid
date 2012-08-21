// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Color.java

package zombie.core;

import java.io.Serializable;

public class Color
    implements Serializable
{

    public Color(Color color)
    {
        a = 1.0F;
        if(color == null)
        {
            r = 0.0F;
            g = 0.0F;
            b = 0.0F;
            a = 1.0F;
            return;
        } else
        {
            r = color.r;
            g = color.g;
            b = color.b;
            a = color.a;
            return;
        }
    }

    public Color(float r, float g, float b)
    {
        a = 1.0F;
        this.r = r;
        this.g = g;
        this.b = b;
        a = 1.0F;
    }

    public Color(float r, float g, float b, float a)
    {
        this.a = 1.0F;
        this.r = Math.min(r, 1.0F);
        this.g = Math.min(g, 1.0F);
        this.b = Math.min(b, 1.0F);
        this.a = Math.min(a, 1.0F);
    }

    public Color(Color A, Color B, float delta)
    {
        this.a = 1.0F;
        float r = (B.r - A.r) * delta;
        float g = (B.g - A.g) * delta;
        float b = (B.b - A.b) * delta;
        float a = (B.a - A.a) * delta;
        this.r = A.r + r;
        this.g = A.g + g;
        this.b = A.b + b;
        this.a = A.a + a;
    }

    public void setColor(Color A, Color B, float delta)
    {
        float r = (B.r - A.r) * delta;
        float g = (B.g - A.g) * delta;
        float b = (B.b - A.b) * delta;
        float a = (B.a - A.a) * delta;
        this.r = A.r + r;
        this.g = A.g + g;
        this.b = A.b + b;
        this.a = A.a + a;
    }

    public Color(int r, int g, int b)
    {
        a = 1.0F;
        this.r = (float)r / 255F;
        this.g = (float)g / 255F;
        this.b = (float)b / 255F;
        a = 1.0F;
    }

    public Color(int r, int g, int b, int a)
    {
        this.a = 1.0F;
        this.r = (float)r / 255F;
        this.g = (float)g / 255F;
        this.b = (float)b / 255F;
        this.a = (float)a / 255F;
    }

    public Color(int value)
    {
        this.a = 1.0F;
        int b = (value & 0xff0000) >> 16;
        int g = (value & 0xff00) >> 8;
        int r = value & 0xff;
        int a = (value & 0xff000000) >> 24;
        if(a < 0)
            a += 256;
        if(a == 0)
            a = 255;
        this.r = (float)r / 255F;
        this.g = (float)g / 255F;
        this.b = (float)b / 255F;
        this.a = (float)a / 255F;
    }

    public void fromColor(int value)
    {
        int b = (value & 0xff0000) >> 16;
        int g = (value & 0xff00) >> 8;
        int r = value & 0xff;
        int a = (value & 0xff000000) >> 24;
        if(a < 0)
            a += 256;
        if(a == 0)
            a = 255;
        this.r = (float)r / 255F;
        this.g = (float)g / 255F;
        this.b = (float)b / 255F;
        this.a = (float)a / 255F;
    }

    public static Color decode(String nm)
    {
        return new Color(Integer.decode(nm).intValue());
    }

    public void add(Color c)
    {
        r += c.r;
        g += c.g;
        b += c.b;
        a += c.a;
    }

    public Color addToCopy(Color c)
    {
        Color copy = new Color(r, g, b, a);
        copy.r += c.r;
        copy.g += c.g;
        copy.b += c.b;
        copy.a += c.a;
        return copy;
    }

    public Color brighter()
    {
        return brighter(0.2F);
    }

    public Color brighter(float scale)
    {
        r = r += scale;
        g = g += scale;
        b = b += scale;
        return this;
    }

    public Color darker()
    {
        return darker(0.5F);
    }

    public Color darker(float scale)
    {
        r = r -= scale;
        g = g -= scale;
        b = b -= scale;
        return this;
    }

    public boolean equals(Object other)
    {
        if(other instanceof Color)
        {
            Color o = (Color)other;
            return o.r == r && o.g == g && o.b == b && o.a == a;
        } else
        {
            return false;
        }
    }

    public void set(Color other)
    {
        r = other.r;
        g = other.g;
        b = other.b;
        a = other.a;
    }

    public int getAlpha()
    {
        return (int)(a * 255F);
    }

    public int getAlphaByte()
    {
        return (int)(a * 255F);
    }

    public int getBlue()
    {
        return (int)(b * 255F);
    }

    public int getBlueByte()
    {
        return (int)(b * 255F);
    }

    public int getGreen()
    {
        return (int)(g * 255F);
    }

    public int getGreenByte()
    {
        return (int)(g * 255F);
    }

    public int getRed()
    {
        return (int)(r * 255F);
    }

    public int getRedByte()
    {
        return (int)(r * 255F);
    }

    public int hashCode()
    {
        return (int)(r + g + b + a) * 255;
    }

    public Color multiply(Color c)
    {
        return new Color(r * c.r, g * c.g, b * c.b, a * c.a);
    }

    public void scale(float value)
    {
        r *= value;
        g *= value;
        b *= value;
        a *= value;
    }

    public Color scaleCopy(float value)
    {
        Color copy = new Color(r, g, b, a);
        copy.r *= value;
        copy.g *= value;
        copy.b *= value;
        copy.a *= value;
        return copy;
    }

    public String toString()
    {
        return (new StringBuilder()).append("Color (").append(r).append(",").append(g).append(",").append(b).append(",").append(a).append(")").toString();
    }

    public void interp(Color to, float delta, Color dest)
    {
        float r = to.r - this.r;
        float g = to.g - this.g;
        float b = to.b - this.b;
        float a = to.a - this.a;
        r *= delta;
        g *= delta;
        b *= delta;
        a *= delta;
        dest.r = this.r + r;
        dest.g = this.g + g;
        dest.b = this.b + b;
        dest.a = this.a + a;
    }

    private static final long serialVersionUID = 0x154513L;
    public static final Color transparent = new Color(0.0F, 0.0F, 0.0F, 0.0F);
    public static final Color white = new Color(1.0F, 1.0F, 1.0F, 1.0F);
    public static final Color yellow = new Color(1.0F, 1.0F, 0.0F, 1.0F);
    public static final Color red = new Color(1.0F, 0.0F, 0.0F, 1.0F);
    public static final Color blue = new Color(0.0F, 0.0F, 1.0F, 1.0F);
    public static final Color green = new Color(0.0F, 1.0F, 0.0F, 1.0F);
    public static final Color black = new Color(0.0F, 0.0F, 0.0F, 1.0F);
    public static final Color gray = new Color(0.5F, 0.5F, 0.5F, 1.0F);
    public static final Color cyan = new Color(0.0F, 1.0F, 1.0F, 1.0F);
    public static final Color darkGray = new Color(0.3F, 0.3F, 0.3F, 1.0F);
    public static final Color lightGray = new Color(0.7F, 0.7F, 0.7F, 1.0F);
    public static final Color pink = new Color(255, 175, 175, 255);
    public static final Color orange = new Color(255, 200, 0, 255);
    public static final Color magenta = new Color(255, 0, 255, 255);
    public float a;
    public float b;
    public float g;
    public float r;

}
