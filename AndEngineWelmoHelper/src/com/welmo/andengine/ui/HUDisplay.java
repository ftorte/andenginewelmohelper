package com.welmo.andengine.ui;

import java.util.Iterator;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import android.util.Log;
import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.ISceneMessageHandler;
import com.welmo.andengine.scenes.components.buttons.ToolsBar;
import com.welmo.andengine.scenes.descriptors.components.HUDDescriptor;
import com.welmo.andengine.scenes.descriptors.components.Positioning;
import com.welmo.andengine.scenes.descriptors.components.ToolsBarDescriptor;

/*
 * 
 */

public class HUDisplay extends HUD implements ISceneMessageHandler, IScrollDetectorListener,IPinchZoomDetectorListener{

	//-----------------------------------------------------------------------------------------------------------------------------
	// Constants
	private final static String 						TAG 				= "HUDisplay";
	//Manage configuration status
	private final static int 							START 				= 0;
	private final static int 							CONFIGURED 			= 1;
	private final static int 							INITIALIZED  		= 2;
	private final static float							MAX_ZOOM			= 4.0f;
	private final static float							MIN_ZOOM			= 1.0f;
	//icons IDS
	private final static int 							ICON_NONE  			= -1;
	private final static int 							ICON_SCROLL  		= 0;
	private final static int 							ICON_ZOOM_IN 		= 1;
	private final static int 							ICON_ZOOM_OUT  		= 2;
	
	//only for test
	boolean 	 										FLAG 				= false;	
	// -----------------------------------------------------------------------------------------------------------------------------
	// Private & Protected Member Variables
	protected	HUDDescriptor					mDescriptor			= null;
	protected 	Engine							mEngine 			= null;
	protected	int								mWidth				= 1280;
	protected 	float							mMaxZoom			= MAX_ZOOM;
	protected 	float							mMinZoom			= MIN_ZOOM;
	protected 	float							mCurrentZoom		= 1;
	
	protected 	Sprite[]						spArrayIcons		= null;
	
