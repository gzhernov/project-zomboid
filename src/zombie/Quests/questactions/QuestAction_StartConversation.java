// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QuestAction_StartConversation.java

package zombie.Quests.questactions;

import zombie.characters.Talker;

// Referenced classes of package zombie.Quests.questactions:
//            QuestAction

public class QuestAction_StartConversation
    implements QuestAction
{

    public QuestAction_StartConversation(String conversation, Talker a, Talker b)
    {
        Conversation = conversation;
        A = a;
        B = b;
    }

    public void Execute()
    {
    }

    Talker A;
    Talker B;
    String Conversation;
}
