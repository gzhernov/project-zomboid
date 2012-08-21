// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TutorialManager.java

package zombie.ui;

import zombie.FrameLoader;
import zombie.GameTime;
import zombie.Quests.*;
import zombie.characters.*;
import zombie.core.Collections.NulledArrayList;
import zombie.iso.*;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.objects.IsoRadio;
import zombie.iso.objects.IsoStove;
import zombie.iso.sprite.IsoAnim;
import zombie.iso.sprite.IsoSprite;
import zombie.scripting.ScriptManager;

// Referenced classes of package zombie.ui:
//            UIManager, TutorialPanel

public class TutorialManager
{
	/*     */   public static enum Stage
	/*     */   {
	/*  82 */     getBelt, 
	/*  83 */     RipSheet, 
	/*  84 */     Apply, 
	/*  85 */     FindShed, 
	/*  86 */     getShedItems, 
	/*  87 */     EquipHammer, 
	/*  88 */     BoardUpHouse, 
	/*  89 */     FindFood, 
	/*  90 */     InHouseFood, 
	/*  91 */     KillZombie, 
	/*  92 */     StockUp, 
	/*  93 */     ExploreHouse, 
	/*  94 */     BreakBarricade, 
	/*  95 */     getSoupIngredients, 
	/*  96 */     MakeSoupPot, 
	/*  97 */     LightStove, 
	/*  98 */     Distraction, 
	/*  99 */     InvestigateSound, 
	/* 100 */     Alarm, 
	/* 101 */     Mouseover, 
	/* 102 */     Escape, 
	/* 103 */     ShouldBeOk;
	/*     */   }


    public TutorialManager()
    {
        Active = true;
        ActiveControlZombies = false;
        TargetZombies = 0.0F;
        stage = Stage.getBelt;
        wife = null;
        DoorsLocked = true;
        BarricadeCount = 0;
        PrefMusic = null;
        StealControl = false;
        AlarmTime = 0;
        ProfanityFilter = false;
        Timer = 0;
        AlarmTickTime = 160;
        DoneFirstSleep = false;
        wifeKilledByEarl = false;
        warnedHammer = false;
        TriggerFire = false;
        CanDragWife = false;
        AllowSleep = false;
        skipped = false;
        bDoneDeath = false;
        bDoGunnutDeadTalk = true;
        millingTune = "tune1.ogg";
        radio = null;
        SpottedGunNut = false;
        bDoneWifeDeath = false;
        bDoneWifeDeathMourn = false;
    }

    public boolean AllowUse(IsoObject Object)
    {
        return true;
    }

    public void CheckWake()
    {
    }

    public void CreateQuests()
    {
        for(int n = 0; n < IsoWorld.instance.CurrentCell.getStaticUpdaterObjectList().size(); n++)
        {
            IsoObject obj = (IsoObject)IsoWorld.instance.CurrentCell.getStaticUpdaterObjectList().get(n);
            if(obj instanceof IsoRadio)
                radio = (IsoRadio)obj;
        }

    }

    public void init()
    {
        if(FrameLoader.bServer)
            return;
        if(!Active)
            return;
        else
            return;
    }

    public void TutorialMessage(String a, String b)
    {
        if(a == null && b == null)
            UIManager.getTutorial().hide();
        else
            UIManager.getTutorial().ShowMessage(a, b);
    }

    public void update()
    {
        if(wife == null)
            wife = (IsoSurvivor)ScriptManager.instance.getCharacterActual("KateBaldspotCharacters.Kate");
        if(gunnut == null)
            gunnut = (IsoSurvivor)ScriptManager.instance.getCharacterActual("KateBaldspotCharacters.Raider");
        if(IsoPlayer.getInstance().getCurrentSquare() == null);
        if(IsoPlayer.getInstance().getCurrentSquare() == null)
            return;
        if(wife != null)
        {
            String val = ScriptManager.instance.getFlagValue("FireTutorial.CanDragKate");
            wife.Draggable = val.equals("yes");
        }
        if(Math.abs(IsoPlayer.getInstance().getX() - 43F) < 1.0F && Math.abs(IsoPlayer.getInstance().getY() - 22F) < 2.0F && IsoPlayer.getInstance().getZ() == 1.0F)
            UIManager.AddTutorial(43F, 22F, 1.0F, "Opening Containers", "You can search inside the wardrobe by clicking it when stood next to it.<br><br>Click items to pick them up.<br><br>You can then click on your inventory icon to the left to place it in your bag.", false, 0.0F);
        Quest quest = QuestManager.instance.FindQuest("TendWifeQuest");
        if(wife != null && quest != null && quest.FindTask("BandageLeg").Complete && wife.sprite.CurrentAnim.name.equals("asleep_bleeding"))
            wife.sprite.PlayAnim("asleep_bandaged");
        String wifeTune = "tune2.ogg";
        if(wife != null && wife.getHealth() <= 0.0F)
            wifeTune = "tune8.ogg";
        String zombieTune = "tune4.ogg";
        millingTune = GameTime.instance.millingtune;
        zombieTune = GameTime.instance.zombieTune;
        if(!Active)
        {
            return;
        } else
        {
            Timer++;
            return;
        }
    }

    private void ForceKillZombies()
    {
        IsoWorld.instance.ForceKillAllZombies();
    }

    public static boolean Debug = false;
    public boolean Active;
    public boolean ActiveControlZombies;
    public float TargetZombies;
    public Stage stage;
    public IsoSurvivor wife;
    private IsoZombie zombie;
    public IsoStove tutorialStove;
    public IsoBuilding tutBuilding;
    public boolean DoorsLocked;
    public int BarricadeCount;
    public String PrefMusic;
    public IsoSurvivor gunnut;
    public boolean StealControl;
    public int AlarmTime;
    public boolean ProfanityFilter;
    public int Timer;
    public int AlarmTickTime;
    public boolean DoneFirstSleep;
    public boolean wifeKilledByEarl;
    public boolean warnedHammer;
    public boolean TriggerFire;
    public boolean CanDragWife;
    public boolean AllowSleep;
    public boolean skipped;
    private boolean bDoneDeath;
    boolean bDoGunnutDeadTalk;
    public String millingTune;
    IsoRadio radio;
    public boolean SpottedGunNut;
    boolean bDoneWifeDeath;
    boolean bDoneWifeDeathMourn;
    public static TutorialManager instance = new TutorialManager();

}
