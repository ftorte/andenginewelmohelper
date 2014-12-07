package com.welmo.andengine.scenes.components.buttons;

import java.util.EnumMap;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.primitive.vbo.IRectangleVertexBufferObject;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.managers.SharedPreferenceManager;
import com.welmo.andengine.scenes.components.CardSprite.CardSide;
import com.welmo.andengine.scenes.components.interfaces.IComponentClickableDfltImp;
import com.welmo.andengine.scenes.components.interfaces.IActionSceneListener;
import com.welmo.andengine.scenes.components.interfaces.IActivitySceneListener;
import com.welmo.andengine.scenes.components.interfaces.IComponentClickable;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.components.interfaces.IComponentEventHandler;
import com.welmo.andengine.scenes.components.interfaces.IPersistent;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ButtonSceneLauncherDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ButtonSceneLauncherDescriptor.ImgData;
import com.welmo.andengine.scenes.descriptors.components.ButtonSceneLauncherDescriptor.ImgType;
import com.welmo.andengine.scenes.descriptors.components.ButtonSceneLauncherDescriptor.Status;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;
import com.welmo.andengine.scenes.descriptors.events.SceneActions.ActionType;
import com.welmo.andengine.scenes.operations.IOperationHandler;

public class ButtonSceneLauncher extends Rectangle implements IComponentClickable, IActivitySceneListener, IActionSceneListener, IPersistent{

	// ========================================================================
	// Fields implementation of IClickable, IActivitySceneListener, IActionOnSceneListene
	// ========================================================================
	IComponentClickable				mIClicakableImpmementation 		= null;	
	IActionSceneListener			mIActionOnSceneListener 		= null;
	IActivitySceneListener			mIActivitySceneListener 		= null;
	// ========================================================================

