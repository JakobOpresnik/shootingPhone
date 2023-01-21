package com.mygdx.shooting_phone;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.shooting_phone.ObjectOriented.ShootingPhoneOOP;

public class DesktopLauncherOOP {
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("shooting_phone_oop");
        config.setWindowSizeLimits(10, 10, 1920, 1080);
        new Lwjgl3Application(new ShootingPhoneOOP(), config);
    }
}
