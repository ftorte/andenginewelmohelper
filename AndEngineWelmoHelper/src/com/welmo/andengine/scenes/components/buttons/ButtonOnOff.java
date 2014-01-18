package com.welmo.andengine.scenes.components.buttons;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.welmo.andengine.scenes.descriptors.components.ButtonDescriptor;
import com.welmo.andengine.scenes.messages.ISceneMessageHandler;
import com.welmo.andengine.scenes.messages.ISceneMessageHandler.MessageTypes;
import com.welmo.andengine.scenes.messages.Message;

public class ButtonOnOff extends ButtonBasic{
	
	public static boolean					ON 			= true;
	public static boolean					OFF 		= false;
	public static int						BUTTON_ON 	= 1;
	public static int						BUTTON_OFF 	= 2;
	
	boolean 								bON 		= false;
	
	protected Message  						msgON		= null;
	protected Message						msgOFF		= null;
	
	
	public ButtonOnOff(ButtonDescriptor parameters, ISceneMessageHandler messageHandler,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(parameters,messageHandler,pVertexBufferObjectManager);
		
		msgON = new Message(MessageTypes.ON,0);
		msgOFF = new Message(MessageTypes.OFF,0);
	}
	@Override
	public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){

		if(touchEvent.isActionUp()){
			if(this.mParameters.bSpriteBased){
				if(bON) {
					this.setOFF();
					this.mMessageHandler.SendMessage(msgON);
				}
				else{
					this.setON();
					this.mMessageHandler.SendMessage(msgOFF);
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
		// TODO Auto-generated method stub
		
	}
}
