// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RandomSelector.java

package zombie.scripting.objects;

import java.util.ArrayList;
import zombie.core.Rand;
import zombie.scripting.ScriptManager;

// Referenced classes of package zombie.scripting.objects:
//            Script, ScriptModule

public class RandomSelector extends Script
{

    public RandomSelector()
    {
        scriptsToCall = new ArrayList();
    }

    public void Load(String name, String strArray[])
    {
        this.name = new String(name);
        for(int n = 0; n < strArray.length; n++)
            DoLine(new String(strArray[n].trim()));

    }

    private void DoLine(String line)
    {
        if(line.isEmpty())
        {
            return;
        } else
        {
            scriptsToCall.add(line);
            return;
        }
    }

    public String Process()
    {
        int n = Rand.Next(scriptsToCall.size());
        if(((String)scriptsToCall.get(n)).contains("."))
            ScriptManager.instance.PlayScript((String)scriptsToCall.get(n));
        else
            ScriptManager.instance.PlayScript((new StringBuilder()).append(module.name).append(".").append((String)scriptsToCall.get(n)).toString());
        return (String)scriptsToCall.get(n);
    }

    public Script.ScriptInstance Process(Script.ScriptInstance aliases)
    {
        int n = Rand.Next(scriptsToCall.size());
        if(((String)scriptsToCall.get(n)).contains("."))
            return ScriptManager.instance.PlayScript((String)scriptsToCall.get(n), aliases);
        else
            return ScriptManager.instance.PlayScript((new StringBuilder()).append(module.name).append(".").append((String)scriptsToCall.get(n)).toString(), aliases);
    }

    public String name;
    public ArrayList scriptsToCall;
}
