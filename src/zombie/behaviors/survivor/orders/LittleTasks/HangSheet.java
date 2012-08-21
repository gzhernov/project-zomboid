// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HangSheet.java

package zombie.behaviors.survivor.orders.LittleTasks;

import zombie.behaviors.Behavior;
import zombie.behaviors.survivor.orders.Order;
import zombie.characters.IsoGameCharacter;
import zombie.inventory.ItemContainer;
import zombie.iso.*;
import zombie.iso.objects.IsoCurtain;
import zombie.iso.objects.IsoWindow;

public class HangSheet extends Order
{

    public HangSheet(IsoGameCharacter chr, IsoWindow win, IsoGridSquare spot)
    {
        super(chr);
        door = null;
        door = win;
        sq = spot;
        this.chr = chr;
    }

    public boolean complete()
    {
        return true;
    }

    public zombie.behaviors.Behavior.BehaviorResult process()
    {
        if(!character.getInventory().contains("Sheet"))
            return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
        if(door.HasCurtains() != null)
            return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
        int gid = IsoCell.getSheetCurtains();
        IsoDirections d = IsoDirections.N;
        if(door.north && sq == door.square)
            d = IsoDirections.N;
        if(door.north && sq != door.square)
            d = IsoDirections.S;
        if(!door.north && sq == door.square)
            d = IsoDirections.W;
        if(!door.north && sq != door.square)
            d = IsoDirections.E;
        gid = 16;
        if(d == IsoDirections.E)
            gid++;
        if(d == IsoDirections.S)
            gid += 3;
        if(d == IsoDirections.N)
            gid += 2;
        gid += 4;
        IsoCurtain c = new IsoCurtain(door.getCell(), sq, (new StringBuilder()).append("TileObjects3_").append(gid).toString(), door.north);
        sq.AddSpecialTileObject(c);
        if(c.open)
            c.ToggleDoorSilent();
        character.getInventory().RemoveOneOf("Sheet");
        return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
    }

    public void update()
    {
    }

    IsoWindow door;
    IsoGameCharacter chr;
    IsoGridSquare sq;
}
