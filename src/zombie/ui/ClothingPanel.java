// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ClothingPanel.java

package zombie.ui;

import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.textures.Texture;

// Referenced classes of package zombie.ui:
//            NewWindow, VirtualItemSlot, Sidebar, HUDButton

public class ClothingPanel extends NewWindow
{

    public ClothingPanel(int x, int y, IsoGameCharacter ParentCharacter)
    {
        super(x, y, 10, 10, true);
        DisableHands = true;
        HeadItem = null;
        TorsoItem = null;
        HandsItem = null;
        LegsItem = null;
        FeetItem = null;
        if(DisableHands)
        {
            HeadItem = new VirtualItemSlot("Head_Clothing", 25, 28, "media/ui/ClothingIcons_Head.png", IsoPlayer.getInstance());
            TorsoItem = new VirtualItemSlot("Torso_Clothing", 25, 71, "media/ui/ClothingIcons_Torso.png", IsoPlayer.getInstance());
            HandsItem = new VirtualItemSlot("Hands_Clothing", 25, 0xdfa8ad1, "media/ui/ClothingIcons_Torso.png", IsoPlayer.getInstance());
            LegsItem = new VirtualItemSlot("Legs_Clothing", 25, 114, "media/ui/ClothingIcons_Legs.png", IsoPlayer.getInstance());
            FeetItem = new VirtualItemSlot("Feet_Clothing", 25, 157, "media/ui/ClothingIcons_Feet.png", IsoPlayer.getInstance());
        } else
        {
            HeadItem = new VirtualItemSlot("Head_Clothing", 25, 28, "media/ui/ClothingIcons_Head.png", IsoPlayer.getInstance());
            TorsoItem = new VirtualItemSlot("Torso_Clothing", 25, 71, "media/ui/ClothingIcons_Torso.png", IsoPlayer.getInstance());
            HandsItem = new VirtualItemSlot("Hands_Clothing", 25, 114, "media/ui/ClothingIcons_Torso.png", IsoPlayer.getInstance());
            LegsItem = new VirtualItemSlot("Legs_Clothing", 25, 157, "media/ui/ClothingIcons_Legs.png", IsoPlayer.getInstance());
            FeetItem = new VirtualItemSlot("Feet_Clothing", 25, 200, "media/ui/ClothingIcons_Feet.png", IsoPlayer.getInstance());
        }
        ParentChar = ParentCharacter;
        ResizeToFitY = false;
        visible = false;
        instance = this;
        width = 82;
        if(DisableHands)
            height = 170 + titleRight.getHeight() + 5;
        else
            height = 210 + titleRight.getHeight() + 5;
        int cx = 6;
        int px = 37;
        AddChild(HeadItem);
        AddChild(TorsoItem);
        AddChild(HandsItem);
        AddChild(LegsItem);
        AddChild(FeetItem);
    }

    public void render()
    {
        if(!isVisible())
        {
            return;
        } else
        {
            super.render();
            DrawTextCentre("Clothing", 40, 2, 1.0F, 1.0F, 1.0F, 1.0F);
            return;
        }
    }

    public void update()
    {
        if(!isVisible())
            return;
        super.update();
        float absY = getAbsoluteY();
        Sidebar _tmp = Sidebar.instance;
        float dif = absY - (Sidebar.Clothing.getY() - 70F);
        float val = (float)Core.getInstance().getOffscreenHeight() - absY;
        if(val > 0.0F)
            dif /= val;
        else
            dif = 1.0F;
        dif *= 4F;
        dif = 1.0F - dif;
        if(dif < 0.0F)
            dif = 0.0F;
    }

    boolean DisableHands;
    public static ClothingPanel instance;
    VirtualItemSlot HeadItem;
    VirtualItemSlot TorsoItem;
    VirtualItemSlot HandsItem;
    VirtualItemSlot LegsItem;
    VirtualItemSlot FeetItem;
    IsoGameCharacter ParentChar;
}
