package com.welmo.andengine.ui;
import java.util.ArrayList;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.primitive.vbo.IRectangleVertexBufferObject;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ContinuousHoldDetector;
import org.andengine.input.touch.detector.HoldDetector;
import org.andengine.input.touch.detector.HoldDetector.IHoldDetectorListener;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.welmo.andengine.scenes.ColoringScene;
import com.welmo.andengine.scenes.ISceneMessageHandler;
import com.welmo.andengine.scenes.ISceneMessageHandler.Message;
import com.welmo.andengine.scenes.ISceneMessageHandler.MessageTypes;

import android.os.Handler;
import android.util.Log;

public class ColorPiker extends Rectangle{

	// Constants
	private final static String 					TAG 					= "ColorPiker";
		
	// default values and constants
	public static int								NB_OF_PRIMARY_COLORS 	= 8;
	public static int								INVALID	 				= -1;
	public static int								NB_OF_SECONDARY_COLORS 	= 8;
	public static int								START 					= 0;
	public static int								INITIALIZED 			= 1;
	public static int								NBOFBUTTON	 			= 8;
	public static float								INT_EXT_BUTTON_FACTOR	= 0.75f;
	public static int								ACTIVE_COLORS 			= NB_OF_PRIMARY_COLORS;
	public static int								TOOLBARBACKGROUND  		= 0XA6A6A6;
	public static int								SELCTEDTOOLBACKGROUND  	= 0XAA0000;
	
	public int[]				ColorPalletIndex = {0x000000,0x191970,0x006400,0xB8860B,0xB22222,0x800000,0x8B008B,0x800080};
	
	public int[][]				ColorPallet = {
			{0x000000,0x808080,0xA9A9A9,0xB3B3B3,0xDCDCDC,0xDCDCDC,0xF8F8F8,0xFFFFFF},
			{0x191970,0x0000FF,0x4169E1,0x1E90FF,0x00BFFF,0x87CEEB,0xADD8E6,0xB0E0E6},
			{0x006400,0x6B8E23,0x008000,0x228B22,0x2E8B57,0x3CB371,0x00FF7F,0X00FA9A},
			{0xB8860B,0xFC8C00,0xFFA500,0xFFD700,0xFFFF00,0xEEE8AA,0xFAFAD2,0xF5F5D5},
			{0xB22222,0xA52A2A,0xCD5C5C,0xF08080,0xFA8072,0xFAA07A,0xFFA4B5,0xFFE4C4},
			{0x800000,0xD269E1,0xFF0000,0xFF4500,0xFF6347,0xF4A460,0xF5BED3,0xFFDAB9},
			{0x8B008B,0xD02090,0xFF1493,0xFF69B4,0xFF00FF,0xD87093,0xFFB6C1,0xFFC0CB},
			{0x800080,0x9932CC,0x8A2BE2,0xBA55D3,0xDA70D6,0xDDA0DD,0xD8BFD8,0xE6E6FA}};


	
	protected Rectangle[][]							pColorPalletSemector	= null;
	
	
	public static int								SCENEHEIGHT	 			= 800;
	public static int								BUTTOEXTDIM				= SCENEHEIGHT/NBOFBUTTON;
	public static int								BUTTOINTDIM				= (int)((float)BUTTOEXTDIM*INT_EXT_BUTTON_FACTOR);
	public static int								INBUTTONPXY				= (int)((BUTTOEXTDIM -BUTTOINTDIM)/2);
	public static int								SPACE	 				= (SCENEHEIGHT - (BUTTOEXTDIM * NBOFBUTTON)) / (NBOFBUTTON + 1);
	
	protected int 									nStatus					= START;
	protected VertexBufferObjectManager				pVBO 					= null;
	protected Scene									pTheScene				= null;
	protected ISceneMessageHandler					pSceneMessageHandler	= null;
	protected ColorPickerToolBar					pColorPicker			= null;
	//protected ColorToolBar[]						pColorPikers			= null;
	

	//-------------------------------------------------------------------------------------------------------
	// PRIVATE INNER CLASSES
	//-------------------------------------------------------------------------------------------------------
	private class ToolBarButton extends Rectangle {
		
