// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Talker.java

package zombie.characters;


public interface Talker
{

    public abstract boolean IsSpeaking();

    public abstract void Say(String s);

    public abstract String getTalkerType();
}
