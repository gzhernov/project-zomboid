/*     */ package zombie.iso;
/*     */ 
/*     */ public enum IsoDirections
/*     */ {
/*  10 */   N(0), 
/*  11 */   NW(1), 
/*  12 */   W(2), 
/*  13 */   SW(3), 
/*  14 */   S(4), 
/*  15 */   SE(5), 
/*  16 */   E(6), 
/*  17 */   NE(7), 
/*  18 */   Max(8);
/*     */ 
/*     */   private final int index;
/*     */   static IsoDirections[][] directionLookup;
/*     */   static Vector2 temp;
/*     */ 
/*  24 */   private IsoDirections(int index) { this.index = index;
/*     */   }
/*     */ 
/*     */   public static IsoDirections fromIndex(int index)
/*     */   {
/*  29 */     index %= 8;
/*  30 */     if (index < 0) {
/*  31 */       index += 8;
/*     */     }
/*  33 */     switch (index)
/*     */     {
/*     */     case 0:
/*  37 */       return N;
/*     */     case 1:
/*  40 */       return NW;
/*     */     case 2:
/*  43 */       return W;
/*     */     case 3:
/*  46 */       return SW;
/*     */     case 4:
/*  49 */       return S;
/*     */     case 5:
/*  52 */       return SE;
/*     */     case 6:
/*  55 */       return E;
/*     */     case 7:
/*  58 */       return NE;
/*     */     }
/*     */ 
/*  61 */     return Max;
/*     */   }
/*     */ 
/*     */   public static IsoDirections RotLeft(IsoDirections dir)
/*     */   {
/*  66 */     switch (dir.ordinal())
/*     */     {
/*     */     case 1:
/*  70 */       return N;
/*     */     case 2:
/*  73 */       return NW;
/*     */     case 3:
/*  76 */       return W;
/*     */     case 4:
/*  79 */       return SW;
/*     */     case 5:
/*  82 */       return S;
/*     */     case 6:
/*  85 */       return SE;
/*     */     case 7:
/*  88 */       return E;
/*     */     case 8:
/*  91 */       return NE;
/*     */     }
/*     */ 
/*  94 */     return Max;
/*     */   }
/*     */ 
/*     */   public static IsoDirections RotRight(IsoDirections dir)
/*     */   {
/*  99 */     switch (dir.ordinal())
/*     */     {
/*     */     case 2:
/* 103 */       return NE;
/*     */     case 1:
/* 106 */       return E;
/*     */     case 8:
/* 109 */       return SE;
/*     */     case 7:
/* 112 */       return S;
/*     */     case 6:
/* 115 */       return SW;
/*     */     case 5:
/* 118 */       return W;
/*     */     case 4:
/* 121 */       return NW;
/*     */     case 3:
/* 124 */       return N;
/*     */     }
/*     */ 
/* 127 */     return Max;
/*     */   }
/*     */ 
/*     */   public static void generateTables()
/*     */   {
/* 133 */     directionLookup = new IsoDirections[200][200];
/* 134 */     for (int x = 0; x < 200; x++)
/*     */     {
/* 136 */       for (int y = 0; y < 200; y++)
/*     */       {
/* 138 */         int ux = x - 100;
/* 139 */         int uy = y - 100;
/* 140 */         float fx = ux / 100.0F;
/* 141 */         float fy = uy / 100.0F;
/*     */ 
/* 143 */         Vector2 vec = new Vector2(fx, fy);
/* 144 */         vec.normalize();
/* 145 */         directionLookup[x][y] = fromAngleActual(vec);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static IsoDirections fromAngleActual(Vector2 angle)
/*     */   {
/* 154 */     temp.x = angle.x;
/* 155 */     temp.y = angle.y;
/*     */ 
/* 157 */     temp.normalize();
/*     */ 
/* 162 */     float dir = temp.getDirection();
/*     */ 
/* 164 */     float totalRad = 0.7853982F;
/* 165 */     float radCheck = 6.283186F;
/*     */ 
/* 167 */     radCheck = (float)(radCheck + Math.toRadians(112.5D));
/*     */ 
/* 169 */     for (int n = 0; n < 8; n++)
/*     */     {
/* 171 */       radCheck += totalRad;
/*     */ 
/* 173 */       if ( ((dir >= radCheck) && (dir <= radCheck + totalRad)) 
                 || ((dir + 6.283186F >= radCheck) && (dir + 6.283186F <= radCheck + totalRad)) 
                 || ((dir - 6.283186F >= radCheck) && (dir - 6.283186F <= radCheck + totalRad)))
/*     */       {
/* 177 */         return fromIndex(n);
/*     */       }
/*     */ 
/* 180 */       if (radCheck > 6.283185307179586D) {
/* 181 */         radCheck = (float)(radCheck - 6.283185307179586D);
/*     */       }
/*     */     }
/* 184 */     if (temp.x > 0.5F)
/*     */     {
/* 187 */       if (temp.y < -0.5F)
/*     */       {
/* 189 */         return NE;
/*     */       }
/*     */ 
/* 192 */       if (temp.y > 0.5F)
/*     */       {
/* 194 */         return SE;
/*     */       }
/*     */ 
/* 200 */       return E;
/*     */     }
/*     */ 
/* 204 */     if (temp.x < -0.5F)
/*     */     {
/* 207 */       if (temp.y < -0.5F)
/*     */       {
/* 209 */         return NW;
/*     */       }
/*     */ 
/* 212 */       if (temp.y > 0.5F)
/*     */       {
/* 214 */         return SW;
/*     */       }
/*     */ 
/* 219 */       return W;
/*     */     }
/*     */ 
/* 225 */     if (temp.y < -0.5F)
/*     */     {
/* 227 */       return N;
/*     */     }
/*     */ 
/* 230 */     if (temp.y > 0.5F)
/*     */     {
/* 232 */       return S;
/*     */     }
/*     */ 
/* 239 */     return N;
/*     */   }
/*     */ 
/*     */   public int index()
/*     */   {
/* 244 */     return this.index % 8;
/*     */   }
/*     */ 
/*     */   public static IsoDirections fromAngle(Vector2 angle)
/*     */   {
/* 251 */     temp.x = angle.x;
/* 252 */     temp.y = angle.y;
/* 253 */     if (angle.getLength() > 1.0F)
/* 254 */       temp.normalize();
/* 255 */     if (directionLookup == null) {
/* 256 */       generateTables();
/*     */     }
/* 258 */     int x = (int)((temp.x + 1.0F) * 100.0F);
/* 259 */     int y = (int)((temp.y + 1.0F) * 100.0F);
/* 260 */     if (x >= 200)
/* 261 */       x = 199;
/* 262 */     if (y >= 200)
/* 263 */       y = 199;
/* 264 */     if (x < 0)
/* 265 */       x = 0;
/* 266 */     if (y < 0) {
/* 267 */       y = 0;
/*     */     }
/* 269 */     return directionLookup[x][y];
/*     */   }
/*     */ 
/*     */   public static IsoDirections reverse(IsoDirections dir)
/*     */   {
/* 274 */     switch (dir.ordinal())
/*     */     {
/*     */     case 6:
/* 278 */       return N;
/*     */     case 7:
/* 281 */       return NW;
/*     */     case 8:
/* 284 */       return W;
/*     */     case 1:
/* 287 */       return SW;
/*     */     case 2:
/* 290 */       return S;
/*     */     case 3:
/* 293 */       return SE;
/*     */     case 4:
/* 296 */       return E;
/*     */     case 5:
/* 299 */       return NE;
/*     */     }
/*     */ 
/* 302 */     return Max;
/*     */   }
/*     */ 
/*     */   public String toCompassString()
/*     */   {
/* 309 */     switch (this.index)
/*     */     {
/*     */     case 0:
/* 313 */       return "9";
/*     */     case 1:
/* 316 */       return "8";
/*     */     case 2:
/* 319 */       return "7";
/*     */     case 3:
/* 322 */       return "4";
/*     */     case 4:
/* 325 */       return "1";
/*     */     case 5:
/* 328 */       return "2";
/*     */     case 6:
/* 331 */       return "3";
/*     */     case 7:
/* 334 */       return "6";
/*     */     }
/*     */ 
/* 337 */     return "";
/*     */   }
/*     */ 
/*     */   public Vector2 ToVector()
/*     */   {
/* 343 */     switch (this.ordinal())
/*     */     {
/*     */     case 6:
/* 347 */       temp.x = 0.0F;
/* 348 */       temp.y = 1.0F;
/*     */     case 7:
/* 352 */       temp.x = 1.0F;
/* 353 */       temp.y = 1.0F;
/*     */     case 8:
/* 356 */       temp.x = 1.0F;
/*     */     case 1:
/* 359 */       temp.x = 1.0F;
/* 360 */       temp.y = -1.0F;
/*     */     case 2:
/* 364 */       temp.x = 0.0F;
/* 365 */       temp.y = -1.0F;
/*     */     case 3:
/* 369 */       temp.x = -1.0F;
/* 370 */       temp.y = -1.0F;
/*     */     case 4:
/* 373 */       temp.x = -1.0F;
/*     */     case 5:
/* 376 */       temp.x = -1.0F;
/* 377 */       temp.y = 1.0F;
/*     */     }
/*     */ 
/* 380 */     temp.normalize();
/* 381 */     return temp;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 247 */     temp = new Vector2();
/*     */   }
/*     */ }

/* Location:           C:\Users\work\Desktop\Project Zomboid\
 * Qualified Name:     zombie.iso.IsoDirections
 * JD-Core Version:    0.6.0
 */