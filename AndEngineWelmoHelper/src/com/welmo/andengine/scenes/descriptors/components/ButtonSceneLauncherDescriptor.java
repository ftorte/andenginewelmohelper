package com.welmo.andengine.scenes.descriptors.components;

import java.util.EnumMap;
import java.util.StringTokenizer;

import org.andengine.engine.Engine;
import org.xml.sax.Attributes;

import com.welmo.andengine.scenes.components.buttons.ButtonSceneLauncher;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.ScnTags;

public class ButtonSceneLauncherDescriptor extends BasicComponentDescriptor{
	public enum Status {NotActive, Locked, Unlocked, level0, level1, level2, level3}
	public enum ImgType{bg_inactive, ico_inactive, bg_final, ico_locked, ico_unlocked, ico_free, ico_star_1, ico_star_2, ico_star_3, 
		ico_star_inactive_1, ico_star_inactive_2, ico_star_inactive_3}
	
	public class ImgData{
		public String strResourceName = "";
		public int 	px		= 0;
		public int 	py	 	= 0;
		public int 	width	= 0;
		public int 	height	= 0;
		public final static int  NB_OF_PARAMETERS = 5;
	}
	public void copyFrom(ButtonSceneLauncherDescriptor objectCopy){
		super.copyFrom((BasicComponentDescriptor)objectCopy);
		defaultstatus = objectCopy.defaultstatus;
		imagesList.clear();
		imagesList.putAll(objectCopy.imagesList);
		strNextScene = objectCopy.strNextScene;
		strUnlockCondition = objectCopy.strUnlockCondition;
		strLicence = objectCopy.strLicence;
		
	}
	protected Status 				defaultstatus 			= Status.NotActive;
	protected String				strNextScene			= "";
	protected String				strUnlockCondition		= "";
	protected String				strLicence				= null;
	
	public String getLicence() {
		return strLicence;
	}
	public void setLicence(String strLicence) {
		this.strLicence = strLicence;
	}
	
