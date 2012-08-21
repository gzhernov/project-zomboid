// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AbstractStyle.java

package zombie.core.Styles;


// Referenced classes of package zombie.core.Styles:
//            Style, AlphaOp, GeometryData

public abstract class AbstractStyle
    implements Style
{

    public AbstractStyle()
    {
    }

    public boolean getRenderSprite()
    {
        return false;
    }

    public AlphaOp getAlphaOp()
    {
        return null;
    }

    public int getStyleID()
    {
        return hashCode();
    }

    public void resetState()
    {
    }

    public void setupState()
    {
    }

    public GeometryData build()
    {
        return null;
    }

    public void render(int i, int j)
    {
    }

    private static final long serialVersionUID = 1L;
}
