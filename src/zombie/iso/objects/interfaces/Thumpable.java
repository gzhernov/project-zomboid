// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Thumpable.java

package zombie.iso.objects.interfaces;

import zombie.iso.IsoMovingObject;

public interface Thumpable
{

    public abstract boolean isDestroyed();

    public abstract void Thump(IsoMovingObject isomovingobject);
}
