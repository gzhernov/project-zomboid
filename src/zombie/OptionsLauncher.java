// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   OptionsLauncher.java

package zombie;

import java.awt.EventQueue;
import java.awt.Toolkit;
import javax.swing.UIManager;
import zombie.core.LaunchDialog;

public class OptionsLauncher
{

    public OptionsLauncher()
    {
    }

    public static void main(String args[])
    {
        EventQueue.invokeLater(new Runnable() {

            public void run()
            {
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                java.awt.Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                LaunchDialog window = new LaunchDialog(null, true);
            }

        }
);
    }
}