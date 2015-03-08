package com.welmo.andengine.scenes.descriptors;

import java.security.InvalidParameterException;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.util.Log;

import com.welmo.andengine.managers.EventDescriptionsManager;
import com.welmo.andengine.managers.SceneDescriptorsManager;
import com.welmo.andengine.scenes.components.Stick;
import com.welmo.andengine.scenes.descriptors.components.BackGroundObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ButtonDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ButtonSceneLauncherDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ColoringSpriteDescriptor;
import com.welmo.andengine.scenes.descriptors.components.GameLevel;
import com.welmo.andengine.scenes.descriptors.components.HUDDescriptor;
import com.welmo.andengine.scenes.descriptors.components.MultiViewSceneDescriptor;
import com.welmo.andengine.scenes.descriptors.components.PuzzleObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.TextObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ToolsBarDescriptor;
import com.welmo.andengine.scenes.descriptors.components.BasicComponentDescriptor.Alignment;
import com.welmo.andengine.scenes.descriptors.components.BasicComponentDescriptor.ICharacteristics;
import com.welmo.andengine.scenes.descriptors.components.BasicComponentDescriptor.IDimension;
import com.welmo.andengine.scenes.descriptors.components.BasicComponentDescriptor.IOrientation;
import com.welmo.andengine.scenes.descriptors.components.BasicComponentDescriptor.IPosition;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor.SpritesTypes;
import com.welmo.andengine.scenes.descriptors.events.BasicModifierDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;
import com.welmo.andengine.scenes.descriptors.events.ComponentModifierDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentModifierListDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ExecutionOrder;
import com.welmo.andengine.scenes.descriptors.events.SceneActions;
import com.welmo.andengine.scenes.descriptors.events.SceneActions.ActionType;
import com.welmo.andengine.utility.ScreenDimensionHelper;


public class ParserXMLSceneDescriptor extends DefaultHandler {

	//--------------------------------------------------------
	// Variables
	//--------------------------------------------------------
	private static final String TAG = "ParserXMLSceneDescriptor";
	//Helpers
	private ScreenDimensionHelper				dimHelper				= null;
	//Managers
	private EventDescriptionsManager			pEventDscMgr			= null;
	private SceneDescriptorsManager				pSceneDescManager		= null;
	//Scene Descriptor
	protected SceneDescriptor					pSceneDsc				= null;
	protected ConfiguredSceneDescriptor			pSceneInstantiationDsc	= null;
	protected MultiViewSceneDescriptor			pMultiViewSceneDsc		= null;
	//Modifier descriptors
	protected SceneActions						pAction					= null;
	protected ComponentModifierDescriptor		pModifier				= null;
	protected ComponentEventHandlerDescriptor	pEventHandler			= null;
	//Object Descriptor
	protected BackGroundObjectDescriptor		pBackGroundDescriptor	= null;
	protected SpriteObjectDescriptor 			pSpriteDsc				= null;
	protected SpriteObjectDescriptor 			pCompoundSpriteDsc		= null;
	//List to manage descriptor chain
	protected LinkedList<BasicDescriptor> 		pDescriptorsInProcessing= null;
	protected BasicDescriptor 					pCurrentDescriptorInProcessing=null;
	//List to manage parser status chain
	protected int								nStatus					= STATUS_BEGIN;
	protected int								nStatusPrec				= STATUS_BEGIN;
	protected int								nComponents				= 0;
	protected int								nModifiers				= 0;
	static final int STATUS_BEGIN										= 1;
	static final int STATUS_PARSE_SCENES								= 2;
	static final int STATUS_PARSE_SCENE									= 3;
	static final int STATUS_PARSE_SCENE_INSTANTIATION  					= 4; 
	static final int STATUS_PARSE_COMPONENT								= 5;
	static final int STATUS_PARSE_EVENT_HANDLER							= 6;
	static final int STATUS_PARSE_ACTITIVY_MODIFIER 					= 7;
	static final int STATUS_PARSE_ACTITIVY_ACTION 						= 8;
	
