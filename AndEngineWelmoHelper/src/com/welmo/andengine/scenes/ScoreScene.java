package com.welmo.andengine.scenes;

import java.util.ArrayList;

import org.andengine.entity.IEntity;
import org.andengine.entity.text.Text;

import com.welmo.andengine.scenes.components.TextComponent;
import com.welmo.andengine.scenes.descriptors.ConfiguredSceneDescriptor;

public class ScoreScene extends ManageableCameraScene implements IConfigurableScene {

	@Override
	public void configure(ConfiguredSceneDescriptor descriptor) {
		return;
	}

	@Override
	public String getNameOfInstantiatedScene() {
		//Not applicable for this modal scene
		return null;
	}

	@Override
	public void setParameter(ArrayList<String> parameterList) {
		
		int 	result 		= Integer.parseInt(parameterList.get(0));
		CharSequence message = parameterList.get(2);
		CharSequence score = parameterList.get(1);
		
		//((TextComponent)((IEntity)this.mapOfObjects.get(106)).getFirstChild()).setText(parameterList.get(2));
		IEntity ent1 = (IEntity)this.mapOfObjects.get(107);
		IEntity ent2 = ent1.getFirstChild();
		((TextComponent)ent2).setText(message);
		
		((TextComponent)((IEntity)this.mapOfObjects.get(109)).getFirstChild()).setText(score);
		
		//Disable all stars
		this.mapOfObjects.get(112).setVisible(false);
		this.mapOfObjects.get(113).setVisible(false);
		this.mapOfObjects.get(114).setVisible(false);
		
		switch(result){
		case 3:
			this.mapOfObjects.get(114).setVisible(true);
			this.mapOfObjects.get(113).setVisible(true);
			this.mapOfObjects.get(112).setVisible(true);
			break;
		case 2:
			this.mapOfObjects.get(113).setVisible(true);
			this.mapOfObjects.get(112).setVisible(true);
			break;
		case 1:
			this.mapOfObjects.get(112).setVisible(true);
			break;
		default:
			break;
		}
	
	}
}
