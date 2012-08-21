// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SpeedControls.java

package zombie.ui;

import zombie.GameTime;
import zombie.core.Core;

// Referenced classes of package zombie.ui:
//            UIElement, HUDButton

public class SpeedControls extends UIElement
{

    public SpeedControls()
    {
        clientH = 0;
        clientW = 0;
        Movable = false;
        ncclientH = 0;
        ncclientW = 0;
        DistFromRightEdge = 126F;
        ButtonWidth = 22;
        CurrentSpeed = 1;
        SpeedBeforePause = 1;
        MultiBeforePause = 1.0F;
        alpha = 1.0F;
        MouseOver = false;
        x = (float)Core.getInstance().getOffscreenWidth() - DistFromRightEdge;
        y = 45F;
        width = (int)DistFromRightEdge - 10;
        height = 24;
        Pause = new HUDButton("Pause", ButtonWidth * 0, 0, "media/ui/Time_Pause_Off.png", "media/ui/Time_Pause_On.png", this);
        Play = new HUDButton("Play", ButtonWidth * 1 + 3, 0, "media/ui/Time_Play_Off.png", "media/ui/Time_Play_On.png", this);
        FastForward = new HUDButton("Fast Forward x 1", ButtonWidth * 2 - 1, 0, "media/ui/Time_FFwd1_Off.png", "media/ui/Time_FFwd1_On.png", this);
        FasterForward = new HUDButton("Fast Forward x 2", ButtonWidth * 3, 0, "media/ui/Time_FFwd2_Off.png", "media/ui/Time_FFwd2_On.png", this);
        Wait = new HUDButton("Wait", ButtonWidth * 4, 0, "media/ui/Time_Wait_Off.png", "media/ui/Time_Wait_On.png", this);
        AddChild(Pause);
        AddChild(Play);
        AddChild(FastForward);
        AddChild(FasterForward);
        AddChild(Wait);
        clientW = width;
        clientH = height;
    }

    public void ButtonClicked(String name)
    {
        GameTime.instance.setMultiplier(1.0F);
        if(name == "Pause")
            if(CurrentSpeed > 0)
                SetCurrentGameSpeed(0);
            else
                SetCurrentGameSpeed(4);
        if(name == "Play")
        {
            SetCurrentGameSpeed(1);
            GameTime.instance.setMultiplier(1.0F);
        }
        if(name == "Fast Forward x 1")
        {
            SetCurrentGameSpeed(1);
            GameTime.instance.setMultiplier(5F);
        }
        if(name == "Fast Forward x 2")
        {
            SetCurrentGameSpeed(1);
            GameTime.instance.setMultiplier(20F);
        }
        if(name == "Wait")
        {
            SetCurrentGameSpeed(1);
            GameTime.instance.setMultiplier(40F);
        }
    }

    public int getCurrentGameSpeed()
    {
        return CurrentSpeed;
    }

    public void SetCorrectIconStates()
    {
        if(CurrentSpeed == 0)
            super.ButtonClicked("Pause");
        if(CurrentSpeed == 1)
            super.ButtonClicked("Play");
        if(GameTime.instance.getTrueMultiplier() == 5F)
            super.ButtonClicked("Fast Forward x 1");
        if(GameTime.instance.getTrueMultiplier() == 20F)
            super.ButtonClicked("Fast Forward x 2");
        if(GameTime.instance.getTrueMultiplier() == 40F)
            super.ButtonClicked("Wait");
    }

    public void SetCurrentGameSpeed(int NewSpeed)
    {
        GameTime.instance.setMultiplier(1.0F);
        if(NewSpeed == 0)
        {
            SpeedBeforePause = CurrentSpeed;
            MultiBeforePause = GameTime.instance.getMultiplier();
        }
        if(NewSpeed == 4)
        {
            NewSpeed = SpeedBeforePause;
            GameTime.instance.setMultiplier(MultiBeforePause);
        }
        CurrentSpeed = NewSpeed;
        SetCorrectIconStates();
    }

    public boolean onMouseMove(int dx, int dy)
    {
        if(!isVisible())
        {
            return false;
        } else
        {
            MouseOver = true;
            super.onMouseMove(dx, dy);
            SetCorrectIconStates();
            return true;
        }
    }

    public void onMouseMoveOutside(int dx, int dy)
    {
        super.onMouseMoveOutside(dx, dy);
        MouseOver = false;
        SetCorrectIconStates();
    }

    public void render()
    {
        super.render();
        SetCorrectIconStates();
    }

    public void update()
    {
        super.update();
        SetCorrectIconStates();
        setX((float)Core.getInstance().getOffscreenWidth() - DistFromRightEdge);
    }

    public static SpeedControls instance = null;
    public int clientH;
    public int clientW;
    public boolean Movable;
    public int ncclientH;
    public int ncclientW;
    public float DistFromRightEdge;
    public int ButtonWidth;
    public int CurrentSpeed;
    public int SpeedBeforePause;
    public float MultiBeforePause;
    float alpha;
    boolean MouseOver;
    public static HUDButton Play;
    public static HUDButton Pause;
    public static HUDButton FastForward;
    public static HUDButton FasterForward;
    public static HUDButton Wait;

}
