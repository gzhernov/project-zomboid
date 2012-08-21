// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScriptParsingUtils.java

package zombie.scripting;

import java.util.Stack;

public class ScriptParsingUtils
{

    public ScriptParsingUtils()
    {
    }

    public static String[] SplitExceptInbetween(String src, String delimiter, String exceptionContainerOpen)
    {
        Stack Tokens = new Stack();
        boolean insideContainer = false;
        do
        {
            if(!src.contains(delimiter))
                break;
            if(!insideContainer)
            {
                int nextdelimiter = src.indexOf(delimiter);
                int nextcontaineropen = src.indexOf(exceptionContainerOpen);
                if(nextcontaineropen == -1)
                {
                    String str[] = src.split(delimiter);
                    for(int n = 0; n < str.length; n++)
                        Tokens.add(str[n].trim());

                    str = new String[Tokens.size()];
                    for(int n = 0; n < Tokens.size(); n++)
                        str[n] = (String)Tokens.get(n);

                    return str;
                }
                if(nextdelimiter == -1)
                {
                    String str[] = new String[Tokens.size()];
                    if(!src.trim().isEmpty())
                        Tokens.add(src.trim());
                    for(int n = 0; n < Tokens.size(); n++)
                        str[n] = (String)Tokens.get(n);

                    return str;
                }
                if(nextdelimiter < nextcontaineropen)
                {
                    Tokens.add(src.substring(0, nextdelimiter));
                    src = src.substring(nextdelimiter + 1);
                } else
                {
                    insideContainer = true;
                }
                continue;
            }
            int nextcontaineropen = src.indexOf(exceptionContainerOpen);
            int nextcontainerclose = src.indexOf(exceptionContainerOpen);
            int nextEnd = src.indexOf(exceptionContainerOpen, src.indexOf(exceptionContainerOpen) + 1);
            int nextDelimiterEnd = src.indexOf(delimiter, nextEnd + 1);
            if(nextDelimiterEnd == -1)
                break;
            String s = src.substring(0, nextDelimiterEnd).trim();
            if(!s.isEmpty())
                Tokens.add(s);
            src = src.substring(nextDelimiterEnd + 1);
            insideContainer = false;
        } while(true);
        if(!src.trim().isEmpty())
            Tokens.add(src.trim());
        String s[] = new String[Tokens.size()];
        for(int n = 0; n < Tokens.size(); n++)
            s[n] = (String)Tokens.get(n);

        return s;
    }

    public static String[] SplitExceptInbetween(String src, String delimiter, String exceptionContainerOpen, String exceptionContainerCloser)
    {
        String orig = src;
        int nextindexOfOpen = 0;
        int nextindexOfClosed = 0;
        int nextindexOfSemi = 0;
        int depth = 0;
        depth = 0;
        nextindexOfOpen = 0;
        nextindexOfClosed = 0;
        nextindexOfSemi = 0;
        Stack Tokens = new Stack();
        if(src.indexOf(exceptionContainerOpen, nextindexOfOpen) == -1)
            return src.split(delimiter);
        do
        {
            nextindexOfOpen = src.indexOf(exceptionContainerOpen, nextindexOfOpen + 1);
            nextindexOfClosed = src.indexOf(exceptionContainerCloser, nextindexOfClosed + 1);
            nextindexOfSemi = src.indexOf(delimiter, nextindexOfSemi + 1);
            if(nextindexOfSemi == -1)
            {
                Tokens.add(src.trim());
                src = "";
            } else
            if((nextindexOfSemi < nextindexOfOpen || nextindexOfOpen == -1 && nextindexOfSemi != -1) && depth == 0)
            {
                Tokens.add(src.substring(0, nextindexOfSemi));
                src = src.substring(nextindexOfSemi + 1);
                nextindexOfOpen = 0;
                nextindexOfClosed = 0;
                nextindexOfSemi = 0;
            } else
            if(nextindexOfClosed < nextindexOfOpen && nextindexOfClosed != -1 || nextindexOfOpen == -1)
            {
                nextindexOfOpen = nextindexOfClosed;
                if(--depth == 0)
                {
                    Tokens.add(src.substring(0, nextindexOfOpen + 1));
                    src = src.substring(nextindexOfOpen + 1);
                    nextindexOfOpen = 0;
                    nextindexOfClosed = 0;
                    nextindexOfSemi = 0;
                }
            } else
            if(nextindexOfOpen != -1 && nextindexOfClosed == -1)
            {
                nextindexOfClosed = nextindexOfOpen;
                depth++;
            } else
            if(nextindexOfOpen != -1 && nextindexOfClosed != -1 && nextindexOfOpen < nextindexOfClosed && (nextindexOfOpen > nextindexOfSemi || nextindexOfClosed < nextindexOfSemi))
            {
                nextindexOfOpen = nextindexOfClosed;
                Tokens.add(src.substring(0, nextindexOfSemi));
                src = src.substring(nextindexOfSemi + 1);
                nextindexOfOpen = 0;
                nextindexOfClosed = 0;
                nextindexOfSemi = 0;
            }
        } while(src.trim().length() > 0);
        if(!src.trim().isEmpty())
            Tokens.add(src.trim());
        String s[] = new String[Tokens.size()];
        for(int n = 0; n < Tokens.size(); n++)
            s[n] = ((String)Tokens.get(n)).trim();

        return s;
    }
}
