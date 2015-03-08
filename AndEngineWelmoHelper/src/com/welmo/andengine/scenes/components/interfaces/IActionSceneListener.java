package com.welmo.andengine.scenes.components.interfaces;

import org.andengine.entity.shape.IAreaShape;

import com.welmo.andengine.scenes.components.CardSprite;
import com.welmo.andengine.scenes.components.CardSprite.CardSide;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;

public interface IActionSceneListener {
	public void setIActionOnSceneListener(IActionSceneListener pListener);
	public void onStick(IAreaShape currentShapeToStick, SceneActions stickActionDescription);
	public void onFlipCard(int CardID, CardSide currentSide);
	public void lockTouch();
	public void unLockTouch();
	public void onResult(int result);
	public void onResult(int result, int score, String string);			// sore tipe, score, string score
}