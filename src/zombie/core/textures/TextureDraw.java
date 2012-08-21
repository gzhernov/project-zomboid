/*     */ package zombie.core.textures;
/*     */ 
/*     */ import zombie.IndieGL;
/*     */ import zombie.core.Core;
/*     */ 
/*     */ public class TextureDraw
/*     */ {
/* 264 */   public Type type = Type.glDraw;
/*     */ 
/* 266 */   public int a = 0;
/* 267 */   public int b = 0;
/* 268 */   public float f1 = 0.0F;
/* 269 */   public int c = 0;
/* 270 */   public int[] col = new int[4];
/* 271 */   public short[] x = new short[4];
/* 272 */   public short[] y = new short[4];
/* 273 */   public float[] u = new float[4];
/* 274 */   public float[] v = new float[4];
/*     */   public Texture tex;
/* 276 */   public boolean bSingleCol = false;
/*     */ 
/* 355 */   public boolean flipped = false;
/*     */ 
/*     */   public static void Create(TextureDraw texd, Texture tex, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2, float r3, float g3, float b3, float a3, float r4, float g4, float b4, float a4)
/*     */   {
/*  22 */     texd.bSingleCol = false;
/*  23 */     if (r1 > 1.0F)
/*  24 */       r1 = 1.0F;
/*  25 */     if (g1 > 1.0F)
/*  26 */       g1 = 1.0F;
/*  27 */     if (b1 > 1.0F)
/*  28 */       b1 = 1.0F;
/*  29 */     if (a1 > 1.0F) {
/*  30 */       a1 = 1.0F;
/*     */     }
/*  32 */     if (r2 > 1.0F)
/*  33 */       r2 = 1.0F;
/*  34 */     if (g2 > 1.0F)
/*  35 */       g2 = 1.0F;
/*  36 */     if (b2 > 1.0F)
/*  37 */       b2 = 1.0F;
/*  38 */     if (a2 > 1.0F)
/*  39 */       a2 = 1.0F;
/*  40 */     if (r3 > 1.0F)
/*  41 */       r3 = 1.0F;
/*  42 */     if (g3 > 1.0F)
/*  43 */       g3 = 1.0F;
/*  44 */     if (b3 > 1.0F)
/*  45 */       b3 = 1.0F;
/*  46 */     if (a3 > 1.0F) {
/*  47 */       a3 = 1.0F;
/*     */     }
/*  49 */     if (r4 > 1.0F)
/*  50 */       r4 = 1.0F;
/*  51 */     if (g4 > 1.0F)
/*  52 */       g4 = 1.0F;
/*  53 */     if (b4 > 1.0F)
/*  54 */       b4 = 1.0F;
/*  55 */     if (a4 > 1.0F) {
/*  56 */       a4 = 1.0F;
/*     */     }
/*  58 */     texd.tex = tex;
/*  59 */     texd.x[0] = (short)x1;
/*  60 */     texd.y[0] = (short)y1;
/*  61 */     texd.x[1] = (short)x2;
/*  62 */     texd.y[1] = (short)y2;
/*  63 */     texd.x[2] = (short)x3;
/*  64 */     texd.y[2] = (short)y3;
/*  65 */     texd.x[3] = (short)x4;
/*  66 */     texd.y[3] = (short)y4;
/*     */ 
/*  68 */     texd.col[0] = ((int)(r1 * 255.0F) << 0 | (int)(g1 * 255.0F) << 8 | (int)(b1 * 255.0F) << 16 | (int)(a1 * 255.0F) << 24);
/*  69 */     texd.col[1] = ((int)(r2 * 255.0F) << 0 | (int)(g2 * 255.0F) << 8 | (int)(b2 * 255.0F) << 16 | (int)(a2 * 255.0F) << 24);
/*  70 */     texd.col[2] = ((int)(r3 * 255.0F) << 0 | (int)(g3 * 255.0F) << 8 | (int)(b3 * 255.0F) << 16 | (int)(a3 * 255.0F) << 24);
/*  71 */     texd.col[3] = ((int)(r4 * 255.0F) << 0 | (int)(g4 * 255.0F) << 8 | (int)(b4 * 255.0F) << 16 | (int)(a4 * 255.0F) << 24);
/*     */   }
/*     */ 
/*     */   public static void Create(TextureDraw texd, Texture tex, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, int c1, int c2, int c3, int c4)
/*     */   {
/*  97 */     texd.bSingleCol = false;
/*     */ 
/*  99 */     texd.tex = tex;
/* 100 */     texd.x[0] = (short)x1;
/* 101 */     texd.y[0] = (short)y1;
/* 102 */     texd.x[1] = (short)x2;
/* 103 */     texd.y[1] = (short)y2;
/* 104 */     texd.x[2] = (short)x3;
/* 105 */     texd.y[2] = (short)y3;
/* 106 */     texd.x[3] = (short)x4;
/* 107 */     texd.y[3] = (short)y4;
/*     */ 
/* 109 */     texd.col[0] = c1;
/* 110 */     texd.col[1] = c2;
/* 111 */     texd.col[2] = c3;
/* 112 */     texd.col[3] = c4;
/*     */   }
/*     */ 
/*     */   public static void glStencilFunc(TextureDraw texd, int a, int b, int c)
/*     */   {
/* 139 */     texd.type = Type.glStencilFunc;
/* 140 */     texd.a = a;
/* 141 */     texd.b = b;
/* 142 */     texd.c = c;
/*     */   }
/*     */ 
/*     */   public static void glStencilOp(TextureDraw texd, int a, int b, int c)
/*     */   {
/* 148 */     texd.type = Type.glStencilOp;
/* 149 */     texd.a = a;
/* 150 */     texd.b = b;
/* 151 */     texd.c = c;
/*     */   }
/*     */ 
/*     */   public static void glDisable(TextureDraw texd, int a)
/*     */   {
/* 156 */     texd.type = Type.glDisable;
/* 157 */     texd.a = a;
/*     */   }
/*     */ 
/*     */   public static void glClear(TextureDraw texd, int a)
/*     */   {
/* 163 */     texd.type = Type.glClear;
/* 164 */     texd.a = a;
/*     */   }
/*     */ 
/*     */   public static void glEnable(TextureDraw texd, int a)
/*     */   {
/* 169 */     texd.type = Type.glEnable;
/* 170 */     texd.a = a;
/*     */   }
/*     */ 
/*     */   public static void glAlphaFunc(TextureDraw texd, int a, float b)
/*     */   {
/* 176 */     texd.type = Type.glAlphaFunc;
/* 177 */     texd.a = a;
/* 178 */     texd.f1 = b;
/*     */   }
/*     */ 
/*     */   public static void glColorMask(TextureDraw texd, int a, int b, int c, int d)
/*     */   {
/* 183 */     texd.type = Type.glColorMask;
/* 184 */     texd.a = a;
/* 185 */     texd.b = b;
/* 186 */     texd.c = c;
/* 187 */     texd.x[0] = (short)d;
/*     */   }
/*     */ 
/*     */   public static void glStencilMask(TextureDraw texd, int a)
/*     */   {
/* 192 */     texd.type = Type.glStencilMask;
/* 193 */     texd.a = a;
/*     */   }
/*     */ 
/*     */   public static void glBlendFunc(TextureDraw texd, int a, float b) {
/* 197 */     texd.type = Type.glBlendFunc;
/* 198 */     texd.a = a;
/* 199 */     texd.b = a;
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 205 */     switch (this.type.ordinal())
/*     */     {
/*     */     case 1:
/* 209 */       IndieGL.glClearA(this.a);
/* 210 */       break;
/*     */     case 2:
/* 212 */       IndieGL.glBlendFuncA(this.a, this.b);
/* 213 */       break;
/*     */     case 3:
/* 216 */       break;
/*     */     case 4:
/* 218 */       IndieGL.glStencilMaskA(this.a);
/* 219 */       break;
/*     */     case 5:
/* 221 */       IndieGL.glStencilFuncA(this.a, this.b, this.c);
/* 222 */       break;
/*     */     case 6:
/* 224 */       IndieGL.glStencilOpA(this.a, this.b, this.c);
/* 225 */       break;
/*     */     case 7:
/* 227 */       IndieGL.glAlphaFuncA(this.a, this.f1);
/* 228 */       break;
/*     */     case 8:
/* 230 */       IndieGL.glEnableA(this.a);
/* 231 */       break;
/*     */     case 9:
/* 233 */       IndieGL.glDisableA(this.a);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isOnScreen()
/*     */   {
/* 240 */     int w = Core.getInstance().getOffscreenWidth();
/* 241 */     int h = Core.getInstance().getOffscreenHeight();
/* 242 */     if ((this.x[0] < 0) && (this.x[1] < 0) && (this.x[2] < 0) && (this.x[3] < 0))
/* 243 */       return false;
/* 244 */     if ((this.y[0] < 0) && (this.y[1] < 0) && (this.y[2] < 0) && (this.y[3] < 0))
/* 245 */       return false;
/* 246 */     if ((this.x[0] > w) && (this.x[1] > w) && (this.x[2] > w) && (this.x[3] > w)) {
/* 247 */       return false;
/*     */     }
/* 249 */     return (this.y[0] <= h) || (this.y[1] <= h) || (this.y[2] <= h) || (this.y[3] <= h);
/*     */   }
/*     */ 
/*     */   public static TextureDraw Create(TextureDraw texd, Texture tex, int x, int y, int width, int height, float r, float g, float b, float a)
/*     */   {
/* 279 */     texd.bSingleCol = true;
/* 280 */     texd.tex = tex;
/*     */     short tmp22_21 = (short)x; texd.x[3] = tmp22_21; texd.x[0] = tmp22_21;
/*     */     short tmp37_36 = (short)y; texd.y[1] = tmp37_36; texd.y[0] = tmp37_36;
/*     */     short tmp55_54 = (short)(x + width); texd.x[2] = tmp55_54; texd.x[1] = tmp55_54;
/*     */     short tmp73_72 = (short)(y + height); texd.y[3] = tmp73_72; texd.y[2] = tmp73_72;
/*     */     int tmp138_137 = (texd.col[2] = texd.col[3] = (int)(r * 255.0F) << 0 | (int)(g * 255.0F) << 8 | (int)(b * 255.0F) << 16 | (int)(a * 255.0F) << 24); texd.col[1] = tmp138_137; texd.col[0] = tmp138_137;
/*     */ 
/* 288 */     if (tex != null)
/*     */     {
/* 290 */       texd.u[0] = tex.getXStart();
/* 291 */       texd.u[1] = tex.getXEnd();
/* 292 */       texd.u[2] = tex.getXEnd();
/* 293 */       texd.u[3] = tex.getXStart();
/* 294 */       texd.v[0] = tex.getYStart();
/* 295 */       texd.v[1] = tex.getYStart();
/* 296 */       texd.v[2] = tex.getYEnd();
/* 297 */       texd.v[3] = tex.getYEnd();
/*     */     }
/*     */ 
/* 300 */     return texd;
/*     */   }
/*     */ 
/*     */   public static TextureDraw Create(TextureDraw texd, Texture tex, int x, int y, int width, int height, float r, float g, float b, float a, float u1, float v1, float u2, float v2, float u3, float v3, float u4, float v4) {
/* 304 */     texd.bSingleCol = true;
/* 305 */     texd.tex = tex;
/*     */     short tmp22_21 = (short)x; texd.x[3] = tmp22_21; texd.x[0] = tmp22_21;
/*     */     short tmp37_36 = (short)y; texd.y[1] = tmp37_36; texd.y[0] = tmp37_36;
/*     */     short tmp55_54 = (short)(x + width); texd.x[2] = tmp55_54; texd.x[1] = tmp55_54;
/*     */     short tmp73_72 = (short)(y + height); texd.y[3] = tmp73_72; texd.y[2] = tmp73_72;
/*     */     int tmp138_137 = (texd.col[2] = texd.col[3] = (int)(r * 255.0F) << 0 | (int)(g * 255.0F) << 8 | (int)(b * 255.0F) << 16 | (int)(a * 255.0F) << 24); texd.col[1] = tmp138_137; texd.col[0] = tmp138_137;
/*     */ 
/* 313 */     if (tex != null)
/*     */     {
/* 315 */       texd.u[0] = u1;
/* 316 */       texd.u[1] = u2;
/* 317 */       texd.u[2] = u3;
/* 318 */       texd.u[3] = u4;
/* 319 */       texd.v[0] = v1;
/* 320 */       texd.v[1] = v2;
/* 321 */       texd.v[2] = v3;
/* 322 */       texd.v[3] = v4;
/*     */     }
/*     */ 
/* 325 */     return texd;
/*     */   }
/*     */ 
/*     */   public static TextureDraw Create(TextureDraw texd, Texture tex, int x, int y, int width, int height, int c) {
/* 329 */     texd.bSingleCol = true;
/* 330 */     texd.tex = tex;
/*     */     short tmp22_21 = (short)x; texd.x[3] = tmp22_21; texd.x[0] = tmp22_21;
/*     */     short tmp37_36 = (short)y; texd.y[1] = tmp37_36; texd.y[0] = tmp37_36;
/*     */     short tmp55_54 = (short)(x + width); texd.x[2] = tmp55_54; texd.x[1] = tmp55_54;
/*     */     short tmp73_72 = (short)(y + height); texd.y[3] = tmp73_72; texd.y[2] = tmp73_72;
/*     */ 
/* 336 */     texd.col[0] = c;
/* 337 */     texd.col[1] = c;
/* 338 */     texd.col[2] = c;
/* 339 */     texd.col[3] = c;
/*     */ 
/* 341 */     if (tex != null)
/*     */     {
/* 343 */       texd.u[0] = tex.getXStart();
/* 344 */       texd.u[1] = tex.getXEnd();
/* 345 */       texd.u[2] = tex.getXEnd();
/* 346 */       texd.u[3] = tex.getXStart();
/* 347 */       texd.v[0] = tex.getYStart();
/* 348 */       texd.v[1] = tex.getYStart();
/* 349 */       texd.v[2] = tex.getYEnd();
/* 350 */       texd.v[3] = tex.getYEnd();
/*     */     }
/* 352 */     return texd;
/*     */   }
/*     */ 
/*     */   public int getColor(int i)
/*     */   {
/* 359 */     if (this.bSingleCol) {
/* 360 */       return this.col[0];
/*     */     }
/* 362 */     if (i == 0)
/* 363 */       return this.col[0];
/* 364 */     if (i == 1)
/* 365 */       return this.col[1];
/* 366 */     if (i == 2)
/* 367 */       return this.col[2];
/* 368 */     if (i == 3) {
/* 369 */       return this.col[3];
/*     */     }
/* 371 */     return this.col[0];
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 376 */     this.type = Type.glDraw;
/* 377 */     this.flipped = false;
/* 378 */     this.tex = null;
/* 379 */     this.col[0] = -1;
/* 380 */     this.col[1] = -1;
/* 381 */     this.col[2] = -1;
/* 382 */     this.col[3] = -1;
/* 383 */     this.bSingleCol = true;
/*     */     int tmp103_102 = ( this.x[2] = this.x[3] = this.y[0] = this.y[1] = this.y[2] = this.y[3] = 0);

			 this.x[1] = (short) tmp103_102;
			 this.x[0] = (short) tmp103_102;
/*     */   }
/*     */ 
/*     */   public static enum Type
/*     */   {
/* 256 */     glDraw, 
/* 257 */     glStencilFunc, 
/* 258 */     glAlphaFunc, 
/* 259 */     glStencilOp, 
/* 260 */     glEnable, 
/* 261 */     glDisable, glColorMask, 
/* 262 */     glStencilMask, glClear, glBlendFunc;
/*     */   }
/*     */ }

/* Location:           C:\Users\work\Desktop\Project Zomboid\
 * Qualified Name:     zombie.core.textures.TextureDraw
 * JD-Core Version:    0.6.0
 */