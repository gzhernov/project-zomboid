// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QuestControl.java

package zombie.ui;

import java.util.Iterator;
import java.util.Stack;
import zombie.Quests.*;
import zombie.core.Color;
import zombie.core.textures.Texture;

// Referenced classes of package zombie.ui:
//            UIElement, TextManager, UIFont

public class QuestControl extends UIElement
{

    public QuestControl(boolean bShort)
    {
        this.bShort = false;
        IndentLines = new Stack();
        originalWidth = 0;
        Status = new Stack();
        Lines = new Stack();
        maxWidthUsed = 0;
        bullet = Texture.getSharedTexture("media/ui/Quest_Bullet.png");
        succeed = Texture.getSharedTexture("media/ui/Quest_Succeed.png");
        failed = Texture.getSharedTexture("media/ui/Quest_Failed.png");
        this.bShort = bShort;
        originalWidth = 500;
        width = 500;
    }

    public int getHeight()
    {
        int height = Lines.size() * 17;
        if(height < 50)
            return 50;
        else
            return height;
    }

    public void render()
    {
        setWidth(originalWidth);
        Paginate();
        setHeight(getHeight());
        int n = 0;
        int y = 0;
        for(Iterator i$ = Lines.iterator(); i$.hasNext();)
        {
            String line = (String)i$.next();
            Completable comp = (Completable)Status.get(n);
            int x = 0;
            if(IndentLines.contains(Integer.valueOf(n)))
                x += 16;
            if(comp != null)
            {
                Texture tex = bullet;
                if(comp.IsComplete())
                    tex = succeed;
                if(comp.IsFailed())
                    tex = failed;
                DrawTextureCol(tex, x, y + 2, Color.white);
            }
            DrawText(line, x + 16, y, 1.0F, 1.0F, 1.0F, 1.0F);
            y += 17;
            n++;
        }

    }

    private void Paginate()
    {
        maxWidthUsed = 0;
        int n = 0;
        Lines.clear();
        Status.clear();
        IndentLines.clear();
        boolean bDoneQuest = false;
        boolean bDoneTask = false;
        Iterator i$ = QuestManager.instance.QuestStack.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            Quest quest = (Quest)i$.next();
            if(quest.Unlocked && ((!bShort || !quest.Complete && !quest.Failed) && (!bShort || !bDoneQuest)))
            {
                String Text = quest.getName();
                PaginateText(Text, false, quest);
                if(!quest.Complete && !quest.Failed)
                {
                    Iterator i$1 = quest.QuestTaskStack.iterator();
                    do
                    {
                        if(!i$1.hasNext())
                            break;
                        QuestTask task = (QuestTask)i$1.next();
                        if(task.Unlocked && (!bShort || !task.Complete && !task.Failed && !task.Hidden && task.getName().length() != 0))
                        {
                            Text = task.getName();
                            PaginateText(Text, true, task);
                            bDoneTask = true;
                        }
                    } while(true);
                    bDoneQuest = true;
                }
            }
        } while(true);
        if(getParent() != null)
            getParent().setHeight(getHeight() + 32);
        setWidth(maxWidthUsed);
    }

    private void PaginateText(String text, boolean indent, Completable completable)
    {
        int width = getWidth();
        if(indent)
            width -= 20;
        boolean addedQuestStatus = false;
        int n = 0;
        do
        {
            int m = text.indexOf(" ", n + 1);
            int z = m;
            if(z == -1)
                z = text.length();
            int wid = TextManager.instance.MeasureStringX(UIFont.Small, text.substring(0, z));
            if(wid >= width)
            {
                maxWidthUsed = originalWidth;
                String sub = text.substring(0, n);
                text = text.substring(n + 1);
                Lines.add(sub);
                if(indent)
                    IndentLines.add(Integer.valueOf(Lines.size() - 1));
                if(!addedQuestStatus)
                {
                    Status.add(completable);
                    addedQuestStatus = true;
                } else
                {
                    Status.add(null);
                }
                m = 0;
            } else
            {
                if(wid > maxWidthUsed)
                    maxWidthUsed = wid + 16;
                if(m == -1)
                {
                    String sub = text;
                    Lines.add(sub);
                    if(!addedQuestStatus)
                    {
                        Status.add(completable);
                        addedQuestStatus = true;
                    } else
                    {
                        Status.add(null);
                    }
                    if(indent)
                        IndentLines.add(Integer.valueOf(Lines.size() - 1));
                    return;
                }
            }
            n = m;
        } while(text.length() > 0);
        if(indent)
            maxWidthUsed += 20;
        if(maxWidthUsed > originalWidth)
            maxWidthUsed = originalWidth;
    }

    public boolean bShort;
    public Texture bullet;
    public Texture failed;
    public Stack IndentLines;
    public int originalWidth;
    public Stack Status;
    public Texture succeed;
    Stack Lines;
    int maxWidthUsed;
}
