// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MainScreenState.java

package zombie.gameStates;

import java.io.*;
import org.lwjgl.opengl.ARBShaderObjects;
import zombie.GameTime;
import zombie.Lua.LuaEventManager;
import zombie.characters.IsoPlayer;
import zombie.core.Collections.NulledArrayList;
import zombie.core.*;
import zombie.core.textures.*;
import zombie.input.Mouse;
import zombie.openal.Audio;
import zombie.ui.TextManager;
import zombie.ui.UIFont;

// Referenced classes of package zombie.gameStates:
//            GameState, GameStateMachine, IngameState

public class MainScreenState extends GameState
{
    public static class ScreenElement
    {

        public void render()
        {
            int x = (int)this.x;
            int y = (int)this.y;
            for(int n = 0; n < xCount; n++)
            {
                MainScreenState.DrawTexture(tex, x, y, (int)((float)tex.getWidth() * MainScreenState.totalScale), (int)((float)tex.getHeight() * MainScreenState.totalScale), alpha);
                x = (int)((float)x + (float)tex.getWidth() * MainScreenState.totalScale);
            }

            TextManager.instance.DrawStringRight(Core.getInstance().getOffscreenWidth() - 5, Core.getInstance().getOffscreenHeight() - 15, (new StringBuilder()).append("Version: ").append(MainScreenState.Version).toString(), 1.0F, 1.0F, 1.0F, 1.0F);
        }

        public void setY(float y)
        {
            this.y = sy = y - (float)tex.getHeight() * MainScreenState.totalScale;
        }

        public void update()
        {
            x += xVel * MainScreenState.totalScale;
            y += yVel * MainScreenState.totalScale;
            TicksTillTargetAlpha--;
            if(TicksTillTargetAlpha <= 0)
                targetAlpha = 1.0F;
            if(jumpBack && sx - x > (float)tex.getWidth() * MainScreenState.totalScale)
                x += (float)tex.getWidth() * MainScreenState.totalScale;
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
        public boolean jumpBack;
        public float sx;
        public float sy;
        public float targetAlpha;
        public Texture tex;
        public int TicksTillTargetAlpha;
        public float x;
        public int xCount;
        public float xVel;
        public float xVelO;
        public float y;
        public float yVel;
        public float yVelO;

        public ScreenElement(Texture tex, int x, int y, float xVel, float yVel, int xCount)
        {
            alpha = 0.0F;
            alphaStep = 0.2F;
            jumpBack = true;
            sx = 0.0F;
            sy = 0.0F;
            targetAlpha = 0.0F;
            TicksTillTargetAlpha = 0;
            this.x = 0.0F;
            this.xCount = 1;
            this.xVel = 0.0F;
            xVelO = 0.0F;
            this.y = 0.0F;
            this.yVel = 0.0F;
            yVelO = 0.0F;
            this.x = sx = x;
            this.y = sy = (float)y - (float)tex.getHeight() * MainScreenState.totalScale;
            this.xVel = xVel;
            this.yVel = yVel;
            this.tex = tex;
            this.xCount = xCount;
        }
    }

    public class Credit
    {

        public int disappearDelay;
        public Texture name;
        public float nameAlpha;
        public int nameAppearDelay;
        public float nameTargetAlpha;
        public Texture title;
        public float titleAlpha;
        public float titleTargetAlpha;
       

        public Credit(Texture title, Texture name)
        {
         
            disappearDelay = 200;
            nameAppearDelay = 40;
            titleTargetAlpha = 1.0F;
            titleAlpha = 0.0F;
            nameTargetAlpha = 0.0F;
            nameAlpha = 0.0F;
            this.title = title;
            this.name = name;
        }
    }


    public MainScreenState()
    {
        alpha = 1.0F;
        alphaStep = 0.03F;
        creditID = 0;
        Credits = new NulledArrayList();
        RestartDebounceClickTimer = 10;
        Elements = new NulledArrayList(16);
        targetAlpha = 1.0F;
        creditDelay = 300;
        vertShader = 0;
        fragShader = 0;
    }

