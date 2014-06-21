package com.welmo.andengine.scenes.components;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.components.interfaces.IComponentClickableDfltImp;
import com.welmo.andengine.scenes.components.interfaces.IActionOnSceneListener;
import com.welmo.andengine.scenes.components.interfaces.IComponentClickable;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.components.interfaces.IComponentEventHandler;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.descriptors.events.SceneActions.ActionType;

public class CompoundSprite extends Rectangle implements IComponent, IComponentClickable{

	@Override
	public void attachChild(IEntity pEntity) throws IllegalStateException {
		if(pEntity instanceof IAreaShape )
			attachComponentChild((IAreaShape)pEntity);
		super.attachChild(pEntity);
	}
	// ===================================================================================
	// Constants
	// ===================================================================================
	//Log & Debug
	private static final String 						TAG = "ClickableSprite";

	private SpriteObjectDescriptor						pDescriptor					= null;
	protected IComponentClickableDfltImp 			mIClicakableImpmementation 	= null;
	// ===================================================================================
	// Constructors
	// ===================================================================================

	public CompoundSprite(float pX, float pY, float pWidth, float pHeight,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
		this.setColor(0, 1, 0);
		init();
	}
	// ===================================================================================
	// private member function
	// ===========================================================	
	protected void init(){
		mIClicakableImpmementation =  new IComponentClickableDfltImp();
		mIClicakableImpmementation.setParent(this);
	}
	public void attachComponentChild(IAreaShape pNewShape) throws IllegalStateException {


		float curObjXmin,curObjXmax,curObjYmin,curObjYmax;
		float newObjXmin,newObjXmax,newObjYmin,newObjYmax;

		float px = pNewShape.getX(); 
		float py = pNewShape.getY();

		//Calculate coordinate current rectangle
		curObjXmin = this.getX();
		curObjXmax = this.getX() + this.getWidth();
		curObjYmin = this.getY();
		curObjYmax = this.getY() + this.getHeight();


		newObjXmin = pNewShape.getX();
		newObjXmax = newObjXmin + pNewShape.getWidth();
		newObjYmin = pNewShape.getY();
		newObjYmax = newObjYmin + pNewShape.getHeight();

		if(mChildren == null){
			this.setPosition(pNewShape.getX(),pNewShape.getY());
			this.setSize(pNewShape.getWidth(),pNewShape.getHeight());
			pNewShape.setPosition(0, 0);
		}
		else{
			//1st expand compound zone
			this.setWidth(Math.max(curObjXmax, newObjXmax)-Math.min(curObjXmin,newObjXmin));
			this.setHeight(Math.max(curObjYmax, newObjYmax)-Math.min(curObjYmin,newObjYmin));
			//2nd calculate compound delta PX and delta PY
			float deltaPX = Math.min(curObjXmin,newObjXmin)-curObjXmin;
			float deltaPY = Math.min(curObjYmin,newObjYmin)-curObjYmin;
			//3th move all child by -delatX/Y
			for (IEntity tmpEntity:mChildren)
				tmpEntity.setPosition(tmpEntity.getX()-deltaPX, tmpEntity.getY()-deltaPY);
			//4th move compound by delatX/Y
			this.setPosition(this.getX()+deltaPX, this.getY()+deltaPY);
			//5th add new entity to relative position in the Compound space
			pNewShape.setPosition(px-this.getX(),py-this.getY());
		}
	}
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		return this.onTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	}
	public Object getPDescriptor() {
		return pDescriptor;
	}
	public void setPDescriptor(SpriteObjectDescriptor pDescriptor) {
		this.pDescriptor = pDescriptor;
		this.configure(pDescriptor);
	}
	public void configure(SpriteObjectDescriptor spDsc){
		ResourcesManager pRM = ResourcesManager.getInstance();
		setID(spDsc.getID());

		/* Setup Rotation*/
		setRotationCenter(spDsc.getIOriantation().getRotationCenterX(), spDsc.getIOriantation().getRotationCenterX());
		setRotation(spDsc.getIOriantation().getOrientation());

		//set position			
		setX(spDsc.getIPosition().getX());
		setY(spDsc.getIPosition().getY());

		//set Z_Order
		this.setZIndex(spDsc.getIPosition().getZorder());	

		//set color	
		String theColor = spDsc.getICharacteristis().getColor();
		Log.i(TAG,"Get Color: " + theColor);
		if(!theColor.equals(""))
			this.setColor(pRM.getColor(theColor));
	}

	// ===========================================================
	// Interfaces & Superclass
	// ===========================================================	
	// ====== IClickableSprite ==== 	
	public void addEventsHandler(Events theEvent, IComponentEventHandler oCmpDefEventHandler){
		mIClicakableImpmementation.addEventsHandler(theEvent, oCmpDefEventHandler);
	}
	public IActionOnSceneListener getActionOnSceneListener(){
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
	public void build(BasicDescriptor pDsc) {
		// TODO Auto-generated method stub
		
	}
}
