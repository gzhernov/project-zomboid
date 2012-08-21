// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Door.java

package zombie.scripting.objects;


// Referenced classes of package zombie.scripting.objects:
//            BaseScriptObject

public class Door extends BaseScriptObject
{

    public Door()
    {
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
}
