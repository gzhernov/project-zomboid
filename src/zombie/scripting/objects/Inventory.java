// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Inventory.java

package zombie.scripting.objects;

import zombie.core.Collections.NulledArrayList;

// Referenced classes of package zombie.scripting.objects:
//            BaseScriptObject

public class Inventory extends BaseScriptObject
{
    public class Source
    {

        public String type;
        public int count;
       

        public Source()
        {
          
            count = 1;
        }
    }


    public Inventory()
    {
        Items = new NulledArrayList();
    }

    public void Load(String name, String strArray[])
    {
        for(int n = 0; n < strArray.length; n++)
            if(strArray[n] != null)
                DoSource(strArray[n].trim());

    }

    private void DoSource(String type)
    {
        Source source = new Source();
        if(type.contains("="))
        {
            source.count = Integer.parseInt(type.split("=")[1].trim());
            type = type.split("=")[0].trim();
        }
        if(type.equals("null"))
            source.type = null;
        else
            source.type = type;
        Items.add(source);
    }

    public NulledArrayList Items;
}
