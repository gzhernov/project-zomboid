// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Waypoint.java

package zombie.scripting.objects;


// Referenced classes of package zombie.scripting.objects:
//            BaseScriptObject

public class Waypoint extends BaseScriptObject
{

    public Waypoint()
    {
    }

    public void Load(String name, String strArray[])
    {
        this.name = new String(name);
        x = Integer.parseInt(strArray[0].trim());
        y = Integer.parseInt(strArray[1].trim());
        z = Integer.parseInt(strArray[2].trim());
    }

    public int x;
    public int y;
    public int z;
    public String name;
}
