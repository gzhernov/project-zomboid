// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   GameWindow.java

package zombie;

import java.awt.Container;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.*;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.*;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.characters.professions.ProfessionFactory;
import zombie.characters.skills.PerkFactory;
import zombie.characters.traits.TraitFactory;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.SpriteRenderer;
import zombie.core.input.Input;
import zombie.core.sound.SoundEngine;
import zombie.core.textures.AuthenticatingFrame;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureID;
import zombie.core.textures.TexturePackPage;
import zombie.debug.LineDrawer;
import zombie.gameStates.CharacterCreationState;
import zombie.gameStates.GameLoadingState;
import zombie.gameStates.GameStateMachine;
import zombie.gameStates.IngameState;
import zombie.gameStates.LoadGamePopupState;
import zombie.gameStates.LoginState;
import zombie.gameStates.MainScreenState;
import zombie.input.Mouse;
import zombie.iso.IsoCell;
import zombie.iso.IsoObjectPicker;
import zombie.iso.IsoWorld;
import zombie.iso.Vector2;
import zombie.openal.SoundStore;
import zombie.scripting.ScriptManager;
import zombie.ui.PZConsole;
import zombie.ui.SpeedControls;
import zombie.ui.TextManager;
import zombie.ui.UIManager;

// Referenced classes of package zombie:
//            GameApplet, FrameLoader, IndieLogger, SoundManager, 
//            ZomboidGlobals, GameTime

public class GameWindow
{

    public GameWindow()
    {
    }

    public static void DrawDebugLine(int x1, int x2, int y1, int y2)
    {
        debugLine.addLine(x1, y1, x2, y2, 255, 0, 0, "test");
    }

    public static void initApplet()
        throws Exception
    {
        Vector2 temp = new Vector2();
        temp.x = 0.5F;
        temp.y = 1.0F;
        temp.normalize();
        float len = temp.getLength();
        float half = temp.x / 2.0F;
        initShared();
    }

