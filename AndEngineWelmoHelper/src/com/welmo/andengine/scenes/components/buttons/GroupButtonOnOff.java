package com.welmo.andengine.scenes.components.buttons;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.managers.SharedPreferenceManager;
import com.welmo.andengine.scenes.components.interfaces.IActionSceneListener;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.components.interfaces.IPersistent;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ButtonDescriptor;
import com.welmo.andengine.scenes.descriptors.components.GroupButtonsDescriptor;
import com.welmo.andengine.scenes.operations.IOperationHandler;

public class GroupButtonOnOff extends Rectangle implements IComponent , IPersistent{

	final static String 							TAG 					= "GroupButtonOnOff";
	public int 										nID 					= -1;
	
	//Parameters	
	protected GroupButtonsDescriptor					mParameters				= null;
	protected boolean									bIsPersistent			= false;
	//set type by defaul = one choice
	protected GroupButtonsDescriptor.Types				mType					= GroupButtonsDescriptor.Types.ONE_CHOICE;

	
	//****************************************************************************************
	// constructor
	//****************************************************************************************
	public GroupButtonOnOff(GroupButtonsDescriptor parameters,VertexBufferObjectManager pVertexBufferObjectManager) {
		super(parameters.getIPosition().getX(),parameters.getIPosition().getY(), parameters.getIDimension().getWidth(),parameters.getIDimension().getHeight(), pVertexBufferObjectManager);
		mParameters = parameters;
		init();
	}
	
	//****************************************************************************************
	// private functions
	//****************************************************************************************
	private void init(){

	//Set Button ID
	this.setID(mParameters.getID());
	
	//Setup is Persitent
	this.bIsPersistent = mParameters.getPersistence();

	//set button default background
	//TODO  int backgroundCol=mParameters.nSelectedColotBackGround;
	//TODO  this.setColor(ColorHelper.Red(backgroundCol),ColorHelper.Green(backgroundCol),ColorHelper.Blue(backgroundCol));
	//set Transparent Background
	this.setColor(0.0f,0.0f,0.0f,0.0f);


	this.mType = mParameters.getType();
	
	//create inside button 1st check that there are not yet background and buttons sprites already configured
	/*if(insButtonBG != null){
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
	}*/

	/*if(mParameters.bSpriteBased){
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


	}

	else{
		this.insButtonBG = new Rectangle(0,0,mParameters.nInternalDimension,mParameters.nInternalDimension,pVBO);
		attachChild(insButtonBG);
		int posXY = (mParameters.nExternaDimension - mParameters.nExternaDimension)/2;
		insButtonBG.setPosition(posXY,posXY);
	}

	this.parseMessage(mParameters);
	}*/
	}


	@Override
	public void doLoad() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doLoad(SharedPreferenceManager sp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSave(SharedPreferenceManager sp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSharedPreferenceManager(SharedPreferenceManager sp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPersitent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IEntity getTheComponentParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setID(int ID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTheComponentParent(IEntity parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configure(BasicDescriptor pDSC) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setActionSceneListner(IActionSceneListener scenelistener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOperationsHandler(IOperationHandler messageHandler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getPersistenceURL() {
		// TODO Auto-generated method stub
		return null;
	}
}
