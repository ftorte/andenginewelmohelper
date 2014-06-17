package com.welmo.andengine.scenes;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.andengine.entity.shape.IAreaShape;

import com.welmo.andengine.scenes.components.puzzle.PuzzleSprites;
import com.welmo.andengine.scenes.descriptors.SceneDescriptor;
import com.welmo.andengine.scenes.descriptors.components.PuzzleObjectDescriptor;
import com.welmo.andengine.scenes.operations.Operation;

public class PuzzleScene extends ManageableScene implements IConfigurableScene {
	
	// ===========================================================
	// Constants
	// ===========================================================
	private static final String 	TAG 				= "ColoringScene";
	PuzzleSprites 					thePuzzle	=null;

	// ===========================================================
	// Fields
	// ===========================================================	
	Deque<Operation> 			qMessageStack = new LinkedList<Operation>();

	@Override
	public void loadScene(SceneDescriptor sceneDescriptor) {
		super.loadScene(sceneDescriptor);
	
		Iterator<Entry<Integer, IAreaShape>> it = mapOfObjects.entrySet().iterator();
		
	    while (it.hasNext()) {
	    	IAreaShape value = it.next().getValue();
	    	if(value  instanceof PuzzleSprites)
	    		thePuzzle=(PuzzleSprites)value;
	    }
	    
	}
		
	// ===========================================================
	// IConfigurableScene Methods
	// ===========================================================	
	@Override
	public void configure(ArrayList<String> parameterList) {
		String[] 	tokens = parameterList.get(0).split(";");
		
		//first clear the puzzle
		thePuzzle.clearPuzzle();
		
		//setup new paramteres
		thePuzzle.setmTiledTextureResource(tokens[0]);
		thePuzzle.setmHelperImage(tokens[1]);
		
		if(tokens.length > 2){
			thePuzzle.setNbCols(Integer.parseInt(tokens[2]));
			thePuzzle.setNbRows(Integer.parseInt(tokens[3]));
		}
		
		if(tokens.length > 4){
			this.setFatherScene(tokens[4]);
		}
		
		thePuzzle.createPuzzle();
	}
}
