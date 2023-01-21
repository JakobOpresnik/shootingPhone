package com.mygdx.shooting_phone.ObjectOriented;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

public class VideoFile extends DynamicGameObject implements Pool.Poolable {
    public static int SPEED = 60;
    public static final long CREATE_TIME = 2140000000;
    public static long lastSpawnTime;

    public static final Pool<VideoFile> videofilePool = new Pool<VideoFile>(1, 5) {
        @Override
        protected  VideoFile newObject() {
            return new VideoFile(MathUtils.random(0, Gdx.graphics.getWidth() - Assets.videoFileImage.getWidth()), Gdx.graphics.getHeight(), Assets.videoFileImage.getWidth(), Assets.videoFileImage.getHeight());
        }
    };

    public VideoFile(int x, int y, int width, int height) {
        super(x, y, width, height);
        lastSpawnTime = TimeUtils.nanoTime();
    }

    public static boolean canCreate() {
        return TimeUtils.nanoTime() - lastSpawnTime > CREATE_TIME*3;
    }

    @Override
    public void update(float deltaTime) {
        this.bounds.y -= SPEED * deltaTime;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(Assets.videoFileImage, this.bounds.x, this.bounds.y);
    }

    @Override
    public void reset() {
        init();
    }

    public void init() {
        this.bounds.set(MathUtils.random(0, Gdx.graphics.getWidth() - Assets.videoFileImage.getWidth()), Gdx.graphics.getHeight(), Assets.videoFileImage.getWidth(), Assets.videoFileImage.getHeight());
        lastSpawnTime = TimeUtils.nanoTime();
    }

}
