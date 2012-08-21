require "ISBaseObject"

ISUIElement = ISBaseObject:derive("ISUIElement");

ISUIElement.IDMax = 1;

--************************************************************************--
--** ISUIElement:initialise
--**
--************************************************************************--
function ISUIElement:initialise()
	if self.data == nil then
		self.data =  {}
	end
	self.data.children = {}
	--	Give each UI element a unique ID.
	self.data.ID = ISUIElement.IDMax;
	ISUIElement.IDMax = ISUIElement.IDMax + 1;
end

--************************************************************************--
--** ISUIElement:setAnchorBottom
--**
--************************************************************************--
function ISUIElement:setAnchorBottom(bAnchor)
	self.data.anchorBottom = bAnchor;
	if self.data.javaObject ~= nil then
		self.data.javaObject:setAnchorBottom(bAnchor);
	end
end

--************************************************************************--
--** ISUIElement:setAnchorTop
--**
--************************************************************************--
function ISUIElement:setAnchorTop(bAnchor)
	self.data.anchorTop = bAnchor;
	if self.data.javaObject ~= nil then
		self.data.javaObject:setAnchorTop(bAnchor);
	end
end

--************************************************************************--
--** ISUIElement:setAnchorLeft
--**
--************************************************************************--
function ISUIElement:setAnchorLeft(bAnchor)
	self.data.anchorLeft = bAnchor;
	if self.data.javaObject ~= nil then
		self.data.javaObject:setAnchorLeft(bAnchor);
	end
end

--************************************************************************--
--** ISUIElement:setAnchorRight
--**
--************************************************************************--
function ISUIElement:setAnchorRight(bAnchor)
	self.data.anchorRight = bAnchor;
	if self.data.javaObject ~= nil then
		self.data.javaObject:setAnchorRight(bAnchor);
	end
end

--************************************************************************--
--** ISUIElement:setX
--**
--************************************************************************--
function ISUIElement:setX(x)
	self.data.x = x;
	if self.data.javaObject ~= nil then
		self.data.javaObject:setX(x);
	end
end

--************************************************************************--
--** ISUIElement:setY
--**
--************************************************************************--
function ISUIElement:setY(y)
	self.data.y = y;
	if self.data.javaObject ~= nil then
		self.data.javaObject:setX(y);
	end
end

--************************************************************************--
--** ISUIElement:setWidth
--**
--************************************************************************--
function ISUIElement:setWidth(w)
	self.data.width = w;
	if self.data.javaObject ~= nil then
		self.data.javaObject:setWidth(w);
	end
end

--************************************************************************--
--** ISUIElement:setHeight
--**
--************************************************************************--
function ISUIElement:setHeight(h)
	self.data.height = h;
	if self.data.javaObject ~= nil then
		self.data.javaObject:setHeight(h);
	end
end

--************************************************************************--
--** ISUIElement:getWidth
--**
--************************************************************************--
function ISUIElement:getWidth()
	if self.data.javaObject == nil then
		self:instantiate();
	end
	return self.data.javaObject:getWidth();
end

--************************************************************************--
--** ISUIElement:getHeight
--**
--************************************************************************--
function ISUIElement:getHeight()
	if self.data.javaObject == nil then
		self:instantiate();
	end
	return self.data.javaObject:getHeight();
end


--************************************************************************--
--** ISUIElement:getCentreX
--**
--************************************************************************--
function ISUIElement:getCentreX()
	return self:getWidth() / 2.0;
end

--************************************************************************--
--** ISUIElement:getCentreY
--**
--************************************************************************--
function ISUIElement:getCentreY()
	return self:getHeight() / 2.0;
end

--************************************************************************--
--** ISUIElement:getX
--**
--************************************************************************--
function ISUIElement:getX()
	if self.data.javaObject == nil then
		self:instantiate();
	end
	return self.data.javaObject:getX();
end

--************************************************************************--
--** ISUIElement:getY
--**
--************************************************************************--
function ISUIElement:getY()
	if self.data.javaObject == nil then
		self:instantiate();
	end
	return self.data.javaObject:getY();
end

