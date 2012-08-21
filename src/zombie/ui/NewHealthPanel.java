// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NewHealthPanel.java

package zombie.ui;

import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.BodyDamage.BodyPartType;
import zombie.characters.IsoGameCharacter;
import zombie.core.Core;
import zombie.core.textures.Texture;

// Referenced classes of package zombie.ui:
//            NewWindow, UI_BodyPart, Sidebar, HUDButton

public class NewHealthPanel extends NewWindow
{

    public void SetCharacter(IsoGameCharacter chr)
    {
        ParentChar = chr;
    }

    public NewHealthPanel(int x, int y, IsoGameCharacter ParentCharacter)
    {
        super(x, y, 10, 10, true);
        MousedOverPart = null;
        ParentChar = ParentCharacter;
        ResizeToFitY = false;
        visible = false;
        instance = this;
        BodyOutline = Texture.getSharedTexture("media/ui/BodyDamage/BodyDamage_Background.png");
        HealthIcon = Texture.getSharedTexture("media/ui/Heart_On.png");
        HealthBarBack = Texture.getSharedTexture("media/ui/BodyDamage/DamageBar_Vert.png");
        HealthBar = Texture.getSharedTexture("media/ui/BodyDamage/DamageBar_Vert_Fill.png");
        width = 300;
        height = 210 + titleRight.getHeight() + 5;
        int cx = 6;
        int px = 37;
        Hand_L = new UI_BodyPart(BodyPartType.Hand_L, 6, 23, "media/ui/BodyDamage/BodyDamage_RHand.png", ParentChar, true);
        Hand_R = new UI_BodyPart(BodyPartType.Hand_R, 5, 23, "media/ui/BodyDamage/BodyDamage_RHand.png", ParentChar, false);
        ForeArm_L = new UI_BodyPart(BodyPartType.ForeArm_L, 6, 23, "media/ui/BodyDamage/BodyDamage_RLArm.png", ParentChar, true);
        ForeArm_R = new UI_BodyPart(BodyPartType.ForeArm_R, 5, 23, "media/ui/BodyDamage/BodyDamage_RLArm.png", ParentChar, false);
        UpperArm_L = new UI_BodyPart(BodyPartType.UpperArm_L, 6, 23, "media/ui/BodyDamage/BodyDamage_RUArm.png", ParentChar, true);
        UpperArm_R = new UI_BodyPart(BodyPartType.UpperArm_R, 5, 23, "media/ui/BodyDamage/BodyDamage_RUArm.png", ParentChar, false);
        Torso_Upper = new UI_BodyPart(BodyPartType.Torso_Upper, 5, 23, "media/ui/BodyDamage/BodyDamage_UBody.png", ParentChar, false);
        Torso_Lower = new UI_BodyPart(BodyPartType.Torso_Lower, 5, 23, "media/ui/BodyDamage/BodyDamage_LBody.png", ParentChar, false);
        Head = new UI_BodyPart(BodyPartType.Head, 5, 23, "media/ui/BodyDamage/BodyDamage_Head.png", ParentChar, false);
        Neck = new UI_BodyPart(BodyPartType.Neck, 5, 23, "media/ui/BodyDamage/BodyDamage_Neck.png", ParentChar, false);
        Groin = new UI_BodyPart(BodyPartType.Groin, 5, 23, "media/ui/BodyDamage/BodyDamage_Groin.png", ParentChar, false);
        UpperLeg_L = new UI_BodyPart(BodyPartType.UpperLeg_L, 6, 23, "media/ui/BodyDamage/BodyDamage_RULeg.png", ParentChar, true);
        UpperLeg_R = new UI_BodyPart(BodyPartType.UpperLeg_R, 5, 23, "media/ui/BodyDamage/BodyDamage_RULeg.png", ParentChar, false);
        LowerLeg_L = new UI_BodyPart(BodyPartType.LowerLeg_L, 6, 23, "media/ui/BodyDamage/BodyDamage_RLLeg.png", ParentChar, true);
        LowerLeg_R = new UI_BodyPart(BodyPartType.LowerLeg_R, 5, 23, "media/ui/BodyDamage/BodyDamage_RLLeg.png", ParentChar, false);
        Foot_L = new UI_BodyPart(BodyPartType.Foot_L, 6, 23, "media/ui/BodyDamage/BodyDamage_RFoot.png", ParentChar, true);
        Foot_R = new UI_BodyPart(BodyPartType.Foot_R, 5, 23, "media/ui/BodyDamage/BodyDamage_RFoot.png", ParentChar, false);
        AddChild(Hand_L);
        AddChild(Hand_R);
        AddChild(ForeArm_L);
        AddChild(ForeArm_R);
        AddChild(UpperArm_L);
        AddChild(UpperArm_R);
        AddChild(Torso_Upper);
        AddChild(Torso_Lower);
        AddChild(Head);
        AddChild(Neck);
        AddChild(Groin);
        AddChild(UpperLeg_L);
        AddChild(UpperLeg_R);
        AddChild(LowerLeg_L);
        AddChild(LowerLeg_R);
        AddChild(Foot_L);
        AddChild(Foot_R);
    }

