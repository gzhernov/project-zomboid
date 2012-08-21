// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AngelCodeFont.java

package zombie.core.fonts;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import zombie.GameApplet;
import zombie.GameWindow;
import zombie.core.Color;
import zombie.core.SpriteRenderer;
import zombie.core.textures.Texture;

// Referenced classes of package zombie.core.fonts:
//            Font

public class AngelCodeFont    implements Font
{
    private static class DisplayList
    {

        Short height;
        int id;
        String text;
        Short width;
        Short yOffset;

        private DisplayList()
        {
        }

    }

    private class CharDef
    {

        public void draw(float x, float y)
        {
            Texture tex = image;
            SpriteRenderer.instance.render(tex, (int)(x + (float)xoffset + (float)xoff), (int)(y + (float)yoffset + (float)yoff), width, height, AngelCodeFont.curCol.r, AngelCodeFont.curCol.g, AngelCodeFont.curCol.b, AngelCodeFont.curCol.a);
        }

        public int getKerning(int otherCodePoint)
        {
            if(kerning == null)
                return 0;
            int low = 0;
            for(int high = kerning.length - 1; low <= high;)
            {
                int midIndex = low + high >>> 1;
                int value = kerning[midIndex];
                int foundCodePoint = value & 0xff;
                if(foundCodePoint < otherCodePoint)
                    low = midIndex + 1;
                else
                if(foundCodePoint > otherCodePoint)
                    high = midIndex - 1;
                else
                    return value >> 8;
            }

            return 0;
        }

        public void init()
        {
            image = new Texture(fontImage.getTextureId(), (new StringBuilder()).append(fontImage.getName()).append("_").append(x).append("_").append(y).toString());
            image.setRegion(x + (int)(fontImage.xStart * (float)fontImage.getWidthHW()), y + (int)(fontImage.yStart * (float)fontImage.getHeightHW()), width, height);
        }

        public String toString()
        {
            return (new StringBuilder()).append("[CharDef id=").append(id).append(" x=").append(x).append(" y=").append(y).append("]").toString();
        }

        public short dlIndex;
        public short height;
        public short id;
        public Texture image;
        public short kerning[];
        public short width;
        public short x;
        public short xadvance;
        public short xoffset;
        public short y;
        public short yoffset;
        

        private CharDef()
        {

        }

    }


    
    
    public AngelCodeFont(String fntFile, Texture image)
    {
    	/* 114 */     this.fontImage = image;
    	/*     */ 
    	/* 116 */     String path = fntFile;
    	/* 117 */     InputStream is = null;
    	/*     */ 
    	/* 121 */     if (path.startsWith("/"))
    	/*     */     {
    	/* 123 */       path = path.substring(1);
    	/*     */     }
    	/*     */     int index;
    	/* 128 */     while ((index = path.indexOf("\\")) != -1)
    	/*     */     {
    	/* 130 */       path = path.substring(0, index) + '/' + path.substring(index + 1);
    	/*     */     }
    	/*     */ 
    	/* 133 */     URL url = GameWindow.class.getClassLoader().getResource(path);
    	/*     */ 
    	/* 135 */     if (url != null)
    	/*     */     {
    	/*     */       try
    	/*     */       {
    	/* 140 */         is = url.openStream();
    	/*     */       }
    	/*     */       catch (IOException ex)
    	/*     */       {
    	/* 144 */         Logger.getLogger(GameApplet.class.getName()).log(Level.SEVERE, null, ex);
    	/*     */       }
    	/*     */ 
    	/*     */     }
    	/*     */ 
    	/* 153 */     parseFnt(is);
    }

    public AngelCodeFont(String fntFile, String imgFile)        throws FileNotFoundException
    {
    	this.fontImage = Texture.getSharedTexture(imgFile);
    	/*     */ 
    	/* 173 */     String path = fntFile;
    	/* 174 */     InputStream is = null;
    	/*     */ 
    	/* 178 */     if (path.startsWith("/"))
    	/*     */     {
    	/* 180 */       path = path.substring(1);
    	/*     */     }
    	/*     */     int index;
    	/* 185 */     while ((index = path.indexOf("\\")) != -1)
    	/*     */     {
    	/* 187 */       path = path.substring(0, index) + '/' + path.substring(index + 1);
    	/*     */     }
    	/*     */ 
    	/* 190 */     is = new FileInputStream(path);
    	/*     */ 
    	/* 194 */     parseFnt(is);
    }

