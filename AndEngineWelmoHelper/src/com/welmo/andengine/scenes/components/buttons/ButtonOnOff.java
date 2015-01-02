package com.welmo.andengine.scenes.components.buttons;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.andengine.entity.IEntity;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.welmo.andengine.managers.SharedPreferenceManager;
import com.welmo.andengine.scenes.components.interfaces.IActionSceneListener;
import com.welmo.andengine.scenes.components.interfaces.IPersistent;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ButtonDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ButtonSceneLauncherDescriptor;
import com.welmo.andengine.scenes.operations.IOperationHandler;
import com.welmo.andengine.scenes.operations.Operation;
import com.welmo.andengine.scenes.operations.IOperationHandler.OperationTypes;

public class ButtonOnOff extends ButtonBasic implements IPersistent{
	
	public static boolean					ON 			= true;
	public static boolean					OFF 		= false;
	public static int						BUTTON_ON 	= 1;
	public static int						BUTTON_OFF 	= 2;
	
	boolean 								bON 		= false;
	
	protected Operation  					msgPUT_ON		= null;
	protected Operation						msgPUT_OFF		= null;
	
	protected boolean						bActivePersitence  = true;
	
	
	
	public ButtonOnOff(ButtonDescriptor parameters, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(parameters,pVertexBufferObjectManager);
	}
	@Override
	public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){

		if(touchEvent.isActionUp()){
			if(this.mParameters.bSpriteBased){
				if(bON) {
					this.setOFF();
					this.mMessageHandler.doOperation(msgPUT_OFF);
				}
				else{
					this.setON();
					this.mMessageHandler.doOperation(msgPUT_ON);
				}
				if(this.bIsPersistent)
					this.doSave();
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
	// -----------------------------------------------------------------------------------------------------------------
	// Override functions 
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public void parseMessage(ButtonDescriptor pDsc){
		
		if(msgPUT_ON == null)
			msgPUT_ON = new Operation(OperationTypes.NULL,0f);
		if(msgPUT_OFF == null)
			msgPUT_OFF = new Operation(OperationTypes.NULL,0f);
		
		
		if(pDsc.getOnClickMessage()!=""){
			StringTokenizer st = new StringTokenizer(pDsc.getOnClickMessage(),",");
			String strOperation = st.nextToken();
			msgPUT_ON.setType(OperationTypes.valueOf(strOperation));
			msgPUT_ON.setParametersBoolean(true);
			msgPUT_OFF.setType(msgPUT_ON.getType());
			msgPUT_OFF.setParametersBoolean(false);
		}
	}
	@Override
	public IEntity getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(IEntity parent) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setActionSceneListner(IActionSceneListener scenelistener) {
		// TODO Auto-generated method stub
		
	}
	// -----------------------------------------------------------------------------------------------------------------
	// Override IPersistent doLoad and doSave 
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public void doLoad() {
		super.doLoad();
		if(mParameters.sGlobaVariable != null){
			SharedPreferences sp = pSPM.getSharedPreferences(SharedPreferenceManager.STDPreferences.GLOBA_VARIABLES.name());
			if(sp.getBoolean(mParameters.sGlobaVariable, true))
				this.setON();
			else
				this.setOFF();
		}
	}

	@Override
	public void doSave() {
		if(mParameters.sGlobaVariable != null){
			Editor ed = pSPM.getSharedPreferences(SharedPreferenceManager.STDPreferences.GLOBA_VARIABLES.name()).edit();
			ed.putBoolean(mParameters.sGlobaVariable, bON);
			ed.commit();
		}
	}
}
