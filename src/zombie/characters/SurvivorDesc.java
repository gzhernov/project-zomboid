// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SurvivorDesc.java

package zombie.characters;

import gnu.trove.map.hash.THashMap;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Stack;
import zombie.GameWindow;
import zombie.core.Rand;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoWorld;

// Referenced classes of package zombie.characters:
//            SurvivorGroup, IsoZombie, IsoSurvivor, IsoPlayer, 
//            IsoGameCharacter

public class SurvivorDesc
{

    public static int getIDCount()
    {
        return IDCount;
    }

    public static void setIDCount(int aIDCount)
    {
        IDCount = aIDCount;
    }

    public SurvivorDesc()
    {
        Profession = "";
        ChildrenList = new Stack();
        forename = "None";
        ID = 0;
        Instance = null;
        ParentList = new Stack();
        SiblingList = new Stack();
        surname = "None";
        InventoryScript = null;
        legs = "Base_Legs";
        torso = "Base_Torso";
        head = "Base_Head1";
        top = "Shirt";
        bottoms = "Trousers";
        shoes = "Shoes1";
        shoespal = null;
        bottomspal = "Trousers_Grey";
        toppal = "Shirt_Blue";
        skinpal = "Skin_01";
        MetCount = new THashMap();
        bravery = 1.0F;
        loner = 0.0F;
        aggressiveness = 1.0F;
        compassion = 1.0F;
        temper = 0.0F;
        friendliness = 0.0F;
        favourindoors = 0.0F;
        loyalty = 0.0F;
        Group = new SurvivorGroup(this);
        ID = IDCount++;
        IsoWorld.instance.SurvivorDescriptors.put(Integer.valueOf(ID), this);
        doStats();
    }

    public SurvivorDesc(boolean bNew)
    {
        Profession = "";
        ChildrenList = new Stack();
        forename = "None";
        ID = 0;
        Instance = null;
        ParentList = new Stack();
        SiblingList = new Stack();
        surname = "None";
        InventoryScript = null;
        legs = "Base_Legs";
        torso = "Base_Torso";
        head = "Base_Head1";
        top = "Shirt";
        bottoms = "Trousers";
        shoes = "Shoes1";
        shoespal = null;
        bottomspal = "Trousers_Grey";
        toppal = "Shirt_Blue";
        skinpal = "Skin_01";
        MetCount = new THashMap();
        bravery = 1.0F;
        loner = 0.0F;
        aggressiveness = 1.0F;
        compassion = 1.0F;
        temper = 0.0F;
        friendliness = 0.0F;
        favourindoors = 0.0F;
        loyalty = 0.0F;
        Group = new SurvivorGroup(this);
        ID = IDCount++;
        doStats();
    }

    public void meet(SurvivorDesc desc)
    {
        if(MetCount.containsKey(Integer.valueOf(desc.ID)))
            MetCount.put(Integer.valueOf(desc.ID), Integer.valueOf(((Integer)MetCount.get(Integer.valueOf(desc.ID))).intValue() + 1));
        else
            MetCount.put(Integer.valueOf(desc.ID), Integer.valueOf(1));
        if(desc.MetCount.containsKey(Integer.valueOf(ID)))
            desc.MetCount.put(Integer.valueOf(ID), Integer.valueOf(((Integer)desc.MetCount.get(Integer.valueOf(ID))).intValue() + 1));
        else
            desc.MetCount.put(Integer.valueOf(ID), Integer.valueOf(1));
    }

    public void load(DataInputStream input, IsoGameCharacter chr)
        throws IOException
    {
        ID = input.readInt();
        IsoWorld.instance.SurvivorDescriptors.put(Integer.valueOf(ID), this);
        forename = GameWindow.ReadString(input);
        surname = GameWindow.ReadString(input);
        legs = GameWindow.ReadString(input);
        torso = GameWindow.ReadString(input);
        head = GameWindow.ReadString(input);
        top = GameWindow.ReadString(input);
        bottoms = GameWindow.ReadString(input);
        shoes = GameWindow.ReadString(input);
        shoespal = GameWindow.ReadString(input);
        bottomspal = GameWindow.ReadString(input);
        toppal = GameWindow.ReadString(input);
        skinpal = GameWindow.ReadString(input);
        if(shoespal.length() == 0)
            shoespal = null;
        doStats();
        if(IDCount < ID)
            IDCount = ID;
        Instance = chr;
    }

