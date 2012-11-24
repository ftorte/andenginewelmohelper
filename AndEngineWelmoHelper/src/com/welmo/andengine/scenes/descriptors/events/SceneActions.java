package com.welmo.andengine.scenes.descriptors.events;

import com.welmo.andengine.scenes.components2.Stick;
import com.welmo.andengine.scenes.components2.Stick.StickMode;
import com.welmo.andengine.scenes.descriptors.components.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;

public class SceneActions extends BasicModifierDescriptor{
	public SceneActions() {
		super();
		// TODO Auto-generated constructor stub
	}
	public enum ActionType {
		NO_ACTION, CHANGE_SCENE,STICK,PLAY_SOUND,PLAY_MUSIC,CHANGE_Z_ORDER,FLIP
	}
	public enum ActionMode {
		NO_MODE, STICK_MERGE, 
	}
	
	//[FT] public SpritesEvents event = SpritesEvents.NO_EVENTS; 
	public ActionType type =ActionType.NO_ACTION;
	public String resourceName="";
	public String NextScene="";
	public int stick_with=0;
	public StickMode stickMode = Stick.StickMode.NO_STICK;
	public Events event;
	public int ZIndex;
	public int sideA;
	public int sideB;
	
}