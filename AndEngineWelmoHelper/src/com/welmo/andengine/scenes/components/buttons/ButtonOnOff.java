package com.welmo.andengine.scenes.components.buttons;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.welmo.andengine.scenes.components.interfaces.IActionSceneListener;
import com.welmo.andengine.scenes.descriptors.components.ButtonDescriptor;
import com.welmo.andengine.scenes.operations.IOperationHandler;
import com.welmo.andengine.scenes.operations.Operation;
import com.welmo.andengine.scenes.operations.IOperationHandler.OperationTypes;

public class ButtonOnOff extends ButtonBasic{
	
	public static boolean					ON 			= true;
	public static boolean					OFF 		= false;
	public static int						BUTTON_ON 	= 1;
	public static int						BUTTON_OFF 	= 2;
	
	boolean 								bON 		= false;
	
	protected Operation  					msgON		= null;
	protected Operation						msgOFF		= null;
	
	
	public ButtonOnOff(ButtonDescriptor parameters, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(parameters,pVertexBufferObjectManager);
		
		msgON = new Operation(OperationTypes.ON,0f);
		msgOFF = new Operation(OperationTypes.OFF,0f);
	}
	@Override
	public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){

		if(touchEvent.isActionUp()){
			if(this.mParameters.bSpriteBased){
				if(bON) {
					this.setOFF();
					this.mMessageHandler.doOperation(msgON);
				}
				else{
					this.setON();
					this.mMessageHandler.doOperation(msgOFF);
				}
			}
			else{

			}
		}
		return true;
	}
	
	
	public void setON(){
		if(!bON){
			this.bON = true;
			this.insButtonOFF.setVisible(false);
			this.insButtonON.setVisible(true);
		}
	}
	public void setOFF(){
		if(bON){
			this.bON = false;
			this.insButtonON.setVisible(false);
			this.insButtonOFF.setVisible(true);
		}
	}
	@Override
	public void parseMessage(ButtonDescriptor pDsc) {
		pDsc.getOnClickMessage();
	}
	@Override
	public void setActionSceneListner(IActionSceneListener scenelistener) {
		// TODO Auto-generated method stub
		
	}
}
