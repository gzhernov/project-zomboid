// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Moodle.java

package zombie.characters.Moodles;

import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.IsoGameCharacter;
import zombie.inventory.ItemContainer;

// Referenced classes of package zombie.characters.Moodles:
//            MoodleType

public class Moodle
{

    public Moodle(MoodleType ChosenType, IsoGameCharacter parent)
    {
        Parent = parent;
        Type = ChosenType;
        Level = 0;
    }

    public int getLevel()
    {
        return Level;
    }

    public void SetLevel(int val)
    {
        if(val < 0)
            val = 0;
        if(val > 4)
            val = 4;
        Level = val;
    }

    public boolean Update()
    {
        boolean MoodleStatusChanged = false;
        if(Type == MoodleType.Endurance)
        {
            int LevelToSet = 0;
            if(Parent.getBodyDamage().getHealth() != 0.0F)
            {
                if(Parent.getStats().endurance < Parent.getStats().endurancewarn)
                    LevelToSet = 1;
                if(Parent.getStats().endurance < Parent.getStats().endurancewarn - (Parent.getStats().endurancewarn - Parent.getStats().endurancedanger) / 2.0F)
                    LevelToSet = 2;
                if(Parent.getStats().endurance < Parent.getStats().endurancedanger)
                    LevelToSet = 3;
                if(Parent.getStats().endurance < 0.1F)
                    LevelToSet = 4;
            }
            if(LevelToSet != getLevel())
            {
                SetLevel(LevelToSet);
                MoodleStatusChanged = true;
            }
        }
        if(Type == MoodleType.Angry)
        {
            int LevelToSet = 0;
            if(Parent.getStats().Anger > 0.75F)
                LevelToSet = 4;
            else
            if(Parent.getStats().Anger > 0.5F)
                LevelToSet = 3;
            else
            if(Parent.getStats().Anger > 0.25F)
                LevelToSet = 2;
            else
            if(Parent.getStats().Anger > 0.1F)
                LevelToSet = 1;
            if(LevelToSet != getLevel())
            {
                SetLevel(LevelToSet);
                MoodleStatusChanged = true;
            }
        }
        if(Type == MoodleType.Tired)
        {
            int LevelToSet = 0;
            if(Parent.getBodyDamage().getHealth() != 0.0F)
            {
                if(Parent.getStats().fatigue > 0.6F)
                    LevelToSet = 1;
                if(Parent.getStats().fatigue > 0.7F)
                    LevelToSet = 2;
                if(Parent.getStats().fatigue > 0.8F)
                    LevelToSet = 3;
                if(Parent.getStats().fatigue > 0.9F)
                    LevelToSet = 4;
            }
            if(LevelToSet != getLevel())
            {
                SetLevel(LevelToSet);
                MoodleStatusChanged = true;
            }
        }
        if(Type == MoodleType.Hungry)
        {
            int LevelToSet = 0;
            if(Parent.getBodyDamage().getHealth() != 0.0F)
            {
                if(Parent.getStats().hunger > 0.25F)
                    LevelToSet = 1;
                if(Parent.getStats().hunger > 0.4F)
                    LevelToSet = 2;
                if(Parent.getStats().hunger > 0.7F)
                    LevelToSet = 3;
                if(Parent.getStats().hunger > 0.87F)
                    LevelToSet = 4;
            }
            if(LevelToSet != getLevel())
            {
                SetLevel(LevelToSet);
                MoodleStatusChanged = true;
            }
        }
        if(Type == MoodleType.Panic)
        {
            int LevelToSet = 0;
            if(Parent.getBodyDamage().getHealth() != 0.0F)
            {
                if(Parent.getStats().Panic > 6F)
                    LevelToSet = 1;
                if(Parent.getStats().Panic > 30F)
                    LevelToSet = 2;
                if(Parent.getStats().Panic > 65F)
                    LevelToSet = 3;
                if(Parent.getStats().Panic > 80F)
                    LevelToSet = 4;
            }
            if(LevelToSet != getLevel())
            {
                SetLevel(LevelToSet);
                MoodleStatusChanged = true;
            }
        }
        if(Type == MoodleType.Sick)
        {
            int LevelToSet = 0;
            if(Parent.getBodyDamage().getHealth() != 0.0F)
            {
                Parent.getStats().Sickness = Parent.getBodyDamage().getInfectionLevel() / 100F;
                if(Parent.getStats().Sickness > 0.25F)
                    LevelToSet = 1;
                if(Parent.getStats().Sickness > 0.5F)
                    LevelToSet = 2;
                if(Parent.getStats().Sickness > 0.75F)
                    LevelToSet = 3;
                if(Parent.getStats().Sickness > 0.9F)
                    LevelToSet = 4;
            }
            if(LevelToSet != getLevel())
            {
                SetLevel(LevelToSet);
                MoodleStatusChanged = true;
            }
        }
        if(Type == MoodleType.Bored)
        {
            int LevelToSet = 0;
            if(Parent.getBodyDamage().getHealth() != 0.0F)
            {
                Parent.getStats().Boredom = Parent.getBodyDamage().getBoredomLevel() / 100F;
                if(Parent.getStats().Boredom > 0.25F)
                    LevelToSet = 1;
                if(Parent.getStats().Boredom > 0.5F)
                    LevelToSet = 2;
                if(Parent.getStats().Boredom > 0.75F)
                    LevelToSet = 3;
                if(Parent.getStats().Boredom > 0.9F)
                    LevelToSet = 4;
            }
            if(LevelToSet != getLevel())
            {
                SetLevel(LevelToSet);
                MoodleStatusChanged = true;
            }
        }
        if(Type == MoodleType.Unhappy)
        {
            int LevelToSet = 0;
            if(Parent.getBodyDamage().getHealth() != 0.0F)
            {
                if(Parent.getBodyDamage().getUnhappynessLevel() > 20F)
                    LevelToSet = 1;
                if(Parent.getBodyDamage().getUnhappynessLevel() > 45F)
                    LevelToSet = 2;
                if(Parent.getBodyDamage().getUnhappynessLevel() > 60F)
                    LevelToSet = 3;
                if(Parent.getBodyDamage().getUnhappynessLevel() > 80F)
                    LevelToSet = 4;
            }
            if(LevelToSet != getLevel())
            {
                SetLevel(LevelToSet);
                MoodleStatusChanged = true;
            }
        }
        if(Type == MoodleType.Stress)
        {
            int LevelToSet = 0;
            if(Parent.getStats().stress > 0.9F)
                LevelToSet = 4;
            else
            if(Parent.getStats().stress > 0.75F)
                LevelToSet = 3;
            else
            if(Parent.getStats().stress > 0.5F)
                LevelToSet = 2;
            else
            if(Parent.getStats().stress > 0.25F)
                LevelToSet = 1;
            if(LevelToSet != getLevel())
            {
                SetLevel(LevelToSet);
                MoodleStatusChanged = true;
            }
        }
        if(Type == MoodleType.Thirst)
        {
            int LevelToSet = 0;
            if(Parent.getStats().getThirst() > 0.9F)
                LevelToSet = 4;
            else
            if(Parent.getStats().getThirst() > 0.75F)
                LevelToSet = 3;
            else
            if(Parent.getStats().getThirst() > 0.5F)
                LevelToSet = 2;
            else
            if(Parent.getStats().getThirst() > 0.25F)
                LevelToSet = 1;
            if(LevelToSet != getLevel())
            {
                SetLevel(LevelToSet);
                MoodleStatusChanged = true;
            }
        }
        if(Type == MoodleType.Bleeding)
        {
            int LevelToSet = 0;
            if(Parent.getBodyDamage().getHealth() != 0.0F)
            {
                LevelToSet = Parent.getBodyDamage().getNumPartsBleeding();
                if(LevelToSet > 4)
                    LevelToSet = 4;
            }
            if(LevelToSet != getLevel())
            {
                SetLevel(LevelToSet);
                MoodleStatusChanged = true;
            }
        }
        if(Type == MoodleType.Wet)
        {
            int LevelToSet = 0;
            if(Parent.getBodyDamage().getHealth() != 0.0F)
            {
                if(Parent.getBodyDamage().getWetness() > 10F)
                    LevelToSet = 1;
                if(Parent.getBodyDamage().getWetness() > 30F)
                    LevelToSet = 2;
                if(Parent.getBodyDamage().getWetness() > 60F)
                    LevelToSet = 3;
                if(Parent.getBodyDamage().getWetness() > 75F)
                    LevelToSet = 4;
            }
            if(LevelToSet != getLevel())
            {
                SetLevel(LevelToSet);
                MoodleStatusChanged = true;
            }
        }
        if(Type == MoodleType.HasACold)
        {
            int LevelToSet = 0;
            if(Parent.getBodyDamage().getHealth() != 0.0F)
            {
                if(Parent.getBodyDamage().getColdStrength() > 20F)
                    LevelToSet = 1;
                if(Parent.getBodyDamage().getColdStrength() > 40F)
                    LevelToSet = 2;
                if(Parent.getBodyDamage().getColdStrength() > 60F)
                    LevelToSet = 3;
                if(Parent.getBodyDamage().getColdStrength() > 75F)
                    LevelToSet = 4;
            }
            if(LevelToSet != getLevel())
            {
                SetLevel(LevelToSet);
                MoodleStatusChanged = true;
            }
        }
        if(Type == MoodleType.Injured)
        {
            int LevelToSet = 0;
            if(Parent.getBodyDamage().getHealth() != 0.0F)
            {
                if(100F - Parent.getBodyDamage().getHealth() > 20F)
                    LevelToSet = 1;
                if(100F - Parent.getBodyDamage().getHealth() > 40F)
                    LevelToSet = 2;
                if(100F - Parent.getBodyDamage().getHealth() > 60F)
                    LevelToSet = 3;
                if(100F - Parent.getBodyDamage().getHealth() > 75F)
                    LevelToSet = 4;
            }
            if(LevelToSet != getLevel())
            {
                SetLevel(LevelToSet);
                MoodleStatusChanged = true;
            }
        }
        if(Type == MoodleType.Pain)
        {
            int LevelToSet = 0;
            if(Parent.getBodyDamage().getHealth() != 0.0F)
            {
                if(Parent.getStats().Pain > 10F)
                    LevelToSet = 1;
                if(Parent.getStats().Pain > 20F)
                    LevelToSet = 2;
                if(Parent.getStats().Pain > 50F)
                    LevelToSet = 3;
                if(Parent.getStats().Pain > 75F)
                    LevelToSet = 4;
            }
            if(LevelToSet != getLevel())
            {
                SetLevel(LevelToSet);
                MoodleStatusChanged = true;
            }
        }
        if(Type == MoodleType.HeavyLoad)
        {
            int LevelToSet = 0;
            float weight = Parent.getInventory().getWeight();
            float maxWeight = (float)Parent.getMaxWeight().intValue() * Parent.getWeightMod();
            float Ratio = weight / maxWeight;
            if(Parent.getBodyDamage().getHealth() != 0.0F)
            {
                if(Ratio > 0.5F)
                    LevelToSet = 1;
                if(Ratio > 0.8F)
                    LevelToSet = 2;
                if(Ratio > 0.95F)
                    LevelToSet = 3;
                if(Ratio > 1.0F)
                    LevelToSet = 4;
            }
            if(LevelToSet != getLevel())
            {
                SetLevel(LevelToSet);
                MoodleStatusChanged = true;
            }
        }
        if(Type == MoodleType.Drunk)
        {
            int LevelToSet = 0;
            if(Parent.getBodyDamage().getHealth() != 0.0F)
            {
                if(Parent.getStats().Drunkenness > 10F)
                    LevelToSet = 1;
                if(Parent.getStats().Drunkenness > 30F)
                    LevelToSet = 2;
                if(Parent.getStats().Drunkenness > 50F)
                    LevelToSet = 3;
                if(Parent.getStats().Drunkenness > 70F)
                    LevelToSet = 4;
            }
            if(LevelToSet != getLevel())
            {
                SetLevel(LevelToSet);
                MoodleStatusChanged = true;
            }
        }
        if(Type == MoodleType.Dead)
        {
            int LevelToSet = 0;
            if(Parent.getBodyDamage().getHealth() == 0.0F)
            {
                LevelToSet = 4;
                if(!Parent.getBodyDamage().IsFakeInfected() && Parent.getBodyDamage().getInfectionLevel() >= 35F)
                    LevelToSet = 0;
            }
            if(LevelToSet != getLevel())
            {
                SetLevel(LevelToSet);
                MoodleStatusChanged = true;
            }
        }
        if(Type == MoodleType.Zombie)
        {
            int LevelToSet = 0;
            if(Parent.getBodyDamage().getHealth() == 0.0F && !Parent.getBodyDamage().IsFakeInfected() && Parent.getBodyDamage().getInfectionLevel() >= 35F)
                LevelToSet = 4;
            if(LevelToSet != getLevel())
            {
                SetLevel(LevelToSet);
                MoodleStatusChanged = true;
            }
        }
        if(Type == MoodleType.FoodEaten)
        {
            int LevelToSet = 0;
            if(Parent.getBodyDamage().getHealth() != 0.0F)
            {
                if((float)Parent.getBodyDamage().getHealthFromFoodTimer() > 0.0F)
                    LevelToSet = 1;
                if(Parent.getBodyDamage().getHealthFromFoodTimer() > Parent.getBodyDamage().getStandardHealthFromFoodTime())
                    LevelToSet = 2;
                if((float)Parent.getBodyDamage().getHealthFromFoodTimer() > (float)Parent.getBodyDamage().getStandardHealthFromFoodTime() * 2.0F)
                    LevelToSet = 3;
                if((float)Parent.getBodyDamage().getHealthFromFoodTimer() > (float)Parent.getBodyDamage().getStandardHealthFromFoodTime() * 3F)
                    LevelToSet = 4;
            }
            if(LevelToSet != getLevel())
            {
                SetLevel(LevelToSet);
                MoodleStatusChanged = true;
            }
        }
        return MoodleStatusChanged;
    }

    MoodleType Type;
    private int Level;
    IsoGameCharacter Parent;
}
