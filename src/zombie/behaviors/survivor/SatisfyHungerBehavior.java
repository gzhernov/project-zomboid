// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SatisfyHungerBehavior.java

package zombie.behaviors.survivor;

import zombie.behaviors.Behavior;
import zombie.behaviors.DecisionPath;
import zombie.characters.IsoGameCharacter;
import zombie.inventory.ItemContainer;
import zombie.inventory.ItemType;

// Referenced classes of package zombie.behaviors.survivor:
//            ObtainItemBehavior

public class SatisfyHungerBehavior extends Behavior
{

    public SatisfyHungerBehavior()
    {
        obtain = new ObtainItemBehavior();
    }

    public zombie.behaviors.Behavior.BehaviorResult process(DecisionPath path, IsoGameCharacter character)
    {
        obtain.FindItem = ItemType.Food;
        obtain.Found = false;
        zombie.behaviors.Behavior.BehaviorResult res = obtain.process(path, character);
        if(res == zombie.behaviors.Behavior.BehaviorResult.Succeeded)
        {
            zombie.inventory.InventoryItem item = character.getInventory().Remove(ItemType.Food);
            character.Eat(item);
            reset();
        }
        return res;
    }

    public void reset()
    {
        obtain.reset();
        obtain.FindItem = ItemType.Food;
        obtain.Found = false;
        obtain.HaveLocation = false;
        obtain.DoneFindItem = false;
        obtain.container = null;
    }

    public boolean valid()
    {
        return true;
    }

    ObtainItemBehavior obtain;
}
