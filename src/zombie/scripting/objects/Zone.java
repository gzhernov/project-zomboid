// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Zone.java

package zombie.scripting.objects;


// Referenced classes of package zombie.scripting.objects:
//            BaseScriptObject

public class Zone extends BaseScriptObject
{

    public Zone()
    {
        z = 0;
    }

    public Zone(String name, int x, int y, int x2, int y2)
    {
        z = 0;
        this.name = new String(name);
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Zone(String name, int x, int y, int x2, int y2, int z)
    {
        this.z = 0;
        this.name = new String(name);
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        this.z = z;
    }

    public void Load(String name, String strArray[])
    {
        this.name = new String(name);
        x = Integer.parseInt(strArray[0].trim());
        y = Integer.parseInt(strArray[1].trim());
        x2 = Integer.parseInt(strArray[2].trim());
        y2 = Integer.parseInt(strArray[3].trim());
        if(strArray.length > 4)
            z = Integer.parseInt(strArray[4].trim());
    }

    public int x;
    public int y;
    public int x2;
    public int y2;
    public int z;
    public String name;
}
