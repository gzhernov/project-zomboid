// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UIEventHandler.java

package zombie.ui;


public interface UIEventHandler
{

    public abstract void DoubleClick(String s, int i, int j);

    public abstract void ModalClick(String s, String s1);

    public abstract void Selected(String s, int i, int j);
}
