// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   KeyListener.java

package zombie.core.input;


// Referenced classes of package zombie.core.input:
//            ControlledInputReciever

public interface KeyListener
    extends ControlledInputReciever
{

    public abstract void keyPressed(int i, char c);

    public abstract void keyReleased(int i, char c);
}
