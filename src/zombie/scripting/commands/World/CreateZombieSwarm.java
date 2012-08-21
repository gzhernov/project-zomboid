// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CreateZombieSwarm.java

package zombie.scripting.commands.World;

import javax.swing.JOptionPane;
import zombie.iso.IsoWorld;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptModule;
import zombie.scripting.objects.Zone;

public class CreateZombieSwarm extends BaseCommand
{

    public CreateZombieSwarm()
    {
        num = 1;
    }

    public void init(String object, String params[])
    {
        if(object == null || !object.equals("World"))
        {
            JOptionPane.showMessageDialog(null, (new StringBuilder()).append("Command: ").append(getClass().getName()).append(" is not part of ").append(object).toString(), "Error", 0);
            return;
        } else
        {
            num = Integer.parseInt(params[0].trim());
            position = params[1].trim().replace("\"", "");
            return;
        }
    }

    public void begin()
    {
        Zone w = module.getZone(position);
        if(w == null)
        {
            return;
        } else
        {
            IsoWorld.instance.CreateSwarm(num, w.x, w.y, w.x2, w.y2);
            return;
        }
    }

    public boolean IsFinished()
    {
        return true;
    }

    public void update()
    {
    }

    public boolean DoesInstantly()
    {
        return true;
    }

    String position;
    int num;
}
