package com.welmo.andengine.scenes.descriptors;


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
	public final static String S_IS_ISTANCE_OF_ID	= "isIstanceOfID";
	public final static String S_MULTIVIEWSCENE 	= "multiviewscene";
	public final static String S_FATHER				= "sceneFather";
	public final static String S_A_PERSISTENCE_FILE	= "persistenceFile";
	public final static String S_A_PERSISTENCE_MODE	= "persistenceMode";
	
	public final static String S_A_END_SCENE		= "end_scene";
	
	//--------------------------------------------------------
	//Scene Objects
	public final static String S_O_SPRITE 			= "sprite";
	public final static String S_CLICKABLE_SPRITE 	= "clickable_sprite";
	public final static String S_ANIMATED_SPRITE 	= "animated_sprite";
	public final static String S_COMPOUND_SPRITE 	= "compound_sprite";
	public final static String S_COLORING_SPRITE	= "coloring_sprite";
	public final static String S_TEXT 				= "text";
	public final static String S_BACKGROUND 		= "background";
	public final static String S_PUZZLE_SPRITE		= "puzzle_sprite";
	public final static String S_HUD				= "HUD";
	public static final String S_TOOLBAR 			= "toolbar";
	public static final String S_BUTTON 			= "button";
	public static final String S_SCENELAUNCHER 		= "scene_launcher_btn";
	public static final String S_IS_TEMPLATE		= "isTemplate";
	public static final String S_PARTICULESYSTEM 	= "particule_system";
	public static final String S_INITIALIZER 		= "initializer";
	public static final String S_PROGRESSBAR 		= "progress_bar";
		
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
	public final static String  S_A_MASTERSCENE		= "masterscene";
	
	//--------------------------------------------------------
	//Scene Objects' attributes
	public final static String S_A_PINTCHZOOM		= "pinch_zoom";
	

	public final static String S_A_STICK_WITH		= "stick_with";
	public final static String S_A_STICK_MODE		= "stick_mode";
	
	//Scene Objects' parameters
	public final static String S_A_NEXT_SCENE		= "next_scene";
	public final static String S_A_SCENE_CHANGE 	= "scene_change";
	public final static String S_A_ID 				= "ID";
	public final static String S_A_CLASSNAME 		= "ClassName";
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
	public static final String S_A_SCALE 			= "scale";
	public final static String S_A_ORIENTATION		= "orientation";
	public final static String S_A_ROTAION_CENTER_X	= "rX";
	public final static String S_A_ROTAION_CENTER_Y	= "rX";
	public final static String S_A_FILE_NAME		= "filename";
	public final static String S_A_PARAMETER_LIST	= "parametersList";
	
	

	public final static int S_A_HEIGHT_IDX			= 0;
	public final static int S_A_WIDTH_IDX			= 1;
	public final static int S_A_POSITION_X_IDX		= 2;
	public final static int S_A_POSITION_Y_IDX		= 3;
	public final static String S_A_SIDEA			= "sideA";
	public final static String S_A_SIDEB			= "sideB";
	
	//Font and text
	public final static String S_A_MESSAGE 			= "message";
	public final static String S_A_COLOR			= "color";
	public final static String S_A_TEXTMAXLENGHT	= "textmaxlenght";
	public final static String S_A_TEXTMAXWIDTH		= "textmaxwidth";
	
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
	public static final String S_A_ACTIVE_ZONE_BORDERS ="ActiveZoneBorders";
	public final static String S_A_ACTIVE_ZONE		="ActiveZone";
	public final static String S_A_HAS_HELPER_IMG	="HasHelperImg";
	public final static String S_A_HELPER_IMAGE		="HelperImage";
	public final static String S_A_HELPER_IMG_ALPHA	="HIAlpha";
	public static final String S_A_HAS_WHITE_BACKGROUND = "hasWhiteBackground";
	public static final String S_A_FIREWORKS 		= "fireworks";
	public static final String S_A_GAME_LEVEL_MAP 	= "GameLevelsMap";

	//Head UP Display
	public final static String S_A_HUD				="HUD";
	public final static String S_A_HASCOLORPICKER	="hasColorPicker";
	public static final String S_A_HASSCROLLDETECTOR = "hasScrollDetector";
	public static final String S_A_HASPINCHANDZOOM 	= "hasPinchAndZoom";
	
	//tools bar
	public static final String S_A_TOLLBARPOSITION  ="position";
	public static final String S_A_TOOLS 			="tools";
	
	//Button
	public static final String S_A_BG_TEXTURE 		= "texture_BG";
	public static final String S_A_ON_TEXTURE 		= "texture_ON";
	public static final String S_A_OFF_TEXTURE 		= "texture_OFF";
	public static final String S_A_EXT_DIM 			= "ext_dim";
	public static final String S_A_INT_DIM 			= "int_dim";
	public static final String S_A_ON_CLIK_MESSAGE 	= "on_click_message";
	
	//Button Scene Launcher
	public static final String S_A_BG_INACTIVE		= "back_ground_inactive";
	public static final String S_A_ICO_INACTIVE 	= "ico_inactive";
	public static final String S_A_BG_FINAL 		= "back_ground_final";
	public static final String S_A_ICO_LOCKED 		= "ico_locked";
	public static final String S_A_ICO_UNLOCKED 	= "ico_unlocked";
	public static final String S_A_ICO_FREE 		= "ico_free";
	public static final String S_A_ICO_STAR_1 		= "ico_star_1";
	public static final String S_A_ICO_STAR_2 		= "ico_star_2";
	public static final String S_A_ICO_STAR_3		= "ico_star_3";	
	public static final String S_A_ICO_STAR_INACTIVE_1= "ico_star_inactive_1";
	public static final String S_A_ICO_STAR_INACTIVE_2= "ico_star_inactive_2";
	public static final String S_A_ICO_STAR_INACTIVE_3= "ico_star_inactive_3";
	
	public static final String S_A_STATUS_DEFAUTL	= "status_default";
	public static final String S_A_PERSISTENCE 		= "persistent";
	public static final String S_A_PERSISTENCE_VARNAME = "persistent_value";
	public static final String S_A_UNLOCK_CONDITION = "unlock_condition";
	public static final String S_A_LICENCE 			= "licence";
	public static final String S_A_PRODUCTID 		= "productID";
	
	//Particles specific
	public static final String S_A_RADIUS_X 		= "radiusX";
	public static final String S_A_RADIUS_Y 		= "radiusY";
	public static final String S_A_RATEMIN 			= "rateMin";
	public static final String S_A_RATEMAX 			= "rateMax";
	public static final String S_A_PARTICELMAX 		= "particelsMax";
	public static final String S_A_CENTER_X			= "centerX";
	public static final String S_A_CENTER_Y			= "centerY";
	public static final String S_A_FIREWORKS_DURATION = "fireworks_duration";
	
	//Particles specific for Modifier
	public static final String S_A_FROM_TYME 		= "from_time";
	public static final String S_A_TO_TYME 			= "to_time";
	public static final String S_A_FROM_SCALE		= "from_scale";
	public static final String S_A_TO_SCALE			= "to_scale";
	public static final String S_A_FROM_RED			= "from_red";
	public static final String S_A_TO_RED			= "ro_red";
	public static final String S_A_FROM_GREEN		= "from_green";
	public static final String S_A_TO_GREEN			= "to_green";
	public static final String S_A_FROM_BLUE		= "from_blue";
	public static final String S_A_TO_BLUE 			= "to_blue";
	public static final String S_A_FROM_ALPHA		= "from_alpha";
	public static final String S_A_TO_ALPHA			= "to_alpha";
	
	//Particles specific for Initializer
	public static final String S_A_RED				= "red";
	public static final String S_A_GREEN			= "green";
	public static final String S_A_BLUE				= "blue";
	public static final String S_A_MIN_RED			= "min_red";
	public static final String S_A_MIN_GREEN		= "min_green";
	public static final String S_A_MIN_BLUE			= "min_blue";
	public static final String S_A_MAX_RED			= "max_red";
	public static final String S_A_MAX_GREEN		= "max_green";
	public static final String S_A_MAX_BLUE			= "max_blue";
	public static final String S_A_ALPHA			= "alpha";
	public static final String S_A_MIN_VELX			= "min_velX";
	public static final String S_A_MAX_VELX			= "max_velX";
	public static final String S_A_MIN_VELY			= "min_velY";
	public static final String S_A_MAX_VELY			= "max_velY";
	public static final String S_A_MIN_ROTATION		= "min_rotation";
	public static final String S_A_MAX_ROTATION		= "max_rotation";
	public static final String S_A_LIFE_TIME		= "life_time";
	public static final String S_A_ACCEL_X 			= "accX";
	public static final String S_A_ACCEL_Y 			= "accY";
	
	//Progress Bar
	public static final String S_A_INCREMENT_BUTTON = "increment_button";
	public static final String S_A_DECREMENT_RUTTON = "decrement_button";
	public static final String S_A_BAR_BACKGROUND 	= "bar_background";
	public static final String S_A_BAR_PROGRESS_NOTCH = "bar_progress_notch";
	public static final String S_A_MIN_VAL 			= "min_value";
	public static final String S_A_MAX_VAL 			= "max_value";
	public static final String S_A_INTERNALl_HEIGHT = "internal_height";
	public static final String S_A_DISPLAYID 		= "displayID";
	public static final String S_A_MAPDISPLAYVALUES = "MapDisplayValues";
	
}
