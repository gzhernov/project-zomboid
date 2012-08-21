// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoRainSplash.java

package zombie.iso.objects;

import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.core.properties.PropertyContainer;
import zombie.core.textures.ColorInfo;
import zombie.iso.*;
import zombie.iso.SpriteDetails.IsoFlagType;

// Referenced classes of package zombie.iso.objects:
//            RainManager

public class IsoRainSplash extends IsoObject
{

    public boolean Serialize()
    {
        return false;
    }

    public IsoRainSplash(IsoCell cell, IsoGridSquare gridSquare)
    {
        if(gridSquare == null)
            return;
        if(gridSquare.getProperties().Is(IsoFlagType.HasRainSplashes))
            return;
        Age = 0;
        square = gridSquare;
        int NumRainSplashParticles = 1 + Rand.Next(2);
        for(int i = 0; i < NumRainSplashParticles; i++)
            AttachAnim("Rain", "00", 4, RainManager.RainSplashAnimDelay, -16 + (-16 + Rand.Next(32)), (-85 + (-16 + Rand.Next(32))) - 32, true, 0, false, 0.7F, RainManager.RainSplashTintMod);

        gridSquare.getProperties().Set(IsoFlagType.HasRainSplashes, "Has Rain Splashes");
        RainManager.AddRainSplash(this);
    }

    public boolean HasTooltip()
    {
        return false;
    }

    public boolean TestCollide(IsoMovingObject obj, IsoGridSquare PassedObjectSquare)
    {
        return square == PassedObjectSquare;
    }

    public zombie.iso.IsoObject.VisionResult TestVision(IsoGridSquare from, IsoGridSquare to)
    {
        return zombie.iso.IsoObject.VisionResult.NoEffect;
    }

    public void ChangeTintMod(ColorInfo NewTintMod)
    {
        if(AttachedAnimSprite != null)
        {
            for(int i = 0; i < AttachedAnimSprite.size(); i++);
        }
    }

    public void update()
    {
        Age++;
        if(AttachedAnimSprite != null)
        {
            for(int i = 0; i < AttachedAnimSprite.size(); i++)
            {
                Integer NewX = Integer.valueOf(-16 + (-16 + Rand.Next(32)));
                Integer NewY = Integer.valueOf((-85 + (-16 + Rand.Next(32))) - 32);
            }

        }
    }

    void Reset(IsoGridSquare gridSquare)
    {
        if(gridSquare == null)
            return;
        if(gridSquare.getProperties().Is(IsoFlagType.HasRainSplashes))
            return;
        Age = 0;
        square = gridSquare;
        int NumRainSplashParticles = 1 + Rand.Next(2);
        if(AttachedAnimSprite != null)
        {
            for(int i = 0; i < AttachedAnimSprite.size(); i++);
        }
        gridSquare.getProperties().Set(IsoFlagType.HasRainSplashes, "Has Rain Splashes");
        RainManager.AddRainSplash(this);
    }

    public int Age;
}
