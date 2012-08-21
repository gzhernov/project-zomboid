// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AddScriptConditionTask.java

package zombie.scripting.commands.quest;

import zombie.Quests.QuestCreator;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptModule;

public class AddScriptConditionTask extends BaseCommand
{

    public AddScriptConditionTask()
    {
    }

    public void init(String object, String params[])
    {
        if(object == null || !object.equals("Quest"))
            return;
        name = params[0].trim().replace("\"", "");
        description = params[1].trim().replace("\"", "");
        description = module.getLanguage(description);
        if(description.indexOf("\"") == 0)
        {
            description = description.substring(1);
            description = description.substring(0, description.length() - 1);
        }
        task = params[2].trim().replace("\"", "");
    }

    public void begin()
    {
        String t = task;
        if(!t.contains("."))
            t = (new StringBuilder()).append(module.name).append(".").append(t).toString();
        QuestCreator.AddQuestTask_ScriptCondition(name, description, t);
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
    String description;
    String task;
}
