package com.cgranule.dropdots.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cgranule.dropdots.DropDotsGame;

public class DesktopLauncher {

	public static void main (String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 800;
		new LwjglApplication(new DropDotsGame(new GameServicesDesktop()), config);
	}
}
