// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ChooseStory.java

package zombie.gameStates;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.lwjgl.opengl.GL11;
import zombie.GameApplet;
import zombie.IndieGL;
import zombie.core.Core;
import zombie.core.IndieFileLoader;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureID;
import zombie.input.Mouse;
import zombie.scripting.ScriptManager;
import zombie.ui.TextManager;
import zombie.ui.UIFont;

// Referenced classes of package zombie.gameStates:
//            GameState, GameStateMachine

public class ChooseStory extends GameState
{
    public class Story
    {

        public String dir;
        public Texture tex;
        private String name;
        private String desc;
       
        public Story(String dir)
        {
          
            name = "Unnamed Story";
            desc = "An adventure by someone or other.";
            tex = tex;
            this.dir = dir;
        }
    }


    public ChooseStory()
    {
        StoryDetails = new Stack();
        Stories = new Stack();
        SelectedStory = 0;
    }

    private void getStoryDetails(Story story, String dir)
        throws FileNotFoundException
    {
        InputStreamReader is = IndieFileLoader.getStreamReader((new StringBuilder()).append("media/stories/").append(dir).append("/story.info").toString());
        String inputLine = null;
        BufferedReader br = new BufferedReader(is);
        try
        {
            do
            {
                if((inputLine = br.readLine()) == null)
                    break;
                if(inputLine.contains("name="))
                {
                    String posterfile = inputLine.replace("name=", "");
                    story.name = posterfile;
                } else
                if(inputLine.contains("poster="))
                {
                    String posterfile = inputLine.replace("poster=", "");
                    Texture tex = Texture.getSharedTexture((new StringBuilder()).append("media/stories/").append(dir).append("/").append(posterfile).toString());
                    story.tex = tex;
                } else
                if(inputLine.contains("description="))
                {
                    String posterfile = inputLine.replace("description=", "");
                    story.desc = posterfile;
                }
            } while(true);
        }
        catch(IOException ex)
        {
            Logger.getLogger(ChooseStory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enter()
    {
        getStoryList();
    }

    public void getStoryList()
    {
        TextureID.UseFiltering = true;
        Texture.getSharedTexture("media/ui/blank.png");
        StoryDetails.clear();
        try
        {
            Stories = ScriptManager.instance.getStoryList();
        }
        catch(IOException ex)
        {
            Logger.getLogger(ChooseStory.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(URISyntaxException ex)
        {
            Logger.getLogger(ChooseStory.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int n = 0; n < Stories.size(); n++)
        {
            String dir = (String)Stories.get(n);
            if(dir.contains("Sandbox"))
                continue;
            Core.storyDirectory = "mods/";
            Story s = new Story(dir);
            try
            {
                getStoryDetails(s, dir);
            }
            catch(FileNotFoundException ex)
            {
                Logger.getLogger(ChooseStory.class.getName()).log(Level.SEVERE, null, ex);
            }
            StoryDetails.add(s);
        }

        TextureID.UseFiltering = false;
    }

    public GameStateMachine.StateAction update()
    {
        int startY = 64;
        int index = (int)((float)(Mouse.getY() - startY) / 60F);
        float uiscale = (float)Core.getInstance().getOffscreenHeight() / 1024F;
        int indent = (int)(200F * ((float)Core.getInstance().getOffscreenWidth() / (float)Core.getInstance().getOffscreenHeight() - 1.33F));
        int y = startY;
        int menuend = (int)((float)Core.getInstance().getOffscreenWidth() - ((float)indent + 870F * uiscale));
        if(index < StoryDetails.size() && index >= 0 && Mouse.getX() < menuend)
        {
            SelectedStory = index;
            if(Mouse.isButtonDown(0))
            {
                Core.GameMode = ((Story)StoryDetails.get(SelectedStory)).dir;
                try
                {
                    ScriptManager.instance.LoadDirPP((new StringBuilder()).append("stories/").append(Core.GameMode).append("/").toString(), true);
                    ScriptManager.instance.LoadDir((new StringBuilder()).append("stories/").append(Core.GameMode).append("/").toString(), true);
                    ScriptManager.instance.LoadDirPP((new StringBuilder()).append("stories/").append(Core.GameMode).append("/").toString(), false);
                    ScriptManager.instance.LoadDir((new StringBuilder()).append("stories/").append(Core.GameMode).append("/").toString(), false);
                }
                catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(null, ex.getStackTrace(), ex.getMessage(), 0);
                    Logger.getLogger(ChooseStory.class.getName()).log(Level.SEVERE, null, ex);
                }
                ScriptManager.instance.Trigger("OnPostLoadStory");
                return GameStateMachine.StateAction.Continue;
            }
        }
        return GameStateMachine.StateAction.Remain;
    }

    public void render()
    {
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        int startY = 64;
        int index = (int)((float)(Mouse.getY() - startY) / 60F);
        float uiscale = (float)Core.getInstance().getOffscreenHeight() / 1024F;
        int indent = (int)(200F * ((float)Core.getInstance().getOffscreenWidth() / (float)Core.getInstance().getOffscreenHeight() - 1.33F));
        int y = startY;
        int menuend = (int)((float)Core.getInstance().getOffscreenWidth() - ((float)indent + 870F * uiscale));
        if(index < StoryDetails.size() && index >= 0 && Mouse.getX() < menuend)
            SelectedStory = index;
        Core.getInstance().StartFrame();
        for(int n = 0; n < StoryDetails.size(); n++)
        {
            if(n == SelectedStory)
            {
                DrawTexture(Texture.getSharedTexture("media/ui/white.png"), 0, y + 5, Core.getInstance().getOffscreenWidth() - indent, 60, 0.8F);
                TextManager.instance.DrawString(UIFont.MainMenu1, 30, y, ((Story)StoryDetails.get(n)).name, 0.0F, 0.0F, 0.0F, 1.0F);
                TextManager.instance.DrawString(UIFont.MainMenu2, 40, y + 40, ((Story)StoryDetails.get(n)).desc, 0.3F, 0.3F, 0.3F, 1.0F);
            } else
            {
                TextManager.instance.DrawString(UIFont.MainMenu1, 30, y, ((Story)StoryDetails.get(n)).name, 1.0F, 1.0F, 1.0F, 1.0F);
                TextManager.instance.DrawString(UIFont.MainMenu2, 40, y + 40, ((Story)StoryDetails.get(n)).desc, 0.7F, 0.7F, 0.7F, 1.0F);
            }
            y += 60;
        }

        if(StoryDetails.size() > SelectedStory)
        {
            Story st = (Story)StoryDetails.get(SelectedStory);
            if(st.tex != null)
            {
                float scale = (float)Core.getInstance().getOffscreenHeight() / (float)st.tex.getHeight();
                int w = (int)((float)st.tex.getWidth() * scale);
                int h = (int)((float)st.tex.getHeight() * scale);
                DrawTexture(st.tex, Core.getInstance().getOffscreenWidth() - w - indent, 0, w, h, 1.0F);
            }
        }
        Core.getInstance().EndFrame();
        Core.getInstance().StartFrameUI();
        Core.getInstance().EndFrameUI();
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
    }

    public static void DrawTexture(Texture tex, int x, int y, int width, int height, float alpha)
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

    Stack StoryDetails;
    Stack Stories;
    public int SelectedStory;
}
