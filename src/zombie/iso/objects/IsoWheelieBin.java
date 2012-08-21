// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoWheelieBin.java

package zombie.iso.objects;

import zombie.characters.IsoPlayer;
import zombie.inventory.ItemContainer;
import zombie.iso.*;
import zombie.iso.sprite.IsoSprite;

public class IsoWheelieBin extends IsoPushableObject
{

    public String getObjectName()
    {
        return "WheelieBin";
    }

    public IsoWheelieBin(IsoCell cell)
    {
        super(cell);
        velx = 0.0F;
        vely = 0.0F;
        container = new ItemContainer("wheeliebin", square, this, 8, 8);
        Collidable = true;
        solid = true;
        shootable = false;
        width = 0.3F;
        dir = IsoDirections.E;
        alpha = 0.0F;
        targetAlpha = 0.0F;
        offsetX = -26F;
        offsetY = -248F;
        OutlineOnMouseover = true;
        sprite.LoadFramesPageSimple("TileObjectsExt_7", "TileObjectsExt_5", "TileObjectsExt_6", "TileObjectsExt_8");
    }

    public IsoWheelieBin(IsoCell cell, int x, int y, int z)
    {
        super(cell, x, y, z);
        velx = 0.0F;
        vely = 0.0F;
        this.x = (float)x + 0.5F;
        this.y = (float)y + 0.5F;
        this.z = z;
        nx = this.x;
        ny = this.y;
        offsetX = -26F;
        offsetY = -248F;
        weight = 6F;
        sprite.LoadFramesPageSimple("TileObjectsExt_7", "TileObjectsExt_5", "TileObjectsExt_6", "TileObjectsExt_8");
        square = getCell().getGridSquare(x, y, z);
        current = getCell().getGridSquare(x, y, z);
        container = new ItemContainer("wheeliebin", square, this, 8, 8);
        Collidable = true;
        solid = true;
        shootable = false;
        width = 0.3F;
        dir = IsoDirections.E;
        alpha = 0.0F;
        targetAlpha = 0.0F;
        OutlineOnMouseover = true;
    }

    public void update()
    {
        velx = getX() - getLx();
        vely = getY() - getLy();
        float capacityDelta = 1.0F - (float)container.getWeight() / 500F;
        if(capacityDelta < 0.0F)
            capacityDelta = 0.0F;
        if(capacityDelta < 0.7F)
            capacityDelta *= capacityDelta;
        if(IsoPlayer.getInstance().getDragObject() != this)
        {
            if(velx != 0.0F && vely == 0.0F && (dir == IsoDirections.E || dir == IsoDirections.W))
                setNx(getNx() + velx * 0.65F * capacityDelta);
            if(vely != 0.0F && velx == 0.0F && (dir == IsoDirections.N || dir == IsoDirections.S))
                setNy(getNy() + vely * 0.65F * capacityDelta);
        }
        super.update();
    }

    public float getWeight(float x, float y)
    {
        float capacityDelta = (float)container.getWeight() / 500F;
        if(capacityDelta < 0.0F)
            capacityDelta = 0.0F;
        if(capacityDelta > 1.0F)
            return getWeight() * 8F;
        float weight = getWeight() * capacityDelta + 1.5F;
        if(dir == IsoDirections.W || dir == IsoDirections.E && y == 0.0F)
            return weight / 2.0F;
        if(dir == IsoDirections.N || dir == IsoDirections.S && x == 0.0F)
            return weight / 2.0F;
        else
            return weight * 3F;
    }

    float velx;
    float vely;
}
