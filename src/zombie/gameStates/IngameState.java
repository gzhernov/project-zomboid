// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IngameState.java

package zombie.gameStates;

import gnu.trove.map.hash.THashMap;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.lwjgl.input.Keyboard;
import zombie.*;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.Quests.QuestManager;
import zombie.*;
import zombie.ai.astar.AStarPathFinder;
import zombie.ai.astar.Path;
import zombie.ai.astar.heuristics.ManhattanHeuristic;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.properties.PropertyContainer;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureID;
import zombie.core.utils.OnceEvery;
import zombie.input.Mouse;
import zombie.inventory.ItemContainer;
import zombie.iso.*;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.objects.IsoFireManager;
import zombie.iso.objects.RainManager;
import zombie.network.GameClient;
import zombie.openal.Audio;
import zombie.scripting.ScriptManager;
import zombie.ui.*;

// Referenced classes of package zombie.gameStates:
//            GameState, GameStateMachine

public class IngameState extends GameState
{

    public IngameState()
    {
        numberTicks = 0L;
        Paused = false;
        alt = false;
        TimeSinceDeath = 0;
        cacheMusic = true;
        insanityScareCount = 5;
        MDebounce = false;
        insanitypic = -1;
        timesincelastinsanity = 0x989680;
        replayUpdate = new OnceEvery(0.1F, false);
        nReplay = 0;
        instance = this;
    }

