package com.welmo.andengine.utility;

import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.source.BaseTextureAtlasSource;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;

public class TransparentBitmapTextureAtlasSource extends BaseTextureAtlasSource implements IBitmapTextureAtlasSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mWidth;
	private final int mHeight;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TransparentBitmapTextureAtlasSource(final int pWidth, final int pHeight) {
		this(0, 0, pWidth, pHeight);
	}

	public TransparentBitmapTextureAtlasSource(final int pTexturePositionX, 
			final int pTexturePositionY, final int pWidth, final int pHeight) {
		super(pTexturePositionX, pTexturePositionY,pWidth,pHeight);
		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	@Override
	public TransparentBitmapTextureAtlasSource deepCopy() {
		return new TransparentBitmapTextureAtlasSource(this.mTextureX, 
				this.mTextureY, this.mWidth, this.mHeight);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public int getTextureWidth() {
		return this.mWidth;
	}

	@Override
	public int getTextureHeight() {
		return this.mHeight;
	}

	@Override
	public Bitmap onLoadBitmap(final Config pBitmapConfig) {
		int[] colors = new int[this.mWidth * this.mHeight];
		for (int y = 0; y < this.mHeight; y++) {
			for (int x = 0; x < this.mWidth; x++) {
				colors[y * this.mWidth + x] = Color.TRANSPARENT;
			}
		}
		return Bitmap.createBitmap(colors,this.mWidth, this.mHeight, pBitmapConfig);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.mWidth + " x " + this.mHeight + ")";
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}