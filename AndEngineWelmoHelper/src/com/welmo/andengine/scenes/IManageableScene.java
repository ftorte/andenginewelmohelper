package com.welmo.andengine.scenes;

import org.andengine.engine.Engine;

import com.welmo.andengine.managers.SceneManager;
import com.welmo.andengine.scenes.descriptors.components.SceneDescriptor;

import android.content.Context;

public interface IManageableScene {
	public void loadScene(SceneDescriptor sceneDescriptor);
	public void init(Engine theEngine, Context ctx);
	public void resetScene();
	public String getFatherScene();
	public void setSceneManager(SceneManager sceneManager);
}
