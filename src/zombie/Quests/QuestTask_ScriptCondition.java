// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QuestTask_ScriptCondition.java

package zombie.Quests;

import zombie.scripting.ScriptManager;
import zombie.scripting.objects.QuestTaskCondition;

// Referenced classes of package zombie.Quests:
//            QuestTask, QuestTaskType

public class QuestTask_ScriptCondition extends QuestTask
{

    public QuestTask_ScriptCondition(String InternalName, String TaskNameString, String ArbAction)
    {
        super(QuestTaskType.Custom, InternalName, TaskNameString);
        this.ArbAction = ArbAction;
    }

    public void Update()
    {
        if(!Complete && ArbActionCheck())
            Complete = true;
        super.Update();
    }

    private boolean ArbActionCheck()
    {
        QuestTaskCondition con = ScriptManager.instance.getQuestCondition(ArbAction);
        if(con == null)
            return true;
        else
            return con.ConditionPassed();
    }

    String ArbAction;
}
