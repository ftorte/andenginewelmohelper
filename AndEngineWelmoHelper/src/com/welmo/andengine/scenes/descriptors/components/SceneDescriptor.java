package com.welmo.andengine.scenes.descriptors.components;

public class SceneDescriptor extends BasicObjectDescriptor {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Member Variables
	// ===========================================================
	String sceneName="";
	String sceneFather="";
	String className="";
	// ===========================================================
	// Getters & Setters
	// ===========================================================
	public String getSceneName() {
		return sceneName;
	}
	public String getSceneFather() {
		return sceneFather;
	}
	public String getClassName() {
		return className;
	}

	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public void setSceneFather(String scene) {
		sceneFather=scene;
	}
	// ===========================================================
	// Constructor(s)
	// ===========================================================
	@SuppressWarnings("static-access")
	public SceneDescriptor() {
	}
}
