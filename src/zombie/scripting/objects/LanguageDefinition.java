// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LanguageDefinition.java

package zombie.scripting.objects;

import zombie.core.Collections.NulledArrayList;

// Referenced classes of package zombie.scripting.objects:
//            BaseScriptObject

public class LanguageDefinition extends BaseScriptObject
{

    public LanguageDefinition()
    {
        Items = new NulledArrayList();
    }

    public String get(int index)
    {
        return (String)Items.get(index);
    }

    public void Load(String name, String strArray[])
    {
        for(int n = 0; n < strArray.length; n++)
            if(strArray[n] != null)
                DoSource(strArray[n].trim());

    }

    private void DoSource(String type)
    {
        String source = "";
        source = type;
        Items.add(source);
    }

    public NulledArrayList Items;
}
