// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EatFoodOrder.java

package zombie.behaviors.survivor.orders.LittleTasks;

import zombie.behaviors.Behavior;
import zombie.behaviors.survivor.orders.Order;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.IsoGameCharacter;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.Food;

public class EatFoodOrder extends Order
{

    public EatFoodOrder(IsoGameCharacter chr)
    {
        super(chr);
        this.chr = chr;
    }

    public zombie.behaviors.Behavior.BehaviorResult process()
    {
        return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
    }

    public void initOrder()
    {
        InventoryItem item = chr.getInventory().getBestFood(chr.getDescriptor());
        if(item == null)
        {
            return;
        } else
        {
            chr.Eat(item);
            chr.getBodyDamage().JustAteFood((Food)item);
            item.Use();
            return;
        }
    }

    public boolean complete()
    {
        return true;
    }

    public void update()
    {
    }

    IsoGameCharacter chr;
}
