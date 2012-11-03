package com.welmo.andengine.scenes.descriptors.components;


import java.util.LinkedList;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.util.Log;

import com.welmo.andengine.managers.EventDescriptionsManager;
import com.welmo.andengine.managers.SceneDescriptorsManager;
import com.welmo.andengine.scenes.components.Stick;
import com.welmo.andengine.scenes.descriptors.components.BasicObjectDescriptor.Alignment;
import com.welmo.andengine.scenes.descriptors.components.BasicObjectDescriptor.ICharacteristics;
import com.welmo.andengine.scenes.descriptors.components.BasicObjectDescriptor.IDimension;
import com.welmo.andengine.scenes.descriptors.components.BasicObjectDescriptor.IOrientation;
import com.welmo.andengine.scenes.descriptors.components.BasicObjectDescriptor.IPosition;
import com.welmo.andengine.scenes.descriptors.events.ComponentModifierDescriptor;
import com.welmo.andengine.scenes.descriptors.events.EventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.SceneActionsSet;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.utility.ScreenDimensionHelper;


public class ParserXMLSceneDescriptor extends DefaultHandler {

	//--------------------------------------------------------
	// Variables
	//--------------------------------------------------------
	private static final String TAG = "ParserXMLSceneDescriptor";
	//Helpers
	private ScreenDimensionHelper				dimHelper=null;
	//Managers
	private EventDescriptionsManager			pEventDscMgr=null;
	private SceneDescriptorsManager				pSceneDescManager=null;
	//Scene Descriptor
	protected SceneDescriptor					pSceneDsc=null;
	protected MultiViewSceneDescriptor			pMultiViewSceneDsc=null;
	//Modifier descriptors
	protected SceneActionsSet					pAction=null;
	protected ComponentModifierDescriptor		pModifier=null;
	protected ComponentEventHandlerDescriptor	pModifierSet=null;
	//Object Descriptor
	protected TextObjectDescriptor				pTextDescriptor=null;
	protected BackGroundObjectDescriptor		pBackGroundDescriptor=null;
	protected SpriteObjectDescriptor 			pSpriteDsc=null;
	protected SpriteObjectDescriptor 			pCompoundSpriteDsc=null;
	//List to manage descriptor chain
	protected LinkedList<BasicDescriptor> 		pDescriptorsInProcessing=null;
	protected BasicDescriptor 					pCurrentDescriptorInProcessing=null;
	//List to manage parser status chain
	protected int								nStatus=0;
	protected int								nComponents=0;
	protected int								nModifiers=0;
	static final int STATUS_BEGIN				=1;
	static final int STATUS_PARSE_SCENES		=2;
	static final int STATUS_PARSE_SCENE			=3;
	static final int STATUS_PARSE_COMPONENT		=4;
	static final int STATUS_PARSE_MODIFIER		=5;
	//--------------------------------------------------------
	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		super.processingInstruction(target, data);
	}
	public ParserXMLSceneDescriptor(Context ctx) {
		super();
		dimHelper = ScreenDimensionHelper.getInstance(ctx);
		pEventDscMgr = EventDescriptionsManager.getInstance();
		pDescriptorsInProcessing = new LinkedList<BasicDescriptor>();
		pCurrentDescriptorInProcessing = null;
		nStatus = STATUS_BEGIN;
	}
	@Override
	// ===========================================================
	// * Cette m�thode est appel�e par le parser une et une seule
	// * fois au d�marrage de l'analyse de votre flux xml.
	// * Elle est appel�e avant toutes les autres m�thodes de l'interface,
	// * � l'exception unique, �videmment, de la m�thode setDocumentLocator.
	// * Cet �v�nement devrait vous permettre d'initialiser tout ce qui doit
	// * l'�tre avant led�but du parcours du document.
	// ===========================================================
	public void startDocument() throws SAXException {
		super.startDocument();
		nStatus = this.STATUS_BEGIN;
		pSceneDescManager = SceneDescriptorsManager.getInstance();
	}
	@Override
	/*
	 * Fonction �tant d�clench�e lorsque le parser trouve un tag XML
	 * C'est cette m�thode que nous allons utiliser pour instancier un nouveau feed
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
			else
				throw new NullPointerException("ParserXMLSceneDescriptor error invalid Element");
			break;
		case STATUS_PARSE_SCENE:
			if((newDescriptor = parseComponentDescriptor(localName, attributes)) != null)
				addComponentDescriptor(newDescriptor);
			else
				throw new NullPointerException("ParserXMLSceneDescriptor error invalid Element");
			break;
		case STATUS_PARSE_COMPONENT:
			if((newDescriptor = parseComponentDescriptor(localName, attributes)) != null)
				addComponentDescriptor(newDescriptor);
			else if((newDescriptor = parseActionDescriptor(localName, attributes)) != null)
				break;
			else if((pModifierSet = (ComponentEventHandlerDescriptor)parseComponentEventDescriptor(localName, attributes))!= null){
				if(pCurrentDescriptorInProcessing != null)
					pCurrentDescriptorInProcessing.pEventHandlerList.put(pModifierSet.event, pModifierSet);	
				break;
			}
			else
				throw new NullPointerException("ParserXMLSceneDescriptor error invalid Element");
		case STATUS_PARSE_MODIFIER:
			if((newDescriptor = parseComponentModifierDescriptor(localName, attributes))!=null)
				pModifierSet.ModifiersList.add((ComponentModifierDescriptor) newDescriptor);
			break;
		}
	}
	public void addComponentDescriptor(BasicDescriptor newDesciptor){
		Log.i(TAG,"\t\t\t\t add MOdifier Descriptor in List");
		//Save father descriptor to LIFO
		if(pCurrentDescriptorInProcessing != null) 
			pDescriptorsInProcessing.addLast(pCurrentDescriptorInProcessing); 
		
		pCurrentDescriptorInProcessing = newDesciptor;
		
		//attach new description as child of father descriptor
		if(!pDescriptorsInProcessing.isEmpty()) 
			pDescriptorsInProcessing.getLast().pChild.add(pCurrentDescriptorInProcessing); 
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
	public BasicDescriptor parseComponentDescriptor(String localName, Attributes attributes){ 
		BasicDescriptor newDescriptor = null;

		if (localName.equalsIgnoreCase(ScnTags.S_SPRITE))
			newDescriptor = readSpriteDescription(attributes);
		else if (localName.equalsIgnoreCase(ScnTags.S_COMPOUND_SPRITE))
			newDescriptor = readCupondSprite(attributes);
		else if (localName.equalsIgnoreCase(ScnTags.S_TEXT))
			newDescriptor = readTextDescription(attributes);
		else if (localName.equalsIgnoreCase(ScnTags.S_BACKGROUND))
			newDescriptor = readBackGroudDescription(attributes); //Read new descriptor	
		else 
			return null;

		//update status and components nb
		Log.i(TAG,"\t parseComponentDescriptor");
		nComponents ++;
		nStatus = STATUS_PARSE_COMPONENT;
		return newDescriptor;
	}
	public BasicDescriptor parseActionDescriptor(String localName, Attributes attributes){ 
		BasicDescriptor newDescriptor=null;
		if (localName.equalsIgnoreCase(ScnTags.S_ACTION)) {
			newDescriptor = readAction(attributes);
			pEventDscMgr.addAction(pAction.event,pCurrentDescriptorInProcessing,pAction);
		}
		nStatus = STATUS_PARSE_MODIFIER;
		return newDescriptor;
	}
	public BasicDescriptor parseComponentEventDescriptor(String localName, Attributes attributes){ 
		BasicDescriptor newDescriptor=null;
		if (localName.equalsIgnoreCase(ScnTags.S_EVENT_HANDLER)){
			newDescriptor = readComponentEventHandlerDescriptor(attributes);
		}
		else if (localName.equalsIgnoreCase(ScnTags.S_MODIFIER)){ 
			newDescriptor = readModifierDescription(attributes);
		}
		nStatus = STATUS_PARSE_MODIFIER;
		return newDescriptor;
	}
	public BasicDescriptor parseComponentModifierDescriptor(String localName, Attributes attributes){ 
		BasicDescriptor newDescriptor=null;
		if (localName.equalsIgnoreCase(ScnTags.S_MODIFIER)){
			newDescriptor = readComponentModifierDescriptor(attributes);
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

		pSceneDsc = new SceneDescriptor();

		// Read scene description
		pSceneDsc.sceneName = new String(attr.getValue(ScnTags.S_A_NAME));
		if(attr.getValue(ScnTags.S_FATHER)!= null)
			pSceneDsc.sceneFather = new String(attr.getValue(ScnTags.S_FATHER));
		
		if(attr.getValue(ScnTags.S_CLASS_NAME)!= null)
			pSceneDsc.className = new String(attr.getValue(ScnTags.S_CLASS_NAME));
		
		pSceneDescManager.addScene(pSceneDsc.sceneName, pSceneDsc);

		return pSceneDsc;
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
		pSpriteDsc.textureName = new String(attr.getValue(ScnTags.S_A_RESOURCE_NAME));
		pSpriteDsc.type = SpriteObjectDescriptor.SpritesTypes.valueOf(attr.getValue(ScnTags.S_A_TYPE));

		//parse position, dimension & orientation attributes
		this.parseAttributesPosition(pSpriteDsc.getIPosition(),attr);
		this.parseAttributesDimensions(pSpriteDsc.getIDimension(),attr);
		this.parseAttributesOrientation(pSpriteDsc.getIOriantation(),attr);
		this.parseAttributesCharacteristics(pSpriteDsc.getICharacteristis(),attr);
	
		return pSpriteDsc;
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
		pSceneDsc.pChild.add(pCompoundSpriteDsc);	
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

		pDescriptor.event=EventHandlerDescriptor.Events.valueOf(attributes.getValue(ScnTags.S_A_EVENT));
		pDescriptor.modifierListType=ComponentEventHandlerDescriptor.ModifiersListType.valueOf(attributes.getValue(ScnTags.S_A_TYPE));
		pDescriptor.ID=Integer.parseInt(attributes.getValue(ScnTags.S_A_ID));

		return pDescriptor;
	}
	private ComponentModifierDescriptor readComponentModifierDescriptor(Attributes attributes){
		Log.i(TAG,"\t\t ComponentEventHandlerDescriptor");
		//create new action
		ComponentModifierDescriptor pDescriptor = new ComponentModifierDescriptor();

		pDescriptor.setIsASet(Boolean.parseBoolean(attributes.getValue(ScnTags.S_A_TYPE_SET)));
		if(!pDescriptor.isASet()){
			pDescriptor.getIModifier().setType(ComponentModifierDescriptor.ModifierType.valueOf(attributes.getValue(ScnTags.S_A_TYPE)));
			switch(pDescriptor.getIModifier().getType()){
			case SCALE:
				pDescriptor.getIModifier().setScaleBegin(Float.parseFloat(attributes.getValue(ScnTags.S_A_SCALE_BEGIN)));
				pDescriptor.getIModifier().setScaleEnd(Float.parseFloat(attributes.getValue(ScnTags.S_A_SCALE_END)));
				break;
			case SOUND:
				pDescriptor.getIModifier().setSoundName(attributes.getValue(ScnTags.S_A_NAME));
				break;
			}
		}
		else{
			pDescriptor.getIModifierList().setModifierListType(
					ComponentEventHandlerDescriptor.ModifiersListType.valueOf(attributes.getType(ScnTags.S_A_TYPE)));	
		}
		return pDescriptor;
	}
	//-------------------------------------------------------------------------------------
	// Private functions to read the action/modifiers 
	//-------------------------------------------------------------------------------------
	private BasicDescriptor readAction(Attributes attr){
		Log.i(TAG,"\t\t\t readAction");
		if(this.pAction != null) //check if new action object descriptor
			throw new NullPointerException("ParserXMLSceneDescriptor encountered action description with another action description inside");
		if(this.pSpriteDsc == null && this.pCompoundSpriteDsc == null ) //check if action is part of a sprite
			throw new NullPointerException("ParserXMLSceneDescriptor encountered acton description not in a sprite or compound sprite");

		//create new action
		pAction = new SceneActionsSet();

		// read type and init the correct parameter as per action type
		pAction.type=SceneActionsSet.ActionType.valueOf(attr.getValue(ScnTags.S_A_TYPE));
		switch (SceneActionsSet.ActionType.valueOf(attr.getValue(ScnTags.S_A_TYPE))){
		case CHANGE_SCENE:
			pAction.NextScene=attr.getValue(ScnTags.S_A_NEXT_SCENE);
			break;
		case STICK:	
			pAction.stick_with=Integer.parseInt(attr.getValue(ScnTags.S_A_STICK_WITH));
			pAction.stickMode=Stick.StickMode.valueOf(attr.getValue(ScnTags.S_A_STICK_MODE));
		default:
			break;
		}
		//the event
		pAction.event = EventHandlerDescriptor.Events.valueOf(attr.getValue(ScnTags.S_A_EVENT));
		return pAction;
	}
	private BasicDescriptor readModifierSetDescription(Attributes attr){
		Log.i(TAG,"\t\t\t readModifierSetDescription");
		
		if(this.pModifierSet != null) //check if a modifer set already exist
			throw new NullPointerException("ParserXMLSceneDescriptor encountered modifier set description within another modifer set");
		if(this.pSpriteDsc == null && this.pCompoundSpriteDsc == null ) //check if a modifier is attached toa sprite
			throw new NullPointerException("ParserXMLSceneDescriptor encountered modifier set dsc. not in a sprite or compound sprite");

		pModifierSet = new ComponentEventHandlerDescriptor();
		pModifierSet.modifierListType = ComponentEventHandlerDescriptor.ModifiersListType.valueOf(attr.getValue(ScnTags.S_A_TYPE));
		pModifierSet.event = EventHandlerDescriptor.Events.valueOf(attr.getValue(ScnTags.S_A_EVENT));
		return pModifierSet;
	}
	private BasicDescriptor readModifierDescription(Attributes attr){
		Log.i(TAG,"\t\t\t\t readModifierDescription");
		if(this.pModifierSet == null ) //check if modifier is declared in a modifier set
			throw new NullPointerException("ParserXMLSceneDescriptor encountered modifier description not in a modiferset");

		//create new action description
		//pModifier = new SceneComponentModifier();

		//****************************************************
		//FT ERRORS
		/*
		pModifier.type=SceneComponentModifier.ModifierType.valueOf(attr.getValue(ScnTags.S_A_TYPE));
		switch (SceneComponentModifier.ModifierType.valueOf(attr.getValue(ScnTags.S_A_TYPE))){
		case MOVE:
			pModifier.fMoveFactor=Float.parseFloat(attr.getValue(ScnTags.S_A_MOVE_FACTOR));
			break;
		case SCALE:
			pModifier.fScaleFactor=Float.parseFloat(attr.getValue(ScnTags.S_A_SCALE_FACTOR));
			break;
		default:
			break;
		}
		pModifier.event = EventDescriptionsManager.Events.valueOf(attr.getValue(ScnTags.S_A_EVENT));
		*/
		return pModifier;
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
	// * Fonction �tant d�clench�e lorsque le parser � pars�
	// * l'int�rieur de la balise XML La m�thode characters
	// * a donc fait son ouvrage et tous les caract�re inclus
	// * dans la balise en cours sont copi�s dans le buffer
	// * On peut donc tranquillement les r�cup�rer pour compl�ter
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
		case STATUS_PARSE_COMPONENT:
			if (localName.equalsIgnoreCase(ScnTags.S_SPRITE)){
				Log.i(TAG,"\t\t end Element SPRITE");
				pSpriteDsc = null;
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
			else
				throw new NullPointerException("ParserXMLSceneDescriptor error invalid End Element STATUS_PARSE_COMPONENT");

			if(nComponents == 0)
				nStatus = STATUS_PARSE_SCENE;
			break;
		case STATUS_PARSE_MODIFIER:
			if (localName.equalsIgnoreCase(ScnTags.S_ACTION)){
				Log.i(TAG,"\t\t\t end Element ACTION");
				pAction = null; 
				nStatus = STATUS_PARSE_COMPONENT;
			}
			else if (localName.equalsIgnoreCase(ScnTags.S_EVENT_HANDLER)){
				Log.i(TAG,"\t\t\t end Event Handler");
				pModifierSet = null;
				nStatus = STATUS_PARSE_COMPONENT;
			}
			else if (localName.equalsIgnoreCase(ScnTags.S_MODIFIER)){
				break; //nStatus = STATUS_PARSE_COMPONENT;
			}
			break;
		}			
	}
	public void removeLastComponentDescriptor(){
		Log.i(TAG,"\t\t\t\t remove last Descriptor from the List");
		
		//Save father descriptor to LIFO
		if(!pDescriptorsInProcessing.isEmpty())
			pCurrentDescriptorInProcessing = pDescriptorsInProcessing.removeLast();
	}

	// * Tout ce qui est dans l'arborescence mais n'est pas partie
	// * int�grante d'un tag, d�clenche la lev�e de cet �v�nement.
	// * En g�n�ral, cet �v�nement est donc lev� tout simplement
	// * par la pr�sence de texte entre la balise d'ouverture et
	// * la balise de fermeture
	public void characters(char[] ch,int start, int length)	throws SAXException{
		String lecture = new String(ch,start,length);
	}
}
