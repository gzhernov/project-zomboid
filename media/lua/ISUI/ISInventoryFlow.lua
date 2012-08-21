require "ISUI/ISUIElement"

ISInventoryFlow = ISUIElement:derive("ISInventoryFlow");

--************************************************************************--
--** ISInventoryFlow:initialise
--**
--************************************************************************--
function ISInventoryFlow:initialise()
	ISUIElement.initialise(self);

end


--************************************************************************--
--** ISInventoryFlow:render
--**
--************************************************************************--
function ISInventoryFlow:render()
	ISUIElement.render(self); -- Call the parent's render to draw title.
	
	local texture = getTexture("media/ui/PZ_Logo.png");

	self:drawTextureScaled(texture, 0, 0, self:getWidth(), self:getHeight(), 1.0);

end

--************************************************************************--
--** ISInventoryFlow:new
--**
--************************************************************************--
function ISInventoryFlow:new (x, y, width, height)
    o = {}  
	o.data = {};
    setmetatable(o, self)
    self.__index = self	
	o.data.x = x;
	o.data.y = y;
	o.data.width = width;
	o.data.height = height;
	o.data.anchorLeft = true;
	o.data.anchorRight = false;
	o.data.anchorTop = true;
	o.data.anchorBottom = false;		
   return o
end
