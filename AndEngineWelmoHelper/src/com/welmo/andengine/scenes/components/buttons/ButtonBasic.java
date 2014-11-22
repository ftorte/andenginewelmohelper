package com.welmo.andengine.scenes.components.buttons;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ButtonDescriptor;
import com.welmo.andengine.scenes.operations.IOperationHandler;
import com.welmo.andengine.scenes.operations.Operation;
import com.welmo.andengine.scenes.operations.IOperationHandler.OperationTypes;
import com.welmo.andengine.utility.ColorHelper;

/*
 * A button basic is an abstract square button build as an extension of a Rectangle.
 * The button is compose of:
 * 		1) a rectangle or a sprite for the background.
 * 		1) a rectangle or a sprite for the on Status.
 * 		1) a rectangle or a sprite for the off Status.
 * 
 *  To be configure the button get a parameter type class ButtonParameters
 */

public abstract class ButtonBasic extends Rectangle implements IComponent{
	
	public enum Types{
		BASIC, CLICK, ON_OFF, ON_OFF_WITH_TIMER, PULSE
	}

	final static String 							TAG 					= "ButtonBasic";
	public int 										nID 					= -1;
	// button images
	protected Entity								insButtonBG 			= null;
	protected Entity								insButtonON 			= null;
	protected Entity								insButtonOFF 			= null;
	
	protected int									nStatus					= START;
	protected Types									mtype					= Types.BASIC;
	
	protected IOperationHandler						mMessageHandler			= null;
	protected VertexBufferObjectManager				pVBO 					= null;
	//Parameters	
	ButtonDescriptor								mParameters				= null;
	
	//Object Status values handler
	public static int								START 					= 0;
	public static int								CONFIGURED 				= 0;
	public static int								INITIALIZED 			= 1;
	
	//EventMessage
	List<Operation>									mMessages				=null;
	

	public ButtonBasic(ButtonDescriptor parameters, IOperationHandler messageHandler, VertexBufferObjectManager pVertexBufferObjectManager) {
		
		super(0, 0, parameters.nExternaDimension, parameters.nExternaDimension, pVertexBufferObjectManager);
		
		if(! (messageHandler instanceof IOperationHandler))
			throw new NullPointerException("the message handler is not right class type");
		
		//Init Variables
		
		mMessageHandler = messageHandler;
		mMessages = new ArrayList<Operation>();
		pVBO = pVertexBufferObjectManager;
		configure(parameters);
	}
	
	public void configure(ButtonDescriptor parameters){
		//Constructor copy disabled mParameters = new ButtonDescriptor(parameters);	
		mParameters = parameters;	
		nStatus = ButtonBasic.CONFIGURED;
		init();
	}

	void init(){

		//set button default background
		int backgroundCol=mParameters.nSelectedColotBackGround;
		this.setColor(ColorHelper.Red(backgroundCol),ColorHelper.Green(backgroundCol),ColorHelper.Blue(backgroundCol));
		
		//Set Button ID
		this.setID(mParameters.getID());
		
		//create inside button 1st check that there are not yet background and buttons sprites already configured
		if(insButtonBG != null){
			this.detachChild(insButtonBG);
			insButtonBG = null;
		}
		if(insButtonON != null){
			this.detachChild(insButtonON);
			insButtonON = null;
		}
		if(insButtonOFF != null){
			this.detachChild(insButtonOFF);
			insButtonOFF = null;
		}
		
		if(mParameters.bSpriteBased){
			ResourcesManager pRM = ResourcesManager.getInstance();
			ITextureRegion textureBG = pRM.getTextureRegion(mParameters.sBackGroundTextureName); 
			ITextureRegion textureON = pRM.getTextureRegion(mParameters.sButtonTextureON);
			ITextureRegion textureOFF = pRM.getTextureRegion(mParameters.sButtonTextureOFF);

			if(textureBG == null ||  textureON == null || textureOFF == null)
				throw new NullPointerException("no tecture defined for the button");

			this.insButtonBG = new Sprite(0,0,mParameters.nExternaDimension,mParameters.nExternaDimension,textureBG,pVBO);
			this.insButtonON = new Sprite(0,0,mParameters.nInternalDimension,mParameters.nInternalDimension,textureON,pVBO);
			this.insButtonOFF = new Sprite(0,0,mParameters.nInternalDimension,mParameters.nInternalDimension,textureOFF,pVBO);
			
			//set postions
			int posXY = (mParameters.nExternaDimension - mParameters.nInternalDimension)/2;
			insButtonBG.setPosition(0,0);
			insButtonON.setPosition(posXY,posXY);
			insButtonOFF.setPosition(posXY,posXY);
			
			// set Z orders
			insButtonBG.setZIndex(1);
			insButtonON.setZIndex(10);
			insButtonOFF.setZIndex(10);
			
			//enable by default status = OFF
			insButtonON.setVisible(false);
			insButtonOFF.setVisible(true);
			
			//Attache buttons to parend rectangle
			attachChild(insButtonBG);
			attachChild(insButtonON);
			attachChild(insButtonOFF);
		}
		
		else{
			this.insButtonBG = new Rectangle(0,0,mParameters.nInternalDimension,mParameters.nInternalDimension,pVBO);
			attachChild(insButtonBG);
			int posXY = (mParameters.nExternaDimension - mParameters.nExternaDimension)/2;
			insButtonBG.setPosition(posXY,posXY);
		}
		
		this.parseMessage(mParameters);
		
		nStatus = ButtonBasic.INITIALIZED;
	}

	public int getID() {
		return nID;
	}

	public void setID(int nID) {
		this.nID = nID;
	}

	public void registerTouchAreaToScene(Scene theScene){
		theScene.registerTouchArea((ITouchArea) this);
	}
	@Override
	public void configure(BasicDescriptor pDsc) {
		if(!(pDsc instanceof ButtonDescriptor))
			throw new NullPointerException("Wrong descriptor type: expected ButtonDescriptor");
		if (Types.BASIC != Types.valueOf(pDsc.getSubType()))
			throw new NullPointerException("Wrong button type");
		configure((ButtonDescriptor)pDsc);
		init();
	}
	@Override
	abstract public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y);
	public void parseMessage(ButtonDescriptor pDsc){
		if(pDsc.getOnClickMessage()!=""){
			StringTokenizer st = new StringTokenizer(pDsc.getOnClickMessage(),",");
			Operation theMessage = new Operation(OperationTypes.valueOf(st.nextToken()));
			List<Float> arParameters = new ArrayList<Float>();
			while (st.hasMoreTokens()) {
				arParameters.add(Float.parseFloat(st.nextToken()));
			}
			if(arParameters.size()>0)
				theMessage.setParameterNumbers(arParameters);
			//add message to message list
			this.mMessages.add(theMessage);
		}
	}
}
