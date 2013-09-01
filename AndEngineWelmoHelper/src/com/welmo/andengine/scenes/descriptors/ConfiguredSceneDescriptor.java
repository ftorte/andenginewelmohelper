package com.welmo.andengine.scenes.descriptors;

import java.util.HashMap;
import java.util.Map;

import com.welmo.andengine.scenes.descriptors.components.BasicObjectDescriptor;

public class ConfiguredSceneDescriptor extends BasicObjectDescriptor{
	protected String sceneName						 	= new String("");
	protected String masterSceneName  				 	= new String("");
	protected Map<String,String> parameterList 	= null;
	
	// ===========================================================
	// Constructor(s)
	// ===========================================================
	public ConfiguredSceneDescriptor(String sceneName, String masterSceneName){
		super();
		sceneName			= new String(sceneName);
		masterSceneName  	= new String(masterSceneName);
		parameterList 		= new HashMap<String,String>();
	}
	
	public ConfiguredSceneDescriptor(){
		super();
		sceneName			= new String("");
		masterSceneName  	= new String("");
		parameterList 		= new HashMap<String,String>();
	}
	public void addParameter(String key, String value){
		parameterList.put(key, value);
	}
	public String getValue(String key){
		return parameterList.get(key);
	}
	
	public Map<String,String> getParameterMap() {
		return parameterList;
	}
	// ===========================================================
	// Getters & Setters
	// ===========================================================
	public String getSceneName() {
		return sceneName;
	}
	public String getNameOfSceneMaster() {
		return masterSceneName;
	}
	public void setSceneName(String scene) {
		this.sceneName = new String(scene);
	}
	public void setNameOfSceneMaster(String scene) {
		this.masterSceneName=new String(scene);
	}
}
