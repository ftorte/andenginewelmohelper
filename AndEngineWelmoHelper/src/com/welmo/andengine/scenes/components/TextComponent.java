package com.welmo.andengine.scenes.components;

import org.andengine.engine.Engine;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

import android.util.Log;

import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.descriptors.components.SpriteObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.TextObjectDescriptor;
import com.welmo.andengine.scenes.descriptors.components.BasicObjectDescriptor.Alignment;

public class TextComponent extends Text{
	// ===========================================================
	// Constants
	// ===========================================================
	//Log & Debug
	private static final String TAG = "TextComponent";

	// ===========================================================
	// Fields
	// ===========================================================
	private int nID											=-1;

	// ===========================================================
	// Constructors
	// ===========================================================
	public TextComponent(float pX, float pY, IFont pFont, CharSequence pText,
			TextOptions pTextOptions,
			VertexBufferObjectManager pTextVertexBufferObject) {
		super(pX, pY, pFont, pText, pTextOptions,
				pTextVertexBufferObject);
		// TODO Auto-generated constructor stub
	}
	public TextComponent(TextObjectDescriptor pTXTDscf, ResourcesManager pRM, Engine theEngine,IAreaShape theFather){
		super(0, 0, pRM.getFont(pTXTDscf.getFontName()), 
				pTXTDscf.getMessage(), new TextOptions(HorizontalAlign.CENTER), 
				theEngine.getVertexBufferObjectManager());
		configure(pTXTDscf);
		if(theFather != null){
			PositionHelper.align(pTXTDscf.getIPosition(), this, theFather);
		}
		//set color	
		String colorName = pTXTDscf.getICharacteristis().getColor();
		Log.i(TAG,"Get Color: " + colorName);
		if(!colorName.equals(""))
				this.setColor(pRM.getColor(colorName));
	}
	
	public void configure(TextObjectDescriptor spDsc){
		nID = spDsc.getID();
		/* Setup Rotation*/
		setRotationCenter(getWidth()/2, getHeight()/2);
		setRotation(spDsc.getIOriantation().getOrientation());
		//set position			
		setX(spDsc.getIPosition().getX());
		setY(spDsc.getIPosition().getY());
	}
}
