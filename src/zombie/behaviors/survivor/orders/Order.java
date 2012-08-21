// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Order.java

package zombie.behaviors.survivor.orders;

import java.util.Stack;
import zombie.behaviors.Behavior;
import zombie.characters.IsoGameCharacter;

public abstract class Order
{

    public Order(IsoGameCharacter chr)
    {
        character = null;
        type = "Order";
        name = "unnamed";
        bInit = false;
        character = chr;
    }

    public abstract zombie.behaviors.Behavior.BehaviorResult process();

    public abstract boolean complete();

    public boolean ActedThisFrame()
    {
        return true;
    }

    public zombie.behaviors.Behavior.BehaviorResult processNext()
    {
        for(int n = character.getOrders().size() - 1; n >= 0; n--)
            if(character.getOrders().get(n) == this && n > 1)
                return ((Order)character.getOrders().get(n - 1)).process();

        return zombie.behaviors.Behavior.BehaviorResult.Succeeded;
    }

    public void updatenext()
    {
        for(int n = character.getOrders().size() - 1; n >= 0; n--)
            if(character.getOrders().get(n) == this && n > 1)
                ((Order)character.getOrders().get(n - 1)).update();

    }

    public abstract void update();

    public boolean isCancelledOnAttack()
    {
        return true;
    }

    public void initOrder()
    {
    }

    public float getPriority(IsoGameCharacter character)
    {
        return 100000F;
    }

    public int renderDebug(int y)
    {
        return y;
    }

    public float getPathSpeed()
    {
        if(character.getDangerLevels() == 0.0F)
            return 0.06F;
        float delta = 10F / character.getDangerLevels();
        if(delta > 1.0F)
            delta = 1.0F;
        if(delta < 0.0F)
            delta = 0.0F;
        return 0.06F + 0.02F * delta;
    }

    public int getAttackIfEnemiesAroundBias()
    {
        return 0;
    }

    public boolean isCritical()
    {
        return false;
    }

    public IsoGameCharacter character;
    public String type;
    public String name;
    public boolean bInit;
}
