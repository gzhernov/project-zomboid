// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AlphaWarningState.java

package zombie.gameStates;

import org.lwjgl.opengl.GL11;
import zombie.IndieGL;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureID;
import zombie.input.Mouse;
import zombie.openal.Audio;

// Referenced classes of package zombie.gameStates:
//            GameState, GameStateMachine

public class AlphaWarningState extends GameState
{

    public AlphaWarningState()
    {
        alpha = 0.0F;
        alpha2 = 0.0F;
        alphaStep = 0.02F;
        bFixLogo = false;
        bFixLogo2 = false;
        delay = 20;
        leavedelay = 0;
        logoUseAlpha = 0.0F;
        logoUseAlpha2 = 0.0F;
        messageTime = 350F;
        messageTimeMax = 350F;
        stage = 0;
        targetAlpha = 0.0F;
        bNoRender = false;
    }

    public void DrawTexture(Texture tex, int x, int y, int width, int height, float alpha)
    {
        int dx = x;
        int dy = y;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
        Texture.lr = 1.0F;
        Texture.lg = 1.0F;
        Texture.lb = 1.0F;
        Texture.la = alpha;
        tex.bind();
        IndieGL.Begin();
        GL11.glTexCoord2f(tex.getXStart(), tex.getYStart());
        GL11.glVertex2f(dx, dy);
        GL11.glTexCoord2f(tex.getXEnd(), tex.getYStart());
        GL11.glVertex2f(dx + width, dy);
        GL11.glTexCoord2f(tex.getXEnd(), tex.getYEnd());
        GL11.glVertex2f(dx + width, dy + height);
        GL11.glTexCoord2f(tex.getXStart(), tex.getYEnd());
        GL11.glVertex2f(dx, dy + height);
        IndieGL.End();
    }

    public void DrawTexture(Texture tex, int x, int y, int width, int height, Color col)
    {
        int dx = x;
        int dy = y;
        GL11.glColor4f(col.r, col.g, col.b, col.a);
        tex.bind();
        IndieGL.Begin();
        GL11.glTexCoord2f(tex.getXStart(), tex.getYStart());
        GL11.glVertex2f(dx, dy);
        GL11.glTexCoord2f(tex.getXEnd(), tex.getYStart());
        GL11.glVertex2f(dx + width, dy);
        GL11.glTexCoord2f(tex.getXEnd(), tex.getYEnd());
        GL11.glVertex2f(dx + width, dy + height);
        GL11.glTexCoord2f(tex.getXStart(), tex.getYEnd());
        GL11.glVertex2f(dx, dy + height);
        IndieGL.End();
    }

    public void enter()
    {
        TextureID.UseFiltering = true;
        Texture.getSharedTexture("media/ui/alpha.png");
        TextureID.UseFiltering = false;
        alpha = 0.0F;
        targetAlpha = 1.0F;
    }

    public void exit()
    {
    }

    public void render()
    {
        TextureID.UseFiltering = true;
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        if(bNoRender)
        {
            Core.getInstance().StartFrame();
            DrawTexture(Texture.getSharedTexture("media/black.png"), -100, -100, Core.getInstance().getOffscreenWidth() + 200, Core.getInstance().getOffscreenHeight() + 200, 1.0F);
            Core.getInstance().EndFrame();
            return;
        }
        Core.getInstance().StartFrame();
        DrawTexture(Texture.getSharedTexture("media/black.png"), -100, -100, Core.getInstance().getOffscreenWidth() + 200, Core.getInstance().getOffscreenHeight() + 200, 1.0F);
        if(messageTime <= 0.0F)
        {
            stage++;
            if(stage < 2)
            {
                alpha = 0.0F;
                alpha2 = 0.0F;
            }
            messageTime = messageTimeMax;
            targetAlpha = 1.0F;
            bFixLogo2 = false;
        }
        if(!bFixLogo)
            logoUseAlpha = alpha;
        if(alpha == 1.0F)
            bFixLogo = true;
        Texture tex = Texture.getSharedTexture("media/ui/alpha.png");
        int x = Core.getInstance().getOffscreenWidth() / 2;
        x -= tex.getWidth() / 2;
        int y = Core.getInstance().getOffscreenHeight() / 2;
        y -= tex.getHeight() / 2;
        DrawTexture(tex, x, y, tex.getWidth(), tex.getHeight(), new Color(1.0F, 1.0F, 1.0F, logoUseAlpha));
        Core.getInstance().EndFrame();
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
        TextureID.UseFiltering = false;
    }

    public GameStateMachine.StateAction update()
    {
        if(Mouse.isLeftDown() && messageTime < 150F)
        {
            targetAlpha = 0.0F;
            messageTime = 0.0F;
        }
        if(stage < 2 && alpha == 1.0F && targetAlpha == 1.0F)
            messageTime--;
        if(stage >= 1)
        {
            targetAlpha = 0.0F;
            bFixLogo = false;
            bFixLogo2 = false;
            if(leavedelay > 0)
                leavedelay--;
            else
            if(alpha == 0.0F)
            {
                Core.getInstance().StartFrame();
                DrawTexture(Texture.getSharedTexture("media/black.png"), -100, -100, Core.getInstance().getOffscreenWidth() + 200, Core.getInstance().getOffscreenHeight() + 200, 1.0F);
                bNoRender = true;
                return GameStateMachine.StateAction.Continue;
            }
        }
        if(alpha < targetAlpha)
        {
            alpha += alphaStep;
            if(alpha > targetAlpha)
                alpha = targetAlpha;
        } else
        if(alpha > targetAlpha)
        {
            alpha -= alphaStep * 2.0F;
            if(alpha < targetAlpha)
                alpha = targetAlpha;
        }
        if(alpha2 < targetAlpha)
        {
            alpha2 += alphaStep;
            if(alpha2 > targetAlpha)
                alpha2 = targetAlpha;
        } else
        if(alpha2 > targetAlpha)
        {
            alpha2 -= alphaStep * 2.0F;
            if(alpha2 < targetAlpha)
                alpha2 = targetAlpha;
        }
        return GameStateMachine.StateAction.Remain;
    }

    public float alpha;
    public float alpha2;
    public float alphaStep;
    public boolean bFixLogo;
    public boolean bFixLogo2;
    public int delay;
    public int leavedelay;
    public float logoUseAlpha;
    public float logoUseAlpha2;
    public float messageTime;
    public float messageTimeMax;
    public Audio musicTrack;
    public int stage;
    public float targetAlpha;
    private boolean bNoRender;
}
