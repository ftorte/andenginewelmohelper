package com.welmo.andengine.scenes.descriptors.components;

import java.lang.reflect.Constructor;
import java.util.Set;
import java.util.Map.Entry;

import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.xml.sax.Attributes;

import android.util.Log;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.components.ClickableSprite;
import com.welmo.andengine.scenes.components.ComponentDefaultEventHandler;
import com.welmo.andengine.scenes.components.CompoundSprite;
import com.welmo.andengine.scenes.components.interfaces.IActionOnSceneListener;
import com.welmo.andengine.scenes.components.interfaces.IActivitySceneListener;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.components.interfaces.IComponentClickable;
import com.welmo.andengine.scenes.components.interfaces.IComponentEventHandler;
import com.welmo.andengine.scenes.descriptors.ScnTags;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor;
import com.welmo.andengine.scenes.descriptors.events.ComponentEventHandlerDescriptor.Events;


public class SpriteObjectDescriptor extends BasicComponentDescriptor{
	// enumerators to manage object types & object events
	public enum SpritesTypes {
	    NO_TYPE, STATIC, CLICKABLE, COMPOUND_SPRITE, ANIMATED, COLORING_SPRITE
	}
	
	public SpritesTypes 	type;
	public String 			textureName;
	public int 				nSideATile =0;
	public int 				nSideBTile =0;
	protected String 		soundName = "";
	
	public String getSoundName() {
		return soundName;
	}
	public void setSoundName(String soundName) {
		this.soundName = soundName;
	}
	public SpritesTypes getType() {
		return type;
	}
	public void setType(SpritesTypes type) {
		this.type = type;
	}
	public String getTextureName() {
		return textureName;
	}
	public void setTextureName(String textureName) {
		this.textureName = textureName;
	}
	public SpriteObjectDescriptor() {
		super();
		this.type=SpritesTypes.NO_TYPE;
		this.textureName=new String("");
	}
	public void setSidesTiles(int sideA, int sideB){
		nSideATile = sideA;
		nSideBTile = sideB;
	}
	public int getSidesA(){
		return nSideATile;
	}
	public int getSidesB(){
		return nSideBTile;
	}
	
	//@Override
	public void copyFrom(SpriteObjectDescriptor copyfrom){
		
		super.copyFrom(copyfrom);
		
		type = copyfrom.type;
		textureName = copyfrom.textureName;
		nSideATile = copyfrom.nSideATile;
		nSideBTile = copyfrom.nSideBTile;
	}
	@Override
	public IComponent CreateComponentInstance(Engine theEng) {
		// TODO Auto-generated method stub
		
		switch(type){	
			case  STATIC:
				//FT newEntity = createSprite(pSprtDsc);
				break;
			case CLICKABLE: // Create the clickable sprite elements
				
				IComponentClickable newClickableSprite = null;
				ResourcesManager pRM = ResourcesManager.getInstance();
				String className = this.getClassName();
				try {
					if(!className.equals("")){
						// Get the class of className
						Class<?> classe = Class.forName (className);
						
						// Get the constructor
						Constructor<?> constructor = 
								classe.getConstructor (new Class [] {Class.forName ("com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor"),
										Class.forName ("com.welmo.andengine.managers.ResourcesManager"),
										Class.forName ("org.andengine.engine.Engine")});
						
			
						newClickableSprite = (IComponentClickable) constructor.newInstance (new Object [] {this,pRM,theEng});
				}
					else newClickableSprite = (IComponentClickable) new ClickableSprite (this,pRM,theEng);
						
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
				
				return newClickableSprite;
			case COMPOUND_SPRITE:
				//FT replace code newEntity = createCompoundSprite(pSprtDsc);
				CompoundSprite newCompound = new CompoundSprite(0, 0, 0,0, theEng.getVertexBufferObjectManager());
				newCompound.setID(this.getID());
				newCompound.setPDescriptor(this);
				return newCompound;
			case ANIMATED: // Create the animated sprite elements
				//IComponent newSceneComponent = createAnimatedSprite(this);
				return null;
				
			default:
				break;
			}		
		return null;
	}
	@Override
	public void readXMLDescription(Attributes attributes) {
		Log.i(TAG,"\t\t readXMLDescription");
		super.readXMLDescription(attributes);
		
		// Read the sprite specific attributes
		
		this.textureName = new String(attributes.getValue(ScnTags.S_A_RESOURCE_NAME));
		if(attributes.getValue(ScnTags.S_A_TYPE)!= null)
			this.type = SpriteObjectDescriptor.SpritesTypes.valueOf(attributes.getValue(ScnTags.S_A_TYPE));
		else
			this.type = SpriteObjectDescriptor.SpritesTypes.valueOf("STATIC");

		if((attributes.getValue(ScnTags.S_A_SIDEA)!= null) && (attributes.getValue(ScnTags.S_A_SIDEB) != null)){
			this.nSideATile = Integer.parseInt(attributes.getValue(ScnTags.S_A_SIDEA));
			this.nSideBTile = Integer.parseInt(attributes.getValue(ScnTags.S_A_SIDEB));
		}
	}
	
}
