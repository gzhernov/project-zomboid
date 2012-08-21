// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ZomboidHashSet.java

package zombie.core.Collections;

import java.io.*;
import java.util.*;

// Referenced classes of package zombie.core.Collections:
//            ZomboidHashMap

public class ZomboidHashSet extends AbstractSet
    implements Set, Cloneable, Serializable
{

    public ZomboidHashSet()
    {
        map = new ZomboidHashMap();
    }

    public ZomboidHashSet(Collection c)
    {
        map = new ZomboidHashMap(Math.max((int)((float)c.size() / 0.75F) + 1, 16));
        addAll(c);
    }

    public ZomboidHashSet(int initialCapacity, float loadFactor)
    {
        map = new ZomboidHashMap(initialCapacity);
    }

    public ZomboidHashSet(int initialCapacity)
    {
        map = new ZomboidHashMap(initialCapacity);
    }

    public Iterator iterator()
    {
        return map.keySet().iterator();
    }

    public int size()
    {
        return map.size();
    }

    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    public boolean contains(Object o)
    {
        return map.containsKey(o);
    }

    public boolean add(Object e)
    {
        return map.put(e, PRESENT) == null;
    }

    public boolean remove(Object o)
    {
        return map.remove(o) == PRESENT;
    }

    public void clear()
    {
        map.clear();
    }

    public Object clone()
    {
        try
        {
            ZomboidHashSet newSet = (ZomboidHashSet)super.clone();
            newSet.map = (ZomboidHashMap)map.clone();
            return newSet;
        }
        catch(CloneNotSupportedException e)
        {
            throw new InternalError();
        }
    }

    private void writeObject(ObjectOutputStream s)
        throws IOException
    {
        s.defaultWriteObject();
        s.writeInt(map.size());
        for(Iterator i = map.keySet().iterator(); i.hasNext(); s.writeObject(i.next()));
    }

    private void readObject(ObjectInputStream objectinputstream)
        throws IOException, ClassNotFoundException
    {
    }

    static final long serialVersionUID = 0xba44859596b8b734L;
    private transient ZomboidHashMap map;
    private static final Object PRESENT = new Object();

}
