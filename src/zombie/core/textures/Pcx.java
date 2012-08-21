// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Pcx.java

package zombie.core.textures;

import gnu.trove.map.hash.THashMap;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import zombie.GameWindow;

public class Pcx
{
    class pcx_t
    {

        char bits_per_pixel;
        short bytes_per_line;
        char color_planes;
        byte data[];
        char encoding;
        byte filler[];
        short hres;
        short vres;
        char manufacturer;
        int palette[];
        short palette_type;
        char reserved;
        char version;
        short xmin;
        short ymin;
        short xmax;
        short ymax;
        

        pcx_t(byte raw[])
        {
           
            filler = new byte[58];
            palette = new int[48];
            manufacturer = (char)raw[0];
            version = (char)raw[1];
            encoding = (char)raw[2];
            bits_per_pixel = (char)raw[3];
            xmin = (short)(raw[4] + (raw[5] << 8) & 0xff);
            ymin = (short)(raw[6] + (raw[7] << 8) & 0xff);
            xmax = (short)(raw[8] + (raw[9] << 8) & 0xff);
            ymax = (short)(raw[10] + (raw[11] << 8) & 0xff);
            hres = (short)(raw[12] + (raw[13] << 8) & 0xff);
            vres = (short)(raw[14] + (raw[15] << 8) & 0xff);
            for(int i = 0; i < 48; i++)
                palette[i] = raw[16 + i] & 0xff;

            reserved = (char)raw[64];
            color_planes = (char)raw[65];
            bytes_per_line = (short)(raw[66] + (raw[67] << 8) & 0xff);
            palette_type = (short)(raw[68] + (raw[69] << 8) & 0xff);
            for(int i = 0; i < 58; i++)
                filler[i] = raw[70 + i];

            data = new byte[raw.length - 128];
            for(int i = 0; i < raw.length - 128; i++)
                data[i] = raw[128 + i];

        }
    }


    public Pcx(String file)
    {
    }

    public Pcx(URL url)
    {
    }

    public Pcx(String url, int urlPal[])
    {
    }

    public Pcx(String url, String urlPal)
    {
    }

    public Image getImage()
    {
        int pixel[] = new int[imageWidth * imageHeight];
        int z = 0;
        int w = 0;
        for(int i = 0; i < imageWidth; i++)
        {
            for(int j = 0; j < imageHeight; j++)
                pixel[z++] = 0xff000000 | (imageData[w++] & 0xff) << 16 | (imageData[w++] & 0xff) << 8 | imageData[w++] & 0xff;

        }

        Toolkit tk = Toolkit.getDefaultToolkit();
        return tk.createImage(new MemoryImageSource(imageWidth, imageHeight, pixel, 0, imageWidth));
    }

