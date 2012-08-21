// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UnbarricadeAction.java

package zombie.characters.CharacterTimedActions;

import zombie.SoundManager;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.skills.PerkFactory;
import zombie.inventory.ItemContainer;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoWindow;

// Referenced classes of package zombie.characters.CharacterTimedActions:
//            BaseAction

public class UnbarricadeAction extends BaseAction
{

    public UnbarricadeAction(IsoGameCharacter chr, IsoDoor door)
    {
        super(chr);
        this.door = null;
        window = null;
        this.door = door;
        MaxTime = 90 * door.Barricaded;
        if(chr.HasTrait("Handy"))
            MaxTime *= 0.5D;
        MaxTime *= chr.getBarricadeTimeMod();
    }

    public UnbarricadeAction(IsoGameCharacter chr, IsoWindow window)
    {
        super(chr);
        door = null;
        this.window = null;
        this.window = window;
        MaxTime = 90 * window.Barricaded;
        if(chr.HasTrait("Handy"))
            MaxTime *= 0.5D;
        MaxTime *= chr.getBarricadeTimeMod();
    }

    public boolean valid()
    {
        return chr.getInventory().contains("Hammer") && (window != null && window.Barricaded > 0 || door != null && door.Barricaded > 0);
    }

    public void start()
    {
        SoundEffect = SoundManager.instance.PlayWorldSound("crackwood", true, IsoPlayer.getInstance().getCurrentSquare(), 0.2F, 10F, 0.4F, true);
    }

    public void perform()
    {
        if(door != null)
        {
            door.Unbarricade(chr);
            chr.getXp().AddXP(zombie.characters.skills.PerkFactory.Perks.Woodwork, 2);
            chr.getXp().AddXP(zombie.characters.skills.PerkFactory.Perks.Strength, 2);
        }
        if(window != null)
        {
            window.Unbarricade(chr);
            chr.getXp().AddXP(zombie.characters.skills.PerkFactory.Perks.Woodwork, 5);
            chr.getXp().AddXP(zombie.characters.skills.PerkFactory.Perks.Strength, 2);
        }
    }

    IsoDoor door;
    IsoWindow window;
}
