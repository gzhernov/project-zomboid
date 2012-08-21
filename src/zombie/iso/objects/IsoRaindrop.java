// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoRaindrop.java

package zombie.iso.objects;

import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.core.properties.PropertyContainer;
import zombie.core.textures.ColorInfo;
import zombie.iso.*;
import zombie.iso.SpriteDetails.IsoFlagType;

// Referenced classes of package zombie.iso.objects:
//            RainManager

public class IsoRaindrop extends IsoObject
{

    public boolean Serialize()
    {
        return false;
    }

    public IsoRaindrop(IsoCell cell, IsoGridSquare gridSquare, boolean CanSee)
    {
        if(!CanSee)
            return;
        if(gridSquare == null)
            return;
        if(gridSquare.getProperties().Is(IsoFlagType.HasRaindrop))
            return;
        Life = 0;
        square = gridSquare;
        OffsetY = ((-85F + (float)(-16 + Rand.Next(32))) - 32F) + (float)(int)RainManager.RaindropStartDistance;
        if(CanSee)
            AttachAnim("Raindrop", "00", 1, 0, -16 + (-16 + Rand.Next(32)), (int)OffsetY, true, 0, false, 0.7F, RainManager.RaindropTintMod);
        else
            AttachAnim("Raindrop", "00", 1, 0, -16 + (-16 + Rand.Next(32)), (int)OffsetY, true, 0, false, 0.7F, RainManager.DarkRaindropTintMod);
        if(AttachedAnimSprite != null)
            AnimSpriteIndex = AttachedAnimSprite.size() - 1;
        else
            AnimSpriteIndex = 0;
        gridSquare.getProperties().Set(IsoFlagType.HasRaindrop, "Has Raindrop");
        Vel_Y = 0.0F;
        float Modulus = 1000000F / (float)Rand.Next(0xf4240) + 1E-005F;
        GravMod = -(RainManager.GravModMin + (RainManager.GravModMax - RainManager.GravModMin) * Modulus);
        if(AttachedAnimSprite == null);
        RainManager.AddRaindrop(this);
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
        Life++;
        Vel_Y += GravMod;
        OffsetY += Vel_Y;
        if(AttachedAnimSprite == null);
        if(OffsetY < -128F)
        {
            OffsetY = ((-85F + (float)(-16 + Rand.Next(32))) - 32F) + (float)(int)RainManager.RaindropStartDistance;
            Vel_Y = 0.0F;
            float Modulus = 1000000F / (float)Rand.Next(0xf4240) + 1E-005F;
            GravMod = -(RainManager.GravModMin + (RainManager.GravModMax - RainManager.GravModMin) * Modulus);
        }
    }

    void Reset(IsoGridSquare gridSquare, boolean CanSee)
    {
        if(!CanSee)
            return;
        if(gridSquare == null)
            return;
        if(gridSquare.getProperties().Is(IsoFlagType.HasRaindrop))
            return;
        Life = 0;
        square = gridSquare;
        OffsetY = ((-85F + (float)(-16 + Rand.Next(32))) - 32F) + (float)(int)RainManager.RaindropStartDistance;
        if(AttachedAnimSprite != null)
            AnimSpriteIndex = AttachedAnimSprite.size() - 1;
        else
            AnimSpriteIndex = 0;
        gridSquare.getProperties().Set(IsoFlagType.HasRaindrop, "Has Raindrop");
        Vel_Y = 0.0F;
        float Modulus = 1000000F / (float)Rand.Next(0xf4240) + 1E-005F;
        GravMod = -(RainManager.GravModMin + (RainManager.GravModMax - RainManager.GravModMin) * Modulus);
        RainManager.AddRaindrop(this);
    }

    public int AnimSpriteIndex;
    public float GravMod;
    public int Life;
    public float OffsetY;
    public float Vel_Y;
}
