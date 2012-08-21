// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UnlockTaskOnComplete.java

package zombie.scripting.commands.quest;

import zombie.Quests.QuestCreator;
import zombie.scripting.commands.BaseCommand;

public class UnlockTaskOnComplete extends BaseCommand
{

    public UnlockTaskOnComplete()
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
        QuestCreator.AddQuestAction_UnlockQuestTask(script);
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
