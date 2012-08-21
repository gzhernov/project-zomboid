/*     */ package zombie.core.Collections;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import se.krka.kahlua.j2se.J2SEPlatform;
/*     */ import se.krka.kahlua.vm.KahluaTable;
/*     */ import zombie.Lua.LuaManager;
/*     */ 
/*     */ public class NulledArrayList<T>
/*     */ {
/*     */   public ArrayList<T> list;
/*  25 */   int cap = -1;
/*     */   NulledArrayList<T>.Itr itr;
/*     */ 
/*     */   public NulledArrayList(int startCap)
/*     */   {
/*  28 */     this.cap = startCap;
/*     */   }
/*     */ 
/*     */   public NulledArrayList()
/*     */   {
/*     */   }
/*     */ 
/*     */   void check() {
/*  36 */     if (this.list == null)
/*     */     {
/*  38 */       if (this.cap != -1)
/*  39 */         this.list = new ArrayList(this.cap);
/*     */       else
/*  41 */         this.list = new ArrayList();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void add(T toAdd)
/*     */   {
/*  47 */     check();
/*  48 */     this.list.add(toAdd);
/*     */   }
/*     */ 
/*     */   public KahluaTable getLuaTable()
/*     */   {
/*  53 */     KahluaTable table = LuaManager.platform.newTable();
/*  54 */     for (int n = 0; n < this.list.size(); n++) {
/*  55 */       table.rawset(n + 1, this.list.get(n));
/*     */     }
/*  57 */     return table;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/*  63 */     if (this.list == null) {
/*  64 */       return 0;
/*     */     }
/*  66 */     return this.list.size();
/*     */   }
/*     */ 
/*     */   public T get(int n)
/*     */   {
/*  71 */     check();
/*  72 */     return this.list.get(n);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/*  78 */     if (this.list != null)
/*  79 */       this.list.clear();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty() {
/*  83 */     if (this.list == null) {
/*  84 */       return true;
/*     */     }
/*  86 */     return this.list.isEmpty();
/*     */   }
/*     */ 
/*     */   public void remove(int index)
/*     */   {
/*  92 */     if (this.list == null) {
/*  93 */       return;
/*     */     }
/*  95 */     this.list.remove(index);
/*     */   }
/*     */ 
/*     */   public void remove(T item)
/*     */   {
/* 101 */     if (this.list == null) {
/* 102 */       return;
/*     */     }
/* 104 */     this.list.remove(item);
/*     */   }
/*     */ 
/*     */   public Iterator<T> iterator()
/*     */   {
/* 109 */     if (this.list == null) {
/* 110 */       return null;
/*     */     }
/* 112 */     if (this.itr == null) {
/* 113 */       this.itr = new Itr();
/*     */     }
/* 115 */     this.itr.cursor = 0;
/* 116 */     this.itr.lastRet = -1;
/* 117 */     return this.itr;
/*     */   }
/*     */ 
/*     */   public boolean contains(T item)
/*     */   {
/* 123 */     if (this.list == null) {
/* 124 */       return false;
/*     */     }
/* 126 */     return this.list.contains(item);
/*     */   }
/*     */ 
/*     */   public void addAll(Collection<T> t) {
/* 130 */     if (!t.isEmpty())
/* 131 */       check();
/*     */     else {
/* 133 */       return;
/*     */     }
/* 135 */     this.list.addAll(t);
/*     */   }
/*     */ 
/*     */   public int indexOf(T get)
/*     */   {
/* 140 */     if (this.list == null) {
/* 141 */       return -1;
/*     */     }
/* 143 */     return this.list.indexOf(get);
/*     */   }
/*     */ 
/*     */   public void addAll(NulledArrayList<T> t)
/*     */   {
/* 205 */     if (!t.isEmpty())
/* 206 */       check();
/*     */     else {
/* 208 */       return;
/*     */     }
/* 210 */     this.list.addAll(t.list);
/*     */   }
/*     */ 
/*     */   public void insert(T obj, int i)
/*     */   {
/* 215 */     check();
/*     */ 
/* 217 */     if (this.list.isEmpty())
/*     */     {
/* 219 */       add(obj);
/* 220 */       return;
/*     */     }
/*     */ 
/* 223 */     ArrayList newList = new ArrayList();
/* 224 */     for (int n = 0; n < this.list.size(); n++)
/*     */     {
/* 226 */       if (n == i)
/* 227 */         newList.add(obj);
/* 228 */       newList.add(this.list.get(n));
/*     */     }
/*     */ 
/* 231 */     this.list = newList;
/*     */   }
/*     */ 
/*     */   private class Itr
/*     */     implements Iterator<T>
/*     */   {
/* 150 */     int cursor = 0;
/*     */ 
/* 157 */     int lastRet = -1;
/*     */ 
/*     */     private Itr()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/* 167 */       return this.cursor != NulledArrayList.this.size();
/*     */     }
/*     */ 
/*     */     public T next() {
/* 171 */       checkForComodification();
/*     */       try {
/* 173 */         T next = NulledArrayList.this.get(this.cursor);
/* 174 */         this.lastRet = (this.cursor++);
/* 175 */         return next;
/*     */       } catch (IndexOutOfBoundsException e) {
/* 177 */         checkForComodification();
/* 178 */       }throw new NoSuchElementException();
/*     */     }
/*     */ 
/*     */     public void remove()
/*     */     {
/* 183 */       if (this.lastRet == -1)
/* 184 */         throw new IllegalStateException();
/* 185 */       checkForComodification();
/*     */       try
/*     */       {
/* 188 */         NulledArrayList.this.list.remove(this.lastRet);
/* 189 */         if (this.lastRet < this.cursor)
/* 190 */           this.cursor -= 1;
/* 191 */         this.lastRet = -1;
/*     */       }
/*     */       catch (IndexOutOfBoundsException e) {
/* 194 */         throw new ConcurrentModificationException();
/*     */       }
/*     */     }
/*     */ 
/*     */     final void checkForComodification()
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\work\Desktop\Project Zomboid\
 * Qualified Name:     zombie.core.Collections.NulledArrayList
 * JD-Core Version:    0.6.0
 */