// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsFlagValue.java

package zombie.scripting.commands.Flags;

import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptFlag;
import zombie.scripting.objects.ScriptModule;

public class IsFlagValue extends BaseCommand
{

    public IsFlagValue()
    {
        invert = false;
    }

    public void begin()
    {
    }

    public boolean getValue()
    {
        ScriptFlag val = module.getFlag(name);
        if(val == null)
            return false;
        if(invert)
            return !val.IsValue(value);
        else
            return val.IsValue(value);
    }

    public boolean IsFinished()
    {
        return true;
    }

    public void update()
    {
    }

    public void init(String object, String params[])
    {
        name = object;
        if(name != null && name.indexOf("!") == 0)
        {
            invert = true;
            name = name.substring(1);
        }
        value = params[0].trim().replace("\"", "");
    }

    public boolean DoesInstantly()
    {
        return true;
    }

    boolean invert;
    String name;
    String value;
}
