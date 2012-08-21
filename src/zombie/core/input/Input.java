// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Input.java

package zombie.core.input;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.*;
import org.lwjgl.opengl.Display;

// Referenced classes of package zombie.core.input:
//            KeyListener, MouseListener, ControlledInputReciever, ControllerListener, 
//            InputListener

public class Input
{
    private class NullOutputStream extends OutputStream
    {

        public void write(int i)
            throws IOException
        {
        }

       

        private NullOutputStream()
        {
          
        }
    }


    public static void disableControllers()
    {
        controllersInited = true;
    }

    public Input(int height)
    {
        mousePressed = new boolean[10];
        controllerPressed = new boolean[100][100];
        keys = new char[1024];
        pressed = new boolean[1024];
        nextRepeat = new long[1024];
        controls = new boolean[10][110];
        consumed = false;
        allListeners = new HashSet();
        keyListeners = new ArrayList();
        keyListenersToAdd = new ArrayList();
        mouseListeners = new ArrayList();
        mouseListenersToAdd = new ArrayList();
        controllerListeners = new ArrayList();
        displayActive = true;
        scaleX = 1.0F;
        scaleY = 1.0F;
        xoffset = 0.0F;
        yoffset = 0.0F;
        doubleClickDelay = 250;
        doubleClickTimeout = 0L;
        pressedX = -1;
        pressedY = -1;
        mouseClickTolerance = 5;
        presseda = new boolean[20];
        init(height);
    }

    public void setDoubleClickInterval(int delay)
    {
        doubleClickDelay = delay;
    }

    public void setMouseClickTolerance(int mouseClickTolerance)
    {
        this.mouseClickTolerance = mouseClickTolerance;
    }

