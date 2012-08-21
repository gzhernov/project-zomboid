// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScrollBar.java

package zombie.ui;

import java.util.Stack;
import zombie.core.Color;
import zombie.core.textures.Texture;
import zombie.input.Mouse;

// Referenced classes of package zombie.ui:
//            UIElement, ListBox, TextManager, UITextBox2, 
//            UIEventHandler

public class ScrollBar extends UIElement
{

    public ScrollBar(String name, UIEventHandler messages, int x_pos, int y_pos, int Length, boolean IsVertical)
    {
        BackgroundColour = new Color(255, 255, 255, 255);
        ButtonColour = new Color(255, 255, 255, 127);
        ButtonHighlightColour = new Color(255, 255, 255, 255);
        IsVerticle = true;
        FullLength = 114;
        InsideLength = 100;
        EndLength = 7;
        ButtonInsideLength = 30;
        ButtonEndLength = 6;
        Thickness = 10;
        ButtonThickness = 9;
        ButtonOffset = 40;
        MouseDragStartPos = 0;
        ButtonDragStartPos = 0;
        mouseOver = false;
        BeingDragged = false;
        ParentListBox = null;
        ParentTextBox = null;
        messageParent = messages;
        this.name = name;
        x = x_pos;
        y = y_pos;
        FullLength = Length;
        InsideLength = Length - EndLength * 2;
        IsVerticle = true;
        width = Thickness;
        height = Length;
        ButtonInsideLength = height - ButtonEndLength * 2;
        ButtonOffset = 0;
        BackVertical = Texture.getSharedTexture("media/ui/ScrollbarV_Bkg_Middle.png");
        TopVertical = Texture.getSharedTexture("media/ui/ScrollbarV_Bkg_Top.png");
        BottomVertical = Texture.getSharedTexture("media/ui/ScrollbarV_Bkg_Bottom.png");
        ButtonBackVertical = Texture.getSharedTexture("media/ui/ScrollbarV_Middle.png");
        ButtonTopVertical = Texture.getSharedTexture("media/ui/ScrollbarV_Top.png");
        ButtonBottomVertical = Texture.getSharedTexture("media/ui/ScrollbarV_Bottom.png");
        BackHorizontal = Texture.getSharedTexture("media/ui/ScrollbarH_Bkg_Middle.png");
        LeftHorizontal = Texture.getSharedTexture("media/ui/ScrollbarH_Bkg_Bottom.png");
        RightHorizontal = Texture.getSharedTexture("media/ui/ScrollbarH_Bkg_Top.png");
        ButtonBackHorizontal = Texture.getSharedTexture("media/ui/ScrollbarH_Middle.png");
        ButtonLeftHorizontal = Texture.getSharedTexture("media/ui/ScrollbarH_Bottom.png");
        ButtonRightHorizontal = Texture.getSharedTexture("media/ui/ScrollbarH_Top.png");
    }

    public void SetParentListBox(ListBox Parent)
    {
        ParentListBox = Parent;
    }

    public void SetParentTextBox(UITextBox2 Parent)
    {
        ParentTextBox = Parent;
    }

    public void render()
    {
        if(IsVerticle)
        {
            DrawTextureScaledCol(TopVertical, 0, 0, Thickness, EndLength, BackgroundColour);
            DrawTextureScaledCol(BackVertical, 0, 0 + EndLength, Thickness, InsideLength, BackgroundColour);
            DrawTextureScaledCol(BottomVertical, 0, 0 + EndLength + InsideLength, Thickness, EndLength, BackgroundColour);
            Color DrawCol;
            if(mouseOver)
                DrawCol = ButtonHighlightColour;
            else
                DrawCol = ButtonColour;
            DrawTextureScaledCol(ButtonTopVertical, 1, ButtonOffset + 1, ButtonThickness, ButtonEndLength, DrawCol);
            DrawTextureScaledCol(ButtonBackVertical, 1, ButtonOffset + 1 + ButtonEndLength, ButtonThickness, ButtonInsideLength, DrawCol);
            DrawTextureScaledCol(ButtonBottomVertical, 1, ButtonOffset + 1 + ButtonEndLength + ButtonInsideLength, ButtonThickness, ButtonEndLength, DrawCol);
        }
    }

    public boolean onMouseMove(int dx, int dy)
    {
        mouseOver = true;
        return true;
    }

    public void onMouseMoveOutside(int dx, int dy)
    {
        mouseOver = false;
    }

    public boolean onMouseUp(int x, int y)
    {
        BeingDragged = false;
        return false;
    }

