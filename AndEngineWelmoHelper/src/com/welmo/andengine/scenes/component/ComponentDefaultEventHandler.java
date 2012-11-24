package com.welmo.andengine.scenes.components;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import org.andengine.audio.sound.Sound;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
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

public class ComponentDefaultEventHandler implements IEntityModifierListener {
	
	// ===========================================================
	// Constants
	// ===========================================================
	//Log & Debug
	private static final String TAG = "ComponentDefaultEventHandler";
		
	//public Events						eEvent					= Events.NO_EVENT;
	public IEntityModifier 				modifierSet				= null; //	contains the modifier set
	public IEntityModifier	 			iEntityModifiers[] 		= null; //  contains the list of modifiers in the modifier set
	public SceneActions	 				oPreModifierAction		= null; //  contains the action to be launched before the modifiers;
	public SceneActions					oPostModifierAction		= null; //  contains the action to be launched after the modifiers;
	public SceneActions	 			 	oOnModifierAction		= null; //  contains the action to be launched in parallel with the modifiers;
	private int	 						nStatus = 0;
	
	//  --------------------------------------------------------
	//	Implement Interfaces
	//  --------------------------------------------------------
    //	IEntityModifierListener members
	//  --------------------------------------------------------
	@Override
	public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
		Log.i(TAG,"\t onModifierStarted object");
	}

	@Override
	public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
		Log.i(TAG,"\t onModifierFinished object");
		if(oPostModifierAction != null)
			ExecuteAction(oPostModifierAction,pItem);
	}

	//  --------------------------------------------------------
	//	Members
	//  --------------------------------------------------------
	public void setEventsHandler(ComponentEventHandlerDescriptor entry){

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


	public void setUpModifier(ComponentModifierListDescriptor modifiersList){
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
	public void handleEvent(IEntity pItem){
		if(oPreModifierAction != null){
			Log.i(TAG,"\t Execute pre Mofier");
			ExecuteAction(oPreModifierAction, pItem);
		}
		if(modifierSet != null){
			Log.i(TAG,"\t Execute Modifier");
			modifierSet.reset();
			pItem.registerEntityModifier(modifierSet.deepCopy());
		}
		if(oOnModifierAction != null){
			Log.i(TAG,"\t Execute OnModiferAction");
			ExecuteAction(oOnModifierAction, pItem);
		}
	}
	public void ExecuteAction(SceneActions action, IEntity pItem){
		switch(action.type){
		case NO_ACTION:
			break;
		case CHANGE_SCENE:
			if(pItem instanceof IActionOnSceneListener)
				((IActionOnSceneListener)pItem).onActionChangeScene(action.NextScene);
			break;
		case STICK:
			break;
		case PLAY_SOUND:
			Log.i(TAG,"\t PLay Music");
			ResourcesManager rMgr = ResourcesManager.getInstance();
			Sound snd = rMgr.getSound(action.resourceName);
			snd.setVolume(1000);
			snd.play();break;
		case PLAY_MUSIC:
			break;
		default:
			break;
		}	
	}
}