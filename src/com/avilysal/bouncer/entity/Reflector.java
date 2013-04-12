package com.avilysal.bouncer.entity;

public class Reflector extends Entity{
	
	public Reflector(float x, float y, byte dir){
		this.x = x;
		this.y = y;
		direction = dir;
		released = true;
		this.name = "reflector";
	}
	
	public void rotate(){
		direction += 1;
		if(direction == 4) direction = 0;
	}
	
	public byte getReflectionDirection(byte d){
		byte temp = -1;
		if(direction == 0){
			if(d == 3) temp = 0;
			if(d == 2) temp = 1;
		} else if(direction == 1){
			if(d == 0) temp = 1;
			if(d == 3) temp = 2;
		} else if(direction == 2){
			if(d == 1) temp = 2;
			if(d == 0) temp = 3;
		} else if(direction == 3){
			if(d == 2) temp = 3;
			if(d == 1) temp = 0;
		}
		return temp;
	}
	
	public void update(){}
	
	public void render(){
		if(direction == 0){
			com.avilysal.bouncer.Bouncer.render.drawSprite("reflector01", x, y, 64, 64);
		}
		if(direction == 1){
			com.avilysal.bouncer.Bouncer.render.drawSprite("reflector12", x, y, 64, 64);
		}
		if(direction == 2){
			com.avilysal.bouncer.Bouncer.render.drawSprite("reflector23", x, y, 64, 64);
		}
		if(direction == 3){
			com.avilysal.bouncer.Bouncer.render.drawSprite("reflector30", x, y, 64, 64);
		}
	}
}
