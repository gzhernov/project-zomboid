// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QuestTaskCondition.java

package zombie.scripting.objects;

import java.util.Stack;
import zombie.scripting.commands.BaseCommand;

// Referenced classes of package zombie.scripting.objects:
//            Script

public class QuestTaskCondition extends Script
{

    public QuestTaskCondition()
    {
        Conditions = new Stack();
    }

    public void Load(String name, String strArray[])
    {
        String con = strArray[0].trim();
        if(con == null)
            return;
        String cons[] = con.split("&&");
        for(int n = 0; n < cons.length; n++)
            if(!cons[n].trim().isEmpty())
                Conditions.add(ReturnCommand(cons[n].trim()));

    }

    public boolean ConditionPassed()
    {
        for(int n = 0; n < Conditions.size(); n++)
            if(!((BaseCommand)Conditions.get(n)).getValue())
                return false;

        return true;
    }

    Stack Conditions;
}
