// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LockHud.java

package zombie.scripting.commands;

import zombie.scripting.ScriptManager;
import zombie.ui.TutorialManager;

// Referenced classes of package zombie.scripting.commands:
//            BaseCommand

public class LockHud extends BaseCommand
{

    public LockHud()
    {
        doIt = false;
    }

    public void init(String object, String params[])
    {
        if(params.length == 1)
            doIt = params[0].trim().equalsIgnoreCase("true");
    }

    public void begin()
    {
        TutorialManager.instance.StealControl = doIt;
        if(!doIt)
            ScriptManager.instance.skipping = false;
    }

    public boolean IsFinished()
    {
        return true;
    }

    public void update()
    {
    }

    public boolean DoesInstantly()
    {
        return true;
    }

    boolean doIt;
}
