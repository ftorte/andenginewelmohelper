package com.welmo.andengine.scenes.components;

import org.andengine.entity.shape.IAreaShape;
import com.welmo.andengine.scenes.descriptors.components.BasicComponentDescriptor.Alignment;
import com.welmo.andengine.scenes.descriptors.components.BasicComponentDescriptor.IPosition;

public final class PositionHelper{
	public static void  align(IPosition iPosition, IAreaShape theEntity, IAreaShape theFather){
		//Setup horizontal Alignment
		Alignment alignment = iPosition.getHorizzontalAlignment();
		if(alignment != Alignment.NO_ALIGNEMENT){
			switch(alignment){
			case LEFTH:
				theEntity.setX(0);
				break;
			case RIGHT:
				theEntity.setX(theFather.getWidth()-theEntity.getWidth());
				break;
			case CENTER:
				theEntity.setX(theFather.getWidth()/2-theEntity.getWidth()/2);
				break;
			default:
				break;
			}
		}
		//Setup Vertical Alignment
		alignment = iPosition.getVerticalAlignment();
		if(alignment != Alignment.NO_ALIGNEMENT){
			switch(alignment){
			case TOP:
				theEntity.setY(0);
				break;
			case BOTTOM:
				theEntity.setY(theFather.getHeight()-theEntity.getHeight());
				break;
			case CENTER:
				theEntity.setY(theFather.getHeight()/2-theEntity.getHeight()/2);
				break;
			default:
				break;
			}
		}	
	}
}
