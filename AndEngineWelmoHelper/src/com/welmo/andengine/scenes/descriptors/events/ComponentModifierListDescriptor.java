package com.welmo.andengine.scenes.descriptors.events;

import java.util.LinkedList;


public class ComponentModifierListDescriptor extends BasicModifierDescriptor{
	// ========================================================
	// Private Members	
	// ========================================================
	private LinkedList<ComponentModifierDescriptor> 	llModifierSetList;
	private ExecutionOrder 								eExecOrder;
	
	// ========================================================
	// Constructor	
	// ========================================================
	public ComponentModifierListDescriptor() {
		super();
		this.isAList = true;
		llModifierSetList = new LinkedList<ComponentModifierDescriptor>();
		eExecOrder = ExecutionOrder.SERIAL;
	};

	// ========================================================
	// Methods Get & Set	
	// ========================================================
	public boolean isASet() { return isAList;}
	
	@Override
	public IModifierList getIModifierList(){
		return  new IModifierList()  {
			@Override
			public LinkedList<ComponentModifierDescriptor> getModifiers() {
				return llModifierSetList;
			}
			@Override
			public ExecutionOrder getExecOrder() {
				return eExecOrder;
			}
			@Override
			public void setExecOrder(ExecutionOrder execOrder) {
				eExecOrder = execOrder;
			} };
	}
}
