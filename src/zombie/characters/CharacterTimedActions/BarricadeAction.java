// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BarricadeAction.java

package zombie.characters.CharacterTimedActions;

import zombie.SoundManager;
import zombie.WorldSoundManager;
import zombie.characters.IsoGameCharacter;
import zombie.characters.skills.PerkFactory;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.iso.IsoGridSquare;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoWindow;
import zombie.ui.TutorialManager;

// Referenced classes of package zombie.characters.CharacterTimedActions:
//            BaseAction

public class BarricadeAction extends BaseAction
{

    public BarricadeAction(IsoGameCharacter chr, IsoDoor door)
    {
        super(chr);
        this.door = null;
        window = null;
        this.door = door;
        MaxTime = 70;
        if(chr.HasTrait("Handy"))
            MaxTime = 35;
        MaxTime *= chr.getBarricadeTimeMod();
    }

    public BarricadeAction(IsoGameCharacter chr, IsoWindow window)
    {
        super(chr);
        door = null;
        this.window = null;
        this.window = window;
        MaxTime = 70;
        if(chr.HasTrait("Handy"))
            MaxTime = 35;
        MaxTime *= chr.getBarricadeTimeMod();
    }

    public boolean valid()
    {
        InventoryItem item = chr.getInventory().getBestCondition("Plank");
        return chr.getInventory().contains("Nails") && item != null && chr.hasEquipped("Hammer") && (window != null && window.Barricaded <= 3 || door != null && door.Barricaded <= 3);
    }

    public void start()
    {
        SoundEffect = SoundManager.instance.PlayWorldSound("hammernail", true, chr.getCurrentSquare(), 0.2F, 10F, 0.4F * chr.getHammerSoundMod(), true);
        int rad = 40;
        rad = (int)((float)rad * chr.getHammerSoundMod());
        if(door != null)
            WorldSoundManager.instance.addSound(door, door.square.getX(), door.square.getY(), door.square.getZ(), rad, rad);
        else
            WorldSoundManager.instance.addSound(window, window.square.getX(), window.square.getY(), window.square.getZ(), rad, rad);
        if(door != null && door.open)
            door.ToggleDoor(chr);
    }

    public void perform()
    {
        if(door != null)
        {
            InventoryItem item = chr.getInventory().getBestCondition("Plank");
            if(item != null)
            {
                door.Barricade(chr, item);
                TutorialManager.instance.BarricadeCount++;
                if(item.getUses() > 1)
                    item.setUses(item.getUses() - 1);
                else
                    chr.getInventory().Remove(item);
                chr.getInventory().RemoveOneOf("Nails");
                chr.getXp().AddXP(zombie.characters.skills.PerkFactory.Perks.Woodwork, 5);
            }
        }
        if(window != null)
        {
            InventoryItem item = chr.getInventory().getBestCondition("Plank");
            if(item != null)
            {
                window.Barricade(item);
                TutorialManager.instance.BarricadeCount++;
                if(item.getUses() > 1)
                    item.setUses(item.getUses() - 1);
                else
                    chr.getInventory().Remove(item);
                chr.getInventory().RemoveOneOf("Nails");
                chr.getXp().AddXP(zombie.characters.skills.PerkFactory.Perks.Woodwork, 5);
            }
        }
    }

    IsoDoor door;
    IsoWindow window;
}
