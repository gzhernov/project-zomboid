// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EmotionPanel.java

package zombie.ui;

import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.Moodles.MoodleType;
import zombie.characters.Moodles.Moodles;
import zombie.core.textures.Texture;
import zombie.iso.IsoCamera;

// Referenced classes of package zombie.ui:
//            UIElement, HUDButton

public class EmotionPanel extends UIElement
{

    public EmotionPanel(int x, int y)
    {
        selected = 1;
        friMouseOver = null;
        a = new HUDButton("angry", 0, 0, "media/ui/Emotion_Aggressive.png", "media/ui/Emotion_Aggressive_MouseOver.png", this);
        b = new HUDButton("neutral", a.getWidth(), 0, "media/ui/Emotion_Neutral.png", "media/ui/Emotion_Neutral_MouseOver.png", this);
        c = new HUDButton("happy", a.getWidth() + b.getWidth(), 0, "media/ui/Emotion_Friendly.png", "media/ui/Emotion_Friendly_MouseOver.png", this);
        width = a.width + b.width + c.width;
        a.notclickedAlpha = b.notclickedAlpha = c.notclickedAlpha = 0.6F;
        a.clickedalpha = b.clickedalpha = c.clickedalpha = 1.0F;
        AddChild(a);
        AddChild(b);
        AddChild(c);
    }

    public void ButtonClicked(String name)
    {
        if(name.equals("angry"))
            IsoPlayer.instance.setDialogMood(0);
        if(IsoCamera.CamCharacter.getMoodles().getMoodleLevel(MoodleType.Angry) <= 2 && name.equals("neutral"))
            IsoPlayer.instance.setDialogMood(1);
        if(IsoCamera.CamCharacter.getMoodles().getMoodleLevel(MoodleType.Angry) <= 1 && name.equals("happy"))
            IsoPlayer.instance.setDialogMood(2);
    }

    public void render()
    {
        if(friMouseOver == null)
        {
            aggMouseOver = Texture.getSharedTexture("media/ui/Emotion_Aggressive_MouseOver.png");
            aggSel = Texture.getSharedTexture("media/ui/Emotion_Aggressive_Selected.png");
            neuMouseOver = Texture.getSharedTexture("media/ui/Emotion_Neutral_MouseOver.png");
            neuSel = Texture.getSharedTexture("media/ui/Emotion_Neutral_Selected.png");
            friMouseOver = Texture.getSharedTexture("media/ui/Emotion_Friendly_MouseOver.png");
            friSel = Texture.getSharedTexture("media/ui/Emotion_Friendly_Selected.png");
        }
        if(IsoPlayer.getInstance().getDialogMood() == 0)
            a.highlight = aggSel;
        else
            a.highlight = aggMouseOver;
        if(IsoPlayer.getInstance().getDialogMood() == 1)
            b.highlight = neuSel;
        else
            b.highlight = neuMouseOver;
        if(IsoPlayer.getInstance().getDialogMood() == 2)
            c.highlight = friSel;
        else
            c.highlight = friMouseOver;
        super.render();
    }

    public void update()
    {
        super.update();
        if(IsoCamera.CamCharacter != IsoPlayer.getInstance())
            return;
        a.notclickedAlpha = b.notclickedAlpha = c.notclickedAlpha = 0.6F;
        a.clickedalpha = b.clickedalpha = c.clickedalpha = 1.0F;
        if(IsoCamera.CamCharacter.getMoodles().getMoodleLevel(MoodleType.Angry) > 1)
        {
            c.notclickedAlpha = 0.3F;
            c.clickedalpha = 0.3F;
            if(IsoPlayer.getInstance().getDialogMood() >= 2)
                IsoPlayer.instance.setDialogMood(1);
        }
        if(IsoCamera.CamCharacter.getMoodles().getMoodleLevel(MoodleType.Angry) > 2)
        {
            b.notclickedAlpha = 0.3F;
            b.clickedalpha = 0.3F;
            if(IsoPlayer.getInstance().getDialogMood() >= 1)
                IsoPlayer.instance.setDialogMood(0);
        }
    }

    public String getClickedValue()
    {
        if(IsoPlayer.getInstance().getDialogMood() == 0)
            return "angry";
        if(IsoPlayer.getInstance().getDialogMood() == 1)
            return "neutral";
        if(IsoPlayer.getInstance().getDialogMood() == 2)
            return "happy";
        else
            return "neutral";
    }

    HUDButton a;
    HUDButton b;
    HUDButton c;
    int selected;
    Texture aggSel;
    Texture aggMouseOver;
    Texture neuSel;
    Texture neuMouseOver;
    Texture friSel;
    Texture friMouseOver;
}
