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
import com.welmo.andengine.scenes.descriptors.components.HUDDescriptor;

public class HUDisplay extends HUD{

	//-----------------------------------------------------------------------------------------------------------------------------
	// Constants
	private final static String 						TAG 	= "HUDisplay";
	private final static int 							START 			= 0;
	private final static int 							CONFIGURED 		= 1;
	private final static int 							INITIALIZED  	= 2;
	private final static int 							ICON_DIM_IN_dp		= 80;
	private final static float							ICON_INT_DIM_IN_dp	= 66;
	
	//only for test
	boolean  FLAG = false;					
	//-----------------------------------------------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------------------------

	// -----------------------------------------------------------------------------------------------------------------------------
	// Private & Protected Member Variables
	private 	List<Rectangle> 				pTouchAreas	= null;
	protected	HUDDescriptor					pDescriptor; 
	protected 	boolean							configured 	= false;
	protected 	Engine							mEngine 	= null;
	protected 	int								mStatus		= START;
	Display										mDisplay	= null;
	List<Sprite>								buttons		= null;
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// Constructor
	public HUDisplay(Engine theEngine){
		super();
		pTouchAreas = new ArrayList<Rectangle>();
		mEngine		= theEngine;
		buttons = new ArrayList<Sprite>();
	}
	// -----------------------------------------------------------------------------------------------------------------------------
	// Private function
	public void init(){
		//FT Temporary if(!configured)
		//FT Temporary 	throw new NullPointerException("HUD Not Configurrd");
		if(mStatus>CONFIGURED)
			return;
		
		
		ColorPiker theColorPiker = new ColorPiker(this.mEngine.getVertexBufferObjectManager(),this);
		
		this.attachChild(theColorPiker);
		theColorPiker.setRotationCenter(0, 0);
		theColorPiker.setRotation(-90);
		theColorPiker.setPosition(0,800);
		this.registerTouchArea(theColorPiker);
		
		/*
		ResourcesManager pRM = ResourcesManager.getInstance();
		
		DisplayMetrics dm = new DisplayMetrics();
		mDisplay.getMetrics(dm);
		float scale = 800.0f/dm.heightPixels;
		
		//int buttonExtDim = (int) (dm.ydpi/160 * ICON_DIM_IN_dp);
		//int buttonIntDim = (int) (dm.ydpi/160 * ICON_INT_DIM_IN_dp);
		
		//int buttonExtDim = (int)ICON_DIM_IN_dp;
		//int buttonIntDim = (int)ICON_INT_DIM_IN_dp;
		
		// calculate width of screen in mm
		float nHeighInMM = (dm.heightPixels/dm.ydpi)*25.4f;
		//int nbOfButton = (int)(nHeighInMM/8);
		int nbOfButton = 8;
		int buttonExtDim = (int)(800/nbOfButton);
		int buttonIntDim = (int)(0.90f * (float)buttonExtDim);

	
		//FT Test
		final Rectangle left = new Rectangle(0, 0, 800, buttonExtDim , this.mEngine.getVertexBufferObjectManager());

		//int nbOfButton = (int)800/buttonExtDim;
		int space = (800 - (buttonIntDim * nbOfButton)) / (nbOfButton + 1);

		for (int index = 0; index <nbOfButton; index++){
			Sprite button = new Sprite(0, 0, buttonIntDim, buttonIntDim, pRM.getTextureRegion("EmpyImageRegion"),this.mEngine.getVertexBufferObjectManager())
			{
				public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
				{
					if (touchEvent.isActionUp())
					{
						if(FLAG){
							this.setColor(new Color(0,1,0));
							left.setColor(new Color(0,1,0));
						}
						else{
							this.setColor(new Color(1,0,0));
							left.setColor(new Color(1,0,0));
						}
						FLAG = !FLAG;

					}
					return true;
				};
			};
			buttons.add(button);
			left.attachChild(button);
			this.registerTouchArea(button);
			button.setPosition(space/2+ index*(buttonExtDim), (buttonExtDim - buttonIntDim)/2);
			left.setRotationCenter(0, 0);
			left.setRotation(-90);
			left.setPosition(0,800);
			this.registerTouchArea(button);
		}
		*/
     
       /* this.registerTouchArea(left);
        this.registerTouchArea(right);
        this.attachChild(left);
        this.attachChild(right);*/
        
        mStatus = INITIALIZED;
	}
	// -----------------------------------------------------------------------------------------------------------------------------
	// Pubblic function
	public void config(HUDDescriptor pDescriptor){
		if(!(null == pDescriptor))
			throw new NullPointerException("HUD Configuration: passed descriptor is null");
		pDescriptor = pDescriptor;
		configured = true;
	}
	public void setDisplayMetrics(Display pDisplay){
		mDisplay =pDisplay;
	}
}
