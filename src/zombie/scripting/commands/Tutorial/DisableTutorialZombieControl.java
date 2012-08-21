// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DisableTutorialZombieControl.java

package zombie.scripting.commands.Tutorial;

import javax.swing.JOptionPane;
import zombie.scripting.commands.BaseCommand;
import zombie.ui.TutorialManager;

public class DisableTutorialZombieControl extends BaseCommand
{

    public DisableTutorialZombieControl()
    {
    }

    public void init(String object, String params[])
    {
        if(object == null || !object.equals("Tutorial"))
        {
            JOptionPane.showMessageDialog(null, (new StringBuilder()).append("Command: ").append(getClass().getName()).append(" is not part of ").append(object).toString(), "Error", 0);
            return;
        } else
        {
            return;
        }
    }

    public void begin()
    {
        TutorialManager.instance.ActiveControlZombies = false;
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

    int limit;
}
