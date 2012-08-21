// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SpriteRenderer.java

package zombie.core;

import java.io.PrintStream;
import java.nio.*;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import zombie.core.Collections.NulledArrayList;
import zombie.core.Styles.AlphaOp;
import zombie.core.Styles.FloatList;
import zombie.core.Styles.GeometryData;
import zombie.core.Styles.LightingStyle;
import zombie.core.Styles.ShortList;
import zombie.core.Styles.Style;
import zombie.core.Styles.TransparentStyle;
import zombie.core.VBO.GLVertexBufferObject;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureDraw;

public class SpriteRenderer
{
    private static class RingBuffer
    {
        private class StateRun
        {

            void render()
            {
                if(!ops.isEmpty())
                {
                    TextureDraw o;
                    for(Iterator it = ops.iterator(); it.hasNext(); o.run())
                        o = (TextureDraw)it.next();

                    ops.clear();
                    return;
                }
                if(style != lastRenderedStyle)
                {
                    if(lastRenderedStyle != null)
                        lastRenderedStyle.resetState();
                    if(style != null)
                        style.setupState();
                    lastRenderedStyle = style;
                }
                if(texture0 != lastRenderedTexture0)
                {
                    if(texture0 != null)
                    {
                        if(lastRenderedTexture0 == null)
                            GL11.glEnable(3553);
                        texture0.bind();
                    } else
                    if(lastRenderedTexture0 != null)
                        GL11.glDisable(3553);
                    lastRenderedTexture0 = texture0;
                }
                if(length == 0)
                    return;
                if(length == -1)
                {
                    restoreVBOs = true;
                    return;
                }
                if(restoreVBOs)
                {
                    restoreVBOs = false;
                    GL11.glVertexPointer(2, 5126, 20, 0L);
                    GL11.glTexCoordPointer(2, 5126, 20, 8L);
                    GL11.glColorPointer(4, 5121, 20, 16L);
                }
                if(style.getRenderSprite())
                    GL12.glDrawRangeElements(4, start, start + length, endIndex - startIndex, 5123, startIndex * 2);
                else
                    style.render(start, startIndex);
            }

            Texture texture0;
            Style style;
            int start;
            int length;
            ShortBuffer indices;
            int startIndex;
            int endIndex;
            NulledArrayList ops;
          

            private StateRun()
            {
                
                ops = new NulledArrayList();
            }

        }


        void create()
        {
            GL11.glEnableClientState(32884);
            GL11.glEnableClientState(32886);
            GL11.glEnableClientState(32888);
            bufferSize = 0x102010L;
            bufferSizeInVertices = bufferSize / 20L;
            numBuffers = 12;
            indexBufferSize = bufferSizeInVertices * 3L;
            vertices = new FloatBuffer[numBuffers];
            verticesBytes = new ByteBuffer[numBuffers];
            indices = new ShortBuffer[numBuffers];
            indicesBytes = new ByteBuffer[numBuffers];
            stateRun = new StateRun[5000];
            for(int n = 0; n < 5000; n++)
                stateRun[n] = new StateRun();

            vbo = new GLVertexBufferObject[numBuffers];
            ibo = new GLVertexBufferObject[numBuffers];
            for(int i = 0; i < numBuffers; i++)
            {
                vbo[i] = new GLVertexBufferObject(bufferSize, 34962, 35040);
                vbo[i].create();
                ibo[i] = new GLVertexBufferObject(indexBufferSize * 2L, 34963, 35040);
                ibo[i].create();
            }

        }

