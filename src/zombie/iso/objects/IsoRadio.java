// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoRadio.java

package zombie.iso.objects;

import zombie.IndieGL;
import zombie.characters.Talker;
import zombie.core.Color;
import zombie.iso.*;
import zombie.iso.sprite.IsoSprite;
import zombie.ui.*;

public class IsoRadio extends IsoObject
    implements Talker
{

    public String getObjectName()
    {
        return "Radio";
    }

    public IsoRadio(IsoCell cell)
    {
        super(cell);
        SpeakTime = 0;
        Speaking = false;
        SpeakColour = new Color(255, 255, 255, 255);
    }

    public IsoRadio(IsoCell cell, IsoGridSquare sq, IsoSprite spr)
    {
        super(cell, sq, spr);
        SpeakTime = 0;
        Speaking = false;
        SpeakColour = new Color(255, 255, 255, 255);
    }

    public boolean IsSpeaking()
    {
        return Speaking;
    }

    public void Say(String line)
    {
        SpeakTime = line.length() * 4;
        if(SpeakTime < 60)
            SpeakTime = 60;
        sayLine = line;
        if(TutorialManager.instance.ProfanityFilter)
        {
            sayLine = sayLine.replace("Fuck", "****");
            sayLine = sayLine.replace("fuck", "****");
            sayLine = sayLine.replace("Shit", "****");
            sayLine = sayLine.replace("shit", "****");
        }
        Speaking = true;
    }

    public void update()
    {
        SpeakTime--;
        if(SpeakTime <= 0)
            Speaking = false;
    }

    public void renderlast()
    {
        if(Speaking)
        {
            float sx = sprite.lsx;
            float sy = sprite.lsy;
            sx = (int)sx;
            sy = (int)sy;
            sx -= offsetX;
            sy -= offsetY;
            sx -= (int)IsoCamera.getOffX();
            sy -= (int)IsoCamera.getOffY();
            sx += 8F;
            sy += 32F;
            sy += 128F;
            if(sayLine != null)
            {
                IndieGL.End();
                TextManager.instance.DrawStringCentre(UIFont.Medium, (int)sx, (int)sy, sayLine, SpeakColour.r, SpeakColour.g, SpeakColour.b, SpeakColour.a);
            }
        }
    }

    public String getTalkerType()
    {
        return "radio";
    }

    private int SpeakTime;
    private String sayLine;
    private boolean Speaking;
    private Color SpeakColour;
}
