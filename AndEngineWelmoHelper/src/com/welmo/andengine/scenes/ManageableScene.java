package com.welmo.andengine.scenes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.managers.ResourcesManager.SoundType;
import com.welmo.andengine.managers.SceneManager;
import com.welmo.andengine.managers.SharedPreferenceManager;
import com.welmo.andengine.scenes.components.ComponentDefaultEventHandler;
import com.welmo.andengine.scenes.components.PositionHelper;
import com.welmo.andengine.scenes.components.Stick;
import com.welmo.andengine.scenes.components.CardSprite.CardSide;
import com.welmo.andengine.scenes.components.interfaces.IActionSceneListener;
import com.welmo.andengine.scenes.components.interfaces.IActivitySceneListener;
import com.welmo.andengine.scenes.components.interfaces.IComponentClickable;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.components.interfaces.IComponentEventHandler;
import com.welmo.andengine.scenes.components.interfaces.IComponentLifeCycle;
import com.welmo.andengine.scenes.components.interfaces.IPersistent;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.SceneDescriptor;
import com.welmo.andengine.scenes.descriptors.components.BackGroundObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.BasicComponentDescriptor;
import com.welmo.andengine.scenes.descriptors.components.HUDDescriptor;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.operations.IOperationHandler;
import com.welmo.andengine.scenes.operations.Operation;
import com.welmo.andengine.utility.SoundSequence;
import android.content.Context;
import android.util.Log;


public class ManageableScene extends Scene implements IManageableScene, IActionSceneListener, IOperationHandler {
	//------------------------------------------------------------------------------------------
	// Variables
	//------------------------------------------------------------------------------------------
	private static final String 							TAG  = "ManageableScene";
	public static enum soundtype {SOUND, PAUSE, PARAMETRABLE_SOUND};
	
	
	protected Engine 										mEngine;
	protected Context 										mContext;
	protected ResourcesManager								pRM;
	protected SceneManager									pSM;
	protected SharedPreferenceManager						pSPM;
	protected HashMap<Integer, IAreaShape> 					mapOfObjects;
	protected HashMap<String, SoundSequence> 				mapOfSoundSequences;
	protected boolean										bImplementPinchAndZoom;
	protected boolean										bHasHUD;
	protected HUDDescriptor									pHUDDsc;
	protected IActivitySceneListener						pIActivitySceneListener = null;
	

	protected SceneDescriptor 								pSCDescriptor;
	
	public SceneDescriptor getpSCDescriptor() {
		return pSCDescriptor;
	}
	public void setpSCDescriptor(SceneDescriptor pSCDescriptor) {
		this.pSCDescriptor = pSCDescriptor;
	}
	protected HashMap<Integer, IComponentEventHandler> 		hmEventHandlers;
	protected HashMap<Integer, IComponentEventHandler> 		hmOnStartEventHandlers;
	
	protected IOperationHandler 							hdFatherSceneMessageHandler = null;
	protected int											nLockTouch=0;
	
