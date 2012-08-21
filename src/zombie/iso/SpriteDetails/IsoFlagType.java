/*    */ package zombie.iso.SpriteDetails;
/*    */ 
/*    */ public enum IsoFlagType
/*    */ {
/* 13 */   collideW(0), 
/* 14 */   collideN(1), 
/* 15 */   solidfloor(2), 
/* 16 */   noStart(3), 
/* 17 */   windowW(4), 
/* 18 */   windowN(5), 
/* 19 */   hidewalls(6), 
/* 20 */   exterior(7), 
/* 21 */   unused(8), 
/* 22 */   doorW(9), 
/* 23 */   doorN(10), 
/* 24 */   transparentW(11), 
/* 25 */   transparentN(12), 
/* 26 */   WallOverlay(13), 
/* 27 */   FloorOverlay(14), 
/* 28 */   vegitation(15), 
/* 29 */   burning(16), 
/* 30 */   burntOut(17), 
/* 31 */   unflamable(18), 
/* 32 */   cutW(19), 
/* 33 */   cutN(20), 
/* 34 */   tableN(21), 
/* 35 */   tableNW(22), 
/* 36 */   tableW(23), 
/* 37 */   tableSW(24), 
/* 38 */   tableS(25), 
/* 39 */   tableSE(26), 
/* 40 */   tableE(27), 
/* 41 */   tableNE(28), 
/* 42 */   halfheight(29), 
/* 43 */   HasRainSplashes(30), 
/* 44 */   HasRaindrop(31), 
/* 45 */   solid(32), 
/* 46 */   trans(33), 
/* 47 */   pushable(34), 
/* 48 */   solidtrans(35), 
/* 49 */   invisible(36), 
/* 50 */   floorS(37), 
/* 51 */   floorE(38), 
/* 52 */   shelfS(39), 
/* 53 */   shelfE(40), 
/* 54 */   alwaysDraw(41), 
/* 55 */   ontable(42), 
/* 56 */   sledgesmash(43), 
/* 57 */   climbSheetW(44), 
/* 58 */   climbSheetN(45), 
/* 59 */   climbSheetTopN(46), 
/* 60 */   climbSheetTopW(47), 
/* 61 */   attachtostairs(48), 
/* 62 */   sheetCurtains(49), 
/* 63 */   waterPiped(50), 
/* 64 */   HoppableN(51), 
/* 65 */   HoppableW(52), 
/* 66 */   bed(53), 
/* 67 */   MAX(54);
/*    */ 
/*    */   private int index;
/*    */ 
/* 72 */   private IsoFlagType(int index) { this.index = index;
/*    */   }
/*    */ 
/*    */   public int index()
/*    */   {
/* 77 */     return this.index;
/*    */   }
/*    */ 
/*    */   public static IsoFlagType fromIndex(int value)
/*    */   {
/* 82 */     return ((IsoFlagType[])IsoFlagType.class.getEnumConstants())[value];
/*    */   }
/*    */ 
/*    */   public static IsoFlagType FromString(String str)
/*    */   {
/*    */     try
/*    */     {
/* 90 */       return valueOf(str);
/*    */     }
/*    */     catch (Exception ex) {
/*    */     }
/* 94 */     return MAX;
/*    */   }
/*    */ }

/* Location:           C:\Users\work\Desktop\Project Zomboid\
 * Qualified Name:     zombie.iso.SpriteDetails.IsoFlagType
 * JD-Core Version:    0.6.0
 */