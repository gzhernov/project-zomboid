// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   IndieGL.java

package zombie;

import org.lwjgl.opengl.GL11;
import zombie.core.SpriteRenderer;

public class IndieGL
{

    public IndieGL()
    {
    }

    public static void glBlendFunc(int i, int j)
    {
    }

    public static void glBlendFuncA(int i, int j)
    {
    }

    public static void glEnable(int a)
    {
        SpriteRenderer.instance.glEnable(a);
    }

    public static void glColorMask(boolean flag, boolean flag1, boolean flag2, boolean flag3)
    {
    }

    public static void glColorMaskA(boolean bln, boolean bln1, boolean bln2, boolean bln3)
    {
        GL11.glColorMask(bln, bln, bln3, bln3);
    }

    public static void glEnableA(int a)
    {
        GL11.glEnable(a);
    }

    public static void glAlphaFunc(int a, float b)
    {
        if(a == glAlphaFuncAA && b == glAlphaFuncBB)
        {
            return;
        } else
        {
            SpriteRenderer.instance.glAlphaFunc(a, b);
            glAlphaFuncAA = a;
            glAlphaFuncBB = b;
            return;
        }
    }

    public static void glAlphaFuncA(int a, float b)
    {
        GL11.glAlphaFunc(a, b);
    }

    public static void glStencilFunc(int a, int b, int c)
    {
        if(a == glStencilFuncA && b == glStencilFuncB && c == glStencilFuncC)
        {
            return;
        } else
        {
            SpriteRenderer.instance.glStencilFunc(a, b, c);
            glStencilFuncA = a;
            glStencilFuncB = b;
            glStencilFuncC = c;
            return;
        }
    }

    public static void glStencilFuncA(int a, int b, int c)
    {
        GL11.glStencilFunc(a, b, c);
    }

    public static void glStencilOp(int a, int b, int c)
    {
        if(a == glStencilOpA && b == glStencilOpB && c == glStencilOpC)
        {
            return;
        } else
        {
            SpriteRenderer.instance.glStencilOp(a, b, c);
            glStencilOpA = a;
            glStencilOpB = b;
            glStencilOpC = c;
            return;
        }
    }

    public static void glStencilOpA(int a, int b, int c)
    {
        GL11.glStencilOp(a, b, c);
    }

    public static void glStencilMask(int a)
    {
        SpriteRenderer.instance.glStencilMask(a);
    }

    public static void glStencilMaskA(int a)
    {
        GL11.glStencilMask(a);
    }

    public static void glDisable(int a)
    {
        SpriteRenderer.instance.glDisable(a);
    }

    public static void glClear(int a)
    {
        SpriteRenderer.instance.glClear(a);
    }

    public static void glClearA(int a)
    {
        GL11.glClear(a);
    }

    public static void glDisableA(int a)
    {
        GL11.glDisable(a);
    }

    public static void End()
    {
    }

    public static void Begin()
    {
    }

    public static void BeginLine()
    {
    }

    public static int nCount = 0;
    static int glBlendFuncA = -12345;
    static int glBlendFuncB = -12345;
    static int glStencilFuncA = -12345;
    static int glStencilFuncB = -12345;
    static int glStencilFuncC = -12345;
    static int glStencilOpA = -12345;
    static int glStencilOpB = -12345;
    static int glStencilOpC = -12345;
    static int glAlphaFuncAA = -1;
    static float glAlphaFuncBB = -1F;

}