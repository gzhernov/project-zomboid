// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InventoryItemFactory.java

package zombie.inventory;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import zombie.GameApplet;
import zombie.inventory.types.Drainable;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Item;

// Referenced classes of package zombie.inventory:
//            InventoryItem

public class InventoryItemFactory
{
	/*     */   public static enum ItemConcreteTypes
	/*     */   {
	/*     */   }


    public InventoryItemFactory()
    {
    }

    public static InventoryItem CreateItem(String itemType)
    {
        return CreateItem(itemType, 1.0F);
    }

    public static InventoryItem CreateItem(String itemType, float useDelta)
    {
        InventoryItem item = null;
        Item scriptItem = ScriptManager.instance.FindItem(itemType);
        if(scriptItem == null)
        {
            JOptionPane.showMessageDialog(null, (new StringBuilder()).append(itemType).append(" not found.").toString(), (new StringBuilder()).append(itemType).append(" item not found.").toString(), 0);
            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, (new StringBuilder()).append(itemType).append(" item not found.").toString());
            return null;
        }
        item = scriptItem.InstanceItem(null);
        if(item instanceof Drainable)
            ((Drainable)item).setUsedDelta(useDelta);
        return item;
    }

    public static InventoryItem CreateItem(String itemType, float useDelta, String param)
    {
        InventoryItem item = null;
        Item scriptItem = ScriptManager.instance.getItem(itemType);
        if(scriptItem == null)
        {
            JOptionPane.showMessageDialog(null, (new StringBuilder()).append(itemType).append(" not found.").toString(), (new StringBuilder()).append(itemType).append(" item not found.").toString(), 0);
            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, (new StringBuilder()).append(itemType).append(" item not found.").toString());
            return null;
        }
        item = scriptItem.InstanceItem(param);
        if(item instanceof Drainable)
            ((Drainable)item).setUsedDelta(useDelta);
        return item;
    }
}
