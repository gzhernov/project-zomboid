// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QuestAction_TutorialIcon.java

package zombie.Quests.questactions;

import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.ui.UIElement;
import zombie.ui.UIManager;

// Referenced classes of package zombie.Quests.questactions:
//            QuestAction

public class QuestAction_TutorialIcon
    implements QuestAction
{

    public QuestAction_TutorialIcon(String title, String message, IsoObject obj, boolean bAutoExpand, float yoff)
    {
        this.bAutoExpand = bAutoExpand;
        this.obj = obj;
        this.message = message;
        this.title = title;
        x = 0;
        y = 0;
        this.yoff = yoff;
    }

    public QuestAction_TutorialIcon(UIElement parent, int x, int y, String title, String string, boolean bAutoExpand)
    {
        this.bAutoExpand = bAutoExpand;
        message = string;
        this.title = title;
        yoff = 0.0F;
        Parent = parent;
        this.x = x;
        this.y = y;
    }

    public void Execute()
    {
        if(Parent == null)
            UIManager.AddTutorial(obj.square.getX(), obj.square.getY(), obj.square.getZ(), title, message, bAutoExpand, yoff);
        else
            UIManager.AddTutorial(Parent, x, y, title, message, bAutoExpand);
    }

    boolean bAutoExpand;
    UIElement Parent;
    private String message;
    private IsoObject obj;
    private String title;
    private final int x;
    private final int y;
    private final float yoff;
}
