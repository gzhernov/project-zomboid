// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ToggleActivatable.java

package zombie.scripting.commands.Activatable;

import zombie.iso.objects.interfaces.Activatable;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptActivatable;
import zombie.scripting.objects.ScriptModule;

public class ToggleActivatable extends BaseCommand
{

    public ToggleActivatable()
    {
        num = 1.0F;
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
        owner = object;
    }

    public void begin()
    {
        ScriptActivatable chr = module.getActivatable(owner);
        if(chr == null)
            return;
        Activatable acc = chr.getActual();
        if(acc != null)
            acc.Toggle();
    }

    public boolean DoesInstantly()
    {
        return true;
    }

    String owner;
    float num;
}
