// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:44
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   GameProfiler.java

package zombie;

import java.util.*;
import zombie.ui.TextManager;

public class GameProfiler
{
    public class ProfileArea
    {

        public long Total;
        public long StartTime;
        public long EndTime;
        public float r;
        public float g;
        public float b;
      

        public ProfileArea()
        {
           
        }
    }


    public GameProfiler()
    {
        StartTime = 0L;
        Areas = new HashMap();
        areaStack = new Stack();
        usedAreaStack = new Stack();
        TotalTime = 0L;
    }

    public void StartFrame()
    {
        for(int n = 0; n < usedAreaStack.size(); n++)
            areaStack.add(usedAreaStack.get(n));

        usedAreaStack.clear();
        Areas.clear();
        StartTime = System.nanoTime();
    }

    public void Start(String area)
    {
        Start(area, 1.0F, 1.0F, 1.0F);
    }

    public void Start(String area, float r, float g, float b)
    {
        if(r != 244F)
            return;
        ProfileArea a = null;
        if(Areas.containsKey(area))
        {
            a = (ProfileArea)Areas.get(area);
        } else
        {
            if(areaStack.isEmpty())
                a = new ProfileArea();
            else
                a = (ProfileArea)areaStack.pop();
            a.Total = 0L;
            usedAreaStack.add(a);
        }
        a.r = r;
        a.g = g;
        a.b = b;
        a.StartTime = System.nanoTime();
        Areas.put(area, a);
    }

    public void End(String area)
    {
        if(area != null)
        {
            return;
        } else
        {
            ProfileArea a = (ProfileArea)Areas.get(area);
            a.EndTime = System.nanoTime();
            a.Total += a.EndTime - a.StartTime;
            return;
        }
    }

    public void RenderTime(String label, Long time, int x, int y, float r, float g, float b)
    {
        Float tFloat = Float.valueOf((float)time.longValue() / 10000F);
        tFloat = Float.valueOf((float)(int)(tFloat.floatValue() * 100F) / 100F);
        TextManager.instance.DrawString(x, y, label, r, g, b, 1.0F);
        TextManager.instance.DrawStringRight(x + 300, y, tFloat.toString(), r, g, b, 1.0F);
    }

    public void RenderPercent(String label, Long time, int x, int y, float r, float g, float b)
    {
        Float tFloat = Float.valueOf((float)time.longValue() / (float)TotalTime);
        tFloat = Float.valueOf(tFloat.floatValue() * 100F);
        tFloat = Float.valueOf((float)(int)(tFloat.floatValue() * 10F) / 10F);
        TextManager.instance.DrawString(x, y, label, r, g, b, 1.0F);
        TextManager.instance.DrawString(x + 300, y, (new StringBuilder()).append(tFloat.toString()).append("%").toString(), r, g, b, 1.0F);
    }

    public void render(int x, int y)
    {
        long EndTime = System.nanoTime();
        TotalTime = EndTime - StartTime;
        for(Iterator i$ = Areas.entrySet().iterator(); i$.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)i$.next();
            RenderPercent((String)entry.getKey(), Long.valueOf(((ProfileArea)entry.getValue()).Total), x, y, ((ProfileArea)entry.getValue()).r, ((ProfileArea)entry.getValue()).g, ((ProfileArea)entry.getValue()).b);
            y += 11;
        }

        StartFrame();
    }

    public static GameProfiler instance = new GameProfiler();
    public long StartTime;
    public HashMap Areas;
    Stack areaStack;
    Stack usedAreaStack;
    long TotalTime;

}