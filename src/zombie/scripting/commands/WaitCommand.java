// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WaitCommand.java

package zombie.scripting.commands;

import zombie.scripting.objects.Conditional;
import zombie.scripting.objects.Script;

// Referenced classes of package zombie.scripting.commands:
//            BaseCommand

public class WaitCommand extends BaseCommand
{

    public WaitCommand()
    {
        frames = 0;
        con = null;
        obj = null;
    }

    public boolean IsFinished()
    {
        if(con != null)
        {
            boolean passed = con.ConditionPassed(currentinstance);
            if(passed)
                passed = passed;
            return passed;
        } else
        {
            return frames <= 0;
        }
    }

    public void update()
    {
        frames--;
    }

    public void init(String object, String params[])
    {
        obj = object;
        try
        {
            frames = (int)(30F * Float.parseFloat(params[0].trim()));
        }
        catch(Exception ex)
        {
            String total = params[0].trim();
            for(int n = 1; n < params.length; n++)
            {
                total = (new StringBuilder()).append(total).append(", ").toString();
                total = (new StringBuilder()).append(total).append(params[n]).toString();
            }

            con = new Conditional(total.trim(), "");
        }
    }

    public boolean AllowCharacterBehaviour(String scriptCharacter)
    {
        return !scriptCharacter.equals(obj);
    }

    public void begin()
    {
    }

    public boolean DoesInstantly()
    {
        return false;
    }

    int frames;
    Conditional con;
    String obj;
}
