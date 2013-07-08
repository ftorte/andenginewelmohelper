package com.welmo.andengine.managers;

import java.util.HashMap;
import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import com.welmo.andengine.scenes.IManageableScene;
import com.welmo.andengine.scenes.ManageableScene;
import com.welmo.andengine.scenes.descriptors.components.BasicObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.SceneDescriptor;


import android.content.Context;

public class SceneManager {

	// Constants
	// ===========================================================
	//Log & Debug & trace
	private static final String TAG = "SceneManager";	
	// ===========================================================
	// Variables
	// ===========================================================
	private Engine 							mEngine=null;
	private Context 						mContext=null;
	HashMap<String, IManageableScene> 		mapScenes =null;
	protected SceneDescriptorsManager 		pSDM;
	boolean 								initialized = false;
	BaseGameActivity						theApplication; 


	//final Scene scene = new Scene();
	// ===========================================================
	public SceneManager(BaseGameActivity application) {  
		mapScenes = new HashMap<String, IManageableScene>();
		theApplication = application;
	}  


	// ===========================================================

	public synchronized void init(Engine eng, Context ctx){
		if(!initialized){
			mEngine = eng;
			mContext = ctx;
			pSDM = SceneDescriptorsManager.getInstance();
			initialized = true;
		}
		else
			//if initialization called for already initialized manager but with different context and engine throw an exception
			if(mContext != ctx || mEngine!= eng) //if initialization called on another context
				throw new IllegalArgumentException("ResourceManager, Init called two times with different context");
	}


	public void setSceneDescriptionManager(SceneDescriptorsManager pSDM) {
		this.pSDM = pSDM;
	}
	
	//create scene and add to the map with name strSceneName
	//public void BuildScenes(String strSceneName, BaseGameActivity activity){
	public synchronized IManageableScene BuildScenes(String strSceneName){
		if((mEngine == null) | (mContext == null)){ 
			throw new NullPointerException("Scene Manager not initialized: mEngine &/or mContext are null"); 
		}
		
		SceneDescriptor pSCDercriptor;
		if((pSCDercriptor = pSDM.getScene(strSceneName))== null)
			return null;
	
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

		if(iManageableScene != null){
			iManageableScene.init(mEngine, mContext,theApplication);
			iManageableScene.loadScene(pSCDercriptor);
			iManageableScene.setSceneManager(this);
			mapScenes.put(strSceneName, iManageableScene);
		}
		else
			throw new NullPointerException("In BuildScenes: the scene: " + strSceneName + " cannot being created");

		return iManageableScene;
	}

	// Get the scene strSceneName
	public Scene getScene(String strSceneName){
		Scene theScene = (Scene)mapScenes.get(strSceneName);
		if(theScene == null)
			this.BuildScenes(strSceneName);
		
		return (Scene) mapScenes.get(strSceneName);
	}
	// Get the scene strSceneName
	public synchronized void addScene(String strSceneName, IManageableScene theScene){
		mapScenes.put(strSceneName, theScene);
	}
}