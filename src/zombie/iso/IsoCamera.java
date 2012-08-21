// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoCamera.java

package zombie.iso;

import java.util.Stack;
import zombie.characters.*;
import zombie.input.Mouse;
import zombie.ui.MoodlesUI;
import zombie.ui.UIManager;

// Referenced classes of package zombie.iso:
//            Vector2, IsoUtils

public class IsoCamera
{

    public IsoCamera()
    {
    }

    public static void update()
    {
        RightClickX += (RightClickTargetX - RightClickX) / 80F;
        RightClickY += (RightClickTargetY - RightClickY) / 80F;
        if(IsoPlayer.instance.IsSneaking() || IsoPlayer.instance.bRightClickMove && !IsoPlayer.instance.JustMoved)
        {
            float mX = Mouse.getX();
            float mY = Mouse.getY();
            float wX = IsoUtils.XToIsoTrue(mX, mY, (int)CamCharacter.getZ());
            float wY = IsoUtils.YToIsoTrue(mX, mY, (int)CamCharacter.getZ());
            wX -= 9F;
            wY -= 9F;
            wX--;
            wY -= 0.3F;
            wX -= 0.3F;
            float dX = wX - CamCharacter.x;
            float dY = wY - CamCharacter.y;
            offVec.x = dX;
            offVec.y = dY;
            if(offVec.getLength() < 7F)
                offVec.setLength(0.0F);
            else
                offVec.setLength(offVec.getLength() - 7F);
            if(offVec.getLength() > 20F)
                offVec.setLength(20F);
            float sX = IsoUtils.XToScreen(offVec.x + CamCharacter.x, offVec.y + CamCharacter.y, (int)CamCharacter.getZ(), 0);
            float sY = IsoUtils.YToScreen(offVec.x + CamCharacter.x, offVec.y + CamCharacter.y, (int)CamCharacter.getZ(), 0);
            float cX = IsoUtils.XToScreen(CamCharacter.x, CamCharacter.y, (int)CamCharacter.getZ(), 0);
            float cY = IsoUtils.YToScreen(CamCharacter.x, CamCharacter.y, (int)CamCharacter.getZ(), 0);
            float difX = sX - cX;
            float difY = sY - cY;
            RightClickTargetX = difX;
            RightClickTargetY = difY;
        } else
        {
            RightClickTargetX = 0.0F;
            RightClickTargetY = 0.0F;
            RightClickX += (RightClickTargetX - RightClickX) / 16F;
            RightClickY += (RightClickTargetY - RightClickY) / 16F;
        }
    }

    public static void updateDemo()
    {
    }

    public static void SetCharacterToFollow(IsoGameCharacter GameChar)
    {
        CamCharacter = GameChar;
        if(!(CamCharacter instanceof IsoZombie) && UIManager.getMoodleUI() != null)
        {
            UIManager.getUI().remove(UIManager.getMoodleUI());
            UIManager.setMoodleUI(new MoodlesUI());
            UIManager.getUI().add(UIManager.getMoodleUI());
        }
    }

    public static float getOffX()
    {
        return (float)(int)(OffX + RightClickX);
    }

    public static void setOffX(float aOffX)
    {
        OffX = aOffX;
    }

    public static float getOffY()
    {
        return (float)(int)(OffY + RightClickY);
    }

    public static void setOffY(float aOffY)
    {
        OffY = aOffY;
    }

    public static float getLastOffX()
    {
        return (float)(int)(lastOffX + RightClickX);
    }

    public static void setLastOffX(float aLastOffX)
    {
        lastOffX = aLastOffX;
    }

    public static float getLastOffY()
    {
        return (float)(int)(lastOffY + RightClickY);
    }

    public static void setLastOffY(float aLastOffY)
    {
        lastOffY = aLastOffY;
    }

    public static IsoGameCharacter getCamCharacter()
    {
        return CamCharacter;
    }

    public static void setCamCharacter(IsoGameCharacter aCamCharacter)
    {
        CamCharacter = aCamCharacter;
    }

    public static Vector2 getFakePos()
    {
        return FakePos;
    }

    public static void setFakePos(Vector2 aFakePos)
    {
        FakePos = aFakePos;
    }

    public static Vector2 getFakePosVec()
    {
        return FakePosVec;
    }

    public static void setFakePosVec(Vector2 aFakePosVec)
    {
        FakePosVec = aFakePosVec;
    }

    public static int getTargetTileX()
    {
        return TargetTileX;
    }

    public static void setTargetTileX(int aTargetTileX)
    {
        TargetTileX = aTargetTileX;
    }

    public static int getTargetTileY()
    {
        return TargetTileY;
    }

    public static void setTargetTileY(int aTargetTileY)
    {
        TargetTileY = aTargetTileY;
    }

    public static float OffX = 0.0F;
    public static float OffY = 0.0F;
    public static float lastOffX = 0.0F;
    public static float lastOffY = 0.0F;
    public static float RightClickTargetX = 0.0F;
    public static float RightClickTargetY = 0.0F;
    public static float RightClickX = 0.0F;
    public static float RightClickY = 0.0F;
    public static IsoGameCharacter CamCharacter = null;
    public static Vector2 FakePos = new Vector2();
    public static Vector2 FakePosVec = new Vector2();
    public static int TargetTileX = 0;
    public static int TargetTileY = 0;
    public static Vector2 offVec = new Vector2();

}
