// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Commands.java

package zombie.console;

import java.security.InvalidParameterException;
import zombie.characters.IsoPlayer;
import zombie.inventory.ItemContainer;
import zombie.ui.PZConsole;
import zombie.ui.TutorialManager;

public class Commands
{

    public Commands()
    {
    }

    public static void Log(String str)
    {
        PZConsole.instance.Log(str);
    }

    public static void ProcessCommand(String command, String params[])
    {
        command = command.toLowerCase();
        if(command.equals("addinv"))
            IsoPlayer.getInstance().getInventory().AddItem(params[0]);
        else
        if(command.equals("debug"))
        {
            if(params.length > 1)
                throw new InvalidParameterException();
            if(params[0].equals("on"))
            {
                TutorialManager.Debug = true;
                Log("Debug mode activated");
            }
            if(params[0].equals("off"))
            {
                TutorialManager.Debug = false;
                Log("Debug mode deactivated");
            }
        }
    }
}
