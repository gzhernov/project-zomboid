// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MoodlesUI.java

package zombie.ui;

import java.util.Stack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Rectangle;
import zombie.characters.*;
import zombie.characters.Moodles.MoodleType;
import zombie.characters.Moodles.Moodles;
import zombie.core.Core;
import zombie.core.textures.Texture;
import zombie.input.Mouse;
import zombie.iso.IsoCamera;

// Referenced classes of package zombie.ui:
//            UIElement

public class MoodlesUI extends UIElement
{

    public MoodlesUI()
    {
        clientH = 0;
        clientW = 0;
        Movable = false;
        ncclientH = 0;
        ncclientW = 0;
        nestedItems = new Stack();
        alpha = 1.0F;
        Back_Bad_1 = null;
        Back_Bad_2 = null;
        Back_Bad_3 = null;
        Back_Bad_4 = null;
        Back_Good_1 = null;
        Back_Good_2 = null;
        Back_Good_3 = null;
        Back_Good_4 = null;
        Back_Neutral = null;
        Endurance = null;
        Bleeding = null;
        Angry = null;
        Stress = null;
        Thirst = null;
        Panic = null;
        Hungry = null;
        Injured = null;
        Pain = null;
        Sick = null;
        Bored = null;
        Unhappy = null;
        Tired = null;
        HeavyLoad = null;
        Drunk = null;
        Wet = null;
        HasACold = null;
        Dead = null;
        Zombie = null;
        FoodEaten = null;
        MoodleDistY = 36F;
        MouseOver = false;
        MouseOverSlot = 0;
        NumUsedSlots = 0;
        DebugKeyDelay = 0;
        DistFromRighEdge = 46;
        GoodBadNeutral = new int[MoodleType.ToIndex(MoodleType.MAX)];
        MoodleLevel = new int[MoodleType.ToIndex(MoodleType.MAX)];
        MoodleOscilationLevel = new float[MoodleType.ToIndex(MoodleType.MAX)];
        MoodleSlotsDesiredPos = new float[MoodleType.ToIndex(MoodleType.MAX)];
        MoodleSlotsPos = new float[MoodleType.ToIndex(MoodleType.MAX)];
        MoodleTypeInSlot = new int[MoodleType.ToIndex(MoodleType.MAX)];
        Oscilator = 0.0F;
        OscilatorDecelerator = 0.96F;
        OscilatorRate = 0.8F;
        OscilatorScalar = 15.6F;
        OscilatorStartLevel = 1.0F;
        OscilatorStep = 0.0F;
        UseCharacter = null;
        x = Core.getInstance().getOffscreenWidth() - DistFromRighEdge;
        y = 74F;
        width = 32;
        height = 500;
        Back_Bad_1 = Texture.getSharedTexture("media/ui/Moodles/Moodle_Bkg_Bad_1.png");
        Back_Bad_2 = Texture.getSharedTexture("media/ui/Moodles/Moodle_Bkg_Bad_2.png");
        Back_Bad_3 = Texture.getSharedTexture("media/ui/Moodles/Moodle_Bkg_Bad_3.png");
        Back_Bad_4 = Texture.getSharedTexture("media/ui/Moodles/Moodle_Bkg_Bad_4.png");
        Back_Good_1 = Texture.getSharedTexture("media/ui/Moodles/Moodle_Bkg_Good_1.png");
        Back_Good_2 = Texture.getSharedTexture("media/ui/Moodles/Moodle_Bkg_Good_2.png");
        Back_Good_3 = Texture.getSharedTexture("media/ui/Moodles/Moodle_Bkg_Good_3.png");
        Back_Good_4 = Texture.getSharedTexture("media/ui/Moodles/Moodle_Bkg_Good_4.png");
        Back_Neutral = Texture.getSharedTexture("media/ui/Moodles/Moodle_Bkg_Bad_1.png");
        Endurance = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Endurance.png");
        Tired = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Tired.png");
        Hungry = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Hungry.png");
        Panic = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Panic.png");
        Sick = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Sick.png");
        Bored = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Bored.png");
        Unhappy = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Unhappy.png");
        Bleeding = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Bleeding.png");
        Wet = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Wet.png");
        HasACold = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Cold.png");
        Angry = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Angry.png");
        Stress = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Stressed.png");
        Thirst = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Thirsty.png");
        Injured = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Injured.png");
        Pain = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Pain.png");
        HeavyLoad = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_HeavyLoad.png");
        Drunk = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Drunk.png");
        Dead = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Dead.png");
        Zombie = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Zombie.png");
        FoodEaten = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Hungry.png");
        for(int i = 0; i < MoodleType.ToIndex(MoodleType.MAX); i++)
        {
            MoodleSlotsPos[i] = 3000F;
            MoodleSlotsDesiredPos[i] = 3000F;
        }

        clientW = width;
        clientH = height;
    }

