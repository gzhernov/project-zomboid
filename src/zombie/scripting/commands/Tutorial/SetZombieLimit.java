// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SetZombieLimit.java

package zombie.scripting.commands.Tutorial;

import javax.swing.JOptionPane;
import zombie.scripting.commands.BaseCommand;
import zombie.ui.TutorialManager;

public class SetZombieLimit extends BaseCommand
{

    public SetZombieLimit()
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
            limit = Integer.parseInt(params[0].trim().replace("\"", ""));
            return;
        }
    }

    public void begin()
    {
        TutorialManager.instance.TargetZombies = limit;
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
