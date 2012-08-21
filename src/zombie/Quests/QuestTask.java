// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QuestTask.java

package zombie.Quests;

import java.util.Iterator;
import java.util.Stack;
import zombie.Quests.questactions.QuestAction;

// Referenced classes of package zombie.Quests:
//            Completable, QuestTaskType

public class QuestTask
    implements Completable
{

    public QuestTask(QuestTaskType TaskType, String InternalName, String TaskNameString)
    {
        Hidden = false;
        OnCompleteActions = new Stack();
        Unlocked = false;
        InternalTaskName = "Default Task Name";
        TaskName = "Default Task Name";
        InternalTaskName = InternalName;
        this.TaskType = TaskType;
        TaskName = TaskNameString;
        Complete = false;
        Failed = false;
        WasComplete = false;
    }

    public String getInternalName()
    {
        return InternalTaskName;
    }

    public String getName()
    {
        return TaskName;
    }

    public boolean IsComplete()
    {
        return Complete;
    }

    public boolean IsFailed()
    {
        return Failed;
    }

    public void Update()
    {
        if(Complete && !WasComplete)
        {
            QuestAction action;
            for(Iterator i$ = OnCompleteActions.iterator(); i$.hasNext(); action.Execute())
                action = (QuestAction)i$.next();

        }
        WasComplete = Complete;
    }

    public boolean Complete;
    public boolean Failed;
    public boolean Hidden;
    public Stack OnCompleteActions;
    public QuestTaskType TaskType;
    public boolean Unlocked;
    public boolean WasComplete;
    String InternalTaskName;
    String TaskName;
}
