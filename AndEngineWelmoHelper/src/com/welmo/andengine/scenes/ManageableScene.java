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
import com.welmo.andengine.scenes.components.ClickableSprite;
import com.welmo.andengine.scenes.components.ComponentDefaultEventHandler;
import com.welmo.andengine.scenes.components.CompoundSprite;
import com.welmo.andengine.scenes.components.Stick;
import com.welmo.andengine.scenes.components.TextComponent;
import com.welmo.andengine.scenes.components.CardSprite.CardSide;
import com.welmo.andengine.scenes.components.buttons.ButtonSceneLauncher;
import com.welmo.andengine.scenes.components.interfaces.IActionOnSceneListener;
import com.welmo.andengine.scenes.components.interfaces.IActivitySceneListener;
import com.welmo.andengine.scenes.components.interfaces.IComponentClickable;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.components.interfaces.IComponentEventHandler;
import com.welmo.andengine.scenes.components.interfaces.IComponentLifeCycle;
import com.welmo.andengine.scenes.components.puzzle.PuzzleSprites;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.SceneDescriptor;
import com.welmo.andengine.scenes.descriptors.components.BackGroundObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ButtonSceneLauncherDescriptor;
import com.welmo.andengine.scenes.descriptors.components.HUDDescriptor;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.TextObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.PuzzleObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.operations.IOperationHandler;
import com.welmo.andengine.utility.SoundSequence;















import android.content.Context;
import android.util.Log;


public class ManageableScene extends Scene implements IManageableScene, IActionOnSceneListener{
	//------------------------------------------------------------------------------------------
	// Variables
	//------------------------------------------------------------------------------------------
	private static final String 							TAG  = "ManageableScene";
	public static enum soundtype {SOUND, PAUSE, PARAMETRABLE_SOUND};
	
	
	protected Engine 										mEngine;
	protected Context 										mContext;
	protected ResourcesManager								pRM;
	protected SceneManager									pSM;
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
	
