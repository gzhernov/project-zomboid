require "ISBaseObject"

ISTimer = ISBaseObject:derive("ISTimer");

ISTimer.IDMax = 1;
ISTimer.Timers = {};

--************************************************************************--
--** ISTimer:initialise
--**
--************************************************************************--
function ISTimer:initialise()
	self:reset();
	self.data.ID = ISTimer.IDMax;
	ISTimer.IDMax = ISTimer.IDMax + 1;
	self.data.realWorldTime = false;
	self.data.repeats = false;
end

--************************************************************************--
--** ISTimer:reset
--**
--************************************************************************--
function ISTimer:reset()
	self.data.elapsed = 0;
	self.data.active = false;
end

--************************************************************************--
--** ISTimer:start
--**
--************************************************************************--
function ISTimer:start()
	self.data.active = true;
	ISTimer.Timers[self.data.ID] = self;
end

--************************************************************************--
--** ISTimer:start
--**
--************************************************************************--
function ISTimer:stop()
	self.data.active = false;
	ISTimer.Timers[self.data.ID] = nil;
end

--************************************************************************--
--** ISTimer:new
--**
--************************************************************************--
function ISTimer:new (name, objectOnExpire, funcOnExpire, timeInSeconds, repeats, realWorldTime)
    o = {}  
	o.data = {};
    setmetatable(o, self)
    self.__index = self
	o:initialise();
	o.data.name = name;
	if objectOnExpire ~= nil then
		o.data.classOnExpire = objectOnExpire;
	end
	if funcOnExpire ~= nil then
		o.data.funcOnExpire = funcOnExpire;
	end
	if timeInSeconds ~= nil then
		o.data.timeInSeconds = timeInSeconds;
	end
	if repeats ~= nil then
		o.data.repeats = repeats;
	end
	if realWorldTime ~= nil then
		o.data.realWorldTime = realWorldTime;
	end
   return o
end

--************************************************************************--
--** Static creation function called on tick.
--**
--************************************************************************--
ISTimer.updateTimers = function(ticks)

	for i,v in ipairs(ISTimer.Timers) do		
		if v.active then
			if v.realWorldTime then
				v.elapsed = v.elapsed + GameTime.getInstance():getRealworldSecondsSinceLastUpdate();	
			else
				v.elapsed = v.elapsed + GameTime.getInstance():getMultipliedSecondsSinceLastUpdate();	
			end
			if v.timeInSeconds ~= nil and v.elapsed >= v.timeInSeconds then
				if v.funcOnExpire ~= nil then
					v.funcOnExpire(v.classOnExpire);
				end
				if not v.repeats then
					v:stop();
					ISTimer.Timers[i] = nil;
				else
					v:reset();
					v:start();
				end
				
			end
		end
	end
end

Events.OnTick.Add(ISTimer.updateTimers);