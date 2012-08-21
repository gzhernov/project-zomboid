// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TexturePackPage.java

package zombie.core.textures;

import gnu.trove.map.hash.THashMap;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import zombie.core.opengl.OpenGLState;
import zombie.scripting.commands.LoadTexturePage;

// Referenced classes of package zombie.core.textures:
//            Texture, TextureID

public class TexturePackPage
{

    public TexturePackPage()
    {
        subTextures = new THashMap();
        tex = null;
    }

    public static void LoadDir(String path)
        throws URISyntaxException
    {
        File dir = new File((new StringBuilder()).append("media/").append(path).toString());
        URL url = null;
        String children[] = dir.list();
        for(int i = 0; i < children.length; i++)
        {
            String filename = children[i];
            File fileMetaInf = new File(filename);
            searchFolders(fileMetaInf);
        }

        File fo = new File((new StringBuilder()).append("mods").append(path).toString());
        searchFolders(fo);
    }

    public static void searchFolders(File fo)
    {
        if(fo.isDirectory())
        {
            String internalNames[] = fo.list();
            for(int i = 0; i < internalNames.length; i++)
                searchFolders(new File((new StringBuilder()).append(fo.getAbsolutePath()).append("\\").append(internalNames[i]).toString()));

        } else
        if(fo.getAbsolutePath().toLowerCase().contains(".txt"))
            getPackPage(fo.getName().replace(".txt", ""));
    }

    public static TexturePackPage getPackPage(String page, Stack Pairs)
    {
        if(texturePackPageMap.containsKey(page))
            return (TexturePackPage)texturePackPageMap.get(page);
        TexturePackPage pack = new TexturePackPage();
        try
        {
            pack.load((new StringBuilder()).append("media/texturepacks/").append(page).append(".txt").toString(), Pairs);
        }
        catch(IOException ex)
        {
            Logger.getLogger(TexturePackPage.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        texturePackPageMap.put(page, pack);
        return pack;
    }

    public static TexturePackPage getPackPage(String page)
    {
        return getPackPage(page, null);
    }

    public static Texture getTexture(String tex)
    {
        if(tex.contains(".png"))
            return Texture.getSharedTexture(tex);
        Integer id = Integer.valueOf(OpenGLState.lastTextureID);
        if(subTextureMap2.containsKey((new StringBuilder()).append(tex).append("_").append(id.toString()).toString()))
            return (Texture)subTextureMap2.get((new StringBuilder()).append(tex).append("_").append(id.toString()).toString());
        if(subTextureMap.containsKey(tex))
            return (Texture)subTextureMap.get(tex);
        else
            return null;
    }

    public void load(String filename, Stack Pairs)
        throws IOException, FileNotFoundException
    {
        tex = Texture.getSharedTexture(filename.replace(".txt", ".png"));
        if(tex == null)
            return;
        InputStream is = new FileInputStream(filename);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        boolean nomask = false;
        do
        {
            String line;
            if((line = br.readLine()) == null)
                break;
            if(line.contains("##nomask"))
            {
                nomask = true;
            } else
            {
                if(!nomask && tex.getMask() == null)
                    tex.createMask();
                String split1[] = line.split("=");
                String spriteName = split1[0].trim();
                String rest = split1[1].trim();
                String params[] = rest.split(" ");
                if(spriteName != null)
                {
                    int x = Integer.parseInt(params[0]);
                    int y = Integer.parseInt(params[1]);
                    int w = Integer.parseInt(params[2]);
                    int h = Integer.parseInt(params[3]);
                    int offx = Integer.parseInt(params[4]);
                    int offy = Integer.parseInt(params[5]);
                    int origw = Integer.parseInt(params[6]);
                    int origh = Integer.parseInt(params[7]);
                    Texture sub = new Texture(tex.dataid, spriteName);
                    sub.offsetX = offx;
                    sub.offsetY = offy;
                    sub.widthOrig = origw;
                    sub.heightOrig = origh;
                    sub.width = w;
                    sub.height = h;
                    sub.xStart = (float)x / (float)tex.getWidthHW();
                    sub.yStart = (float)y / (float)tex.getHeightHW();
                    sub.xEnd = (float)(x + w) / (float)tex.getWidthHW();
                    sub.yEnd = (float)(y + h) / (float)tex.getHeightHW();
                    subTextures.put(spriteName, sub);
                    Integer id = Integer.valueOf(sub.dataid.id);
                    if(Pairs != null)
                    {
                        for(int n = 0; n < Pairs.size(); n++)
                        {
                            zombie.scripting.commands.LoadTexturePage.WatchPair pair = (zombie.scripting.commands.LoadTexturePage.WatchPair)Pairs.get(n);
                            if(!spriteName.contains(pair.token))
                                continue;
                            if(FoundTextures.containsKey(pair.name))
                            {
                                ((Stack)FoundTextures.get(pair.name)).add(spriteName);
                            } else
                            {
                                FoundTextures.put(pair.name, new Stack());
                                ((Stack)FoundTextures.get(pair.name)).add(spriteName);
                            }
                        }

                    }
                    subTextureMap.put(spriteName, sub);
                    subTextureMap2.put((new StringBuilder()).append(spriteName).append("_").append(id.toString()).toString(), sub);
                    sub.copyMaskRegion(tex, x, y, w, h);
                }
            }
        } while(true);
        br.close();
        isr.close();
        is.close();
    }

    public static THashMap FoundTextures = new THashMap();
    static THashMap subTextureMap = new THashMap();
    static THashMap subTextureMap2 = new THashMap();
    static THashMap texturePackPageMap = new THashMap();
    public THashMap subTextures;
    public Texture tex;

}
