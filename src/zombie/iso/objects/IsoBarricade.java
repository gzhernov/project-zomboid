// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoBarricade.java

package zombie.iso.objects;

import java.io.IOException;
import java.nio.ByteBuffer;
import zombie.SoundManager;
import zombie.WorldSoundManager;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.core.properties.PropertyContainer;
import zombie.inventory.types.HandWeapon;
import zombie.iso.*;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.objects.interfaces.Thumpable;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.ui.ObjectTooltip;

public class IsoBarricade extends IsoObject
    implements Thumpable
{

	/*     */   public static enum DoorType
	/*     */   {
	/*  52 */     WeakWooden, 
	/*  53 */     StrongWooden;
	/*     */   }
	


    public IsoBarricade(IsoCell cell)
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
        type = DoorType.WeakWooden;
        open = false;
        destroyed = false;
        sprite = IsoWorld.instance.spriteManager.getSprite("TileObjects2_26");
        sprite.getProperties().Set(IsoFlagType.cutW, "yes");
        sprite.getProperties().Set(IsoFlagType.cutN, "yes");
    }

    public String getObjectName()
    {
        return "Barricade";
    }

    public IsoBarricade(IsoCell cell, IsoGridSquare gridSquare, int gid)
    {
        Barricaded = false;
        BarricideMaxStrength = Integer.valueOf(0);
        BarricideStrength = Integer.valueOf(0);
        Health = Integer.valueOf(1000);
        Locked = false;
        MaxHealth = Integer.valueOf(1000);
        PushedMaxStrength = Integer.valueOf(0);
        PushedStrength = Integer.valueOf(0);
        type = DoorType.WeakWooden;
        open = false;
        destroyed = false;
        sprite = IsoWorld.instance.spriteManager.getSprite("TileObjects2_26");
        PushedMaxStrength = PushedStrength = Integer.valueOf(2500);
        sprite.getProperties().Set(IsoFlagType.cutW, "yes");
        sprite.getProperties().Set(IsoFlagType.cutN, "yes");
        square = gridSquare;
        DirtySlice();


        switch(type.ordinal())
        {
        case 1: // '\001'
            MaxHealth = Health = Integer.valueOf(300);
            break;
        }
    }

    public void Barricade(IsoGameCharacter isogamecharacter)
    {
    }

    public void load(ByteBuffer input)
        throws IOException
    {
        super.load(input);
        Health = Integer.valueOf(input.getInt());
        MaxHealth = Integer.valueOf(input.getInt());
        BarricideStrength = Integer.valueOf(input.getInt());
        PushedMaxStrength = PushedStrength = Integer.valueOf(2500);
    }

    public void save(ByteBuffer output)
        throws IOException
    {
        super.save(output);
        output.putInt(Health.intValue());
        output.putInt(MaxHealth.intValue());
        output.putInt(BarricideStrength.intValue());
    }

    public void DoTooltip(ObjectTooltip tooltipUI)
    {
        int mid = 60;
        String name = "";
        switch(type.ordinal())
        {
        default:
            break;

        case 1: // '\001'
            if(Locked)
                name = "Barricade";
            else
                name = "Barricade";
            break;

        case 2: // '\002'
            name = "Strong Wooden Door";
            break;
        }
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
        if(Barricaded)
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
        IsoGridSquare sq = square;
        IsoGridSquare other = null;
        other = sq.getCell().getGridSquare(sq.getX() - 1, sq.getY(), sq.getZ());
        return other.getProperties().Is(IsoFlagType.pushable) || sq.getProperties().Is(IsoFlagType.pushable);
    }

    public boolean onMouseLeftClick(int x, int y)
    {
        return super.onMouseLeftClick(x, y);
    }

    public boolean TestCollide(IsoMovingObject obj, IsoGridSquare from, IsoGridSquare to)
    {
        if(from != square)
        {
            if(obj != null)
                obj.collideWith(this);
            return true;
        } else
        {
            return false;
        }
    }

    public zombie.iso.IsoObject.VisionResult TestVision(IsoGridSquare from, IsoGridSquare to)
    {
        return zombie.iso.IsoObject.VisionResult.Blocked;
    }

    public void Thump(IsoMovingObject thumper)
    {
        if(isDestroyed())
            return;
        if(thumper instanceof IsoZombie)
        {
            if(thumper.getCurrentSquare().getMovingObjects().size() >= 4)
                Damage(1);
            WorldSoundManager.instance.addSound(null, square.getX(), square.getY(), square.getZ(), 20, 20, true, 4F, 15F);
        }
        if(!IsStrengthenedByPushedItems() && Health.intValue() <= 0 || IsStrengthenedByPushedItems() && Health.intValue() <= -PushedMaxStrength.intValue())
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

        }
    }

    public void WeaponHit(IsoGameCharacter owner, HandWeapon weapon)
    {
        if(isDestroyed())
            return;
        Damage(weapon.getDoorDamage());
        if(weapon.getDoorHitSound() != null)
            SoundManager.instance.PlayWorldSound(weapon.getDoorHitSound(), square, 0.2F, 20F, 1.0F, true);
        WorldSoundManager.instance.addSound(null, square.getX(), square.getY(), square.getZ(), 20, 20, true, 4F, 15F);
        if(!IsStrengthenedByPushedItems() && Health.intValue() <= 0 || IsStrengthenedByPushedItems() && Health.intValue() <= -PushedMaxStrength.intValue())
        {
            SoundManager.instance.PlayWorldSound("breakdoor", square, 0.2F, 20F, 1.1F, true);
            WorldSoundManager.instance.addSound(null, square.getX(), square.getY(), square.getZ(), 10, 20, true, 4F, 15F);
            destroyed = true;
            square.getObjects().remove(this);
            square.getSpecialObjects().remove(this);
        }
    }

    public void ToggleDoor(IsoGameCharacter isogamecharacter)
    {
    }

    public void ToggleDoorSilent()
    {
    }

    public void Unbarricade()
    {
    }

    void Damage(int amount)
    {
        if(Barricaded)
        {
            BarricideStrength = Integer.valueOf(BarricideStrength.intValue() - amount);
            if(BarricideStrength.intValue() <= 0)
                Unbarricade();
        } else
        {
            Health = Integer.valueOf(Health.intValue() - amount);
        }
    }

    public static boolean BarricadeFed = false;
    public boolean Barricaded;
    public Integer BarricideMaxStrength;
    public Integer BarricideStrength;
    public Integer Health;
    public boolean Locked;
    public Integer MaxHealth;
    public Integer PushedMaxStrength;
    public Integer PushedStrength;
    public DoorType type;
    IsoSprite barricadeSprite;
    IsoSprite closedSprite;
    boolean open;
    IsoSprite openSprite;
    private boolean destroyed;

}
