package com.welmo.andengine.scenes.descriptors.components;

import java.lang.reflect.Constructor;

import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.xml.sax.Attributes;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.components.ClickableSprite;
import com.welmo.andengine.scenes.components.CompoundSprite;
import com.welmo.andengine.scenes.components.RectangleComponent;
import com.welmo.andengine.scenes.components.StaticSprite;
import com.welmo.andengine.scenes.components.TextComponent;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.components.interfaces.IComponentClickable;
import com.welmo.andengine.scenes.descriptors.ScnTags;
import com.welmo.andengine.scenes.descriptors.components.ButtonSceneLauncherDescriptor.Status;

import android.util.Log;

public class TextObjectDescriptor extends BasicComponentDescriptor{
	// enumerators to manage object types & object events
	// RESOURCE => The message is in a staa=ndard =android resource and the massage value is the string_name. To access if use the R.string.<string_name>
	public enum TextTypes {
		NO_TYPE, SIMPLE,RESOURCE
	}
	public TextTypes type = TextTypes.NO_TYPE;;
	public String message;
	public String FontName;
	public float  scale = 1.0f;
	
	public float getScale() {
		return scale;
	}
	public void setScale(float scale) {
		this.scale = scale;
	}
	public TextTypes getType() {
		return type;
	}
	public void setType(TextTypes type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getFontName() {
		return FontName;
	}
	public void setFontName(String fontName) {
		FontName = fontName;
	}
	public TextObjectDescriptor() {
		super();
		this.message=new String("");
		this.FontName=new String("");
	}
	
	@Override
	public void readXMLDescription(Attributes attributes) {
		Log.i(TAG,"\t\t readXMLDescription");
		
		super.readXMLDescription(attributes);
		
		//set touchable = false
		this.isTouchable = false;
				
		//read Status
		String value = null;

		value = attributes.getValue(ScnTags.S_A_ID );
		if(value!= null)  this.ID=Integer.parseInt(value);
		
		value = attributes.getValue(ScnTags.S_A_RESOURCE_NAME);
		if(value!= null)  this.FontName = new String(value);
		
		value = attributes.getValue(ScnTags.S_A_MESSAGE);
		if(value!= null)  this.message = new String (value);
		
		value = attributes.getValue(ScnTags.S_A_SCALE);
		if(value!= null)  this.scale = Float.parseFloat(value);
		
		value = attributes.getValue(ScnTags.S_A_TYPE);
		if(value!= null)  this.type=TextObjectDescriptor.TextTypes.valueOf(value);
	}
	@Override
	public IComponent CreateComponentInstance(Engine theEng) {
		IComponent newEntity = null;
		newEntity = new TextComponent(this, ResourcesManager.getInstance(), theEng);
		return newEntity;
	}
}
