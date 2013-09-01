package com.welmo.andengine.scenes.components;

import java.io.IOException;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.EmptyBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.decorator.BaseBitmapTextureAtlasSourceDecorator;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.modifier.IModifier.DeepCopyNotSupportedException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class CororingSprite{

	//ITextureRegion 					mTextureRegions;
	//ITextureRegion 					mBitmmapTextureRegion;
	Bitmap 							balloon 				= null;
	Bitmap 							baloonworking 			= null;
	private int[] 					pixelsCopy				= new int[1024*750];
	BitmapTextureAtlas 				mBitmapTexturesAtlas 	= null;
	final String 					TEXTUREBASEPATH 		= "gfx/";
	
	/*
	public void loadImage(String ImageFile) {	
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(TEXTUREBASEPATH);
		//Create the texture
		mBitmapTexturesAtlas = new BitmapTextureAtlas(mEngine.getTextureManager(), 
				2048, 2048, TextureOptions.BILINEAR_PREMULTIPLYALPHA);			
		
		try {
			balloon = BitmapFactory.decodeStream(this.getAssets().open("gfx/monster05BW.png"));
			baloonworking = balloon.copy(Bitmap.Config.ARGB_8888, true);
			baloonworking.getPixels(pixelsCopy, 0, 1024, 0, 0, 1024, 750);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.mEngine.getTextureManager().loadTexture(mBitmapTexturesAtlas);
		
		//test creation of texture from Bitmap
		//int[] colors ={10,10,10,10};
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 1024, 750, TextureOptions.BILINEAR);
		
		baseTextureSource = new EmptyBitmapTextureAtlasSource(1024, 750);
		decoratedTextureAtlasSource = new BaseBitmapTextureAtlasSourceDecorator(baseTextureSource) {
			@Override
			protected void onDecorateBitmap(Canvas pCanvas) throws Exception {
				pCanvas.drawBitmap(baloonworking, 0, 0, this.mPaint);
			}
			
			@Override
			public BaseBitmapTextureAtlasSourceDecorator deepCopy() {
				throw new DeepCopyNotSupportedException();
			}
		};

		this.mDecoratedBalloonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(this.mBitmapTextureAtlas, decoratedTextureAtlasSource, 0, 0);
		this.mBitmapTextureAtlas.load();
		
	
	}
	
	final Sprite balloonSprite = new Sprite(50, 25, this.mDecoratedBalloonTextureRegion, this.getVertexBufferObjectManager()){
		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
			if(pSceneTouchEvent.isActionDown()){
				
				int OldColor = pixelsCopy[convertXYtoID((int)pTouchAreaLocalX,(int)pTouchAreaLocalY)];
				
				floodFillScanlineStack(pixelsCopy,(int)pTouchAreaLocalX, (int)pTouchAreaLocalY, 0x00FF00FF, OldColor);
				
				baloonworking.setPixels(pixelsCopy, 0, 1024, 0, 0, 1024, 750);
				mBitmapTextureAtlas.clearTextureAtlasSources();
				mDecoratedBalloonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(mBitmapTextureAtlas, decoratedTextureAtlasSource, 0, 0);
				return true;
			}
			return true;
		}
	};
	*/
	
	
}
