package com.avilysal.bouncer.entity;

public class Reflector extends Entity{
	private byte direction1, direction2;
	
	public Reflector(float x, float y, byte dir1, byte dir2){
		this.x = x;
		this.y = y;
		direction1 = dir1;
		direction2 = dir2;
		this.name = "reflector";
	}
	
	public void rotate(){
		direction1 += 1;
		direction2 += 1;
		if(direction1 > 3) direction1 = 0;
		if(direction2 > 3) direction2 = 0;
	}
	
	public void update(){
		
	}
	
	public void render(){
		if(direction1 == 0 && direction2 == 1){
			com.avilysal.bouncer.Bouncer.render.drawSprite("reflector01", x, y, 64, 64);
		}
		if(direction1 == 1 && direction2 == 2){
			com.avilysal.bouncer.Bouncer.render.drawSprite("reflector12", x, y, 64, 64);
		}
		if(direction1 == 2 && direction2 == 3){
			com.avilysal.bouncer.Bouncer.render.drawSprite("reflector23", x, y, 64, 64);
		}
		if(direction1 == 3 && direction2 == 0){
			com.avilysal.bouncer.Bouncer.render.drawSprite("reflector30", x, y, 64, 64);
		}
	}
}
