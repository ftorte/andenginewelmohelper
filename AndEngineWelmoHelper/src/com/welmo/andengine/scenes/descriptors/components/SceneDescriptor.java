package com.welmo.andengine.scenes.descriptors.components;

import java.util.HashMap;
import java.util.LinkedList;

import org.xml.sax.Attributes;

import android.util.Log;

import com.welmo.andengine.scenes.descriptors.SceneType;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;

public class SceneDescriptor extends BasicComponentDescriptor {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Member Variables
	// ===========================================================
	protected String 					sceneName="";
	protected String 					sceneFather="";
	protected GameLevel					gameLevel=GameLevel.EASY;
	protected HashMap<String,String[]> 	phrasesMap;
	
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
	public LinkedList<ComponentEventHandlerDescriptor> pGlobalEventHandlerList;
	
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
	// ===========================================================
	// Constructor(s)
	// ===========================================================
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
	// ===========================================================
	// Member Function
	// ===========================================================
	/*@Override
	public void readXMLDescription(Attributes attr) {
		Log.i(TAG,"\t\t readPuzzleDescription");
	
		super.readXMLDescription(attr);
	}*/
	
}
