package com.welmo.andengine.scenes.components;

import org.andengine.entity.shape.IAreaShape;


public final class Stick{
	
	public enum StickMode {
	    NO_STICK,STICK_OVER, STICK_LEFTH, STICK_RIGHT, STICK_UP, STICK_BOTTOM, 
	    STICK_UP_LEFTH, STICK_UP_RIGHT, STICK_BOTTOM_LEFTH, STICK_BOTTOM_RIGHT 
	}
	
	public static boolean isStickOn(IAreaShape entityToStick, IAreaShape entityToStickWith, float threshold){
		double deltaXSqr = Math.pow(entityToStick.getX() - entityToStickWith.getX(), 2);
		double deltaYSqr = Math.pow(entityToStick.getY() - entityToStickWith.getY(), 2);
		double distance = Math.sqrt(deltaXSqr + deltaYSqr);
		if (threshold >= distance) 
			return true;
		else
			return false;	
	}
	
	public static void lefth(IAreaShape entityToStick, IAreaShape entityToStickWith){
		entityToStick.setPosition(entityToStickWith.getX() + entityToStickWith.getWidth(),entityToStickWith.getY());	
	}
	public static void right(IAreaShape entityToStick, IAreaShape entityToStickWith){
		entityToStick.setPosition(entityToStickWith.getX() - entityToStick.getWidth(),entityToStickWith.getY());
	}
	public static void up(IAreaShape entityToStick, IAreaShape entityToStickWith){
		entityToStick.setPosition(entityToStickWith.getX(),entityToStickWith.getY() - entityToStick.getHeight());
	}
	public static void bottom(IAreaShape entityToStick, IAreaShape entityToStickWith){
		entityToStick.setPosition(entityToStickWith.getX(),entityToStickWith.getY() + entityToStickWith.getHeight());
	}
	public static void upLefth(IAreaShape entityToStick, IAreaShape entityToStickWith){
		entityToStick.setPosition(entityToStickWith.getX() + entityToStickWith.getWidth(),entityToStickWith.getY() - entityToStick.getHeight());
	}
	public static void upRight(IAreaShape entityToStick, IAreaShape entityToStickWith){
		entityToStick.setPosition(entityToStickWith.getX() - entityToStick.getWidth(),entityToStickWith.getY() - entityToStick.getHeight());
	}
	public static void bottomLefth(IAreaShape entityToStick, IAreaShape entityToStickWith){
		entityToStick.setPosition(entityToStickWith.getX() + entityToStickWith.getWidth(),entityToStickWith.getY() + entityToStickWith.getHeight());
	}
	public static void bottomRight(IAreaShape entityToStick, IAreaShape entityToStickWith){
		entityToStick.setPosition(entityToStickWith.getX() - entityToStick.getWidth(),entityToStickWith.getY() + entityToStickWith.getHeight());
	}
	public static void over(IAreaShape entityToStick, IAreaShape entityToStickWith){
		entityToStick.setPosition(entityToStickWith.getX(),entityToStickWith.getY());
	}
}
