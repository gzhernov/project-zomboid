ISBaseObject = {};

ISBaseObject.type = "ISBaseObject";

--************************************************************************--
--** ISBaseObject:initialise
--**
--************************************************************************--
function ISBaseObject:initialise()

end

--************************************************************************--
--** ISBaseObject:new
--**
--************************************************************************--
function ISBaseObject:derive (type)
    o = {}   
    setmetatable(o, self)
    self.__index = self
	o.Type= type;	
    return o
end