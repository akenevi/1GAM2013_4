package com.avilysal.bouncer.entity;

public abstract class Entity {
	protected float x, y;
	protected byte direction;
	protected boolean released;
	protected String name ="";
	
	public abstract void render();
	public abstract void update();
	
	public void setX(float n){
		x = n;
	}
	public void setY(float n){
		y = n;
	}
	public void setReleased(boolean r){
		released = r;
	}
	public void setDirection(byte d){
		direction = d;
	}

	public String getName(){
		return name;
	}
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	public boolean isReleased(){
		return released;
	}
	public byte getDirection(){
		return direction;
	}
}