	// ===========================================================================================
	// Constructor
	// ===========================================================================================
	public ManageableScene(){
		//Initialize pointer to resource manager and object map
		pRM = ResourcesManager.getInstance();
		mapOfObjects = new HashMap<Integer, IAreaShape>();
		hmEventHandlers = new HashMap<Integer, IComponentEventHandler>();
		hmOnStartEventHandlers =  new HashMap<Integer, IComponentEventHandler>();
		mapOfSoundSequences = new HashMap<String, SoundSequence>(); 
		bImplementPinchAndZoom = false;
	}
	// ===========================================================================================
	// Interfaces & Superclass implementation & overloading
	// ===========================================================================================
	// ===========================================================================================
	// Interface IManageableScene
	// ===========================================================================================
	@Override
	public void refreshPersistentComponents(SharedPreferenceManager pSPM) {
	    for (Object value : mapOfObjects.values()) {
	    	if(value instanceof IPersistent){
				((IPersistent)value).doLoad(pSPM);
			}
	    }
	}
	@Override
	public void loadScene(SceneDescriptor sceneDescriptor) {
		pSCDescriptor = sceneDescriptor;

		//Init default value by reading configuration
		bImplementPinchAndZoom 	= sceneDescriptor.isPinchAndZoom();
		bHasHUD					= sceneDescriptor.hasHUD();
		pHUDDsc					= sceneDescriptor.getHUDDsc();
		
		//load each event handrel for the scene
		for (ComponentEventHandlerDescriptor ehDsc:pSCDescriptor.pGlobalEventHandlerList){
					ComponentDefaultEventHandler newEventHandler= new ComponentDefaultEventHandler();
					newEventHandler.setUpEventsHandler(ehDsc);
					this.hmEventHandlers.put(ehDsc.getID(), newEventHandler);
		}

		//load each components constituting the scene id descriptor is instance of basiccomponentdescriptor
		for(BasicDescriptor scObjDsc:pSCDescriptor.pChild.values()){
			if (scObjDsc instanceof BasicComponentDescriptor){
				loadComponent2((BasicComponentDescriptor) scObjDsc, this);
			}
		}
		this.sortChildren();
		
		loadScenePhrases(pSCDescriptor);
	}
	public void loadScenePhrases(SceneDescriptor sceneDescriptor) {
		HashMap<String, String[]> phrases = sceneDescriptor.getPhrasesMap();
		
		ResourcesManager rMgr = ResourcesManager.getInstance();
		
		Iterator<Entry<String, String[]>> it = phrases.entrySet().iterator();
	    while (it.hasNext()){
	    	Entry<String, String[]> entry = it.next();
	    	String key = entry.getKey();
	    	String[] config = entry.getValue();
	    	//count nb of parameters
	    	
	    	//create array of sound
	    	SoundSequence phraseSounds = new SoundSequence(config.length);
	    	for(int indexX=0; indexX < config.length; indexX++){
	    		if(config[indexX].startsWith("#")){
	    			phraseSounds.getSequence()[indexX] = rMgr.new SoundContainer();
	    			phraseSounds.getSequence()[indexX].setType(SoundType.PAUSE);
	    			phraseSounds.getSequence()[indexX].setDuration(Integer.parseInt(config[indexX].substring(1)));
	    		}
	    		else if (config[indexX].startsWith("%")){
	    			phraseSounds.addParameter(indexX);
	    		}
	    		else{
	    			phraseSounds.getSequence()[indexX] = rMgr.getSound(config[indexX]);
	    		}	
	    	}
	    	mapOfSoundSequences.put(key,phraseSounds);
	    }
	}

	public String getSceneName() {
		// TODO Auto-generated method stub
		return this.pSCDescriptor.sceneName;
	}
	public String getLicenceID() {
		// TODO Auto-generated method stub
		return this.pSCDescriptor.sceneLicenceID;
	}
	
