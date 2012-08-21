// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoAnim.java

package zombie.iso.sprite;

import gnu.trove.map.hash.THashMap;
import zombie.core.Collections.NulledArrayList;
import zombie.core.textures.*;

// Referenced classes of package zombie.iso.sprite:
//            IsoDirectionFrame

public class IsoAnim
{

    public IsoAnim()
    {
        FinishUnloopedOnFrame = 0;
        FrameDelay = 0;
        LastFrame = 0;
        Frames = new NulledArrayList(8);
        looped = true;
        ID = 0;
    }

    void LoadExtraFrame(String ObjectName, String AnimName, int i)
    {
        name = AnimName;
        String pre = (new StringBuilder()).append("media/characters/").append(ObjectName).append("_").toString();
        String post = (new StringBuilder()).append("_").append(AnimName).append("_").toString();
        Integer a = new Integer(i);
        IsoDirectionFrame frame = new IsoDirectionFrame(Texture.getSharedTexture((new StringBuilder()).append(pre).append("8").append(post).append(a.toString()).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("9").append(post).append(a.toString()).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("6").append(post).append(a.toString()).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("3").append(post).append(a.toString()).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("2").append(post).append(a.toString()).append(".png").toString()));
        Frames.add(frame);
    }

    public void LoadFrames(String ObjectName, String AnimName, int nFrames)
    {
        name = AnimName;
        strBuf.setLength(0);
        strBuf.append("media/characters/");
        strBuf.append(ObjectName);
        strBuf.append("_1_");
        strBuf.append(AnimName);
        strBuf.append("_0.png");
        Texture tex = Texture.getSharedTexture(strBuf.toString());
        int diri = strBuf.indexOf("_1_") + 1;
        int framei = strBuf.indexOf("0.");
        for(int n = 0; n < nFrames; n++)
        {
            if(n == 10)
            {
                strBuf.setLength(0);
                strBuf.append("media/characters/");
                strBuf.append(ObjectName);
                strBuf.append("_1_");
                strBuf.append(AnimName);
                strBuf.append("_10.png");
            }
            statInt = Integer.valueOf(n);
            Integer a = statInt;
            IsoDirectionFrame frame = null;
            String str = a.toString();
            if(tex != null)
            {
                strBuf.setCharAt(diri, '9');
                for(int l = 0; l < str.length(); l++)
                    strBuf.setCharAt(framei + l, a.toString().charAt(l));

                String stra = strBuf.toString();
                strBuf.setCharAt(diri, '6');
                String strb = strBuf.toString();
                strBuf.setCharAt(diri, '3');
                String strc = strBuf.toString();
                strBuf.setCharAt(diri, '2');
                String strd = strBuf.toString();
                strBuf.setCharAt(diri, '1');
                String stre = strBuf.toString();
                strBuf.setCharAt(diri, '4');
                String strf = strBuf.toString();
                strBuf.setCharAt(diri, '7');
                String strg = strBuf.toString();
                strBuf.setCharAt(diri, '8');
                String strh = strBuf.toString();
                frame = new IsoDirectionFrame(Texture.getSharedTexture(stra), Texture.getSharedTexture(strb), Texture.getSharedTexture(strc), Texture.getSharedTexture(strd), Texture.getSharedTexture(stre), Texture.getSharedTexture(strf), Texture.getSharedTexture(strg), Texture.getSharedTexture(strh));
            } else
            {
                strBuf.setCharAt(diri, '8');
                strBuf.setCharAt(framei, a.toString().charAt(0));
                String stra = strBuf.toString();
                strBuf.setCharAt(diri, '9');
                String strb = strBuf.toString();
                strBuf.setCharAt(diri, '6');
                String strc = strBuf.toString();
                strBuf.setCharAt(diri, '3');
                String strd = strBuf.toString();
                strBuf.setCharAt(diri, '2');
                String stre = strBuf.toString();
                frame = new IsoDirectionFrame(Texture.getSharedTexture(stra), Texture.getSharedTexture(strb), Texture.getSharedTexture(strc), Texture.getSharedTexture(strd), Texture.getSharedTexture(stre));
            }
            Frames.add(frame);
        }

        FinishUnloopedOnFrame = (short)(Frames.size() - 1);
    }