    public void render()
    {
        if(!isVisible())
            return;
        super.render();
        DrawTexture(BodyOutline, 5, titleRight.getHeight() - 5, alpha);
        Hand_L.render();
        Hand_R.render();
        ForeArm_L.render();
        ForeArm_R.render();
        UpperArm_L.render();
        UpperArm_R.render();
        Torso_Upper.render();
        Torso_Lower.render();
        Head.render();
        Neck.render();
        Groin.render();
        UpperLeg_L.render();
        UpperLeg_R.render();
        LowerLeg_L.render();
        LowerLeg_R.render();
        Foot_L.render();
        Foot_R.render();
        float HealthBarLength = (100F - ParentChar.getBodyDamage().getHealth()) * 1.7F;
        DrawTexture(HealthIcon, 96, 200, alpha);
        DrawTextureScaled(HealthBarBack, 100, 25, 18, 172, alpha);
        DrawTextureScaled(HealthBar, 100, 26 + (int)HealthBarLength, 18, 170 - (int)HealthBarLength, alpha);
        DrawTextCentre("Body Damage Status", 150, 2, 1.0F, 1.0F, 1.0F, 1.0F);
        DrawText("Overall Body Status :", 125, 29, 1.0F, 1.0F, 1.0F, 1.0F);
        float InjuryRedTextTint = (100F - ParentChar.getBodyDamage().getHealth()) * 0.01F;
        if(InjuryRedTextTint < 0.2F)
            InjuryRedTextTint = 0.2F;
        DrawText(getDamageStatusString(), 125, 43, 1.0F, 1.0F - InjuryRedTextTint, 1.0F - InjuryRedTextTint, 1.0F);
        boolean MousingOverBodyPart = false;
        if(Hand_L.IsMouseOver() || Hand_R.IsMouseOver() || ForeArm_L.IsMouseOver() || ForeArm_R.IsMouseOver() || UpperArm_L.IsMouseOver() || UpperArm_R.IsMouseOver() || Torso_Upper.IsMouseOver() || Torso_Lower.IsMouseOver() || Head.IsMouseOver() || Neck.IsMouseOver() || Groin.IsMouseOver() || UpperLeg_L.IsMouseOver() || UpperLeg_R.IsMouseOver() || LowerLeg_L.IsMouseOver() || LowerLeg_R.IsMouseOver() || Foot_L.IsMouseOver() || Foot_R.IsMouseOver())
            MousingOverBodyPart = true;
        if(!MousingOverBodyPart)
        {
            DrawText("Tips:", 125, 80, 0.9F, 0.9F, 0.9F, 1.0F);
            DrawText("* Flashing indicates damage &", 125, 105, 0.6F, 0.6F, 0.6F, 1.0F);
            DrawText("the rate indicates severity.", 125, 120, 0.6F, 0.6F, 0.6F, 1.0F);
            DrawText("* Mouse-over body parts to see", 125, 148, 0.6F, 0.6F, 0.6F, 1.0F);
            DrawText("more detailed information.", 125, 163, 0.6F, 0.6F, 0.6F, 1.0F);
        }
    }

    public void update()
    {
        if(!isVisible())
            return;
        super.update();
        float absY = getAbsoluteY();
        Sidebar _tmp = Sidebar.instance;
        float dif = absY - (Sidebar.Heart.getY() - 70F);
        float val = (float)Core.getInstance().getOffscreenHeight() - absY;
        if(val > 0.0F)
            dif /= val;
        else
            dif = 1.0F;
        dif *= 4F;
        dif = 1.0F - dif;
        if(dif < 0.0F)
            dif = 0.0F;
    }

    private String getDamageStatusString()
    {
        if(!ParentChar.getBodyDamage().IsFakeInfected() && ParentChar.getBodyDamage().getInfectionLevel() == 100F)
            return "ZOMBIFIED!";
        if(ParentChar.getBodyDamage().getHealth() == 100F)
            return "OK";
        if(ParentChar.getBodyDamage().getHealth() > 90F)
            return "Slight damage";
        if(ParentChar.getBodyDamage().getHealth() > 80F)
            return "Very Minor damage";
        if(ParentChar.getBodyDamage().getHealth() > 70F)
            return "Minor damage";
        if(ParentChar.getBodyDamage().getHealth() > 60F)
            return "Moderate damage";
        if(ParentChar.getBodyDamage().getHealth() > 50F)
            return "Severe damage";
        if(ParentChar.getBodyDamage().getHealth() > 40F)
            return "Very Severe damage";
        if(ParentChar.getBodyDamage().getHealth() > 20F)
            return "Crital damage";
        if(ParentChar.getBodyDamage().getHealth() > 10F)
            return "Highly Crital damage";
        if(ParentChar.getBodyDamage().getHealth() > 0.0F)
            return "Terminal damage";
        else
            return "Deceased";
    }

    public static NewHealthPanel instance;
    public Texture BodyOutline;
    public UI_BodyPart Foot_L;
    public UI_BodyPart Foot_R;
    public UI_BodyPart ForeArm_L;
    public UI_BodyPart ForeArm_R;
    public UI_BodyPart Groin;
    public UI_BodyPart Hand_L;
    public UI_BodyPart Hand_R;
    public UI_BodyPart Head;
    public Texture HealthBar;
    public Texture HealthBarBack;
    public Texture HealthIcon;
    public UI_BodyPart LowerLeg_L;
    public UI_BodyPart LowerLeg_R;
    public UI_BodyPart Neck;
    public UI_BodyPart Torso_Lower;
    public UI_BodyPart Torso_Upper;
    public UI_BodyPart UpperArm_L;
    public UI_BodyPart UpperArm_R;
    public UI_BodyPart UpperLeg_L;
    public UI_BodyPart UpperLeg_R;
    public UI_BodyPart MousedOverPart;
    IsoGameCharacter ParentChar;
}