--************************************************************************--
--** ISUIElement:getAbsoluteX
--**
--************************************************************************--
function ISUIElement:getAbsoluteX()
	if self.data.javaObject == nil then
		self:instantiate();
	end

	return self.data.javaObject:getAbsoluteX();
end

--************************************************************************--
--** ISUIElement:getAbsoluteY
--**
--************************************************************************--
function ISUIElement:getAbsoluteY()
	if self.data.javaObject == nil then
		self:instantiate();
	end
	return self.data.javaObject:getAbsoluteY();
end


--************************************************************************--
--** ISUIElement:setCapture
--**
--************************************************************************--
function ISUIElement:setCapture(bCapture)
	if self.data.javaObject == nil then
		self:instantiate();
	end
	self.data.javaObject:setCapture(bCapture);
end

--************************************************************************--
--** ISUIElement:getCapture
--**
--************************************************************************--
function ISUIElement:getIsCaptured()
	if self.data.javaObject == nil then
		self:instantiate();
	end
	return self.data.javaObject:isCapture();
end

--************************************************************************--
--** ISUIElement:setFollowGameWorld
--**
--************************************************************************--
function ISUIElement:setFollowGameWorld(bFollow)
	if self.data.javaObject == nil then
		self:instantiate();
	end
	self.data.javaObject:setFollowGameWorld(bFollow);
end

--************************************************************************--
--** ISUIElement:getCapture
--**
--************************************************************************--
function ISUIElement:getIsFollowGameWorld()
	if self.data.javaObject == nil then
		self:instantiate();
	end
	return self.data.javaObject:isFollowGameWorld();
end

--************************************************************************--
--** ISUIElement:setVisible
--**
--************************************************************************--
function ISUIElement:setVisible(bVisible)
	if self.data.javaObject == nil then
		self:instantiate();
	end
	self.data.javaObject:setVisible(bVisible);
end

--************************************************************************--
--** ISUIElement:getIsVisible
--**
--************************************************************************--
function ISUIElement:getIsVisible()
	if self.data.javaObject == nil then
		self:instantiate();
	end
	return self.data.javaObject:isVisible();
end


--************************************************************************--
--** ISUIElement:instantiate
--**
--************************************************************************--
function ISUIElement:instantiate()	

	self.data.javaObject = UIElement.new(self);	
	self.data.javaObject:setX(self.data.x);
	self.data.javaObject:setY(self.data.y);
	self.data.javaObject:setHeight(self.data.height);
	self.data.javaObject:setWidth(self.data.width);
	self.data.javaObject:setAnchorLeft(self.data.anchorLeft);
	self.data.javaObject:setAnchorRight(self.data.anchorRight);
	self.data.javaObject:setAnchorTop(self.data.anchorTop);
	self.data.javaObject:setAnchorBottom(self.data.anchorBottom);
end

--************************************************************************--
--** ISUIElement:drawTexture
--**
--************************************************************************--
function ISUIElement:drawTextureScaled(texture, x, y, w, h, a, r, g, b)
	if self.data.javaObject ~= nil then
		if r==nil then
			self.data.javaObject:DrawTextureScaled(texture, x, y, w, h, a);		
		else
			self.data.javaObject:DrawTextureScaledColor(texture, x, y, w, h, r, g, b, a);		
		end
	end
end

--************************************************************************--
--** ISUIElement:drawTexture
--**
--************************************************************************--
function ISUIElement:drawTexture(texture, x, y, a, r, g, b)
	if self.data.javaObject ~= nil then
		if r==nil then
			self.data.javaObject:DrawTexture(texture, x, y, a);		
		else
			self.data.javaObject:DrawTextureColor(texture, x, y, r, g, b, a);		
		end
	end
end

--************************************************************************--
--** ISUIElement:drawText
--**
--************************************************************************--
function ISUIElement:drawText(str, x, y, a, r, g, b, font)
	if self.data.javaObject ~= nil then
		if font ~= nil then
			self.data.javaObject:DrawText(font, str, x, y, r, g, b, a);
		else
			self.data.javaObject:DrawText(str, x, y, r, g, b, a);
		end
	end
end

