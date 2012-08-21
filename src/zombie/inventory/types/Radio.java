// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Radio.java

package zombie.inventory.types;

import zombie.GameTime;
import zombie.IndieGL;
import zombie.characters.IsoPlayer;
import zombie.characters.Talker;
import zombie.core.Color;
import zombie.core.Rand;
import zombie.interfaces.IUpdater;
import zombie.ui.*;

// Referenced classes of package zombie.inventory.types:
//            DrainableComboItem

public class Radio extends DrainableComboItem
    implements Talker, IUpdater
{

    public Radio(String module, String name, String itemType, String texName)
    {
        super(module, name, itemType, texName);
        lastAd = -1;
        OneWay = true;
        playedAdverts = 2;
        SpeakTime = 0;
        timeSinceLastBroadcast = 10000;
        Speaking = false;
        SpeakColour = new Color(255, 255, 255, 255);
    }

    public boolean IsSpeaking()
    {
        return Speaking;
    }

    public void render()
    {
        if(getUsedDelta() <= 0.0F)
            return;
        else
            return;
    }

    public void renderlast()
    {
        if(getUsedDelta() <= 0.0F)
            return;
        boolean bLeft = false;
        if(IsoPlayer.getInstance().getPrimaryHandItem() == this)
            bLeft = true;
        else
        if(IsoPlayer.getInstance().getSecondaryHandItem() == this)
            bLeft = false;
        else
            return;
        if(Speaking)
        {
            int y = 20;
            if(!bLeft)
                y += 40;
            if(sayLine != null)
            {
                IndieGL.End();
                TextManager.instance.DrawString(UIFont.Small, (int)UIManager.getSidebar().getParent().getX() + UIManager.getSidebar().getWidth() + 3, y, sayLine, SpeakColour.r, SpeakColour.g, SpeakColour.b, 1.0F);
                IndieGL.Begin();
            }
        }
    }

    public void Say(String line)
    {
        SpeakTime = line.length() * 3;
        if(SpeakTime < 65)
            SpeakTime = 65;
        sayLine = line;
        Speaking = true;
        timeSinceLastBroadcast = 0;
    }

    public void update()
    {
        if(timeSinceLastBroadcast > 150)
        {
            StartRadioBroadcast();
            timeSinceLastBroadcast = 0;
        }
        if(!Speaking)
            timeSinceLastBroadcast++;
        SpeakTime--;
        if(SpeakTime <= 0)
            Speaking = false;
        super.update();
    }

    private void StartRadioBroadcast()
    {
        if(playedAdverts < numAdverts)
        {
            Integer day;
            for(day = Integer.valueOf(Rand.Next(3)); lastAd == day.intValue(); day = Integer.valueOf(Rand.Next(3)));
            lastAd = day.intValue();
            playedAdverts++;
        } else
        {
            Integer day = Integer.valueOf(GameTime.getInstance().getNightsSurvived());
            playedAdverts = 0;
        }
    }

    public String getTalkerType()
    {
        return "radio";
    }

    public static int numAdverts = 2;
    public int lastAd;
    public boolean OneWay;
    public int playedAdverts;
    public String sayLine;
    public Color SpeakColour;
    public int SpeakTime;
    public int timeSinceLastBroadcast;
    boolean Speaking;

}
