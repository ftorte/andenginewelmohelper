package com.welmo.andengine.scenes.components;

import org.andengine.entity.IEntity;
import com.welmo.andengine.scenes.descriptors.components.BasicDescriptor;

public interface IBasicComponent {
	int getID();
	void setID(int ID);
	IEntity getParent();
	void setParent(IEntity parent);
	void build(BasicDescriptor pDsc);
}
