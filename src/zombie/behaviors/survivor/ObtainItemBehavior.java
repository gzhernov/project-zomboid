// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ObtainItemBehavior.java

package zombie.behaviors.survivor;

import zombie.behaviors.Behavior;
import zombie.behaviors.DecisionPath;
import zombie.behaviors.general.PathFindBehavior;
import zombie.characters.IsoGameCharacter;
import zombie.inventory.ItemContainer;
import zombie.inventory.ItemType;
import zombie.iso.IsoGridSquare;

// Referenced classes of package zombie.behaviors.survivor:
//            FindKnownItemBehavior

public class ObtainItemBehavior extends Behavior
{

    public ObtainItemBehavior()
    {
        FindItem = ItemType.None;
        DoneFindItem = false;
        findItem = new FindKnownItemBehavior();
        Found = false;
        HaveLocation = false;
        LocationIsInventory = false;
        pathFind = new PathFindBehavior("ObtainItem");
    }

    public zombie.behaviors.Behavior.BehaviorResult process(DecisionPath path, IsoGameCharacter character)
    {
        if(!HaveLocation)
        {
            if(!DoneFindItem)
            {
                DoneFindItem = true;
                findItem.reset();
                findItem.FindItem = FindItem;
                zombie.behaviors.Behavior.BehaviorResult res = findItem.process(path, character);
                if(res == zombie.behaviors.Behavior.BehaviorResult.Failed)
                    return zombie.behaviors.Behavior.BehaviorResult.Working;
                if(res == zombie.behaviors.Behavior.BehaviorResult.Succeeded)
                {
                    if(findItem.LocationIsInventory)
                    {
                        LocationIsInventory = true;
                        Found = true;
                        return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
                    }
                    LocationIsInventory = false;
                    Found = true;
                    container = findItem.container;
                    pathFind.reset();
                    pathFind.sx = (int)character.getX();
                    pathFind.sy = (int)character.getY();
                    pathFind.sz = (int)character.getZ();
                    pathFind.tx = container.SourceGrid.getX();
                    pathFind.ty = container.SourceGrid.getY();
                    pathFind.tz = container.SourceGrid.getZ();
                    HaveLocation = true;
                }
            }
        } else
        {
            zombie.behaviors.Behavior.BehaviorResult result = pathFind.process(path, character);
            if(result == zombie.behaviors.Behavior.BehaviorResult.Succeeded)
            {
                zombie.inventory.InventoryItem item = container.Remove(FindItem);
                pathFind.reset();
                character.setPath(null);
                if(item != null)
                {
                    character.getInventory().AddItem(item);
                    return result;
                }
            }
            if(result == zombie.behaviors.Behavior.BehaviorResult.Failed)
                return result;
        }
        return zombie.behaviors.Behavior.BehaviorResult.Working;
    }

    public void reset()
    {
        HaveLocation = false;
        findItem.reset();
    }

    public boolean valid()
    {
        return true;
    }

    public ItemType FindItem;
    ItemContainer container;
    boolean DoneFindItem;
    FindKnownItemBehavior findItem;
    boolean Found;
    boolean HaveLocation;
    boolean LocationIsInventory;
    PathFindBehavior pathFind;
}
