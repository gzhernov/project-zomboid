// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CallAndWait.java

package zombie.scripting.commands.Script;

import gnu.trove.map.hash.THashMap;
import javax.swing.JOptionPane;
import zombie.scripting.ScriptManager;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.*;

public class CallAndWait extends BaseCommand
{

    public CallAndWait()
    {
        inst = null;
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
            return;
        }
    }

    public void begin()
    {
        if(module.RandomSelectorMap.containsKey(position))
        {
            inst = ((RandomSelector)module.RandomSelectorMap.get(position)).Process(currentinstance);
        } else
        {
            inst = module.PlayScript(position, currentinstance);
            check = position;
        }
    }

    public boolean IsFinished()
    {
        return !ScriptManager.instance.IsScriptPlaying(inst);
    }

    public void update()
    {
    }

    public boolean DoesInstantly()
    {
        return false;
    }

    String position;
    String check;
    zombie.scripting.objects.Script.ScriptInstance inst;
}
