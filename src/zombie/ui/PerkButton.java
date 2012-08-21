// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PerkButton.java

package zombie.ui;

import zombie.FrameLoader;
import zombie.core.textures.Texture;

// Referenced classes of package zombie.ui:
//            UIElement, UIEventHandler

public class PerkButton extends UIElement
{

    public PerkButton(String name, int x, int y, Texture texture, boolean bAvailable, boolean bPicked, UIEventHandler handler)
    {
        clicked = false;
        overicon = null;
        mouseOver = false;
        this.bPicked = false;
        this.bAvailable = false;
        notclickedAlpha = 0.85F;
        clickedalpha = 1.0F;
        if(FrameLoader.bServer)
            return;
        this.bAvailable = bAvailable;
        this.bPicked = bPicked;
        this.texture = texture;
        this.handler = handler;
        this.name = name;
        if(this.texture == null)
            this.texture = texture;
        this.x = x;
        this.y = y;
        width = this.texture.getWidth();
        height = this.texture.getHeight();
    }

    public boolean onMouseDown(int x, int y)
    {
        if(bAvailable && !bPicked)
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
        float notclickedalpha = 1.0F;
        if(bAvailable && !bPicked)
            notclickedalpha = 0.5F;
        if(!bAvailable)
            notclickedalpha = 0.2F;
        if(bPicked)
            notclickedalpha = 1.0F;
        DrawTextureScaled(texture, 0, dy, getWidth(), getHeight(), notclickedalpha);
        super.render();
    }

    public void update()
    {
        super.update();
    }

    boolean clicked;
    UIElement display;
    Texture overicon;
    boolean mouseOver;
    String name;
    Texture texture;
    boolean bPicked;
    boolean bAvailable;
    UIEventHandler handler;
    public float notclickedAlpha;
    public float clickedalpha;
}