    public void setScale(float scaleX, float scaleY)
    {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public void setOffset(float xoffset, float yoffset)
    {
        this.xoffset = xoffset;
        this.yoffset = yoffset;
    }

    public void resetInputTransform()
    {
        setOffset(0.0F, 0.0F);
        setScale(1.0F, 1.0F);
    }

    public void addListener(InputListener listener)
    {
        addKeyListener(listener);
        addMouseListener(listener);
        addControllerListener(listener);
    }

    public void addKeyListener(KeyListener listener)
    {
        keyListenersToAdd.add(listener);
    }

    private void addKeyListenerImpl(KeyListener listener)
    {
        if(keyListeners.contains(listener))
        {
            return;
        } else
        {
            keyListeners.add(listener);
            allListeners.add(listener);
            return;
        }
    }

    public void addMouseListener(MouseListener listener)
    {
        mouseListenersToAdd.add(listener);
    }

    private void addMouseListenerImpl(MouseListener listener)
    {
        if(mouseListeners.contains(listener))
        {
            return;
        } else
        {
            mouseListeners.add(listener);
            allListeners.add(listener);
            return;
        }
    }

    public void addControllerListener(ControllerListener listener)
    {
        if(controllerListeners.contains(listener))
        {
            return;
        } else
        {
            controllerListeners.add(listener);
            allListeners.add(listener);
            return;
        }
    }

    public void removeAllListeners()
    {
        removeAllKeyListeners();
        removeAllMouseListeners();
        removeAllControllerListeners();
    }

    public void removeAllKeyListeners()
    {
        allListeners.removeAll(keyListeners);
        keyListeners.clear();
    }

    public void removeAllMouseListeners()
    {
        allListeners.removeAll(mouseListeners);
        mouseListeners.clear();
    }

    public void removeAllControllerListeners()
    {
        allListeners.removeAll(controllerListeners);
        controllerListeners.clear();
    }

    public void addPrimaryListener(InputListener listener)
    {
        removeListener(listener);
        keyListeners.add(0, listener);
        mouseListeners.add(0, listener);
        controllerListeners.add(0, listener);
        allListeners.add(listener);
    }

    public void removeListener(InputListener listener)
    {
        removeKeyListener(listener);
        removeMouseListener(listener);
        removeControllerListener(listener);
    }

    public void removeKeyListener(KeyListener listener)
    {
        keyListeners.remove(listener);
        if(!mouseListeners.contains(listener) && !controllerListeners.contains(listener))
            allListeners.remove(listener);
    }

    public void removeControllerListener(ControllerListener listener)
    {
        controllerListeners.remove(listener);
        if(!mouseListeners.contains(listener) && !keyListeners.contains(listener))
            allListeners.remove(listener);
    }

    public void removeMouseListener(MouseListener listener)
    {
        mouseListeners.remove(listener);
        if(!controllerListeners.contains(listener) && !keyListeners.contains(listener))
            allListeners.remove(listener);
    }

    void init(int height)
    {
        this.height = height;
        lastMouseX = getMouseX();
        lastMouseY = getMouseY();
    }

    public static String getKeyName(int code)
    {
        return Keyboard.getKeyName(code);
    }

    public boolean isKeyPressed(int code)
    {
        if(pressed[code])
        {
            pressed[code] = false;
            return true;
        } else
        {
            return false;
        }
    }

    public boolean isMousePressed(int button)
    {
        if(mousePressed[button])
        {
            mousePressed[button] = false;
            return true;
        } else
        {
            return false;
        }
    }

    public boolean isControlPressed(int button)
    {
        return isControlPressed(button, 0);
    }

    public boolean isControlPressed(int button, int controller)
    {
        if(controllerPressed[controller][button])
        {
            controllerPressed[controller][button] = false;
            return true;
        } else
        {
            return false;
        }
    }

    public void clearControlPressedRecord()
    {
        for(int i = 0; i < controllers.size(); i++)
            Arrays.fill(controllerPressed[i], false);

    }

    public void clearKeyPressedRecord()
    {
        Arrays.fill(pressed, false);
    }

    public void clearMousePressedRecord()
    {
        Arrays.fill(mousePressed, false);
    }

    public boolean isKeyDown(int code)
    {
        return Keyboard.isKeyDown(code);
    }

    public int getAbsoluteMouseX()
    {
        return Mouse.getX();
    }

    public int getAbsoluteMouseY()
    {
        return height - Mouse.getY();
    }

    public int getMouseX()
    {
        return (int)((float)Mouse.getX() * scaleX + xoffset);
    }

    public int getMouseY()
    {
        return (int)((float)(height - Mouse.getY()) * scaleY + yoffset);
    }

    public boolean isMouseButtonDown(int button)
    {
        return Mouse.isButtonDown(button);
    }

    private boolean anyMouseDown()
    {
        for(int i = 0; i < 3; i++)
            if(Mouse.isButtonDown(i))
                return true;

        return false;
    }

    public int getControllerCount()
    {
        initControllers();
        return controllers.size();
    }

    public int getAxisCount(int controller)
    {
        return ((Controller)controllers.get(controller)).getAxisCount();
    }

    public float getAxisValue(int controller, int axis)
    {
        return ((Controller)controllers.get(controller)).getAxisValue(axis);
    }

    public String getAxisName(int controller, int axis)
    {
        return ((Controller)controllers.get(controller)).getAxisName(axis);
    }

    public boolean isControllerLeft(int controller)
    {
        if(controller >= getControllerCount())
            return false;
        if(controller == -1)
        {
            for(int i = 0; i < controllers.size(); i++)
                if(isControllerLeft(i))
                    return true;

            return false;
        } else
        {
            return ((Controller)controllers.get(controller)).getXAxisValue() < -0.5F || ((Controller)controllers.get(controller)).getPovX() < -0.5F;
        }
    }

    public boolean isControllerRight(int controller)
    {
        if(controller >= getControllerCount())
            return false;
        if(controller == -1)
        {
            for(int i = 0; i < controllers.size(); i++)
                if(isControllerRight(i))
                    return true;

            return false;
        } else
        {
            return ((Controller)controllers.get(controller)).getXAxisValue() > 0.5F || ((Controller)controllers.get(controller)).getPovX() > 0.5F;
        }
    }

    public boolean isControllerUp(int controller)
    {
        if(controller >= getControllerCount())
            return false;
        if(controller == -1)
        {
            for(int i = 0; i < controllers.size(); i++)
                if(isControllerUp(i))
                    return true;

            return false;
        } else
        {
            return ((Controller)controllers.get(controller)).getYAxisValue() < -0.5F || ((Controller)controllers.get(controller)).getPovY() < -0.5F;
        }
    }

    public boolean isControllerDown(int controller)
    {
        if(controller >= getControllerCount())
            return false;
        if(controller == -1)
        {
            for(int i = 0; i < controllers.size(); i++)
                if(isControllerDown(i))
                    return true;

            return false;
        } else
        {
            return ((Controller)controllers.get(controller)).getYAxisValue() > 0.5F || ((Controller)controllers.get(controller)).getPovY() > 0.5F;
        }
    }

    public boolean isButtonPressed(int index, int controller)
    {
        if(controller >= getControllerCount())
            return false;
        if(controller == -1)
        {
            for(int i = 0; i < controllers.size(); i++)
                if(isButtonPressed(index, i))
                    return true;

            return false;
        } else
        {
            return ((Controller)controllers.get(controller)).isButtonPressed(index);
        }
    }

    public boolean isButtonPressedD(int index, int controller)
    {
        if(controller >= getControllerCount())
            return false;
        if(controller == -1)
        {
            for(int i = 0; i < controllers.size(); i++)
                if(isButtonPressed(index, i))
                    return true;

            return false;
        }
        boolean pres = ((Controller)controllers.get(controller)).isButtonPressed(index);
        if(!pres && presseda[index])
        {
            presseda[index] = false;
            return false;
        }
        if(pres)
        {
            if(presseda[index])
            {
                return false;
            } else
            {
                presseda[index] = true;
                return true;
            }
        } else
        {
            return false;
        }
    }

    public boolean isButton1Pressed(int controller)
    {
        return isButtonPressed(0, controller);
    }

    public boolean isButton2Pressed(int controller)
    {
        return isButtonPressed(1, controller);
    }

    public boolean isButton3Pressed(int controller)
    {
        return isButtonPressed(2, controller);
    }

    public void initControllers()
    {
        if(controllersInited)
            return;
        controllersInited = true;
        try
        {
            Controllers.create();
            int count = Controllers.getControllerCount();
            for(int i = 0; i < count; i++)
            {
                Controller controller = Controllers.getController(i);
                if(controller.getButtonCount() >= 3 && controller.getButtonCount() < 100)
                    controllers.add(controller);
            }

        }
        catch(LWJGLException e)
        {
            if(!(e.getCause() instanceof ClassNotFoundException));
        }
        catch(NoClassDefFoundError e) { }
    }

    public void consumeEvent()
    {
        consumed = true;
    }

    private int resolveEventKey(int key, char c)
    {
        if(c == '=' || key == 0)
            return 13;
        else
            return key;
    }

    public void considerDoubleClick(int button, int x, int y)
    {
        if(doubleClickTimeout == 0L)
        {
            clickX = x;
            clickY = y;
            clickButton = button;
            doubleClickTimeout = System.currentTimeMillis() + (long)doubleClickDelay;
            fireMouseClicked(button, x, y, 1);
        } else
        if(clickButton == button && System.currentTimeMillis() < doubleClickTimeout)
        {
            fireMouseClicked(button, x, y, 2);
            doubleClickTimeout = 0L;
        }
    }

    public void poll(int width, int height)
    {
        if(paused)
        {
            while(Keyboard.next()) ;
            while(Mouse.next()) ;
            return;
        }
        
        for(int i = 0; i < keyListenersToAdd.size(); i++)
            addKeyListenerImpl((KeyListener)keyListenersToAdd.get(i));

        keyListenersToAdd.clear();
        
        for(int i = 0; i < mouseListenersToAdd.size(); i++)
            addMouseListenerImpl((MouseListener)mouseListenersToAdd.get(i));

        mouseListenersToAdd.clear();
        
        if(doubleClickTimeout != 0L && System.currentTimeMillis() > doubleClickTimeout)
            doubleClickTimeout = 0L;
        
        this.height = height;
        
        /* 1191 */     Iterator allStarts = this.allListeners.iterator();
        /* 1192 */     while (allStarts.hasNext()) {
        /* 1193 */       ControlledInputReciever listener = (ControlledInputReciever)allStarts.next();
        /* 1194 */       listener.inputStarted();
        /*      */     }
        

        /* 1197 */     while (Keyboard.next()) {
        	/* 1198 */       if (Keyboard.getEventKeyState()) {
        	/* 1199 */         int eventKey = resolveEventKey(Keyboard.getEventKey(), Keyboard.getEventCharacter());
        	/*      */ 
        	/* 1201 */         this.keys[eventKey] = Keyboard.getEventCharacter();
        	/* 1202 */         this.pressed[eventKey] = true;
        	/* 1203 */         this.nextRepeat[eventKey] = (System.currentTimeMillis() + this.keyRepeatInitial);
        	/*      */ 
        	/* 1205 */         this.consumed = false;
        	/* 1206 */         for (int i = 0; i < this.keyListeners.size(); i++) {
        	/* 1207 */           KeyListener listener = (KeyListener)this.keyListeners.get(i);
        	/*      */ 
        	/* 1209 */           if (listener.isAcceptingInput()) {
        	/* 1210 */             listener.keyPressed(eventKey, Keyboard.getEventCharacter());
        	/* 1211 */             if (this.consumed) {
        	/*      */               break;
        	/*      */             }
        	/*      */           }
        	/*      */         }
        	/* 1216 */         continue;
        	/* 1217 */       }int eventKey = resolveEventKey(Keyboard.getEventKey(), Keyboard.getEventCharacter());
        	/* 1218 */       this.nextRepeat[eventKey] = 0L;
        	/*      */ 
        	/* 1220 */       this.consumed = false;
        	/* 1221 */       for (int i = 0; i < this.keyListeners.size(); i++) {
        	/* 1222 */         KeyListener listener = (KeyListener)this.keyListeners.get(i);
        	/* 1223 */         if (listener.isAcceptingInput()) {
        	/* 1224 */           listener.keyReleased(eventKey, this.keys[eventKey]);
        	/* 1225 */           if (this.consumed)
        	/*      */           {
        	/*      */             break;
        	/*      */           }
        	/*      */         }
        	/*      */       }
        	/*      */     }
        	/*      */ 










        	/* 1233 */     while (Mouse.next()) {
        	/* 1234 */       if (Mouse.getEventButton() >= 0) {
        	/* 1235 */         if (Mouse.getEventButtonState()) {
        	/* 1236 */           this.consumed = false;
        	/* 1237 */           this.mousePressed[Mouse.getEventButton()] = true;
        	/*      */ 
        	/* 1239 */           this.pressedX = (int)(this.xoffset + Mouse.getEventX() * this.scaleX);
        	/* 1240 */           this.pressedY = (int)(this.yoffset + (height - Mouse.getEventY()) * this.scaleY);
        	/*      */ 
        	/* 1242 */           for (int i = 0; i < this.mouseListeners.size(); i++) {
        	/* 1243 */             MouseListener listener = (MouseListener)this.mouseListeners.get(i);
        	/* 1244 */             if (listener.isAcceptingInput()) {
        	/* 1245 */               listener.mousePressed(Mouse.getEventButton(), this.pressedX, this.pressedY);
        	/* 1246 */               if (this.consumed)
        	/*      */                 break;
        	/*      */             }
        	/*      */           }
        	/* 1242 */           continue;
        	/*      */         }
        	/*      */ 
        	/* 1252 */         this.consumed = false;
        	/* 1253 */         this.mousePressed[Mouse.getEventButton()] = false;
        	/*      */ 
        	/* 1255 */         int releasedX = (int)(this.xoffset + Mouse.getEventX() * this.scaleX);
        	/* 1256 */         int releasedY = (int)(this.yoffset + (height - Mouse.getEventY()) * this.scaleY);
        	/* 1257 */         if ((this.pressedX != -1) && (this.pressedY != -1) && (Math.abs(this.pressedX - releasedX) < this.mouseClickTolerance) && (Math.abs(this.pressedY - releasedY) < this.mouseClickTolerance))
        	/*      */         {
        	/* 1261 */           considerDoubleClick(Mouse.getEventButton(), releasedX, releasedY);
        	/* 1262 */           this.pressedX = (this.pressedY = -1);
        	/*      */         }
        	/*      */ 
        	/* 1265 */         for (int i = 0; i < this.mouseListeners.size(); i++) {
        	/* 1266 */           MouseListener listener = (MouseListener)this.mouseListeners.get(i);
        	/* 1267 */           if (listener.isAcceptingInput()) {
        	/* 1268 */             listener.mouseReleased(Mouse.getEventButton(), releasedX, releasedY);
        	/* 1269 */             if (this.consumed) {
        	/*      */               break;
        	/*      */             }
        	/*      */           }
        	/*      */         }
        	/* 1274 */         continue;
        	/*      */       }
        	/* 1276 */       if ((Mouse.isGrabbed()) && (
        	/* 1277 */         (Mouse.getEventDX() != 0) || (Mouse.getEventDY() != 0))) {
        	/* 1278 */         this.consumed = false;
        	/* 1279 */         for (int i = 0; i < this.mouseListeners.size(); i++) {
        	/* 1280 */           MouseListener listener = (MouseListener)this.mouseListeners.get(i);
        	/* 1281 */           if (listener.isAcceptingInput()) {
        	/* 1282 */             if (anyMouseDown())
        	/* 1283 */               listener.mouseDragged(0, 0, Mouse.getEventDX(), -Mouse.getEventDY());
        	/*      */             else {
        	/* 1285 */               listener.mouseMoved(0, 0, Mouse.getEventDX(), -Mouse.getEventDY());
        	/*      */             }
        	/*      */ 
        	/* 1288 */             if (this.consumed)
        	/*      */             {
        	/*      */               break;
        	/*      */             }
        	/*      */           }
        	/*      */         }
        	/*      */       }
        	/*      */ 
        	/* 1296 */       int dwheel = Mouse.getEventDWheel();
        	/* 1297 */       this.wheel += dwheel;
        	/* 1298 */       if (dwheel != 0) {
        	/* 1299 */         this.consumed = false;
        	/* 1300 */         for (int i = 0; i < this.mouseListeners.size(); i++) {
        	/* 1301 */           MouseListener listener = (MouseListener)this.mouseListeners.get(i);
        	/* 1302 */           if (listener.isAcceptingInput()) {
        	/* 1303 */             listener.mouseWheelMoved(dwheel);
        	/* 1304 */             if (this.consumed)
        	/*      */             {
        	/*      */               break;
        	/*      */             }
        	/*      */           }
        	/*      */         }
        	/*      */       }
        	/*      */     }
          
          
          
          
          
        if(!displayActive)
        {
            lastMouseX = getMouseX();
            lastMouseY = getMouseY();
        } else
        if(lastMouseX != getMouseX() || lastMouseY != getMouseY())
        {
            consumed = false;
            for(int dwheel = 0; dwheel < mouseListeners.size(); dwheel++)
            {
                MouseListener listener = (MouseListener)mouseListeners.get(dwheel);
                if(!listener.isAcceptingInput())
                    continue;
                if(anyMouseDown())
                    listener.mouseDragged(lastMouseX, lastMouseY, getMouseX(), getMouseY());
                else
                    listener.mouseMoved(lastMouseX, lastMouseY, getMouseX(), getMouseY());
                if(consumed)
                    break;
            }

            lastMouseX = getMouseX();
            lastMouseY = getMouseY();
        }
        if(controllersInited)
            for(int dwheel = 0; dwheel < getControllerCount(); dwheel++)
            {
                int count = ((Controller)controllers.get(dwheel)).getButtonCount() + 3;
                count = Math.min(count, 24);
                for(int c = 0; c <= count; c++)
                {
                    if(controls[dwheel][c] && !isControlDwn(c, dwheel))
                    {
                        controls[dwheel][c] = false;
                        fireControlRelease(c, dwheel);
                        continue;
                    }
                    if(!controls[dwheel][c] && isControlDwn(c, dwheel))
                    {
                        controllerPressed[dwheel][c] = true;
                        controls[dwheel][c] = true;
                        fireControlPress(c, dwheel);
                    }
                }

            }

        if(keyRepeat)
            for(int dwheel = 0; dwheel < 1024; dwheel++)
            {
                if(!pressed[dwheel] || nextRepeat[dwheel] == 0L || System.currentTimeMillis() <= nextRepeat[dwheel])
                    continue;
                nextRepeat[dwheel] = System.currentTimeMillis() + (long)keyRepeatInterval;
                consumed = false;
                for(int j = 0; j < keyListeners.size(); j++)
                {
                    KeyListener listener = (KeyListener)keyListeners.get(j);
                    if(!listener.isAcceptingInput())
                        continue;
                    listener.keyPressed(dwheel, keys[dwheel]);
                    if(consumed)
                        break;
                }

            }

        ControlledInputReciever listener;
        for(Iterator all = allListeners.iterator(); all.hasNext(); listener.inputEnded())
            listener = (ControlledInputReciever)all.next();

        if(Display.isCreated())
            displayActive = Display.isActive();
        return;
    }

    /**
     * @deprecated Method enableKeyRepeat is deprecated
     */

    public void enableKeyRepeat(int initial, int interval)
    {
        Keyboard.enableRepeatEvents(true);
    }

    public void enableKeyRepeat()
    {
        Keyboard.enableRepeatEvents(true);
    }

    public void disableKeyRepeat()
    {
        Keyboard.enableRepeatEvents(false);
    }

    public boolean isKeyRepeatEnabled()
    {
        return Keyboard.areRepeatEventsEnabled();
    }

    private void fireControlPress(int index, int controllerIndex)
    {
        consumed = false;
        for(int i = 0; i < controllerListeners.size(); i++)
        {
            ControllerListener listener = (ControllerListener)controllerListeners.get(i);
            if(!listener.isAcceptingInput())
                continue;
            switch(index)
            {
            case 0: // '\0'
                listener.controllerLeftPressed(controllerIndex);
                break;

            case 1: // '\001'
                listener.controllerRightPressed(controllerIndex);
                break;

            case 2: // '\002'
                listener.controllerUpPressed(controllerIndex);
                break;

            case 3: // '\003'
                listener.controllerDownPressed(controllerIndex);
                break;

            default:
                listener.controllerButtonPressed(controllerIndex, (index - 4) + 1);
                break;
            }
            if(consumed)
                break;
        }

    }

    private void fireControlRelease(int index, int controllerIndex)
    {
        consumed = false;
        for(int i = 0; i < controllerListeners.size(); i++)
        {
            ControllerListener listener = (ControllerListener)controllerListeners.get(i);
            if(!listener.isAcceptingInput())
                continue;
            switch(index)
            {
            case 0: // '\0'
                listener.controllerLeftReleased(controllerIndex);
                break;

            case 1: // '\001'
                listener.controllerRightReleased(controllerIndex);
                break;

            case 2: // '\002'
                listener.controllerUpReleased(controllerIndex);
                break;

            case 3: // '\003'
                listener.controllerDownReleased(controllerIndex);
                break;

            default:
                listener.controllerButtonReleased(controllerIndex, (index - 4) + 1);
                break;
            }
            if(consumed)
                break;
        }

    }

    private boolean isControlDwn(int index, int controllerIndex)
    {
        switch(index)
        {
        case 0: // '\0'
            return isControllerLeft(controllerIndex);

        case 1: // '\001'
            return isControllerRight(controllerIndex);

        case 2: // '\002'
            return isControllerUp(controllerIndex);

        case 3: // '\003'
            return isControllerDown(controllerIndex);
        }
        if(index >= 4)
            return isButtonPressed(index - 4, controllerIndex);
        else
            throw new RuntimeException("Unknown control index");
    }

    public void pause()
    {
        paused = true;
        clearKeyPressedRecord();
        clearMousePressedRecord();
        clearControlPressedRecord();
    }

    public void resume()
    {
        paused = false;
    }

    private void fireMouseClicked(int button, int x, int y, int clickCount)
    {
        consumed = false;
        for(int i = 0; i < mouseListeners.size(); i++)
        {
            MouseListener listener = (MouseListener)mouseListeners.get(i);
            if(!listener.isAcceptingInput())
                continue;
            listener.mouseClicked(button, x, y, clickCount);
            if(consumed)
                break;
        }

    }

    public static final int ANY_CONTROLLER = -1;
    private static final int MAX_BUTTONS = 100;
    public static final int KEY_ESCAPE = 1;
    public static final int KEY_1 = 2;
    public static final int KEY_2 = 3;
    public static final int KEY_3 = 4;
    public static final int KEY_4 = 5;
    public static final int KEY_5 = 6;
    public static final int KEY_6 = 7;
    public static final int KEY_7 = 8;
    public static final int KEY_8 = 9;
    public static final int KEY_9 = 10;
    public static final int KEY_0 = 11;
    public static final int KEY_MINUS = 12;
    public static final int KEY_EQUALS = 13;
    public static final int KEY_BACK = 14;
    public static final int KEY_TAB = 15;
    public static final int KEY_Q = 16;
    public static final int KEY_W = 17;
    public static final int KEY_E = 18;
    public static final int KEY_R = 19;
    public static final int KEY_T = 20;
    public static final int KEY_Y = 21;
    public static final int KEY_U = 22;
    public static final int KEY_I = 23;
    public static final int KEY_O = 24;
    public static final int KEY_P = 25;
    public static final int KEY_LBRACKET = 26;
    public static final int KEY_RBRACKET = 27;
    public static final int KEY_RETURN = 28;
    public static final int KEY_ENTER = 28;
    public static final int KEY_LCONTROL = 29;
    public static final int KEY_A = 30;
    public static final int KEY_S = 31;
    public static final int KEY_D = 32;
    public static final int KEY_F = 33;
    public static final int KEY_G = 34;
    public static final int KEY_H = 35;
    public static final int KEY_J = 36;
    public static final int KEY_K = 37;
    public static final int KEY_L = 38;
    public static final int KEY_SEMICOLON = 39;
    public static final int KEY_APOSTROPHE = 40;
    public static final int KEY_GRAVE = 41;
    public static final int KEY_LSHIFT = 42;
    public static final int KEY_BACKSLASH = 43;
    public static final int KEY_Z = 44;
    public static final int KEY_X = 45;
    public static final int KEY_C = 46;
    public static final int KEY_V = 47;
    public static final int KEY_B = 48;
    public static final int KEY_N = 49;
    public static final int KEY_M = 50;
    public static final int KEY_COMMA = 51;
    public static final int KEY_PERIOD = 52;
    public static final int KEY_SLASH = 53;
    public static final int KEY_RSHIFT = 54;
    public static final int KEY_MULTIPLY = 55;
    public static final int KEY_LMENU = 56;
    public static final int KEY_SPACE = 57;
    public static final int KEY_CAPITAL = 58;
    public static final int KEY_F1 = 59;
    public static final int KEY_F2 = 60;
    public static final int KEY_F3 = 61;
    public static final int KEY_F4 = 62;
    public static final int KEY_F5 = 63;
    public static final int KEY_F6 = 64;
    public static final int KEY_F7 = 65;
    public static final int KEY_F8 = 66;
    public static final int KEY_F9 = 67;
    public static final int KEY_F10 = 68;
    public static final int KEY_NUMLOCK = 69;
    public static final int KEY_SCROLL = 70;
    public static final int KEY_NUMPAD7 = 71;
    public static final int KEY_NUMPAD8 = 72;
    public static final int KEY_NUMPAD9 = 73;
    public static final int KEY_SUBTRACT = 74;
    public static final int KEY_NUMPAD4 = 75;
    public static final int KEY_NUMPAD5 = 76;
    public static final int KEY_NUMPAD6 = 77;
    public static final int KEY_ADD = 78;
    public static final int KEY_NUMPAD1 = 79;
    public static final int KEY_NUMPAD2 = 80;
    public static final int KEY_NUMPAD3 = 81;
    public static final int KEY_NUMPAD0 = 82;
    public static final int KEY_DECIMAL = 83;
    public static final int KEY_F11 = 87;
    public static final int KEY_F12 = 88;
    public static final int KEY_F13 = 100;
    public static final int KEY_F14 = 101;
    public static final int KEY_F15 = 102;
    public static final int KEY_KANA = 112;
    public static final int KEY_CONVERT = 121;
    public static final int KEY_NOCONVERT = 123;
    public static final int KEY_YEN = 125;
    public static final int KEY_NUMPADEQUALS = 141;
    public static final int KEY_CIRCUMFLEX = 144;
    public static final int KEY_AT = 145;
    public static final int KEY_COLON = 146;
    public static final int KEY_UNDERLINE = 147;
    public static final int KEY_KANJI = 148;
    public static final int KEY_STOP = 149;
    public static final int KEY_AX = 150;
    public static final int KEY_UNLABELED = 151;
    public static final int KEY_NUMPADENTER = 156;
    public static final int KEY_RCONTROL = 157;
    public static final int KEY_NUMPADCOMMA = 179;
    public static final int KEY_DIVIDE = 181;
    public static final int KEY_SYSRQ = 183;
    public static final int KEY_RMENU = 184;
    public static final int KEY_PAUSE = 197;
    public static final int KEY_HOME = 199;
    public static final int KEY_UP = 200;
    public static final int KEY_PRIOR = 201;
    public static final int KEY_LEFT = 203;
    public static final int KEY_RIGHT = 205;
    public static final int KEY_END = 207;
    public static final int KEY_DOWN = 208;
    public static final int KEY_NEXT = 209;
    public static final int KEY_INSERT = 210;
    public static final int KEY_DELETE = 211;
    public static final int KEY_LWIN = 219;
    public static final int KEY_RWIN = 220;
    public static final int KEY_APPS = 221;
    public static final int KEY_POWER = 222;
    public static final int KEY_SLEEP = 223;
    public static final int KEY_LALT = 56;
    public static final int KEY_RALT = 184;
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private static final int BUTTON1 = 4;
    private static final int BUTTON2 = 5;
    private static final int BUTTON3 = 6;
    private static final int BUTTON4 = 7;
    private static final int BUTTON5 = 8;
    private static final int BUTTON6 = 9;
    private static final int BUTTON7 = 10;
    private static final int BUTTON8 = 11;
    private static final int BUTTON9 = 12;
    private static final int BUTTON10 = 13;
    public static final int MOUSE_LEFT_BUTTON = 0;
    public static final int MOUSE_RIGHT_BUTTON = 1;
    public static final int MOUSE_MIDDLE_BUTTON = 2;
    private static boolean controllersInited = false;
    private static ArrayList controllers = new ArrayList();
    private int lastMouseX;
    private int lastMouseY;
    protected boolean mousePressed[];
    private boolean controllerPressed[][];
    protected char keys[];
    protected boolean pressed[];
    protected long nextRepeat[];
    private boolean controls[][];
    protected boolean consumed;
    protected HashSet allListeners;
    protected ArrayList keyListeners;
    protected ArrayList keyListenersToAdd;
    protected ArrayList mouseListeners;
    protected ArrayList mouseListenersToAdd;
    protected ArrayList controllerListeners;
    private int wheel;
    private int height;
    private boolean displayActive;
    private boolean keyRepeat;
    private int keyRepeatInitial;
    private int keyRepeatInterval;
    private boolean paused;
    private float scaleX;
    private float scaleY;
    private float xoffset;
    private float yoffset;
    private int doubleClickDelay;
    private long doubleClickTimeout;
    private int clickX;
    private int clickY;
    private int clickButton;
    private int pressedX;
    private int pressedY;
    private int mouseClickTolerance;
    public boolean presseda[];

}
