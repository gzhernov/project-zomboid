// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Need.java

package zombie.behaviors.survivor.orders.Needs;


public class Need
{

    public Need(String item, int priority)
    {
        numToSatisfy = 1;
        this.item = item;
        this.priority = priority;
    }

    public int priority;
    public String item;
    public int numToSatisfy;
}
