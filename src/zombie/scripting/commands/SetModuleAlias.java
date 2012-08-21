// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SetModuleAlias.java

package zombie.scripting.commands;

import gnu.trove.map.hash.THashMap;
import zombie.scripting.ScriptManager;

// Referenced classes of package zombie.scripting.commands:
//            BaseCommand

public class SetModuleAlias extends BaseCommand
{

    public SetModuleAlias()
    {
    }

    public void init(String object, String params[])
    {
        a = params[0].trim();
        b = params[1].trim();
    }

    public void begin()
    {
        ScriptManager.instance.ModuleAliases.put(a, b);
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
    String a;
    String b;
}