    public boolean onMouseDown(int x, int y)
    {
        boolean ClickedOnButton = false;
        if(y >= ButtonOffset && y <= ButtonOffset + ButtonInsideLength + ButtonEndLength * 2)
            ClickedOnButton = true;
        if(ClickedOnButton)
        {
            BeingDragged = true;
            MouseDragStartPos = Mouse.getY();
            ButtonDragStartPos = ButtonOffset;
        } else
        {
            ButtonOffset = y - (ButtonInsideLength + ButtonEndLength * 2) / 2;
        }
        if(ButtonOffset < 0)
            ButtonOffset = 0;
        if(ButtonOffset > getHeight() - (ButtonInsideLength + ButtonEndLength * 2) - 1)
            ButtonOffset = getHeight() - (ButtonInsideLength + ButtonEndLength * 2) - 1;
        return false;
    }

    public void update()
    {
        super.update();
        if(BeingDragged)
        {
            int MouseDist = MouseDragStartPos - Mouse.getY();
            ButtonOffset = ButtonDragStartPos - MouseDist;
            if(ButtonOffset < 0)
                ButtonOffset = 0;
            if(ButtonOffset > getHeight() - (ButtonInsideLength + ButtonEndLength * 2) - 1)
                ButtonOffset = getHeight() - (ButtonInsideLength + ButtonEndLength * 2) - 1;
            if(!Mouse.isButtonDown(0))
                BeingDragged = false;
        }
        if(ParentListBox != null)
        {
            if(ParentListBox.Items.size() * ParentListBox.itemHeight > ParentListBox.getHeight())
            {
                if(ParentListBox.Items.size() > 0)
                {
                    float PercentShown = (float)ParentListBox.getHeight() / (float)((ParentListBox.Items.size() + 1) * ParentListBox.itemHeight);
                    ButtonInsideLength = (int)((float)getHeight() * PercentShown) - ButtonEndLength * 2;
                    if(ButtonOffset < 0)
                        ButtonOffset = 0;
                    if(ButtonOffset > getHeight() - (ButtonInsideLength + ButtonEndLength * 2) - 1)
                        ButtonOffset = getHeight() - (ButtonInsideLength + ButtonEndLength * 2) - 1;
                    float PercentDown = (float)ButtonOffset / (float)getHeight();
                    ParentListBox.topIndex = (int)((float)ParentListBox.Items.size() * PercentDown);
                } else
                {
                    ButtonOffset = 0;
                    ButtonInsideLength = getHeight() - ButtonEndLength * 2;
                    ParentListBox.topIndex = 0;
                }
            } else
            {
                ButtonOffset = 0;
                ButtonInsideLength = getHeight() - ButtonEndLength * 2;
                ParentListBox.topIndex = 0;
            }
        } else
        if(ParentTextBox != null)
        {
            int TextHeight = TextManager.instance.MeasureStringY(ParentTextBox.font, (String)ParentTextBox.Lines.get(0));
            if(ParentTextBox.Lines.size() * TextHeight > ParentTextBox.getHeight())
            {
                if(ParentTextBox.Lines.size() > 0)
                {
                    float PercentShown = (float)ParentTextBox.getHeight() / (float)(ParentTextBox.Lines.size() * TextHeight);
                    ButtonInsideLength = (int)((float)getHeight() * PercentShown) - ButtonEndLength * 2;
                    if(ButtonOffset < 0)
                        ButtonOffset = 0;
                    if(ButtonOffset > getHeight() - (ButtonInsideLength + ButtonEndLength * 2) - 1)
                        ButtonOffset = getHeight() - (ButtonInsideLength + ButtonEndLength * 2) - 1;
                    float PercentDown = (float)ButtonOffset / (float)getHeight();
                    ParentTextBox.TopLineIndex = (int)((float)ParentTextBox.Lines.size() * PercentDown);
                } else
                {
                    ButtonOffset = 0;
                    ButtonInsideLength = getHeight() - ButtonEndLength * 2;
                    ParentTextBox.TopLineIndex = 0;
                }
            } else
            {
                ButtonOffset = 0;
                ButtonInsideLength = getHeight() - ButtonEndLength * 2;
                ParentTextBox.TopLineIndex = 0;
            }
        }
    }

    public Color BackgroundColour;
    public Color ButtonColour;
    public Color ButtonHighlightColour;
    public boolean IsVerticle;
    private int FullLength;
    private int InsideLength;
    private int EndLength;
    private int ButtonInsideLength;
    private int ButtonEndLength;
    private int Thickness;
    private int ButtonThickness;
    private int ButtonOffset;
    private int MouseDragStartPos;
    private int ButtonDragStartPos;
    private Texture BackVertical;
    private Texture TopVertical;
    private Texture BottomVertical;
    private Texture ButtonBackVertical;
    private Texture ButtonTopVertical;
    private Texture ButtonBottomVertical;
    private Texture BackHorizontal;
    private Texture LeftHorizontal;
    private Texture RightHorizontal;
    private Texture ButtonBackHorizontal;
    private Texture ButtonLeftHorizontal;
    private Texture ButtonRightHorizontal;
    private boolean mouseOver;
    private boolean BeingDragged;
    private ListBox ParentListBox;
    private UITextBox2 ParentTextBox;
    UIEventHandler messageParent;
    private String name;
}
