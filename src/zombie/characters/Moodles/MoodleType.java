// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MoodleType.java

package zombie.characters.Moodles;


/*     */ public enum MoodleType
/*     */ {
/*  15 */   Endurance, 
/*  16 */   Tired, 
/*  17 */   Hungry, 
/*  18 */   Panic, 
/*  19 */   Sick, 
/*  20 */   Bored, 
/*  21 */   Unhappy, 
/*  22 */   Bleeding, 
/*  23 */   Wet, 
/*  24 */   HasACold, 
/*  25 */   Angry, 
/*  26 */   Stress, 
/*  27 */   Thirst, 
/*     */ 
/*  34 */   Injured, 
/*  35 */   Pain, 
/*  36 */   HeavyLoad, 
/*  37 */   Drunk, 
/*  38 */   Dead, 
/*  39 */   Zombie, 
/*     */ 
/*  42 */   FoodEaten, 
/*     */ 
/*  46 */   MAX;

    public static MoodleType FromIndex(int index)
    {
        switch(index)
        {
        case 0: // '\0'
            return Endurance;

        case 1: // '\001'
            return Tired;

        case 2: // '\002'
            return Hungry;

        case 3: // '\003'
            return Panic;

        case 4: // '\004'
            return Sick;

        case 5: // '\005'
            return Bored;

        case 6: // '\006'
            return Unhappy;

        case 7: // '\007'
            return Bleeding;

        case 8: // '\b'
            return Wet;

        case 9: // '\t'
            return HasACold;

        case 10: // '\n'
            return Angry;

        case 11: // '\013'
            return Injured;

        case 12: // '\f'
            return Pain;

        case 13: // '\r'
            return HeavyLoad;

        case 14: // '\016'
            return Drunk;

        case 15: // '\017'
            return Dead;

        case 16: // '\020'
            return Zombie;

        case 17: // '\021'
            return FoodEaten;
        }
        return MAX;
    }

    public static MoodleType FromString(String str)
    {
        if(str.equals("Endurance"))
            return Endurance;
        if(str.equals("Tired"))
            return Tired;
        if(str.equals("Hungry"))
            return Hungry;
        if(str.equals("Panic"))
            return Panic;
        if(str.equals("Sick"))
            return Sick;
        if(str.equals("Bored"))
            return Bored;
        if(str.equals("Unhappy"))
            return Unhappy;
        if(str.equals("Bleeding"))
            return Bleeding;
        if(str.equals("Wet"))
            return Wet;
        if(str.equals("HasACold"))
            return HasACold;
        if(str.equals("Angry"))
            return Angry;
        if(str.equals("Stress"))
            return Stress;
        if(str.equals("Thirst"))
            return Thirst;
        if(str.equals("Injured"))
            return Injured;
        if(str.equals("Pain"))
            return Pain;
        if(str.equals("HeavyLoad"))
            return HeavyLoad;
        if(str.equals("Drunk"))
            return Drunk;
        if(str.equals("Dead"))
            return Dead;
        if(str.equals("Zombie"))
            return Zombie;
        if(str.equals("FoodEaten"))
            return FoodEaten;
        else
            return MAX;
    }

    public static String getDisplayName(MoodleType MT, int Level)
    {
        if(Level > 4)
            Level = 4;
        if(Level == 0)
            return "Invalid Moodle Level";
        if(MT == Endurance)
            switch(Level)
            {
            case 1: // '\001'
                return "Moderate Exhursion";

            case 2: // '\002'
                return "High Exhursion";

            case 3: // '\003'
                return "Excessive Exhursion";

            case 4: // '\004'
                return "Out Of Breath";
            }
        if(MT == Angry)
            switch(Level)
            {
            case 1: // '\001'
                return "Irritated";

            case 2: // '\002'
                return "Annoyed";

            case 3: // '\003'
                return "Angry";

            case 4: // '\004'
                return "Furious";
            }
        if(MT == Stress)
            switch(Level)
            {
            case 1: // '\001'
                return "Anxious";

            case 2: // '\002'
                return "Agitated";

            case 3: // '\003'
                return "Stressed";

            case 4: // '\004'
                return "Nervous Wreck";
            }
        if(MT == Thirst)
            switch(Level)
            {
            case 1: // '\001'
                return "Slightly Thirsty";

            case 2: // '\002'
                return "Thirsty";

            case 3: // '\003'
                return "Parched";

            case 4: // '\004'
                return "Dying of Thirst";
            }
        if(MT == Tired)
            switch(Level)
            {
            case 1: // '\001'
                return "Drowsey";

            case 2: // '\002'
                return "Tired";

            case 3: // '\003'
                return "Very Tired";

            case 4: // '\004'
                return "Exhausted";
            }
        if(MT == Hungry)
            switch(Level)
            {
            case 1: // '\001'
                return "Peckish";

            case 2: // '\002'
                return "Hungry";

            case 3: // '\003'
                return "Very Hungry";

            case 4: // '\004'
                return "Starving";
            }
        if(MT == Panic)
            switch(Level)
            {
            case 1: // '\001'
                return "Slight Panic";

            case 2: // '\002'
                return "Panic";

            case 3: // '\003'
                return "Strong Panic";

            case 4: // '\004'
                return "Extreme Panic";
            }
        if(MT == Sick)
            switch(Level)
            {
            case 1: // '\001'
                return "Queesy";

            case 2: // '\002'
                return "Nautious";

            case 3: // '\003'
                return "Sick";

            case 4: // '\004'
                return "Fever";
            }
        if(MT == Bored)
            switch(Level)
            {
            case 1: // '\001'
                return "getting bored";

            case 2: // '\002'
                return "Bored";

            case 3: // '\003'
                return "Very bored";

            case 4: // '\004'
                return "Extremely bored";
            }
        if(MT == Unhappy)
            switch(Level)
            {
            case 1: // '\001'
                return "Becoming Unhappy";

            case 2: // '\002'
                return "Minor Unhappyness";

            case 3: // '\003'
                return "Unhappyness";

            case 4: // '\004'
                return "Severe Unhappyness";
            }
        if(MT == Bleeding)
            switch(Level)
            {
            case 1: // '\001'
                return "Minor Bleeding";

            case 2: // '\002'
                return "Bleeding";

            case 3: // '\003'
                return "Severe Bleeding";

            case 4: // '\004'
                return "Massive Blood Loss";
            }
        if(MT == Wet)
            switch(Level)
            {
            case 1: // '\001'
                return "Damp";

            case 2: // '\002'
                return "Wet";

            case 3: // '\003'
                return "Soaking";

            case 4: // '\004'
                return "Drenched";
            }
        if(MT == HasACold)
            switch(Level)
            {
            case 1: // '\001'
                return "Runny nose";

            case 2: // '\002'
                return "The sniffles";

            case 3: // '\003'
                return "Has a cold";

            case 4: // '\004'
                return "Has a nasty cold";
            }
        if(MT == Injured)
            switch(Level)
            {
            case 1: // '\001'
                return "Minor Injuries";

            case 2: // '\002'
                return "Injured";

            case 3: // '\003'
                return "Severe Injuries";

            case 4: // '\004'
                return "Critical Injuries";
            }
        if(MT == Pain)
            switch(Level)
            {
            case 1: // '\001'
                return "Minor Pain";

            case 2: // '\002'
                return "Pain";

            case 3: // '\003'
                return "Severe Pain";

            case 4: // '\004'
                return "Agony";
            }
        if(MT == HeavyLoad)
            switch(Level)
            {
            case 1: // '\001'
                return "Quite Heavy Load";

            case 2: // '\002'
                return "Heavy Load";

            case 3: // '\003'
                return "Very Heavy Load";

            case 4: // '\004'
                return "Extremely Heavy Load";
            }
        if(MT == Drunk)
            switch(Level)
            {
            case 1: // '\001'
                return "A Bit Tipsy";

            case 2: // '\002'
                return "Inebreated";

            case 3: // '\003'
                return "Plastered";

            case 4: // '\004'
                return "Utterly Shit-Faced";
            }
        if(MT == Dead)
            switch(Level)
            {
            case 1: // '\001'
                return "Deceased";

            case 2: // '\002'
                return "Deceased";

            case 3: // '\003'
                return "Deceased";

            case 4: // '\004'
                return "Deceased";
            }
        if(MT == Zombie)
            switch(Level)
            {
            case 1: // '\001'
                return "Zombified";

            case 2: // '\002'
                return "Zombified";

            case 3: // '\003'
                return "Zombified";

            case 4: // '\004'
                return "Zombified";
            }
        if(MT == FoodEaten)
            switch(Level)
            {
            case 1: // '\001'
                return "Slightly Fed";

            case 2: // '\002'
                return "Fed";

            case 3: // '\003'
                return "Well Fed";

            case 4: // '\004'
                return "Very Well Fed";
            }
        return "Unkown Moodle Type";
    }

    public static String getDescriptionText(MoodleType MT, int Level)
    {
        if(Level > 4)
            Level = 4;
        if(Level == 0)
            return "Invalid Moodle Level";
        if(MT == Endurance)
            switch(Level)
            {
            case 1: // '\001'
                return "Rest your legs";

            case 2: // '\002'
                return "Can't Run";

            case 3: // '\003'
                return "Can barely walk";

            case 4: // '\004'
                return "Can barely move";
            }
        if(MT == Angry)
            switch(Level)
            {
            case 1: // '\001'
                return "A bit narked off";

            case 2: // '\002'
                return "Can't respond positively";

            case 3: // '\003'
                return "Can only respond angrily";

            case 4: // '\004'
                return "Likely to start a fight";
            }
        if(MT == Stress)
            switch(Level)
            {
            case 1: // '\001'
                return "On edge";

            case 2: // '\002'
                return "getting stressed out";

            case 3: // '\003'
                return "Has a short fuse";

            case 4: // '\004'
                return "Could explode at slightest provocation";
            }
        if(MT == Stress)
            switch(Level)
            {
            case 1: // '\001'
                return "Dry mouth";

            case 2: // '\002'
                return "Dehydrated";

            case 3: // '\003'
                return "Feeling weak from thirst";

            case 4: // '\004'
                return "In danger of dying";
            }
        if(MT == Tired)
            switch(Level)
            {
            case 1: // '\001'
                return "Have a lie down";

            case 2: // '\002'
                return "Awareness reduced";

            case 3: // '\003'
                return "Awareness severely reduced";

            case 4: // '\004'
                return "Will pass out";
            }
        if(MT == Hungry)
            switch(Level)
            {
            case 1: // '\001'
                return "Have a snack";

            case 2: // '\002'
                return "Reduced strength & Healing";

            case 3: // '\003'
                return "Severly Reduced strength & Healing";

            case 4: // '\004'
                return "Very weak, health dropping";
            }
        if(MT == Panic)
            switch(Level)
            {
            case 1: // '\001'
                return "Try to stay calm";

            case 2: // '\002'
                return "Accuracy reduced";

            case 3: // '\003'
                return "Accuracy severly reduced";

            case 4: // '\004'
                return "Accuracy & vision severly reduced";
            }
        if(MT == Sick)
            switch(Level)
            {
            case 1: // '\001'
                return "Take things easy";

            case 2: // '\002'
                return "Strength & healing reduced";

            case 3: // '\003'
                return "Strength & healing severly reduced";

            case 4: // '\004'
                return "Strength severly reduced, slowly dying";
            }
        if(MT == Bored)
            switch(Level)
            {
            case 1: // '\001'
                return "Seek entertainment (Read a book, have a conversation, etc...)";

            case 2: // '\002'
                return "In danger of becoming unhappy";

            case 3: // '\003'
                return "High chance of becoming unhappy";

            case 4: // '\004'
                return "Very high chance of becoming unhappy";
            }
        if(MT == Unhappy)
            switch(Level)
            {
            case 1: // '\001'
                return "Seek some exitement or human contact";

            case 2: // '\002'
                return "Seek some exitement or human contact";

            case 3: // '\003'
                return "Seek some exitement or human contact";

            case 4: // '\004'
                return "Seek some exitement or human contact";
            }
        if(MT == Bleeding)
            switch(Level)
            {
            case 1: // '\001'
                return "Bandage required";

            case 2: // '\002'
                return "Strength & speed reduced";

            case 3: // '\003'
                return "Strength & speed severly reduced";

            case 4: // '\004'
                return "Strength & speed severly reduced. Dying.";
            }
        if(MT == Wet)
            switch(Level)
            {
            case 1: // '\001'
                return "get out of the rain";

            case 2: // '\002'
                return "Speed slightly reduced";

            case 3: // '\003'
                return "Speed reduced & chance of catching a cold";

            case 4: // '\004'
                return "Speed severly reduce & high chance of catching a cold";
            }
        if(MT == HasACold)
            switch(Level)
            {
            case 1: // '\001'
                return "Slight chance of sneezing";

            case 2: // '\002'
                return "Prone to sneezing";

            case 3: // '\003'
                return "Sneezing & Coughing. Speed & healing reduced.";

            case 4: // '\004'
                return "Sneezing & Coughing. Speed & healing severely reduced.";
            }
        if(MT == Injured)
            switch(Level)
            {
            case 1: // '\001'
                return "First aid required";

            case 2: // '\002'
                return "Strength & speed reduced";

            case 3: // '\003'
                return "Strength & speed severly reduced";

            case 4: // '\004'
                return "Strength & speed severly reduced. Dying.";
            }
        if(MT == Pain)
            switch(Level)
            {
            case 1: // '\001'
                return "Feeling slight pain";

            case 2: // '\002'
                return "Speed & Accuracy slighty reduced";

            case 3: // '\003'
                return "Speed & Accuracy reduced";

            case 4: // '\004'
                return "Speed & Accuracy severly reduced";
            }
        if(MT == HeavyLoad)
            switch(Level)
            {
            case 1: // '\001'
                return "Carrying a little too much";

            case 2: // '\002'
                return "Movement Speed Reduced";

            case 3: // '\003'
                return "Movement Speed Highly Reduced";

            case 4: // '\004'
                return "Movement Speed Highly Reduced, Causing Back Indjury";
            }
        if(MT == Drunk)
            switch(Level)
            {
            case 1: // '\001'
                return "Had Some Booze";

            case 2: // '\002'
                return "Coordination slightly impared";

            case 3: // '\003'
                return "Coordination impared";

            case 4: // '\004'
                return "Coordination Severely impared";
            }
        if(MT == Dead)
            switch(Level)
            {
            case 1: // '\001'
                return "High risk of becoming rat food.";

            case 2: // '\002'
                return "High risk of becoming rat food.";

            case 3: // '\003'
                return "High risk of becoming rat food.";

            case 4: // '\004'
                return "High risk of becoming rat food.";
            }
        if(MT == Zombie)
            switch(Level)
            {
            case 1: // '\001'
                return "Permanent hunger for BRAINS!";

            case 2: // '\002'
                return "Permanent hunger for BRAINS!";

            case 3: // '\003'
                return "Permanent hunger for BRAINS!";

            case 4: // '\004'
                return "Permanent hunger for BRAINS!";
            }
        if(MT == FoodEaten)
            switch(Level)
            {
            case 1: // '\001'
                return "Short healing & strength boost";

            case 2: // '\002'
                return "Healing & strength boost";

            case 3: // '\003'
                return "Long healing & strength boost";

            case 4: // '\004'
                return "Very Long healing & strength boost";
            }
        return "Unkown Moodle Type";
    }

    public static int GoodBadNeutral(MoodleType MT)
    {
        if(MT == Endurance)
            return 2;
        if(MT == Tired)
            return 2;
        if(MT == Hungry)
            return 2;
        if(MT == Panic)
            return 2;
        if(MT == Sick)
            return 2;
        if(MT == Bored)
            return 2;
        if(MT == Unhappy)
            return 2;
        if(MT == Bleeding)
            return 2;
        if(MT == Wet)
            return 2;
        if(MT == HasACold)
            return 2;
        if(MT == Angry)
            return 2;
        if(MT == Stress)
            return 2;
        if(MT == Thirst)
            return 2;
        if(MT == Injured)
            return 2;
        if(MT == Pain)
            return 2;
        if(MT == HeavyLoad)
            return 2;
        if(MT == Drunk)
            return 2;
        if(MT == Dead)
            return 2;
        if(MT == Zombie)
            return 2;
        return MT != FoodEaten ? 2 : 1;
    }

    public static int ToIndex(MoodleType MT)
    {
        if(MT == null)
            return 0;
       

        switch(MT.ordinal())
        {
        case 1: // '\001'
            return 0;

        case 2: // '\002'
            return 1;

        case 3: // '\003'
            return 2;

        case 4: // '\004'
            return 3;

        case 5: // '\005'
            return 4;

        case 6: // '\006'
            return 5;

        case 7: // '\007'
            return 6;

        case 8: // '\b'
            return 7;

        case 9: // '\t'
            return 8;

        case 10: // '\n'
            return 9;

        case 11: // '\013'
            return 10;

        case 12: // '\f'
            return 11;

        case 13: // '\r'
            return 12;

        case 14: // '\016'
            return 13;

        case 15: // '\017'
            return 14;

        case 16: // '\020'
            return 15;

        case 17: // '\021'
            return 16;

        case 18: // '\022'
            return 17;

        case 19: // '\023'
            return 18;

        case 20: // '\024'
            return 19;

        case 21: // '\025'
            return 20;
        }
        return 0;
    }

 
}
