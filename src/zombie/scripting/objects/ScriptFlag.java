// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScriptFlag.java

package zombie.scripting.objects;


// Referenced classes of package zombie.scripting.objects:
//            BaseScriptObject

public class ScriptFlag extends BaseScriptObject
{

    public ScriptFlag()
    {
    }

    public void Load(String name, String strArray[])
    {
        this.name = name;
        value = strArray[0].trim();
    }

    public void SetValue(String newVal)
    {
        value = newVal;
    }

    public boolean IsValue(String test)
    {
        return value.equals(test);
    }

    public String name;
    public String value;
}
