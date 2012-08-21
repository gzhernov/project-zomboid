// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QuestAction_RunScript.java

package zombie.Quests.questactions;

import zombie.scripting.ScriptManager;

// Referenced classes of package zombie.Quests.questactions:
//            QuestAction

public class QuestAction_RunScript
    implements QuestAction
{

    public QuestAction_RunScript(String script)
    {
        Script = script;
    }

    public void Execute()
    {
        ScriptManager.instance.PlayScript(Script);
    }

    String Script;
}
