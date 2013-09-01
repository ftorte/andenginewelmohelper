package com.welmo.andengine.scenes.descriptors.components;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.primitive.Rectangle;
import org.xml.sax.Attributes;

import android.util.Log;

public class HUDDescriptor extends BasicObjectDescriptor {
	// -----------------------------------------------------------------------------------------------------------------------------
	// Constants
	private final static String 						TAG = "HUDisplay";
	
	
	public HUDDescriptor readHUDXMLDescription(Attributes attr){
		Log.i(TAG,"\t\t readSpriteDescription");

		HUDDescriptor pHUDDsc = new HUDDescriptor();

		/*
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
		 */

		return pHUDDsc;
	}

}
