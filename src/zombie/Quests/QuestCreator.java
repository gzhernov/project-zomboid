// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QuestCreator.java

package zombie.Quests;

import java.util.Stack;
import zombie.Quests.questactions.QuestAction;
import zombie.Quests.questactions.QuestAction_RunScript;
import zombie.Quests.questactions.QuestAction_StartConversation;
import zombie.Quests.questactions.QuestAction_TutorialIcon;
import zombie.Quests.questactions.QuestAction_UnlockQuest;
import zombie.Quests.questactions.QuestAction_UnlockQuestTask;
import zombie.characters.Talker;
import zombie.iso.IsoObject;
import zombie.scripting.objects.ScriptCharacter;
import zombie.ui.UIElement;

// Referenced classes of package zombie.Quests:
//            QuestManager, Quest, QuestTask

public class QuestCreator
{

    public QuestCreator()
    {
    }

    public static void AddQuestAction_StartConversation(Talker a, Talker b, String name)
    {
        QuestAction action = new QuestAction_StartConversation(name, a, b);
        AddAction(action);
    }

    public static void AddQuestAction_TutorialIcon(String title, String message, IsoObject obj, boolean bAutoExpand, float yoff)
    {
        QuestAction action = new QuestAction_TutorialIcon(title, message, obj, bAutoExpand, yoff);
        AddAction(action);
    }

    public static void AddQuestAction_TutorialIcon(UIElement parent, int x, int y, String title, String string, boolean bAutoExpand)
    {
        QuestAction action = new QuestAction_TutorialIcon(parent, x, y, title, string, bAutoExpand);
        AddAction(action);
    }

    public static void AddQuestAction_UnlockQuest(String name)
    {
        QuestAction action = new QuestAction_UnlockQuest(name);
        AddAction(action);
    }

    public static void AddQuestAction_RunScript(String script)
    {
        QuestAction action = new QuestAction_RunScript(script);
        AddAction(action);
    }

    public static void AddQuestAction_UnlockQuestTask(String name)
    {
        QuestAction action = new QuestAction_UnlockQuestTask(CurrentQuest, name);
        AddAction(action);
    }

    public static void AddQuestTask_ArbitaryAction(String InternalName, String Title, String ArbActionHandle)
    {
        QuestTask NewQuestTask = QuestManager.instance.AddQuestTask_ArbitaryAction(CurrentQuest, InternalName, Title, ArbActionHandle);
        HandleUnlockNext(NewQuestTask);
        CurrentQuestTask = NewQuestTask;
    }

    public static void AddQuestTask_ScriptCondition(String InternalName, String Title, String ArbActionHandle)
    {
        QuestTask NewQuestTask = QuestManager.instance.AddQuestTask_ScriptCondition(CurrentQuest, InternalName, Title, ArbActionHandle);
        HandleUnlockNext(NewQuestTask);
        CurrentQuestTask = NewQuestTask;
    }

    public static void AddQuestTask_EquipItem(String InternalName, String NewTaskName, String ItemType)
    {
        QuestTask NewQuestTask = QuestManager.instance.AddQuestTask_EquipItem(CurrentQuest, InternalName, NewTaskName, ItemType);
        HandleUnlockNext(NewQuestTask);
        CurrentQuestTask = NewQuestTask;
    }

    public static void AddQuestTask_FindItem(String InternalName, String NewTaskName, String ItemType, int NumRequired)
    {
        QuestTask NewQuestTask = QuestManager.instance.AddQuestTask_FindItem(CurrentQuest, InternalName, NewTaskName, ItemType, NumRequired);
        HandleUnlockNext(NewQuestTask);
        CurrentQuestTask = NewQuestTask;
    }

    public static void AddQuestTask_GiveItem(String InternalName, String NewTaskName, String QuestItemName, String QuestCharacterName)
    {
        QuestTask NewQuestTask = QuestManager.instance.AddQuestTask_GiveItem(CurrentQuest, InternalName, NewTaskName, QuestItemName, QuestCharacterName);
        HandleUnlockNext(NewQuestTask);
        CurrentQuestTask = NewQuestTask;
    }

