// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CharacterCreationState.java

package zombie.gameStates;

import gnu.trove.map.hash.THashMap;
import java.util.*;
import zombie.GameWindow;
import zombie.characters.*;
import zombie.characters.professions.ProfessionFactory;
import zombie.characters.traits.TraitFactory;
import zombie.core.Collections.NulledArrayList;
import zombie.core.*;
import zombie.core.textures.Texture;
import zombie.interfaces.IListBoxItem;
import zombie.scripting.ScriptManager;
import zombie.ui.*;

// Referenced classes of package zombie.gameStates:
//            GameState, GameStateMachine

public class CharacterCreationState extends GameState
    implements UIEventHandler
{
    public static class SelectSlot
        implements IListBoxItem
    {

        public int getID()
        {
            return ID;
        }

        public String getLabel()
        {
            return name;
        }

        public String getLeftLabel()
        {
            return name;
        }

        public String getRightLabel()
        {
            return description;
        }

        public String getDescription()
        {
            return description;
        }

        public int ID;
        public int data;
        public String name;
        public String description;

        public SelectSlot(int id, int data, String name, String desc)
        {
            ID = id;
            this.name = name;
            description = desc;
        }
    }


    public CharacterCreationState()
    {
        Forename = "Geoff";
        Surname = "Capes";
        CharRotation = 0;
        CharRotationDelay = 0;
        CharRotationTimer = 3;
        AnimFrameTimer = 0;
        AnimFrameDelay = 12;
        FrameIndex = 0;
        NumAnimFrames = 4;
        Done = false;
        ProfessionSelected = false;
        ViewerAnimName = "Run";
        CurrentSkinTone = 0;
        NumSkinTones = 4;
        CurrentShirtColour = 0;
        NumShirtColours = 10;
        CurrentTrousersColour = 0;
        NumTrousersColours = 4;
        CurrentHead = 0;
        NumHeads = 2;
        PointsToSpend = 0;
        desc = new SurvivorDesc();
        UseCustomChatacterData = false;
    }

    public void ModalClick(String s, String s1)
    {
    }

    public void enter()
    {
        if(Core.bDoubleSize)
            Core.getInstance().doubleSizeToggle();
        TutorialManager.instance.StealControl = false;
        GameWindow.CharacterCreationStateHandle = this;
        CharacterCreationPanel = null;
        UseCustomChatacterData = true;
        Forename = SurvivorFactory.MaleForenames[Rand.Next(SurvivorFactory.MaleForenames.length)];
        Surname = SurvivorFactory.Surnames[Rand.Next(SurvivorFactory.Surnames.length)];
        Done = false;
        traitList = new ListBox("unpicked", this);
        pickedTraitList = new ListBox("picked", this);
        professionList = new ListBox("profession", this);
        GenderList = new ListBox("Gender", this);
        UIManager.initCharCreation();
        ScriptManager.instance.Trigger("OnPreCharacterCreation");
        GenderList.setX(30F);
        GenderList.setY(30F);
        GenderList.setWidth(290);
        GenderList.setHeight(136);
        GenderList.itemHeight = 68;
        GenderList.AddItem(new SelectSlot(0, 0, "Male", "Male"), Texture.getSharedTexture("media/ui/Male.png"), Color.white, new Color(67, 176, 28, 15), new Color(255, 255, 255, 15));
        GenderList.AddItem(new SelectSlot(1, 1, "Female", "Female"), Texture.getSharedTexture("media/ui/Female.png"), Color.white, new Color(67, 176, 28, 15), new Color(255, 255, 255, 15));
        GenderList.LastSelected = GenderList.Selected = 0;
        professionList.setX(30F);
        professionList.setY(211F);
        professionList.setWidth(290);
        professionList.setHeight(272);
        professionList.itemHeight = 68;
        ProffessionScrollBar = new ScrollBar("ProffessionScrollBar", this, (int)(professionList.getX() + (float)professionList.getWidth() + 4F), (int)professionList.getY(), professionList.getHeight(), true);
        ProffessionScrollBar.SetParentListBox(professionList);
        float y = professionList.getY() + (float)professionList.getHeight();
        traitList.setX(343F);
        traitList.setY(professionList.getY());
        traitList.setWidth(177);
        traitList.setHeight(professionList.getHeight());
        traitList.itemHeight = 19;
        TraitsScrollBar = new ScrollBar("TraitsScrollBar", this, (int)(traitList.getX() + (float)traitList.getWidth() + 4F), (int)traitList.getY(), traitList.getHeight(), true);
        TraitsScrollBar.SetParentListBox(traitList);
        pickedTraitList.setX(traitList.getX() + (float)traitList.getWidth() + 24F);
        pickedTraitList.setY(professionList.getY());
        pickedTraitList.setWidth(traitList.getWidth());
        pickedTraitList.setHeight(traitList.getHeight());
        pickedTraitList.itemHeight = 19;
        PickedTraitsScrollBar = new ScrollBar("PickedTraitsScrollBar", this, (int)(pickedTraitList.getX() + (float)pickedTraitList.getWidth() + 4F), (int)pickedTraitList.getY(), pickedTraitList.getHeight(), true);
        PickedTraitsScrollBar.SetParentListBox(pickedTraitList);
        button = new DialogButton(this, 722, 547, "Start", "Start");
        RotateAntiClockwise = new GenericButton(this, 373, 102, 11, 17, "RotateAntiClockwise", " ", Texture.getSharedTexture("media/ui/LeftArrow_Up.png"), Texture.getSharedTexture("media/ui/LeftArrow_Down.png"));
        RotateClockwise = new GenericButton(this, 479, 102, 11, 17, "RotateClockwise", " ", Texture.getSharedTexture("media/ui/RightArrow_Up.png"), Texture.getSharedTexture("media/ui/RightArrow_Down.png"));
        FaceCycleLeft = new GenericButton(this, 633, 130, 11, 17, "CycleFaceLeft", " ", Texture.getSharedTexture("media/ui/LeftArrow_Up.png"), Texture.getSharedTexture("media/ui/LeftArrow_Down.png"));
        FaceCycleRight = new GenericButton(this, 663, 130, 11, 17, "CycleFaceRight", " ", Texture.getSharedTexture("media/ui/RightArrow_Up.png"), Texture.getSharedTexture("media/ui/RightArrow_Down.png"));
        SkinToneLeft = new GenericButton(this, 633, 147, 11, 17, "SkinToneLeft", " ", Texture.getSharedTexture("media/ui/LeftArrow_Up.png"), Texture.getSharedTexture("media/ui/LeftArrow_Down.png"));
        SkinToneRight = new GenericButton(this, 663, 147, 11, 17, "SkinToneRight", " ", Texture.getSharedTexture("media/ui/RightArrow_Up.png"), Texture.getSharedTexture("media/ui/RightArrow_Down.png"));
        ShirtColourLeft = new GenericButton(this, 653, 151, 11, 17, "ShirtColourLeft", " ", Texture.getSharedTexture("media/ui/LeftArrow_Up.png"), Texture.getSharedTexture("media/ui/LeftArrow_Down.png"));
        ShirtColourRight = new GenericButton(this, 712, 151, 11, 17, "ShirtColourRight", " ", Texture.getSharedTexture("media/ui/RightArrow_Up.png"), Texture.getSharedTexture("media/ui/RightArrow_Down.png"));
        TrousersColourLeft = new GenericButton(this, 653, 168, 11, 17, "TrousersColourLeft", " ", Texture.getSharedTexture("media/ui/LeftArrow_Up.png"), Texture.getSharedTexture("media/ui/LeftArrow_Down.png"));
        TrousersColourRight = new GenericButton(this, 712, 168, 11, 17, "TrousersColourRight", " ", Texture.getSharedTexture("media/ui/RightArrow_Up.png"), Texture.getSharedTexture("media/ui/RightArrow_Down.png"));
        helpText = new TextBox(UIFont.Small, (int)professionList.getX(), (int)(pickedTraitList.getY() + (float)pickedTraitList.getHeight() + 10F), professionList.getWidth(), "");
        TraitAdviceText = new TextBox(UIFont.Small, (int)traitList.getX(), (int)(pickedTraitList.getY() + (float)pickedTraitList.getHeight() + 10F), traitList.getWidth(), "");
        TraitAdviceText.SetText("Choose negative character traits (red) to gain points to spend on positive character traits (green). Some pairs of traits are mutually exclusive.");
        CantStartText = new UITextBox2(UIFont.Small, (int)pickedTraitList.getX(), (int)(pickedTraitList.getY() + (float)pickedTraitList.getHeight() + 28F), traitList.getWidth(), 66, "", false);
        CantStartText.SetText("Can't start with negative character points. Pick more negative traits, or ditch some positives traits.");
        Iterator i$ = TraitFactory.TraitMap.values().iterator();
        do
        {
            if(!i$.hasNext())
                break;
            zombie.characters.traits.TraitFactory.Trait trait = (zombie.characters.traits.TraitFactory.Trait)i$.next();
            Color col = new Color(201, 19, 10);
            int cost = trait.cost;
            if(trait.cost > 0)
                col = new Color(67, 176, 28);
            else
            if(trait.cost == 0)
                col = new Color(0, 0, 0, 0);
            if(cost < 0)
                cost = -cost;
            if(!trait.prof)
                traitList.AddItem(trait, Color.white, col, new Color(255, 255, 255, 15));
        } while(true);
        zombie.characters.professions.ProfessionFactory.Profession prof;
        Color col;
        Texture IconTex;
        for(i$ = ProfessionFactory.ProfessionMap.values().iterator(); i$.hasNext(); professionList.AddItem(prof, IconTex, Color.white, col, new Color(255, 255, 255, 15)))
        {
            prof = (zombie.characters.professions.ProfessionFactory.Profession)i$.next();
            col = new Color(201, 19, 10);
            int cost = prof.getCost();
            if(prof.getCost() > 0)
                col = new Color(67, 176, 28);
            else
            if(prof.getCost() == 0)
                col = new Color(0, 0, 0, 0);
            if(cost < 0)
                cost = -cost;
            IconTex = Texture.getSharedTexture(prof.getIconPath());
        }

        CharacterViewPanel = new UIDialoguePanel((int)traitList.getX(), (int)GenderList.getY(), traitList.getWidth(), GenderList.getHeight() + 18);
        UIManager.getUI().clear();
        CharacterCreationPanel = new CharacterCreationPanel(Core.getInstance().getOffscreenWidth() / 2 - 375, Core.getInstance().getOffscreenHeight() / 2 - 285);
        CharacterCreationPanel.setVisible(true);
        UIManager.getUI().add(CharacterCreationPanel);
        ForenameBox = new UITextBox2(UIFont.Small, (int)pickedTraitList.getX() - 15, 46, 207, 24, "Quest Text", true);
        SurnameBox = new UITextBox2(UIFont.Small, (int)pickedTraitList.getX() - 15, 87, 207, 24, "Quest Text", true);
        ForenameBox.SetText(Forename);
        SurnameBox.SetText(Surname);
        ForenameBox.IsEditable = true;
        SurnameBox.IsEditable = true;
        ForenameBox.TextEntryMaxLength = 20;
        SurnameBox.TextEntryMaxLength = 20;
        InstructionText = new UITextBox2(UIFont.Small, 30, 29, 307, 156, "Select your character's profession.\n\nEach profession has unique free traits.\n\nThen select which additional traits your character has.\n\nChoosing negative character traits (red) gains you points to spend on positive character traits (green).\n\nSome pairs of traits are mutually exclusive.", true);
        int Diff = 86;
        CustomiseBox = new UINineGrid((int)pickedTraitList.getX() - 15, 29 + Diff, 207, 156 - Diff, 5, 5, 5, 5, "media/ui/Box_TopLeft.png", "media/ui/Box_Top.png", "media/ui/Box_TopRight.png", "media/ui/Box_Left.png", "media/ui/Box_Center.png", "media/ui/Box_Right.png", "media/ui/Box_BottomLeft.png", "media/ui/Box_Bottom.png", "media/ui/Box_BottomRight.png");
        CharacterCreationPanel.AddChild(CustomiseBox);
        CharacterCreationPanel.AddChild(ForenameBox);
        CharacterCreationPanel.AddChild(SurnameBox);
        CharacterCreationPanel.AddChild(traitList);
        CharacterCreationPanel.AddChild(pickedTraitList);
        CharacterCreationPanel.AddChild(professionList);
        CharacterCreationPanel.AddChild(helpText);
        CharacterCreationPanel.AddChild(TraitAdviceText);
        CharacterCreationPanel.AddChild(CantStartText);
        CharacterCreationPanel.AddChild(button);
        CharacterCreationPanel.AddChild(ProffessionScrollBar);
        CharacterCreationPanel.AddChild(TraitsScrollBar);
        CharacterCreationPanel.AddChild(PickedTraitsScrollBar);
        CharacterCreationPanel.AddChild(CharacterViewPanel);
        CharacterCreationPanel.AddChild(RotateClockwise);
        CharacterCreationPanel.AddChild(RotateAntiClockwise);
        CharacterCreationPanel.AddChild(FaceCycleLeft);
        CharacterCreationPanel.AddChild(FaceCycleRight);
        CharacterCreationPanel.AddChild(SkinToneLeft);
        CharacterCreationPanel.AddChild(SkinToneRight);
        CharacterCreationPanel.AddChild(InstructionText);
        PointsToSpend = 0;
        ProfessionSelected = false;
    }

    public void exit()
    {
    }

    public void render()
    {
        GenderList.LastSelected = GenderList.Selected = 0;
        Core.getInstance().StartFrame();
        Core.getInstance().EndFrame();
        Core.getInstance().StartFrameUI();
        UIManager.render();
        CharacterCreationPanel.DrawTextureScaledCol(Texture.getSharedTexture("media/white.png"), (int)pickedTraitList.getX(), (int)(pickedTraitList.getY() + (float)pickedTraitList.getHeight()) + 2, pickedTraitList.getWidth(), 20, new Color(255, 255, 255, 15));
        if(PointsToSpend >= 0)
        {
            CharacterCreationPanel.DrawTextRight((new StringBuilder()).append("Points to spend : ").append(PointsToSpend).toString(), (int)(pickedTraitList.getX() + (float)pickedTraitList.getWidth()) - 5, (int)(pickedTraitList.getY() + (float)pickedTraitList.getHeight()) + 6, 0.5F, 0.9F, 0.5F, 1.0F);
            CantStartText.setVisible(false);
            button.setVisible(true);
            if(!ProfessionSelected)
            {
                CantStartText.SetText("Can't start withithout choosing a character proffesion.");
                CantStartText.setVisible(true);
                button.setVisible(false);
            }
        } else
        {
            CharacterCreationPanel.DrawTextRight((new StringBuilder()).append("Points to spend : ").append(PointsToSpend).toString(), (int)(pickedTraitList.getX() + (float)pickedTraitList.getWidth()) - 5, (int)(pickedTraitList.getY() + (float)pickedTraitList.getHeight()) + 6, 0.9F, 0.5F, 0.5F, 1.0F);
            CantStartText.setVisible(true);
            button.setVisible(false);
            if(!ProfessionSelected)
                CantStartText.SetText("Can't start withithout choosing a character proffesion.");
            else
                CantStartText.SetText("Can't start with negative character points. Pick more negative traits, or ditch some positives traits.");
        }
        if(AnimFrameTimer > 0)
            AnimFrameTimer--;
        if(AnimFrameTimer <= 0)
        {
            AnimFrameTimer = AnimFrameDelay;
            FrameIndex++;
            if(FrameIndex == NumAnimFrames)
                FrameIndex = 0;
        }
        boolean FlipX = false;
        if(CharRotationDelay > 0)
            CharRotationDelay--;
        if(CharRotationDelay == 0)
        {
            CharRotationDelay = CharRotationTimer;
            if(RotateClockwise.clicked)
                CharRotation++;
            if(RotateAntiClockwise.clicked)
                CharRotation--;
            if(CharRotation > 7)
                CharRotation = 0;
            if(CharRotation < 0)
                CharRotation = 7;
        }
        Texture CharTex = null;
        int BodyScale = 2;
        int BodyPosX = (int)CharacterViewPanel.getX() + 26;
        int BodyPosY = (int)CharacterViewPanel.getY() - 40;
        FlipX = Flipped[CharRotation];
        String LegsTexName = (new StringBuilder()).append("media/Characters/Pieces/").append(ViewerAnimName).append("_Base_Legs_").append(DirectionValues[CharRotation]).append("_").append(FrameIndex).append("_Skin_0").append(CurrentSkinTone).append(".pcx").toString();
        CharTex = Texture.getSharedTexture(LegsTexName);
        if(CharTex != null)
            if(FlipX)
            {
                int FlippedOffset = CharTex.getWidthOrig() - (int)CharTex.offsetX - CharTex.getWidth();
                CharacterCreationPanel.DrawTexture_FlippedXIgnoreOffset(CharTex, BodyPosX + FlippedOffset * BodyScale, BodyPosY + (int)CharTex.offsetY * BodyScale, CharTex.getWidth() * BodyScale, CharTex.getHeight() * BodyScale, new Color(255, 255, 255, 255));
            } else
            {
                CharacterCreationPanel.DrawTextureIgnoreOffset(CharTex, BodyPosX + (int)CharTex.offsetX * BodyScale, BodyPosY + (int)CharTex.offsetY * BodyScale, CharTex.getWidth() * BodyScale, CharTex.getHeight() * BodyScale, new Color(255, 255, 255, 255));
            }
        String TorsoTexName = (new StringBuilder()).append("media/Characters/Pieces/").append(ViewerAnimName).append("_Base_Torso_").append(DirectionValues[CharRotation]).append("_").append(FrameIndex).append("_Skin_0").append(CurrentSkinTone).append(".pcx").toString();
        CharTex = Texture.getSharedTexture(TorsoTexName);
        if(CharTex != null)
            if(FlipX)
            {
                int FlippedOffset = CharTex.getWidthOrig() - (int)CharTex.offsetX - CharTex.getWidth();
                CharacterCreationPanel.DrawTexture_FlippedXIgnoreOffset(CharTex, BodyPosX + FlippedOffset * BodyScale, BodyPosY + (int)CharTex.offsetY * BodyScale, CharTex.getWidth() * BodyScale, CharTex.getHeight() * BodyScale, new Color(255, 255, 255, 255));
            } else
            {
                CharacterCreationPanel.DrawTextureIgnoreOffset(CharTex, BodyPosX + (int)CharTex.offsetX * BodyScale, BodyPosY + (int)CharTex.offsetY * BodyScale, CharTex.getWidth() * BodyScale, CharTex.getHeight() * BodyScale, new Color(255, 255, 255, 255));
            }
        if(CurrentShirtColour != -1)
        {
            String ShirtTexName = (new StringBuilder()).append("media/Characters/Pieces/").append(ViewerAnimName).append("_Shirt1_").append(DirectionValues[CharRotation]).append("_").append(FrameIndex).append("_Shirt_").append(ShirtColours[CurrentShirtColour]).append(".pcx").toString();
            CharTex = Texture.getSharedTexture(ShirtTexName);
            if(CharTex != null)
                if(FlipX)
                {
                    int FlippedOffset = CharTex.getWidthOrig() - (int)CharTex.offsetX - CharTex.getWidth();
                    CharacterCreationPanel.DrawTexture_FlippedXIgnoreOffset(CharTex, BodyPosX + FlippedOffset * BodyScale, BodyPosY + (int)CharTex.offsetY * BodyScale, CharTex.getWidth() * BodyScale, CharTex.getHeight() * BodyScale, new Color(255, 255, 255, 255));
                } else
                {
                    CharacterCreationPanel.DrawTextureIgnoreOffset(CharTex, BodyPosX + (int)CharTex.offsetX * BodyScale, BodyPosY + (int)CharTex.offsetY * BodyScale, CharTex.getWidth() * BodyScale, CharTex.getHeight() * BodyScale, new Color(255, 255, 255, 255));
                }
        }
        String TrousersTexName = (new StringBuilder()).append("media/Characters/Pieces/").append(ViewerAnimName).append("_Trousers1_").append(DirectionValues[CharRotation]).append("_").append(FrameIndex).append("_Trousers_").append(TrousersColours[CurrentTrousersColour]).append(".pcx").toString();
        CharTex = Texture.getSharedTexture(TrousersTexName);
        if(CharTex != null)
            if(FlipX)
            {
                int FlippedOffset = CharTex.getWidthOrig() - (int)CharTex.offsetX - CharTex.getWidth();
                CharacterCreationPanel.DrawTexture_FlippedXIgnoreOffset(CharTex, BodyPosX + FlippedOffset * BodyScale, BodyPosY + (int)CharTex.offsetY * BodyScale, CharTex.getWidth() * BodyScale, CharTex.getHeight() * BodyScale, new Color(255, 255, 255, 255));
            } else
            {
                CharacterCreationPanel.DrawTextureIgnoreOffset(CharTex, BodyPosX + (int)CharTex.offsetX * BodyScale, BodyPosY + (int)CharTex.offsetY * BodyScale, CharTex.getWidth() * BodyScale, CharTex.getHeight() * BodyScale, new Color(255, 255, 255, 255));
            }
        String ShoesTexName = (new StringBuilder()).append("media/Characters/Pieces/").append(ViewerAnimName).append("_Shoes1_").append(DirectionValues[CharRotation]).append("_").append(FrameIndex).append(".pcx").toString();
        CharTex = Texture.getSharedTexture(ShoesTexName);
        if(CharTex != null)
            if(FlipX)
            {
                int FlippedOffset = CharTex.getWidthOrig() - (int)CharTex.offsetX - CharTex.getWidth();
                CharacterCreationPanel.DrawTexture_FlippedXIgnoreOffset(CharTex, BodyPosX + FlippedOffset * BodyScale, BodyPosY + (int)CharTex.offsetY * BodyScale, CharTex.getWidth() * BodyScale, CharTex.getHeight() * BodyScale, new Color(255, 255, 255, 255));
            } else
            {
                CharacterCreationPanel.DrawTextureIgnoreOffset(CharTex, BodyPosX + (int)CharTex.offsetX * BodyScale, BodyPosY + (int)CharTex.offsetY * BodyScale, CharTex.getWidth() * BodyScale, CharTex.getHeight() * BodyScale, new Color(255, 255, 255, 255));
            }
        String HeadTexName = (new StringBuilder()).append("media/Characters/Pieces/").append(ViewerAnimName).append("_Base_Head").append(CurrentHead + 1).append("_").append(DirectionValues[CharRotation]).append("_").append(FrameIndex).append("_Skin_0").append(CurrentSkinTone).append(".pcx").toString();
        CharTex = Texture.getSharedTexture(HeadTexName);
        if(CharTex != null)
            if(FlipX)
            {
                int FlippedOffset = CharTex.getWidthOrig() - (int)CharTex.offsetX - CharTex.getWidth();
                CharacterCreationPanel.DrawTexture_FlippedXIgnoreOffset(CharTex, BodyPosX + FlippedOffset * BodyScale, BodyPosY + (int)CharTex.offsetY * BodyScale, CharTex.getWidth() * BodyScale, CharTex.getHeight() * BodyScale, new Color(255, 255, 255, 255));
            } else
            {
                CharacterCreationPanel.DrawTextureIgnoreOffset(CharTex, BodyPosX + (int)CharTex.offsetX * BodyScale, BodyPosY + (int)CharTex.offsetY * BodyScale, CharTex.getWidth() * BodyScale, CharTex.getHeight() * BodyScale, new Color(255, 255, 255, 255));
            }
        CharacterCreationPanel.DrawTextCentre((new StringBuilder()).append(" ").append(CurrentHead + 1).append(" ").toString(), 653, 133, 1.0F, 1.0F, 1.0F, 1.0F);
        CharacterCreationPanel.DrawTextCentre((new StringBuilder()).append(" ").append(CurrentSkinTone + 1).append(" ").toString(), 653, 150, 1.0F, 1.0F, 1.0F, 1.0F);
        Core.getInstance().EndFrameUI();
    }

    public GameStateMachine.StateAction update()
    {
        if(SkinToneRight.clicked)
        {
            SkinToneRight.clicked = false;
            CurrentSkinTone++;
            if(CurrentSkinTone >= NumSkinTones)
                CurrentSkinTone = 0;
        }
        if(SkinToneLeft.clicked)
        {
            SkinToneLeft.clicked = false;
            CurrentSkinTone--;
            if(CurrentSkinTone < 0)
                CurrentSkinTone = NumSkinTones - 1;
        }
        if(FaceCycleRight.clicked)
        {
            FaceCycleRight.clicked = false;
            CurrentHead++;
            if(CurrentHead >= NumHeads)
                CurrentHead = 0;
        }
        if(FaceCycleLeft.clicked)
        {
            FaceCycleLeft.clicked = false;
            CurrentHead--;
            if(CurrentHead < 0)
                CurrentHead = NumHeads - 1;
        }
        if(ShirtColourRight.clicked)
        {
            ShirtColourRight.clicked = false;
            CurrentShirtColour++;
            if(CurrentShirtColour >= NumShirtColours)
                CurrentShirtColour = 0;
        }
        if(ShirtColourLeft.clicked)
        {
            ShirtColourLeft.clicked = false;
            CurrentShirtColour--;
            if(CurrentShirtColour < 0)
                CurrentShirtColour = NumShirtColours - 1;
        }
        if(TrousersColourRight.clicked)
        {
            TrousersColourRight.clicked = false;
            CurrentTrousersColour++;
            if(CurrentTrousersColour >= NumTrousersColours)
                CurrentTrousersColour = 0;
        }
        if(TrousersColourLeft.clicked)
        {
            TrousersColourLeft.clicked = false;
            CurrentTrousersColour--;
            if(CurrentTrousersColour < 0)
                CurrentTrousersColour = NumTrousersColours - 1;
        }
        for(int i = 0; i < traitList.Items.size(); i++)
        {
            zombie.ui.ListBox.ListItem trait = (zombie.ui.ListBox.ListItem)traitList.Items.get(i);
            trait.bDisabled = false;
        }

        for(int i = 0; i < pickedTraitList.Items.size(); i++)
        {
            zombie.ui.ListBox.ListItem trait = (zombie.ui.ListBox.ListItem)pickedTraitList.Items.get(i);
            zombie.characters.traits.TraitFactory.Trait tr = (zombie.characters.traits.TraitFactory.Trait)(zombie.characters.traits.TraitFactory.Trait)((zombie.ui.ListBox.ListItem)pickedTraitList.Items.get(i)).item;
            for(int j = 0; j < traitList.Items.size(); j++)
            {
                zombie.ui.ListBox.ListItem trait2 = (zombie.ui.ListBox.ListItem)traitList.Items.get(j);
                zombie.characters.traits.TraitFactory.Trait tr2 = (zombie.characters.traits.TraitFactory.Trait)(zombie.characters.traits.TraitFactory.Trait)((zombie.ui.ListBox.ListItem)traitList.Items.get(j)).item;
                if(tr.MutuallyExclusive.contains(tr2.traitID))
                    trait2.bDisabled = true;
            }

        }

        CharacterCreationPanel.setX(Core.getInstance().getOffscreenWidth() / 2 - 375);
        CharacterCreationPanel.setY(Core.getInstance().getOffscreenHeight() / 2 - 275);
        if(Done)
        {
            desc.setLegs("Base_Legs");
            desc.setTorso("Base_Torso");
            desc.setHead((new StringBuilder()).append("Base_Head").append(CurrentHead + 1).toString());
            desc.setForename(Forename);
            desc.setSurname(Surname);
            if(CurrentShirtColour != -1)
            {
                desc.setTop("Shirt1");
                desc.setToppal((new StringBuilder()).append("Shirt").append(ShirtColours[CurrentShirtColour]).toString());
            } else
            {
                desc.setTop(null);
                desc.setToppal(null);
            }
            desc.setBottoms("Trousers1");
            desc.setBottomspal((new StringBuilder()).append("Trousers").append(TrousersColours[CurrentTrousersColour]).toString());
            desc.setSkinpal((new StringBuilder()).append("Skin_0").append(CurrentSkinTone).toString());
            desc.setShoes("Shoes1");
            desc.setShoespal(null);
            return GameStateMachine.StateAction.Continue;
        } else
        {
            return GameStateMachine.StateAction.Remain;
        }
    }

    public void DoubleClick(String name, int x, int y)
    {
        if(name.equals("unpicked") && traitList.Selected < traitList.Items.size())
        {
            zombie.ui.ListBox.ListItem trait = (zombie.ui.ListBox.ListItem)traitList.Items.get(traitList.Selected);
            zombie.characters.traits.TraitFactory.Trait tr = (zombie.characters.traits.TraitFactory.Trait)(zombie.characters.traits.TraitFactory.Trait)((zombie.ui.ListBox.ListItem)traitList.Items.get(traitList.Selected)).item;
            traitList.Items.remove(traitList.Selected);
            pickedTraitList.Items.add(trait);
            traitList.Selected = -1;
            PointsToSpend -= tr.cost;
        }
        if(name.equals("picked") && pickedTraitList.Selected < pickedTraitList.Items.size())
        {
            zombie.ui.ListBox.ListItem trait = (zombie.ui.ListBox.ListItem)pickedTraitList.Items.get(pickedTraitList.Selected);
            zombie.characters.traits.TraitFactory.Trait tr = (zombie.characters.traits.TraitFactory.Trait)(zombie.characters.traits.TraitFactory.Trait)((zombie.ui.ListBox.ListItem)pickedTraitList.Items.get(pickedTraitList.Selected)).item;
            pickedTraitList.Items.remove(pickedTraitList.Selected);
            traitList.Items.add(trait);
            pickedTraitList.Selected = -1;
            PointsToSpend += tr.cost;
        }
    }

    public void Selected(String name, int Selected, int LastSelected)
    {
        if(name.equals("Start") && PointsToSpend >= 0 && ProfessionSelected)
        {
            IsoPlayer.getStaticTraits().clear();
            for(int n = 0; n < pickedTraitList.Items.size(); n++)
            {
                zombie.characters.traits.TraitFactory.Trait tr = (zombie.characters.traits.TraitFactory.Trait)(zombie.characters.traits.TraitFactory.Trait)((zombie.ui.ListBox.ListItem)pickedTraitList.Items.get(n)).item;
                IsoPlayer.getStaticTraits().add(tr.traitID);
            }

            Done = true;
        }
        if(name.equals("profession"))
        {
            if(professionList.LastSelected != -1)
            {
                zombie.characters.professions.ProfessionFactory.Profession lastProf = (zombie.characters.professions.ProfessionFactory.Profession)((zombie.ui.ListBox.ListItem)professionList.Items.get(professionList.LastSelected)).item;
                desc.setProfession(lastProf.type);
                if(lastProf.getType().equals("fireofficer"))
                {
                    CurrentShirtColour = 0;
                    CurrentTrousersColour = 3;
                }
                if(lastProf.getType().equals("policeofficer"))
                {
                    CurrentShirtColour = 7;
                    CurrentTrousersColour = 2;
                }
                if(lastProf.getType().equals("parkranger"))
                {
                    CurrentShirtColour = 5;
                    CurrentTrousersColour = 1;
                }
                if(lastProf.getType().equals("securityguard"))
                {
                    CurrentShirtColour = 7;
                    CurrentTrousersColour = 0;
                }
                if(lastProf.getType().equals("constructionworker"))
                {
                    CurrentShirtColour = -1;
                    CurrentTrousersColour = 0;
                }
                for(int n = 0; n < lastProf.getFreeTraitStack().size(); n++)
                    pickedTraitList.remove(TraitFactory.getTrait((String)lastProf.getFreeTraitStack().get(n)));

                helpText.SetText(lastProf.getDescription());
                ProfessionSelected = true;
            }
            if(professionList.Selected != -1)
            {
                zombie.characters.professions.ProfessionFactory.Profession lastProf = (zombie.characters.professions.ProfessionFactory.Profession)((zombie.ui.ListBox.ListItem)professionList.Items.get(professionList.Selected)).item;
                desc.setProfession(lastProf.type);
                if(lastProf.getType().equals("fireofficer"))
                {
                    CurrentShirtColour = 0;
                    CurrentTrousersColour = 3;
                }
                if(lastProf.getType().equals("policeofficer"))
                {
                    CurrentShirtColour = 7;
                    CurrentTrousersColour = 2;
                }
                if(lastProf.getType().equals("parkranger"))
                {
                    CurrentShirtColour = 5;
                    CurrentTrousersColour = 1;
                }
                if(lastProf.getType().equals("securityguard"))
                {
                    CurrentShirtColour = 7;
                    CurrentTrousersColour = 0;
                }
                if(lastProf.getType().equals("constructionworker"))
                {
                    CurrentShirtColour = -1;
                    CurrentTrousersColour = 0;
                }
                for(int n = 0; n < lastProf.getFreeTraitStack().size(); n++)
                    pickedTraitList.AddItem(TraitFactory.getTrait((String)lastProf.getFreeTraitStack().get(n)), Color.white, Color.black, new Color(255, 128, 255, 15), true);

                helpText.SetText(lastProf.getDescription());
                ProfessionSelected = true;
            }
        } else
        if(name.equals("unpicked"))
        {
            pickedTraitList.Selected = -1;
            pickedTraitList.LastSelected = -1;
            helpText.SetText(((zombie.characters.traits.TraitFactory.Trait)traitList.getSelected()).description);
        } else
        if(name.equals("picked"))
        {
            traitList.Selected = -1;
            traitList.LastSelected = -1;
            helpText.SetText(((zombie.characters.traits.TraitFactory.Trait)pickedTraitList.getSelected()).description);
        }
    }

    ListBox traitList;
    ListBox professionList;
    ListBox pickedTraitList;
    ListBox GenderList;
    TextBox helpText;
    TextBox TraitAdviceText;
    UITextBox2 CantStartText;
    DialogButton button;
    UITextBox2 InstructionText;
    GenericButton RotateClockwise;
    GenericButton RotateAntiClockwise;
    GenericButton FaceCycleLeft;
    GenericButton FaceCycleRight;
    GenericButton SkinToneLeft;
    GenericButton SkinToneRight;
    GenericButton ShirtColourLeft;
    GenericButton ShirtColourRight;
    GenericButton TrousersColourLeft;
    GenericButton TrousersColourRight;
    UITextBox2 ForenameBox;
    UITextBox2 SurnameBox;
    UINineGrid CustomiseBox;
    String Forename;
    String Surname;
    UIDialoguePanel CharacterViewPanel;
    ScrollBar ProffessionScrollBar;
    ScrollBar TraitsScrollBar;
    ScrollBar PickedTraitsScrollBar;
    int CharRotation;
    int CharRotationDelay;
    int CharRotationTimer;
    int AnimFrameTimer;
    int AnimFrameDelay;
    int FrameIndex;
    int NumAnimFrames;
    private boolean Done;
    private boolean ProfessionSelected;
    boolean Flipped[] = {
        false, true, true, true, false, false, false, false
    };
    String ViewerAnimName;
    int DirectionValues[] = {
        2, 3, 6, 9, 8, 9, 6, 3
    };
    int CurrentSkinTone;
    int NumSkinTones;
    int CurrentShirtColour;
    int NumShirtColours;
    String ShirtColours[] = {
        "Black", "Blue", "Brown", "Green", "Grey", "Sand", "White", "DarkBlue", "Orange", "Pink"
    };
    String TrousersColours[] = {
        "Blue", "Brown", "Grey", "White"
    };
    int CurrentTrousersColour;
    int NumTrousersColours;
    int CurrentHead;
    int NumHeads;
    public static CharacterCreationPanel CharacterCreationPanel;
    public int PointsToSpend;
    public SurvivorDesc desc;
    public boolean UseCustomChatacterData;
}
