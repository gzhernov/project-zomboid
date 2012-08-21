// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Core.java

package zombie.core;

import java.awt.Canvas;
import java.awt.image.BufferedImage;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Stack;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;
import zombie.FrameLoader;
import zombie.IndieGL;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.opengl.OpenGLState;
import zombie.core.opengl.Shader;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureFBO;
import zombie.core.textures.TextureID;
import zombie.iso.Vector2;
import zombie.ui.*;

// Referenced classes of package zombie.core:
//            SpriteRenderer

public class Core
{

    public Core()
    {
        lastHeight = 0;
        lastWidth = 0;
        bUseShaders = true;
        nGraphicLevel = 5;
        lviewwid = -1;
        lviewhei = -1;
        CircleVecs = new Vector2[32];
    }

    public static int getGLMajorVersion()
    {
        if(glMajorVersion == -1)
            getOpenGLVersions();
        return glMajorVersion;
    }

    public static String getGLVersion()
    {
        if(glVersion == null)
            getOpenGLVersions();
        return glVersion;
    }

    public static Core getInstance()
    {
        return core;
    }

    public static void getOpenGLVersions()
    {
        glVersion = GL11.glGetString(7938);
        glMajorVersion = glVersion.charAt(0) - 48;
    }

    public static void setFullScreen(boolean flag)
        throws LWJGLException
    {
    }

    public static boolean supportNPTTexture()
    {
        return false;
    }

    void sharedInit()
    {
    }

    public void MoveMethodToggle()
    {
        bAltMoveMethod = !bAltMoveMethod;
    }

    public void doubleSizeToggle()
    {
        bDoubleSize = !bDoubleSize;
        if(bDoubleSize)
        {
            Core _tmp = this;
            offscreenwidth = width / 2;
            Core _tmp1 = this;
            offscreenheight = height / 2;
        } else
        {
            Core _tmp2 = this;
            offscreenwidth = width;
            Core _tmp3 = this;
            offscreenheight = height;
            Core _tmp4 = this;
            offscreenwidth = width;
            Core _tmp5 = this;
            offscreenheight = height;
            OffscreenTexture = null;
            OffscreenBuffer = null;
        }
        UIManager.resize();
    }

    public void EndFrame()
    {
        GL11.glPopAttrib();
        GL11.glMatrixMode(5889);
        GL11.glPopMatrix();
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        if(CurrentTextEntryBox == null)
            while(Keyboard.next()) ;
        else
        if(CurrentTextEntryBox.IsEditable && CurrentTextEntryBox.DoingTextEntry)
            do
            {
                if(!Keyboard.next())
                    break;
                if(Keyboard.getEventKeyState())
                {
                    if(Keyboard.getEventKey() == 1 || Keyboard.getEventKey() == 28)
                    {
                        boolean CommandEntered = false;
                        if(Keyboard.getEventKey() == 28 && UIManager.getDebugConsole() != null && CurrentTextEntryBox == UIManager.getDebugConsole().CommandLine)
                            CommandEntered = true;
                        if(CommandEntered)
                        {
                            UIManager.getDebugConsole().ProcessCommand();
                        } else
                        {
                            CurrentTextEntryBox.DoingTextEntry = false;
                            if(CurrentTextEntryBox.Frame != null)
                                CurrentTextEntryBox.Frame.Colour = CurrentTextEntryBox.StandardFrameColour;
                            CurrentTextEntryBox = null;
                        }
                    }
                    if(Keyboard.getEventKey() == 203)
                    {
                        CurrentTextEntryBox.TextEntryCursorPos--;
                        if(CurrentTextEntryBox.TextEntryCursorPos < 0)
                            CurrentTextEntryBox.TextEntryCursorPos = 0;
                    } else
                    if(Keyboard.getEventKey() == 205)
                    {
                        CurrentTextEntryBox.TextEntryCursorPos++;
                        if(CurrentTextEntryBox.TextEntryCursorPos > CurrentTextEntryBox.Text.length())
                            CurrentTextEntryBox.TextEntryCursorPos = CurrentTextEntryBox.Text.length();
                    } else
                    if(CurrentTextEntryBox.DoingTextEntry)
                        if(Keyboard.getEventKey() == 211 || Keyboard.getEventKey() == 14)
                        {
                            if(CurrentTextEntryBox.Text.length() > 0)
                            {
                                if(CurrentTextEntryBox.TextEntryCursorPos < CurrentTextEntryBox.Text.length())
                                    CurrentTextEntryBox.Text = (new StringBuilder()).append(CurrentTextEntryBox.Text.substring(0, CurrentTextEntryBox.TextEntryCursorPos - 1)).append(CurrentTextEntryBox.Text.substring(CurrentTextEntryBox.TextEntryCursorPos)).toString();
                                else
                                    CurrentTextEntryBox.Text = CurrentTextEntryBox.Text.substring(0, CurrentTextEntryBox.Text.length() - 1);
                                CurrentTextEntryBox.TextEntryCursorPos--;
                            }
                        } else
                        if(Keyboard.getEventKey() != 42 && Keyboard.getEventKey() != 54 && CurrentTextEntryBox.Text.length() <= CurrentTextEntryBox.TextEntryMaxLength)
                        {
                            if(CurrentTextEntryBox.TextEntryCursorPos < CurrentTextEntryBox.Text.length())
                                CurrentTextEntryBox.Text = (new StringBuilder()).append(CurrentTextEntryBox.Text.substring(0, CurrentTextEntryBox.TextEntryCursorPos)).append(Keyboard.getEventCharacter()).append(CurrentTextEntryBox.Text.substring(CurrentTextEntryBox.TextEntryCursorPos)).toString();
                            else
                                CurrentTextEntryBox.Text = (new StringBuilder()).append(CurrentTextEntryBox.Text).append(Keyboard.getEventCharacter()).toString();
                            CurrentTextEntryBox.TextEntryCursorPos++;
                        }
                }
            } while(true);
    }

