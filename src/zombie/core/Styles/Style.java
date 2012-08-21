// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Style.java

package zombie.core.Styles;


// Referenced classes of package zombie.core.Styles:
//            AlphaOp, GeometryData

public interface Style
{

    public abstract void setupState();

    public abstract void resetState();

    public abstract int getStyleID();

    public abstract AlphaOp getAlphaOp();

    public abstract boolean getRenderSprite();

    public abstract GeometryData build();

    public abstract void render(int i, int j);
}
