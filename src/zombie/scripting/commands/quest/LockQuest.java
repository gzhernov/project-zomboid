// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LockQuest.java

package zombie.scripting.commands.quest;

import zombie.Quests.Quest;
import zombie.Quests.QuestManager;
import zombie.scripting.commands.BaseCommand;
import zombie.ui.QuestPanel;

public class LockQuest extends BaseCommand
{

    public LockQuest()
    {
        quest = null;
    }

    public void init(String object, String params[])
    {
        if(object == null || !object.equals("Quest"))
            return;
        if(params.length == 1)
            quest = params[0].trim().replace("\"", "");
    }

    public void begin()
    {
        Quest questa = QuestManager.instance.FindQuest(quest);
        if(questa == null)
            return;
        questa.Unlocked = false;
        if(QuestPanel.instance.ActiveQuest == questa)
            QuestPanel.instance.ActiveQuest = null;
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
