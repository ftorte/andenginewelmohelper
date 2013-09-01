package com.welmo.andengine.scenes.descriptors;

import java.util.HashMap;
import java.util.LinkedList;

import com.welmo.andengine.scenes.descriptors.components.BasicObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.GameLevel;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;

public class SceneDescriptor extends BasicObjectDescriptor {
	// =======================================================================================
	// Constants
	// =======================================================================================

	// =======================================================================================
	// Member Variables
	// =======================================================================================
	public String 										sceneName="";
	public String 										sceneFather="";
	protected GameLevel									gameLevel=GameLevel.EASY;
	protected HashMap<String,String[]> 					phrasesMap;
	public LinkedList<ComponentEventHandlerDescriptor> 	pGlobalEventHandlerList;
	private boolean 									bPinchAndZoom;
	private boolean 									bHasHUD;
	
	
	public HashMap<String, String[]> getPhrasesMap() {
		return phrasesMap;
	}
	public void setPhrasesMap(HashMap<String, String[]> phrasesMap) {
		this.phrasesMap = phrasesMap;
	}
	protected SceneType	sceneType=SceneType.DEFAULT;
	
	public SceneType getSceneType() {
		return sceneType;
	}
	public void setSceneType(SceneType sceneType) {
		this.sceneType = sceneType;
	}
	
	
	// ===========================================================
	// Getters & Setters
	// ===========================================================
	public String getSceneName() {
		return sceneName;
	}
	public String getSceneFather() {
		return sceneFather;
	}
	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}
	public void setSceneFather(String scene) {
		sceneFather=scene;
	}
	public boolean isPinchAndZoom() {
		return bPinchAndZoom;
	}
	public void setPinchAndZoom(boolean bPinchAndZoom) {
		this.bPinchAndZoom = bPinchAndZoom;
	}
	public boolean hasHUD() {
		return bHasHUD;
	}
	public void hasHUD(boolean hasHUD) {
		this.bHasHUD = hasHUD;
	}
	// ===========================================================
	// Constructor(s)
	// ===========================================================
	@SuppressWarnings("static-access")
	public SceneDescriptor() {
		pGlobalEventHandlerList = new LinkedList<ComponentEventHandlerDescriptor> ();
		phrasesMap				= new HashMap<String,String[]>();
	}
	public GameLevel getGameLevel() {
		return gameLevel;
	}
	public void setGameLevel(GameLevel newGameLevel) {
		gameLevel=newGameLevel;
	}
}
