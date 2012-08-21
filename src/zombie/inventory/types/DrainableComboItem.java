// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DrainableComboItem.java

package zombie.inventory.types;

import zombie.characters.IsoPlayer;
import zombie.interfaces.IUpdater;
import zombie.inventory.*;

// Referenced classes of package zombie.inventory.types:
//            Drainable

public class DrainableComboItem extends InventoryItem
    implements Drainable, IUpdater
{

    public DrainableComboItem(String module, String name, String itemType, String texName)
    {
        super(module, name, itemType, texName);
        bUseWhileEquiped = true;
        ticksPerEquipUse = 30;
        useDelta = 0.03125F;
        delta = 1.0F;
        ticks = 0;
        ReplaceOnDeplete = null;
    }

    public boolean CanStack(InventoryItem item)
    {
        return delta == ((DrainableComboItem)item).delta && name.equals(item.getName());
    }

    public float getUsedDelta()
    {
        return delta;
    }

    public void render()
    {
    }

    public void renderlast()
    {
    }

    public void setUsedDelta(float usedDelta)
    {
        delta = usedDelta;
    }

    public void update()
    {
        if(bUseWhileEquiped && (IsoPlayer.getInstance().getPrimaryHandItem() == this || IsoPlayer.getInstance().getSecondaryHandItem() == this))
        {
            ticks++;
            if(ticks > ticksPerEquipUse)
            {
                ticks = 0;
                Use();
            }
        }
    }

    public void Use()
    {
        delta -= useDelta;
        if(uses > 1)
        {
            int otherUses = uses - 1;
            uses = 1;
            InventoryItem newItem = InventoryItemFactory.CreateItem(getFullType());
            newItem.setUses(otherUses);
            container.AddItem(newItem);
        }
        if(delta <= 0.0F)
        {
            delta = 0.0F;
            if(getReplaceOnDeplete() != null)
            {
                String s = getReplaceOnDeplete();
                if(!ReplaceOnDeplete.contains("."))
                    s = (new StringBuilder()).append(module).append(".").append(s).toString();
                container.AddItem(s);
                container.Remove(this);
                container.dirty = true;
            } else
            {
                super.Use();
            }
        }
    }

    public boolean isUseWhileEquiped()
    {
        return bUseWhileEquiped;
    }

    public void setUseWhileEquiped(boolean bUseWhileEquiped)
    {
        this.bUseWhileEquiped = bUseWhileEquiped;
    }

    public int getTicksPerEquipUse()
    {
        return ticksPerEquipUse;
    }

    public void setTicksPerEquipUse(int ticksPerEquipUse)
    {
        this.ticksPerEquipUse = ticksPerEquipUse;
    }

    public float getUseDelta()
    {
        return useDelta;
    }

    public void setUseDelta(float useDelta)
    {
        this.useDelta = useDelta;
    }

    public float getDelta()
    {
        return delta;
    }

    public void setDelta(float delta)
    {
        this.delta = delta;
    }

    public int getTicks()
    {
        return ticks;
    }

    public void setTicks(int ticks)
    {
        this.ticks = ticks;
    }

    public void setReplaceOnDeplete(String ReplaceOnDeplete)
    {
        this.ReplaceOnDeplete = ReplaceOnDeplete;
    }

    public String getReplaceOnDeplete()
    {
        return ReplaceOnDeplete;
    }

    protected boolean bUseWhileEquiped;
    protected int ticksPerEquipUse;
    protected float useDelta;
    protected float delta;
    protected int ticks;
    public String ReplaceOnDeplete;
}
