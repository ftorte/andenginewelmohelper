package com.welmo.andengine.scenes.descriptors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.xml.sax.Attributes;

import com.welmo.andengine.scenes.descriptors.components.BasicObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ScnTags;

public class ConfiguredSceneDescriptor extends BasicObjectDescriptor{
	protected String 					sceneName		= new String("");
	protected String 					masterSceneName = new String("");
	protected ArrayList<String> 		parameterList 	= null;
	
	// ===========================================================
	// Constructor(s)
	// ===========================================================
	public ConfiguredSceneDescriptor(String sceneName, String masterSceneName){
		super();
		sceneName			= new String(sceneName);
		masterSceneName  	= new String(masterSceneName);
		parameterList 		= new ArrayList<String>();
	}
	
	public ConfiguredSceneDescriptor(){
		super();
		sceneName			= new String("");
		masterSceneName  	= new String("");
		parameterList 		= new ArrayList<String>();
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
	public ArrayList<String>  getParameterList() {
		return parameterList;
	}
	public void setNameOfSceneMaster(String scene) {
		this.masterSceneName=new String(scene);
	}
	
	@Override
	public void readXMLDescription(Attributes attributes) {
		super.readXMLDescription(attributes);
		String value = null;
		
		//read the sceneName of the image
		if((value = attributes.getValue(ScnTags.S_A_NAME))!=null) 
			setSceneName(value);
		
		//read the sceneFather of the image
		if((value = attributes.getValue(ScnTags.S_FATHER))!=null) 
			masterSceneName=new String(value);
		
		if((value = attributes.getValue(ScnTags.S_A_MASTERSCENE))!=null)
			setNameOfSceneMaster(value);
		
		if((value = attributes.getValue(ScnTags.S_A_PARAMETER_LIST))!=null){
			StringTokenizer st = new StringTokenizer(value, ",");
			while (st.hasMoreTokens()) {
				parameterList.add(new String(st.nextToken()));
			}
		}
		
	}
}
