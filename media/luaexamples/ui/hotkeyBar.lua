require 'ui/uiHelpers'
require 'ui/hotkeySlot'
require 'luatimer'
	
HotkeyBar = {};

HotkeyBar.window = 
{		
	onButtonClicked = function(name)
		
	end,
	
	render = function(element) 
		
	end,
	slots = {},
	x = 100,
	y = 100,
	width = 406,
	height = 64,		
	hasClose = true;
};	

HotkeyBar.hotkeys = {};

HotkeyBar.layout = function()

	HotkeyBar.heightUsed = 64 + 16;
	local x = 10;
	local y = 20;
	for i,v in ipairs(HotkeyBar.window.slots) do
		if v.seperator ~= nil then
			x = x + 4;
		else
			if v.newline ~= nil then
				x = 10;
				y = y + 45 + 10;
				HotkeyBar.heightUsed = HotkeyBar.heightUsed + 45 + 10;
			else
				v.javaObject:setX(x);
				v.javaObject:setY(y);
				x = x + 32;
			end
		end
		
	end
end

HotkeyBar.addHotkeySlot = function(name, keycode, displaykey)	
	hotkey = HotkeySlots.create(name, keycode, displaykey, HotkeyBar.window);
	table.insert(HotkeyBar.window.slots, hotkey);	
	HotkeyBar.hotkeys[name] = hotkey;
	HotkeyBar.layout();
end

HotkeyBar.addSeperator = function()	
	hotkey = {seperator = {};}
	table.insert(HotkeyBar.window.slots, hotkey);	
	HotkeyBar.layout();
end

HotkeyBar.addNewLine = function()	
	hotkey = {newline = {};}
	table.insert(HotkeyBar.window.slots, hotkey);	
	HotkeyBar.layout();
end

HotkeyBar.update = function(ticks)
	for i,v in ipairs(HotkeyBar.window.slots) do
		if v.action ~= nil then
			if v.debounce == false and isKeyDown(v.keycode) then
				v.debounce = true;
				
				if v.action.performing == false then
					v.action.performAction(v.action);					
					v.action.performing = true;
				end

			else
				if not isKeyDown(v.keycode) then
					v.debounce = false;
				end
			end
		end
	end
end

HotkeyBar.initialise = function(self)
	print("Initialising hotkeys");
	HotkeyBar.addHotkeySlot("F1", Keyboard.KEY_F1, "F1", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("F2", Keyboard.KEY_F2, "F2", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("F3", Keyboard.KEY_F3, "F3", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("F4", Keyboard.KEY_F4, "F4", HotkeyBar.window);	
	HotkeyBar.addHotkeySlot("F5", Keyboard.KEY_F5, "F5", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("F6", Keyboard.KEY_F6, "F6", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("F7", Keyboard.KEY_F7, "F7", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("F8", Keyboard.KEY_F8, "F8", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("F9", Keyboard.KEY_F9, "F9", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("F10", Keyboard.KEY_F10, "F10", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("F11", Keyboard.KEY_F11, "F11", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("F12", Keyboard.KEY_F12, "F12", HotkeyBar.window);
	HotkeyBar.addNewLine();
	HotkeyBar.addHotkeySlot("1", Keyboard.KEY_1, "1", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("2", Keyboard.KEY_2, "2", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("3", Keyboard.KEY_3, "3", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("4", Keyboard.KEY_4, "4", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("5", Keyboard.KEY_5, "5", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("6", Keyboard.KEY_6, "6", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("7", Keyboard.KEY_7, "7", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("8", Keyboard.KEY_8, "8", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("9", Keyboard.KEY_9, "9", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("0", Keyboard.KEY_10, "10", HotkeyBar.window);
	HotkeyBar.addNewLine();
	HotkeyBar.addHotkeySlot("Q", Keyboard.KEY_Q, "Q", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("W", Keyboard.KEY_W, "W", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("E", Keyboard.KEY_E, "E", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("R", Keyboard.KEY_R, "R", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("T", Keyboard.KEY_T, "T", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("Y", Keyboard.KEY_Y, "Y", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("U", Keyboard.KEY_U, "U", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("I", Keyboard.KEY_I, "I", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("O", Keyboard.KEY_O, "O", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("P", Keyboard.KEY_P, "P", HotkeyBar.window);
	HotkeyBar.addNewLine();
	HotkeyBar.addHotkeySlot("A", Keyboard.KEY_A, "A", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("S", Keyboard.KEY_S, "S", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("D", Keyboard.KEY_D, "D", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("F", Keyboard.KEY_F, "F", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("G", Keyboard.KEY_G, "G", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("H", Keyboard.KEY_H, "H", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("J", Keyboard.KEY_J, "J", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("K", Keyboard.KEY_K, "K", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("L", Keyboard.KEY_L, "L", HotkeyBar.window);
	HotkeyBar.addNewLine();
	HotkeyBar.addHotkeySlot("Z", Keyboard.KEY_Z, "Z", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("X", Keyboard.KEY_X, "X", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("C", Keyboard.KEY_C, "C", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("V", Keyboard.KEY_V, "V", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("B", Keyboard.KEY_B, "B", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("N", Keyboard.KEY_N, "N", HotkeyBar.window);
	HotkeyBar.addHotkeySlot("M", Keyboard.KEY_M, "M", HotkeyBar.window);
	HotkeyBar.window.javaObject:setHeight(HotkeyBar.heightUsed);
end
	
HotkeyBar.createHotkeyBar = function()
	LuaUI.createWindow(HotkeyBar.window);		
	HotkeyBar:initialise();
	
	local world = getWorld();
	local cell = world:getCell();
	local stack = cell:getZoneStack();
	
	local it = stack:iterator();
	
	while(it:hasNext()) do
		local zone = it:next();
		print(zone:getX());
	end
end

HotkeyBar.save = function()
	output = getFileOutput("hotkeys/hotkeys.data");
	
	local count = 0;
	for i,v in ipairs(HotkeyBar.window.slots) do
		if v.action ~= nil and v.newline == nil and v.seperator==nil then
			count = count + 1;
		end
	end	
	
	output:writeInt(count);
	
	for i,v in ipairs(HotkeyBar.window.slots) do
		if v.action ~= nil  and v.newline == nil and v.seperator==nil then
			print("Saving "..v.action.name);
			output:writeUTF(v.name);
			v:save(output);
		end
	end
	
	endFileOutput();
end

HotkeyBar.load = function()
	input = getFileInput("hotkeys/hotkeys.data");
	if input ~= nil then
		
		local count = input:readInt();
		print("Loading "..count.." hotkeys.");
		for i = 0, count do
			str = input:readUTF();
			print(str);
			v = HotkeyBar.hotkeys[str];		
			v:load(input);
		end
					
		endFileInput();
	end
end

Events.OnSave.Add(HotkeyBar.save);
Events.OnLoad.Add(HotkeyBar.load);

Events.OnCreateUI.Add(HotkeyBar.createHotkeyBar);

Events.OnTick.Add(HotkeyBar.update);