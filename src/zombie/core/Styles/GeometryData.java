// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GeometryData.java

package zombie.core.Styles;


// Referenced classes of package zombie.core.Styles:
//            FloatList, ShortList

public class GeometryData
{

    public GeometryData(FloatList vertexData, ShortList indexData)
    {
        this.vertexData = vertexData;
        this.indexData = indexData;
    }

    public void clear()
    {
        vertexData.clear();
        indexData.clear();
    }

    public FloatList getVertexData()
    {
        return vertexData;
    }

    public ShortList getIndexData()
    {
        return indexData;
    }

    private final FloatList vertexData;
    private final ShortList indexData;
}