    public static void AddQuestTask_GotoLocation(String InternalName, String NewTaskName, int x, int y, int z)
    {
        QuestTask NewQuestTask = CurrentQuest.AddTask_GotoLocation(InternalName, NewTaskName, x, y, z);
        HandleUnlockNext(NewQuestTask);
        CurrentQuestTask = NewQuestTask;
    }

    public static void AddQuestTask_TalkTo(String InternalName, String NewTaskName, String QuestCharacterName)
    {
        QuestTask NewQuestTask = QuestManager.instance.AddQuestTask_TalkTo(CurrentQuest, InternalName, NewTaskName, QuestCharacterName);
        HandleUnlockNext(NewQuestTask);
        CurrentQuestTask = NewQuestTask;
    }

    public static void AddQuestTask_UseItemOn(String InternalName, String NewTaskName, String ItemType, ScriptCharacter TaskCharacter)
    {
        QuestTask NewQuestTask = QuestManager.instance.AddQuestTask_UseItemOn(CurrentQuest, InternalName, NewTaskName, ItemType, TaskCharacter);
        HandleUnlockNext(NewQuestTask);
        CurrentQuestTask = NewQuestTask;
    }

    public static void CreateQuest(String InternalName, String QuestName)
    {
        Quest NextQuest = QuestManager.instance.CreateQuest(InternalName, QuestName);
        HandleUnlockNext(NextQuest);
        CurrentQuest = NextQuest;
        CurrentQuestTask = null;
    }

    public static void LockLast()
    {
        if(CurrentQuestTask == null)
            CurrentQuest.Unlocked = false;
        else
            CurrentQuestTask.Unlocked = false;
    }

    public static void SetToUnlockNext()
    {
        UnlockNextQuest = CurrentQuestTask;
        UnlockNext = 1;
    }

    public static void SetToUnlockNext(int count)
    {
        UnlockNextQuest = CurrentQuestTask;
        UnlockNext = count;
    }

    public static void Unlock()
    {
        if(CurrentQuestTask == null)
            CurrentQuest.Unlocked = true;
        else
            CurrentQuestTask.Unlocked = true;
    }

    public static void UnlockButHide()
    {
        CurrentQuestTask.Unlocked = true;
        CurrentQuestTask.Hidden = true;
    }

    static void AddAction(QuestAction action)
    {
        if(CurrentQuestTask == null)
            CurrentQuest.OnCompleteActions.add(action);
        else
            CurrentQuestTask.OnCompleteActions.add(action);
    }

    static void HandleUnlockNext(QuestTask task)
    {
        if(UnlockNext <= 0)
            return;
        if(UnlockNextQuest != null)
            UnlockNextQuest.OnCompleteActions.add(new QuestAction_UnlockQuestTask(CurrentQuest, task.InternalTaskName));
        else
            CurrentQuest.OnCompleteActions.add(new QuestAction_UnlockQuestTask(CurrentQuest, task.InternalTaskName));
        UnlockNext--;
        if(UnlockNext == 0)
            UnlockNextQuest = null;
    }

    static void HandleUnlockNext(Quest quest)
    {
        if(UnlockNext <= 0)
            return;
        if(UnlockNextQuest != null)
            UnlockNextQuest.OnCompleteActions.add(new QuestAction_UnlockQuest(quest.InternalQuestName));
        else
            CurrentQuest.OnCompleteActions.add(new QuestAction_UnlockQuest(quest.InternalQuestName));
        UnlockNext--;
        if(UnlockNext == 0)
            UnlockNextQuest = null;
    }

    static Quest CurrentQuest;
    static QuestTask CurrentQuestTask;
    static int UnlockNext = 0;
    static QuestTask UnlockNextQuest = null;

}
