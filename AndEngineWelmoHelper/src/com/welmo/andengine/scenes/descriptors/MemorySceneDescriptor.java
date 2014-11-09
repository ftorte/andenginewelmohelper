package com.welmo.andengine.scenes.descriptors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Attributes;

import com.welmo.andengine.scenes.descriptors.components.GameLevel;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor.SpritesTypes;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.descriptors.events.SceneActions.ActionType;

public class MemorySceneDescriptor extends SceneDescriptor{
	
	//  ========================================================
	//  Value of difficulties (are row index in memoryeStructure}
	//  =========================================================		
	//Value of parameter (are column index in memoryeStructure}
	public static final int 				DIFFICULTY 	= 0;
	public static final int 				HEIGHT 		= 1;
	public static final int 				WIDTH 		= 2;
	public static final int 				NB_SYMBOLS 	= 3;
	public static final int					GEOMETRY_DSC_NBCOL = 4;
	public static final int					DEFAULT_FLIP_TIME = 1000;
	
	protected GameLevel						nMaxLevelAllowed;
	protected int							nTopBottomBorder;
	protected int							nLeftRightBorder;
	protected int							nVIntraBorder;
	protected int							nHIntraBorder;
	protected int							nStdCardHeight;
	protected int							nStdCardWidth;
	protected String 						resouceName;
	protected int  							nMaxNbOfSymbols;
	protected int[][]						memoryStructure = {{0,3,5,7},{1,2,4,4},{2,5,8,18},{3,6,10,30}};
	

	protected int[][]						memoryMapOfCardsTiles;
	protected float							fFlipTime = DEFAULT_FLIP_TIME;
	protected float							fWaitBackFlip=DEFAULT_FLIP_TIME;

	public float getWaitBackFlip() {
		return fWaitBackFlip;
	}
	public void setWaitBackFlip(float fWaitBackFlip) {
		this.fWaitBackFlip = fWaitBackFlip;
	}
	public float getFlipTime() {
		return fFlipTime;
	}
	public void setFlipTime(float fFlipTime) {
		this.fFlipTime = fFlipTime;
	}
	public int[][] getMemoryMapOfCardsTiles() {
		return memoryMapOfCardsTiles;
	}
	public void setMemoryMapOfCardsTiles(int[][] memoryMapOfCardsTiles) {
		this.memoryMapOfCardsTiles = memoryMapOfCardsTiles;
	}
	public void setMemoryStructure(int[][] memoryStructure) {
		this.memoryStructure = memoryStructure;
	}
	public int getMaxNbOfSymbols() {
		return nMaxNbOfSymbols;
	}
	public void setMaxNbOfSymbols(int MaxNbOfSymbols) {
		this.nMaxNbOfSymbols = MaxNbOfSymbols;
	}
	public String getResouceName() {
		return resouceName;
	}
	public void setResouceName(String resouceName) {
		this.resouceName = resouceName;
	}

	//  ========================================================
	//  Getter & Setters
	//  =========================================================	
	public GameLevel getMaxLevelAllowed() {
		return nMaxLevelAllowed;
	}
	public int getTopBottomBorder() {
		return nTopBottomBorder;
	}
	public int getLeftRightBorder() {
		return nLeftRightBorder;
	}
	public int getVIntraBorder() {
		return nVIntraBorder;
	}
	public int getHIntraBorder() {
		return nHIntraBorder;
	}
	public int getStdCardHeight() {
		return nStdCardHeight;
	}
	public int getStdCardWidth() {
		return nStdCardWidth;
	}
	public  int[][] getMemoryStructure() {
		return memoryStructure;
	}
	
	
	public void setMaxLevelAllowed(GameLevel nMaxLevelAllowed) {
		this.nMaxLevelAllowed = nMaxLevelAllowed;
	}
	public void setTopBottomBorder(int nTopBottomBorder) {
		this.nTopBottomBorder = nTopBottomBorder;
	}
	public void setLeftRightBorder(int nLeftRightBorder) {
		this.nLeftRightBorder = nLeftRightBorder;
	}
	public void setVIntraBorder(int nVIntraBorder) {
		this.nVIntraBorder = nVIntraBorder;
	}
	public void setHIntraBorder(int nHIntraBorder) {
		this.nHIntraBorder = nHIntraBorder;
	}
	public void setStdCardHeight(int nStdCardHeight) {
		this.nStdCardHeight = nStdCardHeight;
	}
	public void setStdCardWidth(int nStdCardWidth) {
		this.nStdCardWidth = nStdCardWidth;
	}
	
