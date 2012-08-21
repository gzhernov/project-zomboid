// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DoubleSizer.java

package zombie.ui;

import zombie.core.Core;
import zombie.core.textures.Texture;

// Referenced classes of package zombie.ui:
//            UIElement

public class DoubleSizer extends UIElement
{

    public DoubleSizer(int x, int y, String texture, String highlight, String texture2, String highlight2)
    {
        clicked = false;
        mouseOver = false;
        display = display;
        this.texture = Texture.getSharedTexture((new StringBuilder()).append("media/ui/").append(texture).append(".png").toString());
        this.highlight2 = Texture.getSharedTexture((new StringBuilder()).append("media/ui/").append(highlight2).append(".png").toString());
        this.texture2 = Texture.getSharedTexture((new StringBuilder()).append("media/ui/").append(texture2).append(".png").toString());
        this.highlight = Texture.getSharedTexture((new StringBuilder()).append("media/ui/").append(highlight).append(".png").toString());
        this.x = x;
        this.y = y;
        width = this.texture.getWidth();
        height = this.texture.getHeight();
    }

    public boolean onMouseDown(int x, int y)
    {
        clicked = true;
        return true;
    }

    public boolean onMouseMove(int dx, int dy)
    {
        mouseOver = true;
        return true;
    }

    public void onMouseMoveOutside(int dx, int dy)
    {
        clicked = false;
        mouseOver = false;
    }

    public boolean onMouseUp(int x, int y)
    {
        if(clicked)
            Core.getInstance().doubleSizeToggle();
        clicked = false;
        return true;
    }

    public void render()
    {
        if(clicked)
            DrawTextureScaled(highlight, 0, 0, highlight.getWidth(), highlight.getHeight(), 1.0F);
        else
        if(mouseOver)
            DrawTextureScaled(texture, 0, 0, texture.getWidth(), texture.getHeight(), 1.0F);
        else
            DrawTextureScaled(texture, 0, 0, texture.getWidth(), texture.getHeight(), 0.85F);
        super.render();
    }

    public void update()
    {
        super.update();
    }

    boolean clicked;
    UIElement display;
    Texture highlight;
    Texture highlight2;
    boolean mouseOver;
    Texture texture;
    Texture texture2;
}
