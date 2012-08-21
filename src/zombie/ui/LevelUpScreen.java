// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LevelUpScreen.java

package zombie.ui;

import java.util.ArrayList;
import zombie.characters.IsoPlayer;
import zombie.characters.skills.PerkFactory;
import zombie.core.Collections.NulledArrayList;

// Referenced classes of package zombie.ui:
//            NewWindow, LevelUpCategory, DialogButton, UIEventHandler, 
//            UIManager, SpeedControls, UIFont

public class LevelUpScreen extends NewWindow
    implements UIEventHandler
{

    public LevelUpScreen(int x, int y)
    {
        super(x, y, 10, 10, true);
        cats = new ArrayList();
        bDirty = true;
        IgnoreLossControl = true;
        ResizeToFitY = false;
        width = 820;
        height = 600;
        int ty = 40;
        for(int n = 0; n < PerkFactory.PerkList.size(); n++)
        {
            zombie.characters.skills.PerkFactory.Perk perk = (zombie.characters.skills.PerkFactory.Perk)PerkFactory.PerkList.get(n);
            if(perk.parent == zombie.characters.skills.PerkFactory.Perks.None)
            {
                LevelUpCategory l = new LevelUpCategory(perk.type);
                l.x = 10F;
                l.y = ty;
                AddChild(l);
                ty += 80;
                cats.add(l);
            }
        }

        height = ty + 8 + 24;
        but = new DialogButton((UIElement)this, getWidth() - 30, getHeight() - 24, "Done", "done");
        AddChild(but);
    }

    public void ButtonClicked(String name)
    {
        if(name.equals("done") || name.equals("close"))
            if(!IsoPlayer.instance.getCanUpgradePerk().isEmpty() && IsoPlayer.getInstance().getNumberOfPerksToPick() > 0)
            {
                UIManager.DoModal("close", "Are you sure? You still have skills points available.", true, this);
            } else
            {
                setVisible(false);
                UIManager.getSpeedControls().SetCurrentGameSpeed(3);
            }
    }

    public void init()
    {
    }

    public void update()
    {
        if(bDirty && visible)
            reset();
        super.update();
    }

    public void render()
    {
        super.render();
        if(isVisible())
            DrawText(UIFont.Small, (new StringBuilder()).append("Skills to pick: ").append(IsoPlayer.getInstance().getNumberOfPerksToPick()).toString(), 10, 23, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void DoubleClick(String s, int i, int j)
    {
    }

    public void Selected(String s, int i, int j)
    {
    }

    public void reset()
    {
        if(!visible)
        {
            bDirty = true;
            return;
        }
        if(!bDirty)
            return;
        for(int n = 0; n < cats.size(); n++)
            ((LevelUpCategory)cats.get(n)).reset();

        bDirty = false;
    }

    public void ModalClick(String name, String chosen)
    {
        if(name.equals("close") && chosen.equals("Yes"))
        {
            setVisible(false);
            UIManager.getSpeedControls().SetCurrentGameSpeed(3);
        }
    }

    ArrayList cats;
    DialogButton but;
    boolean bDirty;
}
