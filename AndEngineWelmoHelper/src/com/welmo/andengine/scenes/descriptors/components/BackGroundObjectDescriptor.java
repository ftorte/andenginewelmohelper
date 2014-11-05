package com.welmo.andengine.scenes.descriptors.components;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.IBackground;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.xml.sax.Attributes;

import android.util.Log;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.descriptors.ScnTags;

public class BackGroundObjectDescriptor extends BasicComponentDescriptor {
		// enumerators to manage object types & object events
		public enum BackGroundTypes {
		    NO_TYPE, COLOR, SPRITE
		}
		//--------------------------------------------------------
		// Variables
		//--------------------------------------------------------
		private static final String 	TAG = "ParserXMLSceneDescriptor";
		public BackGroundTypes 			type;
		public String 					color;
		//--------------------------------------------------------
		// Constructor(s)
		public BackGroundObjectDescriptor(){
			type 	= BackGroundTypes.NO_TYPE;
			color 	= "";
		}
		//--------------------------------------------------------
		// function
		//--------------------------------------------------------
		// Instantiate a background object
		//--------------------------------------------------------
		public IBackground createBackground(Engine mEngine){
			
			ResourcesManager pRM = ResourcesManager.getInstance();
			
			switch(type){

				case COLOR:
					return new Background(pRM.getColor(color));

				case SPRITE:
					SpriteObjectDescriptor pSDsc = null;
					for (BasicDescriptor pObjDsc : this.pChild.values()){
						if(pObjDsc instanceof SpriteObjectDescriptor){
							pSDsc = (SpriteObjectDescriptor)pObjDsc;
							Sprite spriteBKG = new Sprite(0, 0, pSDsc.getIDimension().getWidth(), pSDsc.getIDimension().getHeight(), 
									pRM.getTextureRegion(pSDsc.getTextureName()), 
									mEngine.getVertexBufferObjectManager());

							return new SpriteBackground(spriteBKG);
						}
					}
				throw new NullPointerException("Invalid Sprite Backgound. Not found sprite descriptor for background");
			default:
				return null;
			}
		}
		//-------------------------------------------------------------------------------------
		// Read the descriptor from the XML flow 
		//-------------------------------------------------------------------------------------
		@Override
		public void readXMLDescription(Attributes attributes) {
	
			Log.i(TAG,"\t\t readBackGroudDescription");
			//read value from superclass
			super.readXMLDescription(attributes);
			//read type & color
			this.type=BackGroundObjectDescriptor.BackGroundTypes.valueOf(attributes.getValue(ScnTags.S_A_TYPE));
			switch (type){
			case COLOR:
				this.color=attributes.getValue(ScnTags.S_A_COLOR);
				break;
			default:
				break;
			}	
		}
		
		//TODO Instantiate from
		
}
