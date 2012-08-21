// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EnduranceWidget.java

package zombie.ui;

import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Color;
import zombie.core.textures.Texture;
import zombie.core.textures.TexturePackPage;

// Referenced classes of package zombie.ui:
//            UIElement, StatBar

public class EnduranceWidget extends UIElement
{

    public EnduranceWidget(int x, int y)
    {
        this.x = x;
        this.y = y;
        run = TexturePackPage.getTexture("Endurance_Run");
        endurance = new StatBar(0, 0, TexturePackPage.getTexture("EnduranceBar_Border"), TexturePackPage.getTexture("EnduranceBar_Fill"), true, Color.red);
        col = Color.green;
        AddChild(endurance);
        update();
    }

    public void render()
    {
        DrawTextureScaledCol(run, 0, 0, run.getWidth(), run.getHeight(), col);
        super.render();
    }

    public void update()
    {
        super.update();
        float endurance = IsoPlayer.getInstance().getStats().endurance;
        if(endurance > IsoPlayer.getInstance().getStats().endurancewarn)
            col = new Color(0.3F, 0.7F, 0.1F);
        else
        if(endurance > IsoPlayer.getInstance().getStats().endurancedanger)
            col = new Color(0.9F, 0.5F, 0.1F);
        else
            col = Color.red;
        this.endurance.col = col;
        this.endurance.setValue(endurance);
    }

    Color col;
    StatBar endurance;
    Texture run;
}