    public void CalcCircle()
    {
        Vector2 Vec = new Vector2(0.0F, -1F);
        for(int n = 0; n < 32; n++)
        {
            CircleVecs[n] = new Vector2(Vec.x, Vec.y);
            Vec.rotate(0.1963495F);
        }

    }

    public void DrawCircle(float f, float f1, float f2)
    {
    }

    public void EndFrameUI()
    {
        SpriteRenderer.instance.postRender();
        GL11.glPopAttrib();
        GL11.glMatrixMode(5889);
        GL11.glPopMatrix();
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
    }

    public static void UnfocusActiveTextEntryBox()
    {
        if(CurrentTextEntryBox != null)
        {
            CurrentTextEntryBox.DoingTextEntry = false;
            if(CurrentTextEntryBox.Frame != null)
                CurrentTextEntryBox.Frame.Colour = CurrentTextEntryBox.StandardFrameColour;
            CurrentTextEntryBox = null;
        }
    }

    public int getOffscreenHeight()
    {
        return offscreenheight;
    }

    public int getOffscreenWidth()
    {
        return offscreenwidth;
    }

    public int getScreenHeight()
    {
        return height;
    }

    public int getScreenWidth()
    {
        return width;
    }

    public static void setDisplayMode(int width, int height, boolean fullscreen)
    {
        if(Display.getDisplayMode().getWidth() == width && Display.getDisplayMode().getHeight() == height && Display.isFullscreen() == fullscreen)
            return;
        DisplayMode targetDisplayMode;
        targetDisplayMode = null;
        try
        {
        	
        if(fullscreen)
        {
            DisplayMode modes[] = Display.getAvailableDisplayModes();
            int freq = 0;
            int i = 0;
            do
            {
                if(i >= modes.length)
                    break;
                DisplayMode current = modes[i];
                if(current.getWidth() == width && current.getHeight() == height)
                {
                    if((targetDisplayMode == null || current.getFrequency() >= freq) && (targetDisplayMode == null || current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel()))
                    {
                        targetDisplayMode = current;
                        freq = targetDisplayMode.getFrequency();
                    }
                    if(current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel() && current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())
                    {
                        targetDisplayMode = current;
                        break;
                    }
                }
                i++;
            } while(true);
        } else
        {
            targetDisplayMode = new DisplayMode(width, height);
        }
        if(targetDisplayMode == null)
        {
            System.out.println((new StringBuilder()).append("Failed to find value mode: ").append(width).append("x").append(height).append(" fs=").append(fullscreen).toString());
            return;
        }
        
            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);
        }
        catch(LWJGLException e)
        {
            System.out.println((new StringBuilder()).append("Unable to setup mode ").append(width).append("x").append(height).append(" fullscreen=").append(fullscreen).append(e).toString());
        }
        return;
    }

    public void init(int width, int height)
        throws LWJGLException
    {
        Core _tmp = this;
        width = width;
        Core _tmp1 = this;
        height = height;
        Core _tmp2 = this;
        offscreenwidth = width;
        Core _tmp3 = this;
        offscreenheight = height;
        Display.setVSyncEnabled(true);
        if(!FrameLoader.bFullscreen)
            Display.setDisplayMode(new DisplayMode(width, height));
        Display.create(new PixelFormat(32, 0, 24, 8, 0));
        if(FrameLoader.bFullscreen)
            setDisplayMode(width, height, FrameLoader.bFullscreen);
        ByteBuffer b = ByteBuffer.allocateDirect(128);
        java.nio.IntBuffer i = b.asIntBuffer();
        GL11.glGetInteger(3415, i);
        String bits = (new StringBuilder()).append("Bits: ").append(b.get()).toString();
        Display.setVSyncEnabled(true);
        GL11.glEnable(3553);
        GL13.glActiveTexture(33985);
        OpenGLState.enableBlending();
        IndieGL.glBlendFunc(770, 771);
        GL11.glTexEnvf(8960, 8704, 8448F);
        GL11.glEnable(3553);
        GL13.glActiveTexture(33984);
        GL11.glTexEnvf(8960, 8704, 8448F);
        OpenGLState.enableBlending();
        IndieGL.glBlendFunc(770, 771);
        sharedInit();
    }

    public void init(int width, int height, int offscreenwidth, int offscreenheight, Canvas canvas, Canvas full)
        throws LWJGLException
    {
        Core _tmp = this;
        width = width;
        Core _tmp1 = this;
        height = height;
        Core _tmp2 = this;
        offscreenwidth = width;
        Core _tmp3 = this;
        offscreenheight = height;
        if(height > 768)
        {
            bDoubleSize = true;
            if(OffscreenBuffer == null)
            {
                Core _tmp4 = this;
                offscreenwidth = width / 2;
                Core _tmp5 = this;
                offscreenheight = height / 2;
            }
        }
        Core _tmp6 = this;
        canvas = canvas;
        Core _tmp7 = this;
        fullscreencanvas = full;
        Display.setVSyncEnabled(true);
        GL11.glEnable(3553);
        GL11.glTexEnvf(8960, 8704, 8448F);
        OpenGLState.enableBlending();
        IndieGL.glBlendFunc(770, 771);
        GL11.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
        sharedInit();
    }

    public void setScreenSize(int width, int height)
    {
        if (FrameLoader.bFullscreen) {
  	      return;
  	    }
  	    if ((this.width != width) || (this.height != height))
  	    {
  	      offscreenwidth = Core.width = width;
  	      offscreenheight = Core.height = height;

  	      if (bDoubleSize)
  	      {
  	        offscreenwidth /= 2;
  	        offscreenheight /= 2;
  	      }

  	      GL11.glViewport(0, 0, offscreenwidth, offscreenheight);
  	      this.lastWidth = offscreenwidth;
  	      this.lastHeight = offscreenheight;
  	    }
    }

    public void StartFrame()
    {
        SpriteRenderer.instance.preRender();
        if(!bDoubleSize)
        {
            Core _tmp = this;
            offscreenwidth = width;
            Core _tmp1 = this;
            offscreenheight = height;
            Core _tmp2 = this;
            offscreenwidth = width;
            Core _tmp3 = this;
            offscreenheight = height;
        }
        UIManager.resize();
        boolean PlayerTripping = false;
        if(IsoPlayer.getInstance() != null && IsoPlayer.getInstance().getStats().Tripping)
            PlayerTripping = true;
        TextureID.TextureIDStack.clear();
        Texture.BindCount = 0;
        if(!PlayerTripping)
            GL11.glClear(18176);
        GL11.glLoadIdentity();
        GL11.glAlphaFunc(516, 0.0F);
        GL11.glPushAttrib(2048);
        GL11.glMatrixMode(5889);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GLU.gluOrtho2D(0.0F, offscreenwidth, offscreenheight, 0.0F);
        GL11.glMatrixMode(5888);
        GL11.glViewport(0, 0, offscreenwidth, offscreenheight);
        lastWidth = offscreenwidth;
        lastHeight = offscreenheight;
        GL11.glLoadIdentity();
        if(DoFiltering)
        {
            GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
        }
    }

    public void StartFrameUI()
    {
        GL11.glLoadIdentity();
        IndieGL.glAlphaFunc(516, 0.0F);
        GL11.glPushAttrib(2048);
        GL11.glMatrixMode(5889);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GLU.gluOrtho2D(0.0F, width, height, 0.0F);
        GL11.glMatrixMode(5888);
        GL11.glViewport(0, 0, width, height);
        lastWidth = offscreenwidth;
        lastHeight = offscreenheight;
        GL11.glLoadIdentity();
        if(!DoFiltering);
        UIManager.resize();
    }

    static Canvas canvas;
    static Canvas fullscreencanvas;
    public static boolean bDoubleSize = false;
    public static boolean bAltMoveMethod = false;
    static TextureFBO OffscreenBuffer;
    static Texture OffscreenTexture;
    public static boolean useLwjgl = true;
    public static boolean DoFiltering = false;
    static int width = 800;
    static int height = 600;
    static int offscreenwidth = 800;
    static int offscreenheight = 600;
    public static int MaxJukeBoxesActive = 10;
    public static int NumJukeBoxesActive = 0;
    public static String GameMode = "Sandbox";
    private static String glVersion;
    private static int glMajorVersion = -1;
    private static Core core = new Core();
    public static boolean bDebug = false;
    public int lastHeight;
    public int lastWidth;
    public static UITextBox2 CurrentTextEntryBox = null;
    public static String storyDirectory = "mods/";
    public static String modRootDirectory = "mods/media/";
    public Shader shaderTest;
    public boolean bUseShaders;
    public int nGraphicLevel;
    int lviewwid;
    int lviewhei;
    public Vector2 CircleVecs[];
    ByteBuffer buffer;
    BufferedImage image;
    Texture buffertexture;

}
