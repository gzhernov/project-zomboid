// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CloseCurtains.java

package zombie.behaviors.survivor.orders.LittleTasks;

import zombie.behaviors.Behavior;
import zombie.behaviors.survivor.orders.Order;
import zombie.characters.IsoGameCharacter;
import zombie.iso.objects.IsoCurtain;

public class CloseCurtains extends Order
{

    public CloseCurtains(IsoGameCharacter chr, IsoCurtain curtain)
    {
        super(chr);
        door = null;
        door = curtain;
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

    IsoCurtain door;
    IsoGameCharacter chr;
}
