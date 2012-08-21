// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Bucket.java

package zombie.core.bucket;

import gnu.trove.map.hash.THashMap;
import java.util.Collection;
import java.util.Iterator;
import zombie.core.textures.Texture;
import zombie.iso.sprite.IsoSpriteManager;

public class Bucket
{

    public Bucket(IsoSpriteManager spr)
    {
        textures = new THashMap();
        SpriteManager = spr;
    }

    public Bucket()
    {
        textures = new THashMap();
        SpriteManager = new IsoSpriteManager();
    }

    public void AddTexture(String filename, Texture texture)
    {
        if(texture != null)
            textures.put(filename, texture);
    }

    public void Dispose()
    {
        Texture tex;
        for(Iterator i$ = textures.values().iterator(); i$.hasNext(); tex.destroy())
            tex = (Texture)i$.next();

        SpriteManager.Dispose();
    }

    public Texture getTexture(String filename)
    {
        return (Texture)textures.get(filename);
    }

    public boolean HasTexture(String filename)
    {
        return textures.containsKey(filename);
    }

    String getName()
    {
        return name;
    }

    void setName(String name)
    {
        this.name = name;
    }

    public IsoSpriteManager SpriteManager;
    private String name;
    private THashMap textures;
}
