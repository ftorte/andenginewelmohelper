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
	public final static String S_SCENE_INSTANTIATION= "instantiatescene";
	public final static String S_MULTIVIEWSCENE 	= "multiviewscene";
	public final static String S_FATHER				= "sceneFather";
	
	//--------------------------------------------------------
	//Scene Objects
	public final static String S_SPRITE 			= "sprite";
	public final static String S_CLICKABLE_SPRITE 	= "clickable_sprite";
	public final static String S_ANIMATED_SPRITE 	= "animated_sprite";
	public final static String S_COMPOUND_SPRITE 	= "compound_sprite";
	public final static String S_COLORING_SPRITE	= "coloring_sprite";
	public final static String S_TEXT 				= "text";
	public final static String S_BACKGROUND 		= "background";
	public final static String S_PUZZLE_SPRITE		= "puzzle_sprite";
	public final static String S_HUD				= "HUD";
	
	//Event handler
	public final static String S_ACTION 			= "action";
	public final static String S_EVENT_HANDLER 		= "event_handler";
	public final static String S_CLONED_EVENT_HANDLER = "cloned_event_handler";
	public final static String S_MODIFIER_LIST 		= "modifier_list";
	public final static String S_MODIFIER 			= "modifier";
	public final static String S_PRE_MOD_ACTION		= "pre_mod_action";
	public final static String S_POST_MOD_ACTION	= "post_mod_action";
	public final static String S_ON_MOD_ACTION		= "on_mod_action";
	
	//--------------------------------------------------------
	//Scene instantiation specific parameters
	public final static String  S_A_SCENEMASTER		= "masterscene";
	
	//--------------------------------------------------------
	//Scene Objects' attributes
	public final static String S_A_PINTCHZOOM		= "pinch_zoom";
	

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
	public final static String S_A_SIDEA			= "sideA";
	public final static String S_A_SIDEB			= "sideB";
	
	//Font
	public final static String S_A_MESSAGE 			= "message";
	public final static String S_A_COLOR			= "color";
	
	//Alignement
	public final static String S_A_H_ALIGNEMENT 	= "h_alignment";
	public final static String S_A_V_ALIGNEMENT 	= "v_alignment";
	
	//Duration
	public final static String S_A_DURATION  		="duration";
	
	//event handler
	public final static String S_A_TYPE_SET 		="typeSet";
	public final static String S_A_EXECUTION_ORDER	="execution_order";
	public final static String S_A_CLONEID			="cloneID";
	public final static String S_A_USE_WITH_PRE_ID 	="use_with_pre_mod_ID";
	public final static String S_A_USE_WITH_POST_ID ="use_with_post_mod_ID";
	public final static String S_A_USE_WITH_ON_ID 	="use_with_on_mod_ID";
	public final static String S_A_USE_WITH_MOD_ID 	="use_with_mod_ID";	
	
	//memory
	public final static String S_A_MAX_LEVELS		="maxLevels";
	public final static String S_A_TOPBOTTOMBORDER  ="TopBottomBorder";
	public final static String S_A_LEFTRIGHTBORDER  ="LeftRightBorder";
	public final static String S_A_VINTREBORDER  	="VIntraBorder";
	public final static String S_A_HINTREBORDER  	="nHIntraBorder";
	public final static String S_A_STDCARDHEIGHT	="StdCardHeight";
	public final static String S_A_STDCARDWIDTH 	="StdCardWidth";
	public final static String S_A_GEOMETRY  		="geometry";
	
	public final static String S_A_MAXNBOFSYMBOLS  	="MaxNumberOfSymbols";
	public final static String S_A_RESOURCES		="resource";
	public final static String S_A_MAPCARDTILES		="mapCardTiles";
	public final static String S_A_MAPCARDSOUND  	="mapCardSound";
	public final static String S_A_SCENE_PHRASES  	="phrases";
	public final static String S_A_FLIP_TIME  		="FlipTime";
	public final static String S_A_WAIT_BACK_FLIP	="WaitBackFlip";
	
	//puzzles
	public final static String S_A_NBCOLS			="nbOfColumns"; 
	public final static String S_A_NBROWS			="nbOfRows";
	
	public final static String S_A_PUZZLE_BOX		="puzzleBox";
	public final static String S_A_PUZZLE_ZONE		="puzzleZone";
	
	public final static String S_A_ACTIVE_BORDER	="ActiveBorder";
	public final static String S_A_ACTIVE_ZONE		="ActiveZone";
	public final static String S_A_HELPER_IMAGE		="HelperImage";
	public final static String S_A_HELPER_IMG_ALPHA	="HIAlpha";
	
	//Head UP Display
	public final static String S_A_HUD				="HUD";
	
}	
