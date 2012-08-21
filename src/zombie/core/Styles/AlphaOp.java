// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AlphaOp.java

package zombie.core.Styles;

import java.nio.FloatBuffer;
import org.lwjgl.util.ReadableColor;

public enum AlphaOp
/*    */ {
/* 13 */   PREMULTIPLY{
				protected int calc(ReadableColor c, int alpha){
					 float alpha00 = (float)(c.getAlpha() * alpha) * 0.003921569F;
		                float preMultAlpha00 = alpha00 * 0.003921569F;
		                return (int)((float)c.getRed() * preMultAlpha00) << 0 | (int)((float)c.getGreen() * preMultAlpha00) << 8 | (int)((float)c.getBlue() * preMultAlpha00) << 16 | (int)alpha00 << 24;
				}
}, 
/*    */ 
/* 21 */   KEEP{
	protected int calc(ReadableColor c, int alpha){
		 return c.getRed() << 0 | c.getGreen() << 8 | c.getBlue() << 16 | c.getAlpha() << 24;
	}
}, 
/*    */ 
/* 27 */   ZERO{
	protected int calc(ReadableColor c, int alpha){
		float alpha00 = (float)(c.getAlpha() * alpha) * 0.003921569F;
        float preMultAlpha00 = alpha00 * 0.003921569F;
        return (int)((float)c.getRed() * preMultAlpha00) << 0 | (int)((float)c.getGreen() * preMultAlpha00) << 8 | (int)((float)c.getBlue() * preMultAlpha00) << 16;
	}
};
/*    */ 
/*    */   private static final float PREMULT_ALPHA = 0.003921569F;
/*    */ 
/*    */   public final void op(ReadableColor c, int alpha, FloatBuffer dest)
/*    */   {
/* 39 */     dest.put(Float.intBitsToFloat(  calc(c, alpha)   ));
/*    */   }
/*    */ 
/*    */   public final void op(int c, int alpha, FloatBuffer dest)
/*    */   {
/* 44 */     dest.put(Float.intBitsToFloat(c));
/*    */   }
/*    */ 
/*    */   protected abstract int calc(ReadableColor paramReadableColor, int paramInt);



/*    */ }

    
    
    
    
    
    
    
    
    
    
    
    
    
   