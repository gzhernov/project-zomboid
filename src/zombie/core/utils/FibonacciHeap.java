// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FibonacciHeap.java

package zombie.core.utils;

import java.util.*;
import zombie.ai.astar.AStarPathMap;

public final class FibonacciHeap
{
    public static final class Entry
    {

        public Object getValue()
        {
            return mElem;
        }

        public void setValue(Object value)
        {
            mElem = value;
        }

        public double getPriority()
        {
            return mPriority;
        }

        private int mDegree;
        private boolean mIsMarked;
        private Entry mNext;
        private Entry mPrev;
        private Entry mParent;
        private Entry mChild;
        private Object mElem;
        private double mPriority;
















        private Entry(Object elem, double priority)
        {
            mDegree = 0;
            mIsMarked = false;
            mNext = mPrev = this;
            mElem = elem;
            mPriority = priority;
        }

    }


    public FibonacciHeap()
    {
        mMin = null;
        mSize = 0;
        treeTable = new ArrayList(300);
        toVisit = new ArrayList(300);
    }

    public void empty()
    {
        mMin = null;
        mSize = 0;
    }

    public Entry enqueue(Object value, double priority)
    {
        checkPriority(priority);
        Entry result = new Entry(value, priority);
        mMin = mergeLists(mMin, result);
        mSize++;
        return result;
    }

    public Entry min()
    {
        if(isEmpty())
            throw new NoSuchElementException("Heap is empty.");
        else
            return mMin;
    }

    public boolean isEmpty()
    {
        return mMin == null;
    }

    public int size()
    {
        return mSize;
    }

    public static FibonacciHeap merge(FibonacciHeap one, FibonacciHeap two)
    {
        FibonacciHeap result = new FibonacciHeap();
        result.mMin = mergeLists(one.mMin, two.mMin);
        result.mSize = one.mSize + two.mSize;
        one.mSize = two.mSize = 0;
        one.mMin = null;
        two.mMin = null;
        return result;
    }

    public Entry dequeueMin()
    {
        if(isEmpty())
            throw new NoSuchElementException("Heap is empty.");
        mSize--;
        Entry minElem = mMin;
        if(mMin.mNext == mMin)
        {
            mMin = null;
        } else
        {
            mMin.mPrev.mNext = mMin.mNext;
            mMin.mNext.mPrev = mMin.mPrev;
            mMin = mMin.mNext;
        }
        if(minElem.mChild != null)
        {
            Entry curr = minElem.mChild;
            do
            {
                curr.mParent = null;
                curr = curr.mNext;
            } while(curr != minElem.mChild);
        }
        mMin = mergeLists(mMin, minElem.mChild);
        if(mMin == null)
            return minElem;
        treeTable.clear();
        toVisit.clear();
        for(Entry curr = mMin; toVisit.isEmpty() || toVisit.get(0) != curr; curr = curr.mNext)
            toVisit.add(curr);

        Iterator it = toVisit.iterator();
        do
        {
            if(!it.hasNext())
                break;
            Entry curr;
            do
            {
                for(curr = (Entry)it.next(); curr.mDegree >= treeTable.size(); treeTable.add(null));
                if(treeTable.get(curr.mDegree) == null)
                {
                    treeTable.set(curr.mDegree, curr);
                    break;
                }
                Entry other = (Entry)treeTable.get(curr.mDegree);
                treeTable.set(curr.mDegree, null);
                Entry min = other.mPriority >= curr.mPriority ? curr : other;
                Entry max = other.mPriority >= curr.mPriority ? other : curr;
                max.mNext.mPrev = max.mPrev;
                max.mPrev.mNext = max.mNext;
                max.mNext = max.mPrev = max;
                min.mChild = mergeLists(min.mChild, max);
                max.mParent = min;
                max.mIsMarked = false;
                ++min.mDegree;
                curr = min;
            } while(true);
            if(curr.mPriority <= mMin.mPriority)
                mMin = curr;
        } while(true);
        return minElem;
    }

    public void decreaseKey(Entry entry, double newPriority)
    {
        checkPriority(newPriority);
        if(newPriority > entry.mPriority)
        {
            throw new IllegalArgumentException("New priority exceeds old.");
        } else
        {
            decreaseKeyUnchecked(entry, newPriority);
            return;
        }
    }

    public void delete(Entry entry)
    {
        decreaseKeyUnchecked(entry, (-1.0D / 0.0D));
        dequeueMin();
    }

    public void delete(int i, zombie.ai.astar.AStarPathMap.Node node1)
    {
    }

    private void checkPriority(double priority)
    {
        if(Double.isNaN(priority))
            throw new IllegalArgumentException((new StringBuilder()).append(priority).append(" is invalid.").toString());
        else
            return;
    }

    private static Entry mergeLists(Entry one, Entry two)
    {
        if(one == null && two == null)
            return null;
        if(one != null && two == null)
            return one;
        if(one == null && two != null)
        {
            return two;
        } else
        {
            Entry oneNext = one.mNext;
            one.mNext = two.mNext;
            one.mNext.mPrev = one;
            two.mNext = oneNext;
            two.mNext.mPrev = two;
            return one.mPriority >= two.mPriority ? two : one;
        }
    }

    private void decreaseKeyUnchecked(Entry entry, double priority)
    {
        entry.mPriority = priority;
        if(entry.mParent != null && entry.mPriority <= entry.mParent.mPriority)
            cutNode(entry);
        if(entry.mPriority <= mMin.mPriority)
            mMin = entry;
    }

    private void decreaseKeyUncheckedNode(Entry entry, double priority)
    {
        entry.mPriority = priority;
        if(entry.mParent != null && entry.mPriority <= entry.mParent.mPriority)
            cutNodeNode(entry);
        if(entry.mPriority <= mMin.mPriority)
            mMin = entry;
    }

    private void cutNode(Entry entry)
    {
        entry.mIsMarked = false;
        if(entry.mParent == null)
            return;
        if(entry.mNext != entry)
        {
            entry.mNext.mPrev = entry.mPrev;
            entry.mPrev.mNext = entry.mNext;
        }
        if(entry.mParent.mChild == entry)
            if(entry.mNext != entry)
                entry.mParent.mChild = entry.mNext;
            else
                entry.mParent.mChild = null;
        --entry.mParent.mDegree;
        entry.mPrev = entry.mNext = entry;
        mMin = mergeLists(mMin, entry);
        if(entry.mParent.mIsMarked)
            cutNode(entry.mParent);
        else
            entry.mParent.mIsMarked = true;
        entry.mParent = null;
    }

    private void cutNodeNode(Entry entry)
    {
        entry.mIsMarked = false;
        if(entry.mParent == null)
            return;
        if(entry.mNext != entry)
        {
            entry.mNext.mPrev = entry.mPrev;
            entry.mPrev.mNext = entry.mNext;
        }
        if(entry.mParent.mChild == entry)
            if(entry.mNext != entry)
                entry.mParent.mChild = entry.mNext;
            else
                entry.mParent.mChild = null;
        --entry.mParent.mDegree;
        entry.mPrev = entry.mNext = entry;
        mMin = mergeLists(mMin, entry);
        if(entry.mParent.mIsMarked)
            cutNode(entry.mParent);
        else
            entry.mParent.mIsMarked = true;
        entry.mParent = null;
    }

    private Entry mMin;
    private int mSize;
    List treeTable;
    List toVisit;
}
