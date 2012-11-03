package com.welmo.andengine.managers;

import java.util.HashMap;

import com.welmo.andengine.scenes.descriptors.components.MultiViewSceneDescriptor;
import com.welmo.andengine.scenes.descriptors.components.SceneDescriptor;

public class SceneDescriptorsManager {

	//--------------------------------------------------------
	// Variables
	//--------------------------------------------------------
	//Scene
	protected HashMap<String,SceneDescriptor> 			hmSceneDscMap;
	protected HashMap<String,MultiViewSceneDescriptor> 	hmMVSceneDscMap;

	// singleton Instance
	private static SceneDescriptorsManager 	mInstance=null;
	//--------------------------------------------------------

	//--------------------------------------------------------
	// Constructors
	//--------------------------------------------------------
	private SceneDescriptorsManager(){
		hmSceneDscMap = new HashMap<String,SceneDescriptor>();
		hmMVSceneDscMap = new HashMap<String,MultiViewSceneDescriptor>(); 

	}
	@method
	public static SceneDescriptorsManager getInstance(){
		if(mInstance == null)
			mInstance = new  SceneDescriptorsManager();
		return mInstance;
	}
	//--------------------------------------------------------
	// SCENE
	//--------------------------------------------------------
	@method
	//Add the description of a scene descriptions list
	public void addScene(String name, SceneDescriptor scene){
		if (hmSceneDscMap == null)
			throw new NullPointerException("ResurceDescriptorsManager not initialized correctly"); 
		hmSceneDscMap.put(name,scene);
	}
	@method
	//Add the description of a scene descriptions list
	public SceneDescriptor getScene(String name){
		if (hmSceneDscMap == null)
			throw new NullPointerException("ResurceDescriptorsManager not initialized correctly"); 
		return hmSceneDscMap.get(name);
	}
	//--------------------------------------------------------
	// MULTIVEIWSCENE
	//--------------------------------------------------------
	@method
	//Add the description of a scene descriptions list
	public void addMVScene(String name, MultiViewSceneDescriptor scene){
		if (hmSceneDscMap == null)
			throw new NullPointerException("ResurceDescriptorsManager not initialized correctly"); 
		hmMVSceneDscMap.put(name,scene);
	}
	@method
	//Add the description of a scene descriptions list
	public MultiViewSceneDescriptor getMVScene(String name){
		if (hmSceneDscMap == null)
			throw new NullPointerException("ResurceDescriptorsManager not initialized correctly"); 
		return hmMVSceneDscMap.get(name);
	}

}
