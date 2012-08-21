// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ZomboidGlobals.java

package zombie;

import se.krka.kahlua.vm.KahluaTable;
import zombie.Lua.LuaManager;

public class ZomboidGlobals
{

    public ZomboidGlobals()
    {
    }

    public static void Load()
    {
        KahluaTable globals = (KahluaTable)LuaManager.env.rawget("ZomboidGlobals");
        RunningEnduranceReduce = ((Double)globals.rawget("RunningEnduranceReduce")).doubleValue();
        ImobileEnduranceReduce = ((Double)globals.rawget("ImobileEnduranceIncrease")).doubleValue();
        ThirstIncrease = ((Double)globals.rawget("ThirstIncrease")).doubleValue();
        ThirstSleepingIncrease = ((Double)globals.rawget("ThirstSleepingIncrease")).doubleValue();
        ThirstLevelToAutoDrink = ((Double)globals.rawget("ThirstLevelToAutoDrink")).doubleValue();
        ThirstLevelReductionOnAutoDrink = ((Double)globals.rawget("ThirstLevelReductionOnAutoDrink")).doubleValue();
        HungerIncrease = ((Double)globals.rawget("HungerIncrease")).doubleValue();
        HungerIncreaseWhenWellFed = ((Double)globals.rawget("HungerIncreaseWhenWellFed")).doubleValue();
        HungerIncreaseWhileAsleep = ((Double)globals.rawget("HungerIncreaseWhileAsleep")).doubleValue();
        FatigueIncrease = ((Double)globals.rawget("FatigueIncrease")).doubleValue();
        StressReduction = ((Double)globals.rawget("StressDecrease")).doubleValue();
        BoredomIncreaseRate = ((Double)globals.rawget("BoredomIncrease")).doubleValue();
        BoredomDecreaseRate = ((Double)globals.rawget("BoredomDecrease")).doubleValue();
        UnhappinessIncrease = ((Double)globals.rawget("UnhappinessIncrease")).doubleValue();
        StressFromSoundsMultiplier = ((Double)globals.rawget("StressFromSoundsMultiplier")).doubleValue();
        StressFromBiteOrScratch = ((Double)globals.rawget("StressFromBiteOrScratch")).doubleValue();
        AngerDecrease = ((Double)globals.rawget("AngerDecrease")).doubleValue();
        BroodingAngerDecreaseMultiplier = ((Double)globals.rawget("BroodingAngerDecreaseMultiplier")).doubleValue();
        SleepFatigueReduction = ((Double)globals.rawget("SleepFatigueReduction")).doubleValue();
    }

    public static double RunningEnduranceReduce = 0.0D;
    public static double ImobileEnduranceReduce = 0.0D;
    public static double ThirstIncrease = 0.0D;
    public static double ThirstSleepingIncrease = 0.0D;
    public static double ThirstLevelToAutoDrink = 0.0D;
    public static double ThirstLevelReductionOnAutoDrink = 0.0D;
    public static double HungerIncrease = 0.0D;
    public static double HungerIncreaseWhenWellFed = 0.0D;
    public static double HungerIncreaseWhileAsleep = 0.0D;
    public static double FatigueIncrease = 0.0D;
    public static double StressReduction = 0.0D;
    public static double BoredomIncreaseRate = 0.0D;
    public static double BoredomDecreaseRate = 0.0D;
    public static double UnhappinessIncrease = 0.0D;
    public static double StressFromSoundsMultiplier = 0.0D;
    public static double StressFromBiteOrScratch = 0.0D;
    public static double AngerDecrease = 0.0D;
    public static double BroodingAngerDecreaseMultiplier = 0.0D;
    public static double SleepFatigueReduction = 0.0D;

}