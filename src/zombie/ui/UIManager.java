// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UIManager.java

package zombie.ui;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import zombie.FrameLoader;
import zombie.GameApplet;
import zombie.GameWindow;
import zombie.IndieGL;
import zombie.Lua.LuaEventManager;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoSurvivor;
import zombie.characters.IsoZombie;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.textures.Texture;
import zombie.core.textures.TexturePackPage;
import zombie.gameStates.IngameState;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.HandWeapon;
import zombie.iso.IsoCamera;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoObjectPicker;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.Vector2;

// Referenced classes of package zombie.ui:
//            HelpIcon, ModalDialog, UIElement, NewContainerPanel, 
//            EndTutorialMessage, SpeedControls, TutorialPanel, QuestHUD, 
//            DirectionSwitcher, ObjectTooltip, EmotionPanel, Clock, 
//            Sidebar, NewCraftingPanel, NewHealthPanel, ClothingPanel, 
//            QuestPanel, UIDebugConsole, MoodlesUI, LevelUpScreen, 
//            MovementBlender, ActionProgressBar, RadialMenu, InventoryFlowControl, 
//            TutorialManager, TextManager, PZConsole, EnduranceWidget, 
//            StatsPage, HUDButton, DoubleSizer, QuestControl, 
//            NewWindow, UIEventHandler

public class UIManager
{

    public UIManager()
    {
    }

    public static void AddTutorial(float x, float y, String title, String string, boolean bAutoExpand)
    {
        if(!HelpIcon.doOthers)
            return;
        if(getDoneTutorials().contains(title))
            return;
        getDoneTutorials().add(title);
        int sx = (int)x;
        int sy = (int)y;
        HelpIcon icon = new HelpIcon(sx, sy, title, string);
        icon.setFollowGameWorld(false);
        if(bAutoExpand)
            icon.Closed = false;
        getUI().add(icon);
    }

    public static void DoModal(String name, String help, boolean bYesNo)
    {
        if(getModal() != null)
            getUI().remove(getModal());
        setModal(new ModalDialog(name, help, bYesNo));
        getUI().add(getModal());
    }

    public static void AddUI(UIElement el)
    {
        toAdd.add(el);
    }

    public static void RemoveElement(UIElement el)
    {
        toRemove.add(el);
    }

    public static void DoModal(String name, String help, boolean bYesNo, UIEventHandler handler)
    {
        if(getModal() != null)
            getUI().remove(getModal());
        setModal(new ModalDialog(name, help, bYesNo));
        Modal.handler = handler;
        getUI().add(getModal());
    }

    public static void AddTutorial(UIElement parent, int x, int y, String title, String string, boolean bAutoExpand)
    {
        if(!HelpIcon.doOthers)
            return;
        if(getDoneTutorials().contains(title))
            return;
        getDoneTutorials().add(title);
        int sx = x;
        int sy = y;
        HelpIcon icon = new HelpIcon(sx, sy, title, string);
        icon.setFollowGameWorld(false);
        if(bAutoExpand)
            icon.Closed = false;
        icon.follow = parent;
        getUI().add(icon);
    }

    public static void AddTutorial(float x, float y, float z, String title, String string, boolean bAutoExpand, float yoff)
    {
        if(!HelpIcon.doOthers)
            return;
        if(getDoneTutorials().contains(title))
            return;
        getDoneTutorials().add(title);
        int sx = (int)IsoUtils.XToScreen(x, y, z, 0);
        int sy = (int)IsoUtils.YToScreen(x, y, z, 0);
        sx -= (int)IsoCamera.getOffX();
        sy -= (int)IsoCamera.getOffY();
        sy = (int)((float)sy + yoff);
        sx += 32;
        sy += 320;
        HelpIcon icon = new HelpIcon(sx, sy, title, string);
        if(bAutoExpand)
            icon.Closed = false;
        getUI().add(icon);
    }

    public static void closeContainers()
    {
    }

    public static void CloseContainers()
    {
        for(int i = getUI().size() - 1; i >= 0; i--)
        {
            UIElement ui = (UIElement)getUI().get(i);
            if(!(ui instanceof NewContainerPanel))
                continue;
            if(getDragInventory() != null && ((NewContainerPanel)ui).Flow.Container == getDragInventory().getContainer())
            {
                ((NewContainerPanel)ui).Flow.Container.Items.remove(getDragInventory());
                ((NewContainerPanel)ui).Flow.Container.dirty = true;
                IsoPlayer.getInstance().getInventory().AddItem(getDragInventory());
            }
            getUI().remove(ui);
            i--;
        }

    }

    public static void closeInventory()
    {
    }

    public static void DoTutorialEndMessage()
    {
        EndTutorialMessage mes = new EndTutorialMessage();
        getUI().add(mes);
    }

