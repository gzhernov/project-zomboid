// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CommandFactory.java

package zombie.scripting.commands;

import zombie.scripting.commands.Activatable.IsActivated;
import zombie.scripting.commands.Activatable.ToggleActivatable;
import zombie.scripting.commands.Character.ActualizeCommand;
import zombie.scripting.commands.Character.AddEnemy;
import zombie.scripting.commands.Character.AddInventory;
import zombie.scripting.commands.Character.AddToGroup;
import zombie.scripting.commands.Character.AimWhileStationary;
import zombie.scripting.commands.Character.AllowBehaviours;
import zombie.scripting.commands.Character.AllowConversation;
import zombie.scripting.commands.Character.Anger;
import zombie.scripting.commands.Character.Attack;
import zombie.scripting.commands.Character.Die;
import zombie.scripting.commands.Character.EquipItem;
import zombie.scripting.commands.Character.Exists;
import zombie.scripting.commands.Character.FaceCommand;
import zombie.scripting.commands.Character.HasInventory;
import zombie.scripting.commands.Character.HasTrait;
import zombie.scripting.commands.Character.InRange;
import zombie.scripting.commands.Character.IncrementCharacterScriptFlag;
import zombie.scripting.commands.Character.IsAggressive;
import zombie.scripting.commands.Character.IsAggressivePose;
import zombie.scripting.commands.Character.IsCharacterScriptFlagEqualTo;
import zombie.scripting.commands.Character.IsCharacterScriptFlagOver;
import zombie.scripting.commands.Character.IsDead;
import zombie.scripting.commands.Character.IsFriendly;
import zombie.scripting.commands.Character.IsInGroup;
import zombie.scripting.commands.Character.IsInGroupWith;
import zombie.scripting.commands.Character.IsInRoom;
import zombie.scripting.commands.Character.IsInside;
import zombie.scripting.commands.Character.IsLeaderOfGroup;
import zombie.scripting.commands.Character.IsNeutral;
import zombie.scripting.commands.Character.IsNumberOfEnemiesOver;
import zombie.scripting.commands.Character.IsNumberOfEnemiesUnder;
import zombie.scripting.commands.Character.IsNumberOfLocalOver;
import zombie.scripting.commands.Character.IsNumberOfLocalUnder;
import zombie.scripting.commands.Character.IsNumberOfNeutralOver;
import zombie.scripting.commands.Character.IsNumberOfNeutralUnder;
import zombie.scripting.commands.Character.IsOnFloor;
import zombie.scripting.commands.Character.IsPlayer;
import zombie.scripting.commands.Character.IsSpeaking;
import zombie.scripting.commands.Character.MetCountIsOver;
import zombie.scripting.commands.Character.NamedOrder;
import zombie.scripting.commands.Character.Order;
import zombie.scripting.commands.Character.PopOrder;
import zombie.scripting.commands.Character.RemoveNamedOrder;
import zombie.scripting.commands.Character.SayAt;
import zombie.scripting.commands.Character.SayCommand;
import zombie.scripting.commands.Character.SayIdle;
import zombie.scripting.commands.Character.Sleep;
import zombie.scripting.commands.Character.StopAction;
import zombie.scripting.commands.Character.TestStat;
import zombie.scripting.commands.Character.WalkCommand;
import zombie.scripting.commands.Character.WalkToLastHeardSound;
import zombie.scripting.commands.Character.WalkToLastKnownLocationOf;
import zombie.scripting.commands.Character.WalkWithinRangeOf;
import zombie.scripting.commands.DayNight.IsNight;
import zombie.scripting.commands.Flags.Decrement;
import zombie.scripting.commands.Flags.Increment;
import zombie.scripting.commands.Flags.IsFlagValue;
import zombie.scripting.commands.Flags.IsGreaterThan;
import zombie.scripting.commands.Flags.IsGreaterThanEqualTo;
import zombie.scripting.commands.Flags.IsLessThan;
import zombie.scripting.commands.Flags.IsLessThanEqualTo;
import zombie.scripting.commands.Flags.SetFlag;
import zombie.scripting.commands.Hook.RegisterOneTime;
import zombie.scripting.commands.Module.Enabled;
import zombie.scripting.commands.Script.Call;
import zombie.scripting.commands.Script.CallAndWait;
import zombie.scripting.commands.Script.CharactersAlreadyInScript;
import zombie.scripting.commands.Script.IsPlaying;
import zombie.scripting.commands.Script.Pause;
import zombie.scripting.commands.Script.Resume;
import zombie.scripting.commands.Script.StopScript;
import zombie.scripting.commands.Trigger.IsLastFiredParameter;
import zombie.scripting.commands.Trigger.ProcessAlways;
import zombie.scripting.commands.Trigger.ProcessNever;
import zombie.scripting.commands.Trigger.TimeSinceLastRan;
import zombie.scripting.commands.Tutorial.AddHelpIconToUIElement;
import zombie.scripting.commands.Tutorial.AddHelpIconToWorld;
import zombie.scripting.commands.Tutorial.DisableTutorialZombieControl;
import zombie.scripting.commands.Tutorial.SetZombieLimit;
import zombie.scripting.commands.World.CreateZombieSwarm;
import zombie.scripting.commands.World.PlaySoundEffect;
import zombie.scripting.commands.World.PlayWorldSoundEffect;
import zombie.scripting.commands.World.SpawnZombie;
import zombie.scripting.commands.World.StartFire;
import zombie.scripting.commands.quest.AddEquipItemTask;
import zombie.scripting.commands.quest.AddFindItemTask;
import zombie.scripting.commands.quest.AddGotoLocationTask;
import zombie.scripting.commands.quest.AddHardCodedTask;
import zombie.scripting.commands.quest.AddScriptConditionTask;
import zombie.scripting.commands.quest.AddUseItemOnTask;
import zombie.scripting.commands.quest.CreateQuest;
import zombie.scripting.commands.quest.LockQuest;
import zombie.scripting.commands.quest.RunScriptOnComplete;
import zombie.scripting.commands.quest.UnlockLast;
import zombie.scripting.commands.quest.UnlockLastButHide;
import zombie.scripting.commands.quest.UnlockQuest;
import zombie.scripting.commands.quest.UnlockTaskOnComplete;
import zombie.scripting.commands.quest.UnlockTasksOnComplete;

