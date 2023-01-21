package com.mygdx.shooting_phone.ObjectOriented;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

public class ImageFile extends DynamicGameObject implements Pool.Poolable {
    public static int SPEED = 75;
    public static final long CREATE_TIME = 2120000000;
    public static long lastSpawnTime;

    public static final Pool<ImageFile> imgfilePool = new Pool<ImageFile>(1, 5) {
        @Override
        protected ImageFile newObject() {
            return new ImageFile(MathUtils.random(0, Gdx.graphics.getWidth() - Assets.imageFileImage.getWidth()), Gdx.graphics.getHeight(), Assets.imageFileImage.getWidth(), Assets.imageFileImage.getHeight());
        }
    };

    public ImageFile(int x, int y, int width, int height) {
        super(x, y, width, height);
        lastSpawnTime = TimeUtils.nanoTime();
    }

    public static boolean canCreate() {
        return TimeUtils.nanoTime() - lastSpawnTime > CREATE_TIME*2;
    }

    @Override
    public void update(float deltaTime) {
        this.bounds.y -= SPEED * deltaTime;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(Assets.imageFileImage, this.bounds.x, this.bounds.y);
    }

    @Override
    public void reset() {
        init();
    }

    public void init() {
        this.bounds.set(MathUtils.random(0, Gdx.graphics.getWidth() - Assets.imageFileImage.getWidth()), Gdx.graphics.getHeight(), Assets.imageFileImage.getWidth(), Assets.imageFileImage.getHeight());
        lastSpawnTime = TimeUtils.nanoTime();
    }
}
