package com.welmo.andengine.scenes;

import java.util.Map;

import com.welmo.andengine.scenes.components.ColoringSprite;
import com.welmo.andengine.scenes.descriptors.SceneDescriptor;
import com.welmo.andengine.scenes.messages.ISceneMessageHandler;
import com.welmo.andengine.scenes.messages.Message;

public class ColoringScene extends ManageableScene implements IConfigurableScene, ISceneMessageHandler {
	
	// ===========================================================
	// Constants
	// ===========================================================
	private static final String TAG = "ColoringScene";
	private ColoringSprite	theColoringImage = null;


	// ===========================================================
	// Fields
	// ===========================================================	


	@Override
	public void configure(Map<String,String> parameterList) {
		
		this.bImplementPinchAndZoom = true;
		this.resetScene();
		this.loadScene(this.pSCDescriptor);
		this.resetScene();
	}
	

	
	@Override
	public void loadScene(SceneDescriptor sceneDescriptor) {
		super.loadScene(sceneDescriptor);
		
		if(theColoringImage== null){
			theColoringImage = new ColoringSprite(50, 25, pRM.getDecoratedTextureRegion("MonsterColor","gfx/monster05BW.png"), this.mEngine.getVertexBufferObjectManager());
			this.attachChild(theColoringImage);
			this.registerTouchArea(theColoringImage);
		}
		else{
			theColoringImage.loadImage("gfx/monster05BW.png");
			return;
		}
	}
	
	public void setColorFill(int color){
		theColoringImage.setColorFill(color);
	}


	// ===========================================================
	// ISceneMessageHandler Methods
	// ===========================================================	
	@Override
	public void SendMessage(Message msg) {
		switch(msg.type){
			case SET_COLOR:
				setColorFill(msg.parameters.get(0));
				break;
		}// TODO Auto-generated method stub
		
	}	
}
