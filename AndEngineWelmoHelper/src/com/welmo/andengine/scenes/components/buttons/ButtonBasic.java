package com.welmo.andengine.scenes.components.buttons;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.components.interfaces.IActionSceneListener;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ButtonDescriptor;
import com.welmo.andengine.scenes.operations.IOperationHandler;
import com.welmo.andengine.scenes.operations.Operation;
import com.welmo.andengine.scenes.operations.IOperationHandler.OperationTypes;

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
	
	public enum Types{BASIC, CLICK, ON_OFF, ON_OFF_WITH_TIMER}

	final static String 							TAG 					= "ButtonBasic";
	public int 										nID 					= -1;
	// button images
	protected Entity								insButtonBG 			= null;
	protected Entity								insButtonON 			= null;
	protected Entity								insButtonOFF 			= null;
	
	// button handling status
	protected int									nStatus					= START;
	public static int								START 					= 0;
	public static int								CONFIGURED 				= 1;
	public static int								INITIALIZED 			= 2;
	protected Types									mtype					= Types.BASIC;
	
	protected IOperationHandler						mMessageHandler			= null;
	protected VertexBufferObjectManager				pVBO 					= null;
	
	//Parameters	
	ButtonDescriptor								mParameters				= null;
	
	//Object Status values handler
	
	//EventMessage
	List<Operation>									mMessages				=null;
	

	public ButtonBasic(ButtonDescriptor parameters,VertexBufferObjectManager pVertexBufferObjectManager) {
		super(parameters.getIPosition().getX(),parameters.getIPosition().getY(), parameters.nExternaDimension, parameters.nExternaDimension, pVertexBufferObjectManager);
		
		pVBO = pVertexBufferObjectManager;
		mParameters = parameters;
		mMessages = new ArrayList<Operation>();
		configure(mParameters);
		
		nStatus = START;
	}
	
	

		
	void init(){

		//Set Button ID
		this.setID(mParameters.getID());
				
		//set button default background
		//TODO  int backgroundCol=mParameters.nSelectedColotBackGround;
		//TODO  this.setColor(ColorHelper.Red(backgroundCol),ColorHelper.Green(backgroundCol),ColorHelper.Blue(backgroundCol));
		//set Transparent Background
		this.setColor(0.0f,0.0f,0.0f,0.0f);
		
		
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
			
			ITextureRegion textureBG = null;
			ITextureRegion textureON = null;
			ITextureRegion textureOFF = null;
			
			
			if(mParameters.sBackGroundTextureName != null){
				textureBG = pRM.getTextureRegion(mParameters.sBackGroundTextureName); 
			}
			textureON = pRM.getTextureRegion(mParameters.sButtonTextureON);
			textureOFF = pRM.getTextureRegion(mParameters.sButtonTextureOFF);
			
			if(textureON == null || textureOFF == null)
				throw new NullPointerException("no teXture defined for the button");

			if(textureBG != null)
				this.insButtonBG = new Sprite(0,0,mParameters.nExternaDimension,mParameters.nExternaDimension,textureBG,pVBO);
			
			this.insButtonON = new Sprite(0,0,mParameters.nInternalDimension,mParameters.nInternalDimension,textureON,pVBO);
			this.insButtonOFF = new Sprite(0,0,mParameters.nInternalDimension,mParameters.nInternalDimension,textureOFF,pVBO);
			
			//set postions
			int posXY = (mParameters.nExternaDimension - mParameters.nInternalDimension)/2;
			if(mParameters.sBackGroundTextureName != null)
				insButtonBG.setPosition(0,0);
			insButtonON.setPosition(posXY,posXY);
			insButtonOFF.setPosition(posXY,posXY);
			
			// set Z orders
			if(mParameters.sBackGroundTextureName != null)
				insButtonBG.setZIndex(1);
			insButtonON.setZIndex(10);
			insButtonOFF.setZIndex(10);
			
			//enable by default status = OFF
			insButtonON.setVisible(false);
			insButtonOFF.setVisible(true);
			
			//Attache buttons to parend rectangle
			if(mParameters.sBackGroundTextureName != null)
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
	}

	// -----------------------------------------------------------------------------------------------------------------
	// Getter & Setters
	// -----------------------------------------------------------------------------------------------------------------

	public void registerTouchAreaToScene(Scene theScene){
		theScene.registerTouchArea((ITouchArea) this);
	}
	// -----------------------------------------------------------------------------------------------------------------
	// Override functions 
	// -----------------------------------------------------------------------------------------------------------------
	public void parseMessage(ButtonDescriptor pDsc){
		if(pDsc.getOnClickMessage()!=""){
			StringTokenizer st = new StringTokenizer(pDsc.getOnClickMessage(),",");
			Operation theMessage = new Operation(OperationTypes.valueOf(st.nextToken()));
			List<Float> arParameters = new ArrayList<Float>();
			while (st.hasMoreTokens()) {
				arParameters.add(Float.parseFloat(st.nextToken()));
			}
			if(arParameters.size()>0)
				theMessage.setParametersNumbers(arParameters);
			//add message to message list
			this.mMessages.add(theMessage);
		}
	}
	// -----------------------------------------------------------------------------------------------------------------
	// Implement Interfaces.
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public int 	getID() {return nID;}
	@Override
	public void setID(int nID) {this.nID = nID;}
	@Override
	public void configure(BasicDescriptor pDsc) {
		if(!(pDsc instanceof ButtonDescriptor))
			throw new NullPointerException("Wrong descriptor type: expected a ButtonDescriptor");
		init();
		nStatus = ButtonBasic.CONFIGURED;
	}
	@Override		
	public void setOperationsHandler(IOperationHandler messageHandler){
		//initialize message handler
		mMessageHandler = messageHandler;
		if(! (messageHandler instanceof IOperationHandler))
			throw new NullPointerException("the message handler is not right class type");
		nStatus = ButtonBasic.INITIALIZED;
	}
	// -----------------------------------------------------------------------------------------------------------------
	// abstract methods
	// -----------------------------------------------------------------------------------------------------------------
	abstract public IEntity getParent();
	abstract public void 	setParent(IEntity parent);
	@Override
	abstract public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y);
}