        void add(TextureDraw s, Style newStyle, float engineAlpha)
        {
            if(s.type == zombie.core.textures.TextureDraw.Type.glDraw && !s.isOnScreen())
                return;
            if((long)(vertexCursor + 4) > bufferSizeInVertices || (long)(indexCursor + 6) > indexBufferSize)
            {
                render();
                next();
            }
            Texture newTexture0 = s.tex;
            if(currentRun == null || newStyle != currentStyle && (currentStyle == null || newStyle.getStyleID() != currentStyle.getStyleID()) || newTexture0 != currentTexture0)
            {
                currentRun = stateRun[numRuns];
                currentRun.start = vertexCursor;
                currentRun.length = 0;
                currentRun.style = newStyle;
                currentRun.texture0 = newTexture0;
                currentRun.indices = currentIndices;
                currentRun.startIndex = indexCursor;
                currentRun.endIndex = indexCursor;
                numRuns++;
                if(numRuns == stateRun.length)
                    growStateRuns();
                currentStyle = newStyle;
                currentTexture0 = newTexture0;
                if(s.type != zombie.core.textures.TextureDraw.Type.glDraw)
                {
                    currentRun.ops.add(s);
                    return;
                }
            } else
            if(s.type != zombie.core.textures.TextureDraw.Type.glDraw)
            {
                currentRun.ops.add(s);
                return;
            }
            FloatBuffer floats = currentVertices;
            AlphaOp alphaOp = newStyle.getAlphaOp();
            floats.put(s.x[0]);
            floats.put(s.y[0]);
            if(s.tex == null)
            {
                floats.put(0.0F);
                floats.put(0.0F);
            } else
            {
                if(s.flipped)
                    floats.put(s.u[1]);
                else
                    floats.put(s.u[0]);
                floats.put(s.v[0]);
            }
            int color = s.getColor(0);
            alphaOp.op(color, 255, floats);
            floats.put(s.x[1]);
            floats.put(s.y[1]);
            if(s.tex == null)
            {
                floats.put(0.0F);
                floats.put(0.0F);
            } else
            {
                if(s.flipped)
                    floats.put(s.u[0]);
                else
                    floats.put(s.u[1]);
                floats.put(s.v[1]);
            }
            color = s.getColor(1);
            alphaOp.op(color, 255, floats);
            floats.put(s.x[2]);
            floats.put(s.y[2]);
            if(s.tex == null)
            {
                floats.put(0.0F);
                floats.put(0.0F);
            } else
            {
                if(s.flipped)
                    floats.put(s.u[3]);
                else
                    floats.put(s.u[2]);
                floats.put(s.v[2]);
            }
            color = s.getColor(2);
            alphaOp.op(color, 255, floats);
            floats.put(s.x[3]);
            floats.put(s.y[3]);
            if(s.tex == null)
            {
                floats.put(0.0F);
                floats.put(0.0F);
            } else
            {
                if(s.flipped)
                    floats.put(s.u[2]);
                else
                    floats.put(s.u[3]);
                floats.put(s.v[3]);
            }
            color = s.getColor(3);
            alphaOp.op(color, 255, floats);
            currentIndices.put((short)vertexCursor);
            currentIndices.put((short)(vertexCursor + 1));
            currentIndices.put((short)(vertexCursor + 2));
            currentIndices.put((short)vertexCursor);
            currentIndices.put((short)(vertexCursor + 2));
            currentIndices.put((short)(vertexCursor + 3));
            indexCursor += 6;
            vertexCursor += 4;
            currentRun.endIndex += 6;
            currentRun.length += 4;
        }

        void add(Style s)
        {
            GeometryData data = s.build();
            if(data == null)
            {
                currentRun = stateRun[numRuns];
                currentRun.start = vertexCursor;
                currentRun.length = -1;
                currentRun.style = s;
                currentRun.texture0 = null;
                currentRun.startIndex = 0;
                numRuns++;
                if(numRuns == stateRun.length)
                    growStateRuns();
                currentStyle = null;
                currentTexture0 = null;
                return;
            }
            FloatList vertexData = data.getVertexData();
            ShortList indexData = data.getIndexData();
            int vertsToWrite = vertexData.size() / 5;
            if((long)(vertexCursor + vertsToWrite) > bufferSizeInVertices)
            {
                render();
                next();
            }
            currentRun = stateRun[numRuns];
            currentRun.start = vertexCursor;
            currentRun.length = vertsToWrite;
            currentRun.style = s;
            currentRun.texture0 = null;
            currentRun.startIndex = indexCursor;
            numRuns++;
            if(numRuns == stateRun.length)
                growStateRuns();
            currentVertices.position(vertexCursor * 20 >> 2);
            currentVertices.put(vertexData.array(), 0, vertexData.size());
            currentIndices.position(indexCursor);
            short idx[] = indexData.array();
            int indicesToWrite = indexData.size();
            for(int i = 0; i < indicesToWrite; i++)
                idx[i] += vertexCursor;

            currentIndices.put(idx, 0, indicesToWrite);
            vertexCursor += vertsToWrite;
            indexCursor += indicesToWrite;
            currentStyle = null;
            currentTexture0 = null;
        }

