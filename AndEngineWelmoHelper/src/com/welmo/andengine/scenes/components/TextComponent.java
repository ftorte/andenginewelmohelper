package com.welmo.andengine.scenes.components;


import org.andengine.engine.Engine;
import org.andengine.entity.shape.IAreaShape;
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
	private static final String TAG = "TextComponent";

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
	public TextComponent(TextObjectDescriptor pTXTDscf, ResourcesManager pRM, Engine theEngine,IAreaShape theFather){
		super(0, 0, pRM.getFont(pTXTDscf.getFontName()), 
				pTXTDscf.getMessage(), new TextOptions(HorizontalAlign.CENTER), 
				theEngine.getVertexBufferObjectManager());
		init();
		configure(pTXTDscf);
		if(theFather != null){
			PositionHelper.align(pTXTDscf.getIPosition(), this, theFather);
		}
		//set color	
		String colorName = pTXTDscf.getICharacteristis().getColor();
		Log.i(TAG,"Get Color: " + colorName);
		if(!colorName.equals(""))
				this.setColor(pRM.getColor(colorName));
	}

	// ===========================================================
	// private member function
	// ===========================================================	
	protected void init(){
		mIClicakableImpmementation =  new IComponentClickableDfltImp();
		mIClicakableImpmementation.setParent(this);
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
	public IComponentEventHandler getEventsHandler(Events theEvent) {
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
}
