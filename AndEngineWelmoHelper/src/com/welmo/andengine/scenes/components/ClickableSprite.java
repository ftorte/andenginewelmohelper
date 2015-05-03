package com.welmo.andengine.scenes.components;

import java.util.ArrayList;

import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import android.util.Log;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.components.CardSprite.CardSide;
import com.welmo.andengine.scenes.components.interfaces.IComponentClickableDfltImp;
import com.welmo.andengine.scenes.components.interfaces.IActionSceneListener;
import com.welmo.andengine.scenes.components.interfaces.IActivitySceneListener;
import com.welmo.andengine.scenes.components.interfaces.IComponentClickable;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.components.interfaces.IComponentEventHandler;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;
import com.welmo.andengine.scenes.descriptors.events.SceneActions.ActionType;
import com.welmo.andengine.scenes.operations.IOperationHandler;

public class ClickableSprite extends Sprite implements IComponentClickable, IActivitySceneListener, IActionSceneListener,IComponent{
	// ========================================================================
	// Constants
	// ========================================================================
	//Log & Debug
	private static final String 		TAG = "ClickableSprite";
	// ========================================================================
	// Fields implementation of IClickable, IActivitySceneListener, IActionOnSceneListene
	// ========================================================================
	//DefaultIClickableImplementation 	mIClicakableImpmementation = null;
	IComponentClickable						mIClicakableImpmementation 	= null;	
	IActionSceneListener			mIActionOnSceneListener 	= null;
	IActivitySceneListener			mIActivitySceneListener 	= null;
	// ========================================================================
	// Constructors
	// ========================================================================
	public ClickableSprite(SpriteObjectDescriptor pSPRDscf, ResourcesManager pRM, Engine theEngine){
		super(pSPRDscf.getIPosition().getX(), pSPRDscf.getIPosition().getY(), 
				pSPRDscf.getIDimension().getWidth(), pSPRDscf.getIDimension().getHeight(), 
				pRM.getTextureRegion(pSPRDscf.getTextureName()), 
				theEngine.getVertexBufferObjectManager());
		
		mIClicakableImpmementation 	=   new IComponentClickableDfltImp();
		mIActionOnSceneListener		= 	null;
		mIActivitySceneListener 	=	null;
		mIClicakableImpmementation.setTheComponentParent(this);
		
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
	public IActionSceneListener getActionOnSceneListener(){
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
	@Override
	public IEntity getTheComponentParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setTheComponentParent(IEntity parent) {
		// TODO Auto-generated method stub
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
	@Override
	public ArrayList<IComponentEventHandler>  getEventsHandler(Events theEvent) {
		return mIClicakableImpmementation.getEventsHandler(theEvent);
	}
	@Override
	public String getPersistenceURL() {
		return mIClicakableImpmementation.getPersistenceURL();
	}
	@Override
	public boolean onFireEvent(Events event) {
		return mIClicakableImpmementation.onFireEvent(event);
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
	public void setIActionOnSceneListener(IActionSceneListener pListener){
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
	public void configure(BasicDescriptor pDsc) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onFatherScene(){
		return mIActivitySceneListener.onFatherScene();
	}

	@Override
	public boolean onChangeChildScene(String nextScene) {
		return mIActivitySceneListener.onChangeChildScene(nextScene);
	}
	@Override
	public void onReloadScene() {
		mIActivitySceneListener.onReloadScene();
	}
	@Override
	public void onGoToMenu() {
		mIActivitySceneListener.onGoToMenu();
	}
	@Override
	public void onGoToNextLevel() {
		mIActivitySceneListener.onGoToNextLevel();
	}
	
	@Override
	public void setActionSceneListner(IActionSceneListener scenelistener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResult(int result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOperationsHandler(IOperationHandler messageHandler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCloseChildScene() {
		mIActivitySceneListener.onCloseChildScene();// TODO Auto-generated method stub	
	}

	@Override
	public void onResult(int i, int j, String string) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onLaunchChildScene(String nextScene,
			ArrayList<String> parameters) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkLicence(String sLicence) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onInAppPurchasing(String sProductID) {
		mIActivitySceneListener.onInAppPurchasing(sProductID);
	}
}