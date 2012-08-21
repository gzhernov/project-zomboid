// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Literature.java

package zombie.inventory.types;

import zombie.inventory.InventoryItem;
import zombie.inventory.ItemType;
import zombie.ui.ObjectTooltip;

public class Literature extends InventoryItem
{

    public Literature(String module, String name, String itemType, String texName)
    {
        super(module, name, itemType, texName);
        bAlreadyRead = false;
        requireInHandOrInventory = null;
        stressChange = 0.0F;
        useOnConsume = null;
        cat = ItemType.Literature;
    }

    public void update()
    {
        if(container == null);
    }

    public boolean CanStack(InventoryItem item)
    {
        return name.equals(((Literature)item).name);
    }

    public boolean finishupdate()
    {
        return true;
    }

    public void DoTooltip(ObjectTooltip tooltipUI)
    {
        tooltipUI.render();
        super.DoTooltip(tooltipUI);
        int mid = 150;
        int y = tooltipUI.getHeight();
        y -= 19;
        tooltipUI.setWidth(160);
        if(getBoredomChange() != 0.0F)
        {
            int Boredom = (int)getBoredomChange();
            tooltipUI.DrawText("Boredom Reduction:", 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
            tooltipUI.DrawValueRight(Boredom, mid, y, false);
            y += 14;
        }
        if(getStressChange() != 0.0F)
        {
            int stress = (int)getStressChange();
            tooltipUI.DrawText("Stress Reduction:", 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
            tooltipUI.DrawValueRight(stress, mid, y, false);
        }
        y += 19;
        tooltipUI.setHeight(y);
    }

    public float getBoredomChange()
    {
        if(!bAlreadyRead)
            return boredomChange;
        else
            return 0.0F;
    }

    public float getUnhappyChange()
    {
        if(!bAlreadyRead)
            return unhappyChange;
        else
            return 0.0F;
    }

    public float getStressChange()
    {
        if(!bAlreadyRead)
            return stressChange;
        else
            return 0.0F;
    }

    public boolean bAlreadyRead;
    public String requireInHandOrInventory;
    public float stressChange;
    public String useOnConsume;
}
