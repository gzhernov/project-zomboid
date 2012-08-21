// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoWindow.java

package zombie.iso.objects;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.lwjgl.input.Keyboard;
import zombie.*;
import zombie.characters.CharacterTimedActions.BarricadeAction;
import zombie.characters.CharacterTimedActions.UnbarricadeAction;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;
import zombie.core.properties.PropertyContainer;
import zombie.inventory.*;
import zombie.inventory.types.HandWeapon;
import zombie.iso.*;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.objects.interfaces.Thumpable;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteInstance;
import zombie.network.*;
import zombie.ui.ObjectTooltip;
import zombie.ui.UIManager;

// Referenced classes of package zombie.iso.objects:
//            IsoCurtain

public class IsoWindow extends IsoObject
    implements Thumpable
{

	/*     */   public static enum WindowType
	/*     */   {
	/* 120 */     SinglePane, 
	/* 121 */     DoublePane;
	/*     */   }
	


    public IsoCurtain HasCurtains()
    {
        IsoGridSquare toCheck = getInsideSquare();
        if(!toCheck.getSpecialObjects().isEmpty())
        {
            for(int n = 0; n < toCheck.getSpecialObjects().size(); n++)
                if(toCheck.getSpecialObjects().get(n) instanceof IsoCurtain)
                    return (IsoCurtain)toCheck.getSpecialObjects().get(n);

        }
        return null;
    }

    public void AttackObject(IsoGameCharacter owner)
    {
        super.AttackObject(owner);
        IsoObject o = square.getWall(north);
        if(o != null)
            o.AttackObject(owner);
    }

    public IsoGridSquare getInsideSquare()
    {
        IsoGridSquare toCheck = square;
        if(north)
        {
            if(square.getRoom() == null)
                toCheck = square.getCell().getGridSquare(square.getX(), square.getY() - 1, square.getZ());
            else
                toCheck = square;
        } else
        if(square.getRoom() == null)
            toCheck = square.getCell().getGridSquare(square.getX() - 1, square.getY(), square.getZ());
        else
            toCheck = square;
        return toCheck;
    }

    public IsoWindow(IsoCell cell)
    {
        super(cell);
        Barricaded = 0;
        BarricideMaxStrength = Integer.valueOf(0);
        BarricideStrength = Integer.valueOf(0);
        Health = Integer.valueOf(100);
        MaxHealth = Integer.valueOf(100);
        type = WindowType.SinglePane;
        north = false;
        locked = false;
        open = false;
        destroyed = false;
    }

    public String getObjectName()
    {
        return "Window";
    }

    public void WeaponHit(IsoGameCharacter owner, HandWeapon weapon)
    {
        if(open)
            return;
        if(isDestroyed())
            return;
        DoorInteraction j = new DoorInteraction();
        j.CharacterID = owner.getRemoteID();
        j.interactionType = 4;
        j.x = square.getX();
        j.y = square.getY();
        j.z = square.getZ();
        if(FrameLoader.bServer)
            GameServer.instance.SendToClientsTCP(j);
        if(FrameLoader.bClient)
            GameClient.instance.SendToServerTCP(j);
        if(weapon != null)
            Damage(weapon.getDoorDamage() * 5);
        else
            Damage(100);
        DirtySlice();
        WorldSoundManager.instance.addSound(owner, square.getX(), square.getY(), square.getZ(), 20, 20, false, 0.0F, 15F);
        if(Health.intValue() <= 0)
        {
            SoundManager.instance.PlayWorldSound("smashwindow", square, 0.2F, 20F, 1.1F, true);
            WorldSoundManager.instance.addSound(null, square.getX(), square.getY(), square.getZ(), 10, 20, true, 4F, 15F);
            destroyed = true;
            sprite = smashedSprite;
            closedSprite = smashedSprite;
            square.InvalidateSpecialObjectPaths();
        }
    }

    public IsoWindow(IsoCell cell, IsoGridSquare gridSquare, IsoSprite gid, boolean north)
    {
        Barricaded = 0;
        BarricideMaxStrength = Integer.valueOf(0);
        BarricideStrength = Integer.valueOf(0);
        Health = Integer.valueOf(100);
        MaxHealth = Integer.valueOf(100);
        type = WindowType.SinglePane;
        this.north = false;
        locked = false;
        open = false;
        destroyed = false;
        gid.getProperties().UnSet(IsoFlagType.cutN);
        gid.getProperties().UnSet(IsoFlagType.cutW);
        int openOffset = 0;
        if(gid.getProperties().Is("OpenTileOffset"))
            openOffset = Integer.parseInt(gid.getProperties().Val("OpenTileOffset"));
        int smashedOffset = 0;
        locked = gid.getProperties().Is("WindowLocked");
        if(gid.getProperties().Is("SmashedTileOffset"))
            smashedOffset = Integer.parseInt(gid.getProperties().Val("SmashedTileOffset"));
        closedSprite = gid;
        if(north)
            closedSprite.getProperties().Set(IsoFlagType.cutN);
        else
            closedSprite.getProperties().Set(IsoFlagType.cutW);
        openSprite = IsoSprite.getSprite(getCell().SpriteManager, gid, openOffset);
        smashedSprite = IsoSprite.getSprite(getCell().SpriteManager, gid, smashedOffset);
        if(smashedSprite != closedSprite && smashedSprite != null)
        {
            smashedSprite.AddProperties(closedSprite);
            smashedSprite.setType(closedSprite.getType());
        }
        if(openSprite != closedSprite && openSprite != null)
        {
            openSprite.AddProperties(closedSprite);
            openSprite.setType(closedSprite.getType());
        }
        sprite = closedSprite;
        IsoObject wall = gridSquare.getWall(north);
        if(wall != null)
        {
            wall.rerouteCollide = this;
            wall.rerouteMask = this;
        }
        square = gridSquare;
        this.north = north;
        

        switch(type.ordinal())
        {
        case 1: // '\001'
            MaxHealth = Health = Integer.valueOf(50);
            break;

        case 2: // '\002'
            MaxHealth = Health = Integer.valueOf(150);
            break;
        }
    }

    public void Barricade(InventoryItem plank)
    {
        DirtySlice();
        if(Barricaded >= 4)
            return;
        IsoGridSquare.setRecalcLightTime(-1);
        BarricideMaxStrength = Integer.valueOf(BarricideMaxStrength.intValue() + (int)(1000F * ((float)plank.getCondition() / (float)plank.getConditionMax())));
        BarricideStrength = Integer.valueOf(BarricideStrength.intValue() + 1000);
        if(barricadeSprite != null && AttachedAnimSprite != null)
            AttachedAnimSprite.remove(barricadeSprite);
        Integer n = Integer.valueOf(8);
        if(north)
            n = Integer.valueOf(n.intValue() + 1);
        n = Integer.valueOf(n.intValue() + Barricaded * 2);
        barricadeSprite = new IsoSpriteInstance(IsoSprite.getSprite(IsoWorld.instance.spriteManager, (new StringBuilder()).append("TileBarricade_").append(n).toString(), 0));
        AttachedAnimSprite.add(barricadeSprite);
        Barricaded++;
        square.InvalidateSpecialObjectPaths();
    }

    public void DoTooltip(ObjectTooltip tooltipUI)
    {
        int mid = 60;
        String name = "";
        switch(type.ordinal())
        {
        case 1: // '\001'
            name = "Single Pane Window";
            break;

        case 2: // '\002'
            name = "Double Pane Window";
            break;
        }
        if(Barricaded > 0)
            name = "Barricaded Window";
        int y = 5;
        tooltipUI.DrawText(name, 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
        y += 20;
        tooltipUI.DrawText("Health:", 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
        if((double)((float)Health.intValue() / 100F) > (double)((float)MaxHealth.intValue() / 100F) * 0.75D)
            tooltipUI.DrawText((new StringBuilder()).append((float)Health.intValue() / 100F).append("/").append((float)MaxHealth.intValue() / 100F).toString(), mid, y, 0.3F, 1.0F, 0.2F, 1.0F);
        else
        if((double)((float)Health.intValue() / 100F) > (double)((float)MaxHealth.intValue() / 100F) * 0.33000000000000002D)
            tooltipUI.DrawText((new StringBuilder()).append((float)Health.intValue() / 100F).append("/").append((float)MaxHealth.intValue() / 100F).toString(), mid, y, 0.8F, 1.0F, 0.2F, 1.0F);
        else
            tooltipUI.DrawText((new StringBuilder()).append((float)Health.intValue() / 100F).append("/").append((float)MaxHealth.intValue() / 100F).toString(), mid, y, 0.8F, 0.3F, 0.2F, 1.0F);
        y += 15;
        if(Barricaded > 0)
        {
            tooltipUI.DrawText("Barricade:", 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
            if((double)((float)BarricideStrength.intValue() / 100F) > (double)((float)BarricideMaxStrength.intValue() / 100F) * 0.75D)
                tooltipUI.DrawText((new StringBuilder()).append((float)BarricideStrength.intValue() / 100F).append("/").append((float)BarricideMaxStrength.intValue() / 100F).toString(), mid, y, 0.3F, 1.0F, 0.2F, 1.0F);
            else
            if((double)((float)BarricideStrength.intValue() / 100F) > (double)((float)BarricideMaxStrength.intValue() / 100F) * 0.33000000000000002D)
                tooltipUI.DrawText((new StringBuilder()).append((float)BarricideStrength.intValue() / 100F).append("/").append((float)BarricideMaxStrength.intValue() / 100F).toString(), mid, y, 0.8F, 1.0F, 0.2F, 1.0F);
            else
                tooltipUI.DrawText((new StringBuilder()).append((float)BarricideStrength.intValue() / 100F).append("/").append((float)BarricideMaxStrength.intValue() / 100F).toString(), mid, y, 0.8F, 0.3F, 0.2F, 1.0F);
        }
        y += 19;
        tooltipUI.setHeight(y);
    }

    public boolean HasTooltip()
    {
        return true;
    }

    public boolean isDestroyed()
    {
        return destroyed;
    }

    public boolean IsOpen()
    {
        return open;
    }

    public boolean onMouseLeftClick(int x, int y)
    {
        if(super.onMouseLeftClick(x, y))
            return true;
        if(IsoUtils.DistanceTo(square.getX(), square.getY(), IsoPlayer.getInstance().getX(), IsoPlayer.getInstance().getY()) < 2.0F && (float)square.getZ() == IsoPlayer.getInstance().getZ())
        {
            ToggleWindow(IsoPlayer.instance);
            return true;
        } else
        {
            return false;
        }
    }

    public boolean TestCollide(IsoMovingObject obj, IsoGridSquare from, IsoGridSquare to)
    {
        if((open || destroyed) && Barricaded == 0)
            return false;
        if(from == square)
        {
            if(north && to.getY() < from.getY())
            {
                if(obj != null)
                    obj.collideWith(this);
                return true;
            }
            if(!north && to.getX() < from.getX())
            {
                if(obj != null)
                    obj.collideWith(this);
                return true;
            }
        } else
        {
            if(north && to.getY() > from.getY())
            {
                if(obj != null)
                    obj.collideWith(this);
                return true;
            }
            if(!north && to.getX() > from.getX())
            {
                if(obj != null)
                    obj.collideWith(this);
                return true;
            }
        }
        return false;
    }

    public zombie.iso.IsoObject.VisionResult TestVision(IsoGridSquare from, IsoGridSquare to)
    {
        if(to.getZ() != from.getZ())
            return zombie.iso.IsoObject.VisionResult.NoEffect;
        if(from == square)
        {
            if(north && to.getY() < from.getY())
                if(Barricaded > 2)
                    return zombie.iso.IsoObject.VisionResult.Blocked;
                else
                    return zombie.iso.IsoObject.VisionResult.Unblocked;
            if(!north && to.getX() < from.getX())
                if(Barricaded > 2)
                    return zombie.iso.IsoObject.VisionResult.Blocked;
                else
                    return zombie.iso.IsoObject.VisionResult.Unblocked;
        } else
        {
            if(north && to.getY() > from.getY())
                if(Barricaded > 2)
                    return zombie.iso.IsoObject.VisionResult.Blocked;
                else
                    return zombie.iso.IsoObject.VisionResult.Unblocked;
            if(!north && to.getX() > from.getX())
                if(Barricaded > 2)
                    return zombie.iso.IsoObject.VisionResult.Blocked;
                else
                    return zombie.iso.IsoObject.VisionResult.Unblocked;
        }
        return zombie.iso.IsoObject.VisionResult.NoEffect;
    }

    public void Thump(IsoMovingObject thumper)
    {
        if(isDestroyed())
            return;
        if(thumper instanceof IsoZombie)
        {
            DoorInteraction j = new DoorInteraction();
            j.CharacterID = ((IsoZombie)thumper).ZombieID;
            j.interactionType = 0;
            j.x = square.getX();
            j.y = square.getY();
            j.z = square.getZ();
            if(FrameLoader.bServer)
                GameServer.instance.SendToClientsTCP(j);
            DirtySlice();
            Damage(1);
            WorldSoundManager.instance.addSound(thumper, square.getX(), square.getY(), square.getZ(), 20, 20, true, 4F, 15F);
        }
        if(Health.intValue() <= 0)
        {
            SoundManager.instance.PlayWorldSound("smashwindow", thumper.getCurrentSquare(), 0.2F, 20F, 1.1F, true);
            WorldSoundManager.instance.addSound(null, square.getX(), square.getY(), square.getZ(), 10, 20, true, 4F, 15F);
            thumper.setThumpTarget(null);
            destroyed = true;
            sprite = smashedSprite;
            closedSprite = smashedSprite;
            square.InvalidateSpecialObjectPaths();
        }
    }

    public void load(ByteBuffer input)
        throws IOException
    {
        super.load(input);
        open = input.get() == 1;
        north = input.get() == 1;
        Barricaded = input.getInt();
        Health = Integer.valueOf(input.getInt());
        BarricideStrength = Integer.valueOf(input.getInt());
        locked = input.get() == 1;
        destroyed = input.get() == 1;
        if(input.getInt() == 1)
            openSprite = IsoSprite.getSprite(getCell().SpriteManager, input.getInt());
        if(input.getInt() == 1)
            closedSprite = IsoSprite.getSprite(getCell().SpriteManager, input.getInt());
        if(input.getInt() == 1)
            smashedSprite = IsoSprite.getSprite(getCell().SpriteManager, input.getInt());
    }

    public void save(ByteBuffer output)
        throws IOException
    {
        super.save(output);
        output.put(((byte)(open ? 1 : 0)));
        output.put(((byte)(north ? 1 : 0)));
        output.putInt(Barricaded);
        output.putInt(Health.intValue());
        output.putInt(BarricideStrength.intValue());
        output.put(((byte)(locked ? 1 : 0)));
        output.put(((byte)(destroyed ? 1 : 0)));
        if(openSprite != null)
        {
            output.putInt(1);
            output.putInt(openSprite.ID);
        } else
        {
            output.putInt(0);
        }
        if(closedSprite != null)
        {
            output.putInt(1);
            output.putInt(closedSprite.ID);
        } else
        {
            output.putInt(0);
        }
        if(smashedSprite != null)
        {
            output.putInt(1);
            output.putInt(smashedSprite.ID);
        } else
        {
            output.putInt(0);
        }
    }

    public void ToggleWindow(IsoGameCharacter chr)
    {
        DirtySlice();
        IsoGridSquare.setRecalcLightTime(-1);
        if(UIManager.getDragInventory() != null && chr == IsoPlayer.instance)
        {
            if("Sheet".equals(UIManager.getDragInventory().getType()))
            {
                IsoGridSquare sq = null;
                IsoDirections d = IsoDirections.N;
                if(north)
                {
                    IsoGridSquare s = square;
                    d = IsoDirections.N;
                    if(s.getRoom() == null)
                    {
                        s = getCell().getGridSquare(s.getX(), s.getY() - 1, s.getZ());
                        d = IsoDirections.S;
                    }
                    sq = s;
                } else
                {
                    IsoGridSquare s = square;
                    d = IsoDirections.W;
                    if(s.getRoom() == null)
                    {
                        s = getCell().getGridSquare(s.getX() - 1, s.getY(), s.getZ());
                        d = IsoDirections.E;
                    }
                    sq = s;
                }
                boolean bAddCurtain = true;
                for(int n = 0; n < sq.getSpecialObjects().size(); n++)
                    if(sq.getSpecialObjects().get(n) instanceof IsoCurtain)
                        bAddCurtain = false;

                if(sq != null && bAddCurtain)
                {
                    int gid = 16;
                    if(d == IsoDirections.E)
                        gid++;
                    if(d == IsoDirections.S)
                        gid += 3;
                    if(d == IsoDirections.N)
                        gid += 2;
                    gid += 4;
                    IsoCurtain c = new IsoCurtain(getCell(), sq, (new StringBuilder()).append("TileObjects3_").append(gid).toString(), north);
                    sq.AddSpecialTileObject(c);
                    if(!c.open)
                        c.ToggleDoorSilent();
                    IsoPlayer.getInstance().getInventory().RemoveOneOf("Sheet");
                    return;
                }
            }
            if("Hammer".equals(UIManager.getDragInventory().getType()))
            {
                IsoPlayer.getInstance().StartAction(new UnbarricadeAction(IsoPlayer.getInstance(), this));
                return;
            }
            if(Barricaded >= 4)
                return;
            if("Plank".equals(UIManager.getDragInventory().getType()))
            {
                InventoryItem nails = IsoPlayer.getInstance().getInventory().FindAndReturn("Nails");
                if(nails != null && IsoPlayer.getInstance().getPrimaryHandItem() != null && "Hammer".equals(IsoPlayer.getInstance().getPrimaryHandItem().getType()) || IsoPlayer.getInstance().getSecondaryHandItem() != null && "Hammer".equals(IsoPlayer.getInstance().getSecondaryHandItem().getType()))
                {
                    IsoPlayer.getInstance().StartAction(new BarricadeAction(IsoPlayer.getInstance(), this));
                    return;
                }
            }
        } else
        if(chr == IsoPlayer.instance)
        {
            IsoGridSquare sq = null;
            IsoGridSquare sq2 = null;
            IsoDirections d = IsoDirections.N;
            if(north)
            {
                IsoGridSquare s = square;
                d = IsoDirections.N;
                if(s.getRoom() == null)
                {
                    s = getCell().getGridSquare(s.getX(), s.getY() - 1, s.getZ());
                    d = IsoDirections.S;
                }
                sq2 = sq;
                sq = s;
            } else
            {
                IsoGridSquare s = square;
                d = IsoDirections.W;
                if(s.getRoom() == null)
                {
                    s = getCell().getGridSquare(s.getX() - 1, s.getY(), s.getZ());
                    d = IsoDirections.E;
                }
                sq2 = sq;
                sq = s;
            }
            if(Keyboard.isKeyDown(42))
            {
                if(sq != null)
                {
                    for(int n = 0; n < sq.getSpecialObjects().size(); n++)
                        if(sq.getSpecialObjects().get(n) instanceof IsoCurtain)
                        {
                            ((IsoCurtain)sq.getSpecialObjects().get(n)).ToggleDoorSilent();
                            return;
                        }

                }
                if(sq2 != null)
                {
                    for(int n = 0; n < sq2.getSpecialObjects().size(); n++)
                        if(sq2.getSpecialObjects().get(n) instanceof IsoCurtain)
                        {
                            ((IsoCurtain)sq2.getSpecialObjects().get(n)).ToggleDoorSilent();
                            return;
                        }

                }
            }
        }
        if(locked)
            return;
        if(destroyed)
            return;
        open = !open;
        sprite = closedSprite;
        square.InvalidateSpecialObjectPaths();
        if(open)
        {
            SoundManager.instance.PlayWorldSound("dooropen", square, 0.0F, 10F, 0.7F, true);
            sprite = openSprite;
        } else
        {
            SoundManager.instance.PlayWorldSound("doorclose", square, 0.0F, 10F, 0.7F, true);
        }
    }

    public void Unbarricade(IsoGameCharacter chr)
    {
        if(Barricaded == 0)
            return;
        if(chr != null && BarricideMaxStrength.intValue() > 0)
        {
            float brokeDelta = (float)BarricideStrength.intValue() / (float)BarricideMaxStrength.intValue();
            for(int n = 0; n < Barricaded; n++)
            {
                InventoryItem item = InventoryItemFactory.CreateItem("Base.Plank");
                item.setCondition((int)((float)item.getConditionMax() * brokeDelta));
                if(item.getCondition() < 0)
                    item.setCondition(0);
                chr.getInventory().AddItem(item);
            }

        }
        DirtySlice();
        SoundManager.instance.PlayWorldSound("woodfall", square, 0.2F, 10F, 0.8F, true);
        square.InvalidateSpecialObjectPaths();
        if(AttachedAnimSprite != null)
            AttachedAnimSprite.clear();
        Barricaded = 0;
        IsoGridSquare.setRecalcLightTime(-1);
        BarricideStrength = Integer.valueOf(0);
        BarricideMaxStrength = Integer.valueOf(0);
    }

    void Damage(int amount)
    {
        DirtySlice();
        if(Barricaded > 0)
        {
            BarricideStrength = Integer.valueOf(BarricideStrength.intValue() - amount);
            if(BarricideStrength.intValue() <= 0)
                Unbarricade(null);
        } else
        {
            Health = Integer.valueOf(Health.intValue() - amount);
        }
        if(Health.intValue() < 0)
            Health = Integer.valueOf(0);
    }

    public int Barricaded;
    public Integer BarricideMaxStrength;
    public Integer BarricideStrength;
    public Integer Health;
    public Integer MaxHealth;
    public WindowType type;
    IsoSpriteInstance barricadeSprite;
    IsoSprite closedSprite;
    IsoSprite smashedSprite;
    public boolean north;
    public boolean locked;
    public boolean open;
    IsoSprite openSprite;
    private boolean destroyed;
}
