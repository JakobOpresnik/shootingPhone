package com.mygdx.shooting_phone.ObjectOriented;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

public class Antivirus extends DynamicGameObject {
    public static int SPEED = 400;
    public static final long CREATE_TIME = 2140000000;
    public static long lastSpawnTime;

    public Antivirus(int x, int y, int width, int height) {
        super(x, y, width, height);
        lastSpawnTime = TimeUtils.nanoTime();
    }

    public static boolean canCreate() {
        return TimeUtils.nanoTime() - lastSpawnTime > CREATE_TIME*6;
    }

    @Override
    public void update(float deltaTime) {
        this.bounds.y -= SPEED * deltaTime;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(Assets.antivirusImage, this.bounds.x, this.bounds.y);
    }

}
