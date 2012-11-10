package com.welmo.andengine.scenes.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.andengine.audio.sound.Sound;
import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;

import android.util.Log;

import com.welmo.andengine.managers.EventDescriptionsManager;
import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentModifierDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.descriptors.events.ExecutionOrder;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;
import com.welmo.andengine.utility.MLOG;


public class ClickableSprite extends Sprite implements IEntityModifierListener,  IActionOnSceneListener{
	// ===========================================================
	// Constants
	// ===========================================================
	//Log & Debug
	private static final String TAG = "ClickableSprite";
	// ===========================================================
	// Fields
	// ===========================================================
	private IActionOnSceneListener   mActionListener		=null;
	private int nID											=-1;
	private EventDescriptionsManager pEDMgr					=null;
	private Object					 pDescriptor			=null;
	//Fields to manage modifiers & actions
	HashMap<Events,IEntityModifier>	 hmEntityModifiers		=null;	
	HashMap<Events,SceneActions>	 hmPreModifierAction	=null;
	HashMap<Events,SceneActions>	 hmPostModifierAction	=null;
	HashMap<Events,SceneActions>	 hmOnModifierAction		=null;
	HashMap<Events,ComponentDefaultEventHandler> hmEventHandlers = null;
	
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public ClickableSprite(SpriteObjectDescriptor pSPRDscf, ResourcesManager pRM, Engine theEngine){
		super(pSPRDscf.getIPosition().getX(), pSPRDscf.getIPosition().getY(), 
				pSPRDscf.getIDimension().getWidth(), pSPRDscf.getIDimension().getHeight(), 
				pRM.getTextureRegion(pSPRDscf.getTextureName()), 
				theEngine.getVertexBufferObjectManager());
		init();
		configure(pSPRDscf); 
	}
	public ClickableSprite(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		init(); 		
	}
	// ===========================================================
	// private member function
	// ===========================================================	
	private void init(){
		pEDMgr 					= EventDescriptionsManager.getInstance();
		hmEntityModifiers 		= new HashMap<Events,IEntityModifier>();	
		hmPreModifierAction		= new HashMap<Events,SceneActions>();
		hmPostModifierAction	= new HashMap<Events,SceneActions>();
		hmOnModifierAction		= new HashMap<Events,SceneActions>();
		hmEventHandlers			= new HashMap<Events,ComponentDefaultEventHandler>();
	}
	// ===========================================================
	// public member function
	// ===========================================================	
	
	public void configure(SpriteObjectDescriptor spDsc){
		ResourcesManager pRM = ResourcesManager.getInstance();
		setID(spDsc.getID());
		setPDescriptor(spDsc);
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
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		
		boolean managed = false;
		List<SceneActions> pActionList = null;
		List<ComponentEventHandlerDescriptor> pModifierList = null;
		
		switch (pSceneTouchEvent.getAction()) {
		case TouchEvent.ACTION_DOWN:
			if (MLOG.LOG)Log.i(TAG,"onAreaTouched ACTION_DOWN = " + nID);
			break;
		case TouchEvent.ACTION_MOVE:
			if (MLOG.LOG)Log.i(TAG,"onAreaTouched ACTION_MOVE = " + nID);
			pModifierList = pEDMgr.getModifierList(ComponentEventHandlerDescriptor.Events.ON_MOVE,this.getPDescriptor());
			if (pModifierList != null){
			}
			if(mActionListener != null){
				pActionList = pEDMgr.getActionList(ComponentEventHandlerDescriptor.Events.ON_MOVE,this.getPDescriptor());
				if (pActionList != null){
					for (SceneActions act: pActionList) {
						switch(act.type){
						case STICK:
							mActionListener.onStick(this, act);
							managed = true;
							break;
						default:
							break;
						}
					}
				}
			}
			break;
		case TouchEvent.ACTION_UP:
			if (MLOG.LOG)Log.i(TAG,"onAreaTouched ACTION_UP= " + nID);
			// [FT] mClickListener.onClick(this.nID);
			if(mActionListener != null){
				pActionList = pEDMgr.getActionList(ComponentEventHandlerDescriptor.Events.ON_CLICK,this.getPDescriptor());
				if (pActionList != null){
					for (SceneActions act: pActionList) {
						switch(act.type){
						case CHANGE_SCENE:
							mActionListener.onActionChangeScene(act.NextScene);
							managed = true;
							break;
						default:
							break;
						}
					}
				}
			}
			if(hmEntityModifiers != null){
				IEntityModifier mEntityModifier = hmEntityModifiers.get(ComponentEventHandlerDescriptor.Events.ON_CLICK);
				if(mEntityModifier != null){
					mEntityModifier.reset();
					this.registerEntityModifier(mEntityModifier);
				}
				SceneActions scAction;
				if((scAction = hmOnModifierAction.get(ComponentEventHandlerDescriptor.Events.ON_CLICK)) != null)
					Execute(scAction);
			}
			if(hmEventHandlers != null){
				Log.i(TAG,"\t launch event handler trough object control");
				ComponentDefaultEventHandler handlerEvent = hmEventHandlers.get(ComponentEventHandlerDescriptor.Events.ON_CLICK);
				if(handlerEvent != null){
					Log.i(TAG,"\t launch event handler trough object found");
					//handlerEvent.modifierSet.reset();
					//this.registerEntityModifier(handlerEvent.modifierSet);
					handlerEvent.handleEvent(this);
				}
			}
			break;
		}
		return managed;
	}
	public void Execute(SceneActions scAction){
		ResourcesManager rMgr = ResourcesManager.getInstance();
		switch(scAction.type){
		case PLAY_SOUND:
			Log.i(TAG,"\t PLAY_SOUND");
			Sound snd = rMgr.getSound(scAction.resourceName);
			snd.setVolume(1000);
			snd.play();
		}
	}
	public int getID() {
		return nID;
	}
	public void setID(int ID) {
		this.nID = ID;
	}
	public static interface  IClickLeastener{
		public void onClick(int ObjectID); // Sprite call this interface to inform parent that has been clicked
	}
	public void setActionOnSceneListener(IActionOnSceneListener actionLeastner) {
		this.mActionListener=actionLeastner;
	}
	public IActionOnSceneListener getActionOnSceneListener(){
		return mActionListener;
	}

