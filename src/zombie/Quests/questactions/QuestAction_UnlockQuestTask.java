// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QuestAction_UnlockQuestTask.java

package zombie.Quests.questactions;

import zombie.Quests.Quest;
import zombie.Quests.QuestTask;

// Referenced classes of package zombie.Quests.questactions:
//            QuestAction

public class QuestAction_UnlockQuestTask
    implements QuestAction
{

    public QuestAction_UnlockQuestTask(Quest quest, String taskInternal)
    {
        Task = taskInternal;
        Quest = quest;
    }

    public void Execute()
    {
        QuestTask task = Quest.FindTask(Task);
        task.Unlocked = true;
    }

    Quest Quest;
    String Task;
}
