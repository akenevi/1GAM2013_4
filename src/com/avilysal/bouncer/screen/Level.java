package com.avilysal.bouncer.screen;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.avilysal.bouncer.Levels;
import com.avilysal.bouncer.entity.*;

public class Level extends Screen{
	private byte[] level;
	private Entity[][] layout;
	private List<Tile> tiles;
	private List<Entity> entities;
	private Ball ball;
	
	private Reflector ref;
	
	private boolean createdNew = false;
	private boolean rotated = false;
	
	public Level(int n){
		init(n);
	}
	
	public void init(int n){
		tiles = new ArrayList<Tile>();
		entities = new ArrayList<Entity>();
		layout = new Entity[10][10];
		
		level = Levels.getLevel(n);
		
		int i = 0;
		for(int j=10; j<level.length+9; j++){
			if(j>10 && j%10 == 0) i++;
			float x = 400 - (i*32 - (j%10)*32);
			float y = 500 - (i*16 + (j%10)*16);
			if(level[j-10] == 0){
				layout[i][j%10] = new BgTile(x,y);
			}
			if(level[j-10] == 1){
				tiles.add(new Tile(x,y));
				layout[i][j%10] = tiles.get(tiles.size()-1);
			}
			if(level[j-10] == 2){
				ball = new Ball(x,y+15);
				entities.add(ball);
				tiles.add(new Tile(x,y));
				layout[i][j%10] = tiles.get(tiles.size()-1);
			}
		}
		ball.setDirection(level[level.length-1]);
		ball.incSpeed(6);
	}
	
	public void update(){
		getInput();
		float ballX = ball.getX();
		float ballY = ball.getY();
		for(Tile t : tiles){
			if(t.containsPos(ballX, ballY)){
				ball.update();
				continue;
			}
		}
	}
	
	public void addEntity(Entity e){
		entities.add(e);
	}
	
	public void render(){
		for(int i=0; i<layout.length; i++){
			for(int j=0; j<layout[i].length; j++){
				layout[i][j].render();
			}
		}
		for(Entity e : entities)
			e.render();
		renderUI();
	}
	
	private void renderUI(){
		com.avilysal.bouncer.Bouncer.render.drawSprite("reflector", 32, 32, 64, 64);
	}
	
	private void getInput(){
		if(Mouse.getEventButtonState()){
			System.out.println("click");
			if(Mouse.isButtonDown(0)){
				float mouseX = Mouse.getX();
				float mouseY = Mouse.getY();
				if(!createdNew)
					if(mouseX > 0 && mouseX < 64 && mouseY > 0 && mouseY < 64){
						entities.add(new Reflector(32,32, (byte)0, (byte)1));
						createdNew = true;
					} else {
						for(Entity e : entities){
							if(e.getName() == "ball") continue;
							float ex = e.getX();
							float ey = e.getY();
							if(mouseX > ex-32 && mouseX < ex+32  && mouseY > ey-32 && mouseY < ey+32){
								e.setX(mouseX);
								e.setY(mouseY);
							}
						}
					}
				if(createdNew){
					entities.get(entities.size()-1).setX(mouseX);
					entities.get(entities.size()-1).setY(mouseY);
				}
			}
			else if(Mouse.isButtonDown(1)){
				float mouseX = Mouse.getX();
				float mouseY = Mouse.getY();
				for(Entity e : entities){
					float ex = e.getX();
					float ey = e.getY();
					if(mouseX > ex-32 && mouseX < ex+32  && mouseY > ey-32 && mouseY < ey+32){
						if(!rotated && e.getName() == "reflector"){
							ref = (Reflector) e;
							ref.rotate();
							rotated = true;
						}
					}
				}
			}
		}
		createdNew = false;
		rotated = false;
	}
}
