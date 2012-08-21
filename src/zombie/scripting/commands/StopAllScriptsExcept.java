// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StopAllScriptsExcept.java

package zombie.scripting.commands;

import java.util.ArrayList;
import java.util.Stack;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Script;

// Referenced classes of package zombie.scripting.commands:
//            BaseCommand

public class StopAllScriptsExcept extends BaseCommand
{

    public StopAllScriptsExcept()
    {
        scripts = new ArrayList();
    }

    public void init(String object, String params[])
    {
        for(int n = 0; n < params.length; n++)
            scripts.add(params[n].trim());

    }

    public void begin()
    {
        for(int n = 0; n < ScriptManager.instance.PlayingScripts.size(); n++)
        {
            boolean bFound = false;
            for(int m = 0; m < scripts.size(); m++)
                if(((String)scripts.get(m)).equals(((zombie.scripting.objects.Script.ScriptInstance)ScriptManager.instance.PlayingScripts.get(n)).theScript.name))
                    bFound = true;

            if(!bFound)
                ScriptManager.instance.StopScript(((zombie.scripting.objects.Script.ScriptInstance)ScriptManager.instance.PlayingScripts.get(n)).theScript.name);
        }

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

    String name;
    ArrayList scripts;
}
