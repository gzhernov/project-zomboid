// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ITexture.java

package zombie.interfaces;

import java.nio.ByteBuffer;
import zombie.core.textures.Mask;

// Referenced classes of package zombie.interfaces:
//            IDestroyable, IMaskerable

public interface ITexture
    extends IDestroyable, IMaskerable
{

    public abstract void bind();

    public abstract void bind(int i);

    public abstract ByteBuffer getData();

    public abstract int getHeight();

    public abstract int getHeightHW();

    public abstract int getID();

    public abstract int getWidth();

    public abstract int getWidthHW();

    public abstract float getXEnd();

    public abstract float getXStart();

    public abstract float getYEnd();

    public abstract float getYStart();

    public abstract boolean isSolid();

    public abstract void makeTransp(int i, int j, int k);

    public abstract void setAlphaForeach(int i, int j, int k, int l);

    public abstract void setData(ByteBuffer bytebuffer);

    public abstract void setMask(Mask mask);

    public abstract void setRegion(int i, int j, int k, int l);
}
