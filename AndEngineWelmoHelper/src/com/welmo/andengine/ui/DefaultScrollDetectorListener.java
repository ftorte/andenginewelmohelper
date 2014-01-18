package com.welmo.andengine.ui;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;

import com.welmo.andengine.managers.ResourcesManager;

import android.util.Log;

public class DefaultScrollDetectorListener implements IScrollDetectorListener {

	protected Camera 							mCamera		= null;
	protected Engine							mEngine		= null;
	protected Sprite							mScrollIcon	= null;
	protected ResourcesManager					mResMgr		= null;
	//-----------------------------------------------------------------------------------------------------------------------------------------
	// Constants
	final static String TAG="DefaultScrollDetectorListener";
	DefaultScrollDetectorListener(final Camera thecamera, Engine theEngine, ResourcesManager theResMgr){
		mCamera = thecamera;
		mEngine = theEngine;
		mResMgr	= theResMgr;
		mScrollIcon = new Sprite (0,0,512,512,theResMgr.getTextureRegion("ico_zoom_in"), mEngine.getVertexBufferObjectManager());
		mScrollIcon.setVisible(false);
	}
	//-----------------------------------------------------------------------------------------------------------------------------------------
	// Scroll detector listener methods
	//-----------------------------------------------------------------------------------------------------------------------------------------
	@Override
	public void onScrollStarted(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
		Log.i(TAG,"onScrollStarted");
		//set camera position
		mCamera.offsetCenter(-pDistanceX, -pDistanceY);
		//attach the icon to the scene and show it in the camera center
		if(mScrollIcon.hasParent())
			mScrollIcon.detachSelf();
		
		(mEngine.getScene()).attachChild(mScrollIcon);
		mScrollIcon.setPosition(mCamera.getCenterX()-mScrollIcon.getWidth(),mCamera.getCenterY()-mScrollIcon.getHeight());
		mScrollIcon.setVisible(true);
	}

	@Override
	public void onScroll(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
		Log.i(TAG,"onScroll");
		mCamera.offsetCenter(-pDistanceX, -pDistanceY);
	}

	@Override
	public void onScrollFinished(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
		Log.i(TAG,"onScrollFinished");
		mCamera.offsetCenter(-pDistanceX, -pDistanceY);
		mScrollIcon.setVisible(false);
		(mEngine.getScene()).detachChild(mScrollIcon);
	}
}