		protected int 			nID 		= -1;
		protected Rectangle		insButton 	= null;
		protected ColorToolBar 	theToolbar 	= null;						//the tool-bar containing the button
		
		
		public ToolBarButton(float pX, float pY, float pWidth, float pHeight, int color, ColorToolBar theToolbar,
				VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
			this.theToolbar=theToolbar;
			init(color);
		}
		
		void init(int color){
			
			//set button default background
			this.setColor(Red(TOOLBARBACKGROUND),Green(TOOLBARBACKGROUND),Blue(TOOLBARBACKGROUND));
			//create inside button
			if(insButton != null){
				this.detachChild(insButton);
				insButton = null;
			}
			this.insButton = new Rectangle(0,0,BUTTOINTDIM,BUTTOINTDIM,pVBO);
			this.setButtonColor(color);
			insButton.setPosition(INBUTTONPXY,INBUTTONPXY);
			pTheScene.registerTouchArea(insButton);
			attachChild(insButton);
		}
		public void setButtonColor(int color){
			insButton.setColor(Red(color),Green(color),Blue(color));
		}
		public int getButtonColor(){
			return insButton.getColor().getARGBPackedInt();
		}
		
		@Override
		public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
		{
			//if toolbar is invisible is a just a problem of andengine
			if(!theToolbar.isVisible())
				return false;
			
			//on action up intercept the event
			if (touchEvent.isActionUp())
			{
				theToolbar.onClickButton(nID);	
				return true;
			}
			return false;
		};
	};
	
	private abstract class ColorToolBar extends Rectangle {
		protected int 								nID 			= -1;
		ToolBarButton[] 							pButtons;
		int 										nSelectedButton = INVALID;
		
		public ColorToolBar(VertexBufferObjectManager pRectangleVertexBufferObject, int[] colors, int ID) {
			super(0,0,SCENEHEIGHT,BUTTOEXTDIM, pRectangleVertexBufferObject);
			
			//setupID
			nID = ID;
			
			//create the array of buttons
			pButtons = new ToolBarButton[NBOFBUTTON];
			
			//set default background of the ColorToolBar
			this.setColor(Red(TOOLBARBACKGROUND),Green(TOOLBARBACKGROUND),Blue(TOOLBARBACKGROUND));
			
			//create the buttons line (one big rectangle for the background, one small rectangle for the color)
			for (int index =0; index < NBOFBUTTON; index ++){
				ToolBarButton theButton = new ToolBarButton(0,0,BUTTOEXTDIM,BUTTOEXTDIM,colors[index],this,pVBO);
				theButton.nID = index;
				pButtons[index] = theButton;
				this.attachChild(theButton);
				theButton.setPosition(index*(BUTTOEXTDIM),0);
				pTheScene.registerTouchArea(theButton);
			}
			
		}
		
		@Override
		public void setVisible(boolean pVisible) {
			super.setVisible(pVisible);
			for (int index =0; index < NBOFBUTTON; index ++){
				if(pVisible)
					pTheScene.registerTouchArea(pButtons[index]);
				else
					pTheScene.unregisterTouchArea(pButtons[index]);
			}
		}
		public void onClickButton(int ID){};
		public void onChangeColorButton(int ID,int color){};
	}
	
	private class ColorPickerToolBar extends ColorToolBar {
		
		ISceneMessageHandler			theMessageHandler 	= null;
		ArrayList<ColorSelectorToolBar> listSelector		= null;
		
		public ColorPickerToolBar(VertexBufferObjectManager pRectangleVertexBufferObject,int[] colors, int ID, ISceneMessageHandler msgHandler) {
			super(pRectangleVertexBufferObject, colors,ID);
			theMessageHandler = msgHandler;
			listSelector = new ArrayList<ColorSelectorToolBar>();
		}

		public void addColorSelector(ColorSelectorToolBar theSelector){
			listSelector.add(theSelector.nID, theSelector);
		}
		
