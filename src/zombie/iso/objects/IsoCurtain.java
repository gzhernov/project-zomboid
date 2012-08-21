// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoCurtain.java

package zombie.iso.objects;

import java.io.IOException;
import java.nio.ByteBuffer;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.core.textures.ColorInfo;
import zombie.iso.*;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.sprite.IsoSprite;
import zombie.ui.UIManager;

public class IsoCurtain extends IsoObject
{

    public IsoCurtain(IsoCell cell, IsoGridSquare gridSquare, IsoSprite gid, boolean north)
    {
        Barricaded = false;
        BarricideMaxStrength = Integer.valueOf(0);
        BarricideStrength = Integer.valueOf(0);
        Health = Integer.valueOf(1000);
        Locked = false;
        MaxHealth = Integer.valueOf(1000);
        PushedMaxStrength = Integer.valueOf(0);
        PushedStrength = Integer.valueOf(0);
        this.north = false;
        open = false;
        destroyed = false;
        OutlineOnMouseover = true;
        PushedMaxStrength = PushedStrength = Integer.valueOf(2500);
        closedSprite = IsoSprite.getSprite(getCell().SpriteManager, gid, -4);
        openSprite = gid;
        open = true;
        sprite = openSprite;
        square = gridSquare;
        this.north = north;
        DirtySlice();
        if(Rand.Next(3) == 0)
            ToggleDoorSilent();
    }

    public IsoCurtain(IsoCell cell, IsoGridSquare gridSquare, String gid, boolean north)
    {
        Barricaded = false;
        BarricideMaxStrength = Integer.valueOf(0);
        BarricideStrength = Integer.valueOf(0);
        Health = Integer.valueOf(1000);
        Locked = false;
        MaxHealth = Integer.valueOf(1000);
        PushedMaxStrength = Integer.valueOf(0);
        PushedStrength = Integer.valueOf(0);
        this.north = false;
        open = false;
        destroyed = false;
        OutlineOnMouseover = true;
        PushedMaxStrength = PushedStrength = Integer.valueOf(2500);
        closedSprite = IsoSprite.getSprite(getCell().SpriteManager, gid, -4);
        openSprite = IsoSprite.getSprite(getCell().SpriteManager, gid, 0);
        open = true;
        sprite = openSprite;
        square = gridSquare;
        this.north = north;
        DirtySlice();
        if(Rand.Next(3) == 0)
            ToggleDoorSilent();
    }

    public IsoCurtain(IsoCell cell)
    {
        super(cell);
        Barricaded = false;
        BarricideMaxStrength = Integer.valueOf(0);
        BarricideStrength = Integer.valueOf(0);
        Health = Integer.valueOf(1000);
        Locked = false;
        MaxHealth = Integer.valueOf(1000);
        PushedMaxStrength = Integer.valueOf(0);
        PushedStrength = Integer.valueOf(0);
        north = false;
        open = false;
        destroyed = false;
    }

    public String getObjectName()
    {
        return "Curtain";
    }

    public void load(ByteBuffer input)
        throws IOException
    {
        super.load(input);
        open = input.get() == 1;
        north = input.get() == 1;
        Health = Integer.valueOf(input.getInt());
        BarricideStrength = Integer.valueOf(input.getInt());
        if(open)
        {
            closedSprite = IsoSprite.getSprite(IsoWorld.instance.spriteManager, input.getInt());
            openSprite = sprite;
        } else
        {
            openSprite = IsoSprite.getSprite(IsoWorld.instance.spriteManager, input.getInt());
            closedSprite = sprite;
        }
    }

    public void save(ByteBuffer output)
        throws IOException
    {
        super.save(output);
        output.put(((byte)(open ? 1 : 0)));
        output.put(((byte)(north ? 1 : 0)));
        output.putInt(Health.intValue());
        output.putInt(BarricideStrength.intValue());
        if(open)
            output.putInt(closedSprite.ID);
        else
            output.putInt(openSprite.ID);
    }

    public boolean IsOpen()
    {
        return open;
    }

