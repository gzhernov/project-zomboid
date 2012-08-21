// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Quest.java

package zombie.Quests;

import java.util.Iterator;
import java.util.Stack;
import zombie.Quests.questactions.QuestAction;
import zombie.scripting.objects.ScriptCharacter;

// Referenced classes of package zombie.Quests:
//            QuestTask_EquipItem, QuestTask_FindItem, QuestTask_GiveItem, QuestTask_GotoLocation, 
//            QuestTask_TalkTo, QuestTask_UseItemOn, QuestTask, QuestTask_ArbitaryAction, 
//            QuestTask_ScriptCondition, Completable

public class Quest
    implements Completable
{

    public Quest(String InternalName, String QuestNameString)
    {
        Complete = false;
        Failed = false;
        NumQuestTasks = 0;
        OnCompleteActions = new Stack();
        QuestTaskStack = new Stack();
        Unlocked = false;
        InternalQuestName = "Default Quest Name";
        QuestName = "Default Quest Name";
        InternalQuestName = InternalName;
        QuestName = QuestNameString;
        NumQuestTasks = 0;
        Complete = false;
        Failed = false;
    }

    public QuestTask AddQuestTask_EquipItem(String InternalName, String TaskNameString, String ItemType)
    {
        QuestTask_EquipItem NewTask = new QuestTask_EquipItem(InternalName, TaskNameString, ItemType);
        QuestTaskStack.add(NewTask);
        NumQuestTasks++;
        return NewTask;
    }

    public QuestTask AddTask_FindItem(String InternalName, String TaskNameString, String ItemType, int NumRequired)
    {
        QuestTask_FindItem NewTask = new QuestTask_FindItem(InternalName, TaskNameString, ItemType, NumRequired);
        QuestTaskStack.add(NewTask);
        NumQuestTasks++;
        return NewTask;
    }

    public QuestTask AddTask_GiveItem(String InternalName, String TaskNameString, String TaskItemName, String TaskCharacterName)
    {
        QuestTask_GiveItem NewTask = new QuestTask_GiveItem(InternalName, TaskNameString, TaskItemName, TaskCharacterName);
        QuestTaskStack.add(NewTask);
        NumQuestTasks++;
        return NewTask;
    }

    public QuestTask AddTask_GotoLocation(String InternalName, String TaskNameString, int x, int y, int z)
    {
        QuestTask_GotoLocation NewTask = new QuestTask_GotoLocation(InternalName, TaskNameString, x, y, z);
        QuestTaskStack.add(NewTask);
        NumQuestTasks++;
        return NewTask;
    }

    public QuestTask AddTask_TalkTo(String InternalName, String TaskNameString, String TaskCharacterName)
    {
        QuestTask_TalkTo NewTask = new QuestTask_TalkTo(InternalName, TaskNameString, TaskCharacterName);
        QuestTaskStack.add(NewTask);
        NumQuestTasks++;
        return NewTask;
    }

    public QuestTask AddTask_UseItemOn(String InternalName, String TaskNameString, String ItemType, ScriptCharacter TaskCharacter)
    {
        QuestTask_UseItemOn NewTask = new QuestTask_UseItemOn(InternalName, TaskNameString, ItemType, TaskCharacter);
        QuestTaskStack.add(NewTask);
        NumQuestTasks++;
        return NewTask;
    }

    public QuestTask FindTask(String InternalName)
    {
        for(int i = 0; i < QuestTaskStack.size(); i++)
            if(((QuestTask)QuestTaskStack.get(i)).getInternalName().equals(InternalName))
                return (QuestTask)QuestTaskStack.get(i);

        return null;
    }

    public String getInternalName()
    {
        return InternalQuestName;
    }

    public String getName()
    {
        return QuestName;
    }

    public int getNumTasks()
    {
        return QuestTaskStack.size();
    }

    public String getTaskName(int i)
    {
        if(i < 0)
            return "Task does not exist.";
        if(i >= QuestTaskStack.size())
            return "Task does not exist.";
        else
            return ((QuestTask)QuestTaskStack.get(i)).getName();
    }

    public boolean IsComplete()
    {
        return Complete;
    }

    public boolean IsFailed()
    {
        return Failed;
    }

    public boolean TaskComplete(int i)
    {
        if(i < 0)
            return false;
        if(i >= QuestTaskStack.size())
            return false;
        else
            return ((QuestTask)QuestTaskStack.get(i)).IsComplete();
    }

    public boolean TaskFailed(int i)
    {
        if(i < 0)
            return false;
        if(i >= QuestTaskStack.size())
            return false;
        else
            return ((QuestTask)QuestTaskStack.get(i)).IsFailed();
    }

    public void Update()
    {
        if(!Complete)
        {
            boolean QuestComplete = true;
            for(int i = 0; i < QuestTaskStack.size(); i++)
            {
                if(!((QuestTask)QuestTaskStack.get(i)).Unlocked)
                {
                    QuestComplete = false;
                    continue;
                }
                if(!((QuestTask)QuestTaskStack.get(i)).IsComplete())
                    QuestComplete = false;
            }

            Complete = QuestComplete;
            if(Complete)
            {
                QuestAction action;
                for(Iterator i$ = OnCompleteActions.iterator(); i$.hasNext(); action.Execute())
                    action = (QuestAction)i$.next();

            }
        }
        if(!Failed)
        {
            boolean QuestFailed = false;
            for(int i = 0; i < QuestTaskStack.size(); i++)
                if(((QuestTask)QuestTaskStack.get(i)).IsFailed())
                    QuestFailed = true;

            Failed = QuestFailed;
        }
        for(int i = 0; i < QuestTaskStack.size(); i++)
            ((QuestTask)QuestTaskStack.get(i)).Update();

    }

    QuestTask AddQuestTask_ArbitaryAction(String InternalName, String Title, String ArbActionHandle)
    {
        QuestTask_ArbitaryAction NewTask = new QuestTask_ArbitaryAction(InternalName, Title, ArbActionHandle);
        QuestTaskStack.add(NewTask);
        NumQuestTasks++;
        return NewTask;
    }

    QuestTask AddQuestTask_ScriptCondition(String InternalName, String Title, String ArbActionHandle)
    {
        QuestTask_ScriptCondition NewTask = new QuestTask_ScriptCondition(InternalName, Title, ArbActionHandle);
        QuestTaskStack.add(NewTask);
        NumQuestTasks++;
        return NewTask;
    }

    public boolean Complete;
    public boolean Failed;
    public int NumQuestTasks;
    public Stack OnCompleteActions;
    public Stack<QuestTask> QuestTaskStack;
    public boolean Unlocked;
    String InternalQuestName;
    String QuestName;
}
