// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ObjectTooltip.java

package zombie.ui;

import zombie.core.textures.Texture;
import zombie.core.textures.TexturePackPage;
import zombie.inventory.InventoryItem;
import zombie.iso.IsoObject;

// Referenced classes of package zombie.ui:
//            UIElement

public class ObjectTooltip extends UIElement
{

    public ObjectTooltip()
    {
        bIsItem = false;
        Item = null;
        alpha = 0.0F;
        showDelay = 0;
        targetAlpha = 0.0F;
        texture = TexturePackPage.getTexture("black");
        width = 130;
        height = 130;
        defaultDraw = false;
    }

    public void DrawValueRight(int value, int x, int y, boolean highGood)
    {
        Integer val = Integer.valueOf(value);
        String str = val.toString();
        float r = 0.3F;
        float g = 1.0F;
        float b = 0.2F;
        float a = 1.0F;
        if(value > 0)
            str = (new StringBuilder()).append("+").append(str).toString();
        if(value < 0 && highGood || value > 0 && !highGood)
        {
            r = 0.8F;
            g = 0.3F;
            b = 0.2F;
        }
        DrawTextRight(str, x, y, r, g, b, a);
    }

    public void DrawValueRightNoPlus(int value, int x, int y)
    {
        Integer val = Integer.valueOf(value);
        String str = val.toString();
        float r = 1.0F;
        float g = 1.0F;
        float b = 1.0F;
        float a = 1.0F;
        DrawTextRight(str, x, y, r, g, b, a);
    }

    public void DrawValueRightNoPlus(float value, int x, int y)
    {
        Float val = Float.valueOf(value);
        val = Float.valueOf((float)(int)(val.floatValue() * 10F) / 10F);
        String str = val.toString();
        float r = 1.0F;
        float g = 1.0F;
        float b = 1.0F;
        float a = 1.0F;
        DrawTextRight(str, x, y, r, g, b, a);
    }

    public boolean onMouseMove(int dx, int dy)
    {
        setX(getX() + (float)dx);
        setY(getY() + (float)dy);
        return false;
    }

    public void onMouseMoveOutside(int dx, int dy)
    {
        setX(getX() + (float)dx);
        setY(getY() + (float)dy);
    }

    public void render()
    {
        if(alpha <= 0.0F)
            return;
        DrawTextureScaled(texture, 0, 0, getWidth(), getHeight(), alpha);
        if(bIsItem)
        {
            if(Item != null)
                Item.DoTooltip(this);
        } else
        if(Object != null)
            Object.DoTooltip(this);
        super.render();
    }

    public void show(IsoObject obj, int x, int y)
    {
        bIsItem = false;
        Object = obj;
        setX(x);
        setY(y);
        targetAlpha = 0.5F;
        showDelay = 15;
        alpha = 0.0F;
    }

    public void update()
    {
        showDelay--;
        if(alpha <= 0.0F && targetAlpha == 0.0F)
            return;
        if(showDelay > 0)
            return;
        if(alpha < targetAlpha)
        {
            alpha += alphaStep;
            if(alpha > targetAlpha)
                alpha = targetAlpha;
        } else
        if(alpha > targetAlpha)
        {
            alpha -= alphaStep;
            if(alpha < targetAlpha)
                alpha = targetAlpha;
        }
    }

    void show(InventoryItem info, int i, int i0)
    {
        Object = null;
        Item = info;
        bIsItem = true;
        setX(getX());
        setY(getY());
        targetAlpha = 0.5F;
        showDelay = 15;
        alpha = 0.0F;
    }

    public static float alphaStep = 0.05F;
    public boolean bIsItem;
    public InventoryItem Item;
    public IsoObject Object;
    float alpha;
    int showDelay;
    float targetAlpha;
    Texture texture;

}
