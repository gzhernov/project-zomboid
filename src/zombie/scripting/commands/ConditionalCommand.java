// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ConditionalCommand.java

package zombie.scripting.commands;

import zombie.scripting.objects.Conditional;
import zombie.scripting.objects.Script;

// Referenced classes of package zombie.scripting.commands:
//            BaseCommand

public class ConditionalCommand extends BaseCommand
{

    public ConditionalCommand(String con, String internal, Script parent)
    {
        bDoIt = false;
        this.con = new Conditional(con, internal, this);
        inst = new zombie.scripting.objects.Script.ScriptInstance();
        inst.theScript = this.con;
        this.parent = parent;
    }

    public void init(String s, String as[])
    {
    }

    public boolean AllowCharacterBehaviour(String scriptCharacter)
    {
        if(bDoIt)
            return con.AllowCharacterBehaviour(scriptCharacter, inst);
        if(elsecon != null)
            return elsecon.AllowCharacterBehaviour(scriptCharacter, inst);
        else
            return true;
    }

    public void begin()
    {
        bDoIt = con.ConditionPassed(currentinstance);
        if(bDoIt)
        {
            inst.CopyAliases(currentinstance);
            inst.theScript = con;
            inst.begin();
        } else
        if(elsecon != null)
        {
            elseinst.CopyAliases(currentinstance);
            elseinst.theScript = elsecon;
            elseinst.begin();
        }
    }

    public boolean IsFinished()
    {
        return !bDoIt && elsecon == null || bDoIt && inst.finished() || !bDoIt && elsecon != null && elseinst.finished();
    }

    public void update()
    {
        if(bDoIt)
        {
            inst.CopyAliases(currentinstance);
            inst.update();
        } else
        if(elsecon != null)
        {
            elseinst.CopyAliases(currentinstance);
            elseinst.update();
        }
    }

    public boolean DoesInstantly()
    {
        return false;
    }

    public void AddElse(String internal)
    {
        elsecon = new Conditional(null, internal);
        elseinst = new zombie.scripting.objects.Script.ScriptInstance();
        elseinst.theScript = elsecon;
    }

    boolean bDoIt;
    zombie.scripting.objects.Script.ScriptInstance inst;
    public Script parent;
    Conditional con;
    Conditional elsecon;
    zombie.scripting.objects.Script.ScriptInstance elseinst;
}