    public void drawString(float x, float y, String text)
    {
        drawString(x, y, text, Color.white);
    }

    public void drawString(float x, float y, String text, Color col)
    {
        drawString(x, y, text, col, 0, text.length() - 1);
    }

    public void drawString(float x, float y, String text, Color col, int startIndex, int endIndex)
    {
        xoff = (int)x;
        yoff = (int)y;
        curCol = col;
        Texture.lr = col.r;
        Texture.lg = col.g;
        Texture.lb = col.b;
        Texture.la = col.a;
        if(displayListCaching && startIndex == 0 && endIndex == text.length() - 1)
        {
            DisplayList displayList = (DisplayList)displayLists.get(text);
            if(displayList != null)
            {
                GL11.glCallList(displayList.id);
            } else
            {
                displayList = new DisplayList();
                displayList.text = text;
                int displayListCount = displayLists.size();
                if(displayListCount < 200)
                {
                    displayList.id = baseDisplayListID + displayListCount;
                } else
                {
                    displayList.id = eldestDisplayListID;
                    displayLists.remove(eldestDisplayList.text);
                }
                displayLists.put(text, displayList);
                GL11.glNewList(displayList.id, 4865);
                render(text, startIndex, endIndex);
                GL11.glEndList();
            }
        } else
        {
            render(text, startIndex, endIndex);
        }
    }

    public int getHeight(String text)
    {
        DisplayList displayList = null;
        if(displayListCaching)
        {
            displayList = (DisplayList)displayLists.get(text);
            if(displayList != null && displayList.height != null)
                return displayList.height.intValue();
        }
        int lines = 0;
        int maxHeight = 0;
        for(int i = 0; i < text.length(); i++)
        {
            int id = text.charAt(i);
            if(id == 10)
            {
                lines++;
                maxHeight = 0;
                continue;
            }
            if(id == 32 || id >= chars.length)
                continue;
            CharDef charDef = chars[id];
            if(charDef != null)
                maxHeight = Math.max(charDef.height + charDef.yoffset, maxHeight);
        }

        maxHeight += lines * getLineHeight();
        if(displayList != null)
            displayList.height = new Short((short)maxHeight);
        return maxHeight;
    }

    public int getLineHeight()
    {
        return lineHeight;
    }

    public int getWidth(String text)
    {
        DisplayList displayList = null;
        if(displayListCaching)
        {
            displayList = (DisplayList)displayLists.get(text);
            if(displayList != null && displayList.width != null)
                return displayList.width.intValue();
        }
        int maxWidth = 0;
        int width = 0;
        CharDef lastCharDef = null;
        int i = 0;
        for(int n = text.length(); i < n; i++)
        {
            int id = text.charAt(i);
            if(id == 10)
            {
                width = 0;
                continue;
            }
            if(id >= chars.length)
                continue;
            CharDef charDef = chars[id];
            if(charDef == null)
                continue;
            if(lastCharDef != null)
                width += lastCharDef.getKerning(id);
            lastCharDef = charDef;
            if(i < n - 1)
                width += charDef.xadvance;
            else
                width += charDef.width;
            maxWidth = Math.max(maxWidth, width);
        }

        if(displayList != null)
            displayList.width = new Short((short)maxWidth);
        return maxWidth;
    }

    public int getYOffset(String text)
    {
        DisplayList displayList = null;
        if(displayListCaching)
        {
            displayList = (DisplayList)displayLists.get(text);
            if(displayList != null && displayList.yOffset != null)
                return displayList.yOffset.intValue();
        }
        int stopIndex = text.indexOf('\n');
        if(stopIndex == -1)
            stopIndex = text.length();
        int minYOffset = 10000;
        for(int i = 0; i < stopIndex; i++)
        {
            int id = text.charAt(i);
            CharDef charDef = chars[id];
            if(charDef != null)
                minYOffset = Math.min(charDef.yoffset, minYOffset);
        }

        if(displayList != null)
            displayList.yOffset = new Short((short)minYOffset);
        return minYOffset;
    }