        private void next()
        {
            sequence++;
            if(sequence == numBuffers)
                sequence = 0;
            if(sequence == mark)
                System.out.println("Buffer overrun");
            vbo[sequence].render();
            ByteBuffer buf = vbo[sequence].map();
            if(vertices[sequence] == null || verticesBytes[sequence] != buf)
            {
                verticesBytes[sequence] = buf;
                vertices[sequence] = buf.asFloatBuffer();
            }
            ibo[sequence].render();
            ByteBuffer ibuf = ibo[sequence].map();
            if(indices[sequence] == null || indicesBytes[sequence] != ibuf)
            {
                indicesBytes[sequence] = ibuf;
                indices[sequence] = ibuf.asShortBuffer();
            }
            currentVertices = vertices[sequence];
            currentVertices.clear();
            currentIndices = indices[sequence];
            currentIndices.clear();
            vertexCursor = 0;
            indexCursor = 0;
            numRuns = 0;
            currentRun = null;
        }

        void begin()
        {
            currentStyle = null;
            currentTexture0 = null;
            next();
            mark = sequence;
        }

        void render()
        {
            vbo[sequence].unmap();
            ibo[sequence].unmap();
            lastRenderedStyle = null;
            lastRenderedTexture0 = null;
            restoreVBOs = true;
            for(int i = 0; i < numRuns; i++)
                stateRun[i].render();

            if(lastRenderedStyle != null)
                lastRenderedStyle.resetState();
        }

        void finish()
        {
            render();
            SpriteRenderer.instance.clearSprites();
        }

        void growStateRuns()
        {
            StateRun newStateRun[] = new StateRun[(int)((float)stateRun.length * 1.5F)];
            System.arraycopy(stateRun, 0, newStateRun, 0, stateRun.length);
            for(int i = numRuns; i < newStateRun.length; i++)
                newStateRun[i] = new StateRun();

            stateRun = newStateRun;
        }

        GLVertexBufferObject vbo[];
        GLVertexBufferObject ibo[];
        long bufferSize;
        long bufferSizeInVertices;
        long indexBufferSize;
        int numBuffers;
        int sequence;
        int mark;
        FloatBuffer currentVertices;
        ShortBuffer currentIndices;
        FloatBuffer vertices[];
        ByteBuffer verticesBytes[];
        ShortBuffer indices[];
        ByteBuffer indicesBytes[];
        Texture lastRenderedTexture0;
        Texture currentTexture0;
        Style lastRenderedStyle;
        Style currentStyle;
        StateRun stateRun[];
        boolean restoreVBOs;
        int vertexCursor;
        int indexCursor;
        int numRuns;
        StateRun currentRun;

        RingBuffer()
        {
            sequence = -1;
            mark = -1;
        }
    }


    public SpriteRenderer()
    {
        numSprites = 0;
        sprite = new TextureDraw[20000];
        for(int n = 0; n < sprite.length; n++)
            sprite[n] = new TextureDraw();

        style = new Style[20000];
    }

    public void CheckSpriteSlots()
    {
        if(numSprites == sprite.length)
        {
            TextureDraw old_sprite[] = sprite;
            sprite = new TextureDraw[numSprites * 2];
            for(int n = numSprites; n < sprite.length; n++)
                sprite[n] = new TextureDraw();

            System.arraycopy(old_sprite, 0, sprite, 0, numSprites);
            old_sprite = null;
            Style old_style[] = style;
            style = new Style[numSprites * 2];
            System.arraycopy(old_style, 0, style, 0, numSprites);
            old_style = null;
        }
    }

    public void create()
    {
        if(ringBuffer == null)
        {
            ringBuffer = new RingBuffer();
            ringBuffer.create();
        }
    }

    private void clearSprites()
    {
        for(int n = 0; n < sprite.length; n++)
            if(sprite[n] != null)
            {
                sprite[n].reset();
                style[n] = null;
            }

    }

    public void renderflipped(Texture tex, int x, int y, int width, int height, float r, float g, 
            float b, float a)
    {
        render(tex, x, y, width, height, r, g, b, a);
        sprite[numSprites - 1].flipped = true;
    }

    public void renderflipped(Texture tex, int x, int y, int width, int height, int c)
    {
        render(tex, x, y, width, height, c);
        sprite[numSprites - 1].flipped = true;
    }

    public void glDisable(int a)
    {
        CheckSpriteSlots();
        TextureDraw.glDisable(sprite[numSprites], a);
        style[numSprites] = TransparentStyle.instance;
        numSprites++;
    }

    public void glEnable(int a)
    {
        CheckSpriteSlots();
        TextureDraw.glEnable(sprite[numSprites], a);
        style[numSprites] = TransparentStyle.instance;
        numSprites++;
    }

    public void glStencilMask(int a)
    {
        CheckSpriteSlots();
        TextureDraw.glStencilMask(sprite[numSprites], a);
        style[numSprites] = TransparentStyle.instance;
        numSprites++;
    }

