package com.welmo.andengine.scenes.components.buttons;

import java.util.EnumMap;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.primitive.vbo.IRectangleVertexBufferObject;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.components.CardSprite.CardSide;
import com.welmo.andengine.scenes.components.DefaultIClickableImplementation;
import com.welmo.andengine.scenes.components.IActionOnSceneListener;
import com.welmo.andengine.scenes.components.IActivitySceneListener;
import com.welmo.andengine.scenes.components.IBasicComponent;
import com.welmo.andengine.scenes.components.IClickable;
import com.welmo.andengine.scenes.components.IComponentEventHandler;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ButtonSceneLauncherDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ButtonSceneLauncherDescriptor.ImgData;
import com.welmo.andengine.scenes.descriptors.components.ButtonSceneLauncherDescriptor.ImgType;
import com.welmo.andengine.scenes.descriptors.components.ButtonSceneLauncherDescriptor.Status;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;
import com.welmo.andengine.scenes.descriptors.events.SceneActions.ActionType;

public class ButtonSceneLauncher extends Rectangle implements IClickable, IActivitySceneListener, IActionOnSceneListener{

	// ========================================================================
	// Fields implementation of IClickable, IActivitySceneListener, IActionOnSceneListene
	// ========================================================================
	IClickable						mIClicakableImpmementation 		= null;	
	IActionOnSceneListener			mIActionOnSceneListener 		= null;
	IActivitySceneListener			mIActivitySceneListener 		= null;
	// ========================================================================

	ButtonSceneLauncherDescriptor.Status 	theStatus				= Status.NotActive;
	
	protected VertexBufferObjectManager		pVBO 					= null;
	Sprite 									spBG_Inactive			= null;
	Sprite 									spBG_final				= null;
	Sprite									spIco_locked 			= null;
	Sprite									spIco_free 				= null;
	Sprite									spIco_star_1 			= null;
	Sprite									spIco_star_2 			= null;
	Sprite									spIco_star_3 			= null;
	Sprite									spIco_star_inactive_1 	= null;
	Sprite									spIco_star_inactive_2 	= null;
	Sprite									spIco_star_inactive_3 	= null;
	
		
	public ButtonSceneLauncher(ButtonSceneLauncherDescriptor pDsc,
			VertexBufferObjectManager pRectangleVertexBufferObject) {
		super(pDsc.getIPosition().getX(), pDsc.getIPosition().getY(), 
				pDsc.getIDimension().getWidth(), pDsc.getIDimension().getHeight(), 
				pRectangleVertexBufferObject);
		this.setAlpha(0);
		
		//create default interface implementations
		mIClicakableImpmementation 	=   new DefaultIClickableImplementation();
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
	public IActionOnSceneListener getActionOnSceneListener(){
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
	public void setIActionOnSceneListener(IActionOnSceneListener pListener){
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
	// =================================================================================	



	@Override
	public void build(BasicDescriptor pDsc) {
		if(! (pDsc instanceof ButtonSceneLauncherDescriptor))
			throw new IllegalArgumentException("In ButtonSceneLauncher build: the descriptor is not a ButtonSceneLauncherDescriptor");

		//cast to ButtonSceneLauncherDescriptor
		ButtonSceneLauncherDescriptor theDescriptor = (ButtonSceneLauncherDescriptor)pDsc;
		
		//get list of images
		EnumMap<ImgType,ImgData> imagesList = theDescriptor.getImagesList();
		
		//obtine access to the Resource manger
		ResourcesManager pRM = ResourcesManager.getInstance();
		ImgData theImage = null;
		
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
}
