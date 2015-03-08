package com.welmo.andengine.scenes.components.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.andengine.entity.IEntity;
import org.andengine.input.touch.TouchEvent;

import android.util.Log;

import com.welmo.andengine.scenes.IManageableScene;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.descriptors.events.SceneActions.ActionType;
import com.welmo.andengine.scenes.operations.IOperationHandler;

/*****************************************************************************************
// Default Impementation of the IComponent Clickable interface that may be used by a compont
//
//******************************************************************************************/
public class IComponentClickableDfltImp implements IComponentClickable {
	
	private final static String 							TAG				= "DefaultIClickableImplementation";
	protected int 											nID				=-1;
	protected HashMap<Events,ArrayList<IComponentEventHandler>> 	hmEventHandlers = null;
	protected IEntity										mParent			= null;
	protected boolean										on_move			= false;
	protected TouchEvent									lastTouchEvent	= null;
	protected IOperationHandler 							pMessageHandler = null;
	
			
	public IComponentClickableDfltImp(){
		hmEventHandlers	= new HashMap<Events,ArrayList<IComponentEventHandler>>();
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
		//check if already has an handler for the event
		ArrayList<IComponentEventHandler> 	lListEventHandlers = null;
												
		if((lListEventHandlers = hmEventHandlers.get(theEvent)) == null){
			lListEventHandlers = new ArrayList<IComponentEventHandler>();
			lListEventHandlers.add(oCmpDefEventHandler);
			hmEventHandlers.put(theEvent, lListEventHandlers);
		}
		else
			lListEventHandlers.add(oCmpDefEventHandler);
	}
	@Override
	public IActionSceneListener getActionOnSceneListener() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onFireEventAction(Events thsEvent, ActionType type){
		
		ArrayList<IComponentEventHandler> 	lListEventHandlers = null;
		
		//check that EventesHandlers has been correctly initialized
		if(hmEventHandlers == null) throw new NullPointerException("onFireAction no eventshmEventHandlers is null");
		
		//check that there is an event for the event fired
		if((lListEventHandlers = hmEventHandlers.get(thsEvent)) == null )throw new NullPointerException("onFireAction no events for this event");
		
		Iterator<IComponentEventHandler> it = lListEventHandlers.iterator();
		
		while(it.hasNext()){
			IComponentEventHandler handlerEvent = (IComponentEventHandler) it.next();
			handlerEvent.onFireAction(type, mParent);
		}
	}
	@Override
	public boolean onTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {

		if (mParent instanceof IEntity){

			boolean managed = false;
			switch (pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
			case TouchEvent.ACTION_MOVE:
				if(hmEventHandlers != null){
					Log.i(TAG,"\t launch event handler trough object control");
					ArrayList<IComponentEventHandler> lstHandlerEvent = hmEventHandlers.get(ComponentEventHandlerDescriptor.Events.ON_MOVE);
					if(lstHandlerEvent != null){
						if(!on_move){
							on_move = true;
							lastTouchEvent.set(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
						}
						else{
							Iterator<IComponentEventHandler> it = lstHandlerEvent.iterator();
							lastTouchEvent.set(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
							while(it.hasNext()){
								IComponentEventHandler handlerEvent = (IComponentEventHandler) it.next();
								handlerEvent.handleEvent(mParent,pSceneTouchEvent,lastTouchEvent);
							}
						}
					}
				}
				break;
			case TouchEvent.ACTION_UP:
				if(on_move)
					on_move=false;
				if(hmEventHandlers != null){
					Log.i(TAG,"\t launch event handler trough object control");
					ArrayList<IComponentEventHandler> lstHandlerEvent  = hmEventHandlers.get(ComponentEventHandlerDescriptor.Events.ON_CLICK);
					if(lstHandlerEvent != null){
						Log.i(TAG,"\t launch event handler trough object found");
						Iterator<IComponentEventHandler> it = lstHandlerEvent.iterator();
						while(it.hasNext()){
							IComponentEventHandler handlerEvent = (IComponentEventHandler) it.next();
							handlerEvent.handleEvent(mParent,pSceneTouchEvent,null);
						}
						
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
	public void configure(BasicDescriptor pDsc) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public ArrayList<IComponentEventHandler>  getEventsHandler(Events theEvent) {
		return hmEventHandlers.get(theEvent);
	}
	@Override
	public void setActionSceneListner(IActionSceneListener scenelistener) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setOperationsHandler(IOperationHandler messageHandler){
		if(! (messageHandler instanceof IOperationHandler))
			throw new NullPointerException("the message handler is not right class type");
		pMessageHandler = messageHandler;
				
	}
	@Override
	public String getPersistenceURL() {
		String sPersistenceURL = null;

		IEntity pFather = this.getParent();
		if(pFather instanceof IManageableScene){
			sPersistenceURL = new String(((IManageableScene)pFather).getSceneName());
		}
		else{
			if(pFather instanceof IComponent){
				sPersistenceURL = new String(((IComponent)pFather).getPersistenceURL());
			}
			else
				throw new NullPointerException("Not Correct Hierarchy compnent is not attached to another component or a scene");
		}
		sPersistenceURL = sPersistenceURL.concat(new String("/" + this.getID()));
		return sPersistenceURL;
	}
}