    public boolean onMouseLeftClick(int x, int y)
    {
        if(IsoUtils.DistanceTo(square.getX(), square.getY(), IsoPlayer.getInstance().getX(), IsoPlayer.getInstance().getY()) < 2.0F && (float)square.getZ() == IsoPlayer.getInstance().getZ())
        {
            if(UIManager.getDragInventory() == null);
            if(Barricaded)
            {
                return false;
            } else
            {
                ToggleDoor(IsoPlayer.getInstance());
                return true;
            }
        } else
        {
            return false;
        }
    }

    public zombie.iso.IsoObject.VisionResult TestVision(IsoGridSquare from, IsoGridSquare to)
    {
        if(to.getZ() != from.getZ())
            return zombie.iso.IsoObject.VisionResult.NoEffect;
        if(from == square && (getType() == IsoObjectType.curtainW || getType() == IsoObjectType.curtainN) || from != square && (getType() == IsoObjectType.curtainE || getType() == IsoObjectType.curtainS))
        {
            if(north && to.getY() < from.getY() && !open)
                return zombie.iso.IsoObject.VisionResult.Blocked;
            if(!north && to.getX() < from.getX() && !open)
                return zombie.iso.IsoObject.VisionResult.Blocked;
        } else
        {
            if(north && to.getY() > from.getY() && !open)
                return zombie.iso.IsoObject.VisionResult.Blocked;
            if(!north && to.getX() > from.getX() && !open)
                return zombie.iso.IsoObject.VisionResult.Blocked;
        }
        return zombie.iso.IsoObject.VisionResult.NoEffect;
    }

    public void ToggleDoor(IsoGameCharacter chr)
    {
        if(Barricaded)
            return;
        DirtySlice();
        if(Locked && chr != null && chr.getCurrentSquare().getRoom() == null && !open)
            return;
        for(int x = 0; x < 200; x++)
        {
            for(int y = 0; y < 200; y++)
            {
                for(int z = 0; z < 32; z++)
                    LosUtil.cachedresults[x][y][z] = 0;

            }

        }

        LosUtil.cachecleared = true;
        IsoGridSquare.setRecalcLightTime(-1);
        for(int n = 0; n < square.getLights().size(); n++)
        {
            ((IsoLightSource)square.getLights().get(n)).bActive = !((IsoLightSource)square.getLights().get(n)).bActive;
            ((IsoLightSource)square.getLights().get(n)).update();
        }

        open = !open;
        for(int n = 0; n < square.getLights().size(); n++)
        {
            ((IsoLightSource)square.getLights().get(n)).bActive = !((IsoLightSource)square.getLights().get(n)).bActive;
            ((IsoLightSource)square.getLights().get(n)).update();
        }

        sprite = closedSprite;
        if(open)
            sprite = openSprite;
    }

    public void ToggleDoorSilent()
    {
        if(Barricaded)
            return;
        DirtySlice();
        for(int x = 0; x < 200; x++)
        {
            for(int y = 0; y < 200; y++)
            {
                for(int z = 0; z < 32; z++)
                    LosUtil.cachedresults[x][y][z] = 0;

            }

        }

        LosUtil.cachecleared = true;
        IsoGridSquare.setRecalcLightTime(-1);
        for(int n = 0; n < square.getLights().size(); n++)
        {
            ((IsoLightSource)square.getLights().get(n)).bActive = !((IsoLightSource)square.getLights().get(n)).bActive;
            ((IsoLightSource)square.getLights().get(n)).update();
        }

        open = !open;
        for(int n = 0; n < square.getLights().size(); n++)
        {
            ((IsoLightSource)square.getLights().get(n)).bActive = !((IsoLightSource)square.getLights().get(n)).bActive;
            ((IsoLightSource)square.getLights().get(n)).update();
        }

        sprite = closedSprite;
        if(open)
            sprite = openSprite;
    }

    public void render(float x, float y, float z, ColorInfo col)
    {
        super.render(x, y, z, col, true);
    }

    public boolean Barricaded;
    public Integer BarricideMaxStrength;
    public Integer BarricideStrength;
    public Integer Health;
    public boolean Locked;
    public Integer MaxHealth;
    public Integer PushedMaxStrength;
    public Integer PushedStrength;
    IsoSprite closedSprite;
    public boolean north;
    public boolean open;
    IsoSprite openSprite;
    private boolean destroyed;
}
