package com.welmo.andengine.scenes.descriptors.events;

import org.xml.sax.Attributes;

import android.util.Log;

import com.welmo.andengine.scenes.components.Stick;
import com.welmo.andengine.scenes.components.Stick.StickMode;
import com.welmo.andengine.scenes.descriptors.ScnTags;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;

public class SceneActions extends BasicModifierDescriptor{
	public static final int				DEFAULT_FLIP_TIME = 1000;
	public static final String			TAG="SceneActions";
	
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
	public boolean bChangeToFather				= false;
	public boolean bChangeToChild 				= false;
	
	
	public SceneActions() {
		super();
		// TODO Auto-generated constructor stub
		flipTime = DEFAULT_FLIP_TIME;
		bChangeToFather = false;
	}
	public enum ActionType {
		NO_ACTION, CHANGE_SCENE,STICK,PLAY_SOUND,PLAY_MUSIC,CHANGE_Z_ORDER,FLIP,DISABLE_SCENE_TOUCH,
		ENABLE_SCENE_TOUCH,ON_MOVE_FOLLOW, CHANGE_TO_FATHER_SCENE, CHANGE_TO_CHILD_SCENE
	}
	public enum ActionMode {
		NO_MODE, STICK_MERGE, 
	}
	@Override
	public void readXMLDescription(Attributes attributes) {

		Log.i(TAG,"\t\t readComponentActionDescriptor");

		//SceneActions newAction = new SceneActions();
		String value;
		if((value = attributes.getValue(ScnTags.S_A_TYPE))!=null){
			this.type = ActionType.valueOf(value);
			switch(this.type){
			case PLAY_SOUND:
				this.resourceName = new String(attributes.getValue(ScnTags.S_A_RESOURCE_NAME));
				break;
			case CHANGE_TO_FATHER_SCENE:
				this.bChangeToFather = true;
				break;
			case CHANGE_TO_CHILD_SCENE:
				this.bChangeToChild = true;
				this.NextScene = new String(attributes.getValue(ScnTags.S_A_NEXT_SCENE));
				break;
			case CHANGE_SCENE:
				this.NextScene = new String(attributes.getValue(ScnTags.S_A_NEXT_SCENE));
				break;
			case CHANGE_Z_ORDER:
				this.ZIndex = Integer.parseInt(attributes.getValue(ScnTags.S_A_Z_ORDER));
				break;
			case FLIP:
				if(attributes.getValue(ScnTags.S_A_FLIP_TIME) != null)
					this.flipTime = Integer.parseInt(attributes.getValue(ScnTags.S_A_FLIP_TIME));
				break;
			case ON_MOVE_FOLLOW:
				break;
			case STICK:	
				this.stick_with=Integer.parseInt(attributes.getValue(ScnTags.S_A_STICK_WITH));
				this.stickMode=Stick.StickMode.valueOf(attributes.getValue(ScnTags.S_A_STICK_MODE));
				break;
			default:
				break;
			}
		}
		if((value = attributes.getValue(ScnTags.S_A_EVENT))!=null){
			this.event = ComponentEventHandlerDescriptor.Events.valueOf(value);
		}
	}
}