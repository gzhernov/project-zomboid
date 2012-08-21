// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoPushableObject.java

package zombie.iso;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import zombie.core.Collections.NulledArrayList;
import zombie.inventory.ItemContainer;
import zombie.iso.objects.IsoWheelieBin;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;

// Referenced classes of package zombie.iso:
//            IsoMovingObject, IsoCell, IsoGridSquare, IsoWorld

public class IsoPushableObject extends IsoMovingObject
{

    public String getObjectName()
    {
        return "Pushable";
    }

    public IsoPushableObject(IsoCell cell)
    {
        super(cell);
        velx = 0.0F;
        vely = 0.0F;
        carryCapacity = 100;
        emptyWeight = 4.5F;
        connectList = null;
        ox = 0.0F;
        oy = 0.0F;
        getCell().getPushableObjectList().add(this);
    }

    public IsoPushableObject(IsoCell cell, int x, int y, int z)
    {
        super(cell, true);
        velx = 0.0F;
        vely = 0.0F;
        carryCapacity = 100;
        emptyWeight = 4.5F;
        connectList = null;
        ox = 0.0F;
        oy = 0.0F;
        getCell().getPushableObjectList().add(this);
    }

    public IsoPushableObject(IsoCell cell, IsoGridSquare square, IsoSprite spr)
    {
        super(cell, square, spr, true);
        velx = 0.0F;
        vely = 0.0F;
        carryCapacity = 100;
        emptyWeight = 4.5F;
        connectList = null;
        ox = 0.0F;
        oy = 0.0F;
        setX((float)square.getX() + 0.5F);
        setY((float)square.getY() + 0.5F);
        setZ(square.getZ());
        ox = getX();
        oy = getY();
        setNx(getX());
        setNy(getNy());
        offsetX = -26F;
        offsetY = -258F;
        setWeight(6F);
        this.square = square;
        setCurrent(square);
        getCurrentSquare().getMovingObjects().add(this);
        Collidable = true;
        solid = true;
        shootable = false;
        width = 0.5F;
        alpha = 0.0F;
        targetAlpha = 0.0F;
        getCell().getPushableObjectList().add(this);
    }

    public void update()
    {
        float a = 0.0F;
        if(connectList != null)
        {
            Iterator it = connectList.iterator();
            do
            {
                if(!it.hasNext())
                    break;
                IsoPushableObject p = (IsoPushableObject)it.next();
                if(p.alpha > a)
                    a = p.alpha;
            } while(true);
            alpha = a;
            targetAlpha = a;
        }
        seperate();
        super.update();
    }

    public void load(ByteBuffer input)
        throws IOException
    {
        super.load(input);
        if(!(this instanceof IsoWheelieBin))
            sprite = IsoWorld.instance.spriteManager.getSprite(input.getInt());
        if(input.get() == 1)
        {
            container = new ItemContainer();
            container.load(input);
        }
    }

    public void save(ByteBuffer output)
        throws IOException
    {
        super.save(output);
        if(!(this instanceof IsoWheelieBin))
            output.putInt(sprite.ID);
        if(container != null)
        {
            output.put((byte)1);
            container.save(output);
        } else
        {
            output.put((byte)0);
        }
    }

    public float getWeight(float x, float y)
    {
        if(container == null)
            return emptyWeight;
        float capacityDelta = (float)container.getWeight() / (float)carryCapacity;
        if(capacityDelta < 0.0F)
            capacityDelta = 0.0F;
        if(capacityDelta > 1.0F)
        {
            return getWeight() * 8F;
        } else
        {
            float weight = getWeight() * capacityDelta + emptyWeight;
            return weight;
        }
    }

    public boolean Serialize()
    {
        return true;
    }

    public void DoCollideNorS()
    {
        if(connectList == null)
        {
            super.DoCollideNorS();
            return;
        }
        Iterator it = connectList.iterator();
        do
        {
            if(!it.hasNext())
                break;
            IsoPushableObject p = (IsoPushableObject)it.next();
            if(p != this)
            {
                if(p.ox < ox)
                {
                    p.setNx(getNx() - 1.0F);
                    p.setX(getX() - 1.0F);
                } else
                if(p.ox > ox)
                {
                    p.setNx(getNx() + 1.0F);
                    p.setX(getX() + 1.0F);
                } else
                {
                    p.setNx(getNx());
                    p.setX(getX());
                }
                if(p.oy < oy)
                {
                    p.setNy(getNy() - 1.0F);
                    p.setY(getY() - 1.0F);
                } else
                if(p.oy > oy)
                {
                    p.setNy(getNy() + 1.0F);
                    p.setY(getY() + 1.0F);
                } else
                {
                    p.setNy(getNy());
                    p.setY(getY());
                }
                p.setImpulsex(getImpulsex());
                p.setImpulsey(getImpulsey());
            }
        } while(true);
    }

    public void DoCollideWorE()
    {
        if(connectList == null)
        {
            super.DoCollideWorE();
            return;
        }
        Iterator it = connectList.iterator();
        do
        {
            if(!it.hasNext())
                break;
            IsoPushableObject p = (IsoPushableObject)it.next();
            if(p != this)
            {
                if(p.ox < ox)
                {
                    p.setNx(getNx() - 1.0F);
                    p.setX(getX() - 1.0F);
                } else
                if(p.ox > ox)
                {
                    p.setNx(getNx() + 1.0F);
                    p.setX(getX() + 1.0F);
                } else
                {
                    p.setNx(getNx());
                    p.setX(getX());
                }
                if(p.oy < oy)
                {
                    p.setNy(getNy() - 1.0F);
                    p.setY(getY() - 1.0F);
                } else
                if(p.oy > oy)
                {
                    p.setNy(getNy() + 1.0F);
                    p.setY(getY() + 1.0F);
                } else
                {
                    p.setNy(getNy());
                    p.setY(getY());
                }
                p.setImpulsex(getImpulsex());
                p.setImpulsey(getImpulsey());
            }
        } while(true);
    }

    float velx;
    float vely;
    public int carryCapacity;
    public float emptyWeight;
    public NulledArrayList connectList;
    public float ox;
    public float oy;
}
