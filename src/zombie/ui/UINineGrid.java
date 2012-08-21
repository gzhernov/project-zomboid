// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UINineGrid.java

package zombie.ui;

import java.awt.Rectangle;
import java.util.*;
import zombie.core.Color;
import zombie.core.textures.Texture;

// Referenced classes of package zombie.ui:
//            UIElement

public class UINineGrid extends UIElement
{

    public UINineGrid(int x, int y, int width, int height, int TopWidth, int LeftWidth, int RightWidth, 
            int BottomWidth, String TL_Tex, String T_Tex, String TR_Tex, String L_Tex, String C_Tex, String R_Tex, 
            String BL_Tex, String B_Tex, String BR_Tex)
    {
        alpha = 1.0F;
        GridTopLeft = null;
        GridTop = null;
        GridTopRight = null;
        GridLeft = null;
        GridCenter = null;
        GridRight = null;
        GridBottomLeft = null;
        GridBottom = null;
        GridBottomRight = null;
        this.TopWidth = 10;
        this.LeftWidth = 10;
        this.RightWidth = 10;
        this.BottomWidth = 10;
        clientH = 0;
        clientW = 0;
        nestedItems = new Stack();
        Colour = new Color(50, 50, 50, 212);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.TopWidth = TopWidth;
        this.LeftWidth = LeftWidth;
        this.RightWidth = RightWidth;
        this.BottomWidth = BottomWidth;
        GridTopLeft = Texture.getSharedTexture(TL_Tex);
        GridTop = Texture.getSharedTexture(T_Tex);
        GridTopRight = Texture.getSharedTexture(TR_Tex);
        GridLeft = Texture.getSharedTexture(L_Tex);
        GridCenter = Texture.getSharedTexture(C_Tex);
        GridRight = Texture.getSharedTexture(R_Tex);
        GridBottomLeft = Texture.getSharedTexture(BL_Tex);
        GridBottom = Texture.getSharedTexture(B_Tex);
        GridBottomRight = Texture.getSharedTexture(BR_Tex);
        clientW = width;
        clientH = height;
    }

    public void Nest(UIElement el, int t, int r, int b, int l)
    {
        AddChild(el);
        nestedItems.add(new Rectangle(l, t, r, b));
        el.setX(l);
        el.setY(t);
        el.update();
    }

    public void render()
    {
        DrawTextureScaledCol(GridTopLeft, 0, 0, LeftWidth, TopWidth, Colour);
        DrawTextureScaledCol(GridTop, LeftWidth, 0, getWidth() - (LeftWidth + RightWidth), TopWidth, Colour);
        DrawTextureScaledCol(GridTopRight, getWidth() - RightWidth, 0, RightWidth, TopWidth, Colour);
        DrawTextureScaledCol(GridLeft, 0, TopWidth, LeftWidth, getHeight() - (TopWidth + BottomWidth), Colour);
        DrawTextureScaledCol(GridCenter, LeftWidth, TopWidth, getWidth() - (LeftWidth + RightWidth), getHeight() - (TopWidth + BottomWidth), Colour);
        DrawTextureScaledCol(GridRight, getWidth() - RightWidth, TopWidth, RightWidth, getHeight() - (TopWidth + BottomWidth), Colour);
        DrawTextureScaledCol(GridBottomLeft, 0, getHeight() - BottomWidth, LeftWidth, BottomWidth, Colour);
        DrawTextureScaledCol(GridBottom, LeftWidth, getHeight() - BottomWidth, getWidth() - (LeftWidth + RightWidth), BottomWidth, Colour);
        DrawTextureScaledCol(GridBottomRight, getWidth() - RightWidth, getHeight() - BottomWidth, RightWidth, BottomWidth, Colour);
        super.render();
    }

    public void update()
    {
        super.update();
        int n = 0;
        for(Iterator i$ = nestedItems.iterator(); i$.hasNext();)
        {
            Rectangle rect = (Rectangle)i$.next();
            UIElement con = (UIElement)getControls().get(n);
            con.setX((float)rect.getX());
            con.setY((float)rect.getY());
            con.setWidth((int)((double)clientW - (rect.getX() + rect.getWidth())));
            con.setHeight((int)((double)clientH - (rect.getY() + rect.getHeight())));
            con.onresize();
            n++;
        }

    }

    float alpha;
    Texture GridTopLeft;
    Texture GridTop;
    Texture GridTopRight;
    Texture GridLeft;
    Texture GridCenter;
    Texture GridRight;
    Texture GridBottomLeft;
    Texture GridBottom;
    Texture GridBottomRight;
    int TopWidth;
    int LeftWidth;
    int RightWidth;
    int BottomWidth;
    public int clientH;
    public int clientW;
    public Stack nestedItems;
    public Color Colour;
}