    public void LoadFramesUseOtherFrame(String ObjectName, String Variant, String AnimName, String OtherAnimName, int nOtherFrameFrame, String pal)
    {
        name = AnimName;
        String pre = (new StringBuilder()).append("media/characters/").append(OtherAnimName).append("_").append(Variant).append("_").toString();
        String post = "_";
        String palstr = "";
        if(pal != null)
            palstr = (new StringBuilder()).append("_").append(pal).toString();
        for(int n = 0; n < 1; n++)
        {
            Integer a = new Integer(nOtherFrameFrame);
            IsoDirectionFrame frame = new IsoDirectionFrame(Texture.getSharedTexture((new StringBuilder()).append(pre).append("8").append(post).append(a.toString()).append(palstr).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("9").append(post).append(a.toString()).append(palstr).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("6").append(post).append(a.toString()).append(palstr).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("3").append(post).append(a.toString()).append(palstr).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("2").append(post).append(a.toString()).append(palstr).append(".png").toString()));
            Frames.add(frame);
        }

        FinishUnloopedOnFrame = (short)(Frames.size() - 1);
    }

    public void LoadFramesBits(String ObjectName, String Variant, String AnimName, int nFrames)
    {
        name = AnimName;
        String pre = (new StringBuilder()).append("media/characters/").append(AnimName).append("_").append(Variant).append("_").toString();
        String post = "_";
        for(int n = 0; n < nFrames; n++)
        {
            Integer a = new Integer(n);
            IsoDirectionFrame frame = new IsoDirectionFrame(Texture.getSharedTexture((new StringBuilder()).append(pre).append("8").append(post).append(a.toString()).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("9").append(post).append(a.toString()).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("6").append(post).append(a.toString()).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("3").append(post).append(a.toString()).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("2").append(post).append(a.toString()).append(".png").toString()));
            Frames.add(frame);
        }

        FinishUnloopedOnFrame = (short)(Frames.size() - 1);
    }

    public void LoadFramesBits(String ObjectName, String AnimName, int nFrames)
    {
        name = AnimName;
        String pre = (new StringBuilder()).append("media/characters/").append(ObjectName).append("_").append(AnimName).append("_").toString();
        String post = "_";
        for(int n = 0; n < nFrames; n++)
        {
            Integer a = new Integer(n);
            IsoDirectionFrame frame = new IsoDirectionFrame(Texture.getSharedTexture((new StringBuilder()).append(pre).append("8").append(post).append(a.toString()).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("9").append(post).append(a.toString()).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("6").append(post).append(a.toString()).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("3").append(post).append(a.toString()).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("2").append(post).append(a.toString()).append(".png").toString()));
            Frames.add(frame);
        }

        FinishUnloopedOnFrame = (short)(Frames.size() - 1);
    }

    public void LoadFramesBitRepeatFrame(String ObjectName, String Variant, String AnimName, int RepeatFrame, String pal)
    {
        name = AnimName;
        String pre = (new StringBuilder()).append("media/characters/").append(AnimName).append("_").append(Variant).append("_").toString();
        String post = "_";
        String palstr = "";
        if(pal != null)
            palstr = (new StringBuilder()).append("_").append(pal).toString();
        int n = RepeatFrame;
        Integer a = new Integer(n);
        IsoDirectionFrame frame = new IsoDirectionFrame(Texture.getSharedTexture((new StringBuilder()).append(pre).append("8").append(post).append(a.toString()).append(palstr).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("9").append(post).append(a.toString()).append(palstr).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("6").append(post).append(a.toString()).append(palstr).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("3").append(post).append(a.toString()).append(palstr).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("2").append(post).append(a.toString()).append(palstr).append(".png").toString()));
        Frames.add(frame);
        FinishUnloopedOnFrame = (short)(Frames.size() - 1);
    }

