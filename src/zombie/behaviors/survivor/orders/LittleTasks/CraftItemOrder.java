// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CraftItemOrder.java

package zombie.behaviors.survivor.orders.LittleTasks;

import java.util.ArrayList;
import zombie.behaviors.Behavior;
import zombie.behaviors.survivor.orders.Order;
import zombie.characters.IsoGameCharacter;
import zombie.inventory.*;
import zombie.inventory.types.Drainable;
import zombie.scripting.objects.Recipe;
import zombie.scripting.objects.ScriptModule;

public class CraftItemOrder extends Order
{

    public CraftItemOrder(IsoGameCharacter chr, Recipe rec)
    {
        super(chr);
        this.chr = chr;
        this.rec = rec;
    }

    public zombie.behaviors.Behavior.BehaviorResult process()
    {
        return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
    }

    public void initOrder()
    {
        PerformMakeItem();
    }

    public boolean complete()
    {
        return true;
    }

    public void update()
    {
    }

    void DoDrainOnItem(InventoryItem ing1, String resType)
    {
        if(RecipeManager.DoesWipeUseDelta(ing1.getType(), resType))
            ((Drainable)ing1).setUsedDelta(0.0F);
        if(RecipeManager.DoesUseItemUp(ing1.getType(), rec))
        {
            int size = RecipeManager.UseAmount(ing1.getType(), rec);
            for(int n = 0; n < size; n++)
                ing1.Use(true);

        }
    }

    void PerformMakeItem()
    {
        InventoryItem ing1 = null;
        if(((zombie.scripting.objects.Recipe.Source)rec.Source.get(0)).type != null)
        {
            ing1 = character.getInventory().FindAndReturn(((zombie.scripting.objects.Recipe.Source)rec.Source.get(0)).type);
            if(ing1 == null)
                return;
            DoDrainOnItem(ing1, rec.Result.type);
        }
        if(rec.Source.size() > 1 && ((zombie.scripting.objects.Recipe.Source)rec.Source.get(1)).type != null)
        {
            ing1 = character.getInventory().FindAndReturn(((zombie.scripting.objects.Recipe.Source)rec.Source.get(1)).type);
            if(ing1 == null)
                return;
            DoDrainOnItem(ing1, rec.Result.type);
        }
        if(rec.Source.size() > 2 && ((zombie.scripting.objects.Recipe.Source)rec.Source.get(2)).type != null)
        {
            ing1 = character.getInventory().FindAndReturn(((zombie.scripting.objects.Recipe.Source)rec.Source.get(2)).type);
            if(ing1 == null)
                return;
            DoDrainOnItem(ing1, rec.Result.type);
        }
        if(rec.Source.size() > 3 && ((zombie.scripting.objects.Recipe.Source)rec.Source.get(3)).type != null)
        {
            ing1 = character.getInventory().FindAndReturn(((zombie.scripting.objects.Recipe.Source)rec.Source.get(3)).type);
            if(ing1 == null)
                return;
            DoDrainOnItem(ing1, rec.Result.type);
        }
        character.getInventory().AddItem((new StringBuilder()).append(rec.module.name).append(".").append(rec.Result.type).toString());
    }

    IsoGameCharacter chr;
    Recipe rec;
}
