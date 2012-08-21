// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DeferredResource.java

package zombie.loading;

import java.io.IOException;

public interface DeferredResource
{

    public abstract String getDescription();

    public abstract void load()
        throws IOException;
}
