// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TransparentStyle.java

package zombie.core.Styles;

import org.lwjgl.opengl.GL11;

// Referenced classes of package zombie.core.Styles:
//            AbstractStyle, AlphaOp

public class TransparentStyle extends AbstractStyle
{

    public TransparentStyle()
    {
    }

    public void setupState()
    {
        GL11.glBlendFunc(770, 771);
    }

    public void resetState()
    {
    }

    public AlphaOp getAlphaOp()
    {
        return AlphaOp.KEEP;
    }

    public int getStyleID()
    {
        return 2;
    }

    public boolean getRenderSprite()
    {
        return true;
    }

    private static final long serialVersionUID = 1L;
    public static TransparentStyle instance = new TransparentStyle();

}
