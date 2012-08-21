// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PlayWorldSoundEffect.java

package zombie.scripting.commands.World;

import javax.swing.JOptionPane;
import zombie.WorldSoundManager;
import zombie.iso.*;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptModule;
import zombie.scripting.objects.Waypoint;

public class PlayWorldSoundEffect extends BaseCommand
{

    public PlayWorldSoundEffect()
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
            radius = Integer.parseInt(params[1].trim().replace("\"", ""));
            volume = Integer.parseInt(params[2].trim().replace("\"", ""));
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
            WorldSoundManager.instance.addSound(null, w.x, w.y, w.z, radius, volume);
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
    int radius;
    int volume;
}
