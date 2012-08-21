// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InventoryItem.java

package zombie.inventory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;

import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.GameWindow;
import zombie.Lua.LuaManager;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoSurvivor;
import zombie.characters.SurvivorDesc;
import zombie.core.Collections.NulledArrayList;
import zombie.core.textures.Texture;
import zombie.inventory.types.Clothing;
import zombie.inventory.types.DrainableComboItem;
import zombie.inventory.types.Food;
import zombie.inventory.types.HandWeapon;
import zombie.inventory.types.Literature;
import zombie.iso.IsoDirections;
import zombie.iso.IsoObject;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Item;
import zombie.ui.ObjectTooltip;
import zombie.ui.TutorialManager;
import zombie.ui.UIManager;

// Referenced classes of package zombie.inventory:
//            ItemType, ItemContainer

public class InventoryItem
{
		 protected IsoGameCharacter previousOwner = null;
/*   52 */   protected Item ScriptItem = null;
/*   53 */   protected ItemType cat = ItemType.None;
/*   54 */   protected String consumeMenu = "Eat";
/*      */   protected ItemContainer container;
/*   56 */   protected int containerX = 0;
/*   57 */   protected int containerY = 0;
/*   58 */   protected boolean DisappearOnUse = true;
/*      */   protected String name;
/*   60 */   protected String replaceOnUse = null;
/*   61 */   protected int ConditionMax = 100;
/*   62 */   protected ItemContainer rightClickContainer = null;
/*   63 */   protected String swingAnim = "Rifle";
/*      */   protected Texture texture;
/*      */   protected Texture texturerotten;
/*      */   protected Texture textureCooked;
/*      */   protected Texture textureBurnt;
/*      */   protected String type;
/*   69 */   protected int uses = 1;
/*   70 */   protected float Age = 0.0F;
/*   71 */   protected boolean IsCookable = false;
/*   72 */   protected float CookingTime = 0.0F;
/*   73 */   protected float MinutesToCook = 60.0F;
/*   74 */   protected float MinutesToBurn = 120.0F;
/*   75 */   public boolean Cooked = false;
/*   76 */   protected boolean Burnt = false;
/*   77 */   protected int OffAge = 1000000000;
/*   78 */   protected int OffAgeMax = 1000000000;
/*   79 */   protected float Weight = 1.0F;
/*   80 */   protected float ActualWeight = 1.0F;
/*      */   protected String WorldTexture;
/*      */   protected String Description;
/*   83 */   protected int Condition = 100;
/*   84 */   protected String OffString = "Rotten";
/*   85 */   protected String CookedString = "Cooked";
/*   86 */   protected String UnCookedString = "Uncooked";
/*   87 */   protected String BurntString = "Burnt";
/*   88 */   protected String module = "Base";
/*   89 */   protected boolean AlwaysWelcomeGift = false;
/*   90 */   protected boolean CanBandage = false;
/*   91 */   protected float boredomChange = 0.0F;
/*   92 */   protected float unhappyChange = 0.0F;
/*   93 */   protected float stressChange = 0.0F;

/*   94 */   protected NulledArrayList<IsoObject> Taken = new NulledArrayList();

/*   95 */   protected IsoDirections placeDir = IsoDirections.Max;
/*   96 */   protected IsoDirections newPlaceDir = IsoDirections.Max;
/*   97 */   private KahluaTable table = null;
/*   98 */   public String ReplaceOnUseOn = null;
/*   99 */   public boolean IsWaterSource = false;
/*  100 */   public boolean CanStoreWater = false;
/*  101 */   public boolean CanStack = true;


    public KahluaTable getModData()
    {
        if(table == null)
            table = LuaManager.platform.newTable();
        return table;
    }

