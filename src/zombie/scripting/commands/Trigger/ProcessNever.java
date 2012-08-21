// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ProcessNever.java

package zombie.scripting.commands.Trigger;

import gnu.trove.map.hash.THashMap;
import java.util.List;
import javax.swing.JOptionPane;
import zombie.scripting.ScriptManager;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.Trigger;

public class ProcessNever extends BaseCommand
{

    public ProcessNever()
    {
    }

    public void init(String object, String params[])
    {
        if(object == null)
        {
            JOptionPane.showMessageDialog(null, (new StringBuilder()).append("Command: ").append(getClass().getName()).append(" is not part of ").append(object).toString(), "Error", 0);
            return;
        } else
        {
            position = object.toLowerCase();
            return;
        }
    }

    public void begin()
    {
        List trigList = (List)ScriptManager.instance.CustomTriggerMap.get(position);
        for(int n = 0; n < trigList.size(); n++)
            ((Trigger)trigList.get(n)).Locked = true;

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
}
