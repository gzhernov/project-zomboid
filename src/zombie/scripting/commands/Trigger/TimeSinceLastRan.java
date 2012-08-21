// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TimeSinceLastRan.java

package zombie.scripting.commands.Trigger;

import gnu.trove.map.hash.THashMap;
import zombie.scripting.ScriptManager;
import zombie.scripting.commands.BaseCommand;

public class TimeSinceLastRan extends BaseCommand
{

    public TimeSinceLastRan()
    {
        invert = false;
        frames = 0;
    }

    public void begin()
    {
    }

    public boolean getValue()
    {
        boolean bOver = ((Integer)ScriptManager.instance.CustomTriggerLastRan.get(triggerInst)).intValue() > frames;
        if(invert)
            return !bOver;
        else
            return bOver;
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
        if(object != null)
        {
            triggerInst = object.toLowerCase();
            if(triggerInst.indexOf("!") == 0)
            {
                invert = true;
                triggerInst = triggerInst.substring(1);
            }
        }
        frames = (int)(30F * Float.parseFloat(params[0].trim()));
    }

    public boolean DoesInstantly()
    {
        return true;
    }

    boolean invert;
    public String triggerInst;
    int frames;
}
