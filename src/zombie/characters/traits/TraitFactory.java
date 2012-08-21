// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TraitFactory.java

package zombie.characters.traits;

import gnu.trove.map.hash.THashMap;
import zombie.core.Collections.NulledArrayList;
import zombie.interfaces.IListBoxItem;

public class TraitFactory
{
    public static class Trait
        implements IListBoxItem
    {

        public String getLabel()
        {
            return name;
        }

        public String getLeftLabel()
        {
            return name;
        }

        public String getRightLabel()
        {
            int cost = this.cost;
            if(cost == 0)
                return "";
            String before = "+";
            if(cost > 0)
                before = "-";
            else
            if(cost == 0)
                before = "";
            if(cost < 0)
                cost = -cost;
            return (new StringBuilder()).append(before).append((new Integer(cost)).toString()).toString();
        }

        public String traitID;
        public String name;
        public int cost;
        public String description;
        public boolean prof;
        public NulledArrayList MutuallyExclusive;

        public Trait(String tr, String name, int cost, String desc, boolean prof)
        {
            MutuallyExclusive = new NulledArrayList(0);
            traitID = tr;
            this.name = name;
            this.cost = cost;
            description = desc;
            this.prof = prof;
        }
    }


    public TraitFactory()
    {
    }

    public static void init()
    {
    }

    public static void setMutualExclusive(String a, String b)
    {
        ((Trait)TraitMap.get(a)).MutuallyExclusive.add(b);
        ((Trait)TraitMap.get(b)).MutuallyExclusive.add(a);
    }

    public static void addTrait(String type, String name, int cost, String desc, boolean profession)
    {
        TraitMap.put(type, new Trait(type, name, cost, desc, profession));
    }

    public static Trait getTrait(String name)
    {
        if(TraitMap.containsKey(name))
            return (Trait)TraitMap.get(name);
        else
            return null;
    }

    public static THashMap TraitMap = new THashMap();

}
