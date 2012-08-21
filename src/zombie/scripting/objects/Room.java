// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Room.java

package zombie.scripting.objects;

import java.util.Vector;
import zombie.iso.IsoGridSquare;
import zombie.iso.areas.IsoRoom;

// Referenced classes of package zombie.scripting.objects:
//            BaseScriptObject

public class Room extends BaseScriptObject
{

    public Room()
    {
        room = null;
    }

    public Room(String RoomDef, IsoRoom room)
    {
        this.room = null;
        name = RoomDef;
        this.room = room;
        IsoGridSquare sq = (IsoGridSquare)room.TileList.get(0);
        x = sq.getX();
        y = sq.getY();
        z = sq.getZ();
    }

    public void Load(String name, String strArray[])
    {
        this.name = name;
        x = Integer.parseInt(strArray[0].trim());
        y = Integer.parseInt(strArray[1].trim());
        z = Integer.parseInt(strArray[2].trim());
    }

    public int x;
    public int y;
    public int z;
    public String name;
    public IsoRoom room;
}