	protected	int								mHeight				= 800;
	protected 	int								mStatus				= START;
	protected 	ISceneMessageHandler			mMsgHandler			= null;
	protected 	float 							mPinchZoomStartedCameraZoomFactor = 0;
	protected 	ResourcesManager				mResMgr				= null;
	//Camera scroll
	protected 	boolean							bScrollDetectorON	= false;
	//Camera pinch zoom
	protected 	boolean							bPinchZoomON		= false;
	protected 	boolean 						bSmoothCamera		= false;
	// -----------------------------------------------------------------------------------------------------------------------------
	// Constructor
	public HUDisplay(Engine theEngine, int width, int height){
		super();
		mEngine				= theEngine;
		mWidth				= width;
		mHeight				= height;
		spArrayIcons		= new Sprite[3];
		mStatus = START;		
	}
	// -----------------------------------------------------------------------------------------------------------------------------
	// Private function
	private void init(ResourcesManager pResManger){
		
		//if not configured generate an exception
		if(this.mStatus < CONFIGURED)
			throw new NullPointerException("HUD: not configures");

		//check if has a special button bar called color piker & create it
		if(this.mDescriptor.hasColorPicker()){
			//Create ColorPikerToolBar
			ColorPiker theColorPiker = new ColorPiker(this.mEngine.getVertexBufferObjectManager(),this,this);
			this.attachChild(theColorPiker);
			theColorPiker.setRotationCenter(0, 0);
			theColorPiker.setRotation(-90);
			theColorPiker.setPosition(0,800);
			this.registerTouchArea(theColorPiker);
		}
		
		//create the tools bars
		Iterator<ToolsBarDescriptor> toolBarDscIter = this.mDescriptor.getToolBarDescriptors().iterator(); 
		while(toolBarDscIter.hasNext()){
			ToolsBarDescriptor toolBarDsc = toolBarDscIter.next();
			
			//Create button toolBar
			ToolsBar theToolsBar = new ToolsBar(this.mEngine.getVertexBufferObjectManager(),this, toolBarDsc);
			
			//set position of the toolBar
			Positioning position = toolBarDsc.getPosition();
			theToolsBar.setRotationCenter(0, 0);
			switch (position){
				case TOP:
					theToolsBar.setRotation(0);
					theToolsBar.setPosition(0,0);
					break;
				case BOTTOM:	
					theToolsBar.setRotation(0);
					theToolsBar.setPosition(0,mHeight-theToolsBar.getHeight());
					break;
				case LEFT:
					theToolsBar.setRotation(-90);
					theToolsBar.setPosition(0,mHeight);
					break;
				case RIGHT:	
					theToolsBar.setRotation(-90);
					theToolsBar.setPosition(this.mWidth-theToolsBar.getHeight(),mHeight);
					break;
			}
			//Register toolsbar for touch and attach to the HUD
			this.registerTouchArea(theToolsBar);
			this.attachChild(theToolsBar);		
		}
		
		//check if has scroll detector
		//check if has a special button bar called color piker & create it
		if(this.mDescriptor.hasScrollDetector()){
			bScrollDetectorON = true;
			spArrayIcons[ICON_SCROLL] = new Sprite (0,0,256,256,pResManger.getTextureRegion("ico_scroll"), mEngine.getVertexBufferObjectManager());
			spArrayIcons[ICON_SCROLL].setPosition((this.mWidth-spArrayIcons[ICON_SCROLL].getWidth())/2,(this.mHeight-spArrayIcons[ICON_SCROLL].getHeight())/2);
			spArrayIcons[ICON_SCROLL].setVisible(false);
			this.attachChild(spArrayIcons[ICON_SCROLL]);
		}
		
		//check if has pinchzoom
		//check if has a special button bar called color piker & create it
		if(this.mDescriptor.hasPinchAndZoom()){
			bPinchZoomON = true;
			//icon zoom in
			spArrayIcons[ICON_ZOOM_IN] = new Sprite (0,0,256,256,pResManger.getTextureRegion("ico_zoom_in"), mEngine.getVertexBufferObjectManager());
			spArrayIcons[ICON_ZOOM_IN].setPosition((this.mWidth-spArrayIcons[ICON_ZOOM_IN].getWidth())/2,(this.mHeight-spArrayIcons[ICON_ZOOM_IN].getHeight())/2);
			spArrayIcons[ICON_ZOOM_IN].setVisible(false);
			this.attachChild(spArrayIcons[ICON_ZOOM_IN]);
			//icon zoom out
			spArrayIcons[ICON_ZOOM_OUT] = new Sprite (0,0,256,256,pResManger.getTextureRegion("ico_zoom_out"), mEngine.getVertexBufferObjectManager());
			spArrayIcons[ICON_ZOOM_OUT] .setPosition((this.mWidth-spArrayIcons[ICON_ZOOM_OUT].getWidth())/2,(this.mHeight-spArrayIcons[ICON_ZOOM_OUT].getHeight())/2);
			spArrayIcons[ICON_ZOOM_OUT] .setVisible(false);
			this.attachChild(spArrayIcons[ICON_ZOOM_OUT]);
		}
		
		mStatus = INITIALIZED;
	}
	// -----------------------------------------------------------------------------------------------------------------------------
	// Public function
	public void config(HUDDescriptor pDescriptor, ISceneMessageHandler msgHandler, ResourcesManager pResManger){
		if(pDescriptor == null)
			throw new NullPointerException("HUD Configuration: passed descriptor is null");
		this.mMsgHandler 	= msgHandler;
		this.mDescriptor 	= pDescriptor;
		mStatus = CONFIGURED;		
		init(pResManger);
	}
	public boolean isScrollDetectorON(){
		return bScrollDetectorON;
	}
	// -----------------------------------------------------------------------------------------------------------------------------
	// Interface ISceneMessageHandler
	@Override
	public void SendMessage(Message msg) {
		if (mMsgHandler != null)
			mMsgHandler.SendMessage(msg);
	}
	//-----------------------------------------------------------------------------------------------------------------------------------------
	// Scroll detector listener methods
	//-----------------------------------------------------------------------------------------------------------------------------------------
	@Override
	public void onScrollStarted(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
		Log.i(TAG,"onScrollStarted");
		//set camera position
		mCamera.offsetCenter(-pDistanceX, -pDistanceY);
		//reset icons if other icons are displied
		displayIcons(ICON_NONE);
	}
	@Override
	public void onScroll(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
		Log.i(TAG,"onScroll");
		mCamera.offsetCenter(-pDistanceX, -pDistanceY);
		displayIcons(ICON_SCROLL);
	}
	@Override
	public void onScrollFinished(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
		Log.i(TAG,"onScrollFinished");
		mCamera.offsetCenter(-pDistanceX, -pDistanceY);
		//reset icons
		displayIcons(ICON_NONE);
	}
	//-----------------------------------------------------------------------------------------------------------------------------------------
	// Pinch and zoom detector
	//-----------------------------------------------------------------------------------------------------------------------------------------	
	@Override
	public void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent) {
		Log.i(TAG,"onPinchZoomStarted");
		if(this.bSmoothCamera){
			this.mPinchZoomStartedCameraZoomFactor = ((SmoothCamera)this.mCamera).getZoomFactor();
			mCurrentZoom = mPinchZoomStartedCameraZoomFactor;
		}
		//reset icons if other icons are displied
		displayIcons(ICON_NONE);
	}

	@Override
	public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		//this.mSmoothCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor);
		Log.i(TAG,"onPinchZoom");
		float newZoomFactor = 0; 
		if(this.bSmoothCamera){
			if (pZoomFactor != 1)
			{
				// check bounds
				newZoomFactor = mPinchZoomStartedCameraZoomFactor * pZoomFactor;
				newZoomFactor = (newZoomFactor <= mMinZoom ? mMinZoom : (newZoomFactor >= mMaxZoom ? mMaxZoom : newZoomFactor));
				((SmoothCamera)mCamera).setZoomFactor(newZoomFactor);
			}
			if(mCurrentZoom > newZoomFactor)
				displayIcons(ICON_ZOOM_OUT);
			else
				displayIcons(ICON_ZOOM_IN);
		}

	}

	@Override
	public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		//this.mSmoothCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor);
		Log.i(TAG,"onPinchZoomFinished");
		if(this.bSmoothCamera){
			if (pZoomFactor != 1)
			{
				// check bounds
				float newZoomFactor = mPinchZoomStartedCameraZoomFactor * pZoomFactor;
				float newZoomFactorBis = (newZoomFactor <= mMinZoom ? mMinZoom : (newZoomFactor >= mMaxZoom ? mMaxZoom : newZoomFactor));
				((SmoothCamera)mCamera).setZoomFactor(newZoomFactorBis);	
			}
		}
		displayIcons(ICON_NONE);
	}
	//-----------------------------------------------------------------------------------------------------------------------------------------
	// Camera Scene Method
	//-----------------------------------------------------------------------------------------------------------------------------------------	
	@Override
	public void setCamera(Camera pCamera) {
		if(pCamera instanceof SmoothCamera)
			this.bSmoothCamera = true;
		super.setCamera(pCamera);
	}
	private void displayIcons(int icon){
		switch(icon){
			case ICON_NONE:
				spArrayIcons[ICON_ZOOM_IN].setVisible(false);
				spArrayIcons[ICON_ZOOM_OUT].setVisible(false);
				spArrayIcons[ICON_SCROLL].setVisible(false);
				break;
			case ICON_ZOOM_IN:
				spArrayIcons[ICON_ZOOM_IN].setVisible(true);
				spArrayIcons[ICON_ZOOM_OUT].setVisible(false);
				spArrayIcons[ICON_SCROLL].setVisible(false);
				break;
			case ICON_ZOOM_OUT:
				spArrayIcons[ICON_ZOOM_IN].setVisible(false);
				spArrayIcons[ICON_ZOOM_OUT].setVisible(true);
				spArrayIcons[ICON_SCROLL].setVisible(false);
				break;
			case ICON_SCROLL:
				spArrayIcons[ICON_ZOOM_IN].setVisible(false);
				spArrayIcons[ICON_ZOOM_OUT].setVisible(false);
				spArrayIcons[ICON_SCROLL].setVisible(true);
				break;
		}
	}
}
