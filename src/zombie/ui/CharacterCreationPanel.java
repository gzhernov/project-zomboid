// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CharacterCreationPanel.java

package zombie.ui;

import zombie.core.Core;
import zombie.scripting.ScriptManager;

// Referenced classes of package zombie.ui:
//            NewWindow

public class CharacterCreationPanel extends NewWindow
{

    public CharacterCreationPanel(int x, int y)
    {
        super(x, y, 10, 10, false);
        ResizeToFitY = false;
        visible = false;
        instance = this;
        width = 750;
        height = 570;
        Movable = false;
        int cx = 6;
        int px = 37;
    }

    public void enter()
    {
        ScriptManager.instance.Trigger("OnPreCharacterCreation");
    }

    public void render()
    {
        if(!isVisible())
        {
            return;
        } else
        {
            super.render();
            DrawTextCentre("Create your character", getWidth() / 2, 2, 1.0F, 1.0F, 1.0F, 1.0F);
            DrawTextCentre("Viewer", 430, 33, 1.0F, 1.0F, 1.0F, 1.0F);
            DrawText("Forename", 532, 31, 1.0F, 1.0F, 1.0F, 1.0F);
            DrawText("Surname", 532, 72, 1.0F, 1.0F, 1.0F, 1.0F);
            DrawTextRight("Face", 624, 133, 1.0F, 1.0F, 1.0F, 1.0F);
            DrawTextRight("Skin-tone", 624, 150, 1.0F, 1.0F, 1.0F, 1.0F);
            DrawText("Select a profession (many more to come in future updates)", 30, 194, 1.0F, 1.0F, 1.0F, 1.0F);
            DrawTextCentre("Available traits", 438, 194, 1.0F, 1.0F, 1.0F, 1.0F);
            DrawTextCentre("Chosen traits", 626, 194, 1.0F, 1.0F, 1.0F, 1.0F);
            return;
        }
    }

    public void update()
    {
        if(!isVisible())
            return;
        super.update();
        float absY = getAbsoluteY();
        float dif = absY - 40F;
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

    public static CharacterCreationPanel instance;
}