    public static void DrawTexture(Texture tex, int x, int y)
    {
        int dx = x;
        int dy = y;
        dx = (int)((float)dx + tex.offsetX);
        dy = (int)((float)dy + tex.offsetY);
        SpriteRenderer.instance.render(tex, dx, dy, tex.getWidth(), tex.getHeight(), 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void DrawTexture(Texture tex, int x, int y, int width, int height, float alpha)
    {
        int dx = x;
        int dy = y;
        dx = (int)((float)dx + tex.offsetX);
        dy = (int)((float)dy + tex.offsetY);
        SpriteRenderer.instance.render(tex, dx, dy, width, height, 1.0F, 1.0F, 1.0F, alpha);
    }

    public static void FadeIn(int seconds)
    {
        setFadeInTimeMax(seconds * 30);
        setFadeInTime(getFadeInTimeMax());
        setFadingOut(false);
    }

    public static void FadeOut(int seconds)
    {
        setFadeInTimeMax(seconds * 30);
        setFadeInTime(getFadeInTimeMax());
        setFadingOut(true);
    }

    public static void initCharCreation()
    {
        getUI().clear();
        setPicked(null);
        setLastPicked(null);
        TexturePackPage.getPackPage("ui");
        TexturePackPage.getPackPage("inventory");
        nativeCursor = Mouse.getNativeCursor();
        setMouseArrow(Texture.getSharedTexture("media/ui/mouseArrow.png"));
        setMouseAttack(Texture.getSharedTexture("media/ui/mouseAttack.png"));
        setMouseExamine(Texture.getSharedTexture("media/ui/mouseExamine.png"));
        setMouseGrab(Texture.getSharedTexture("media/ui/mouseGrab.png"));
    }

    public static void init()
    {
        getUI().clear();
        TexturePackPage.getPackPage("ui");
        TexturePackPage.getPackPage("inventory");
        setSpeedControls(new SpeedControls());
        SpeedControls.instance = getSpeedControls();
        getUI().add(getSpeedControls());
        if(FrameLoader.bServer)
            return;
        setPicked(null);
        setLastPicked(null);
        try
        {
            setTutorial(new TutorialPanel());
        }
        catch(FileNotFoundException ex)
        {
            Logger.getLogger(UIManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        setOnscreenQuest(new QuestHUD());
        onscreenQuest.setX(Core.getInstance().getOffscreenWidth() - 400);
        onscreenQuest.setWidth(280);
        onscreenQuest.setY(4F);
        getUI().add(getTutorial());
        getUI().add(getOnscreenQuest());
        getUI().add(getRadial());
        setDirectionSwitcher(new DirectionSwitcher(Core.getInstance().getOffscreenWidth() - 80, Core.getInstance().getOffscreenHeight() - 40));
        getUI().add(getDirectionSwitcher());
        setToolTip(new ObjectTooltip());
        setEmotion(new EmotionPanel(Core.getInstance().getOffscreenWidth() / 2, Core.getInstance().getOffscreenHeight() - 64));
        getUI().add(getEmotion());
        setClock(new Clock(Core.getInstance().getOffscreenWidth() - 110, 10));
        getUI().add(getClock());
        getUI().add(getToolTip());
        setSidebar(new Sidebar(-53, 0));
        setCrafting(new NewCraftingPanel(63, Core.getInstance().getOffscreenHeight()));
        crafting.setVisible(false);
        setHealthPanel(new NewHealthPanel(63, Core.getInstance().getOffscreenHeight(), IsoPlayer.getInstance()));
        HealthPanel.setVisible(false);
        setClothingPanel(new ClothingPanel(63, Core.getInstance().getOffscreenHeight(), IsoPlayer.getInstance()));
        clothingPanel.setVisible(false);
        setQuestPanel(new QuestPanel(Core.getInstance().getOffscreenWidth() - 463, 66));
        questPanel.setVisible(false);
        setDebugConsole(new UIDebugConsole(20, Core.getInstance().getOffscreenHeight() - 265));
        if(Core.bDebug)
            DebugConsole.setVisible(true);
        else
            DebugConsole.setVisible(false);
        setMoodleUI(new MoodlesUI());
        MoodleUI.setVisible(true);
        setLevelup(new LevelUpScreen(100, 100));
        Levelup.setX(Core.getInstance().getOffscreenWidth() / 2 - getLevelup().getWidth() / 2);
        Levelup.setY(Core.getInstance().getOffscreenHeight() / 2 - getLevelup().getHeight() / 2);
        Levelup.setVisible(false);
        getUI().add(getLevelup());
        MovementBlender blend = new MovementBlender(getSidebar());
        getUI().add(blend);
        getUI().add(getCrafting());
        getUI().add(getHealthPanel());
        getUI().add(getClothingPanel());
        getUI().add(getQuestPanel());
        getUI().add(getMoodleUI());
        getUI().add(getDebugConsole());
        setBlendTest(blend);
        setLastMouseTexture(getMouseArrow());
        resize();
        setProgressBar(new ActionProgressBar(300, 300));
        getUI().add(getProgressBar());
        getProgressBar().setValue(1.0F);
        LuaEventManager.TriggerEvent("OnCreateUI");
    }

    public static void render()
    {
        IndieGL.glAlphaFunc(519, 0.0F);
        IndieGL.glBlendFunc(770, 771);
        IndieGL.glStencilFunc(519, 0, 255);
        if(getBlack() == null)
            setBlack(Texture.getSharedTexture("black.png"));
        if(IsoPlayer.DemoMode)
            return;
        LuaEventManager.TriggerEvent("OnPreUIDraw");
        int mx = zombie.input.Mouse.getXA();
        int my = zombie.input.Mouse.getYA();
        if(IsoPlayer.instance != null && IsoPlayer.instance.IsAiming() && IsoPlayer.instance.getPrimaryHandItem() != null && (IsoPlayer.instance.getPrimaryHandItem() instanceof HandWeapon) && (IsoPlayer.instance.isCharging || ((HandWeapon)IsoPlayer.instance.getPrimaryHandItem()).isAimedFirearm()) && IsoPlayer.instance.IsUsingAimWeapon())
            if(Core.bDoubleSize)
                Core.getInstance().DrawCircle(IsoPlayer.instance.AimRadius * 2.0F, mx, my);
            else
                Core.getInstance().DrawCircle(IsoPlayer.instance.AimRadius, mx, my);
        if(isbFadeBeforeUI())
        {
            setFadeAlpha((float)getFadeInTime() / (float)getFadeInTimeMax());
            if(getFadeAlpha() > 1.0F)
                setFadeAlpha(1.0F);
            if(getFadeAlpha() < 0.0F)
                setFadeAlpha(0.0F);
            if(isFadingOut())
                setFadeAlpha(1.0F - getFadeAlpha());
            if(IsoPlayer.getInstance() != null && IsoPlayer.getInstance().isAsleep())
                setFadeAlpha(1.0F);
            else
                setFadeAlpha(0.0F);
            DrawTexture(getBlack(), 0, 0, Core.getInstance().getOffscreenWidth(), Core.getInstance().getOffscreenHeight(), getFadeAlpha());
        }
        if(getLastAlpha() != 1.0F && getFadeAlpha() == 1.0F && IsoPlayer.getInstance() != null && IsoPlayer.getInstance().isAsleep() && !IsoPlayer.instance.getCanUpgradePerk().isEmpty() && IsoPlayer.getInstance().getNumberOfPerksToPick() > 0)
        {
            Levelup.setVisible(true);
            getSpeedControls().SetCurrentGameSpeed(0);
        }
        setLastAlpha(getFadeAlpha());
        for(int i = 0; i < getUI().size(); i++)
        {
            if(!((UIElement)UI.get(i)).isIgnoreLossControl() && TutorialManager.instance.StealControl)
                continue;
            try
            {
                if(((UIElement)getUI().get(i)).isDefaultDraw())
                    ((UIElement)getUI().get(i)).render();
            }
            catch(Exception ex)
            {
                Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(getToolTip() != null)
            getToolTip().render();
        if(getDragInventory() != null)
            if(Core.bDoubleSize)
            {
                DrawTexture(getDragInventory().getTex(), mx, my);
                if(getDragInventory().getUses() > 1)
                    TextManager.instance.DrawStringRight(mx + 32, my, (new StringBuilder()).append("x").append((new Integer(getDragInventory().getUses())).toString()).toString(), 1.0F, 1.0F, 1.0F, 1.0F);
            } else
            {
                DrawTexture(getDragInventory().getTex(), mx + 10, my + 12);
                if(getDragInventory().getUses() > 1)
                    TextManager.instance.DrawStringRight(mx + 32 + 10, my + 12, (new StringBuilder()).append("x").append((new Integer(getDragInventory().getUses())).toString()).toString(), 1.0F, 1.0F, 1.0F, 1.0F);
            }
        if(getSpeedControls() != null && getSpeedControls().getCurrentGameSpeed() == 0 && !Levelup.isVisible() && (getModal() == null || !Modal.isVisible()))
            TextManager.instance.DrawStringCentre(Core.getInstance().getOffscreenWidth() / 2, Core.getInstance().getOffscreenHeight() / 2, "- Game Paused -", 1.0F, 1.0F, 1.0F, 1.0F);
        if(!isbFadeBeforeUI())
        {
            setFadeAlpha((float)getFadeInTime() / (float)getFadeInTimeMax());
            if(getFadeAlpha() > 1.0F)
                setFadeAlpha(1.0F);
            if(getFadeAlpha() < 0.0F)
                setFadeAlpha(0.0F);
            if(isFadingOut())
                setFadeAlpha(1.0F - getFadeAlpha());
            if(IsoPlayer.getInstance() != null && IsoPlayer.getInstance().isAsleep())
                setFadeAlpha(1.0F);
            else
                setFadeAlpha(0.0F);
            DrawTexture(getBlack(), 0, 0, Core.getInstance().getOffscreenWidth(), Core.getInstance().getOffscreenHeight(), getFadeAlpha());
        }
        if(IsoPlayer.getInstance() != null && IsoPlayer.getInstance().isAsleep() && getLevelup().isVisible())
        {
            getLevelup().render();
            if(getModal() != null && getModal().isVisible())
                getModal().render();
        }
        PZConsole.instance.render();
        LuaEventManager.TriggerEvent("OnPostUIDraw");
    }

    public static void resizeCharCreation()
    {
    }

    public static void resize()
    {
        if(getClock() == null)
        {
            return;
        } else
        {
            setLastDoubleSize(Core.bDoubleSize);
            setLastOffX(Core.getInstance().getScreenWidth());
            setLastOffY(Core.getInstance().getScreenHeight());
            Tutorial.setX(Core.getInstance().getScreenWidth() / 2);
            Tutorial.setX(Tutorial.getX() - 300F);
            Tutorial.setY(Core.getInstance().getScreenHeight() - 60);
            MoodleUI.setX(Core.getInstance().getScreenWidth() - 50);
            directionSwitcher.setX(Core.getInstance().getScreenWidth() - 80);
            directionSwitcher.setY(Core.getInstance().getScreenHeight() - 40);
            emotion.setX(Core.getInstance().getScreenWidth() / 2 - 48);
            emotion.setY(Core.getInstance().getScreenHeight() - 40);
            onscreenQuest.setX(Core.getInstance().getScreenWidth() - 400);
            clock.setX(Core.getInstance().getScreenWidth() - 110);
            speedControls.setX(Core.getInstance().getScreenWidth() - 110);
            sidebar.height = Core.getInstance().getScreenHeight();
            return;
        }
    }

    public static Vector2 getTileFromMouse(int mx, int my, int z)
    {
        PickedTile.x = IsoUtils.XToIso(mx - 30, my - 366, z);
        PickedTile.y = IsoUtils.YToIso(mx - 30, my - 366, z);
        PickedTileLocal.x = getPickedTile().x - (float)(int)getPickedTile().x;
        PickedTileLocal.y = getPickedTile().y - (float)(int)getPickedTile().y;
        PickedTile.x = (int)getPickedTile().x;
        PickedTile.y = (int)getPickedTile().y;
        return getPickedTile();
    }

    public static void update()
    {
         try
        {
        	 if(nativeCursor == null)
            nativeCursor = Mouse.getNativeCursor();
        
        Mouse.setNativeCursor(nativeCursor);
        
        if(IsoPlayer.DemoMode || IngameState.DebugPathfinding || IngameState.AlwaysDebugPathfinding)
            return;
       
            if(!toRemove.isEmpty())
                UI.removeAll(toRemove);
            toRemove.clear();
            if(!toAdd.isEmpty())
                UI.addAll(toAdd);
            toAdd.clear();
            setFadeInTime(getFadeInTime() - 1);
            if(getHealthPanel() != null)
                getHealthPanel().SetCharacter(IsoCamera.CamCharacter);
            Stack tutorialStack = new Stack();
            if(getDragInventory() != null && !IsoPlayer.instance.getInventory().contains(DragInventory.getType()) && getDragInventory().getContainer() == IsoPlayer.getInstance().getInventory())
                setDragInventory(null);
            boolean consumedClick = false;
            boolean consumedRClick = false;
            boolean consumedMove = false;
            int mx = zombie.input.Mouse.getXA();
            int my = zombie.input.Mouse.getYA();
            int mxw = zombie.input.Mouse.getX();
            int myw = zombie.input.Mouse.getY();
            if(getBlendTest() != null)
                if(BlendTest.Running());
            for(int i = getUI().size() - 1; i >= 0; i--)
            {
                if(((UIElement)getUI().get(i)).isFollowGameWorld() || (getUI().get(i) instanceof HelpIcon))
                    tutorialStack.add(getUI().get(i));
                if(getUI().get(i) instanceof ObjectTooltip)
                {
                    UIElement rem = (UIElement)getUI().remove(i);
                    getUI().add(rem);
                }
                if(getUI().get(i) instanceof TutorialPanel)
                {
                    UIElement rem = (UIElement)getUI().remove(i);
                    getUI().add(rem);
                }
            }

            for(int i = 0; i < getUI().size(); i++)
                if(((UIElement)getUI().get(i)).alwaysOnTop)
                {
                    UIElement rem = (UIElement)getUI().remove(i);
                    i--;
                    toAdd.add(rem);
                }

            getUI().addAll(toAdd);
            toAdd.clear();
            for(int i = 0; i < tutorialStack.size(); i++)
            {
                getUI().remove(tutorialStack.get(i));
                getUI().insertElementAt(tutorialStack.elementAt(i), 0);
            }

            if(zombie.input.Mouse.isLeftPressed())
            {
                Core.UnfocusActiveTextEntryBox();
                int i = getUI().size() - 1;
                do
                {
                    if(i < 0)
                        break;
                    UIElement ui = (UIElement)getUI().get(i);
                    if((getModal() == null || getModal() == ui || !getModal().isVisible()) && (ui.isIgnoreLossControl() || !TutorialManager.instance.StealControl) && ui.isVisible() && ((float)mx > ui.getX() && (float)my > ui.getY() && (float)mx < ui.getX() + (float)ui.getWidth() && (float)my < ui.getY() + (float)ui.getHeight() || ui.isCapture()) && ui.onMouseDown(mx - (int)ui.getX(), my - (int)ui.getY()))
                    {
                        consumedClick = true;
                        break;
                    }
                    i--;
                } while(true);
                if(getSidebar() != null)
                    sidebar.setClickedValue(null);
                if(!consumedClick && !Keyboard.isKeyDown(57))
                {
                    if(getBlendTest() != null)
                        getBlendTest().MoveTo(-53F, 0.0F, 0.4F);
                    if(NewCraftingPanel.instance != null)
                        NewCraftingPanel.instance.setVisible(false);
                }
                if(!consumedClick)
                {
                    if(IsoPlayer.getInstance() != null && IsoPlayer.getInstance().getGuardModeUI() > 0 && getPicked().square.isFree(false))
                        if(IsoPlayer.getInstance().getGuardModeUI() == 1)
                        {
                            IsoPlayer.instance.setGuardStand(getPicked().square);
                            IsoPlayer.instance.setGuardModeUI(IsoPlayer.instance.getGuardModeUI() + 1);
                        } else
                        if(IsoPlayer.getInstance().getGuardModeUI() == 2)
                        {
                            IsoPlayer.instance.setGuardFace(getPicked().square);
                            IsoPlayer.instance.setGuardModeUI(0);
                            IsoPlayer.getInstance().getGuardChosen().DoGuard(IsoPlayer.getInstance());
                        }
                    CloseContainers();
                    if(IsoWorld.instance.CurrentCell != null && !IsoWorld.instance.CurrentCell.DoBuilding(false) && getPicked() != null && IsoPlayer.getInstance() != null && !IsoPlayer.isIsAiming() && !IsoPlayer.instance.isAsleep())
                    {
                        getPicked().tile.onMouseLeftClick(getPicked().lx, getPicked().ly);
                        if(getPicked().tile != getRadial().RadialTarget)
                            getRadial().close();
                    }
                }
            }
            if(zombie.input.Mouse.isLeftReleased())
            {
                int i = getUI().size() - 1;
                do
                {
                    if(i < 0)
                        break;
                    UIElement ui = (UIElement)getUI().get(i);
                    if((ui.isIgnoreLossControl() || !TutorialManager.instance.StealControl) && ui.isVisible() && (getModal() == null || getModal() == ui || !getModal().isVisible()) && ((float)mx > ui.getX() && (float)my > ui.getY() && (float)mx < ui.getX() + (float)ui.getWidth() && (float)my < ui.getY() + (float)ui.getHeight() || ui.isCapture()) && ui.onMouseUp(mx - (int)ui.getX(), my - (int)ui.getY()))
                    {
                        if(ui != getRadial())
                            getRadial().close();
                        break;
                    }
                    i--;
                } while(true);
            }
            if(zombie.input.Mouse.isRightPressed())
            {
                int i = getUI().size() - 1;
                do
                {
                    if(i < 0)
                        break;
                    UIElement ui = (UIElement)getUI().get(i);
                    if(ui.isVisible() && (getModal() == null || getModal() == ui || !getModal().isVisible()) && ((float)mx > ui.getX() && (float)my > ui.getY() && (float)mx < ui.getX() + (float)ui.getWidth() && (float)my < ui.getY() + (float)ui.getHeight() || ui.isCapture()) && ui.onRightMouseDown(mx - (int)ui.getX(), my - (int)ui.getY()))
                    {
                        consumedRClick = true;
                        break;
                    }
                    i--;
                } while(true);
                if(IsoWorld.instance.CurrentCell != null && getPicked() != null && !IsoPlayer.isIsAiming() && !IsoPlayer.instance.isAsleep())
                {
                    getPicked().tile.onMouseRightClick(getPicked().lx, getPicked().ly);
                    setRightDownObject(getPicked().tile);
                }
            }
            if(zombie.input.Mouse.isRightReleased())
            {
                int i = 0;
                if(IsoPlayer.getInstance() != null && IsoPlayer.getInstance().getGuardModeUI() > 0)
                    IsoPlayer.instance.setGuardModeUI(0);
                i = getUI().size() - 1;
                do
                {
                    if(i < 0)
                        break;
                    UIElement ui = (UIElement)getUI().get(i);
                    if((ui.isIgnoreLossControl() || !TutorialManager.instance.StealControl) && ui.isVisible() && (getModal() == null || getModal() == ui || !getModal().isVisible()) && ((float)mx > ui.getX() && (float)my > ui.getY() && (float)mx < ui.getX() + (float)ui.getWidth() && (float)my < ui.getY() + (float)ui.getHeight() || ui.isCapture()) && ui.onRightMouseUp(mx - (int)ui.getX(), my - (int)ui.getY()))
                        break;
                    i--;
                } while(true);
                if(i == -1)
                {
                    setDragInventory(null);
                    if(getPicked() == null);
                }
                if(IsoPlayer.getInstance() != null && IsoPlayer.getInstance() != null)
                    IsoPlayer.instance.setDragObject(null);
                if(IsoWorld.instance.CurrentCell != null && getRightDownObject() != null && !IsoPlayer.isIsAiming() && !IsoPlayer.instance.isAsleep())
                {
                    getRightDownObject().onMouseRightReleased();
                    setRightDownObject(null);
                }
            }
            if(getLastMouseX() != mx || getLastMouseY() != my)
            {
                setDoMouseControls(true);
                for(int i = getUI().size() - 1; i >= 0; i--)
                {
                    UIElement ui = (UIElement)getUI().get(i);
                    if(!ui.isIgnoreLossControl() && TutorialManager.instance.StealControl || !ui.isVisible())
                        continue;
                    if((float)mx > ui.getX() && (float)my > ui.getY() && (float)mx < ui.getX() + (float)ui.getWidth() && (float)my < ui.getY() + (float)ui.getHeight() || ui.isCapture())
                    {
                        if(!ui.onMouseMove(mx - getLastMouseX(), my - getLastMouseY()))
                            continue;
                        consumedMove = true;
                        break;
                    }
                    ui.onMouseMoveOutside(mx - getLastMouseX(), my - getLastMouseY());
                }

            }
            if(!isDoMouseControls() && GameWindow.ActivatedJoyPad)
            {
                setPicked(IsoObjectPicker.Instance.ClickObjectStore[IsoObjectPicker.Instance.ThisFrame.size() + 1000]);
                Picked.tile = IsoPlayer.getInstance().getInteract();
                if(getPicked().tile == null)
                {
                    setPicked(null);
                    setLastPicked(null);
                }
            } else
            if(!consumedMove)
            {
                setPicked(IsoObjectPicker.Instance.Pick(mxw, myw));
                if(IsoCamera.CamCharacter != null)
                    setPickedTile(getTileFromMouse(mxw, myw, (int)IsoCamera.CamCharacter.getZ()));
            }
            setLastMouseX(mx);
            setLastMouseY(my);
            for(int i = 0; i < getUI().size(); i++)
                ((UIElement)getUI().get(i)).update();

            if(!consumedMove && getPicked() != null)
            {
                if(getPicked().tile != getLastPicked())
                {
                    toolTip.targetAlpha = 0.0F;
                    if(getPicked().tile.HasTooltip())
                        if(getToolTip().Object != getPicked().tile)
                            getToolTip().show(getPicked().tile, mx + 8, my + 16);
                        else
                            toolTip.targetAlpha = 1.0F;
                }
                setLastPicked(getPicked().tile);
            }
            IsoCamera.lastOffX = IsoCamera.OffX;
            IsoCamera.lastOffY = IsoCamera.OffY;
            UpdateMouse();
        }
        catch(LWJGLException ex)
        {
            Logger.getLogger(UIManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return;
    }

    private static void UpdateMouse()
    {
        Texture tex = getLastMouseTexture();
        if(getPicked() != null)
            if(getPicked().tile instanceof IsoZombie)
            {
                IsoZombie z = (IsoZombie)getPicked().tile;
                if(IsoPlayer.getInstance().IsAttackRange(z.getX(), z.getY(), z.getZ()))
                    tex = getMouseAttack();
            } else
            if(IsoPlayer.isIsAiming())
                tex = getMouseAttack();
            else
                tex = getMouseArrow();
        PZConsole.instance.update();
    }

    public static NewContainerPanel getOpenContainer()
    {
        for(int n = 0; n < getUI().size(); n++)
            if(getUI().get(n) instanceof NewContainerPanel)
                return (NewContainerPanel)getUI().get(n);

        return null;
    }

    public static void LaunchRadial(IsoObject obj)
    {
        getRadial().CreateFromObject(obj, Core.getInstance().getOffscreenWidth() / 2 + 14, Core.getInstance().getOffscreenHeight() / 2 - 40);
    }

    public static int getLastMouseX()
    {
        return lastMouseX;
    }

    public static void setLastMouseX(int aLastMouseX)
    {
        lastMouseX = aLastMouseX;
    }

    public static int getLastMouseY()
    {
        return lastMouseY;
    }

    public static void setLastMouseY(int aLastMouseY)
    {
        lastMouseY = aLastMouseY;
    }

    public static zombie.iso.IsoObjectPicker.ClickObject getPicked()
    {
        return Picked;
    }

    public static void setPicked(zombie.iso.IsoObjectPicker.ClickObject aPicked)
    {
        Picked = aPicked;
    }

    public static Clock getClock()
    {
        return clock;
    }

    public static void setClock(Clock aClock)
    {
        clock = aClock;
    }

    public static EnduranceWidget getEndurance()
    {
        return Endurance;
    }

    public static void setEndurance(EnduranceWidget aEndurance)
    {
        Endurance = aEndurance;
    }

    public static Stack getUI()
    {
        return UI;
    }

    public static void setUI(Stack aUI)
    {
        UI = aUI;
    }

    public static ObjectTooltip getToolTip()
    {
        return toolTip;
    }

    public static void setToolTip(ObjectTooltip aToolTip)
    {
        toolTip = aToolTip;
    }

    public static Texture getMouseArrow()
    {
        return mouseArrow;
    }

    public static void setMouseArrow(Texture aMouseArrow)
    {
        mouseArrow = aMouseArrow;
    }

    public static Texture getMouseExamine()
    {
        return mouseExamine;
    }

    public static void setMouseExamine(Texture aMouseExamine)
    {
        mouseExamine = aMouseExamine;
    }

    public static Texture getMouseAttack()
    {
        return mouseAttack;
    }

    public static void setMouseAttack(Texture aMouseAttack)
    {
        mouseAttack = aMouseAttack;
    }

    public static Texture getMouseGrab()
    {
        return mouseGrab;
    }

    public static void setMouseGrab(Texture aMouseGrab)
    {
        mouseGrab = aMouseGrab;
    }

    public static StatsPage getStatsPage()
    {
        return StatsPage;
    }

    public static void setStatsPage(StatsPage aStatsPage)
    {
        StatsPage = aStatsPage;
    }

    public static TutorialPanel getTutorial()
    {
        return Tutorial;
    }

    public static void setTutorial(TutorialPanel aTutorial)
    {
        Tutorial = aTutorial;
    }

    public static HUDButton getInv()
    {
        return Inv;
    }

    public static void setInv(HUDButton aInv)
    {
        Inv = aInv;
    }

    public static HUDButton getHea()
    {
        return Hea;
    }

    public static void setHea(HUDButton aHea)
    {
        Hea = aHea;
    }

    public static DoubleSizer getResizer()
    {
        return Resizer;
    }

    public static void setResizer(DoubleSizer aResizer)
    {
        Resizer = aResizer;
    }

    public static DirectionSwitcher getDirectionSwitcher()
    {
        return directionSwitcher;
    }

    public static void setDirectionSwitcher(DirectionSwitcher aDirectionSwitcher)
    {
        directionSwitcher = aDirectionSwitcher;
    }

    public static MovementBlender getBlendTest()
    {
        return BlendTest;
    }

    public static void setBlendTest(MovementBlender aBlendTest)
    {
        BlendTest = aBlendTest;
    }

    public static Sidebar getSidebar()
    {
        return sidebar;
    }

    public static void setSidebar(Sidebar aSidebar)
    {
        sidebar = aSidebar;
    }

    public static SpeedControls getSpeedControls()
    {
        return speedControls;
    }

    public static void setSpeedControls(SpeedControls aSpeedControls)
    {
        speedControls = aSpeedControls;
    }

    public static InventoryItem getDragInventory()
    {
        return DragInventory;
    }

    public static void setDragInventory(InventoryItem aDragInventory)
    {
        DragInventory = aDragInventory;
    }

    public static NewCraftingPanel getCrafting()
    {
        return crafting;
    }

    public static void setCrafting(NewCraftingPanel aCrafting)
    {
        crafting = aCrafting;
    }

    public static NewHealthPanel getHealthPanel()
    {
        return HealthPanel;
    }

    public static void setHealthPanel(NewHealthPanel aHealthPanel)
    {
        HealthPanel = aHealthPanel;
    }

    public static ClothingPanel getClothingPanel()
    {
        return clothingPanel;
    }

    public static void setClothingPanel(ClothingPanel aClothingPanel)
    {
        clothingPanel = aClothingPanel;
    }

    public static QuestPanel getQuestPanel()
    {
        return questPanel;
    }

    public static void setQuestPanel(QuestPanel aQuestPanel)
    {
        questPanel = aQuestPanel;
    }

    public static EmotionPanel getEmotion()
    {
        return emotion;
    }

    public static void setEmotion(EmotionPanel aEmotion)
    {
        emotion = aEmotion;
    }

    public static UIDebugConsole getDebugConsole()
    {
        return DebugConsole;
    }

    public static void setDebugConsole(UIDebugConsole aDebugConsole)
    {
        DebugConsole = aDebugConsole;
    }

    public static MoodlesUI getMoodleUI()
    {
        return MoodleUI;
    }

    public static void setMoodleUI(MoodlesUI aMoodleUI)
    {
        MoodleUI = aMoodleUI;
    }

    public static QuestControl getTempQuest()
    {
        return tempQuest;
    }

    public static void setTempQuest(QuestControl aTempQuest)
    {
        tempQuest = aTempQuest;
    }

    public static QuestHUD getOnscreenQuest()
    {
        return onscreenQuest;
    }

    public static void setOnscreenQuest(QuestHUD aOnscreenQuest)
    {
        onscreenQuest = aOnscreenQuest;
    }

    public static NewWindow getTempQuestWin()
    {
        return tempQuestWin;
    }

    public static void setTempQuestWin(NewWindow aTempQuestWin)
    {
        tempQuestWin = aTempQuestWin;
    }

    public static boolean isbFadeBeforeUI()
    {
        return bFadeBeforeUI;
    }

    public static void setbFadeBeforeUI(boolean abFadeBeforeUI)
    {
        bFadeBeforeUI = abFadeBeforeUI;
    }

    public static ActionProgressBar getProgressBar()
    {
        return ProgressBar;
    }

    public static void setProgressBar(ActionProgressBar aProgressBar)
    {
        ProgressBar = aProgressBar;
    }

    public static BodyDamage getBD_Test()
    {
        return BD_Test;
    }

    public static void setBD_Test(BodyDamage aBD_Test)
    {
        BD_Test = aBD_Test;
    }

    public static LevelUpScreen getLevelup()
    {
        return Levelup;
    }

    public static void setLevelup(LevelUpScreen aLevelup)
    {
        Levelup = aLevelup;
    }

    public static float getFadeAlpha()
    {
        return FadeAlpha;
    }

    public static void setFadeAlpha(float aFadeAlpha)
    {
        FadeAlpha = aFadeAlpha;
    }

    public static int getFadeInTimeMax()
    {
        return FadeInTimeMax;
    }

    public static void setFadeInTimeMax(int aFadeInTimeMax)
    {
        FadeInTimeMax = aFadeInTimeMax;
    }

    public static int getFadeInTime()
    {
        return FadeInTime;
    }

    public static void setFadeInTime(int aFadeInTime)
    {
        FadeInTime = aFadeInTime;
    }

    public static boolean isFadingOut()
    {
        return FadingOut;
    }

    public static void setFadingOut(boolean aFadingOut)
    {
        FadingOut = aFadingOut;
    }

    public static Texture getLastMouseTexture()
    {
        return lastMouseTexture;
    }

    public static void setLastMouseTexture(Texture aLastMouseTexture)
    {
        lastMouseTexture = aLastMouseTexture;
    }

    public static IsoObject getLastPicked()
    {
        return LastPicked;
    }

    public static void setLastPicked(IsoObject aLastPicked)
    {
        LastPicked = aLastPicked;
    }

    public static Stack getDoneTutorials()
    {
        return DoneTutorials;
    }

    public static void setDoneTutorials(Stack aDoneTutorials)
    {
        DoneTutorials = aDoneTutorials;
    }

    public static float getLastOffX()
    {
        return lastOffX;
    }

    public static void setLastOffX(float aLastOffX)
    {
        lastOffX = aLastOffX;
    }

    public static float getLastOffY()
    {
        return lastOffY;
    }

    public static void setLastOffY(float aLastOffY)
    {
        lastOffY = aLastOffY;
    }

    public static boolean isLastDoubleSize()
    {
        return lastDoubleSize;
    }

    public static void setLastDoubleSize(boolean aLastDoubleSize)
    {
        lastDoubleSize = aLastDoubleSize;
    }

    public static boolean isDoMouseControls()
    {
        return DoMouseControls;
    }

    public static void setDoMouseControls(boolean aDoMouseControls)
    {
        DoMouseControls = aDoMouseControls;
    }

    public static ModalDialog getModal()
    {
        return Modal;
    }

    public static void setModal(ModalDialog aModal)
    {
        Modal = aModal;
    }

    public static Texture getBlack()
    {
        return black;
    }

    public static void setBlack(Texture aBlack)
    {
        black = aBlack;
    }

    public static float getLastAlpha()
    {
        return lastAlpha;
    }

    public static void setLastAlpha(float aLastAlpha)
    {
        lastAlpha = aLastAlpha;
    }

    public static Vector2 getPickedTileLocal()
    {
        return PickedTileLocal;
    }

    public static void setPickedTileLocal(Vector2 aPickedTileLocal)
    {
        PickedTileLocal = aPickedTileLocal;
    }

    public static Vector2 getPickedTile()
    {
        return PickedTile;
    }

    public static void setPickedTile(Vector2 aPickedTile)
    {
        PickedTile = aPickedTile;
    }

    public static IsoObject getRightDownObject()
    {
        return RightDownObject;
    }

    public static void setRightDownObject(IsoObject aRightDownObject)
    {
        RightDownObject = aRightDownObject;
    }

    public static RadialMenu getRadial()
    {
        return radial;
    }

    public static void setRadial(RadialMenu aRadial)
    {
        radial = aRadial;
    }

    public static int lastMouseX = 0;
    public static int lastMouseY = 0;
    public static zombie.iso.IsoObjectPicker.ClickObject Picked = null;
    public static Clock clock;
    public static EnduranceWidget Endurance;
    public static Stack UI = new Stack();
    public static ObjectTooltip toolTip = null;
    public static Texture mouseArrow;
    public static Texture mouseExamine;
    public static Texture mouseAttack;
    public static Texture mouseGrab;
    public static StatsPage StatsPage;
    public static TutorialPanel Tutorial;
    public static HUDButton Inv;
    public static HUDButton Hea;
    public static DoubleSizer Resizer;
    public static DirectionSwitcher directionSwitcher;
    public static MovementBlender BlendTest;
    public static Sidebar sidebar;
    public static SpeedControls speedControls;
    public static InventoryItem DragInventory;
    public static NewCraftingPanel crafting;
    public static NewHealthPanel HealthPanel;
    public static ClothingPanel clothingPanel;
    public static QuestPanel questPanel;
    public static EmotionPanel emotion;
    public static UIDebugConsole DebugConsole;
    public static MoodlesUI MoodleUI;
    public static QuestControl tempQuest;
    public static QuestHUD onscreenQuest;
    public static NewWindow tempQuestWin;
    public static boolean bFadeBeforeUI = false;
    public static ActionProgressBar ProgressBar;
    public static BodyDamage BD_Test = null;
    public static LevelUpScreen Levelup = null;
    public static float FadeAlpha = 1.0F;
    public static int FadeInTimeMax = 180;
    public static int FadeInTime = 180;
    public static boolean FadingOut = false;
    public static Texture lastMouseTexture;
    public static IsoObject LastPicked = null;
    public static Stack DoneTutorials = new Stack();
    public static float lastOffX = 0.0F;
    public static float lastOffY = 0.0F;
    public static boolean lastDoubleSize = false;
    public static boolean DoMouseControls = true;
    public static ModalDialog Modal = null;
    static ArrayList toRemove = new ArrayList();
    static ArrayList toAdd = new ArrayList();
    public static Cursor nativeCursor = null;
    public static Texture black = null;
    public static float lastAlpha = 10000F;
    public static Vector2 PickedTileLocal = new Vector2();
    public static Vector2 PickedTile = new Vector2();
    public static IsoObject RightDownObject = null;
    static Texture mouse = null;
    public static RadialMenu radial = new RadialMenu();

}
