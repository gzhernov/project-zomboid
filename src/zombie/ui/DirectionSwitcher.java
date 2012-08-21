// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DirectionSwitcher.java

package zombie.ui;

import zombie.core.Core;
import zombie.core.textures.Texture;

// Referenced classes of package zombie.ui:
//            UIElement

public class DirectionSwitcher extends UIElement
{

    public DirectionSwitcher(int x, int y)
    {
        texture_small = Texture.getSharedTexture("media/ui/Controls_Small_TypeA.png");
        texture2_small = Texture.getSharedTexture("media/ui/Controls_Small_TypeB.png");
        texture = Texture.getSharedTexture("media/ui/Controls_Large_TypeA.png");
        texture2 = Texture.getSharedTexture("media/ui/Controls_Large_TypeB.png");
        this.x = x;
        this.y = y;
        width = texture.getWidth() - 8;
        height = texture.getHeight();
    }

    public boolean onMouseDown(int x, int y)
    {
        Core.getInstance().MoveMethodToggle();
        return true;
    }

    public boolean onMouseMove(int dx, int dy)
    {
        return true;
    }

    public void onMouseMoveOutside(int i, int j)
    {
    }

    public boolean onMouseUp(int x, int y)
    {
        return true;
    }

    public void render()
    {
        if(Core.bAltMoveMethod)
            DrawTextureScaled(texture2, 0, 0, texture2.getWidth(), texture2.getHeight(), 0.85F);
        else
            DrawTextureScaled(texture, 0, 0, texture.getWidth(), texture.getHeight(), 0.85F);
        super.render();
    }

    public void update()
    {
        super.update();
    }

    Texture texture;
    Texture texture2;
    Texture texture_small;
    Texture texture2_small;
}
