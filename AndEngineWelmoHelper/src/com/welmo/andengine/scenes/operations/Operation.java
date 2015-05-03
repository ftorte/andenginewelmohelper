package com.welmo.andengine.scenes.operations;

import java.util.ArrayList;
import java.util.List;

import com.welmo.andengine.scenes.operations.IOperationHandler.OperationTypes;

public class Operation {

	public 		OperationTypes 				type;					//	type of message
	public 		List<Float> 				parametersNumbers;		//  Parameter of the message
	public 		List<String> 				parametersStrings;		//  Parameter of the message
	public		List<Boolean>				parametersBoolean;		//  Parameter of the message
	
	protected 	IOperationHandler 			hdHandler = null; 		//  Contain the Object path the massage has made 
																	//  from the source to the final object
	
	//--------------------------------------------------------------------------------------
	//Constructors
	//--------------------------------------------------------------------------------------
	public Operation(Operation copy){
		this();
		this.type=copy.type;
		this.parametersNumbers = new ArrayList<Float>();
		this.parametersNumbers.clear();
		if(copy.parametersNumbers != null){
			for(int i=0; i < copy.parametersNumbers.size();i++)
				this.parametersNumbers.add(new Float(copy.parametersNumbers.get(i).floatValue()));
		}
		if(copy.parametersStrings != null){
			for(int i=0; i < copy.parametersStrings.size();i++)
				this.parametersStrings.add(new String(copy.parametersStrings.get(i)));
		}
		if(copy.parametersBoolean != null){
			for(int i=0; i < copy.parametersBoolean.size();i++)
				this.parametersBoolean.add(new Boolean(copy.parametersBoolean.get(i)));
		}
		this.hdHandler = copy.hdHandler;
	}
	public Operation(){
		this.type=OperationTypes.NULL;
		parametersNumbers = new ArrayList<Float>();
		parametersNumbers.clear();
		parametersStrings = new ArrayList<String>();
		parametersStrings.clear();
		parametersBoolean = new ArrayList<Boolean>();
		parametersBoolean.clear();

	}
	public Operation(OperationTypes type, Float parameter){
		this();
		this.type=type;
		parametersNumbers.clear();
		parametersNumbers.add(parameter);
	}
	public Operation(OperationTypes type) {
		this();
		this.type=type;
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
	public Float getParametersNumber(int id){
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
	public void setParametersNumbers(List<Float> arParameters) {
		parametersNumbers.clear();
	    for (int i = 0; i < arParameters.size(); ++i){
	    	parametersNumbers.add(arParameters.get(i));
	    }
	}
	// *************************************************************************************
	// get/set parameter as boolean
	// *************************************************************************************
	public Boolean getParameterBoolean(int id){
		if(id >= parametersBoolean.size() || id < 0)
			throw new NullPointerException("The message don't has the requested parameter");
		return parametersBoolean.get(id);
	}
	public void setParametersBoolean(Boolean... params) {
		parametersBoolean.clear();
	    for (int i = 0; i < params.length; ++i) {
	    	parametersBoolean.add(params[i]);
	    }
	}
	public void setParametersBoolean(List<Boolean> arParameters) {
		parametersBoolean.clear();
	    for (int i = 0; i < arParameters.size(); ++i){
	    	parametersBoolean.add(arParameters.get(i));
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
