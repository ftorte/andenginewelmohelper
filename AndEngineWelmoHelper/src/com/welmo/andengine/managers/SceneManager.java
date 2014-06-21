package com.welmo.andengine.managers;

import java.util.HashMap;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import com.welmo.andengine.scenes.IConfigurableScene;
import com.welmo.andengine.scenes.IManageableScene;
import com.welmo.andengine.scenes.ManageableScene;
import com.welmo.andengine.scenes.components.interfaces.IActivitySceneListener;
import com.welmo.andengine.scenes.descriptors.ConfiguredSceneDescriptor;
import com.welmo.andengine.scenes.descriptors.SceneDescriptor;




import android.content.Context;

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
	BaseGameActivity						theApplication; 		// the activity
	// ===========================================================
	// Constructors
	/** 
	 * Constructor of scene manger. He initialize the scene hash map & the pointer to the application.
	 * A scene manager is attached to the that create it application => So is not a singleton and Scenes cannot be shared through applications.
	 * 
	 * @param BaseGameActivity application
	 */
	public SceneManager(BaseGameActivity application) {  
		mapScenes = new HashMap<String, IManageableScene>();
		theApplication = application;
	}  
	// ===========================================================
	// public methods
	/**
	 * INITIALIZE the scene manager. If not called all public methods are disabled.
	 * The initialization set-up the Engine, the Context and link the the Scene Descriptions Manger
	 * @param Engine 	eng		// The andengine engine
	 * @param Context 	ctx		// The context on which the application/scene are executes
	 */
	public synchronized void init(Engine eng, Context ctx){
		if(!initialized){
			mEngine = eng;
			mContext = ctx;
			pSDM = SceneDescriptorsManager.getInstance();
			initialized = true;
		}
		else
			//if initialization is called for an already initialized manager but with different context and engine throw an exception
			if(mContext != ctx || mEngine!= eng) //if initialization called on another context
				throw new IllegalArgumentException("ResourceManager, Init called two times with different context");
	}
	// Get the scene 
	/**
	 * GETSCENE: method to obtain a Scene. 
	 * If the scene is not found the scene manager try to build the scene. If is not able to buld it the function
	 * return the null value
	 * 
	 * @param 	strSceneName the name of the scene to obtain
	 * @return 	the Scene or null
	 */
	public Scene getScene(String strSceneName){
		
		Scene theScene = null;
		
		if(!initialized)
			throw new NullPointerException("Scene Manager not initialized"); 
		
		//get the scene
		theScene = (Scene)mapScenes.get(strSceneName);
		
		//if scene not found check if it is a request for a configurable scene
		if(theScene == null){
			ConfiguredSceneDescriptor pCFGScene = null;;
			if (!(null ==(pCFGScene = pSDM.getCFGScene(strSceneName))))
				theScene = getConfiguredScene(pCFGScene);
		}
		
		//if is not a configure try to build the scene
		if(theScene == null)
			theScene =  (Scene) BuildScenes(strSceneName);
		
		return theScene;
		
	}
	// ===========================================================
	// private methods
	/**
	 * BUILD A SCENE.  /create scene and add to the map with name strSceneName
	 * @param String strSceneName the neme of the Scene to create 
	 * @return	Return a new scene
	 * @thrown	If Scene Manger is not initialized
	 * @thrown	If Scene descriptor don't exist for the requested scene
	 */
	private synchronized IManageableScene BuildScenes(String strSceneName){
		if(!initialized)
			throw new NullPointerException("Scene Manager not initialized"); 
		
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

		// Call Methods to create the scene
		if(iManageableScene != null){
			iManageableScene.initScene(this, mEngine, mContext,theApplication); 	// Initialize scene with context info
			iManageableScene.loadScene(pSCDercriptor);						// Create the scene by loading & initilizing all scene component
			mapScenes.put(strSceneName, iManageableScene);					// add scene to active scene map
		}
		else
			throw new NullPointerException("In BuildScenes: the scene: " + strSceneName + " cannot being created");

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
	 * @param strSceneName
	 * @return
	 */
	
	private Scene getConfiguredScene(ConfiguredSceneDescriptor pCFGScene){
		String strMasterSceneName = null;
		
		//get the masterscene
		Scene theScene = (Scene) getScene(pCFGScene.getNameOfSceneMaster());
		
		if(!(theScene instanceof IConfigurableScene))
			throw new NullPointerException("Instantiation of a non Configurable Scene"); 
		
		((IConfigurableScene)theScene).configure(pCFGScene.getParameterList());
		
		
		return theScene;
	}
	public IActivitySceneListener getIActivitySceneListener(){
		if(theApplication instanceof IActivitySceneListener)
			return (IActivitySceneListener)theApplication;
		return null;
	}
	
}