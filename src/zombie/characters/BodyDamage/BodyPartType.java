// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BodyPartType.java

package zombie.characters.BodyDamage;


 public enum BodyPartType
/*     */ {
/*  14 */   Hand_L, 
/*  15 */   Hand_R, 
/*  16 */   ForeArm_L, 
/*  17 */   ForeArm_R, 
/*  18 */   UpperArm_L, 
/*  19 */   UpperArm_R, 
/*  20 */   Torso_Upper, 
/*  21 */   Torso_Lower, 
/*  22 */   Head, 
/*  23 */   Neck, 
/*  24 */   Groin, 
/*  25 */   UpperLeg_L, 
/*  26 */   UpperLeg_R, 
/*  27 */   LowerLeg_L, 
/*  28 */   LowerLeg_R, 
/*  29 */   Foot_L, 
/*  30 */   Foot_R, 
/*     */ 
/*  32 */   MAX;
    public static BodyPartType FromIndex(int index)
    {
        switch(index)
        {
        case 0: // '\0'
            return Hand_L;

        case 1: // '\001'
            return Hand_R;

        case 2: // '\002'
            return ForeArm_L;

        case 3: // '\003'
            return ForeArm_R;

        case 4: // '\004'
            return UpperArm_L;

        case 5: // '\005'
            return UpperArm_R;

        case 6: // '\006'
            return Torso_Upper;

        case 7: // '\007'
            return Torso_Lower;

        case 8: // '\b'
            return Head;

        case 9: // '\t'
            return Neck;

        case 10: // '\n'
            return Groin;

        case 11: // '\013'
            return UpperLeg_L;

        case 12: // '\f'
            return UpperLeg_R;

        case 13: // '\r'
            return LowerLeg_L;

        case 14: // '\016'
            return LowerLeg_R;

        case 15: // '\017'
            return Foot_L;

        case 16: // '\020'
            return Foot_R;
        }
        return MAX;
    }

    public static BodyPartType FromString(String str)
    {
        if(str.equals("Hand_L"))
            return Hand_L;
        if(str.equals("Hand_R"))
            return Hand_R;
        if(str.equals("ForeArm_L"))
            return ForeArm_L;
        if(str.equals("ForeArm_R"))
            return ForeArm_R;
        if(str.equals("UpperArm_L"))
            return UpperArm_L;
        if(str.equals("UpperArm_R"))
            return UpperArm_R;
        if(str.equals("Torso_Upper"))
            return Torso_Upper;
        if(str.equals("Torso_Lower"))
            return Torso_Lower;
        if(str.equals("Head"))
            return Head;
        if(str.equals("Neck"))
            return Neck;
        if(str.equals("Groin"))
            return Groin;
        if(str.equals("UpperLeg_L"))
            return UpperLeg_L;
        if(str.equals("UpperLeg_R"))
            return UpperLeg_R;
        if(str.equals("LowerLeg_L"))
            return LowerLeg_L;
        if(str.equals("LowerLeg_R"))
            return LowerLeg_R;
        if(str.equals("Foot_L"))
            return Foot_L;
        if(str.equals("Foot_R"))
            return Foot_R;
        else
            return MAX;
    }

    public static float getDamageModifyer(int index)
    {
        switch(index)
        {
        case 0: // '\0'
            return 0.07F;

        case 1: // '\001'
            return 0.07F;

        case 2: // '\002'
            return 0.3F;

        case 3: // '\003'
            return 0.3F;

        case 4: // '\004'
            return 0.5F;

        case 5: // '\005'
            return 0.5F;

        case 6: // '\006'
            return 0.6F;

        case 7: // '\007'
            return 0.78F;

        case 8: // '\b'
            return 0.6F;

        case 9: // '\t'
            return 0.8F;

        case 10: // '\n'
            return 0.7F;

        case 11: // '\013'
            return 0.56F;

        case 12: // '\f'
            return 0.56F;

        case 13: // '\r'
            return 0.5F;

        case 14: // '\016'
            return 0.5F;

        case 15: // '\017'
            return 0.025F;

        case 16: // '\020'
            return 0.025F;
        }
        return 1.0F;
    }

    public static String getDisplayName(BodyPartType BPT)
    {
        if(BPT == Hand_L)
            return "Left Hand";
        if(BPT == Hand_R)
            return "Right Hand";
        if(BPT == ForeArm_L)
            return "Left Forearm";
        if(BPT == ForeArm_R)
            return "Right Forearm";
        if(BPT == UpperArm_L)
            return "Left Upper Arm";
        if(BPT == UpperArm_R)
            return "Right Upper Arm";
        if(BPT == Torso_Upper)
            return "Upper Torso";
        if(BPT == Torso_Lower)
            return "Lower Torso";
        if(BPT == Head)
            return "Head";
        if(BPT == Neck)
            return "Neck";
        if(BPT == Groin)
            return "Groin";
        if(BPT == UpperLeg_L)
            return "Left Thigh";
        if(BPT == UpperLeg_R)
            return "Right Thigh";
        if(BPT == LowerLeg_L)
            return "Left Shin";
        if(BPT == LowerLeg_R)
            return "Right Shin";
        if(BPT == Foot_L)
            return "Left Foot";
        if(BPT == Foot_R)
            return "Right Foot";
        else
            return "Unkown Body Part";
    }

    public static int ToIndex(BodyPartType BPT)
    {
        if(BPT == null)
            return 0;
        

        switch(BPT.ordinal())
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
        }
        return 17;
    }

    public static String ToString(BodyPartType BPT)
    {
        if(BPT == Hand_L)
            return "Hand_L";
        if(BPT == Hand_R)
            return "Hand_R";
        if(BPT == ForeArm_L)
            return "ForeArm_L";
        if(BPT == ForeArm_R)
            return "ForeArm_R";
        if(BPT == UpperArm_L)
            return "UpperArm_L";
        if(BPT == UpperArm_R)
            return "UpperArm_R";
        if(BPT == Torso_Upper)
            return "Torso_Upper";
        if(BPT == Torso_Lower)
            return "Torso_Lower";
        if(BPT == Head)
            return "Head";
        if(BPT == Neck)
            return "Neck";
        if(BPT == Groin)
            return "Groin";
        if(BPT == UpperLeg_L)
            return "UpperLeg_L";
        if(BPT == UpperLeg_R)
            return "UpperLeg_R";
        if(BPT == LowerLeg_L)
            return "LowerLeg_L";
        if(BPT == LowerLeg_R)
            return "LowerLeg_R";
        if(BPT == Foot_L)
            return "Foot_L";
        if(BPT == Foot_R)
            return "Foot_R";
        else
            return "Unkown Body Part";
    }



   
}
