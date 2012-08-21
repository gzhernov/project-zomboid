// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RunScriptOnComplete.java

package zombie.scripting.commands.quest;

import zombie.Quests.QuestCreator;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptModule;

public class RunScriptOnComplete extends BaseCommand
{

    public RunScriptOnComplete()
    {
    }

    public void init(String object, String params[])
    {
        if(object == null || !object.equals("Quest"))
        {
            return;
        } else
        {
            script = params[0].trim().replace("\"", "");
            return;
        }
    }

    public void begin()
    {
        if(script.contains("."))
            QuestCreator.AddQuestAction_RunScript(script);
        else
            QuestCreator.AddQuestAction_RunScript((new StringBuilder()).append(module.name).append(".").append(script).toString());
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

    String script;
}
