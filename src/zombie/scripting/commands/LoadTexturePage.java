// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LoadTexturePage.java

package zombie.scripting.commands;

import java.util.Stack;
import zombie.FrameLoader;
import zombie.core.textures.TexturePackPage;

// Referenced classes of package zombie.scripting.commands:
//            BaseCommand

public class LoadTexturePage extends BaseCommand
{
    public class WatchPair
    {

        public String name;
        public String token;
       

        public WatchPair(String name, String token)
        {
          
            this.name = name;
            this.token = token;
        }
    }


    public LoadTexturePage()
    {
        page = null;
        Pairs = new Stack();
    }

    public void init(String object, String params[])
    {
        page = params[0];
        if(params.length > 1)
        {
            String name = null;
            String token = null;
            boolean bAlt = false;
            for(int n = 1; n < params.length; n++)
            {
                if(!bAlt)
                {
                    token = null;
                    name = params[n];
                } else
                {
                    token = params[n];
                    Pairs.add(new WatchPair(name, token));
                    name = null;
                    token = null;
                }
                bAlt = !bAlt;
            }

        }
    }

    public void begin()
    {
        if(FrameLoader.bServer)
            return;
        if(!Pairs.isEmpty())
            TexturePackPage.getPackPage(page, Pairs);
        else
            TexturePackPage.getPackPage(page);
    }

    public void Finish()
    {
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

    String page;
    public Stack Pairs;
}
