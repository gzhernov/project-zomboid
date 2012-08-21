// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoDoor.java

package zombie.iso.objects;

import java.io.IOException;
import java.nio.ByteBuffer;
import zombie.*;
import zombie.behaviors.SequenceBehavior;
import zombie.characters.CharacterTimedActions.BarricadeAction;
import zombie.characters.CharacterTimedActions.UnbarricadeAction;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.core.textures.ColorInfo;
import zombie.inventory.*;
import zombie.inventory.types.HandWeapon;
import zombie.iso.*;
import zombie.iso.objects.interfaces.Thumpable;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteInstance;
import zombie.network.*;
import zombie.ui.ObjectTooltip;
import zombie.ui.UIManager;

public class IsoDoor extends IsoObject
    implements Thumpable
{
	/*      */   public static enum DoorType
	/*      */   {
	/*   55 */     WeakWooden, 
	/*   56 */     StrongWooden;
	/*      */   }


    public IsoDoor(IsoCell cell)
    {
        super(cell);
        Barricaded = 0;
        BarricideMaxStrength = Integer.valueOf(0);
        BarricideStrength = Integer.valueOf(0);
        Health = Integer.valueOf(500);
        Locked = false;
        MaxHealth = Integer.valueOf(500);
        PushedMaxStrength = Integer.valueOf(0);
        PushedStrength = Integer.valueOf(0);
        type = DoorType.WeakWooden;
        north = false;
        gid = -1;
        open = false;
        destroyed = false;
    }

    public String getObjectName()
    {
        return "Door";
    }

    public void render(float x, float y, float z, ColorInfo info)
    {
        super.render(x, y, z, info, true);
    }

    public IsoDoor(IsoCell cell, IsoGridSquare gridSquare, IsoSprite gid, boolean north)
    {
        Barricaded = 0;
        BarricideMaxStrength = Integer.valueOf(0);
        BarricideStrength = Integer.valueOf(0);
        Health = Integer.valueOf(500);
        Locked = false;
        MaxHealth = Integer.valueOf(500);
        PushedMaxStrength = Integer.valueOf(0);
        PushedStrength = Integer.valueOf(0);
        type = DoorType.WeakWooden;
        this.north = false;
        this.gid = -1;
        open = false;
        destroyed = false;
        OutlineOnMouseover = true;
        PushedMaxStrength = PushedStrength = Integer.valueOf(2500);
        closedSprite = gid;
        openSprite = IsoSprite.getSprite(getCell().SpriteManager, gid, 2);
        sprite = closedSprite;
        square = gridSquare;
        this.north = north;



        switch(type.ordinal())
        {
        case 1: // '\001'
            MaxHealth = Health = Integer.valueOf(500);
            break;

        case 2: // '\002'
            MaxHealth = Health = Integer.valueOf(800);
            break;
        }
    }

    public IsoDoor(IsoCell cell, IsoGridSquare gridSquare, String gid, boolean north)
    {
        Barricaded = 0;
        BarricideMaxStrength = Integer.valueOf(0);
        BarricideStrength = Integer.valueOf(0);
        Health = Integer.valueOf(500);
        Locked = false;
        MaxHealth = Integer.valueOf(500);
        PushedMaxStrength = Integer.valueOf(0);
        PushedStrength = Integer.valueOf(0);
        type = DoorType.WeakWooden;
        this.north = false;
        this.gid = -1;
        open = false;
        destroyed = false;
        OutlineOnMouseover = true;
        PushedMaxStrength = PushedStrength = Integer.valueOf(2500);
        closedSprite = IsoSprite.getSprite(getCell().SpriteManager, gid, 0);
        openSprite = IsoSprite.getSprite(getCell().SpriteManager, gid, 2);
        sprite = closedSprite;
        square = gridSquare;
        this.north = north;
        switch(type.ordinal())
        {
        case 1: // '\001'
            MaxHealth = Health = Integer.valueOf(500);
            break;

        case 2: // '\002'
            MaxHealth = Health = Integer.valueOf(800);
            break;
        }
    }

    public void load(ByteBuffer input)
        throws IOException
    {
        super.load(input);
        open = input.get() == 1;
        Locked = input.get() == 1;
        north = input.get() == 1;
        Barricaded = input.getInt();
        Health = Integer.valueOf(input.getInt());
        MaxHealth = Integer.valueOf(input.getInt());
        BarricideStrength = Integer.valueOf(input.getInt());
        closedSprite = IsoSprite.getSprite(IsoWorld.instance.spriteManager, input.getInt());
        openSprite = IsoSprite.getSprite(IsoWorld.instance.spriteManager, input.getInt());
        OutlineOnMouseover = true;
        PushedMaxStrength = PushedStrength = Integer.valueOf(2500);
    }

    public void save(ByteBuffer output)
        throws IOException
    {
        super.save(output);
        output.put(((byte)(open ? 1 : 0)));
        output.put(((byte)(Locked ? 1 : 0)));
        output.put(((byte)(north ? 1 : 0)));
        output.putInt(Barricaded);
        output.putInt(Health.intValue());
        output.putInt(MaxHealth.intValue());
        output.putInt(BarricideStrength.intValue());
        output.putInt(closedSprite.ID);
        output.putInt(openSprite.ID);
    }

    public void Barricade(IsoGameCharacter chr, InventoryItem plank)
    {
        if(plank == null)
            return;
        DirtySlice();
        square.InvalidateSpecialObjectPaths();
        if(Barricaded >= 4)
            return;
        if(open)
            ToggleDoor(chr);
        IsoGridSquare.setRecalcLightTime(-1);
        BarricideMaxStrength = Integer.valueOf(BarricideMaxStrength.intValue() + (int)(1000F * ((float)plank.getCondition() / (float)plank.getConditionMax()) * chr.getBarricadeStrengthMod()));
        BarricideStrength = Integer.valueOf(BarricideStrength.intValue() + (int)(1000F * ((float)plank.getCondition() / (float)plank.getConditionMax()) * chr.getBarricadeStrengthMod()));
        if(barricadeSprite != null && AttachedAnimSprite != null)
            AttachedAnimSprite.remove(barricadeSprite);
        Integer n = Integer.valueOf(8);
        if(north)
            n = Integer.valueOf(n.intValue() + 1);
        n = Integer.valueOf(n.intValue() + Barricaded * 2);
        barricadeSprite = new IsoSpriteInstance(IsoSprite.getSprite(IsoWorld.instance.spriteManager, (new StringBuilder()).append("TileBarricade_").append(n).toString(), 0));
        AttachedAnimSprite.add(barricadeSprite);
        Barricaded++;
    }

    public void DoTooltip(ObjectTooltip tooltipUI)
    {
        int mid = 60;
        String name = "";
        switch(type.ordinal())
        {
        case 1: // '\001'
            if(Locked)
                name = "Locked Wooden Door";
            else
                name = "Wooden Door";
            break;

        case 2: // '\002'
            name = "Strong Wooden Door";
            break;
        }
        if(Barricaded > 0)
        {
            name = "Barricaded Door";
            if(IsStrengthenedByPushedItems())
                name = "Heavy Barricaded Door";
        } else
        if(IsStrengthenedByPushedItems())
            name = "Blocked Door";
        int y = 5;
        tooltipUI.DrawText(name, 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
        y += 20;
        int Health = this.Health.intValue();
        int MaxHealth = this.MaxHealth.intValue();
        int OrigHealth = Health;
        int OrigMaxHealth = MaxHealth;
        if(IsStrengthenedByPushedItems())
        {
            Health += PushedMaxStrength.intValue();
            MaxHealth += PushedMaxStrength.intValue();
            Health /= 100;
            MaxHealth /= 100;
            if(OrigHealth < OrigMaxHealth && OrigHealth > 0)
                Health++;
            tooltipUI.DrawText("Health:", 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
        } else
        {
            Health /= 100;
            MaxHealth /= 100;
            if(OrigHealth < OrigMaxHealth && OrigHealth > 0)
                Health++;
            tooltipUI.DrawText("Health:", 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
        }
        if((double)Health > (double)MaxHealth * 0.75D)
            tooltipUI.DrawText((new StringBuilder()).append(Health).append("/").append(MaxHealth).toString(), mid, y, 0.3F, 1.0F, 0.2F, 1.0F);
        else
        if((double)Health > (double)MaxHealth * 0.33000000000000002D)
            tooltipUI.DrawText((new StringBuilder()).append(Health).append("/").append(MaxHealth).toString(), mid, y, 0.8F, 1.0F, 0.2F, 1.0F);
        else
            tooltipUI.DrawText((new StringBuilder()).append(Health).append("/").append(MaxHealth).toString(), mid, y, 0.8F, 0.3F, 0.2F, 1.0F);
        y += 15;
        if(Barricaded > 0)
        {
            if(!IsStrengthenedByPushedItems())
                tooltipUI.DrawText("Barricade:", 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
            else
                tooltipUI.DrawText("Barricade:", 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
            if((double)(BarricideStrength.intValue() / 100) > (double)(BarricideMaxStrength.intValue() / 100) * 0.75D)
                tooltipUI.DrawText((new StringBuilder()).append(BarricideStrength.intValue() / 100).append("/").append(BarricideMaxStrength.intValue() / 100).toString(), mid, y, 0.3F, 1.0F, 0.2F, 1.0F);
            else
            if((double)(BarricideStrength.intValue() / 100) > (double)(BarricideMaxStrength.intValue() / 100) * 0.33000000000000002D)
                tooltipUI.DrawText((new StringBuilder()).append(BarricideStrength.intValue() / 100).append("/").append(BarricideMaxStrength.intValue() / 100).toString(), mid, y, 0.8F, 1.0F, 0.2F, 1.0F);
            else
                tooltipUI.DrawText((new StringBuilder()).append(BarricideStrength.intValue() / 100).append("/").append(BarricideMaxStrength.intValue() / 100).toString(), mid, y, 0.8F, 0.3F, 0.2F, 1.0F);
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

    public boolean IsStrengthenedByPushedItems()
    {
        return false;
    }

    public boolean onMouseLeftClick(int x, int y)
    {
        DirtySlice();
        if(IsoUtils.DistanceTo(square.getX(), square.getY(), IsoPlayer.getInstance().getX(), IsoPlayer.getInstance().getY()) < 2.0F && (float)square.getZ() == IsoPlayer.getInstance().getZ())
        {
            if(UIManager.getDragInventory() != null)
            {
                if("Hammer".equals(UIManager.getDragInventory().getType()))
                {
                    IsoPlayer.getInstance().StartAction(new UnbarricadeAction(IsoPlayer.getInstance(), this));
                    return true;
                }
                if(Barricaded >= 4)
                    return false;
                if("Plank".equals(UIManager.getDragInventory().getType()))
                {
                    InventoryItem nails = IsoPlayer.getInstance().getInventory().FindAndReturn("Nails");
                    if(nails != null && IsoPlayer.getInstance().getPrimaryHandItem() != null && "Hammer".equals(IsoPlayer.getInstance().getPrimaryHandItem().getType()) || IsoPlayer.getInstance().getSecondaryHandItem() != null && "Hammer".equals(IsoPlayer.getInstance().getSecondaryHandItem().getType()))
                    {
                        IsoPlayer.getInstance().StartAction(new BarricadeAction(IsoPlayer.getInstance(), this));
                        return true;
                    } else
                    {
                        return false;
                    }
                }
            }
            if(Barricaded > 0)
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

    public boolean TestPathfindCollide(IsoMovingObject obj, IsoGridSquare from, IsoGridSquare to)
    {
        boolean north = this.north;
        if(Barricaded == 0)
            return false;
        if((obj instanceof IsoSurvivor) && ((IsoSurvivor)obj).getInventory().contains("Hammer"))
            return false;
        if(open)
            north = !north;
        if(from == square)
        {
            if(north && to.getY() < from.getY())
                return true;
            if(!north && to.getX() < from.getX())
                return true;
        } else
        {
            if(north && to.getY() > from.getY())
                return true;
            if(!north && to.getX() > from.getX())
                return true;
        }
        return false;
    }

    public boolean TestCollide(IsoMovingObject obj, IsoGridSquare from, IsoGridSquare to)
    {
        boolean north = this.north;
        if(open)
            north = !north;
        if(from == square)
        {
            if(north && to.getY() < from.getY())
            {
                obj.collideWith(this);
                return true;
            }
            if(!north && to.getX() < from.getX())
            {
                obj.collideWith(this);
                return true;
            }
        } else
        {
            if(north && to.getY() > from.getY())
            {
                obj.collideWith(this);
                return true;
            }
            if(!north && to.getX() > from.getX())
            {
                obj.collideWith(this);
                return true;
            }
        }
        return false;
    }

    public zombie.iso.IsoObject.VisionResult TestVision(IsoGridSquare from, IsoGridSquare to)
    {
        boolean north = this.north;
        if(open)
            north = !north;
        if(to.getZ() != from.getZ())
            return zombie.iso.IsoObject.VisionResult.NoEffect;
        if(from == square)
        {
            if(north && to.getY() < from.getY())
                return zombie.iso.IsoObject.VisionResult.Blocked;
            if(!north && to.getX() < from.getX())
                return zombie.iso.IsoObject.VisionResult.Blocked;
        } else
        {
            if(north && to.getY() > from.getY())
                return zombie.iso.IsoObject.VisionResult.Blocked;
            if(!north && to.getX() > from.getX())
                return zombie.iso.IsoObject.VisionResult.Blocked;
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
            int tot = thumper.getCurrentSquare().getMovingObjects().size();
            if(thumper.getCurrentSquare().getW() != null)
                tot += thumper.getCurrentSquare().getW().getMovingObjects().size();
            if(thumper.getCurrentSquare().getE() != null)
                tot += thumper.getCurrentSquare().getE().getMovingObjects().size();
            if(thumper.getCurrentSquare().getS() != null)
                tot += thumper.getCurrentSquare().getS().getMovingObjects().size();
            if(thumper.getCurrentSquare().getN() != null)
                tot += thumper.getCurrentSquare().getN().getMovingObjects().size();
            if(tot >= 8)
            {
                DirtySlice();
                Damage(1);
            }
            WorldSoundManager.instance.addSound(thumper, square.getX(), square.getY(), square.getZ(), 20, 20, true, 4F, 15F);
        }
        if(Health.intValue() <= 0)
        {
            SoundManager.instance.PlayWorldSound("breakdoor", thumper.getCurrentSquare(), 0.2F, 20F, 1.1F, true);
            WorldSoundManager.instance.addSound(null, square.getX(), square.getY(), square.getZ(), 10, 20, true, 4F, 15F);
            thumper.setThumpTarget(null);
            destroyed = true;
            square.getObjects().remove(this);
            square.getSpecialObjects().remove(this);
            int NumPlanks = Rand.Next(2) + 1;
            for(int i = 0; i < NumPlanks; i++)
                square.AddWorldInventoryItem("Base.Plank", Rand.Next(-1F, 1.0F), Rand.Next(-1F, 1.0F), 0.0F);

            square.AddWorldInventoryItem("Base.Doorknob", Rand.Next(-1F, 1.0F), Rand.Next(-1F, 1.0F), 0.0F);
            int NumHinges = Rand.Next(3);
            for(int i = 0; i < NumHinges; i++)
                square.AddWorldInventoryItem("Base.Hinge", Rand.Next(-1F, 1.0F), Rand.Next(-1F, 1.0F), 0.0F);

        }
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
        Damage(weapon.getDoorDamage());
        DirtySlice();
        if(weapon.getDoorHitSound() != null)
            SoundManager.instance.PlayWorldSound(weapon.getDoorHitSound(), square, 0.2F, 20F, 1.0F, true);
        WorldSoundManager.instance.addSound(owner, square.getX(), square.getY(), square.getZ(), 20, 20, false, 0.0F, 15F);
        if(!IsStrengthenedByPushedItems() && Health.intValue() <= 0 || IsStrengthenedByPushedItems() && Health.intValue() <= -PushedMaxStrength.intValue())
        {
            SoundManager.instance.PlayWorldSound("breakdoor", square, 0.2F, 20F, 1.1F, true);
            WorldSoundManager.instance.addSound(owner, square.getX(), square.getY(), square.getZ(), 20, 20, false, 0.0F, 15F);
            int NumPlanks = Rand.Next(2) + 1;
            for(int i = 0; i < NumPlanks; i++)
                square.AddWorldInventoryItem("Base.Plank", Rand.Next(-1F, 1.0F), Rand.Next(-1F, 1.0F), 0.0F);

            square.AddWorldInventoryItem("Base.Doorknob", Rand.Next(-1F, 1.0F), Rand.Next(-1F, 1.0F), 0.0F);
            int NumHinges = Rand.Next(3);
            for(int i = 0; i < NumHinges; i++)
                square.AddWorldInventoryItem("Base.Hinge", Rand.Next(-1F, 1.0F), Rand.Next(-1F, 1.0F), 0.0F);

            destroyed = true;
            square.getObjects().remove(this);
            square.getSpecialObjects().remove(this);
        }
    }

    public IsoGridSquare getOtherSideOfDoor(IsoGameCharacter chr)
    {
        if(north)
            if(chr.getCurrentSquare().getRoom() == square.getRoom())
                return IsoWorld.instance.CurrentCell.getGridSquare(square.getX(), square.getY() - 1, square.getZ());
            else
                return IsoWorld.instance.CurrentCell.getGridSquare(square.getX(), square.getY(), square.getZ());
        if(chr.getCurrentSquare().getRoom() == square.getRoom())
            return IsoWorld.instance.CurrentCell.getGridSquare(square.getX() - 1, square.getY(), square.getZ());
        else
            return IsoWorld.instance.CurrentCell.getGridSquare(square.getX(), square.getY(), square.getZ());
    }

    public void ToggleDoorActual(IsoGameCharacter chr)
    {
        DirtySlice();
        square.InvalidateSpecialObjectPaths();
        if((chr instanceof IsoPlayer) && !open && chr.getStats().Sanity < 0.5F && (float)Rand.Next(100) > chr.getStats().Sanity * 100F && Rand.Next(5) == 0)
        {
            IsoGridSquare sq = getOtherSideOfDoor(chr);
            for(int n = 0; n < 4; n++)
            {
                IsoZombie zombie = new IsoZombie(IsoWorld.instance.CurrentCell);
                zombie.setX(sq.getX());
                zombie.setX(zombie.getX() + (float)Rand.Next(1000) / 1000F);
                zombie.setY(sq.getY());
                zombie.setY(zombie.getY() + (float)Rand.Next(1000) / 1000F);
                zombie.setZ(sq.getZ());
                zombie.setCurrent(sq);
                zombie.GhostLife = 30F;
                zombie.Ghost = true;
                Vector2 vec = new Vector2(IsoPlayer.getInstance().getX(), IsoPlayer.getInstance().getY());
                vec.x -= zombie.getX();
                vec.y -= zombie.getY();
                vec.normalize();
                zombie.DirectionFromVector(vec);
                zombie.target = IsoPlayer.getInstance();
                zombie.spotted(IsoPlayer.getInstance());
                IsoWorld.instance.CurrentCell.getZombieList().add(zombie);
                IsoWorld.instance.CurrentCell.getGhostList().add(zombie);
            }

        }
        if((chr instanceof IsoSurvivor) && chr.getInventory().contains("Hammer"))
        {
            if(Barricaded > 0)
                Unbarricade(chr);
        } else
        if(Barricaded > 0)
            return;
        if(Locked && chr != null && (chr instanceof IsoPlayer) && chr.getCurrentSquare().getRoom() == null && !open)
        {
            SoundManager.instance.PlayWorldSound("doorlocked", square, 0.0F, 10F, 0.7F, true);
            chr.Say("Gah, locked!");
            if(chr instanceof IsoSurvivor)
                chr.getMasterBehaviorList().reset();
            return;
        }
        if(chr instanceof IsoPlayer)
        {
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
        }
        for(int n = 0; n < square.getLights().size(); n++)
        {
            if(((IsoLightSource)square.getLights().get(n)).bActive)
                ((IsoLightSource)square.getLights().get(n)).bWasActive = true;
            else
                ((IsoLightSource)square.getLights().get(n)).bWasActive = false;
            if(((IsoLightSource)square.getLights().get(n)).bWasActive)
            {
                ((IsoLightSource)square.getLights().get(n)).bActive = !((IsoLightSource)square.getLights().get(n)).bActive;
                ((IsoLightSource)square.getLights().get(n)).update();
            }
        }

        open = !open;
        for(int n = 0; n < square.getLights().size(); n++)
            if(((IsoLightSource)square.getLights().get(n)).bWasActive)
            {
                ((IsoLightSource)square.getLights().get(n)).bActive = !((IsoLightSource)square.getLights().get(n)).bActive;
                ((IsoLightSource)square.getLights().get(n)).update();
            }

        sprite = closedSprite;
        if(open)
        {
            SoundManager.instance.PlayWorldSound("dooropen", square, 0.0F, 10F, 0.7F, true);
            sprite = openSprite;
        } else
        {
            SoundManager.instance.PlayWorldSound("doorclose", square, 0.0F, 10F, 0.7F, true);
        }
    }

    public void ToggleDoor(IsoGameCharacter chr)
    {
        DoorInteraction j = new DoorInteraction();
        if(chr == null)
            j.CharacterID = -1;
        else
            j.CharacterID = chr.getRemoteID();
        if(!open)
            j.interactionType = 1;
        else
            j.interactionType = 2;
        j.x = square.getX();
        j.y = square.getY();
        j.z = square.getZ();
        if(FrameLoader.bServer)
            GameServer.instance.SendToClientsTCP(j);
        else
        if(FrameLoader.bClient)
            GameClient.instance.SendToServerTCP(j);
        ToggleDoorActual(chr);
    }

    public void ToggleDoorSilent()
    {
        if(Barricaded > 0)
            return;
        square.InvalidateSpecialObjectPaths();
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

    public void Unbarricade(IsoGameCharacter chr)
    {
        DirtySlice();
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
        square.InvalidateSpecialObjectPaths();
        SoundManager.instance.PlayWorldSound("woodfall", square, 0.2F, 10F, 0.8F, true);
        if(AttachedAnimSprite != null)
            AttachedAnimSprite.clear();
        Barricaded = 0;
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
    }

    public int Barricaded;
    public Integer BarricideMaxStrength;
    public Integer BarricideStrength;
    public Integer Health;
    public boolean Locked;
    public Integer MaxHealth;
    public Integer PushedMaxStrength;
    public Integer PushedStrength;
    public DoorType type;
    IsoSpriteInstance barricadeSprite;
    IsoSprite closedSprite;
    public boolean north;
    int gid;
    public boolean open;
    IsoSprite openSprite;
    private boolean destroyed;
}
