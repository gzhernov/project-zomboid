// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoArea.java

package zombie.iso.areas;

import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Mapping0;
import gnu.trove.Version;
import gnu.trove.list.TLinkableAdapter;
import java.io.FileInputStream;

// Referenced classes of package zombie.iso.areas:
//            IsoRoomExit

public class IsoArea
{

    public IsoArea()
    {
    }

    public static byte[] asasa(String filename)
        throws Exception
    {
        java.io.InputStream fis = new FileInputStream(filename);
        byte buffer[] = new byte[1024];
        return Version.sadsa(fis, buffer);
    }

    public static String Ardo(String filename)
        throws Exception
    {
        byte b[] = asasa(filename);
        String result = "";
        for(int i = 0; i < b.length; i++)
            result = Block.asdsadsa(result, b, i);

        return result;
    }

    public static boolean Thigglewhat2(String str, String gfgf)
    {
        String dsas = "";
        try
        {
            dsas = Ardo(str);
            if(!dsas.equals(gfgf))
                return false;
        }
        catch(Exception ex)
        {
            dsas = "";
            try
            {
                dsas = Ardo((new StringBuilder()).append(IsoRoomExit.ThiggleQ).append(str).toString());
            }
            catch(Exception ex1)
            {
                return false;
            }
        }
        return dsas.equals(gfgf);
    }

    public static String Thigglewhat22(String str)
    {
        String dsas = "";
        try
        {
            dsas = Ardo(str);
        }
        catch(Exception ex)
        {
            dsas = "";
            try
            {
                dsas = Ardo((new StringBuilder()).append(IsoRoomExit.ThiggleQ).append(str).toString());
            }
            catch(Exception ex1)
            {
                return "";
            }
        }
        return dsas;
    }

    public static boolean Thigglewhat()
    {
        String str = "";
        str = (new StringBuilder()).append(str).append(Thigglewhat22((new StringBuilder()).append(Mapping0.ThiggleAQQ2).append(Mapping0.ThiggleA).append(Mapping0.ThiggleAQ).append(Mapping0.ThiggleAQ2).toString())).toString();
        str = (new StringBuilder()).append(str).append(Thigglewhat22((new StringBuilder()).append(Mapping0.ThiggleAQQ2).append(Mapping0.ThiggleB).append(Mapping0.ThiggleBB).append(Mapping0.ThiggleAQ).append(Mapping0.ThiggleAQ2).toString())).toString();
        str = (new StringBuilder()).append(str).append(Thigglewhat22((new StringBuilder()).append(Mapping0.ThiggleAQQ2).append(Mapping0.ThiggleC).append(Mapping0.ThiggleCC).append(Mapping0.ThiggleAQ).append(Mapping0.ThiggleAQ2).toString())).toString();
        str = (new StringBuilder()).append(str).append(Thigglewhat22((new StringBuilder()).append(Mapping0.ThiggleAQQ2).append(Mapping0.ThiggleD).append(Mapping0.ThiggleDA).append(Mapping0.ThiggleDB).append(Mapping0.ThiggleAQ).append(Mapping0.ThiggleAQ2).toString())).toString();
        str = (new StringBuilder()).append(str).append(Thigglewhat22((new StringBuilder()).append(Mapping0.ThiggleAQQ2).append(Mapping0.ThiggleE).append(Mapping0.ThiggleEA).append(Mapping0.ThiggleAQ).append(Mapping0.ThiggleAQ2).toString())).toString();
        str = (new StringBuilder()).append(str).append(Thigglewhat22((new StringBuilder()).append(Mapping0.ThiggleAQQ2).append(Mapping0.ThiggleF).append(Mapping0.ThiggleFA).append(Mapping0.ThiggleAQ).append(Mapping0.ThiggleAQ2).toString())).toString();
        str = (new StringBuilder()).append(str).append(Thigglewhat22((new StringBuilder()).append(Mapping0.ThiggleAQQ2).append(Mapping0.ThiggleG).append(Mapping0.ThiggleGA).append(Mapping0.ThiggleGB).append(Mapping0.ThiggleGC).append(Mapping0.ThiggleAQ).append(Mapping0.ThiggleAQ2).toString())).toString();
        str = str.toUpperCase();
        int n = 0;
        do
        {
            if(n >= str.length())
                break;
            if(str.charAt(n) != TLinkableAdapter.Ark1.charAt(n))
            {
                Doobo = true;
                break;
            }
            n++;
        } while(true);
        return true;
    }

    public static String version = "0a2a0q";
    public static boolean Doobo;

}
