// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ZomboidBitFlag.java

package zombie;

import java.io.*;
import java.util.HashMap;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;

public class ZomboidBitFlag
{

    public ZomboidBitFlag(int size)
    {
        this.size = 64;
    }

    public long toInt()
    {
        if(flags == null)
            return 0L;
        long f = 0L;
        for(int n = 0; n < flags.length; n++)
        {
            boolean b = flags[n];
            if(b)
                f |= 1L << n;
        }

        return f;
    }

    public void set(int off, boolean b)
    {
        if(off >= 64)
            return;
        long flags = toInt();
        if(b)
            flags |= 1L << off;
        else
            flags &= ~(1L << off);
        if(BitFlags.containsKey(Long.valueOf(flags)))
        {
            this.flags = (boolean[])BitFlags.get(Long.valueOf(flags));
            return;
        } else
        {
            this.flags = toArray(flags);
            boolean isSet = this.flags[5];
            BitFlags.put(Long.valueOf(flags), this.flags);
            return;
        }
    }

    public void clear()
    {
        flags = null;
    }

    public boolean isSet(int off)
    {
        if(off >= 64)
            return false;
        if(flags == null)
            return false;
        else
            return flags[off];
    }

    public boolean isSet(IsoFlagType flag)
    {
        return isSet(flag.index());
    }

    public void set(IsoFlagType flag, boolean b)
    {
        set(flag.index(), b);
    }

    public boolean isSet(IsoObjectType flag)
    {
        return isSet(flag.index());
    }

    public void set(IsoObjectType flag, boolean b)
    {
        set(flag.index(), b);
    }

    public void Or(ZomboidBitFlag SpriteFlags)
    {
        long result = SpriteFlags.toInt() | toInt();
        getFromLong(result);
    }

    public void save(DataOutputStream output)
        throws IOException
    {
        output.writeLong(toInt());
    }

    public void load(DataInputStream input)
        throws IOException
    {
        getFromLong(input.readLong());
    }

    public void getFromLong(long l)
    {
        long flags = l;
        if(BitFlags.containsKey(Long.valueOf(flags)))
        {
            this.flags = (boolean[])BitFlags.get(Long.valueOf(flags));
            return;
        } else
        {
            this.flags = toArray(flags);
            BitFlags.put(Long.valueOf(flags), this.flags);
            return;
        }
    }

    private static boolean[] toArray(long f)
    {
        boolean flags[] = new boolean[64];
        for(int n = 0; n < 64; n++)
        {
            long d = 1L << (int)(long)n;
            if((d & f) == d)
                flags[n] = true;
        }

        return flags;
    }

    public static HashMap BitFlags;
    protected boolean flags[];
    public int size;

    static 
    {
        BitFlags = new HashMap();
        BitFlags.put(new Long(0L), new boolean[64]);
    }
}