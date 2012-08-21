// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Barricade.java

package zombie.behaviors.survivor.orders.LittleTasks;

import java.util.Stack;
import zombie.behaviors.Behavior;
import zombie.behaviors.survivor.orders.Order;
import zombie.characters.CharacterTimedActions.BarricadeAction;
import zombie.characters.IsoGameCharacter;
import zombie.iso.objects.IsoDoor;

public class Barricade extends Order
{

    public Barricade(IsoGameCharacter chr, IsoDoor door)
    {
        super(chr);
        this.door = null;
        level = 2;
        this.door = door;
        this.chr = chr;
    }

    public boolean complete()
    {
        return door.Barricaded >= level;
    }

    public zombie.behaviors.Behavior.BehaviorResult process()
    {
        if(chr.getCharacterActions().isEmpty() && door.Barricaded < level)
            chr.StartAction(new BarricadeAction(chr, door));
        if(door.Barricaded >= level)
            return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
        else
            return zombie.behaviors.Behavior.BehaviorResult.Working;
    }

    public void update()
    {
    }

    IsoDoor door;
    IsoGameCharacter chr;
    int level;
}
