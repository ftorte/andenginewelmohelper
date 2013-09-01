package com.welmo.andengine.ui;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.primitive.vbo.IRectangleVertexBufferObject;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

public class ColorPiker extends Rectangle {

	public static int								NB_OF_PRIMARY_COLORS 	= 8;
	public static int								NB_OF_SECONDARY_COLORS 	= 8;
	public static int								ACTIVE_COLORS 			= NB_OF_PRIMARY_COLORS;
	protected Rectangle[][]							pColorPalletSemector	= null;
	
	
	public static int								START 					= 0;
	public static int								INITIALIZED 			= 1;
	public static int								NBOFBUTTON	 			= 8;
	public static int								SCENEHEIGHT	 			= 800;
	public static int								BUTTOEXTDIM				= SCENEHEIGHT/NBOFBUTTON;
	public static int								BUTTOINTDIM				= (int)((float)BUTTOEXTDIM*0.75f);
	public static int								INBUTTONPXY				= (int)((BUTTOEXTDIM -BUTTOINTDIM)/2);
	
	
	public static int								SPACE	 				= (SCENEHEIGHT - (BUTTOEXTDIM * NBOFBUTTON)) / (NBOFBUTTON + 1);
	//colors
	public static int								TOOLBARBACKGROUND  		= 0XA6A6A6;
	public static int								SELCTEDTOOLBACKGROUND  	= 0X505050;


	protected int 									nStatus					= START;
	protected VertexBufferObjectManager				pVBO 					= null;
	protected Scene									pTheScene				= null;
	protected ColorToolBar							pToolBar				= null;
	protected ColorToolBar[]						pColorPikers			= null;


	public int[][]				ColorPallet = {
			{0x000000,0x808080,0xA9A9A9,0xB3B3B3,0xDCDCDC,0xDCDCDC,0xF8F8F8,0xFFFFFF},
			{0x191970,0x0000FF,0x4169E1,0x1E90FF,0x00BFFF,0x87CEEB,0xADD8E6,0xB0E0E6},
			{0x006400,0x6B8E23,0x008000,0x228B22,0x2E8B57,0x3CB371,0x00FF7F,0X00FA9A},
			{0xB8860B,0xFC8C00,0xFFA500,0xFFD700,0xFFFF00,0xEEE8AA,0xFAFAD2,0xF5F5D5},
			{0xB22222,0xA52A2A,0xCD5C5C,0xF08080,0xFA8072,0xFAA07A,0xFFA4B5,0xFFE4C4},
			{0x800000,0xD269E1,0xFF0000,0xFF4500,0xFF6347,0xF4A460,0xF5BED3,0xFFDAB9},
			{0x8B008B,0xD02090,0xFF1493,0xFF69B4,0xFF00FF,0xD87093,0xFFB6C1,0xFFC0CB},
			{0x800080,0x9932CC,0x8A2BE2,0xBA55D3,0xDA70D6,0xDDA0DD,0xD8BFD8,0xE6E6FA}};

	
	private class Button extends Rectangle{
		public Button(float pX, float pY, float pWidth, float pHeight,
				VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
			// TODO Auto-generated constructor stub
		}
		protected int ID;
		public Button(float pX, float pY, float pWidth, float pHeight,
				IRectangleVertexBufferObject pRectangleVertexBufferObject) {
			super(pX, pY, pWidth, pHeight, pRectangleVertexBufferObject);
			// TODO Auto-generated constructor stub
		}
		
	};
	
	private class ColorToolBar extends Rectangle{
		Rectangle[] pButtons;
		int selected;
		
		public void setSelected(int ID){
			pButtons[selected].setColor(Red(TOOLBARBACKGROUND),Green(TOOLBARBACKGROUND),Blue(TOOLBARBACKGROUND));
			selected = ID;
			pButtons[selected].setColor(Red(SELCTEDTOOLBACKGROUND),Green(SELCTEDTOOLBACKGROUND),Blue(SELCTEDTOOLBACKGROUND));
		}
		public ColorToolBar(VertexBufferObjectManager pRectangleVertexBufferObject,Scene theScene,ColorToolBar fatherTB) {
			super(0,0,SCENEHEIGHT,BUTTOEXTDIM, pRectangleVertexBufferObject);
			
			pButtons = new Rectangle[NBOFBUTTON];
			
			this.setColor(Red(TOOLBARBACKGROUND),Green(TOOLBARBACKGROUND),Blue(TOOLBARBACKGROUND));
			//create Line of buttons
			for (int index =0; index < NBOFBUTTON; index ++){
				Rectangle extButton = new Rectangle(0,0,BUTTOEXTDIM,BUTTOEXTDIM,pVBO);
				extButton.setColor(Red(TOOLBARBACKGROUND),Green(TOOLBARBACKGROUND),Blue(TOOLBARBACKGROUND));
				
				Button insButton = new Button(0,0,BUTTOINTDIM,BUTTOINTDIM,pVBO)	
				{
					public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
					{
						if (touchEvent.isActionUp())
						{
							setSelected(this.ID);
							return false;
						}
						return false;
					};
				};
				insButton.ID = index;
				pButtons[index] = extButton;
				extButton.attachChild(insButton);
				insButton.setPosition(INBUTTONPXY,INBUTTONPXY);
				insButton.setColor(Red(ColorPallet[index][0]),Green(ColorPallet[index][0]),Blue(ColorPallet[index][0]));
				pTheScene.registerTouchArea(insButton);
				
				this.attachChild(extButton);
				extButton.setPosition(index*(BUTTOEXTDIM),0);

			}
		}
	}
	
	public ColorPiker(VertexBufferObjectManager pRectangleVertexBufferObject,Scene theScene){
		super(0,0,SCENEHEIGHT,BUTTOEXTDIM, pRectangleVertexBufferObject);
		pTheScene = theScene;
		pVBO = pRectangleVertexBufferObject;
		init();
	}
	

	public void init(){
		nStatus			= START;
		//create tool-bar
		pTheScene.registerTouchArea(this);
		pTheScene.setColor(Red(TOOLBARBACKGROUND),Green(TOOLBARBACKGROUND),Blue(TOOLBARBACKGROUND));

		pToolBar = new ColorToolBar(pVBO,pTheScene,null);
		this.attachChild(pToolBar);
		
		pColorPikers = new ColorToolBar[NB_OF_PRIMARY_COLORS];
		for (int index=0; index <  NB_OF_PRIMARY_COLORS; index ++){
			pColorPikers[index] = new ColorToolBar(pVBO,pTheScene,pToolBar);
			this.attachChild(pColorPikers[index]);
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
