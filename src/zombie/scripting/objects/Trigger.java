// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Trigger.java

package zombie.scripting.objects;

import gnu.trove.map.hash.THashMap;
import java.util.Stack;
import zombie.core.Collections.NulledArrayList;
import zombie.scripting.ScriptManager;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.commands.Trigger.TimeSinceLastRan;

// Referenced classes of package zombie.scripting.objects:
//            Script, ScriptModule

public class Trigger extends Script
{

    public Trigger()
    {
        Conditions = new Stack();
        Locked = false;
        scriptsToCall = new NulledArrayList();
        TriggerParam = null;
        TriggerParam2 = null;
        TriggerParam3 = null;
    }

    public void Load(String name, String strArray[])
    {
        this.name = (new StringBuilder()).append(name).append(tot).toString();
        Integer integer = tot;
        Integer integer1 = tot = Integer.valueOf(tot.intValue() + 1);
        Integer _tmp = integer;
        for(int n = 0; n < strArray.length; n++)
            DoLine(strArray[n].trim());

    }

    private void DoLine(String line)
    {
        if(line.isEmpty())
            return;
        if(line.indexOf("call") == 0)
        {
            line = line.replace("call", "").trim();
            scriptsToCall.add(line);
        } else
        {
            String cons[] = line.split("&&");
            for(int n = 0; n < cons.length; n++)
            {
                if(cons[n].trim().isEmpty())
                    continue;
                BaseCommand com = ReturnCommand(cons[n].trim());
                if(com instanceof TimeSinceLastRan)
                    ((TimeSinceLastRan)com).triggerInst = name;
                Conditions.add(com);
            }

        }
    }

    public boolean ConditionPassed()
    {
        for(int n = 0; n < Conditions.size(); n++)
            if(!((BaseCommand)Conditions.get(n)).getValue())
                return false;

        return true;
    }

    public void Process()
    {
        if(!ConditionPassed())
            return;
        if(ScriptManager.instance.CustomTriggerLastRan.containsKey(name))
            ScriptManager.instance.CustomTriggerLastRan.put(name, Integer.valueOf(0));
        for(int n = 0; n < scriptsToCall.size(); n++)
        {
            String str = (String)scriptsToCall.get(n);
            module.PlayScript(str);
        }

    }

    public static Integer tot = Integer.valueOf(0);
    public String name;
    Stack Conditions;
    public boolean Locked;
    public NulledArrayList scriptsToCall;
    public String TriggerParam;
    public String TriggerParam2;
    public String TriggerParam3;

}