    int loadPCX(URL url)
    {
    	try
    	/*     */     {
    	/* 104 */       InputStream f = url.openStream();
    	/* 105 */       int len = f.available();
    	/* 106 */       byte[] buffer = new byte[len + 1];
    	/* 107 */       buffer[len] = 0;
    	/*     */ 
    	/* 109 */       for (int i = 0; i < len; i++) {
    	/* 110 */         buffer[i] = (byte)f.read();
    	/*     */       }
    	/* 112 */       f.close();
    	/*     */ 
    	/* 114 */       byte[] raw = buffer;
    	/*     */ 
    	/* 117 */       if (len == -1) {
    	/* 118 */         return -1;
    	/*     */       }
    	/*     */ 
    	/* 123 */       pcx_t pcx = new pcx_t(raw);
    	/* 124 */       raw = pcx.data;
    	/*     */ 
    	/* 126 */       if ((pcx.manufacturer != '\n') || (pcx.version != '\005') || (pcx.encoding != '\001') || (pcx.bits_per_pixel != '\b') || (pcx.xmax >= 640) || (pcx.ymax >= 480))
    	/*     */       {
    	/* 129 */         System.out.println("Bad pcx file " + url);
    	/*     */ 
    	/* 131 */         return -1;
    	/*     */       }
    	/*     */ 
    	/* 134 */       this.palette = new int[768];
    	/*     */ 
    	/* 136 */       for (int i = 0; i < 768; i++) {
    	/* 137 */         if (len - 128 - 768 + i < pcx.data.length)
    	/* 138 */           this.palette[i] = (pcx.data[(len - 128 - 768 + i)] & 0xFF);
    	/*     */       }
    	/* 140 */       this.imageWidth = (pcx.xmax + 1);
    	/* 141 */       this.imageHeight = (pcx.ymax + 1);
    	/*     */ 
    	/* 143 */       int[] out = new int[(pcx.ymax + 1) * (pcx.xmax + 1)];
    	/*     */ 
    	/* 145 */       this.pic = out;
    	/*     */ 
    	/* 147 */       int[] pix = out;
    	/*     */ 
    	/* 149 */       int pixcount = 0;
    	/* 150 */       int rawcount = 0;
    	/*     */ 
    	/* 152 */       for (int y = 0; y <= pcx.ymax; pixcount += pcx.xmax + 1)
    	/*     */       {
    	/* 155 */         for (int x = 0; x <= pcx.xmax; )
    	/*     */         {
    	/* 157 */           int dataByte = raw[(rawcount++)];
    	/*     */           int runLength;
    	/* 159 */           if ((dataByte & 0xC0) == 192)
    	/*     */           {
    	/* 161 */             runLength = dataByte & 0x3F;
    	/* 162 */             dataByte = raw[(rawcount++)];
    	/*     */           }
    	/*     */           else {
    	/* 165 */             runLength = 1;
    	/*     */           }
    	/* 167 */           while (runLength-- > 0)
    	/* 168 */             pix[(pixcount + x++)] = (dataByte & 0xFF);
    	/*     */         }
    	/* 152 */         y++;
    	/*     */       }
    	/*     */ 
    	/* 182 */       if ((this.pic == null) || (this.palette == null)) {
    	/* 183 */         return -1;
    	/*     */       }
    	/* 185 */       this.imageData = new byte[(this.imageWidth + 1) * (this.imageHeight + 1) * 3];
    	/*     */ 
    	/* 188 */       for (int k = 0; k < this.imageWidth * this.imageHeight; k++)
    	/*     */       {
    	/* 190 */         this.imageData[(k * 3)] = (byte)this.palette[(this.pic[k] * 3)];
    	/* 191 */         this.imageData[(k * 3 + 1)] = (byte)this.palette[(this.pic[k] * 3 + 1)];
    	/* 192 */         this.imageData[(k * 3 + 2)] = (byte)this.palette[(this.pic[k] * 3 + 2)];
    	/*     */       }
    	/*     */ 
    	/* 195 */       return 1;
    	/*     */     }
    	/*     */     catch (Exception e)
    	/*     */     {
    	/* 199 */       e.printStackTrace();
    	/*     */     }
    	/*     */ 
    	/* 202 */     return 1;
    }

