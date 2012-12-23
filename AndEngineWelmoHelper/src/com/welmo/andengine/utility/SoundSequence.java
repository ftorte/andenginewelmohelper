package com.welmo.andengine.utility;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import com.welmo.andengine.managers.ResourcesManager.SoundContainer;

public class SoundSequence {
	private SoundContainer[] 	sequece;
	private ArrayList<Integer> 	parameterIndex;
	
	public SoundSequence(int sequencelenght){
		parameterIndex = new ArrayList<Integer>();
		sequece = new SoundContainer[sequencelenght];
	}
	public SoundContainer[] getSequence(){
		return sequece;
	}
	public void addParameter(int parameter){
		parameterIndex.add(parameter);
	}
	public Integer getParameter(int index){
		return parameterIndex.get(index);
	}
	public void setUpParametreableSound(int parametrerNumber, SoundContainer sound){
		if(parameterIndex.size()>= parametrerNumber)
			throw new InvalidParameterException("Declared Parameter Nb out of array limit");
		sequece[parameterIndex.get(parametrerNumber)]=sound;
	}
	public void setParameterIndex(int parNb, int id){
		
		if(id >= sequece.length)
			throw new InvalidParameterException("Declared Parameter Nb out of array limit");
		parameterIndex.set(parNb,id);
	}
}