    public boolean CurrentlyAnimating()
    {
        boolean Animating = false;
        for(int i = 0; i < MoodleType.ToIndex(MoodleType.MAX); i++)
            if(MoodleSlotsPos[i] != MoodleSlotsDesiredPos[i])
                Animating = true;

        return Animating;
    }

    public void Nest(UIElement el, int t, int r, int b, int l)
    {
        AddChild(el);
        nestedItems.add(new Rectangle(l, t, r, b));
    }

    public boolean onMouseMove(int dx, int dy)
    {
        if(!isVisible())
            return false;
        MouseOver = true;
        super.onMouseMove(dx, dy);
        MouseOverSlot = (int)(((float)Mouse.getY() - getY()) / MoodleDistY);
        if(MouseOverSlot >= NumUsedSlots)
            MouseOverSlot = 1000;
        return true;
    }

    public void onMouseMoveOutside(int dx, int dy)
    {
        super.onMouseMoveOutside(dx, dy);
        MouseOverSlot = 1000;
        MouseOver = false;
    }

    public void render()
    {
        if(IsoPlayer.getInstance() == null)
            return;
        UseCharacter = IsoCamera.CamCharacter;
        if(UseCharacter instanceof IsoZombie)
            UseCharacter = IsoPlayer.getInstance();
        OscilatorStep += OscilatorRate;
        Oscilator = (float)Math.sin(OscilatorStep);
        for(int i = 0; i < MoodleType.ToIndex(MoodleType.MAX); i++)
        {
            float WiggleOffset = Oscilator * OscilatorScalar * MoodleOscilationLevel[i];
            Texture BackTex = Back_Neutral;
            Texture MoodleTex = Tired;
            switch(GoodBadNeutral[i])
            {
            case 0: // '\0'
                BackTex = Back_Neutral;
                break;

            case 1: // '\001'
                switch(MoodleLevel[i])
                {
                case 1: // '\001'
                    BackTex = Back_Good_1;
                    break;

                case 2: // '\002'
                    BackTex = Back_Good_2;
                    break;

                case 3: // '\003'
                    BackTex = Back_Good_3;
                    break;

                case 4: // '\004'
                    BackTex = Back_Good_4;
                    break;
                }
                break;

            case 2: // '\002'
                switch(MoodleLevel[i])
                {
                case 1: // '\001'
                    BackTex = Back_Bad_1;
                    break;

                case 2: // '\002'
                    BackTex = Back_Bad_2;
                    break;

                case 3: // '\003'
                    BackTex = Back_Bad_3;
                    break;

                case 4: // '\004'
                    BackTex = Back_Bad_4;
                    break;
                }
                break;
            }
            switch(i)
            {
            case 0: // '\0'
                MoodleTex = Endurance;
                break;

            case 1: // '\001'
                MoodleTex = Tired;
                break;

            case 2: // '\002'
                MoodleTex = Hungry;
                break;

            case 3: // '\003'
                MoodleTex = Panic;
                break;

            case 4: // '\004'
                MoodleTex = Sick;
                break;

            case 5: // '\005'
                MoodleTex = Bored;
                break;

            case 6: // '\006'
                MoodleTex = Unhappy;
                break;

            case 7: // '\007'
                MoodleTex = Bleeding;
                break;

            case 8: // '\b'
                MoodleTex = Wet;
                break;

            case 9: // '\t'
                MoodleTex = HasACold;
                break;

            case 10: // '\n'
                MoodleTex = Angry;
                break;

            case 11: // '\013'
                MoodleTex = Stress;
                break;

            case 12: // '\f'
                MoodleTex = Thirst;
                break;

            case 13: // '\r'
                MoodleTex = Injured;
                break;

            case 14: // '\016'
                MoodleTex = Pain;
                break;

            case 15: // '\017'
                MoodleTex = HeavyLoad;
                break;

            case 16: // '\020'
                MoodleTex = Drunk;
                break;

            case 17: // '\021'
                MoodleTex = Dead;
                break;

            case 18: // '\022'
                MoodleTex = Zombie;
                break;

            case 19: // '\023'
                MoodleTex = FoodEaten;
                break;
            }
            DrawTexture(BackTex, 0 + (int)WiggleOffset, (int)MoodleSlotsPos[i], alpha);
            DrawTexture(MoodleTex, 0 + (int)WiggleOffset, (int)MoodleSlotsPos[i], alpha);
            if(MouseOver && i == MouseOverSlot)
            {
                DrawTextRight(UseCharacter.getMoodles().getMoodleDisplayString(MoodleTypeInSlot[i]), -10, (int)MoodleSlotsPos[MoodleTypeInSlot[i]] + 3, 1.0F, 1.0F, 1.0F, 1.0F);
                DrawTextRight(UseCharacter.getMoodles().getMoodleDescriptionString(MoodleTypeInSlot[i]), -10, (int)MoodleSlotsPos[MoodleTypeInSlot[i]] + 15, 0.75F, 0.75F, 0.75F, 1.0F);
            }
        }

        super.render();
    }

