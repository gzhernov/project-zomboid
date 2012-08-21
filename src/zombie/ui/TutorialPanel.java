// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TutorialPanel.java

package zombie.ui;

import java.io.FileNotFoundException;
import zombie.core.Core;
import zombie.core.fonts.AngelCodeFont;
import zombie.core.textures.Texture;
import zombie.core.textures.TexturePackPage;

// Referenced classes of package zombie.ui:
//            UIElement, TextManager, UIFont

public class TutorialPanel extends UIElement
{

    public TutorialPanel()
        throws FileNotFoundException
    {
        alpha = 0.0F;
        alphaStep = 0.05F;
        hiding = false;
        MaxMessageTime = 20;
        MessageTime = 0.0F;
        NextMessage = null;
        NextMessage2 = null;
        showing = false;
        targetAlpha = 0.0F;
        background = null;
        font = new AngelCodeFont("media/zombiefont2.fnt", "zombiefont2_0");
        background = TexturePackPage.getTexture("black");
        width = 650;
        height = 50;
        x = Core.getInstance().getOffscreenWidth() / 2;
        x -= width / 2;
        y = Core.getInstance().getOffscreenHeight();
        y -= 60F;
    }

    public void hide()
    {
        hiding = true;
    }

    public void render()
    {
        if(showing && alpha > 0.0F)
        {
            DrawTextureScaled(background, 0, 0, getWidth(), getHeight(), alpha * 0.75F);
            int y = Core.getInstance().getOffscreenHeight() - 47;
            if(Message2 != null)
                y -= 10;
            TextManager.instance.DrawStringCentre(UIFont.Medium, Core.getInstance().getOffscreenWidth() / 2, y, Message, 1.0F, 1.0F, 1.0F, alpha);
            if(Message2 != null)
                TextManager.instance.DrawStringCentre(UIFont.Medium, Core.getInstance().getOffscreenWidth() / 2, y + 20, Message2, 1.0F, 1.0F, 1.0F, alpha);
        }
    }

    public void ShowMessage(String message, String message2)
    {
        if(message != null && message.equals(Message) && message2 != null && message2.equals(Message2))
        {
            return;
        } else
        {
            MessageTime = 0.0F;
            NextMessage = message;
            NextMessage2 = message2;
            showing = true;
            hiding = false;
            return;
        }
    }

    public void update()
    {
        if(NextMessage != null)
            targetAlpha = 0.0F;
        else
        if(Message != null)
            targetAlpha = 1.0F;
        if(hiding)
            targetAlpha = 0.0F;
        if(alpha <= 0.0F && NextMessage != null)
        {
            Message = NextMessage;
            Message2 = NextMessage2;
            NextMessage = null;
            NextMessage2 = null;
        }
        if(alpha < targetAlpha)
        {
            alpha += alphaStep;
            if(alpha > targetAlpha)
                alpha = targetAlpha;
        } else
        if(alpha > targetAlpha)
        {
            alpha -= alphaStep;
            if(alpha < targetAlpha)
                alpha = targetAlpha;
        }
    }

    public float alpha;
    public float alphaStep;
    public boolean hiding;
    public int MaxMessageTime;
    public String Message;
    public float MessageTime;
    public String NextMessage;
    public String NextMessage2;
    public boolean showing;
    public float targetAlpha;
    Texture background;
    AngelCodeFont font;
    private String Message2;
}
