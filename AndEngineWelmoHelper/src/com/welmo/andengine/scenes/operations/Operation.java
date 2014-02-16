package com.welmo.andengine.scenes.operations;

import java.util.ArrayList;
import java.util.List;

import com.welmo.andengine.scenes.operations.IOperationHandler.OperationTypes;

public class Operation {

	public 		OperationTypes 				type;			//	type of message
	public 		List<Integer> 				parameters;		//  Parameter of the message
	protected 	IOperationHandler 			hdHandler = null; //  Contain the Object path the massage has made 
															//  from the source to the final object
	
	//--------------------------------------------------------------------------------------
	//Constructors
	//--------------------------------------------------------------------------------------
	public Operation(OperationTypes type, List<Integer> parameter){
		this.type=type;
		parameters = new ArrayList<Integer>();
		parameters.clear();
		if(parameter != null){
			for(int i=0; i < parameter.size();i++)
				parameters.add(new Integer(parameter.get(i)));
		}
	}
	public Operation(Operation copy){
		this.type=copy.type;
		this.parameters = new ArrayList<Integer>();
		this.parameters.clear();
		if(copy.parameters != null){
			for(int i=0; i < copy.parameters.size();i++)
				this.parameters.add(new Integer(copy.parameters.get(i)));
		}
		this.hdHandler = copy.hdHandler;
	}
	public Operation(){
		this.type=OperationTypes.NULL;
		parameters = new ArrayList<Integer>();
		parameters.clear();

	}
	public Operation(OperationTypes type, Integer parameter){
		this.type=type;
		parameters = new ArrayList<Integer>();
		parameters.clear();
		parameters.add(parameter);
	}
	public Operation(OperationTypes type) {
		this.type=type;
		parameters = new ArrayList<Integer>();
		parameters.clear();
	}
	//--------------------------------------------------------------------------------------
	public OperationTypes getType(){
		return type;
	}
	public void setType(OperationTypes theType){
		type=theType;
	}
	public int getParameter(int id){
		if(id >= parameters.size() || id < 0)
			throw new NullPointerException("The message don't has the requested parameter");
		return parameters.get(id);
	}
	public void setParameter(int... params) {
		parameters.clear();
	    for (int i = 0; i < params.length; ++i) {
	    	parameters.add(params[i]);
	    }
	}
	public void setParameter(List<Integer> arParameters) {
		parameters.clear();
	    for (int i = 0; i < arParameters.size(); ++i){
	    	parameters.add(arParameters.get(i));
	    }
	}
	public void pushHander(IOperationHandler hdOper){
		hdHandler = hdOper;
	}
	public IOperationHandler getHander(){
		return hdHandler;
	}
}