    public void load(ByteBuffer input, IsoGameCharacter chr)
        throws IOException
    {
        ID = input.getInt();
        IsoWorld.instance.SurvivorDescriptors.put(Integer.valueOf(ID), this);
        forename = GameWindow.ReadString(input);
        surname = GameWindow.ReadString(input);
        legs = GameWindow.ReadString(input);
        torso = GameWindow.ReadString(input);
        head = GameWindow.ReadString(input);
        top = GameWindow.ReadString(input);
        bottoms = GameWindow.ReadString(input);
        shoes = GameWindow.ReadString(input);
        shoespal = GameWindow.ReadString(input);
        bottomspal = GameWindow.ReadString(input);
        toppal = GameWindow.ReadString(input);
        skinpal = GameWindow.ReadString(input);
        if(shoespal.length() == 0)
            shoespal = null;
        doStats();
        if(IDCount < ID)
            IDCount = ID;
        Instance = chr;
    }

    public void save(DataOutputStream output)
        throws IOException
    {
        output.writeInt(ID);
        GameWindow.WriteString(output, forename);
        GameWindow.WriteString(output, surname);
        GameWindow.WriteString(output, legs);
        GameWindow.WriteString(output, torso);
        GameWindow.WriteString(output, head);
        GameWindow.WriteString(output, top);
        GameWindow.WriteString(output, bottoms);
        GameWindow.WriteString(output, shoes);
        if(shoespal == null)
            GameWindow.WriteString(output, "");
        else
            GameWindow.WriteString(output, shoespal);
        GameWindow.WriteString(output, bottomspal);
        GameWindow.WriteString(output, toppal);
        GameWindow.WriteString(output, skinpal);
    }

    public void save(ByteBuffer output)
        throws IOException
    {
        output.putInt(ID);
        GameWindow.WriteString(output, forename);
        GameWindow.WriteString(output, surname);
        GameWindow.WriteString(output, legs);
        GameWindow.WriteString(output, torso);
        GameWindow.WriteString(output, head);
        GameWindow.WriteString(output, top);
        GameWindow.WriteString(output, bottoms);
        GameWindow.WriteString(output, shoes);
        if(shoespal == null)
            GameWindow.WriteString(output, "");
        else
            GameWindow.WriteString(output, shoespal);
        GameWindow.WriteString(output, bottomspal);
        GameWindow.WriteString(output, toppal);
        GameWindow.WriteString(output, skinpal);
    }

    public void AddToGroup(SurvivorGroup Group)
    {
        if(this.Group == Group)
        {
            return;
        } else
        {
            Group.AddMember(this);
            this.Group = Group;
            return;
        }
    }

    public boolean InGroupWith(IsoMovingObject obj)
    {
        if(obj instanceof IsoZombie)
            return false;
        if((obj instanceof IsoSurvivor) || (obj instanceof IsoPlayer))
            return ((IsoGameCharacter)obj).descriptor.Group == Group && Group != null;
        else
            return false;
    }

    private void doStats()
    {
        bravery = Rand.Next(0.1F, 10F);
        aggressiveness = bravery;
        aggressiveness += Rand.Next(-5F, 5F);
        if(aggressiveness > 10F)
            aggressiveness = 10F;
        if(aggressiveness < 0.1F)
            aggressiveness = 0.1F;
        compassion = Rand.Next(0.1F, 10F);
        loner = Rand.Next(0.0F, 10F);
        if(Rand.Next(3) == 0)
        {
            loner += 5F;
            if(loner > 10F)
                loner = 10F;
        }
        temper = Rand.Next(0.1F, 10F);
        if(Rand.Next(3) == 0)
        {
            temper += 5F;
            if(temper > 10F)
                temper = 10F;
        }
        friendliness = Rand.Next(0.1F, 10F);
        favourindoors = Rand.Next(0.1F, 10F);
        loyalty = Rand.Next(0.1F, 10F);
    }

    public boolean Test(float val)
    {
        val *= 10F;
        return (float)Rand.Next(100) < val;
    }

    void AddToOthersGroup(SurvivorDesc descriptor)
    {
        if(descriptor.Group == Group && Group != null)
        {
            return;
        } else
        {
            Group = descriptor.Group;
            Group.AddMember(this);
            return;
        }
    }

    public int getMetCount(SurvivorDesc descriptor)
    {
        if(MetCount.containsKey(Integer.valueOf(descriptor.ID)))
            return ((Integer)MetCount.get(Integer.valueOf(descriptor.ID))).intValue();
        else
            return 0;
    }

    boolean IsLeader()
    {
        return Group != null && Group.Leader == this;
    }

    public SurvivorGroup getGroup()
    {
        return Group;
    }

    public void setGroup(SurvivorGroup Group)
    {
        this.Group = Group;
    }

    public Stack getChildrenList()
    {
        return ChildrenList;
    }

    public void setChildrenList(Stack ChildrenList)
    {
        this.ChildrenList = ChildrenList;
    }

