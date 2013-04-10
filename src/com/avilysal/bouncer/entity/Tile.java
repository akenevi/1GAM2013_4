package com.avilysal.bouncer.entity;

import org.lwjgl.util.Timer;

public class Tile extends Entity{
	private boolean hasObjectOn = false;
	private boolean hadObject = false;
	private Timer clock = new Timer();

	public Tile(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public boolean containsPos(float ox, float oy){
		if(ox < x+31 && ox > x-31)
			if(oy < y+16 && oy > y-16)
				return true;
		return false;
	}
	
	public void resetObject(){
		clock.reset();
		hadObject = hasObjectOn;
		hasObjectOn = false;
	}
	
	public void setObject(boolean b){
		hasObjectOn = b;
		hadObject = b;
	}
	
	public boolean hasObject(){
		return hasObjectOn;
	}
	
	public void update(){
		if(clock.getTime() > 0.4)
			hasObjectOn = hadObject;
	}
	
	public void render(){
		com.avilysal.bouncer.Bouncer.render.drawSprite("tile", x, y, 64, 64);
	}
}
