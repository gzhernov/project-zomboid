// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FaceOrder.java

package zombie.behaviors.survivor.orders.LittleTasks;

import zombie.behaviors.Behavior;
import zombie.behaviors.survivor.orders.Order;
import zombie.characters.IsoGameCharacter;
import zombie.iso.IsoDirections;
import zombie.iso.Vector2;

public class FaceOrder extends Order
{

    public FaceOrder(IsoGameCharacter chr, IsoDirections dir)
    {
        super(chr);
        vec = new Vector2();
        instantComplete = false;
        this.chr = chr;
        this.dir = dir;
        instantComplete = true;
    }

    public FaceOrder(IsoGameCharacter chr, IsoGameCharacter chr2)
    {
        super(chr);
        vec = new Vector2();
        instantComplete = false;
        this.chr = chr;
        this.chr2 = chr2;
    }

    public zombie.behaviors.Behavior.BehaviorResult process()
    {
        if(chr2 == null)
        {
            chr.dir = dir;
        } else
        {
            vec.x = chr2.getX();
            vec.y = chr2.getY();
            vec.x -= character.getX();
            vec.y -= character.getY();
            vec.normalize();
            character.DirectionFromVector(vec);
        }
        return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
    }

    public boolean complete()
    {
        return instantComplete;
    }

    public void update()
    {
    }

    IsoGameCharacter chr;
    IsoGameCharacter chr2;
    IsoDirections dir;
    Vector2 vec;
    boolean instantComplete;
}
