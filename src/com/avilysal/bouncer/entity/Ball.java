package com.avilysal.bouncer.entity;

public class Ball extends Entity{
	private byte direction = -1;
	private float speed = 0;
	
	public Ball(float x, float y){
		this.x = x;
		this.y = y;
		this.name = "ball";
	}
	
	public void update(){
		move();
	}
	
	public void move(){
		if(direction != -1 && speed >= 0.01){
			if(direction == 0){
				x += speed;
				y += speed/2;
			} else if (direction == 1){
				x += speed;
				y -= speed/2;
			} else if (direction == 2){
				x -= speed;
				y -= speed/2;
			} else if (direction == 3){
				x -= speed;
				y += speed/2;
			}
			speed -= speed/64;
		} else
			speed = 0;
	}
	
	public void incSpeed(float n){
		if(speed == 0)
			speed = n/2;
		else
			speed += n/2;
	}
	
	public float[] getNextPos(){
		float[] nextPos = new float[2];
		if(direction == 0){nextPos[0] = (x + speed - speed/64); nextPos[1] = (y + speed/2 - speed/64);}
		else if(direction == 1){nextPos[0] = (x + speed - speed/64); nextPos[1] = (y - speed/2 - speed/64);}
		else if(direction == 2){nextPos[0] = (x - speed - speed/64); nextPos[1] = (y - speed/2 - speed/64);}
		else if(direction == 3){nextPos[0] = (x - speed - speed/64); nextPos[1] = (y + speed/2 - speed/64);}
		return nextPos;
	}
	
	public void setDirection(byte n){
		direction = n;
	}
	
	public void render(){
		com.avilysal.bouncer.Bouncer.render.drawSprite("ball", x, y, 64, 64);
	}
}
