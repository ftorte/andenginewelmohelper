package com.welmo.andengine.resources.descriptors;

import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.TextureRegion;

public class DecoratedTextureRegion extends TextureRegion{

	public DecoratedTextureRegion(ITexture pTexture, float pTextureX,
			float pTextureY, float pTextureWidth, float pTextureHeight,
			boolean pRotated) {
		super(pTexture, pTextureX, pTextureY, pTextureWidth, pTextureHeight, pRotated);
		// TODO Auto-generated constructor stub
	}

	public DecoratedTextureRegion(ITexture pTexture, float pTextureX,
			float pTextureY, float pTextureWidth, float pTextureHeight,
			float pScale, boolean pRotated) {
		super(pTexture, pTextureX, pTextureY, pTextureWidth, pTextureHeight, pScale,
				pRotated);
		// TODO Auto-generated constructor stub
	}

	public DecoratedTextureRegion(ITexture pTexture, float pTextureX,
			float pTextureY, float pTextureWidth, float pTextureHeight,
			float pScale) {
		super(pTexture, pTextureX, pTextureY, pTextureWidth, pTextureHeight, pScale);
		// TODO Auto-generated constructor stub
	}

	public DecoratedTextureRegion(ITexture pTexture, float pTextureX,
			float pTextureY, float pTextureWidth, float pTextureHeight) {
		super(pTexture, pTextureX, pTextureY, pTextureWidth, pTextureHeight);
		// TODO Auto-generated constructor stub
	}

}
