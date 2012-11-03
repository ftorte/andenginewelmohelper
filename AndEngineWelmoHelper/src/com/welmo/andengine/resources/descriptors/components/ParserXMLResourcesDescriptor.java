package com.welmo.andengine.resources.descriptors.components;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.graphics.Typeface;

import com.welmo.andengine.managers.ResourceDescriptorsManager;
import com.welmo.andengine.utility.ScreenDimensionHelper;


public class ParserXMLResourcesDescriptor extends DefaultHandler {

	//--------------------------------------------------------
	// Variables
	//--------------------------------------------------------
	private ResourceDescriptorsManager		pResDescManager;
	private ScreenDimensionHelper			dimHelper=null;

	//Containers for element description
	protected TextureDescriptor				pTextureDsc;
	protected FontDescriptor				pFontDsc;
	protected TextureRegionDescriptor		pTextureRegionDsc;
	protected ColorDescriptor				pColorDsc;
	protected BuildableTextureDescriptor 	pBuildableTextureDsc;
	protected TiledTextureRegionDescriptor	pTiledTextureRegionDsc;
	protected SoundDescriptor				pSoundDsc;
	protected MusicDescriptor				pMusicDsc;
	
	//--------------------------------------------------------
	
	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		super.processingInstruction(target, data);
	}

	public ParserXMLResourcesDescriptor(Context ctx) {
		super();
		dimHelper = ScreenDimensionHelper.getInstance(ctx);
	}


	@Override
	// ===========================================================
	// * Cette méthode est appelée par le parser une et une seule
	// * fois au démarrage de l'analyse de votre flux xml.
	// * Elle est appelée avant toutes les autres méthodes de l'interface,
	// * à l'exception unique, évidemment, de la méthode setDocumentLocator.
	// * Cet événement devrait vous permettre d'initialiser tout ce qui doit
	// * l'être avant ledébut du parcours du document.
	// ===========================================================
	public void startDocument() throws SAXException {
		super.startDocument();
		pResDescManager = ResourceDescriptorsManager.getInstance();
	}

	@Override
	/*
	 * Fonction étant déclenchée lorsque le parser trouve un tag XML
	 * C'est cette méthode que nous allons utiliser pour instancier un nouveau feed
	 */
	public void startElement(String uri, String localName, String name,	Attributes attributes) throws SAXException {

		if (localName.equalsIgnoreCase(ResTags.R_TEXTURE)){
	
			if(this.pTextureDsc != null)
				throw new NullPointerException("ParserXMLSceneDescriptor encountered texture description with another texture description inside");
			pTextureDsc = new TextureDescriptor();

			// Read texture description
			pTextureDsc.ID=0;
			pTextureDsc.Name = new String(attributes.getValue(ResTags.R_A_NAME));
			pTextureDsc.Parameters[ResTags.R_A_HEIGHT_IDX]=Integer.parseInt(attributes.getValue(ResTags.R_A_HEIGHT));
			pTextureDsc.Parameters[ResTags.R_A_WIDTH_IDX]=Integer.parseInt(attributes.getValue(ResTags.R_A_WIDTH));
			
			pResDescManager.addTexture(pTextureDsc.Name, pTextureDsc);
			return;
		
		}
		if (localName.equalsIgnoreCase(ResTags.R_FONT)){
			
			if(this.pFontDsc != null)
				throw new NullPointerException("ParserXMLSceneDescriptor encountered font description with another font description inside");
			pFontDsc = new FontDescriptor();

			// Read font description
			pFontDsc.ID=0;
			pFontDsc.Name = new String(attributes.getValue(ResTags.R_A_NAME));
			
			String strVal1,strVal2;
			pFontDsc.filename="";
			//read Typeface configuration or filename
			if((strVal1 = attributes.getValue(ResTags.R_A_TYPEFACE_FAMILY)) != null && (strVal2=attributes.getValue(ResTags.R_A_TYPEFACE_STYLE)) != null){
					pFontDsc.TypeFace = Typeface.create(strVal1,Integer.parseInt(strVal2));
			}
			else{
				pFontDsc.filename = attributes.getValue(ResTags.R_A_FILE_NAME);
			}
			
			//read anti-aliasing configuration
			if((strVal1 = attributes.getValue(ResTags.R_A_ANTIALIASING)) != null)			
				pFontDsc.AntiAlias = Boolean.parseBoolean(strVal1);
			
			//read FontSize configuration
			if((strVal1 = attributes.getValue(ResTags.R_A_FONT_SIZE)) != null)		
				pFontDsc.Parameters[ResTags.R_A_FONT_SIZE_IDX]=Integer.parseInt(strVal1);
			
			//read FontColor configuration
			if((strVal1 = attributes.getValue(ResTags.R_A_FONT_COLOR)) != null)		
				pFontDsc.color=new String(strVal1);
			
			//read Texture size
			pFontDsc.texture_sizeX = Integer.parseInt(attributes.getValue(ResTags.R_A_TEXTURE_DIMX));
			pFontDsc.texture_sizeY = Integer.parseInt(attributes.getValue(ResTags.R_A_TEXTURE_DIMY));
			
			//load descriptor in descriptor manager
			pResDescManager.addFont(pFontDsc.Name, pFontDsc);
			return;
		}
	
		if (localName.equalsIgnoreCase(ResTags.R_TEXTUREREGION)){
		
			if(this.pTextureRegionDsc != null) //check is new texture region
				throw new NullPointerException("ParserXMLSceneDescriptor encountered texture description with another texture description inside");
			
			if(this.pTextureDsc == null) //check region is part of a texture
				throw new NullPointerException("ParserXMLSceneDescriptor encountered textureregion description withou texture description");
			
			pTextureRegionDsc = new TextureRegionDescriptor();

			pTextureRegionDsc.ID=0;
			pTextureRegionDsc.Name = new String(attributes.getValue(ResTags.R_A_NAME));
			pTextureRegionDsc.filename = new String(attributes.getValue(ResTags.R_A_FILE_NAME));
			pTextureRegionDsc.Parameters[ResTags.R_A_HEIGHT_IDX]=dimHelper.parsLenght(ScreenDimensionHelper.H,attributes.getValue(ResTags.R_A_HEIGHT));
			pTextureRegionDsc.Parameters[ResTags.R_A_WIDTH_IDX]=dimHelper.parsLenght(ScreenDimensionHelper.W,attributes.getValue(ResTags.R_A_WIDTH));
			pTextureRegionDsc.Parameters[ResTags.R_A_POSITION_X_IDX]=dimHelper.parsPosition(ScreenDimensionHelper.X,attributes.getValue(ResTags.R_A_POSITION_X));
			pTextureRegionDsc.Parameters[ResTags.R_A_POSITION_Y_IDX]=dimHelper.parsPosition(ScreenDimensionHelper.Y,attributes.getValue(ResTags.R_A_POSITION_Y));
			pTextureRegionDsc.textureName = new String(pTextureDsc.Name);					//add parent texture name to textureregion descriptor
			
			pResDescManager.addTextureRegion(pTextureRegionDsc.Name, pTextureRegionDsc);	//add textureregion to maps or texture region
			pTextureDsc.Regions.add(pTextureRegionDsc);										//add textureregion to list of region in parent texture
			return;
		}
		
		if (localName.equalsIgnoreCase(ResTags.COLOR)){
			
			if(this.pColorDsc != null)
				throw new NullPointerException("ParserXMLSceneDescriptor encountered color description inside another color description inside");
			
			pColorDsc = new ColorDescriptor();

			// Read texture description
			pColorDsc.Name = new String(attributes.getValue(ResTags.R_A_NAME));
			pColorDsc.Parameters[ResTags.R_A_RED_IDX]=Integer.parseInt(attributes.getValue(ResTags.R_A_RED), 16);
			pColorDsc.Parameters[ResTags.R_A_GREEN_IDX]=Integer.parseInt(attributes.getValue(ResTags.R_A_GREEN), 16);
			pColorDsc.Parameters[ResTags.R_A_BLUE_IDX]=Integer.parseInt(attributes.getValue(ResTags.R_A_BLUE), 16);
			
			pResDescManager.addColor(pColorDsc.Name, pColorDsc);
			return;
		}
		
		if (localName.equalsIgnoreCase(ResTags.R_BUILDABLETEXTURE)){
			
			if(this.pBuildableTextureDsc != null)
				throw new NullPointerException("ParserXMLSceneDescriptor encountered texture description with another texture description inside");
			pBuildableTextureDsc = new BuildableTextureDescriptor();

			// Read texture description
			pBuildableTextureDsc.ID=0;
			pBuildableTextureDsc.Name = new String(attributes.getValue(ResTags.R_A_NAME));
			pBuildableTextureDsc.Parameters[ResTags.R_A_HEIGHT_IDX]=Integer.parseInt(attributes.getValue(ResTags.R_A_HEIGHT));
			pBuildableTextureDsc.Parameters[ResTags.R_A_WIDTH_IDX]=Integer.parseInt(attributes.getValue(ResTags.R_A_WIDTH));
			
			pResDescManager.addBuildableTexture(pBuildableTextureDsc.Name, pBuildableTextureDsc);
			return;
		
		}
		if (localName.equalsIgnoreCase(ResTags.R_TILEDTEXTUREREGION)){
			
			if(this.pTiledTextureRegionDsc != null) //check is new texture region
				throw new NullPointerException("ParserXMLSceneDescriptor encountered texture description with another texture description inside");
			
			if(this.pBuildableTextureDsc == null) //check region is part of a texture
				throw new NullPointerException("ParserXMLSceneDescriptor encountered textureregion description withou texture description");
			
			pTiledTextureRegionDsc = new TiledTextureRegionDescriptor();

			pTiledTextureRegionDsc.ID=0;
			pTiledTextureRegionDsc.Name = new String(attributes.getValue(ResTags.R_A_NAME));
			pTiledTextureRegionDsc.filename = new String(attributes.getValue(ResTags.R_A_FILE_NAME));
			pTiledTextureRegionDsc.textureName = new String(pBuildableTextureDsc.Name);	//add parent texture name to textureregion descriptor
			pTiledTextureRegionDsc.column = Integer.parseInt(attributes.getValue(ResTags.R_A_COL));
			pTiledTextureRegionDsc.row = Integer.parseInt(attributes.getValue(ResTags.R_A_ROW));
			pResDescManager.addTiledTextureRegion(pTiledTextureRegionDsc.Name, pTiledTextureRegionDsc);	//add textureregion to maps or texture region
			pBuildableTextureDsc.Regions.add(pTiledTextureRegionDsc);										//add textureregion to list of region in parent texture
			return;
		}
		if (localName.equalsIgnoreCase(ResTags.R_MUSIC)){
			if(this.pMusicDsc != null) //check is new texture region
				throw new NullPointerException("ParserXMLSceneDescriptor encountered music description with another music descriptor");
			pMusicDsc = new MusicDescriptor();
			pMusicDsc.ID=0;
			pMusicDsc.Name = new String(attributes.getValue(ResTags.R_A_NAME));
			pMusicDsc.filename = new String(attributes.getValue(ResTags.R_A_FILE_NAME));
			pResDescManager.addMusicDescriptor(pMusicDsc.Name, pMusicDsc);
		}
		if (localName.equalsIgnoreCase(ResTags.R_SOUND)){
			if(this.pSoundDsc != null) //check is new texture region
				throw new NullPointerException("ParserXMLSceneDescriptor encountered sound description with another sound descriptor");
			pSoundDsc = new SoundDescriptor();
			pSoundDsc.ID=0;
			pSoundDsc.Name = new String(attributes.getValue(ResTags.R_A_NAME));
			pSoundDsc.filename = new String(attributes.getValue(ResTags.R_A_FILE_NAME));
			pResDescManager.addSoundDescriptor(pSoundDsc.Name, pSoundDsc);
		}
	}

	@Override
	// * Fonction étant déclenchée lorsque le parser à parsé
	// * l'intérieur de la balise XML La méthode characters
	// * a donc fait son ouvrage et tous les caractère inclus
	// * dans la balise en cours sont copiés dans le buffer
	// * On peut donc tranquillement les récupérer pour compléter
	// * notre objet currentFeed
	public void endElement(String uri, String localName, String name) throws SAXException {		

		if (localName.equalsIgnoreCase(ResTags.R_TEXTURE)){
			pTextureDsc  = null;
		}
		
		if (localName.equalsIgnoreCase(ResTags.R_TEXTUREREGION)){
			pTextureRegionDsc = null;
		}
		
		if (localName.equalsIgnoreCase(ResTags.COLOR)){
			pColorDsc = null;
		}
		if (localName.equalsIgnoreCase(ResTags.R_FONT)){
			pFontDsc = null;
		}
		if (localName.equalsIgnoreCase(ResTags.R_TILEDTEXTUREREGION)){
			pTiledTextureRegionDsc  = null;
		}
		if (localName.equalsIgnoreCase(ResTags.R_BUILDABLETEXTURE)){
			pBuildableTextureDsc = null;
		}
		if (localName.equalsIgnoreCase(ResTags.R_MUSIC)){
			this.pMusicDsc = null;
		}
		if (localName.equalsIgnoreCase(ResTags.R_SOUND)){
			this.pSoundDsc = null;
		}
		
	}


	// * Tout ce qui est dans l'arborescence mais n'est pas partie
	// * intégrante d'un tag, déclenche la levée de cet événement.
	// * En général, cet événement est donc levé tout simplement
	// * par la présence de texte entre la balise d'ouverture et
	// * la balise de fermeture
	public void characters(char[] ch,int start, int length)	throws SAXException{
		String lecture = new String(ch,start,length);
		//if(buffer != null) buffer.append(lecture);
	}
}
