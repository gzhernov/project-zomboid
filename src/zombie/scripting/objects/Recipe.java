// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Recipe.java

package zombie.scripting.objects;

import java.util.ArrayList;
import zombie.inventory.InventoryItem;

// Referenced classes of package zombie.scripting.objects:
//            BaseScriptObject

public class Recipe extends BaseScriptObject
{
    public class Result
    {

        public String type;
      

        public Result()
        {
          
        }
    }

    public class Source
    {

        public String type;
        public boolean keep;
        public int count;
      

        public Source()
        {
           
            keep = false;
            count = 1;
        }
    }


    public Recipe()
    {
        TimeToMake = 0.0F;
        Source = new ArrayList();
        Result = null;
        LuaTest = null;
        LuaCreate = null;
        LuaGrab = null;
    }

    public int FindIndexOf(InventoryItem a)
    {
        if(a == null)
            return 4;
        if(((Source)Source.get(0)).type != null && ((Source)Source.get(0)).type.contains(a.getType()))
            return 0;
        if(((Source)Source.get(1)).type != null && ((Source)Source.get(1)).type.contains(a.getType()))
            return 1;
        if(((Source)Source.get(2)).type != null && ((Source)Source.get(2)).type.contains(a.getType()))
            return 2;
        return ((Source)Source.get(3)).type == null || !((Source)Source.get(3)).type.contains(a.getType()) ? 4 : 3;
    }

    public void Load(String name, String strArray[])
    {
        if(strArray.length < 6)
            return;
        DoSource(strArray[0].trim());
        DoSource(strArray[1].trim());
        DoSource(strArray[2].trim());
        DoSource(strArray[3].trim());
        DoResult(strArray[4].trim());
        if(strArray.length > 7)
        {
            LuaTest = strArray[6].trim();
            LuaCreate = strArray[7].trim();
            LuaGrab = strArray[8].trim();
        }
        TimeToMake = Float.parseFloat(strArray[5].trim());
    }

    private void DoSource(String type)
    {
        Source source = new Source();
        if(type.contains("="))
        {
            source.count = Integer.parseInt(type.split("=")[1].trim());
            type = type.split("=")[0].trim();
        }
        if(type.indexOf("keep") == 0)
        {
            type = type.replace("keep ", "");
            source.keep = true;
        }
        if(type.equals("null"))
            source.type = null;
        else
            source.type = type;
        Source.add(source);
    }

    private void DoResult(String type)
    {
        Result result = new Result();
        result.type = type;
        Result = result;
    }

    public float TimeToMake;
    public ArrayList Source;
    public Result Result;
    public String LuaTest;
    public String LuaCreate;
    public String LuaGrab;
}
