// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FloorDistribution.java

package zombie.scripting.objects;

import java.util.Stack;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.scripting.ScriptManager;

// Referenced classes of package zombie.scripting.objects:
//            BaseScriptObject, Zone, ScriptModule

public class FloorDistribution extends BaseScriptObject
{
    public class Entry
    {

        String objectType;
        int minimum;
        int maximum;
       

        public Entry(String o, int min, int max)
        {
          
            objectType = o;
            minimum = min;
            maximum = max;
        }
    }


    public FloorDistribution()
    {
        Entries = new NulledArrayList(1);
    }

    public void Load(String name, String strArray[])
    {
        String arr$[] = strArray;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            String str = arr$[i$];
            str = str.trim();
            String arr[] = str.split("=");
            if(arr.length == 2)
                DoLine(arr[0].trim(), arr[1].trim());
        }

    }

    private void DoLine(String param, String value)
    {
        if(param.equals("Zone"))
        {
            Zone = value;
        } else
        {
            int min = 0;
            int max = 0;
            if(value.contains("-"))
            {
                String v[] = value.split("-");
                min = Integer.parseInt(v[0].trim());
                max = Integer.parseInt(v[1].trim());
            } else
            {
                min = max = Integer.parseInt(value.trim());
            }
            Entry en = new Entry(param.trim(), min, max);
            Entries.add(en);
        }
    }

    public void Process(IsoCell cell)
    {
        Stack zones = ScriptManager.instance.getZones(Zone);
        if(zones.isEmpty())
            return;
        for(int m = 0; m < Entries.size(); m++)
        {
            int count = Rand.Next(((Entry)Entries.get(m)).minimum, ((Entry)Entries.get(m)).maximum);
            for(int n = 0; n < count; n++)
            {
                Zone choice = (Zone)zones.get(Rand.Next(zones.size()));
                IsoGridSquare sq = cell.getFreeTile(new zombie.iso.IsoCell.Zone(choice.name, choice.x, choice.y, choice.x2 - choice.x, choice.y2 - choice.y, choice.z));
                if(sq == null)
                    continue;
                String na = ((Entry)Entries.get(m)).objectType;
                if(!na.contains("."))
                    na = (new StringBuilder()).append(module.name).append(".").append(na).toString();
                sq.AddWorldInventoryItem(na, (float)(100 + Rand.Next(400)) / 1000F, (float)(100 + Rand.Next(400)) / 1000F, 0.0F);
            }

        }

    }

    public String Zone;
    public NulledArrayList Entries;
}