    private int createVertShader(String filename)
    {
        vertShader = ARBShaderObjects.glCreateShaderObjectARB(35633);
        if(vertShader == 0)
            return 0;
        String vertexCode = "";
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while((line = reader.readLine()) != null) 
                vertexCode = (new StringBuilder()).append(vertexCode).append(line).append("\n").toString();
        }
        catch(Exception e)
        {
            System.out.println("Fail reading vertex shading code");
            return 0;
        }
        ARBShaderObjects.glShaderSourceARB(vertShader, vertexCode);
        ARBShaderObjects.glCompileShaderARB(vertShader);
        if(ARBShaderObjects.glGetObjectParameteriARB(vertShader, 35713) == 0)
            vertShader = 0;
        return vertShader;
    }

    private int createFragShader(String filename)
    {
        fragShader = ARBShaderObjects.glCreateShaderObjectARB(35632);
        if(fragShader == 0)
            return 0;
        String fragCode = "";
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while((line = reader.readLine()) != null) 
                fragCode = (new StringBuilder()).append(fragCode).append(line).append("\n").toString();
        }
        catch(Exception e)
        {
            System.out.println("Fail reading fragment shading code");
            return 0;
        }
        ARBShaderObjects.glShaderSourceARB(fragShader, fragCode);
        ARBShaderObjects.glCompileShaderARB(fragShader);
        if(ARBShaderObjects.glGetObjectParameteriARB(fragShader, 35713) == 0)
        {
            System.err.println(fragShader);
            fragShader = 0;
        }
        return fragShader;
    }

    public static void DrawTexture(Texture tex, int x, int y, int width, int height, float alpha)
    {
        int dx = x;
        int dy = y;
        SpriteRenderer.instance.render(tex, x, y, width, height, 1.0F, 1.0F, 1.0F, alpha);
    }

    public static void DrawTexture(Texture tex, int x, int y, int width, int height, Color col)
    {
        SpriteRenderer.instance.render(tex, x, y, width, height, col.r, col.g, col.b, col.a);
    }

    public void enter()
    {
        TexturePackPage t = TexturePackPage.getPackPage("tiles2");
        t = TexturePackPage.getPackPage("tiles3");
        Credits.clear();
        Elements.clear();
        targetAlpha = 1.0F;
        TextureID.UseFiltering = true;
        RestartDebounceClickTimer = 100;
        do
            if(Credits.size() < 4)
            {
                switch(Rand.Next(4))
                {
                case 0: // '\0'
                    AddCredit(new Credit(Texture.getSharedTexture("media/ui/Credits_IndieStone.png"), Texture.getSharedTexture("media/ui/Credits_ChrisS.png")));
                    break;

                case 1: // '\001'
                    AddCredit(new Credit(Texture.getSharedTexture("media/ui/Credits_IndieStone.png"), Texture.getSharedTexture("media/ui/Credits_AndyH.png")));
                    break;

                case 2: // '\002'
                    AddCredit(new Credit(Texture.getSharedTexture("media/ui/Credits_IndieStone.png"), Texture.getSharedTexture("media/ui/Credits_MarinaSC.png")));
                    break;

                case 3: // '\003'
                    AddCredit(new Credit(Texture.getSharedTexture("media/ui/Credits_IndieStone.png"), Texture.getSharedTexture("media/ui/Credits_NickC.png")));
                    break;

                case 4: // '\004'
                    AddCredit(new Credit(Texture.getSharedTexture("media/ui/Credits_IndieStone.png"), Texture.getSharedTexture("media/ui/Credits_WillP.png")));
                    break;
                }
            } else
            {
                Credits.add(new Credit(Texture.getSharedTexture("media/ui/Credits_Music.png"), Texture.getSharedTexture("media/ui/Credits_ZachB.png")));
                Credits.add(new Credit(Texture.getSharedTexture("media/ui/Credits_CArt.png"), Texture.getSharedTexture("media/ui/Credits_Afekay.png")));
                Credits.add(new Credit(Texture.getSharedTexture("media/ui/Credits_SysAdmin.png"), Texture.getSharedTexture("media/ui/Credits_JohnathonT.png")));
                totalScale = (float)Core.getInstance().getOffscreenHeight() / 1080F;
                lastW = Core.getInstance().getOffscreenWidth();
                lastH = Core.getInstance().getOffscreenHeight();
                alpha = 1.0F;
                IsoPlayer.setGhostMode(true);
                IsoPlayer.DemoMode = true;
                Credits.clear();
                int y = (int)((float)Core.getInstance().getOffscreenHeight() * 0.7F);
                int x = 0;
                ScreenElement el = new ScreenElement(Texture.getSharedTexture("media/ui/PZ_Logo.png"), Core.getInstance().getOffscreenWidth() / 2 - (int)((float)Texture.getSharedTexture("media/ui/PZ_Logo.png").getWidth() * totalScale) / 2, y - (int)(350F * totalScale), 0.0F, 0.0F, 1);
                el.targetAlpha = 1.0F;
                el.alphaStep *= 0.9F;
                Logo = el;
                Elements.add(el);
                el = new ScreenElement(Texture.getSharedTexture("media/ui/StoryMode_Text.png"), Core.getInstance().getOffscreenWidth() / 2 - (int)((float)Texture.getSharedTexture("media/ui/StoryMode_Text.png").getWidth() * totalScale) / 2, y - (int)(140F * totalScale), 0.0F, 0.0F, 1);
                el.alphaStep *= 0.5F;
                el.TicksTillTargetAlpha = 160;
                StoryModeText = el;
                el = new ScreenElement(Texture.getSharedTexture("media/ui/SandboxMode_Text.png"), Core.getInstance().getOffscreenWidth() / 2 - (int)((float)Texture.getSharedTexture("media/ui/SandboxMode_Text.png").getWidth() * totalScale) / 2, y - (int)(70F * totalScale), 0.0F, 0.0F, 1);
                el.alphaStep *= 0.5F;
                el.TicksTillTargetAlpha = 160;
                SandboxModeText = el;
                TextureID.UseFiltering = false;
                return;
            }
        while(true);
    }

    public void exit()
    {
        IsoPlayer.setGhostMode(false);
        IsoPlayer.DemoMode = false;
    }

    public void render()
    {
        GameTime.instance.setTimeOfDay(23F);
        Core.getInstance().StartFrame();
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
        totalScale = (float)Core.getInstance().getOffscreenHeight() / 1080F;
        int y = (int)((float)Core.getInstance().getOffscreenHeight() * 0.9F);
        if(Cloud2 != null)
        {
            Cloud2.setY((y - (int)(90F * totalScale)) + 20);
            Cloud2.xCount = (int)((float)Core.getInstance().getOffscreenWidth() / (1920F * totalScale)) + 2;
            Cloud1.setY((float)y + 20F * totalScale);
            Cloud1.xCount = (int)((float)Core.getInstance().getOffscreenWidth() / (1920F * totalScale)) + 2;
            Cloud0.setY((float)y - 40F * totalScale);
            Cloud0.xCount = (int)((float)Core.getInstance().getOffscreenWidth() / (1920F * totalScale)) + 2;
            Ground.setY((float)y + 22F * totalScale);
            Ground.xCount = (int)((float)Core.getInstance().getOffscreenWidth() / (1014F * totalScale)) + 2;
            City.setY((float)y + 22F * totalScale);
            City.x = (int)(150F * totalScale);
            Guy.setY((float)y + 22F * totalScale);
            Guy.x = Core.getInstance().getOffscreenWidth() - (int)((float)Texture.getSharedTexture("media/ui/LoneMan.png").getWidth() * totalScale);
            Logo.setY(y - (int)(480F * totalScale));
            Logo.x = Core.getInstance().getOffscreenWidth() / 2 - (int)((float)Texture.getSharedTexture("media/ui/PZ_Logo.png").getWidth() * totalScale) / 2;
            StoryModeText.setY(y - (int)(380F * totalScale));
            StoryModeText.x = Core.getInstance().getOffscreenWidth() / 2 - (int)((float)Texture.getSharedTexture("media/ui/StoryMode_Text.png").getWidth() * totalScale) / 2;
            SandboxModeText.setY(y - (int)(310F * totalScale));
            SandboxModeText.x = Core.getInstance().getOffscreenWidth() / 2 - (int)((float)Texture.getSharedTexture("media/ui/SandboxMode_Text.png").getWidth() * totalScale) / 2;
        }
        DrawTexture(Texture.getSharedTexture("media/ui/white.png"), -100, -100, Core.getInstance().getOffscreenWidth() + 200, Core.getInstance().getOffscreenHeight() + 200, new Color(0.6F, 0.1F, 0.0F, 0.4F));
        for(int n = 0; n < Elements.size(); n++)
        {
            ScreenElement el = (ScreenElement)Elements.get(n);
            el.render();
        }

        TextManager.instance.DrawStringCentre(UIFont.MainMenu1, Core.getInstance().getOffscreenWidth() / 2, (int)SandboxModeText.y, "Sandbox Mode", 1.0F, 1.0F, 1.0F, SandboxModeText.alpha);
        if(Current != null)
        {
            float cx = 40F;
            float cy = (float)Core.getInstance().getOffscreenHeight() * 0.72F;
            cx *= totalScale;
            DrawTexture(Current.title, (int)cx, (int)cy, (int)((float)Current.title.getWidth() * totalScale * 0.8F), (int)((float)Current.title.getHeight() * totalScale * 0.8F), Current.titleAlpha);
            DrawTexture(Current.name, (int)cx, (int)cy, (int)((float)Current.title.getWidth() * totalScale * 0.8F), (int)((float)Current.title.getHeight() * totalScale * 0.8F), Current.nameAlpha);
        }
        DrawTexture(Texture.getSharedTexture("media/black.png"), -100, -100, Core.getInstance().getOffscreenWidth() + 200, Core.getInstance().getOffscreenHeight() + 200, 1.0F - alpha);
        Core.getInstance().EndFrame();
        Core.getInstance().StartFrameUI();
        Core.getInstance().EndFrameUI();
    }

    public GameStateMachine.StateAction update()
    {
        if(ingameState == null);
        if(creditID < Credits.size())
            Current = (Credit)Credits.get(creditID);
        else
            Current = null;
        if(creditID + 1 < Credits.size())
            Next = (Credit)Credits.get(creditID + 1);
        else
            Next = null;
        boolean bDoTitleFadeout = Next == null || Next.title != Current.title;
        creditDelay--;
        if(Current != null && creditDelay <= 0)
        {
            Current.nameAppearDelay--;
            Current.disappearDelay--;
            if(Current.nameAppearDelay < 0 && Current.disappearDelay > 0)
                Current.nameTargetAlpha = 1.0F;
            if(Current.disappearDelay < 0)
            {
                Current.nameTargetAlpha = 0.0F;
                if(bDoTitleFadeout)
                    Current.titleTargetAlpha = 0.0F;
                if(Current.nameAlpha <= 0.0F)
                {
                    creditID++;
                    if(!bDoTitleFadeout)
                    {
                        Next.titleAlpha = 1.0F;
                        Next.nameAppearDelay = 0;
                    }
                }
            }
            if(Current.nameAlpha < Current.nameTargetAlpha)
            {
                Current.nameAlpha += alphaStep;
                if(Current.nameAlpha > Current.nameTargetAlpha)
                    Current.nameAlpha = Current.nameTargetAlpha;
            } else
            if(Current.nameAlpha > Current.nameTargetAlpha)
            {
                Current.nameAlpha -= alphaStep;
                if(Current.nameAlpha < Current.nameTargetAlpha)
                    Current.nameAlpha = Current.nameTargetAlpha;
            }
            if(Current.titleAlpha < Current.titleTargetAlpha)
            {
                Current.titleAlpha += alphaStep;
                if(Current.titleAlpha > Current.titleTargetAlpha)
                    Current.titleAlpha = Current.titleTargetAlpha;
            } else
            if(Current.titleAlpha > Current.titleTargetAlpha)
            {
                Current.titleAlpha -= alphaStep;
                if(Current.titleAlpha < Current.titleTargetAlpha)
                    Current.titleAlpha = Current.titleTargetAlpha;
            }
        }
        int Lit = 0;
        if((float)Mouse.getX() > SandboxModeText.x && (float)Mouse.getX() < SandboxModeText.x + (float)SandboxModeText.tex.getWidth() && (float)Mouse.getY() > SandboxModeText.y && (float)Mouse.getY() < SandboxModeText.y + (float)SandboxModeText.tex.getHeight())
            Lit = 1;
        if((float)Mouse.getX() > StoryModeText.x && (float)Mouse.getX() < StoryModeText.x + (float)StoryModeText.tex.getWidth() && (float)Mouse.getY() > StoryModeText.y && (float)Mouse.getY() < StoryModeText.y + (float)StoryModeText.tex.getHeight())
            Lit = 2;
        if(Lit == 1)
        {
            SandboxModeText.alpha = 1.0F;
            StoryModeText.alpha = 0.5F;
        } else
        if(Lit == 2)
        {
            SandboxModeText.alpha = 0.5F;
            StoryModeText.alpha = 1.0F;
        } else
        {
            SandboxModeText.alpha = 0.5F;
            StoryModeText.alpha = 0.5F;
        }
        if(RestartDebounceClickTimer > 0)
            RestartDebounceClickTimer--;
        if(RestartDebounceClickTimer == 0 && Mouse.isLeftDown() && Lit != 0)
        {
            targetAlpha = 0.0F;
            if(Lit == 1)
                Core.GameMode = "Sandbox";
        }
        for(int n = 0; n < Elements.size(); n++)
        {
            ScreenElement el = (ScreenElement)Elements.get(n);
            el.update();
        }

        lastW = Core.getInstance().getOffscreenWidth();
        lastH = Core.getInstance().getOffscreenHeight();
        if(alpha <= 0.0F)
            return GameStateMachine.StateAction.Continue;
        else
            return GameStateMachine.StateAction.Remain;
    }

    private void AddCredit(Credit credit)
    {
        for(int n = 0; n < Credits.size(); n++)
            if(((Credit)Credits.get(n)).title == credit.title && ((Credit)Credits.get(n)).name == credit.name)
                return;

        Credits.add(credit);
    }

    public GameState redirectState()
    {
        LuaEventManager.TriggerEvent("OnPreGameStart");
        return null;
    }

    public static String Version = "0.2.0r rc 2";
    public static Audio ambient;
    public static Audio musicTrack;
    public static float totalScale = 1.0F;
    public float alpha;
    public float alphaStep;
    public int creditID;
    public NulledArrayList Credits;
    private int RestartDebounceClickTimer;
    public NulledArrayList Elements;
    public float targetAlpha;
    ScreenElement City;
    ScreenElement Cloud0;
    ScreenElement Cloud1;
    ScreenElement Cloud2;
    Credit Current;
    ScreenElement Ground;
    ScreenElement Guy;
    int lastH;
    int lastW;
    ScreenElement Logo;
    Credit Next;
    ScreenElement StoryModeText;
    ScreenElement SandboxModeText;
    int creditDelay;
    static IngameState ingameState;
    private int vertShader;
    private int fragShader;
    static int shader = -1;

}