--************************************************************************--
--** ISUIElement:drawTextCentre
--**
--************************************************************************--
function ISUIElement:drawTextCentre(str, x, y, r, g, b, a, font)
	if self.data.javaObject ~= nil then
		if font ~= nil then
			self.data.javaObject:DrawTextCentre(font, str, x, y, r, g, b, a);
		else
			self.data.javaObject:DrawTextCentre(str, x, y, r, g, b, a);
		end
	end
end

--************************************************************************--
--** ISUIElement:drawText
--**
--************************************************************************--
function ISUIElement:drawText(str, x, y, r, g, b, a, font)
	if self.data.javaObject ~= nil then
		if font ~= nil then
			self.data.javaObject:DrawText(font, str, x, y, r, g, b, a);
		else
			self.data.javaObject:DrawText(str, x, y, r, g, b, a);
		end
	end
end

--************************************************************************--
--** ISUIElement:drawTextRight
--**
--************************************************************************--
function ISUIElement:drawTextRight(str, x, y, r, g, b, a, font)
	if self.data.javaObject ~= nil then
		if font ~= nil then
			self.data.javaObject:DrawTextRight(font, str, x, y, r, g, b, a);
		else
			self.data.javaObject:DrawTextRight(str, x, y, r, g, b, a);
		end
	end
end

--************************************************************************--
--** ISUIElement:addToUIManager
--**
--************************************************************************--
function ISUIElement:addToUIManager()
	if self.data.javaObject == nil then
		self:instantiate();
	end
	
	UIManager.AddUI(self.data.javaObject);
end

--************************************************************************--
--** ISUIElement:removeFromUIManager
--**
--************************************************************************--
function ISUIElement:removeFromUIManager()
	if self.data.javaObject == nil then
		return;
	end
	
	UIManager.RemoveElement(self.data.javaObject);
end

--************************************************************************--
--** ISUIElement:addChild
--**
--************************************************************************--
function ISUIElement:addChild(otherElement)
	if self.data.javaObject == nil then
		self:instantiate();
	end
	if otherElement.data.javaObject == nil then
		otherElement:instantiate();
	end
	
	self.data.children[otherElement.data.ID] = otherElement;
	print("Adding child control to parent.");
	self.data.javaObject:AddChild(otherElement.data.javaObject);
end

--************************************************************************--
--** ISUIElement:removeChild
--**
--************************************************************************--
function ISUIElement:removeChild(otherElement)
	if self.data.javaObject == nil then
		self:instantiate();
	end
	if otherElement.data.javaObject == nil then
		otherElement:instantiate();
	end
	
	self.data.children[otherElement.data.ID] = nil;
	self.data.javaObject:RemoveChild(otherElement.data.javaObject);
end

--************************************************************************--
--** ISUIElement:clearChildren
--**
--************************************************************************--
function ISUIElement:clearChildren()
	if self.data.javaObject == nil then
		return;
	end	
	self.data.children = {}
	self.data.javaObject:ClearChildren();
end

--************************************************************************--
--** ISUIElement:onMouseUp
--**
--************************************************************************--
function ISUIElement:onMouseUp(x, y)

end

--************************************************************************--
--** ISUIElement:onMouseDown
--**
--************************************************************************--
function ISUIElement:onMouseDown(x, y)

end

--************************************************************************--
--** ISUIElement:onRightMouseUp
--**
--************************************************************************--
function ISUIElement:onRightMouseUp(x, y)

end

--************************************************************************--
--** ISUIElement:onRightMouseDown
--**
--************************************************************************--
function ISUIElement:onRightMouseDown(x, y)

end

--************************************************************************--
--** ISUIElement:onMouseMove
--**
--************************************************************************--
function ISUIElement:onMouseMove(dx, dy)

end

--************************************************************************--
--** ISUIElement:onMouseMoveOutside
--**
--************************************************************************--
function ISUIElement:onMouseMoveOutside(dx, dy)

end

--************************************************************************--
--** ISUIElement:update
--**
--************************************************************************--
function ISUIElement:update()
	
end

--************************************************************************--
--** ISUIElement:prerender
--**
--************************************************************************--
function ISUIElement:prerender()
	
end

--************************************************************************--
--** ISUIElement:render
--**
--************************************************************************--
function ISUIElement:render()
	
end

--************************************************************************--
--** ISUIElement:new
--**
--************************************************************************--
function ISUIElement:new (x, y, width, height)
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

