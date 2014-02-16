package com.welmo.andengine.scenes.descriptors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import com.welmo.andengine.scenes.descriptors.components.GameLevel;
import com.welmo.andengine.scenes.descriptors.components.HUDDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;

public class SceneDescriptor extends BasicDescriptor /*BasicObjectDescriptor*/ {
	// =======================================================================================
	// Constants
	// =======================================================================================

	// =======================================================================================
	// Member Variables
	// =======================================================================================
	public String 										sceneName="";
	public String 										sceneFather="";
	public LinkedList<ComponentEventHandlerDescriptor>  pGlobalEventHandlerList;
	
	protected GameLevel									gameLevel		=GameLevel.EASY;
	protected HashMap<String,String[]> 					phrasesMap;
	protected SceneType									sceneType		=SceneType.DEFAULT;
	
	private boolean 									bPinchAndZoom	= false;
	private boolean 									bHasHUD 		= false;
	@SuppressWarnings("unused")
	private HUDDescriptor								pHUDDsc 		= null;
	
	
	public HashMap<String, String[]> getPhrasesMap() {
		return phrasesMap;
	}
	public void setPhrasesMap(HashMap<String, String[]> phrasesMap) {
		this.phrasesMap = phrasesMap;
	}
	
	
	// ===========================================================
	// Getters & Setters
	// ===========================================================
	public String 			getSceneName(){return sceneName;}
	public void 			setSceneName(String sceneName){this.sceneName = sceneName;}
	
	public String 			getSceneFather(){return sceneFather;}
	public void 			setSceneFather(String scene){sceneFather=scene;}
	
	public boolean 			isPinchAndZoom(){return bPinchAndZoom;}
	public void 			setPinchAndZoom(boolean bPinchAndZoom){this.bPinchAndZoom = bPinchAndZoom;}
	
	public boolean 			hasHUD(){return bHasHUD;}
	public void 			hasHUD(boolean hasHUD){this.bHasHUD = hasHUD;}
	
	public HUDDescriptor 	getHUDDsc(){return getHudDsc();}
	
	public GameLevel 		getGameLevel(){return gameLevel;}
	public void 			setGameLevel(GameLevel newGameLevel){gameLevel=newGameLevel;}
	
	public SceneType 		getSceneType(){return sceneType;}
	public void 			setSceneType(SceneType sceneType){this.sceneType = sceneType;}
	// ===========================================================
	// Constructor(s)
	// ===========================================================
	public SceneDescriptor() {
		pGlobalEventHandlerList = new LinkedList<ComponentEventHandlerDescriptor> ();
		phrasesMap				= new HashMap<String,String[]>();
	}
	private HUDDescriptor getHudDsc(){	
		Iterator<Entry<Integer,BasicDescriptor>> it = pChild.entrySet().iterator();
	    while (it.hasNext()) {
	    	Entry<Integer,BasicDescriptor> pairs = (Entry<Integer, BasicDescriptor>) it.next();
	    	if(pairs.getValue() instanceof HUDDescriptor) 
	    		return (HUDDescriptor)pairs.getValue();
	    }
	    return null;
	}
}
