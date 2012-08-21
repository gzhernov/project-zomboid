// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QuestTask_EquipItem.java

package zombie.Quests;

import zombie.characters.IsoPlayer;
import zombie.inventory.InventoryItem;
import zombie.ui.QuestHUD;
import zombie.ui.UIManager;

// Referenced classes of package zombie.Quests:
//            QuestTask, QuestTaskType

public class QuestTask_EquipItem extends QuestTask
{

    public QuestTask_EquipItem(String InternalName, String TaskNameString, String ItemType)
    {
        super(QuestTaskType.FindItem, InternalName, TaskNameString);
        QuestItemType = ItemType;
    }

    public void Update()
    {
        if(!Complete && IsoPlayer.getInstance().getPrimaryHandItem() != null && IsoPlayer.getInstance().getPrimaryHandItem().getType().equals(QuestItemType))
        {
            Complete = true;
            if(UIManager.getOnscreenQuest() != null)
                UIManager.getOnscreenQuest().TriggerQuestWiggle();
        }
        super.Update();
    }

    String QuestItemType;
}
