// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AddHelpIconToWorld.java

package zombie.scripting.commands.Tutorial;

import javax.swing.JOptionPane;
import zombie.characters.IsoGameCharacter;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptModule;
import zombie.scripting.objects.Waypoint;
import zombie.ui.UIManager;

public class AddHelpIconToWorld extends BaseCommand
{

    public AddHelpIconToWorld()
    {
        offset = 0;
        x = 0;
        y = 0;
        z = 0;
    }

    public void init(String object, String params[])
    {
        if(object == null || !object.equals("Tutorial"))
        {
            JOptionPane.showMessageDialog(null, (new StringBuilder()).append("Command: ").append(getClass().getName()).append(" is not part of ").append(object).toString(), "Error", 0);
            return;
        }
        title = params[0].trim().replace("\"", "");
        title = module.getLanguage(title);
        if(title.indexOf("\"") == 0)
        {
            title = title.substring(1);
            title = title.substring(0, title.length() - 1);
        }
        message = params[1].trim().replace("\"", "");
        message = module.getLanguage(message);
        if(message.indexOf("\"") == 0)
        {
            message = message.substring(1);
            message = message.substring(0, message.length() - 1);
        }
        location = params[2].trim().replace("\"", "");
        offset = Integer.parseInt(params[3].trim());
    }

    public void begin()
    {
        Waypoint pt = module.getWaypoint(location.trim());
        if(pt != null)
        {
            x = pt.x;
            y = pt.y;
            z = pt.z;
        } else
        {
            IsoGameCharacter chr = module.getCharacterActual(location);
            if(chr != null)
            {
                x = (int)chr.getX();
                y = (int)chr.getY();
                z = (int)chr.getZ();
            }
        }
        UIManager.AddTutorial(x, y, z, title, message, false, offset);
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

    String title;
    String message;
    String location;
    int offset;
    int x;
    int y;
    int z;
}