    int loadPCXminusPal(String url)
    {
    	 try
    	 /*     */     {
    	 /* 211 */       if (Cache.containsKey(url))
    	 /*     */       {
    	 /* 213 */         Pcx pcx = (Pcx)Cache.get(url);
    	 /*     */ 
    	 /* 215 */         this.imageWidth = pcx.imageWidth;
    	 /* 216 */         this.imageHeight = pcx.imageHeight;
    	 /* 217 */         this.imageData = new byte[(pcx.imageWidth + 1) * (pcx.imageHeight + 1) * 3];
    	 /*     */ 
    	 /* 220 */         for (int k = 0; k < pcx.imageWidth * pcx.imageHeight; k++)
    	 /*     */         {
    	 /* 222 */           this.imageData[(k * 3)] = (byte)this.palette[(pcx.pic[k] * 3)];
    	 /* 223 */           this.imageData[(k * 3 + 1)] = (byte)this.palette[(pcx.pic[k] * 3 + 1)];
    	 /* 224 */           this.imageData[(k * 3 + 2)] = (byte)this.palette[(pcx.pic[k] * 3 + 2)];
    	 /*     */         }
    	 /*     */ 
    	 /* 227 */         return 1;
    	 /*     */       }
    	 /*     */ 
    	 /* 248 */       InputStream f = GameWindow.class.getClassLoader().getResourceAsStream(url);
    	 /*     */ 
    	 /* 250 */       if (f == null)
    	 /*     */       {
    	 /* 252 */         return 0;
    	 /*     */       }
    	 /*     */ 
    	 /* 255 */       int len = f.available();
    	 /* 256 */       byte[] buffer = new byte[len + 1];
    	 /* 257 */       buffer[len] = 0;
    	 /*     */ 
    	 /* 259 */       for (int i = 0; i < len; i++) {
    	 /* 260 */         buffer[i] = (byte)f.read();
    	 /*     */       }
    	 /* 262 */       f.close();
    	 /*     */ 
    	 /* 264 */       byte[] raw = buffer;
    	 /*     */ 
    	 /* 267 */       if (len == -1) {
    	 /* 268 */         return -1;
    	 /*     */       }
    	 /*     */ 
    	 /* 273 */       pcx_t pcx = new pcx_t(raw);
    	 /* 274 */       raw = pcx.data;
    	 /*     */ 
    	 /* 276 */       if ((pcx.manufacturer != '\n') || (pcx.version != '\005') || (pcx.encoding != '\001') || (pcx.bits_per_pixel != '\b') || (pcx.xmax >= 640) || (pcx.ymax >= 480))
    	 /*     */       {
    	 /* 279 */         System.out.println("Bad pcx file " + url);
    	 /*     */ 
    	 /* 281 */         return -1;
    	 /*     */       }
    	 /*     */ 
    	 /* 284 */       this.imageWidth = (pcx.xmax + 1);
    	 /* 285 */       this.imageHeight = (pcx.ymax + 1);
    	 /*     */ 
    	 /* 287 */       int[] out = new int[(pcx.ymax + 1) * (pcx.xmax + 1)];
    	 /*     */ 
    	 /* 289 */       this.pic = out;
    	 /*     */ 
    	 /* 291 */       int[] pix = out;
    	 /*     */ 
    	 /* 293 */       int pixcount = 0;
    	 /* 294 */       int rawcount = 0;
    	 /*     */ 
    	 /* 296 */       for (int y = 0; y <= pcx.ymax; pixcount += pcx.xmax + 1)
    	 /*     */       {
    	 /* 299 */         for (int x = 0; x <= pcx.xmax; )
    	 /*     */         {
    	 /* 301 */           int dataByte = raw[(rawcount++)];
    	 /*     */           int runLength;
    	 /* 303 */           if ((dataByte & 0xC0) == 192)
    	 /*     */           {
    	 /* 305 */             runLength = dataByte & 0x3F;
    	 /* 306 */             dataByte = raw[(rawcount++)];
    	 /*     */           }
    	 /*     */           else {
    	 /* 309 */             runLength = 1;
    	 /*     */           }
    	 /* 311 */           while (runLength-- > 0)
    	 /* 312 */             pix[(pixcount + x++)] = (dataByte & 0xFF);
    	 /*     */         }
    	 /* 296 */         y++;
    	 /*     */       }
    	 /*     */ 
    	 /* 326 */       if ((this.pic == null) || (this.palette == null)) {
    	 /* 327 */         return -1;
    	 /*     */       }
    	 /* 329 */       this.imageData = new byte[(this.imageWidth + 1) * (this.imageHeight + 1) * 3];
    	 /*     */ 
    	 /* 332 */       for (int k = 0; k < this.imageWidth * this.imageHeight; k++)
    	 /*     */       {
    	 /* 334 */         this.imageData[(k * 3)] = (byte)this.palette[(this.pic[k] * 3)];
    	 /* 335 */         this.imageData[(k * 3 + 1)] = (byte)this.palette[(this.pic[k] * 3 + 1)];
    	 /* 336 */         this.imageData[(k * 3 + 2)] = (byte)this.palette[(this.pic[k] * 3 + 2)];
    	 /*     */       }
    	 /*     */ 
    	 /* 339 */       Cache.put(url, this);
    	 /*     */ 
    	 /* 341 */       return 1;
    	 /*     */     }
    	 /*     */     catch (Exception e)
    	 /*     */     {
    	 /* 345 */       e.printStackTrace();
    	 /*     */     }
    	 /*     */ 
    	 /* 348 */     return 1;
    }