    public InventoryItem(String module, String name, String type, String tex)
    {
    	this.texture = Texture.getSharedTexture("media/inventory/" + tex + ".png");
    	/*  114 */     Texture.WarnFailFindTexture = false;
    	/*  115 */     this.texturerotten = Texture.trygetTexture("media/inventory/" + tex + "Rotten.png");
    	/*  116 */     this.textureCooked = Texture.trygetTexture("media/inventory/" + tex + "Cooked.png");
    	/*  117 */     this.textureBurnt = Texture.trygetTexture("media/inventory/" + tex + "Overdone.png");
    	/*  118 */     Texture.WarnFailFindTexture = true;
    	/*      */ 
    	/*  120 */     if (this.texture == null)
    	/*  121 */       this.texture = Texture.getSharedTexture("media/inventory/Question_On.png");
    	/*  122 */     if (this.texturerotten == null)
    	/*  123 */       this.texturerotten = this.texture;
    	/*  124 */     if (this.textureCooked == null)
    	/*  125 */       this.textureCooked = this.texture;
    	/*  126 */     if (this.textureBurnt == null) {
    	/*  127 */       this.textureBurnt = this.texture;
    	/*      */     }
    	/*  129 */     this.module = module;
    	/*  130 */     this.name = name;
    	/*  131 */     this.type = type;
    	/*  132 */     this.WorldTexture = tex.replace("Item_", "media/inventory/world/WItem_");
    	/*  133 */     this.WorldTexture += ".png";
    }

    public String getType()
    {
        return type;
    }

    public Texture getTex()
    {
        if(Burnt)
            return textureBurnt;
        if(Age > (float)OffAge)
            return texturerotten;
        if(isCooked())
            return textureCooked;
        else
            return texture;
    }

    public boolean IsRotten()
    {
        return Age > (float)OffAge;
    }

    public float HowRotten()
    {
        if(OffAgeMax - OffAge == 0)
        {
            return Age <= (float)OffAge ? 0.0F : 1.0F;
        } else
        {
            float del = (Age - (float)OffAge) / (float)(OffAgeMax - OffAge);
            return del;
        }
    }

    public boolean CanStack(InventoryItem item)
    {
        return this.Taken.isEmpty() && item.Taken.isEmpty() && name.equals(item.name) && CanStack;
    }

