// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ProfessionFactory.java

package zombie.characters.professions;

import gnu.trove.map.hash.THashMap;
import java.util.Stack;
import zombie.interfaces.IListBoxItem;

public class ProfessionFactory
{
    public static class Profession
        implements IListBoxItem
    {

        public void addFreeTrait(String trait)
        {
            FreeTraitStack.add(trait);
        }

        public String getLabel()
        {
            return getName();
        }

        public String getIconPath()
        {
            return IconPath;
        }

        public String getLeftLabel()
        {
            return getName();
        }

        public String getRightLabel()
        {
            int cost = getCost();
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

        public String getType()
        {
            return type;
        }

        public void setType(String type)
        {
            this.type = type;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public int getCost()
        {
            return cost;
        }

        public void setCost(int cost)
        {
            this.cost = cost;
        }

        public String getDescription()
        {
            return description;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

        public void setIconPath(String IconPath)
        {
            this.IconPath = IconPath;
        }

        public Stack getFreeTraitStack()
        {
            return FreeTraitStack;
        }

        public void setFreeTraitStack(Stack FreeTraitStack)
        {
            this.FreeTraitStack = FreeTraitStack;
        }

        public String type;
        public String name;
        public int cost;
        public String description;
        public String IconPath;
        public Stack FreeTraitStack;

        public Profession(String type, String name, String IconPathname, int cost, String desc)
        {
            FreeTraitStack = new Stack();
            this.type = type;
            this.name = name;
            IconPath = IconPathname;
            this.cost = cost;
            description = desc;
        }
    }


    public ProfessionFactory()
    {
    }

    public static void init()
    {
    }

    public static Profession addProfession(String type, String name, String IconPath)
    {
        Profession prof = new Profession(type, name, IconPath, 0, "");
        ProfessionMap.put(type, prof);
        return prof;
    }

    public static THashMap ProfessionMap = new THashMap();

}