	// ===========================================================
	// Load components
	// ===========================================================
	protected IEntity loadComponent2(BasicComponentDescriptor scObjDsc, IEntity pEntityFather) {
		IComponent newSceneComponent = null;
		if (!(scObjDsc instanceof BasicComponentDescriptor)){
			return null;
		}	
		if(scObjDsc instanceof BackGroundObjectDescriptor){
			this.setBackground(((BackGroundObjectDescriptor)scObjDsc).createBackground(this.mEngine));
			return null;
		}
		else{
			//create the component
			newSceneComponent = scObjDsc.CreateComponentInstance(this.mEngine);
			if(newSceneComponent == null)
				return null;
			
			newSceneComponent.setOperationsHandler(this);

			//Set-up listeners MUST be done just after the creation to ensure that if listners are used in the cofiguration listners are available
			if(newSceneComponent instanceof IActionSceneListener)
				((IActionSceneListener)newSceneComponent).setIActionOnSceneListener(this);

			if(newSceneComponent instanceof IActivitySceneListener)
				((IActivitySceneListener)newSceneComponent).setIActivitySceneListener(pSM.getIActivitySceneListener());
			
			
			//Configured the new component and attach it to the father
			newSceneComponent.configure(scObjDsc);
			pEntityFather.attachChild((IEntity)newSceneComponent);

			//if component is a touchable one register the touch area
			if(scObjDsc.isTouchable()){
				this.registerTouchArea((ITouchArea) newSceneComponent);
				mapOfObjects.put(scObjDsc.getID(), (IAreaShape) newSceneComponent);
			}
			
			//if component is a persistent one read the configuration paramters from the sharedpreference file
			if(newSceneComponent instanceof IPersistent){
				((IPersistent)newSceneComponent).doLoad(pSPM);
			}

			// if entity fater type of shape position the object horizzontaly and vertically
			if(pEntityFather instanceof IAreaShape & newSceneComponent instanceof IAreaShape){
				PositionHelper.align(scObjDsc.getIPosition(), (IAreaShape) newSceneComponent, (IAreaShape) pEntityFather);
			}

			
			//Create events handler
			if(!scObjDsc.pEventHandlerList.isEmpty()){

				Set<Entry<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor>> eventHandlersSet;
				eventHandlersSet = scObjDsc.pEventHandlerList.entrySet();

				if(newSceneComponent instanceof IComponentClickable){
					IComponentClickable clicableComponent = (IComponentClickable)newSceneComponent;

					for (Entry<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor> entry : eventHandlersSet){
						ComponentEventHandlerDescriptor eventHandler = entry.getValue();
						Events theEvent = entry.getKey();

						ComponentDefaultEventHandler oCmpDefEventHandler =  new ComponentDefaultEventHandler();
						if(eventHandler.cloneID!=-1){
							IComponentEventHandler eventHandlerToClone = hmEventHandlers.get(eventHandler.cloneID);
							if (eventHandlerToClone!=null){
								clicableComponent.addEventsHandler(theEvent, eventHandlerToClone.cloneEvent(eventHandler));
							}
							else
								throw new NullPointerException("Invalid Event clone ID createClickableSprite [Clone ID = " + eventHandler.cloneID + " ]");
						}
						else{
							oCmpDefEventHandler.setUpEventsHandler(eventHandler);
							clicableComponent.addEventsHandler(theEvent, oCmpDefEventHandler);
						}	
					}
				}
			}
			//handle children
			if(newSceneComponent != null)
				for(BasicDescriptor theChild:scObjDsc.pChild.values())
					loadComponent2((BasicComponentDescriptor) theChild, (IEntity)newSceneComponent);
			((IEntity)newSceneComponent).sortChildren(true);
			return (IEntity)newSceneComponent;
		}
	}
	
	// ===========================================================
	// Create component Animated Sprite
	// ===========================================================
	protected IEntity createAnimatedSprite(SpriteObjectDescriptor spDsc){
		final AnimatedSprite animatedObject = new AnimatedSprite(100,100, 
				pRM.getTiledTextureRegion(spDsc.getTextureName()), 
				this.mEngine.getVertexBufferObjectManager());

		animatedObject.animate(100);
		return animatedObject;
	}
	
	public void initScene(SceneManager sceneManager, Engine theEngine, Context ctx, IActivitySceneListener activity) {
		mEngine = theEngine;
		mContext = ctx;
		pSM = sceneManager;
		pSPM = SharedPreferenceManager.getInstance(ctx);
		pIActivitySceneListener = activity;
		
		this.setTouchAreaBindingOnActionDownEnabled(true);
		this.setTouchAreaBindingOnActionMoveEnabled(true);
	}
	