    public void UpdateStuff()
    {
        if(IsoCamera.CamCharacter != null)
        {
            IsoCamera.OffX = IsoCamera.CamCharacter.getScreenX();
            IsoCamera.OffY = IsoCamera.CamCharacter.getScreenY();
            IsoCamera.OffX -= Core.getInstance().getOffscreenWidth() / 2;
            IsoCamera.OffY -= Core.getInstance().getOffscreenHeight() / 2;
            IsoCamera.OffY -= IsoCamera.CamCharacter.getOffsetY() * 1.5F;
            IsoCamera.OffX = (int)IsoCamera.OffX;
            IsoCamera.OffY = (int)IsoCamera.OffY;
            IsoCamera.OffX += 20F;
        }
        if(IsoCamera.lastOffX == 0.0F && IsoCamera.lastOffY == 0.0F)
        {
            IsoCamera.lastOffX = IsoCamera.OffX;
            IsoCamera.lastOffY = IsoCamera.OffY;
        }
        GameTime.instance.setLastTimeOfDay(GameTime.getInstance().getTimeOfDay());
        boolean asleep = false;
        if(!FrameLoader.bServer)
            asleep = IsoPlayer.getInstance().isAsleep();
        GameTime.getInstance().Update(asleep && UIManager.getFadeAlpha() == 1.0F);
        if(!Paused)
            ScriptManager.instance.update();
        try
        {
            TutorialManager.instance.update();
        }
        catch(Exception ex) { }
        if(!Paused)
        {
            try
            {
                WorldSoundManager.instance.update();
            }
            catch(Exception ex)
            {
                Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
            }
            try
            {
                IsoFireManager.Update();
            }
            catch(Exception ex)
            {
                Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
            }
            try
            {
                if(!FrameLoader.bClient)
                    RainManager.Update();
            }
            catch(Exception ex)
            {
                Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
            }
            try
            {
                QuestManager.instance.Update();
            }
            catch(Exception ex)
            {
                Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
            }
            try
            {
                VirtualZombieManager.instance.update();
            }
            catch(Exception ex)
            {
                Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void enter()
    {
        Core.getInstance().CalcCircle();
        GameID = Long.valueOf(Rand.Next(0x989680));
        GameID = Long.valueOf(GameID.longValue() + (long)Rand.Next(0x989680));
        GameID = Long.valueOf(GameID.longValue() + (long)Rand.Next(0x989680));
        GameID = Long.valueOf(GameID.longValue() + (long)Rand.Next(0x989680));
        GameID = Long.valueOf(GameID.longValue() + (long)Rand.Next(0x989680));
        GameID = Long.valueOf(GameID.longValue() + (long)Rand.Next(0x989680));
        GameID = Long.valueOf(GameID.longValue() + (long)Rand.Next(0x989680));
        GameID = Long.valueOf(GameID.longValue() + (long)Rand.Next(0x989680));
        GameID = Long.valueOf(GameID.longValue() + (long)Rand.Next(0x989680));
        GameID = Long.valueOf(GameID.longValue() + (long)Rand.Next(0x989680));
        GameID = Long.valueOf(GameID.longValue() + (long)Rand.Next(0x989680));
        GameID = Long.valueOf(GameID.longValue() + (long)Rand.Next(0x989680));
        GameID = Long.valueOf(GameID.longValue() + (long)Rand.Next(0x989680));
        GameID = Long.valueOf(GameID.longValue() + (long)Rand.Next(0x989680));
        GameID = Long.valueOf(GameID.longValue() + (long)Rand.Next(0x989680));
        GameID = Long.valueOf(GameID.longValue() + (long)Rand.Next(0x989680));
        GameID = Long.valueOf(GameID.longValue() + (long)Rand.Next(0x989680));
        GameID = Long.valueOf(GameID.longValue() + (long)Rand.Next(0x989680));
        GameID = Long.valueOf(GameID.longValue() + (long)Rand.Next(0x989680));
        GameID = Long.valueOf(GameID.longValue() + (long)Rand.Next(0x989680));
        GameID = Long.valueOf(GameID.longValue() + (long)Rand.Next(0x989680));
        GameID = Long.valueOf(GameID.longValue() + (long)Rand.Next(0x989680));
        System.gc();
        LuaEventManager.TriggerEvent("OnGameStart");
        for(int n = 0; n < IsoWorld.instance.CurrentCell.getContainers().size(); n++)
        {
            ItemContainer con = (ItemContainer)IsoWorld.instance.CurrentCell.getContainers().get(n);
            if(ContainerTypes.containsKey(con.type))
                ContainerTypes.put(con.type, Integer.valueOf(((Integer)ContainerTypes.get(con.type)).intValue() + 1));
            else
                ContainerTypes.put(con.type, Integer.valueOf(1));
        }

        LuaEventManager.TriggerEvent("OnLoad");
    }

    public void exit()
    {
        LOSThread.instance.finished = true;
    }

    public void FadeIn(int seconds)
    {
        UIManager.FadeIn(seconds);
    }

    public void FadeOut(int seconds)
    {
        UIManager.FadeOut(seconds);
    }

    public void renderframe()
    {
        if(UIManager.getProgressBar() != null)
        {
            UIManager.ProgressBar.setX(IsoUtils.XToScreen(IsoPlayer.getInstance().getX(), IsoPlayer.getInstance().getY(), IsoPlayer.getInstance().getZ(), 0));
            UIManager.ProgressBar.setY(IsoUtils.YToScreen(IsoPlayer.getInstance().getX(), IsoPlayer.getInstance().getY(), IsoPlayer.getInstance().getZ(), 0));
            UIManager.ProgressBar.setX(UIManager.ProgressBar.getX() - IsoCamera.OffX);
            UIManager.ProgressBar.setY(UIManager.ProgressBar.getY() - IsoCamera.OffY);
            UIManager.ProgressBar.setX(UIManager.ProgressBar.getX() - IsoPlayer.getInstance().offsetX);
            UIManager.ProgressBar.setY(UIManager.ProgressBar.getY() - IsoPlayer.getInstance().offsetY);
            UIManager.ProgressBar.setX(UIManager.ProgressBar.getX() - 16F);
            UIManager.ProgressBar.setY(UIManager.ProgressBar.getY() + 32F);
            if(UIManager.getProgressBar().getValue() > 0.0F && UIManager.getProgressBar().getValue() < 1.0F)
                UIManager.ProgressBar.setVisible(true);
            else
                UIManager.ProgressBar.setVisible(false);
        }
        try
        {
            if(!IsoPlayer.instance.isAsleep() || UIManager.getFadeAlpha() < 1.0F)
                IsoWorld.instance.render();
            try
            {
                Core.getInstance().EndFrame();
            }
            catch(Exception ex) { }
            Core.getInstance().StartFrameUI();
            UIManager.render();
            int y = 10;
            if(IsoPlayer.getInstance() != null)
            {
                Integer mem = Integer.valueOf(Texture.BindCount);
                if(IsoPlayer.getInstance().getCurrentSquare().getRoom() == null);
                Integer totVirtual = Integer.valueOf(VirtualZombieManager.instance.TotalVirtualZombies);
                Integer totReal = Integer.valueOf(IsoWorld.instance.CurrentCell.getZombieList().size());
                Integer tot = Integer.valueOf(VirtualZombieManager.instance.ReusableZombies.size());
                totVirtual = Integer.valueOf(IsoPlayer.getInstance().getStats().NumVisibleZombies);
                Float sanity = Float.valueOf(IsoPlayer.getInstance().getStats().Sanity);
                if(IsoPlayer.getInstance().getHealth() <= 0.0F || IsoPlayer.getInstance().getBodyDamage().getHealth() <= 0.0F || IsoCamera.CamCharacter != IsoPlayer.getInstance())
                {
                    EndGameTextManager _tmp = EndGameTextManager.instance;
                    EndGameTextManager.instance.render();
                }
            }
            Object arr$[] = TextureID.TextureIDStack.toArray();
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                Object in = arr$[i$];
                Integer obj = (Integer)in;
                String str = (String)TextureID.TextureIDMap.get(obj);
                y += 10;
            }

        }
        catch(Exception ex)
        {
            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(!IsoPlayer.instance.getCanUpgradePerk().isEmpty() && IsoPlayer.getInstance().getNumberOfPerksToPick() > 0 && !IsoPlayer.instance.isAsleep())
            TextManager.instance.DrawString(110, 8, "You should rest and meditate on what you've learned", 1.0F, 1.0F, 1.0F, 0.6F);
        GameProfiler.instance.render(500, 150);
        if(FrameLoader.bClient)
        {
            int y = 150;
            int x = Core.getInstance().getOffscreenWidth() - 20;
            TextManager.instance.DrawStringRight(x, y, (new StringBuilder()).append(IsoPlayer.getInstance().getDescriptor().getForename()).append(" ").append(IsoPlayer.getInstance().getDescriptor().getSurname()).append(" - ").append(IsoPlayer.getInstance().getPing()).toString(), 1.0F, 1.0F, 1.0F, 1.0F);
            y += 12;
            for(int n = 0; n < IsoWorld.instance.CurrentCell.getRemoteSurvivorList().size(); n++)
            {
                IsoSurvivor s = (IsoSurvivor)IsoWorld.instance.CurrentCell.getRemoteSurvivorList().get(n);
                TextManager.instance.DrawStringRight(x, y, (new StringBuilder()).append(s.getDescriptor().getForename()).append(" ").append(s.getDescriptor().getSurname()).append(" - ").append(s.ping).toString(), 1.0F, 1.0F, 1.0F, 1.0F);
                y += 12;
            }

        }
        if(IsoWorld.instance.TotalSurvivorsDead <= 0);
        Core.getInstance().EndFrameUI();
    }

    public void render()
    {
        if(AlwaysDebugPathfinding)
            return;
        if(IsoPlayer.getInstance() == null)
        {
            return;
        } else
        {
            Core.getInstance().StartFrame();
            renderframe();
            return;
        }
    }

    public void StartMusic()
    {
    }

    public void StartMusic(String s)
    {
    }

    public GameStateMachine.StateAction update()
    {
        if(finder == null && AlwaysDebugPathfinding)
            finder = new AStarPathFinder(null, IsoWorld.instance.CurrentCell.getPathMap(), 800, true, new ManhattanHeuristic(1));
        if(AlwaysDebugPathfinding)
        {
            finder.maxSearchDistance = 1000;
            Path path = finder.findPath(0, null, 170, 110, 0, 85, 110, 0);
            return GameStateMachine.StateAction.Remain;
        }
        if(FrameLoader.bClient)
            GameClient.instance.update();
        if(IsoPlayer.DemoMode)
            IsoCamera.updateDemo();
        if(IsoPlayer.getInstance() != null && IsoPlayer.getInstance().getStats().Sanity < 0.5F)
            SoundManager.instance.BlendVolume(insanityambient, 0.5F - IsoPlayer.getInstance().getStats().Sanity);
        if(!FrameLoader.bServer)
            updateOverheadReplay();
        timesincelastinsanity++;
        if(!PZConsole.instance.isVisible() && Keyboard.isKeyDown(50) && !MDebounce)
        {
            MDebounce = true;
            SoundManager.instance.AllowMusic = !SoundManager.instance.AllowMusic;
            if(!SoundManager.instance.AllowMusic)
            {
                SoundManager.instance.StopMusic();
                TutorialManager.instance.PrefMusic = null;
                instance.musicTrack = null;
            }
        } else
        if(!FrameLoader.bServer && !Keyboard.isKeyDown(50))
            MDebounce = false;
        if(IsoPlayer.getInstance() != null && IsoPlayer.getInstance().getHealth() > 0.0F && IsoPlayer.getInstance().getBodyDamage().getHealth() > 0.0F)
            TimeSinceDeath = 0;
        else
            TimeSinceDeath++;
        if(Mouse.isLeftDown() && TimeSinceDeath > 100)
        {
            ScriptManager.instance.Reset();
            try
            {
                GameWindow.save(true);
            }
            catch(FileNotFoundException ex)
            {
                Logger.getLogger(IngameState.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch(IOException ex)
            {
                Logger.getLogger(IngameState.class.getName()).log(Level.SEVERE, null, ex);
            }
            File file = new File((new StringBuilder()).append(GameWindow.getGameModeCacheDir()).append(File.separator).append("map_p.bin").toString());
            if(file.exists())
                file.delete();
            System.exit(0);
            IsoWorld.instance.KillCell();
            IsoWorld.instance = new IsoWorld();
            UIManager.init();
            IsoPlayer.instance = null;
            ScriptManager.instance.Reset();
            LuaManager.init();
            try
            {
                ScriptManager.instance.LoadDirPP("scripts/", true);
                ScriptManager.instance.LoadDir("scripts/", true);
                ScriptManager.instance.LoadDirPP("scripts/", false);
                ScriptManager.instance.LoadDir("scripts/", false);
            }
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(null, ex.getStackTrace(), ex.getMessage(), 0);
                Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
            }
            try
            {
                LuaManager.LoadDirBase("media/lua/");
            }
            catch(URISyntaxException ex)
            {
                Logger.getLogger(IngameState.class.getName()).log(Level.SEVERE, null, ex);
            }
            return GameStateMachine.StateAction.Continue;
        }
        try
        {
            if(IsoPlayer.getInstance() != null && (IsoPlayer.getInstance().getHealth() <= 0.0F || IsoPlayer.getInstance().getBodyDamage().getHealth() <= 0.0F))
            {
                TutorialManager.instance.StealControl = true;
                IsoPlayer.instance.setWaiting(!PZConsole.instance.isVisible() && Keyboard.isKeyDown(28));
                if(IsoPlayer.getInstance().isWaiting() && UIManager.getSpeedControls().getCurrentGameSpeed() != 3)
                    UIManager.getSpeedControls().SetCurrentGameSpeed(2);
                EndGameTextManager _tmp = EndGameTextManager.instance;
                EndGameTextManager.instance.update();
            }
            alt = !alt;
            if(!FrameLoader.bServer)
            {
                WaitMul = 1;
                if(UIManager.getSpeedControls().getCurrentGameSpeed() == 2)
                    WaitMul = 15;
                if(UIManager.getSpeedControls().getCurrentGameSpeed() == 3)
                    WaitMul = 30;
            }
            if(IsoPlayer.getInstance() != null && (IsoPlayer.getInstance() != null && IsoPlayer.getInstance().isWaiting() || UIManager.getSpeedControls().getCurrentGameSpeed() > 1))
            {
                for(int n = 0; n < WaitMul; n++)
                    if(!Paused)
                        try
                        {
                            Iterator it;
                            for(it = IsoWorld.instance.CurrentCell.getSurvivorList().iterator(); it != null && it.hasNext();)
                            {
                                IsoMovingObject iso = (IsoMovingObject)it.next();
                                ((IsoSurvivor)iso).bLastSpottedPlayer = ((IsoSurvivor)iso).bSpottedPlayer;
                                ((IsoSurvivor)iso).bSpottedPlayer = false;
                            }

                            IsoWorld.instance.update();
                            UpdateStuff();
                            LuaEventManager.TriggerEvent("OnTick", Double.valueOf(numberTicks));
                            numberTicks++;
                            ScriptManager.instance.Trigger("OnTick");
                            it = IsoWorld.instance.CurrentCell.getSurvivorList().iterator();
                            do
                            {
                                if(it == null || !it.hasNext())
                                    break;
                                IsoMovingObject iso = (IsoMovingObject)it.next();
                                if(((IsoSurvivor)iso).bLastSpottedPlayer && !((IsoSurvivor)iso).bSpottedPlayer)
                                    ScriptManager.instance.Trigger("OnLoseSightOfPlayer", ((IsoSurvivor)iso).getScriptName());
                            } while(true);
                        }
                        catch(Exception ex) { }

            } else
            if(!Paused)
                try
                {
                    Iterator it;
                    for(it = IsoWorld.instance.CurrentCell.getSurvivorList().iterator(); it != null && it.hasNext();)
                    {
                        IsoMovingObject iso = (IsoMovingObject)it.next();
                        ((IsoSurvivor)iso).bLastSpottedPlayer = ((IsoSurvivor)iso).bSpottedPlayer;
                        ((IsoSurvivor)iso).bSpottedPlayer = false;
                    }

                    IsoWorld.instance.update();
                    UpdateStuff();
                    LuaEventManager.TriggerEvent("OnTick", Double.valueOf(numberTicks));
                    numberTicks++;
                    ScriptManager.instance.Trigger("OnTick");
                    it = IsoWorld.instance.CurrentCell.getSurvivorList().iterator();
                    do
                    {
                        if(it == null || !it.hasNext())
                            break;
                        IsoMovingObject iso = (IsoMovingObject)it.next();
                        if(((IsoSurvivor)iso).bLastSpottedPlayer && !((IsoSurvivor)iso).bSpottedPlayer)
                            ScriptManager.instance.Trigger("OnLoseSightOfPlayer", ((IsoSurvivor)iso).getScriptName());
                    } while(true);
                }
                catch(Exception ex)
                {
                    Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
                }
            if(RainManager.IsRaining)
                SoundManager.instance.BlendVolume(musicTrack, 0.5F);
            else
                SoundManager.instance.BlendVolume(musicTrack, 0.3F);
        }
        catch(Exception ex)
        {
            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return GameStateMachine.StateAction.Remain;
    }

    private void renderOverhead()
    {
        if(!Core.bDebug)
            return;
        if(!Keyboard.isKeyDown(15))
            return;
        TextureID.UseFiltering = true;
        Texture.getSharedTexture("media/ui/white.png");
        IsoCell cell = IsoWorld.instance.CurrentCell;
        Texture tex = Texture.getSharedTexture("media/ui/white.png");
        int drawFloor = 0;
        int tilew = 2;
        int offx = Core.getInstance().getOffscreenWidth() - cell.getWidthInTiles() * tilew;
        int offy = Core.getInstance().getOffscreenHeight() - cell.getHeightInTiles() * tilew;
        tex.render(offx, offy, tilew * cell.getWidthInTiles(), tilew * cell.getHeightInTiles(), 0.7F, 0.7F, 0.7F, 1.0F);
        for(int x = 25; x < 75; x++)
        {
            for(int y = 25; y < 75; y++)
            {
                float del = (float)IsoWorld.instance.MetaCellGrid[x][y].zombieCount / 260F;
                del *= 0.5F;
                del += 0.5F;
                if(del < 0.0F)
                    del = 0.0F;
                if(del > 1.0F)
                    del = 1.0F;
                int ux = x - 25;
                int uy = y - 25;
                tex.render(120 + ux * (tilew * 4), uy * (tilew * 4), tilew * 4, tilew * 4, del, 0.5F, 0.5F, 1.0F);
            }

        }

        for(int x = 0; x < cell.getWidthInTiles(); x++)
        {
            for(int y = 0; y < cell.getHeightInTiles(); y++)
            {
                IsoGridSquare sq = cell.getGridSquare(x, y, drawFloor);
                if(sq == null)
                    continue;
                if(sq.getProperties().Is(IsoFlagType.solid) || sq.getProperties().Is(IsoFlagType.solidtrans))
                    tex.render(offx + x * tilew, offy + y * tilew, tilew, tilew, 0.5F, 0.5F, 0.5F, 1.0F);
                else
                if(sq.getProperties().Is(IsoFlagType.exterior))
                    tex.render(offx + x * tilew, offy + y * tilew, tilew, tilew, 0.8F, 0.8F, 0.8F, 1.0F);
                if(sq.getProperties().Is(IsoFlagType.collideN))
                    tex.render(offx + x * tilew, offy + y * tilew, tilew, 1, 0.2F, 0.2F, 0.2F, 1.0F);
                if(sq.getProperties().Is(IsoFlagType.collideW))
                    tex.render(offx + x * tilew, offy + y * tilew, 1, tilew, 0.2F, 0.2F, 0.2F, 1.0F);
                float del;
                if(cell.DangerScore[x][y] > 0)
                {
                    del = (float)cell.DangerScore[x][y] / 1000F;
                    if(del < 0.0F)
                        del = 0.0F;
                    if(del > 1.0F)
                        del = 1.0F;
                    tex.render(offx + x * tilew, offy + y * tilew, tilew, tilew, del, 0.0F, 0.0F, 0.3F);
                    continue;
                }
                del = (float)(-cell.DangerScore[x][y]) / 1000F;
                if(del < 0.0F)
                    del = 0.0F;
                if(del > 1.0F)
                    del = 1.0F;
                tex.render(offx + x * tilew, offy + y * tilew, tilew, tilew, 0.0F, del, 0.0F, 0.3F);
            }

        }

        for(int n = 0; n < IsoWorld.instance.CurrentCell.getObjectList().size(); n++)
        {
            IsoMovingObject mov = (IsoMovingObject)IsoWorld.instance.CurrentCell.getObjectList().get(n);
            
            if(mov.getZ() != (float)drawFloor)
                continue;
            
            IsoZombie s;
            if(mov instanceof IsoZombie)
            {
                tex.render(offx + (int)mov.getX() * tilew, offy + (int)mov.getY() * tilew, tilew, tilew, 1.0F, 0.0F, 0.0F, 1.0F);
                s = (IsoZombie)mov;
            }
            
            if(!(mov instanceof IsoSurvivor))
                continue;
            tex.render(offx + (int)mov.getX() * tilew + 1, offy + (int)mov.getY() * tilew + 1, tilew, tilew, 0.0F, 0.0F, 0.0F, 1.0F);
            tex.render((offx + (int)mov.getX() * tilew) - 1, (offy + (int)mov.getY() * tilew) - 1, tilew, tilew, 0.0F, 0.0F, 0.0F, 1.0F);
            tex.render((offx + (int)mov.getX() * tilew) - 1, offy + (int)mov.getY() * tilew + 1, tilew, tilew, 0.0F, 0.0F, 0.0F, 1.0F);
            tex.render(offx + (int)mov.getX() * tilew + 1, (offy + (int)mov.getY() * tilew) - 1, tilew, tilew, 0.0F, 0.0F, 0.0F, 1.0F);
            tex.render(offx + (int)mov.getX() * tilew, offy + (int)mov.getY() * tilew, tilew, tilew, 1.0F, 1.0F, 1.0F, 1.0F);
            
            IsoSurvivor s1 = (IsoSurvivor)mov;
            if(s1.getPath() == null)
                continue;
            
            for(int m = 0; m < s1.getPath().getLength(); m++);
        }

        TextureID.UseFiltering = false;
    }

    private void updateOverheadReplay()
    {
        nReplay++;
        if(nReplay >= Frames.size())
            nReplay = 0;
        if(!Keyboard.isKeyDown(15))
            nReplay = 0;
    }

    private void renderOverheadReplay()
    {
        if(!Keyboard.isKeyDown(15))
            return;
        if(Frames.isEmpty())
            return;
        zombie.iso.IsoWorld.Frame frame = (zombie.iso.IsoWorld.Frame)Frames.get(nReplay);
        TextureID.UseFiltering = true;
        Texture.getSharedTexture("media/ui/white.png");
        IsoCell cell = IsoWorld.instance.CurrentCell;
        Texture tex = Texture.getSharedTexture("media/ui/white.png");
        int drawFloor = 0;
        int tilew = 4;
        for(int x = 0; x < cell.getWidthInTiles(); x++)
        {
            for(int y = 0; y < cell.getHeightInTiles(); y++)
            {
                IsoGridSquare sq = cell.getGridSquare(x, y, drawFloor);
                if(sq.getProperties().Is(IsoFlagType.exterior))
                    tex.render(x * tilew, y * tilew, tilew, tilew, 0.8F, 0.8F, 0.8F, 1.0F);
                else
                    tex.render(x * tilew, y * tilew, tilew, tilew, 0.7F, 0.7F, 0.7F, 1.0F);
                if(sq.getProperties().Is(IsoFlagType.solid) || sq.getProperties().Is(IsoFlagType.solidtrans))
                    tex.render(x * tilew, y * tilew, tilew, tilew, 0.5F, 0.5F, 0.5F, 255F);
                if(sq.getProperties().Is(IsoFlagType.collideN))
                    tex.render(x * tilew, y * tilew, tilew, 1, 0.2F, 0.2F, 0.2F, 1.0F);
                if(sq.getProperties().Is(IsoFlagType.collideW))
                    tex.render(x * tilew, y * tilew, 1, tilew, 0.2F, 0.2F, 0.2F, 1.0F);
            }

        }

        Iterator xPos = frame.xPos.iterator();
        Iterator yPos = frame.yPos.iterator();
        Iterator type = frame.Type.iterator();
        do
        {
            if(xPos == null || !xPos.hasNext())
                break;
            int x = ((Integer)xPos.next()).intValue();
            int y = ((Integer)yPos.next()).intValue();
            int t = ((Integer)type.next()).intValue();
            if(t == 0)
            {
                tex.render(x * tilew + 1, y * tilew + 1, tilew, tilew, 0.0F, 0.0F, 0.0F, 1.0F);
                tex.render(x * tilew - 1, y * tilew - 1, tilew, tilew, 0.0F, 0.0F, 0.0F, 1.0F);
                tex.render(x * tilew - 1, y * tilew + 1, tilew, tilew, 0.0F, 0.0F, 0.0F, 1.0F);
                tex.render(x * tilew + 1, y * tilew - 1, tilew, tilew, 0.0F, 0.0F, 0.0F, 1.0F);
                tex.render(x * tilew, y * tilew, tilew, tilew, 0.5F, 0.5F, 1.0F, 1.0F);
            }
            if(t == 1)
            {
                tex.render(x * tilew + 1, y * tilew + 1, tilew, tilew, 0.0F, 0.0F, 0.0F, 1.0F);
                tex.render(x * tilew - 1, y * tilew - 1, tilew, tilew, 0.0F, 0.0F, 0.0F, 1.0F);
                tex.render(x * tilew - 1, y * tilew + 1, tilew, tilew, 0.0F, 0.0F, 0.0F, 1.0F);
                tex.render(x * tilew + 1, y * tilew - 1, tilew, tilew, 0.0F, 0.0F, 0.0F, 1.0F);
                tex.render(x * tilew, y * tilew, tilew, tilew, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            if(t == 2)
                tex.render(x * tilew, y * tilew, tilew, tilew, 1.0F, 0.0F, 0.0F, 1.0F);
        } while(true);
        TextureID.UseFiltering = false;
    }

    public static boolean DebugPathfinding = false;
    public static boolean AlwaysDebugPathfinding = false;
    public static int WaitMul = 20;
    public static IngameState instance;
    static int last = -1;
    public long numberTicks;
    public Audio musicTrack;
    public boolean Paused;
    boolean alt;
    private int TimeSinceDeath;
    public boolean cacheMusic;
    Audio insanityambient;
    public static Long GameID = Long.valueOf(0L);
    static HashMap ContainerTypes = new HashMap();
    int insanityScareCount;
    boolean MDebounce;
    static int nSaveCycle = 1800;
    static boolean bDoChars = false;
    int insanitypic;
    int timesincelastinsanity;
    AStarPathFinder finder;
    public static NulledArrayList Frames = new NulledArrayList();
    OnceEvery replayUpdate;
    int nReplay;

}
