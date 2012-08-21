// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CloseDoor.java

package zombie.behaviors.survivor.orders.LittleTasks;

import zombie.behaviors.Behavior;
import zombie.behaviors.survivor.orders.Order;
import zombie.characters.IsoGameCharacter;
import zombie.iso.objects.IsoDoor;

public class CloseDoor extends Order
{

    public CloseDoor(IsoGameCharacter chr, IsoDoor door)
    {
        super(chr);
        this.door = null;
        this.door = door;
        this.chr = chr;
    }

    public boolean complete()
    {
        if(door == null)
            return true;
        else
            return !door.open;
    }

    public zombie.behaviors.Behavior.BehaviorResult process()
    {
        if(door != null && door.open)
            door.ToggleDoor(chr);
        return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
    }

    public void update()
    {
    }

    IsoDoor door;
    IsoGameCharacter chr;
}