    int loadPCXpal(String url)
    {
    	try
    	/*     */     {
    	/* 376 */       InputStream f = GameWindow.class.getClassLoader().getResourceAsStream(url);
    	/*     */ 
    	/* 378 */       if (f == null) {
    	/* 379 */         return 1;
    	/*     */       }
    	/* 381 */       int len = f.available();
    	/* 382 */       byte[] buffer = new byte[len + 1];
    	/* 383 */       buffer[len] = 0;
    	/*     */ 
    	/* 385 */       for (int i = 0; i < len; i++) {
    	/* 386 */         buffer[i] = (byte)f.read();
    	/*     */       }
    	/* 388 */       f.close();
    	/*     */ 
    	/* 390 */       byte[] raw = buffer;
    	/*     */ 
    	/* 392 */       if (len == -1) {
    	/* 393 */         return -1;
    	/*     */       }
    	/*     */ 
    	/* 398 */       pcx_t pcx = new pcx_t(raw);
    	/* 399 */       raw = pcx.data;
    	/*     */ 
    	/* 401 */       if ((pcx.manufacturer != '\n') || (pcx.version != '\005') || (pcx.encoding != '\001') || (pcx.bits_per_pixel != '\b') || (pcx.xmax >= 640) || (pcx.ymax >= 480))
    	/*     */       {
    	/* 404 */         System.out.println("Bad pcx file " + url);
    	/*     */ 
    	/* 406 */         return -1;
    	/*     */       }
    	/*     */ 
    	/* 409 */       this.palette = new int[768];
    	/*     */ 
    	/* 411 */       for (int i = 0; i < 768; i++) {
    	/* 412 */         if (len - 128 - 768 + i < pcx.data.length)
    	/* 413 */           this.palette[i] = (pcx.data[(len - 128 - 768 + i)] & 0xFF);
    	/*     */       }
    	/* 415 */       this.imageWidth = (pcx.xmax + 1);
    	/* 416 */       this.imageHeight = (pcx.ymax + 1);
    	/*     */ 
    	/* 418 */       int[] out = new int[(pcx.ymax + 1) * (pcx.xmax + 1)];
    	/*     */ 
    	/* 420 */       this.pic = out;
    	/*     */ 
    	/* 422 */       int[] pix = out;
    	/*     */ 
    	/* 424 */       int pixcount = 0;
    	/* 425 */       int rawcount = 0;
    	/*     */ 
    	/* 456 */       if ((this.pic == null) || (this.palette == null)) {
    	/* 457 */         return -1;
    	/*     */       }
    	/*     */ 
    	/* 470 */       return 1;
    	/*     */     }
    	/*     */     catch (Exception e)
    	/*     */     {
    	/* 474 */       e.printStackTrace();
    	/*     */     }
    	/*     */ 
    	/* 477 */     return 1;
    }

    private void loadPCXpal(int urlPal[])
    {
        palette = urlPal;
    }

    public static THashMap Cache = new THashMap();
    public byte imageData[];
    public int imageWidth;
    public int imageHeight;
    public int palette[];
    public int pic[];

}
