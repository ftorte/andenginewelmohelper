package com.welmo.andengine.scenes;

import java.util.ArrayList;
import java.util.List;

import com.welmo.andengine.scenes.ISceneMessageHandler.Message;
import com.welmo.andengine.scenes.ISceneMessageHandler.MessageTypes;

public interface ISceneMessageHandler {
	public class Message{
		public MessageTypes 		type;
		public List<Integer> 		parameters;
		
		public Message(MessageTypes type, List<Integer> parameter){
			this.type=type;
			parameters = new ArrayList<Integer>();
			parameters.clear();
			if(parameter != null){
			for(int i=0; i < parameter.size();i++)
				parameters.add(new Integer(parameter.get(i)));
			}
		}
	}
	public enum MessageTypes{
		SET_COLOR,ON,OFF,RESET_SCROLL_ZOOM
	}
	public void SendMessage(ISceneMessageHandler.Message msg);
}
