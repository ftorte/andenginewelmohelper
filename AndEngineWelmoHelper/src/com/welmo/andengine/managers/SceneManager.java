package com.welmo.andengine.managers;

import java.util.HashMap;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import com.welmo.andengine.scenes.IConfigurableScene;
import com.welmo.andengine.scenes.IManageableScene;
import com.welmo.andengine.scenes.ManageableCameraScene;
import com.welmo.andengine.scenes.ManageableScene;
import com.welmo.andengine.scenes.components.interfaces.IActivitySceneListener;
import com.welmo.andengine.scenes.descriptors.ConfiguredSceneDescriptor;
import com.welmo.andengine.scenes.descriptors.SceneDescriptor;
import com.welmo.andengine.ui.SimpleWelmoActivity;




import android.content.Context;
//Description
/**
 * A scene manager (SM) is an object that manage scenes. He create and store the scene in an internal map so accessing to them is fast.
 * 
 * When the application ask SM for a scene the SM returns a pointer to that scene that can be used by the Engine.
 * If the scene don't exist the SM create it.
 * If it is impossible to get the scene the scene manager return a null scene pointer.
 * 
 * To be used the scene manager need:
 * 	the pointer to the application that has instantiate it
 * 	to be initialized with the engine, the application context, and the scene descriptor manager.
 * 
 */	
public class SceneManager {
	// ===========================================================
	// Constants
	//Log & Debug & trace
	@SuppressWarnings("unused")
	private static final String TAG = "SceneManager";	
	// ===========================================================
	// Variables
	// ===========================================================
	private Engine 							mEngine=null;			// the Engine
	private Context 						mContext=null;			// the Context
	HashMap<String, IManageableScene> 		mapScenes =null;		// Map of scenes
	private SceneDescriptorsManager 		pSDM;					// private pointer to the Scene Descriptor Manager used while requesting a scene not yet created
	boolean 								initialized = false;	// flag to see if is initialized correctly
	SimpleWelmoActivity						theApplication; 		// the activity
	private SharedPreferenceManager			pSPM = null;
	// ===========================================================
	
	// Constructors
	/** 
	* Constructor of scene manger. He initializes the scene hash map & the pointer to the application.
	* A scene manager is attached to the application that has create it  => So is not a singleton and Scenes cannot be shared through applications.
	* 
	* @param BaseGameActivity 	application
	* @param Engine 		   	eng
	* @param Context 			ctx
	*/ 
	public SceneManager(SimpleWelmoActivity application, Engine eng, Context ctx) {  
		//check validity of variables an make exceptions
		if(application == null)
			throw new NullPointerException("Scene Manager cannot be instantiate => null application");
		if(eng == null)
			throw new NullPointerException("Scene Manager cannot be instantiate => null engine");
		if(ctx == null)
			throw new NullPointerException("Scene Manager cannot be instantiate => null context");
		
		pSDM = SceneDescriptorsManager.getInstance();
		if(pSDM == null)
			throw new NullPointerException("Scene Manager cannot be instantiate => null SceneDescriptor");
		
		theApplication = application;
		mEngine = eng;
		mContext = ctx;
		//create the map that will contains all scenes
		mapScenes = new HashMap<String, IManageableScene>();
		pSPM = SharedPreferenceManager.getInstance(ctx);
	}  
	
	// ===========================================================
	// public methods
	// ===========================================================
	/**
	* GETSCENE: method to obtain a Scene. 
	* If the scene is not found the scene manager try to build the scene. If is not able to buld it the function
	* return the null value
	* 
	* @param 	strSceneName the name of the scene to obtain
	* @return 	the Scene or null if any scene with the requested name is present, configures or can be created 
	*/
	public Scene getScene(String strSceneName){
		
		Scene theScene = null;
		
		//get the scene
		theScene = (Scene)mapScenes.get(strSceneName);
	
		//if scene not found check if it is a request for a configurable scene
		if(theScene == null) 
			theScene = getConfiguredScene(strSceneName);
		
		//if is not a configure try to build the scene
		if(theScene == null)
			theScene =  (Scene) BuildScene(strSceneName);
		
		//refresh persistent components
		((IManageableScene)theScene).refreshPersistentComponents(pSPM);
				
		return theScene;
		
	}	
	public IActivitySceneListener getIActivitySceneListener(){
		if(theApplication instanceof IActivitySceneListener)
			return (IActivitySceneListener)theApplication;
		return null;
	}
	
