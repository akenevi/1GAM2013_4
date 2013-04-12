package com.avilysal.bouncer.entity;

import org.lwjgl.util.Timer;

public abstract class LevelTile extends Entity{
	private boolean hasObjectOn = false;
	private boolean hadObject = false;
	private LevelTile[] neighbors;
	protected Timer clock;
	
	protected void startClock(){
		clock = new Timer();
	}
	
	public void setNeighbors(LevelTile[] neighb){
		neighbors = neighb;
	}
	
	public LevelTile[] getNeighbors(){
		return neighbors;
	}
	
	public LevelTile getNeighbor(byte dir){
		float xOffs = 0;
		float yOffs = 0;
		if(dir == 0){ xOffs = 32; yOffs = 16;}
		if(dir == 1){ xOffs = 32; yOffs = -16;}
		if(dir == 2){ xOffs = -32; yOffs = -16;}
		if(dir == 3){ xOffs = -32; yOffs = 16;}
		for(LevelTile n : neighbors)
			if(n.getX() == x+xOffs && n.getY() == y+yOffs)
				return n;
		return null;
	}
	
	public boolean containsPos(float ox, float oy){
		if(ox < x+17 && ox > x-17)
			if(oy < y+9 && oy > y-9)
				return true;
		return false;
	}

	public void setObject(boolean b){
		hasObjectOn = b;
		hadObject = b;
	}
	
	public void resetObject(){
		clock.reset();
		hadObject = hasObjectOn;
		hasObjectOn = false;
	}
	
	public boolean hasObject(){
		return hasObjectOn;
	}
	
	public void updateObject(){
		if(clock.getTime() > 2.0f)
			hasObjectOn = hadObject;
	}
}
