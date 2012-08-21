// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ObtainItem.java

package zombie.behaviors.survivor.orders;

import java.util.ArrayList;
import java.util.Stack;
import zombie.behaviors.survivor.orders.LittleTasks.CraftItemOrder;
import zombie.behaviors.survivor.orders.LittleTasks.TakeItemFromContainer;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.inventory.ItemContainer;
import zombie.iso.*;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.areas.IsoRoom;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Recipe;

// Referenced classes of package zombie.behaviors.survivor.orders:
//            OrderSequence, GotoNextTo, GotoBuildingOrder

public class ObtainItem extends OrderSequence
{

    public ObtainItem(IsoGameCharacter chr, Stack AcceptableItems, int priority)
    {
        super(chr);
        items = new Stack();
        this.priority = 1.0F;
        items.addAll(AcceptableItems);
        this.priority = priority;
    }

    public ObtainItem(IsoGameCharacter character, String type, int priority)
    {
        super(character);
        items = new Stack();
        this.priority = 1.0F;
        items.add(type);
        this.priority = priority;
    }

    public void initOrder()
    {
        for(int n = 0; n < items.size(); n++)
        {
            String item = (String)items.get(n);
            if(character.getInventory().contains(item))
                return;
        }

        if(character.getCurrentSquare().getRoom() != null && character.getCurrentSquare().getRoom().building != null && character.getCurrentSquare().getRoom().building != character.getDescriptor().getGroup().Safehouse && character.getCurrentSquare().getRoom().building != null && CheckBuildingForItems(character.getCurrentSquare().getRoom().building))
            return;
        if(character.getDescriptor().getGroup().Safehouse != null && CheckBuildingForItems(character.getDescriptor().getGroup().Safehouse))
            return;
        if(!IsoWorld.instance.CurrentCell.getBuildingList().isEmpty())
        {
            for(int n = 0; n < 10; n++)
            {
                IsoBuilding b = (IsoBuilding)IsoWorld.instance.CurrentCell.getBuildingList().get(Rand.Next(IsoWorld.instance.CurrentCell.getBuildingList().size()));
                CheckBuildingForItems(b);
            }

        }
        if(Orders.isEmpty())
        {
            boolean bDone = false;
            int n = Rand.Next(items.size());
            Stack rec = ScriptManager.instance.getAllRecipesFor((String)items.get(n));
            if(rec.size() > 0)
            {
                Recipe r = (Recipe)rec.get(Rand.Next(rec.size()));
                for(int m = 0; m < r.Source.size(); m++)
                    if(((zombie.scripting.objects.Recipe.Source)r.Source.get(m)).type != null)
                        Orders.add(new ObtainItem(character, ((zombie.scripting.objects.Recipe.Source)r.Source.get(m)).type, (int)priority));

                Orders.add(new CraftItemOrder(character, r));
            }
        }
        if(Orders.isEmpty())
        {
            for(int n = 0; n < items.size(); n++)
                character.getDescriptor().getGroup().AddNeed((String)items.get(n), (int)priority);

        }
    }

    public boolean ActedThisFrame()
    {
        if(Orders.isEmpty())
            return false;
        return (Orders.get(ID) instanceof GotoNextTo) || (Orders.get(ID) instanceof GotoBuildingOrder);
    }

    private boolean CheckBuildingForItems(IsoBuilding building)
    {
        for(int n = 0; n < building.container.size(); n++)
        {
            for(int m = 0; m < items.size(); m++)
            {
                String item = (String)items.get(m);
                if(((ItemContainer)building.container.get(n)).contains(item))
                {
                    ItemContainer con = (ItemContainer)building.container.get(n);
                    Orders.add(new GotoNextTo(character, con.parent.square.getX(), con.parent.square.getY(), con.parent.square.getZ()));
                    Orders.add(new TakeItemFromContainer(character, con, item));
                    if(character.getCurrentSquare().getRoom() == null || character.getCurrentSquare().getRoom().building != building)
                        Orders.insertElementAt(new GotoBuildingOrder(character, building), 0);
                    return true;
                }
            }

        }

        return false;
    }

    Stack items;
    public float priority;
}
