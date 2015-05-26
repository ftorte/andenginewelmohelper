package com.welmo.andengine.scenes.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.primitive.vbo.IRectangleVertexBufferObject;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.util.Pair;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.managers.SharedPreferenceManager;
import com.welmo.andengine.scenes.components.interfaces.IActionSceneListener;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.components.interfaces.IPersistent;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.ScnTags;
import com.welmo.andengine.scenes.descriptors.components.BasicComponentDescriptor.IDimension;
import com.welmo.andengine.scenes.descriptors.components.ProgressBarDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ProgressBarDescriptor.Type;
import com.welmo.andengine.scenes.operations.IOperationHandler;
import org.andengine.audio.sound.Sound;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProgressBar extends Rectangle implements IComponent, IPersistent{

	/*
	 *		  |---------------Width--------------|
	 *        |---H---|--- Width - H ----|---H---|
	 *         _______                    _______
	 * 	|	  |       |------------------|       |  -
	 * 	|	  |   |   |					 |       |  |
	 *  H 	  |  -+-  |    ProgressBar   |  ---  |  Internal Height
	 *  |	  |   |   |					 |     	 |  |
	 *  |     |_______|------------------|_______|  -
	 *   
	 * 
	 * 
	 * 
	 */
	
	
	/************************************************
	 * Constant
	 ***********************************************/
	final static String					TAG 			= "ProgressBar";
	
	final static int					NOTCH_PAD 			= 10;	//the top/bottom/left/right notch padding in pixels
	final static int					BUTTON_PAD 			= 10;	//the top/bottom/left/right notch padding in pixels
	final static int					DEFAUL_VALUE		= 0;	//the top/bottom/left/right notch padding in pixels
	
	/**********************************************
	 * Variables
	 **********************************************/
	
	protected VertexBufferObjectManager 			pVBO 				= null;
	protected ProgressBarDescriptor 				mParameters			= null;
	
	// Graphic Components
	protected ClickSprite							sButtonPlus				= null;
	protected ClickSprite							sButtonMinus			= null;
	protected Sprite								sProgressBarBackground	= null;
	protected Sprite								sProgressBarNotch 		= null;
	protected boolean								hasButtonsPlusMinus		= true;
	
	protected int									nBarWidth				= 0;
	protected List<Sprite>							lNotchs					= null;
	protected int									nNotchWidth				= 0;
	protected int									nTheValue				= 0;
	protected int									nMaxValue				= 0;
	protected Type									eType					= Type.DISCRETE;
	protected SharedPreferenceManager				pSPM					= null;
	
	protected Sound									sndTouch				= null;
	protected TextComponent							pValueDisplay			= null;
	protected Map<Integer,Integer>					mapDisplayValue			= null;
	protected int									nValueDisplayID			= 0;
	
	private class ClickSprite extends Sprite{

		public ClickSprite(float pX, float pY, float pWidth, float pHeight,
				ITextureRegion pTextureRegion,
				VertexBufferObjectManager pSpriteVertexBufferObject) {
			super(pX, pY, pWidth, pHeight, pTextureRegion, pSpriteVertexBufferObject);
		}
		@Override
		public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			if(pSceneTouchEvent.isActionDown()) {
				//Perform Some task
				return true;
			}
			else {
			//Perform Some task
			return false;
			}
		}
	}

	public ProgressBar(ProgressBarDescriptor parameters,VertexBufferObjectManager pVertexBufferObjectManager) {
		super(parameters.getIPosition().getX(),parameters.getIPosition().getY(), parameters.getIDimension().getWidth(),parameters.getIDimension().getHeight(), pVertexBufferObjectManager);
		this.setAlpha(0);		//set background transparent
		pVBO = pVertexBufferObjectManager;
		lNotchs = new ArrayList<Sprite>();
		mapDisplayValue = new HashMap<Integer,Integer>();
		configure(parameters);
	}
	
	

	protected void configure(ProgressBarDescriptor parameters){
		this.mParameters = parameters;
		
		ResourcesManager pRM = ResourcesManager.getInstance();
		
		String sIncButton 		= parameters.getIncrementButton();			//get texture for the button plus
		String sDecButton		= parameters.getsDecrementButton();			//get texture for the button minus
		String sBackground		= parameters.getsBarBackgroun();			//get texture for the progress background
		String sProgressNotch	= parameters.getsBarProgressNotch();
		
		IDimension		dim = parameters.getIDimension();
		
		nMaxValue				= parameters.getMaxValue();
		//nMinValue				= parameters.getMinValue();
		//nTheValue				= nMinValue;
		
		
		nValueDisplayID = parameters.getDisplayID();
		
		String values = parameters.getMapDisplayValues();
		if(values != null)
			readMapDisplayValues(values);
		
		//create button plus and minus
		if((sIncButton != null) && (sDecButton != null)){
			hasButtonsPlusMinus = true;
			sButtonMinus 	= new ClickSprite(0,0, dim.getHeight(), dim.getHeight(), pRM.getTextureRegion(sDecButton), pVBO);
			sButtonPlus 	= new ClickSprite(dim.getWidth() - dim.getHeight() ,0, dim.getHeight(), dim.getHeight(), pRM.getTextureRegion(sIncButton), pVBO);
			this.attachChild(sButtonMinus);
			this.attachChild(sButtonPlus);
		}
		else{
			hasButtonsPlusMinus = false;
		}

		//create background
		if(hasButtonsPlusMinus){
			nBarWidth = parameters.getIDimension().getWidth() - (2*(dim.getHeight() + BUTTON_PAD));
			sProgressBarBackground = new Sprite(dim.getHeight() + BUTTON_PAD ,0, nBarWidth, dim.getHeight(), pRM.getTextureRegion(sBackground), pVBO);
		}
		else{
			nBarWidth = parameters.getIDimension().getWidth();
			sProgressBarBackground = new Sprite(0,0, nBarWidth, dim.getHeight(), pRM.getTextureRegion(sBackground), pVBO);
		}
		this.attachChild(sProgressBarBackground);
		
		//create progress notch
		switch(mParameters.getType()){ 
			case DISCRETE:
				nNotchWidth = (int) ((nBarWidth-1*NOTCH_PAD)/nMaxValue - NOTCH_PAD);
				for(int index = 0; index < nMaxValue; index++){
					Sprite theNotch = new Sprite(NOTCH_PAD+index*(nNotchWidth + NOTCH_PAD) ,NOTCH_PAD, nNotchWidth, dim.getHeight()-2*NOTCH_PAD, pRM.getTextureRegion(sProgressNotch), pVBO);
					if(nTheValue > index)
						theNotch.setVisible(true);
					else
						theNotch.setVisible(false);
					sProgressBarBackground.attachChild(theNotch);
					lNotchs.add(theNotch);
				}
				break;
			default:
			break;
		}
		
		//get the touch 
		sndTouch = ResourcesManager.getInstance().getSound("puzzlepieces_touch").getTheSound();;
		
	}
	private void readMapDisplayValues(String mapDspValues) {
		//Parse JSON strings
		JSONObject jObject;
		try {
			jObject = new JSONObject(mapDspValues);
			JSONArray gamelevels = jObject.getJSONArray(ScnTags.S_A_MAPDISPLAYVALUES);
			//parse the array
			mapDisplayValue.clear();
			for (int i=0; i<gamelevels.length(); i++){
				
				JSONArray currLine = (JSONArray)gamelevels.get(i);
				mapDisplayValue.put(currLine.getInt(0),currLine.getInt(1));
			} 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void doIncrement(){
		if(nTheValue < nMaxValue){
			nTheValue ++;
			switch(mParameters.getType()){ 
			case DISCRETE:
				lNotchs.get(nTheValue-1).setVisible(true);
				doSave();
				break;
			default:
				break;
			}
		}
	}
	public void doDecrement(){
		if(nTheValue > 0){
			nTheValue --;
			switch(mParameters.getType()){ 
			case DISCRETE:
				lNotchs.get(nTheValue).setVisible(false);
				doSave();
				break;
			default:
				break;
			}
		}
	}
	public void updateNotchsShow(){
		int index;
		for(index = 0; index < nMaxValue; index++) lNotchs.get(index).setVisible(false);
		for(index = 0; index < nTheValue; index++) lNotchs.get(index).setVisible(true);
	}
	@Override
	public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
		if(touchEvent.isActionDown() && hasButtonsPlusMinus){
			
			final float[] sceneTouchEventXY = this.convertLocalToSceneCoordinates(X,Y);
			
			//check if has sound but has been release and re-aquire it
			if(sndTouch != null){
				if(sndTouch.isReleased()) sndTouch = ResourcesManager.getInstance().getSound("puzzlepieces_touch").getTheSound();
				sndTouch.play();
			}
			
			if(sButtonMinus.contains(sceneTouchEventXY[0],sceneTouchEventXY[1])) {
				doDecrement();
				doUpdateDisplay();
				return true;
			}
			if(sButtonPlus.contains(sceneTouchEventXY[0],sceneTouchEventXY[1])) {
				doIncrement();
				doUpdateDisplay();
				return true;
			}
		}
		return false;
	}
	@Override
	public void attachChild(IEntity pEntity) throws IllegalStateException {
		super.attachChild(pEntity);
		if(pEntity instanceof TextComponent && ((TextComponent)pEntity).getID() == this.nValueDisplayID){
			this.pValueDisplay = (TextComponent)pEntity;
		doUpdateDisplay();
		}
	}
	@Override
	public boolean detachChild(IEntity pEntity) {
		if(pEntity instanceof TextComponent && ((TextComponent)pEntity).getID() == this.nValueDisplayID)
			this.pValueDisplay = null;
		return super.detachChild(pEntity);
	}
	
	@Override
	public void doLoad() {
		if(!mParameters.getPersistence()){
			Log.i(TAG, "called doLoad on iPertistent but IsPersistent is false");
			return;
		}	
		
		if(pSPM == null)
			throw new NullPointerException("In doSave the Shared Preferences Manager is null");
		
		String varaible = null;
		if((varaible = mParameters.getGlobalVariable()) != null){
			SharedPreferences sp = pSPM.getSharedPreferences(SharedPreferenceManager.STDPreferences.GLOBAL_VARIABLES.name());
			nTheValue=sp.getInt(varaible, DEFAUL_VALUE);
			if(nTheValue < 0 || nTheValue > nMaxValue){
					nTheValue = DEFAUL_VALUE;
					doSave();
			}
			updateNotchsShow();
		}
	}
	@Override
	public void doSave() {
		String varaible = null;
		if((varaible = mParameters.getGlobalVariable()) != null){
			Editor ed = pSPM.getSharedPreferences(SharedPreferenceManager.STDPreferences.GLOBAL_VARIABLES.name()).edit();
			ed.putInt(varaible, nTheValue);
			ed.commit();
		}
	}	
	public void doUpdateDisplay() {
		if(this.pValueDisplay != null){
			String displayedvalue = Integer.toString(mapDisplayValue.get(nTheValue));
			pValueDisplay.setText(displayedvalue);
		}
	}	
	@Override
	public void doLoad(SharedPreferenceManager sp) {
		pSPM = sp;
		doLoad();
	}

	@Override
	public void doSave(SharedPreferenceManager sp) {
		pSPM = sp;
		doSave();
	}
	@Override
	public boolean isPersitent(){
		return mParameters.getPersistence();
	}
	@Override
	public void setSharedPreferenceManager(SharedPreferenceManager sp) {
		this.pSPM = sp;
	}

	@Override
	public int getID() {
		return mParameters.getID();
	}
	@Override
	public IEntity getTheComponentParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setID(int ID) {
		 mParameters.setID(ID);
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
