package com.welmo.andengine.scenes.components;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map.Entry;

import org.andengine.audio.sound.Sound;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.util.modifier.IModifier;

import android.util.Log;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentModifierDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentModifierListDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ExecutionOrder;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;

public class ComponentDefaultEventHandler implements IEntityModifierListener, IComponentEventHandler{
	
	// ===========================================================
	// Constants
	// ===========================================================
	//Log & Debug
	private static final String TAG = "ComponentDefaultEventHandler";
	private int							nID=-1;	
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
	public void handleEvent(IEntity pItem){
		if(oPreModifierAction != null){
			Log.i(TAG,"\t Execute pre Mofier");
			for(SceneActions action:oPreModifierAction) 
				ExecuteAction(action,pItem);

		}
		if(modifierSet != null){
			Log.i(TAG,"\t Execute Modifier");
			modifierSet.reset();
			//pItem.registerEntityModifier(modifierSet);
			//pItem.registerEntityModifier(modifierSet.deepCopy()); it seams that with deepCopy listener is not called
			IEntityModifier m = modifierSet.deepCopy();
			m.addModifierListener(this);
			pItem.registerEntityModifier(m);
		}
		if(oOnModifierAction != null){
			Log.i(TAG,"\t Execute OnModiferAction");
			for(SceneActions action:oOnModifierAction) 
				ExecuteAction(action,pItem);

		}
	}
	
	public IComponentEventHandler cloneEvent(ComponentEventHandlerDescriptor entry){
		ComponentDefaultEventHandler newHandler= new ComponentDefaultEventHandler();
		
		//by default clone the handler without customizations
		newHandler.modifierSet				= this.modifierSet; 
		newHandler.iEntityModifiers 		= this.iEntityModifiers; 
		newHandler.oPreModifierAction		= this.oPreModifierAction; 
		newHandler.oPostModifierAction		= this.oPostModifierAction; 
		newHandler.oOnModifierAction		= this.oOnModifierAction; 
			
		//Customize Pre/On/Post Modifier Action. 
		// This version replace the cloned with the new one and don't change each single action;
		//customize PostModifierAction
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
				modifier = new ScaleModifier(1,m.getIModifier().getScaleBegin(),m.getIModifier().getScaleEnd());
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
		Log.i(TAG,"\t ExecuteAction");
		switch(action.type){
		case NO_ACTION:
			break;
		case CHANGE_SCENE:
			executeChangeScene(action, pItem);
			break;
		case STICK:
			break;
		case PLAY_SOUND:
			executePlaySound(action, pItem);
			break;
		case PLAY_MUSIC:
			break;
		case FLIP:
			executeFlip(action, pItem);
			break;
		case CHANGE_Z_ORDER:
			executeChangeZOrder(action, pItem);
			break;
		default:
			break;
		}	
	}
	//------------------------------------------------------------
	//protected methods
	//------------------------------------------------------------
	protected void executeChangeScene(SceneActions action, IEntity pItem){
		Log.i(TAG,"\t Change Scene");
		if(pItem instanceof IActionOnSceneListener)
			((IActionOnSceneListener)pItem).onActionChangeScene(action.NextScene);
	}
	protected void executePlaySound(SceneActions action, IEntity pItem){
		Log.i(TAG,"\t PLay Music");
		ResourcesManager rMgr = ResourcesManager.getInstance();
		Sound snd = rMgr.getSound(action.resourceName);
		snd.setVolume(1000);
		snd.play();
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
			pItem.registerEntityModifier(new RotationModifier(1,0,180));
		}
		else{
			Log.i(TAG,"\t executeFlip flip 2");
			pItem.registerEntityModifier(new RotationModifier(1,180,0));
		}
	}

}