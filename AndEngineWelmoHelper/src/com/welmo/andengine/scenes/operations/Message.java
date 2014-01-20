package com.welmo.andengine.scenes.messages;

import java.util.ArrayList;
import java.util.List;

import com.welmo.andengine.scenes.messages.ISceneMessageHandler.MessageTypes;

public class Message {

	public MessageTypes 		type;
	public List<Integer> 		parameters;

	//Constructor
	public Message(MessageTypes type, List<Integer> parameter){
		this.type=type;
		parameters = new ArrayList<Integer>();
		parameters.clear();
		if(parameter != null){
			for(int i=0; i < parameter.size();i++)
				parameters.add(new Integer(parameter.get(i)));
		}
	}
	//Constructor
	public Message(MessageTypes type, Integer parameter){
		this.type=type;
		parameters = new ArrayList<Integer>();
		parameters.clear();
		parameters.add(parameter);
	}
	public MessageTypes getType(){
		return type;
	}
	public int getParameter(int id){
		if(id > parameters.size() || id < 1)
			throw new NullPointerException("The message don't has the requested parameter");
		return parameters.get(id-1);
	}
}
