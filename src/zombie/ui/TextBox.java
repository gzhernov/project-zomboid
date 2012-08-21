// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TextBox.java

package zombie.ui;

import java.util.Iterator;
import java.util.Stack;

// Referenced classes of package zombie.ui:
//            UIElement, TextManager, UIFont

public class TextBox extends UIElement
{

    public TextBox(UIFont font, int x, int y, int width, String text)
    {
        Lines = new Stack();
        Centred = false;
        this.font = font;
        this.x = x;
        this.y = y;
        Text = text;
        this.width = width;
        Paginate();
    }

    public void onresize()
    {
        Paginate();
    }

    public void render()
    {
        if(!isVisible())
            return;
        super.render();
        Paginate();
        int y = 0;
        for(Iterator i$ = Lines.iterator(); i$.hasNext();)
        {
            String text = (String)i$.next();
            if(Centred)
                TextManager.instance.DrawStringCentre(font, getAbsoluteX() + getWidth() / 2, getAbsoluteY() + y, text, 1.0F, 1.0F, 1.0F, 1.0F);
            else
                TextManager.instance.DrawString(font, getAbsoluteX(), getAbsoluteY() + y, text, 1.0F, 1.0F, 1.0F, 1.0F);
            y += TextManager.instance.MeasureStringY(font, (String)Lines.get(0));
        }

        setHeight(y);
    }

    public void update()
    {
        Paginate();
        int y = 0;
        for(Iterator i$ = Lines.iterator(); i$.hasNext();)
        {
            String text = (String)i$.next();
            y += TextManager.instance.MeasureStringY(font, (String)Lines.get(0));
        }

        setHeight(y);
    }

    private void Paginate()
    {
        int n = 0;
        Lines.clear();
        String textarr[] = Text.split("<br>");
        String arr$[] = textarr;
        int len$ = arr$.length;
label0:
        for(int i$ = 0; i$ < len$; i$++)
        {
            String text = arr$[i$];
            if(text.length() == 0)
            {
                Lines.add(" ");
                continue;
            }
            do
            {
                int m = text.indexOf(" ", n + 1);
                int z = m;
                if(z == -1)
                    z = text.length();
                int wid = TextManager.instance.MeasureStringX(font, text.substring(0, z));
                if(wid >= getWidth())
                {
                    String sub = text.substring(0, n);
                    text = text.substring(n + 1);
                    Lines.add(sub);
                    m = 0;
                } else
                if(m == -1)
                {
                    String sub = text;
                    Lines.add(sub);
                    continue label0;
                }
                n = m;
            } while(text.length() > 0);
        }

    }

    public void SetText(String text)
    {
        Text = text;
        Paginate();
    }

    public boolean ResizeParent;
    UIFont font;
    Stack Lines;
    String Text;
    public boolean Centred;
}
