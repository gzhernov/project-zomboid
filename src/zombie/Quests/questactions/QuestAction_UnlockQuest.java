// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QuestAction_UnlockQuest.java

package zombie.Quests.questactions;

import zombie.Quests.Quest;
import zombie.Quests.QuestManager;

// Referenced classes of package zombie.Quests.questactions:
//            QuestAction

public class QuestAction_UnlockQuest
    implements QuestAction
{

    public QuestAction_UnlockQuest(String questInternal)
    {
        Quest = questInternal;
    }

    public void Execute()
    {
        Quest quest = QuestManager.instance.FindQuest(Quest);
        if(quest == null)
        {
            return;
        } else
        {
            quest.Unlocked = true;
            return;
        }
    }

    String Quest;
}
