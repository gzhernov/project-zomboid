
------------------------------------------------------------------------
-- Generic creation functions - will be part of a Lua library in time.
------------------------------------------------------------------------
LuaUI = {}
LuaUI.createWindow = function (data)

	luawindow = LuaUIWindow.new(data.x, data.y, data.width, data.height, data.hasClose, data);	
	data.javaObject = luawindow;
	UIManager.AddUI(luawindow);
	

	if data.doCreation ~= nil then
		data:doCreation();
	end
		
	return data.javaObject;
end

LuaUI.createButton = function (data)
	if data.parent ~= nil then
		luabutton = DialogButton.new(data.parent.javaObject, data.x, data.y, data.text, data.name);	
		data.javaObject = luabutton;
		data.parent.javaObject:AddChild(data.javaObject);
	else
		luabutton = DialogButton.new(nil, data.x, data.y, data.text, data.name);	
		data.javaObject = luabutton;
		UIManager.AddUI(luabutton);
	end
	
	if data.doCreation ~= nil then
		data:doCreation();
	end
	
	return data.javaObject;
end

LuaUI.createElement = function (data, createElement)
	luaelement = {};
	if data.parent ~= nil then
		luaelement = UIElement.new(data);	
		data.javaObject = luaelement;
		if createElement == nil or createElement == true then
			data.parent.javaObject:AddChild(data.javaObject);
		else
			data.addToUI = function(self) self.parent.javaObject:AddChild(self.javaObject); end
		end

	else
		luaelement = UIElement.new(data);	
		data.javaObject = luaelement;
		if createElement == nil or createElement == true then
			UIManager.AddUI(luaelement);
		else
			data.addToUI = function(self) UIManager.AddUI(self.javaObject); end
		end

	end
	
	luaelement:setX(data.x);
	luaelement:setY(data.y);	
	luaelement:setWidth(data.width);
	luaelement:setHeight(data.height);
	
	if data.doCreation ~= nil then
		data:doCreation();
	end
	
	return data.javaObject;
end
