package com.welmo.andengine.managers;

import java.util.HashMap;

import com.welmo.andengine.scenes.descriptors.ConfiguredSceneDescriptor;
import com.welmo.andengine.scenes.descriptors.SceneDescriptor;
import com.welmo.andengine.scenes.descriptors.components.MultiViewSceneDescriptor;
import com.welmo.andengine.utility.method;
//Description
/**
* A scene descriptors manager (SDM) is an object that manage scenes descriptors and allow application to get it by name. 
* 
*/	
public class SceneDescriptorsManager {

	//--------------------------------------------------------
	// Variables
	//--------------------------------------------------------
	//Scene
	protected HashMap<String,SceneDescriptor> 			hmSceneDscMap;
	protected HashMap<String,MultiViewSceneDescriptor> 	hmMVSceneDscMap;
	protected HashMap<String,ConfiguredSceneDescriptor> hmConfigSceneDscMap;
	// singleton Instance
	private static SceneDescriptorsManager 				mInstance=null;
	//--------------------------------------------------------

	//--------------------------------------------------------
	// Constructors
	//--------------------------------------------------------
	private SceneDescriptorsManager(){
		hmSceneDscMap = new HashMap<String,SceneDescriptor>();
		hmMVSceneDscMap = new HashMap<String,MultiViewSceneDescriptor>(); 
		hmConfigSceneDscMap = new HashMap<String,ConfiguredSceneDescriptor>(); 
		
		//if failed to build one of the map throw exception
		if (hmSceneDscMap == null || hmMVSceneDscMap == null ||hmConfigSceneDscMap == null)
			throw new NullPointerException("ResurceDescriptorsManager not initialized correctly"); 
	}
	@method
	public static SceneDescriptorsManager getInstance(){
		if(mInstance == null)
			mInstance = new  SceneDescriptorsManager();
		return mInstance;
	}
	//--------------------------------------------------------
	// ADD SCENE
	//--------------------------------------------------------
	@method
	public void addScene(String name, SceneDescriptor scene){
			hmSceneDscMap.put(name,scene);
	}
	@method
	/**
	 * Get the descriptor of the  Scene or null if not found
	 * @param String 						//the name of the scene
	 * @return SceneDescriptor 	//the descriptor or null if not found
	 */
	//Add the description of a scene descriptions list
	public SceneDescriptor getScene(String name){
		return hmSceneDscMap.get(name);
	}
	//--------------------------------------------------------
	// CONFIGUREDSCENE
	//--------------------------------------------------------
	@method
	/**
	 * Get the descriptor of the Configured Scene or null if not found
	 * @param String 						//the name of the scene
	 * @return ConfiguredSceneDescriptor 	//the descriptor or null if not found
	 */
	public ConfiguredSceneDescriptor getCFGScene(String name){
		return hmConfigSceneDscMap.get(name);
	}
	@method
	//Get the description of a configured scene from the hashmap 
	public void addCFGScene(String name, ConfiguredSceneDescriptor dsc){
			hmConfigSceneDscMap.put(name,dsc);
	}
	//--------------------------------------------------------
	// MULTIVEIWSCENE
	//--------------------------------------------------------
	@method
	//Add the description of a scene descriptions list
	public void addMVScene(String name, MultiViewSceneDescriptor scene){
		hmMVSceneDscMap.put(name,scene);
	}
	/**
	 * Get the descriptor of the MultiViewScene or null if not found
	 * @param String name   //the name of the scen
	 * @return the scene descriptor or null
	 */
	@method
	//Add the description of a scene descriptions list
	public MultiViewSceneDescriptor getMVScene(String name){
		return hmMVSceneDscMap.get(name);
	}

}
