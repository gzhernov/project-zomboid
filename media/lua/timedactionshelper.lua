
TimedActions = {};

function PZ_createNewTimedAction(name, isValidFunc, startFunc, performFunc, onStopFunc)
	timedAction = {}
	timedAction.name = name;
	timedAction.isValidFunc = isValidFunc;
	timedAction.startFunc = startFunc;
	timedAction.performFunc = performFunc;
	timedAction.onStopFunc = onStopFunc;
	return timedAction;
end

function PZ_startTimedAction(timedAction, character, maxTime, stopOnWalk, stopOnRun, param1, param2, param3, param4, param5)
	timedAction.stopOnWalk = stopOnWalk;
	timedAction.stopOnRun = stopOnRun;
	timedAction.character = character;
	timedAction.param1 = param1;
	timedAction.param2 = param2;
	timedAction.param3 = param3;
	timedAction.param4 = param4;
	timedAction.param5 = param5;
	timedAction.maxTime = maxTime;		
	timedAction.javaAction = LuaTimedAction.new(timedAction, character);
	timedAction.playLoopedSoundTillComplete = function(self, name, radius, gain) self.javaAction:PlayLoopedSoundTillComplete(name, radius, gain) end;
	character:StartAction(timedAction.javaAction);
end

TimedActions.Start = PZ_startTimedAction;
TimedActions.Create = PZ_createNewTimedAction;