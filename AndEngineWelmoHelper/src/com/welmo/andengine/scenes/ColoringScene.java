package com.welmo.andengine.scenes;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

import android.util.Log;

import com.welmo.andengine.scenes.components.ColoringSprite;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.SceneDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ColoringSpriteDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ScnTags;
import com.welmo.andengine.scenes.operations.IOperationHandler;
import com.welmo.andengine.scenes.operations.Operation;
public class ColoringScene extends ManageableScene implements IConfigurableScene, IOperationHandler {
	
	// ===========================================================
	// Constants
	// ===========================================================
	private static final String 	TAG 				= "ColoringScene";
	private ColoringSprite			theColoringImage 	= null;
	@SuppressWarnings("unused")
	private String					sImageFileName		= null;

	// ===========================================================
	// Fields
	// ===========================================================	
	Deque<Operation> 			qMessageStack = new LinkedList<Operation>();

	@Override
	public void configure(ArrayList<String> parameterList) {
		String fileName = parameterList.get(0);
		theColoringImage.loadImage(fileName);
	}
	

	
	@Override
	public void loadScene(SceneDescriptor sceneDescriptor) {
		super.loadScene(sceneDescriptor);
		
		String imageFileName=null;

		//after having loaded the default component of the scene with superclass load the coloring sprite is exist
		for(BasicDescriptor scObjDsc:sceneDescriptor.pChild.values()){
			if(scObjDsc instanceof ColoringSpriteDescriptor){
				ColoringSpriteDescriptor theCororingSprite = (ColoringSpriteDescriptor)scObjDsc;
				imageFileName = theCororingSprite.getImageFilename();
				if(theColoringImage== null){
					theColoringImage = new ColoringSprite(100, 0, pRM.getDecoratedTextureRegion("MonsterColor",imageFileName), this.mEngine.getVertexBufferObjectManager());
					this.attachChild(theColoringImage);
					this.registerTouchArea(theColoringImage);
				}
				else{
					theColoringImage.loadImage(imageFileName);
				}
			}
		}
	}
	
	public void setColorFill(int color){
		theColoringImage.setColorFill(color);
	}


	// ===========================================================
	// ISceneMessageHandler Methods
	// ===========================================================	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void doOperation(Operation msg) {
		switch(msg.type){
			case SET_COLOR:
				Log.i(TAG,"SET_COLOR");
				setColorFill(msg.parameters.get(0));
				break;
			case RESET_SCROLL_ZOOM:
				Log.i(TAG,"RESET_SCROLL");
				if(hdFatherSceneMessageHandler != null)hdFatherSceneMessageHandler.doOperation(msg);
				break;
			case COLORING_CKIK:
				Log.i(TAG,"COLORING_CKIK");
				qMessageStack.push(new Operation(msg));
				break;
			case UNDO:
				Log.i(TAG,"UNDO");
				if(!qMessageStack.isEmpty()){
					undoOperation(qMessageStack.pop());
				}
				break;
		}// TODO Auto-generated method stub
		
	}
	@Override
	public void undoOperation(Operation ope) {
		IOperationHandler hdOperation = ope.getHander();// TODO Auto-generated method stub
		hdOperation.undoOperation(ope);
	}	
}
