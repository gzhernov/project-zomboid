// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoSpriteInstance.java

package zombie.iso.sprite;

import zombie.core.textures.ColorInfo;
import zombie.iso.IsoDirections;
import zombie.iso.IsoObject;

// Referenced classes of package zombie.iso.sprite:
//            IsoSprite

public class IsoSpriteInstance
{

    public void setFrameSpeedPerFrame(float perSecond)
    {
        AnimFrameIncrease = perSecond * multiplier;
    }

    public int getID()
    {
        return parentSprite.ID;
    }

    public IsoSpriteInstance(IsoSprite spr)
    {
        tintb = 1.0F;
        tintg = 1.0F;
        tintr = 1.0F;
        Frame = 0.0F;
        alpha = 1.0F;
        targetAlpha = 1.0F;
        bCopyTargetAlpha = true;
        offZ = 0.0F;
        offX = 0.0F;
        offY = 0.0F;
        AnimFrameIncrease = 1.0F;
        Looped = true;
        Finished = false;
        parentSprite = spr;
    }

    public void render(IsoObject obj, float x, float y, float z, IsoDirections dir, float offsetX, float offsetY, 
            ColorInfo info2)
    {
        parentSprite.render(this, obj, x, y, z, dir, offsetX, offsetY, info2);
    }

    public void SetAlpha(float f)
    {
        alpha = f;
        bCopyTargetAlpha = false;
    }

    public void SetTargetAlpha(float targetAlpha)
    {
        this.targetAlpha = targetAlpha;
        bCopyTargetAlpha = false;
    }

    public void update()
    {
    }

    protected void renderprep(IsoObject obj)
    {
        if(obj != null && bCopyTargetAlpha)
        {
            targetAlpha = obj.targetAlpha;
            alpha = obj.alpha;
        } else
        {
            if(alpha < targetAlpha)
            {
                alpha += IsoSprite.alphaStep;
                if(alpha > targetAlpha)
                    alpha = targetAlpha;
            } else
            if(alpha > targetAlpha)
            {
                alpha -= IsoSprite.alphaStep;
                if(alpha < targetAlpha)
                    alpha = targetAlpha;
            }
            if(alpha < 0.0F)
                alpha = 0.0F;
            if(alpha > 1.0F)
                alpha = 1.0F;
        }
    }

    public void Dispose()
    {
        parentSprite.Dispose();
    }

    public IsoSprite parentSprite;
    public float tintb;
    public float tintg;
    public float tintr;
    public float Frame;
    public float alpha;
    public float targetAlpha;
    public boolean bCopyTargetAlpha;
    public boolean Flip;
    public float offZ;
    public float offX;
    public float offY;
    public float AnimFrameIncrease;
    static float multiplier = 1.0F;
    public boolean Looped;
    public boolean Finished;

}
