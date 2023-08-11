package com.mygdx.rocket;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.rocket.RocketFlight;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(RocketFlight.V_WIDTH, RocketFlight.V_HEIGHT);
		config.useVsync(true);
		config.setForegroundFPS(60);
		config.setTitle(RocketFlight.TITLE);
		new Lwjgl3Application(new RocketFlight(), config);
	}
}
