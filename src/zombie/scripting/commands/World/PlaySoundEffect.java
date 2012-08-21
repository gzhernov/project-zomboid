// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PlaySoundEffect.java

package zombie.scripting.commands.World;

import javax.swing.JOptionPane;
import zombie.SoundManager;
import zombie.iso.*;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptModule;
import zombie.scripting.objects.Waypoint;

public class PlaySoundEffect extends BaseCommand
{

    public PlaySoundEffect()
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
            format = params[0].trim().replace("\"", "");
            sound = params[1].trim().replace("\"", "");
            position = params[2].trim().replace("\"", "");
            pitchVar = Float.parseFloat(params[3].trim().replace("\"", ""));
            radius = Integer.parseInt(params[4].trim().replace("\"", ""));
            volume = Float.parseFloat(params[5].trim().replace("\"", ""));
            ignoreOutside = params[6].trim().replace("\"", "").equals("true");
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
            if(format.equals("WAV"))
                SoundManager.instance.PlayWorldSoundWav(sound, sq, pitchVar, radius, volume, ignoreOutside);
            else
            if(format.equals("OGG"))
                SoundManager.instance.PlayWorldSound(sound, sq, pitchVar, radius, volume, ignoreOutside);
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
    String sound;
    float pitchVar;
    int radius;
    float volume;
    boolean ignoreOutside;
    public String format;
}
