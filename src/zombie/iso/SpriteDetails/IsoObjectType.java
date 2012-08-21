/*    */ package zombie.iso.SpriteDetails;
/*    */ 
/*    */ public enum IsoObjectType
/*    */ {
/* 13 */   normal(0), 
/* 14 */   jukebox(1), 
/* 15 */   wall(2), 
/* 16 */   stairsTW(3), 
/* 17 */   stairsTN(4), 
/* 18 */   stairsMW(5), 
/* 19 */   stairsMN(6), 
/* 20 */   stairsBW(7), 
/* 21 */   stairsBN(8), 
/* 22 */   windowW(9), 
/* 23 */   windowN(10), 
/* 24 */   doorW(11), 
/* 25 */   doorN(12), 
/* 26 */   lightswitch(13), 
/* 27 */   radio(14), 
/* 28 */   curtainN(15), 
/* 29 */   curtainS(16), 
/* 30 */   curtainW(17), 
/* 31 */   curtainE(18), 
/* 32 */   doorFrW(19), 
/* 33 */   doorFrN(20), 
/* 34 */   tree(21), 
/* 35 */   windowFN(22), 
/* 36 */   windowFW(23), 
/* 37 */   dsadsadsadsa(24), 
/* 38 */   WestRoofB(25), 
/* 39 */   WestRoofM(26), 
/* 40 */   WestRoofT(27), 
/* 41 */   MAX(28);
/*    */ 
/*    */   private int index;
/*    */ 
/* 46 */   private IsoObjectType(int index) { this.index = index;
/*    */   }
/*    */ 
/*    */   public int index()
/*    */   {
/* 51 */     return this.index;
/*    */   }
/*    */ 
/*    */   public static IsoObjectType fromIndex(int value)
/*    */   {
/* 56 */     return ((IsoObjectType[])IsoObjectType.class.getEnumConstants())[value];
/*    */   }
/*    */ 
/*    */   public static IsoObjectType FromString(String str)
/*    */   {
/*    */     try
/*    */     {
/* 63 */       return valueOf(str);
/*    */     }
/*    */     catch (Exception ex) {
/*    */     }
/* 67 */     return MAX;
/*    */   }
/*    */ }

/* Location:           C:\Users\work\Desktop\Project Zomboid\
 * Qualified Name:     zombie.iso.SpriteDetails.IsoObjectType
 * JD-Core Version:    0.6.0
 */