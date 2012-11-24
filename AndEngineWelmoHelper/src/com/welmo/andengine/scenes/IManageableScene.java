package com.welmo.andengine.scenes;

import org.andengine.engine.Engine;
import org.andengine.ui.activity.BaseGameActivity;

import com.welmo.andengine.managers.SceneManager;
import com.welmo.andengine.scenes.descriptors.components.SceneDescriptor;

import android.content.Context;

public interface IManageableScene {
	public void loadScene(SceneDescriptor sceneDescriptor);
	public void init(Engine theEngine, Context ctx, BaseGameActivity activity);
	public void resetScene();
	public String getFatherScene();
	public void setSceneManager(SceneManager sceneManager);
}
