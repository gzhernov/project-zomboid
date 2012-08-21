// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StatsPage.java

package zombie.ui;

import java.io.FileNotFoundException;
import zombie.characters.IsoGameCharacter;
import zombie.core.Color;
import zombie.core.textures.Texture;

// Referenced classes of package zombie.ui:
//            DraggableWindow

public class StatsPage extends DraggableWindow
{

    public StatsPage(IsoGameCharacter chr)
        throws FileNotFoundException
    {
        super(100, 100, "ContainerTitlebar", "StatsPage");
        drawY = 0;
        this.chr = chr;
        width = 128;
        height = 256;
    }

    public void drawBarStat(String name, float value, boolean lowGood)
    {
        float r = value;
        float g = value;
        if(lowGood)
            g = 1.0F - g;
        else
            r = 1.0F - r;
        Color col = new Color(r, g, 0.0F);
        int x = 48;
        DrawText(name, 5, drawY, 1.0F, 1.0F, 1.0F, 1.0F);
        DrawTextureScaledCol(Texture.getSharedTexture("media/white.png"), x, drawY + 3, (int)(value * 75F), 8, col);
        drawY += 12;
    }

    public void drawBarStat(String name, float value, Color col)
    {
        int x = 48;
        DrawText(name, 5, drawY, 1.0F, 1.0F, 1.0F, 1.0F);
        DrawTextureScaledCol(Texture.getSharedTexture("media/white.png"), x, drawY + 3, (int)(value * 75F), 8, col);
        drawY += 12;
    }

    public void render()
    {
        super.render();
        if(!isVisible())
        {
            return;
        } else
        {
            drawY = 20;
            drawBarStat("Hunger", chr.getStats().hunger, true);
            drawBarStat("Fatigue", chr.getStats().fatigue, true);
            drawY += 10;
            drawBarStat("Stress", chr.getStats().stress, true);
            drawBarStat("Morale", chr.getStats().morale, false);
            return;
        }
    }

    IsoGameCharacter chr;
    int drawY;
}
