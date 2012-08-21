// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Conditional.java

package zombie.scripting.objects;

import zombie.core.Collections.NulledArrayList;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.commands.ConditionalCommand;

// Referenced classes of package zombie.scripting.objects:
//            Script

public class Conditional extends Script
{

    public Conditional(String con, String internal)
    {
        Conditions = new NulledArrayList();
        DoScriptParsing("", internal);
        if(con == null)
            return;
        String cons[] = con.split("&&");
        for(int n = 0; n < cons.length; n++)
            if(!cons[n].trim().isEmpty())
                Conditions.add(ReturnCommand(cons[n].trim()));

    }

    public Conditional(String con, String internal, ConditionalCommand command)
    {
        Conditions = new NulledArrayList();
        this.command = command;
        DoScriptParsing("", internal);
        if(con == null)
            return;
        String cons[] = con.split("&&");
        for(int n = 0; n < cons.length; n++)
            if(!cons[n].trim().isEmpty())
                Conditions.add(ReturnCommand(cons[n].trim()));

    }

    public boolean ConditionPassed(Script.ScriptInstance inst)
    {
        for(int n = 0; n < Conditions.size(); n++)
        {
            ((BaseCommand)Conditions.get(n)).currentinstance = inst;
            if(!((BaseCommand)Conditions.get(n)).getValue())
                return false;
        }

        return true;
    }

    NulledArrayList Conditions;
    public ConditionalCommand command;
}
