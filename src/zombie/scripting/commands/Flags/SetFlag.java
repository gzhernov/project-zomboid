// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SetFlag.java

package zombie.scripting.commands.Flags;

import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptFlag;
import zombie.scripting.objects.ScriptModule;

public class SetFlag extends BaseCommand
{

    public SetFlag()
    {
    }

    public void init(String object, String params[])
    {
        name = object.trim().replace("\"", "");
        val = params[0].trim().replace("\"", "");
    }

    public void begin()
    {
        ScriptFlag vala;
        vala = module.getFlag(name);
        if(vala == null)
            return;
        try
        {
            vala.SetValue(val);
        }
        catch(Exception ex)
        {
            ex = ex;
        }
        return;
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