    public void update()
    {
        super.update();
        if(IsoPlayer.getInstance() == null)
            return;
        setX(Core.getInstance().getOffscreenWidth() - DistFromRighEdge);
        if(!CurrentlyAnimating())
            if(DebugKeyDelay > 0)
                DebugKeyDelay--;
            else
            if(Keyboard.isKeyDown(57))
                DebugKeyDelay = 10;
        for(int i = 0; i < MoodleType.ToIndex(MoodleType.MAX); i++)
            MoodleOscilationLevel[i] *= OscilatorDecelerator;

        if(UseCharacter == null)
            UseCharacter = IsoPlayer.getInstance();
        if(UseCharacter.getMoodles().UI_RefreshNeeded())
        {
            int CurrentSlotPlace = 0;
            for(int i = 0; i < MoodleType.ToIndex(MoodleType.MAX); i++)
                if(UseCharacter.getMoodles().getMoodleLevel(i) > 0)
                {
                    boolean HasChanged = false;
                    if(MoodleLevel[i] != UseCharacter.getMoodles().getMoodleLevel(i))
                    {
                        HasChanged = true;
                        MoodleLevel[i] = UseCharacter.getMoodles().getMoodleLevel(i);
                        MoodleOscilationLevel[i] = OscilatorStartLevel;
                    }
                    if(HasChanged)
                    {
                        if(MoodleSlotsPos[i] == 3000F)
                        {
                            MoodleSlotsDesiredPos[i] = MoodleDistY * (float)CurrentSlotPlace;
                            MoodleSlotsPos[i] = MoodleSlotsDesiredPos[i] + 500F;
                            MoodleOscilationLevel[i] = 0.0F;
                        }
                        GoodBadNeutral[i] = UseCharacter.getMoodles().getGoodBadNeutral(i);
                    } else
                    {
                        MoodleSlotsDesiredPos[i] = MoodleDistY * (float)CurrentSlotPlace;
                        MoodleOscilationLevel[i] = 0.0F;
                    }
                    MoodleTypeInSlot[CurrentSlotPlace] = i;
                    CurrentSlotPlace++;
                    NumUsedSlots = CurrentSlotPlace;
                } else
                {
                    MoodleSlotsPos[i] = 3000F;
                    MoodleSlotsDesiredPos[i] = 3000F;
                    MoodleOscilationLevel[i] = 0.0F;
                    MoodleLevel[i] = 0;
                }

        }
        for(int i = 0; i < MoodleType.ToIndex(MoodleType.MAX); i++)
            if(Math.abs(MoodleSlotsPos[i] - MoodleSlotsDesiredPos[i]) > 0.8F)
                MoodleSlotsPos[i] += (MoodleSlotsDesiredPos[i] - MoodleSlotsPos[i]) * 0.15F;
            else
                MoodleSlotsPos[i] = MoodleSlotsDesiredPos[i];

    }

    public static MoodlesUI instance;
    public int clientH;
    public int clientW;
    public boolean Movable;
    public int ncclientH;
    public int ncclientW;
    public Stack nestedItems;
    float alpha;
    Texture Back_Bad_1;
    Texture Back_Bad_2;
    Texture Back_Bad_3;
    Texture Back_Bad_4;
    Texture Back_Good_1;
    Texture Back_Good_2;
    Texture Back_Good_3;
    Texture Back_Good_4;
    Texture Back_Neutral;
    Texture Endurance;
    Texture Bleeding;
    Texture Angry;
    Texture Stress;
    Texture Thirst;
    Texture Panic;
    Texture Hungry;
    Texture Injured;
    Texture Pain;
    Texture Sick;
    Texture Bored;
    Texture Unhappy;
    Texture Tired;
    Texture HeavyLoad;
    Texture Drunk;
    Texture Wet;
    Texture HasACold;
    Texture Dead;
    Texture Zombie;
    Texture FoodEaten;
    float MoodleDistY;
    boolean MouseOver;
    int MouseOverSlot;
    int NumUsedSlots;
    private int DebugKeyDelay;
    private int DistFromRighEdge;
    private int GoodBadNeutral[];
    private int MoodleLevel[];
    private float MoodleOscilationLevel[];
    private float MoodleSlotsDesiredPos[];
    private float MoodleSlotsPos[];
    private int MoodleTypeInSlot[];
    private float Oscilator;
    private float OscilatorDecelerator;
    private float OscilatorRate;
    private float OscilatorScalar;
    private float OscilatorStartLevel;
    private float OscilatorStep;
    private IsoGameCharacter UseCharacter;
}
