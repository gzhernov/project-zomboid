// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoBloodDrop.java

package zombie.iso.objects;

import zombie.GameTime;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.core.textures.ColorInfo;
import zombie.iso.*;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteInstance;

public class IsoBloodDrop extends IsoPhysicsObject
{

    public IsoBloodDrop(IsoCell cell)
    {
        super(cell);
        tintb = 1.0F;
        tintg = 1.0F;
        tintr = 1.0F;
        time = 0.0F;
        sx = 0.0F;
        sy = 0.0F;
        lsx = 0.0F;
        lsy = 0.0F;
    }

    public boolean Serialize()
    {
        return false;
    }

    public String getObjectName()
    {
        return "ZombieGiblets";
    }

    public void collideGround()
    {
        float lx = x - (float)(int)x;
        float ly = y - (float)(int)y;
        IsoGridSquare sq = IsoWorld.instance.CurrentCell.getGridSquare((int)x, (int)y, (int)z);
        if(sq != null)
        {
            IsoObject floor = sq.getFloor();
            floor.addChild(this);
            setCollidable(false);
            IsoWorld.instance.CurrentCell.getRemoveList().add(this);
        }
    }

    public void collideWall()
    {
        IsoGridSquare sq = IsoWorld.instance.CurrentCell.getGridSquare((int)x, (int)y, (int)z);
        if(sq != null)
        {
            IsoObject floor = null;
            if(isCollidedN())
                floor = sq.getWall(true);
            else
            if(isCollidedS())
            {
                sq = IsoWorld.instance.CurrentCell.getGridSquare((int)x, (int)y + 1, (int)z);
                if(sq != null)
                    floor = sq.getWall(true);
            } else
            if(isCollidedW())
                floor = sq.getWall(false);
            else
            if(isCollidedE())
            {
                sq = IsoWorld.instance.CurrentCell.getGridSquare((int)x + 1, (int)y, (int)z);
                if(sq != null)
                    floor = sq.getWall(false);
            }
            if(floor != null)
            {
                floor.addChild(this);
                setCollidable(false);
                IsoWorld.instance.CurrentCell.getRemoveList().add(this);
            }
        }
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
        targetAlpha = 0.3F;
        if(targetAlpha < 0.0F)
            targetAlpha = 0.0F;
        if(targetAlpha > 1.0F)
            targetAlpha = 1.0F;
        sprite.render(this, x, y, z, dir, offsetX, offsetY, info);
    }

    public IsoBloodDrop(IsoCell cell, float x, float y, float z, float xvel, float yvel)
    {
        super(cell);
        tintb = 1.0F;
        tintg = 1.0F;
        tintr = 1.0F;
        time = 0.0F;
        sx = 0.0F;
        sy = 0.0F;
        lsx = 0.0F;
        lsy = 0.0F;
        velX = xvel * 2.0F;
        velY = yvel * 2.0F;
        terminalVelocity = -0.1F;
        velZ += ((float)Rand.Next(10000) / 10000F - 0.5F) * 0.05F;
        float res = (float)Rand.Next(9000) / 10000F;
        res += 0.1F;
        velX *= res;
        velY *= res;
        velZ += res * 0.05F;
        if(Rand.Next(7) == 0)
        {
            velX *= 2.0F;
            velY *= 2.0F;
        }
        velX *= 0.8F;
        velY *= 0.8F;
        temp.x = velX;
        temp.y = velY;
        temp.rotate(((float)Rand.Next(1000) / 1000F - 0.5F) * 0.07F);
        if(Rand.Next(3) == 0)
            temp.rotate(((float)Rand.Next(1000) / 1000F - 0.5F) * 0.1F);
        if(Rand.Next(5) == 0)
            temp.rotate(((float)Rand.Next(1000) / 1000F - 0.5F) * 0.2F);
        if(Rand.Next(8) == 0)
            temp.rotate(((float)Rand.Next(1000) / 1000F - 0.5F) * 0.3F);
        if(Rand.Next(10) == 0)
            temp.rotate(((float)Rand.Next(1000) / 1000F - 0.5F) * 0.4F);
        velX = temp.x;
        velY = temp.y;
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
        offsetX += 8F;
        offsetY += 9F;
        sprite.LoadFramesNoDirPageSimple("BloodSplat");
    }

    public float tintb;
    public float tintg;
    public float tintr;
    public float time;
    float sx;
    float sy;
    float lsx;
    float lsy;
    static Vector2 temp = new Vector2();

}
