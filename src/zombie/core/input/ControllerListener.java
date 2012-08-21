// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ControllerListener.java

package zombie.core.input;


// Referenced classes of package zombie.core.input:
//            ControlledInputReciever

public interface ControllerListener
    extends ControlledInputReciever
{

    public abstract void controllerLeftPressed(int i);

    public abstract void controllerLeftReleased(int i);

    public abstract void controllerRightPressed(int i);

    public abstract void controllerRightReleased(int i);

    public abstract void controllerUpPressed(int i);

    public abstract void controllerUpReleased(int i);

    public abstract void controllerDownPressed(int i);

    public abstract void controllerDownReleased(int i);

    public abstract void controllerButtonPressed(int i, int j);

    public abstract void controllerButtonReleased(int i, int j);
}
