// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ModalDialog.java

package zombie.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.gameStates.IngameState;

// Referenced classes of package zombie.ui:
//            NewWindow, TextBox, DialogButton, UIFont, 
//            UIEventHandler, UIManager, SpeedControls, TutorialManager

public class ModalDialog extends NewWindow
{

    public ModalDialog(String name, String help, boolean bYesNo)
    {
        super(Core.getInstance().getOffscreenWidth() / 2, Core.getInstance().getOffscreenHeight() / 2, 470, 10, false);
        bYes = false;
        handler = null;
        Clicked = false;
        Name = name;
        ResizeToFitY = false;
        IgnoreLossControl = true;
        TextBox text = new TextBox(UIFont.Medium, 0, 0, 450, help);
        text.Centred = true;
        text.ResizeParent = true;
        text.update();
        Nest(text, 20, 10, 20, 10);
        update();
        height *= 1.3F;
        if(bYesNo)
        {
            AddChild(new DialogButton(this, getWidth() / 2 - 40, getHeight() - 18, "Yes", "Yes"));
            AddChild(new DialogButton(this, getWidth() / 2 + 40, getHeight() - 18, "No", "No"));
        } else
        {
            AddChild(new DialogButton(this, getWidth() / 2, getHeight() - 18, "Ok", "Ok"));
        }
        x -= width / 2;
        y -= height / 2;
    }

    public void ButtonClicked(String name)
    {
        if(handler != null)
        {
            handler.ModalClick(Name, name);
            setVisible(false);
            return;
        }
        if(name.equals("Ok"))
        {
            UIManager.getSpeedControls().SetCurrentGameSpeed(4);
            Clicked(name);
            Clicked = true;
            bYes = true;
            setVisible(false);
            IngameState.instance.Paused = false;
        }
        if(name.equals("Yes"))
        {
            UIManager.getSpeedControls().SetCurrentGameSpeed(4);
            Clicked(name);
            Clicked = true;
            bYes = true;
            setVisible(false);
            IngameState.instance.Paused = false;
        }
        if(name.equals("No"))
        {
            UIManager.getSpeedControls().SetCurrentGameSpeed(4);
            Clicked(name);
            Clicked = true;
            bYes = false;
            setVisible(false);
            IngameState.instance.Paused = false;
        }
    }

    public void Clicked(String name)
    {
        if(Name.equals("Sleep") && name.equals("Yes"))
        {
            float SleepHours = 12F * IsoPlayer.getInstance().getStats().fatigue;
            if(SleepHours < 7F)
                SleepHours = 7F;
            SleepHours += GameTime.getInstance().getTimeOfDay();
            if(SleepHours >= 24F)
                SleepHours -= 24F;
            IsoPlayer.getInstance().setForceWakeUpTime((int)SleepHours);
            IsoPlayer.instance.setAsleepTime(0.0F);
            TutorialManager.instance.StealControl = true;
            IsoPlayer.getInstance().setAsleep(true);
            UIManager.setbFadeBeforeUI(true);
            UIManager.FadeOut(4);
            UIManager.getSpeedControls().SetCurrentGameSpeed(3);
            try
            {
                GameWindow.save(true);
            }
            catch(FileNotFoundException ex)
            {
                Logger.getLogger(ModalDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch(IOException ex)
            {
                Logger.getLogger(ModalDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        UIManager.Modal.setVisible(false);
    }

    public boolean bYes;
    public String Name;
    UIEventHandler handler;
    public boolean Clicked;
}
