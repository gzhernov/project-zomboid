// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Font.java

package zombie.core.fonts;

import zombie.core.Color;

public interface Font
{

    public abstract void drawString(float f, float f1, String s);

    public abstract void drawString(float f, float f1, String s, Color color);

    public abstract void drawString(float f, float f1, String s, Color color, int i, int j);

    public abstract int getHeight(String s);

    public abstract int getWidth(String s);

    public abstract int getLineHeight();
}
