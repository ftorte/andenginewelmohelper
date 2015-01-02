package com.welmo.andengine.scenes.components.interfaces;

import org.andengine.entity.IEntity;
import com.welmo.andengine.scenes.descriptors.BasicDescriptor;
import com.welmo.andengine.scenes.operations.IOperationHandler;

/*
 * The I BasicComponent Interface implement the default methods any component should implement
 * 		1) methods to set/get the ID of the component
 * 		2) method to build the object by passing is descriptor that must be a BasicDescriptor class
 *  	3) method to set/get the IEntity father that contains this component
 */

public interface IComponent{
	//getters
	public int 	getID();
	public IEntity getParent();
	//setters
	public void 	setID(int ID);	
	public void 	setParent(IEntity parent);
	//method
	public void	configure(BasicDescriptor pDSC);
	public void	setActionSceneListner(IActionSceneListener scenelistener);
	public void setOperationsHandler(IOperationHandler messageHandler);
	public String getPersistenceURL();
}
