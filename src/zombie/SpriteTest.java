// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SpriteTest.java

package zombie;

import org.lwjgl.opengl.Display;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureID;

// Referenced classes of package zombie:
//            FrameLoader

public class SpriteTest
{

    public SpriteTest()
    {
    }

    public static void main(String args[])
        throws Exception
    {
        Display.setResizable(true);
        Display.setTitle("SpriteTest");
        Display.setFullscreen(false);
        Core.getInstance().init(FrameLoader.FullX, FrameLoader.FullY);
        SpriteRenderer.instance = new SpriteRenderer();
        SpriteRenderer.instance.create();
        TextureID.UseFiltering = false;
        Texture tex = Texture.getSharedTexture("media/white.png");
        Texture tex2 = Texture.getSharedTexture("media/Wood3.png");
        TextureID.UseFiltering = true;
        for(; !Display.isCloseRequested(); Display.sync(60))
        {
            Core.getInstance().StartFrame();
            for(int x = 0; x < FrameLoader.FullX / 2; x++)
            {
                for(int y = 0; y < FrameLoader.FullY / 2; y++)
                    if(x % 2 == 0)
                        tex.render(x * 2, y * 2, 2, 2, 0.5F, (float)y / ((float)FrameLoader.FullY / 2.0F), 0.8F, 1.0F);
                    else
                        tex2.render(x * 2, y * 2, 2, 2, 0.5F, (float)y / ((float)FrameLoader.FullY / 2.0F), 0.8F, 1.0F);

            }

            for(int x = 0; x < FrameLoader.FullX / 2; x++)
            {
                for(int y = 0; y < FrameLoader.FullY / 2; y++)
                    if(x % 2 == 0)
                        tex.render(x * 2, y * 2, 2, 2, 0.5F, (float)y / ((float)FrameLoader.FullY / 2.0F), 0.8F, 1.0F);
                    else
                        tex2.render(x * 2, y * 2, 2, 2, 0.5F, (float)y / ((float)FrameLoader.FullY / 2.0F), 0.8F, 1.0F);

            }

            for(int x = 0; x < FrameLoader.FullX / 2; x++)
            {
                for(int y = 0; y < FrameLoader.FullY / 2; y++)
                    if(x % 2 == 0)
                        tex.render(x * 2, y * 2, 2, 2, 0.5F, (float)y / ((float)FrameLoader.FullY / 2.0F), 0.8F, 1.0F);
                    else
                        tex2.render(x * 2, y * 2, 2, 2, 0.5F, (float)y / ((float)FrameLoader.FullY / 2.0F), 0.8F, 1.0F);

            }

            for(int x = 0; x < FrameLoader.FullX / 2; x++)
            {
                for(int y = 0; y < FrameLoader.FullY / 2; y++)
                    if(y % 2 == 0)
                        tex.render(x * 2, y * 2, 2, 2, 0.5F, (float)y / ((float)FrameLoader.FullY / 2.0F), 0.8F, 1.0F);
                    else
                        tex2.render(x * 2, y * 2, 2, 2, 0.5F, (float)y / ((float)FrameLoader.FullY / 2.0F), 0.8F, 1.0F);

            }

            Core.getInstance().EndFrame();
            Core.getInstance().StartFrameUI();
            Core.getInstance().EndFrameUI();
            Display.update();
        }

    }
}