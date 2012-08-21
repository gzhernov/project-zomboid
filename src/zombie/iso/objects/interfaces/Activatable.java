// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Activatable.java

package zombie.iso.objects.interfaces;


public interface Activatable
{

    public abstract boolean Activated();

    public abstract void Toggle();

    public abstract String getActivatableType();
}
