// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsNight.java

package zombie.scripting.commands.DayNight;

import zombie.GameTime;
import zombie.scripting.commands.BaseCommand;

public class IsNight extends BaseCommand
{

    public IsNight()
    {
        invert = false;
    }

    public void begin()
    {
    }

    public boolean getValue()
    {
        boolean bNight = false;
        if(GameTime.getInstance().getTimeOfDay() > 20F || GameTime.getInstance().getTimeOfDay() < 6F)
            bNight = true;
        if(invert)
            bNight = !bNight;
        return bNight;
    }

    public boolean IsFinished()
    {
        return true;
    }

    public void update()
    {
    }

    public void init(String object, String params[])
    {
        if(object != null && object.equals("!"))
            invert = true;
    }

    public boolean DoesInstantly()
    {
        return true;
    }

    boolean invert;
    String character;
}
