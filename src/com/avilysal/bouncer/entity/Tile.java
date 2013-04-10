package com.avilysal.bouncer.entity;

public class Tile extends Entity{

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
	public void update(){}
	
	public void render(){
		com.avilysal.bouncer.Bouncer.render.drawSprite("tile", x, y, 64, 64);
	}
}
