// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Heal.java

package zombie.behaviors.survivor.orders.Needs;

import java.util.Stack;
import zombie.behaviors.survivor.orders.LittleTasks.EatFoodOrder;
import zombie.behaviors.survivor.orders.ObtainItem;
import zombie.behaviors.survivor.orders.OrderSequence;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.IsoGameCharacter;
import zombie.characters.Moodles.MoodleType;
import zombie.characters.Moodles.Moodles;
import zombie.inventory.ItemContainer;

public class Heal extends OrderSequence
{

    public Heal(IsoGameCharacter chr)
    {
        super(chr);
        Items = new Stack();
        obtain = null;
        type = "Heal";
    }

    public void initOrder()
    {
        Items.add("Type:Food");
        if(character.getInventory().getBestFood(character.getDescriptor()) == null)
        {
            obtain = new ObtainItem(character, Items, 1000);
            Orders.add(obtain);
        }
        Orders.add(new EatFoodOrder(character));
    }

    public boolean isCritical()
    {
        return character.getMoodles().getMoodleLevel(MoodleType.Hungry) > 1 || character.getBodyDamage().getHealth() <= 60F;
    }

    public boolean ActedThisFrame()
    {
        if(obtain != null && Orders.get(ID) == obtain)
            return obtain.ActedThisFrame();
        else
            return false;
    }

    public Stack Items;
    ObtainItem obtain;
}