	//--------------------------------------------------------
	/*
	 * Constructor
	 */
	public ParserXMLSceneDescriptor(Context ctx) {
		super();
		dimHelper 						= ScreenDimensionHelper.getInstance(ctx);
		pEventDscMgr 					= EventDescriptionsManager.getInstance();
		pDescriptorsInProcessing 		= new LinkedList<BasicDescriptor>();
		pCurrentDescriptorInProcessing 	= null;
		nStatus = STATUS_BEGIN;
	}
	/*
	 * Overrided methods
	 * @see org.xml.sax.helpers.DefaultHandler#processingInstruction(java.lang.String, java.lang.String)
	 */
	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		super.processingInstruction(target, data);
	}
	@Override
	// ===========================================================
	// * Cette méthode est appelée par le parser une et une seule
	// * fois au démarrage de l'analyse de votre flux xml.
	// * Elle est appelée avant toutes les autres méthodes de l'interface,
	// * à l'exception unique, évidemment, de la méthode setDocumentLocator.
	// * Cet événement devrait vous permettre d'initialiser tout ce qui doit
	// * l'être avant ledébut du parcours du document.
	// ===========================================================
	public void startDocument() throws SAXException {
		super.startDocument();
		nStatus 						= ParserXMLSceneDescriptor.STATUS_BEGIN;
		pSceneDescManager 				= SceneDescriptorsManager.getInstance();
	}
	@Override
	/*
	 * Fonction étant déclenchée lorsque le parser trouve un tag XML
	 * C'est cette méthode que nous allons utiliser pour instancier un nouveau feed
	 */
	public void startElement(String uri, String localName, String name,	Attributes attributes) throws SAXException {	
		// Read actions and modifiers
		
		BasicDescriptor newDescriptor = null;
	
		switch(nStatus){
		case STATUS_BEGIN:
			if(parseScenes(localName, attributes) == false)
				throw new NullPointerException("ParserXMLSceneDescriptor error invalid Element");
			break;
		case STATUS_PARSE_SCENES:
			if((newDescriptor = parseScene(localName, attributes)) != null)
				addComponentDescriptor(newDescriptor);
			else if((newDescriptor = parseSceneInstantiation(localName, attributes)) != null)
				addComponentDescriptor(newDescriptor);
			else
				throw new NullPointerException("ParserXMLSceneDescriptor error invalid Element");
			break;
		case STATUS_PARSE_SCENE:
			if((newDescriptor = parseComponentDescriptor(localName, attributes)) != null)
				//check if a template and add it to template list
				if(newDescriptor.isTemplate)
					addTemplateDescriptor(newDescriptor);
				else
					//check if and instance of an object 
					if(newDescriptor.isInstanceOfID > 0 ){ //if instanceOfID return value > 0 it means it an instace of an object 
						if(pCurrentDescriptorInProcessing instanceof SceneDescriptor) {
							BasicDescriptor template = ((SceneDescriptor)pCurrentDescriptorInProcessing).pTemplates.get(newDescriptor.isInstanceOfID);
							createFromTemplate(template, newDescriptor, localName, attributes);
						}
					}
					else
						addComponentDescriptor(newDescriptor);
			else if((pEventHandler = (ComponentEventHandlerDescriptor)parseComponentEventHandlerDescriptor(localName, attributes))!= null){
				if(pCurrentDescriptorInProcessing != null)
					((SceneDescriptor)pCurrentDescriptorInProcessing).pGlobalEventHandlerList.add(pEventHandler);
				else
					break;
			}
			else
				throw new NullPointerException("ParserXMLSceneDescriptor error invalid Element");
			break;
		case STATUS_PARSE_COMPONENT:
			
			if((newDescriptor = parseComponentDescriptor(localName, attributes)) != null){					
				//addComponentDescriptor(newDescriptor);
				//check if a template and add it to template list
				if(newDescriptor.isTemplate)
					addTemplateDescriptor(newDescriptor);
				else
					//check if is an  instance of an object 
					if(newDescriptor.isInstanceOfID > 0 ){ //if instanceOfID return value > 0 it means it an instance of an object 
						if(pCurrentDescriptorInProcessing instanceof SceneDescriptor) {
							BasicDescriptor template = ((SceneDescriptor)pCurrentDescriptorInProcessing).pTemplates.get(newDescriptor.isInstanceOfID);
							createFromTemplate(template, newDescriptor, localName, attributes);
						}
					}
					else
						addComponentDescriptor(newDescriptor);
			}
			else if((parseActionDescriptor(localName, attributes)) != null)
				break;
			else if((pEventHandler = (ComponentEventHandlerDescriptor)parseComponentEventHandlerDescriptor(localName, attributes))!= null){
				if(pCurrentDescriptorInProcessing != null)
					pCurrentDescriptorInProcessing.pEventHandlerList.put(pEventHandler.event, pEventHandler);	
				break;
			}
			else
				throw new NullPointerException("ParserXMLSceneDescriptor error invalid Element");
			break;
		case STATUS_PARSE_EVENT_HANDLER:
			Log.i(TAG,"\t parse  STATUS_PARSE_EVENT_HANDLER");
			if((parseComponentEventHandlerDescriptor(localName, attributes))==null)
				throw new NullPointerException("ParserXMLSceneDescriptor error invalid Element");
			break;	
		case STATUS_PARSE_ACTITIVY_MODIFIER:
			ComponentModifierDescriptor modDsc;
			if((modDsc = parseComponentModifierDescriptor(localName, attributes))!=null)
				pEventHandler.modifierSet.getIModifierList().getModifiers().add(modDsc);
			else
				throw new NullPointerException("ParserXMLSceneDescriptor error invalid Element");
			break;
		}
	}
	/*
	 * Methods
	 */
	public void addComponentDescriptor(BasicDescriptor newDesciptor){
		Log.i(TAG,"\t\t\t\t add Component Descriptor in List");
		//Save father descriptor to LIFO
		if(pCurrentDescriptorInProcessing != null) 
			pDescriptorsInProcessing.addLast(pCurrentDescriptorInProcessing); 
		
		pCurrentDescriptorInProcessing = newDesciptor;
		
		//attach new description as child of father descriptor
		if(!pDescriptorsInProcessing.isEmpty()) 
			pDescriptorsInProcessing.getLast().pChild.put(pCurrentDescriptorInProcessing.ID,
					pCurrentDescriptorInProcessing); 
	}
	public void addTemplateDescriptor(BasicDescriptor newDesciptor){
		Log.i(TAG,"\t\t\t\t add Template Descriptor in List");
		//check curret descripotr is a scene to add the template
		if(!(pCurrentDescriptorInProcessing instanceof SceneDescriptor))
			throw new InvalidParameterException ("tri to add template to a non scene object"); 
		
		//attach new template description as child of father descriptor
		((SceneDescriptor)pCurrentDescriptorInProcessing).pTemplates.put(newDesciptor.ID,newDesciptor);
				
		//Save father descriptor to LIFO to allow recoursive parsing of template
		if(pCurrentDescriptorInProcessing != null) 
			pDescriptorsInProcessing.addLast(pCurrentDescriptorInProcessing); 
		
		pCurrentDescriptorInProcessing = newDesciptor;
	}
	public void createFromTemplate(BasicDescriptor template, BasicDescriptor newDesciptor, String localName, Attributes attributes){
		
		if(localName.equalsIgnoreCase(ScnTags.S_SCENELAUNCHER)){
			((ButtonSceneLauncherDescriptor)newDesciptor).instantiateXMLDescription((ButtonSceneLauncherDescriptor)template, attributes);

			//Save father descriptor to LIFO
			if(pCurrentDescriptorInProcessing != null) 
				pDescriptorsInProcessing.addLast(pCurrentDescriptorInProcessing); 

			pCurrentDescriptorInProcessing = newDesciptor;

			//attach new description as child of father descriptor
			if(!pDescriptorsInProcessing.isEmpty()) 
				pDescriptorsInProcessing.getLast().pChild.put(pCurrentDescriptorInProcessing.ID,
						pCurrentDescriptorInProcessing);
		}
	}
	
	public boolean parseScenes(String localName, Attributes attributes){ 
		if (localName.equalsIgnoreCase(ScnTags.S_SCENES)){
			nStatus = STATUS_PARSE_SCENES;
			return true;
		}
		return false;
	}
	public BasicDescriptor parseScene(String localName, Attributes attributes){ 
		if (localName.equalsIgnoreCase(ScnTags.S_SCENE)){
			nStatus = STATUS_PARSE_SCENE;
			this.nComponents=0;
			return readSceneDescription(attributes);
		}
		else
			return null; 
	}
	public BasicDescriptor parseSceneInstantiation(String localName, Attributes attributes){ 
		ConfiguredSceneDescriptor newDescriptor =  null;
		if (localName.equalsIgnoreCase(ScnTags.S_SCENE_INSTANTIATION)){
			nStatus = STATUS_PARSE_SCENE_INSTANTIATION;
			(newDescriptor = new ConfiguredSceneDescriptor()).readXMLDescription(attributes);
			pSceneDescManager.addCFGScene(newDescriptor.sceneName, newDescriptor);
		}
		return newDescriptor;
	}
	public BasicDescriptor parseComponentDescriptor(String localName, Attributes attributes){ 
		Log.i(TAG,"\t parseComponentDescriptor");
		
		BasicDescriptor newDescriptor = null;
		
		if (localName.equalsIgnoreCase(ScnTags.S_O_SPRITE))
			newDescriptor = (BasicDescriptor)(new SpriteObjectDescriptor()); 
		else if (localName.equalsIgnoreCase(ScnTags.S_COLORING_SPRITE))
			newDescriptor = (BasicDescriptor)(new ColoringSpriteDescriptor());	
		/*else if (localName.equalsIgnoreCase(ScnTags.S_COMPOUND_SPRITE))
			newDescriptor = readCupondSprite(attributes);*/
		else if (localName.equalsIgnoreCase(ScnTags.S_TEXT))
			newDescriptor = (BasicDescriptor)(new TextObjectDescriptor());
		else if (localName.equalsIgnoreCase(ScnTags.S_BACKGROUND))
			newDescriptor = (BasicDescriptor)(new BackGroundObjectDescriptor()); //Read new descriptor	
		else if (localName.equalsIgnoreCase(ScnTags.S_PUZZLE_SPRITE))
			newDescriptor = (BasicDescriptor)(new PuzzleObjectDescriptor());
		else if(localName.equalsIgnoreCase(ScnTags.S_HUD))
			newDescriptor = (BasicDescriptor)(new HUDDescriptor());
		else if(localName.equalsIgnoreCase(ScnTags.S_TOOLBAR))
			newDescriptor = (BasicDescriptor)(new ToolsBarDescriptor());	
		else if(localName.equalsIgnoreCase(ScnTags.S_BUTTON))
			newDescriptor = (BasicDescriptor)(new ButtonDescriptor());	
		else if(localName.equalsIgnoreCase(ScnTags.S_SCENELAUNCHER))
			newDescriptor = (BasicDescriptor)(new ButtonSceneLauncherDescriptor());	
		else
			return null;

		newDescriptor.readXMLDescription(attributes);
		
		//update status and components nb
		Log.i(TAG,"\t parseComponentDescriptor");
		nComponents ++;
		nStatus = STATUS_PARSE_COMPONENT;
		return newDescriptor;
	}
	public SceneActions parseActionDescriptor(String localName, Attributes attributes){ 
		Log.i(TAG,"\t parseActionDescriptor");
		SceneActions newDescriptor=null;
		if (localName.equalsIgnoreCase(ScnTags.S_ACTION)) {
			newDescriptor = new SceneActions();
			newDescriptor.readXMLDescription(attributes);
			pEventDscMgr.addAction(pAction.event,pCurrentDescriptorInProcessing,pAction);
		}
		return newDescriptor;
	}
	public BasicModifierDescriptor parseComponentEventHandlerDescriptor(String localName, Attributes attributes){ 
		Log.i(TAG,"\t parseComponentEventHandlerDescriptor");
		BasicModifierDescriptor newDescriptor=null;
		
		if (nStatus==STATUS_PARSE_SCENE){
			if(localName.equalsIgnoreCase(ScnTags.S_EVENT_HANDLER)){
				newDescriptor = new ComponentEventHandlerDescriptor();
				newDescriptor.readXMLDescription(attributes);
				nStatus = STATUS_PARSE_EVENT_HANDLER;
				nStatusPrec=STATUS_PARSE_SCENE;
				return newDescriptor;
			}
			else
				return null;
		}
		if (nStatus==STATUS_PARSE_COMPONENT) 
			if(localName.equalsIgnoreCase(ScnTags.S_EVENT_HANDLER)){
				newDescriptor = new ComponentEventHandlerDescriptor();
				newDescriptor.readXMLDescription(attributes);
				nStatus = STATUS_PARSE_EVENT_HANDLER;
				nStatusPrec=STATUS_PARSE_COMPONENT;
				return newDescriptor;
			}
			else
				return null;
		if (nStatus==STATUS_PARSE_EVENT_HANDLER){ 
			if(localName.equalsIgnoreCase(ScnTags.S_MODIFIER_LIST)){
				Log.i(TAG,"\t\t readComponentModifierListDescriptor " + localName);
				pEventHandler.modifierSet = new ComponentModifierListDescriptor();
				pEventHandler.modifierSet.readXMLDescription(attributes);
				nStatus = STATUS_PARSE_ACTITIVY_MODIFIER;
				return pEventHandler.modifierSet;
			}
			else{ 
				Log.i(TAG,"\t\t readActionDescriptoon ");
				SceneActions newActionDescriptoon = new SceneActions();
				newActionDescriptoon.readXMLDescription(attributes);
				if(localName.equalsIgnoreCase(ScnTags.S_PRE_MOD_ACTION)){
					Log.i(TAG,"\t\t readComponentActionDescriptor PreModifierAction ");
					pEventHandler.preModAction.add(newActionDescriptoon);
				}
				else if(localName.equalsIgnoreCase(ScnTags.S_POST_MOD_ACTION)){
					Log.i(TAG,"\t\t readComponentActionDescriptor PostModifierAction ");
					pEventHandler.postModAction.add(newActionDescriptoon);
				}
				else if(localName.equalsIgnoreCase(ScnTags.S_ON_MOD_ACTION)){
					Log.i(TAG,"\t\t readComponentActionDescriptor OnModifierAction ");
					pEventHandler.onModAction.add(newActionDescriptoon);
				}
				else return null;
				return newActionDescriptoon;
			}
		}
		return null;
	}	
	public ComponentModifierDescriptor parseComponentModifierDescriptor(String localName, Attributes attributes){ 
		Log.i(TAG,"\t parseComponentModifierDescriptor");
		ComponentModifierDescriptor newDescriptor=null;
		if (localName.equalsIgnoreCase(ScnTags.S_MODIFIER)){
			newDescriptor = new ComponentModifierDescriptor();
			newDescriptor.readXMLDescription(attributes);
		}
		return newDescriptor;
	}
	//-------------------------------------------------------------------------------------
	// Private functions to read the elements 
	//-------------------------------------------------------------------------------------
	private SceneDescriptor readSceneDescription(Attributes attr){
		Log.i(TAG,"\t readSceneDescription");
		if(this.pSceneDsc != null)
			throw new NullPointerException("ParserXMLSceneDescriptor encountered scene description with another scene description inside");

		if(attr.getValue(ScnTags.S_A_TYPE)!=null){
			switch(SceneType.valueOf(attr.getValue(ScnTags.S_A_TYPE))){
			case MEMORY:
				pSceneDsc = new MemorySceneDescriptor();
				pSceneDsc.readXMLDescription(attr);
				pSceneDsc.setSceneType(SceneType.MEMORY);
				break;
			case DEFAULT:
				pSceneDsc = new SceneDescriptor();
				pSceneDsc.readXMLDescription(attr);
				pSceneDsc.setSceneType(SceneType.DEFAULT);
				break;
			default:
				pSceneDsc = new SceneDescriptor();
				pSceneDsc.readXMLDescription(attr);
				pSceneDsc.setSceneType(SceneType.DEFAULT);
				break;
			}
		}
		else{
			pSceneDsc = new SceneDescriptor();
			pSceneDsc.readXMLDescription(attr);
		}
		//Parse JSON strings
		JSONObject jObject;
		
		//Parse phrases
		if(attr.getValue(ScnTags.S_A_SCENE_PHRASES)!= null){
			try {
				jObject = new JSONObject(attr.getValue(ScnTags.S_A_SCENE_PHRASES));
				
				JSONArray names = jObject.names();

				for (int indexI = 0; indexI < names.length(); indexI++){
					JSONArray currentarray= jObject.getJSONArray(names.getString(indexI));
					//create array will contians the phrase
					String[] phrase = new String [currentarray.length()];
					for(int indexJ=0; indexJ < currentarray.length(); indexJ++){
						phrase[indexJ]= new String(currentarray.getString(indexJ));
					}
					this.pSceneDsc.getPhrasesMap().put(names.getString(indexI), phrase);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		pSceneDescManager.addScene(pSceneDsc.sceneName, pSceneDsc);
		return pSceneDsc;
	}
	
	
	private SpriteObjectDescriptor readCupondSprite(Attributes attr){
		Log.i(TAG,"\t\t readCupondSprite");
		if(this.pCompoundSpriteDsc != null) //check if new compound sprite
			throw new NullPointerException("ParserXMLSceneDescriptor encountered compoundsprite description with another compoundsprite description inside");

		if(this.pSceneDsc == null) //check if compound is part of a scene
			throw new NullPointerException("ParserXMLSceneDescriptor encountered sceneobject description withou sceneo description");

		pCompoundSpriteDsc = new SpriteObjectDescriptor();

		// Read the compound sprite parameters
		pCompoundSpriteDsc.ID=Integer.parseInt(attr.getValue(ScnTags.S_A_ID ));
		pCompoundSpriteDsc.type = SpriteObjectDescriptor.SpritesTypes.valueOf(attr.getValue(ScnTags.S_A_TYPE));
		//manage position and dimensions
		// FTO To reactivete this.parseAttributesPosition(pCompoundSpriteDsc.getIPosition(),attr);

		//add compound sprite to scene
		pSceneDsc.pChild.put(pCompoundSpriteDsc.ID,pCompoundSpriteDsc);	
		return pCompoundSpriteDsc;
	}
	//-------------------------------------------------------------------------------------
	// Specfic private functions to read the attributes
	//-------------------------------------------------------------------------------------
	
	@Override
	// * Fonction étant déclenchée lorsque le parser à parsé
	// * l'intérieur de la balise XML La méthode characters
	// * a donc fait son ouvrage et tous les caractère inclus
	// * dans la balise en cours sont copiés dans le buffer
	// * On peut donc tranquillement les récupérer pour compléter
	// * notre objet currentFeed
	public void endElement(String uri, String localName, String name) throws SAXException {	

		switch(nStatus){
		case STATUS_PARSE_SCENES:
			Log.i(TAG,"endElement SceneS");
			if (localName.equalsIgnoreCase(ScnTags.S_SCENES)){
				nStatus = STATUS_BEGIN;
			}
			break;
		case STATUS_PARSE_SCENE:
			Log.i(TAG,"\t endElement Scene");
			if (localName.equalsIgnoreCase(ScnTags.S_SCENE)){
				pSceneDsc = null; 
				removeLastComponentDescriptor();
				nComponents=0;
				nStatus = STATUS_PARSE_SCENES;
			}
			else
				throw new NullPointerException("ParserXMLSceneDescriptor error invalid End Element STATUS_PARSE_SCENE");
			break;
			
		case STATUS_PARSE_SCENE_INSTANTIATION:
			Log.i(TAG,"\t endElement SceneInstantiation");
			if (localName.equalsIgnoreCase(ScnTags.S_SCENE_INSTANTIATION)){
				this.pSceneInstantiationDsc = null; 
				nStatus = STATUS_PARSE_SCENES;
			}
			else
				throw new NullPointerException("ParserXMLSceneDescriptor error invalid End Element STATUS_PARSE_SCENE");
			break;
		case STATUS_PARSE_COMPONENT:
			if (localName.equalsIgnoreCase(ScnTags.S_O_SPRITE)){
				Log.i(TAG,"\t\t end Element SPRITE");
				pSpriteDsc = null;
				removeLastComponentDescriptor();
				nComponents--;
			}
			else if (localName.equalsIgnoreCase(ScnTags.S_COLORING_SPRITE)){
				Log.i(TAG,"\t\t end Element COLOTING_SPRITE");
				removeLastComponentDescriptor();
				nComponents--;
			}
			else if(localName.equalsIgnoreCase(ScnTags.S_COMPOUND_SPRITE)){
				Log.i(TAG,"\t\t end Element COUPOND_SPRITE");
				pCompoundSpriteDsc = null;
				removeLastComponentDescriptor();
				nComponents--;
			}
			else if (localName.equalsIgnoreCase(ScnTags.S_TEXT)){
				Log.i(TAG,"\t\t end Element TEXT");
				removeLastComponentDescriptor();
				nComponents--;
			}
			else if(localName.equalsIgnoreCase(ScnTags.S_BACKGROUND)){
				Log.i(TAG,"\t\t end Element BACKGROUND");
				pBackGroundDescriptor = null;
				removeLastComponentDescriptor();
				nComponents--;
			}
			else if(localName.equalsIgnoreCase(ScnTags.S_ACTION)){
				Log.i(TAG,"\t\t end Element ACTION");
				pAction = null;
			}
			else if(localName.equalsIgnoreCase(ScnTags.S_PUZZLE_SPRITE)){
				Log.i(TAG,"\t\t end Element S_PUZZLE_SPRITE");
				removeLastComponentDescriptor();
				nComponents--;
			}
			else if(localName.equalsIgnoreCase(ScnTags.S_HUD)){
				Log.i(TAG,"\t\t end Element S_HUD");
				//FT pHUDDsc = null;
				removeLastComponentDescriptor();
				nComponents--;
			}
			else if(localName.equalsIgnoreCase(ScnTags.S_TOOLBAR)){
				Log.i(TAG,"\t\t end Element S_TOOLBAR");
				removeLastComponentDescriptor();
				nComponents--;
			}
			else if(localName.equalsIgnoreCase(ScnTags.S_BUTTON)){
				Log.i(TAG,"\t\t end Element S_BUTTON");
				removeLastComponentDescriptor();
				nComponents--;
			}
			else if(localName.equalsIgnoreCase(ScnTags.S_SCENELAUNCHER)){
				Log.i(TAG,"\t\t end Element S_BN_SCENELAUNCHER");
				removeLastComponentDescriptor();
				nComponents--;
			}
			else
				throw new NullPointerException("ParserXMLSceneDescriptor error invalid End Element:" +  localName);

			if(nComponents == 0)
				nStatus = STATUS_PARSE_SCENE;
			break;
		case STATUS_PARSE_EVENT_HANDLER:
			if (localName.equalsIgnoreCase(ScnTags.S_EVENT_HANDLER)){
				Log.i(TAG,"\t\t\t end Element EVENT HANDLER");
				pAction = null; 
				nStatus = this.nStatusPrec;
			}
			break;
		case STATUS_PARSE_ACTITIVY_MODIFIER:
			if (localName.equalsIgnoreCase(ScnTags.S_MODIFIER_LIST)){
				Log.i(TAG,"\t\t\t end Modifier List");
				nStatus = STATUS_PARSE_EVENT_HANDLER;
			}
			break;
		default: 
			break;
		}
	}
	public void removeLastComponentDescriptor(){
		Log.i(TAG,"\t\t\t\t remove last Descriptor from the List");
				//Save father descriptor to LIFO
		if(!pDescriptorsInProcessing.isEmpty())
			pCurrentDescriptorInProcessing = pDescriptorsInProcessing.removeLast();
	}

	public void characters(char[] ch,int start, int length)	throws SAXException{
		//String lecture = new String(ch,start,length);
	}
}
