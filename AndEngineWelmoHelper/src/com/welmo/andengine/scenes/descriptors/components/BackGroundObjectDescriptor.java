package com.welmo.andengine.scenes.descriptors.components;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.IBackground;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.components.interfaces.IComponent;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;

public class BackGroundObjectDescriptor extends BasicComponentDescriptor {
		// enumerators to manage object types & object events
		public enum BackGroundTypes {
		    NO_TYPE, COLOR, SPRITE
		}
		
		public BackGroundTypes 			type;
		public String 					color;
		//public SpriteObjectDescriptor 	sprite;
		
		public BackGroundObjectDescriptor(){
			type 	= BackGroundTypes.NO_TYPE;
			color 	= "";
			//sprite 	= null;
		}
		
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
		
}
