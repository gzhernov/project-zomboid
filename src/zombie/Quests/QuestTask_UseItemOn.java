// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QuestTask_UseItemOn.java

package zombie.Quests;

import java.util.Stack;
import zombie.characters.IsoGameCharacter;
import zombie.scripting.objects.ScriptCharacter;
import zombie.ui.QuestHUD;
import zombie.ui.UIManager;

// Referenced classes of package zombie.Quests:
//            QuestTask, QuestTaskType

public class QuestTask_UseItemOn extends QuestTask
{

    public QuestTask_UseItemOn(String InternalName, String TaskNameString, String ItemType, ScriptCharacter TaskCharacter)
    {
        super(QuestTaskType.UseItemOn, InternalName, TaskNameString);
        QuestItemType = ItemType;
        Character = TaskCharacter;
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
            if(Character.Actual == null)
            {
                super.Update();
                return;
            }
            boolean found = false;
            int n = 0;
            do
            {
                if(n >= Character.Actual.getUsedItemsOn().size())
                    break;
                if(((String)Character.Actual.getUsedItemsOn().get(n)).equals(QuestItemType))
                {
                    found = true;
                    break;
                }
                n++;
            } while(true);
            if(found)
            {
                Character.Actual.getUsedItemsOn().remove(QuestItemType);
                if(UIManager.getOnscreenQuest() != null)
                    UIManager.getOnscreenQuest().TriggerQuestWiggle();
                if(UIManager.getOnscreenQuest() != null)
                    UIManager.getOnscreenQuest().TriggerQuestWiggle();
                Complete = true;
            }
        }
        if(!Failed)
        {
            if(Character.Actual == null)
            {
                super.Update();
                return;
            }
            if(Character.Actual.getHealth() <= 0.0F)
            {
                Failed = true;
                if(UIManager.getOnscreenQuest() != null)
                    UIManager.getOnscreenQuest().TriggerQuestWiggle();
            }
        }
        super.Update();
    }

    ScriptCharacter Character;
    String QuestItemType;
}
