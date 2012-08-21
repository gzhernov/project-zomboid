// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ShelfDistribution.java

package zombie.scripting.objects;

import java.util.Stack;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.core.properties.PropertyContainer;
import zombie.inventory.ItemContainerFiller;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.scripting.ScriptManager;

// Referenced classes of package zombie.scripting.objects:
//            BaseScriptObject, Zone, ScriptModule

public class ShelfDistribution extends BaseScriptObject
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


    public ShelfDistribution()
    {
        LootedValue = 0;
        Entries = new NulledArrayList(1);
        ItemDepth = 0.5F;
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
            {
                DoLine(arr[0].trim(), arr[1].trim());
                continue;
            }
            if(arr[0].trim().length() > 0)
                Entries.add(new Entry(arr[0].trim(), 1, 1));
        }

    }

    private void DoLine(String param, String value)
    {
        if(param.equals("Zone"))
            Zone = value;
        if(param.equals("LootedValue"))
            LootedValue = Integer.parseInt(value);
        if(param.equals("ItemDepth"))
            ItemDepth = Float.parseFloat(value);
    }

    public void Process(IsoCell cell)
    {
        Stack zones = ScriptManager.instance.getZones(Zone);
        for(int n = 0; n < zones.size(); n++)
        {
            Zone z = (Zone)zones.get(n);
            for(int x = z.x; x < z.x2; x++)
            {
                for(int y = z.y; y < z.y2; y++)
                {
                    IsoGridSquare sq = cell.getGridSquare(x, y, z.z);
                    if(sq == null)
                        continue;
                    float height;
                    String s;
                    if(sq.getProperties().Is(IsoFlagType.floorS) || sq.getProperties().Is(IsoFlagType.floorE))
                    {
                        height = 0.1F;
                        s = ((Entry)Entries.get(Rand.Next(Entries.size()))).objectType;
                        if(!s.contains("."))
                            s = (new StringBuilder()).append(module.name).append(".").append(s).toString();
                        ItemContainerFiller.FillTable(LootedValue, sq, null, s, height, ItemDepth);
                    }
                    if(sq.getProperties().Is(IsoFlagType.tableS) || sq.getProperties().Is(IsoFlagType.tableE))
                    {
                        height = 0.4F;
                        s = ((Entry)Entries.get(Rand.Next(Entries.size()))).objectType;
                        if(!s.contains("."))
                            s = (new StringBuilder()).append(module.name).append(".").append(s).toString();
                        ItemContainerFiller.FillTable(LootedValue, sq, null, s, height);
                    }
                    if(!sq.getProperties().Is(IsoFlagType.shelfE) && !sq.getProperties().Is(IsoFlagType.shelfS))
                        continue;
                    height = 0.65F;
                    s = ((Entry)Entries.get(Rand.Next(Entries.size()))).objectType;
                    if(!s.contains("."))
                        s = (new StringBuilder()).append(module.name).append(".").append(s).toString();
                    ItemContainerFiller.FillTable(LootedValue, sq, null, s, height, ItemDepth);
                }

            }

        }

    }

    public String Zone;
    public int LootedValue;
    public NulledArrayList Entries;
    private float ItemDepth;
}
