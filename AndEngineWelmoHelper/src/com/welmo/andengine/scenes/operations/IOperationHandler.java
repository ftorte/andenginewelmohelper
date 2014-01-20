package com.welmo.andengine.scenes.operations;


public interface IOperationHandler {
	public enum OperationTypes{
		NULL,
		SET_COLOR, 					// ColorPicker Tool Bar Set Colod
		ON,OFF,						// Button status change
		RESET_SCROLL_ZOOM,			// Button REset Scroll & zoom to 0
		COLORING_CKIK,				// Coloring Sprite clik on a point
		UNDO						// undolastoperation
	}
	public void doOperation(Operation msg);
	public void undoOperation(Operation msg, int ObjectID);
}
