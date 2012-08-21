// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QuestManager.java

package zombie.Quests;

import java.util.Iterator;
import java.util.Stack;
import zombie.scripting.objects.ScriptCharacter;

// Referenced classes of package zombie.Quests:
//            Quest, QuestTask

public class QuestManager
{

    public QuestManager()
    {
        NumActiveQuests = 0;
        QuestStack = new Stack();
    }

    public QuestTask AddQuestTask_ArbitaryAction(Quest CurrentQuest, String InternalName, String Title, String ArbActionHandle)
    {
        LastAddedQuestTask = CurrentQuest.AddQuestTask_ArbitaryAction(InternalName, Title, ArbActionHandle);
        return LastAddedQuestTask;
    }

    public QuestTask AddQuestTask_ScriptCondition(Quest CurrentQuest, String InternalName, String Title, String ArbActionHandle)
    {
        LastAddedQuestTask = CurrentQuest.AddQuestTask_ScriptCondition(InternalName, Title, ArbActionHandle);
        return LastAddedQuestTask;
    }

    public QuestTask AddQuestTask_EquipItem(Quest CurrentQuest, String InternalName, String NewTaskName, String ItemType)
    {
        LastAddedQuestTask = CurrentQuest.AddQuestTask_EquipItem(InternalName, NewTaskName, ItemType);
        return LastAddedQuestTask;
    }

    public QuestTask AddQuestTask_FindItem(Quest CurrentQuest, String InternalName, String NewTaskName, String ItemType, int NumRequired)
    {
        LastAddedQuestTask = CurrentQuest.AddTask_FindItem(InternalName, NewTaskName, ItemType, NumRequired);
        return LastAddedQuestTask;
    }

    public QuestTask AddQuestTask_GiveItem(Quest CurrentQuest, String InternalName, String NewTaskName, String QuestItemName, String QuestCharacterName)
    {
        LastAddedQuestTask = CurrentQuest.AddTask_GiveItem(InternalName, NewTaskName, QuestItemName, QuestCharacterName);
        return LastAddedQuestTask;
    }

    public QuestTask AddQuestTask_GotoLocation(Quest CurrentQuest, String InternalName, String NewTaskName, int x, int y, int z)
    {
        LastAddedQuestTask = CurrentQuest.AddTask_GotoLocation(InternalName, NewTaskName, x, y, z);
        return LastAddedQuestTask;
    }

    public QuestTask AddQuestTask_TalkTo(Quest CurrentQuest, String InternalName, String NewTaskName, String QuestCharacterName)
    {
        LastAddedQuestTask = CurrentQuest.AddTask_TalkTo(InternalName, NewTaskName, QuestCharacterName);
        return LastAddedQuestTask;
    }

    public QuestTask AddQuestTask_UseItemOn(Quest CurrentQuest, String InternalName, String NewTaskName, String ItemType, ScriptCharacter TaskCharacter)
    {
        LastAddedQuestTask = CurrentQuest.AddTask_UseItemOn(InternalName, NewTaskName, ItemType, TaskCharacter);
        return LastAddedQuestTask;
    }

    public Quest CreateQuest(String InternalName, String QuestName)
    {
        Quest NewQuest = new Quest(InternalName, QuestName);
        QuestStack.add(NewQuest);
        NumActiveQuests++;
        return NewQuest;
    }

    public Quest FindQuest(String InternalName)
    {
        for(Iterator i$ = QuestStack.iterator(); i$.hasNext();)
        {
            Quest q = (Quest)i$.next();
            if(q.getInternalName().trim().equalsIgnoreCase(InternalName))
                return q;
        }

        return null;
    }

    public int getNumQuests()
    {
        return NumActiveQuests;
    }

    public Quest getQuest(int i)
    {
        if(i < 0)
            return null;
        if(i >= QuestStack.size())
            return null;
        else
            return (Quest)QuestStack.get(i);
    }

    public String getQuestName(int i)
    {
        if(i < 0)
            return null;
        if(i >= QuestStack.size())
            return null;
        else
            return ((Quest)QuestStack.get(i)).getName();
    }

    public boolean QuestComplete(int i)
    {
        if(i < 0)
            return false;
        if(i >= QuestStack.size())
            return false;
        else
            return ((Quest)QuestStack.get(i)).Complete;
    }

    public void Update()
    {
        for(int i = 0; i < QuestStack.size(); i++)
            if(((Quest)QuestStack.get(i)).Unlocked)
                ((Quest)QuestStack.get(i)).Update();

    }

    public static QuestManager instance = new QuestManager();
    public int NumActiveQuests;
    public Stack<Quest> QuestStack;
    QuestTask LastAddedQuestTask;

}
