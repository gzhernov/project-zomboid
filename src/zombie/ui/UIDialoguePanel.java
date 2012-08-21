// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UIDialoguePanel.java

package zombie.ui;

import java.awt.Rectangle;
import java.util.*;
import zombie.core.Color;
import zombie.core.textures.Texture;

// Referenced classes of package zombie.ui:
//            UIElement

public class UIDialoguePanel extends UIElement
{

    public UIDialoguePanel(int x, int y, int width, int height)
    {
        alpha = 1.0F;
        dialogBottomLeft = null;
        dialogBottomMiddle = null;
        dialogBottomRight = null;
        dialogLeft = null;
        dialogMiddle = null;
        dialogRight = null;
        titleLeft = null;
        titleMiddle = null;
        titleRight = null;
        clientH = 0;
        clientW = 0;
        nestedItems = new Stack();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        titleLeft = Texture.getSharedTexture("media/ui/Dialog_Titlebar_Left.png");
        titleMiddle = Texture.getSharedTexture("media/ui/Dialog_Titlebar_Middle.png");
        titleRight = Texture.getSharedTexture("media/ui/Dialog_Titlebar_Right.png");
        dialogLeft = Texture.getSharedTexture("media/ui/Dialog_Left.png");
        dialogMiddle = Texture.getSharedTexture("media/ui/Dialog_Middle.png");
        dialogRight = Texture.getSharedTexture("media/ui/Dialog_Right.png");
        dialogBottomLeft = Texture.getSharedTexture("media/ui/Dialog_Bottom_Left.png");
        dialogBottomMiddle = Texture.getSharedTexture("media/ui/Dialog_Bottom_Middle.png");
        dialogBottomRight = Texture.getSharedTexture("media/ui/Dialog_Bottom_Right.png");
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
        DrawTextureScaledCol(titleLeft, 0, 0, 28, 28, new Color(255, 255, 255, 100));
        DrawTextureScaledCol(titleMiddle, 28, 0, getWidth() - 56, 28, new Color(255, 255, 255, 100));
        DrawTextureScaledCol(titleRight, (0 + getWidth()) - 28, 0, 28, 28, new Color(255, 255, 255, 100));
        DrawTextureScaledCol(dialogLeft, 0, 28, 78, getHeight() - 100, new Color(255, 255, 255, 100));
        DrawTextureScaledCol(dialogMiddle, 78, 28, getWidth() - 156, getHeight() - 100, new Color(255, 255, 255, 100));
        DrawTextureScaledCol(dialogRight, (0 + getWidth()) - 78, 28, 78, getHeight() - 100, new Color(255, 255, 255, 100));
        DrawTextureScaledCol(dialogBottomLeft, 0, (0 + getHeight()) - 72, 78, 72, new Color(255, 255, 255, 100));
        DrawTextureScaledCol(dialogBottomMiddle, 78, (0 + getHeight()) - 72, getWidth() - 156, 72, new Color(255, 255, 255, 100));
        DrawTextureScaledCol(dialogBottomRight, (0 + getWidth()) - 78, (0 + getHeight()) - 72, 78, 72, new Color(255, 255, 255, 100));
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
    Texture dialogBottomLeft;
    Texture dialogBottomMiddle;
    Texture dialogBottomRight;
    Texture dialogLeft;
    Texture dialogMiddle;
    Texture dialogRight;
    Texture titleLeft;
    Texture titleMiddle;
    Texture titleRight;
    public int clientH;
    public int clientW;
    public Stack nestedItems;
}
