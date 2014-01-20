package com.welmo.andengine.scenes.messages;


public interface ISceneMessageHandler {
	public enum MessageTypes{
		SET_COLOR,ON,OFF,RESET_SCROLL_ZOOM
	}
	public void SendMessage(Message msg);
}
