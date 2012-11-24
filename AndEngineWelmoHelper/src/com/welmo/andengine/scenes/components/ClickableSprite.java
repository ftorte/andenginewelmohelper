package com.welmo.andengine.scenes.components;

import java.util.HashMap;

import org.andengine.engine.Engine;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import android.util.Log;
import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.components.CardSprite.CardSide;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;

public class ClickableSprite extends Sprite implements IClickableSprite, IActionOnSceneListener{
	// ===========================================================
	// Constants
	// ===========================================================
	//Log & Debug
	private static final String TAG = "ClickableSprite";
	// ===========================================================
	// Fields
	// ===========================================================
	private IActionOnSceneListener   mActionListener		=null;
	private int nID											=-1;
	HashMap<Events,IComponentEventHandler> hmEventHandlers = null;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public ClickableSprite(SpriteObjectDescriptor pSPRDscf, ResourcesManager pRM, Engine theEngine){
		super(pSPRDscf.getIPosition().getX(), pSPRDscf.getIPosition().getY(), 
				pSPRDscf.getIDimension().getWidth(), pSPRDscf.getIDimension().getHeight(), 
				pRM.getTextureRegion(pSPRDscf.getTextureName()), 
				theEngine.getVertexBufferObjectManager());
		init();
		configure(pSPRDscf); 
	}
	// ===========================================================
	// private member function
	// ===========================================================	
	protected void init(){
		hmEventHandlers			= new HashMap<Events,IComponentEventHandler>();
	}
	// ===========================================================
	// public member function
	// ===========================================================	
	
	public void configure(SpriteObjectDescriptor spDsc){
		ResourcesManager pRM = ResourcesManager.getInstance();
		setID(spDsc.getID());
		
		/* Setup Rotation*/
		setRotationCenter(spDsc.getIOriantation().getRotationCenterX(), spDsc.getIOriantation().getRotationCenterX());
		setRotation(spDsc.getIOriantation().getOrientation());
		
		//set position			
		setX(spDsc.getIPosition().getX());
		setY(spDsc.getIPosition().getY());
		
		//set Z_Order
		this.setZIndex(spDsc.getIPosition().getZorder());	
		
		//set color	
		String theColor = spDsc.getICharacteristis().getColor();
		Log.i(TAG,"Get Color: " + theColor);
		if(!theColor.equals(""))
			this.setColor(pRM.getColor(theColor));
	}
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		
		boolean managed = false;
		switch (pSceneTouchEvent.getAction()) {
		case TouchEvent.ACTION_DOWN:
			/*if (MLOG.LOG)Log.i(TAG,"onAreaTouched ACTION_DOWN = " + nID);
			break;*/
		case TouchEvent.ACTION_MOVE:
			break;
		case TouchEvent.ACTION_UP:
			if(hmEventHandlers != null){
				Log.i(TAG,"\t launch event handler trough object control");
				IComponentEventHandler handlerEvent = hmEventHandlers.get(ComponentEventHandlerDescriptor.Events.ON_CLICK);
				if(handlerEvent != null){
					Log.i(TAG,"\t launch event handler trough object found");
					handlerEvent.handleEvent(this);
				}
			}
			break;
		}
		return managed;
	}
	public int getID() {
		return nID;
	}
	public void setID(int ID) {
		this.nID = ID;
	}
	// ===========================================================
	// Interfaces & Superclass
	// ===========================================================	
	// ===========================================================		
	// ====== IClickableSprite ==== 	
	public void addEventsHandler(Events theEvent, IComponentEventHandler oCmpDefEventHandler){
			hmEventHandlers.put(theEvent, oCmpDefEventHandler);
	}
	public void setActionOnSceneListener(IActionOnSceneListener actionLeastner) {
		this.mActionListener=actionLeastner;
	}
	public IActionOnSceneListener getActionOnSceneListener(){
		return mActionListener;
	}
	// ===========================================================		
	// ====== IActionOnSceneListener ==== 	
	@Override
	public boolean onActionChangeScene(String nextSceneName) {
		return this.mActionListener.onActionChangeScene(nextSceneName);
	}
	@Override
	public void onStick(IAreaShape currentShapeToStick,
			SceneActions stickActionDescription) {
		this.mActionListener.onStick(currentShapeToStick, stickActionDescription);
		
	}
	@Override
	public void onFlipCard(int CardID, CardSide CardSide) {
		// TODO Auto-generated method stub
		
	}
}