		@Override
		public void onClickButton(int ID){
			//if selected the same button just check that selector id and hide the selector
			if(nSelectedButton == ID && listSelector.get(ID).isVisible()){
				listSelector.get(ID).setVisible(false);
				return;
			}
			
			//if selection has changed unselect previous button before to select it.
			if(nSelectedButton != INVALID){
				pButtons[nSelectedButton].setColor(Red(TOOLBARBACKGROUND),Green(TOOLBARBACKGROUND),Blue(TOOLBARBACKGROUND));			
				listSelector.get(nSelectedButton).setVisible(false);
			}
			//select the button
			nSelectedButton = ID;
			pButtons[nSelectedButton].setColor(Red(SELCTEDTOOLBACKGROUND),Green(SELCTEDTOOLBACKGROUND),Blue(SELCTEDTOOLBACKGROUND));
			listSelector.get(ID).setVisible(true);
			// send message to the scene
			theMessageHandler.SendMessage(new Message(MessageTypes.SET_COLOR,pButtons[nSelectedButton].getButtonColor())); 
		}
		@Override
		public void onChangeColorButton(int theID,int color){
			pButtons[theID].setButtonColor(color);
			listSelector.get(theID).setVisible(false);
			// send message to the scene
			theMessageHandler.SendMessage(new Message(MessageTypes.SET_COLOR,color)); 
		};
		
	}
	
	private class ColorSelectorToolBar extends ColorToolBar {
		
		protected ColorToolBar			pParentToolBar 	= null; // different from null if is a selector for a button
		
		public ColorSelectorToolBar(VertexBufferObjectManager pRectangleVertexBufferObject,int[] colors, int ID) {
			super(pRectangleVertexBufferObject,colors, ID);
		}
		
		@Override
		public void onClickButton(int ID){
			
			//if already selected a button unselect the button
			if(nSelectedButton != INVALID){
				pButtons[nSelectedButton].setColor(Red(TOOLBARBACKGROUND),Green(TOOLBARBACKGROUND),Blue(TOOLBARBACKGROUND));
			}
			
			//select the button
			nSelectedButton = ID;
			pButtons[nSelectedButton].setColor(Red(SELCTEDTOOLBACKGROUND),Green(SELCTEDTOOLBACKGROUND),Blue(SELCTEDTOOLBACKGROUND));			
			
			//update color to father button
			if (pParentToolBar != null) {
				this.pParentToolBar.onChangeColorButton(this.nID,pButtons[nSelectedButton].getButtonColor());
			}
		}

		
		public void setParentColorToolBar(ColorToolBar toolBar) {
			pParentToolBar = toolBar;
		}		
	}
	//-------------------------------------------------------------------------------------------------------
	// END PRIVATE INNER CLASSES
	//-------------------------------------------------------------------------------------------------------
	public ColorPiker(VertexBufferObjectManager pRectangleVertexBufferObject,Scene theScene, ISceneMessageHandler theMessageHandler){
		super(0,0,SCENEHEIGHT,BUTTOEXTDIM, pRectangleVertexBufferObject);
		pTheScene 				= theScene;
		pSceneMessageHandler 	= theMessageHandler;
		pVBO = pRectangleVertexBufferObject;
		init();
	}
	

	public void init(){
		nStatus			= START;
		//create tool-bar
		pTheScene.registerTouchArea(this);
		pTheScene.setColor(Red(TOOLBARBACKGROUND),Green(TOOLBARBACKGROUND),Blue(TOOLBARBACKGROUND));

		pColorPicker = new ColorPickerToolBar(pVBO,ColorPalletIndex,INVALID,pSceneMessageHandler);
		this.attachChild(pColorPicker);
				
		for (int index=0; index <  NB_OF_PRIMARY_COLORS; index ++){
			//create new color tool bar
			ColorSelectorToolBar tmpColorSelectorToolBar = new ColorSelectorToolBar(pVBO,ColorPallet[index],index);
			tmpColorSelectorToolBar.setY(BUTTOEXTDIM);
			tmpColorSelectorToolBar.setVisible(false);
			tmpColorSelectorToolBar.setParentColorToolBar(pColorPicker);
			pColorPicker.addColorSelector(tmpColorSelectorToolBar);
			this.attachChild(tmpColorSelectorToolBar);
		}
	}
	private float Red(int color){
		int red = ((color >> 16) & 0x0000FF);
		return (float)red/256;
	}
	private float Green(int color){
		int green = ((color >> 8) & 0x0000FF);
		return (float)green/256;
	}
	private float Blue(int color){
		int blue = (color & 0x0000FF);
		return (float)blue/256;
	}
}