	public MemorySceneDescriptor(){
		//Default Values
		nMaxLevelAllowed 	= GameLevel.HARD;
		nTopBottomBorder 	= 40;
		nLeftRightBorder 	= 40;
		nVIntraBorder 		= 10;
		nHIntraBorder 		= 10;
		nStdCardHeight 		= 175;
		nStdCardWidth 		= 125;
		resouceName			= new String();
		nMaxNbOfSymbols 	= 30;
	}
	
	public void readXMLDescription(Attributes attributes) {
		super.readXMLDescription(attributes);
	
		int stdCardH=0;
		int stdCardW=0;
		String resourceName="";
		float flipTime=0;
		
		
		if(attributes.getValue(ScnTags.S_A_MAX_LEVELS)!= null)
			this.setMaxLevelAllowed(GameLevel.valueOf(attributes.getValue(ScnTags.S_A_MAX_LEVELS)));
		if(attributes.getValue(ScnTags.S_A_TOPBOTTOMBORDER)!= null)
			this.setTopBottomBorder(Integer.parseInt(attributes.getValue(ScnTags.S_A_TOPBOTTOMBORDER)));
		if(attributes.getValue(ScnTags.S_A_LEFTRIGHTBORDER)!= null)
			this.setLeftRightBorder(Integer.parseInt(attributes.getValue(ScnTags.S_A_LEFTRIGHTBORDER)));
		if(attributes.getValue(ScnTags.S_A_VINTREBORDER)!= null)
			this.setVIntraBorder(Integer.parseInt(attributes.getValue(ScnTags.S_A_VINTREBORDER)));
		if(attributes.getValue(ScnTags.S_A_HINTREBORDER)!= null)
			this.setHIntraBorder(Integer.parseInt(attributes.getValue(ScnTags.S_A_HINTREBORDER)));
		if(attributes.getValue(ScnTags.S_A_MAXNBOFSYMBOLS)!= null)
			this.setMaxNbOfSymbols(Integer.parseInt(attributes.getValue(ScnTags.S_A_MAXNBOFSYMBOLS)));
		if(attributes.getValue(ScnTags.S_A_STDCARDHEIGHT)!= null){
			stdCardH=Integer.parseInt(attributes.getValue(ScnTags.S_A_STDCARDHEIGHT));
			this.setStdCardHeight(stdCardH);
		}
		if(attributes.getValue(ScnTags.S_A_STDCARDWIDTH)!= null){
			stdCardW=Integer.parseInt(attributes.getValue(ScnTags.S_A_STDCARDWIDTH));
			this.setStdCardWidth(stdCardW);
		}
		if(attributes.getValue(ScnTags.S_A_RESOURCES)!= null){
			resourceName = attributes.getValue(ScnTags.S_A_RESOURCES);
			this.setResouceName(resourceName);
		}
		if(attributes.getValue(ScnTags.S_A_FLIP_TIME)!= null){
			this.setFlipTime(Float.parseFloat(attributes.getValue(ScnTags.S_A_FLIP_TIME)));
			flipTime = this.getFlipTime();
		}
		if(attributes.getValue(ScnTags.S_A_WAIT_BACK_FLIP)!= null){
			this.setWaitBackFlip(Float.parseFloat(attributes.getValue(ScnTags.S_A_WAIT_BACK_FLIP)));
		}
		
		//Parse JSON strings
		JSONObject jObject;
		//Geometry
		try {
			//Parse Geometry
			if(attributes.getValue(ScnTags.S_A_GEOMETRY)!= null){
				jObject = new JSONObject(attributes.getValue(ScnTags.S_A_GEOMETRY));
				JSONArray geometries = jObject.getJSONArray(ScnTags.S_A_GEOMETRY);

				if((this.getMaxLevelAllowed()).ordinal() > geometries.length())
					this.setMaxLevelAllowed(GameLevel.fromOrdinal(geometries.length()));

				//create array will contians the geometry
				int[][] geometryArray = new int[geometries.length()][MemorySceneDescriptor.GEOMETRY_DSC_NBCOL];

				//parse the array
				for (int i=0; i<geometries.length(); i++){
					JSONArray currLine = (JSONArray)geometries.get(i);
					for (int j=0; j<MemorySceneDescriptor.GEOMETRY_DSC_NBCOL; j++){
						geometryArray[i][j]=currLine.getInt(j);
					}
					this.setMemoryStructure(geometryArray);
				} 
			}
			//Parse tiled Maps
			if(attributes.getValue(ScnTags.S_A_MAPCARDTILES)!= null){
				jObject = new JSONObject(attributes.getValue(ScnTags.S_A_MAPCARDTILES));
				JSONArray mapOfTiles = jObject.getJSONArray(ScnTags.S_A_MAPCARDTILES);

				if(this.getMaxNbOfSymbols() > mapOfTiles.length())
					this.setMaxNbOfSymbols(mapOfTiles.length());

				//create array will contains the geometry
				int[][] mapOfCardTiles = new int[mapOfTiles.length()][3];

				//parse the array
				for (int i=0; i<mapOfTiles.length(); i++){
					JSONArray currLine = (JSONArray)mapOfTiles.get(i);
					
					//create a sprite descriptor
					SpriteObjectDescriptor oCardDsc  = new SpriteObjectDescriptor();
					oCardDsc.setClassName("com.welmo.andengine.scenes.components.CardSprite");
					oCardDsc.ID = currLine.getInt(0);
					oCardDsc.setSidesTiles(currLine.getInt(1), currLine.getInt(2));
					oCardDsc.getIDimension().setHeight(stdCardH);
					oCardDsc.getIDimension().setWidth(stdCardW);
					oCardDsc.getIPosition().setX(0);
					oCardDsc.getIPosition().setY(0);
					oCardDsc.getIPosition().setZorder(0);
					oCardDsc.textureName = new String(resourceName);
					oCardDsc.setSoundName(currLine.getString(3));
					oCardDsc.setType(SpritesTypes.CLICKABLE);
					this.pChild.put(oCardDsc.ID,oCardDsc);
	
					ComponentEventHandlerDescriptor pNewEvent = new ComponentEventHandlerDescriptor();
					
					//create card event actions & modifiers
					pNewEvent.event = Events.ON_CLICK;
					pNewEvent.setID(0);
					
					//flip action 
					SceneActions action1 = new SceneActions();
					//Disable
					action1.type = ActionType.DISABLE_SCENE_TOUCH;
					pNewEvent.preModAction.add(action1);
					//Flip
					SceneActions action2 = new SceneActions();
					action2.type = ActionType.FLIP;
					action2.flipTime = flipTime;
					pNewEvent.preModAction.add(action2);

					//sound on going  
					SceneActions action3 = new SceneActions();
					action3.type = ActionType.PLAY_SOUND;
					action3.resourceName = currLine.getString(3);
					pNewEvent.preModAction.add(action3);
					
					//Disable
					SceneActions action4 = new SceneActions();
					action4.type = ActionType.ENABLE_SCENE_TOUCH;
					pNewEvent.postModAction.add(action4);
				
					oCardDsc.pEventHandlerList.put(pNewEvent.event, pNewEvent);
				}
				this.setMemoryMapOfCardsTiles(mapOfCardTiles);
			} 
		}
		catch (JSONException e) {
		}	
	}
}
