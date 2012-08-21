require 'ui/uiHelpers'

------------------------------------------------------------------------
-- Example of creating a window with a button on it
------------------------------------------------------------------------

clickCounter = 0;

-- UI objects
	mywindow = 
	{		
		onButtonClicked = function(name)
			clickCounter = clickCounter + 1;
		end,
		
		render = function(element) 
			element:DrawTextCentre("Has been clicked "..clickCounter.." times.", 150, 100, 1, 0, 0, 1);
		end,
		
		x = 100,
		y = 100,
		width = 300,
		height = 300,		
		hasClose = true;
	};	
	
	mybutton = 
	{
		parent=mywindow,
		name="testButton",
		text="Bob's Button",
		x=150,
		y=250;		
	}
	
	mygenericelement = 
	{		
		x=150,
		y=250,
		width=120,
		height=120,
		render = function(element)
			element:DrawTextCentre("Test Render", 150, 100, 1, 0, 0, 1);
			player = getPlayer();
			
			equippeditem = player:getPrimaryHandItem();
			
			if equippeditem ~= nil then
				texture = equippeditem:getTexture();
				element:DrawTexture(texture, 0, 0, 0.3);
			end
		end;		
	}

function CreateTest()

	--LuaUI.createWindow(mywindow);
	--LuaUI.createButton(mybutton);
	--element = LuaUI.createElement(mygenericelement);
	
end
