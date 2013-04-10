package com.avilysal.bouncer;

import static org.lwjgl.opengl.ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB;
import static org.lwjgl.opengl.GL11.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.Timer;

import com.avilysal.bouncer.PNGDecoder;

public class Renderer implements Runnable{
	private int desirableFPS = 0;
	
	private HashMap<String, Sprite> spriteMap;
	private int texID;
	private String location = "res/BouncerSMap.png"; //location of spriteMap
	private Sprite curSprite;
	
	public Timer clock;
	@Override
	public void run() {
		
	}
	
	public boolean initialize(int DW, int DH, int fps, boolean isResizable, boolean vSync){
		desirableFPS = fps;
		initGL(DW,DH,isResizable,vSync);
		initSprites();
		clock = new Timer();
		return true;
	}
	
	public void drawSprite(String name, float x, float y, int w, int h){
		drawSprite(name, x, y, w, h, false);
	}
	
	//draws given sprite centered at given coordinates.
	//make this to draw DisplayLists instead.
	public void drawSprite(String name, float x, float y, int w, int h, boolean reverseMap) {
		curSprite = spriteMap.get(name);
		int u = curSprite.x;
		int v = curSprite.y2;
		int u2 = curSprite.x2;
		int v2 = curSprite.y;
		float hh = (float)h/2;
		float hw = (float)w/2;
		if(reverseMap){
			u=curSprite.x2;
			u2=curSprite.x;
		}
		glPushMatrix();
		
		glTranslatef(x,y,0);
		glColor3f(1f,1f,1f);
		
		glBindTexture(GL_TEXTURE_RECTANGLE_ARB, texID);
		{
			glBegin(GL_QUADS);
			{
				glTexCoord2f(u, v);		glVertex2f(-hw, -hh);
				glTexCoord2f(u2, v);	glVertex2f(hw, -hh);
				glTexCoord2f(u2, v2);	glVertex2f(hw, hh);
				glTexCoord2f(u, v2);	glVertex2f(-hw, hh);
			}
			glEnd();
		}
		glBindTexture(GL_TEXTURE_RECTANGLE_ARB, 0);
		glPopMatrix();
	}
	
	//should be changed to create DisplayLists
	private void initSprites(){
		spriteMap = new HashMap<String, Sprite>();
		int texture = glGenTextures();
		texID = texture;
		glBindTexture(GL_TEXTURE_RECTANGLE_ARB, texture);
		InputStream in = null;
		try {
			in = new FileInputStream(location);
			PNGDecoder decoder = new PNGDecoder(in);
			ByteBuffer buffer = BufferUtils.createByteBuffer(4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
			buffer.flip();
			in.close();
			glTexParameteri(GL_TEXTURE_RECTANGLE_ARB, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_RECTANGLE_ARB, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexImage2D(GL_TEXTURE_RECTANGLE_ARB, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		} catch (FileNotFoundException e) {
			System.err.println("File couldn't be found at "+ location);
		} catch (IOException e) {
			System.err.println("File couldn't be loaded from "+ location);
		}
		glBindTexture(GL_TEXTURE_RECTANGLE_ARB, 0);
		
		spriteMap.put("ball", new Sprite("ball", 0*64, 0*64, 64, 64));
		spriteMap.put("tile", new Sprite("tile", 0*64, 1*64, 64, 64));
		spriteMap.put("reflector", new Sprite("reflector", 0*64, 2*64, 64, 64));
		spriteMap.put("reflector01", new Sprite("reflector01", 1*64, 2*64, 64, 64));
		spriteMap.put("reflector12", new Sprite("reflector12", 2*64, 2*64, 64, 64));
		spriteMap.put("reflector23", new Sprite("reflector23", 3*64, 2*64, 64, 64));
		spriteMap.put("reflector30", new Sprite("reflector30", 4*64, 2*64, 64, 64));
		spriteMap.put("booster0", new Sprite("booster0", 1*64, 1*64, 64, 64));
		spriteMap.put("booster1", new Sprite("booster1", 2*64, 1*64, 64, 64));
	}
	
	private void initGL(int DW, int DH, boolean isResizable, boolean vSync){
		try{
			Display.setDisplayMode(new DisplayMode(DW,DH));
			Display.create();
			Display.setResizable(isResizable);
			Display.setVSyncEnabled(vSync);
			Display.setTitle("Bouncer");
			Mouse.create();
		} catch (Exception e) {
			System.err.println("Failed to create window");
		}
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,DW,0,DH,-2,2);
		
		glEnable(GL_TEXTURE_RECTANGLE_ARB);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glClearColor(0,0,0,1);
		
		glDisable(GL_DEPTH_TEST);
		glMatrixMode(GL_MODELVIEW);
	}
	
	public void closingCheck(){
		if(Display.isCloseRequested())
			Bouncer.end();
	}
	
	public void cleanUp(){
		Display.sync(0);
		Display.destroy();
		Mouse.destroy();
	}
	
	public void preRender(){
	//clearing old stuff
		glClear(GL_COLOR_BUFFER_BIT);
		glLoadIdentity();
	}
	
	public void afterRender(){
	//swap buffers, poll new input and finally autotune (Display.sync(fps)) Thread sleep to meet stated fps
		Display.update();
		Display.sync(desirableFPS);
		Timer.tick();
	}
}

class Sprite {
    String name;
    public int x, y, x2, y2, w, h;

    public Sprite(String name, int x, int y, int w, int h) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        x2 = x + w;
        y2 = y + h;
    }
}