// Referenced classes of package zombie.scripting.commands:
//            SetModuleAlias, StopAllScriptsExceptContaining, StopAllScriptsContaining, StopAllScriptsExcept, 
//            PauseAllScriptsExcept, ResumeAllScriptsExcept, WaitCommand, LockHud, 
//            LoadTexturePage, BaseCommand

public class CommandFactory
{

    public CommandFactory()
    {
    }

    public static BaseCommand CreateCommand(String command)
    {
        if(command.equals("CreateQuest"))
            return new CreateQuest();
        if(command.equals("SetModuleAlias"))
            return new SetModuleAlias();
        if(command.equals("SayAt"))
            return new SayAt();
        if(command.equals("Exists"))
            return new Exists();
        if(command.equals("TestStat"))
            return new TestStat();
        if(command.equals("Enabled"))
            return new Enabled();
        if(command.equals("AddHelpIconToWorld"))
            return new AddHelpIconToWorld();
        if(command.equals("AddHelpIconToUIElement"))
            return new AddHelpIconToUIElement();
        if(command.equals("AimWhileStationary"))
            return new AimWhileStationary();
        if(command.equals("HasTrait"))
            return new HasTrait();
        if(command.equals("Die"))
            return new Die();
        if(command.equals("LockQuest"))
            return new LockQuest();
        if(command.equals("IsDead"))
            return new IsDead();
        if(command.equals("StopAllScriptsExceptContaining"))
            return new StopAllScriptsExceptContaining();
        if(command.equals("StopAllScriptsContaining"))
            return new StopAllScriptsContaining();
        if(command.equals("IsInRoom"))
            return new IsInRoom();
        if(command.equals("Attack"))
            return new Attack();
        if(command.equals("InRange"))
            return new InRange();
        if(command.equals("Increment"))
            return new Increment();
        if(command.equals("Decrement"))
            return new Decrement();
        if(command.equals("IsLessThan"))
            return new IsLessThan();
        if(command.equals("IsLessThanEqualTo"))
            return new IsLessThanEqualTo();
        if(command.equals("IsGreaterThan"))
            return new IsGreaterThan();
        if(command.equals("IsGreaterThanEqualTo"))
            return new IsGreaterThanEqualTo();
        if(command.equals("DisableTutorialZombieControl"))
            return new DisableTutorialZombieControl();
        if(command.equals("IsAggressivePose"))
            return new IsAggressivePose();
        if(command.equals("IsAggressive"))
            return new IsAggressive();
        if(command.equals("IsNeutral"))
            return new IsNeutral();
        if(command.equals("IsFriendly"))
            return new IsFriendly();
        if(command.equals("Equip"))
            return new EquipItem();
        if(command.equals("AllowConversation"))
            return new AllowConversation();
        if(command.equals("IsNumberOfEnemiesUnder"))
            return new IsNumberOfEnemiesUnder();
        if(command.equals("IsNumberOfEnemiesOver"))
            return new IsNumberOfEnemiesOver();
        if(command.equals("IsPlayer"))
            return new IsPlayer();
        if(command.equals("Order"))
            return new Order();
        if(command.equals("CharactersAlreadyInScript"))
            return new CharactersAlreadyInScript();
        if(command.equals("PopOrder"))
            return new PopOrder();
        if(command.equals("IsLastFiredParameter"))
            return new IsLastFiredParameter();
        if(command.equals("PlaySoundEffect"))
            return new PlaySoundEffect();
        if(command.equals("StopScript"))
            return new StopScript();
        if(command.equals("WalkWithinRangeOf"))
            return new WalkWithinRangeOf();
        if(command.equals("IsNumberOfLocalUnder"))
            return new IsNumberOfLocalUnder();
        if(command.equals("IsNumberOfLocalOver"))
            return new IsNumberOfLocalOver();
        if(command.equals("IsCharacterScriptFlagOver"))
            return new IsCharacterScriptFlagOver();
        if(command.equals("IsCharacterScriptFlagEqualTo"))
            return new IsCharacterScriptFlagEqualTo();
        if(command.equals("IncrementCharacterScriptFlag"))
            return new IncrementCharacterScriptFlag();
        if(command.equals("MetCountIsOver"))
            return new MetCountIsOver();
        if(command.equals("Anger"))
            return new Anger();
        if(command.equals("NamedOrder"))
            return new NamedOrder();
        if(command.equals("RemoveNamedOrder"))
            return new RemoveNamedOrder();
        if(command.equals("IsNumberOfNeutralUnder"))
            return new IsNumberOfNeutralUnder();
        if(command.equals("IsNumberOfNeutralOver"))
            return new IsNumberOfNeutralOver();
        if(command.equals("IsLeaderOfGroup"))
            return new IsLeaderOfGroup();
        if(command.equals("Call"))
            return new Call();
        if(command.equals("AllowBehaviours"))
            return new AllowBehaviours();
        if(command.equals("CallWait"))
            return new CallAndWait();
        if(command.equals("WalkToLastHeardSound"))
            return new WalkToLastHeardSound();
        if(command.equals("PlayWorldSound"))
            return new PlayWorldSoundEffect();
        if(command.equals("IsInside"))
            return new IsInside();
        if(command.equals("CreateZombieSwarm"))
            return new CreateZombieSwarm();
        if(command.equals("StartFire"))
            return new StartFire();
        if(command.equals("TimeSinceLastRan"))
            return new TimeSinceLastRan();
        if(command.equals("UnlockButHide"))
            return new UnlockLastButHide();
        if(command.equals("IsPlaying"))
            return new IsPlaying();
        if(command.equals("IsOnFloor"))
            return new IsOnFloor();
        if(command.equals("StopAction"))
            return new StopAction();
        if(command.equals("StopAllScriptsExcept"))
            return new StopAllScriptsExcept();
        if(command.equals("WalkToLastKnownLocationOf"))
            return new WalkToLastKnownLocationOf();
        if(command.equals("PauseAllScriptsExcept"))
            return new PauseAllScriptsExcept();
        if(command.equals("ResumeAllScriptsExcept"))
            return new ResumeAllScriptsExcept();
        if(command.equals("AddInventory"))
            return new AddInventory();
        if(command.equals("IsActivated"))
            return new IsActivated();
        if(command.equals("Toggle"))
            return new ToggleActivatable();
        if(command.equals("SetZombieLimit"))
            return new SetZombieLimit();
        if(command.equals("IsSpeaking"))
            return new IsSpeaking();
        if(command.equals("RegisterOneTime"))
            return new RegisterOneTime();
        if(command.equals("Sleep"))
            return new Sleep();
        if(command.equals("SpawnZombie"))
            return new SpawnZombie();
        if(command.equals("HasInventory"))
            return new HasInventory();
        if(command.equals("AddUseItemOnTask"))
            return new AddUseItemOnTask();
        if(command.equals("ProcessNever"))
            return new ProcessNever();
        if(command.equals("ProcessAlways"))
            return new ProcessAlways();
        if(command.equals("IsNight"))
            return new IsNight();
        if(command.equals("Is"))
            return new IsFlagValue();
        if(command.equals("Set"))
            return new SetFlag();
        if(command.equals("Wait"))
            return new WaitCommand();
        if(command.equals("AddGotoLocationTask"))
            return new AddGotoLocationTask();
        if(command.equals("AddHardCodedTask"))
            return new AddHardCodedTask();
        if(command.equals("AddScriptConditionTask"))
            return new AddScriptConditionTask();
        if(command.equals("RunScriptOnComplete"))
            return new RunScriptOnComplete();
        if(command.equals("AddFindItemTask"))
            return new AddFindItemTask();
        if(command.equals("AddEquipItemTask"))
            return new AddEquipItemTask();
        if(command.equals("UnlockTaskOnComplete"))
            return new UnlockTaskOnComplete();
        if(command.equals("UnlockNextTasksOnComplete"))
            return new UnlockTasksOnComplete();
        if(command.equals("Unlock"))
            return new UnlockLast();
        if(command.equals("Walk"))
            return new WalkCommand();
        if(command.equals("UnlockQuest"))
            return new UnlockQuest();
        if(command.equals("LockHud"))
            return new LockHud();
        if(command.equals("LoadTexturePage"))
            return new LoadTexturePage();
        if(command.equals("Actualize"))
            return new ActualizeCommand();
        if(command.equals("Face"))
            return new FaceCommand();
        if(command.equals("Say"))
            return new SayCommand();
        if(command.equals("Pause"))
            return new Pause();
        if(command.equals("Resume"))
            return new Resume();
        if(command.equals("SayIdle"))
            return new SayIdle();
        if(command.equals("AddEnemy"))
            return new AddEnemy();
        if(command.equals("AddToGroup"))
            return new AddToGroup();
        if(command.equals("IsInGroup"))
            return new IsInGroup();
        if(command.equals("IsInGroupWith"))
        {
            return new IsInGroupWith();
        } else
        {
            command = command;
            return null;
        }
    }
}
