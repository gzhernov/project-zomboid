// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   IndieLogger.java

package zombie;

import java.io.*;
import java.util.logging.Logger;

public class IndieLogger
{

    public IndieLogger()
    {
    }

    public static void init()
        throws IOException
    {
    }

    private static String getCacheDir()
    {
        String cacheDir = System.getProperty("deployment.user.cachedir");
        if(cacheDir == null || System.getProperty("os.name").startsWith("Win"))
            cacheDir = System.getProperty("java.io.tmpdir");
        return (new StringBuilder()).append(cacheDir).append(File.separator).append("lwjglcache").toString();
    }

    public static void Log(String s)
    {
    }

    public static Logger logger;
    private static FileWriter fwrite;
}