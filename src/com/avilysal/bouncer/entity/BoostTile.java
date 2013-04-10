package com.avilysal.bouncer.entity;

public class BoostTile extends Entity{
	private byte direction;
	
	public BoostTile(float x, float y, byte dir){
		this.x = x;
		this.y = y;
		this.direction = dir;
		this.name = "booster";
	}
	
	public void setDirection(byte d){
		direction = d;
	}
	
	public byte getDirection(){
		return direction;
	}
	
	public void update(){}
	
	public void render(){
		if(direction == 0)
			com.avilysal.bouncer.Bouncer.render.drawSprite("booster0", x, y, 64, 64);
		if(direction == 1)
			com.avilysal.bouncer.Bouncer.render.drawSprite("booster1", x, y, 64, 64);
		if(direction == 2)
			com.avilysal.bouncer.Bouncer.render.drawSprite("booster1", x, y, 64, 64, true);
		if(direction == 3)
			com.avilysal.bouncer.Bouncer.render.drawSprite("booster0", x, y, 64, 64, true);
	}
}
