// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ShortList.java

package zombie.core.Styles;

import java.io.Serializable;

public class ShortList
    implements Serializable
{

    public ShortList()
    {
        this(0);
    }

    public ShortList(int size)
    {
        this(true, size);
    }

    public ShortList(boolean fastExpand, int size)
    {
        count = 0;
        this.fastExpand = fastExpand;
        value = new short[size];
    }

    public short add(short f)
    {
        if(count == value.length)
        {
            short oldValue[] = value;
            if(fastExpand)
                value = new short[(oldValue.length << 1) + 1];
            else
                value = new short[oldValue.length + 1];
            System.arraycopy(oldValue, 0, value, 0, oldValue.length);
        }
        value[count] = f;
        return count++;
    }

    public short remove(int idx)
    {
        if(idx >= count || idx < 0)
            throw new IndexOutOfBoundsException((new StringBuilder()).append("Referenced ").append(idx).append(", size=").append(count).toString());
        short ret = value[idx];
        if(idx < count - 1)
            System.arraycopy(value, idx + 1, value, idx, count - idx - 1);
        count--;
        return ret;
    }

    public void addAll(short f[])
    {
        ensureCapacity(count + f.length);
        System.arraycopy(f, 0, value, count, f.length);
        count += f.length;
    }

    public void addAll(ShortList f)
    {
        ensureCapacity(count + f.count);
        System.arraycopy(f.value, 0, value, count, f.count);
        count += f.count;
    }

    public short[] array()
    {
        return value;
    }

    public int capacity()
    {
        return value.length;
    }

    public void clear()
    {
        count = 0;
    }

    public void ensureCapacity(int size)
    {
        if(value.length >= size)
        {
            return;
        } else
        {
            short oldValue[] = value;
            value = new short[size];
            System.arraycopy(oldValue, 0, value, 0, oldValue.length);
            return;
        }
    }

    public short get(int index)
    {
        return value[index];
    }

    public boolean isEmpty()
    {
        return count == 0;
    }

    public int size()
    {
        return count;
    }

    public short[] toArray(short dest[])
    {
        if(dest == null)
            dest = new short[count];
        System.arraycopy(value, 0, dest, 0, count);
        return dest;
    }

    public void trimToSize()
    {
        if(count == value.length)
        {
            return;
        } else
        {
            short oldValue[] = value;
            value = new short[count];
            System.arraycopy(oldValue, 0, value, 0, count);
            return;
        }
    }

    private static final long serialVersionUID = 1L;
    private short value[];
    private short count;
    private final boolean fastExpand;
}
