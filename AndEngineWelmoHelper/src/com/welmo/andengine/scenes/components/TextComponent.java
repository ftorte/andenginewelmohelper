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

	// ===========================================================
	// Fields
	// ===========================================================
	protected IComponentClickableDfltImp 			mIClicakableImpmementation 	= null;

		
	// ===========================================================
	// Constructors
	// ===========================================================
	public TextComponent(float pX, float pY, IFont pFont, CharSequence pText,
			TextOptions pTextOptions,
			VertexBufferObjectManager pTextVertexBufferObject) {
		super(pX, pY, pFont, pText, pTextOptions,
				pTextVertexBufferObject);
		init();
	}
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
			CharSequence message = pRM.getStringResourceByName(pTXTDscf.message);
			if (message.length() > this.nTextMaxLenght)
				message = new String(message.subSequence(0, nTextMaxLenght).toString());
			
			this.setText(message);
		}
		
		//set scale 
		if(pTXTDscf.scale != 1 ){
			this.setScaleCenter(0, 0);
			this.setScale(pTXTDscf.scale);
			this.scale	= pTXTDscf.scale;
		}
		
		//set position	after having loaded the text	
		setX(pTXTDscf.getIPosition().getX());
		setY(pTXTDscf.getIPosition().getY());
		
	}

	// ===========================================================
	// private member function
	// ===========================================================	
	protected void init(){
		mIClicakableImpmementation =  new IComponentClickableDfltImp();
		mIClicakableImpmementation.setTheComponentParent(this);
		
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
