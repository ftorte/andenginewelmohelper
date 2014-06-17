package com.welmo.andengine.scenes;

import org.andengine.engine.Engine;
import org.andengine.ui.activity.BaseGameActivity;

import com.welmo.andengine.managers.SceneManager;
import com.welmo.andengine.scenes.descriptors.SceneDescriptor;
import com.welmo.andengine.scenes.operations.IOperationHandler;

import android.content.Context;

public interface IManageableScene {
	public void 	loadScene(SceneDescriptor sceneDescriptor);
	public void 	initScene(SceneManager pSM, Engine theEngine, Context ctx, BaseGameActivity activity);
	public void 	resetScene();
	public String 	getFatherScene();
	public void 	setFatherScene(String fatherScene);
	public boolean 	hasPinchAndZoomActive();
	public boolean 	hasHUD();
	public void 	setFatherSceneMessageHandler(IOperationHandler pMgsHnd);
}
