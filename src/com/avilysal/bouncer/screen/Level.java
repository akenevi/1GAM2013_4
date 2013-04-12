package com.avilysal.bouncer.screen;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.avilysal.bouncer.Levels;
import com.avilysal.bouncer.entity.*;

public class Level extends Screen{
	private List<BoostTile> boosts;
	private List<Reflector> reflectors;
	
	private LevelTile[][] layout;
	private LevelTile current = null;
	
	private List<Entity> bgRender;
	private List<Entity> fgRender;
	
	private Ball ball;
	private BoostTile boost;
	
	private boolean started = false;
	private boolean grabbed = false;
	private boolean rotated = false;
	private boolean change = false;
	
	public Level(int n){
		init(n);
	}
	
	public void update(){
		//ball speed after 1 tile == 0.9160938px
		int range = 4; //in pixels
		getInput();
		
		if(current == null)
			for(LevelTile[] row : layout)
				for(LevelTile t : row)
					if(t.getName() == "tile" && t.containsPos(ball.getX(), ball.getY()))
						current = t;
		if(started){
			if(!change && current.hasObject()){
				float bx = ball.getX();
				float by = ball.getY();
				float cx = current.getX();
				float cy = current.getY();
				if(bx > cx-range/2 && bx < cx+range/2 && by > cy-range/2 && by < cy+range/2){
					for(BoostTile b : boosts){
						if(b.getX() == cx && b.getY() == cy){
							ball.setX(cx);
							ball.setY(cy+15);
							ball.setDirection(b.getDirection());
							ball.incSpeed(10);
							continue;
						}
					}
					for(Reflector r : reflectors){
						if(r.getX() == cx && r.getY() == cy){
							ball.setX(cx);
							ball.setY(cy+15);
							ball.setDirection(r.getReflectionDirection(ball.getDirection()));
							continue;
						}
					}
					change = true;
					//current.resetObject();
				}
			}
			if(current.containsPos(ball.getX(), ball.getY()))
				ball.update();
			if(current.getNeighbor(ball.getDirection()).getName()=="tile" && current.getNeighbor(ball.getDirection()).containsPos(ball.getX(), ball.getY())){
				current = current.getNeighbor(ball.getDirection());
				change = false;
			}
			if(current.getName() == "bgTile")
				ball.stop();
		}
	
		
		//snapping reflectors to the tiles
		for(Reflector r : reflectors){
			if(r.isReleased()){
				float rx = r.getX();
				float ry = r.getY();
				for(LevelTile[] row : layout)
					for(LevelTile t : row)
						if(t.getName() == "tile"){
							float tx = t.getX();
							float ty = t.getY();
							if(rx > tx-16 && rx < tx+16 && ry > ty-16 && ry < ty+16){
								r.setX(tx);
								r.setY(ty);
								t.setObject(true);
							}
				}
			}
			if(r.getDirection() == 3)
				changeToFg(r);
			else if(fgRender.contains(r))
				changeToBg(r);
		}
	}
	
	private void getInput(){
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			if(!started){
				ball.incSpeed(10);
				started = true;
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			init(0);
		}
		if(Mouse.isButtonDown(Mouse.getButtonIndex("BUTTON0")) || Mouse.isButtonDown(Mouse.getButtonIndex("BUTTON1"))){
			float mx = Mouse.getX();
			float my = Mouse.getY();
			if(Mouse.isButtonDown(Mouse.getButtonIndex("BUTTON0"))){
				//placement
				if(mx > 8 && mx < 72 && my > 8 && my < 72){
					if(!grabbed){
						reflectors.add(new Reflector(mx,my,(byte)0));
						reflectors.get(reflectors.size()-1).setReleased(false);
						bgRender.add(reflectors.get(reflectors.size()-1));
						grabbed = true;
					}
				} else {
					if(!grabbed){
						for(Reflector r : reflectors){
							float rx = r.getX();
							float ry = r.getY();
							if(mx > rx-32 && mx < rx+32 && my > ry-32 && my < ry+32){
								grabbed = true;
								r.setReleased(false);
								for(LevelTile[] row : layout)
									for(LevelTile t : row)
										if(t.getName() == "tile" && t.getX() == rx && t.getY() == ry)
											t.setObject(false);
							}
						}
					}
				}
				for(Reflector r : reflectors){
					if(r.isReleased() == false){
						r.setX(mx);
						r.setY(my);
					}
				}
			}
			else if(Mouse.isButtonDown(Mouse.getButtonIndex("BUTTON1"))){
				//rotation
				if(!rotated){
					for(Reflector r : reflectors){
						float rx = r.getX();
						float ry = r.getY();
						if(mx > rx-16 && mx < rx+16 && my > ry-16 && my < ry+16)
							r.rotate();
					}
					rotated = true;
				}
			}
		}
		if(Mouse.isButtonDown(Mouse.getButtonIndex("BUTTON0")) == false && Mouse.isButtonDown(Mouse.getButtonIndex("BUTTON1")) == false){
			for(Reflector r : reflectors) r.setReleased(true);
			grabbed = false;
			rotated = false;
		}
	}
	
