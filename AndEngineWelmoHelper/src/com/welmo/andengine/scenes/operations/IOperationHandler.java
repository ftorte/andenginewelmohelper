package com.welmo.andengine.scenes.operations;


public interface IOperationHandler {
	public enum OperationTypes{
		NULL,
		SET_COLOR, 							// ColorPicker Tool Bar Set color
		RESET_SCROLL_ZOOM,					// Button REset Scroll & zoom to 0
		COLORING_CKIK,						// Coloring Sprite click on a point
		UNDO,								// Undo last operation
		ACTIVATE_BTN_NEXT_SCENE_LAUNCER,	// Set the button launcher status
		SET_MUSIC_ONOFF					    // Set Music On or OFF
	}
	public void doOperation(Operation msg);
	public void undoOperation(Operation msg);
}
