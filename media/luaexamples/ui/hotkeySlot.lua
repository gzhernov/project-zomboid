require 'ui/uiHelpers'
require 'ui/actionUIElement'
	
HotkeySlots = {}

HotkeySlots.prerender = function(elementTable, element)
	element:DrawTexture(elementTable.texture, 0, 0, 1);	
end
HotkeySlots.render = function(elementTable, element)
	element:DrawTextCentre(elementTable.keycodeDisplay, 16, 33, 1, 1, 1, 1);	
end

HotkeySlots.save = function(self, output)
	if self.action ~= nil then
		output:writeUTF(self.action.name);
		
		self.action:save(output);

	end
end
HotkeySlots.load = function(self, input)	
		local actionName = input:readUTF();
		self.action = ActionFactory.createFromName(actionName);
		
		print("Loading "..actionName);
		self.action:load(input);
		self.javaObject:ClearChildren();
		self.javaObject:AddChild(self.action.javaObject);
		self.action.parent = self;
		self.action.javaObject:setX(0);
		self.action.javaObject:setY(0);
		self.action.dragging = false;		
	
		UIActions.draggingAction = nil;
		UIActions.lastDraggingAction = nil;
	
end

HotkeySlots.onMouseDownAction = function(elementTable, x, y)
	UIActions.lastDraggingAction = nil;
end

HotkeySlots.onMouseUpAction = function(elementTable, x, y)
	if UIActions.lastDraggingAction ~= nil then
		-- there's already one in this slot, so swap places.
		if elementTable.action ~= nil then
			local x = UIActions.lastDraggingAction.startDragX;
			local y = UIActions.lastDraggingAction.startDragY;
			elementTable.javaObject:RemoveControl(elementTable.action.javaObject);
			print("Successfully detached. Now setting absolute x,y");
			elementTable.action.parent = nil;
			elementTable.action.javaObject:setX(x);
			elementTable.action.javaObject:setY(y);
			UIManager.AddUI(elementTable.action.javaObject);
			print("Adding onscreen element ".. elementTable.action.ID);
			UIActions.onScreenActions[elementTable.action.ID] = elementTable.action;
		end
		print("Removing element");
		UIManager.RemoveElement(UIActions.lastDraggingAction.javaObject);
		print("Adding element as child.");
		elementTable.action = UIActions.lastDraggingAction;
		UIActions.onScreenActions[elementTable.action.ID] = nil;
		elementTable.javaObject:ClearChildren();
		elementTable.javaObject:AddChild(elementTable.action.javaObject);
		elementTable.action.parent = elementTable;
		elementTable.action.javaObject:setX(0);
		elementTable.action.javaObject:setY(0);
		elementTable.action.dragging = false;	
	else
		print("No action to drop.");
	end
	
	UIActions.draggingAction = nil;
	UIActions.lastDraggingAction = nil;
end

HotkeySlots.create = function(name, keycode, keycodeDisplay, parent)
	
	hotkey = 
	{	
		parent = parent,
		x=0,
		y=0,
		width=32,
		height=32,
		name = name,
		keycode = keycode,
		keycodeDisplay = keycodeDisplay,
		debounce=false,
		render = HotkeySlots.render,
		prerender = HotkeySlots.prerender,
		save = HotkeySlots.save,
		load = HotkeySlots.load,
		texture = getTexture("ItemBackground_Blue"),
		onMouseUp = HotkeySlots.onMouseUpAction,
		onMouseDown = HotkeySlots.onMouseDownAction,
	}
	element = LuaUI.createElement(hotkey);
	
	return hotkey;
end