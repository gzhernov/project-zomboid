// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UnlockTasksOnComplete.java

package zombie.scripting.commands.quest;

import zombie.Quests.QuestCreator;
import zombie.scripting.commands.BaseCommand;

public class UnlockTasksOnComplete extends BaseCommand
{

    public UnlockTasksOnComplete()
    {
    }

    public void init(String object, String params[])
    {
        if(object == null || !object.equals("Quest"))
        {
            return;
        } else
        {
            count = Integer.parseInt(params[0].trim());
            return;
        }
    }

    public void begin()
    {
        QuestCreator.SetToUnlockNext(count);
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

    int count;
}
