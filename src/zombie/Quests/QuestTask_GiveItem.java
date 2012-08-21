// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QuestTask_GiveItem.java

package zombie.Quests;

import zombie.ui.QuestHUD;
import zombie.ui.UIManager;

// Referenced classes of package zombie.Quests:
//            QuestTask, QuestTaskType

public class QuestTask_GiveItem extends QuestTask
{

    public QuestTask_GiveItem(String InternalName, String TaskNameString, String TaskItemName, String TaskCharacterName)
    {
        super(QuestTaskType.GiveItem, InternalName, TaskNameString);
        ItemName = TaskItemName;
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
    String ItemName;
}
