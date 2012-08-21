// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StatBar.java

package zombie.ui;

import zombie.core.Color;
import zombie.core.textures.Texture;

// Referenced classes of package zombie.ui:
//            UIElement

public class StatBar extends UIElement
{

    public StatBar(int x, int y, Texture background, Texture foreground, boolean vert, Color col)
    {
        deltaValue = 1.0F;
        vertical = false;
        this.col = col;
        vertical = vert;
        this.background = background;
        this.foreground = foreground;
        this.x = x;
        this.y = y;
        width = background.getWidth();
        height = background.getHeight();
    }

    public void render()
    {
        DrawUVSliceTexture(background, 0, 0, background.getWidth(), background.getHeight(), col, 0.0F, 0.0F, 1.0F, 1.0F);
        if(vertical)
            DrawUVSliceTexture(foreground, 0, 0, foreground.getWidth(), foreground.getHeight(), col, 0.0F, 1.0F - deltaValue, 1.0F, 1.0F);
        else
            DrawUVSliceTexture(foreground, 0, 0, foreground.getWidth(), foreground.getHeight(), col, 0.0F, 0.0F, 1.0F - deltaValue, 1.0F);
    }

    public void setValue(float delta)
    {
        deltaValue = delta;
    }

    Texture background;
    Color col;
    float deltaValue;
    Texture foreground;
    boolean vertical;
}
