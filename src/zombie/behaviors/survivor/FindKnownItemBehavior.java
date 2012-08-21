// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FindKnownItemBehavior.java

package zombie.behaviors.survivor;

import zombie.behaviors.Behavior;
import zombie.behaviors.DecisionPath;
import zombie.characters.IsoGameCharacter;
import zombie.inventory.ItemContainer;
import zombie.inventory.ItemType;
import zombie.iso.IsoGridSquare;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.areas.IsoRoom;

public class FindKnownItemBehavior extends Behavior
{

    public FindKnownItemBehavior()
    {
        FindItem = ItemType.None;
        Found = false;
        LocationIsInventory = false;
    }

    public zombie.behaviors.Behavior.BehaviorResult process(DecisionPath path, IsoGameCharacter character)
    {
        Found = false;
        if(character.getInventory().HasType(FindItem))
        {
            LocationIsInventory = true;
            Found = true;
            return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
        }
        LocationIsInventory = false;
        if(character.getCurrentSquare().getRoom() != null)
        {
            container = character.getCurrentSquare().getRoom().building.getContainerWith(FindItem);
            if(container != null)
            {
                Found = true;
                return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
            }
        }
        return zombie.behaviors.Behavior.BehaviorResult.Failed;
    }

    public void reset()
    {
    }

    public boolean valid()
    {
        return true;
    }

    public ItemType FindItem;
    public boolean Found;
    public boolean LocationIsInventory;
    ItemContainer container;
}
