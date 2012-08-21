require "ISUI/ISWindow"
require "ISUI/ISInventoryFlow"

ISTestWindow = ISWindow:derive("ISTestWindow");

--************************************************************************--
--** ISTestWindow:initialise
--**
--************************************************************************--
function ISTestWindow:initialise()
	ISWindow.initialise(self);
	print("Instancing window.");
	
	local flow = ISInventoryFlow:new(0, 0, 0, 0);
	flow:initialise();
	flow:instantiate();
	-- Add as a toolbar.
	self:addToolbar(flow, 16);
	
	
end

--************************************************************************--
--** ISTestWindow:render
--**
--************************************************************************--
function ISTestWindow:render()
	ISWindow.render(self); -- Call the parent's render to draw title.
	
end
