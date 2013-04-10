package com.avilysal.bouncer.screen;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Timer;

import com.avilysal.bouncer.Levels;
import com.avilysal.bouncer.entity.*;

public class Level extends Screen{
	private byte[] level;
	private Entity[][] layout;
	private List<Tile> tiles;
	private List<Entity> entities;
	private Ball ball;
	
	private Reflector ref;
	private BoostTile boost;
	
	private boolean createdNew = false;
	private boolean rotated = false;
	private boolean increased = false;
	
	public Level(int n){
		init(n);
	}
	
	public void init(int n){
		tiles = new ArrayList<Tile>();
		entities = new ArrayList<Entity>();
		layout = new Entity[10][10];
		increased = false;
		
		level = Levels.getLevel(n);
		
		int i = 0;
		for(int j=10; j<level.length+9; j++){
			if(j>10 && j%10 == 0) i++;
			float x = 400 - (i*32 - (j%10)*32);
			float y = 500 - (i*16 + (j%10)*16);
			if(level[j-10] == 0){
				layout[i][j%10] = new BgTile(x,y);
			}
			if(level[j-10] == 2){
				tiles.add(new Tile(x,y));
				layout[i][j%10] = tiles.get(tiles.size()-1);
			}
			if(level[j-10] == 1){
				ball = new Ball(x,y+15);
				tiles.add(new Tile(x,y));
				layout[i][j%10] = tiles.get(tiles.size()-1);
			}
			if(level[j-10] == 3 || level[j-10] == 4 || level[j-10] == 5 || level[j-10] == 6){
				if(level[j-10] == 3) boost = new BoostTile(x,y,(byte)0);
				if(level[j-10] == 4) boost = new BoostTile(x,y,(byte)1);
				if(level[j-10] == 5) boost = new BoostTile(x,y,(byte)2);
				if(level[j-10] == 6) boost = new BoostTile(x,y,(byte)3);
				entities.add(boost);
				tiles.add(new Tile(x,y));
				tiles.get(tiles.size()-1).setObject(true);
				layout[i][j%10] = tiles.get(tiles.size()-1);
			}
		}
		ball.setDirection(level[level.length-1]);
	}
	
	public void update(){
		getInput();
		float ballX = ball.getX();
		float ballY = ball.getY()-15;
		for(Tile t : tiles){
			if(t.containsPos(ballX, ballY)){
				if(t.hasObject() && ballX > t.getX()-3 && ballX < t.getX()+3 && ballY > t.getY()-3 && ballY < t.getY()+3){
					for(Entity e : entities){
						if(e.getName() == "reflector" && e.getX() == t.getX() && e.getY() == t.getY()){
							ref = (Reflector) e;
							if(ball.getIncomingDirection() == ref.getDirections()[0]){
								ball.setDirection(ref.getDirections()[1]);
								t.resetObject();
							}else if(ball.getIncomingDirection() == ref.getDirections()[1]){
								ball.setDirection(ref.getDirections()[0]);
								t.resetObject();
							}else{
								ball.stop();
								t.resetObject();
							}
						}
						if(e.getName() == "booster" && e.getX() == t.getX() && e.getY() == t.getY()){
							boost = (BoostTile) e;
							ball.setDirection(boost.getDirection());
							ball.stop();
							ball.incSpeed(10);
							t.resetObject();
						}
					}
				}
			}
			t.update();
		}
		ball.update();
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
		renderUI();
		for(Entity e : entities)
			e.render();
		ball.render();
	}
	
	private void renderUI(){
		com.avilysal.bouncer.Bouncer.render.drawSprite("reflector", 32, 32, 64, 64);
	}
	
	private void getInput(){
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			if(!increased){
				ball.incSpeed(10);
				increased = true;
			}
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			init(0);
		if(Mouse.isButtonDown(Mouse.getButtonIndex("BUTTON0")) || Mouse.isButtonDown(Mouse.getButtonIndex("BUTTON1"))){
			if(Mouse.isButtonDown(Mouse.getButtonIndex("BUTTON0"))){
				float mouseX = Mouse.getX();
				float mouseY = Mouse.getY();
				if(!createdNew)
					if(mouseX > 0 && mouseX < 64 && mouseY > 0 && mouseY < 64){
						entities.add(new Reflector(32,32, (byte)0, (byte)1));
						createdNew = true;
					} else {
						for(Entity e : entities){
							if(e.getName() == "ball" || e.getName() == "booster") continue;
							float ex = e.getX();
							float ey = e.getY();
							if(mouseX > ex-16 && mouseX < ex+16  && mouseY > ey-16 && mouseY < ey+16){
								e.setX(mouseX);
								e.setY(mouseY);
								for(Tile t : tiles){
									if(mouseX > t.getX()-16 && mouseX < t.getX()+16 && mouseY > t.getY()-16 && mouseY < t.getY()+16){
										e.setX(t.getX());
										e.setY(t.getY());
										t.setObject(true);
									}
								}
							}
						}
					}
				if(createdNew){
					entities.get(entities.size()-1).setX(mouseX);
					entities.get(entities.size()-1).setY(mouseY);
				}
			}
			else if(Mouse.isButtonDown(Mouse.getButtonIndex("BUTTON1"))){
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
		if(Mouse.isButtonDown(Mouse.getButtonIndex("BUTTON0")) == false && Mouse.isButtonDown(Mouse.getButtonIndex("BUTTON1")) == false){
			createdNew = false;
			rotated = false;
		}
	}
}
