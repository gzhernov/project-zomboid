// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ZombieUpdateData.java

package zombie.network;

import zombie.characters.IsoZombie;
import zombie.core.Collections.NulledArrayList;
import zombie.iso.*;
import zombie.iso.sprite.IsoAnim;
import zombie.iso.sprite.IsoSprite;

// Referenced classes of package zombie.network:
//            GameServer

public class ZombieUpdateData
{

    public ZombieUpdateData()
    {
        ZombieID = 0;
        x = 0.0F;
        y = 0.0F;
        z = 0.0F;
        anim = 0;
        dir = 0;
        animframedelay = 0;
    }

    public void unspool()
    {
        if(ZombieID >= IsoWorld.instance.CurrentCell.getZombieList().size())
        {
            return;
        } else
        {
            IsoZombie zombie = (IsoZombie)IsoWorld.instance.CurrentCell.getZombieList().get(ZombieID);
            zombie.predXVel = (x - zombie.getLrx()) / (float)GameServer.instance.updatedelayMax;
            zombie.predYVel = (y - zombie.getLry()) / (float)GameServer.instance.updatedelayMax;
            zombie.setZ(z);
            zombie.setNx(x);
            zombie.setNy(y);
            zombie.setX(x);
            zombie.setY(y);
            zombie.setLrx(x);
            zombie.setLry(y);
            zombie.dir = IsoDirections.fromIndex(dir);
            zombie.sprite.CurrentAnim = (IsoAnim)zombie.sprite.AnimStack.get(anim);
            return;
        }
    }

    public int ZombieID;
    public float x;
    public float y;
    public float z;
    public int anim;
    public int dir;
    public int animframedelay;
}