	EnumMap<ImgType, ImgData> 		imagesList 				= new EnumMap<ImgType, ImgData>(ImgType.class);
	
	
	/* maps active image in function of the buttons status
	/NotActive, Locked, Unlocked, level0, level1, level2, level3
	//					BG_INACTIVE		ICO_INACTIVE	BG_FINAL	ICO_LOCKED 	ICO_UNLOCKED	IMG_ICON_FREE	strStarInacive	strStar		
	// 	NotActive			yes				yes				no			no			no				no				no				no
	//	Locked				no				no				yes			yes			no				no				no				no
	//	Unlocked			no				no				yes			no			yes				no				no 				no
	//	level0				no				no				yes			no			no				yes				yes (1,2,3)		no			
	//	level1				no				no				yes			no			no				yes				yes (2,3)		yes (1)
	//	level2				no				no				yes			no			no				yes				yes	(3)			yes (1,2)
	//	level3				no				no				yes			no			no				yes				no				yes (1,2,3)
	*/
	public ButtonSceneLauncherDescriptor(){
		super();
	}
	public void instantiateXMLDescription(ButtonSceneLauncherDescriptor instantiateFromObject, Attributes attributes) {
		this.instantiateFrom(instantiateFromObject);
		//read parameter to change all customized values
		this.readXMLDescription(attributes);
		
	}
	private void instantiateFrom(ButtonSceneLauncherDescriptor instantiateFromObject){
		copyFrom(instantiateFromObject);
		this.isTemplate = false;
	}
	public void readXMLDescription(Attributes attributes) {
		
		//check if is an instantiaion of a previous descriptor
		String value = attributes.getValue(ScnTags.S_IS_ISTANCE_OF_ID);
		if(value!= null) this.isIstanceOfID(Integer.parseInt(value));
		
		//call XML parser for class parent parameters
		super.readXMLDescription(attributes);
				
		//read Status
		value = attributes.getValue(ScnTags.S_A_STATUS_DEFAUTL);
		if(value!= null) defaultstatus = Status.valueOf(value);
				
		//read Background
		value = attributes.getValue(ScnTags.S_A_BG_INACTIVE);
		if(value!= null) imagesList.put(ImgType.bg_inactive,readImageDataFromString(value));
		
		//read Background
		value = attributes.getValue(ScnTags.S_A_ICO_INACTIVE);
		if(value!= null) imagesList.put(ImgType.ico_inactive,readImageDataFromString(value));

		//read bg_fina
		value = attributes.getValue(ScnTags.S_A_BG_FINAL);
		if(value!= null) imagesList.put(ImgType.bg_final,readImageDataFromString(value));
		
		//read ico_locked
		value = attributes.getValue(ScnTags.S_A_ICO_LOCKED);
		if(value!= null) imagesList.put(ImgType.ico_locked,readImageDataFromString(value));
		
		//read ico_locked
		value = attributes.getValue(ScnTags.S_A_ICO_UNLOCKED);
		if(value!= null) imagesList.put(ImgType.ico_unlocked,readImageDataFromString(value));
		
		//read ico_free
		value = attributes.getValue(ScnTags.S_A_ICO_FREE);
		if(value!= null) imagesList.put(ImgType.ico_free,readImageDataFromString(value));

		//read ico_stars & ico_star_inactive
		value = attributes.getValue(ScnTags.S_A_ICO_STAR_1);
		if(value!= null) imagesList.put(ImgType.ico_star_1,readImageDataFromString(value));

		//read ico_stars & ico_star_inactive
		value = attributes.getValue(ScnTags.S_A_ICO_STAR_2);
		if(value!= null) imagesList.put(ImgType.ico_star_2,readImageDataFromString(value));
		//read ico_stars & ico_star_inactive
		value = attributes.getValue(ScnTags.S_A_ICO_STAR_3);
		if(value!= null) imagesList.put(ImgType.ico_star_3,readImageDataFromString(value));
		
		//read ico_star, ico_star_inactive
		value = attributes.getValue(ScnTags.S_A_ICO_STAR_INACTIVE_1);
		if(value!= null) imagesList.put(ImgType.ico_star_inactive_1,readImageDataFromString(value));
		
		//read ico_star, ico_star_inactive
		value = attributes.getValue(ScnTags.S_A_ICO_STAR_INACTIVE_2);
		if(value!= null) imagesList.put(ImgType.ico_star_inactive_2,readImageDataFromString(value));
		
		//read ico_star, ico_star_inactive
		value = attributes.getValue(ScnTags.S_A_ICO_STAR_INACTIVE_3);
		if(value!= null) imagesList.put(ImgType.ico_star_inactive_3,readImageDataFromString(value));
		
		//read the next scene is launched while button is pressed
		value = attributes.getValue(ScnTags.S_A_NEXT_SCENE);
		if(value!= null) strNextScene = new String(value);
		
		value = attributes.getValue(ScnTags.S_A_UNLOCK_CONDITION);
		if(value!= null) strUnlockCondition = new String(value);
		
		value = attributes.getValue(ScnTags.S_A_LICENCE);
		if(value!= null) 
			strLicence =  new String(value);
	}

	private ImgData readImageDataFromString(String imgData){
		String[] tokens = imgData.split(";");
		
		//if the string doesen't contains 4 tokens return null;
		ImgData theData = new ImgData();
		if(tokens.length != ImgData.NB_OF_PARAMETERS)
			return null;
		
		theData.strResourceName = new String(tokens[0]);
		theData.px				= Integer.parseInt(tokens[1]);
		theData.py				= Integer.parseInt(tokens[2]);
		theData.width			= Integer.parseInt(tokens[3]);
		theData.height			= Integer.parseInt(tokens[4]);
		return theData;
	}
	
	//-------------------------------------------------------------
	// Getters and setters
	//-------------------------------------------------------------
	// Status
	public Status 	getDefaultStatus() {return defaultstatus;}
	public void 	setDefaultStatus(Status thestatus) {this.defaultstatus = thestatus;}
	//resources
	public EnumMap<ImgType, ImgData> getImagesList() {return imagesList;}
	public void 	setImagesInList(ImgType theType, ImgData theData){imagesList.put(theType, theData);}
	
	@Override
	public IComponent CreateComponentInstance(Engine theEng) {
		return 	new ButtonSceneLauncher(this, theEng.getVertexBufferObjectManager());
	}
	public String getNextScene() {
		return strNextScene;
	}
}
