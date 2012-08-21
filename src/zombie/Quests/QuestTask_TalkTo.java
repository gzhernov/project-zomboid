// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QuestTask_TalkTo.java

package zombie.Quests;

import zombie.ui.QuestHUD;
import zombie.ui.UIManager;

// Referenced classes of package zombie.Quests:
//            QuestTask, QuestTaskType

public class QuestTask_TalkTo extends QuestTask
{

    public QuestTask_TalkTo(String InternalName, String TaskNameString, String TaskCharacterName)
    {
        super(QuestTaskType.TalkTo, InternalName, TaskNameString);
        CharacterName = TaskCharacterName;
    }

    public void Update()
    {
        if(!Unlocked)
        {
            Complete = false;
            return;
        }
        if(!Complete)
        {
            Complete = true;
            if(UIManager.getOnscreenQuest() != null)
                UIManager.getOnscreenQuest().TriggerQuestWiggle();
        }
        if(!Failed)
        {
            Failed = true;
            if(UIManager.getOnscreenQuest() != null)
                UIManager.getOnscreenQuest().TriggerQuestWiggle();
        }
        super.Update();
    }

    String CharacterName;
}
