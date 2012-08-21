// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ZomboidAbstractMap.java

package zombie.core.Collections;

import java.io.Serializable;
import java.util.*;

public abstract class ZomboidAbstractMap
    implements Map
{
    public static class SimpleImmutableEntry
        implements java.util.Map.Entry, Serializable
    {

        public Object getKey()
        {
            return key;
        }

        public Object getValue()
        {
            return value;
        }

        public Object setValue(Object value)
        {
            throw new UnsupportedOperationException();
        }

        public boolean equals(Object o)
        {
            if(!(o instanceof java.util.Map.Entry))
            {
                return false;
            } else
            {
                java.util.Map.Entry e = (java.util.Map.Entry)o;
                return ZomboidAbstractMap.eq(key, e.getKey()) && ZomboidAbstractMap.eq(value, e.getValue());
            }
        }

        public int hashCode()
        {
            return (key != null ? key.hashCode() : 0) ^ (value != null ? value.hashCode() : 0);
        }

        public String toString()
        {
            return (new StringBuilder()).append(key).append("=").append(value).toString();
        }

        private static final long serialVersionUID = 0x6310708932e65f81L;
        private final Object key;
        private final Object value;

        public SimpleImmutableEntry(Object key, Object value)
        {
            this.key = key;
            this.value = value;
        }

        public SimpleImmutableEntry(java.util.Map.Entry entry)
        {
            key = entry.getKey();
            value = entry.getValue();
        }
    }

    public static class SimpleEntry
        implements java.util.Map.Entry, Serializable
    {

        public Object getKey()
        {
            return key;
        }

        public Object getValue()
        {
            return value;
        }

        public Object setValue(Object value)
        {
            Object oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public boolean equals(Object o)
        {
            if(!(o instanceof java.util.Map.Entry))
            {
                return false;
            } else
            {
                java.util.Map.Entry e = (java.util.Map.Entry)o;
                return ZomboidAbstractMap.eq(key, e.getKey()) && ZomboidAbstractMap.eq(value, e.getValue());
            }
        }

        public int hashCode()
        {
            return (key != null ? key.hashCode() : 0) ^ (value != null ? value.hashCode() : 0);
        }

        public String toString()
        {
            return (new StringBuilder()).append(key).append("=").append(value).toString();
        }

        private static final long serialVersionUID = 0x8a0aeca5fc56801fL;
        private final Object key;
        private Object value;

        public SimpleEntry(Object key, Object value)
        {
            this.key = key;
            this.value = value;
        }

        public SimpleEntry(java.util.Map.Entry entry)
        {
            key = entry.getKey();
            value = entry.getValue();
        }
    }


    protected ZomboidAbstractMap()
    {
        keySet = null;
        values = null;
    }

    public int size()
    {
        return entrySet().size();
    }

    public boolean isEmpty()
    {
        return size() == 0;
    }

    public boolean containsValue(Object value)
    {
label0:
        {
            Iterator i = entrySet().iterator();
            java.util.Map.Entry e;
            if(value == null)
            {
                do
                {
                    if(!i.hasNext())
                        break label0;
                    e = (java.util.Map.Entry)i.next();
                } while(e.getValue() != null);
                return true;
            }
            do
            {
                if(!i.hasNext())
                    break label0;
                e = (java.util.Map.Entry)i.next();
            } while(!value.equals(e.getValue()));
            return true;
        }
        return false;
    }

    public boolean containsKey(Object key)
    {
label0:
        {
            Iterator i = entrySet().iterator();
            java.util.Map.Entry e;
            if(key == null)
            {
                do
                {
                    if(!i.hasNext())
                        break label0;
                    e = (java.util.Map.Entry)i.next();
                } while(e.getKey() != null);
                return true;
            }
            do
            {
                if(!i.hasNext())
                    break label0;
                e = (java.util.Map.Entry)i.next();
            } while(!key.equals(e.getKey()));
            return true;
        }
        return false;
    }

    public Object get(Object key)
    {
label0:
        {
            Iterator i = entrySet().iterator();
            java.util.Map.Entry e;
            if(key == null)
            {
                do
                {
                    if(!i.hasNext())
                        break label0;
                    e = (java.util.Map.Entry)i.next();
                } while(e.getKey() != null);
                return e.getValue();
            }
            do
            {
                if(!i.hasNext())
                    break label0;
                e = (java.util.Map.Entry)i.next();
            } while(!key.equals(e.getKey()));
            return e.getValue();
        }
        return null;
    }

    public Object put(Object key, Object value)
    {
        throw new UnsupportedOperationException();
    }

    public Object remove(Object key)
    {
        Iterator i = entrySet().iterator();
        java.util.Map.Entry correctEntry = null;
        if(key == null)
            do
            {
                if(correctEntry != null || !i.hasNext())
                    break;
                java.util.Map.Entry e = (java.util.Map.Entry)i.next();
                if(e.getKey() == null)
                    correctEntry = e;
            } while(true);
        else
            do
            {
                if(correctEntry != null || !i.hasNext())
                    break;
                java.util.Map.Entry e = (java.util.Map.Entry)i.next();
                if(key.equals(e.getKey()))
                    correctEntry = e;
            } while(true);
        Object oldValue = null;
        if(correctEntry != null)
        {
            oldValue = correctEntry.getValue();
            i.remove();
        }
        return oldValue;
    }

    public void putAll(Map m)
    {
        java.util.Map.Entry e;
        for(Iterator i$ = m.entrySet().iterator(); i$.hasNext(); put(e.getKey(), e.getValue()))
            e = (java.util.Map.Entry)i$.next();

    }

    public void clear()
    {
        entrySet().clear();
    }

    public Set keySet()
    {
        if(keySet == null)
            keySet = new AbstractSet() {

                public Iterator iterator()
                {
                    return new Iterator() {

                    	private Iterator i = ZomboidAbstractMap.this.entrySet().iterator();
                    	
                        public boolean hasNext()
                        {
                            return i.hasNext();
                        }

                        public Object next()
                        {
                            return ((java.util.Map.Entry)i.next()).getKey();
                        }

                        public void remove()
                        {
                            i.remove();
                        }


                    }
;
                }

                public int size()
                {
                    return ZomboidAbstractMap.this.size();
                }

                public boolean contains(Object k)
                {
                    return containsKey(k);
                }



            

            }
;
        return keySet;
    }

    public Collection values()
    {
        if(values == null)
            values = new AbstractCollection() {

                public Iterator iterator()
                {
                    return new Iterator() {

                        public boolean hasNext()
                        {
                            return i.hasNext();
                        }

                        public Object next()
                        {
                            return ((java.util.Map.Entry)i.next()).getValue();
                        }

                        public void remove()
                        {
                            i.remove();
                        }

                        private Iterator i = ZomboidAbstractMap.this.entrySet().iterator();

                    
  
                    }
;
                }

                public int size()
                {
                    return ZomboidAbstractMap.this.size();
                }

                public boolean contains(Object v)
                {
                    return containsValue(v);
                }


            }
;
        return values;
    }

    public abstract Set entrySet();

    public boolean equals(Object o)
    {
    	/* 376 */     if (o == this) {
    		/* 377 */       return true;
    		/*     */     }
    		/* 379 */     if (!(o instanceof Map))
    		/* 380 */       return false;
    		/* 381 */     Map m = (Map)o;
    		/* 382 */     if (m.size() != size())
    		/* 383 */       return false;
    		/*     */     try
    		/*     */     {
    		/* 386 */       Iterator i = entrySet().iterator();
    		/* 387 */       while (i.hasNext()) {
    		/* 388 */         Map.Entry e = (Map.Entry)i.next();
    		/* 389 */         Object key = e.getKey();
    		/* 390 */         Object value = e.getValue();
    		/* 391 */         if (value == null) {
    		/* 392 */           if ((m.get(key) != null) || (!m.containsKey(key)))
    		/* 393 */             return false;
    		/*     */         }
    		/* 395 */         else if (!value.equals(m.get(key)))
    		/* 396 */           return false;
    		/*     */       }
    		/*     */     }
    		/*     */     catch (ClassCastException unused) {
    		/* 400 */       return false;
    		/*     */     } catch (NullPointerException unused) {
    		/* 402 */       return false;
    		/*     */     }
    		/*     */ 
    		/* 405 */     return true;
    }

    public int hashCode()
    {
        int h = 0;
        for(Iterator i = entrySet().iterator(); i.hasNext();)
            h += ((java.util.Map.Entry)i.next()).hashCode();

        return h;
    }

    public String toString()
    {
        Iterator i = entrySet().iterator();
        if(!i.hasNext())
            return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        do
        {
            java.util.Map.Entry e = (java.util.Map.Entry)i.next();
            Object key = e.getKey();
            Object value = e.getValue();
            sb.append(key != this ? key : "(this Map)");
            sb.append('=');
            sb.append(value != this ? value : "(this Map)");
            if(!i.hasNext())
                return sb.append('}').toString();
            sb.append(", ");
        } while(true);
    }

    protected Object clone()
        throws CloneNotSupportedException
    {
        ZomboidAbstractMap result = (ZomboidAbstractMap)super.clone();
        result.keySet = null;
        result.values = null;
        return result;
    }

    private static boolean eq(Object o1, Object o2)
    {
        return o1 != null ? o1.equals(o2) : o2 == null;
    }

    volatile transient Set keySet;
    volatile transient Collection values;

}
