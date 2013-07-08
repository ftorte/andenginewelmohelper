package com.welmo.andengine.scenes.components;

import org.andengine.entity.shape.IAreaShape;


import com.welmo.andengine.scenes.components.CardSprite.CardSide;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;

public interface IActionOnSceneListener {
	public boolean onActionChangeScene(String nextSceneName); // Sprite call this interface to inform scene parentthat has been clicked
	public void onStick(IAreaShape currentShapeToStick, SceneActions stickActionDescription);
	public void onFlipCard(int CardID, CardSide currentSide);
	public void lockTouch();
	public void unLockTouch();
}