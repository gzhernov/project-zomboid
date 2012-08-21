// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QuestTask_ArbitaryAction.java

package zombie.Quests;

import java.util.Iterator;
import java.util.Stack;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.inventory.ItemContainer;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMovingObject;
import zombie.iso.objects.IsoStove;
import zombie.ui.*;

// Referenced classes of package zombie.Quests:
//            QuestTask, QuestTaskType

public class QuestTask_ArbitaryAction extends QuestTask
{

    public QuestTask_ArbitaryAction(String InternalName, String TaskNameString, String ArbAction)
    {
        super(QuestTaskType.Custom, InternalName, TaskNameString);
        this.ArbAction = ArbAction;
    }

    public void Update()
    {
        if(!Complete && ArbActionCheck())
        {
            Complete = true;
            if(UIManager.getOnscreenQuest() != null)
                UIManager.getOnscreenQuest().TriggerQuestWiggle();
        }
        super.Update();
    }

    private boolean ArbActionCheck()
    {
label0:
        {
            if(ArbAction.equals("barricadeTutorial"))
                return TutorialManager.instance.BarricadeCount >= 7;
            if(!ArbAction.equals("spotzombie"))
                break label0;
            Iterator i$ = IsoPlayer.getInstance().getSpottedList().iterator();
            IsoMovingObject obj;
            do
            {
                if(!i$.hasNext())
                    break label0;
                obj = (IsoMovingObject)i$.next();
            } while(!(obj instanceof IsoZombie) || obj.alpha <= 0.5F);
            return true;
        }
        if(ArbAction.equals("killzombie") && IsoPlayer.getInstance().getZombieKills() > IsoPlayer.getInstance().getLastZombieKills())
            return true;
        if(ArbAction.equals("PlayerOutside") && IsoPlayer.getInstance().getCurrentSquare().getRoom() == null)
            return true;
        return ArbAction.equals("tutSoupInStove") && TutorialManager.instance.tutorialStove.container.contains("PotOfSoup") && TutorialManager.instance.tutorialStove.Activated();
    }

    String ArbAction;
}
