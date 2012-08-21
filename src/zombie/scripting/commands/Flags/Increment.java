// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Increment.java

package zombie.scripting.commands.Flags;

import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptFlag;
import zombie.scripting.objects.ScriptModule;

public class Increment extends BaseCommand
{

    public Increment()
    {
    }

    public void init(String object, String params[])
    {
        name = object.trim().replace("\"", "");
    }

    public void begin()
    {
        try
        {
            val = module.getFlagValue(name);
            Integer ival = Integer.valueOf(Integer.parseInt(val));
            Integer integer = ival;
            Integer integer1 = ival = Integer.valueOf(ival.intValue() + 1);
            Integer _tmp = integer;
            module.getFlag(name).SetValue(ival.toString());
        }
        catch(Exception ex) { }
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
    String val;
}
