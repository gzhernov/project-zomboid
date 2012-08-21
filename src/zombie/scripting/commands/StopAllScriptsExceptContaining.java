// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StopAllScriptsExceptContaining.java

package zombie.scripting.commands;

import java.util.Stack;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Script;

// Referenced classes of package zombie.scripting.commands:
//            BaseCommand

public class StopAllScriptsExceptContaining extends BaseCommand
{

    public StopAllScriptsExceptContaining()
    {
        scripts = null;
    }

    public void init(String object, String params[])
    {
        scripts = params[0].trim().replace("\"", "");
    }

    public void begin()
    {
        for(int n = 0; n < ScriptManager.instance.PlayingScripts.size(); n++)
            if(!((zombie.scripting.objects.Script.ScriptInstance)ScriptManager.instance.PlayingScripts.get(n)).theScript.name.contains(scripts))
                ScriptManager.instance.StopScript(((zombie.scripting.objects.Script.ScriptInstance)ScriptManager.instance.PlayingScripts.get(n)).theScript.name);

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
    String scripts;
}
