require 'ui/uiHelpers'
require 'ui/actionOptionsWindow'

UIActions = {}
UIActions.onScreenActions = {};

UIActions.renderAction = function (elementTable, element)
	if elementTable.performing then
		element:DrawTexture(elementTable.texture, 0, 0, 0.5);
	else
		element:DrawTexture(elementTable.texture, 0, 0, 1.0);
	end
end

UIActions.updateAction = function (elementTable)
	if elementTable.performing and elementTable.actionFinished(elementTable) then
		elementTable.performing = false;
	end
end

UIActions.onMouseDownAction = function(elementTable, x, y)
	if UIActions.draggingAction == nil then
		elementTable.clickX = x;
		elementTable.clickY = y;
		elementTable.startDragX = elementTable.javaObject:getX();
		elementTable.startDragY = elementTable.javaObject:getY();
		elementTable.dragging = true;
		elementTable.javaObject:setConsumeMouseEvents(false);
		elementTable.movedSinceClick = false;
		UIActions.draggingAction = elementTable;
	end
end

UIActions.onMouseUpAction = function(elementTable, x, y)

	if Math.abs(elementTable.clickX - x) < 2 and Math.abs(elementTable.clickY - y) < 2 then
		if elementTable.performing == false and elementTable.movedSinceClick == false then
			elementTable.performAction(elementTable);
			
			elementTable.performing = true;
		end
		elementTable.javaObject:setConsumeMouseEvents(true);
	end	
	print("Dropping action.");
	elementTable.dragging = false;		
	UIActions.lastDraggingAction = UIActions.draggingAction;
	UIActions.draggingAction = nil;
end

UIActions.onRightMouseDownAction = function(elementTable, x, y)
	
end

UIActions.onRightMouseUpAction = function(elementTable, x, y)

	if ActionOptions.activeOptions ~= nil then
		ActionOptions.activeOptions:setVisible(false);
		ActionOptions.activeOptions = nil;
	end
	if elementTable.optionsUI ~= nil then
		if elementTable.optionsUIInst == nil then
			print("Cloning option window.");
			elementTable.optionsWindow = ActionOptions.window:clone();			
			elementTable.optionsWindow.action = elementTable;			
			elementTable.optionsUI.action = elementTable;			
			print("Loading options panel");
			elementTable.optionsUIInst = LuaUI.createWindow(elementTable.optionsWindow);		
			print("Setting option window parent.");
			elementTable.optionsUI.parent = elementTable.optionsWindow;
			print("Creating element");
			LuaUI.createElement(elementTable.optionsUI);
			print("Setting window dimensions");
			elementTable.optionsWindow.javaObject:setWidth(elementTable.optionsUI.width+10);
			elementTable.optionsWindow.javaObject:setHeight(elementTable.optionsUI.height+32);
			print("Setting window non moveable");
			
			elementTable.optionsUIInst:setMovable(false);
			print("Setting window position");
			elementTable.optionsUIInst:setX(elementTable.javaObject:getAbsoluteX());
			elementTable.optionsUIInst:setY(elementTable.javaObject:getAbsoluteY()+32);
			ActionOptions.activeOptions = elementTable.optionsUIInst;
		else
			print("Reopening options panel");
			elementTable.optionsUIInst:setVisible(true);
			elementTable.optionsUI.action = elementTable;			
			elementTable.optionsUIInst:setX(elementTable.javaObject:getAbsoluteX());
			elementTable.optionsUIInst:setY(elementTable.javaObject:getAbsoluteY()+32);
			ActionOptions.activeOptions = elementTable.optionsUIInst;
		end
	end
end

UIActions.onMouseMoveAction = function(elementTable, x, y)
	if elementTable.dragging then
		if elementTable.parent ~= nil then
			elementTable.parent.action = nil;
			print("Detaching action from hotbar.");
			local x = elementTable.javaObject:getAbsoluteX();
			local y = elementTable.javaObject:getAbsoluteY();
			elementTable.parent.javaObject:RemoveControl(elementTable.javaObject);
			print("Successfully detached. Now setting absolute x,y");
			elementTable.parent = nil;
			elementTable.javaObject:setX(x);
			elementTable.javaObject:setY(y);
			UIManager.AddUI(elementTable.javaObject);
			print("Adding onscreen element ".. elementTable.ID);
			UIActions.onScreenActions[elementTable.ID] = elementTable;
		end
	
		elementTable.javaObject:setX(elementTable.javaObject:getX()+x);
		elementTable.javaObject:setY(elementTable.javaObject:getY()+y);
		elementTable.movedSinceClick = true;
		UIActions.draggingAction = elementTable;
		if not elementTable.dragging then
			UIActions.lastDraggingAction = nil;
			UIActions.draggingAction = nil;
		end
	end
end

UIActions.IDMax = 1;
UIActions.createNewAction = function(name, title, textureFilename, save, load, performAction, actionIsFinished, renderFunc, optionsUI, doAddToUI)
	print("Creating new action "..name);
	local action = 
	{	
		name = name,
		title = title,
		performing = false,
		dragging = false,
		x=150,
		y=250,
		width=32,
		height=32,		
		render = UIActions.renderAction,
		update = UIActions.updateAction,
		onMouseDown = UIActions.onMouseDownAction,
		onMouseUp = UIActions.onMouseUpAction,
		onMouseMove = UIActions.onMouseMoveAction,
		onRightMouseDown = UIActions.onRightMouseDownAction,
		onRightMouseUp = UIActions.onRightMouseUpAction,
		onMouseMoveOutside = UIActions.onMouseMoveAction,
		texture = getTexture(textureFilename),
		performAction = performAction,
		save = save,
		load = load,
		actionFinished = actionIsFinished,
		optionsUI = optionsUI;
	}
	if renderFunc ~= nil then
		action.render = renderFunc;
	end
	action.optionsUI.action = action;
	element = LuaUI.createElement(action, doAddToUI);
	element:setAlwaysOnTop(true);
	action.ID = UIActions.IDMax;
	UIActions.IDMax = UIActions.IDMax + 1;
	if doAddToUI == nil or doAddToUI == true then
		print("Adding "..action.name.." to onscreen list.");
		UIActions.onScreenActions[action.ID] = action;
	end
	return action;
end

UIActions.save = function()
	output = getFileOutput("hotkeys/onscreenaction.data");
	
	local count = 0;
	for i,v in ipairs(UIActions.onScreenActions) do
		count = count + 1;
	end	
	
	output:writeInt(count);
	
	for i,v in ipairs(UIActions.onScreenActions) do
	print("Saving "..v.name);
		output:writeUTF(v.name);		
		v:save(output);
		output:writeInt(v.javaObject:getX());
		output:writeInt(v.javaObject:getY());
	end
	
	endFileOutput();
end

UIActions.load = function()
	input = getFileInput("hotkeys/onscreenaction.data");
	if input ~= nil then
		
		local count = input:readInt();

		print("Loading "..count.." onscreen actions.");
		for i = 0, count do
			str = input:readUTF();
			
			v = ActionFactory.createFromName(str);
			v:load(input);
			v.javaObject:setX(input:readInt());
			v.javaObject:setY(input:readInt());
			v:addToUI();
			UIActions.onScreenActions[v.ID] = v;
		end
					
		endFileInput();
	end
end

Events.OnSave.Add(UIActions.save);
Events.OnLoad.Add(UIActions.load);


