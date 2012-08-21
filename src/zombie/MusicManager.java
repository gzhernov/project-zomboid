/*    */ package zombie;
/*    */ 
/*    */ import java.util.EnumMap;
/*    */ import java.util.Stack;
/*    */ 
/*    */ public class MusicManager
/*    */ {
/* 27 */   public static EnumMap<Categories, Stack<String>> Choices = new EnumMap(Categories.class);
/*    */ 
/*    */   public static enum Categories
/*    */   {
/* 18 */     Intro, 
/* 19 */     Danger, 
/* 20 */     Sad, 
/* 21 */     Raider, 
/* 22 */     Action, 
/* 23 */     Ambient, 
/* 24 */     Max;
/*    */   }
/*    */ }

/* Location:           C:\Users\work\Desktop\Project Zomboid\
 * Qualified Name:     zombie.MusicManager
 * JD-Core Version:    0.6.0
 */