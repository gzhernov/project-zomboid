// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QuestPanel.java

package zombie.ui;

import java.util.Iterator;
import java.util.Stack;
import zombie.Quests.*;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.textures.Texture;
import zombie.input.Mouse;

// Referenced classes of package zombie.ui:
//            NewWindow, UITextBox2, ScrollBar, UIEventHandler, 
//            UIFont, UIManager, QuestHUD

public class QuestPanel extends NewWindow
    implements UIEventHandler
{

    public QuestPanel(int x, int y)
    {
        super(x, y, 10, 10, true);
        QuestScrollbar = null;
        QuestTextBox = null;
        MaxQuestIcons = 1000;
        QuestIcons = new int[MaxQuestIcons];
        NumUsedIconSlots = 0;
        MouseOverX = 0;
        MouseOverY = 0;
        TempText = null;
        ActiveQuest = null;
        MouseOverQuest = null;
        int cx = 6;
        int px = 37;
        ResizeToFitY = false;
        visible = false;
        instance = this;
        width = 340;
        height = 400;
        Movable = true;
        MouseOverX = 0;
        MouseOverY = 0;
        QuestTextBox = new UITextBox2(UIFont.Small, 4, 21, getWidth() - 20, getHeight() - 26, "Quest Text", true);
        QuestScrollbar = new ScrollBar("QuestScrollBar", this, getWidth() - 15, 21, getHeight() - 26, true);
        QuestScrollbar.SetParentTextBox(QuestTextBox);
        AddChild(QuestTextBox);
        AddChild(QuestScrollbar);
    }

    public void render()
    {
        if(!isVisible())
            return;
        super.render();
        DrawTextCentre("Quests", getWidth() / 2, 2, 1.0F, 1.0F, 1.0F, 1.0F);
        if(QuestTextBox.Lines != null)
        {
            int IconSlot = QuestTextBox.TopLineIndex;
            for(int place = 0; IconSlot < MaxQuestIcons && IconSlot < QuestTextBox.Lines.size() && place < QuestTextBox.NumVisibleLines; IconSlot++)
            {
                Texture IconTex = null;
                if(QuestIcons[IconSlot] != 0)
                    switch(QuestIcons[IconSlot])
                    {
                    case 1: // '\001'
                        IconTex = Texture.getSharedTexture("media/ui/Quest_Succeed.png");
                        break;

                    case 2: // '\002'
                        IconTex = Texture.getSharedTexture("media/ui/Quest_Failed.png");
                        break;

                    case 3: // '\003'
                        IconTex = Texture.getSharedTexture("media/ui/Quest_Bullet.png");
                        break;
                    }
                if(IconTex != null)
                    DrawTextureCol(IconTex, 8, 28 + place * 14, Color.white);
                place++;
            }

        }
    }

    public void update()
    {
    	/* 109 */     if (!isVisible()) {
    		/* 110 */       return;
    		/*     */     }
    		/* 112 */     super.update();
    		/*     */ 
    		/* 114 */     float absY = getAbsoluteY();
    		/* 115 */     float dif = absY - 40.0F;
    		/* 116 */     float val = Core.getInstance().getOffscreenHeight() - absY;
    		/*     */ 
    		/* 118 */     if (val > 0.0F)
    		/* 119 */       dif /= val;
    		/*     */     else {
    		/* 121 */       dif = 1.0F;
    		/*     */     }
    		/* 123 */     dif *= 4.0F;
    		/*     */ 
    		/* 125 */     dif = 1.0F - dif;
    		/*     */ 
    		/* 127 */     if (dif < 0.0F) {
    		/* 128 */       dif = 0.0F;
    		/*     */     }
    		/* 130 */     String quest = "";
    		/* 131 */     String task = "";
    		/* 132 */     int ItemsAdded = 0;
    		/* 133 */     int IconSlot = 0;
    		/* 134 */     this.NumUsedIconSlots = 0;
    		/*     */ 
    		/* 137 */     for (int i = 0; i < this.MaxQuestIcons; i++) {
    		/* 138 */       this.QuestIcons[i] = 0;
    		/*     */     }
    		/* 140 */     this.MouseOverQuest = null;
    		/*     */ 
    		/* 142 */     this.QuestTextBox.ClearHighlights();
    		/*     */ 
    		/* 144 */     if (QuestManager.instance.QuestStack != null) {
    		/* 145 */       for (Quest q : QuestManager.instance.QuestStack)
    		/*     */       {
    		/* 147 */         if ((q != null) && 
    		/* 148 */           (q.Unlocked))
    		/*     */         {
    		/* 151 */           if (q.Complete) {
    		/* 152 */             this.QuestIcons[IconSlot] = 1;
    		/*     */           }
    		/* 154 */           else if (q.Failed) {
    		/* 155 */             this.QuestIcons[IconSlot] = 2;
    		/*     */           }
    		/* 157 */           if ((q.Complete) || (q.Failed))
    		/*     */           {
    		/* 159 */             if (ItemsAdded == 0)
    		/*     */             {
    		/* 161 */               this.TempText = ("    " + q.getName() + ".\n");
    		/* 162 */               IconSlot++;
    		/*     */             }
    		/*     */             else
    		/*     */             {
    		/* 166 */               this.TempText = (this.TempText + "    " + q.getName() + ".\n");
    		/* 167 */               IconSlot++;
    		/*     */             }
    		/*     */           }
    		/*     */           else
    		/*     */           {
    		/* 172 */             if (q == this.ActiveQuest)
    		/*     */             {
    		/* 175 */               this.QuestTextBox.HighlightLines[IconSlot] = 1;
    		/*     */             }
    		/*     */ 
    		/* 178 */             if (ItemsAdded == 0)
    		/*     */             {
    		/* 180 */               this.TempText = (q.getName() + ".\n");
    		/* 181 */               IconSlot++;
    		/*     */             }
    		/*     */             else
    		/*     */             {
    		/* 185 */               this.TempText = (this.TempText + q.getName() + ".\n");
    		/* 186 */               IconSlot++;
    		/*     */             }
    		/*     */           }
    		/* 189 */           ItemsAdded++;
    		/*     */ 
    		/* 192 */           if ((this.ActiveQuest != q) && 
    		/* 193 */             (!q.Complete) && (!q.Failed))
    		/*     */           {
    		/* 195 */             int TestSlot = IconSlot - 1 - this.QuestTextBox.TopLineIndex;
    		/*     */ 
    		/* 197 */             int YPos = 28 + TestSlot * 14;
    		/*     */ 
    		/* 199 */             if ((this.MouseOverX > 10) && (this.MouseOverX < 276))
    		/*     */             {
    		/* 201 */               if ((this.MouseOverY >= YPos - 6) && (this.MouseOverY <= YPos + 19))
    		/*     */               {
    		/* 203 */                 this.MouseOverQuest = q;
    		/*     */ 
    		/* 206 */                 this.QuestTextBox.HighlightLines[(IconSlot - 1)] = 2;
    		/*     */               }
    		/*     */             }
    		/*     */ 
    		/*     */           }
    		/*     */ 
    		/* 212 */           if ((this.ActiveQuest == q) && 
    		/* 213 */             (!q.Complete) && (!q.Failed)) {
    		/* 214 */             for (QuestTask t : q.QuestTaskStack)
    		/*     */             {
    		/* 216 */               if ((t.Unlocked) && (!t.Hidden))
    		/*     */               {
    		/* 219 */                 if (t.Complete) {
    		/* 220 */                   this.QuestIcons[IconSlot] = 1;
    		/*     */                 }
    		/* 222 */                 else if (t.Failed) {
    		/* 223 */                   this.QuestIcons[IconSlot] = 2;
    		/*     */                 }
    		/*     */                 else {
    		/* 226 */                   this.QuestIcons[IconSlot] = 3;
    		/*     */                 }
    		/* 228 */                 this.TempText = (this.TempText + "   " + t.getName() + ".\n");
    		/*     */ 
    		/* 231 */                 this.QuestTextBox.HighlightLines[IconSlot] = 1;
    		/*     */ 
    		/* 233 */                 IconSlot++;
    		/*     */               }
    		/*     */             }
    		/*     */           }
    		/* 237 */           this.TempText += "    \n";
    		/* 238 */           IconSlot++;
    		/*     */         }
    		/*     */       }
    		/*     */     }
    		/* 242 */     this.NumUsedIconSlots = IconSlot;
    		/*     */ 
    		/* 244 */     if (this.TempText != null) {
    		/* 245 */       this.QuestTextBox.SetText(this.TempText);
    		/*     */     }
    		/*     */ 
    		/* 248 */     if (this.ActiveQuest != null)
    		/*     */     {
    		/* 250 */       if ((this.ActiveQuest.Complete) || (this.ActiveQuest.Failed))
    		/*     */       {
    		/* 252 */         this.ActiveQuest = null;
    		/*     */ 
    		/* 255 */         if (UIManager.getOnscreenQuest() != null)
    		/* 256 */           UIManager.getOnscreenQuest().TriggerQuestWiggle();
    		/*     */       }
    		/*     */     }
    }

    public void DoubleClick(String s, int i, int j)
    {
    }

    public void Selected(String s, int i, int j)
    {
    }

    public void SetActiveQuest(Quest q)
    {
        ActiveQuest = q;
    }

    public boolean onMouseDown(int x, int y)
    {
        if(!isVisible())
            return false;
        super.onMouseDown(x, y);
        if(MouseOverQuest != null)
        {
            ActiveQuest = MouseOverQuest;
            if(UIManager.getOnscreenQuest() != null)
                UIManager.getOnscreenQuest().TriggerQuestWiggle();
        }
        return true;
    }

    public boolean onMouseMove(int dx, int dy)
    {
        if(!isVisible())
        {
            return false;
        } else
        {
            super.onMouseMove(dx, dy);
            MouseOverX = Mouse.getXA() - getAbsoluteX();
            MouseOverY = Mouse.getYA() - getAbsoluteY();
            return false;
        }
    }

    public void onMouseMoveOutside(int dx, int dy)
    {
        super.onMouseMoveOutside(dx, dy);
        MouseOverX = 0;
        MouseOverY = 0;
        MouseOverQuest = null;
    }

    public void ModalClick(String name, String chosen)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static QuestPanel instance;
    ScrollBar QuestScrollbar;
    UITextBox2 QuestTextBox;
    private int MaxQuestIcons;
    int QuestIcons[];
    int NumUsedIconSlots;
    int MouseOverX;
    int MouseOverY;
    String TempText;
    public Quest ActiveQuest;
    Quest MouseOverQuest;
}