    private CharDef parseChar(String line)
    {
        CharDef def = new CharDef();
        StringTokenizer tokens = new StringTokenizer(line, " =");
        tokens.nextToken();
        tokens.nextToken();
        def.id = Short.parseShort(tokens.nextToken());
        if(def.id < 0)
            return null;
        if(def.id <= 255);
        tokens.nextToken();
        def.x = Short.parseShort(tokens.nextToken());
        tokens.nextToken();
        def.y = Short.parseShort(tokens.nextToken());
        tokens.nextToken();
        def.width = Short.parseShort(tokens.nextToken());
        tokens.nextToken();
        def.height = Short.parseShort(tokens.nextToken());
        tokens.nextToken();
        def.xoffset = Short.parseShort(tokens.nextToken());
        tokens.nextToken();
        def.yoffset = Short.parseShort(tokens.nextToken());
        tokens.nextToken();
        def.xadvance = Short.parseShort(tokens.nextToken());
        def.init();
        if(def.id != 32)
            lineHeight = Math.max(def.height + def.yoffset, lineHeight);
        return def;
    }

    private void parseFnt(InputStream fntFile)
    {
        if(displayListCaching)
        {
            baseDisplayListID = GL11.glGenLists(200);
            if(baseDisplayListID == 0)
                displayListCaching = false;
        }
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(fntFile));
            String info = in.readLine();
            String common = in.readLine();
            String page = in.readLine();
            Map kerning = new HashMap(64);
            List charDefs = new ArrayList(255);
            int maxChar = 0;
            boolean done = false;
            do
            {
                if(done)
                    break;
                String line = in.readLine();
                if(line == null)
                {
                    done = true;
                } else
                {
                    if(!line.startsWith("chars c") && line.startsWith("char"))
                    {
                        CharDef def = parseChar(line);
                        if(def != null)
                        {
                            maxChar = Math.max(maxChar, def.id);
                            charDefs.add(def);
                        }
                    }
                    if(!line.startsWith("kernings c") && line.startsWith("kerning"))
                    {
                        StringTokenizer tokens = new StringTokenizer(line, " =");
                        tokens.nextToken();
                        tokens.nextToken();
                        short first = Short.parseShort(tokens.nextToken());
                        tokens.nextToken();
                        int second = Integer.parseInt(tokens.nextToken());
                        tokens.nextToken();
                        int offset = Integer.parseInt(tokens.nextToken());
                        List values = (List)kerning.get(new Short(first));
                        if(values == null)
                        {
                            values = new ArrayList();
                            kerning.put(new Short(first), values);
                        }
                        values.add(new Short((short)(offset << 8 | second)));
                    }
                }
            } while(true);
            chars = new CharDef[maxChar + 1];
            for(Iterator iter = charDefs.iterator(); iter.hasNext();)
            {
                CharDef def = (CharDef)iter.next();
                chars[def.id] = def;
            }

            for(Iterator iter = kerning.entrySet().iterator(); iter.hasNext();)
            {
                java.util.Map.Entry entry = (java.util.Map.Entry)iter.next();
                short first = ((Short)entry.getKey()).shortValue();
                List valueList = (List)entry.getValue();
                short valueArray[] = new short[valueList.size()];
                int i = 0;
                for(Iterator valueIter = valueList.iterator(); valueIter.hasNext();)
                {
                    valueArray[i] = ((Short)valueIter.next()).shortValue();
                    i++;
                }

                chars[first].kerning = valueArray;
            }

        }
        catch(IOException e) { }
    }

    private void render(String text, int start, int end)
    {
        int x = 0;
        int y = 0;
        CharDef lastCharDef = null;
        char data[] = text.toCharArray();
        for(int i = 0; i < data.length; i++)
        {
            int id = data[i];
            if(id == 10)
            {
                x = 0;
                y += getLineHeight();
                continue;
            }
            if(id >= chars.length)
                continue;
            CharDef charDef = chars[id];
            if(charDef == null)
                continue;
            if(lastCharDef != null)
                x += lastCharDef.getKerning(id);
            lastCharDef = charDef;
            if(i >= start && i <= end)
                charDef.draw(x, y);
            x += charDef.xadvance;
        }

    }

    private static final int DISPLAY_LIST_CACHE_SIZE = 200;
    private static final int MAX_CHAR = 255;
    private int baseDisplayListID;
    private CharDef chars[];
    private boolean displayListCaching;
    private DisplayList eldestDisplayList;
    private int eldestDisplayListID;
    private final LinkedHashMap displayLists = new LinkedHashMap(200, 1.0F, true);
    private Texture fontImage;
    private int lineHeight;
    public int xoff;
    public int yoff;
    public static Color curCol = null;





}
