// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AddHelpIconToUIElement.java

package zombie.scripting.commands.Tutorial;

import javax.swing.JOptionPane;
import zombie.scripting.commands.BaseCommand;
import zombie.scripting.objects.ScriptModule;
import zombie.ui.*;

public class AddHelpIconToUIElement extends BaseCommand
{

    public AddHelpIconToUIElement()
    {
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
        uielement = params[2].trim().replace("\"", "");
        x = Integer.parseInt(params[3].trim());
        y = Integer.parseInt(params[4].trim());
    }

    public void begin()
    {
        UIElement el = null;
        if(uielement.equals("SIDEBAR_INVENTORY"))
            el = Sidebar.InventoryIcon;
        if(el != null)
            UIManager.AddTutorial(el, x, y, title, message, false);
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
    int x;
    int y;
    String uielement;
}
