// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Moodles.java

package zombie.characters.Moodles;

import java.util.Stack;
import zombie.characters.IsoGameCharacter;

// Referenced classes of package zombie.characters.Moodles:
//            Moodle, MoodleType

public class Moodles
{

    public Moodles(IsoGameCharacter parent)
    {
        MoodlesStateChanged = false;
        MoodleList = new Stack();
        Parent = parent;
        MoodleList.add(new Moodle(MoodleType.Endurance, Parent));
        MoodleList.add(new Moodle(MoodleType.Tired, Parent));
        MoodleList.add(new Moodle(MoodleType.Hungry, Parent));
        MoodleList.add(new Moodle(MoodleType.Panic, Parent));
        MoodleList.add(new Moodle(MoodleType.Sick, Parent));
        MoodleList.add(new Moodle(MoodleType.Bored, Parent));
        MoodleList.add(new Moodle(MoodleType.Unhappy, Parent));
        MoodleList.add(new Moodle(MoodleType.Bleeding, Parent));
        MoodleList.add(new Moodle(MoodleType.Wet, Parent));
        MoodleList.add(new Moodle(MoodleType.HasACold, Parent));
        MoodleList.add(new Moodle(MoodleType.Angry, Parent));
        MoodleList.add(new Moodle(MoodleType.Stress, Parent));
        MoodleList.add(new Moodle(MoodleType.Thirst, Parent));
        MoodleList.add(new Moodle(MoodleType.Injured, Parent));
        MoodleList.add(new Moodle(MoodleType.Pain, Parent));
        MoodleList.add(new Moodle(MoodleType.HeavyLoad, Parent));
        MoodleList.add(new Moodle(MoodleType.Drunk, Parent));
        MoodleList.add(new Moodle(MoodleType.Dead, Parent));
        MoodleList.add(new Moodle(MoodleType.Zombie, Parent));
        MoodleList.add(new Moodle(MoodleType.FoodEaten, Parent));
    }

    public int getGoodBadNeutral(int MoodleIndex)
    {
        return MoodleType.GoodBadNeutral(((Moodle)MoodleList.get(MoodleIndex)).Type);
    }

    public String getMoodleDisplayString(int MoodleIndex)
    {
        return MoodleType.getDisplayName(((Moodle)MoodleList.get(MoodleIndex)).Type, ((Moodle)MoodleList.get(MoodleIndex)).getLevel());
    }

    public String getMoodleDescriptionString(int MoodleIndex)
    {
        return MoodleType.getDescriptionText(((Moodle)MoodleList.get(MoodleIndex)).Type, ((Moodle)MoodleList.get(MoodleIndex)).getLevel());
    }

    public int getMoodleLevel(int MoodleIndex)
    {
        return ((Moodle)MoodleList.get(MoodleIndex)).getLevel();
    }

    public int getMoodleLevel(MoodleType MType)
    {
        return ((Moodle)MoodleList.get(MoodleType.ToIndex(MType))).getLevel();
    }

    public MoodleType getMoodleType(int MoodleIndex)
    {
        return ((Moodle)MoodleList.get(MoodleIndex)).Type;
    }

    public int getNumMoodles()
    {
        return MoodleList.size();
    }

    public void Randomise()
    {
    }

    public boolean UI_RefreshNeeded()
    {
        if(MoodlesStateChanged)
        {
            MoodlesStateChanged = false;
            return true;
        } else
        {
            return false;
        }
    }

    public void Update()
    {
        boolean DoBingSound = false;
        for(int i = 0; i < MoodleList.size(); i++)
            if(((Moodle)MoodleList.get(i)).Update())
            {
                MoodlesStateChanged = true;
                DoBingSound = true;
            }

    }

    boolean MoodlesStateChanged;
    private Stack MoodleList;
    private final IsoGameCharacter Parent;
}
