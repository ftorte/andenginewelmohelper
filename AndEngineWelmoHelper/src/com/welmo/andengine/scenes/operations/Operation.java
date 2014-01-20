package com.welmo.andengine.scenes.operations;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import com.welmo.andengine.scenes.operations.IOperationHandler.OperationTypes;

public class Operation {

	public 		OperationTypes 	type;			//	type of message
	public 		List<Integer> 	parameters;		//  Parameter of the message
	protected 	Deque<Operation> 	qObjectChain 	= new LinkedList<Operation>(); //Contain the Object path the massage has made from the source to the final object
	
	//Constructor
	public Operation(OperationTypes type, List<Integer> parameter){
		this.type=type;
		parameters = new ArrayList<Integer>();
		parameters.clear();
		if(parameter != null){
			for(int i=0; i < parameter.size();i++)
				parameters.add(new Integer(parameter.get(i)));
		}
	}
	//Constructor
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
	public OperationTypes getType(){
		return type;
	}
	public void setType(OperationTypes theType){
		type=theType;
	}
	public int getParameter(int id){
		if(id > parameters.size() || id < 1)
			throw new NullPointerException("The message don't has the requested parameter");
		return parameters.get(id-1);
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
}
