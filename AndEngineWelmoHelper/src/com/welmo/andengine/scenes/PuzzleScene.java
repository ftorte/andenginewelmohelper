package com.welmo.andengine.scenes;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.andengine.entity.shape.IAreaShape;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.welmo.andengine.managers.SharedPreferenceManager;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.components.interfaces.IComponentClickable;
import com.welmo.andengine.scenes.components.interfaces.IComponentEventHandler;
import com.welmo.andengine.scenes.components.interfaces.IPersistent;
import com.welmo.andengine.scenes.components.puzzle.PuzzleSprites;
import com.welmo.andengine.scenes.descriptors.ConfiguredSceneDescriptor;
import com.welmo.andengine.scenes.descriptors.SceneDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ButtonSceneLauncherDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ButtonSceneLauncherDescriptor.Status;
import com.welmo.andengine.scenes.descriptors.components.PuzzleObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.operations.IOperationHandler;
import com.welmo.andengine.scenes.operations.Operation;

public class PuzzleScene extends ManageableScene implements IConfigurableScene , IPersistent {
	
	// ===========================================================
	// Constants
	// ===========================================================
	protected static final String 				TAG 				= "ColoringScene";
	protected PuzzleSprites 					thePuzzle			= null;
	protected int								nLastResult			= 0;
	protected int								nNBOfStars			= 0;
	protected boolean							bIsAnInstance		= false;
	protected String							strInstantiatedSceneName = "";
	protected String							strLaunchStatus		= ButtonSceneLauncherDescriptor.Status.Locked.name();
	protected String							strEndScene		= null;

	// ===========================================================
	// Fields
	// ===========================================================	
	Deque<Operation> 			qMessageStack 		= new LinkedList<Operation>();

	//Object Status values handler
	Boolean									bIsPersistent			= true;
		
		
	@Override
	public void loadScene(SceneDescriptor sceneDescriptor) {
		super.loadScene(sceneDescriptor);
	
		Iterator<Entry<Integer, IAreaShape>> it 	= mapOfObjects.entrySet().iterator();
		
	    while (it.hasNext()) {
	    	IAreaShape value = it.next().getValue();
	    	if(value  instanceof PuzzleSprites)
	    		thePuzzle=(PuzzleSprites)value;
	    }
	    
	    doLoad(pSPM.getSharedPreferences(this.pSCDescriptor.sceneName),null);
	    
	    if(sceneDescriptor.getEndScene() != null)
	    	strEndScene = new String(sceneDescriptor.getEndScene());
	    
	}
		
	// ===========================================================
	// IConfigurableScene Methods
	// ===========================================================	
	@Override
	public void configure(ConfiguredSceneDescriptor pCFGScene) {
		
		bIsAnInstance = true;
		
		strInstantiatedSceneName = pCFGScene.getSceneName();
		
		ArrayList<String> parameterList = pCFGScene.getParameterList();
		
		String[] 	tokens = parameterList.get(0).split(";");
		
		//first clear the puzzle
		thePuzzle.clearPuzzle();
		
		//setup new parameteres
		thePuzzle.setmTiledTextureResource(tokens[0]);
		thePuzzle.setmHelperImage(tokens[1]);
		
		//setup nw numbers of puzzle's rows and columbns 
		if(tokens.length > 2){
			thePuzzle.setNbCols(Integer.parseInt(tokens[2]));
			thePuzzle.setNbRows(Integer.parseInt(tokens[3]));
		}
		
		//setup name of father scene that launched the puzzle
		if(tokens.length > 4){
			this.setFatherScene(tokens[4]);
		}
		thePuzzle.createPuzzle();
		
		doLoad(pSPM.getSharedPreferences(strInstantiatedSceneName),null);
	}

	
	public void doLoad(SharedPreferences sp, String url_root) {
		nLastResult = sp.getInt("lastResult", 0);
		strLaunchStatus = sp.getString("LaunchStatus", ButtonSceneLauncherDescriptor.Status.Locked.name());
	}

	@Override
	public void setSharedPreferenceManager(SharedPreferenceManager sp) {
		pSPM = sp;
	}
	@Override
	public void onResult(int result, int score, String scoremessage) {
		
		ButtonSceneLauncherDescriptor.Status newStatus = Status.level0;
		
		switch(result){
			case 0: newStatus = Status.level0; break;
			case 1: newStatus = Status.level1; break;
			case 2: newStatus = Status.level2; break;
			case 3: newStatus = Status.level3; break;
			default: break;	
		};
		
		nLastResult = result;
		strLaunchStatus = newStatus.name();

		// save the new status and result
		SharedPreferences sp = null;

		if(bIsAnInstance)
			sp = pSPM.getSharedPreferences(strInstantiatedSceneName);
		else
			sp = pSPM.getSharedPreferences(this.pSCDescriptor.sceneName);

		Editor ed = sp.edit();
		ed.putInt("lastResult", result);
		ed.putString("LaunchStatus", strLaunchStatus);
		ed.commit();
		
		
		// Create Massage to signal to the father scene that the scene ended with status success and value equal to result
		Operation theOperation = new Operation();
		theOperation.setType(IOperationHandler.OperationTypes.ACTIVATE_BTN_NEXT_SCENE_LAUNCER);
		
		if(bIsAnInstance)
			theOperation.setParameterString(strInstantiatedSceneName);
		else
			theOperation.setParameterString(this.pSCDescriptor.sceneName);
		
		this.hdFatherSceneMessageHandler.doOperation(theOperation);
		//TO DO use a parameter instead of hardcoding it
		if(this.strEndScene != null){
			ArrayList<String> parameters = new ArrayList<String>();
			parameters.add(String.valueOf(result));
			parameters.add(String.valueOf(score));
			parameters.add(new String(scoremessage));
			
			this.pIActivitySceneListener.onLaunchChildScene(strEndScene, parameters);
		}
		
	}

	@Override
	public void doLoad() {
		if(pSPM == null)
			throw new NullPointerException("In doSave the Shared Preferences Manager is null");
		
		SharedPreferences sp = null;
		if(bIsAnInstance)
			sp = pSPM.getSharedPreferences(strInstantiatedSceneName);
		else
			sp = pSPM.getSharedPreferences(this.pSCDescriptor.sceneName);
		
		nLastResult = sp.getInt("lastResult", 0);
		strLaunchStatus = sp.getString("LaunchStatus", ButtonSceneLauncherDescriptor.Status.Locked.name());
		
	}

	@Override
	public void doSave(){
		if(pSPM == null)
			throw new NullPointerException("In doSave the Shared Preferences Manager is null");
			
		SharedPreferences sp = null;
		if(bIsAnInstance)
			sp = pSPM.getSharedPreferences(strInstantiatedSceneName);
		else
			sp = pSPM.getSharedPreferences(this.pSCDescriptor.sceneName);
		
		Editor ed = sp.edit();
		ed.putInt("lastResult", nLastResult);
		ed.putString("LaunchStatus", strLaunchStatus);
		ed.commit();
	}

	@Override
	public void doLoad(SharedPreferenceManager sp) {
		pSPM = sp;
		doLoad();
	}

	@Override
	public void doSave(SharedPreferenceManager sp) {
		pSPM = sp;
		doSave();
	}

	@Override
	public boolean isPersitent() {
		return bIsPersistent;
	}

	@Override
	public String getNameOfInstantiatedScene() {
		return strInstantiatedSceneName;
	}

	@Override
	public void setParameter(ArrayList<String> parameterList) {
		// TODO Auto-generated method stub
		
	}
}
