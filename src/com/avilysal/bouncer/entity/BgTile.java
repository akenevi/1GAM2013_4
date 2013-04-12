package com.avilysal.bouncer.entity;

public class BgTile extends LevelTile{
	
	public BgTile(float x, float y){
		this.x = x;
		this.y = y;
		released = true;
		direction = -1;
		name = "bgTile";
	}
	
	public void update(){}
	
	public void render(){
		
	}
}
