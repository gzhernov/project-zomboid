/*    */ package zombie.Quests;
/*    */ 
/*    */ public enum QuestTaskType
/*    */ {
/* 13 */   GotoLocation, 
/* 14 */   TalkTo, 
/* 15 */   FindItem, 
/* 16 */   GiveItem, 
/* 17 */   UseItemOn, 
/* 18 */   Custom, 
/*    */ 
/* 20 */   MAX;
/*    */ 
/*    */   public static QuestTaskType FromString(String str)
/*    */   {
/* 25 */     if (str.equals("GotoLocation")) {
/* 26 */       return GotoLocation;
/*    */     }
/* 28 */     if (str.equals("TalkTo")) {
/* 29 */       return TalkTo;
/*    */     }
/* 31 */     if (str.equals("FindItem")) {
/* 32 */       return FindItem;
/*    */     }
/* 34 */     if (str.equals("GiveItem")) {
/* 35 */       return GiveItem;
/*    */     }
/* 37 */     if (str.equals("UseItemOn")) {
/* 38 */       return UseItemOn;
/*    */     }
/* 40 */     return MAX;
/*    */   }
/*    */ }

/* Location:           C:\Users\work\Desktop\Project Zomboid\
 * Qualified Name:     zombie.Quests.QuestTaskType
 * JD-Core Version:    0.6.0
 */