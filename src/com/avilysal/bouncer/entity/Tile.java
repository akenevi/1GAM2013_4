package com.avilysal.bouncer.entity;

public class Tile extends LevelTile{
	
	public Tile(float x, float y){
		startClock();
		this.x = x;
		this.y = y;
		released = true;
		direction = -1;
		name = "tile";
	}
	
	public void update(){
		updateObject();
	}
	
	public void render(){
		com.avilysal.bouncer.Bouncer.render.drawSprite("tile", x, y, 64, 64);
	}
}
