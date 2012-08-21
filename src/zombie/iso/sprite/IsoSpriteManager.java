// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoSpriteManager.java

package zombie.iso.sprite;

import gnu.trove.map.hash.THashMap;
import java.util.Collection;
import java.util.Iterator;

// Referenced classes of package zombie.iso.sprite:
//            IsoSprite

public class IsoSpriteManager
{

    public IsoSpriteManager()
    {
        SpriteMap = new THashMap();
        NamedMap = new THashMap();
        IntMap = new THashMap();
        ID = 0;
    }

    public void Dispose()
    {
        IsoSprite sprite;
        for(Iterator i$ = SpriteMap.values().iterator(); i$.hasNext(); sprite.Dispose())
            sprite = (IsoSprite)i$.next();

        SpriteMap.clear();
    }

    public IsoSprite getSprite(int gid)
    {
        if(IntMap.containsKey(Integer.valueOf(gid)))
            return (IsoSprite)IntMap.get(Integer.valueOf(gid));
        else
            return null;
    }

    public IsoSprite getSprite(String gid)
    {
        if(NamedMap.containsKey(gid))
            return (IsoSprite)NamedMap.get(gid);
        else
            return AddSprite(gid);
    }

    public IsoSprite AddSprite(String tex)
    {
        IsoSprite spr = new IsoSprite(this);
        spr.LoadFramesNoDirPageSimple(tex);
        NamedMap.put(tex, spr);
        spr.ID = ID;
        IntMap.put(Integer.valueOf(ID++), spr);
        return spr;
    }

    THashMap SpriteMap;
    public THashMap NamedMap;
    public THashMap IntMap;
    public int ID;
}
