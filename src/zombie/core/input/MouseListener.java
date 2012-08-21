// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MouseListener.java

package zombie.core.input;


// Referenced classes of package zombie.core.input:
//            ControlledInputReciever

public interface MouseListener
    extends ControlledInputReciever
{

    public abstract void mouseWheelMoved(int i);

    public abstract void mouseClicked(int i, int j, int k, int l);

    public abstract void mousePressed(int i, int j, int k);

    public abstract void mouseReleased(int i, int j, int k);

    public abstract void mouseMoved(int i, int j, int k, int l);

    public abstract void mouseDragged(int i, int j, int k, int l);
}
