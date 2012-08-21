// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:44
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   GameApplet.java

package zombie;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import zombie.characters.IsoPlayer;
import zombie.core.Core;

// Referenced classes of package zombie:
//            GameWindow

public class GameApplet extends Applet
    implements MouseListener, MouseMotionListener
{

    public GameApplet()
    {
        frame = new Frame();
        view_rotx = 20F;
        view_roty = 30F;
    }

    public void destroy()
    {
        remove(display_parent);
        super.destroy();
        System.out.println("Clear up");
    }

    public void drawLoop()
    {
        GameWindow.render();
    }

    public void gameLoop()
    {

/*  87 */     long startTime = System.currentTimeMillis() + 5000L;
/*  88 */     long fps = 0L;
/*     */     while (this.running) {
/*  90 */       
/*     */         try
/*     */         {
/*  96 */           Core.getInstance().setScreenSize(getWidth(), getHeight());
/*  97 */           Display.update();
/*     */ 
/* 100 */           drawLoop();
/*     */ 
/* 102 */           if ((IsoPlayer.getInstance() == null) || (!IsoPlayer.instance.isAsleep()))
/*     */           {
/* 104 */             Display.sync(30);
/*     */           }
/*     */ 
/* 110 */           GameWindow.logic();
/*     */         }
/*     */         catch (Exception ex)
/*     */         {
/* 132 */           JOptionPane.showMessageDialog(null, ex.getStackTrace(), "Error", 0);
/* 133 */           Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
/*     */         }
/*     */ 
/*     */       
/*     */ 
/*     */     }
/*     */ 
/* 167 */     Display.destroy();
        
        
        
    }

    public void init()
    {
        setLayout(new BorderLayout());
        try
        {
            display_parent = new Canvas() {

                public void addNotify()
                {
                    super.addNotify();
                    startLWJGL();
                }

                public void removeNotify()
                {
                    stopLWJGL();
                    super.removeNotify();
                }

              

            
         
            }
;
            display_parent.setSize(getWidth(), getHeight());
            add(display_parent);
            display_parent.setFocusable(true);
            display_parent.requestFocus();
            display_parent.setIgnoreRepaint(true);
            addMouseListener(this);
            addMouseMotionListener(this);
            display_parent.setCursor(null);
            setVisible(true);
        }
        catch(Exception e)
        {
            System.err.println(e);
            throw new RuntimeException("Unable to create display");
        }
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mouseDragged(MouseEvent e)
    {
        mx = e.getX();
        my = e.getY();
        e.consume();
    }

    public void mouseEntered(MouseEvent e)
    {
        GameWindow.bDrawMouse = true;
    }

    public void mouseExited(MouseEvent e)
    {
        GameWindow.bDrawMouse = false;
    }

    public void mouseMoved(MouseEvent e)
    {
        mx = e.getX();
        my = e.getY();
        e.consume();
    }

    public void mousePressed(MouseEvent e)
    {
        if(e.getButton() == 1)
            left = true;
        if(e.getButton() == 2)
            right = true;
        if(e.getButton() == 3)
            middle = true;
    }

    public void mouseReleased(MouseEvent e)
    {
        if(e.getButton() == 1)
            left = false;
        if(e.getButton() == 2)
            right = false;
        if(e.getButton() == 3)
            middle = false;
    }

    public void start()
    {
    }

    public void startLWJGL()
    {
        gameThread = new Thread() {

            public void run()
            {
                running = true;
                try
                {
                    Display.setParent(display_parent);
                    Display.create();
                    Core.getInstance().init(800, 600, 800, 600, display_parent, display_parent_full);
                    initGL();
                    try
                    {
                        GameWindow.initApplet();
                    }
                    catch(Exception ex)
                    {
                        Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                catch(LWJGLException e)
                {
                    e.printStackTrace();
                }
                gameLoop();
            }

           
        }
;
        gameThread.start();
    }

    public void stop()
    {
    }

    protected void initGL()
    {
    }

    private void stopLWJGL()
    {
        running = false;
        try
        {
            gameThread.join();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public static boolean left = false;
    public static boolean middle = false;
    public static int mx = 0;
    public static int my = 0;
    public static boolean right = false;
    Canvas display_parent;
    Canvas display_parent_full;
    Frame frame;
    Thread gameThread;
    boolean keyDown;
    boolean running;
    private float angle;
    private int gear1;
    private int gear2;
    private int gear3;
    private boolean mouseButtonDown;
    private int prevMouseX;
    private int prevMouseY;
    private float view_rotx;
    private float view_roty;
    private float view_rotz;


}