// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UI_BodyPart.java

package zombie.ui;

import zombie.Lua.LuaEventManager;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.BodyDamage.BodyPartType;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.textures.Mask;
import zombie.core.textures.Texture;
import zombie.input.Mouse;
import zombie.inventory.InventoryItem;
import zombie.inventory.types.Food;
import zombie.inventory.types.Literature;

// Referenced classes of package zombie.ui:
//            UIElement, NewHealthPanel, UIManager

public class UI_BodyPart extends UIElement
{

    public UI_BodyPart(BodyPartType type, int x, int y, String backgroundTex, IsoGameCharacter character, boolean RenderFlipped)
    {
        alpha = 1.0F;
        IsFlipped = false;
        MaxOscilatorRate = 0.58F;
        MinOscilatorRate = 0.025F;
        Oscilator = 0.0F;
        OscilatorRate = 0.02F;
        OscilatorStep = 0.0F;
        mouseOver = false;
        chr = character;
        BodyPartType = type;
        tex = Texture.getSharedTexture(backgroundTex);
        tex.createMask();
        this.x = x;
        this.y = y;
        width = tex.getWidth();
        height = tex.getHeight();
        IsFlipped = RenderFlipped;
        HealthIcon = Texture.getSharedTexture("media/ui/BodyDamage/HeartIcon_Small.png");
        HealthBarBack = Texture.getSharedTexture("media/ui/BodyDamage/DamageBar_Horz.png");
        HealthBar = Texture.getSharedTexture("media/ui/BodyDamage/DamageBar_Horz_Fill.png");
    }

    public boolean IsMouseOver()
    {
        return mouseOver;
    }

    public boolean onMouseDown(int x, int y)
    {
        if(NewHealthPanel.instance.MousedOverPart == this)
        {
            super.onMouseDown(x, y);
            if(UIManager.getDragInventory() != null)
            {
                LuaEventManager.TriggerEvent("OnUseItem", IsoPlayer.getInstance(), UIManager.getDragInventory());
                if("Pills".equals(UIManager.getDragInventory().getType()))
                {
                    if(this == NewHealthPanel.instance.Head)
                    {
                        UIManager.getDragInventory().Use();
                        IsoPlayer.getInstance().PainMeds(0.3F);
                    }
                } else
                if("PillsBeta".equals(UIManager.getDragInventory().getType()))
                {
                    if(this == NewHealthPanel.instance.Head)
                    {
                        UIManager.getDragInventory().Use();
                        IsoPlayer.getInstance().BetaBlockers(0.3F);
                    }
                } else
                if("PillsAntiDep".equals(UIManager.getDragInventory().getType()))
                {
                    if(this == NewHealthPanel.instance.Head)
                    {
                        UIManager.getDragInventory().Use();
                        IsoPlayer.getInstance().BetaAntiDepress(0.3F);
                    }
                } else
                if("PillsSleepingTablets".equals(UIManager.getDragInventory().getType()))
                {
                    if(this == NewHealthPanel.instance.Head)
                    {
                        UIManager.getDragInventory().Use();
                        IsoPlayer.getInstance().SleepingTablet(0.5F);
                    }
                } else
                if(UIManager.getDragInventory() instanceof Food)
                {
                    if(((Food)UIManager.getDragInventory()).isAlcoholic())
                    {
                        if(this == NewHealthPanel.instance.Head)
                        {
                            chr.Eat(UIManager.getDragInventory());
                            UIManager.getDragInventory().Use();
                            IsoPlayer.getInstance().SleepingTablet(0.02F);
                            IsoPlayer.getInstance().BetaAntiDepress(0.4F);
                            IsoPlayer.getInstance().BetaBlockers(0.2F);
                            IsoPlayer.getInstance().PainMeds(0.2F);
                            chr.getBodyDamage().JustDrankBooze();
                        }
                    } else
                    if(!"TinnedSoup".equals(UIManager.DragInventory.getType()) && this == NewHealthPanel.instance.Head)
                    {
                        Food TheFood = (Food)UIManager.getDragInventory();
                        chr.Eat(UIManager.getDragInventory());
                        chr.getBodyDamage().JustAteFood(TheFood);
                        UIManager.getDragInventory().Use();
                    }
                } else
                if(UIManager.getDragInventory() instanceof Literature)
                {
                    NewHealthPanel.instance.ParentChar.getBodyDamage().JustReadSomething((Literature)UIManager.getDragInventory());
                    IsoPlayer.instance.getStats().stress -= UIManager.getDragInventory().getStressChange() / 100F;
                    UIManager.getDragInventory().Use();
                } else
                if("RippedSheets".equals(UIManager.getDragInventory().getType()) && (chr.getBodyDamage().IsScratched(BodyPartType) || chr.getBodyDamage().IsBitten(BodyPartType) || chr.getBodyDamage().IsWounded(BodyPartType)))
                {
                    UIManager.getDragInventory().Use();
                    chr.getBodyDamage().SetBandaged(BodyPartType, true);
                }
            }
            return true;
        } else
        {
            return false;
        }
    }

