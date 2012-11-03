package com.welmo.andengine.scenes.components;

import java.util.List;

import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.shape.RectangularShape;

import com.welmo.andengine.scenes.descriptors.events.SceneActionsSet;

public interface IActionOnSceneListener {
	public boolean onActionChangeScene(String nextSceneName); // Sprite call this interface to inform scene parentthat has been clicked
	public void onStick(IAreaShape currentShapeToStick, SceneActionsSet stickActionDescription);
}