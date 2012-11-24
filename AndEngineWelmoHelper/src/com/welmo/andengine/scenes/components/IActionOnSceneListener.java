package com.welmo.andengine.scenes.component;

import org.andengine.entity.shape.IAreaShape;


import com.welmo.andengine.scenes.components2.CardSprite.CardSide;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;

public interface IActionOnSceneListener {
	public boolean onActionChangeScene(String nextSceneName); // Sprite call this interface to inform scene parentthat has been clicked
	public void onStick(IAreaShape currentShapeToStick, SceneActions stickActionDescription);
	public void onFlipCard(int CardID, CardSide CardSide);
}