// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AddPlayer.java

package zombie.network;


// Referenced classes of package zombie.network:
//            ClientAddPlayer

public class AddPlayer
{

    public AddPlayer()
    {
    }

    AddPlayer(ClientAddPlayer player, int ID)
    {
        this.ID = ID;
        x = player.x;
        y = player.y;
        z = player.z;
        forename = player.forename;
        surname = player.surname;
    }

    public float x;
    public float y;
    public float z;
    public String forename;
    public String surname;
    public int ID;
}
