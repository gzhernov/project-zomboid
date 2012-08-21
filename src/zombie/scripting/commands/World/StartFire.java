// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StartFire.java

package zombie.scripting.commands.World;

import javax.swing.JOptionPane;
import zombie.iso.*;
import zombie.iso.objects.IsoFireManager;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptModule;
import zombie.scripting.objects.Waypoint;

public class StartFire extends BaseCommand
{

    public StartFire()
    {
    }

    public void init(String object, String params[])
    {
        if(object == null || !object.equals("World"))
        {
            JOptionPane.showMessageDialog(null, (new StringBuilder()).append("Command: ").append(getClass().getName()).append(" is not part of ").append(object).toString(), "Error", 0);
            return;
        } else
        {
            position = params[0].trim().replace("\"", "");
            Energy = Integer.parseInt(params[1].trim());
            return;
        }
    }

    public void begin()
    {
        Waypoint w = module.getWaypoint(position);
        if(w == null)
            return;
        IsoGridSquare sq = IsoWorld.instance.CurrentCell.getGridSquare(w.x, w.y, w.z);
        if(sq != null)
            IsoFireManager.StartFire(IsoWorld.instance.CurrentCell, sq, true, Energy);
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
    int Energy;
}
