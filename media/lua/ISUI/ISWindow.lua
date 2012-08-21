require "ISUI/ISUIElement"

ISWindow = ISUIElement:derive("ISWindow");

ISWindow.TitleBarHeight = 19;
ISWindow.SideMargin = 12;
ISWindow.BottomMargin = 12;

--************************************************************************--
--** ISWindow:initialise
--**
--************************************************************************--

function ISWindow:initialise()
	ISUIElement.initialise(self);
	
	self.data.hasClose = true;
	self.data.toolbars = {};
end

--************************************************************************--
--** ISWindow:onMouseUp
--**
--************************************************************************--
function ISWindow:onMouseUp(x, y)

	if(self.data.resizing) then
		self:setCapture(false);
	end
	self.data.resizing = false;
	
end

--************************************************************************--
--** ISWindow:onMouseDown
--**
--************************************************************************--
function ISWindow:onMouseDown(x, y)
	if x + ISWindow.SideMargin > self:getWidth() and y + ISWindow.SideMargin > self:getHeight() then
		self.data.resizing = true;
		self.data.mDownX = x;
		self.data.mDownY = y;
		self:setCapture(true);
	end

end
--************************************************************************--
--** ISWindow:onMouseMove
--**
--************************************************************************--
function ISWindow:onMouseMove(dx, dy)
	if self.data.resizing then
		self:setWidth(self:getWidth()+dx);
		self:setHeight(self:getHeight()+dy);
	end

end

--************************************************************************--
--** ISWindow:onMouseMoveOutside
--**
--************************************************************************--
function ISWindow:onMouseMoveOutside(dx, dy)
	if self.data.resizing then
		self:setWidth(self:getWidth()+dx);
		self:setHeight(self:getHeight()+dy);
	end

end

--************************************************************************--
--** ISWindow:getClientLeft
--**
--************************************************************************--
function ISWindow:getClientLeft()
	return ISWindow.SideMargin;
end

--************************************************************************--
--** ISWindow:getClientRight
--**
--************************************************************************--
function ISWindow:getClientRight()
	return self:getWidth() - ISWindow.SideMargin;
end

--************************************************************************--
--** ISWindow:getClientBottom
--**
--************************************************************************--
function ISWindow:getClientBottom()
	return self:getHeight() - ISWindow.BottomMargin;
end

--************************************************************************--
--** ISWindow:getClientTop
--**
--************************************************************************--
function ISWindow:getClientTop()
	return self:getNClientTop() + self:getTotalToolbarHeight();
end

--************************************************************************--
--** ISWindow:getNClientTop
--**
--************************************************************************--
function ISWindow:getNClientTop()
	return ISWindow.TitleBarHeight;
end

--************************************************************************--
--** ISWindow:getTotalToolbarHeight
--**
--************************************************************************--
function ISWindow:getTotalToolbarHeight()
	local total = 0;
	for i, element in ipairs(self.data.toolbars) do
		total = total + element:getHeight();
	end
	
	return total;
end

--************************************************************************--
--** ISWindow:addToolbar
--**
--************************************************************************--
function ISWindow:addToolbar(toolbar, height)
	toolbar:setX(0);
	toolbar:setY(self:getClientTop());
	toolbar:setWidth(self:getWidth());
	toolbar:setHeight(height);
	toolbar:setAnchorBottom(true);
	toolbar:setAnchorRight(true);
	toolbar:setAnchorLeft(true);
	toolbar:setAnchorTop(true);	
	toolbar.isToolbar = true;	
	self.data.toolbars[toolbar.ID] = toolbar;

	self:addChild(toolbar);	
end

--************************************************************************--
--** ISWindow:removeToolbar
--**
--************************************************************************--
function ISWindow:removeToolbar(toolbar)
	self.data.toolbars[toolbar.ID] = nil;
	self:removeChild(toolbar);
	toolbar.isToolbar = false;
end

--************************************************************************--
--** ISWindow:getClientWidth
--**
--************************************************************************--
function ISWindow:getClientWidth()
	return self:getWidth() - (ISWindow.SideMargin*2);
end
--************************************************************************--
--** ISWindow:getClientHeight
--**
--************************************************************************--
function ISWindow:getClientHeight()
	return self:getHeight() - (ISWindow.BottomMargin+ISWindow.TitleBarHeight);
end

--************************************************************************--
--** ISWindow:render
--**
--************************************************************************--
function ISWindow:render()
	self:drawTextCentre(self.data.title, self:getCentreX(), 3, 1, 1, 1, 1);
end

--************************************************************************--
--** ISWindow:instantiate
--**
--************************************************************************--
function ISWindow:instantiate()

	self:initialise();
	self.data.javaObject = LuaUIWindow.new(self.data.x, self.data.y, self.data.width, self.data.height, self.data.hasClose, self);	

end

--************************************************************************--
--** ISWindow:new
--**
--************************************************************************--
function ISWindow:new (title, x, y, width, height)
    o = {}  
	o.data = {}
    setmetatable(o, self)
    self.__index = self
	o.data.title = title;
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