    public String getForename()
    {
        return forename;
    }

    public void setForename(String forename)
    {
        this.forename = forename;
    }

    public int getID()
    {
        return ID;
    }

    public void setID(int ID)
    {
        this.ID = ID;
    }

    public IsoGameCharacter getInstance()
    {
        return Instance;
    }

    public void setInstance(IsoGameCharacter Instance)
    {
        this.Instance = Instance;
    }

    public Stack getParentList()
    {
        return ParentList;
    }

    public void setParentList(Stack ParentList)
    {
        this.ParentList = ParentList;
    }

    public Stack getSiblingList()
    {
        return SiblingList;
    }

    public void setSiblingList(Stack SiblingList)
    {
        this.SiblingList = SiblingList;
    }

    public String getSurname()
    {
        return surname;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public String getInventoryScript()
    {
        return InventoryScript;
    }

    public void setInventoryScript(String InventoryScript)
    {
        this.InventoryScript = InventoryScript;
    }

    public String getLegs()
    {
        return legs;
    }

    public void setLegs(String legs)
    {
        this.legs = legs;
    }

    public String getTorso()
    {
        return torso;
    }

    public void setTorso(String torso)
    {
        this.torso = torso;
    }

    public String getHead()
    {
        return head;
    }

    public void setHead(String head)
    {
        this.head = head;
    }

    public String getTop()
    {
        return top;
    }

    public void setTop(String top)
    {
        this.top = top;
    }

    public String getBottoms()
    {
        return bottoms;
    }

    public void setBottoms(String bottoms)
    {
        this.bottoms = bottoms;
    }

    public String getShoes()
    {
        return shoes;
    }

    public void setShoes(String shoes)
    {
        this.shoes = shoes;
    }

    public String getShoespal()
    {
        return shoespal;
    }

    public void setShoespal(String shoespal)
    {
        this.shoespal = shoespal;
    }

    public String getBottomspal()
    {
        return bottomspal;
    }

    public void setBottomspal(String bottomspal)
    {
        this.bottomspal = bottomspal;
    }

    public String getToppal()
    {
        return toppal;
    }

    public void setToppal(String toppal)
    {
        this.toppal = toppal;
    }

    public String getSkinpal()
    {
        return skinpal;
    }

    public void setSkinpal(String skinpal)
    {
        this.skinpal = skinpal;
    }

    public THashMap getMetCount()
    {
        return MetCount;
    }

    public void setMetCount(THashMap MetCount)
    {
        this.MetCount = MetCount;
    }

    public float getBravery()
    {
        return bravery;
    }

    public void setBravery(float bravery)
    {
        this.bravery = bravery;
    }

    public float getLoner()
    {
        return loner;
    }

    public void setLoner(float loner)
    {
        this.loner = loner;
    }

    public float getAggressiveness()
    {
        return aggressiveness;
    }

    public void setAggressiveness(float aggressiveness)
    {
        this.aggressiveness = aggressiveness;
    }

    public float getCompassion()
    {
        return compassion;
    }

    public void setCompassion(float compassion)
    {
        this.compassion = compassion;
    }

    public float getTemper()
    {
        return temper;
    }

    public void setTemper(float temper)
    {
        this.temper = temper;
    }

    public float getFriendliness()
    {
        return friendliness;
    }

    public void setFriendliness(float friendliness)
    {
        this.friendliness = friendliness;
    }

    public float getFavourindoors()
    {
        return favourindoors;
    }

    public void setFavourindoors(float favourindoors)
    {
        this.favourindoors = favourindoors;
    }

    public float getLoyalty()
    {
        return loyalty;
    }

    public void setLoyalty(float loyalty)
    {
        this.loyalty = loyalty;
    }

    public String getProfession()
    {
        return Profession;
    }

    public void setProfession(String Profession)
    {
        this.Profession = Profession;
    }

    protected static int IDCount = 0;
    public String Profession;
    protected SurvivorGroup Group;
    protected Stack ChildrenList;
    protected String forename;
    protected int ID;
    protected IsoGameCharacter Instance;
    protected Stack ParentList;
    protected Stack SiblingList;
    protected String surname;
    protected String InventoryScript;
    protected String legs;
    protected String torso;
    protected String head;
    protected String top;
    protected String bottoms;
    protected String shoes;
    protected String shoespal;
    protected String bottomspal;
    protected String toppal;
    protected String skinpal;
    protected THashMap MetCount;
    protected float bravery;
    protected float loner;
    protected float aggressiveness;
    protected float compassion;
    protected float temper;
    protected float friendliness;
    protected float favourindoors;
    protected float loyalty;

}
