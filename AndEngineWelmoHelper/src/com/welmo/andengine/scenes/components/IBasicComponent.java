package com.welmo.andengine.scenes.components;

import org.andengine.entity.IEntity;

public interface IBasicComponent {
	int getID();
	void setID(int ID);
	IEntity getParent();
	void setParent(IEntity parent);
}