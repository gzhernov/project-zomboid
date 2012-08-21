/*    */ package zombie.ui;
/*    */ 
/*    */ public enum UIFont
/*    */ {
/* 12 */   Small, Medium, Large, Massive, MainMenu1, MainMenu2;
/*    */ 
/*    */   public static UIFont FromString(String str)
/*    */   {
/*    */     try
/*    */     {
/* 19 */       return valueOf(str);
/*    */     }
/*    */     catch (Exception ex) {
/*    */     }
/* 23 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Users\work\Desktop\Project Zomboid\
 * Qualified Name:     zombie.ui.UIFont
 * JD-Core Version:    0.6.0
 */