// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PerkFactory.java

package zombie.characters.skills;

import gnu.trove.map.hash.THashMap;
import java.util.*;
import zombie.SoundManager;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Collections.NulledArrayList;

public class PerkFactory
{
    public static class Perk
    {

        public String name;
        public String level1;
        public String level2;
        public String level3;
        public String level4;
        public String level5;
        public int xp1;
        public int xp2;
        public int xp3;
        public int xp4;
        public int xp5;
        public Perks parent;
        public Perks type;

        public Perk(String name, String level1, String level2, String level3, String level4, String level5)
        {
            parent = Perks.None;
            type = Perks.None;
            this.name = name;
            this.level1 = level1;
            this.level2 = level2;
            this.level3 = level3;
            this.level4 = level4;
            this.level5 = level5;
        }

        public Perk(String name, String level1, String level2, String level3, String level4, String level5, Perks parent)
        {
            this.parent = Perks.None;
            type = Perks.None;
            this.name = name;
            this.level1 = level1;
            this.level2 = level2;
            this.level3 = level3;
            this.level4 = level4;
            this.level5 = level5;
            this.parent = parent;
        }
    }

    /*     */   public static enum Perks
    /*     */   {
    /*  26 */     None(0), 
    /*  27 */     Agility(1), 
    /*  28 */     Cooking(2), 
    /*  29 */     Melee(3), 
    /*  30 */     Crafting(4), 
    /*  31 */     Fitness(5), 
    /*  32 */     Strength(6), 
    /*  33 */     Blunt(7), 
    /*  34 */     Axe(8), 
    /*  35 */     Sprinting(9), 
    /*  36 */     Pacing(10), 
    /*  37 */     Swinging(11), 
    /*  38 */     Recovery(12), 
    /*  39 */     Hauling(13), 
    /*  40 */     Hitting(14), 
    /*  41 */     Shoving(15), 
    /*  42 */     Lightfoot(16), 
    /*  43 */     Nimble(17), 
    /*  44 */     Sneak(18), 
    /*  45 */     Woodwork(19), 
    /*  46 */     Aiming(20), 
    /*  47 */     Reloading(21), 
    /*  48 */     MAX(22);
    /*     */ 
    /*     */     private int index;
    /*     */ 
    /*     */     private Perks(int index) {
    /*  54 */       this.index = index;
    /*     */     }
    /*     */ 
    /*     */     public int index()
    /*     */     {
    /*  59 */       return this.index;
    /*     */     }
    /*     */ 
    /*     */     public static Perks fromIndex(int value)
    /*     */     {
    /*  64 */       return ((Perks[])Perks.class.getEnumConstants())[value];
    /*     */     }
    /*     */ 
    /*     */     public static Perks FromString(String str)
    /*     */     {
    /*     */       try
    /*     */       {
    /*  71 */         return valueOf(str);
    /*     */       }
    /*     */       catch (Exception ex) {
    /*     */       }
    /*  75 */       return MAX;
    /*     */     }
    /*     */   }


    public PerkFactory()
    {
    }

    public static Perks getPerkFromName(String name)
    {
        for(Iterator it = PerkMap.entrySet().iterator(); it.hasNext();)
        {
            java.util.Map.Entry e = (java.util.Map.Entry)it.next();
            if(((Perk)e.getValue()).name.equals(name))
                return (Perks)e.getKey();
        }

        return null;
    }

    public static void AddPerk(Perks perk, String name, String level1, String level2, String level3, String level4, String level5, int xp1, 
            int xp2, int xp3, int xp4, int xp5)
    {
        Perk p = new Perk(name, level1, level2, level3, level4, level5);
        p.type = perk;
        p.xp1 = (int)((float)xp1 * PerkXPReqMultiplier);
        p.xp2 = (int)((float)xp2 * PerkXPReqMultiplier);
        p.xp3 = (int)((float)xp3 * PerkXPReqMultiplier);
        p.xp4 = (int)((float)xp4 * PerkXPReqMultiplier);
        p.xp5 = (int)((float)xp5 * PerkXPReqMultiplier);
        PerkMap.put(perk, p);
        PerkList.add(p);
    }

