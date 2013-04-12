package com.avilysal.bouncer.entity;

public class Ball extends Entity{
	private float speed;
	
	public Ball(float x, float y){
		this.x = x;
		this.y = y;
		released = true;
		name = "ball";
		speed = 0;
	}

	public void update(){
		System.out.println("direction:"+direction+", speed:"+speed);
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
			speed -= speed/128;
		} else {
			stop();
		}
	}
	
	public void incSpeed(float n){
		if(speed == 0)
			speed = n/4;
		else
			speed += n/4;
		if(speed > 5f)
			speed = 5f;
	}
	
	public void stop(){
		speed = 0;
	}
	
	public float[] getNextPos(){
		float[] nextPos = new float[2];
		if(direction == 0){nextPos[0] = (x + speed - speed/64); nextPos[1] = (y + speed/2 - speed/64);}
		else if(direction == 1){nextPos[0] = (x + speed - speed/64); nextPos[1] = (y - speed/2 - speed/64);}
		else if(direction == 2){nextPos[0] = (x - speed - speed/64); nextPos[1] = (y - speed/2 - speed/64);}
		else if(direction == 3){nextPos[0] = (x - speed - speed/64); nextPos[1] = (y + speed/2 - speed/64);}
		return nextPos;
	}
	
	public byte getIncomingDirection(){
		byte temp = (byte) (direction+2);
		if(temp == 4) temp = 0;
		if(temp == 5) temp = 1;
		return temp;
	}
	
	@Override
	public float getY(){
		return (y-15);
	}
	
	public void render(){
		com.avilysal.bouncer.Bouncer.render.drawSprite("ball", x, y, 64, 64);
	}
}
