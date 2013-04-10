package com.avilysal.bouncer.screen;

import com.avilysal.bouncer.Bouncer;

public abstract class Screen {
	
	protected void setScreen(Screen screen) {
        Bouncer.setScreen(screen);
    }

	public abstract void update();
	public abstract void render();
}
