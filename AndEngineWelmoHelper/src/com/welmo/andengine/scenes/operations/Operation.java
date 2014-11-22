package com.welmo.andengine.scenes.operations;

import java.util.ArrayList;
import java.util.List;

import com.welmo.andengine.scenes.operations.IOperationHandler.OperationTypes;

public class Operation {

	public 		OperationTypes 				type;					//	type of message
	public 		List<Float> 				parametersNumbers;		//  Parameter of the message
	public 		List<String> 				parametersStrings;		//  Parameter of the message
	
	protected 	IOperationHandler 			hdHandler = null; 		//  Contain the Object path the massage has made 
																	//  from the source to the final object
	
	//--------------------------------------------------------------------------------------
	//Constructors
	//--------------------------------------------------------------------------------------
	/*public Operation(OperationTypes type, List<Integer> parameter){
		this.type=type;
		parametersNumbers = new ArrayList<Float>();
		parametersNumbers.clear();
		if(parameter != null){
			for(int i=0; i < parameter.size();i++)
				parametersNumbers.add(new Float(parameter.get(i)));
		}
		parametersStrings = new ArrayList<String>();
		parametersStrings.clear();
	}*/
	public Operation(Operation copy){
		this.type=copy.type;
		this.parametersNumbers = new ArrayList<Float>();
		this.parametersNumbers.clear();
		if(copy.parametersNumbers != null){
			for(int i=0; i < copy.parametersNumbers.size();i++)
				this.parametersNumbers.add(new Float(copy.parametersNumbers.get(i)));
		}
		if(copy.parametersStrings != null){
			for(int i=0; i < copy.parametersStrings.size();i++)
				this.parametersStrings.add(new String(copy.parametersStrings.get(i)));
		}
		this.hdHandler = copy.hdHandler;
	}
	public Operation(){
		this.type=OperationTypes.NULL;
		parametersNumbers = new ArrayList<Float>();
		parametersNumbers.clear();
		parametersStrings = new ArrayList<String>();
		parametersStrings.clear();

	}
	public Operation(OperationTypes type, Float parameter){
		this.type=type;
		parametersNumbers = new ArrayList<Float>();
		parametersNumbers.clear();
		parametersNumbers.add(parameter);
	}
	public Operation(OperationTypes type) {
		this.type=type;
		parametersNumbers = new ArrayList<Float>();
		parametersNumbers.clear();
	}
	//--------------------------------------------------------------------------------------
	public OperationTypes getType(){
		return type;
	}
	public void setType(OperationTypes theType){
		type=theType;
	}
	// *************************************************************************************
	// get/set parameter as numbers
	// *************************************************************************************
	public Float getParameterNumber(int id){
		if(id >= parametersNumbers.size() || id < 0)
			throw new NullPointerException("The message don't has the requested parameter");
		return parametersNumbers.get(id);
	}
	public void setParameterNumbers(Float... params) {
		parametersNumbers.clear();
	    for (int i = 0; i < params.length; ++i) {
	    	parametersNumbers.add(params[i]);
	    }
	}
	public void setParameterNumbers(List<Float> arParameters) {
		parametersNumbers.clear();
	    for (int i = 0; i < arParameters.size(); ++i){
	    	parametersNumbers.add(arParameters.get(i));
	    }
	}
	// *************************************************************************************
	// get/set parameter as Strings
	// *************************************************************************************
	public String getParameterString(int id){
		if(id >= parametersStrings.size() || id < 0)
			throw new NullPointerException("The message don't has the requested parameter");
		return parametersStrings.get(id);
	}
	public void setParameterString(String... params) {
		parametersStrings.clear();
	    for (int i = 0; i < params.length; ++i) {
	    	parametersStrings.add(params[i]);
	    }
	}
	public void setParameterString(List<String> arParameters) {
		parametersStrings.clear();
	    for (int i = 0; i < arParameters.size(); ++i){
	    	parametersStrings.add(arParameters.get(i));
	    }
	}
	public void pushHander(IOperationHandler hdOper){
		hdHandler = hdOper;
	}
	public IOperationHandler getHander(){
		return hdHandler;
	}
}
