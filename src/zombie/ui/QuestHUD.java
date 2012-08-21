// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QuestHUD.java

package zombie.ui;

import java.util.Iterator;
import java.util.Stack;
import zombie.Quests.*;
import zombie.core.Core;

// Referenced classes of package zombie.ui:
//            UIElement, DialogButton, UIEventHandler, QuestPanel, 
//            UIManager, UIFont

public class QuestHUD extends UIElement
    implements UIEventHandler
{

    public QuestHUD()
    {
        FirstQuestSet = false;
        QuestOscilationLevel = 0.0F;
        QuestOscilator = 0.0F;
        QuestOscilatorDecelerator = 0.93F;
        QuestOscilatorRate = 0.8F;
        QuestOscilatorScalar = 15.6F;
        QuestOscilatorStartLevel = 1.0F;
        QuestOscilatorStep = 0.0F;
        QuestDefaultXOffset = 0.0F;
        QuestPanelButton = null;
        QuestPanelButton = new DialogButton((UIElement)this, 222, 40, "Quest Manager", "Quest Manager");
        AddChild(QuestPanelButton);
        FirstQuestSet = false;
    }

    public void TriggerQuestWiggle()
    {
        QuestOscilationLevel = QuestOscilatorStartLevel;
    }

    public void render()
    {
label0:
        {
            if(QuestPanelButton.clicked)
            {
                UIManager.questPanel.setVisible(!UIManager.questPanel.isVisible());
                QuestPanelButton.clicked = false;
                UIManager.questPanel.setX(Core.getInstance().getScreenWidth() - 463);
                UIManager.questPanel.setY(66F);
            }
            QuestOscilatorStep += QuestOscilatorRate;
            QuestOscilator = (float)Math.sin(QuestOscilatorStep);
            float WiggleOffset = QuestOscilator * QuestOscilatorScalar * QuestOscilationLevel;
            QuestOscilationLevel *= QuestOscilatorDecelerator;
            String quest = "";
            String task = "";
            super.render();
            Iterator i$;
            if(QuestPanel.instance.ActiveQuest == null || QuestPanel.instance.ActiveQuest.Failed || QuestPanel.instance.ActiveQuest.Complete)
            {
                QuestPanel.instance.ActiveQuest = null;
                FirstQuestSet = false;
                i$ = QuestManager.instance.QuestStack.iterator();
                do
                {
                    if(!i$.hasNext())
                        break;
                    Quest q = (Quest)i$.next();
                    if(q.Unlocked && !q.Complete && !q.Failed && !FirstQuestSet)
                    {
                        FirstQuestSet = true;
                        QuestPanel.instance.SetActiveQuest(q);
                    }
                } while(true);
                break label0;
            }
            if(QuestPanel.instance.ActiveQuest == null)
                break label0;
            i$ = QuestPanel.instance.ActiveQuest.QuestTaskStack.iterator();
            QuestTask t;
            do
            {
                if(!i$.hasNext())
                    break label0;
                t = (QuestTask)i$.next();
            } while(!t.Unlocked || t.Failed || t.Complete || t.Hidden);
            quest = QuestPanel.instance.ActiveQuest.getName();
            task = t.getName();
            DrawTextRight(UIFont.Medium, quest, getWidth(), 0, 1.0F, 1.0F, 1.0F, 1.0F);
            DrawTextRight(UIFont.Small, task, (getWidth() - 3) + (int)WiggleOffset, 19, 1.0F, 1.0F, 1.0F, 1.0F);
            return;
        }
    }

    public void DoubleClick(String s, int i, int j)
    {
    }

    public void Selected(String s, int i, int j)
    {
    }

    public void ModalClick(String name, String chosen)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private boolean FirstQuestSet;
    private float QuestOscilationLevel;
    private float QuestOscilator;
    private float QuestOscilatorDecelerator;
    private float QuestOscilatorRate;
    private float QuestOscilatorScalar;
    private float QuestOscilatorStartLevel;
    private float QuestOscilatorStep;
    private float QuestDefaultXOffset;
    DialogButton QuestPanelButton;
}
