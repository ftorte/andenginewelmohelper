package com.welmo.andengine.ui;

import java.util.ArrayList;
import java.util.List;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

import android.util.DisplayMetrics;
import android.view.Display;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.ColoringScene;
import com.welmo.andengine.scenes.descriptors.components.HUDDescriptor;

public class HUDisplay extends HUD{

	//-----------------------------------------------------------------------------------------------------------------------------
	// Constants
	private final static String 						TAG 				= "HUDisplay";
	//Manage configuration status
	private final static int 							START 				= 0;
	private final static int 							CONFIGURED 			= 1;
	private final static int 							INITIALIZED  		= 2;
	
	//only for test
	boolean 	 										FLAG 				= false;					
	ColoringScene 										psc					= null;
	
	//-----------------------------------------------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------------------------

	// -----------------------------------------------------------------------------------------------------------------------------
	// Private & Protected Member Variables
	private 	List<Rectangle> 				mArrayTouchAreas	= null;
	protected	HUDDescriptor					mDescriptor			= null;; 
	protected 	boolean							mbConfigured 		= false;
	protected 	Engine							mEngine 			= null;
	protected 	int								mStatus				= START;
	//Display									mDisplay			= null;
	List<Sprite>								mButtonsList		= null;
	
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// Constructor
	public HUDisplay(Engine theEngine){
		super();
		mArrayTouchAreas 	= new ArrayList<Rectangle>();
		mEngine				= theEngine;
		mButtonsList 		= new ArrayList<Sprite>();
	}
	// -----------------------------------------------------------------------------------------------------------------------------
	// Private function
	public void init(){
		//FT Temporary if(!configured)
		//FT Temporary 	throw new NullPointerException("HUD Not Configurrd");
		if(mStatus == INITIALIZED)
			return;
		
		ColorPiker theColorPiker = new ColorPiker(this.mEngine.getVertexBufferObjectManager(),this);
		
		this.attachChild(theColorPiker);
		theColorPiker.setRotationCenter(0, 0);
		theColorPiker.setRotation(-90);
		theColorPiker.setPosition(0,800);
		this.registerTouchArea(theColorPiker);
		
		ToolsBar theToolsBar = new ToolsBar(this.mEngine.getVertexBufferObjectManager(),this);
		
		this.attachChild(theToolsBar);
		theToolsBar.setRotationCenter(0, 0);
		theToolsBar.setRotation(-90);
		theToolsBar.setPosition(1280 - theToolsBar.getHeight(),800);
		this.registerTouchArea(theColorPiker);
		
        mStatus = INITIALIZED;
	}
	// -----------------------------------------------------------------------------------------------------------------------------
	// Public function
	public void config(HUDDescriptor pDescriptor){
		if(!(null == pDescriptor))
			throw new NullPointerException("HUD Configuration: passed descriptor is null");
		
		this.mDescriptor 	= pDescriptor;
		this.mbConfigured 	= true;
		mStatus = CONFIGURED;
	}
	
	public void HandleMessage(int msg, int param){
		psc.setColorFill(param);
	}
	public void setSceneMessageHandler(ColoringScene psc){
		this.psc = psc;
	}
}
