package com.welmo.andengine.scenes.descriptors.events;

import com.welmo.andengine.scenes.components.Stick;
import com.welmo.andengine.scenes.components.Stick.StickMode;
import com.welmo.andengine.scenes.descriptors.components.BasicDescriptor;

public class SceneActionsSet extends EventHandlerDescriptor{
	public SceneActionsSet() {
		super();
		// TODO Auto-generated constructor stub
	}
	public enum ActionType {
		NO_ACTION, CHANGE_SCENE,STICK
	}
	public enum ActionMode {
		NO_MODE, STICK_MERGE, 
	}
	
	//[FT] public SpritesEvents event = SpritesEvents.NO_EVENTS; 
	public ActionType type =ActionType.NO_ACTION;
	public String NextScene="";
	public int stick_with=0;
	public StickMode stickMode = Stick.StickMode.NO_STICK;
	public Events event;
}