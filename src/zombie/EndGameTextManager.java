// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:44
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   EndGameTextManager.java

package zombie;

import java.util.Stack;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.iso.IsoCamera;
import zombie.ui.TextManager;
import zombie.ui.UIFont;

// Referenced classes of package zombie:
//            GameTime

public class EndGameTextManager
{
    public class EndGameEvent
    {

        public int Day;
        public String text;
       

        public EndGameEvent(int day, String str)
        {
           
            Day = 0;
            Day = day;
            text = str;
        }
    }


    public EndGameTextManager()
    {
        init = false;
        Story = new Stack();
        yoff = 0.0F;
    }

    public void AddEvent(int day, String string)
    {
        Story.add(new EndGameEvent(day, string));
    }

    public void update()
    {
        if(IsoPlayer.getInstance().getHealth() <= 0.0F || IsoPlayer.getInstance().getBodyDamage().getHealth() <= 0.0F || IsoCamera.CamCharacter != IsoPlayer.getInstance())
        {
            yoff -= 0.5F;
            if(!init)
                doInit();
        }
    }

    public void render()
    {
        if(!init)
            doInit();
        float y = yoff;
        if(IsoPlayer.getInstance().getHealth() <= 0.0F || IsoPlayer.getInstance().getBodyDamage().getHealth() <= 0.0F || IsoCamera.CamCharacter != IsoPlayer.getInstance())
        {
            for(int n = 0; n < Story.size(); n++)
            {
                TextManager.instance.DrawStringCentre(UIFont.Massive, Core.getInstance().getOffscreenWidth() / 2, (int)y, ((EndGameEvent)Story.get(n)).text, 1.0F, 1.0F, 1.0F, 1.0F);
                y += TextManager.instance.MeasureStringY(UIFont.Massive, ((EndGameEvent)Story.get(n)).text) + 2;
            }

        }
    }

    private void doInit()
    {
        init = true;
        Story.clear();
        yoff = Core.getInstance().getOffscreenHeight();
        Story.insertElementAt(new EndGameEvent(-1, GameTime.getInstance().getDeathString()), 0);
        int startday = 0;
        for(int n = 0; n < Story.size(); n++)
        {
            int nowday = ((EndGameEvent)Story.get(n)).Day;
            if(nowday != startday)
            {
                startday = nowday;
                Integer nn = Integer.valueOf(nowday + 1);
            }
        }

    }

    public static EndGameTextManager instance = new EndGameTextManager();
    boolean init;
    public Stack Story;
    float yoff;

}