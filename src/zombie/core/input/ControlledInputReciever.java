// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ControlledInputReciever.java

package zombie.core.input;


// Referenced classes of package zombie.core.input:
//            Input

public interface ControlledInputReciever
{

    public abstract void setInput(Input input);

    public abstract boolean isAcceptingInput();

    public abstract void inputEnded();

    public abstract void inputStarted();
}
