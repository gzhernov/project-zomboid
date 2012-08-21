// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LightingStyle.java

package zombie.core.Styles;

import org.lwjgl.opengl.GL11;

// Referenced classes of package zombie.core.Styles:
//            AbstractStyle, AlphaOp

public class LightingStyle extends AbstractStyle
{

    public LightingStyle()
    {
    }

    public void setupState()
    {
        GL11.glBlendFunc(0, 768);
    }

    public void resetState()
    {
        GL11.glBlendFunc(770, 771);
    }

    public AlphaOp getAlphaOp()
    {
        return AlphaOp.KEEP;
    }

    public int getStyleID()
    {
        return 3;
    }

    public boolean getRenderSprite()
    {
        return true;
    }

    private static final long serialVersionUID = 1L;
    public static LightingStyle instance = new LightingStyle();

}
