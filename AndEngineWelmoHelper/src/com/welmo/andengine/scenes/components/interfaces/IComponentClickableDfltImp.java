package com.welmo.andengine.scenes.components.interfaces;

import java.util.HashMap;

import org.andengine.entity.IEntity;
import org.andengine.input.touch.TouchEvent;

import android.util.Log;

import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.descriptors.events.SceneActions.ActionType;

/*****************************************************************************************
// Default Impementation of the IComponent Clickable interface that may be used by a compont
//
//******************************************************************************************/
public class IComponentClickableDfltImp implements IComponentClickable {
	
	private final static String 							TAG				= "DefaultIClickableImplementation";
	protected int 											nID				=-1;
	protected HashMap<Events,IComponentEventHandler> 		hmEventHandlers = null;
	protected IEntity										mParent			= null;
	protected boolean										on_move			= false;
	protected TouchEvent									lastTouchEvent	= null;
	
			
	public IComponentClickableDfltImp(){
		hmEventHandlers	= new HashMap<Events,IComponentEventHandler>();
		lastTouchEvent = new TouchEvent();
	}
	// -----------------------------------------------------------------------------------------
	// Implement Interfaces
	// -----------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------
	// IBasicComponent
	public IEntity getParent() {
		return mParent;
	}
	public void setParent(IEntity mParent) {
		this.mParent = mParent;
	}
	public int getID() {
		return nID;
	}
	public void setID(int ID) {
		this.nID = ID;
	}
	// -----------------------------------------------------------------------------------------
	// IClickable
	public void addEventsHandler(Events theEvent, IComponentEventHandler oCmpDefEventHandler){
		hmEventHandlers.put(theEvent, oCmpDefEventHandler);
	}
	@Override
	public IActionOnSceneListener getActionOnSceneListener() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onFireEventAction(Events event, ActionType type){
		if(hmEventHandlers == null){
			throw new NullPointerException("onFireAction no eventshmEventHandlers is null");
		}
		IComponentEventHandler handlerEvent = hmEventHandlers.get(event);
		handlerEvent.onFireAction(type, mParent);
	}
	@Override
	public boolean onTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {

		if (mParent instanceof IEntity){

			boolean managed = false;
			switch (pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
				/*if (MLOG.LOG)Log.i(TAG,"onAreaTouched ACTION_DOWN = " + nID);
		break;*/
			case TouchEvent.ACTION_MOVE:
				if(hmEventHandlers != null){
					Log.i(TAG,"\t launch event handler trough object control");
					IComponentEventHandler handlerEvent = hmEventHandlers.get(ComponentEventHandlerDescriptor.Events.ON_MOVE);
					if(handlerEvent != null){
						if(!on_move){
							on_move = true;
							lastTouchEvent.set(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
						}
						else{
							handlerEvent.handleEvent(mParent,pSceneTouchEvent,lastTouchEvent);
							lastTouchEvent.set(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
						}
					}
				}
				break;
			case TouchEvent.ACTION_UP:
				if(on_move)
					on_move=false;
				if(hmEventHandlers != null){
					Log.i(TAG,"\t launch event handler trough object control");
					IComponentEventHandler handlerEvent = hmEventHandlers.get(ComponentEventHandlerDescriptor.Events.ON_CLICK);
					if(handlerEvent != null){
						Log.i(TAG,"\t launch event handler trough object found");
						handlerEvent.handleEvent(mParent,pSceneTouchEvent,null);
					}
				}
				break;
			case TouchEvent.ACTION_CANCEL:
			case TouchEvent.ACTION_OUTSIDE:
			case TouchEvent.INVALID_POINTER_ID:
			default:
				if(on_move)
					on_move=false;
			}
			return managed;
		}
		return false;
	}
	@Override
	public void build(BasicDescriptor pDsc) {
		// TODO Auto-generated method stub
		
	}
}

