package com.welmo.andengine.scenes;

import org.andengine.engine.Engine;
import org.andengine.ui.activity.BaseGameActivity;

import com.welmo.andengine.managers.SceneManager;
import com.welmo.andengine.managers.SharedPreferenceManager;
import com.welmo.andengine.scenes.components.interfaces.IActivitySceneListener;
import com.welmo.andengine.scenes.descriptors.SceneDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.operations.IOperationHandler;

import android.content.Context;

public interface IManageableScene {
	public void 	loadScene(SceneDescriptor sceneDescriptor);
	public void 	initScene(SceneManager pSM, Engine theEngine, Context ctx, IActivitySceneListener activity);
	public void 	resetScene();
	public String 	getFatherScene();
	public void 	setFatherScene(String fatherScene);
	public boolean 	hasPinchAndZoomActive();
	public boolean 	hasHUD();
	public void 	setFatherSceneMessageHandler(IOperationHandler pMgsHnd);
	public void 	refreshPersistentComponents(SharedPreferenceManager pSPM);
	public String 	getSceneName();
	public boolean 	onFireEvent(Events event);
	public String 	getLicenceID();
}
