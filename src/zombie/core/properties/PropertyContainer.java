// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PropertyContainer.java

package zombie.core.properties;

import gnu.trove.map.hash.THashMap;
import java.io.*;
import java.util.*;
import zombie.GameWindow;
import zombie.ZomboidBitFlag;
import zombie.iso.SpriteDetails.IsoFlagType;

public class PropertyContainer
{

    public PropertyContainer()
    {
    }

    public PropertyContainer(PropertyContainer other)
    {
        if(other.Properties != null)
            Properties = new THashMap();
        java.util.Map.Entry entry;
        for(Iterator i$ = other.Properties.entrySet().iterator(); i$.hasNext(); Properties.put(entry.getKey(), entry.getValue()))
            entry = (java.util.Map.Entry)i$.next();

    }

    public void AddProperties(PropertyContainer other)
    {
        if(other.Properties != null)
        {
            if(Properties == null)
                Properties = new THashMap();
            Iterator i$ = other.Properties.entrySet().iterator();
            do
            {
                if(!i$.hasNext())
                    break;
                java.util.Map.Entry entry = (java.util.Map.Entry)i$.next();
                if(!Properties.containsKey(entry.getKey()))
                    Properties.put(entry.getKey(), entry.getValue());
            } while(true);
        }
        if(other.SpriteFlags == null)
            return;
        if(SpriteFlags == null)
            SpriteFlags = new ZomboidBitFlag(IsoFlagType.MAX.index());
        SpriteFlags.Or(other.SpriteFlags);
    }

    public void Clear()
    {
        if(SpriteFlags != null)
            SpriteFlags.clear();
        if(Properties == null)
        {
            return;
        } else
        {
            Properties.clear();
            return;
        }
    }

    public boolean Is(IsoFlagType flag)
    {
        if(SpriteFlags == null)
        {
            return false;
        } else
        {
            boolean bIs = SpriteFlags.isSet(flag);
            return bIs;
        }
    }

    public void Set(String propName, String propName2)
    {
        if(IsoFlagType.FromString(propName) != IsoFlagType.MAX)
        {
            Set(IsoFlagType.FromString(propName), propName2);
            return;
        }
        if(Properties == null)
            Properties = new THashMap();
        Properties.put(propName, propName2);
    }

    public void Set(IsoFlagType propName)
    {
        if(SpriteFlags == null)
            SpriteFlags = new ZomboidBitFlag(IsoFlagType.MAX.index());
        SpriteFlags.set(propName, true);
    }

    public void Set(IsoFlagType propName, String lazy)
    {
        if(SpriteFlags == null)
            SpriteFlags = new ZomboidBitFlag(IsoFlagType.MAX.index());
        SpriteFlags.set(propName, true);
    }

    public void UnSet(String propName)
    {
        if(Properties == null)
        {
            return;
        } else
        {
            Properties.remove(propName);
            return;
        }
    }

    public void UnSet(IsoFlagType propName)
    {
        if(SpriteFlags == null)
        {
            return;
        } else
        {
            SpriteFlags.set(propName, false);
            return;
        }
    }

    public String Val(String property)
    {
        if(Properties == null)
            return null;
        if(Properties.containsKey(property))
            return (String)Properties.get(property);
        else
            return null;
    }

    public boolean Is(String isoPropertyType)
    {
        if(Properties == null)
            return false;
        return Properties.containsKey(isoPropertyType);
    }

    public void save(DataOutputStream output)
        throws IOException
    {
        if(Properties == null)
            Properties = new THashMap();
        if(SpriteFlags == null)
            SpriteFlags = new ZomboidBitFlag(IsoFlagType.MAX.index());
        output.writeInt(Properties.entrySet().size());
        java.util.Map.Entry entry;
        for(Iterator i$ = Properties.entrySet().iterator(); i$.hasNext(); GameWindow.WriteString(output, (String)entry.getValue()))
        {
            entry = (java.util.Map.Entry)i$.next();
            GameWindow.WriteString(output, ((String)entry.getKey()).toString());
        }

    }

    public void load(DataInputStream input)
        throws IOException
    {
        int count = input.readInt();
        Properties = new THashMap();
        SpriteFlags = new ZomboidBitFlag(IsoFlagType.MAX.index());
        for(int n = 0; n < count; n++)
        {
            String prop = GameWindow.ReadString(input);
            Properties.put(prop, GameWindow.ReadString(input));
        }

    }

    public int loadStream(byte databytes[], int index)
    {
        return index;
    }

    public int saveStream(byte databytes[], int index)
    {
        return index;
    }

    ZomboidBitFlag SpriteFlags;
    THashMap Properties;
}
