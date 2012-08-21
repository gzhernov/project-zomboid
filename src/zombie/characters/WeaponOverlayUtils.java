// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WeaponOverlayUtils.java

package zombie.characters;

import gnu.trove.map.hash.THashMap;
import zombie.IndieGL;
import zombie.core.Collections.NulledArrayList;
import zombie.core.bucket.Bucket;
import zombie.core.bucket.BucketManager;
import zombie.core.textures.ColorInfo;
import zombie.inventory.types.HandWeapon;
import zombie.iso.sprite.*;

// Referenced classes of package zombie.characters:
//            IsoGameCharacter

public class WeaponOverlayUtils
{

    public WeaponOverlayUtils()
    {
    }

    public static void DrawWeapon(HandWeapon item, IsoGameCharacter chr, IsoSprite chrspr, float x, float y, float z, ColorInfo col)
    {
        if(item == null || item.getWeaponSprite() == null)
            return;
        if(!SpriteMap.containsKey(item.getWeaponSprite()))
        {
            IndieGL.End();
            IsoSprite spr = IsoSprite.CreateSprite(BucketManager.Shared().SpriteManager);
            for(int n = 0; n < chrspr.AnimStack.size(); n++)
                spr.LoadFrames(item.getWeaponSprite(), ((IsoAnim)chrspr.AnimStack.get(n)).name, ((IsoAnim)chrspr.AnimStack.get(n)).Frames.size());

            SpriteMap.put(item.getWeaponSprite(), spr);
        }
        IsoSprite sprite = (IsoSprite)SpriteMap.get(item.getWeaponSprite());
        int offX = 0;
        int offY = 0;
        if(sprite.def == null)
            sprite.def = new IsoSpriteInstance(sprite);
        sprite.PlayAnim(chr.sprite.CurrentAnim.name);
        sprite.def.Frame = chr.def.Frame;
        float temp = col.a;
        col.a = chr.alpha;
        sprite.render(chr.def, chr, x, y, z, chr.dir, chr.offsetX, chr.offsetY, col);
        col.a = temp;
    }

    static THashMap SpriteMap = new THashMap();

}