    public boolean onMouseMove(int dx, int dy)
    {
        super.onMouseMove(dx, dy);
        if(!isVisible())
        {
            mouseOver = false;
            return false;
        }
        Mask mask = tex.getMask();
        if(mask == null)
        {
            NewHealthPanel.instance.MousedOverPart = this;
            mouseOver = true;
            return true;
        }
        int testX = Mouse.getXA() - getAbsoluteX();
        int testY = Mouse.getYA() - getAbsoluteY();
        if(IsFlipped)
            if(testX > tex.getWidth() / 2)
                testX -= (testX - tex.getWidth() / 2) * 2;
            else
                testX += (tex.getWidth() / 2 - testX) * 2;
        if(testX < 0 || testY < 0)
        {
            mouseOver = false;
            return false;
        }
        if(mask.mask.length < testX)
        {
            mouseOver = false;
            return false;
        }
        if(mask.mask[0].length < testY)
        {
            mouseOver = false;
            return false;
        }
        if(!mask.mask[testX][testY])
        {
            mouseOver = false;
            return false;
        } else
        {
            NewHealthPanel.instance.MousedOverPart = this;
            mouseOver = true;
            return true;
        }
    }

    public void onMouseMoveOutside(int dx, int dy)
    {
        mouseOver = false;
    }