    public void glClear(int a)
    {
        CheckSpriteSlots();
        TextureDraw.glClear(sprite[numSprites], a);
        style[numSprites] = TransparentStyle.instance;
        numSprites++;
    }

    public void glStencilFunc(int a, int b, int c)
    {
        CheckSpriteSlots();
        TextureDraw.glStencilFunc(sprite[numSprites], a, b, c);
        style[numSprites] = TransparentStyle.instance;
        numSprites++;
    }

    public void glStencilOp(int a, int b, int c)
    {
        CheckSpriteSlots();
        TextureDraw.glStencilOp(sprite[numSprites], a, b, c);
        style[numSprites] = TransparentStyle.instance;
        numSprites++;
    }

    public void glColorMask(int a, int b, int c, int d)
    {
        CheckSpriteSlots();
        TextureDraw.glColorMask(sprite[numSprites], a, b, c, d);
        style[numSprites] = TransparentStyle.instance;
        numSprites++;
    }

    public void glAlphaFunc(int a, float b)
    {
        CheckSpriteSlots();
        TextureDraw.glAlphaFunc(sprite[numSprites], a, b);
        style[numSprites] = TransparentStyle.instance;
        numSprites++;
    }

    public void glBlendFunc(int a, float b)
    {
        CheckSpriteSlots();
        TextureDraw.glBlendFunc(sprite[numSprites], a, b);
        style[numSprites] = TransparentStyle.instance;
        numSprites++;
    }

    public void render(Texture tex, int x1, int y1, int x2, int y2, int x3, int y3, 
            int x4, int y4, float r1, float g1, float b1, float a1, float r2, 
            float g2, float b2, float a2, float r3, float g3, float b3, float a3, 
            float r4, float g4, float b4, float a4)
    {
        CheckSpriteSlots();
        TextureDraw.Create(sprite[numSprites], tex, x1, y1, x2, y2, x3, y3, x4, y4, r1, g1, b1, a1, r2, g2, b2, a2, r3, g3, b3, a3, r4, g4, b4, a4);
        addSprite(LightingStyle.instance, sprite[numSprites]);
    }

    public void render(Texture tex, int x1, int y1, int x2, int y2, int x3, int y3, 
            int x4, int y4, int c1, int c2, int c3, int c4)
    {
        CheckSpriteSlots();
        TextureDraw.Create(sprite[numSprites], tex, x1, y1, x2, y2, x3, y3, x4, y4, c1, c2, c3, c4);
        addSprite(LightingStyle.instance, sprite[numSprites]);
    }

    public void render(Texture tex, int x, int y, int width, int height, float r, float g, 
            float b, float a)
    {
        if(a == 0.0F)
        {
            return;
        } else
        {
            CheckSpriteSlots();
            TextureDraw.Create(sprite[numSprites], tex, x, y, width, height, r, g, b, a);
            addSprite(TransparentStyle.instance, null);
            return;
        }
    }

    public void render(Texture tex, int x, int y, int width, int height, float r, float g, 
            float b, float a, float u1, float v1, float u2, float v2, float u3, 
            float v3, float u4, float v4)
    {
        if(a == 0.0F)
        {
            return;
        } else
        {
            CheckSpriteSlots();
            TextureDraw.Create(sprite[numSprites], tex, x, y, width, height, r, g, b, a, u1, v1, u2, v2, u3, v3, u4, v4);
            addSprite(TransparentStyle.instance, null);
            return;
        }
    }

    public void render(Texture tex, int x, int y, int width, int height, int c)
    {
        CheckSpriteSlots();
        TextureDraw.Create(sprite[numSprites], tex, x, y, width, height, c);
        addSprite(TransparentStyle.instance, null);
    }

    private void addSprite(Style spriteStyle, TextureDraw s)
    {
        style[numSprites] = spriteStyle;
        numSprites++;
    }

    private void build()
    {
        for(int i = 0; i < numSprites; i++)
        {
            TextureDraw s = sprite[i];
            Style newStyle = style[i];
            ringBuffer.add(s, newStyle, 1.0F);
        }

    }

    public void preRender()
    {
        numSprites = 0;
    }

    public void postRender()
    {
        if(numSprites == 0)
        {
            return;
        } else
        {
            ringBuffer.begin();
            build();
            ringBuffer.finish();
            return;
        }
    }

    public static SpriteRenderer instance;
    static final int VERTEX_SIZE = 20;
    static final int TEXTURE0_COORD_OFFSET = 8;
    static final int COLOR_OFFSET = 16;
    private static RingBuffer ringBuffer;
    private TextureDraw sprite[];
    public int numSprites;
    public Style style[];

}