	public Object getPDescriptor() {
		return pDescriptor;
	}
	public void setPDescriptor(Object pDescriptor) {
		this.pDescriptor = pDescriptor;
	}
	public void setEventsHandler(HashMap<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor> pEventHandlerDescList){
		Set<Entry<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor>> eventHandlersSet;
		eventHandlersSet = pEventHandlerDescList.entrySet();
		for (Entry<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor> entry : eventHandlersSet){
			ComponentEventHandlerDescriptor eventHandler = entry.getValue();
			Events theEvent = entry.getKey();
			//Setup Modifier if defined
			if(eventHandler.modifierSet != null){
				IEntityModifier modifers[];
				int index = 0;
				modifers = new IEntityModifier[eventHandler.modifierSet.getIModifierList().getModifiers().size()];
				for(ComponentModifierDescriptor m: eventHandler.modifierSet.getIModifierList().getModifiers()){
					IEntityModifier modifier;
					switch(m.getIModifier().getType()){
					case SCALE:
						modifier = new ScaleModifier(1,m.getIModifier().getScaleBegin(),m.getIModifier().getScaleEnd(),new IEntityModifierListener(){
							@Override
					        public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
								Log.i(TAG,"\t onModifierStarted A");
					        }
					        @Override
					        public void onModifierFinished(IModifier<IEntity> pModifier, final IEntity pItem) {
					        	Log.i(TAG,"\t onModifierFinished A");
					        }
						});
						modifers[index++] = modifier;
						break;
					default:
						break;
					}
				}
				IEntityModifier modifierset; 
				if(eventHandler.modifierSet.getIModifierList().getExecOrder() == ExecutionOrder.SERIAL){
					modifierset = new SequenceEntityModifier(new IEntityModifierListener(){
						@Override 
				        public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
							Log.i(TAG,"\t onModifierStarted B");
				        }
						@Override
				        public void onModifierFinished(IModifier<IEntity> pModifier, final IEntity pItem) {
				        	Log.i(TAG,"\t onModifierFinished B");
				        }
					}
				 ,modifers);
				}
				else
					modifierset = new ParallelEntityModifier((IEntityModifierListener)this,modifers);
				this.hmEntityModifiers.put(theEvent, modifierset);
			}
			if(eventHandler.postModAction != null)
				hmPostModifierAction.put(theEvent, eventHandler.postModAction);

			if(eventHandler.preModAction != null)
				hmPreModifierAction.put(theEvent, eventHandler.preModAction);

			if(eventHandler.onModAction != null)
				hmOnModifierAction.put(theEvent, eventHandler.onModAction);
		}
	}
	
	public void addEventsHandler(HashMap<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor> pEventHandlerDescList){
		Set<Entry<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor>> eventHandlersSet;
		eventHandlersSet = pEventHandlerDescList.entrySet();
	
		for (Entry<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor> entry : eventHandlersSet){
			ComponentEventHandlerDescriptor eventHandler = entry.getValue();
			Events theEvent = entry.getKey();
			
			ComponentDefaultEventHandler oCmpDefEventHandler =  new ComponentDefaultEventHandler();
			oCmpDefEventHandler.setEventsHandler(eventHandler);
			
			hmEventHandlers.put(theEvent, oCmpDefEventHandler);
		}
	}
	@Override
	public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
		Log.i(TAG,"\t onModifierStarted C");
	}
	@Override
	public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
		Log.i(TAG,"\t onModifierFinished C");
	}
	@Override
	public boolean onActionChangeScene(String nextSceneName) {
		// TODO Auto-generated method stub
		return this.mActionListener.onActionChangeScene(nextSceneName);
	}
	@Override
	public void onStick(IAreaShape currentShapeToStick,
			SceneActions stickActionDescription) {
		this.mActionListener.onStick(currentShapeToStick, stickActionDescription);
		
	}
}