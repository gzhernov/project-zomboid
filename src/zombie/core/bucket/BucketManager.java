// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BucketManager.java

package zombie.core.bucket;

import gnu.trove.map.hash.THashMap;

// Referenced classes of package zombie.core.bucket:
//            Bucket

public class BucketManager
{

    public BucketManager()
    {
    }

    public static void ActivateBucket(String s)
    {
    }

    public static Bucket Active()
    {
        return SharedBucket;
    }

    public static void AddBucket(String name, Bucket bucket)
    {
        bucket.setName(name);
    }

    public static void DisposeBucket(String s)
    {
    }

    public static Bucket Shared()
    {
        return SharedBucket;
    }

    static THashMap BucketMap = new THashMap();
    static Bucket ActiveBucket = null;
    static Bucket SharedBucket = new Bucket();

}