    public static void AddPerk(Perks perk, String name, String level1, String level2, String level3, String level4, String level5, Perks parent, 
            int xp1, int xp2, int xp3, int xp4, int xp5)
    {
        Perk p = new Perk(name, level1, level2, level3, level4, level5, parent);
        p.type = perk;
        p.xp1 = (int)((float)xp1 * PerkXPReqMultiplier);
        p.xp2 = (int)((float)xp2 * PerkXPReqMultiplier);
        p.xp3 = (int)((float)xp3 * PerkXPReqMultiplier);
        p.xp4 = (int)((float)xp4 * PerkXPReqMultiplier);
        p.xp5 = (int)((float)xp5 * PerkXPReqMultiplier);
        PerkMap.put(perk, p);
        PerkList.add(p);
    }

    public static void init()
    {
        AddPerk(Perks.Melee, "Combat", "", "", "", "", "", 50, 150, 750, 3650, 5450);
        AddPerk(Perks.Blunt, "Blunt", "", "", "", "", "", Perks.Melee, 50, 150, 750, 3650, 5450);
        AddPerk(Perks.Axe, "Blade", "", "", "", "", "", Perks.Melee, 50, 150, 750, 3650, 5450);
        AddPerk(Perks.Aiming, "Aiming", "", "", "", "", "", Perks.Aiming, 50, 150, 750, 3650, 5450);
        AddPerk(Perks.Reloading, "Reloading", "", "", "", "", "", Perks.Reloading, 50, 150, 750, 3650, 5450);
        AddPerk(Perks.Crafting, "Crafting", "", "", "", "", "", 50, 150, 750, 3650, 5450);
        AddPerk(Perks.Woodwork, "Carpentry", "", "", "", "", "", Perks.Crafting, 50, 150, 750, 3650, 5450);
        AddPerk(Perks.Cooking, "Cooking", "", "", "", "", "", Perks.Crafting, 50, 150, 750, 3650, 5450);
        AddPerk(Perks.Fitness, "Fitness", "", "", "", "", "", 50, 150, 750, 3650, 5450);
        AddPerk(Perks.Pacing, "Running", "", "", "", "", "", Perks.Fitness, 50, 150, 750, 3650, 5450);
        AddPerk(Perks.Swinging, "Swinging", "", "", "", "", "", Perks.Fitness, 50, 150, 750, 3650, 5450);
        AddPerk(Perks.Recovery, "Recovery", "", "", "", "", "", Perks.Fitness, 50, 150, 750, 3650, 5450);
        AddPerk(Perks.Strength, "Strength", "", "", "", "", "", 50, 150, 750, 3650, 5450);
        AddPerk(Perks.Hauling, "Hauling", "", "", "", "", "", Perks.Strength, 50, 150, 750, 3650, 5450);
        AddPerk(Perks.Hitting, "Hitting", "", "", "", "", "", Perks.Strength, 50, 150, 750, 3650, 5450);
        AddPerk(Perks.Shoving, "Shoving", "", "", "", "", "", Perks.Strength, 50, 150, 750, 3650, 5450);
        AddPerk(Perks.Agility, "Agility", "", "", "", "", "", 50, 150, 750, 3650, 5450);
        AddPerk(Perks.Sprinting, "Sprinting", "", "", "", "", "", Perks.Agility, 50, 150, 750, 3650, 5450);
        AddPerk(Perks.Lightfoot, "Lightfooted", "", "", "", "", "", Perks.Agility, 50, 150, 750, 3650, 5450);
        AddPerk(Perks.Nimble, "Nimble", "", "", "", "", "", Perks.Agility, 50, 150, 750, 3650, 5450);
        AddPerk(Perks.Sneak, "Sneaking", "", "", "", "", "", Perks.Agility, 50, 150, 750, 3650, 5450);
    }

    public static void CheckForUnlockedPerks(IsoGameCharacter chr)
    {
        for(int n = 0; n < PerkList.size(); n++)
        {
            Perk p = (Perk)PerkList.get(n);
            int level = chr.getPerkLevel(p.type);
            int test = p.xp1;
            if(level == 1)
                test = p.xp2;
            if(level == 2)
                test = p.xp3;
            if(level == 3)
                test = p.xp4;
            if(level == 4)
                test = p.xp5;
            if(level == 5 || test > chr.getXp().getXP(p.type) || chr.getCanUpgradePerk().contains(p.type))
                continue;
            if(chr.getCanUpgradePerk().isEmpty() && IsoPlayer.getInstance().getNumberOfPerksToPick() > 0)
                SoundManager.instance.PlaySound("levelup", false, 0.6F);
            chr.getCanUpgradePerk().add(p.type);
        }

    }

    public static THashMap PerkMap = new THashMap();
    public static ArrayList PerkList = new ArrayList();
    static float PerkXPReqMultiplier = 2.0F;

}
