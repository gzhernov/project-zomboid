// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ZomboidHashMap.java

package zombie.core.Collections;

import java.io.*;
import java.util.*;

// Referenced classes of package zombie.core.Collections:
//            ZomboidAbstractMap

public class ZomboidHashMap extends ZomboidAbstractMap
    implements Map, Cloneable, Serializable
{
    private final class EntrySet extends AbstractSet
    {

        public Iterator iterator()
        {
            return newEntryIterator();
        }

        public boolean contains(Object o)
        {
            if(!(o instanceof java.util.Map.Entry))
            {
                return false;
            } else
            {
                java.util.Map.Entry e = (java.util.Map.Entry)o;
                ZomboidHashMap.Entry candidate = getEntry(e.getKey());
                return candidate != null && candidate.equals(e);
            }
        }

        public boolean remove(Object o)
        {
            return removeMapping(o) != null;
        }

        public int size()
        {
            return ZomboidHashMap.this.size;
        }

        public void clear()
        {
            ZomboidHashMap.this.clear();
        }

       

        private EntrySet()
        {

        }

    }

    private final class Values extends AbstractCollection
    {

        public Iterator iterator()
        {
            return newValueIterator();
        }

        public int size()
        {
            return ZomboidHashMap.this.size;
        }

        public boolean contains(Object o)
        {
            return containsValue(o);
        }

        public void clear()
        {
            ZomboidHashMap.this.clear();
        }

      

        private Values()
        {

        }

    }

    private final class KeySet extends AbstractSet
    {

        public Iterator iterator()
        {
            return newKeyIterator();
        }

        public int size()
        {
            return ZomboidHashMap.this.size;
        }

        public boolean contains(Object o)
        {
            return containsKey(o);
        }

        public boolean remove(Object o)
        {
            return removeEntryForKey(o) != null;
        }

        public void clear()
        {
            ZomboidHashMap.this.clear();
        }

      

        private KeySet()
        {

        }

    }

    private final class EntryIterator extends HashIterator
    {

        public java.util.Map.Entry next()
        {
            return nextEntry();
        }

      
        

        private EntryIterator()
        {

        }

    }

    private final class KeyIterator extends HashIterator
    {

        public Object next()
        {
            return nextEntry().getKey();
        }

       

        private KeyIterator()
        {
            
        }

    }

    private final class ValueIterator extends HashIterator
    {

        public Object next()
        {
            return nextEntry().value;
        }

       

        private ValueIterator()
        {
       
        }

    }

    private abstract class HashIterator
        implements Iterator
    {

        public final boolean hasNext()
        {
            return next != null;
        }

        final ZomboidHashMap.Entry nextEntry()
        {
            if(modCount != expectedModCount)
                throw new ConcurrentModificationException();
            ZomboidHashMap.Entry e = next;
            if(e == null)
                throw new NoSuchElementException();
            if((next = e.next) == null)
            {
                for(ZomboidHashMap.Entry t[] = table; index < t.length && (next = t[index++]) == null;);
            }
            current = e;
            return e;
        }

        public void remove()
        {
            if(current == null)
                throw new IllegalStateException();
            if(modCount != expectedModCount)
            {
                throw new ConcurrentModificationException();
            } else
            {
                Object k = current.key;
                current = null;
                removeEntryForKey(k);
                expectedModCount = modCount;
                return;
            }
        }

        ZomboidHashMap.Entry next;
        int expectedModCount;
        int index;
        ZomboidHashMap.Entry current;
      

        HashIterator()
        {
           
            expectedModCount = modCount;
            if(size > 0)
            {
                for(ZomboidHashMap.Entry t[] = table; index < t.length && (next = t[index++]) == null;);
            }
        }
    }

    static class Entry
        implements java.util.Map.Entry
    {

        public final Object getKey()
        {
            return key;
        }

        public final Object getValue()
        {
            return value;
        }

        public final Object setValue(Object newValue)
        {
            Object oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o)
        {
            if(!(o instanceof java.util.Map.Entry))
                return false;
            java.util.Map.Entry e = (java.util.Map.Entry)o;
            Object k1 = getKey();
            Object k2 = e.getKey();
            if(k1 == k2 || k1 != null && k1.equals(k2))
            {
                Object v1 = getValue();
                Object v2 = e.getValue();
                if(v1 == v2 || v1 != null && v1.equals(v2))
                    return true;
            }
            return false;
        }

        public final int hashCode()
        {
            return (key != null ? key.hashCode() : 0) ^ (value != null ? value.hashCode() : 0);
        }

        public final String toString()
        {
            return (new StringBuilder()).append(getKey()).append("=").append(getValue()).toString();
        }

        void recordAccess(ZomboidHashMap zomboidhashmap)
        {
        }

        void recordRemoval(ZomboidHashMap zomboidhashmap)
        {
        }

        Object key;
        Object value;
        Entry next;
        int hash;

        Entry(int h, Object k, Object v, Entry n)
        {
            value = v;
            next = n;
            key = k;
            hash = h;
        }
    }


    public ZomboidHashMap(int initialCapacity, float loadFactor)
    {
        entryStore = new Stack();
        entrySet = null;
        if(initialCapacity < 0)
            throw new IllegalArgumentException((new StringBuilder()).append("Illegal initial capacity: ").append(initialCapacity).toString());
        if(initialCapacity > 0x40000000)
            initialCapacity = 0x40000000;
        if(loadFactor <= 0.0F || Float.isNaN(loadFactor))
            throw new IllegalArgumentException((new StringBuilder()).append("Illegal load factor: ").append(loadFactor).toString());
        int capacity;
        for(capacity = 1; capacity < initialCapacity; capacity <<= 1);
        for(int n = 0; n < 100; n++)
            entryStore.add(new Entry(0, null, null, null));

        this.loadFactor = loadFactor;
        threshold = (int)((float)capacity * loadFactor);
        table = new Entry[capacity];
        init();
    }

    public ZomboidHashMap(int initialCapacity)
    {
        this(initialCapacity, 0.75F);
    }

    public ZomboidHashMap()
    {
        entryStore = new Stack();
        entrySet = null;
        loadFactor = 0.75F;
        threshold = 12;
        table = new Entry[16];
        init();
    }

    public ZomboidHashMap(Map m)
    {
        this(Math.max((int)((float)m.size() / 0.75F) + 1, 16), 0.75F);
        putAllForCreate(m);
    }

    void init()
    {
    }

    static int hash(int h)
    {
        h ^= h >>> 20 ^ h >>> 12;
        return h ^ h >>> 7 ^ h >>> 4;
    }

    static int indexFor(int h, int length)
    {
        return h & length - 1;
    }

    public int size()
    {
        return size;
    }

    public boolean isEmpty()
    {
        return size == 0;
    }

    public Object get(Object key)
    {
        if(key == null)
            return getForNullKey();
        int hash = hash(key.hashCode());
        for(Entry e = table[indexFor(hash, table.length)]; e != null; e = e.next)
        {
            Object k;
            if(e.hash == hash && ((k = e.key) == key || key.equals(k)))
                return e.value;
        }

        return null;
    }

    private Object getForNullKey()
    {
        for(Entry e = table[0]; e != null; e = e.next)
            if(e.key == null)
                return e.value;

        return null;
    }

    public boolean containsKey(Object key)
    {
        return getEntry(key) != null;
    }

    final Entry getEntry(Object key)
    {
        int hash = key != null ? hash(key.hashCode()) : 0;
        for(Entry e = table[indexFor(hash, table.length)]; e != null; e = e.next)
        {
            Object k;
            if(e.hash == hash && ((k = e.key) == key || key != null && key.equals(k)))
                return e;
        }

        return null;
    }

    public Object put(Object key, Object value)
    {
        if(key == null)
            return putForNullKey(value);
        int hash = hash(key.hashCode());
        int i = indexFor(hash, table.length);
        for(Entry e = table[i]; e != null; e = e.next)
        {
            Object k;
            if(e.hash == hash && ((k = e.key) == key || key.equals(k)))
            {
                Object oldValue = e.value;
                e.value = value;
                e.recordAccess(this);
                return oldValue;
            }
        }

        modCount++;
        addEntry(hash, key, value, i);
        return null;
    }

    private Object putForNullKey(Object value)
    {
        for(Entry e = table[0]; e != null; e = e.next)
            if(e.key == null)
            {
                Object oldValue = e.value;
                e.value = value;
                e.recordAccess(this);
                return oldValue;
            }

        modCount++;
        addEntry(0, null, value, 0);
        return null;
    }

    private void putForCreate(Object key, Object value)
    {
        int hash = key != null ? hash(key.hashCode()) : 0;
        int i = indexFor(hash, table.length);
        for(Entry e = table[i]; e != null; e = e.next)
        {
            Object k;
            if(e.hash == hash && ((k = e.key) == key || key != null && key.equals(k)))
            {
                e.value = value;
                return;
            }
        }

        createEntry(hash, key, value, i);
    }

    private void putAllForCreate(Map m)
    {
        java.util.Map.Entry e;
        for(Iterator i = m.entrySet().iterator(); i.hasNext(); putForCreate(e.getKey(), e.getValue()))
            e = (java.util.Map.Entry)i.next();

    }

    void resize(int newCapacity)
    {
        Entry oldTable[] = table;
        int oldCapacity = oldTable.length;
        if(oldCapacity == 0x40000000)
        {
            threshold = 0x7fffffff;
            return;
        } else
        {
            Entry newTable[] = new Entry[newCapacity];
            transfer(newTable);
            table = newTable;
            threshold = (int)((float)newCapacity * loadFactor);
            return;
        }
    }

    void transfer(Entry newTable[])
    {
        Entry src[] = table;
        int newCapacity = newTable.length;
        for(int j = 0; j < src.length; j++)
        {
            Entry e = src[j];
            if(e == null)
                continue;
            src[j] = null;
            do
            {
                Entry next = e.next;
                int i = indexFor(e.hash, newCapacity);
                e.next = newTable[i];
                newTable[i] = e;
                e = next;
            } while(e != null);
        }

    }

    public void putAll(Map m)
    {
        int numKeysToBeAdded = m.size();
        if(numKeysToBeAdded == 0)
            return;
        if(numKeysToBeAdded > threshold)
        {
            int targetCapacity = (int)((float)numKeysToBeAdded / loadFactor + 1.0F);
            if(targetCapacity > 0x40000000)
                targetCapacity = 0x40000000;
            int newCapacity;
            for(newCapacity = table.length; newCapacity < targetCapacity; newCapacity <<= 1);
            if(newCapacity > table.length)
                resize(newCapacity);
        }
        java.util.Map.Entry e;
        for(Iterator i = m.entrySet().iterator(); i.hasNext(); put(e.getKey(), e.getValue()))
            e = (java.util.Map.Entry)i.next();

    }

    public Object remove(Object key)
    {
        Entry e = removeEntryForKey(key);
        return e != null ? e.value : null;
    }

    final Entry removeEntryForKey(Object key)
    {
        int hash = key != null ? hash(key.hashCode()) : 0;
        int i = indexFor(hash, table.length);
        Entry prev = table[i];
        Entry e;
        Entry next;
        for(e = prev; e != null; e = next)
        {
            next = e.next;
            Object k;
            if(e.hash == hash && ((k = e.key) == key || key != null && key.equals(k)))
            {
                modCount++;
                size--;
                if(prev == e)
                    table[i] = next;
                else
                    prev.next = next;
                e.recordRemoval(this);
                e.value = null;
                e.next = null;
                entryStore.push(e);
                return e;
            }
            prev = e;
        }

        return e;
    }

    final Entry removeMapping(Object o)
    {
        if(!(o instanceof java.util.Map.Entry))
            return null;
        java.util.Map.Entry entry = (java.util.Map.Entry)o;
        Object key = entry.getKey();
        int hash = key != null ? hash(key.hashCode()) : 0;
        int i = indexFor(hash, table.length);
        Entry prev = table[i];
        Entry e;
        Entry next;
        for(e = prev; e != null; e = next)
        {
            next = e.next;
            if(e.hash == hash && e.equals(entry))
            {
                modCount++;
                size--;
                if(prev == e)
                    table[i] = next;
                else
                    prev.next = next;
                e.recordRemoval(this);
                e.value = null;
                e.next = null;
                entryStore.push(e);
                return e;
            }
            prev = e;
        }

        return e;
    }

    public void clear()
    {
        modCount++;
        Entry tab[] = table;
        for(int i = 0; i < tab.length; i++)
        {
            if(tab[i] != null)
            {
                tab[i].value = null;
                tab[i].next = null;
                entryStore.push(tab[i]);
            }
            tab[i] = null;
        }

        size = 0;
    }

    public boolean containsValue(Object value)
    {
        if(value == null)
            return containsNullValue();
        Entry tab[] = table;
        for(int i = 0; i < tab.length; i++)
        {
            for(Entry e = tab[i]; e != null; e = e.next)
                if(value.equals(e.value))
                    return true;

        }

        return false;
    }

    private boolean containsNullValue()
    {
        Entry tab[] = table;
        for(int i = 0; i < tab.length; i++)
        {
            for(Entry e = tab[i]; e != null; e = e.next)
                if(e.value == null)
                    return true;

        }

        return false;
    }

    public Object clone()
    {
        ZomboidHashMap result = null;
        try
        {
            result = (ZomboidHashMap)super.clone();
        }
        catch(CloneNotSupportedException e) { }
        result.table = new Entry[table.length];
        result.entrySet = null;
        result.modCount = 0;
        result.size = 0;
        result.init();
        result.putAllForCreate(this);
        return result;
    }

    void addEntry(int hash, Object key, Object value, int bucketIndex)
    {
        Entry e = table[bucketIndex];
        if(entryStore.isEmpty())
        {
            for(int n = 0; n < 100; n++)
                entryStore.add(new Entry(0, null, null, null));

        }
        Entry pop = (Entry)entryStore.pop();
        pop.hash = hash;
        pop.key = key;
        pop.value = value;
        pop.next = e;
        table[bucketIndex] = pop;
        if(size++ >= threshold)
            resize(2 * table.length);
    }

    void createEntry(int hash, Object key, Object value, int bucketIndex)
    {
        Entry e = table[bucketIndex];
        if(entryStore.isEmpty())
        {
            for(int n = 0; n < 100; n++)
                entryStore.add(new Entry(0, null, null, null));

        }
        Entry pop = (Entry)entryStore.pop();
        pop.hash = hash;
        pop.key = key;
        pop.value = value;
        pop.next = e;
        table[bucketIndex] = pop;
        size++;
    }

    Iterator newKeyIterator()
    {
        return new KeyIterator();
    }

    Iterator newValueIterator()
    {
        return new ValueIterator();
    }

    Iterator newEntryIterator()
    {
        return new EntryIterator();
    }

    public Set keySet()
    {
        Set ks = keySet;
        return ks == null ? (keySet = new KeySet()) : ks;
    }

    public Collection values()
    {
        Collection vs = values;
        return vs == null ? (values = new Values()) : vs;
    }

    public Set entrySet()
    {
        return entrySet0();
    }

    private Set entrySet0()
    {
        Set es = entrySet;
        return es == null ? (entrySet = new EntrySet()) : es;
    }

    private void writeObject(ObjectOutputStream s)
        throws IOException
    {
        Iterator i = size <= 0 ? null : entrySet0().iterator();
        s.defaultWriteObject();
        s.writeInt(table.length);
        s.writeInt(size);
        if(i != null)
        {
            java.util.Map.Entry e;
            for(; i.hasNext(); s.writeObject(e.getValue()))
            {
                e = (java.util.Map.Entry)i.next();
                s.writeObject(e.getKey());
            }

        }
    }

    private void readObject(ObjectInputStream s)
        throws IOException, ClassNotFoundException
    {
        s.defaultReadObject();
        int numBuckets = s.readInt();
        table = new Entry[numBuckets];
        init();
        int size = s.readInt();
        for(int i = 0; i < size; i++)
        {
            Object key = s.readObject();
            Object value = s.readObject();
            putForCreate(key, value);
        }

    }

    int capacity()
    {
        return table.length;
    }

    float loadFactor()
    {
        return loadFactor;
    }

    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int MAXIMUM_CAPACITY = 0x40000000;
    static final float DEFAULT_LOAD_FACTOR = 0.75F;
    transient Entry table[];
    transient int size;
    int threshold;
    final float loadFactor;
    volatile transient int modCount;
    Stack entryStore;
    private transient Set entrySet;
    private static final long serialVersionUID = 0x507dac1c31660d1L;
}
