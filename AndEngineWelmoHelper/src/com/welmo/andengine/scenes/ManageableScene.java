package com.welmo.andengine.scenes;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.IBackground;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.managers.ResourcesManager.SoundType;
import com.welmo.andengine.managers.SceneManager;
import com.welmo.andengine.managers.SharedPreferenceManager;
import com.welmo.andengine.scenes.components.ClickableSprite;
import com.welmo.andengine.scenes.components.ComponentDefaultEventHandler;
import com.welmo.andengine.scenes.components.CompoundSprite;
import com.welmo.andengine.scenes.components.Stick;
import com.welmo.andengine.scenes.components.TextComponent;
import com.welmo.andengine.scenes.components.CardSprite.CardSide;
import com.welmo.andengine.scenes.components.buttons.ButtonSceneLauncher;
import com.welmo.andengine.scenes.components.interfaces.IActionSceneListener;
import com.welmo.andengine.scenes.components.interfaces.IActivitySceneListener;
import com.welmo.andengine.scenes.components.interfaces.IComponentClickable;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.components.interfaces.IComponentEventHandler;
import com.welmo.andengine.scenes.components.interfaces.IComponentLifeCycle;
import com.welmo.andengine.scenes.components.interfaces.IPersistent;
import com.welmo.andengine.scenes.components.puzzle.PuzzleSprites;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.SceneDescriptor;
import com.welmo.andengine.scenes.descriptors.components.BackGroundObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ButtonDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ButtonSceneLauncherDescriptor;
import com.welmo.andengine.scenes.descriptors.components.HUDDescriptor;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.TextObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.PuzzleObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.operations.IOperationHandler;
import com.welmo.andengine.scenes.operations.Operation;
import com.welmo.andengine.utility.SoundSequence;
import android.content.Context;
import android.content.SharedPreferences;
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
	

	protected SceneDescriptor 								pSCDescriptor;
	protected HashMap<Integer, IComponentEventHandler> 		hmEventHandlers;
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
		
		for (ComponentEventHandlerDescriptor ehDsc:pSCDescriptor.pGlobalEventHandlerList){
			ComponentDefaultEventHandler newEventHandler= new ComponentDefaultEventHandler();
			newEventHandler.setUpEventsHandler(ehDsc);
			this.hmEventHandlers.put(ehDsc.getID(), newEventHandler);
		}

		//load each components constituting the scene
		for(BasicDescriptor scObjDsc:pSCDescriptor.pChild.values()){
			loadComponent(scObjDsc, this);
		}
		
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

	@Override
	public String getSceneName() {
		// TODO Auto-generated method stub
		return this.pSCDescriptor.sceneName;
	}
	
	// ===========================================================
	// Load components
	// ===========================================================
	protected IEntity loadComponent2(BasicDescriptor scObjDsc, IEntity pEntityFather) {
		IEntity entity;
		if(scObjDsc instanceof BackGroundObjectDescriptor){
			//this.setBackground(createBackground((BackGroundObjectDescriptor)scObjDsc));
			//return newEntity;
			this.setBackground(((BackGroundObjectDescriptor)scObjDsc).createBackground(this.mEngine));
			return null;
		}
		else{
			//create the component
			IComponent newSceneComponent = scObjDsc.CreateComponentInstance(this.mEngine);
			
			newSceneComponent.setOperationsHandler(this);

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

			if(newSceneComponent instanceof IActionSceneListener)
				((IActionSceneListener)newSceneComponent).setIActionOnSceneListener(this);

			if(newSceneComponent instanceof IActivitySceneListener)
				((IActivitySceneListener)newSceneComponent).setIActivitySceneListener(pSM.getIActivitySceneListener());
			
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
			return (IEntity)newSceneComponent;
		}
	}
	
	protected IEntity loadComponent(BasicDescriptor scObjDsc, IEntity pEntityFather) {
		IEntity newEntity 				= null;
		IComponent newSceneComponent 	= null;
		
		if(scObjDsc instanceof BackGroundObjectDescriptor){
			loadComponent2(scObjDsc,pEntityFather);	
			return null;
		}
		if(scObjDsc instanceof SpriteObjectDescriptor){
			SpriteObjectDescriptor pSprtDsc = (SpriteObjectDescriptor)scObjDsc;
			switch(pSprtDsc.getType()){	
				case  STATIC:
					newEntity = createSprite(pSprtDsc);
					//newEntity =  loadComponent2(scObjDsc,pEntityFather);	
					break;
				case CLICKABLE: // Create the clickable sprite elements
					newEntity = loadComponent2(scObjDsc,this);	
					break;
				case COMPOUND_SPRITE:
					
					newEntity = loadComponent2(scObjDsc,this);	
					break;
				case ANIMATED: // Create the animated sprite elements
					newEntity = loadComponent2(scObjDsc,this);	
					break;
				default:
					break;
				}
				return newEntity;
		}
		if(scObjDsc instanceof TextObjectDescriptor){
			newEntity = loadComponent2(scObjDsc,this);	
		}
		if(scObjDsc instanceof PuzzleObjectDescriptor){
			newEntity = loadComponent2(scObjDsc,pEntityFather);
		}
		if(scObjDsc instanceof ButtonSceneLauncherDescriptor){
			newEntity = loadComponent2(scObjDsc,pEntityFather);
		}
		if(scObjDsc instanceof ButtonDescriptor){
			newEntity = loadComponent2(scObjDsc,pEntityFather);
		}
		//handle children
		if(newEntity != null)
			for(BasicDescriptor theChild:scObjDsc.pChild.values())
				loadComponent(theChild, newEntity);
		this.sortChildren();
		return newEntity;
	}
	// ===========================================================
	// Create components
	// ===========================================================

	// ===========================================================
	// Create component BackGround
	// ===========================================================
	protected IBackground createBackground(BackGroundObjectDescriptor pBkgDsc){
		
		switch(pBkgDsc.type){
		case COLOR:
			return new Background(pRM.getColor(pBkgDsc.color));
		case SPRITE:
			SpriteObjectDescriptor pSDsc = null;
			for (BasicDescriptor pObjDsc : pBkgDsc.pChild.values()){
				if(pObjDsc instanceof SpriteObjectDescriptor){
					pSDsc = (SpriteObjectDescriptor)pObjDsc;
					Sprite spriteBKG = new Sprite(0, 0, pSDsc.getIDimension().getWidth(), pSDsc.getIDimension().getHeight(), 
						pRM.getTextureRegion(pSDsc.getTextureName()), 
						this.mEngine.getVertexBufferObjectManager());
					return new SpriteBackground(spriteBKG);
				}
			}
			throw new NullPointerException("Invalid Sprite Backgound. Not found sprite descriptor for background");
		default:
			return null;
		}
	}
	// ===========================================================
	// Create component Static Sprite
	// ===========================================================
	protected IEntity createSprite(SpriteObjectDescriptor spDsc){
				
		final Sprite newSprite = new Sprite(spDsc.getIPosition().getX(), spDsc.getIPosition().getY(), 
				spDsc.getIDimension().getWidth(), spDsc.getIDimension().getHeight(), 
				pRM.getTextureRegion(spDsc.getTextureName()), 
				this.mEngine.getVertexBufferObjectManager());
		
		newSprite.setZIndex(spDsc.getIPosition().getZorder());
		mapOfObjects.put(spDsc.getID(), newSprite); 
		return newSprite;
	}
	
	
	protected IEntity createButtonSceneLauncher(ButtonSceneLauncherDescriptor spDsc,IEntity pEntityFather){
		
		ButtonSceneLauncher theButton= new ButtonSceneLauncher(spDsc, mEngine.getVertexBufferObjectManager());
		
		theButton.configure(spDsc);
		pEntityFather.attachChild(theButton);
		
		this.registerTouchArea(theButton);
		mapOfObjects.put(spDsc.getID(), theButton); 
		

		if(theButton instanceof IActionSceneListener)
			((IActionSceneListener)theButton).setIActionOnSceneListener(this);
		
		if(theButton instanceof IActivitySceneListener)
			((IActivitySceneListener)theButton).setIActivitySceneListener(pSM.getIActivitySceneListener());
		
		theButton.setID(spDsc.getID());
		//Create events handler

		Set<Entry<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor>> eventHandlersSet;
		eventHandlersSet = spDsc.pEventHandlerList.entrySet();

		for (Entry<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor> entry : eventHandlersSet){
			ComponentEventHandlerDescriptor eventHandler = entry.getValue();
			Events theEvent = entry.getKey();

			ComponentDefaultEventHandler oCmpDefEventHandler =  new ComponentDefaultEventHandler();
			if(eventHandler.cloneID!=-1){
				IComponentEventHandler eventHandlerToClone = hmEventHandlers.get(eventHandler.cloneID);
				if (eventHandlerToClone!=null){
					theButton.addEventsHandler(theEvent, eventHandlerToClone.cloneEvent(eventHandler));
				}
				else
					throw new NullPointerException("Invalid Event clone ID createClickableSprite [Clone ID = " + eventHandler.cloneID + " ]");
			}
			else{
				oCmpDefEventHandler.setUpEventsHandler(eventHandler);
				theButton.addEventsHandler(theEvent, oCmpDefEventHandler);
			}	
		}

		return theButton;
		
		
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
	
	public void initScene(SceneManager sceneManager, Engine theEngine, Context ctx, BaseGameActivity activity) {
		mEngine = theEngine;
		mContext = ctx;
		pSM = sceneManager;
		pSPM = SharedPreferenceManager.getInstance(ctx);
		
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
	
}