    public void LoadFramesBits(String ObjectName, String Variant, String AnimName, int nFrames, String pal)
    {
        name = AnimName;
        String pre = (new StringBuilder()).append("media/characters/").append(AnimName).append("_").append(Variant).append("_").toString();
        String post = "_";
        String palstr = "";
        if(pal != null)
            palstr = (new StringBuilder()).append("_").append(pal).toString();
        for(int n = 0; n < nFrames; n++)
        {
            Integer a = new Integer(n);
            IsoDirectionFrame frame = new IsoDirectionFrame(Texture.getSharedTexture((new StringBuilder()).append(pre).append("8").append(post).append(a.toString()).append(palstr).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("9").append(post).append(a.toString()).append(palstr).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("6").append(post).append(a.toString()).append(palstr).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("3").append(post).append(a.toString()).append(palstr).append(".png").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("2").append(post).append(a.toString()).append(palstr).append(".png").toString()));
            Frames.add(frame);
        }

        FinishUnloopedOnFrame = (short)(Frames.size() - 1);
    }

    public void LoadFramesPcx(String ObjectName, String AnimName, int nFrames)
    {
        name = AnimName;
        String pre = (new StringBuilder()).append("media/characters/").append(ObjectName).append("_").toString();
        String post = (new StringBuilder()).append("_").append(AnimName).append("_").toString();
        for(int n = 0; n < nFrames; n++)
        {
            Integer a = new Integer(n);
            IsoDirectionFrame frame = new IsoDirectionFrame(Texture.getSharedTexture((new StringBuilder()).append(pre).append("8").append(post).append(a.toString()).append(".pcx").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("9").append(post).append(a.toString()).append(".pcx").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("6").append(post).append(a.toString()).append(".pcx").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("3").append(post).append(a.toString()).append(".pcx").toString()), Texture.getSharedTexture((new StringBuilder()).append(pre).append("2").append(post).append(a.toString()).append(".pcx").toString()));
            Frames.add(frame);
        }

        FinishUnloopedOnFrame = (short)(Frames.size() - 1);
    }

    void Dispose()
    {
        for(int n = 0; n < Frames.size(); n++)
        {
            IsoDirectionFrame dir = (IsoDirectionFrame)Frames.get(n);
            dir.SetAllDirections(null);
        }

    }

    void LoadFrameExplicit(String ObjectName)
    {
        IsoDirectionFrame frame = new IsoDirectionFrame(Texture.getSharedTexture(ObjectName));
        Frames.add(frame);
    }

    void LoadFramesNoDir(String ObjectName, String AnimName, int nFrames)
    {
        name = AnimName;
        String pre = (new StringBuilder()).append("media/").append(ObjectName).toString();
        String post = (new StringBuilder()).append("_").append(AnimName).append("_").toString();
        for(int n = 0; n < nFrames; n++)
        {
            Integer a = new Integer(n);
            IsoDirectionFrame frame = new IsoDirectionFrame(Texture.getSharedTexture((new StringBuilder()).append(pre).append(post).append(a.toString()).append(".png").toString()));
            Frames.add(frame);
        }

        FinishUnloopedOnFrame = (short)(Frames.size() - 1);
    }

    void LoadFramesNoDirPage(String ObjectName, String AnimName, int nFrames)
    {
        name = AnimName;
        String pre = ObjectName;
        String post = (new StringBuilder()).append("_").append(AnimName).append("_").toString();
        for(int n = 0; n < nFrames; n++)
        {
            Integer a = new Integer(n);
            IsoDirectionFrame frame = new IsoDirectionFrame(TexturePackPage.getTexture((new StringBuilder()).append(pre).append(post).append(a.toString()).toString()));
            Frames.add(frame);
        }

        FinishUnloopedOnFrame = (short)(Frames.size() - 1);
    }

    void LoadFramesNoDirPage(String ObjectName)
    {
        name = "default";
        String pre = ObjectName;
        for(int n = 0; n < 1; n++)
        {
            Integer a = new Integer(n);
            IsoDirectionFrame frame = new IsoDirectionFrame(TexturePackPage.getTexture(pre));
            Frames.add(frame);
        }

        FinishUnloopedOnFrame = (short)(Frames.size() - 1);
    }

    public void LoadFramesPageSimple(String NObjectName, String SObjectName, String EObjectName, String WObjectName)
    {
        name = "default";
        for(int n = 0; n < 1; n++)
        {
            Integer a = new Integer(n);
            IsoDirectionFrame frame = new IsoDirectionFrame(TexturePackPage.getTexture(NObjectName), TexturePackPage.getTexture(SObjectName), TexturePackPage.getTexture(EObjectName), TexturePackPage.getTexture(WObjectName));
            Frames.add(frame);
        }

        FinishUnloopedOnFrame = (short)(Frames.size() - 1);
    }