	// ===========================================================
	// private methods
	// ===========================================================
	/**
	* BUILD A SCENE.  /create scene and add to the map with name strSceneName
	* @param String strSceneName the neme of the Scene to create 
	* @return	Return a new scene
	* @thrown	If Scene descriptor don't exist for the requested scene
	* @thrown	If Scene cannot be initialized and/or loaded
	*/
	private synchronized IManageableScene BuildScene(String strSceneName){
		// Get the scene descriptor. If descriptor don't exist throw an exception
		SceneDescriptor pSCDercriptor = null;
		if((pSCDercriptor = pSDM.getScene(strSceneName))== null)
			throw new NullPointerException("Scene Descriptor not found for scene " + strSceneName); 
	
		// Create the appropriate class. If descriptor contain specific class create specific class 
		// Otherwise create the generic ManageableClass
		IManageableScene iManageableScene = null;
		String className = pSCDercriptor.getClassName();
		try {
			if(!className.equals(""))
				iManageableScene = (IManageableScene) Class.forName(className).newInstance();
			else
				iManageableScene = new ManageableScene();
			
			//if scene is a camera scene setup the camera
			if(iManageableScene instanceof ManageableCameraScene){
				((ManageableCameraScene)iManageableScene).setCamera(theApplication.getCamera());
				((ManageableCameraScene) iManageableScene).setBackgroundEnabled(false);
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Call Methods to initialize and load the scene
		if(iManageableScene != null){
			iManageableScene.initScene(this, mEngine, mContext,theApplication); 	// Initialize scene with context info
			iManageableScene.loadScene(pSCDercriptor);								// Create the scene by loading & initializing all scene component			//refresh persistent components
			mapScenes.put(strSceneName, iManageableScene);							// add scene to active scene map
		}
		else
			throw new NullPointerException("In BuildScenes: the scene: " + strSceneName + " cannot being created");

		//Manage specific scene attribures
		//manage Pinch and zoom scene
		if(theApplication instanceof IOnSceneTouchListener && iManageableScene.hasPinchAndZoomActive()){
			//enable pinch & zoom for the scene
			((ManageableScene)iManageableScene).setOnAreaTouchTraversalFrontToBack();
			((ManageableScene)iManageableScene).setOnSceneTouchListener((IOnSceneTouchListener)theApplication);
			((ManageableScene)iManageableScene).setTouchAreaBindingOnActionDownEnabled(true);
		}
		
		return iManageableScene;
	}
	/**
	* GET CONFIGURED SCENE /get a configures scene
	* @param String strSceneName the name of the Scene to create 
	* @return	Return a new scene
	* @return 	null  if there is not configures scene with this name
	* @thrown	NullPointerException is the requested scene cannot be build sue to wrong parent scene;
	*/
	private Scene getConfiguredScene(String strSceneName){

		// get configurable scene descriptor
		ConfiguredSceneDescriptor pCFGScene = pSDM.getCFGScene(strSceneName);
		
		//if a configurable scene descriptor exist instantiate the scene 
		if(!(null == pCFGScene)){
			//get the master-scene (recursively crete it if the scene is not in the map)
			Scene theScene = (Scene) getScene(pCFGScene.getNameOfSceneMaster());

			//if master scene is not obtained  then generate an exception
			if(!(theScene instanceof IConfigurableScene))
				throw new NullPointerException("Instantiation of a non Configurable Scene"); 

			//configure the scene
			((IConfigurableScene)theScene).configure(pCFGScene);

			return theScene;
		}
		else
			return null;
		
	}
	
}