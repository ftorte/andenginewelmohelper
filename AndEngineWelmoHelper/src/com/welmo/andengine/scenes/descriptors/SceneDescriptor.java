package com.welmo.andengine.scenes.descriptors;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.xml.sax.Attributes;

import com.welmo.andengine.scenes.descriptors.components.GameLevel;
import com.welmo.andengine.scenes.descriptors.components.HUDDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;

public class SceneDescriptor extends BasicDescriptor {
	// =======================================================================================
	// Constants
	// =======================================================================================

	// =======================================================================================
	// Member Variables
	// =======================================================================================
	public String 										sceneName="";
	public String 										sceneFather="";
	public LinkedList<ComponentEventHandlerDescriptor>  pGlobalEventHandlerList;
	public Map<Integer,BasicDescriptor> 				pTemplates;		//contains all templated for instantiations in the scene			
	
	protected GameLevel									gameLevel		=GameLevel.EASY;
	protected HashMap<String,String[]> 					phrasesMap;
	protected SceneType									sceneType		=SceneType.DEFAULT;
	
	private boolean 									bPinchAndZoom	= false;
	private boolean 									bHasHUD 		= false;
	@SuppressWarnings("unused")
	private HUDDescriptor								pHUDDsc 		= null;
	
	
	
	// ===========================================================
	// Constructor(s)
	// ===========================================================
	public SceneDescriptor() {
		pGlobalEventHandlerList = new LinkedList<ComponentEventHandlerDescriptor> ();
		phrasesMap				= new HashMap<String,String[]>();
		pTemplates 				= new HashMap<Integer,BasicDescriptor>();
	}
	
	// ===========================================================
	// Getters & Setters
	// ===========================================================
	public String 			getSceneName(){return sceneName;}
	public void 			setSceneName(String sceneName){this.sceneName = sceneName;}
	
	public String 			getSceneFather(){return sceneFather;}
	public void 			setSceneFather(String scene){sceneFather=scene;}
	
	public boolean 			isPinchAndZoom(){return bPinchAndZoom;}
	public void 			setPinchAndZoom(boolean bPinchAndZoom){this.bPinchAndZoom = bPinchAndZoom;}
	
	public boolean 			hasHUD(){return bHasHUD;}
	public void 			hasHUD(boolean hasHUD){this.bHasHUD = hasHUD;}
	
	public HUDDescriptor 	getHUDDsc(){return getHudDsc();}
	
	public GameLevel 		getGameLevel(){return gameLevel;}
	public void 			setGameLevel(GameLevel newGameLevel){gameLevel=newGameLevel;}
	
	public SceneType 		getSceneType(){return sceneType;}
	public void 			setSceneType(SceneType sceneType){this.sceneType = sceneType;}
	// ===========================================================
	// Constructor(s)
	// ===========================================================
	
	private HUDDescriptor getHudDsc(){	
		Iterator<Entry<Integer,BasicDescriptor>> it = pChild.entrySet().iterator();
	    while (it.hasNext()) {
	    	Entry<Integer,BasicDescriptor> pairs = (Entry<Integer, BasicDescriptor>) it.next();
	    	if(pairs.getValue() instanceof HUDDescriptor) 
	    		return (HUDDescriptor)pairs.getValue();
	    }
	    return null;
	}
	
	public HashMap<String, String[]> getPhrasesMap() {
		return phrasesMap;
	}
	public void setPhrasesMap(HashMap<String, String[]> phrasesMap) {
		this.phrasesMap = phrasesMap;
	}
	
	public BasicDescriptor getTemplate(Integer key) {
		return pTemplates.get(key);
	}
	
	public void addTemplate(Integer key, BasicDescriptor newTemplate) {
		if(pTemplates.get(key)!= null)
			throw new InvalidParameterException("the template already exist in the scene: " + newTemplate.ID);
		
		pTemplates.put(key, newTemplate);
	}
	public void readXMLDescription(Attributes attributes) {
		super.readXMLDescription(attributes);
		// Read scene description	
		this.sceneName = new String(attributes.getValue(ScnTags.S_A_NAME));
		if(attributes.getValue(ScnTags.S_FATHER)!= null)
			this.sceneFather = new String(attributes.getValue(ScnTags.S_FATHER));
		if(attributes.getValue(ScnTags.S_CLASS_NAME)!= null)
			this.className = new String(attributes.getValue(ScnTags.S_CLASS_NAME));
		if(attributes.getValue(ScnTags.S_A_PINTCHZOOM)!= null)
			this.setPinchAndZoom(Boolean.parseBoolean(attributes.getValue(ScnTags.S_A_PINTCHZOOM)));
		if(attributes.getValue(ScnTags.S_A_HUD)!= null)
			this.hasHUD(Boolean.parseBoolean(attributes.getValue(ScnTags.S_A_HUD)));
	}
}