	public void init(int n){
		boosts = new ArrayList<BoostTile>();
		reflectors = new ArrayList<Reflector>();
		bgRender = new ArrayList<Entity>();
		fgRender = new ArrayList<Entity>(); 
		
		current = null;
		started = false;

		int i = 0;
		byte[] level = Levels.getLevel(n);
		layout = new LevelTile[10][10];
		
		for(int j=10; j<level.length+9; j++){
			if(j>10 && j%10 == 0) i++;
			float x = 350 - (i*32 - (j%10)*32);
			float y = 350 - (i*16 + (j%10)*16);
			if(level[j-10] == 0){
				layout[i][j%10] = new BgTile(x,y);
			}
			if(level[j-10] == 1){
				layout[i][j%10] = new Tile(x,y);
			}
			if(level[j-10] == 2){
				ball = new Ball(x,y+15);
				layout[i][j%10] = new Tile(x,y);
			}
			if(level[j-10] == 3 || level[j-10] == 4 || level[j-10] == 5 || level[j-10] == 6){
				if(level[j-10] == 3) boost = new BoostTile(x,y,(byte)0);
				if(level[j-10] == 4) boost = new BoostTile(x,y,(byte)1);
				if(level[j-10] == 5) boost = new BoostTile(x,y,(byte)2);
				if(level[j-10] == 6) boost = new BoostTile(x,y,(byte)3);
				boosts.add(boost);
				layout[i][j%10] = new Tile(x,y);
				layout[i][j%10].setObject(true);
			}
		}
		ball.setDirection(level[level.length-1]);
		for(int j=0; j<layout.length; j++)
			for(int k=0; k<layout[i].length; k++)
				bgRender.add(layout[j][k]);
		bgRender.addAll(boosts);
		setNeighbours();
	}
	
	private void setNeighbours(){
		for(int i=0; i<layout.length; i++){
			for(int j=0; j<layout[i].length; j++){
				ArrayList<LevelTile> neigh = new ArrayList<LevelTile>();
				if(i == 0){
					if(j == 0){
						neigh.add(layout[i+1][j]);
						neigh.add(layout[i][j+1]);
					} else if(j == layout[i].length-1){
						neigh.add(layout[i][j-1]);
						neigh.add(layout[i+1][j]);
					} else {
						neigh.add(layout[i][j-1]);
						neigh.add(layout[i+1][j]);
						neigh.add(layout[i][j+1]);
					}
				} else if(i == layout.length-1){
					if(j == 0){
						neigh.add(layout[i-1][j]);
						neigh.add(layout[i][j+1]);
					} else if(j == layout[i].length-1){
						neigh.add(layout[i][j-1]);
						neigh.add(layout[i-1][j]);
					} else {
						neigh.add(layout[i][j-1]);
						neigh.add(layout[i-1][j]);
						neigh.add(layout[i][j+1]);
					}
				} else {
					if(j == 0){
						neigh.add(layout[i-1][j]);
						neigh.add(layout[i+1][j]);
						neigh.add(layout[i][j+1]);
					} else if(j == layout[i].length-1){
						;neigh.add(layout[i][j-1]);
						neigh.add(layout[i-1][j]);
						neigh.add(layout[i+1][j]);
					} else {
						neigh.add(layout[i][j-1]);
						neigh.add(layout[i-1][j]);
						neigh.add(layout[i+1][j]);
						neigh.add(layout[i][j+1]);
					}
				}
				LevelTile[] neighbors = new LevelTile[neigh.size()];
				for(int k=0; k<neigh.size(); k++){
					neighbors[k] = neigh.get(k);
				}
				layout[i][j].setNeighbors(neighbors);
			}
		}
	}
	
	public void render(){
		renderUI();
		for(Entity e : bgRender) e.render();
		ball.render();
		for(Entity e : fgRender) e.render();
	}
	
	private void changeToBg(Entity ent){
		for(Entity e : fgRender){
			if(ent == e){
				bgRender.add(e);
				fgRender.remove(e);
				break;
			}
		}
	}
	
	private void changeToFg(Entity ent){
		for(Entity e : bgRender){
			if(ent == e){
				fgRender.add(e);
				bgRender.remove(e);
				break;
			}
		}
	}
	
	private void renderUI(){
		com.avilysal.bouncer.Bouncer.render.drawSprite("reflector", 40, 40, 64, 64);
	}
}
