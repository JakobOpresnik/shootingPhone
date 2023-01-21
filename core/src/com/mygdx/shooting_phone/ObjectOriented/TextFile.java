package com.mygdx.shooting_phone.ObjectOriented;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

public class TextFile extends DynamicGameObject implements Pool.Poolable {
    public static int SPEED = 100;
    public static final long CREATE_TIME = 2100000000;
    public static long lastSpawnTime;

    public static final Pool<TextFile> filePool = new Pool<TextFile>(1, 5) {
        @Override
        protected TextFile newObject() {
            return new TextFile(MathUtils.random(0, Gdx.graphics.getWidth() - Assets.fileImage.getWidth()), Gdx.graphics.getHeight(), Assets.fileImage.getWidth(), Assets.fileImage.getHeight());
        }
    };

    public TextFile(int x, int y, int width, int height) {
        super(x, y, width, height);
        lastSpawnTime = TimeUtils.nanoTime();
    }

    public static boolean canCreate() {
        return TimeUtils.nanoTime() - lastSpawnTime > CREATE_TIME;
    }

    @Override
    public void update(float deltaTime) {
        this.bounds.y -= SPEED * deltaTime;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(Assets.fileImage, this.bounds.x, this.bounds.y);
    }

    @Override
    public void reset() { init(); }

    public void init() {
        this.bounds.set(MathUtils.random(0, Gdx.graphics.getWidth() - Assets.fileImage.getWidth()), Gdx.graphics.getHeight(), Assets.fileImage.getWidth(), Assets.fileImage.getHeight());
        lastSpawnTime = TimeUtils.nanoTime();
    }
}