    public static void initShared()
        throws Exception
    {
        String path = (new StringBuilder()).append(getCacheDir()).append(File.separator).toString();
        File file = new File(path);
        if(!file.exists())
            file.mkdirs();
        path = (new StringBuilder()).append(path).append("2133243254543.log").toString();
        path = path;
        FrameLoader.makefile = new File(path);
        try
        {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run()
                {
                    GameWindow.login();
                }

            }
);
        }
        catch(Exception ex) { }
        TexturePackPage.getPackPage("ui");
        TexturePackPage.getPackPage("inventory");
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
        IndieLogger.init();
        IndieLogger.Log("PZ Debug Logging started");
        TraitFactory.init();
        ProfessionFactory.init();
        PerkFactory.init();
        Rand.init();
        TexturePackPage.getPackPage("ui");
        TexturePackPage.getPackPage("inventory");
        TextManager.instance.Init();
        IndieLogger.Log("Initialised TextManager");
        System.gc();
        MousePointer = Texture.getSharedTexture("media/ui/mouseArrow.png");
        states.States.add(new LoginState());
        states.States.add(new MainScreenState());
        states.LoopToState = 1;
        states.States.add(new LoadGamePopupState());
        states.States.add(new GameLoadingState());
        states.States.add(new IngameState());
        GameInput.initControllers();
        int counta = GameInput.getControllerCount();
        for(int m = 0; m < counta; m++)
        {
            int count = GameInput.getAxisCount(m);
            if(count <= 1)
                continue;
            ActiveController = m;
            for(int n = 0; n < count; n++)
            {
                String name = GameInput.getAxisName(m, n);
                if(name.equals("X Axis"))
                    XLAxis = n;
                if(name.equals("Y Axis"))
                    YLAxis = n;
                if(name.equals("X Rotation"))
                    XRAxis = n;
                if(name.equals("Y Rotation"))
                    YRAxis = n;
            }

        }

        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
        TextureID.UseFiltering = false;
        Texture.getSharedTexture("media/white.png");
        TextureID.UseFiltering = true;
        SpriteRenderer.instance = new SpriteRenderer();
        SpriteRenderer.instance.create();
        int fdsf = 0;
    }

    public static void logic()
    {
        if(SoundEngine.isCreated())
            SoundEngine.tick();
        Mouse.loop();
        debugLine.clear();
        UIManager.update();
        if(ActiveController != -1 && GameInput.isButtonPressed(0, ActiveController))
            ActivatedJoyPad = true;
        SoundManager.instance.Update();
        boolean DoUpdate = true;
        if(UIManager.getSpeedControls() != null && UIManager.getSpeedControls().getCurrentGameSpeed() == 0)
            DoUpdate = false;
        if(DoUpdate)
            states.update();
        UIManager.resize();
        if(PauseKeyDebounce == 0)
        {
            if(!PZConsole.instance.isVisible() && Keyboard.isKeyDown(25))
            {
                if(UIManager.getSpeedControls() != null)
                    if(UIManager.getSpeedControls().getCurrentGameSpeed() == 0)
                        UIManager.getSpeedControls().SetCurrentGameSpeed(4);
                    else
                        UIManager.getSpeedControls().SetCurrentGameSpeed(0);
                PauseKeyDebounce = 20;
            }
        } else
        {
            PauseKeyDebounce--;
        }
    }

    public static void render()
    {
        IsoObjectPicker.Instance.StartRender();
        states.render();
    }

    public static void main(String args[])
        throws Exception
    {
        LuaManager.init();
        LuaManager.LoadDirBase("media/lua/");
        ZomboidGlobals.Load();
        SoundStore.get().init();
        String server = System.getProperty("server");
        String client = System.getProperty("client");
        String f = System.getProperty("fullscreen");
        String debug = System.getProperty("debug");
        String xres = System.getProperty("xres");
        String yres = System.getProperty("yres");
        if(f != null)
            FrameLoader.bFullscreen = true;
        if(debug != null)
            Core.bDebug = true;
        if(xres != null)
            FrameLoader.FullX = Integer.parseInt(xres);
        if(yres != null)
            FrameLoader.FullY = Integer.parseInt(yres);
        String graphiclevel = System.getProperty("graphiclevel");
        if(graphiclevel != null)
            Core.getInstance().nGraphicLevel = Integer.parseInt(graphiclevel);
        if(server != null && server.equals("true"))
            FrameLoader.bServer = true;
        if(client != null)
        {
            FrameLoader.bClient = true;
            FrameLoader.IP = client;
        }
        if(!FrameLoader.bServer);
        try
        {
            System.setProperty("org.lwjgl.input.Mouse.allowNegativeMouseCoords", "true");
            System.setProperty("sun.java2d.noddraw", "true");
            System.setProperty("sun.java2d.opengl", "false");
            System.setProperty("sun.java2d.d3d", "false");
            System.setProperty("sun.java2d.pmoffscreen", "false");
            System.setProperty("sun.awt.noerasebackground", "true");
        }
        catch(Throwable ignored) { }
        int desktopWidth = Display.getDesktopDisplayMode().getWidth();
        int desktopHeight = Display.getDesktopDisplayMode().getHeight();
        Preferences prefs = Preferences.userNodeForPackage(FrameLoader.class);
        Display.setResizable(true);
        init(false);
        run();
        System.exit(0);
    }

    public static void main(boolean fullscreen, int xres, int yres, int graphiclevel)
        throws Exception
    {
        LuaManager.init();
        LuaManager.LoadDirBase("media/lua/");
        ZomboidGlobals.Load();
        SoundStore.get().init();
        Core.getInstance().nGraphicLevel = graphiclevel;
        String server = System.getProperty("server");
        String client = System.getProperty("client");
        String debug = System.getProperty("debug");
        FrameLoader.bFullscreen = fullscreen;
        if(debug != null)
            Core.bDebug = true;
        FrameLoader.FullX = xres;
        FrameLoader.FullY = yres;
        if(server != null && server.equals("true"))
            FrameLoader.bServer = true;
        if(client != null)
        {
            FrameLoader.bClient = true;
            FrameLoader.IP = client;
        }
        if(!FrameLoader.bServer);
        try
        {
            System.setProperty("org.lwjgl.input.Mouse.allowNegativeMouseCoords", "true");
            System.setProperty("sun.java2d.noddraw", "true");
            System.setProperty("sun.java2d.opengl", "false");
            System.setProperty("sun.java2d.d3d", "false");
            System.setProperty("sun.java2d.pmoffscreen", "false");
            System.setProperty("sun.awt.noerasebackground", "true");
        }
        catch(Throwable ignored) { }
        int desktopWidth = Display.getDesktopDisplayMode().getWidth();
        int desktopHeight = Display.getDesktopDisplayMode().getHeight();
        Preferences prefs = Preferences.userNodeForPackage(FrameLoader.class);
        Display.setResizable(true);
        init(false);
        run();
        System.exit(0);
    }

    public static long getTime()
    {
        return (Sys.getTime() * 1000L) / Sys.getTimerResolution();
    }

    public int getDelta()
    {
        long time = getTime();
        int delta = (int)(time - lastFrame);
        lastFrame = time;
        return delta;
    }

    public static void start()
    {
        lastFPS = getTime();
    }

    public static void updateFPS()
    {
        long dif = getTime() - last;
        last = getTime();
        float dif2 = 1000F / (float)dif;
        last10[last10index++] = (long)dif2;
        if(last10index >= 10)
            last10index = 0;
        average10 = 0.0F;
        int div = 0;
        float lowest = 11110F;
        float hightest = -11110F;
        for(int n = 0; n < 10; n++)
        {
            if(last10[n] == 0.0F || last10[n] <= 1.0F)
                continue;
            div++;
            average10 += last10[n];
            if(last10[n] < lowest)
                lowest = last10[n];
            if(last10[n] > hightest)
                hightest = last10[n];
        }

        if(div > 0)
            average10 /= div;
        else
            average10 = 60F;
        GameTime.instance.FPSMultiplier = 60F / average10;
        if(getTime() - lastFPS > 1000L)
        {
            fps = 0L;
            lastFPS += 1000L;
        }
        fps++;
    }

    public static void run()
    {
    	/*  640 */     LuaEventManager.TriggerEvent("OnGameBoot");
    	/*      */     try {
    	/*  642 */       initApplet();
    	/*      */     } catch (Exception ex) {
    	/*  644 */       Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
    	/*      */     }

    	/*  646 */     start();
    	/*      */     while (  (!Display.isCloseRequested()) && (!closeRequested)    ) {
    	/*      */         try
    	/*      */         {
    	/*  658 */           Display.update();
    	/*      */ 
    	/*  665 */           logic();
    	/*  666 */           updateFPS();
    	/*      */ 
    	/*  669 */           Core.getInstance().setScreenSize(Display.getWidth(), Display.getHeight());
    	/*      */ 
    	/*  673 */           render();
    	/*      */ 
    	/*  678 */           Display.sync(60);
    	/*      */         }
    	/*      */         catch (Exception ex)
    	/*      */         {
    	/*  703 */           JOptionPane.showMessageDialog(null, ex.getStackTrace(), "Error: " + ex.getMessage(), 0);
    	/*  704 */           Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
    	/*      */         }
    	/*      */       
    	/*      */     }
    	/*      */ 
    	/*      */     try
    	/*      */     {
    	/*  711 */       save(true);
    	/*      */     } catch (FileNotFoundException ex) {
    	/*  713 */       Logger.getLogger(FrameLoader.class.getName()).log(Level.SEVERE, null, ex);
    	/*      */     } catch (IOException ex) {
    	/*  715 */       Logger.getLogger(FrameLoader.class.getName()).log(Level.SEVERE, null, ex);
    	/*      */     }
    	/*      */ 
    	/*  718 */     Display.destroy();
    	/*      */ 
    	/*  720 */     System.exit(0);
    }

    private static void cleanup()
    {
        Display.destroy();
        AL.destroy();
    }

    private static void init(boolean fullscreen)
        throws Exception
    {
        Display.setResizable(true);
        Display.setTitle("Project Zomboid");
        Display.setFullscreen(fullscreen);
        Core.getInstance().init(FrameLoader.FullX, FrameLoader.FullY);
        String path = (new StringBuilder()).append(getCacheDir()).append(File.separator).toString();
        File file = new File(path);
        if(!file.exists())
            file.mkdirs();
        path = (new StringBuilder()).append(path).append("2133243254543.log").toString();
        path = path;
        File makefile = new File(path);
        try
        {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run()
                {
                    GameWindow.login();
                }

            }
);
        }
        catch(Exception ex) { }
    }

    public static void save(boolean bDoChars)
        throws FileNotFoundException, IOException
    {
        LuaEventManager.TriggerEvent("OnSave");
        if(IsoWorld.instance.CurrentCell == null)
            return;
        if(DoFrame)
        {
            Core.getInstance().StartFrame();
            IsoWorld.instance.render();
            Core.getInstance().EndFrame();
            Core.getInstance().StartFrameUI();
            TextManager.instance.DrawStringCentre(Core.getInstance().getOffscreenWidth() / 2, Core.getInstance().getOffscreenHeight() / 2, "Saving", 1.0F, 1.0F, 1.0F, 1.0F);
            Core.getInstance().EndFrameUI();
            Display.update();
        }
        try
        {
            File outFile = new File((new StringBuilder()).append(getGameModeCacheDir()).append(File.separator).append(IsoWorld.instance.playerCell).append("map.bin").toString());
            FileOutputStream outStream = new FileOutputStream(outFile);
            DataOutputStream output = new DataOutputStream(outStream);
            IsoWorld.instance.CurrentCell.save(output, bDoChars);
            outStream.close();
        }
        catch(Exception ex)
        {
            Logger.getLogger(FrameLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            File outFile = new File((new StringBuilder()).append(getGameModeCacheDir()).append(File.separator).append("pc.bin").toString());
            FileOutputStream outStream = new FileOutputStream(outFile);
            DataOutputStream output = new DataOutputStream(outStream);
            output.writeInt(IsoWorld.instance.x);
            output.writeInt(IsoWorld.instance.y);
            WriteString(output, IsoWorld.instance.playerCell);
            outStream.close();
        }
        catch(Exception ex)
        {
            Logger.getLogger(FrameLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void login()
    {
        AuthenticatingFrame f;
        if(FrameLoader.makefile.exists())
        {
            FileInputStream dis = null;
            try
            {
                dis = new FileInputStream(FrameLoader.makefile);
            }
            catch(FileNotFoundException ex)
            {
                Logger.getLogger(FrameLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
            DataInputStream in = new DataInputStream(dis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            try
            {
                String inputLine = null;
                do
                {
                    if((inputLine = br.readLine()) == null)
                        break;
                    if(inputLine.contains("username:"))
                    {
                        inputLine = inputLine.replace("username:", "");
                        lastU = inputLine;
                    }
                    if(inputLine.contains("password:"))
                    {
                        inputLine = inputLine.replace("password:", "");
                        lastP = inputLine;
                    }
                    if(inputLine.contains("key:"))
                    {
                        inputLine = inputLine.replace("key:", "");
                        lastK = inputLine;
                    }
                } while(true);
            }
            catch(IOException ex)
            {
                Logger.getLogger(FrameLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
            f = new AuthenticatingFrame(lastU, lastP, lastK) {

                public boolean allow(String username, char pw[], String key)
                {
                    try
                    {
                        return GameWindow.validateUser(username, new String(pw), key);
                    }
                    catch(MalformedURLException ex)
                    {
                        Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    catch(IOException ex)
                    {
                        Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return false;
                }

            }
;
        } else
        {
            f = new AuthenticatingFrame() {

                public boolean allow(String username, char pw[], String key)
                {
                    try
                    {
                        return GameWindow.validateUser(username, new String(pw), key);
                    }
                    catch(MalformedURLException ex)
                    {
                        Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    catch(IOException ex)
                    {
                        Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return false;
                }

            }
;
        }
        f.setGui();
        f.getContentPane().add(new JLabel("n!"));
        f.pack();
        f.setVisible(true);
        f.setAlwaysOnTop(true);
        f.setLocationRelativeTo(FrameLoader.canvas);
    }

    private static boolean validateUser(String usr, String pwd, String key)
        throws MalformedURLException, IOException
    {
label0:
        {
            String str = null;
            BufferedReader in;
            try
            {
                URL yahoo = new URL("http://127.0.0.1/external/games/projectzomboid.php");
                URLConnection yc2 = yahoo.openConnection();
                InputStream strw = yc2.getInputStream();
                byte b[] = new byte[7];
                strw.read(b);
                if(b[0] != 115 || b[1] != 117 || b[2] != 99 || b[3] != 99 || b[4] != 101)
                    throw new NullPointerException(str);
                strw.close();
                break label0;
            }
            catch(Exception ex)
            {
                if(usr != null && !usr.isEmpty())
                    str = (new StringBuilder()).append("http://www.desura.com/external/games/projectzomboid.php?username=").append(usr).append("&password=").append(pwd).toString();
                else
                    str = (new StringBuilder()).append("http://www.desura.com/external/games/projectzomboid.php?cdkey=").append(key).toString();
                URL yahoo = new URL(str);
                URLConnection yc = yahoo.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            }
            String inputLine;
            do
                if((inputLine = in.readLine()) == null)
                    break label0;
            while(!inputLine.contains("success"));
            return true;
        }
        return false;
    }

    public static String getCacheDir()
    {
        String cacheDir = System.getProperty("deployment.user.cachedir");
        if(cacheDir == null || System.getProperty("os.name").startsWith("Win"))
            cacheDir = System.getProperty("user.home");
        return (new StringBuilder()).append(cacheDir).append(File.separator).append("Zomboid").toString();
    }

    public static void WriteString(ByteBuffer output, String str)
    {
        if(str == null)
        {
            output.putInt(0);
            return;
        }
        output.putInt(str.length());
        if(str != null && str.length() >= 0)
        {
            for(int n = 0; n < str.length(); n++)
                output.putChar(str.charAt(n));

        }
    }

    public static void WriteString(DataOutputStream output, String str)
        throws IOException
    {
        if(str == null)
        {
            output.writeInt(0);
            return;
        }
        output.writeInt(str.length());
        if(str != null && str.length() >= 0)
            output.writeChars(str);
    }

    public static String ReadString(ByteBuffer input)
    {
        int len = input.getInt();
        if(len == 0)
            return "";
        String className = "";
        for(int n = 0; n < len; n++)
            className = (new StringBuilder()).append(className).append(input.getChar()).toString();

        return className;
    }

    public static String ReadString(DataInputStream input)
        throws IOException
    {
        int len = input.readInt();
        if(len == 0)
            return "";
        String className = "";
        for(int n = 0; n < len; n++)
            className = (new StringBuilder()).append(className).append(input.readChar()).toString();

        return className;
    }

    public static String getGameModeCacheDir()
    {
        String cacheDir = System.getProperty("deployment.user.cachedir");
        if(cacheDir == null || System.getProperty("os.name").startsWith("Win"))
            cacheDir = System.getProperty("user.home");
        return (new StringBuilder()).append(cacheDir).append(File.separator).append("Zomboid").append(File.separator).append(Core.GameMode).toString();
    }

    public static Input GameInput = new Input(1);
    static int PauseKeyDebounce = 0;
    public static int ActiveController = -1;
    public static int XLAxis = 0;
    public static int YLAxis = 0;
    public static int XRAxis = 0;
    public static int YRAxis = 0;
    private static final int FRAMERATE = 30;
    public static String lastU = null;
    public static String lastP = null;
    public static String lastK = null;
    public static final String GAME_TITLE = "Project Zomboid";
    public static GameStateMachine states = new GameStateMachine();
    public static CharacterCreationState CharacterCreationStateHandle = null;
    public static LineDrawer debugLine = new LineDrawer();
    public static boolean bDrawMouse = true;
    static Texture MousePointer;
    private static boolean finished;
    static boolean keyDown;
    public static boolean ActivatedJoyPad = false;
    public static boolean DoFrame = true;
    public static String version = "0.2.0q";
    public static SoundEngine SoundEngine = new SoundEngine();
    static long lastFrame = 0L;
    static long lastFPS = 0L;
    public static float average10 = 0.0F;
    static float total10 = 0.0F;
    static float last10[] = new float[100];
    static int last10index = 0;
    static long fps = 0L;
    static long last = 0L;
    public static volatile boolean closeRequested;


}