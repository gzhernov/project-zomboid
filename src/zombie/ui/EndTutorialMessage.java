// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EndTutorialMessage.java

package zombie.ui;

import zombie.core.Core;
import zombie.gameStates.IngameState;

// Referenced classes of package zombie.ui:
//            NewWindow, TextBox, DialogButton, UIFont

public class EndTutorialMessage extends NewWindow
{

    public EndTutorialMessage()
    {
        super(Core.getInstance().getOffscreenWidth() / 2, Core.getInstance().getOffscreenHeight() / 2, 470, 10, true);
        IgnoreLossControl = true;
        String help = "This is the end of the tutorial<br><br>Now you're on your own<br><br>Whatever happens next, remember you always have a choice<br><br>But the smart choice may not be the one you want to make";
        TextBox text = new TextBox(UIFont.Medium, 0, 0, 450, help);
        text.Centred = true;
        text.ResizeParent = true;
        text.update();
        Nest(text, 20, 10, 20, 10);
        update();
        AddChild(new DialogButton(this, getWidth() / 2, getHeight() - 18, "Ok", "Ok"));
        x -= width / 2;
        y -= height / 2;
    }

    public void ButtonClicked(String name)
    {
        if(name.equals("Ok"))
        {
            setVisible(false);
            IngameState.instance.Paused = false;
        }
    }
}
