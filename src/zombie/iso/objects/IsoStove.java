// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoStove.java

package zombie.iso.objects;

import java.util.Stack;
import zombie.GameTime;
import zombie.characters.IsoPlayer;
import zombie.core.Collections.NulledArrayList;
import zombie.inventory.ItemContainer;
import zombie.iso.*;
import zombie.iso.objects.interfaces.Activatable;
import zombie.iso.sprite.IsoSprite;
import zombie.ui.NewContainerPanel;
import zombie.ui.UIManager;

public class IsoStove extends IsoObject
    implements Activatable
{

    public IsoStove(IsoCell cell, IsoGridSquare sq, IsoSprite gid)
    {
        super(cell, sq, gid);
        activated = false;
    }

    public String getObjectName()
    {
        return "Stove";
    }

    public IsoStove(IsoCell cell)
    {
        super(cell);
        activated = false;
    }

    public boolean Activated()
    {
        return activated;
    }

    public boolean onMouseLeftClick(int x, int y)
    {
        if(IsoUtils.DistanceTo(square.getX(), square.getY(), IsoPlayer.getInstance().getX(), IsoPlayer.getInstance().getY()) < 2.0F && (float)square.getZ() == IsoPlayer.getInstance().getZ())
        {
            int sx = (int)IsoUtils.XToScreen(square.getX(), square.getY(), square.getZ(), 0);
            int sy = (int)IsoUtils.YToScreen(square.getX(), square.getY(), square.getZ(), 0);
            sx -= (int)IsoCamera.getOffX();
            sy -= (int)IsoCamera.getOffY();
            sy += 380;
            sx += 24;
            UIManager.getUI().add(new NewContainerPanel(sx, sy, 2, 3, this));
            return true;
        } else
        {
            return false;
        }
    }

    public void Toggle()
    {
        if(GameTime.instance.NightsSurvived < 30 || activated)
        {
            activated = !activated;
            for(int n = 0; n < container.Items.size(); n++)
                if(!IsoWorld.instance.CurrentCell.getProcessItems().contains(container.Items.get(n)))
                    IsoWorld.instance.CurrentCell.getProcessItems().add(container.Items.get(n));

        }
    }

    public String getActivatableType()
    {
        return "stove";
    }

    boolean activated;
}
