// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoZombieGiblets.java

package zombie.iso.objects;

import zombie.GameTime;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.core.textures.ColorInfo;
import zombie.iso.*;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteInstance;

public class IsoZombieGiblets extends IsoPhysicsObject
{

	/*    */   public static enum GibletType
	/*    */   {
	/* 33 */     A, 
	/* 34 */     B, 
	/* 35 */     Eye;
	/*    */   }


    public IsoZombieGiblets(IsoCell cell)
    {
        super(cell);
        tintb = 1.0F;
        tintg = 1.0F;
        tintr = 1.0F;
        time = 0.0F;
    }

    public boolean Serialize()
    {
        return false;
    }

    public String getObjectName()
    {
        return "ZombieGiblets";
    }

    public void update()
    {
        super.update();
        time += GameTime.instance.getMultipliedSecondsSinceLastUpdate();
        if(velX == 0.0F && velY == 0.0F && getZ() == (float)(int)getZ())
        {
            setCollidable(false);
            IsoWorld.instance.CurrentCell.getRemoveList().add(this);
        }
    }

    public void render(float x, float y, float z, ColorInfo info, boolean bDoAttached)
    {
        targetAlpha = sprite.def.targetAlpha = def.targetAlpha = 1.0F - time / 1.0F;
        if(targetAlpha < 0.0F)
            targetAlpha = 0.0F;
        if(targetAlpha > 1.0F)
            targetAlpha = 1.0F;
        super.render(x, y, z, info, bDoAttached);
    }

    public IsoZombieGiblets(GibletType type, IsoCell cell, float x, float y, float z, float xvel, float yvel)
    {
        super(cell);
        tintb = 1.0F;
        tintg = 1.0F;
        tintr = 1.0F;
        time = 0.0F;
        velX = xvel;
        velY = yvel;
        float randX = (float)Rand.Next(4000) / 10000F;
        float randY = (float)Rand.Next(4000) / 10000F;
        randX -= 0.2F;
        randY -= 0.2F;
        velX += randX;
        velY += randY;
        this.x = x;
        this.y = y;
        this.z = z;
        nx = x;
        ny = y;
        alpha = 0.5F;
        def = new IsoSpriteInstance(sprite);
        def.alpha = 0.4F;
        sprite.def.alpha = 0.4F;
        offsetX = -26F;
        offsetY = -242F;



        switch(type.ordinal())
        {
        case 1: // '\001'
            sprite.LoadFramesNoDirPage("Giblet", "00", 3);
            break;

        case 2: // '\002'
            sprite.LoadFramesNoDirPage("Giblet", "01", 3);
            break;

        case 3: // '\003'
            sprite.LoadFramesNoDirPage("Eyeball", "00", 1);
            break;
        }
    }

    public float tintb;
    public float tintg;
    public float tintr;
    public float time;
}
