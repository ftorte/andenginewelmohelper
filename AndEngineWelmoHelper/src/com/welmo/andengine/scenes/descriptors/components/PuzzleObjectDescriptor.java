package com.welmo.andengine.scenes.descriptors.components;

public class PuzzleObjectDescriptor extends BasicObjectDescriptor {
	protected int 		nbColumns=0;
	protected int		nbRows=0;
	protected String 	textureName="";
	
	public String getTextureName() {
		return textureName;
	}
	public void setTextureName(String textureName) {
		this.textureName = textureName;
	}
	public int getNbColumns() {
		return nbColumns;
	}
	public void setNbColumns(int nbColumns) {
		this.nbColumns = nbColumns;
	}
	public int getNbRows() {
		return nbRows;
	}
	public void setNbRows(int nbRows) {
		this.nbRows = nbRows;
	}
	
}
