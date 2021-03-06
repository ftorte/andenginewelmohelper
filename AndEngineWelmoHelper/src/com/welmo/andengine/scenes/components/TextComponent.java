package com.welmo.andengine.scenes.components;


import java.util.ArrayList;
import java.util.Iterator;

import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

import android.util.Log;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.components.interfaces.IComponentClickableDfltImp;
import com.welmo.andengine.scenes.components.interfaces.IActionSceneListener;
import com.welmo.andengine.scenes.components.interfaces.IComponentClickable;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.components.interfaces.IComponentEventHandler;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.components.TextObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.descriptors.events.SceneActions.ActionType;
import com.welmo.andengine.scenes.operations.IOperationHandler;

public class TextComponent extends Text implements IComponent, IComponentClickable{
	// ===========================================================
	// Constants
	// ===========================================================
	//Log & Debug
	private static final String 	TAG = "TextComponent";
	private float					scale = 1.0f;
	private int						nTextMaxLenght;
	private CharSequence[]			theMessages = null;
	private float 					standarPx=0;
	private float 					standarPy=0;
	
	// ===========================================================
	// Fields
	// ===========================================================
	protected IComponentClickableDfltImp 			mIClicakableImpmementation 	= null;

		
	// ===========================================================
	// Constructors
	// ===========================================================
	/*public TextComponent(float pX, float pY, IFont pFont, CharSequence pText,
			TextOptions pTextOptions,
			VertexBufferObjectManager pTextVertexBufferObject) {
		super(pX, pY, pFont, pText, pTextOptions,
				pTextVertexBufferObject);
		init();		
	}*/
	public TextComponent(TextObjectDescriptor pTXTDscf, ResourcesManager pRM, Engine theEngine){
		super(0, 0, pRM.getFont(pTXTDscf.getFontName()), 
				pTXTDscf.getMessage(), pTXTDscf.textmaxlenght , (pTXTDscf.textmaxwidth != 0 ? new TextOptions(AutoWrap.WORDS, pTXTDscf.textmaxwidth, HorizontalAlign.LEFT): new TextOptions(HorizontalAlign.LEFT)), 
				theEngine.getVertexBufferObjectManager());
		//TO DO set atowrap width are parameter for the descriptot
		nTextMaxLenght = pTXTDscf.textmaxlenght;
		
		init();
		configure(pTXTDscf);
		
		//set color	
		String colorName = pTXTDscf.getICharacteristis().getColor();
		Log.i(TAG,"Get Color: " + colorName);
		if(!colorName.equals(""))
				this.setColor(pRM.getColor(colorName));
		
		if(pTXTDscf.type == TextObjectDescriptor.TextTypes.RESOURCE){
			//get list of available resouces by splitting using , as separator
			String resourceName[] = pTXTDscf.message.split(",");
			theMessages = new CharSequence[resourceName.length];   	//create vecor that will contain the messages to display
			
			for(int index = 0; index < resourceName.length; index++){
				theMessages[index] = pRM.getStringResourceByName(resourceName[index]);
				if (theMessages[index].length() > this.nTextMaxLenght)
					theMessages[index]  = new String(theMessages[index].subSequence(0, nTextMaxLenght).toString());
			}
			this.setText(theMessages[0]);
		}
		
		//set scale 
		
		if(pTXTDscf.scale != 1 ){
			this.setScaleCenter(0, 0);
			this.setScale(pTXTDscf.scale);
			this.scale	= pTXTDscf.scale;
		}
		
		this.standarPx= pTXTDscf.getIPosition().getX();
		this.standarPy= pTXTDscf.getIPosition().getY();
		
		//set position	after having loaded the text	
		setX(this.standarPx);
		setY(this.standarPy);
		
	}

	// ===========================================================
	// private member function
	// ===========================================================	
	protected void init(){
		mIClicakableImpmementation =  new IComponentClickableDfltImp();
		mIClicakableImpmementation.setTheComponentParent(this);
		
	}
	// ===========================================================
	// public member function
	// ===========================================================		
	public void setText(int textNb){
		//check the this ha multiple messages else do nothing
		if(theMessages!=null){
			if(textNb < theMessages.length && textNb >= 0)		//check that the id is valid or do nothing
				this.setScaleCenter(0, 0);
				this.setScale(1);
				this.setText(theMessages[textNb]);
				this.setScale(this.scale);
				
				setX(this.standarPx + (this.getWidthScaled()-this.getWidth())/2);
				setY(this.standarPy + (this.getHeightScaled()- this.getHeight())/2);
		}
	}
	public void configure(TextObjectDescriptor spDsc){
		setID(spDsc.getID()); 
		/* Setup Rotation*/
		setRotationCenter(getWidth()/2, getHeight()/2);
		setRotation(spDsc.getIOriantation().getOrientation());
		//set position			
		setX(spDsc.getIPosition().getX());
		setY(spDsc.getIPosition().getY());
	}
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		return this.onTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	}
	
	// ===========================================================
	// Interfaces & Superclass
	// ===========================================================	
	// ===========================================================		
	// ====== IClickableSprite ==== 	
	public void addEventsHandler(Events theEvent, IComponentEventHandler oCmpDefEventHandler){
			mIClicakableImpmementation.addEventsHandler(theEvent, oCmpDefEventHandler);
	}
	public IActionSceneListener getActionOnSceneListener(){
		return mIClicakableImpmementation.getActionOnSceneListener();
	}
	public int getID() {
		return mIClicakableImpmementation.getID();
	}
	public void setID(int ID) {
		mIClicakableImpmementation.setID(ID);
	}
	public boolean onTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		return mIClicakableImpmementation.onTouched(pSceneTouchEvent,pTouchAreaLocalX,pTouchAreaLocalY);
	}
	@Override
	public void onFireEventAction(Events event, ActionType type) {
		mIClicakableImpmementation.onFireEventAction(event, type);
	}
	@Override
	public String getPersistenceURL() {
		return mIClicakableImpmementation.getPersistenceURL();
	}
	@Override
	public void configure(BasicDescriptor pDsc) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public ArrayList<IComponentEventHandler>  getEventsHandler(Events theEvent) {
		mIClicakableImpmementation.getEventsHandler(theEvent);
		return null;
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
	public boolean onFireEvent(Events event) {
		return mIClicakableImpmementation.onFireEvent(event);
	}
	@Override
	public IEntity getTheComponentParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setTheComponentParent(IEntity parent) {
		// TODO Auto-generated method stub
	}
}
