package com.welmo.andengine.scenes.components;

import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.audio.music.Music;
import org.andengine.audio.sound.Sound;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.CardinalSplineMoveModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.modifier.CardinalSplineMoveModifier.CardinalSplineMoveModifierConfig;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.modifier.IModifier;

import android.util.Log;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.components.interfaces.IActionSceneListener;
import com.welmo.andengine.scenes.components.interfaces.IActivitySceneListener;
import com.welmo.andengine.scenes.components.interfaces.IComponentEventHandler;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentModifierDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentModifierListDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ExecutionOrder;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.descriptors.events.SceneActions.ActionType;

public class ComponentDefaultEventHandler implements IEntityModifierListener, IComponentEventHandler{
	
	// ===========================================================
	// Constants
	// ===========================================================
	//Log & Debug
	private static final String TAG = "ComponentDefaultEventHandler";
	private int							nID=-1;	
	private RotationModifier			onFlipModifier			= null;	//  used to take trace of flip modifier
	public IEntityModifier 				modifierSet				= null; //	contains the modifier set
	public IEntityModifier	 			iEntityModifiers[] 		= null; //  contains the list of modifiers in the modifier set
	public LinkedList<SceneActions>	 	oPreModifierAction		= null; //  contains the action to be launched before the modifiers;
	public LinkedList<SceneActions>		oPostModifierAction		= null; //  contains the action to be launched after the modifiers;
	public LinkedList<SceneActions>	 	oOnModifierAction		= null; //  contains the action to be launched in parallel with the modifiers;
	
