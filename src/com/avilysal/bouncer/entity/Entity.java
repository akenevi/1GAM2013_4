package com.avilysal.bouncer.entity;

public abstract class Entity {
	protected float x, y;
	protected String name ="";
	
	public abstract void render();
	public abstract void update();
	
	public void setX(float n){
		x = n;
	}
	public void setY(float n){
		y = n;
	}
	
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	public String getName(){
		return name;
	}
}
