package com.welmo.andengine.scenes.components;

import org.andengine.engine.Engine;
import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
//import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.entity.sprite.vbo.HighPerformanceTiledSpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.DrawType;
import com.welmo.andengine.managers.ResourcesManager;
import com.welmo.andengine.scenes.descriptors.components.PuzzleObjectDescriptor;

public class PuzzleSprites extends Entity{
	int 			nbRows				= 0;
	int 			nbCols				= 0;
	int 			nbPieces			= 0;
	float 			pieceWidth			= 0;
	float 			pieceHeight 		= 0;
	String			textureName			= null;
	
	TiledSprite[] 	pieces				= null;
	Sprite			theSprite			= null;
	
	/*public void init(Engine theEngine){
		ResourcesManager pRM = ResourcesManager.getInstance();
		theSprite = new Sprite(0, 0, 
			1000, 1200, 
			pRM.getTextureRegion(resourceName), 
			theEngine.getVertexBufferObjectManager());
	
		theSprite.setZIndex(0);
	}*/
	
	public int getNbRows() {
		return nbRows;
	}

	public void setNbRows(int nbRows) {
		this.nbRows = nbRows;
	}

	public int getNbCols() {
		return nbCols;
	}

	public void setNbCols(int nbCols) {
		this.nbCols = nbCols;
	}

	public int getNbPieces() {
		return nbPieces;
	}

	public void setNbPieces(int nbPieces) {
		this.nbPieces = nbPieces;
	}

	public float getPieceWidth() {
		return pieceWidth;
	}

	public void setPieceWidth(float pieceWidth) {
		this.pieceWidth = pieceWidth;
	}

	public float getPieceHeight() {
		return pieceHeight;
	}

	public void setPieceHeight(float pieceHeight) {
		this.pieceHeight = pieceHeight;
	}

	public String getResourceName() {
		return textureName;
	}

	public void setResourceName(String resourceName) {
		this.textureName = resourceName;
	}

	public TiledSprite[] getPieces() {
		return pieces;
	}

	public void setPieces(TiledSprite[] pieces) {
		this.pieces = pieces;
	}

	public Sprite getTheSprite() {
		return theSprite;
	}

	public void setTheSprite(Sprite theSprite) {
		this.theSprite = theSprite;
	}

	public void createPuzzle(Engine theEngine, PuzzleObjectDescriptor pzDsc){
		ResourcesManager pRM = ResourcesManager.getInstance();
		//set position			
		setX(pzDsc.getIPosition().getX());
		setY(pzDsc.getIPosition().getY());
		
		//get geometry 
		nbRows = pzDsc.getNbRows();
		nbCols = pzDsc.getNbRows();
		nbPieces = nbRows*nbCols;
		
		//get the texture
		textureName = pzDsc.getTextureName();
		ITiledTextureRegion theTiledTexture = pRM.getTiledTextureRegion(textureName);
		
		//Create vectors of tiled sprite pointers
		pieces = new TiledSprite[nbPieces];
		
		pieceWidth  = theTiledTexture.getWidth();
		pieceHeight = theTiledTexture.getHeight();
		float pX=0;
		float pY =0;
		
		int pieceNb=0;
		
		for (int i=0; i < nbCols; i++){
			for (int j=0; j < nbRows; j++){

				pieces[pieceNb] = new TiledSprite(pX, pY, pieceWidth, pieceHeight, theTiledTexture,
						new HighPerformanceTiledSpriteVertexBufferObject(theEngine.getVertexBufferObjectManager(), 
								TiledSprite.TILEDSPRITE_SIZE * (theTiledTexture).getTileCount(), 
								DrawType.STATIC,
								true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT)); 
				//Attach the puzzle element to the entity
				pieces[pieceNb].setCurrentTileIndex(pieceNb);
				
				this.attachChild(pieces[i*nbCols+j]);
				pX = pX+ pieceWidth;
				pieceNb +=1;
			}
			pX=0;
			pY = pY+ pieceHeight;
		}
	}
}
