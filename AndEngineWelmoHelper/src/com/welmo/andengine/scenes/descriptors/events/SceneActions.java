package com.welmo.andengine.scenes.descriptors.events;

import com.welmo.andengine.scenes.components.Stick;
import com.welmo.andengine.scenes.components.Stick.StickMode;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;

public class SceneActions extends BasicModifierDescriptor{
	public static final int				DEFAULT_FLIP_TIME = 1000;
	
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
	public float flipTime;
	
	
	public SceneActions() {
		super();
		// TODO Auto-generated constructor stub
		flipTime = DEFAULT_FLIP_TIME;
	}
	public enum ActionType {
		NO_ACTION, CHANGE_SCENE,STICK,PLAY_SOUND,PLAY_MUSIC,CHANGE_Z_ORDER,FLIP,DISABLE_SCENE_TOUCH,
		ENABLE_SCENE_TOUCH,ON_MOVE_FOLLOW
	}
	public enum ActionMode {
		NO_MODE, STICK_MERGE, 
	}
	
}