// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScriptTalker.java

package zombie.scripting.objects;

import zombie.characters.Talker;
import zombie.core.Collections.NulledArrayList;
import zombie.iso.*;

// Referenced classes of package zombie.scripting.objects:
//            BaseScriptObject

public class ScriptTalker extends BaseScriptObject
{

    public ScriptTalker()
    {
    }

    public void Load(String name, String strArray[])
    {
        this.name = name;
        type = strArray[0].trim();
        x = Integer.parseInt(strArray[1].trim());
        y = Integer.parseInt(strArray[2].trim());
        z = Integer.parseInt(strArray[3].trim());
    }

    public Talker getActual()
    {
        if(ac != null)
            return ac;
        IsoGridSquare sq = IsoWorld.instance.CurrentCell.getGridSquare(this.x, y, z);
        for(int x = 0; x < sq.getObjects().size(); x++)
        {
            IsoObject ob = (IsoObject)sq.getObjects().get(x);
            if((ob instanceof Talker) && ((Talker)ob).getTalkerType().equals(type))
            {
                ac = (Talker)ob;
                return (Talker)ob;
            }
        }

        return null;
    }

    public int x;
    public int y;
    public int z;
    public String name;
    public String type;
    Talker ac;
}
