package com.mygdx.shooting_phone;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.shooting_phone.BouncingBall.BouncingBall;

public class LauncherBouncingBall {
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("bouncing_ball");
        config.setWindowSizeLimits(620, 480, 620, 480);
        new Lwjgl3Application(new BouncingBall(), config);
    }
}