	ButtonSceneLauncherDescriptor.Status 	theStatus				= Status.NotActive;
	ButtonSceneLauncherDescriptor.Status 	theDefaultStatus		= Status.NotActive;
	SharedPreferenceManager					pSPM					= null;
	
	
	protected VertexBufferObjectManager		pVBO 					= null;
	protected Sprite 						spBG_Inactive			= null;
	protected Sprite 						spBG_final				= null;
	protected Sprite						spIco_locked 			= null;
	protected Sprite						spIco_free 				= null;
	protected Sprite						spIco_star_1 			= null;
	protected Sprite						spIco_star_2 			= null;
	protected Sprite						spIco_star_3 			= null;
	protected Sprite						spIco_star_inactive_1 	= null;
	protected Sprite						spIco_star_inactive_2 	= null;
	protected Sprite						spIco_star_inactive_3 	= null;
	protected String						sFatherName				= "";
	
	
	
		
	public ButtonSceneLauncher(ButtonSceneLauncherDescriptor pDsc,
			VertexBufferObjectManager pRectangleVertexBufferObject) {
		super(pDsc.getIPosition().getX(), pDsc.getIPosition().getY(), 
				pDsc.getIDimension().getWidth(), pDsc.getIDimension().getHeight(), 
				pRectangleVertexBufferObject);
		this.setAlpha(0);
		
		//create default interface implementations
		mIClicakableImpmementation 	=   new IComponentClickableDfltImp();
		mIActionOnSceneListener		= 	null;
		mIActivitySceneListener 	=	null;
		mIClicakableImpmementation.setParent(this);
		
		//setup vertex bufferhandler
		pVBO 						= pRectangleVertexBufferObject;
		
		
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
	public int getID() {
		if(!(null == mIClicakableImpmementation))
			return mIClicakableImpmementation.getID();
		return 0;
	}
	public void setID(int ID) {
		if(!(null == mIClicakableImpmementation))
		 mIClicakableImpmementation.setID(ID);
	}
	// ===========================================================
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
	public boolean onFatherScene(){
		return mIActivitySceneListener.onFatherScene();
	}
	// =================================================================================	



	@Override
	public void configure(BasicDescriptor pDsc) {
		if(! (pDsc instanceof ButtonSceneLauncherDescriptor))
			throw new IllegalArgumentException("In ButtonSceneLauncher build: the descriptor is not a ButtonSceneLauncherDescriptor");

		//cast to ButtonSceneLauncherDescriptor
		ButtonSceneLauncherDescriptor theDescriptor = (ButtonSceneLauncherDescriptor)pDsc;
		
		//get list of images
		EnumMap<ImgType,ImgData> imagesList = theDescriptor.getImagesList();
		
		//obtine access to the Resource manger
		ResourcesManager pRM = ResourcesManager.getInstance();
		ImgData theImage = null;
		
		this.mIClicakableImpmementation.setID(pDsc.getID()); 
		
		
		sFatherName = new String(theDescriptor.getNextScene());
		
		//create background
		if((theImage=imagesList.get(ImgType.bg_inactive))!= null)
			spBG_Inactive = createSprite(theImage,pRM);
		//create bg_final
		if((theImage=imagesList.get(ImgType.bg_fina))!= null)
			spBG_final = createSprite(theImage,pRM);
		//create ico_locked
		if((theImage=imagesList.get(ImgType.ico_locked))!= null)
			spIco_locked = createSprite(theImage,pRM);
		//read ico_free
		if((theImage=imagesList.get(ImgType.ico_free))!= null)
			spIco_free = createSprite(theImage,pRM);
		//read ico_star
		if((theImage=imagesList.get(ImgType.ico_star_1))!= null)
			spIco_star_1 = createSprite(theImage,pRM);
		//read ico_star
		if((theImage=imagesList.get(ImgType.ico_star_2))!= null)
			spIco_star_2 = createSprite(theImage,pRM);
		//read ico_star
		if((theImage=imagesList.get(ImgType.ico_star_3))!= null)
			spIco_star_3 = createSprite(theImage,pRM);
		//read ico_star_inactive
		if((theImage=imagesList.get(ImgType.ico_star_inactive_1))!= null)
			spIco_star_inactive_1 = createSprite(theImage,pRM);	
		if((theImage=imagesList.get(ImgType.ico_star_inactive_2))!= null)
			spIco_star_inactive_2 = createSprite(theImage,pRM);	
		if((theImage=imagesList.get(ImgType.ico_star_inactive_3))!= null)
			spIco_star_inactive_3 = createSprite(theImage,pRM);	
		
		//get default status from descriptor and init the button
		theDefaultStatus = theDescriptor.getDefaultStatus();
		setStatus(theDescriptor.getDefaultStatus());
	}
	// ===========================================================
	// Private Functions
	// ===========================================================	
	private Sprite createSprite(ImgData theData, ResourcesManager pRM){
		ITextureRegion texture = pRM.getTextureRegion(theData.strResourceName);
		Sprite theSprite = new Sprite(theData.px,theData.py,theData.width, theData.height,texture,this.pVBO);
		theSprite.setZIndex(50);
		this.attachChild(theSprite);
		return theSprite;
	}
	private void setStatus(ButtonSceneLauncherDescriptor.Status theStatus){
		this.theStatus = theStatus;
		desactiveAllSprites();
		//public enum Status {NotActive, Locked, level0, level1, level2, level3}
		switch(this.theStatus){
			case NotActive:
				spBG_Inactive.setVisible(true);
				break;
			case Locked:
				spIco_locked.setVisible(true);
				break;
			case level0:
				spBG_final.setVisible(true);
				spIco_free.setVisible(true);
				spIco_star_inactive_1.setVisible(true);
				spIco_star_inactive_2.setVisible(true);
				spIco_star_inactive_3.setVisible(true);
				break;
			case level1:
				spBG_final.setVisible(true);
				spIco_free.setVisible(true);
				spIco_star_1.setVisible(true);
				spIco_star_inactive_2.setVisible(true);
				spIco_star_inactive_3.setVisible(true);
				break;
			case level2:
				spBG_final.setVisible(true);
				spIco_free.setVisible(true);
				spIco_star_1.setVisible(true);
				spIco_star_2.setVisible(true);
				spIco_star_inactive_3.setVisible(true);
				break;
			case level3:
				spBG_final.setVisible(true);
				spIco_free.setVisible(true);
				spIco_star_1.setVisible(true);
				spIco_star_2.setVisible(true);
				spIco_star_3.setVisible(true);
				break;
		}
	}
	private void desactiveAllSprites(){
		spBG_Inactive.setVisible(false);
		spBG_final.setVisible(false);
		spIco_locked.setVisible(false);
		spIco_free.setVisible(false);
		spIco_star_1.setVisible(false);
		spIco_star_2.setVisible(false);
		spIco_star_3.setVisible(false);
		spIco_star_inactive_1.setVisible(false);
		spIco_star_inactive_2.setVisible(false);
		spIco_star_inactive_3.setVisible(false);
	}
	
	@Override
	public IComponentEventHandler getEventsHandler(Events theEvent) {
		// TODO Auto-generated method stub
		return null;
	}
	// *************************************************************************************
	// Implement interface IPersistnt
	// *************************************************************************************
	@Override
	public void doLoad(SharedPreferences sp, String url_root) {
	}
	@Override
	public void doSave(SharedPreferences sp, String url_root) {
	}
	@Override
	public void setSharedPreferenceManager(SharedPreferenceManager sp) {
		pSPM = sp;// TODO Auto-generated method stub

	}
	@Override
	public void doLoad() {
		if(pSPM == null)
			throw new NullPointerException("In doSave the Shared Preferences Manager is null");
		
		SharedPreferences	sp = pSPM.getSharedPreferences(sFatherName);
		
		String strLaunchStatus = sp.getString("LaunchStatus", theDefaultStatus.name());
		
		this.setStatus(ButtonSceneLauncherDescriptor.Status.valueOf(strLaunchStatus));
		
	}

	@Override
	public void doSave() {
	}
	@Override
	public void doLoad(SharedPreferenceManager sp) {
		pSPM = sp;
		doLoad();
	}

	@Override
	public void doSave(SharedPreferenceManager sp) {
		pSPM = sp;
		doSave();
	}
	// *************************************************************************************

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
}
