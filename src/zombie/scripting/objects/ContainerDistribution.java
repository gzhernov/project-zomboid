// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ContainerDistribution.java

package zombie.scripting.objects;

import java.util.ArrayList;
import java.util.Vector;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Rand;
import zombie.inventory.ItemContainer;
import zombie.inventory.ItemContainerFiller;
import zombie.iso.IsoCell;
import zombie.iso.areas.IsoRoom;

// Referenced classes of package zombie.scripting.objects:
//            BaseScriptObject, ScriptModule

public class ContainerDistribution extends BaseScriptObject
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


    public ContainerDistribution()
    {
        Containers = new NulledArrayList(1);
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
        if(param.equals("Room"))
            RoomDef = value;
        else
        if(param.equals("Containers"))
        {
            String containers[] = value.split("/");
            String arr$[] = containers;
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                String con = arr$[i$];
                Containers.add(con.trim());
            }

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

    public boolean ContainerValid(String containerName)
    {
        if(Containers.isEmpty())
            return true;
        for(int n = 0; n < Containers.size(); n++)
            if(((String)Containers.get(n)).equals(containerName))
                return true;

        return false;
    }

    public void Process(IsoCell cell)
    {
        if(RoomDef != null)
        {
            ArrayList rooms = FindRooms(cell);
            if(rooms.isEmpty())
                return;
            IsoRoom room = null;
            for(int n = 0; n < Entries.size(); n++)
            {
                Entry en = (Entry)Entries.get(n);
                int count = Rand.Next(en.minimum, en.maximum);
                for(int m = 0; m < count; m++)
                {
                    room = (IsoRoom)rooms.get(Rand.Next(rooms.size()));
                    if(room == null || room.Containers.isEmpty())
                    {
                        m--;
                        continue;
                    }
                    int z = Rand.Next(room.Containers.size());
                    if(ContainerValid(((ItemContainer)room.Containers.get(z)).type))
                    {
                        ItemContainer con = (ItemContainer)room.Containers.get(z);
                        String t = en.objectType;
                        if(!t.contains("."))
                            t = (new StringBuilder()).append(module.name).append(".").append(t).toString();
                        con.AddItem(t);
                        continue;
                    }
                    boolean bHasValid = false;
                    for(int nn = 0; nn < room.Containers.size(); nn++)
                        if(ContainerValid(((ItemContainer)room.Containers.get(nn)).type))
                            bHasValid = true;

                    if(bHasValid)
                        m--;
                }

            }

        } else
        {
            for(int n = 0; n < Entries.size(); n++)
            {
                Entry en = (Entry)Entries.get(n);
                int count = Rand.Next(en.minimum, en.maximum);
                for(int m = 0; m < count; m++)
                {
                    ItemContainer cona = getRandomContainer();
                    if(cona == null)
                        continue;
                    String t = en.objectType;
                    if(!t.contains("."))
                        t = (new StringBuilder()).append(module.name).append(".").append(t).toString();
                    cona.AddItem(t);
                }

            }

        }
    }

    private ItemContainer getRandomContainer()
    {
        NulledArrayList choices = new NulledArrayList();
        if(ItemContainerFiller.DistributionTarget.isEmpty())
            return null;
        boolean bDone = false;
        int giveup = 2000;
        do
        {
            if(bDone)
                break;
            if(--giveup <= 0)
                return null;
            for(int l = 0; l < ItemContainerFiller.Containers.size(); l++)
            {
                for(int k = 0; k < Containers.size(); k++)
                    if(((ItemContainer)ItemContainerFiller.Containers.get(l)).type.equals(Containers.get(k)))
                        choices.add(ItemContainerFiller.Containers.get(l));

            }

            if(!choices.isEmpty())
                bDone = true;
        } while(true);
        return (ItemContainer)choices.get(Rand.Next(choices.size()));
    }

    private IsoRoom FindRoom(IsoCell cell)
    {
        roomTemp.clear();
        for(int n = 0; n < cell.getRoomList().size(); n++)
        {
            IsoRoom room = (IsoRoom)cell.getRoomList().get(n);
            if(room.RoomDef != null && room.RoomDef.equals(RoomDef))
                roomTemp.add(room);
        }

        if(!roomTemp.isEmpty())
            return (IsoRoom)roomTemp.get(Rand.Next(roomTemp.size()));
        else
            return null;
    }

    private ArrayList FindRooms(IsoCell cell)
    {
        roomTemp.clear();
        for(int n = 0; n < cell.getRoomList().size(); n++)
        {
            IsoRoom room = (IsoRoom)cell.getRoomList().get(n);
            if(room.RoomDef != null && room.RoomDef.equals(RoomDef))
                roomTemp.add(room);
        }

        return roomTemp;
    }

    public String RoomDef;
    public NulledArrayList Containers;
    public NulledArrayList Entries;
    static ArrayList roomTemp = new ArrayList();

}
