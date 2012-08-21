// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GameClient.java

package zombie.network;

import zombie.characters.IsoPlayer;
import zombie.iso.IsoDirections;
import zombie.iso.sprite.IsoAnim;
import zombie.iso.sprite.IsoSprite;

// Referenced classes of package zombie.network:
//            ClientPlayerUpdateData

public class GameClient
{

    public GameClient()
    {
        readyForGameData = false;
        readyForUpdateData = false;
        updatedelay = 5;
        pingUpdate = 60;
    }

    public boolean Connect(String ip)
    {
        return ip == null ? true : true;
    }

    public void update()
    {
        updatedelay--;
        ClientPlayerUpdateData da = new ClientPlayerUpdateData();
        da.x = IsoPlayer.getInstance().getX();
        da.y = IsoPlayer.getInstance().getY();
        da.z = IsoPlayer.getInstance().getZ();
        da.animID = IsoPlayer.getInstance().sprite.CurrentAnim.ID;
        da.dir = IsoPlayer.getInstance().dir.index();
    }

    public void SendPlayer(IsoPlayer isoplayer)
    {
    }

    public void SendToServerTCP(Object obj)
    {
    }

    public int getPing()
    {
        return 0;
    }

    public static GameClient instance = new GameClient();
    public boolean readyForGameData;
    public boolean readyForUpdateData;
    public int updatedelay;
    public int pingUpdate;

}
