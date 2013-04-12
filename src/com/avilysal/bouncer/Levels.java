package com.avilysal.bouncer;

public class Levels {
	
	public static byte[] getLevel(int n){
		byte[] level = new byte[100];
		
		switch(n){
			case 0:
				byte[] temp = {
						0,0,0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,0,0,
						0,0,2,0,1,1,1,0,0,0,
						0,0,1,0,1,0,1,0,0,0,
						0,0,1,0,1,0,3,0,0,0,
						0,0,1,0,1,0,1,0,0,0,
						0,0,1,0,1,0,1,0,0,0,
						0,0,1,0,3,0,1,0,0,0,
						0,0,1,1,1,0,1,1,1,0,
						0,0,0,0,0,0,0,0,0,0,
						2}; //last is the direction of the ball
				level = temp;
				break;
			default:
				level[0] = -128;
				break;
		}
		return level;
	}
}
