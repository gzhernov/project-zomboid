// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StopScript.java

package zombie.scripting.commands.Script;

import zombie.scripting.ScriptManager;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.Conditional;
import zombie.scripting.objects.Script;

public class StopScript extends BaseCommand
{

    public StopScript()
    {
    }

    public void init(String object, String params[])
    {
        position = object;
    }

    public void begin()
    {
        if(position == null)
        {
            zombie.scripting.objects.Script.ScriptInstance parent;
            for(parent = currentinstance; parent.parent != null && (parent.theScript instanceof Conditional); parent = parent.parent);
            ScriptManager.instance.StopScript(parent);
        } else
        {
            ScriptManager.instance.StopScript(position);
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

    String position;
}
