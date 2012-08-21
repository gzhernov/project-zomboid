// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NewContainerPanel.java

package zombie.ui;

import zombie.GameTime;
import zombie.core.textures.Texture;
import zombie.inventory.ItemContainer;
import zombie.iso.IsoGridSquare;
import zombie.iso.objects.IsoStove;

// Referenced classes of package zombie.ui:
//            UIElement, InventoryFlowControl, DialogButton, SpeedControls

public class NewContainerPanel extends UIElement
{

    public NewContainerPanel(int x, int y, int xtiles, int ytiles, ItemContainer container)
    {
        stove = null;
        this.x = x;
        this.y = y;
        Flow = new InventoryFlowControl(2, 8, xtiles, ytiles, container);
        width = xtiles * 32 + 4;
        height = ytiles * 32 + 16;
        followGameWorld = true;
        TL = Texture.getSharedTexture("media/ui/Container_TL.png");
        TM = Texture.getSharedTexture("media/ui/Container_TM.png");
        TR = Texture.getSharedTexture("media/ui/Container_TR.png");
        ML = Texture.getSharedTexture("media/ui/Container_ML.png");
        MM = Texture.getSharedTexture("media/ui/Container_MM.png");
        MR = Texture.getSharedTexture("media/ui/Container_MR.png");
        BL = Texture.getSharedTexture("media/ui/Container_BL.png");
        BM = Texture.getSharedTexture("media/ui/Container_BM.png");
        BR = Texture.getSharedTexture("media/ui/Container_BR.png");
        AddChild(Flow);
    }

    public NewContainerPanel(int x, int y, int xtiles, int ytiles, IsoStove stove)
    {
        this.stove = null;
        this.stove = stove;
        this.x = x;
        this.y = y;
        Flow = new InventoryFlowControl(2, 8, xtiles, ytiles, stove.container);
        width = xtiles * 32 + 4;
        height = ytiles * 32 + 16;
        followGameWorld = true;
        TL = Texture.getSharedTexture("media/ui/Container_TL.png");
        TM = Texture.getSharedTexture("media/ui/Container_TM.png");
        TR = Texture.getSharedTexture("media/ui/Container_TR.png");
        ML = Texture.getSharedTexture("media/ui/Container_ML.png");
        MM = Texture.getSharedTexture("media/ui/Container_MM.png");
        MR = Texture.getSharedTexture("media/ui/Container_MR.png");
        BL = Texture.getSharedTexture("media/ui/Container_BL.png");
        BM = Texture.getSharedTexture("media/ui/Container_BM.png");
        BR = Texture.getSharedTexture("media/ui/Container_BR.png");
        AddChild(Flow);
        boolean bOnOff = stove.Activated();
        if(bOnOff)
        {
            button = new DialogButton(this, 21, -5, "On", "On");
            AddChild(button);
        } else
        {
            button = new DialogButton(this, 21, -5, "Off", "Off");
            AddChild(button);
        }
    }

    public void ButtonClicked(String name)
    {
        if(GameTime.instance.NightsSurvived >= 30)
            return;
        if(name.equals("On"))
        {
            button.name = "Off";
            stove.Toggle();
            button.text = "Off";
        }
        if(name.equals("Off"))
        {
            button.name = "On";
            button.text = "On";
            stove.Toggle();
        }
    }

    public boolean onMouseDown(int x, int y)
    {
        if(!isVisible() || SpeedControls.instance.getCurrentGameSpeed() == 0)
        {
            return true;
        } else
        {
            super.onMouseDown(x, y);
            IsoGridSquare.setRecalcLightTime(0);
            return true;
        }
    }

    public boolean onMouseUp(int x, int y)
    {
        if(!isVisible() || SpeedControls.instance.getCurrentGameSpeed() == 0)
        {
            return true;
        } else
        {
            super.onMouseUp(x, y);
            IsoGridSquare.setRecalcLightTime(0);
            return true;
        }
    }

    public void render()
    {
        DrawTexture(TL, 0, 0, 0.8F);
        DrawTextureScaled(TM, 8, 0, getWidth() - 16, 8, 0.8F);
        DrawTexture(TR, getWidth() - 8, 0, 0.8F);
        DrawTextureScaled(ML, 0, 8, 8, getHeight() - 16, 0.8F);
        DrawTextureScaled(MM, 8, 8, getWidth() - 16, getHeight() - 16, 0.8F);
        DrawTextureScaled(MR, getWidth() - 8, 8, 8, getHeight() - 16, 0.8F);
        DrawTexture(BL, 0, getHeight() - 8, 0.8F);
        DrawTextureScaled(BM, 8, getHeight() - 8, getWidth() - 16, 8, 0.8F);
        DrawTexture(BR, getWidth() - 8, getHeight() - 8, 0.8F);
        super.render();
    }

    public Texture BL;
    public Texture BM;
    public Texture BR;
    public InventoryFlowControl Flow;
    public Texture ML;
    public Texture MM;
    public Texture MR;
    public Texture TL;
    public Texture TM;
    public Texture TR;
    DialogButton button;
    IsoStove stove;
}
