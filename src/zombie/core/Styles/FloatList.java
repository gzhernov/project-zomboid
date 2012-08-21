// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FloatList.java

package zombie.core.Styles;

import java.io.Serializable;

public class FloatList
    implements Serializable
{

    public FloatList()
    {
        this(0);
    }

    public FloatList(int size)
    {
        this(true, size);
    }

    public FloatList(boolean fastExpand, int size)
    {
        count = 0;
        this.fastExpand = fastExpand;
        value = new float[size];
    }

    public float add(float f)
    {
        if(count == value.length)
        {
            float oldValue[] = value;
            if(fastExpand)
                value = new float[(oldValue.length << 1) + 1];
            else
                value = new float[oldValue.length + 1];
            System.arraycopy(oldValue, 0, value, 0, oldValue.length);
        }
        value[count] = f;
        return (float)(count++);
    }

    public float remove(int idx)
    {
        if(idx >= count || idx < 0)
            throw new IndexOutOfBoundsException((new StringBuilder()).append("Referenced ").append(idx).append(", size=").append(count).toString());
        float ret = value[idx];
        if(idx < count - 1)
            System.arraycopy(value, idx + 1, value, idx, count - idx - 1);
        count--;
        return ret;
    }

    public void addAll(float f[])
    {
        ensureCapacity(count + f.length);
        System.arraycopy(f, 0, value, count, f.length);
        count += f.length;
    }

    public void addAll(FloatList f)
    {
        ensureCapacity(count + f.count);
        System.arraycopy(f.value, 0, value, count, f.count);
        count += f.count;
    }

    public float[] array()
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
            float oldValue[] = value;
            value = new float[size];
            System.arraycopy(oldValue, 0, value, 0, oldValue.length);
            return;
        }
    }

    public float get(int index)
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

    public void toArray(Object dest[])
    {
        System.arraycopy(value, 0, ((Object) (dest)), 0, count);
    }

    public void trimToSize()
    {
        if(count == value.length)
        {
            return;
        } else
        {
            float oldValue[] = value;
            value = new float[count];
            System.arraycopy(oldValue, 0, value, 0, count);
            return;
        }
    }

    private static final long serialVersionUID = 1L;
    private float value[];
    private int count;
    private final boolean fastExpand;
}
