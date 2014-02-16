package com.welmo.andengine.scenes.descriptors;


import java.util.LinkedList;
import java.util.StringTokenizer;







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
import com.welmo.andengine.scenes.descriptors.components.ColoringSpriteDescriptor;
import com.welmo.andengine.scenes.descriptors.components.GameLevel;
import com.welmo.andengine.scenes.descriptors.components.HUDDescriptor;
import com.welmo.andengine.scenes.descriptors.components.MultiViewSceneDescriptor;
import com.welmo.andengine.scenes.descriptors.components.PuzzleObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ScnTags;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.TextObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.ToolsBarDescriptor;
import com.welmo.andengine.scenes.descriptors.components.BasicObjectDescriptor.Alignment;
import com.welmo.andengine.scenes.descriptors.components.BasicObjectDescriptor.ICharacteristics;
import com.welmo.andengine.scenes.descriptors.components.BasicObjectDescriptor.IDimension;
import com.welmo.andengine.scenes.descriptors.components.BasicObjectDescriptor.IOrientation;
import com.welmo.andengine.scenes.descriptors.components.BasicObjectDescriptor.IPosition;
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
	protected TextObjectDescriptor				pTextDescriptor			= null;
	protected BackGroundObjectDescriptor		pBackGroundDescriptor	= null;
	protected SpriteObjectDescriptor 			pSpriteDsc				= null;
	protected SpriteObjectDescriptor 			pCompoundSpriteDsc		= null;
	protected PuzzleObjectDescriptor 			pPuzzleDsc				= null;
	protected HUDDescriptor 					pHUDDsc					= null;
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
			if((newDescriptor = parseComponentDescriptor(localName, attributes)) != null)					
				addComponentDescriptor(newDescriptor);
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
		if (localName.equalsIgnoreCase(ScnTags.S_SCENE_INSTANTIATION)){
			nStatus = STATUS_PARSE_SCENE_INSTANTIATION;
			return readSceneInstantiationDescription(attributes);
		}
		return null;
	}
	public BasicDescriptor parseComponentDescriptor(String localName, Attributes attributes){ 
		Log.i(TAG,"\t parseComponentDescriptor");
		BasicDescriptor newDescriptor = null;

		if (localName.equalsIgnoreCase(ScnTags.S_O_SPRITE))
			newDescriptor = readSpriteDescription(attributes);
		else if (localName.equalsIgnoreCase(ScnTags.S_COLORING_SPRITE))
			//newDescriptor = readColoringSpriteDescription(attributes);
			(newDescriptor = new ColoringSpriteDescriptor()).readXMLDescription(attributes);	
		else if (localName.equalsIgnoreCase(ScnTags.S_COMPOUND_SPRITE))
			newDescriptor = readCupondSprite(attributes);
		else if (localName.equalsIgnoreCase(ScnTags.S_TEXT))
			newDescriptor = readTextDescription(attributes);
		else if (localName.equalsIgnoreCase(ScnTags.S_BACKGROUND))
			newDescriptor = readBackGroudDescription(attributes); //Read new descriptor	
		else if (localName.equalsIgnoreCase(ScnTags.S_PUZZLE_SPRITE))
			newDescriptor = readPuzzleDescription(attributes); //Read new descriptor	
		else if(localName.equalsIgnoreCase(ScnTags.S_HUD))
			(newDescriptor = new HUDDescriptor()).readXMLDescription(attributes);
		else if(localName.equalsIgnoreCase(ScnTags.S_TOOLBAR))
			(newDescriptor = new ToolsBarDescriptor()).readXMLDescription(attributes);	
		else if(localName.equalsIgnoreCase(ScnTags.S_BUTTON))
			(newDescriptor = new ButtonDescriptor()).readXMLDescription(attributes);	
		
		else
			return null;

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
			newDescriptor = readAction(attributes);
			pEventDscMgr.addAction(pAction.event,pCurrentDescriptorInProcessing,pAction);
		}
		return newDescriptor;
	}
	public BasicModifierDescriptor parseComponentEventHandlerDescriptor(String localName, Attributes attributes){ 
		Log.i(TAG,"\t parseComponentEventHandlerDescriptor");
		BasicModifierDescriptor newDescriptor=null;
		
		if (nStatus==STATUS_PARSE_SCENE){
			if(localName.equalsIgnoreCase(ScnTags.S_EVENT_HANDLER)){
				newDescriptor = readComponentEventHandlerDescriptor(attributes);
				nStatus = STATUS_PARSE_EVENT_HANDLER;
				nStatusPrec=STATUS_PARSE_SCENE;
				return newDescriptor;
			}
			else
				return null;
		}
		if (nStatus==STATUS_PARSE_COMPONENT) 
			if(localName.equalsIgnoreCase(ScnTags.S_EVENT_HANDLER)){
				newDescriptor = readComponentEventHandlerDescriptor(attributes);
				nStatus = STATUS_PARSE_EVENT_HANDLER;
				nStatusPrec=STATUS_PARSE_COMPONENT;
				return newDescriptor;
			}
			else
				return null;
		if (nStatus==STATUS_PARSE_EVENT_HANDLER){ 
			if(localName.equalsIgnoreCase(ScnTags.S_MODIFIER_LIST)){
				Log.i(TAG,"\t\t readComponentModifierListDescriptor " + localName);
				pEventHandler.modifierSet = readComponentModifierListDescriptor(attributes);
				nStatus = STATUS_PARSE_ACTITIVY_MODIFIER;
				return pEventHandler.modifierSet;
			}
			else{ 
				Log.i(TAG,"\t\t readActionDescriptoon ");
				SceneActions newActionDescriptoon = new SceneActions();
				if(localName.equalsIgnoreCase(ScnTags.S_PRE_MOD_ACTION)){
					Log.i(TAG,"\t\t readComponentActionDescriptor PreModifierAction ");
					newActionDescriptoon = readComponentActionDescriptor(attributes);
					pEventHandler.preModAction.add(newActionDescriptoon);
				}
				else if(localName.equalsIgnoreCase(ScnTags.S_POST_MOD_ACTION)){
					Log.i(TAG,"\t\t readComponentActionDescriptor PostModifierAction ");
					newActionDescriptoon = readComponentActionDescriptor(attributes);
					pEventHandler.postModAction.add(newActionDescriptoon);
				}
				else if(localName.equalsIgnoreCase(ScnTags.S_ON_MOD_ACTION)){
					Log.i(TAG,"\t\t readComponentActionDescriptor OnModifierAction ");
					newActionDescriptoon = readComponentActionDescriptor(attributes);
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
		if (localName.equalsIgnoreCase(ScnTags.S_MODIFIER))
			newDescriptor = readComponentModifierDescriptor(attributes);
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
				pSceneDsc = readMemorySeceneDescriptor(new MemorySceneDescriptor(),attr);
				pSceneDsc.setSceneType(SceneType.MEMORY);
				break;
			case DEFAULT:
				pSceneDsc = ReadDefaultSceneDescriptor(new SceneDescriptor(),attr);
				pSceneDsc.setSceneType(SceneType.DEFAULT);
				break;
			default:
				pSceneDsc = ReadDefaultSceneDescriptor(new SceneDescriptor(),attr);
				pSceneDsc.setSceneType(SceneType.DEFAULT);
				break;
			}
		}
		else{
			pSceneDsc = ReadDefaultSceneDescriptor(new SceneDescriptor(),attr);
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
	private ConfiguredSceneDescriptor readSceneInstantiationDescription(Attributes attr){
		Log.i(TAG,"\t readSceneInstantiation");
		if(this.pSceneInstantiationDsc != null)
			throw new NullPointerException("ParserXMLSceneDescriptor encountered scene instantiation inside another scene instantiation");

		this.pSceneInstantiationDsc = new ConfiguredSceneDescriptor();
		
		if(attr.getValue(ScnTags.S_A_NAME)!=null)
			pSceneInstantiationDsc.setSceneName(attr.getValue(ScnTags.S_A_NAME));
		
		if(attr.getValue(ScnTags.S_A_SCENEMASTER)!=null)
				pSceneInstantiationDsc.setNameOfSceneMaster(attr.getValue(ScnTags.S_A_SCENEMASTER));
		
		int nbOfAttributes = attr.getLength();
		String value=null;
		String qName=null;
		for (int index=0; index<nbOfAttributes; index++){
			qName = attr.getQName(index);
			value = attr.getValue(index);
			pSceneInstantiationDsc.addParameter(qName, value);
		}
		
		pSceneDescManager.addCFGScene(pSceneInstantiationDsc.getSceneName(), pSceneInstantiationDsc);
		return this.pSceneInstantiationDsc;
	}
	private MemorySceneDescriptor readMemorySeceneDescriptor(MemorySceneDescriptor pScene, Attributes attr){
		
		// Read default values
		ReadDefaultSceneDescriptor(pScene,attr);
		int stdCardH=0;
		int stdCardW=0;
		String resourceName="";
		float flipTime=0;
		
		
		if(attr.getValue(ScnTags.S_A_MAX_LEVELS)!= null)
			pScene.setMaxLevelAllowed(GameLevel.valueOf(attr.getValue(ScnTags.S_A_MAX_LEVELS)));
		if(attr.getValue(ScnTags.S_A_TOPBOTTOMBORDER)!= null)
			pScene.setTopBottomBorder(Integer.parseInt(attr.getValue(ScnTags.S_A_TOPBOTTOMBORDER)));
		if(attr.getValue(ScnTags.S_A_LEFTRIGHTBORDER)!= null)
			pScene.setLeftRightBorder(Integer.parseInt(attr.getValue(ScnTags.S_A_LEFTRIGHTBORDER)));
		if(attr.getValue(ScnTags.S_A_VINTREBORDER)!= null)
			pScene.setVIntraBorder(Integer.parseInt(attr.getValue(ScnTags.S_A_VINTREBORDER)));
		if(attr.getValue(ScnTags.S_A_HINTREBORDER)!= null)
			pScene.setHIntraBorder(Integer.parseInt(attr.getValue(ScnTags.S_A_HINTREBORDER)));
		if(attr.getValue(ScnTags.S_A_MAXNBOFSYMBOLS)!= null)
			pScene.setMaxNbOfSymbols(Integer.parseInt(attr.getValue(ScnTags.S_A_MAXNBOFSYMBOLS)));
		if(attr.getValue(ScnTags.S_A_STDCARDHEIGHT)!= null){
			stdCardH=Integer.parseInt(attr.getValue(ScnTags.S_A_STDCARDHEIGHT));
			pScene.setStdCardHeight(stdCardH);
		}
		if(attr.getValue(ScnTags.S_A_STDCARDWIDTH)!= null){
			stdCardW=Integer.parseInt(attr.getValue(ScnTags.S_A_STDCARDWIDTH));
			pScene.setStdCardWidth(stdCardW);
		}
		if(attr.getValue(ScnTags.S_A_RESOURCES)!= null){
			resourceName = attr.getValue(ScnTags.S_A_RESOURCES);
			pScene.setResouceName(resourceName);
		}
		if(attr.getValue(ScnTags.S_A_FLIP_TIME)!= null){
			pScene.setFlipTime(Float.parseFloat(attr.getValue(ScnTags.S_A_FLIP_TIME)));
			flipTime = pScene.getFlipTime();
		}
		if(attr.getValue(ScnTags.S_A_WAIT_BACK_FLIP)!= null){
			pScene.setWaitBackFlip(Float.parseFloat(attr.getValue(ScnTags.S_A_WAIT_BACK_FLIP)));
		}
		
		//Parse JSON strings
		JSONObject jObject;
		//Geometry
		try {
			//Parse Geometry
			if(attr.getValue(ScnTags.S_A_GEOMETRY)!= null){
				jObject = new JSONObject(attr.getValue(ScnTags.S_A_GEOMETRY));
				JSONArray geometries = jObject.getJSONArray(ScnTags.S_A_GEOMETRY);

				if((pScene.getMaxLevelAllowed()).ordinal() > geometries.length())
					pScene.setMaxLevelAllowed(GameLevel.fromOrdinal(geometries.length()));

				//create array will contians the geometry
				int[][] geometryArray = new int[geometries.length()][MemorySceneDescriptor.GEOMETRY_DSC_NBCOL];

				//parse the array
				for (int i=0; i<geometries.length(); i++){
					JSONArray currLine = (JSONArray)geometries.get(i);
					for (int j=0; j<MemorySceneDescriptor.GEOMETRY_DSC_NBCOL; j++){
						geometryArray[i][j]=currLine.getInt(j);
					}
					pScene.setMemoryStructure(geometryArray);
				} 
			}
			//Parse tiled Maps
			if(attr.getValue(ScnTags.S_A_MAPCARDTILES)!= null){
				jObject = new JSONObject(attr.getValue(ScnTags.S_A_MAPCARDTILES));
				JSONArray mapOfTiles = jObject.getJSONArray(ScnTags.S_A_MAPCARDTILES);

				if(pScene.getMaxNbOfSymbols() > mapOfTiles.length())
					pScene.setMaxNbOfSymbols(mapOfTiles.length());

				//create array will contains the geometry
				int[][] mapOfCardTiles = new int[mapOfTiles.length()][3];

				//parse the array
				for (int i=0; i<mapOfTiles.length(); i++){
					JSONArray currLine = (JSONArray)mapOfTiles.get(i);
					
					//create a sprite descriptor
					SpriteObjectDescriptor oCardDsc  = new SpriteObjectDescriptor();
					oCardDsc.setClassName("com.welmo.andengine.scenes.components.CardSprite");
					oCardDsc.ID = currLine.getInt(0);
					oCardDsc.setSidesTiles(currLine.getInt(1), currLine.getInt(2));
					oCardDsc.getIDimension().setHeight(stdCardH);
					oCardDsc.getIDimension().setWidth(stdCardW);
					oCardDsc.getIPosition().setX(0);
					oCardDsc.getIPosition().setY(0);
					oCardDsc.getIPosition().setZorder(0);
					oCardDsc.textureName = new String(resourceName);
					oCardDsc.setSoundName(currLine.getString(3));
					oCardDsc.setType(SpritesTypes.CLICKABLE);
					pScene.pChild.put(oCardDsc.ID,oCardDsc);
	
					ComponentEventHandlerDescriptor pNewEvent = new ComponentEventHandlerDescriptor();
					
					//create card event actions & modifiers
					pNewEvent.event = Events.ON_CLICK;
					pNewEvent.setID(0);
					
					//flip action 
					SceneActions action1 = new SceneActions();
					//Disable
					action1.type = ActionType.DISABLE_SCENE_TOUCH;
					pNewEvent.preModAction.add(action1);
					//Flip
					SceneActions action2 = new SceneActions();
					action2.type = ActionType.FLIP;
					action2.flipTime = flipTime;
					pNewEvent.preModAction.add(action2);

					//sound on going  
					SceneActions action3 = new SceneActions();
					action3.type = ActionType.PLAY_SOUND;
					action3.resourceName = currLine.getString(3);
					pNewEvent.preModAction.add(action3);
					
					//Disable
					SceneActions action4 = new SceneActions();
					action4.type = ActionType.ENABLE_SCENE_TOUCH;
					pNewEvent.postModAction.add(action4);
				
					oCardDsc.pEventHandlerList.put(pNewEvent.event, pNewEvent);
				}
				pScene.setMemoryMapOfCardsTiles(mapOfCardTiles);
			} 
		}
		catch (JSONException e) {
		}		
		return pScene;
	}
	private SceneDescriptor ReadDefaultSceneDescriptor(SceneDescriptor pScene, Attributes attr){
		// Read scene description	
		pScene.sceneName = new String(attr.getValue(ScnTags.S_A_NAME));
		if(attr.getValue(ScnTags.S_FATHER)!= null)
			pScene.sceneFather = new String(attr.getValue(ScnTags.S_FATHER));
		if(attr.getValue(ScnTags.S_CLASS_NAME)!= null)
			pScene.className = new String(attr.getValue(ScnTags.S_CLASS_NAME));
		if(attr.getValue(ScnTags.S_A_PINTCHZOOM)!= null)
			pScene.setPinchAndZoom(Boolean.parseBoolean(attr.getValue(ScnTags.S_A_PINTCHZOOM)));
		if(attr.getValue(ScnTags.S_A_HUD)!= null)
			pScene.hasHUD(Boolean.parseBoolean(attr.getValue(ScnTags.S_A_HUD)));
		return pScene;
	}
	private BackGroundObjectDescriptor readBackGroudDescription(Attributes attributes){
		Log.i(TAG,"\t\t readBackGroudDescription");
		if(this.pBackGroundDescriptor != null) //check if new action object descriptor
			throw new NullPointerException("ParserXMLSceneDescriptor encountered background description with another background description inside");
		
		pBackGroundDescriptor = new BackGroundObjectDescriptor();
	
		pBackGroundDescriptor.ID=Integer.parseInt(attributes.getValue(ScnTags.S_A_ID ));
		pBackGroundDescriptor.type=BackGroundObjectDescriptor.BackGroundTypes.valueOf(attributes.getValue(ScnTags.S_A_TYPE));
		switch (pBackGroundDescriptor.type){
		case COLOR:
			pBackGroundDescriptor.color=attributes.getValue(ScnTags.S_A_COLOR);
			break;
		default:
			break;
		}	
		return pBackGroundDescriptor;
	}
	private SpriteObjectDescriptor readSpriteDescription(Attributes attr){
		Log.i(TAG,"\t\t readSpriteDescription");
		
		pSpriteDsc = new SpriteObjectDescriptor();
		
		// Read the sprite
		pSpriteDsc.ID=Integer.parseInt(attr.getValue(ScnTags.S_A_ID));
		Log.i(TAG,"\t\t readSpriteDescription ID " + pSpriteDsc.ID);
		pSpriteDsc.textureName = new String(attr.getValue(ScnTags.S_A_RESOURCE_NAME));
		if(attr.getValue(ScnTags.S_A_TYPE)!= null)
			pSpriteDsc.type = SpriteObjectDescriptor.SpritesTypes.valueOf(attr.getValue(ScnTags.S_A_TYPE));
		else
			pSpriteDsc.type = SpriteObjectDescriptor.SpritesTypes.valueOf("STATIC");

		//parse position, dimension & orientation attributes
		this.parseAttributesPosition(pSpriteDsc.getIPosition(),attr);
		this.parseAttributesDimensions(pSpriteDsc.getIDimension(),attr);
		this.parseAttributesOrientation(pSpriteDsc.getIOriantation(),attr);
		this.parseAttributesCharacteristics(pSpriteDsc.getICharacteristis(),attr);
	
		if(attr.getValue(ScnTags.S_CLASS_NAME)!= null)
			pSpriteDsc.className = new String(attr.getValue(ScnTags.S_CLASS_NAME));
		
		if((attr.getValue(ScnTags.S_A_SIDEA)!= null) && (attr.getValue(ScnTags.S_A_SIDEB) != null)){
			pSpriteDsc.nSideATile = Integer.parseInt(attr.getValue(ScnTags.S_A_SIDEA));
			pSpriteDsc.nSideBTile = Integer.parseInt(attr.getValue(ScnTags.S_A_SIDEB));
		}
		
	
		return pSpriteDsc;
	}	
	@SuppressWarnings("unused")
	private SpriteObjectDescriptor readColoringSpriteDescription(Attributes attr){
	Log.i(TAG,"\t\t readColoringSpriteDescription");
		
		pSpriteDsc = new SpriteObjectDescriptor();
		
		// Read the sprite
		pSpriteDsc.ID=Integer.parseInt(attr.getValue(ScnTags.S_A_ID));
		
		//parse position, dimension & orientation attributes
		this.parseAttributesPosition(pSpriteDsc.getIPosition(),attr);
		this.parseAttributesDimensions(pSpriteDsc.getIDimension(),attr);
		this.parseAttributesOrientation(pSpriteDsc.getIOriantation(),attr);
		this.parseAttributesCharacteristics(pSpriteDsc.getICharacteristis(),attr);
	
		return pSpriteDsc;
	}
	private PuzzleObjectDescriptor readPuzzleDescription(Attributes attr){
		Log.i(TAG,"\t\t readPuzzleDescription");

		PuzzleObjectDescriptor pPuzzleDsc = null;

		pPuzzleDsc = new PuzzleObjectDescriptor();


		// Read the puzzle 
		pPuzzleDsc.ID=Integer.parseInt(attr.getValue(ScnTags.S_A_ID));

		pPuzzleDsc.textureName = new String(attr.getValue(ScnTags.S_A_RESOURCE_NAME));

		//parse position, dimension & orientation attributes
		this.parseAttributesPosition(pPuzzleDsc.getIPosition(),attr);
		this.parseAttributesDimensions(pPuzzleDsc.getIDimension(),attr);
		this.parseAttributesOrientation(pPuzzleDsc.getIOriantation(),attr);
		this.parseAttributesCharacteristics(pPuzzleDsc.getICharacteristis(),attr);
			
		// Read specific puzzle parameters Nb cols & Nb Rows
		if(attr.getValue(ScnTags.S_A_NBCOLS)!= null)
			pPuzzleDsc.setNbColumns(Integer.parseInt(attr.getValue(ScnTags.S_A_NBCOLS)));
		else
			pPuzzleDsc.setNbColumns(1);
		if(attr.getValue(ScnTags.S_A_NBROWS)!= null)
			pPuzzleDsc.setNbRows(Integer.parseInt(attr.getValue(ScnTags.S_A_NBROWS)));
		else
			pPuzzleDsc.setNbRows(1);
		
		// Read specific puzzle geometries
		float[] geometry = new float[4];
		// Read specific puzzle zone
		if(attr.getValue(ScnTags.S_A_PUZZLE_ZONE)!= null){
			int i=0;
			StringTokenizer st = new StringTokenizer(attr.getValue(ScnTags.S_A_PUZZLE_ZONE),";");
		    while (st.hasMoreTokens()) {
		    	geometry[i++]=Float.parseFloat(st.nextToken());
		     }
			pPuzzleDsc.setPuzzleZoneGeometry(geometry);
		}
		// Read specific puzzle box
		if(attr.getValue(ScnTags.S_A_PUZZLE_BOX)!= null){
			int i=0;
			StringTokenizer st = new StringTokenizer(attr.getValue(ScnTags.S_A_PUZZLE_BOX),";");
		    while (st.hasMoreTokens()) {
		    	geometry[i++]=Float.parseFloat(st.nextToken());
		     }
			pPuzzleDsc.setPieceBoxGeometry(geometry);
		}
		// Read if active border
		if(attr.getValue(ScnTags.S_A_ACTIVE_BORDER)!= null)
			pPuzzleDsc.setHasActiveBorder(Boolean.parseBoolean(attr.getValue(ScnTags.S_A_ACTIVE_BORDER)));
		else
			pPuzzleDsc.setHasActiveBorder(false);

		// Read if active zone
		if(attr.getValue(ScnTags.S_A_ACTIVE_ZONE)!= null)
			pPuzzleDsc.setHasActiveZone(Boolean.parseBoolean(attr.getValue(ScnTags.S_A_ACTIVE_ZONE)));
		else
			pPuzzleDsc.setHasActiveZone(false);
		// Read if helper image on
		if(attr.getValue(ScnTags.S_A_HELPER_IMAGE)!= null)
			pPuzzleDsc.setHelperImage(attr.getValue(ScnTags.S_A_HELPER_IMAGE));
		
		// Read if helper image on
		if(attr.getValue(ScnTags.S_A_HELPER_IMG_ALPHA)!= null)
			pPuzzleDsc.setHelperImageAlpha(Float.parseFloat(attr.getValue(ScnTags.S_A_HELPER_IMG_ALPHA)));
		else
			pPuzzleDsc.setHelperImageAlpha(1.0f);
		
		return pPuzzleDsc;
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
		this.parseAttributesPosition(pCompoundSpriteDsc.getIPosition(),attr);

		//add compound sprite to scene
		pSceneDsc.pChild.put(pCompoundSpriteDsc.ID,pCompoundSpriteDsc);	
		return pCompoundSpriteDsc;
	}
	private TextObjectDescriptor readTextDescription(Attributes attributes){
		Log.i(TAG,"\t\t readTextDescription");
		//create new action
		pTextDescriptor = new TextObjectDescriptor();

		pTextDescriptor.ID=Integer.parseInt(attributes.getValue(ScnTags.S_A_ID ));
		pTextDescriptor.FontName = attributes.getValue(ScnTags.S_A_RESOURCE_NAME);
		pTextDescriptor.message = new String (attributes.getValue(ScnTags.S_A_MESSAGE));	
		pTextDescriptor.type=TextObjectDescriptor.TextTypes.valueOf(attributes.getValue(ScnTags.S_A_TYPE));

		this.parseAttributesPosition(pTextDescriptor.getIPosition(),attributes);
		this.parseAttributesOrientation(pTextDescriptor.getIOriantation(),attributes);
		this.parseAttributesCharacteristics(pTextDescriptor.getICharacteristis(),attributes);
		
		return pTextDescriptor;
	} 
	private ComponentEventHandlerDescriptor readComponentEventHandlerDescriptor(Attributes attributes){
		Log.i(TAG,"\t\t ComponentEventHandlerDescriptor");
		//create new action
		ComponentEventHandlerDescriptor pDescriptor = new ComponentEventHandlerDescriptor();

		if(attributes.getValue(ScnTags.S_A_ID) != null)
			pDescriptor.setID(Integer.parseInt(attributes.getValue(ScnTags.S_A_ID)));
		
		if(attributes.getValue(ScnTags.S_A_CLONEID) != null)
			pDescriptor.setCloneID(Integer.parseInt(attributes.getValue(ScnTags.S_A_CLONEID)));
		
		pDescriptor.event=ComponentEventHandlerDescriptor.Events.valueOf(attributes.getValue(ScnTags.S_A_EVENT));
		return pDescriptor;
	}
	@SuppressWarnings("incomplete-switch")
	private ComponentModifierDescriptor readComponentModifierDescriptor(Attributes attributes){
		Log.i(TAG,"\t\t readComponentModifierDescriptor");
		//create new action
		ComponentModifierDescriptor pDescriptor = new ComponentModifierDescriptor();

		pDescriptor.getIModifier().setType(ComponentModifierDescriptor.ModifierType.valueOf(attributes.getValue(ScnTags.S_A_TYPE)));
		switch(pDescriptor.getIModifier().getType()){
		case SCALE:
			pDescriptor.getIModifier().setScaleBegin(Float.parseFloat(attributes.getValue(ScnTags.S_A_SCALE_BEGIN)));
			pDescriptor.getIModifier().setScaleEnd(Float.parseFloat(attributes.getValue(ScnTags.S_A_SCALE_END)));
			if(attributes.getValue(ScnTags.S_A_DURATION) != null)
				pDescriptor.getIModifier().setDuration(Float.parseFloat(attributes.getValue(ScnTags.S_A_DURATION)));
			break;
		case SOUND:
			pDescriptor.getIModifier().setSoundName(attributes.getValue(ScnTags.S_A_NAME));
			break;
		}
		return pDescriptor;
	}
	public ComponentModifierListDescriptor readComponentModifierListDescriptor(Attributes attributes){ 
		Log.i(TAG,"\t\t readComponentModifierListDescriptor");
		ComponentModifierListDescriptor newDescriptor=new ComponentModifierListDescriptor();
		String tagString = attributes.getValue(ScnTags.S_A_EXECUTION_ORDER);
		Log.i(TAG,"\t\t readComponentModifierListDescriptor " + tagString);
		newDescriptor.getIModifierList().setExecOrder(ExecutionOrder.valueOf(
				(attributes.getValue(ScnTags.S_A_EXECUTION_ORDER))));
		return newDescriptor;
	}
	public SceneActions readComponentActionDescriptor(Attributes attributes){
		Log.i(TAG,"\t\t readComponentActionDescriptor");
		SceneActions newAction = new SceneActions();
		newAction.type = ActionType.valueOf(attributes.getValue(ScnTags.S_A_TYPE));
		switch(newAction.type){
		case PLAY_SOUND:
			newAction.resourceName = new String(attributes.getValue(ScnTags.S_A_RESOURCE_NAME));
			break;
		case CHANGE_SCENE:
			newAction.NextScene = new String(attributes.getValue(ScnTags.S_A_NEXT_SCENE));
			break;
		case CHANGE_Z_ORDER:
			newAction.ZIndex = Integer.parseInt(attributes.getValue(ScnTags.S_A_Z_ORDER));
			break;
		case FLIP:
			if(attributes.getValue(ScnTags.S_A_FLIP_TIME) != null)
					newAction.flipTime = Integer.parseInt(attributes.getValue(ScnTags.S_A_FLIP_TIME));
			break;
		case ON_MOVE_FOLLOW:
			break;
		default:
			break;
		}
		return newAction;
	}
	//-------------------------------------------------------------------------------------
	// Private functions to read the action/modifiers 
	//-------------------------------------------------------------------------------------
	private SceneActions readAction(Attributes attr){
		Log.i(TAG,"\t\t\t read Action");
		if(this.pAction != null) //check if new action object descriptor
			throw new NullPointerException("ParserXMLSceneDescriptor encountered action description with another action description inside");
		if(this.pSpriteDsc == null && this.pCompoundSpriteDsc == null ) //check if action is part of a sprite
			throw new NullPointerException("ParserXMLSceneDescriptor encountered acton description not in a sprite or compound sprite");

		//create new action
		pAction = new SceneActions();

		// read type and init the correct parameter as per action type
		pAction.type=SceneActions.ActionType.valueOf(attr.getValue(ScnTags.S_A_TYPE));
		switch (SceneActions.ActionType.valueOf(attr.getValue(ScnTags.S_A_TYPE))){
		case CHANGE_SCENE:
			pAction.NextScene=attr.getValue(ScnTags.S_A_NEXT_SCENE);
			break;
		case STICK:	
			pAction.stick_with=Integer.parseInt(attr.getValue(ScnTags.S_A_STICK_WITH));
			pAction.stickMode=Stick.StickMode.valueOf(attr.getValue(ScnTags.S_A_STICK_MODE));
			break;
		default:
			break;
		}

		//the event
		pAction.event = ComponentEventHandlerDescriptor.Events.valueOf(attr.getValue(ScnTags.S_A_EVENT));
		return pAction;
	}
	//-------------------------------------------------------------------------------------
	// Specfic private functions to read the attributes
	//-------------------------------------------------------------------------------------
	private void parseAttributesPosition(IPosition pPosition,Attributes attributes){
		Log.i(TAG,"\t\t\t parseAttributesPosition");
		if((attributes.getValue(ScnTags.S_A_POSITION_X) != null) && (attributes.getValue(ScnTags.S_A_POSITION_Y) != null)){
			pPosition.setX(dimHelper.parsPosition(ScreenDimensionHelper.X,attributes.getValue(ScnTags.S_A_POSITION_X)));
			pPosition.setY(dimHelper.parsPosition(ScreenDimensionHelper.Y,attributes.getValue(ScnTags.S_A_POSITION_Y)));
		}
		else{
			pPosition.setX(0);
			pPosition.setY(0);
		}
		//read Z Order
		if(attributes.getValue(ScnTags.S_A_Z_ORDER) != null){ 
			pPosition.setZorder(Integer.parseInt(attributes.getValue(ScnTags.S_A_Z_ORDER)));
		}
		else{
			pPosition.setZorder(0);
		}
		//read H Alignment
		if(attributes.getValue(ScnTags.S_A_H_ALIGNEMENT) != null){ 
			pPosition.setHorizontalAlignment(Alignment.valueOf(attributes.getValue(ScnTags.S_A_H_ALIGNEMENT)));
		}
		else{
			pPosition.setHorizontalAlignment(Alignment.NO_ALIGNEMENT);
		}
		//read V Alignment
		if(attributes.getValue(ScnTags.S_A_H_ALIGNEMENT) != null){ 
			pPosition.setVerticalAlignment(Alignment.valueOf(attributes.getValue(ScnTags.S_A_V_ALIGNEMENT)));
		}
		else{
			pPosition.setVerticalAlignment(Alignment.NO_ALIGNEMENT);
		}
	}
	private void parseAttributesDimensions(IDimension pDimensions,Attributes attributes){
		Log.i(TAG,"\t\t\t parseAttributesDimensions");
		if((attributes.getValue(ScnTags.S_A_WIDTH) != null) && (attributes.getValue(ScnTags.S_A_WIDTH) != null)){
			pDimensions.setWidth(dimHelper.parsLenght(ScreenDimensionHelper.W, attributes.getValue(ScnTags.S_A_WIDTH)));
			pDimensions.setHeight(dimHelper.parsLenght(ScreenDimensionHelper.H, attributes.getValue(ScnTags.S_A_HEIGHT)));
		}
		else{
			pDimensions.setWidth(100);
			pDimensions.setHeight(100);
		}
	}
	private void parseAttributesOrientation(IOrientation pDimensions,Attributes attributes){
		Log.i(TAG,"\t\t\t parseAttributesOrientation");
		
		if(attributes.getValue(ScnTags.S_A_ORIENTATION) != null)
			pDimensions.setOrientation(Float.parseFloat(attributes.getValue(ScnTags.S_A_ORIENTATION)));
		else
			pDimensions.setOrientation(0f);
		
		if(attributes.getValue(ScnTags.S_A_ROTAION_CENTER_X) != null)
			pDimensions.setRotationCenterX(Float.parseFloat(attributes.getValue(ScnTags.S_A_ROTAION_CENTER_X)));
		else
			pDimensions.setRotationCenterX(0f);
		
		if(attributes.getValue(ScnTags.S_A_ROTAION_CENTER_Y) != null)
			pDimensions.setRotationCenterY(Float.parseFloat(attributes.getValue(ScnTags.S_A_ROTAION_CENTER_Y)));
		else
			pDimensions.setRotationCenterY(0f);
		
	}
	private void parseAttributesCharacteristics(ICharacteristics pCharacteristics,Attributes attributes){
		Log.i(TAG,"\t\t\t parseAttributesCharacteristics");
		if(attributes.getValue(ScnTags.S_A_COLOR) != null){
			Log.i(TAG,"parseAttributesCharacteristics Color = " + attributes.getValue(ScnTags.S_A_COLOR));
			pCharacteristics.setColor(attributes.getValue(ScnTags.S_A_COLOR));
			Log.i(TAG,"parseAttributesCharacteristics Color Set-up= " + pCharacteristics.getColor());
		}
		else
			pCharacteristics.setColor("");
	}
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
				pTextDescriptor = null;
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
				pSpriteDsc = null;
				removeLastComponentDescriptor();
				nComponents--;
			}
			else if(localName.equalsIgnoreCase(ScnTags.S_HUD)){
				Log.i(TAG,"\t\t end Element S_HUD");
				pHUDDsc = null;
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