	// ===========================================================
	// Load components
	// ===========================================================
	protected IEntity loadComponent(BasicDescriptor scObjDsc, IEntity pEntityFather) {
		IEntity newEntity = null;
		
		
		if(scObjDsc instanceof BackGroundObjectDescriptor){
			this.setBackground(createBackground((BackGroundObjectDescriptor)scObjDsc));
			return newEntity;
		}
		/*
		if(scObjDsc instanceof SpriteObjectDescriptor){
			SpriteObjectDescriptor pSprtDsc = (SpriteObjectDescriptor)scObjDsc;
			switch(pSprtDsc.getType()){	
			case  STATIC:
				newEntity = createSprite(pSprtDsc);
				break;
			case CLICKABLE: // Create the clickable sprite elements
				newEntity = createClickableSprite(pSprtDsc);
				break;
			case COMPOUND_SPRITE:
				newEntity = createCompoundSprite(pSprtDsc);
				break;
			case ANIMATED: // Create the animated sprite elements
				newEntity = createAnimatedSprite(pSprtDsc);
				break;
			default:
				break;
			}
			if(newEntity != null)pEntityFather.attachChild(newEntity);
		}*/
		if(scObjDsc instanceof SpriteObjectDescriptor){
			SpriteObjectDescriptor pSprtDsc = (SpriteObjectDescriptor)scObjDsc;
			switch(pSprtDsc.getType()){	
				case  STATIC:
					newEntity = createSprite(pSprtDsc);
					break;
				case CLICKABLE: // Create the clickable sprite elements
					IComponent newSceneComponen = pSprtDsc.CreateComponentInstance(this.mEngine);

					this.registerTouchArea((IAreaShape) newSceneComponen);
					mapOfObjects.put(pSprtDsc.getID(), (IAreaShape)newSceneComponen); 
					
					if(newSceneComponen instanceof IActionOnSceneListener)
						((IActionOnSceneListener)newSceneComponen).setIActionOnSceneListener(this);
					
					if(newSceneComponen instanceof IActivitySceneListener)
						((IActivitySceneListener)newSceneComponen).setIActivitySceneListener(pSM.getIActivitySceneListener());
					
					newSceneComponen.setID(pSprtDsc.getID());
					//Create events handler

					Set<Entry<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor>> eventHandlersSet;
					eventHandlersSet = pSprtDsc.pEventHandlerList.entrySet();

					for (Entry<ComponentEventHandlerDescriptor.Events,ComponentEventHandlerDescriptor> entry : eventHandlersSet){
						ComponentEventHandlerDescriptor eventHandler = entry.getValue();
						Events theEvent = entry.getKey();

						ComponentDefaultEventHandler oCmpDefEventHandler =  new ComponentDefaultEventHandler();
						if(eventHandler.cloneID!=-1){
							IComponentEventHandler eventHandlerToClone = hmEventHandlers.get(eventHandler.cloneID);
							if (eventHandlerToClone!=null){
								((IComponentClickable)newSceneComponen).addEventsHandler(theEvent, eventHandlerToClone.cloneEvent(eventHandler));
							}
							else
								throw new NullPointerException("Invalid Event clone ID createClickableSprite [Clone ID = " + eventHandler.cloneID + " ]");
						}
						else{
							oCmpDefEventHandler.setUpEventsHandler(eventHandler);
							((IComponentClickable)newSceneComponen).addEventsHandler(theEvent, oCmpDefEventHandler);
						}	
					}

					newEntity = (IEntity) newSceneComponen;
					 
					//above codes replace newEntity = createClickableSprite(pSprtDsc);
					break;
				case COMPOUND_SPRITE:
					//FT replaced with next code => newEntity = createCompoundSprite(pSprtDsc);
					newSceneComponen = pSprtDsc.CreateComponentInstance(this.mEngine);
					this.registerTouchArea((IAreaShape)newSceneComponen);
					mapOfObjects.put(pSprtDsc.getID(), (IAreaShape)newSceneComponen); 
					newEntity = (IEntity) newSceneComponen;
					break;
				case ANIMATED: // Create the animated sprite elements
					newEntity = createAnimatedSprite(pSprtDsc);
					break;
				default:
					break;
				}
				if(newEntity != null)pEntityFather.attachChild(newEntity);
		}
		if(scObjDsc instanceof TextObjectDescriptor){
			//newEntity = createText((TextObjectDescriptor)scObjDsc,pEntityFather);
			
			IComponent newSceneComponent = scObjDsc.CreateComponentInstance(this.mEngine);
			
			newSceneComponent.build(scObjDsc);
			
			pEntityFather.attachChild((IEntity)newSceneComponent);
			
			/*IEntity newEntity = null;
			switch(spTxtDsc.getType()){	
			case SIMPLE: //TODO add to text description Text Option with default value
				if(pEntityFather instanceof IAreaShape)
					newEntity = new TextComponent(spTxtDsc, pRM, mEngine, (IAreaShape)pEntityFather);
				else
					newEntity = new TextComponent(spTxtDsc, pRM, mEngine, null);
				//pEntityFather.attachChild(newEntity);
			//	break;
			//default:
			//	break;
			//}
			return newEntity;*/
			
		}
		if(scObjDsc instanceof PuzzleObjectDescriptor){
			// FT newEntity = createPuzzle((PuzzleObjectDescriptor)scObjDsc,pEntityFather);
			IComponent newSceneComponent = scObjDsc.CreateComponentInstance(this.mEngine);
			
			newSceneComponent.build(scObjDsc);
			
			pEntityFather.attachChild((IEntity)newSceneComponent);
			
			this.registerTouchArea((ITouchArea) newSceneComponent);
			
			mapOfObjects.put(scObjDsc.getID(), (IAreaShape) newSceneComponent); 
		}
		if(scObjDsc instanceof ButtonSceneLauncherDescriptor){
			newEntity = createButtonSceneLauncher((ButtonSceneLauncherDescriptor)scObjDsc,pEntityFather);
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
		theButton.build(spDsc);
		pEntityFather.attachChild(theButton);
		
		this.registerTouchArea(theButton);
		mapOfObjects.put(spDsc.getID(), theButton); 
		

		if(theButton instanceof IActionOnSceneListener)
			((IActionOnSceneListener)theButton).setIActionOnSceneListener(this);
		
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
	// Create component Clickable Sprite
	// ===========================================================
	/*protected IEntity createClickableSprite(SpriteObjectDescriptor spDsc) {
			
		IComponentClickable newClickableSprite = null;
		String className = spDsc.getClassName();
		try {
			if(!className.equals("")){
				// Get the class of className
				Class<?> classe = Class.forName (className);
				
				// Get the constructor
				Constructor<?> constructor = 
						classe.getConstructor (new Class [] {Class.forName ("com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor"),
								Class.forName ("com.welmo.andengine.managers.ResourcesManager"),
								Class.forName ("org.andengine.engine.Engine")});
				
	
				newClickableSprite = (IComponentClickable) constructor.newInstance (new Object [] {spDsc,pRM,mEngine});
		}
			else newClickableSprite = (IComponentClickable) new ClickableSprite (spDsc,pRM,mEngine);
				
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (NoSuchMethodException e){
			e.printStackTrace();
		}catch (java.lang.reflect.InvocationTargetException e){
			e.printStackTrace();
		}catch (IllegalArgumentException e){
			e.printStackTrace();
		}
		
		
		this.registerTouchArea((IAreaShape) newClickableSprite);
		mapOfObjects.put(spDsc.getID(), (IAreaShape)newClickableSprite); 
		
		if(newClickableSprite instanceof IActionOnSceneListener)
			((IActionOnSceneListener)newClickableSprite).setIActionOnSceneListener(this);
		
		if(newClickableSprite instanceof IActivitySceneListener)
			((IActivitySceneListener)newClickableSprite).setIActivitySceneListener(pSM.getIActivitySceneListener());
		
		newClickableSprite.setID(spDsc.getID());
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
						newClickableSprite.addEventsHandler(theEvent, eventHandlerToClone.cloneEvent(eventHandler));
				}
				else
					throw new NullPointerException("Invalid Event clone ID createClickableSprite [Clone ID = " + eventHandler.cloneID + " ]");
			}
			else{
				oCmpDefEventHandler.setUpEventsHandler(eventHandler);
				newClickableSprite.addEventsHandler(theEvent, oCmpDefEventHandler);
			}	
		}

		return (IEntity) newClickableSprite;
	}*/
	// ===========================================================
	// Create component Animated Text
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
	public void setIActionOnSceneListener(IActionOnSceneListener pListener) {
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
}
