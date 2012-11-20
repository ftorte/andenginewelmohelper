package com.welmo.andengine.scenes.descriptors.components;

import java.util.LinkedList;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;

public class SceneDescriptor extends BasicObjectDescriptor {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Member Variables
	// ===========================================================
	String sceneName="";
	String sceneFather="";
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
	@SuppressWarnings("static-access")
	public SceneDescriptor() {
		pGlobalEventHandlerList = new LinkedList<ComponentEventHandlerDescriptor> ();
	}
	public int getMemodyLevel() {
		// TODO Auto-generated method stub
		return 0;
	}
}
