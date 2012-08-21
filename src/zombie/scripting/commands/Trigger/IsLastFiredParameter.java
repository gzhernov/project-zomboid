// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsLastFiredParameter.java

package zombie.scripting.commands.Trigger;

import gnu.trove.map.hash.THashMap;
import java.util.List;
import zombie.scripting.ScriptManager;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.Trigger;

public class IsLastFiredParameter extends BaseCommand
{

    public IsLastFiredParameter()
    {
        invert = false;
        param = 0;
    }

    public void begin()
    {
    }

    public boolean getValue()
    {
        List trigList = (List)ScriptManager.instance.TriggerMap.get(val);
        if(trigList == null)
            return false;
        if(trigList.isEmpty())
            return false;
        String par = null;
        switch(param)
        {
        case 0: // '\0'
            par = ((Trigger)trigList.get(0)).TriggerParam;
            break;

        case 1: // '\001'
            par = ((Trigger)trigList.get(0)).TriggerParam2;
            break;

        case 2: // '\002'
            par = ((Trigger)trigList.get(0)).TriggerParam3;
            break;
        }
        if(invert)
            return !paramval.equals(par);
        else
            return paramval.equals(par);
    }

    public boolean IsFinished()
    {
        return true;
    }

    public void update()
    {
    }

    public void init(String object, String params[])
    {
        val = object.toLowerCase();
        if(val.indexOf("!") == 0)
        {
            invert = true;
            val = val.substring(1);
        }
        if(params.length == 1)
            paramval = params[0].trim().replace("\"", "");
        else
        if(params.length == 2)
        {
            param = Integer.parseInt(params[0].trim());
            paramval = params[1].trim().replace("\"", "");
        }
    }

    public boolean DoesInstantly()
    {
        return true;
    }

    boolean invert;
    String val;
    String paramval;
    int param;
}
