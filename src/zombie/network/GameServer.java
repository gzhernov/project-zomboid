// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GameServer.java

package zombie.network;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.Display;
import zombie.FrameLoader;
import zombie.GameApplet;
import zombie.characters.IsoSurvivor;
import zombie.characters.IsoZombie;
import zombie.core.Collections.NulledArrayList;
import zombie.gameStates.GameStateMachine;
import zombie.gameStates.IngameState;
import zombie.iso.*;
import zombie.iso.sprite.IsoAnim;
import zombie.iso.sprite.IsoSprite;

// Referenced classes of package zombie.network:
//            PlayerUpdateData, PingData, DamagePlayer

public class GameServer
{

    public GameServer()
    {
        state = new IngameState();
        pingUpdate = 60;
        updatedelayMax = 5;
        updatedelay = updatedelayMax;
    }

    public void init()
    {
        state.enter();
    }

    public void run()
    {
        boolean bDone;
        bDone = false;
        init();
        
  while (!bDone)
    {
 try {
        
        pingUpdate--;
        if(pingUpdate > 0);
        if(FrameLoader.closeRequested)
            break; /* Loop/switch isn't completed */
      
        
            Display.sync(30);
            state.update();
            for(int n = 0; n < IsoWorld.instance.CurrentCell.getRemoteSurvivorList().size(); n++)
            {
                IsoSurvivor obj = (IsoSurvivor)IsoWorld.instance.CurrentCell.getRemoteSurvivorList().get(n);
                PlayerUpdateData data = new PlayerUpdateData();
                data.ID = obj.getRemoteID();
                data.x = obj.getX();
                data.y = obj.getY();
                data.z = obj.getZ();
                data.dir = obj.dir.index();
                data.animID = obj.getLegsSprite().CurrentAnim.ID;
                PingData data2 = new PingData();
                data2.ping = data.ping;
            }

            updatedelay--;
            if(updatedelay <= 0)
            {
                for(int n = 0; n < IsoWorld.instance.CurrentCell.getZombieList().size(); n++)
                {
                    IsoMovingObject obj = (IsoMovingObject)IsoWorld.instance.CurrentCell.getZombieList().get(n);
                    if(obj instanceof IsoZombie)
                        ((IsoZombie)(IsoZombie)obj).ZombieID = n;
                }

                updatedelay = instance.updatedelayMax;
            }
        }
        catch(Exception ex)
        {
            Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
       
 
        
    }
        
        

        
    }

    public void Damage(int RemoteID, int PartIndex, float Damage, boolean Scratch, boolean Bitten, boolean Infected)
    {
        DamagePlayer p = new DamagePlayer();
        p.RemoteID = RemoteID;
        p.PartIndex = PartIndex;
        p.Damage = Damage;
        p.Scratch = Scratch;
        p.Bitten = Bitten;
        p.Infected = Infected;
    }

    public void SendToClientsTCP(Object obj)
    {
    }

    public static GameServer instance = new GameServer();
    IngameState state;
    public int pingUpdate;
    public int updatedelayMax;
    public int updatedelay;

}
