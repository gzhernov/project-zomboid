// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TextManager.java

package zombie.ui;

import java.io.FileNotFoundException;
import zombie.core.Color;
import zombie.core.fonts.AngelCodeFont;

// Referenced classes of package zombie.ui:
//            UIFont

public class TextManager
{

    public TextManager()
    {
    }

    public void DrawString(int x, int y, String str)
    {
        font.drawString(x, y, str, new Color(255, 255, 255, 255));
    }

    public void DrawString(int x, int y, String str, float r, float g, float b, float a)
    {
        font.drawString(x, y, str, new Color(r, g, b, a));
    }

    public void DrawString(UIFont font, int x, int y, String str, float r, float g, float b, 
            float a)
    {
        AngelCodeFont toUse = this.font;
        if(font == UIFont.Medium)
            toUse = font2;
        if(font == UIFont.Massive)
            toUse = font4;
        if(font == UIFont.Large)
            toUse = font3;
        if(font == UIFont.MainMenu1)
            toUse = main1;
        if(font == UIFont.MainMenu2)
            toUse = main2;
        toUse.drawString(x, y, str, new Color(r, g, b, a));
    }

    public void DrawStringCentre(int x, int y, String str, float r, float g, float b, float a)
    {
        x -= font.getWidth(str) / 2;
        font.drawString(x, y, str, new Color(r, g, b, a));
    }

    public void DrawStringCentre(UIFont font, int x, int y, String str, float r, float g, float b, 
            float a)
    {
        AngelCodeFont toUse = this.font;
        if(font == UIFont.Medium)
            toUse = font2;
        if(font == UIFont.Large)
            toUse = font3;
        if(font == UIFont.Massive)
            toUse = font4;
        if(font == UIFont.MainMenu1)
            toUse = main1;
        if(font == UIFont.MainMenu2)
            toUse = main2;
        x -= toUse.getWidth(str) / 2;
        toUse.drawString(x, y, str, new Color(r, g, b, a));
    }

    public void DrawStringRight(int x, int y, String str, float r, float g, float b, float a)
    {
        x -= font.getWidth(str);
        font.drawString(x, y, str, new Color(r, g, b, a));
    }

    public void DrawStringRight(UIFont font, int x, int y, String str, float r, float g, float b, 
            float a)
    {
        AngelCodeFont toUse = this.font;
        if(font == UIFont.Medium)
            toUse = font2;
        if(font == UIFont.Large)
            toUse = font3;
        if(font == UIFont.Massive)
            toUse = font4;
        if(font == UIFont.MainMenu1)
            toUse = main1;
        if(font == UIFont.MainMenu2)
            toUse = main2;
        x -= toUse.getWidth(str);
        toUse.drawString(x, y, str, new Color(r, g, b, a));
    }

    public void Init()
        throws FileNotFoundException
    {
        font = new AngelCodeFont("media/zombiefont2.fnt", "zombiefont2_0");
        font2 = new AngelCodeFont("media/zombiefont3a.fnt", "zombiefont3a_0");
        font3 = new AngelCodeFont("media/zombiefont3.fnt", "zombiefont3_0");
        font4 = new AngelCodeFont("media/zombiefont5.fnt", "zombiefont5_0");
        main1 = new AngelCodeFont("media/mainfont.fnt", "mainfont_0");
        main2 = new AngelCodeFont("media/mainfont2.fnt", "mainfont2_0");
    }

    public int MeasureStringX(UIFont font, String str)
    {
        if(font == UIFont.Small)
            return this.font.getWidth(str);
        if(font == UIFont.Medium)
            return font2.getWidth(str);
        if(font == UIFont.Large)
            return font3.getWidth(str);
        if(font == UIFont.Massive)
            return font4.getWidth(str);
        if(font == UIFont.MainMenu1)
            return instance.main1.getWidth(str);
        if(font == UIFont.MainMenu2)
            return instance.main2.getWidth(str);
        else
            return 0;
    }

    public int MeasureStringY(UIFont font, String str)
    {
        if(font == UIFont.Small)
            return this.font.getHeight(str);
        if(font == UIFont.Medium)
            return font2.getHeight(str);
        if(font == UIFont.Large)
            return font3.getHeight(str);
        if(font == UIFont.Massive)
            return font4.getHeight(str);
        if(font == UIFont.MainMenu1)
            return instance.main1.getHeight(str);
        if(font == UIFont.MainMenu2)
            return instance.main2.getHeight(str);
        else
            return 0;
    }

    public AngelCodeFont font;
    public AngelCodeFont font2;
    public AngelCodeFont font3;
    public AngelCodeFont font4;
    public AngelCodeFont main1;
    public AngelCodeFont main2;
    public static TextManager instance = new TextManager();

}
