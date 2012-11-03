package com.welmo.andengine.scenes.descriptors.components;

public class ScnTags {
	//--------------------------------------------------------
	// XML TAGS List
	//--------------------------------------------------------
	//--------------------------------------------------------
	//Scene	
	public final static String S_CLASS_NAME			= "class_name";
	public final static String S_A_NAME				= "name";
	public final static String S_SCENE 				= "scene";
	public final static String S_SCENES 			= "scenes";
	public final static String S_MULTIVIEWSCENE 	= "multiviewscene";
	public final static String S_FATHER				= "sceneFather";
	
	//--------------------------------------------------------
	//Scene Objects
	public final static String S_SPRITE 			= "sprite";
	public final static String S_CLICKABLE_SPRITE 	= "clickable_sprite";
	public final static String S_ANIMATED_SPRITE 	= "animated_sprite";
	public final static String S_COMPOUND_SPRITE 	= "compound_sprite";
	public final static String S_TEXT 				= "text";
	public final static String S_BACKGROUND 		= "background";
	
	//Event handler
	public final static String S_ACTION 			= "action";
	public final static String S_MODIFIERS_SET 		= "modifiers_set";
	public final static String S_EVENT_HANDLER 		= "event_handler";
	public final static String S_MODIFIER 			= "modifier";
	//--------------------------------------------------------
	//Scene Objects' attributes

	public final static String S_A_STICK_WITH		= "stick_with";
	public final static String S_A_STICK_MODE		= "stick_mode";
	
	//Scene Objects' parameters
	public final static String S_A_NEXT_SCENE		= "next_scene";
	public final static String S_A_SCENE_CHANGE 	= "scene_change";
	public final static String S_A_ID 				= "ID";
	public final static String S_A_TYPE				= "type";
	public final static String S_A_RESOURCE_NAME 	= "resourceName";
	
	public final static String S_A_EVENT 			= "event";
	public final static String S_A_MOVE_FACTOR 		= "move_factor";
	public final static String S_A_SCALE_FACTOR 	= "scale_factor";
	public final static String S_A_SCALE_BEGIN 		= "scale_begin";
	public final static String S_A_SCALE_END		= "scale_end";
	
	public final static String S_A_HEIGHT			= "height";
	public final static String S_A_WIDTH			= "width";
	public final static String S_A_POSITION_X		= "px";
	public final static String S_A_POSITION_Y		= "py";
	public final static String S_A_Z_ORDER			= "pz";
	public final static String S_A_ORIENTATION		= "orientation";
	public final static String S_A_ROTAION_CENTER_X	= "rX";
	public final static String S_A_ROTAION_CENTER_Y	= "rX";
	public final static String S_A_FILE_NAME		= "filename";
	

	public final static int S_A_HEIGHT_IDX			= 0;
	public final static int S_A_WIDTH_IDX			= 1;
	public final static int S_A_POSITION_X_IDX		= 2;
	public final static int S_A_POSITION_Y_IDX		= 3;
	
	//Font
	public final static String S_A_MESSAGE 			= "message";
	public final static String S_A_COLOR			= "color";
	
	//Alignement
	public final static String S_A_H_ALIGNEMENT 	= "h_alignment";
	public final static String S_A_V_ALIGNEMENT 	= "v_alignment";
	
	//event handler
	public final static String S_A_TYPE_SET 		="typeSet";
}
