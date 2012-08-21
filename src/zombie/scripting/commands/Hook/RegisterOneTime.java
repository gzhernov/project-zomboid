// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RegisterOneTime.java

package zombie.scripting.commands.Hook;

import zombie.scripting.ScriptManager;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptModule;

public class RegisterOneTime extends BaseCommand
{

    public RegisterOneTime()
    {
        num = 1;
    }

    public void init(String object, String params[])
    {
        if(object == null || !object.equals("Hook"))
        {
            return;
        } else
        {
            event = params[0].trim().replace("\"", "");
            script = params[1].trim().replace("\"", "");
            return;
        }
    }

    public void begin()
    {
        String str = script;
        if(!str.contains("."))
            str = (new StringBuilder()).append(module.name).append(".").append(str).toString();
        ScriptManager.instance.AddOneTime(event, str);
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

    String event;
    String script;
    int num;
}
