package com.mygdx.shooting_phone;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.shooting_phone.ObjectOriented.ShootingPhoneOOP;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("shooting_phone");
		config.setWindowSizeLimits(620, 480, 620, 480);
		new Lwjgl3Application(new ShootingPhone(), config);
	}
}