	//  --------------------------------------------------------
	//	Interfaces
	//  --------------------------------------------------------
	//	IEntityModifierListener members
	//  --------------------------------------------------------
	@Override
	public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
		Log.i(TAG,"\t onModifierStarted");
	}


	@Override
	public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
		Log.i(TAG,"\t onModifierFinished");
		if(onFlipModifier == pModifier)
			if(pItem instanceof IActionSceneListener)
				((IActionSceneListener)pItem).onFlipCard(((CardSprite)pItem).getID(),((CardSprite)pItem).getCurrentSidet());
		
		if(oPostModifierAction != null)
			for(SceneActions action:oPostModifierAction){ 
				ExecuteAction(action,pItem);
				Log.i(TAG,"\t onModifierFinished");
			}
	}

	//  --------------------------------------------------------
	//	Members
	//  --------------------------------------------------------
	public int getID() {
		return nID;
	}


	public void setnID(int nID) {
		this.nID = nID;
	}
	public void setUpEventsHandler(ComponentEventHandlerDescriptor entry){

		setnID(entry.getID());
		
		//Setup Modifier if defined
		if(entry.modifierSet != null)
			setUpModifier(entry.modifierSet);
			
		if(entry.postModAction != null)
			oPostModifierAction = entry.postModAction;

		if(entry.preModAction != null)
			oPreModifierAction = entry.preModAction;

		if(entry.onModAction != null)
			oOnModifierAction= entry.onModAction;
	}
	public void handleEvent(IEntity pItem, TouchEvent pTouchEvent, TouchEvent lastTouchEvent){
		if(oPreModifierAction != null){
			Log.i(TAG,"\t Execute pre Mofier");
			for(SceneActions action:oPreModifierAction) 
				ExecuteAction(action,pItem,pTouchEvent,lastTouchEvent);

		}
		if(modifierSet != null){
			Log.i(TAG,"\t Execute Modifier");
			modifierSet.reset();
			IEntityModifier m = modifierSet.deepCopy();
			m.addModifierListener(this);
			pItem.registerEntityModifier(m);
		}
		if(oOnModifierAction != null){
			Log.i(TAG,"\t Execute OnModiferAction");
			for(SceneActions action:oOnModifierAction) 
				ExecuteAction(action,pItem,pTouchEvent,lastTouchEvent);

		}
	}
	@Override
	public void handleEvent(IEntity pItem, Events theEvent) {
		handleEvent(pItem,null,null); 
	}
	public IComponentEventHandler cloneEvent(ComponentEventHandlerDescriptor entry){
		ComponentDefaultEventHandler newHandler= new ComponentDefaultEventHandler();
		
		//by default clone the handler without customizations
		newHandler.modifierSet				= this.modifierSet; 
		newHandler.iEntityModifiers			= this.iEntityModifiers; 
		
		newHandler.oPreModifierAction = new LinkedList<SceneActions>();
		newHandler.oPreModifierAction.addAll(this.oPreModifierAction); 
		
		newHandler.oPostModifierAction = new LinkedList<SceneActions>();
		newHandler.oPostModifierAction.addAll(this.oPostModifierAction); 
		
		newHandler.oOnModifierAction = new LinkedList<SceneActions>();
		newHandler.oOnModifierAction.addAll(this.oOnModifierAction); 
		
		if(!entry.postModAction.isEmpty())
			newHandler.oPostModifierAction = entry.postModAction;
			
		//customize PreModifierAction
		if(!entry.preModAction.isEmpty())
			newHandler.oPreModifierAction = entry.preModAction;
		
		//customize OnModifierAction*/
		if(!entry.onModAction.isEmpty())
			newHandler.oOnModifierAction= entry.onModAction;
		
		return newHandler;
	}
	
	// ----------------------------------------------------------------------------
	// Protected methods
	// ----------------------------------------------------------------------------
	
	protected void setUpModifier(ComponentModifierListDescriptor modifiersList){
		int index = 0;
		iEntityModifiers = new IEntityModifier[modifiersList.getIModifierList().getModifiers().size()];
		for(ComponentModifierDescriptor m: modifiersList.getIModifierList().getModifiers()){
			IEntityModifier modifier;
			switch(m.getIModifier().getType()){
			case SCALE:
				modifier = new ScaleModifier(m.getIModifier().getDuration(),m.getIModifier().getScaleBegin(),m.getIModifier().getScaleEnd());
				iEntityModifiers[index++] = modifier;
				break;
			case MOVE:
				float pTension =0.0f;
				CardinalSplineMoveModifierConfig modifierConfig = new CardinalSplineMoveModifierConfig(
						8, pTension);
				
				modifierConfig.setControlPoint( 0, 86f,47f);
				modifierConfig.setControlPoint( 1, 209f,55f);
				modifierConfig.setControlPoint( 2, 339f,94f);
				modifierConfig.setControlPoint( 3, 316f,126f);
				modifierConfig.setControlPoint( 4, 95f,149f);
				modifierConfig.setControlPoint( 5, 95f,186f);
				modifierConfig.setControlPoint( 6, 268f,260f);
				modifierConfig.setControlPoint( 7, 81f,511f);
				
				modifier = new CardinalSplineMoveModifier(m.getIModifier().getDuration(), modifierConfig);
				
				iEntityModifiers[index++] = modifier;
				break;
			default:
				break;
			}
		}
		if(modifiersList.getIModifierList().getExecOrder() == ExecutionOrder.SERIAL){
			modifierSet = new SequenceEntityModifier(this,iEntityModifiers);
		}
		else
			modifierSet = new ParallelEntityModifier(this,iEntityModifiers);
	}
	
	protected void ExecuteAction(SceneActions action, IEntity pItem){
		if(ActionType.ON_MOVE_FOLLOW != action.type){
			ExecuteAction(action, pItem, null,null);
		}
	}
	public void onFireAction(ActionType type, IEntity pItem){
		if(oPreModifierAction != null){
			Log.i(TAG,"\t onFireAction Execute pre Mofier");
			for(SceneActions theAction:oPreModifierAction) 
				if(theAction.type == type)
				ExecuteAction(theAction,pItem,null,null);
		}
		if(oPostModifierAction != null){
			Log.i(TAG,"\t onFireAction Execute post Mofier");
			for(SceneActions theAction:oPostModifierAction) 
				if(theAction.type == type)
				ExecuteAction(theAction,pItem,null,null);
		}
	}
	
		
		
	public void ExecuteAction(SceneActions action, IEntity pItem, TouchEvent pTouchEvent,TouchEvent lastTouchEvent){
		Log.i(TAG,"\t ExecuteAction");
		switch(action.type){
		case NO_ACTION:
			break;
		case CHANGE_SCENE:
			executeChangeScene(action, pItem);
			break;
		case CHANGE_TO_FATHER_SCENE:
			action.NextScene = "";
			executeChangeSceneToFather(pItem);
			break;
		case CHANGE_TO_CHILD_SCENE:
			executeChangeChildScene(action, pItem);
			break;
		case CLOSE_CHILD:
			executeCloseChild(pItem);
			break;
		case GO_TO_MENU:
			executeGoToMenu(pItem);
			break;
		case GO_TO_NEXTLEVEL:
			executeGoToNextLevel(pItem);
			break;	
		case RELOAD_SCENE:
			executeReloadScene(pItem);
			break;
		case STICK:
			break;
		case PLAY_SOUND:
			executePlaySound(action, pItem);
			break;
		case PLAY_MUSIC:
			executePlayMusic(action, pItem);
			break;
		case FLIP:
			executeFlip(action, pItem);
			break;
		case CHANGE_Z_ORDER:
			executeChangeZOrder(action, pItem);
			break;
		case DISABLE_SCENE_TOUCH:
			disableTouch(pItem);
			break;
		case ENABLE_SCENE_TOUCH:
			enableTouch(pItem);
			break;
		case ON_MOVE_FOLLOW:
			executeOnMoveFollow(pItem,pTouchEvent,lastTouchEvent);
		default:
			break;
		}	
	}


	//------------------------------------------------------------
	//protected methods
	//------------------------------------------------------------
	protected void executeChangeSceneToFather(IEntity pItem){
		Log.i(TAG,"\t Change Scene");
		if(pItem instanceof IActivitySceneListener)
			((IActivitySceneListener)pItem).onFatherScene();
	}
	protected void executeChangeScene(SceneActions action, IEntity pItem){
		Log.i(TAG,"\t Change Scene");
		if(pItem instanceof IActivitySceneListener)
			((IActivitySceneListener)pItem).onChangeScene(action.NextScene);
	}
	protected void executeChangeChildScene(SceneActions action, IEntity pItem){
		Log.i(TAG,"\t Change Scene");
		if(pItem instanceof IActivitySceneListener)
			((IActivitySceneListener)pItem).onChangeChildScene(action.NextScene);
	}
	protected void executeCloseChild(IEntity pItem){
		Log.i(TAG,"\t Change Scene");
		if(pItem instanceof IActivitySceneListener)
			((IActivitySceneListener)pItem).onCloseChildScene();
	}
	protected void executeReloadScene(IEntity pItem){
		Log.i(TAG,"\t Change Scene");
		if(pItem instanceof IActivitySceneListener)
			((IActivitySceneListener)pItem).onReloadScene();
	}
	private void executeGoToNextLevel(IEntity pItem) {
		Log.i(TAG,"\t Change Scene");
		if(pItem instanceof IActivitySceneListener)
			((IActivitySceneListener)pItem).onGoToNextLevel();
	}
	private void executeGoToMenu(IEntity pItem) {
		Log.i(TAG,"\t Change Scene");
		if(pItem instanceof IActivitySceneListener)
			((IActivitySceneListener)pItem).onGoToMenu();
	}
	protected void executePlaySound(SceneActions action, IEntity pItem){
		Log.i(TAG,"\t PLay Music");
		ResourcesManager rMgr = ResourcesManager.getInstance();
		Sound snd = rMgr.getSound(action.resourceName).getTheSound();
		snd.setVolume(1000);
		snd.play();
	}
	protected void executePlayMusic(SceneActions action, IEntity pItem){
		Log.i(TAG,"\t PLay Music");
		ResourcesManager rMgr = ResourcesManager.getInstance();
		Music msc = rMgr.getMusic(action.resourceName);
		msc.setVolume(1000);
		msc.play();
	}
	protected void executeChangeZOrder(SceneActions action, IEntity pItem){
		Log.i(TAG,"\t Change Z Order");
		pItem.setZIndex(action.ZIndex);
		pItem.getParent().sortChildren();
	}
	protected void executeFlip(SceneActions action, IEntity pItem){
		Log.i(TAG,"\t executeFlip");
		float rotation = pItem.getRotation();
		Log.i(TAG,"\t executeFlip from rotation" + rotation);
		if(rotation <=90){
			Log.i(TAG,"\t executeFlip flip 1");
			onFlipModifier = new RotationModifier(action.flipTime/1000,0,180,this);
			pItem.registerEntityModifier(onFlipModifier);
		}
		else{
			Log.i(TAG,"\t executeFlip flip 2");
			onFlipModifier = new RotationModifier(action.flipTime/1000,180,0,this);
			pItem.registerEntityModifier(onFlipModifier);
		}
	}
	protected void enableTouch(IEntity pItem){
		Log.i(TAG,"\t Enable scene touch ");
		if(pItem instanceof IActionSceneListener)
			((IActionSceneListener)pItem).unLockTouch();
	}
	protected void disableTouch(IEntity pItem){
		Log.i(TAG,"\t Disable scene touch ");
		if(pItem instanceof IActionSceneListener)
			((IActionSceneListener)pItem).lockTouch();
	}
	protected void executeOnMoveFollow(IEntity pItem,TouchEvent pTouchEvent,TouchEvent lastTouchEvent){
		float deltaX = pTouchEvent.getX() - lastTouchEvent.getX();
		float deltaY = pTouchEvent.getY() - lastTouchEvent.getY();
		pItem.setPosition(pItem.getX() + deltaX, pItem.getY() + deltaY);
	}
}