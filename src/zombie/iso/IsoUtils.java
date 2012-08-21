// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoUtils.java

package zombie.iso;


// Referenced classes of package zombie.iso:
//            IsoCamera

public class IsoUtils
{

    public IsoUtils()
    {
    }

    public static float DistanceTo(float fromX, float fromY, float toX, float toY)
    {
        return (float)Math.sqrt(Math.pow(toX - fromX, 2D) + Math.pow(toY - fromY, 2D));
    }

    public static float DistanceTo(float fromX, float fromY, float fromZ, float toX, float toY, float toZ)
    {
        return (float)Math.sqrt(Math.pow(toX - fromX, 2D) + Math.pow(toY - fromY, 2D) + Math.pow(toZ - fromZ, 2D) * 2D);
    }

    public static float DistanceToSquared(float fromX, float fromY, float toX, float toY)
    {
        return (float)(Math.pow(toX - fromX, 2D) + Math.pow(toY - fromY, 2D));
    }

    public static float DistanceManhatten(float fromX, float fromY, float toX, float toY)
    {
        return Math.abs(toX - fromX) + Math.abs(toY - fromY);
    }

    public static float DistanceManhatten(float fromX, float fromY, float toX, float toY, float fromZ, float toZ)
    {
        return Math.abs(toX - fromX) + Math.abs(toY - fromY) + Math.abs(toZ - fromZ) * 2.0F;
    }

    public static float DistanceManhattenSquare(float fromX, float fromY, float toX, float toY)
    {
        return Math.max(Math.abs(toX - fromX), Math.abs(toY - fromY));
    }

    public static float XToIso(float screenX, float screenY, float floor)
    {
        float px = screenX + IsoCamera.getOffX();
        float py = screenY + IsoCamera.getOffY();
        float tx = (px + 2.0F * py) / 64F;
        float ty = (px - 2.0F * py) / -64F;
        tx += 3F * floor;
        ty += 3F * floor;
        return tx;
    }

    public static float XToIsoTrue(float screenX, float screenY, int floor)
    {
        float px = screenX + IsoCamera.OffX;
        float py = screenY + IsoCamera.OffY;
        float tx = (px + 2.0F * py) / 64F;
        float ty = (px - 2.0F * py) / -64F;
        tx += 3 * floor;
        ty += 3 * floor;
        return tx;
    }

    public static float XToScreen(float objectX, float objectY, float objectZ, int screenZ)
    {
        float SX = 0.0F;
        SX += objectX * 32F;
        SX -= objectY * 32F;
        return SX;
    }

    public static float YToIso(float screenX, float screenY, float floor)
    {
        float px = screenX + IsoCamera.getOffX();
        float py = screenY + IsoCamera.getOffY();
        float tx = (px + 2.0F * py) / 64F;
        float ty = (px - 2.0F * py) / -64F;
        tx += 3F * floor;
        ty += 3F * floor;
        return ty;
    }

    public static float YToIsoTrue(float screenX, float screenY, int floor)
    {
        float px = screenX + IsoCamera.OffX;
        float py = screenY + IsoCamera.OffY;
        float tx = (px + 2.0F * py) / 64F;
        float ty = (px - 2.0F * py) / -64F;
        tx += 3 * floor;
        ty += 3 * floor;
        return ty;
    }

    public static float YToScreen(float objectX, float objectY, float objectZ, int screenZ)
    {
        float SY = 0.0F;
        SY += objectY * 16F;
        SY += objectX * 16F;
        SY += ((float)screenZ - objectZ) * 96F;
        return SY;
    }
}
