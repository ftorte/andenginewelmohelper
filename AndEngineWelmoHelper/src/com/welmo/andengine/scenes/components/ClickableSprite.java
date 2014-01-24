package com.welmo.andengine.scenes.components;

import org.andengine.engine.Engine;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import android.util.Log;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.components.CardSprite.CardSide;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;
import com.welmo.andengine.scenes.descriptors.events.SceneActions.ActionType;

public class ClickableSprite extends Sprite implements IClickable, IActivitySceneListener, IActionOnSceneListener,IBasicComponent{
	// ========================================================================
	// Constants
	// ========================================================================
	//Log & Debug
	private static final String 		TAG = "ClickableSprite";
	// ========================================================================
	// Fields implementation of IClickable, IActivitySceneListener, IActionOnSceneListene
	// ========================================================================
	//DefaultIClickableImplementation 	mIClicakableImpmementation = null;
	IClickable						mIClicakableImpmementation 	= null;	
	IActionOnSceneListener			mIActionOnSceneListener 	= null;
	IActivitySceneListener			mIActivitySceneListener 	= null;
	// ========================================================================
	// Constructors
	// ========================================================================
	public ClickableSprite(SpriteObjectDescriptor pSPRDscf, ResourcesManager pRM, Engine theEngine){
		super(pSPRDscf.getIPosition().getX(), pSPRDscf.getIPosition().getY(), 
				pSPRDscf.getIDimension().getWidth(), pSPRDscf.getIDimension().getHeight(), 
				pRM.getTextureRegion(pSPRDscf.getTextureName()), 
				theEngine.getVertexBufferObjectManager());
		
		mIClicakableImpmementation 	=   new DefaultIClickableImplementation();
		mIActionOnSceneListener		= 	null;
		mIActivitySceneListener 	=	null;
		mIClicakableImpmementation.setParent(this);
		
		configure(pSPRDscf); 
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
		
		//set alignement
		if(this.getParent() instanceof IAreaShape){
			PositionHelper.align(spDsc.getIPosition(), this, (IAreaShape)this.getParent() );
		}
		
		//set color	
		String theColor = spDsc.getICharacteristis().getColor();
		Log.i(TAG,"Get Color: " + theColor);
		if(!theColor.equals(""))
			this.setColor(pRM.getColor(theColor));
	}
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		return this.onTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);	
	}
	// ===========================================================
	// Interfaces & Superclass
	// ===========================================================	
	// ====== IClickableSprite ==== 	
	public void addEventsHandler(Events theEvent, IComponentEventHandler oCmpDefEventHandler){
		if(!(null == mIClicakableImpmementation))
			mIClicakableImpmementation.addEventsHandler(theEvent, oCmpDefEventHandler);
	}
	/*public void setActionOnSceneListener(IActionOnSceneListener actionLeastner) {
		mIClicakableImpmementation.setActionOnSceneListener(actionLeastner);
	}*/
	public IActionOnSceneListener getActionOnSceneListener(){
			return mIClicakableImpmementation.getActionOnSceneListener();
	}
	public int getID() {
		if(!(null == mIClicakableImpmementation))
			return mIClicakableImpmementation.getID();
		return 0;
	}
	public void setID(int ID) {
		if(!(null == mIClicakableImpmementation))
		 mIClicakableImpmementation.setID(ID);
	}
	public boolean onTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if(!(null == mIClicakableImpmementation))
			return mIClicakableImpmementation.onTouched(pSceneTouchEvent,pTouchAreaLocalX,pTouchAreaLocalY);
		return false;
	}
	@Override
	public void onFireEventAction(Events event, ActionType type) {
		if(!(null == mIClicakableImpmementation))
			mIClicakableImpmementation.onFireEventAction(event, type);
	}
	// ===========================================================		
	// ====== IActionOnSceneListener ==== 	
	@Override
	public void onStick(IAreaShape currentShapeToStick,SceneActions stickActionDescription) {
		//FT mIClicakableImpmementation.getActionOnSceneListener().onStick(currentShapeToStick, stickActionDescription);
		mIActionOnSceneListener.onStick(currentShapeToStick, stickActionDescription);
	}
	@Override
	public void onFlipCard(int CardID, CardSide currentSide) {
		mIActionOnSceneListener.onFlipCard(CardID, currentSide);
	}	
	@Override
	public void lockTouch() {
		//FT mIClicakableImpmementation.getActionOnSceneListener().lockTouch();
		mIActionOnSceneListener.lockTouch();
	}
	@Override
	public void unLockTouch() {
		//FT  mIClicakableImpmementation.getActionOnSceneListener().unLockTouch();
		mIActionOnSceneListener.unLockTouch();
	}
	@Override
	public void setIActionOnSceneListener(IActionOnSceneListener pListener){
		mIActionOnSceneListener = pListener;
	}
	// =================================================================================	
	// ====== IActivityOnSceneListener ==== 
	@Override
	public boolean onChangeScene(String nextSceneName) {
		return mIActivitySceneListener.onChangeScene(nextSceneName);
	}
	@Override
	public void unZoom() {
		mIActivitySceneListener.unZoom();
	}
	@Override
	public void setIActivitySceneListener(IActivitySceneListener pListener){
		mIActivitySceneListener = pListener;
	}

	@Override
	public void build(BasicDescriptor pDsc) {
		// TODO Auto-generated method stub
		
	}
}