	public void resetScene(){	
		//reset values
		for(Integer ID  : mapOfObjects.keySet()){
			IAreaShape theObject = mapOfObjects.get(ID);
			if(theObject instanceof IComponentLifeCycle)
				((IComponentLifeCycle)theObject).reset();
		}
		nLockTouch = 0;
	}	
	protected void ClearScene(){
		
		//Detach all scene's child
		for (int i=this.getChildCount() - 1;  i >= 0; i--){
			IEntity tmpEntity = this.getChildByIndex(i);
			if(tmpEntity instanceof IAreaShape )
				this.unregisterTouchArea((IAreaShape)tmpEntity);
			if(tmpEntity != null)
				this.detachChild(tmpEntity);
		}
		//clear event handler
		this.hmEventHandlers.clear();
	}
	// ===========================================================
	// Dummy Class implementing IClickLeastener of ClicableSprite object
	// ===========================================================
	// Scene methods override
	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		Log.i(TAG,"Touch " + nLockTouch);
		if(nLockTouch > 0){
			return Boolean.TRUE;
		}
		else
			return super.onSceneTouchEvent(pSceneTouchEvent);
	}
	@Override
	public boolean onFireEvent(Events pEvent){
		switch(pEvent){
		case NO_EVENT:
		case ON_MOVE:
		case ON_CLICK:
			break;
		case ON_SCENE_LAUNCH:
			IAreaShape theComponent = null;
			Iterator<IAreaShape> itEvent = this.mapOfObjects.values().iterator();
			while (itEvent.hasNext()){
				if((theComponent = itEvent.next()) instanceof IComponentClickable)
					((IComponentClickable)theComponent).onFireEvent(pEvent);
			}
			break;
		}

		return false;
	}
	@SuppressWarnings("incomplete-switch")
	@Override
	public void onStick(IAreaShape currentShapeToStick,
			SceneActions stickActionDescription) {
		IAreaShape shapeToStickWith = mapOfObjects.get(stickActionDescription.stick_with);
		// TO DO calculation distance must be from border and not from center & value must be a parameter
		if (shapeToStickWith != null){
			if(Stick.isStickOn(currentShapeToStick, shapeToStickWith, 150)){
				switch(stickActionDescription.stickMode){
				case STICK_LEFTH: Stick.lefth(currentShapeToStick, shapeToStickWith);break;
				case STICK_RIGHT: Stick.right(currentShapeToStick, shapeToStickWith);break;
				case STICK_UP: Stick.up(currentShapeToStick, shapeToStickWith);break;
				case STICK_BOTTOM: Stick.bottom(currentShapeToStick, shapeToStickWith);break;
				case STICK_UP_LEFTH: Stick.upLefth(currentShapeToStick, shapeToStickWith);break;
				case STICK_UP_RIGHT: Stick.upRight(currentShapeToStick, shapeToStickWith);break;
				case STICK_BOTTOM_LEFTH: Stick.bottomLefth(currentShapeToStick, shapeToStickWith);break;
				case STICK_BOTTOM_RIGHT: Stick.bottomRight(currentShapeToStick, shapeToStickWith);break;
				}
			}
		}
	}
	@Override
	public String getFatherScene() {
		return this.pSCDescriptor.getSceneFather();
	}
	@Override
	public void setFatherScene(String sceneName) {
		this.pSCDescriptor.sceneFather=new String(sceneName);
	}
	
	@Override
	public void onFlipCard(int CardID, CardSide currentSide) {
	}	
	@Override
	public void lockTouch(){
		nLockTouch++;
		Log.i(TAG,"\t locked Touch = " + nLockTouch);
	}
	@Override
	public void unLockTouch() {
		nLockTouch--;
		
		if(nLockTouch < 0)
			nLockTouch=0;
		Log.i(TAG,"\t unLocked Touch = " + nLockTouch);
	}
	public boolean hasPinchAndZoomActive() {
		return this.bImplementPinchAndZoom;
	}
	@Override
	public void setIActionOnSceneListener(IActionSceneListener pListener) {
		// TODO Auto-generated method stub
		
	}
	public boolean hasHUD() {
		return bHasHUD;
	}
	public HUDDescriptor getHUDDsc() {
		return pHUDDsc;
	}
	public void setHUDDsc(HUDDescriptor pHUDDsc) {
		this.pHUDDsc = pHUDDsc;
	}
	@Override
	public void setFatherSceneMessageHandler(IOperationHandler pMgsHnd) {
		hdFatherSceneMessageHandler = pMgsHnd;
	}
	@Override
	public void onResult(int result) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void doOperation(Operation msg) {
		if(hdFatherSceneMessageHandler != null)
			hdFatherSceneMessageHandler.doOperation(msg);
	}
	@Override
	public void undoOperation(Operation msg) {
		if(hdFatherSceneMessageHandler != null)
			hdFatherSceneMessageHandler.doOperation(msg);
	}
	@Override
	public void onResult(int i, int j, String string) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean checkLicence(String sLicence) {
		// TODO Auto-generated method stub
		return false;
	}
}