    public void render()
    {
        if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) < 90F)
        {
            float DamageRatio = (100F - chr.getBodyDamage().getBodyPartHealth(BodyPartType)) * 0.01F;
            OscilatorRate = MinOscilatorRate + (MaxOscilatorRate - MinOscilatorRate) * DamageRatio;
            OscilatorStep += OscilatorRate;
            Oscilator = (float)(Math.sin(OscilatorStep) / 2D + 0.5D);
            alpha = Oscilator;
        } else
        {
            alpha = 0.0F;
        }
        if(tex != null)
            if(!IsFlipped)
                DrawTexture(tex, 0, 0, alpha);
            else
                DrawTextureFlipped(tex, 0, 0, alpha, true, false);
        if(mouseOver && NewHealthPanel.instance.MousedOverPart == this)
        {
            DrawText((new StringBuilder()).append(BodyPartType.getDisplayName(BodyPartType)).append(" :").toString(), 120, 40, 0.8F, 0.8F, 0.8F, 1.0F);
            float InjuryRedTextTint = (100F - chr.getBodyDamage().getBodyPartHealth(BodyPartType)) * 0.01F;
            if(InjuryRedTextTint < 0.2F)
                InjuryRedTextTint = 0.2F;
            DrawText(getPartStatusString(), 120, 55, 1.0F - (InjuryRedTextTint - 1.0F), 1.0F - InjuryRedTextTint, 1.0F - InjuryRedTextTint, 1.0F);
            float HealthBarLength = (100F - chr.getBodyDamage().getBodyPartHealth(BodyPartType)) * 1.5F;
            DrawTextureScaled(HealthIcon, 120, 72, 15, 14, 1.0F);
            DrawTextureScaled(HealthBarBack, 138, 72, 152, 14, 1.0F);
            DrawTextureScaled(HealthBar, 139, 73, 150 - (int)HealthBarLength, 12, 1.0F);
            if(chr.getBodyDamage().getHealth() > 0.0F && chr.getBodyDamage().getInfectionLevel() < 100F)
            {
                if(!chr.getBodyDamage().DoesBodyPartHaveInjury(BodyPartType))
                    if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) == 100F)
                    {
                        DrawTextCentre("No Attention Needed", 202, 134, 0.6F, 0.6F, 0.8F, 1.0F);
                    } else
                    {
                        DrawTextCentre("Currently Healing", 202, 126, 0.6F, 0.6F, 0.8F, 1.0F);
                        DrawTextCentre("(Food & Rest Required)", 202, 141, 0.6F, 0.6F, 0.8F, 1.0F);
                    }
                if(chr.getBodyDamage().IsBandaged(BodyPartType))
                    DrawTextCentre("Bandaged", 202, 105, 0.0F, 1.0F, 0.0F, 1.0F);
                if(chr.getBodyDamage().IsScratched(BodyPartType))
                    DrawTextCentre("Scratched", 202, 125, 1.0F, 0.0F, 0.0F, 1.0F);
                if(chr.getBodyDamage().IsBitten(BodyPartType))
                    DrawTextCentre("Bitten", 202, 145, 1.0F, 0.0F, 0.0F, 1.0F);
                if(chr.getBodyDamage().IsWounded(BodyPartType))
                    DrawTextCentre("Wounded", 202, 165, 1.0F, 0.0F, 0.0F, 1.0F);
                if(chr.getBodyDamage().IsBleeding(BodyPartType))
                    DrawTextCentre("Bleeding", 202, 185, 1.0F, 0.0F, 0.0F, 1.0F);
            }
        }
        super.render();
    }

    private String getPartStatusString()
    {
        if(chr.getBodyDamage().getInfectionLevel() == 100F)
            return "Slowly decaying";
        if(chr.getBodyDamage().getHealth() <= 0.0F)
        {
            if(chr.getBodyDamage().WasBurntToDeath())
            {
                if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) == 100F)
                    return "OK";
                if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 90F)
                    return "Smoldering";
                if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 80F)
                    return "Scorched";
                if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 70F)
                    return "Chared";
                if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 50F)
                    return "Fried";
                if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 0.0F)
                    return "Frazzled";
                else
                    return "Burnt To A Crisp";
            }
            if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) == 100F)
                return "OK";
            if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 90F)
                return "Gashed";
            if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 80F)
                return "Chewed";
            if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 70F)
                return "Gnawed";
            if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 50F)
                return "Torn";
            if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 0.0F)
                return "Partially Eaten";
            else
                return "Eaten";
        }
        if(chr.getBodyDamage().IsOnFire())
        {
            if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) == 100F)
                return "OK";
            if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 90F)
                return "Smoldering";
            if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 80F)
                return "Scorched";
            if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 70F)
                return "Chared";
            if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 50F)
                return "Fried";
            if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 0.0F)
                return "Frazzled";
            else
                return "Burnt To A Crisp";
        }
        if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) == 100F)
            return "OK";
        if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 90F)
            return "Slight Injuries";
        if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 80F)
            return "Very Minor Injuries";
        if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 70F)
            return "Minor Injuries";
        if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 60F)
            return "Moderate Injuries";
        if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 50F)
            return "Severe Injuries";
        if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 40F)
            return "Very Severe Injuries";
        if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 20F)
            return "Crital Injuries";
        if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 10F)
            return "Highly Crital Injuries";
        if(chr.getBodyDamage().getBodyPartHealth(BodyPartType) > 0.0F)
            return "Terminal Injuries";
        else
            return "Fatal Injuries";
    }

    public float alpha;
    public BodyPartType BodyPartType;
    public Texture HealthBar;
    public Texture HealthBarBack;
    public Texture HealthIcon;
    public boolean IsFlipped;
    public float MaxOscilatorRate;
    public float MinOscilatorRate;
    public float Oscilator;
    public float OscilatorRate;
    public float OscilatorStep;
    IsoGameCharacter chr;
    boolean mouseOver;
    Texture tex;
}