    public void DoTooltip(ObjectTooltip tooltipUI)
    {
        tooltipUI.render();
        int mid = 60;
        int y = 5;
        if(Burnt)
            tooltipUI.DrawText((new StringBuilder()).append(BurntString).append(" ").append(name).toString(), 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
        else
        if(Age > (float)OffAge)
            tooltipUI.DrawText((new StringBuilder()).append(OffString).append(" ").append(name).toString(), 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
        else
        if(isCooked())
            tooltipUI.DrawText((new StringBuilder()).append(CookedString).append(" ").append(name).toString(), 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
        else
        if(IsCookable)
            tooltipUI.DrawText((new StringBuilder()).append(UnCookedString).append(" ").append(name).toString(), 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
        else
            tooltipUI.DrawText(name, 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
        y += 20;
        mid = 100;
        if(type.equals("Tissue"))
        {
            tooltipUI.DrawText("Equip to mute sneezes and coughs", 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
            y += 20;
            tooltipUI.setWidth(200);
        }
        tooltipUI.DrawText("Weight:", 5, y, 1.0F, 1.0F, 0.8F, 1.0F);
        Float weight = Float.valueOf(ActualWeight * (float)uses);
        tooltipUI.DrawValueRightNoPlus(weight.floatValue(), mid, y);
        y += 14;
        y += 19;
        tooltipUI.setHeight(y);
    }

    public void SetContainerPosition(int x, int y)
    {
        containerX = x;
        containerY = y;
    }

    public void Use()
    {
        Use(false);
    }

    public void Use(boolean bCrafting)
    {
        Use(bCrafting, false);
    }

    public void Use(boolean bCrafting, boolean bInContainer)
    {
        if(!DisappearOnUse && !bCrafting)
            return;
        uses--;
        if(replaceOnUse != null && !bInContainer && !bCrafting)
        {
            String s = replaceOnUse;
            if(!replaceOnUse.contains("."))
                s = (new StringBuilder()).append(module).append(".").append(s).toString();
            container.AddItem(s);
            container.dirty = true;
        }
        if(uses <= 0 && container != null)
        {
            container.Items.remove(this);
            container.dirty = true;
            if(this == UIManager.getDragInventory())
                UIManager.setDragInventory(null);
        }
    }

    public void Use(IsoGameCharacter useOn)
    {
        boolean bNeeded = false;
        boolean bUse = false;
        if(AlwaysWelcomeGift)
            bNeeded = true;
        if("Pills".equals(type))
            bUse = true;
        if("Pillow".equals(type))
        {
            bUse = false;
            if(useOn != TutorialManager.instance.wife);
        }
        if(CanBandage)
        {
            Iterator i$ = useOn.getWounds().iterator();
            do
            {
                if(!i$.hasNext())
                    break;
                zombie.characters.IsoGameCharacter.Wound wound = (zombie.characters.IsoGameCharacter.Wound)i$.next();
                if(!wound.bandaged)
                {
                    wound.bandaged = true;
                    bUse = true;
                }
            } while(true);
            bUse = useOn.getBodyDamage().UseBandageOnMostNeededPart();
            if(bUse)
            {
                if(useOn instanceof IsoSurvivor)
                    ((IsoSurvivor)useOn).PatchedUpBy(IsoPlayer.getInstance());
                bNeeded = true;
            }
        }
        if(this instanceof Food)
            bNeeded = true;
        if(this instanceof HandWeapon)
            bNeeded = true;
        if("Belt".equals(type))
        {
            Iterator i$ = useOn.getWounds().iterator();
            do
            {
                if(!i$.hasNext())
                    break;
                zombie.characters.IsoGameCharacter.Wound wound = (zombie.characters.IsoGameCharacter.Wound)i$.next();
                if(!wound.tourniquet)
                {
                    wound.tourniquet = true;
                    bUse = true;
                    wound.bleeding -= 0.5F;
                }
            } while(true);
        }
        if(bUse)
        {
            Use();
            useOn.getUsedItemsOn().add(type);
        } else
        if((useOn instanceof IsoSurvivor) && useOn != TutorialManager.instance.wife)
        {
            useOn.getInventory().AddItem(this, 1);
            Use(true, true);
        }
        if(!useOn.getScriptName().equals("none"))
            ScriptManager.instance.Trigger("OnUseItemOnCharacter", useOn.getScriptName(), type);
        else
        if(useOn instanceof IsoSurvivor)
        {
            if(uses == 1)
            {
                IsoPlayer.instance.getInventory().Remove(this);
                ((IsoSurvivor)useOn).getInventory().AddItem(this);
            } else
            {
                Use(true);
                ((IsoSurvivor)useOn).getInventory().AddItem(getFullType());
            }
            ((IsoSurvivor)useOn).GivenItemBy(IsoPlayer.getInstance(), type, bNeeded);
        }
    }

    public void update()
    {
    }

    public boolean finishupdate()
    {
        return true;
    }

    public String getFullType()
    {
        return (new StringBuilder()).append(module).append(".").append(type).toString();
    }

    public void save(DataOutputStream output)
        throws IOException
    {
        GameWindow.WriteString(output, getFullType());
        output.writeInt(uses);
    }

    public void load(DataInputStream input)
        throws IOException
    {
        uses = input.readInt();
    }

    public void save(ByteBuffer output)
        throws IOException
    {
        GameWindow.WriteString(output, getFullType());
        output.putInt(uses);
        if(table != null)
        {
            output.put((byte)1);
            table.save(output);
        } else
        {
            output.put((byte)0);
        }
    }

    public void load(ByteBuffer input)
        throws IOException
    {
        uses = input.getInt();
        if(input.get() == 1)
        {
            if(table == null)
                table = LuaManager.platform.newTable();
            table.load(input);
        }
    }

    public boolean IsFood()
    {
        return this instanceof Food;
    }

    public boolean IsWeapon()
    {
        return this instanceof HandWeapon;
    }

    public boolean IsDrainable()
    {
        return this instanceof DrainableComboItem;
    }

    public boolean IsLiterature()
    {
        return this instanceof Literature;
    }

    public boolean IsClothing()
    {
        return this instanceof Clothing;
    }

    static InventoryItem LoadFromFile(DataInputStream input)
        throws IOException
    {
        String type = GameWindow.ReadString(input);
        return null;
    }

    public float getScore(SurvivorDesc desc)
    {
        return 0.0F;
    }

    public IsoGameCharacter getPreviousOwner()
    {
        return previousOwner;
    }

    public void setPreviousOwner(IsoGameCharacter previousOwner)
    {
        this.previousOwner = previousOwner;
    }

    public Item getScriptItem()
    {
        return ScriptItem;
    }

    public void setScriptItem(Item ScriptItem)
    {
        this.ScriptItem = ScriptItem;
    }

    public ItemType getCat()
    {
        return cat;
    }

    public void setCat(ItemType cat)
    {
        this.cat = cat;
    }

    public String getConsumeMenu()
    {
        return consumeMenu;
    }

    public void setConsumeMenu(String consumeMenu)
    {
        this.consumeMenu = consumeMenu;
    }

    public ItemContainer getContainer()
    {
        return container;
    }

    public void setContainer(ItemContainer container)
    {
        this.container = container;
    }

    public int getContainerX()
    {
        return containerX;
    }

    public void setContainerX(int containerX)
    {
        this.containerX = containerX;
    }

    public int getContainerY()
    {
        return containerY;
    }

    public void setContainerY(int containerY)
    {
        this.containerY = containerY;
    }

    public boolean isDisappearOnUse()
    {
        return DisappearOnUse;
    }

    public void setDisappearOnUse(boolean DisappearOnUse)
    {
        this.DisappearOnUse = DisappearOnUse;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getReplaceOnUse()
    {
        return replaceOnUse;
    }

    public void setReplaceOnUse(String replaceOnUse)
    {
        this.replaceOnUse = replaceOnUse;
    }

    public int getConditionMax()
    {
        return ConditionMax;
    }

    public void setConditionMax(int ConditionMax)
    {
        this.ConditionMax = ConditionMax;
    }

    public ItemContainer getRightClickContainer()
    {
        return rightClickContainer;
    }

    public void setRightClickContainer(ItemContainer rightClickContainer)
    {
        this.rightClickContainer = rightClickContainer;
    }

    public String getSwingAnim()
    {
        return swingAnim;
    }

    public void setSwingAnim(String swingAnim)
    {
        this.swingAnim = swingAnim;
    }

    public Texture getTexture()
    {
        return texture;
    }

    public void setTexture(Texture texture)
    {
        this.texture = texture;
    }

    public Texture getTexturerotten()
    {
        return texturerotten;
    }

    public void setTexturerotten(Texture texturerotten)
    {
        this.texturerotten = texturerotten;
    }

    public Texture getTextureCooked()
    {
        return textureCooked;
    }

    public void setTextureCooked(Texture textureCooked)
    {
        this.textureCooked = textureCooked;
    }

    public Texture getTextureBurnt()
    {
        return textureBurnt;
    }

    public void setTextureBurnt(Texture textureBurnt)
    {
        this.textureBurnt = textureBurnt;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public int getUses()
    {
        return uses;
    }

    public void setUses(int uses)
    {
        this.uses = uses;
    }

    public float getAge()
    {
        return Age;
    }

    public void setAge(float Age)
    {
        this.Age = Age;
    }

    public boolean isIsCookable()
    {
        return IsCookable;
    }

    public void setIsCookable(boolean IsCookable)
    {
        this.IsCookable = IsCookable;
    }

    public float getCookingTime()
    {
        return CookingTime;
    }

    public void setCookingTime(float CookingTime)
    {
        this.CookingTime = CookingTime;
    }

    public float getMinutesToCook()
    {
        return MinutesToCook;
    }

    public void setMinutesToCook(float MinutesToCook)
    {
        this.MinutesToCook = MinutesToCook;
    }

    public float getMinutesToBurn()
    {
        return MinutesToBurn;
    }

    public void setMinutesToBurn(float MinutesToBurn)
    {
        this.MinutesToBurn = MinutesToBurn;
    }

    public boolean isCooked()
    {
        return Cooked;
    }

    public void setCooked(boolean Cooked)
    {
        this.Cooked = Cooked;
    }

    public boolean isBurnt()
    {
        return Burnt;
    }

    public void setBurnt(boolean Burnt)
    {
        this.Burnt = Burnt;
    }

    public int getOffAge()
    {
        return OffAge;
    }

    public void setOffAge(int OffAge)
    {
        this.OffAge = OffAge;
    }

    public int getOffAgeMax()
    {
        return OffAgeMax;
    }

    public void setOffAgeMax(int OffAgeMax)
    {
        this.OffAgeMax = OffAgeMax;
    }

    public float getWeight()
    {
        return Weight;
    }

    public void setWeight(float Weight)
    {
        this.Weight = Weight;
    }

    public float getActualWeight()
    {
        return ActualWeight;
    }

    public void setActualWeight(float ActualWeight)
    {
        this.ActualWeight = ActualWeight;
    }

    public String getWorldTexture()
    {
        return WorldTexture;
    }

    public void setWorldTexture(String WorldTexture)
    {
        this.WorldTexture = WorldTexture;
    }

    public String getDescription()
    {
        return Description;
    }

    public void setDescription(String Description)
    {
        this.Description = Description;
    }

    public int getCondition()
    {
        return Condition;
    }

    public void setCondition(int Condition)
    {
        this.Condition = Condition;
    }

    public String getOffString()
    {
        return OffString;
    }

    public void setOffString(String OffString)
    {
        this.OffString = OffString;
    }

    public String getCookedString()
    {
        return CookedString;
    }

    public void setCookedString(String CookedString)
    {
        this.CookedString = CookedString;
    }

    public String getUnCookedString()
    {
        return UnCookedString;
    }

    public void setUnCookedString(String UnCookedString)
    {
        this.UnCookedString = UnCookedString;
    }

    public String getBurntString()
    {
        return BurntString;
    }

    public void setBurntString(String BurntString)
    {
        this.BurntString = BurntString;
    }

    public String getModule()
    {
        return module;
    }

    public void setModule(String module)
    {
        this.module = module;
    }

    public boolean isAlwaysWelcomeGift()
    {
        return AlwaysWelcomeGift;
    }

    public void setAlwaysWelcomeGift(boolean AlwaysWelcomeGift)
    {
        this.AlwaysWelcomeGift = AlwaysWelcomeGift;
    }

    public boolean isCanBandage()
    {
        return CanBandage;
    }

    public void setCanBandage(boolean CanBandage)
    {
        this.CanBandage = CanBandage;
    }

    public float getBoredomChange()
    {
        return boredomChange;
    }

    public void setBoredomChange(float boredomChange)
    {
        this.boredomChange = boredomChange;
    }

    public float getUnhappyChange()
    {
        return unhappyChange;
    }

    public void setUnhappyChange(float unhappyChange)
    {
        this.unhappyChange = unhappyChange;
    }

    public float getStressChange()
    {
        return stressChange;
    }

    public void setStressChange(float stressChange)
    {
        this.stressChange = stressChange;
    }

    public NulledArrayList getTaken()
    {
        return Taken;
    }

    public void setTaken(NulledArrayList Taken)
    {
        this.Taken = Taken;
    }

    public IsoDirections getPlaceDir()
    {
        return placeDir;
    }

    public void setPlaceDir(IsoDirections placeDir)
    {
        this.placeDir = placeDir;
    }

    public IsoDirections getNewPlaceDir()
    {
        return newPlaceDir;
    }

    public void setNewPlaceDir(IsoDirections newPlaceDir)
    {
        this.newPlaceDir = newPlaceDir;
    }

    public void setReplaceOnUseOn(String ReplaceOnUseOn)
    {
        this.ReplaceOnUseOn = ReplaceOnUseOn;
    }

    public String getReplaceOnUseOn()
    {
        return ReplaceOnUseOn;
    }

    public void setIsWaterSource(boolean IsWaterSource)
    {
        this.IsWaterSource = IsWaterSource;
    }

    public boolean isWaterSource()
    {
        return IsWaterSource;
    }

    boolean CanStackNoTemp(InventoryItem item)
    {
        return CanStack(item);
    }

    public void CopyModData(KahluaTable DefaultModData)
    {
        if(DefaultModData == null)
            return;
        KahluaTableIterator it = DefaultModData.iterator();
        KahluaTable n = getModData();
        for(; it.advance(); n.rawset(it.getKey(), it.getValue()));
    }

  
}
