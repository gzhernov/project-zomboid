// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QuestTask_FindItem.java

package zombie.Quests;

import zombie.characters.IsoPlayer;
import zombie.core.Collections.NulledArrayList;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.ui.QuestHUD;
import zombie.ui.UIManager;

// Referenced classes of package zombie.Quests:
//            QuestTask, QuestTaskType

public class QuestTask_FindItem extends QuestTask
{

    public QuestTask_FindItem(String InternalName, String TaskNameString, String ItemType, int NumRequired)
    {
        super(QuestTaskType.FindItem, InternalName, TaskNameString);
        QuestItemType = ItemType;
        QuestItemRequiredAmmount = NumRequired;
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
            int NumItemsGathered = 0;
            for(int i = 0; i < IsoPlayer.getInstance().getInventory().Items.size(); i++)
                if(((InventoryItem)IsoPlayer.getInstance().getInventory().Items.get(i)).getType().equals(QuestItemType))
                    NumItemsGathered += ((InventoryItem)IsoPlayer.getInstance().getInventory().Items.get(i)).getUses();

            if(NumItemsGathered >= QuestItemRequiredAmmount)
            {
                Complete = true;
                if(UIManager.getOnscreenQuest() != null)
                    UIManager.getOnscreenQuest().TriggerQuestWiggle();
            }
        }
        super.Update();
    }

    int QuestItemRequiredAmmount;
    String QuestItemType;
}
