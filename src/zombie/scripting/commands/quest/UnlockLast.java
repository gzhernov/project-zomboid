// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UnlockLast.java

package zombie.scripting.commands.quest;

import zombie.Quests.QuestCreator;
import zombie.scripting.commands.BaseCommand;

public class UnlockLast extends BaseCommand
{

    public UnlockLast()
    {
        quest = null;
    }

    public void init(String object, String params[])
    {
        if(object == null || !object.equals("Quest"))
            return;
        else
            return;
    }

    public void begin()
    {
        QuestCreator.Unlock();
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

    String quest;
}
