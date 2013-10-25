package com.welmo.andengine.scenes.components.buttons;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

import com.welmo.andengine.scenes.components.IButtonMessageHandler;


public abstract class ButtonBasic extends Sprite {

	final static String 		TAG = "ButtonBasic"
	protected int 				nID 		= -1;
	protected Rectangle			insButton 	= null;

	IButtonMessageHandler		mMessageHandler			= null;

	public ButtonBasic(float pX, float pY, float pWidth, float pHeight, ITextureRegion texture, VertexBufferObjectManager pVertexBufferObjectManager,
			IButtonMessageHandler messageHandler ) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
		if(! (messageHandler instanceof IButtonMessageHandler))
			throw new NullPointerException("the message handler is not right class type");
		this.mMessageHandler = messageHandler;
		init();
	}

	void init(){

		//set button default background
		this.setColor(Red(TOOLBARBACKGROUND),Green(TOOLBARBACKGROUND),Blue(TOOLBARBACKGROUND));
		//create inside button
		if(insButton != null){
			this.detachChild(insButton);
			insButton = null;
		}
		this.insButton = new Rectangle(0,0,BUTTOINTDIM,BUTTOINTDIM,pVBO);
		insButton.setPosition(INBUTTONPXY,INBUTTONPXY);
		pTheScene.registerTouchArea(insButton);
		attachChild(insButton);
	}

	@Override
	abstract public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y);
}
