// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HUDButton.java

package zombie.ui;

import zombie.FrameLoader;
import zombie.core.textures.Texture;
import zombie.core.textures.TexturePackPage;

// Referenced classes of package zombie.ui:
//            UIElement, UIEventHandler

public class HUDButton extends UIElement
{

    public HUDButton(String name, int x, int y, String texture, String highlight, UIElement display)
    {
        clicked = false;
        overicon = null;
        mouseOver = false;
        notclickedAlpha = 0.85F;
        clickedalpha = 1.0F;
        if(FrameLoader.bServer)
            return;
        this.display = display;
        this.name = name;
        if(this.texture == null)
        {
            this.texture = Texture.getSharedTexture(texture);
            this.highlight = Texture.getSharedTexture(highlight);
        }
        this.x = x;
        this.y = y;
        width = this.texture.getWidth();
        height = this.texture.getHeight();
    }

    public HUDButton(String name, int x, int y, String texture, String highlight, UIEventHandler handler)
    {
        clicked = false;
        overicon = null;
        mouseOver = false;
        notclickedAlpha = 0.85F;
        clickedalpha = 1.0F;
        if(FrameLoader.bServer)
            return;
        this.texture = TexturePackPage.getTexture(texture);
        this.highlight = TexturePackPage.getTexture(highlight);
        this.handler = handler;
        this.name = name;
        if(this.texture == null)
        {
            this.texture = Texture.getSharedTexture(texture);
            this.highlight = Texture.getSharedTexture(highlight);
        }
        this.x = x;
        this.y = y;
        width = this.texture.getWidth();
        height = this.texture.getHeight();
    }

    public HUDButton(String name, int x, int y, String texture, String highlight, String overicon, UIElement display)
    {
        clicked = false;
        this.overicon = null;
        mouseOver = false;
        notclickedAlpha = 0.85F;
        clickedalpha = 1.0F;
        if(FrameLoader.bServer)
            return;
        this.overicon = Texture.getSharedTexture(overicon);
        this.display = display;
        this.texture = TexturePackPage.getTexture(texture);
        this.highlight = TexturePackPage.getTexture(highlight);
        this.name = name;
        if(this.texture == null)
        {
            this.texture = Texture.getSharedTexture(texture);
            this.highlight = Texture.getSharedTexture(highlight);
        }
        this.x = x;
        this.y = y;
        width = this.texture.getWidth();
        height = this.texture.getHeight();
    }

    public HUDButton(String name, int x, int y, String texture, String highlight, String overicon, UIEventHandler handler)
    {
        clicked = false;
        this.overicon = null;
        mouseOver = false;
        notclickedAlpha = 0.85F;
        clickedalpha = 1.0F;
        if(FrameLoader.bServer)
            return;
        this.texture = TexturePackPage.getTexture(texture);
        this.highlight = TexturePackPage.getTexture(highlight);
        this.overicon = Texture.getSharedTexture(overicon);
        this.handler = handler;
        this.name = name;
        if(this.texture == null)
        {
            this.texture = Texture.getSharedTexture(texture);
            this.highlight = Texture.getSharedTexture(highlight);
        }
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
        if(display == null)
            return;
        if(!name.equals(display.getClickedValue()))
            mouseOver = false;
    }

    public boolean onMouseUp(int x, int y)
    {
        if(clicked)
            if(display != null)
                display.ButtonClicked(name);
            else
            if(handler != null)
                handler.Selected(name, 0, 0);
        clicked = false;
        return true;
    }

    public void render()
    {
        int dy = 0;
        if(clicked)
            dy++;
        if(mouseOver || name.equals(display.getClickedValue()))
            DrawTextureScaled(highlight, 0, dy, getWidth(), getHeight(), clickedalpha);
        else
            DrawTextureScaled(texture, 0, dy, getWidth(), getHeight(), notclickedAlpha);
        if(overicon != null)
            DrawTextureScaled(overicon, 0, dy, overicon.getWidth(), overicon.getHeight(), 1.0F);
        super.render();
    }

    public void update()
    {
        super.update();
    }

    boolean clicked;
    UIElement display;
    Texture highlight;
    Texture overicon;
    boolean mouseOver;
    String name;
    Texture texture;
    UIEventHandler handler;
    public float notclickedAlpha;
    public float clickedalpha;
}
