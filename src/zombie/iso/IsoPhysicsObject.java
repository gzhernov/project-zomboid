// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoPhysicsObject.java

package zombie.iso;

import zombie.iso.sprite.IsoSprite;

// Referenced classes of package zombie.iso:
//            IsoMovingObject, IsoGridSquare, IsoCell

public class IsoPhysicsObject extends IsoMovingObject
{

    public IsoPhysicsObject(IsoCell cell)
    {
        super(cell);
        speedMod = 1.0F;
        velX = 0.0F;
        velY = 0.0F;
        velZ = 0.0F;
        terminalVelocity = -0.05F;
        solid = false;
        shootable = false;
    }

    public void collideGround()
    {
    }

    public void collideWall()
    {
    }

    public void update()
    {
        super.update();
        if(isCollidedThisFrame())
        {
            if(isCollidedN() || isCollidedS())
            {
                velY = -velY;
                velY *= 0.5F;
                collideWall();
            }
            if(isCollidedE() || isCollidedW())
            {
                velX = -velX;
                velX *= 0.5F;
                collideWall();
            }
        }
        float remove = 0.1F * speedMod;
        remove = 1.0F - remove;
        velX *= remove;
        velY *= remove;
        velZ -= 0.005F;
        if(velZ < terminalVelocity)
            velZ = terminalVelocity;
        setNx(getNx() + velX * speedMod * 0.3F);
        setNy(getNy() + velY * speedMod * 0.3F);
        setZ(getZ() + velZ * 0.4F);
        if(getZ() < 0.0F)
        {
            setZ(0.0F);
            velZ = -velZ * 0.5F;
            collideGround();
        }
        if(getCurrentSquare() != null && (int)getZ() < (int)getLz() && getCurrentSquare().TreatAsSolidFloor())
        {
            setZ((int)getLz());
            velZ = 0.0F;
            collideGround();
        }
        if(Math.abs(velX) < 0.0001F)
            velX = 0.0F;
        if(Math.abs(velY) < 0.0001F)
            velY = 0.0F;
        if(velX + velY == 0.0F)
            sprite.Animate = false;
    }

    public float speedMod;
    public float velX;
    public float velY;
    public float velZ;
    public float terminalVelocity;
}
