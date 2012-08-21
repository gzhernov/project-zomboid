// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Mouse.java

package zombie.input;

import gnu.trove.map.hash.THashMap;
import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import zombie.core.Core;
import zombie.core.utils.Utility;
import zombie.interfaces.ITexture;

public class Mouse
{

    public Mouse()
    {
    }

    public static int getDeltaX()
    {
        return oldx - x;
    }

    public static int getDeltaY()
    {
        return oldy - y;
    }

    public static int getWheelState()
    {
        if(org.lwjgl.input.Mouse.isCreated())
            return org.lwjgl.input.Mouse.getDWheel();
        else
            return 0;
    }

    public static synchronized int getX()
    {
        return (int)(((float)x / (float)Core.getInstance().getScreenWidth()) * (float)Core.getInstance().getOffscreenWidth());
    }

    public static synchronized int getXA()
    {
        return x;
    }

    public static synchronized int getYA()
    {
        return y;
    }

    public static synchronized int getY()
    {
        return (int)(((float)y / (float)Core.getInstance().getScreenHeight()) * (float)Core.getInstance().getOffscreenHeight());
    }

    public static boolean isButtonDown(int number)
    {
        if(org.lwjgl.input.Mouse.isCreated())
            return org.lwjgl.input.Mouse.isButtonDown(number);
        else
            return false;
    }

    public static boolean isGuiMode()
    {
        return guiMode;
    }

    public static boolean isLeftDown()
    {
        return left;
    }

    public static boolean isLeftPressed()
    {
        return !oldLeft && left;
    }

    public static boolean isLeftReleased()
    {
        return oldLeft && !left;
    }

    public static boolean isLeftUp()
    {
        return !left;
    }

    public static boolean isMiddleDown()
    {
        return middle;
    }

    public static boolean isMiddlePressed()
    {
        return !oldMiddle && middle;
    }

    public static boolean isMiddleReleased()
    {
        return oldMiddle && !middle;
    }

    public static boolean isMiddleUp()
    {
        return !middle;
    }

    public static boolean isRightDown()
    {
        return right;
    }

    public static boolean isRightPressed()
    {
        return !oldRight && right;
    }

    public static boolean isRightReleased()
    {
        return oldRight && !right;
    }

    public static boolean isRightUp()
    {
        return !right;
    }

    public static synchronized void loop()
    {
        if(!org.lwjgl.input.Mouse.isCreated())
        {
            return;
        } else
        {
            oldx = x;
            oldy = y;
            oldLeft = left;
            oldRight = right;
            oldMiddle = middle;
            x = org.lwjgl.input.Mouse.getX();
            y = Core.getInstance().getScreenHeight() - org.lwjgl.input.Mouse.getY() - 1;
            left = org.lwjgl.input.Mouse.isButtonDown(0);
            right = org.lwjgl.input.Mouse.isButtonDown(1);
            middle = org.lwjgl.input.Mouse.isButtonDown(2);
            oldSomeOne = someOne;
            someOne = left || right || middle;
            return;
        }
    }

    public static void resetClickableObjects()
    {
    }

    public static synchronized void setCursor(ITexture image)
    {
        if(!useLwjgl)
        {
            Utility.error("this function don't works in the Swing/Awt+OpenGL combination!", "Mouse.setCursor(ITexture)");
            return;
        }
        if(image.getWidth() > 32 || image.getHeight() > 32)
            Utility.error("The image of cursor should be a 32x32 image", "EWMouse.setCursor(Texture)");
        ByteBuffer bb = image.getData();
        for(int i = image.getHeightHW() * image.getHeightHW() - 1; i >= 0; i--)
        {
            bb.mark();
            byte r = bb.get();
            byte g = bb.get();
            byte b = bb.get();
            byte a = bb.get();
            bb.reset();
            bb.put(a);
            bb.put(r);
            bb.put(g);
            bb.put(b);
        }

        image.setData(bb);
        try
        {
            Cursor cursor;
            if(!CursorMap.containsKey(Integer.valueOf(image.getID())))
            {
                cursor = new Cursor(image.getWidth(), image.getHeight(), 0, image.getHeight() - 1, 1, image.getData().asIntBuffer(), null);
                CursorMap.put(Integer.valueOf(image.getID()), cursor);
            } else
            {
                cursor = (Cursor)CursorMap.get(Integer.valueOf(image.getID()));
            }
            org.lwjgl.input.Mouse.setNativeCursor(cursor);
        }
        catch(LWJGLException e)
        {
            Utility.error("Error on setCursor(Texture) in EWMouse", e);
        }
    }

    public static void setGuiMode(boolean guiMode)
    {
        guiMode = guiMode;
    }

    public static synchronized void setXY(int x, int y)
    {
        org.lwjgl.input.Mouse.setCursorPosition(x, Core.getInstance().getOffscreenHeight() - 1 - y);
    }

    public static boolean useLwjgl;
    protected static int bx;
    protected static int by;
    protected static boolean bleft;
    protected static boolean bright;
    protected static boolean bmiddle;
    protected static int x;
    protected static int y;
    protected static int oldx;
    protected static int oldy;
    protected static boolean oldSomeOne;
    protected static boolean someOne;
    public static boolean left;
    public static boolean oldLeft;
    public static boolean right;
    public static boolean oldRight;
    public static boolean middle;
    public static boolean oldMiddle;
    protected static boolean guiMode = false;
    public static boolean collisionMode = false;
    public static int hotspotX = 0;
    public static int hotspotY = 0;
    static THashMap CursorMap = new THashMap();

    static 
    {
        useLwjgl = Core.useLwjgl;
    }
}
