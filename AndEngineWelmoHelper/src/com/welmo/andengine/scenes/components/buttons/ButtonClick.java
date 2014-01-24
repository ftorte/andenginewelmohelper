package com.welmo.andengine.scenes.components.buttons;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.welmo.andengine.scenes.components.buttons.ButtonBasic.Types;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ButtonDescriptor;
import com.welmo.andengine.scenes.operations.IOperationHandler;
import com.welmo.andengine.scenes.operations.Operation;
import com.welmo.andengine.scenes.operations.IOperationHandler.OperationTypes;

public class ButtonClick extends ButtonBasic{
	
	public ButtonClick(ButtonDescriptor parameters,
			IOperationHandler messageHandler,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(parameters, messageHandler, pVertexBufferObjectManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y) {
		switch(touchEvent.getAction()){
			case TouchEvent.ACTION_DOWN:
				this.insButtonOFF.setVisible(false);
				this.insButtonON.setVisible(true);
				return true;
			case TouchEvent.ACTION_UP:
				this.insButtonOFF.setVisible(true);
				this.insButtonON.setVisible(false);
				if(mMessageHandler!=null && this.mMessages.size()>0)
					mMessageHandler.doOperation(this.mMessages.get(0));
				return true;
			default:
				return true;
		}	
	}

	@Override
	public void build(BasicDescriptor pDsc) {
		if(!(pDsc instanceof ButtonDescriptor))
			throw new NullPointerException("Wrong descriptor type: expected ButtonDescriptor");
		if (Types.CLICK != Types.valueOf(pDsc.getSubType()))
			throw new NullPointerException("Wrong button type");
	
		configure((ButtonDescriptor)pDsc);
		init();
	}
}
