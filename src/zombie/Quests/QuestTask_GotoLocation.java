// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QuestTask_GotoLocation.java

package zombie.Quests;

import zombie.characters.IsoPlayer;
import zombie.iso.*;
import zombie.ui.QuestHUD;
import zombie.ui.UIManager;

// Referenced classes of package zombie.Quests:
//            QuestTask, QuestTaskType

public class QuestTask_GotoLocation extends QuestTask
{

    public QuestTask_GotoLocation(String InternalName, String TaskNameString, int x, int y, int z)
    {
        super(QuestTaskType.GotoLocation, InternalName, TaskNameString);
        Task_x = x;
        Task_y = y;
        Task_z = z;
    }

    public void Update()
    {
        if(!Unlocked)
        {
            Complete = false;
            return;
        }
        if(!Complete && IsoPlayer.getInstance().getCurrentSquare().getX() > Task_x - 2 && IsoPlayer.getInstance().getCurrentSquare().getX() < Task_x + 2 && IsoPlayer.getInstance().getCurrentSquare().getY() > Task_y - 2 && IsoPlayer.getInstance().getCurrentSquare().getY() < Task_y + 2 && IsoPlayer.getInstance().getCurrentSquare().getZ() == Task_z && IsoPlayer.getInstance().getCurrentSquare().getRoom() == IsoWorld.instance.CurrentCell.getGridSquare(Task_x, Task_y, Task_z).getRoom())
        {
            Complete = true;
            if(UIManager.getOnscreenQuest() != null)
                UIManager.getOnscreenQuest().TriggerQuestWiggle();
        }
        super.Update();
    }

    int Task_x;
    int Task_y;
    int Task_z;
}