    void LoadFramesNoDirPalette(String ObjectName, String AnimName, int nFrames, String Palette)
    {
        name = AnimName;
        String pre = (new StringBuilder()).append("media/characters/").append(ObjectName).toString();
        String post = (new StringBuilder()).append("_").append(AnimName).append("_").toString();
        for(int n = 0; n < nFrames; n++)
        {
            Integer a = new Integer(n);
            IsoDirectionFrame frame = new IsoDirectionFrame(Texture.getSharedTexture((new StringBuilder()).append(pre).append(post).append(a.toString()).append(".pcx").toString(), Palette));
            Frames.add(frame);
        }

        FinishUnloopedOnFrame = (short)(Frames.size() - 1);
    }

    void LoadFramesPalette(String ObjectName, String AnimName, int nFrames, zombie.core.textures.PaletteManager.PaletteInfo info)
    {
        name = AnimName;
        String pre = (new StringBuilder()).append("media/characters/").append(ObjectName).append("_").toString();
        String post = (new StringBuilder()).append("_").append(AnimName).append("_").toString();
        for(int n = 0; n < nFrames; n++)
        {
            Integer a = new Integer(n);
            IsoDirectionFrame frame = new IsoDirectionFrame(Texture.getSharedTexture((new StringBuilder()).append(pre).append("8").append(post).append(a.toString()).append(".pcx").toString(), info.palette, info.name), Texture.getSharedTexture((new StringBuilder()).append(pre).append("9").append(post).append(a.toString()).append(".pcx").toString(), info.palette, info.name), Texture.getSharedTexture((new StringBuilder()).append(pre).append("6").append(post).append(a.toString()).append(".pcx").toString(), info.palette, info.name), Texture.getSharedTexture((new StringBuilder()).append(pre).append("3").append(post).append(a.toString()).append(".pcx").toString(), info.palette, info.name), Texture.getSharedTexture((new StringBuilder()).append(pre).append("2").append(post).append(a.toString()).append(".pcx").toString(), info.palette, info.name));
            Frames.add(frame);
        }

        FinishUnloopedOnFrame = (short)(Frames.size() - 1);
    }

    void LoadFramesPalette(String ObjectName, String AnimName, int nFrames, String Palette)
    {
        name = AnimName;
        String pre = (new StringBuilder()).append(ObjectName).append("_").toString();
        String post = (new StringBuilder()).append("_").append(AnimName).append("_").toString();
        for(int n = 0; n < nFrames; n++)
        {
            Integer a = new Integer(n);
            IsoDirectionFrame frame = new IsoDirectionFrame(TexturePackPage.getTexture((new StringBuilder()).append(pre).append("8").append(post).append(a.toString()).append("_").append(Palette).toString()), TexturePackPage.getTexture((new StringBuilder()).append(pre).append("9").append(post).append(a.toString()).append("_").append(Palette).toString()), TexturePackPage.getTexture((new StringBuilder()).append(pre).append("6").append(post).append(a.toString()).append("_").append(Palette).toString()), TexturePackPage.getTexture((new StringBuilder()).append(pre).append("3").append(post).append(a.toString()).append("_").append(Palette).toString()), TexturePackPage.getTexture((new StringBuilder()).append(pre).append("2").append(post).append(a.toString()).append("_").append(Palette).toString()));
            Frames.add(frame);
        }

        FinishUnloopedOnFrame = (short)(Frames.size() - 1);
    }

    void DupeFrame()
    {
        for(int n = 0; n < 8; n++)
        {
            IsoDirectionFrame fr = new IsoDirectionFrame();
            fr.directions[n] = ((IsoDirectionFrame)Frames.get(0)).directions[n];
            fr.bDoFlip = ((IsoDirectionFrame)Frames.get(0)).bDoFlip;
            Frames.add(fr);
        }

    }

    public static THashMap GlobalAnimMap = new THashMap();
    public short FinishUnloopedOnFrame;
    public short FrameDelay;
    public short LastFrame;
    public NulledArrayList Frames;
    public String name;
    boolean looped;
    public int ID;
    static Integer statInt = new Integer(0);
    static StringBuffer strBuf = new StringBuffer(5000);
    static StringBuffer strBuf2 = new StringBuffer(5000);

}
