// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GameLoadingState.java

package zombie.gameStates;

import java.io.*;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import zombie.*;
import zombie.Lua.LuaManager;
import zombie.PathfindManager;
import zombie.Quests.QuestManager;
import zombie.SoundManager;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.sound.SoundEngine;
import zombie.core.textures.TexturePackPage;
import zombie.iso.*;
import zombie.iso.objects.RainManager;
import zombie.network.GameClient;
import zombie.scripting.ScriptManager;
import zombie.ui.*;

// Referenced classes of package zombie.gameStates:
//            GameState, GameStateMachine

public class GameLoadingState extends GameState
{

    public GameLoadingState()
    {
        loader = null;
        bDone = false;
    }

    public void enter()
    {
        SoundEngine.instance.create();
        try
        {
            TexturePackPage.LoadDir("texturepacks");
        }
        catch(URISyntaxException ex)
        {
            Logger.getLogger(GameLoadingState.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(Core.GameMode.equals("Sandbox"))
        {
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
            }
            ScriptManager.instance.Trigger("OnPostLoadStory");
        }
        ScriptManager.instance.Trigger("OnPreMapLoad");
        loader = new Thread(new Runnable() {

            public void run()
            {
                if(FrameLoader.bClient && !GameClient.instance.Connect(FrameLoader.IP))
                    System.exit(0);
                boolean success = (new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).toString())).mkdir();
                try
                {
                    LuaManager.LoadDir((new StringBuilder()).append("stories/").append(Core.GameMode).append("/lua/").toString());
                }
                catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(null, ex.getStackTrace(), ex.getMessage(), 0);
                }
                SoundManager.instance.Purge();
                TutorialManager.instance = new TutorialManager();
                QuestManager.instance = new QuestManager();
                GameTime.setInstance(new GameTime());
                IsoWorld.instance = new IsoWorld();
                System.gc();
                IsoWorld.instance.init();
                IsoObjectPicker.Instance.Init();
                RainManager.reset();
                TutorialManager.instance.init();
                TutorialManager.instance.CreateQuests();
                EndGameTextManager.instance = new EndGameTextManager();
                System.gc();
                GameTime.getInstance().init();
                File inFile = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).append("map_t.bin").toString());
                if(inFile.exists())
                {
                    FileInputStream inStream = null;
                    try
                    {
                        inStream = new FileInputStream(inFile);
                    }
                    catch(FileNotFoundException ex)
                    {
                        Logger.getLogger(GameLoadingState.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    DataInputStream input = new DataInputStream(inStream);
                    try
                    {
                        GameTime.instance.load(input);
                        IsoWorld.instance.PopulateCellWithZombies();
                    }
                    catch(IOException ex)
                    {
                        Logger.getLogger(GameLoadingState.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try
                    {
                        inStream.close();
                    }
                    catch(IOException ex)
                    {
                        Logger.getLogger(GameLoadingState.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                LOSThread.instance.Start();
                ScriptManager.instance.Trigger("OnGameStart");
                PathfindManager.instance.init();
                if(FrameLoader.bClient)
                    GameClient.instance.readyForGameData = true;
                SoundManager.instance.update3D();
                Done();
            }

           

            
          
        }
);
        loader.start();
    }

    public void Done()
    {
        bDone = true;
    }

    public void exit()
    {
        loader = null;
        bDone = false;
        IsoCamera.SetCharacterToFollow(IsoPlayer.getInstance());
        UIManager.init();
        UIManager.getLevelup().init();
    }

    public void render()
    {
        Core.getInstance().StartFrame();
        Core.getInstance().EndFrame();
        Core.getInstance().StartFrameUI();
        TextManager.instance.DrawStringCentre(Core.getInstance().getScreenWidth() / 2, Core.getInstance().getScreenHeight() / 2, "Loading", 1.0F, 1.0F, 1.0F, 1.0F);
        Core.getInstance().EndFrameUI();
    }

    public GameStateMachine.StateAction update()
    {
        if(bDone)
            return GameStateMachine.StateAction.Continue;
        else
            return GameStateMachine.StateAction.Remain;
    }

    Thread loader;
    public boolean bDone;
}
