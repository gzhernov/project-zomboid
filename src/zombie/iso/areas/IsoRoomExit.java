// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IsoRoomExit.java

package zombie.iso.areas;

import zombie.core.Collections.NulledArrayList;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.objects.IsoDoor;

// Referenced classes of package zombie.iso.areas:
//            IsoRoom

public class IsoRoomExit
{

	/*     */   public static enum ExitType
	/*     */   {
	/*  22 */     Door, 
	/*  23 */     Window;
	/*     */   }
	


    public IsoRoomExit(IsoRoomExit to, int x, int y, int layer)
    {
        type = ExitType.Door;
        To = to;
        To.To = this;
        this.layer = layer;
        this.x = x;
        this.y = y;
    }

    public IsoRoomExit(IsoRoom from, IsoRoomExit to, int x, int y, int layer)
    {
        type = ExitType.Door;
        From = from;
        To = to;
        To.To = this;
        this.layer = layer;
        this.x = x;
        this.y = y;
    }

    public IsoRoomExit(IsoRoom from, int x, int y, int layer)
    {
        type = ExitType.Door;
        From = from;
        this.layer = layer;
        this.x = x;
        this.y = y;
    }

    public IsoDoor getDoor(IsoCell cell)
    {
        IsoGridSquare sq = cell.getGridSquare(x, y, layer);
        if(sq != null && sq.getSpecialObjects().size() > 0 && (sq.getSpecialObjects().get(0) instanceof IsoDoor))
            return (IsoDoor)sq.getSpecialObjects().get(0);
        sq = cell.getGridSquare(x, y + 1, layer);
        if(sq != null && sq.getSpecialObjects().size() > 0 && (sq.getSpecialObjects().get(0) instanceof IsoDoor))
            return (IsoDoor)sq.getSpecialObjects().get(0);
        sq = cell.getGridSquare(x + 1, y, layer);
        if(sq != null && sq.getSpecialObjects().size() > 0 && (sq.getSpecialObjects().get(0) instanceof IsoDoor))
            return (IsoDoor)sq.getSpecialObjects().get(0);
        else
            return null;
    }

    public static String ThiggleQ;
    public IsoRoom From;
    public int layer;
    public IsoRoomExit To;
    public ExitType type;
    public int x;
    public int y;

    static 
    {
        ThiggleQ = "";
        ThiggleQ = (new StringBuilder()).append(ThiggleQ).append("D").toString();
        ThiggleQ = (new StringBuilder()).append(ThiggleQ).append(":").toString();
        ThiggleQ = (new StringBuilder()).append(ThiggleQ).append("/").toString();
        ThiggleQ = (new StringBuilder()).append(ThiggleQ).append("Dro").toString();
        ThiggleQ = (new StringBuilder()).append(ThiggleQ).append("pbox").toString();
        ThiggleQ = (new StringBuilder()).append(ThiggleQ).append("/").toString();
        ThiggleQ = (new StringBuilder()).append(ThiggleQ).append("Zom").toString();
        ThiggleQ = (new StringBuilder()).append(ThiggleQ).append("boid").toString();
        ThiggleQ = (new StringBuilder()).append(ThiggleQ).append("/").toString();
        ThiggleQ = (new StringBuilder()).append(ThiggleQ).append("zom").toString();
        ThiggleQ = (new StringBuilder()).append(ThiggleQ).append("bie").toString();
        ThiggleQ = (new StringBuilder()).append(ThiggleQ).append("/").toString();
        ThiggleQ = (new StringBuilder()).append(ThiggleQ).append("bui").toString();
        ThiggleQ = (new StringBuilder()).append(ThiggleQ).append("ld").toString();
        ThiggleQ = (new StringBuilder()).append(ThiggleQ).append("/").toString();
        ThiggleQ = (new StringBuilder()).append(ThiggleQ).append("cla").toString();
        ThiggleQ = (new StringBuilder()).append(ThiggleQ).append("sses/").toString();
    }
}
