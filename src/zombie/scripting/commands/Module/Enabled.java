// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Enabled.java

package zombie.scripting.commands.Module;

import javax.swing.JOptionPane;
import zombie.scripting.ScriptManager;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptModule;

public class Enabled extends BaseCommand
{

    public Enabled()
    {
        b = false;
    }

    public void init(String object, String params[])
    {
        if(object == null)
        {
            JOptionPane.showMessageDialog(null, (new StringBuilder()).append("Command: ").append(getClass().getName()).append(" is not part of ").append(object).toString(), "Error", 0);
            return;
        } else
        {
            position = object;
            b = params[0].trim().equals("true");
            return;
        }
    }

    public void begin()
    {
        ScriptModule module = ScriptManager.instance.getModuleNoDisableCheck(position);
        if(module != null)
            module.disabled = !b;
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

    String position;
    boolean b;
}
