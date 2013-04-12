package com.avilysal.bouncer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;

import com.avilysal.bouncer.screen.*;

public class Bouncer {
	private static int DW = 600,  DH = 400, fps = 60;
	private static boolean isResizable = false, vSync = false;
	
	private static boolean running, exitGame;
	
	public static Renderer render = new Renderer();
	private static Screen screen;
	
	private static void mainLoop(){
		running = true;
		exitGame = false;
		setScreen(new Level(0));
		
		while(!exitGame){
			if(!running){
				//setScreen(paused);
			}
			screen.update();
			
			render.preRender();
			screen.render();
			render.afterRender();
			render.closingCheck();
		}
		render.cleanUp();
	}
	
	public static void end(){
		exitGame = true;
		//save stuff here
	}
	
	public static void setScreen(Screen newScreen) {
        screen = newScreen;
    }
	
	public static void main(String[] args){
		Thread rend = new Thread(render);
		rend.setDaemon(true);
		rend.setName("Rendering thread");
		rend.start();
		
		if(render.initialize(DW,DH,fps,isResizable,vSync)){
			try {
				Mouse.create();
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
			mainLoop();
		}
	}
}
