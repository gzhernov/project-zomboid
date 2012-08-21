// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IndieFileLoader.java

package zombie.core;

import java.io.*;
import zombie.GameWindow;

public class IndieFileLoader
{

    public IndieFileLoader()
    {
    }

    public static InputStreamReader getStreamReader(String path)
        throws FileNotFoundException
    {
        return getStreamReader(path, false);
    }

    public static InputStreamReader getStreamReader(String path, boolean bIgnoreJar)
        throws FileNotFoundException
    {
        InputStreamReader isr = null;
        InputStream is = GameWindow.class.getClassLoader().getResourceAsStream("media" + File.separator + path);
        if(is != null && !bIgnoreJar)
            isr = new InputStreamReader(is);
        else
            try
            {
                FileInputStream fis = new FileInputStream(path);
                isr = new InputStreamReader(fis);
            }
            catch(FileNotFoundException ex)
            {
                FileInputStream fis = new FileInputStream((new StringBuilder()).append("mods").append(File.separator).append(path).toString());
                isr = new InputStreamReader(fis);
            }
        return isr;
    }
}
